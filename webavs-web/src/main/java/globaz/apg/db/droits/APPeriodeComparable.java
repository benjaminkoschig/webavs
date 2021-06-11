package globaz.apg.db.droits;

import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.utils.PRDateUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class APPeriodeComparable extends APPeriodeAPG implements Comparable<APPeriodeAPG> {

    APPeriodeAPG ori;

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public APPeriodeComparable(APPeriodeAPG clone) {
        setIdPeriode(clone.getIdPeriode());
        setDateDebutPeriode(clone.getDateDebutPeriode());
        setDateFinPeriode(clone.getDateFinPeriode());
        setNbrJours(clone.getNbrJours());
        setTypePeriode(clone.getTypePeriode());
        setIdDroit(clone.getIdDroit());
        setCantonImposition(clone.getCantonImposition());
        setTauxImposition(clone.getTauxImposition());
        setNbJourSupplementaire(clone.getNbJourSupplementaire());
        ori =clone;
    }

    public static APPeriodeComparable getSameEntity(APPeriodeAPG periode) {
        APPeriodeComparable clone = new APPeriodeComparable(periode);
        clone.setId(periode.getId());
        clone.populateSpy(periode.getSpy().getFullData());
        return clone;
    }

    @Override
    public int compareTo(APPeriodeAPG o) {
        Integer result = compareDates(getDateDebutPeriode(), o.getDateDebutPeriode());
        if (result == null) {
            result = compareDates(getDateFinPeriode(), o.getDateFinPeriode());
        }
        // Si les 2 dates de début sont égales, on regarde les 2 dates de fin. Si la comparaison de la date de fin
        // échoue on garde le résultat de la comparaison des 2 dates de début
        else if (result == 0) {
            Integer resultFin = compareDates(getDateFinPeriode(), o.getDateFinPeriode());
            if (resultFin != null) {
                result = resultFin;
            }
        }
        // Si aucune comparaison possible
        if (result == null) {
            result = new Integer(-1);
        }
        return result.intValue();
    }

    private Integer compareDates(String date1, String date2) {
        Integer result = null;
        if ((date1 != null) && (date2 != null)) {
            switch (PRDateUtils.compare(date1, date2)) {
                case BEFORE:
                    result = new Integer(1);
                    break;
                case EQUALS:
                    result = new Integer(0);
                    break;
                case AFTER:
                    result = new Integer(-1);
                    break;
                case INCOMPARABLE:
                default:
                    return result;
            }
        }
        return result;
    }

    public APPeriodeAPG getOri() {
        return ori;
    }

    public void setOri(APPeriodeAPG ori) {
        this.ori = ori;
    }

    public long nbJourPeriode() {
        LocalDate dateDeDebutPrestDate = parseStringToDateTime(getDateDebutPeriode(), format);
        LocalDate dateDeFinACMDate = parseStringToDateTime(getDateFinPeriode(), format);
        return ChronoUnit.DAYS.between(dateDeDebutPrestDate, dateDeFinACMDate);
    }

    /**
     *
     *
     * @param date
     * @param formatter
     * @return
     */
    private LocalDate parseStringToDateTime(String date, DateTimeFormatter formatter) {
        if (!JadeStringUtil.isBlankOrZero(date)) {
            return LocalDate.parse(date, formatter);
        }
        return null;
    }

}