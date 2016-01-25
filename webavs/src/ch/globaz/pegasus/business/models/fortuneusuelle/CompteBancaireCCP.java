package ch.globaz.pegasus.business.models.fortuneusuelle;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

public class CompteBancaireCCP extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleCompteBancaireCCP simpleCompteBancaireCCP = null;

    /**
	 * 
	 */
    public CompteBancaireCCP() {
        super();
        simpleCompteBancaireCCP = new SimpleCompteBancaireCCP();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleCompteBancaireCCP.getId();
    }

    /**
     * @return the simpleCompteBancaireCCP
     */
    public SimpleCompteBancaireCCP getSimpleCompteBancaireCCP() {
        return simpleCompteBancaireCCP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleCompteBancaireCCP.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleCompteBancaireCCP.setId(id);
    }

    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleCompteBancaireCCP.setId(null);
        simpleCompteBancaireCCP.setSpy(null);
    }

    /**
     * @param simpleCompteBancaireCCP
     *            the simpleCompteBancaireCCP to set
     */
    public void setSimpleCompteBancaireCCP(SimpleCompteBancaireCCP simpleCompteBancaireCCP) {
        this.simpleCompteBancaireCCP = simpleCompteBancaireCCP;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleCompteBancaireCCP.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleCompteBancaireCCP.setSpy(spy);
    }

}