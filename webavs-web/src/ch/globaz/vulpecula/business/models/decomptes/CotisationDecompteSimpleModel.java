package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSimpleModel;

public class CotisationDecompteSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -3082110212503589549L;

    private String id;
    private String idLigneDecompte;
    private String idCotisation;
    private String taux;
    private String masse;
    private boolean masseForcee;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    public String getIdCotisation() {
        return idCotisation;
    }

    public void setIdCotisation(final String idCotisation) {
        this.idCotisation = idCotisation;
    }

    public String getTaux() {
        return taux;
    }

    public void setTaux(final String taux) {
        this.taux = taux;
    }

    public String getIdLigneDecompte() {
        return idLigneDecompte;
    }

    public void setIdLigneDecompte(final String idLigneDecompte) {
        this.idLigneDecompte = idLigneDecompte;
    }

    public String getMasse() {
        return masse;
    }

    public void setMasse(String masse) {
        this.masse = masse;
    }

    public boolean getMasseForcee() {
        return masseForcee;
    }

    public void setMasseForcee(boolean masseForcee) {
        this.masseForcee = masseForcee;
    }
}
