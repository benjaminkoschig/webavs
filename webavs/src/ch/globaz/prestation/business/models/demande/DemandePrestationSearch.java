package ch.globaz.prestation.business.models.demande;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class DemandePrestationSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDemandePrestation = null;
    private String forIdTiers = null;
    private String forTypeDemande = null;

    /**
     * @return the forIdDemandePrestation
     */
    public String getForIdDemandePrestation() {
        return forIdDemandePrestation;
    }

    /**
     * @return the forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * @return the forTypeDemande
     */
    public String getForTypeDemande() {
        return forTypeDemande;
    }

    /**
     * @param forIdDemandePrestation
     *            the forIdDemandePrestation to set
     */
    public void setForIdDemandePrestation(String forIdDemandePrestation) {
        this.forIdDemandePrestation = forIdDemandePrestation;
    }

    /**
     * @param forIdTiers
     *            the forIdTiers to set
     */
    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    /**
     * @param forTypeDemande
     *            the forTypeDemande to set
     */
    public void setForTypeDemande(String forTypeDemande) {
        this.forTypeDemande = forTypeDemande;
    }

    @Override
    public Class whichModelClass() {
        return DemandePrestation.class;
    }

}
