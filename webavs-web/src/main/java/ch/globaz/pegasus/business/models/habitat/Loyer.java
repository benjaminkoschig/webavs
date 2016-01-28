package ch.globaz.pegasus.business.models.habitat;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class Loyer extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleLoyer simpleLoyer = null;
    private TiersSimpleModel tiersBailleurRegie = null;

    public Loyer() {
        super();
        simpleLoyer = new SimpleLoyer();
        tiersBailleurRegie = new TiersSimpleModel();
    }

    @Override
    public String getId() {
        return simpleLoyer.getId();
    }

    /**
     * @return the simpleLoyer
     */
    public SimpleLoyer getSimpleLoyer() {
        return simpleLoyer;
    }

    @Override
    public String getSpy() {
        return simpleLoyer.getSpy();
    }

    /**
     * @return the tiersBailleurRegie
     */
    public TiersSimpleModel getTiersBailleurRegie() {
        return tiersBailleurRegie;
    }

    @Override
    public void setId(String id) {
        simpleLoyer.setId(id);
    }

    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader donneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(donneeFinanciereHeader);
        simpleLoyer.setIdDonneeFinanciereHeader(donneeFinanciereHeader.getId());
    }

    /**
     * @param simpleLoyer
     *            the simpleLoyer to set
     */
    public void setSimpleLoyer(SimpleLoyer simpleLoyer) {
        this.simpleLoyer = simpleLoyer;
    }

    @Override
    public void setSpy(String spy) {
        simpleLoyer.setSpy(spy);
    }

    /**
     * @param tiersBailleurRegie
     *            the tiersBailleurRegie to set
     */
    public void setTiersBailleurRegie(TiersSimpleModel tiersBailleurRegie) {
        this.tiersBailleurRegie = tiersBailleurRegie;
    }

}
