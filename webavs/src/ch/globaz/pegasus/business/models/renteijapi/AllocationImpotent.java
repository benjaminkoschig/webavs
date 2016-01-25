package ch.globaz.pegasus.business.models.renteijapi;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

/**
 * Modele complexe composé de allocation impotent et de donnee financiere header
 * 
 * @author SCE
 * 
 */
public class AllocationImpotent extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // allocation impotent
    private SimpleAllocationImpotent simpleAllocationImpotent = null;

    public AllocationImpotent() {
        super();
        simpleAllocationImpotent = new SimpleAllocationImpotent();
    }

    /**
     * Return the id of the complex model
     */
    @Override
    public String getId() {
        return simpleAllocationImpotent.getId();
    }

    /**
     * @return the simpleAllocationImpotent
     */
    public SimpleAllocationImpotent getSimpleAllocationImpotent() {
        return simpleAllocationImpotent;
    }

    /**
     * Return the spy of the root model
     */
    @Override
    public String getSpy() {
        return simpleAllocationImpotent.getSpy();
    }

    /**
     * Set the id of the root model
     */
    @Override
    public void setId(String id) {
        simpleAllocationImpotent.setId(id);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel #setIsNew()
     */
    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleAllocationImpotent.setId(null);
        simpleAllocationImpotent.setSpy(null);
    }

    /**
     * @param simpleAllocationImpotent
     *            the simpleAllocationImpotent to set
     */
    public void setSimpleAllocationImpotent(SimpleAllocationImpotent simpleAllocationImpotent) {
        this.simpleAllocationImpotent = simpleAllocationImpotent;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleAllocationImpotent.setIdDonneeFinanciereHeader(simpleDonneeFinanciereHeader.getId());
    }

    /**
     * Set the spy of the root model
     */
    @Override
    public void setSpy(String spy) {
        simpleAllocationImpotent.setSpy(spy);

    }

}
