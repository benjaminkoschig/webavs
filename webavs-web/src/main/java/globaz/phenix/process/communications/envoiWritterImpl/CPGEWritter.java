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
import globaz.globall.util.JAStringFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichage;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import globaz.phenix.db.communications.CPReceptionReader;
import globaz.phenix.db.communications.CPReceptionReaderManager;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.CPUtil;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Hashtable;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPGEWritter {
    private final static String BASE_FILENAME = "GE-FISC.txt";

    public String filename = null;
    public String numAgence = "";
    public String numCaisse = "";
    private BProcess processAppelant = null;
    private BSession session;

    public CPGEWritter(BSession session) {
        this.session = session;
        try {
            CPReceptionReaderManager manager = new CPReceptionReaderManager();
            manager.setSession(session);
            manager.setForIdCanton(IConstantes.CS_LOCALITE_CANTON_GENEVE);
            manager.find();
            if (manager.size() > 0) {
                CPReceptionReader reader = (CPReceptionReader) manager.getFirstEntity();
                if (reader.getNomFichier().length() > 0) {
                    filename = reader.getNomFichier();
                } else {
                    filename = (getSession().getApplication()).getProperty("nomPourEnvoiFisc");
                }
            } else {
                filename = (getSession().getApplication()).getProperty("nomPourEnvoiFisc");
            }
            numCaisse = getSession().getApplication().getProperty("noCaisseFormate");
        } catch (Exception e) {
            filename = CPGEWritter.BASE_FILENAME;
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

        try {
            // String type="";
            // if(CPDecision.CS_NON_ACTIF.equalsIgnoreCase(manager.getForGenreAffilie())){
            // type="P";
            // } else
            // if(!JadeStringUtil.isEmpty(manager.getExceptGenreAffilie())){
            // type="I";
            // }
            if (Boolean.TRUE.equals(manager.getDemandeAnnulee())) {
                filename = "abandon_" + JadeStringUtil.removeChar(numCaisse, '.') + "_" + manager.getForAnneeDecision()
                        + "_" + JACalendar.today().toStrAMJ();
            } else {
                filename = filename + "_" + JadeStringUtil.removeChar(numCaisse, '.') + "_"
                        + JACalendar.today().toStrAMJ();
                // else if(JadeStringUtil.isEmpty(type)){
                // filename= manager.getForAnneeDecision()+"_"+filename+"_"+"_"+
                // JACalendar.today().toStrAMJ();
                // } else {
                // filename=
                // manager.getForAnneeDecision()+"_"+type+"_"+filename+"_"+"_"+
                // JACalendar.today().toStrAMJ();
            }
            filename = getSession().getApplication().getLocalPath() + filename + ".txt";
            statement = manager.cursorOpen(transaction);

            fileOut = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFilename(), false), "ISO8859_1"));
            while (((entity = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                    && (!entity.isNew())) {
                // if(!JadeStringUtil.isEmpty(entity.getNumContri(entity.getAnneeDecision()))){
                entity.getNumContri(entity.getAnneeDecision());
                // N° de caisse sans formatage
                fileOut.write(getNumCaisse() + ";");
                // Demande d'abandon
                if (Boolean.TRUE.equals(entity.getDemandeAnnulee())) {
                    fileOut.write("1;");
                } else {
                    fileOut.write("0;");
                }
                // N° de demande
                fileOut.write(getFormattedString(entity.getIdCommunication(), 10) + ";");
                // Année
                fileOut.write(entity.getAnneeDecision() + ";");
                // Indep (0=faux, 1= vrai)
                if (CPDecision.CS_NON_ACTIF.equals(entity.getGenreAffilie())) {
                    fileOut.write("0;");
                } else {
                    fileOut.write("1;");
                }
                // N° de personne interne à la caisse (N° affilié)
                String numAffilieNonFormatte = CPToolBox.unFormat(entity.getNumAffilie());
                fileOut.write(getFormattedString(numAffilieNonFormatte, 20) + ";");
                // N° avs sur 11 chiffres
                String varString = NSUtil.unFormatAVS(entity.getNumAvs());
                if (varString.length() == 13) {
                    // Recherche de l'ancien n° avs
                    fileOut.write(NSUtil.unFormatAVS(CPUtil.getNssOrNavs(varString, getSession())) + ";");
                } else {
                    fileOut.write(varString + ";");
                }
                // N" AFC -> n° de contribuable
                String numContriBuable = CPToolBox.unFormat(entity.getNumContri(entity.getAnneeDecision()));
                fileOut.write(getFormattedString(numContriBuable, 8) + ";");
                // Nom
                fileOut.write(entity.getNom() + ";");
                // Prénom
                fileOut.write(entity.getPrenom() + ";");
                // Extraction des données du tiers
                TITiersViewBean tiers = new TITiersViewBean();
                tiers.setSession(getSession());
                tiers.setIdTiers(entity.getIdTiers());
                tiers.retrieve();
                if (!tiers.isNew()) {
                    // date de naissance
                    fileOut.write(JACalendar.format(tiers.getDateNaissance(), JACalendar.FORMAT_YYYYMMDD) + ";");
                } else {
                    fileOut.write(";");
                }
                // Raison sociale si indépendant
                if (!CPDecision.CS_NON_ACTIF.equals(entity.getGenreAffilie())) {
                    AFAffiliation affiliation = new AFAffiliation();
                    affiliation.setSession(getSession());
                    affiliation.setAffiliationId(entity.getIdAffiliation());
                    affiliation.retrieve();
                    if (!affiliation.isNew()) {
                        fileOut.write(getFormattedString(affiliation.getRaisonSociale(), 40) + ";");
                    } else {
                        fileOut.write(";");
                    }
                } else {
                    fileOut.write(";");
                }
                if (!tiers.isNew()) {
                    // Adresse 1
                    TIAdresseDataSource adresse = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                            "519005", "31.12." + entity.getAnneeDecision(), true);
                    if (adresse != null) {
                        Hashtable<?, ?> data = adresse.getData();
                        // Renseigner uniquement si adresse1 différente de
                        // données du tiers
                        String vard1 = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D1);
                        String vart1 = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_T1);
                        if (!vard1.equalsIgnoreCase(vart1)) {
                            fileOut.write(vard1 + ";");
                        } else {
                            fileOut.write(";");
                        }
                    } else {
                        fileOut.write(";");
                    }
                    // Adresse 2
                    fileOut.write(tiers.getRue() + ";");
                } else {
                    // Adresse 1
                    fileOut.write(";");
                    // Adresse 2
                    fileOut.write(";");
                }
                // Conjoint
                TITiersViewBean conjoint = null;
                try {
                    conjoint = CPToolBox.rechercheConjoint(getSession(), entity.getIdTiers(),
                            "31.12." + entity.getAnneeDecision());
                } catch (Exception e) {
                    conjoint = null;
                }
                if (conjoint != null) {
                    // N° AVS du conjoint sur 11
                    fileOut.write(JAStringFormatter.unFormatAVS(conjoint.getNumAvsActuel()) + ";");
                    // Nom du conjoint
                    fileOut.write(conjoint.getDesignation1() + ";");
                    // Prénom du conjoint
                    fileOut.write(conjoint.getDesignation2());
                } else {
                    fileOut.write(";");
                    fileOut.write(";");
                }
                if (varString.length() == 11) {
                    // Recherche du NSS
                    fileOut.write(";" + CPUtil.getNssOrNavs(varString, getSession()));
                } else {
                    fileOut.write(";" + varString);
                }
                fileOut.write("\n");
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
                    // }
                }
            }
            fileOut.close();
        } catch (IOException e) {
            JadeLogger.error(this, e);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return getFilename();
    }

    public String getCodeIfd(String idIfd) {
        String numIfd = null;
        String code = null;
        try {
            CPPeriodeFiscale periode = new CPPeriodeFiscale();
            periode.setSession(getSession());
            periode.setIdIfd(idIfd);
            periode.retrieve();
            numIfd = periode.getNumIfd();
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        if ((numIfd != null) || !JadeStringUtil.isEmpty(numIfd)) {
            code = "S" + numIfd;
        } else {
            code = "";
        }

        return code;
    }

    public String getFilename() {
        return filename;
    }

    public String getFormattedInt(String champ, int grandeurMax) {
        String format = "";
        int taille = 0;
        taille = champ.length();
        format = champ;
        if (champ.equals("") || (champ == null)) {
            format = "0";
            taille = 1;
        }
        for (int i = taille; i < grandeurMax; i++) {
            format = " " + format;
        }
        return format;
    }

    public String getFormattedString(String champ, int grandeurMax) {
        String format = "";
        int taille = 0;
        taille = champ.length();
        if (taille > grandeurMax) {
            format = champ.substring(0, grandeurMax - 1);
        } else {
            format = champ;
        }
        return format;
    }

    public String getNumCaisse() {
        return numCaisse;
    }

    public String getNumIfd(String idIfd) {
        String numIfd = null;
        try {
            CPPeriodeFiscale periode = new CPPeriodeFiscale();
            periode.setSession(getSession());
            periode.setIdIfd(idIfd);
            periode.retrieve();
            numIfd = periode.getNumIfd();
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return numIfd;
    }

    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    public BSession getSession() {
        return session;
    }

    public void setProcessAppelant(BProcess processAppelant) {
        this.processAppelant = processAppelant;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

}
