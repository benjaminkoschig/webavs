package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class PeriodeImpotSourceSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String ORDER_BY_DATE_DEBUT_ASC = "dateDebutAsc";
    public static String WITH_DATE_VALABLE = "withDateValable";

    private String forAuDateFin = null;
    private String forDateValable = null;
    private String forDuDateDebut = null;
    private String forIdPeriode = null;
    private Boolean periodeGeneree = null;

    public String getForAuDateFin() {
        return forAuDateFin;
    }

    public String getForDateValable() {
        return forDateValable;
    }

    public String getForDuDateDebut() {
        return forDuDateDebut;
    }

    public String getForIdPeriode() {
        return forIdPeriode;
    }

    public void setForAuDateFin(String forAuDateFin) {
        this.forAuDateFin = forAuDateFin;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    public void setForDuDateDebut(String forDuDateDebut) {
        this.forDuDateDebut = forDuDateDebut;
    }

    public void setForIdPeriode(String forIdPeriode) {
        this.forIdPeriode = forIdPeriode;
    }

    @Override
    public Class whichModelClass() {
        return PeriodeImpotSource.class;
    }

    public void setPeriodeGeneree(Boolean periodeGeneree) {
        this.periodeGeneree = periodeGeneree;
    }

    public Boolean getPeriodeGeneree() {
        return periodeGeneree;
    }

}
