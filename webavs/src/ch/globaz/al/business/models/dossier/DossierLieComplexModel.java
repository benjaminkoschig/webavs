package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Modèle représentant un dossier lié à son père
 * 
 * @author gmo
 * 
 */
public class DossierLieComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dossierActivite = null;
    private String dossierEtat = null;
    private String dossierRadiation = null;
    private String dossierStatut = null;

    private String droitDebut = null;

    private String idDossierLie = null;

    private String idLien = null;

    private String nomAllocataire = null;

    private String nssAllocataire = null;

    private String numAffilie = null;

    private String prenomAllocataire = null;

    private String typeLien = null;

    public String getDossierActivite() {
        return dossierActivite;
    }

    public String getDossierEtat() {
        return dossierEtat;
    }

    public String getDossierRadiation() {
        return dossierRadiation;
    }

    public String getDossierStatut() {
        return dossierStatut;
    }

    public String getDroitDebut() {
        return droitDebut;
    }

    @Override
    public String getId() {
        return idLien;
    }

    public String getIdDossierLie() {
        return idDossierLie;
    }

    public String getIdLien() {
        return idLien;
    }

    public String getNomAllocataire() {
        return nomAllocataire;
    }

    public String getNssAllocataire() {
        return nssAllocataire;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getPrenomAllocataire() {
        return prenomAllocataire;
    }

    @Override
    public String getSpy() {
        return null;
    }

    public String getTypeLien() {
        return typeLien;
    }

    public void setDossierActivite(String dossierActivite) {
        this.dossierActivite = dossierActivite;
    }

    public void setDossierEtat(String dossierEtat) {
        this.dossierEtat = dossierEtat;
    }

    public void setDossierRadiation(String dossierRadiation) {
        this.dossierRadiation = dossierRadiation;
    }

    public void setDossierStatut(String dossierStatut) {
        this.dossierStatut = dossierStatut;
    }

    public void setDroitDebut(String droitDebut) {
        this.droitDebut = droitDebut;
    }

    @Override
    public void setId(String id) {
        // DO NOTHING

    }

    public void setIdDossierLie(String idDossierLie) {
        this.idDossierLie = idDossierLie;
    }

    public void setIdLien(String idLien) {
        this.idLien = idLien;
    }

    public void setNomAllocataire(String nomAllocataire) {
        this.nomAllocataire = nomAllocataire;
    }

    public void setNssAllocataire(String nssAllocataire) {
        this.nssAllocataire = nssAllocataire;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setPrenomAllocataire(String prenomAllocataire) {
        this.prenomAllocataire = prenomAllocataire;
    }

    @Override
    public void setSpy(String spy) {
        // DO NOTHING

    }

    public void setTypeLien(String typeLien) {
        this.typeLien = typeLien;
    }

}
