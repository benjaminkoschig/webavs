/**
 * 
 */
package ch.globaz.pegasus.business.models.fortuneparticuliere;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

/**
 * @author BSC
 * 
 */
public class PretEnversTiers extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    SimplePretEnversTiers simplePretEnversTiers = null;

    /**
	 * 
	 */
    public PretEnversTiers() {
        super();
        simplePretEnversTiers = new SimplePretEnversTiers();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simplePretEnversTiers.getId();
    }

    /**
     * @return the simplePretEnversTiers
     */
    public SimplePretEnversTiers getSimplePretEnversTiers() {
        return simplePretEnversTiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simplePretEnversTiers.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simplePretEnversTiers.setId(id);
    }

    /**
     * configure l'entité pret envers tiers, si existante, comme une nouvelle entité
     */
    @Override
    public void setIsNew() {
        super.setIsNew();
        simplePretEnversTiers.setId(null);
        simplePretEnversTiers.setSpy(null);
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simplePretEnversTiers.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /**
     * @param simplePretEnversTiers
     *            the simplePretEnversTiers to set
     */
    public void setSimplePretEnversTiers(SimplePretEnversTiers simplePretEnversTiers) {
        this.simplePretEnversTiers = simplePretEnversTiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simplePretEnversTiers.setSpy(spy);
    }

}
