/*
 * Créé le 20 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.process.communications.envoiWritterImpl;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAStringFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichage;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import globaz.phenix.db.communications.CPReceptionReader;
import globaz.phenix.db.communications.CPReceptionReaderManager;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.pyxis.constantes.IConstantes;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPJuWritter {

    private final static String BASE_FILENAME = "ENVOI_FISC.txt";
    private String date_impression = "";
    public String filename = null;
    private BProcess processAppelant = null;
    private BSession session;
    private String user = "";

    public CPJuWritter(BSession session) {
        String nom = "";
        this.session = session;
        setUser(getSession().getUserFullName());
        try {
            CPReceptionReaderManager manager = new CPReceptionReaderManager();
            manager.setSession(session);
            manager.setForIdCanton(IConstantes.CS_LOCALITE_CANTON_JURA);
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
            filename = CPJuWritter.BASE_FILENAME;
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
            statement = manager.cursorOpen(transaction);
            fileOut = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFilename(), false), "ISO8859_1"));
            while (((entity = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null)
                    && (!entity.isNew())) {
                if (!JadeStringUtil.isEmpty(entity.getNumContri(entity.getAnneeDecision()))) {
                    String numContriBuable = JAStringFormatter.unFormat(entity.getNumContri(entity.getAnneeDecision()),
                            "", ".");
                    numContriBuable = JAStringFormatter.unFormat(numContriBuable, "", "/");
                    fileOut.write(getFormattedString(getCodeIfd(entity.getIdIfd()), 3));
                    fileOut.write(getFormattedInt(numContriBuable, 11));
                    fileOut.write(getFormattedString(getNumIfd(entity.getIdIfd()), 2) + "\n");
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

    public String getDate_impression() {
        return date_impression;
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
        format = champ;
        for (int i = taille; i < grandeurMax; i++) {
            format = format + " ";
        }
        return format;
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

    public String getUser() {
        return user;
    }

    public void setDate_impression(String string) {
        date_impression = string;
    }

    public void setProcessAppelant(BProcess processAppelant) {
        this.processAppelant = processAppelant;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setUser(String string) {
        user = string;
    }
}
