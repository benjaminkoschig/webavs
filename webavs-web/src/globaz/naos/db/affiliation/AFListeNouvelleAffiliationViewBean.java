package globaz.naos.db.affiliation;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

/**
 * @author MMO
 * @since 14 mars 2011
 */
public class AFListeNouvelleAffiliationViewBean extends AFAbstractViewBean {

    private String critereSelection = "";
    private String email = "";

    private String fromDateCreation = "";

    private String toDateCreation = "";

    /**
     * Constructeur de AFListeNouvelleAffiliationViewBean
     */
    public AFListeNouvelleAffiliationViewBean() throws Exception {
    }

    public String getCritereSelection() {
        return critereSelection;
    }

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    /**
     * getter
     */

    public String getFromDateCreation() {
        return fromDateCreation;
    }

    public String getToDateCreation() {
        return toDateCreation;
    }

    public void setCritereSelection(String critereSelection) {
        this.critereSelection = critereSelection;
    }

    public void setEmail(String newEmail) {
        email = newEmail;
    }

    /**
     * setter
     */
    public void setFromDateCreation(String newFromDateCreation) {
        fromDateCreation = newFromDateCreation;
    }

    public void setToDateCreation(String newToDateCreation) {
        toDateCreation = newToDateCreation;
    }

}
