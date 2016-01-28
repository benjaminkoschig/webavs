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
 * Process de g�n�ration par affili�
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
     * le num�ro d'affili� pour lequel g�n�rer les prestations
     */
    private String numAffilie = null;

    /**
     * la p�riode � g�n�rer pour l'affili�
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
                    "Une erreur s'est produite pendant la g�n�ration de prestation pour l'affili� " + numAffilie
                            + ", p�riode " + periodeAGenerer + ". Raison : " + e2.getMessage() + "\n" + e2.getCause());

        } catch (JadePersistenceException e2) {
            JadeLogger.error(this,
                    "Une erreur s'est produite pendant la g�n�ration de prestation pour l'affili� " + numAffilie
                            + ", p�riode " + periodeAGenerer + ". Raison : " + e2.getMessage() + "\n" + e2.getCause());
        }

        // g�n�ration de l'�ventuel protocole
        if (protocole != null) {

            try {
                this.createDocuments(protocole);
            } catch (Exception e) {
                JadeLogger.error(this,
                        "Une erreur s'est produite pendant la g�n�ration du protocole de g�n�ration pour l'affili� "
                                + numAffilie + ", p�riode " + periodeAGenerer + ". Raison : " + e.getMessage() + "\n"
                                + e.getCause());
            }
        }

        // Envoie d'un mail si probl�me pour lancer le traitement
        try {
            // this.getLogSession().info(source, messageId);
            sendCompletionMail(emails);
        } catch (Exception e1) {
            e1.printStackTrace();
            JadeLogger
                    .error(this,
                            "Impossible d'envoyer le mail de r�sultat du traitement de g�n�ration de prestation pour un affili�");
        }
    }

    /**
     * @param numAffilie
     *            le num�ro d'affili� � g�n�rer
     */
    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    /**
     * @param periodeAGenerer
     *            la p�riode � g�n�rer
     */
    public void setPeriodeAGenerer(String periodeAGenerer) {
        this.periodeAGenerer = periodeAGenerer;
    }
}
