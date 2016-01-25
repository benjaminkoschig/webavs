/**
 * 
 */
package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle simple de recherche de copie
 * 
 * @author PTA
 * 
 */
public class CopieSearchModel extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Recherche par l'identifiant du dossier
     */
    private String forIdDossier = null;

    /**
     * recherche du tiersAllocataire
     */
    private String forIdTiersDestinataire = null;
    /**
     * critère copie en impression batch ou pas
     */
    private Boolean forIsImpressionBatch = null;

    /**
     * recherche de la priorite de la copie
     */
    private String forOrdreCopie = null;

    /**
     * recherche du type de copie
     */
    private String forTypeCopie = null;

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forIdTiersDestinataire
     */
    public String getForIdTiersDestinataire() {
        return forIdTiersDestinataire;
    }

    /**
     * @return forIsImpressionBatch
     */
    public Boolean getForIsImpressionBatch() {
        return forIsImpressionBatch;
    }

    /**
     * @return the forOrdreCopie
     */
    public String getForOrdreCopie() {
        return forOrdreCopie;
    }

    /**
     * @return the forTypeCopie
     */
    public String getForTypeCopie() {
        return forTypeCopie;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forIdTiersDestinataire
     *            the forIdTiersDestinataire to set
     */
    public void setForIdTiersDestinataire(String forIdTiersDestinataire) {
        this.forIdTiersDestinataire = forIdTiersDestinataire;
    }

    /**
     * @param forIsImpressionBatch
     *            the forIsImpressionBatch to set
     */
    public void setForIsImpressionBatch(Boolean forIsImpressionBatch) {
        this.forIsImpressionBatch = forIsImpressionBatch;
    }

    /**
     * @param forOrdreCopie
     *            the forOrdreCopie to set
     */
    public void setForOrdreCopie(String forOrdreCopie) {
        this.forOrdreCopie = forOrdreCopie;
    }

    /**
     * @param forTypeCopie
     *            the forTypeCopie to set
     */
    public void setForTypeCopie(String forTypeCopie) {
        this.forTypeCopie = forTypeCopie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<CopieModel> whichModelClass() {
        return CopieModel.class;
    }

}
