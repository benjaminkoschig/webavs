package globaz.draco.db.declaration;

import globaz.framework.bean.FWViewBeanInterface;

public class DSAnnulerValidationViewBean extends DSDeclarationViewBean implements FWViewBeanInterface {

    private static final long serialVersionUID = -5692221567020199328L;
    String eMailAddress = "";

    public DSAnnulerValidationViewBean() {
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
