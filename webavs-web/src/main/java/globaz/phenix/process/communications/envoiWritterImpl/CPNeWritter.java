/*
 * Créé le 20 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.process.communications.envoiWritterImpl;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichage;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import globaz.phenix.db.communications.CPReceptionReader;
import globaz.phenix.db.communications.CPReceptionReaderManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.translation.CodeSystem;
import globaz.phenix.util.CPUtil;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.alternate.TIPersonneAvsAdresseListViewBean;
import globaz.pyxis.db.alternate.TIPersonneAvsAdresseViewBean;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.pyxis.util.TIAdresseResolver;
import globaz.webavs.common.CommonProperties;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPNeWritter {

    private final static String BASE_FILENAME = "CICICAM-FISC.txt";
    private TIPersonneAvsAdresseViewBean adresse = null;
    private String date_impression = "";
    public String filename = null;
    private BProcess processAppelant = null;
    private BSession session;
    private TITiersViewBean tiers = null;
    private String user = "";

    public CPNeWritter(BSession session) {
        String nom = "";
        this.session = session;
        setUser(getSession().getUserFullName());
        try {
            CPReceptionReaderManager manager = new CPReceptionReaderManager();
            manager.setSession(session);
            manager.setForIdCanton(IConstantes.CS_LOCALITE_CANTON_NEUCHATEL);
            manager.find();
            if (manager.size() > 0) {
                CPReceptionReader reader = (CPReceptionReader) manager.getFirstEntity();
                if (reader.getNomFichier().length() > 0) {
                    nom = reader.getNomFichier();
                } else {
                    nom = (getSession().getApplication()).getProperty("nomPourEnvoiFisc");
                }
            } else {
                nom = (getSession().getApplication()).getProperty("nomPourEnvoiFisc");
            }
            nom += "-" + JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD) + ".txt";
            filename = nom;
        } catch (Exception e) {
            filename = CPNeWritter.BASE_FILENAME;
        }
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures...
     */
    public String create(CPCommunicationFiscaleAffichageManager manager, BTransaction transaction) {
        CPCommunicationFiscaleAffichage entity;
        BStatement statement = null;
        PrintWriter fileOut = null;
        String localite = "";
        String npa = "";
        String numNss = "";
        try {
            statement = manager.cursorOpen(transaction);
            fileOut = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFilename(), false), "ISO8859_1"));
            while (((entity = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                    && (!entity.isNew())) {
                numNss = "";
                // Recherche des données du tiers
                setRenseignementTiers(entity.getIdTiers(), "getNumAffilieActuel");
                if (adresse != null) {
                    localite = adresse.getLocalite();
                    npa = adresse.getNpa();
                }
                String numero = NSUtil.unFormatAVS(entity.getNumAvs());
                if (numero.length() == 13) {
                    numNss = numero;
                    // Recherche de l'ancien n° avs
                    String varNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numero, getSession()));
                    if (!JadeStringUtil.isEmpty(varNumAvs)) {
                        numero = varNumAvs;
                    }
                }
                fileOut.write(getFormattedInt(numero, 11, " ") + getFormattedString(entity.getNom(), 40));
                fileOut.write(getFormattedString(entity.getPrenom(), 20) + getFormattedInt(npa, 4, " "));
                fileOut.write(getFormattedString(localite, 25) + getFormattedInt("0", 5, "0"));
                fileOut.write(getFormattedString(CodeSystem.getCode(session, entity.getCanton()), 2)
                        + getFormattedInt(JadeStringUtil.removeChar(entity.getNumAffilie(), '.'), 15, "0"));
                fileOut.write(getFormattedString(getPrenomConjoint(), 20) + getFormattedInt(getNumCaisse(), 6, " "));
                fileOut.write(getFormattedString(getCodeContri(entity), 1)
                        + getFormattedInt(entity.getNumContri(entity.getAnneeDecision()), 11, "0"));
                fileOut.write(getFormattedString("", 1) + getFormattedString("", 17) + getFormattedInt("0", 2, "0"));
                // NSS
                fileOut.write(getFormattedInt(numNss, 13, "0"));
                // Code Pays
                fileOut.write(getFormattedInt(entity.getIdPays(), 3, "0"));
                // Date de naissance
                fileOut.write(getFormattedInt(JACalendar.format(entity.getDateNaissance(), JACalendar.FORMAT_YYYYMMDD),
                        8, "0"));
                // Code sexe
                if (entity.getSexe().equalsIgnoreCase(TITiersViewBean.CS_HOMME)) {
                    fileOut.write(getFormattedString("M", 1) + "\n");
                } else {
                    fileOut.write(getFormattedString("W", 1) + "\n");
                }
                // Mise à jour de la date d'envoi
                CPCommunicationFiscale comFis = new CPCommunicationFiscale();
                comFis.setSession(getSession());
                comFis.setIdCommunication(entity.getIdCommunication());
                comFis.retrieve(transaction);
                if (!comFis.isNew()) {
                    if (manager.getDemandeAnnulee().equals(Boolean.FALSE)) {
                        comFis.setDateEnvoi(JACalendar.todayJJsMMsAAAA());
                    } else {
                        comFis.setDateEnvoiAnnulation(JACalendar.todayJJsMMsAAAA());
                    }
                    comFis.update(transaction);
                }
                if (transaction.hasErrors()) {
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            }
            fileOut.close();
        } catch (IOException e) {
            JadeLogger.error(this, e);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        } finally {
            try {
                if (statement != null) {
                    manager.cursorClose(statement);
                }
            } catch (Exception e1) {
                JadeLogger.error(this, e1);
            }

        }
        return getFilename();
    }

    public String getCodeContri(CPCommunicationFiscaleAffichage entity) {
        String code = "";
        try {

            if (entity.getGenreAffilie().equals(CPDecision.CS_NON_ACTIF)) {
                code = "3";
            } else {
                code = "2";
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return code;
    }

    public String getDate_impression() {
        return date_impression;
    }

    public String getFilename() {
        return filename;
    }

    public String getFormattedInt(String champ, int grandeurMax, String caractereRemplissage) {
        String format = "";
        int taille = 0;
        taille = champ.length();
        format = champ;
        if (champ.equals("") || (champ == null)) {
            format = "0";
            taille = 1;
        }
        for (int i = taille; i < grandeurMax; i++) {
            format = caractereRemplissage + format;
        }
        return format;
    }

    public String getFormattedString(String champ, int grandeurMax) {
        String format = "";
        int taille = 0;
        taille = champ.length();
        format = champ;
        for (int i = taille; i < grandeurMax; i++) {
            format = format + " ";
        }
        return format;
    }

    public String getNumCaisse() {
        String numCaisse = "";
        try {
            numCaisse = (getSession().getApplication()).getProperty(CommonProperties.KEY_NO_CAISSE);
            numCaisse += (getSession().getApplication()).getProperty(CommonProperties.KEY_NO_AGENCE);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return numCaisse;
    }

    private String getPrenomConjoint() {
        TITiersViewBean conjoint = null;
        if (tiers != null) {
            conjoint = tiers.getConjoint();
        }
        if ((conjoint == null) || conjoint.isNew()) {
            return "";
        } else {
            return conjoint.getDesignation2();
        }
    }

    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    public BSession getSession() {
        return session;
    }

    public String getUser() {
        return user;
    }

    public void setDate_impression(String string) {
        date_impression = string;
    }

    public void setProcessAppelant(BProcess processAppelant) {
        this.processAppelant = processAppelant;
    }

    private void setRenseignementTiers(String idTiers, String numAffilie) {
        // Données du tiers
        try {
            tiers = new TITiersViewBean();
            tiers.setSession(getSession());
            tiers.setIdTiers(idTiers);
            tiers.retrieve();
        } catch (Exception e) {
            tiers = null;
            JadeLogger.error(this, e);
        }
        // Adresse du tiers
        try {
            TIPersonneAvsAdresseListViewBean mgr = new TIPersonneAvsAdresseListViewBean();
            mgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
            mgr.setSession(getSession());
            mgr.setForIdTiers(idTiers);
            mgr.find();
            Collection<?> col = TIAdresseResolver.resolve(mgr, IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                    numAffilie);
            Iterator<?> iterateur = col.iterator();
            if (iterateur.hasNext()) {
                adresse = (TIPersonneAvsAdresseViewBean) iterateur.next();
            }
        } catch (Exception e) {
            adresse = null;
            JadeLogger.error(this, e);
        }
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setUser(String string) {
        user = string;
    }
}
