/**
 * 
 */
package ch.globaz.pegasus.business.models.fortuneparticuliere;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;

/**
 * @author BSC
 * 
 */
public class AssuranceRenteViagere extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private AdministrationComplexModel compagnie = null;

    private SimpleAssuranceRenteViagere simpleAssuranceRenteViagere = null;

    /**
	 * 
	 */
    public AssuranceRenteViagere() {
        super();
        simpleAssuranceRenteViagere = new SimpleAssuranceRenteViagere();
        compagnie = new AdministrationComplexModel();
    }

    /**
     * @return the compagnie
     */
    public AdministrationComplexModel getCompagnie() {
        return compagnie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleAssuranceRenteViagere.getId();
    }

    /**
     * @return the simpleAssuranceRenteViagere
     */
    public SimpleAssuranceRenteViagere getSimpleAssuranceRenteViagere() {
        return simpleAssuranceRenteViagere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleAssuranceRenteViagere.getSpy();
    }

    /**
     * @param compagnie
     *            the compagnie to set
     */
    public void setCompagnie(AdministrationComplexModel compagnie) {
        this.compagnie = compagnie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleAssuranceRenteViagere.setId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel #setIsNew()
     */
    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleAssuranceRenteViagere.setId(null);
        simpleAssuranceRenteViagere.setSpy(null);
    }

    /**
     * @param simpleAssuranceRenteViagere
     *            the simpleAssuranceRenteViagere to set
     */
    public void setSimpleAssuranceRenteViagere(SimpleAssuranceRenteViagere simpleAssuranceRenteViagere) {
        this.simpleAssuranceRenteViagere = simpleAssuranceRenteViagere;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleAssuranceRenteViagere.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleAssuranceRenteViagere.setSpy(spy);
    }

}
