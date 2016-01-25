package ch.globaz.perseus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;

public class AnnexeDecision extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleAnnexeDecision simpleAnnexeDecision = null;

    public AnnexeDecision() {
        super();
        simpleAnnexeDecision = new SimpleAnnexeDecision();
    }

    @Override
    public String getId() {
        return simpleAnnexeDecision.getId();
    }

    public SimpleAnnexeDecision getSimpleAnnexeDecision() {
        return simpleAnnexeDecision;
    }

    @Override
    public String getSpy() {
        return simpleAnnexeDecision.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleAnnexeDecision.setId(id);

    }

    public void setSimpleAnnexeDecision(SimpleAnnexeDecision simpleAnnexeDecision) {
        this.simpleAnnexeDecision = simpleAnnexeDecision;
    }

    @Override
    public void setSpy(String spy) {
        simpleAnnexeDecision.setSpy(spy);

    }

}
