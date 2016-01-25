package ch.globaz.pegasus.business.models.revenusdepenses;

import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class RevenuActiviteLucrativeIndependante extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AdministrationComplexModel caisse = null;
    private AffiliationSimpleModel simpleAffiliation = null;
    private SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante = null;
    private TiersSimpleModel tiersAffilie = null;

    /**
	 * 
	 */
    public RevenuActiviteLucrativeIndependante() {
        super();
        tiersAffilie = new TiersSimpleModel();
        caisse = new AdministrationComplexModel();
        simpleRevenuActiviteLucrativeIndependante = new SimpleRevenuActiviteLucrativeIndependante();
        simpleAffiliation = new AffiliationSimpleModel();
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
        return simpleRevenuActiviteLucrativeIndependante.getId();
    }

    /**
     * @return the simpleAffiliation
     */
    public AffiliationSimpleModel getSimpleAffiliation() {
        return simpleAffiliation;
    }

    /**
     * @return the simpleRevenuActiviteLucrativeIndependante
     */
    public SimpleRevenuActiviteLucrativeIndependante getSimpleRevenuActiviteLucrativeIndependante() {
        return simpleRevenuActiviteLucrativeIndependante;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleRevenuActiviteLucrativeIndependante.getSpy();
    }

    public TiersSimpleModel getTiersAffilie() {
        return tiersAffilie;
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
        simpleRevenuActiviteLucrativeIndependante.setId(id);
    }

    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleRevenuActiviteLucrativeIndependante.setId(null);
        simpleRevenuActiviteLucrativeIndependante.setSpy(null);
    }

    /**
     * @param simpleAffiliation
     *            the simpleAffiliation to set
     */
    public void setSimpleAffiliation(AffiliationSimpleModel simpleAffiliation) {
        this.simpleAffiliation = simpleAffiliation;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleRevenuActiviteLucrativeIndependante
                .setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /**
     * @param simpleRevenuActiviteLucrativeIndependante
     *            the simpleRevenuActiviteLucrativeIndependante to set
     */
    public void setSimpleRevenuActiviteLucrativeIndependante(
            SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante) {
        this.simpleRevenuActiviteLucrativeIndependante = simpleRevenuActiviteLucrativeIndependante;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleRevenuActiviteLucrativeIndependante.setSpy(spy);
    }

    public void setTiersAffilie(TiersSimpleModel tiersAffilie) {
        this.tiersAffilie = tiersAffilie;
    }

}