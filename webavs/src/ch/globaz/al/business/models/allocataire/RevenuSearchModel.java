package ch.globaz.al.business.models.allocataire;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle simple de recherche du revenu
 * 
 * @author PTA
 * 
 */
public class RevenuSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche du revenu pour une date antérieure
     */
    private String forBeforeDate = null;
    /**
     * Recherche du revenu selon pour la date
     */
    private String forDate = null;

    /**
     * Recherche du revenu par l'identifiant de l'allocataire
     */
    private String forIdAllocataire = null;

    /**
     * Recherche du revenu par son identifiant
     */
    private String forIdRevenu = null;
    /**
     * Recherche d'un revenu par le revenu du conjoint (ou non)
     */
    private Boolean forIsConjoint = null;

    /**
     * Recherche d'un revenu par l'impôt fédéral direct
     */
    private Boolean forIsIfd = null;

    /**
     * Recherche du revenu par le montant
     */
    private String forMontant = null;

    /**
     * @return the forBeforeDate
     */
    public String getForBeforeDate() {
        return forBeforeDate;
    }

    /**
     * @return the forDate
     */
    public String getForDate() {
        return forDate;
    }

    /**
     * @return the forIdAllocataire
     */
    public String getForIdAllocataire() {
        return forIdAllocataire;
    }

    /**
     * @return the forIdRevenu
     */
    public String getForIdRevenu() {
        return forIdRevenu;
    }

    /**
     * 
     * @return forIsConjoint
     */

    public Boolean getForIsConjoint() {
        return forIsConjoint;
    }

    /**
     * @return the forIsIfd
     */
    public Boolean getForIsIfd() {
        return forIsIfd;
    }

    /**
     * @return the forMontant
     */
    public String getForMontant() {
        return forMontant;
    }

    /**
     * @param forBeforeDate
     *            the forBeforeDate to set
     */
    public void setForBeforeDate(String forBeforeDate) {
        this.forBeforeDate = forBeforeDate;
    }

    /**
     * @param forDate
     *            the forDate to set
     */
    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    /**
     * @param forIdAllocataire
     *            the forIdAllocataire to set
     */
    public void setForIdAllocataire(String forIdAllocataire) {
        this.forIdAllocataire = forIdAllocataire;
    }

    /**
     * @param forIdRevenu
     *            the forIdRevenu to set
     */
    public void setForIdRevenu(String forIdRevenu) {
        this.forIdRevenu = forIdRevenu;
    }

    /**
     * 
     * @param forIsConjoint
     *            forIsConjoint to set
     */
    public void setForIsConjoint(Boolean forIsConjoint) {
        this.forIsConjoint = forIsConjoint;
    }

    /**
     * @param forIsIfd
     *            the forIsIfd to set
     */
    public void setForIsIfd(Boolean forIsIfd) {
        this.forIsIfd = forIsIfd;
    }

    /**
     * @param forMontant
     *            the forMontant to set
     */
    public void setForMontant(String forMontant) {
        this.forMontant = forMontant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<RevenuModel> whichModelClass() {
        return RevenuModel.class;
    }

}
