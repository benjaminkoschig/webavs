package ch.globaz.common.domaine;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Collection;
import ch.globaz.common.domaine.Periode.TypePeriode;

/**
 * Contient une liste de période et des méthodes utilitaires (comme récupérer la plus petite date de fin, la plus
 * grande, etc...) pour ces périodes
 */
public class GroupePeriodes {

    private Periode debutMax = null;
    private Periode debutMin = null;
    private Periode finMax = null;
    private Periode finMin = null;
    private Boolean hasDateDebutNullValue = false;
    private Boolean hasDateFinNullValue = false;
    private Collection<Periode> periodes = new ArrayList<Periode>();

    public void add(Periode periode) {
        if (JadeStringUtil.isBlankOrZero(periode.getDateDebut())) {
            hasDateDebutNullValue = true;
        }
        if (JadeStringUtil.isBlankOrZero(periode.getDateFin())) {
            hasDateFinNullValue = true;
        }
        if (isPeriodeMonthYear(periode)) {
            setDateWithDateMonthYear(periode);
        } else {
            setDateWithNormalDate(periode);
        }
        periodes.add(periode);
    }

    public void add(String dateDebut, String dateFin) {
        Periode periode = new Periode(dateDebut, dateFin);
        this.add(periode);
    }

    public void add(String[] dates) {
        Periode periode = new Periode(dates[0], dates[1]);
        this.add(periode);
    }

    public String getDateDebutMax() {
        return debutMax.getDateDebut();
    }

    public String getDateDebutMin() {
        return debutMin.getDateDebut();
    }

    public String getDateFinMax() {
        return finMax.getDateFin();
    }

    public String getDateFinMin() {
        return finMin.getDateFin();
    }

    public Periode getDebutMax() {
        return debutMax;
    }

    public Periode getDebutMin() {
        return debutMin;
    }

    public Periode getFinMax() {
        return finMax;
    }

    public Periode getFinMin() {
        return finMin;
    }

    public Collection<Periode> getPeriodes() {
        return periodes;
    }

    public Boolean hasDateDebutNullValue() {
        return hasDateDebutNullValue;
    }

    public Boolean hasDateFinNullValue() {
        return hasDateFinNullValue;
    }

    private boolean isPeriodeMonthYear(Periode date) {
        return date.getType() == TypePeriode.MOIS_ANNEE;
    }

    private void setDateWithDateMonthYear(Periode periode) {
        if ((debutMax == null) || JadeDateUtil.isDateMonthYearBefore(getDateDebutMax(), periode.getDateDebut())) {
            debutMax = periode;
        }
        if ((finMax == null) || JadeDateUtil.isDateMonthYearBefore(getDateFinMax(), periode.getDateFin())) {
            finMax = periode;
        }

        if ((debutMin == null) || JadeDateUtil.isDateMonthYearAfter(getDateDebutMin(), periode.getDateDebut())) {
            debutMin = periode;
        }
        if ((finMin == null) || JadeDateUtil.isDateMonthYearAfter(getDateFinMin(), periode.getDateFin())) {
            finMin = periode;
        }
    }

    private void setDateWithNormalDate(Periode periode) {

        if ((getDateDebutMax() == null) || JadeDateUtil.isDateBefore(getDateDebutMax(), periode.getDateDebut())) {
            debutMax = periode;
        }
        if ((getDateFinMax() == null) || JadeDateUtil.isDateBefore(getDateFinMax(), periode.getDateFin())) {
            finMax = periode;
        }

        if ((getDateDebutMin() == null) || JadeDateUtil.isDateAfter(getDateDebutMin(), periode.getDateDebut())) {
            debutMin = periode;
        }
        if ((getDateFinMin() == null) || JadeDateUtil.isDateAfter(getDateFinMin(), periode.getDateFin())) {
            finMin = periode;
        }

    }

    @Override
    public String toString() {
        return "Periodes [dateDebutMax=" + getDateDebutMax() + ", dateDebutMin=" + getDateDebutMin() + ", dateFinMax="
                + getDateFinMax() + ", dateFinMin=" + getDateFinMin() + ", hasDateDebutNullValue="
                + hasDateDebutNullValue + ", hasDateFinNullValue=" + hasDateFinNullValue + ", periodes=" + periodes
                + "]";
    }
}
