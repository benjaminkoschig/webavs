package ch.globaz.al.business.models.droit;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle d'un enfant lié à un droit
 * 
 * @author jts
 * 
 */
public class EnfantModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * indique si l'allocation naissance a été versée
     */
    private Boolean allocationNaissanceVersee = null;
    /**
     * canton de résidence de l'enfant
     */
    private String cantonResidence = null;
    /**
     * indique si l'enfant est capable d'exercer une activité lucrative
     */
    private Boolean capableExercer = null;
    /**
     * id de l'enfant AF
     */
    private String idEnfant = null;
    /**
     * id du pays de résidence de l'enfant
     */
    private String idPaysResidence = null;
    /**
     * id du tiers enfant
     */
    private String idTiersEnfant = null;
    /**
     * Le montant fixe de l'allocation naissance
     */
    private String montantAllocationNaissanceFixe = null;

    /**
     * Le type de l'allocation de naissance
     */
    private String typeAllocationNaissance = null;

    /**
     * @return the allocationNaissanceVersee
     */
    public Boolean getAllocationNaissanceVersee() {
        return allocationNaissanceVersee;
    }

    /**
     * Retourne le canton de résidence de l'enfant
     * 
     * @return the cantonResidence
     */
    public String getCantonResidence() {
        return cantonResidence;
    }

    /**
     * Indique si l'enfant est capable d'exercer
     * 
     * @return the capableExercer
     */
    public Boolean getCapableExercer() {
        return capableExercer;
    }

    /**
     * Retourne l'id de l'enfant
     */
    @Override
    public String getId() {
        return idEnfant;
    }

    /**
     * @return the idEnfant
     */
    public String getIdEnfant() {
        return idEnfant;
    }

    /**
     * @return the idPaysResidence
     */
    public String getIdPaysResidence() {
        return idPaysResidence;
    }

    /**
     * Retourne l'id du tiers de l'enfant
     * 
     * @return the idTiers
     */
    public String getIdTiersEnfant() {
        return idTiersEnfant;
    }

    /**
     * Retourne le montant forcé de l'allocation de naissance ou d'accueil
     * 
     * @return the montantAllocationNaissanceFixe
     */
    public String getMontantAllocationNaissanceFixe() {
        return montantAllocationNaissanceFixe;
    }

    /**
     * @return the typeAllocationNaissance
     */
    public String getTypeAllocationNaissance() {
        return typeAllocationNaissance;
    }

    /**
     * @param allocationNaissanceVersee
     *            the allocationNaissanceVersee to set
     */
    public void setAllocationNaissanceVersee(Boolean allocationNaissanceVersee) {
        this.allocationNaissanceVersee = allocationNaissanceVersee;
    }

    /**
     * Définit le canton de résidence de l'enfant
     * 
     * @param cantonResidence
     *            the cantonResidence to set
     */
    public void setCantonResidence(String cantonResidence) {
        this.cantonResidence = cantonResidence;
    }

    /**
     * Permet d'indiquer si l'enfant est capable d'exercer ou non
     * 
     * @param capableExercer
     *            the capableExercer to set
     */
    public void setCapableExercer(Boolean capableExercer) {

        this.capableExercer = capableExercer;

    }

    /**
     * @param id
     *            : id de l'enfant
     */
    @Override
    public void setId(String id) {
        idEnfant = id;
    }

    /**
     * @param idEnfant
     *            the idEnfant to set
     */
    public void setIdEnfant(String idEnfant) {
        this.idEnfant = idEnfant;
    }

    /**
     * @param idPaysResidence
     *            the idPaysResidence to set
     */
    public void setIdPaysResidence(String idPaysResidence) {
        this.idPaysResidence = idPaysResidence;
    }

    /**
     * Définit l'id du tiers enfant
     * 
     * @param idTiersEnfant
     *            the idTiers to set
     */
    public void setIdTiersEnfant(String idTiersEnfant) {
        this.idTiersEnfant = idTiersEnfant;
    }

    /**
     * Définit le montant à forcer pour l'allocation de naissance/accueil
     * 
     * @param montantAllocationNaissanceFixe
     *            the montantAllocationNaissanceFixe to set
     */
    public void setMontantAllocationNaissanceFixe(String montantAllocationNaissanceFixe) {
        this.montantAllocationNaissanceFixe = montantAllocationNaissanceFixe;
    }

    /**
     * @param typeAllocationNaissance
     *            the typeAllocationNaissance to set
     */
    public void setTypeAllocationNaissance(String typeAllocationNaissance) {
        this.typeAllocationNaissance = typeAllocationNaissance;
    }
}