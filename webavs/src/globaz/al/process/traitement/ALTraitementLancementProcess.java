package globaz.al.process.traitement;

import globaz.al.process.ALAbsrtactProcess;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.job.common.JadeJobQueueNames;
import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishDocumentMessage;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.exceptions.processus.ALProcessusCtrlException;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Process qui se chargement de lancer un traitement
 * 
 * @author GMO
 * 
 */
public class ALTraitementLancementProcess extends ALAbsrtactProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * mode de fonctionnement, soit on exécute le traitement, soit on l'annule
     */
    private boolean cancelMode = false;

    /**
     * Identifiant du processus contenant le traitement à lancer
     */
    private String idProcessusPeriodique = null;
    /**
     * Identifiant du traitement à lancer
     */
    private String idTraitementPeriodique = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getDescription()
     */
    @Override
    public String getDescription() {
        return JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(),
                "globaz.al.process.traitement.ALTraitementLancementProcess.description");
    }

    /**
     * 
     * @return idProcessusPeriodique
     */
    public String getIdProcessusPeriodique() {
        return idProcessusPeriodique;
    }

    /**
     * @return idTraitementPeriodique
     */
    public String getIdTraitementPeriodique() {
        return idTraitementPeriodique;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getName()
     */
    @Override
    public String getName() {
        return JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(),
                "globaz.al.process.traitement.ALTraitementLancementProcess.description");
    }

    /**
     * @return cancelMode
     */
    public boolean isCancelMode() {
        return cancelMode;
    }

    @Override
    public String jobQueueName() {
        return JadeJobQueueNames.SYSTEM_BATCH_JOB_QUEUE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.al.process.ALAbsrtactProcess#process()
     */
    @Override
    protected void process() {

        HashMap<ALConstProtocoles.TypeProtocole, Object> protocoles = null;
        ArrayList<String> emails = new ArrayList<String>();
        emails.add(JadeThread.currentUserEmail());
        try {
            protocoles = ALServiceLocator.getBusinessProcessusService().executeTraitementPeriodique(
                    idProcessusPeriodique, idTraitementPeriodique);

        } catch (ALProcessusCtrlException e) {
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.nonpret",
                    new String[] { e.getMessage() });

        } catch (JadeApplicationServiceNotAvailableException e) {
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e.getMessage() });

        } catch (JadeApplicationException e) {
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e.getMessage() });

        } catch (JadePersistenceException e) {
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e.getMessage() });
        }
        // si une exception se produit dans l'execution du traitement et remonte ici, protocoles sera null
        if (protocoles != null) {

            /*
             * Protocole PDF
             */
            if (protocoles.get(ALConstProtocoles.TypeProtocole.PDF) != null) {
                JadePrintDocumentContainer[] protocolesPDF = (JadePrintDocumentContainer[]) protocoles
                        .get(ALConstProtocoles.TypeProtocole.PDF);

                for (int i = 0; i < protocolesPDF.length; i++) {
                    // génération de l'éventuel protocole
                    if (protocolesPDF[i] != null) {

                        try {
                            this.createDocuments(protocolesPDF[i]);
                        } catch (Exception e) {
                            JadeLogger.error(this, "Impossible de créer les documents. Raison : " + e.getMessage()
                                    + ", " + e.getCause());
                        }
                    }
                }
            }

            ArrayList<String> protocolesCSV = (ArrayList<String>) protocoles.get(ALConstProtocoles.TypeProtocole.CSV);

            /*
             * création du fichier CSV
             */
            if ((protocolesCSV != null) && (protocolesCSV.size() > 0)) {

                String fileNameBase = new String("protocole_csv_%d.csv");
                int counter = 0;
                for (String csv : protocolesCSV) {
                    try {
                        if (!JadeStringUtil.isBlank(csv)) {
                            String fileName = Jade.getInstance().getPersistenceDir()
                                    + String.format(fileNameBase, counter++);
                            FileOutputStream fichier = new FileOutputStream(fileName);

                            fichier.write(csv.getBytes());
                            System.out.printf("wrote %d bytes in file %s.\n", csv.getBytes().length, fileName);
                            fichier.flush();
                            fichier.close();
                            JadePublishDocument docInfoCSV = new JadePublishDocument(fileName,
                                    ((JadePublishDocumentInfo) protocoles.get(ALConstProtocoles.TypeProtocole.INFO))
                                            .createCopy());
                            System.out.printf("Publishing file %s\n", docInfoCSV.getDocumentLocation());
                            JadePublishServerFacade.publishDocument(new JadePublishDocumentMessage(docInfoCSV));

                        }
                    } catch (Exception e) {
                        JadeLogger.error(this, new Exception("Erreur lors de la génération du protocole CSV", e));
                        JadeThread.logError(this.getClass().getName() + ".process()",
                                "Erreur lors de la génération du protocole CSV");
                    }

                }

            }
        }

        // Envoie d'un mail si problème pour lancer le traitement
        try {
            sendCompletionMail(emails);
        } catch (Exception e1) {
            JadeLogger.error(
                    this,
                    "Impossible d'envoyer le mail de résultat du traitement. Raison : " + e1.getMessage() + ", "
                            + e1.getCause());
        }

    }

    /**
     * @param cancelMode
     */
    public void setCancelMode(boolean cancelMode) {
        this.cancelMode = cancelMode;
    }

    /**
     * 
     * @param idProcessusPeriodique
     */
    public void setIdProcessusPeriodique(String idProcessusPeriodique) {
        this.idProcessusPeriodique = idProcessusPeriodique;
    }

    /**
     * @param idTraitementPeriodique
     *            définit l'identifiant du traitement à lancer
     */
    public void setIdTraitementPeriodique(String idTraitementPeriodique) {
        this.idTraitementPeriodique = idTraitementPeriodique;
    }

}
