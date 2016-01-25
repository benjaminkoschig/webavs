/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author SCE
 * 
 *         7 oct. 2010
 */
public class CopiesDecision extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String designation1 = null;
    private String designation2 = null;
    private SimpleCopiesDecision simpleCopiesDecision = null;

    public CopiesDecision() {
        super();
        simpleCopiesDecision = new SimpleCopiesDecision();
    }

    /**
     * @return the designation1
     */
    public String getDesignation1() {
        return designation1;
    }

    /**
     * @return the designation2
     */
    public String getDesignation2() {
        return designation2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleCopiesDecision.getId();
    }

    /**
     * @return the simpleCopiesDecision
     */
    public SimpleCopiesDecision getSimpleCopiesDecision() {
        return simpleCopiesDecision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleCopiesDecision.getSpy();
    }

    /**
     * @param designation1
     *            the designation1 to set
     */
    public void setDesignation1(String designation1) {
        this.designation1 = designation1;
    }

    /**
     * @param designation2
     *            the designation2 to set
     */
    public void setDesignation2(String designation2) {
        this.designation2 = designation2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleCopiesDecision.setId(id);

    }

    /**
     * @param simpleCopiesDecision
     *            the simpleCopiesDecision to set
     */
    public void setSimpleCopiesDecision(SimpleCopiesDecision simpleCopiesDecision) {
        this.simpleCopiesDecision = simpleCopiesDecision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleCopiesDecision.setSpy(spy);

    }

}
