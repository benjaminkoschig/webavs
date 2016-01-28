package globaz.naos.db.affiliation;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFAffiliationNAIndSansCIViewBean extends AFAbstractViewBean {

    private String email;
    private String typeImpression = "pdf";

    public AFAffiliationNAIndSansCIViewBean() {
    }

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    public void setEmail(String string) {
        email = string;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }
}
