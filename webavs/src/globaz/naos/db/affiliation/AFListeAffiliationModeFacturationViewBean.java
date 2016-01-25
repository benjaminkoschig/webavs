package globaz.naos.db.affiliation;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

/**
 * @author MMO
 * @since 14 mars 2011
 */
public class AFListeAffiliationModeFacturationViewBean extends AFAbstractViewBean {

    private String email = "";

    /**
     * Constructeur de AFListeNouvelleAffiliationViewBean
     */
    public AFListeAffiliationModeFacturationViewBean() throws Exception {
    }

    /**
     * getter
     */
    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    /**
     * setter
     */
    public void setEmail(String newEmail) {
        email = newEmail;
    }

}
