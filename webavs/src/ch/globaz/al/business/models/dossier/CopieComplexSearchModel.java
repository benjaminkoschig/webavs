package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Classe der recherche complexe de copies
 * 
 * @author GMO/JTS/PTA
 * 
 */
public class CopieComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Recherche par l'identifiant du dossier
     */
    private String forIdDossier = null;

    /**
     * recherche sur l'id Tiers Affilie
     */
    private String forIdTiersAffilie = null;
    /**
     * recherche sur l'id Tiers Allocataire
     */
    private String forIdTiersAllocataire = null;
    /**
     * Recherche par le type de copie
     */
    private String forTypeCopie = null;

    /**
     * 
     * @return forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forIdTiersAffilie
     */
    public String getForIdTiersAffilie() {
        return forIdTiersAffilie;
    }

    /**
     * @return the forIdTiersAllocataire
     */
    public String getForIdTiersAllocataire() {
        return forIdTiersAllocataire;
    }

    /**
     * @return the forTypeEcheance
     */
    public String getForTypeCopie() {
        return forTypeCopie;
    }

    /**
     * 
     * @param forIdDossier
     *            :the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forIdTiersAffilie
     *            the forIdTiersAffilie to set
     */
    public void setForIdTiersAffilie(String forIdTiersAffilie) {
        this.forIdTiersAffilie = forIdTiersAffilie;
    }

    /**
     * @param forIdTiersAllocataire
     *            the forIdTiersAllocataire to set
     */
    public void setForIdTiersAllocataire(String forIdTiersAllocataire) {
        this.forIdTiersAllocataire = forIdTiersAllocataire;
    }

    /**
     * @param forTypeCopie
     *            the forTypeCopie to set
     */
    public void setForTypeCopie(String forTypeCopie) {
        this.forTypeCopie = forTypeCopie;
    }

    @Override
    public Class<CopieComplexModel> whichModelClass() {
        return CopieComplexModel.class;
    }

}
