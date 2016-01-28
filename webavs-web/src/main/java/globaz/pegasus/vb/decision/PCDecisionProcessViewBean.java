package globaz.pegasus.vb.decision;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.ArrayList;

public class PCDecisionProcessViewBean extends BJadePersistentObjectViewBean {
    private String dateDoc = null;
    private ArrayList<String> decisionsId = null;
    private String mailGest = null;
    private String persref = null;
    private String typeDecision = null;

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

    public ArrayList<String> getDecisionsId() {
        return decisionsId;
    }

    @Override
    public String getId() {
        return null;
    }

    public String getMailGest() {
        return mailGest;
    }

    public String getPersref() {
        return persref;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public String getTypeDecision() {
        return typeDecision;
    }

    @Override
    public void retrieve() throws Exception {
        throw new Exception("Action not implemented!");

    }

    public void setDateDoc(String dateDoc) {
        this.dateDoc = dateDoc;
    }

    public void setDecisionsId(ArrayList<String> decisionsId) {
        this.decisionsId = decisionsId;
    }

    @Override
    public void setId(String newId) {
    }

    public void setMailGest(String mailGest) {
        this.mailGest = mailGest;
    }

    public void setPersref(String persref) {
        this.persref = persref;
    }

    public void setTypeDecision(String typeDecision) {
        this.typeDecision = typeDecision;
    }

    @Override
    public void update() throws Exception {
        throw new Exception("Action not implemented!");
    }

}
