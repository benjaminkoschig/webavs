package ch.globaz.vulpecula.domain.models.common;

import java.util.List;
import java.util.Locale;

/**
 * Définit un espace de temps entre deux mois
 * 
 * @author Jonas Paratte (JPA) | Créé le 17.02.2013
 */
public class PeriodeMensuelle implements ValueObject {
    private final Date periodeDebut;
    private final Date periodeFin;

    /**
     * @return le début de la période formattée
     */
    public String getPeriodeDebutFormat() {
        return getMoisDebut() + "." + getAnneeDebut();
    }

    /**
     * @return le fin de la période formattée
     */
    public String getPeriodeFinFormat() {
        return getMoisFin() + "." + getAnneeFin();
    }

    public PeriodeMensuelle(final Date dateDebut, final Date dateFin) {
        if (!isDateDebutBeforeDateFin(dateDebut, dateFin)) {
            throwIllegalArgumentException();
        }

        periodeDebut = dateDebut;
        periodeFin = dateFin.getLastDayOfMonth();
    }

    /**
     * @param dateDebut
     * @param dateFin
     */
    public PeriodeMensuelle(final String dateDebut, final String dateFin) {
        if (!isValid(dateDebut, dateFin)) {
            throwIllegalArgumentException();
        }

        periodeDebut = new Date(dateDebut);
        periodeFin = new Date(dateFin).getLastDayOfMonth();
    }

    private boolean isValid(final String dateDebut, final String dateFin) {
        if (dateDebut == null && dateFin == null) {
            return false;
        }
        if (dateDebut == null) {
            return false;
        }
        if (dateFin == null) {
            return true;
        }

        Date debut = new Date(dateDebut);
        Date fin = new Date(dateFin);
        return isDateDebutBeforeDateFin(debut, fin);
    }

    private boolean isDateDebutBeforeDateFin(final Date dateDebut, final Date dateFin) {
        return dateDebut.before(dateFin) || dateDebut.equals(dateFin);
    }

    private void throwIllegalArgumentException() {
        throw new IllegalArgumentException("La période de début doit être antérieure à la période de fin");
    }

    @Override
    public String toString() {
        return getMoisDebut() + "." + getAnneeDebut() + " - " + getMoisFin() + "." + getAnneeFin();
    }

    public String getMoisDebut() {
        return periodeDebut.getMois();
    }

    public String getMoisFin() {
        return periodeFin.getMois();
    }

    public String getAnneeDebut() {
        return periodeDebut.getAnnee();
    }

    public String getAnneeFin() {
        return periodeFin.getAnnee();
    }

    /**
     * @return the periodeDebut
     */
    public Date getPeriodeDebut() {
        return periodeDebut;
    }

    /**
     * @return the periodeFin
     */
    public Date getPeriodeFin() {
        return periodeFin;
    }

    /**
     * @return Retourne la période de début au format globaz, soit MM.yyyy.
     */
    public String getPeriodeDebutAsValue() {
        return getAnneeDebut() + getMoisDebut();
    }

    /**
     * @return Retourne la période de fin au format globaz, soit MM.yyyy.
     */
    public String getPeriodeFinAsValue() {
        return getAnneeFin() + getMoisFin();
    }

    /**
     * Retourne une liste de dates représentant le début de chaque mois.
     * 
     * @return Liste de mois sous forme de tableaux de dates
     */
    public List<Date> getMois() {
        return Periode.getMois(periodeDebut, periodeFin);
    }

    public int getNombreAnnees() {
        return Periode.getNombreAnnees(periodeDebut, periodeFin);
    }

    /**
     * Retourne le nombre de mois séparant le date de début et la date de fin.
     * <ul>
     * <li>Une période allant du 01.2015 au 01.2015 retournera 0.
     * <li>Une période allant du 01.2015 au 03.2015 retournera 2.
     * </ul>
     * 
     * @return Nombre de mois
     */
    public int getNombreMois() {
        return Periode.getNombreMois(periodeDebut, periodeFin);
    }

    public boolean isTrimestriel() {
        return getNombreMois() == 2;
    }

    public boolean isMensuelle() {
        return getNombreMois() == 0;
    }

    /**
     * Retourne la période de début au format "mm.YYYY".
     * 
     * @return String représentant la période de début au fomrat suisse
     */
    public String getPeriodeDebutAsSwissValue() {
        return getMoisDebut() + "." + getAnneeDebut();
    }

    /**
     * Retourne la date de fin au format "mm.YYYY".
     * 
     * @return String représentant la période de fin au format suisse
     */
    public String getPeriodeFinAsSwissValue() {
        return getMoisFin() + "." + getAnneeFin();
    }

    /**
     * Retourne le premier jour de l'année de la date de début.
     * 
     * @return Nouvelle date représentant la date de l'année (05.10.2014 -> 01.01.2014)
     */
    public Date getFirstOfYearOfDateDebut() {
        return periodeDebut.getDateOfFirstDayOfYear();
    }

    /**
     * Retourne le premier jour de l'année de la date de fin.
     * 
     * @return Nouvelle date représentant le premier jour de l'année (05.10.2014 -> 01.01.2014)
     */
    public Date getFirstOfYearOfDateFin() {
        return periodeFin.getDateOfFirstDayOfYear();
    }

    /**
     * Retourne si la période s'étend sur plus d'un mois.
     * 
     * @return true si elle s'étend sur plus d'un mois
     */
    public boolean isLongerThanOneMonth() {
        return getNombreMois() > 0;
    }

    /**
     * Retourne la période de début au premier jour du mois.
     * Par exemple : 01.10.2014
     * 
     * @return String représentant une date de fin
     */
    public String getPeriodeDebutWithDay() {
        return periodeDebut.getFirstDayOfMonth().getSwissValue();
    }

    /**
     * Retourne la période de fin au dernier jour du mois.
     * Par exemple : 31.10.2014
     * 
     * @return String représentant une date de fin
     */
    public String getPeriodeFinWithDay() {
        return periodeFin.getLastDayOfMonth().getSwissValue();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((periodeDebut == null) ? 0 : periodeDebut.hashCode());
        result = prime * result + ((periodeFin == null) ? 0 : periodeFin.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PeriodeMensuelle other = (PeriodeMensuelle) obj;
        if (periodeDebut == null) {
            if (other.periodeDebut != null) {
                return false;
            }
        } else if (!periodeDebut.equals(other.periodeDebut)) {
            return false;
        }
        if (periodeFin == null) {
            if (other.periodeFin != null) {
                return false;
            }
        } else if (!periodeFin.equals(other.periodeFin)) {
            return false;
        }
        return true;
    }

    /**
     * Retourne le mois en toute lettre et l'année de la période de début.
     * Par exemple : avril 2014, juin 2016, septembre 2013
     * 
     * @return String représentant le mois et l'année de la période de début.
     */
    public String getPeriodeDebutMoisAnnee(Locale locale) {
        StringBuilder sb = new StringBuilder();
        sb.append(periodeDebut.getMois(locale));
        sb.append(" ");
        sb.append(periodeDebut.getAnnee());

        return sb.toString();
    }

    /**
     * Retourne si la période est sur la même année.
     */
    public boolean isMemeAnnee() {
        return periodeDebut.isMemeAnnee(periodeFin);
    }

}
