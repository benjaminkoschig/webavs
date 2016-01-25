package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimplePeriodeImpotSource extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = null;
    private String dateFin = null;
    private String idPeriode = null;
    private Boolean periodeGeneree = null;

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    @Override
    public String getId() {
        return idPeriode;
    }

    public String getIdPeriode() {
        return idPeriode;
    }

    public Boolean getPeriodeGeneree() {
        return periodeGeneree;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void setId(String id) {
        idPeriode = id;

    }

    public void setIdPeriode(String idPeriode) {
        this.idPeriode = idPeriode;
    }

    public void setPeriodeGeneree(Boolean periodeGeneree) {
        this.periodeGeneree = periodeGeneree;
    }

}
