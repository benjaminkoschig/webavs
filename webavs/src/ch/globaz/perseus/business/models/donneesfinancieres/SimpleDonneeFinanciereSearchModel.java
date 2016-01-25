/**
 * 
 */
package ch.globaz.perseus.business.models.donneesfinancieres;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author DDE
 * 
 */
public class SimpleDonneeFinanciereSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDemande = null;
    private String forIdMembreFamille = null;

    /**
     * @return the forIdDemande
     */
    public String getForIdDemande() {
        return forIdDemande;
    }

    /**
     * @return the forIdMembreFamille
     */
    public String getForIdMembreFamille() {
        return forIdMembreFamille;
    }

    /**
     * @param forIdDemande
     *            the forIdDemande to set
     */
    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    /**
     * @param forIdMembreFamille
     *            the forIdMembreFamille to set
     */
    public void setForIdMembreFamille(String forIdMembreFamille) {
        this.forIdMembreFamille = forIdMembreFamille;
    }

    @Override
    public Class whichModelClass() {
        return SimpleDonneeFinanciere.class;
    }

}
