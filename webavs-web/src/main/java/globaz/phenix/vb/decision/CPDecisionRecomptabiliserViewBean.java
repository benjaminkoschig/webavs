package globaz.phenix.vb.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.vb.CPAbstractPersistentObjectViewBean;

public class CPDecisionRecomptabiliserViewBean extends CPAbstractPersistentObjectViewBean {

    private String eMailAdress = "";
    public String idDecision = "";
    private Boolean wantMajCI = Boolean.TRUE;

    @Override
    public void add() throws Exception {
        // DO NOTHING, USED TO LAUNCH PROCESS
    }

    @Override
    public void delete() throws Exception {
        // DO NOTHING, USED TO LAUNCH PROCESS
    }

    public String getEMailAddress() {
        if (JadeStringUtil.isBlankOrZero(eMailAdress)) {
            return getSession().getUserEMail();
        } else {
            return eMailAdress;
        }
    }

    public String getIdDecision() {
        return idDecision;
    }

    public Boolean getWantMajCI() {
        return wantMajCI;
    }

    @Override
    public void retrieve() throws Exception {
        // DO NOTHING, USED TO LAUNCH PROCESS
    }

    public void setEMailAddress(String mailAddress) {
        eMailAdress = mailAddress;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setWantMajCI(Boolean wantMajCI) {
        this.wantMajCI = wantMajCI;
    }

    @Override
    public void update() throws Exception {
        // DO NOTHING, USED TO LAUNCH PROCESS
    }

}
