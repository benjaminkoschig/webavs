package globaz.perseus.process.decision;

import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.perseus.process.PFAbstractJob;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFDecisionProcess extends PFAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDocument = null;
    private String decisionId = null;
    /**
     * La variable de l'adresse email est automatiquement setter à NULL si elle est nommée (eMailAddress) et doit donc
     * être renommée différement (mailAd) pour fonctionner correctement.
     */
    private String eMailAddress = null;

    private String isSendToGed = "";
    private String mailAd = null;

    public String getDateDocument() {
        return dateDocument;
    }

    public String getDecisionId() {
        return decisionId;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public String geteMailAddress() {
        return eMailAddress;
    }

    /**
     * @return the isSendToGed
     */
    public String getIsSendToGed() {
        return isSendToGed;
    }

    public String getMailAd() {
        return mailAd;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    protected void process() throws Exception {
        try {
            // TODO : Appel d'une methode permettant de voir la construction du document.
            // TopazSystem.getInstance().getDocBuilder().setOpenedDocumentsVisible(true);

            // Déterminer le type de décision a générer
            if ("on".equals(isSendToGed)) {
                JadePrintDocumentContainer container = PerseusServiceLocator.getDecisionBuilderProviderService()
                        .getBuilderFor(decisionId).build(decisionId, mailAd, dateDocument, true);
                this.createDocuments(container);

            } else {
                JadePrintDocumentContainer container = PerseusServiceLocator.getDecisionBuilderProviderService()
                        .getBuilderFor(decisionId).build(decisionId, mailAd, dateDocument, false);
                this.createDocuments(container);

            }

        } catch (Exception e) {
            e.printStackTrace();
            JadeThread.logError(this.getClass().getName(),
                    e.getMessage() + System.getProperty("line.separator") + System.getProperty("line.separator")
                            + "Erreur : " + System.getProperty("line.separator") + e.toString());

        }

        Iterator test = JadeLogger.getInstance().logsIterator();
        while (test.hasNext()) {
            Object o = test.next();
            JadeThread.logError(this.getClass().getName(), "/n" + "Erreur technique (jadeLogger : " + o.toString());
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeBusinessMessage[] messages = JadeThread.logMessages();
            for (int i = 0; i < messages.length; i++) {
                getLogSession().addMessage(messages[i]);
            }
            List<String> email = new ArrayList<String>();
            email.add(mailAd);
            this.sendCompletionMail(email);
        }
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDecisionId(String decisionId) {
        this.decisionId = decisionId;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    /**
     * @param isSendToGed
     *            the isSendToGed to set
     */
    public void setIsSendToGed(String isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setMailAd(String mailAd) {
        this.mailAd = mailAd;
    }

}
