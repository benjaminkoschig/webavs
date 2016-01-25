/**
 * 
 */
package ch.globaz.pegasus.business.models.fortuneparticuliere;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

/**
 * @author BSC
 * 
 */
public class Vehicule extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleVehicule simpleVehicule = null;

    /**
	 * 
	 */
    public Vehicule() {
        super();
        simpleVehicule = new SimpleVehicule();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleVehicule.getId();
    }

    /**
     * @return the simpleVehicule
     */
    public SimpleVehicule getSimpleVehicule() {
        return simpleVehicule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleVehicule.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleVehicule.setId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel #setIsNew()
     */
    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleVehicule.setId(null);
        simpleVehicule.setSpy(null);
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleVehicule.setIdDonneeFinanciereHeader(simpleDonneeFinanciereHeader.getId());
    }

    /**
     * @param simpleVehicule
     *            the simpleVehicule to set
     */
    public void setSimpleVehicule(SimpleVehicule simpleVehicule) {
        this.simpleVehicule = simpleVehicule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleVehicule.setSpy(spy);
    }

}
