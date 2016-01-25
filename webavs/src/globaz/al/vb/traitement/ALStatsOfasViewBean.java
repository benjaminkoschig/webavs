package globaz.al.vb.traitement;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

/**
 * ViewBean gérant le lancement de l'impression des récaps
 * 
 * @author GMO
 * 
 */
public class ALStatsOfasViewBean extends AFAbstractViewBean {
    private String email = "";
    private String forAnnee = "";

    public ALStatsOfasViewBean() {
        super();
    }

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }
        return email;
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }
}
