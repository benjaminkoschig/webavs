package ch.globaz.vulpecula.business.models.association;

import globaz.jade.persistence.model.JadeSimpleModel;

public class LigneFactureAssociationProfessionnelleSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -4476704137892710557L;

    private String id;
    private String idEnteteFacture;
    private String periodeDebut;
    private String periodeFin;
    private String idAssociationCotisation;
    private String fourchetteDebut;
    private String fourchetteFin;
    private String montantCotisation;
    private String typeParametre;
    private String tauxCotisation;
    private String massePourCotisation;
    private String facteurCotisation;

    public LigneFactureAssociationProfessionnelleSimpleModel() {
        super();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdEnteteFacture() {
        return idEnteteFacture;
    }

    public void setIdEnteteFacture(String idEnteteFacture) {
        this.idEnteteFacture = idEnteteFacture;
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

    public String getIdAssociationCotisation() {
        return idAssociationCotisation;
    }

    public void setIdAssociationCotisation(String idAssociationCotisation) {
        this.idAssociationCotisation = idAssociationCotisation;
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

    public String getMontantCotisation() {
        return montantCotisation;
    }

    public void setMontantCotisation(String montantCotisation) {
        this.montantCotisation = montantCotisation;
    }

    public String getTypeParametre() {
        return typeParametre;
    }

    public void setTypeParametre(String typeParametre) {
        this.typeParametre = typeParametre;
    }

    public String getTauxCotisation() {
        return tauxCotisation;
    }

    public void setTauxCotisation(String tauxCotisation) {
        this.tauxCotisation = tauxCotisation;
    }

    public String getMassePourCotisation() {
        return massePourCotisation;
    }

    public void setMassePourCotisation(String massePourCotisation) {
        this.massePourCotisation = massePourCotisation;
    }

    public String getFacteurCotisation() {
        return facteurCotisation;
    }

    public void setFacteurCotisation(String facteurCotisation) {
        this.facteurCotisation = facteurCotisation;
    }

}
