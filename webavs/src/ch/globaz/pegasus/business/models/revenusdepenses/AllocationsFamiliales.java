package ch.globaz.pegasus.business.models.revenusdepenses;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;

public class AllocationsFamiliales extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AdministrationComplexModel caisse = null;
    private SimpleAllocationsFamiliales simpleAllocationsFamiliales = null;

    /**
	 * 
	 */
    public AllocationsFamiliales() {
        super();
        simpleAllocationsFamiliales = new SimpleAllocationsFamiliales();
        caisse = new AdministrationComplexModel();
    }

    public AdministrationComplexModel getCaisse() {
        return caisse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleAllocationsFamiliales.getId();
    }

    /**
     * @return the simpleAllocationsFamiliales
     */
    public SimpleAllocationsFamiliales getSimpleAllocationsFamiliales() {
        return simpleAllocationsFamiliales;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleAllocationsFamiliales.getSpy();
    }

    public void setCaisse(AdministrationComplexModel caisse) {
        this.caisse = caisse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleAllocationsFamiliales.setId(id);
    }

    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleAllocationsFamiliales.setId(null);
        simpleAllocationsFamiliales.setSpy(null);
    }

    /**
     * @param simpleAllocationsFamiliales
     *            the simpleAllocationsFamiliales to set
     */
    public void setSimpleAllocationsFamiliales(SimpleAllocationsFamiliales simpleAllocationsFamiliales) {
        this.simpleAllocationsFamiliales = simpleAllocationsFamiliales;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleAllocationsFamiliales.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleAllocationsFamiliales.setSpy(spy);
    }

}
