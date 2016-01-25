package globaz.naos.db.affiliation;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

/**
 * @author MMO
 * @since 22 août 2011
 */
public class AFListeAffilieFraisGestionReduitViewBean extends AFAbstractViewBean {

    private String email = "";
    private String tauxFraisGestion = "";

    /**
     * 
     * Constructor
     */
    public AFListeAffilieFraisGestionReduitViewBean() throws Exception {
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

    public String getTauxFraisGestion() {
        return tauxFraisGestion;
    }

    /**
     * setter
     */
    public void setEmail(String newEmail) {
        email = newEmail;
    }

    public void setTauxFraisGestion(String tauxFraisGestion) {
        this.tauxFraisGestion = tauxFraisGestion;
    }

}
