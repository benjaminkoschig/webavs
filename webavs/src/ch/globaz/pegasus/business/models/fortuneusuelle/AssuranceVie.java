package ch.globaz.pegasus.business.models.fortuneusuelle;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class AssuranceVie extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // private AdministrationComplexModel caisse = null;
    private TiersSimpleModel tiersCompagnie = null;
    private SimpleAssuranceVie simpleAssuranceVie = null;

    /**
	 * 
	 */
    public AssuranceVie() {
        super();
        simpleAssuranceVie = new SimpleAssuranceVie();
        // this.caisse = new AdministrationComplexModel();
        tiersCompagnie = new TiersSimpleModel();
    }

    // public AdministrationComplexModel getCaisse() {
    // return this.caisse;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleAssuranceVie.getId();
    }

    /**
     * @return the simpleAssuranceVie
     */
    public SimpleAssuranceVie getSimpleAssuranceVie() {
        return simpleAssuranceVie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleAssuranceVie.getSpy();
    }

    // public void setCaisse(AdministrationComplexModel caisse) {
    // this.caisse = caisse;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleAssuranceVie.setId(id);
    }

    public TiersSimpleModel getTiersCompagnie() {
        return tiersCompagnie;
    }

    public void setTiersCompagnie(TiersSimpleModel tiersCompagnie) {
        this.tiersCompagnie = tiersCompagnie;
    }

    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleAssuranceVie.setId(null);
        simpleAssuranceVie.setSpy(null);
    }

    /**
     * @param simpleAssuranceVie
     *            the simpleAssuranceVie to set
     */
    public void setSimpleAssuranceVie(SimpleAssuranceVie simpleAssuranceVie) {
        this.simpleAssuranceVie = simpleAssuranceVie;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleAssuranceVie.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleAssuranceVie.setSpy(spy);
    }

}