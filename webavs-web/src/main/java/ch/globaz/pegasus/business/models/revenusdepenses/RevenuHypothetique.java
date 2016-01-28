package ch.globaz.pegasus.business.models.revenusdepenses;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

public class RevenuHypothetique extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleRevenuHypothetique simpleRevenuHypothetique = null;

    /**
	 * 
	 */
    public RevenuHypothetique() {
        super();
        simpleRevenuHypothetique = new SimpleRevenuHypothetique();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleRevenuHypothetique.getId();
    }

    /**
     * @return the simpleRevenuHypothetique
     */
    public SimpleRevenuHypothetique getSimpleRevenuHypothetique() {
        return simpleRevenuHypothetique;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleRevenuHypothetique.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleRevenuHypothetique.setId(id);
    }

    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleRevenuHypothetique.setId(null);
        simpleRevenuHypothetique.setSpy(null);
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleRevenuHypothetique.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /**
     * @param simpleRevenuHypothetique
     *            the simpleRevenuHypothetique to set
     */
    public void setSimpleRevenuHypothetique(SimpleRevenuHypothetique simpleRevenuHypothetique) {
        this.simpleRevenuHypothetique = simpleRevenuHypothetique;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleRevenuHypothetique.setSpy(spy);
    }

}