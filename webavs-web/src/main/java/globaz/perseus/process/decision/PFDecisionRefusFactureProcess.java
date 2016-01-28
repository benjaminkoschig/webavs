package globaz.perseus.process.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.perseus.process.PFAbstractJob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.perseus.businessimpl.services.facture.DecisionRefusFactureBuilder;

public class PFDecisionRefusFactureProcess extends PFAbstractJob {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Map<String, String> donneesProcess = null;
    private String emailAdresse = null;
    private String isSendToGed = "";

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public Map<String, String> getDonneesProcess() {
        return donneesProcess;
    }

    public String getEmailAdresse() {
        return emailAdresse;
    }

    /**
     * @return the isSendToGed
     */
    public String getIsSendToGed() {
        return isSendToGed;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    protected void process() throws Exception {
        try {
            // Infos de publications
            DecisionRefusFactureBuilder builder = new DecisionRefusFactureBuilder();
            builder.setDonneesProcess(donneesProcess);
            if ("on".equals(isSendToGed)) {
                builder.setSendToGed(true);
            }
            JadePrintDocumentContainer container = builder.build();

            this.createDocuments(container);

        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(),
                    e.getMessage() + System.getProperty("line.separator") + System.getProperty("line.separator")
                            + "Erreur : " + System.getProperty("line.separator") + e.getClass());
            e.printStackTrace();
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeBusinessMessage[] messages = JadeThread.logMessages();
            for (int i = 0; i < messages.length; i++) {
                getLogSession().addMessage(messages[i]);
            }
        }

        if (getLogSession().hasMessages()) {
            List<String> email = new ArrayList<String>();
            email.add(getEmailAdresse());
            this.sendCompletionMail(email);
        }
    }

    public void setDonneesProcess(Map<String, String> donneesProcess) {
        this.donneesProcess = donneesProcess;
    }

    public void setEmailAdresse(String emailAdresse) {
        if (!JadeStringUtil.isEmpty(emailAdresse)) {
            this.emailAdresse = emailAdresse;
        }
    }

    /**
     * @param isSendToGed
     *            the isSendToGed to set
     */
    public void setIsSendToGed(String isSendToGed) {
        this.isSendToGed = isSendToGed;
    }
}
