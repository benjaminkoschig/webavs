package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;

public class DecisionDate extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDecision = null;
    private String idDecisionHeader = null;

    public String getDateDecision() {
        return dateDecision;
    }

    @Override
    public String getId() {
        return idDecisionHeader;
    }

    public String getIdDecisionHeader() {
        return idDecisionHeader;
    }

    @Override
    public String getSpy() {
        return null;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    @Override
    public void setId(String id) {
        idDecisionHeader = id;
    }

    public void setIdDecisionHeader(String idDecisionHeader) {
        this.idDecisionHeader = idDecisionHeader;
    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub
    }
}
