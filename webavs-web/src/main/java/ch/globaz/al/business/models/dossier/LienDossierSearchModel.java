package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle de recherche simple de lien dossier.
 * 
 * @author gmo
 * 
 */
public class LienDossierSearchModel extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * critère de recherche id dossier fils
     */
    private String forIdDossierFils = null;
    /**
     * critère de recherche id dossier père
     */
    private String forIdDossierPere = null;
    /**
     * critère de recherche type de lien
     */
    private String forTypeLien = null;

    public String getForIdDossierPere() {
        return forIdDossierPere;
    }

    public String getForTypeLien() {
        return forTypeLien;
    }

    public void setForIdDossierPere(String forIdDossierPere) {
        this.forIdDossierPere = forIdDossierPere;
    }

    public void setForTypeLien(String forTypeLien) {
        this.forTypeLien = forTypeLien;
    }

    @Override
    public Class<LienDossierModel> whichModelClass() {
        return LienDossierModel.class;
    }

    public void setForIdDossierFils(String forIdDossierFils) {
        this.forIdDossierFils = forIdDossierFils;
    }

    public String getForIdDossierFils() {
        return forIdDossierFils;
    }

}
