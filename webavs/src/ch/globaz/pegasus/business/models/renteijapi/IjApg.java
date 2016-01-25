package ch.globaz.pegasus.business.models.renteijapi;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class IjApg extends AbstractDonneeFinanciereModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleIjApg simpleIjApg = null;
    private TiersSimpleModel tiersFournisseurPrestation = null;

    public IjApg() {
        super();
        simpleIjApg = new SimpleIjApg();
        tiersFournisseurPrestation = new TiersSimpleModel();
    }

    @Override
    public String getId() {
        return simpleIjApg.getId();
    }

    /**
     * @return the simpleIjApg
     */
    public SimpleIjApg getSimpleIjApg() {
        return simpleIjApg;
    }

    @Override
    public String getSpy() {
        return simpleIjApg.getSpy();
    }

    /**
     * @return the tiersFournisseurPrestation
     */
    public TiersSimpleModel getTiersFournisseurPrestation() {
        return tiersFournisseurPrestation;
    }

    @Override
    public void setId(String id) {
        simpleIjApg.setId(id);

    }

    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader donneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(donneeFinanciereHeader);
        simpleIjApg.setIdDonneeFinanciereHeader(donneeFinanciereHeader.getId());
    }

    /**
     * @param simpleIjApg
     *            the simpleIjApg to set
     */
    public void setSimpleIjApg(SimpleIjApg simpleIjApg) {
        this.simpleIjApg = simpleIjApg;
    }

    @Override
    public void setSpy(String spy) {
        simpleIjApg.setSpy(spy);
    }

    /**
     * @param tiersFournisseurPrestation
     *            the tiersFournisseurPrestation to set
     */
    public void setTiersFournisseurPrestation(TiersSimpleModel tiersFournisseurPrestation) {
        this.tiersFournisseurPrestation = tiersFournisseurPrestation;
    }

}
