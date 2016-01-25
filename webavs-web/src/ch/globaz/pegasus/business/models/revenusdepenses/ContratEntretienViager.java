package ch.globaz.pegasus.business.models.revenusdepenses;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

public class ContratEntretienViager extends AbstractDonneeFinanciereModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<SimpleLibelleContratEntretienViager> listLiebelleContratEntretienViagers = null;
    private SimpleContratEntretienViager simpleContratEntretienViager = null;

    /**
	 * 
	 */
    public ContratEntretienViager() {
        super();
        listLiebelleContratEntretienViagers = new ArrayList<SimpleLibelleContratEntretienViager>();
        simpleContratEntretienViager = new SimpleContratEntretienViager();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleContratEntretienViager.getId();
    }

    public List<SimpleLibelleContratEntretienViager> getListLiebelleContratEntretienViagers() {
        return listLiebelleContratEntretienViagers;
    }

    /**
     * @return the simpleContratEntretienViager
     */
    public SimpleContratEntretienViager getSimpleContratEntretienViager() {
        return simpleContratEntretienViager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleContratEntretienViager.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleContratEntretienViager.setId(id);
    }

    @Override
    public void setIsNew() {
        // TODO Auto-generated method stub
        super.setIsNew();
        simpleContratEntretienViager.setId(null);
        simpleContratEntretienViager.setSpy(null);
    }

    public void setListLiebelleContratEntretienViagers(
            List<SimpleLibelleContratEntretienViager> listLiebelleContratEntretienViagers) {
        this.listLiebelleContratEntretienViagers = listLiebelleContratEntretienViagers;
    }

    /**
     * @param simpleContratEntretienViager the simpleContratEntretienViager to set
     */
    public void setSimpleContratEntretienViager(SimpleContratEntretienViager simpleContratEntretienViager) {
        this.simpleContratEntretienViager = simpleContratEntretienViager;
    }

    /**
     * @param simpleDonneeFinanciereHeader the simpleDonneeFinanciereHeader to set
     */
    @Override
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader) {
        super.setSimpleDonneeFinanciereHeader(simpleDonneeFinanciereHeader);
        simpleContratEntretienViager.setIdDonneeFinanciereHeader(this.simpleDonneeFinanciereHeader.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleContratEntretienViager.setSpy(spy);
    }

}