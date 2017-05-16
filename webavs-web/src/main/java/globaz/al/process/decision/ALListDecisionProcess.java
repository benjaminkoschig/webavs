/**
 * 
 */
package globaz.al.process.decision;

import globaz.al.process.ALAbsrtactProcess;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishDocumentMessage;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.naos.translation.CodeSystem;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import ch.globaz.al.business.constantes.ALConstDecisions;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Process permettant l'édtion d'une liste de dossier ayant faits l'objets de l'édition de décision pour une période
 * 
 * @author pta
 * 
 */
public class ALListDecisionProcess extends ALAbsrtactProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * date debut
     */
    private String dateDebut = null;
    /**
     * date fin
     */
    private String dateFin = null;
    /**
     * email (par défaut celle de l'utilisateur)
     */
    private String email = null;

    private String csPeriodicite = null;

    /**
     * Méthode qui créer et envoi les fichiers
     * 
     * @param documentCsv
     * @param fileNameDetail
     */
    private void createFileCsv(String documentCsv, String fileNameDetail) {
        FileOutputStream fichier;

        try {

            fichier = new FileOutputStream(fileNameDetail);
            try {
                fichier.write(documentCsv.getBytes());
                fichier.flush();
                fichier.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * Méthode qui cré le document à envoyer
     * 
     * @param listDossierRetro
     *            données des dossier
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private void creationEtEnvoiFichier(String listDossierRetro) throws JadeApplicationException,
            JadePersistenceException {

        String fileNameDetail;
        if (CodeSystem.PERIODICITE_MENSUELLE.equals(csPeriodicite)) {
            fileNameDetail = getSession().getLabel("NOM_LISTE_RETRO_PERIODICITE_M");
        } else {
            fileNameDetail = getSession().getLabel("NOM_LISTE_RETRO_PERIODICITE_T");
        }

        // String fileNameDetail = "ListeDossiersRetroactifs.csv";
        createFileCsv(listDossierRetro, fileNameDetail);
        try {
            envoiDocument(fileNameDetail);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        File file = new File(fileNameDetail);
        file.delete();
    }

    /**
     * Méthode settant les données d'envois du document
     * 
     * @param fileName
     * @throws IOException
     */

    private void envoiDocument(String fileName) throws IOException {

        String documentTitle;
        if (CodeSystem.PERIODICITE_MENSUELLE.equals(csPeriodicite)) {
            documentTitle = getSession().getLabel("TITRE_LISTE_RETRO_PERIODICITE_M");
        } else {
            documentTitle = getSession().getLabel("TITRE_LISTE_RETRO_PERIODICITE_T");
        }

        JadePublishDocumentInfo logInfo = new JadePublishDocumentInfo();
        // // logInfo.setOwnerId(JadeThread.currentUserId());
        logInfo.setOwnerEmail(getEmail());
        logInfo.setDocumentTitle(documentTitle);
        logInfo.setDocumentSubject(documentTitle);
        logInfo.setArchiveDocument(false);
        JadePublishDocument docInfoCSV = new JadePublishDocument(fileName, logInfo);

        try {
            JadePublishServerFacade.publishDocument(new JadePublishDocumentMessage(docInfoCSV));
        } catch (JadeServiceActivatorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassCastException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JadeClassCastException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JadeServiceLocatorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getDescription()
     */
    @Override
    public String getDescription() {

        return null;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }
        return email;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getName()
     */
    @Override
    public String getName() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.al.process.ALAbsrtactProcess#process()
     */
    @Override
    protected void process() {
        boolean errorInProcess = false;

        StringBuffer donnesListDossierRetro = new StringBuffer();

        try {
            // retourne les dossier pour lesquels une décision a été mise en ged dans l'intervalle donné, sauf ceux dont
            // dates de validité
            // débutant le mois suivant la dernière factu
            ArrayList<String> listDossierJournaliser = ALServiceLocator.getDecisionListService()
                    .getListDossierJournaliser(dateDebut, dateFin);

            DossierComplexSearchModel listDossierRetro = new DossierComplexSearchModel();
            if (listDossierJournaliser.size() > ALConstDecisions.DECISION_NBRE_JOURNALISER) {
                // TODO:gérer aussi les certifications de radiation, prest CO et SA
                listDossierRetro = ALServiceLocator.getDecisionListService().getListDossierRetroActif(dateDebut,
                        dateFin, listDossierJournaliser, getCsPeriodicite());
            }

            String periode;
            if (CodeSystem.PERIODICITE_MENSUELLE.equals(csPeriodicite)) {
                periode = getSession().getLabel("AL0028_PERIODICITE_M");
            } else {
                periode = getSession().getLabel("AL0028_PERIODICITE_T");
            }

            donnesListDossierRetro = ALServiceLocator.getDecisionListService().getDonneesListDossier(listDossierRetro,
                    dateDebut, dateFin, periode);

        } catch (Exception e) {
            errorInProcess = true;
            e.printStackTrace();
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e.getMessage() });
        }
        try {
            if (!errorInProcess) {
                creationEtEnvoiFichier(donnesListDossierRetro.toString());
            }
        } catch (Exception e) {
            errorInProcess = true;
            e.printStackTrace();
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e.getMessage() });
        }

        // Envoie d'un mail si problème pour lancer le traitement
        ArrayList<String> emails = new ArrayList<String>();
        emails.add(JadeThread.currentUserEmail());
        if (errorInProcess) {
            try {
                sendCompletionMail(emails);
            } catch (Exception e1) {
                JadeLogger.error(this,
                        "Impossible d'envoyer le mail de résultat du traitement. Raison : " + e1.getMessage() + ", "
                                + e1.getCause());
            }
        }

    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCsPeriodicite() {
        return csPeriodicite;
    }

    public void setCsPeriodicite(String csPeriodicite) {
        this.csPeriodicite = csPeriodicite;
    }

}
