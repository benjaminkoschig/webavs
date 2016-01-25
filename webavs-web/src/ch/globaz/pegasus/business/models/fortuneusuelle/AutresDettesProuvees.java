package ch.globaz.pegasus.business.models.fortuneusuelle;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

public class AutresDettesProuvees extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleAutresDettesProuvees simpleAutresDettesProuvees = null;

    /**
	 * 
	 */
    public AutresDettesProuvees() {
        super();
        simpleAutresDettesProuvees = new SimpleAutresDettesProuvees();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleAutresDettesProuvees.getId();
    }

    /**
     * @return the simpleAutresDettesProuvees
     */
    public SimpleAutresDettesProuvees getSimpleAutresDettesProuvees() {
        return simpleAutresDettesProuvees;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleAutresDettesProuvees.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleAutresDettesProuvees.setId(id);
    }

    @Override
    public void setIsNew() {
        super.setIsNew();
        simpleAutresDettesProuvees.setId(null);
        simpleAutresDettesProuvees.setSpy(null);
    }

    /**
     * @param simpleAutresDettesProuvees
     *            the simpleAutresDettesProuvees to set
     */
    public void setSimpleAutresDettesProuvees(SimpleAutresDettesProuvees simpleAutresDettesProuvees) {
        this.simpleAutresDettesProuvees = simpleAutresDettesProuvees;
    }

    /**
     * @param simpleDonneeFinanciereHeader
     *            the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleAutresDettesProuvees.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleAutresDettesProuvees.setSpy(spy);
    }

}