package ch.globaz.vulpecula.business.models.association;

import globaz.jade.persistence.model.JadeSimpleModel;

public class AssociationCotisationSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -4476704137892710557L;

    private String id;
    private String idCotisationAssociationProfessionnelle;
    private String idEmployeur;
    private String genre;
    private String periodeDebut;
    private String periodeFin;
    private String masseSalariale;
    private String reductionFacture;

    public AssociationCotisationSimpleModel() {
        super();
    }

    public String getIdCotisationAssociationProfessionnelle() {
        return idCotisationAssociationProfessionnelle;
    }

    public void setIdCotisationAssociationProfessionnelle(String idCotisationAssociationProfessionnelle) {
        this.idCotisationAssociationProfessionnelle = idCotisationAssociationProfessionnelle;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

    public String getMasseSalariale() {
        return masseSalariale;
    }

    public void setMasseSalariale(String masseSalariale) {
        this.masseSalariale = masseSalariale;
    }

    public String getReductionFacture() {
        return reductionFacture;
    }

    public void setReductionFacture(String reductionFacture) {
        this.reductionFacture = reductionFacture;
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
