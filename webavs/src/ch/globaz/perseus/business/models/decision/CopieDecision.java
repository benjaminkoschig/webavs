package ch.globaz.perseus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * 
 * @author MBO
 * 
 */

public class CopieDecision extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String designation1 = null;
    private String designation2 = null;
    private SimpleCopieDecision simpleCopieDecision = null;

    public CopieDecision() {
        super();
        simpleCopieDecision = new SimpleCopieDecision();
    }

    public String getDesignation1() {
        return designation1;
    }

    public String getDesignation2() {
        return designation2;
    }

    @Override
    public String getId() {
        return simpleCopieDecision.getId();
    }

    public SimpleCopieDecision getSimpleCopieDecision() {
        return simpleCopieDecision;
    }

    @Override
    public String getSpy() {
        return simpleCopieDecision.getSpy();
    }

    public void setDesignation1(String designation1) {
        this.designation1 = designation1;
    }

    public void setDesignation2(String designation2) {
        this.designation2 = designation2;
    }

    @Override
    public void setId(String id) {
        simpleCopieDecision.setId(id);

    }

    public void setSimpleCopieDecision(SimpleCopieDecision simpleCopieDecision) {
        this.simpleCopieDecision = simpleCopieDecision;
    }

    @Override
    public void setSpy(String spy) {
        simpleCopieDecision.setSpy(spy);

    }

}
