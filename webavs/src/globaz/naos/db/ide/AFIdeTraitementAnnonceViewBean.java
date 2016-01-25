package globaz.naos.db.ide;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

public class AFIdeTraitementAnnonceViewBean extends AFAbstractViewBean {

    private String email;
    private String forTypeTraitement;
    private Boolean modeTestSedex;

    public Boolean getModeTestSedex() {
        return modeTestSedex;
    }

    public void setModeTestSedex(Boolean modeTestSedex) {
        this.modeTestSedex = modeTestSedex;
    }

    public String getForTypeTraitement() {
        return forTypeTraitement;
    }

    public void setForTypeTraitement(String forTypeTraitement) {
        this.forTypeTraitement = forTypeTraitement;
    }

    public AFIdeTraitementAnnonceViewBean() throws Exception {

    }

    public AFIdeTraitementAnnonceViewBean(BSession session) {

    }

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    public void setEmail(String string) {
        email = string;
    }

}
