/**
 * 
 */
package ch.globaz.perseus.business.models.retenue;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;

/**
 * @author dde
 * 
 */
public class Retenue extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private PCFAccordee pcfAccordee = null;
    private SimpleRetenue simpleRetenue = null;

    public Retenue() {
        pcfAccordee = new PCFAccordee();
        simpleRetenue = new SimpleRetenue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleRetenue.getId();
    }

    /**
     * @return the pcfAccordee
     */
    public PCFAccordee getPcfAccordee() {
        return pcfAccordee;
    }

    /**
     * @return the simpleRetenue
     */
    public SimpleRetenue getSimpleRetenue() {
        return simpleRetenue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleRetenue.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleRetenue.setId(id);
    }

    /**
     * @param pcfAccordee
     *            the pcfAccordee to set
     */
    public void setPcfAccordee(PCFAccordee pcfAccordee) {
        this.pcfAccordee = pcfAccordee;
    }

    /**
     * @param simpleRetenue
     *            the simpleRetenue to set
     */
    public void setSimpleRetenue(SimpleRetenue simpleRetenue) {
        this.simpleRetenue = simpleRetenue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleRetenue.setSpy(spy);
    }

}
