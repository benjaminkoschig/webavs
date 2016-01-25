package ch.globaz.perseus.business.models.lot;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * 
 * @author MBO
 * 
 */
public class OrdreVersement extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleOrdreVersement simpleOrdreVersement = null;
    private SimplePrestation simplePrestation = null;
    private TiersSimpleModel tiers = null;

    public OrdreVersement() {
        super();
        simpleOrdreVersement = new SimpleOrdreVersement();
        tiers = new TiersSimpleModel();
        simplePrestation = new SimplePrestation();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleOrdreVersement.getIdOrdreVersement();
    }

    public SimpleOrdreVersement getSimpleOrdreVersement() {
        return simpleOrdreVersement;
    }

    public SimplePrestation getSimplePrestation() {
        return simplePrestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleOrdreVersement.getSpy();
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
        simpleOrdreVersement.setIdOrdreVersement(id);

    }

    public void setSimpleOrdreVersement(SimpleOrdreVersement simpleOrdreVersement) {
        this.simpleOrdreVersement = simpleOrdreVersement;
    }

    public void setSimplePrestation(SimplePrestation simplePrestation) {
        this.simplePrestation = simplePrestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleOrdreVersement.setSpy(spy);
    }

    public void setTiers(TiersSimpleModel tiersBeneficiaire) {
        tiers = tiersBeneficiaire;
    }

}
