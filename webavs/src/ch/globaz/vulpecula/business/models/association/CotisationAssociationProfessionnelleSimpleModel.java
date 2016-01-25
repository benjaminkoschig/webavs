package ch.globaz.vulpecula.business.models.association;

import globaz.jade.persistence.model.JadeSimpleModel;

public class CotisationAssociationProfessionnelleSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 1L;

    private String id;
    private String idAssociationProfessionnelle;
    private String libelle;
    private String libelleUpper;
    private String montantBase;
    private String montantMinimum;
    private String montantMaximum;
    private String genre;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdAssociationProfessionnelle() {
        return idAssociationProfessionnelle;
    }

    public void setIdAssociationProfessionnelle(String idAssociationProfessionnelle) {
        this.idAssociationProfessionnelle = idAssociationProfessionnelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getMontantBase() {
        return montantBase;
    }

    public void setMontantBase(String montantBase) {
        this.montantBase = montantBase;
    }

    public String getMontantMinimum() {
        return montantMinimum;
    }

    public void setMontantMinimum(String montantMinimum) {
        this.montantMinimum = montantMinimum;
    }

    public String getMontantMaximum() {
        return montantMaximum;
    }

    public void setMontantMaximum(String montantMaximum) {
        this.montantMaximum = montantMaximum;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLibelleUpper() {
        return libelleUpper;
    }

    public void setLibelleUpper(String libelleUpper) {
        this.libelleUpper = libelleUpper;
    }
}
