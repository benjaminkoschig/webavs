package globaz.naos.db.ide;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.AFAbstractViewBean;

public class AFIdeSyncRegistreViewBean extends AFAbstractViewBean {

    private String email;
    private Boolean modeForceAllStatus;

    public AFIdeSyncRegistreViewBean() throws Exception {
        modeForceAllStatus = Boolean.FALSE;
    }

    public AFIdeSyncRegistreViewBean(BSession session) {

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

    public Boolean getModeForceAllStatus() {
        return modeForceAllStatus;
    }

    public void setModeForceAllStatus(Boolean modeForceAllStatus) {
        this.modeForceAllStatus = modeForceAllStatus;
    }

}
