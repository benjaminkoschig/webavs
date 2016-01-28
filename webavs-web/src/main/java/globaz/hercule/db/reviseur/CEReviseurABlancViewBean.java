package globaz.hercule.db.reviseur;

import globaz.globall.db.BManager;
import globaz.hercule.db.CEAbstractViewBean;
import globaz.jade.log.JadeLogger;

public class CEReviseurABlancViewBean extends CEAbstractViewBean {

    String idReviseur = "";

    /**
     * Constructeur CEReviseurViewBean
     */
    public CEReviseurABlancViewBean() {
        super();
    }

    /**
     * @return
     */
    public CEReviseurManager _getReviseursList() {

        CEReviseurManager reviseurMan = new CEReviseurManager();
        reviseurMan.setSession(getSession());

        try {

            reviseurMan.find(BManager.SIZE_NOLIMIT);

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        return reviseurMan;

    }

    // *******************************************************
    // Getter
    // ***************************************************

    public String getIdReviseur() {
        return idReviseur;
    }

    // *******************************************************
    // Setter
    // ***************************************************

    public void setIdReviseur(String idReviseur) {
        this.idReviseur = idReviseur;
    }
}
