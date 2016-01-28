package ch.globaz.pegasus.business.models.revenusdepenses;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class PensionAlimentaire extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private SimplePensionAlimentaire simplePensionAlimentaire = null;

    private TiersSimpleModel tiers = null;

    /**
	 * 
	 */
    public PensionAlimentaire() {
        super();
        simplePensionAlimentaire = new SimplePensionAlimentaire();
        tiers = new TiersSimpleModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simplePensionAlimentaire.getId();
    }

    /**
     * @return the simplePensionAlimentaire
     */
    public SimplePensionAlimentaire getSimplePensionAlimentaire() {
        return simplePensionAlimentaire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simplePensionAlimentaire.getSpy();
    }

    public TiersSimpleModel getTiers() {
        return tiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simplePensionAlimentaire.setId(id);
    }

    @Override
    public void setIsNew() {
        // TODO Auto-generated method stub
        super.setIsNew();
        simplePensionAlimentaire.setId(null);
        simplePensionAlimentaire.setSpy(null);
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simplePensionAlimentaire.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /**
     * @param simplePensionAlimentaire
     *            the simplePensionAlimentaire to set
     */
    public void setSimplePensionAlimentaire(SimplePensionAlimentaire simplePensionAlimentaire) {
        this.simplePensionAlimentaire = simplePensionAlimentaire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simplePensionAlimentaire.setSpy(spy);
    }

    public void setTiers(TiersSimpleModel tiers) {
        this.tiers = tiers;
    }

}