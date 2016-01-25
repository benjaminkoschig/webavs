package globaz.pavo.db.splitting;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Bean de la liste des mandats de splitting Date de création : (29.10.2002 13:49:44)
 * 
 * @author: dgi
 */
public class CIMandatSplittingViewBean extends CIMandatSplitting implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;
    private String idAnnonceRCI = null;
    private Boolean isArchivage = new Boolean(false);
    private java.lang.String message = null;
    private java.lang.String msgType = null;
    private String refUniqueRCI = null;

    public java.lang.String getAction() {
        return action;
    }

    /**
     * Returns the idAnnonceRCI.
     * 
     * @return String
     */
    public String getIdAnnonceRCI() {
        return idAnnonceRCI;
    }

    /**
     * @return
     */
    public Boolean getIsArchivage() {
        return isArchivage;
    }

    /**
     * Returns the refUniqueRCI.
     * 
     * @return String
     */
    public String getRefUniqueRCI() {
        return refUniqueRCI;
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }

    /**
     * Sets the idAnnonceRCI.
     * 
     * @param idAnnonceRCI
     *            The idAnnonceRCI to set
     */
    public void setIdAnnonceRCI(String idAnnonceRCI) {
        this.idAnnonceRCI = idAnnonceRCI;
    }

    /**
     * @param boolean1
     */
    public void setIsArchivage(Boolean boolean1) {
        isArchivage = boolean1;
    }

    /**
     * Sets the refUniqueRCI.
     * 
     * @param refUniqueRCI
     *            The refUniqueRCI to set
     */
    public void setRefUniqueRCI(String refUniqueRCI) {
        this.refUniqueRCI = refUniqueRCI;
    }

}
