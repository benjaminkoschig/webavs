package ch.globaz.al.business.models.allocataire;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle allocataire. Il contient les informations liées à l'allocataire d'un dossier et qui ne seraient pas contenues
 * dans le tiers
 * 
 * @author jts
 * 
 */
public class AllocataireModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Canton de résidence de l'allocataire
     */
    private String cantonResidence = null;
    /**
     * identifiant de l'allocataire
     */
    private String idAllocataire = null;
    /**
     * identifiant du pays de résidence de l'allocataire
     */
    private String idPaysResidence = null;
    /**
     * identifiant du tiers pour l'allocataire
     */
    private String idTiersAllocataire = null;
    /**
     * langue identique à affilié
     */
    private Boolean langueAffilie = null;
    /**
     * permis de l'allocataire
     */
    private String permis = null;

    /**
     * Retourne le canton de résidence de l'allocataire
     * 
     * @return the cantonResidence
     */
    public String getCantonResidence() {
        return cantonResidence;
    }

    /**
     * Retourne l'id de l'allocataire
     */
    @Override
    public String getId() {
        return idAllocataire;
    }

    /**
     * @return the idAllocataire
     */
    public String getIdAllocataire() {
        return idAllocataire;
    }

    /**
     * @return the idPaysResidence
     */
    public String getIdPaysResidence() {
        return idPaysResidence;
    }

    /**
     * Retourne l'id du tiers allocataire (provient de web@tiers)
     * 
     * @return the idTiersAllocataire
     */
    public String getIdTiersAllocataire() {
        return idTiersAllocataire;
    }

    public Boolean getLangueAffilie() {
        return langueAffilie;
    }

    /**
     * Retourne le permis de travail de l'allocataire
     * 
     * @return the permis
     */
    public String getPermis() {
        return permis;
    }

    /**
     * Définit le canton de résidence de l'allocataire
     * 
     * @param cantonResidence
     *            the cantonResidence to set
     */
    public void setCantonResidence(String cantonResidence) {
        this.cantonResidence = cantonResidence;
    }

    /**
     * Définit l'id de l'allocataire
     */
    @Override
    public void setId(String id) {
        idAllocataire = id;
    }

    /**
     * @param idAllocataire
     *            the idAllocataire to set
     */
    public void setIdAllocataire(String idAllocataire) {
        this.idAllocataire = idAllocataire;
    }

    /**
     * @param idPaysResidence
     *            the idPaysResidence to set
     */
    public void setIdPaysResidence(String idPaysResidence) {
        this.idPaysResidence = idPaysResidence;
    }

    /**
     * Définit l'id du tiers allocataire (web@tiers)
     * 
     * @param idTiersAllocataire
     *            the idTiersAllocataire to set
     */
    public void setIdTiersAllocataire(String idTiersAllocataire) {
        this.idTiersAllocataire = idTiersAllocataire;
    }

    public void setLangueAffilie(Boolean langueAffilie) {
        this.langueAffilie = langueAffilie;
    }

    /**
     * @param permis
     *            the permis to set
     */
    public void setPermis(String permis) {
        this.permis = permis;
    }
}
