package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeComplexModel;

/*
 * DroitDonneeFinanciere est un modèle servant pour la modification d'un droit dans les écrans AJAX de données
 * financières
 */

public class ModificateurDroitDonneeFinanciere extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDroit simpleDroit = null;
    private transient SimpleDroitMembreFamille simpleDroitMembreFamille = null;
    private SimpleVersionDroit simpleVersionDroit = null;

    /**
	 * 
	 */
    public ModificateurDroitDonneeFinanciere() {
        super();
        simpleDroit = new SimpleDroit();
        simpleDroitMembreFamille = new SimpleDroitMembreFamille();
        simpleVersionDroit = new SimpleVersionDroit();
    }

    @Override
    public String getId() {
        return simpleDroit.getId();
    }

    /**
     * @return the simpleDroit
     */
    public SimpleDroit getSimpleDroit() {
        return simpleDroit;
    }

    /**
     * @return the simpleDroitMembreFamille
     */
    public SimpleDroitMembreFamille getSimpleDroitMembreFamille() {
        return simpleDroitMembreFamille;
    }

    /**
     * @return the simpleVersionDroit
     */
    public SimpleVersionDroit getSimpleVersionDroit() {
        return simpleVersionDroit;
    }

    @Override
    public String getSpy() {
        return simpleVersionDroit.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleDroit.setId(id);
    }

    /**
     * @param simpleDroit
     *            the simpleDroit to set
     */
    public void setSimpleDroit(SimpleDroit simpleDroit) {
        this.simpleDroit = simpleDroit;
    }

    /**
     * @param simpleDroitMembreFamille
     *            the simpleDroitMembreFamille to set
     */
    public void setSimpleDroitMembreFamille(SimpleDroitMembreFamille simpleDroitMembreFamille) {
        this.simpleDroitMembreFamille = simpleDroitMembreFamille;
    }

    /**
     * @param simpleVersionDroit
     *            the simpleVersionDroit to set
     */
    public void setSimpleVersionDroit(SimpleVersionDroit simpleVersionDroit) {
        this.simpleVersionDroit = simpleVersionDroit;
    }

    @Override
    public void setSpy(String spy) {
        simpleVersionDroit.setSpy(spy);
    }

}
