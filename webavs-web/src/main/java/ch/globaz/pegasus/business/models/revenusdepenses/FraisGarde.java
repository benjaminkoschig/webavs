package ch.globaz.pegasus.business.models.revenusdepenses;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;

public class FraisGarde extends AbstractDonneeFinanciereModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private SimpleFraisGarde simpleFraisGarde = null;

    /**
	 *
	 */
    public FraisGarde() {
        super();
        simpleFraisGarde = new SimpleFraisGarde();
    }


    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleFraisGarde.getId();
    }

    /**
     * @return the simpleCotisationsPsal
     */
    public SimpleFraisGarde getSimpleFraisGarde() {
        return simpleFraisGarde;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleFraisGarde.getSpy();
    }


    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleFraisGarde.setId(id);
    }

    @Override
    public void setIsNew() {
        // TODO Auto-generated method stub
        super.setIsNew();
        simpleFraisGarde.setId(null);
        simpleFraisGarde.setSpy(null);
    }

    /**
     * @param simpleCotisationsPsal
     *            the simpleCotisationsPsal to set
     */
    public void setSimpleFraisGarde(SimpleFraisGarde simplesimpleFraisGarde) {
        this.simpleFraisGarde = simplesimpleFraisGarde;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleFraisGarde.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleFraisGarde.setSpy(spy);
    }

}