package ch.globaz.al.impotsource.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class TauxImpositionSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -1413766918472447611L;

    private String id;
    private String periodeDebut;
    private String periodeFin;
    private String canton;
    private String typeImposition;
    private String taux;

    @Override
    public String getId() {
        return id;
    }

    @Override
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

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getTypeImposition() {
        return typeImposition;
    }

    public void setTypeImposition(String typeImposition) {
        this.typeImposition = typeImposition;
    }

    public String getTaux() {
        return taux;
    }

    public void setTaux(String taux) {
        this.taux = taux;
    }
}
