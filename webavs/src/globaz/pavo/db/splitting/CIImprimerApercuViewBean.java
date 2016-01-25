package globaz.pavo.db.splitting;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JAUtil;

/**
 * Insérez la description du type ici. Date de création : (21.01.2003 08:24:32)
 * 
 * @author: David Girardin
 */
public class CIImprimerApercuViewBean extends CIDossierSplitting implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean check = false;
    private String email = "";

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:46:12)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public CIImprimerApercuViewBean() throws java.lang.Exception {
    }

    /**
     * @return
     */
    public String getEmail() {
        if (JAUtil.isStringEmpty(email)) {
            getSession().getUserEMail();
        }
        return email;
    }

    /**
     * @return
     */
    public boolean isCheck() {
        return check;
    };

    /**
     * @param b
     */
    public void setCheck(boolean b) {
        check = b;
    }

    /**
     * @param string
     */
    public void setEmail(String string) {
        email = string;
    }

}
