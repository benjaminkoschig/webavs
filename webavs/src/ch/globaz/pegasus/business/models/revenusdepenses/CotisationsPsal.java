package ch.globaz.pegasus.business.models.revenusdepenses;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;

public class CotisationsPsal extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AdministrationComplexModel caisse = null;
    private SimpleCotisationsPsal simpleCotisationsPsal = null;

    /**
	 * 
	 */
    public CotisationsPsal() {
        super();
        simpleCotisationsPsal = new SimpleCotisationsPsal();
        caisse = new AdministrationComplexModel();
    }

    public AdministrationComplexModel getCaisse() {
        return caisse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleCotisationsPsal.getId();
    }

    /**
     * @return the simpleCotisationsPsal
     */
    public SimpleCotisationsPsal getSimpleCotisationsPsal() {
        return simpleCotisationsPsal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleCotisationsPsal.getSpy();
    }

    public void setCaisse(AdministrationComplexModel caisse) {
        this.caisse = caisse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleCotisationsPsal.setId(id);
    }

    @Override
    public void setIsNew() {
        // TODO Auto-generated method stub
        super.setIsNew();
        simpleCotisationsPsal.setId(null);
        simpleCotisationsPsal.setSpy(null);
    }

    /**
     * @param simpleCotisationsPsal
     *            the simpleCotisationsPsal to set
     */
    public void setSimpleCotisationsPsal(SimpleCotisationsPsal simpleCotisationsPsal) {
        this.simpleCotisationsPsal = simpleCotisationsPsal;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleCotisationsPsal.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleCotisationsPsal.setSpy(spy);
    }

}