package ch.globaz.vulpecula.business.models.registres;

import globaz.jade.persistence.model.JadeSimpleModel;

public class ParametreCotisationAssociationSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -4476704137892710557L;

    private String id;
    private String typeParam;
    private String idCotisationAssociationProfessionnelle;
    private String taux;
    private String montant;
    private String fourchetteDebut;
    private String fourchetteFin;
    private String facteur;

    public ParametreCotisationAssociationSimpleModel() {
        super();
    }

    public String getIdCotisationAssociationProfessionnelle() {
        return idCotisationAssociationProfessionnelle;
    }

    public void setIdCotisationAssociationProfessionnelle(String idCotisationAssociationProfessionnelle) {
        this.idCotisationAssociationProfessionnelle = idCotisationAssociationProfessionnelle;
    }

    public String getTaux() {
        return taux;
    }

    public void setTaux(String taux) {
        this.taux = taux;
    }

    public String getTypeParam() {
        return typeParam;
    }

    public void setTypeParam(String typeParamCotisationAP) {
        typeParam = typeParamCotisationAP;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getFourchetteDebut() {
        return fourchetteDebut;
    }

    public void setFourchetteDebut(String fourchetteDebut) {
        this.fourchetteDebut = fourchetteDebut;
    }

    public String getFourchetteFin() {
        return fourchetteFin;
    }

    public void setFourchetteFin(String fourchetteFin) {
        this.fourchetteFin = fourchetteFin;
    }

    public String getFacteur() {
        return facteur;
    }

    public void setFacteur(String facteur) {
        this.facteur = facteur;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
