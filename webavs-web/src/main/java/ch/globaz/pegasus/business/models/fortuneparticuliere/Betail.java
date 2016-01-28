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
public class Betail extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleBetail simpleBetail = null;

    /**
	 * 
	 */
    public Betail() {
        super();
        simpleBetail = new SimpleBetail();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleBetail.getId();
    }

    /**
     * @return the simpleBetail
     */
    public SimpleBetail getSimpleBetail() {
        return simpleBetail;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleBetail.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleBetail.setId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel #setIsNew()
     */
    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleBetail.setId(null);
        simpleBetail.setSpy(null);
    }

    /**
     * @param simpleBetail
     *            the simpleBetail to set
     */
    public void setSimpleBetail(SimpleBetail simpleBetail) {
        this.simpleBetail = simpleBetail;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleBetail.setIdDonneeFinanciereHeader(simpleDonneeFinanciereHeader.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleBetail.setSpy(spy);
    }

}
