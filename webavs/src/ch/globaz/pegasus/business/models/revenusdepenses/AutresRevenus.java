package ch.globaz.pegasus.business.models.revenusdepenses;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

public class AutresRevenus extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleAutresRevenus simpleAutresRevenus = null;

    /**
	 * 
	 */
    public AutresRevenus() {
        super();
        simpleAutresRevenus = new SimpleAutresRevenus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleAutresRevenus.getId();
    }

    /**
     * @return the simpleAutresRevenus
     */
    public SimpleAutresRevenus getSimpleAutresRevenus() {
        return simpleAutresRevenus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleAutresRevenus.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleAutresRevenus.setId(id);
    }

    @Override
    public void setIsNew() {
        // TODO Auto-generated method stub
        super.setIsNew();
        simpleAutresRevenus.setId(null);
        simpleAutresRevenus.setSpy(null);
    }

    /**
     * @param simpleAutresRevenus
     *            the simpleAutresRevenus to set
     */
    public void setSimpleAutresRevenus(SimpleAutresRevenus simpleAutresRevenus) {
        this.simpleAutresRevenus = simpleAutresRevenus;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleAutresRevenus.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleAutresRevenus.setSpy(spy);
    }

}