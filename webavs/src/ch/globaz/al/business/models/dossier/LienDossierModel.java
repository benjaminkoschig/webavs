package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle simple lien dossier. Décrit les liens entre 2 dossiers
 * 
 * @author gmo
 * 
 */
public class LienDossierModel extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * identifiant du dossier fils du lien
     */
    private String idDossierFils = null;
    /**
     * identifiant du dossier père du lien
     */
    private String idDossierPere = null;
    /**
     * clé primaire du lien
     */
    private String idLien = null;
    /**
     * type de lien entre les 2 dossiers
     */
    private String typeLien = null;

    @Override
    public String getId() {
        return idLien;
    }

    public String getIdDossierFils() {
        return idDossierFils;
    }

    public String getIdDossierPere() {
        return idDossierPere;
    }

    public String getIdLien() {
        return idLien;
    }

    public String getTypeLien() {
        return typeLien;
    }

    @Override
    public void setId(String id) {
        idLien = id;

    }

    public void setIdDossierFils(String idDossierFils) {
        this.idDossierFils = idDossierFils;
    }

    public void setIdDossierPere(String idDossierPere) {
        this.idDossierPere = idDossierPere;
    }

    public void setIdLien(String idLien) {
        this.idLien = idLien;
    }

    public void setTypeLien(String typeLien) {
        this.typeLien = typeLien;
    }

}
