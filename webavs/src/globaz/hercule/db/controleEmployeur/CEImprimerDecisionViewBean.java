package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BManager;
import globaz.hercule.db.CEAbstractViewBean;

public class CEImprimerDecisionViewBean extends CEAbstractViewBean {

    private String idPassage = new String();

    /**
     * Constructeur de CEImprimerDecisionViewBean
     */
    public CEImprimerDecisionViewBean() {
        super();
    }

    public String getIdPassage() {
        return idPassage;
    }

    // *******************************************************
    // Getter
    // ***************************************************

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new CEImprimerDecisionManager();
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }
}
