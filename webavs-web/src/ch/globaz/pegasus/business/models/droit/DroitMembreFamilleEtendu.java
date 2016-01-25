/**
 * 
 */
package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author BSC
 * 
 */
public class DroitMembreFamilleEtendu extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DroitMembreFamille droitMembreFamille = null;
    private SimpleDroit simpleDroit = null;

    /**
	 * 
	 */
    public DroitMembreFamilleEtendu() {
        super();

        simpleDroit = new SimpleDroit();
        droitMembreFamille = new DroitMembreFamille();
    }

    /**
     * @return the droitMembreFamille
     */
    public DroitMembreFamille getDroitMembreFamille() {
        return droitMembreFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return droitMembreFamille.getId();
    }

    /**
     * @return the simpleDroit
     */
    public SimpleDroit getSimpleDroit() {
        return simpleDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return droitMembreFamille.getSpy();
    }

    /**
     * @param droitMembreFamille
     *            the droitMembreFamille to set
     */
    public void setDroitMembreFamille(DroitMembreFamille droitMembreFamille) {
        this.droitMembreFamille = droitMembreFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        droitMembreFamille.setId(id);
    }

    /**
     * @param simpleDroit
     *            the simpleDroit to set
     */
    public void setSimpleDroit(SimpleDroit simpleDroit) {
        this.simpleDroit = simpleDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        droitMembreFamille.setSpy(spy);
    }

}
