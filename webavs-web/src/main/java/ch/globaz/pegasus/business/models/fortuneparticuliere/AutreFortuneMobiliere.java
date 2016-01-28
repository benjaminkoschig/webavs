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
public class AutreFortuneMobiliere extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere = null;

    /**
	 * 
	 */
    public AutreFortuneMobiliere() {
        super();
        simpleAutreFortuneMobiliere = new SimpleAutreFortuneMobiliere();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleAutreFortuneMobiliere.getId();
    }

    /**
     * @return the simpleAutreFortuneMobiliere
     */
    public SimpleAutreFortuneMobiliere getSimpleAutreFortuneMobiliere() {
        return simpleAutreFortuneMobiliere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleAutreFortuneMobiliere.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleAutreFortuneMobiliere.setId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel #setIsNew()
     */
    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleAutreFortuneMobiliere.setId(null);
        simpleAutreFortuneMobiliere.setSpy(null);
    }

    /**
     * @param simpleAutreFortuneMobiliere
     *            the simpleAutreFortuneMobiliere to set
     */
    public void setSimpleAutreFortuneMobiliere(SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere) {
        this.simpleAutreFortuneMobiliere = simpleAutreFortuneMobiliere;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleAutreFortuneMobiliere.setIdDonneeFinanciereHeader(simpleDonneeFinanciereHeader.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleAutreFortuneMobiliere.setSpy(spy);
    }

}
