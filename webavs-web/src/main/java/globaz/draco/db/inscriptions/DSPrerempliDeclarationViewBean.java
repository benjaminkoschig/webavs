package globaz.draco.db.inscriptions;

import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.bean.FWViewBeanInterface;

public class DSPrerempliDeclarationViewBean extends DSDeclarationViewBean implements FWViewBeanInterface {

    private static final long serialVersionUID = 4458537413589935336L;
    private String eMailAddress = "";

    public DSPrerempliDeclarationViewBean() {
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
