package ch.globaz.pegasus.business.models.assurancemaladie;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

public class PrimeAssuranceMaladie extends AbstractDonneeFinanciereModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie = null;

    /**
	 *
	 */
    public PrimeAssuranceMaladie() {
        super();
        simplePrimeAssuranceMaladie = new SimplePrimeAssuranceMaladie();
    }


    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simplePrimeAssuranceMaladie.getId();
    }

    /**
     * @return the simpleCotisationsPsal
     */
    public SimplePrimeAssuranceMaladie getSimplePrimeAssuranceMaladie() {
        return simplePrimeAssuranceMaladie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simplePrimeAssuranceMaladie.getSpy();
    }


    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simplePrimeAssuranceMaladie.setId(id);
    }

    @Override
    public void setIsNew() {
        // TODO Auto-generated method stub
        super.setIsNew();
        simplePrimeAssuranceMaladie.setId(null);
        simplePrimeAssuranceMaladie.setSpy(null);
    }

    /**
     * @param simplePrimeAssuranceMaladie
     *            the simpleCotisationsPsal to set
     */
    public void setSimplePrimeAssuranceMaladie(SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie) {
        this.simplePrimeAssuranceMaladie = simplePrimeAssuranceMaladie;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simplePrimeAssuranceMaladie.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simplePrimeAssuranceMaladie.setSpy(spy);
    }

}
