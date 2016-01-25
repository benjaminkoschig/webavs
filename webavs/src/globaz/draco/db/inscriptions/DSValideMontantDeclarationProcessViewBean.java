package globaz.draco.db.inscriptions;

import globaz.framework.bean.FWViewBeanInterface;

public class DSValideMontantDeclarationProcessViewBean extends DSInscriptionsIndividuellesListeViewBean implements
        FWViewBeanInterface {

    private static final long serialVersionUID = -8081220914891836010L;
    private String eMailAddress = "";

    public DSValideMontantDeclarationProcessViewBean() {
        super();
    }

    /**
     * @return
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getEmailAdressEcran() {
        return getSession().getUserEMail();
    }

    /**
     * @param string
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

}
