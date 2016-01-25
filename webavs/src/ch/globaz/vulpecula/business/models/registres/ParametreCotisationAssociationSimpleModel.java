package ch.globaz.vulpecula.business.models.registres;

import globaz.jade.persistence.model.JadeSimpleModel;

public class ParametreCotisationAssociationSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -4476704137892710557L;

    private String id;
    private String idCotisationAssociationProfessionnelle;
    private String taux;
    private String fourchetteDebut;
    private String fourchetteFin;

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

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
