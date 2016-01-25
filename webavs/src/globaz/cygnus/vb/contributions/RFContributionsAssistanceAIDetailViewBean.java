package globaz.cygnus.vb.contributions;

import globaz.cygnus.db.contributions.RFContributionsAssistanceAI;

/**
 * ViewBean dédié au détail de la capage des contributions d'assistance AI
 * 
 * @author PBA
 */
public class RFContributionsAssistanceAIDetailViewBean extends RFContributionsAssistanceAI {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean dernierePeriodeOuverte;
    private String messageAvertissementModification;

    public RFContributionsAssistanceAIDetailViewBean() {
        super();

        dernierePeriodeOuverte = false;
        messageAvertissementModification = null;
    }

    public String getDetailCodeAPI() {
        return RFContributionsAssistanceAIUtils.getDetailCodeAPI(getSession(), getCodeAPI());
    }

    public String getMessageAvertissementModification() {
        return messageAvertissementModification;
    }

    public boolean isDernierePeriodeOuverte() {
        return dernierePeriodeOuverte;
    }

    public void setDernierePeriodeOuverte(boolean dernierePeriodeOuverte) {
        this.dernierePeriodeOuverte = dernierePeriodeOuverte;
    }

    public void setMessageAvertissementModification(String message) {
        messageAvertissementModification = message;
    }
}
