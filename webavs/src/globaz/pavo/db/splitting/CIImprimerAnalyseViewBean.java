package globaz.pavo.db.splitting;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JAUtil;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (21.01.2003 08:24:32)
 * 
 * @author: David Girardin
 */
public class CIImprimerAnalyseViewBean extends CIDossierSplitting implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String email = "";

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.01.2003 11:46:12)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public CIImprimerAnalyseViewBean() throws java.lang.Exception {
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
     * @param string
     */
    public void setEmail(String string) {
        email = string;
    }

}
