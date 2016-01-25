package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Modele de recherche pour les dossiers liés à un dossier père
 * 
 * @author gmo
 * 
 */
public class DossierLieComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDossierPere = null;
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
    public Class<DossierLieComplexModel> whichModelClass() {
        return DossierLieComplexModel.class;
    }

}
