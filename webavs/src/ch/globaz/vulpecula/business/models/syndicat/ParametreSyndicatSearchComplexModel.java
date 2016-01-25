package ch.globaz.vulpecula.business.models.syndicat;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import ch.globaz.vulpecula.domain.models.common.Annee;

public class ParametreSyndicatSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 8516896571214997386L;

    private String forId;
    private String forIdSyndicat;
    private String forIdCaisseMetier;
    private String afterOrEqualsDateDebut;
    private String beforeOrEqualsDateDebut;
    private String beforeOrEqualsDateFin;
    private String afterOrEqualsDateFin;
    private String dateFinIsZero;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdSyndicat() {
        return forIdSyndicat;
    }

    public void setForIdSyndicat(String forIdSyndicat) {
        this.forIdSyndicat = forIdSyndicat;
    }

    public String getForIdCaisseMetier() {
        return forIdCaisseMetier;
    }

    public void setForIdCaisseMetier(String forIdCaisseMetier) {
        this.forIdCaisseMetier = forIdCaisseMetier;
    }

    public String getAfterOrEqualsDateDebut() {
        return afterOrEqualsDateDebut;
    }

    public void setAfterOrEqualsDateDebut(String afterOrEqualsDateDebut) {
        this.afterOrEqualsDateDebut = afterOrEqualsDateDebut;
    }

    public String getBeforeOrEqualsDateFin() {
        return beforeOrEqualsDateFin;
    }

    public void setForAnnee(Annee annee) {
        beforeOrEqualsDateDebut = annee.getFirstDayOfYear().getSwissValue();
        afterOrEqualsDateFin = annee.getFirstDayOfYear().getSwissValue();
        setDateFinIsZero();
    }

    public void setBeforeOrEqualsDateFin(String beforeOrEqualsDateFin) {
        this.beforeOrEqualsDateFin = beforeOrEqualsDateFin;
    }

    public String getDateFinIsZero() {
        return dateFinIsZero;
    }

    public void setDateFinIsZero() {
        dateFinIsZero = "0";
    }

    public String getBeforeOrEqualsDateDebut() {
        return beforeOrEqualsDateDebut;
    }

    public void setBeforeOrEqualsDateDebut(String beforeOrEqualsDateDebut) {
        this.beforeOrEqualsDateDebut = beforeOrEqualsDateDebut;
    }

    public String getAfterOrEqualsDateFin() {
        return afterOrEqualsDateFin;
    }

    public void setAfterOrEqualsDateFin(String afterOrEqualsDateFin) {
        this.afterOrEqualsDateFin = afterOrEqualsDateFin;
    }

    public void setDateFinIsZero(String dateFinIsZero) {
        this.dateFinIsZero = dateFinIsZero;
    }

    @Override
    public Class<ParametreSyndicatComplexModel> whichModelClass() {
        return ParametreSyndicatComplexModel.class;
    }

}
