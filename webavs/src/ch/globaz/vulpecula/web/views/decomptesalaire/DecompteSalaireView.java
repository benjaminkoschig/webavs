package ch.globaz.vulpecula.web.views.decomptesalaire;

import java.io.Serializable;

public class DecompteSalaireView implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String periodeDebut;
    private String periodeFin;
    private String salaireHoraire;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSalaireHoraire() {
        return salaireHoraire;
    }

    public void setSalaireHoraire(String salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }
}
