package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSimpleModel;

public class ParametreAssuranceSimpleModel extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String assuranceId;
    private String dateDebut;
    private String dateFin;
    private String genre;
    private String parametreAssuranceId;
    private String sexe;
    private String valeurAlpha;
    private String valeurNum;

    public String getAssuranceId() {
        return assuranceId;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public String getId() {

        return getParametreAssuranceId();
    }

    public String getParametreAssuranceId() {
        return parametreAssuranceId;
    }

    public String getSexe() {
        return sexe;
    }

    public String getValeurAlpha() {
        return valeurAlpha;
    }

    public String getValeurNum() {
        return valeurNum;
    }

    public void setAssuranceId(String assuranceId) {
        this.assuranceId = assuranceId;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public void setId(String id) {
        setParametreAssuranceId(id);
    }

    public void setParametreAssuranceId(String parametreAssuranceId) {
        this.parametreAssuranceId = parametreAssuranceId;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setValeurAlpha(String valeurAlpha) {
        this.valeurAlpha = valeurAlpha;
    }

    public void setValeurNum(String valeurNum) {
        this.valeurNum = valeurNum;
    }
}
