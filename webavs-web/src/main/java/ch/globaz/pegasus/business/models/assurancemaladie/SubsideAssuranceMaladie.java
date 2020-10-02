package ch.globaz.pegasus.business.models.assurancemaladie;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

public class SubsideAssuranceMaladie extends AbstractDonneeFinanciereModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie = null;

    /**
	 *
	 */
    public SubsideAssuranceMaladie() {
        super();
        simpleSubsideAssuranceMaladie = new SimpleSubsideAssuranceMaladie();
    }


    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleSubsideAssuranceMaladie.getId();
    }

    /**
     * @return the simpleCotisationsPsal
     */
    public SimpleSubsideAssuranceMaladie getSimpleSubsideAssuranceMaladie() {
        return simpleSubsideAssuranceMaladie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleSubsideAssuranceMaladie.getSpy();
    }


    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleSubsideAssuranceMaladie.setId(id);
    }

    @Override
    public void setIsNew() {
        // TODO Auto-generated method stub
        super.setIsNew();
        simpleSubsideAssuranceMaladie.setId(null);
        simpleSubsideAssuranceMaladie.setSpy(null);
    }

    /**
     * @param simpleSubsideAssuranceMaladie
     *            the simpleCotisationsPsal to set
     */
    public void setSimpleSubsideAssuranceMaladie(SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie) {
        this.simpleSubsideAssuranceMaladie = simpleSubsideAssuranceMaladie;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleSubsideAssuranceMaladie.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleSubsideAssuranceMaladie.setSpy(spy);
    }

}
