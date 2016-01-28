package ch.globaz.pegasus.business.models.fortuneusuelle;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;

public class CapitalLPP extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private AdministrationComplexModel caisse = null;

    private SimpleCapitalLPP simpleCapitalLPP = null;

    /**
	 * 
	 */
    public CapitalLPP() {
        super();
        simpleCapitalLPP = new SimpleCapitalLPP();
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
        return simpleCapitalLPP.getId();
    }

    /**
     * @return the simpleCapitalLPP
     */
    public SimpleCapitalLPP getSimpleCapitalLPP() {
        return simpleCapitalLPP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleCapitalLPP.getSpy();
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
        simpleCapitalLPP.setId(id);
    }

    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleCapitalLPP.setId(null);
        simpleCapitalLPP.setSpy(null);
    }

    /**
     * @param simpleCapitalLPP
     *            the simpleCapitalLPP to set
     */
    public void setSimpleCapitalLPP(SimpleCapitalLPP simpleCapitalLPP) {
        this.simpleCapitalLPP = simpleCapitalLPP;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleCapitalLPP.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleCapitalLPP.setSpy(spy);
    }

}