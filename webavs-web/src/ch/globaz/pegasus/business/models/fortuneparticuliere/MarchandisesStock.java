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
public class MarchandisesStock extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleMarchandisesStock simpleMarchandisesStock = null;

    /**
	 * 
	 */
    public MarchandisesStock() {
        super();
        simpleMarchandisesStock = new SimpleMarchandisesStock();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleMarchandisesStock.getId();
    }

    /**
     * @return the simpleMarchandisesStock
     */
    public SimpleMarchandisesStock getSimpleMarchandisesStock() {
        return simpleMarchandisesStock;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleMarchandisesStock.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleMarchandisesStock.setId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel #setIsNew()
     */
    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleMarchandisesStock.setId(null);
        simpleMarchandisesStock.setSpy(null);
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleMarchandisesStock.setIdDonneeFinanciereHeader(simpleDonneeFinanciereHeader.getId());
    }

    /**
     * @param simpleMarchandisesStock
     *            the simpleMarchandisesStock to set
     */
    public void setSimpleMarchandisesStock(SimpleMarchandisesStock simpleMarchandisesStock) {
        this.simpleMarchandisesStock = simpleMarchandisesStock;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleMarchandisesStock.setSpy(spy);
    }

}
