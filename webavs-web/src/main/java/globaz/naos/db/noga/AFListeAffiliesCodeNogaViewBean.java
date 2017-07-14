/**
 * 
 */
package globaz.naos.db.noga;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;

/**
 * @author est
 * 
 */
public class AFListeAffiliesCodeNogaViewBean extends AFAffiliation implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 290039079313253601L;
    private String email;
    private Boolean isOnlyAffiliesActifs;

    public AFListeAffiliesCodeNogaViewBean() throws Exception {
        //
    }

    public AFListeAffiliesCodeNogaViewBean(BSession session) {
    }

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    public Boolean getIsOnlyAffiliesActifs() {
        return isOnlyAffiliesActifs;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsOnlyAffiliesActifs(Boolean isOnlyAffiliesActifs) {
        this.isOnlyAffiliesActifs = isOnlyAffiliesActifs;
    }
}
