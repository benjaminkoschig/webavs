/**
 * 
 */
package ch.globaz.amal.business.models.reprise;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author DHI
 * 
 */
public class ContribuableReprise extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    SimpleContribuableInfoReprise simpleContribuableInfoReprise = null;

    SimpleContribuableReprise simpleContribuableReprise = null;

    /**
	 * 
	 */
    public ContribuableReprise() {
        super();
        simpleContribuableInfoReprise = new SimpleContribuableInfoReprise();
        simpleContribuableReprise = new SimpleContribuableReprise();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleContribuableReprise.getId();
    }

    /**
     * @return the simpleContribuableInfoReprise
     */
    public SimpleContribuableInfoReprise getSimpleContribuableInfoReprise() {
        return simpleContribuableInfoReprise;
    }

    /**
     * @return the simpleContribuableReprise
     */
    public SimpleContribuableReprise getSimpleContribuableReprise() {
        return simpleContribuableReprise;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleContribuableReprise.setId(id);
    }

    /**
     * @param simpleContribuableInfoReprise
     *            the simpleContribuableInfoReprise to set
     */
    public void setSimpleContribuableInfoReprise(SimpleContribuableInfoReprise simpleContribuableInfoReprise) {
        this.simpleContribuableInfoReprise = simpleContribuableInfoReprise;
    }

    /**
     * @param simpleContribuableReprise
     *            the simpleContribuableReprise to set
     */
    public void setSimpleContribuableReprise(SimpleContribuableReprise simpleContribuableReprise) {
        this.simpleContribuableReprise = simpleContribuableReprise;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
    }

}
