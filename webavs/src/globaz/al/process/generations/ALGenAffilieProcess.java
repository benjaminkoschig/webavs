package globaz.al.process.generations;

import globaz.al.process.ALAbsrtactProcess;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import java.util.ArrayList;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Process de génération par affilié
 * 
 * @author GMO
 * 
 */
public class ALGenAffilieProcess extends ALAbsrtactProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * le numéro d'affilié pour lequel générer les prestations
     */
    private String numAffilie = null;

    /**
     * la période à générer pour l'affilié
     */
    private String periodeAGenerer = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getDescription()
     */
    @Override
    public String getDescription() {
        return JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(),
                "globaz.al.process.generations.ALGenAffilieProcess.description");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getName()
     */
    @Override
    public String getName() {
        return JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(),
                "globaz.al.process.generations.ALGenAffilieProcess.name");
    }

    /**
     * @return numAffilie
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * @return periodeAGenerer
     */
    public String getPeriodeAGenerer() {
        return periodeAGenerer;
    }

    @Override
    protected void process() {
        JadePrintDocumentContainer protocole = null;
        ArrayList<String> emails = new ArrayList<String>();
        emails.add(JadeThread.currentUserEmail());

        try {
            protocole = ALServiceLocator.getGenerationAffilieService().generationAffilie(numAffilie, periodeAGenerer);
        } catch (JadeApplicationException e2) {
            JadeLogger.error(this,
                    "Une erreur s'est produite pendant la génération de prestation pour l'affilié " + numAffilie
                            + ", période " + periodeAGenerer + ". Raison : " + e2.getMessage() + "\n" + e2.getCause());

        } catch (JadePersistenceException e2) {
            JadeLogger.error(this,
                    "Une erreur s'est produite pendant la génération de prestation pour l'affilié " + numAffilie
                            + ", période " + periodeAGenerer + ". Raison : " + e2.getMessage() + "\n" + e2.getCause());
        }

        // génération de l'éventuel protocole
        if (protocole != null) {

            try {
                this.createDocuments(protocole);
            } catch (Exception e) {
                JadeLogger.error(this,
                        "Une erreur s'est produite pendant la génération du protocole de génération pour l'affilié "
                                + numAffilie + ", période " + periodeAGenerer + ". Raison : " + e.getMessage() + "\n"
                                + e.getCause());
            }
        }

        // Envoie d'un mail si problème pour lancer le traitement
        try {
            // this.getLogSession().info(source, messageId);
            sendCompletionMail(emails);
        } catch (Exception e1) {
            e1.printStackTrace();
            JadeLogger
                    .error(this,
                            "Impossible d'envoyer le mail de résultat du traitement de génération de prestation pour un affilié");
        }
    }

    /**
     * @param numAffilie
     *            le numéro d'affilié à générer
     */
    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    /**
     * @param periodeAGenerer
     *            la période à générer
     */
    public void setPeriodeAGenerer(String periodeAGenerer) {
        this.periodeAGenerer = periodeAGenerer;
    }
}
