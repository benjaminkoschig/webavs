package ch.globaz.vulpecula.domain.models.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * D�finit un espace de temps entre deux dates
 */
public class Periode implements Comparable<Periode>, ValueObject {
    private static final long serialVersionUID = 1L;
    private final Date dateDebut;
    private final Date dateFin;

    public enum ComparaisonPeriode {
        INDEPENDANTE,
        CHEVAUCHE,
    }

    public Periode(final Date dateDebut) {
        this(dateDebut, null);
    }

    public Periode(final Date dateDebut, final Date dateFin) {
        checkValidityAndThrowExceptionIfNot(dateDebut, dateFin);
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public Periode(final String dateDebutAsString, final String dateFinAsString) {
        Date dateDebut = null, dateFin = null;

        dateDebut = new Date(dateDebutAsString);
        if (dateFinAsString != null && !dateFinAsString.equals("")) {
            dateFin = new Date(dateFinAsString);
        }

        checkValidityAndThrowExceptionIfNot(dateDebut, dateFin);

        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    private void checkValidityAndThrowExceptionIfNot(final Date dateDebut, final Date dateFin) {
        if (!Periode.isValid(dateDebut, dateFin)) {
            throw new IllegalArgumentException("La date de d�but (" + dateDebut
                    + ") doit �tre ant�rieure � la date de fin (" + dateFin + ")");
        }
    }

    /**
     * Retourne la date de d�but d'une p�riode
     * 
     * @return {@link Date}
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * Retourne l'ann�e de d�but de la p�riode
     */
    public Annee getAnneeDebut() {
        return new Annee(dateDebut.getAnnee());
    }

    /**
     * Retourne l'ann�e de la fin de la p�riode
     * 
     * @return null si la date de fin est null
     */
    public Annee getAnneeFin() {
        if (dateFin == null) {
            return null;
        }
        return new Annee(dateFin.getAnnee());
    }

    /**
     * Retourne la date de fin d'une p�riode
     * 
     * {@link Date}
     */
    public Date getDateFin() {
        return dateFin;
    }

    public String getDateDebutAsSwissValue() {
        if (dateDebut != null) {
            return dateDebut.getSwissValue();
        }
        return null;
    }

    public String getDateFinAsSwissValue() {
        if (dateFin != null) {
            return dateFin.getSwissValue();
        }
        return null;
    }

    public static boolean isValid(final String dateDebut, final String dateFin) {
        try {
            new Periode(dateDebut, dateFin);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }

    public static boolean isValid(final Date dateDebut, final Date dateFin) {
        // FIXME: Est-ce que l'on consid�re que la p�riode est valide lorsque la
        // date d�but est null ET que la date de fin aussi ?
        if (dateDebut == null && dateFin == null) {
            return false;
        }
        if (dateDebut == null) {
            return false;
        }
        if (dateFin == null) {
            return true;
        }

        return dateDebut.before(dateFin) || dateDebut.equals(dateFin);
    }

    private ComparaisonPeriode compare(final Periode periode) {
        Date dateDebutAutrePeriode = periode.getDateDebut();
        Date dateFinAutrePeriode = periode.getDateFin();

        switch (compareTo(periode)) {
            case 0:
                return ComparaisonPeriode.CHEVAUCHE;
            case -1:
                // cas o� la date de d�but de cette p�riode est plus petite (plus
                // ancienne) que celle de l'autre p�riode
                // ou que les dates de d�but sont �gales et que la date de fin est
                // plus petite (plus ancienne)
                // que celle de l'autre p�riode
                if (dateFin == null) {
                    return ComparaisonPeriode.CHEVAUCHE;
                }
                if (dateDebut.equals(dateDebutAutrePeriode)) {
                    // si les dates de d�but sont les m�mes, il y a obligatoirement
                    // chevauchement
                    return ComparaisonPeriode.CHEVAUCHE;
                }
                if (dateFin.after(dateDebutAutrePeriode)) {
                    return ComparaisonPeriode.CHEVAUCHE;
                }
                break;
            case 1:
                // cas o� la date de d�but de cette p�riode est plus grande (plus
                // r�cente) que celle de l'autre p�riode
                // ou que les dates de d�but sont �gales et que la date de fin est
                // plus grande (plus r�cente)
                // que celle de l'autre p�riode
                if (dateDebut.equals(dateDebutAutrePeriode)) {
                    return ComparaisonPeriode.CHEVAUCHE;
                } else {
                    if (dateFinAutrePeriode == null) {
                        return ComparaisonPeriode.CHEVAUCHE;
                    }
                    if (dateDebut.before(dateFinAutrePeriode)) {
                        return ComparaisonPeriode.CHEVAUCHE;
                    }
                }
                break;
        }
        return ComparaisonPeriode.INDEPENDANTE;
    }

    public boolean chevauche(final Periode periode) {
        return compare(periode) == ComparaisonPeriode.CHEVAUCHE;
    }

    /**
     * Retourne la p�riode chevauchant la p�riode actuel gr�ce � une liste de p�riodes.
     * Un tri est effectu� avec de d�terminer le chevauchement. Seule la premi�re p�riode chevauchante est retourn�e.
     * Si aucune p�riode ne chevauche la p�riode actuelle, null est retourn�.
     * 
     * @param periodes Liste de p�riodes sur lesquels effectuer la comparaison
     * @return Une des p�riodes chevauchant la p�riode actuelle, ou null si inexistante
     */
    public Periode chevauche(final List<Periode> periodes) {
        Collections.sort(periodes);
        for (Periode periode : periodes) {
            if (periode.chevauche(this)) {
                return periode;
            }
        }
        return null;
    }

    /**
     * Retourne une map contenant les p�riodes qui se chevauchent par rapport � une liste de p�riodes.
     * La liste est d'abord trier, puis, pour chaque p�riode on recherche si il y a un chevauchement avec la p�riode
     * suivante.
     * 
     * @param periodes Liste de p�riode sur lesquels effectuer la recherche de chevauchement.
     * @return
     */
    public static Map<Periode, Periode> seChevauchent(final List<Periode> periodes) {
        Collections.sort(periodes);
        Map<Periode, Periode> periodesSeChavauchant = new HashMap<Periode, Periode>();
        for (int i = 0; i < periodes.size(); i++) {
            Periode periode = periodes.get(i);
            if (i < periodes.size() - 1) {
                Periode periodeSuivante = periodes.get(i + 1);
                if (periode.chevauche(periodeSuivante)) {
                    periodesSeChavauchant.put(periode, periodeSuivante);
                }
            }
        }
        return periodesSeChavauchant;
    }

    @Override
    public int compareTo(final Periode periode) {
        if (equals(periode)) {
            return 0;
        }

        Date dateDebutAutrePeriode = periode.getDateDebut();
        Date dateFinAutrePeriode = periode.getDateFin();

        if (dateFin == null && dateFinAutrePeriode == null) {
            if (dateDebut.before(dateDebutAutrePeriode)) {
                return -1;
            } else {
                return 1;
            }
        }

        if (dateDebut.after(dateDebutAutrePeriode)) {
            return 1;
        } else if (dateDebut.before(dateDebutAutrePeriode)) {
            return -1;
        }

        if (dateFin == null) {
            return 1;
        } else if (dateFinAutrePeriode == null) {
            return -1;
        }
        if (dateFin.after(dateFinAutrePeriode)) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Retourne si la date est contenu de la p�riode (limite incluse)
     * 
     * @param date
     *            � comparer
     * @return true si incluse
     */
    public boolean contains(final Date date) {
        if (date.equals(dateDebut)) {
            return true;
        } else if (date.after(dateDebut) && dateFin == null) {
            return true;
        } else if (date.after(dateDebut) && date.before(dateFin)) {
            return true;
        } else if (date.after(dateDebut) && date.equals(dateFin)) {
            return true;
        }
        return false;
    }

    /**
     * Retourne si la p�riode est contenu dans l'autre p�riode (limite incluse)
     * 
     * @param date
     *            � comparer
     * @return true si incluse
     */
    public boolean contains(Periode periode) {
        if (periode.dateFin == null) {
            return this.contains(periode.dateDebut);
        } else {
            return this.contains(periode.dateDebut) && this.contains(periode.dateFin);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dateDebut == null) ? 0 : dateDebut.hashCode());
        result = prime * result + ((dateFin == null) ? 0 : dateFin.hashCode());
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
        Periode other = (Periode) obj;
        if (dateDebut == null) {
            if (other.dateDebut != null) {
                return false;
            }
        } else if (!dateDebut.equals(other.dateDebut)) {
            return false;
        }
        if (dateFin == null) {
            if (other.dateFin != null) {
                return false;
            }
        } else if (!dateFin.equals(other.dateFin)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getDateDebutAsSwissValue());
        sb.append(" - ");
        if (getDateFin() != null) {
            sb.append(getDateFinAsSwissValue());
        } else {
            sb.append("???");
        }
        return sb.toString();
    }

    /**
     * Retourne si la p�riode est du m�me mois et de la m�me ann�e qu'une autre p�riode.
     * 
     * @param periode P�riode � contr�ler
     * @return true si m�me mois et m�me ann�e.
     */
    public boolean isSameMonthAndYear(Periode periode) {
        String moisDebutOther = periode.getDateDebut().getMois();
        String anneeDebutOther = periode.getDateDebut().getAnnee();

        String moisDebut = getDateDebut().getMois();
        String anneeDebut = getDateDebut().getAnnee();

        return moisDebut.equals(moisDebutOther) && anneeDebut.equals(anneeDebutOther);
    }

    /**
     * Retourne si la p�riode est du m�me mois et de la m�me ann�e
     * 
     * @return true si m�me mois et m�me ann�e.
     */
    public boolean isSameMonthAndYear() {
        if (dateFin == null) {
            return false;
        }
        String moisDebutOther = dateDebut.getMois();
        String anneeDebutOther = dateDebut.getAnnee();

        String moisDebut = dateFin.getMois();
        String anneeDebut = dateFin.getAnnee();

        return moisDebut.equals(moisDebutOther) && anneeDebut.equals(anneeDebutOther);
    }

    /**
     * Retourne si les deux dates composants la p�riode sont de la m�me ann�e.
     * 
     * @return true si m�me ann�e
     */
    public boolean isSameYear() {
        if (dateFin == null) {
            return false;
        }

        String anneeDebut = dateDebut.getAnnee();
        String anneeFin = dateFin.getAnnee();
        return anneeDebut.equals(anneeFin);
    }

    /**
     * Retourne si la p�riode n'a pas de fin.
     * 
     * @return true si la date de fin est null
     */
    public boolean sansFin() {
        return dateFin == null;
    }

    /**
     * Retourne si la p�riode est active par rapport � la date du jour.
     * 
     * @return true si active
     */
    public boolean isActif() {
        Date today = Date.now();
        return dateDebut.beforeOrEquals(Date.now()) && (dateFin == null || dateFin.afterOrEquals(today));
    }

    /**
     * Retourne l'ann�e est contenue dans la p�riode.
     * 
     * <pre>
     * Exemple avec l'ann�e 2014
     * 01.03.2014 - 31.03.2014 --> true
     * 01.02.2010 - 31.03.2014 --> true
     * 01.02.2010 - 31.03.2010 --> false
     * </pre>
     */
    public boolean isActifIn(Annee annee) {
        Annee anneeDebut = getAnneeDebut();
        Annee anneeFin = getAnneeFin();

        if (anneeDebut.isBeforeOrEquals(annee) && (anneeFin == null || anneeFin.isAfterOrEquals(annee))) {
            return true;
        }
        return false;
    }

    public boolean isActifMois(Date date) {
        if (!dateDebut.isMemeMois(date) && dateDebut.after(date)) {
            return false;
        }
        if (dateFin != null && !dateFin.isMemeMois(date) && dateFin.before(date)) {
            return false;
        }
        return true;
    }

    /**
     * Contr�le si la p�riode est active dans la p�riode demand� en poss�dant soit
     * - pas de date de fin mais une date de d�but avant la date de fin de la p�riode demand�
     * ou
     * - une p�riode (avec une date de fin diff�rente de NULL) qui chevauche la p�riode demand�
     * 
     * @return true si la p�riode est active
     * @param periodeDemande Periode dans laquel le p�riode doit avoir une activit�
     */
    public boolean isActif(final Periode periodeDemande) {
        if (getDateFin() != null) {
            return chevauche(periodeDemande);
        } else {
            return !(getDateDebut()).after(periodeDemande.getDateFin());
        }
    }

    public static List<Date> getMois(Date dateDebut, Date dateFin) {
        List<Date> dates = new ArrayList<Date>();

        final Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(dateDebut.getDate());

        int i = 0;
        do {
            dates.add(new Date(startCalendar.getTime()));
            startCalendar.add(Calendar.MONTH, 1);
            i++;
        } while (i <= getNombreMois(dateDebut, dateFin));
        return dates;
    }

    public List<Date> getMois() {
        return Periode.getMois(dateDebut, dateFin);
    }

    public static int getNombreMois(Date dateDebut, Date dateFin) {
        final Calendar startCalendar = Calendar.getInstance();
        final Calendar endCalendar = Calendar.getInstance();

        startCalendar.setTime(dateDebut.getDate());
        endCalendar.setTime(dateFin.getDate());

        int diffMonth = getNombreAnnees(dateDebut, dateFin) * 12 + endCalendar.get(Calendar.MONTH)
                - startCalendar.get(Calendar.MONTH);

        return diffMonth;
    }

    public static int getNombreAnnees(Date dateDebut, Date dateFin) {
        final Calendar startCalendar = Calendar.getInstance();
        final Calendar endCalendar = Calendar.getInstance();

        startCalendar.setTime(dateDebut.getDate());
        endCalendar.setTime(dateFin.getDate());

        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);

        return diffYear;
    }

    public Periode getPeriodeDate1DernierJourDate2PremierJour() {
        if (getDateFin() != null) {
            return new Periode(getDateDebut().getLastDayOfMonth(), getDateFin().getFirstDayOfMonth());
        } else {
            return new Periode(getDateDebut().getLastDayOfMonth());
        }
    }

    public boolean chevaucheOuSuit(Periode periodePrecedente) {
        if (compare(periodePrecedente) == ComparaisonPeriode.CHEVAUCHE) {
            return true;
        }
        int moisPrecedent = periodePrecedente.getDateFin().getNumeroMois();
        int moisCourant = getDateDebut().getNumeroMois();
        moisPrecedent = moisPrecedent + 1;
        if (moisPrecedent == moisCourant) {
            return true;
        }
        return false;
    }

}
