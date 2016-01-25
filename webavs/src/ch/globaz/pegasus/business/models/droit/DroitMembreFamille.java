/**
 * 
 */
package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.hera.business.models.famille.MembreFamille;

/**
 * @author BSC
 * 
 */
public class DroitMembreFamille extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private MembreFamille membreFamille = null;
    private SimpleDroitMembreFamille simpleDroitMembreFamille = null;

    /**
	 * 
	 */
    public DroitMembreFamille() {
        super();

        membreFamille = new MembreFamille();
        simpleDroitMembreFamille = new SimpleDroitMembreFamille();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleDroitMembreFamille.getId();
    }

    /**
     * @return the membreFamille
     */
    public MembreFamille getMembreFamille() {
        return membreFamille;
    }

    /**
     * @return the simpleDroitMembreFamille
     */
    public SimpleDroitMembreFamille getSimpleDroitMembreFamille() {
        return simpleDroitMembreFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleDroitMembreFamille.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleDroitMembreFamille.setId(id);
    }

    /**
     * @param membreFamille
     *            the membreFamille to set
     */
    public void setMembreFamille(MembreFamille membreFamille) {
        this.membreFamille = membreFamille;
    }

    /**
     * @param simpleDroitMembreFamille
     *            the simpleDroitMembreFamille to set
     */
    public void setSimpleDroitMembreFamille(SimpleDroitMembreFamille simpleDroitMembreFamille) {
        this.simpleDroitMembreFamille = simpleDroitMembreFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleDroitMembreFamille.setSpy(spy);
    }

}
