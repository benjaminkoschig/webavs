package globaz.perseus.vb.decision;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.perseus.business.models.decision.Decision;

public class PFDecisionProcessViewBean extends BJadePersistentObjectViewBean {

    private String dateDoc = null;
    private Decision decision = null;
    private String decisionId = null;
    private String eMailAddress = null;
    private String isSendToGed = null;

    public PFDecisionProcessViewBean() {
        decision = new Decision();
    }

    @Override
    public void add() throws Exception {
        throw new Exception("Action not implemented!");
    }

    @Override
    public void delete() throws Exception {
        throw new Exception("Action not implemented!");
    }

    public String getDateDoc() {
        return dateDoc;
    }

    public Decision getDecision() {
        return decision;
    }

    public String getDecisionId() {
        return decisionId;
    }

    public String geteMailAddress() {
        return eMailAddress;
    }

    @Override
    public String getId() {
        return decisionId;
    }

    @Override
    public BSpy getSpy() {
        return getSpy();
    }

    @Override
    public void retrieve() throws Exception {
        throw new Exception("Action not implemented!");
    }

    public void setDateDoc(String dateDoc) {
        this.dateDoc = dateDoc;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public void setDecisionId(String decisionId) {
        this.decisionId = decisionId;

    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public void update() throws Exception {
        throw new Exception("Action not implemented!");
    }

    public void setIsSendToGed(String isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public String getIsSendToGed() {
        return isSendToGed;
    }

}
