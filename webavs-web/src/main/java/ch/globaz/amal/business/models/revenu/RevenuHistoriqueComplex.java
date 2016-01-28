/**
 * 
 */
package ch.globaz.amal.business.models.revenu;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author dhi
 * 
 */
public class RevenuHistoriqueComplex extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private RevenuFullComplex revenuFullComplex = null;

    private SimpleRevenuDeterminant simpleRevenuDeterminant = null;

    private SimpleRevenuHistorique simpleRevenuHistorique = null;

    /**
     * Default constructor
     */
    public RevenuHistoriqueComplex() {
        super();
        revenuFullComplex = new RevenuFullComplex();
        simpleRevenuDeterminant = new SimpleRevenuDeterminant();
        simpleRevenuHistorique = new SimpleRevenuHistorique();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleRevenuHistorique.getId();
    }

    /**
     * @return the revenuFullComplex
     */
    public RevenuFullComplex getRevenuFullComplex() {
        return revenuFullComplex;
    }

    /**
     * @return the simpleRevenuDeterminant
     */
    public SimpleRevenuDeterminant getSimpleRevenuDeterminant() {
        return simpleRevenuDeterminant;
    }

    /**
     * @return the simpleRevenuHistorique
     */
    public SimpleRevenuHistorique getSimpleRevenuHistorique() {
        return simpleRevenuHistorique;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleRevenuHistorique.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleRevenuHistorique.setId(id);
    }

    /**
     * @param revenuFullComplex
     *            the revenuFullComplex to set
     */
    public void setRevenuFullComplex(RevenuFullComplex revenuFullComplex) {
        this.revenuFullComplex = revenuFullComplex;
    }

    /**
     * @param simpleRevenuDeterminant
     *            the simpleRevenuDeterminant to set
     */
    public void setSimpleRevenuDeterminant(SimpleRevenuDeterminant simpleRevenuDeterminant) {
        this.simpleRevenuDeterminant = simpleRevenuDeterminant;
    }

    /**
     * @param simpleRevenuHistorique
     *            the simpleRevenuHistorique to set
     */
    public void setSimpleRevenuHistorique(SimpleRevenuHistorique simpleRevenuHistorique) {
        this.simpleRevenuHistorique = simpleRevenuHistorique;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleRevenuHistorique.setSpy(spy);
    }

}
