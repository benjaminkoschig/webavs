package ch.globaz.common.domaine;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Red�finition de la classe Date afin qu'elle corresponde � la d�finition d'un
 * ValueObject (immutable)
 * 
 */
public class Date implements Comparable<Date> {
    private static final long serialVersionUID = 1L;

    public static final String DATE_INCONNU = "????";

    private static final String EXCEPTION_DATE_NULL = "La date ne peut �tre null";
    private static final String EXCEPTION_DATE_FORMATS_RESPECT = "La date '%s' doit respecter un des formats suivants : yyyyMMdd ; dd.MM.yyyy ; MM.yyyy ; yyyyMM ou n'est pas une date valable !";
    private static final String EXCEPTION_DATE_FORMATS_RESPECT_ONE = "La date '%s' doit respecter le format suivants : %s ou n'est pas une date valable !";

    public static final String DATE_PATTERN = "yyyyMMdd";
    public static final String DATE_PATTERN_SWISS = "dd.MM.yyyy";
    public static final String DATE_PATTERN_MONTH = "yyyyMM";
    public static final String DATE_PATTERN_MONTH_SWISS = "MM.yyyy";
    public static final String DATE_PATTERN_ddMMyyyy = "ddMMyyyy";

    private static final String NULL_DATE_ALIAS = "";

    private static final String PATTERN_DAY = "dd";
    private static final String PATTERN_YEAR = "yyyy";
    private static final String PATTERN_MONTH = "MM";

    private static final int LENGTH_MONTH_DATE = 6;
    private static final int LENGTH_SWISS_DATE = 10;

    public static final int MOIS_JANVIER = 1;
    public static final int MOIS_FEVRIER = 2;
    public static final int MOIS_MARS = 3;
    public static final int MOIS_AVRIL = 4;
    public static final int MOIS_MAI = 5;
    public static final int MOIS_JUIN = 6;
    public static final int MOIS_JUILLET = 7;
    public static final int MOIS_AOUT = 8;
    public static final int MOIS_SEPTEMBRE = 9;
    public static final int MOIS_OCTOBRE = 10;
    public static final int MOIS_NOVEMBRE = 11;
    public static final int MOIS_DECEMBRE = 12;

    public static final int TRIMESTRE_1 = 1;
    public static final int TRIMESTRE_2 = 2;
    public static final int TRIMESTRE_3 = 3;
    public static final int TRIMESTRE_4 = 4;

    private static final String REGEX_SWISS_MONTH_DATE = "^(0[1-9]|1[0-2]).[0-9][0-9]{3}$";
    protected String pattern;

    protected final java.util.Date date;

    public static final String UNDEFINED_DATE_DEBUT = "01.01.1990";

    /**
     * Cr�ation d'un date � partir de la date du jour
     */
    public static Date now() {
        return new Date();
    }

    /**
     * Retourne une date au premier jour de l'ann�e pass�e en param�tre.
     * 
     * @return Le premier jour de l'ann�e (p.ex : 2014 -> 01.01.2014)
     */
    public static Date getFirstDayOfYear(int annee) {
        return new Date(annee + "0101");
    }

    /**
     * Retourne une date au dernier jour de l'ann�e pass�e en param�tre.
     * 
     * @return Le dernier jour de l'ann�e (p.ex : 2014 -> 31.12.2014)
     */
    public static Date getLastDayOfYear(int annee) {
        return new Date(annee + "1231");
    }

    /**
     * Cr�ation d'un date � partir de la date du jour
     */
    public Date() {
        date = fetchRealDate(new java.util.Date());
    }

    /**
     * Cr�ation d'une date � partir d'un objet {@link java.util.Date} La date
     * est transform� en String avant d'�tre converti en objet m�tier date afin
     * d'�viter les probl�mes inh�rantes aux heures, minutes et secondes de la
     * date.
     * 
     * @param date
     */
    public Date(final java.util.Date date) {
        this.date = fetchRealDate(date);
    }

    /**
     * Cr�ation d'une date � partir d'un objet {@link java.lang.long} La date
     * est transform� en String avant d'�tre converti en objet m�tier date afin
     * d'�viter les probl�mes inh�rantes aux heures, minutes et secondes de la
     * date.
     * 
     * @param date
     */
    public Date(final Long date) {
        this.date = fetchRealDate(new java.util.Date(date));
    }

    /**
     * Supprime les informations suppl�mentaires qui ne devraient pas �tre
     * contenues dans une date (millisecondes, secondes, minutes, heures).
     * 
     * @param date
     *            Une date standard
     * @return Date sans millisecondes, secondes, minutes, heures
     */
    private java.util.Date fetchRealDate(final java.util.Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

    protected String changeDate(String date) {
        return date;
    }

    public Date(String date, final String pattern) {
        this.pattern = pattern;
        if (isNull(date)) {
            throw new IllegalArgumentException(EXCEPTION_DATE_NULL);
        }
        final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        date = changeDate(date);
        if (isDateOf(date, dateFormat)) {
            try {
                this.date = dateFormat.parse(date);
            } catch (ParseException e) {
                throw new IllegalArgumentException(String.format(EXCEPTION_DATE_FORMATS_RESPECT_ONE, date, pattern));
            }
        } else {
            throw new IllegalArgumentException(String.format(EXCEPTION_DATE_FORMATS_RESPECT_ONE, date, pattern));
        }
    }

    /**
     * Cr�ation d'une date � partir d'une cha�ne de caract�res les formats
     * d�finis
     * 
     * @param date
     *            Date au format "yyyyMMdd"
     * @throws IllegalArgumentException()
     */
    public Date(String date) {
        date = changeDate(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        SimpleDateFormat dateFormatSwiss = new SimpleDateFormat(DATE_PATTERN_SWISS);
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat(DATE_PATTERN_MONTH);
        SimpleDateFormat dateFormatSwissMonth = new SimpleDateFormat(DATE_PATTERN_MONTH_SWISS);

        if (isNull(date)) {
            throw new IllegalArgumentException(EXCEPTION_DATE_NULL);
        }

        try {
            if (isDateOf(date, dateFormat)) {
                this.date = dateFormat.parse(date);
            } else if (isDateOf(date, dateFormatSwiss)) {
                this.date = dateFormatSwiss.parse(date);
            } else if (isDateOf(date, dateFormatSwissMonth)) {
                this.date = dateFormatSwissMonth.parse(date);
            } else if (isDateOf(date, dateFormatMonth)) {
                this.date = dateFormatMonth.parse(date);
            } else {
                throw new IllegalArgumentException(String.format(EXCEPTION_DATE_FORMATS_RESPECT, date));
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format(EXCEPTION_DATE_FORMATS_RESPECT, date));
        }
    }

    /***
     * @see Date#after(Date)
     */
    public boolean after(final Date date) {
        return this.date.after(date.getDate());
    }

    /***
     * @see Date#after(Date)
     */
    public boolean afterOrEquals(final Date date) {
        return this.date.after(date.getDate()) || this.date.equals(date.getDate());
    }

    /**
     * Retourne si la date est inf�rieure ou �gal � la date pass�e en param�tre.
     * 
     * @param date Date � comparer
     * @return true si inf�rieure ou �gal
     */
    public boolean beforeOrEquals(final Date date) {
        return before(date) || equals(date);
    }

    /***
     * @see Date#before(Date)
     */
    public boolean before(final Date date) {
        return this.date.before(date.getDate());
    }

    public boolean before(final long time) {
        return time >= getDate().getTime();
    }

    /**
     * Red�finition de la m�thode equals afin qu'elle compare l'objet {@link java.util.Date} encapsul�.
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Date) {
            Date date = (Date) obj;
            return this.date.equals(date.getDate());
        }
        return false;
    }

    public boolean equalsByMonthYear(final Object obj) {
        if (obj instanceof Date) {
            Date date = (Date) obj;
            return getSwissMonthValue().equals(date.getSwissMonthValue());
        }
        return false;
    }

    /**
     * Retourne un objet {@link java.util.Date}
     * 
     * @return La {@link java.util.Date} encapsul�e dans l'objet
     */
    public java.util.Date getDate() {
        return date;
    }

    /**
     * Retour la date au format standard GLOBAZ
     * 
     * @return String au format yyyyMMdd
     */
    public String getValue() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Date.DATE_PATTERN);
        return dateFormat.format(date);
    }

    /**
     * Retour la date au format standard GLOBAZ
     * 
     * @return String au format yyyyMM
     */
    public String getValueMonth() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Date.DATE_PATTERN_MONTH);
        return dateFormat.format(date);
    }

    /**
     * Retour la date au format standard Swiss
     * 
     * @return String au format dd.MM.yyyy
     */
    public String getSwissValue() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Date.DATE_PATTERN_SWISS);
        return dateFormat.format(date);
    }

    /**
     * Retour la date au format standard Swiss
     * 
     * @return String au format MM.yyyy
     */
    public String getSwissMonthValue() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Date.DATE_PATTERN_MONTH_SWISS);
        return dateFormat.format(date);
    }

    /***
     * @see java.util.Date#getTime()
     */
    public long getTime() {
        return date.getTime();
    }

    /**
     * @see java.util.Date#hashCode()
     */
    @Override
    public int hashCode() {
        return date.hashCode();
    }

    /**
     * Retourne si la String pass�e en param�tre correspond � une date au format suisse (dd.MM.yyyy).
     * 
     * @param date Date � v�rifier
     * @return true si le format est respect�
     */
    private static boolean isSwissDate(final String date) {
        if (date.length() != LENGTH_SWISS_DATE) {
            return false;
        }

        try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN_SWISS);
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Retourne si la String pass�e en param�tre correspond � une date au format globaz (yyyyMMdd).
     * 
     * @param date Date � v�rifier
     * @return true si le format est respect�.
     */
    public static boolean isGlobazDate(final String date) {
        try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
            dateFormat.setLenient(false);
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Retourne si le String pass�e en param�tre correspond � une date valide du SimpleDateFormat
     * 
     * @param date Date au format string
     * @param sdf SimpleDateFormat sur lequel effectuer le contr�le
     * @return true si valide
     * @throws ParseException
     */
    private static boolean isDateOf(final String date, final SimpleDateFormat sdf) {
        try {
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Retourne si la String pass�e en param�tre correspond � une date sous forme de mois suisse (MM.yyyy)
     * 
     * @param date Date � v�rifier
     * @return true si le format est respect�
     */
    protected static boolean isSwissMonthDate(final String date) {
        Pattern p = Pattern.compile(REGEX_SWISS_MONTH_DATE);
        boolean isValid = p.matcher(date).matches();

        if (!isValid) {
            return false;
        }

        try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN_MONTH_SWISS);
            dateFormat.setLenient(false);
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Retourne si la String pass�e en param�tre correspond � une date sous forme de mois globaz (yyyyMM)
     * 
     * @param date Date � v�rifier
     * @return true si le format est respect�
     */
    protected static boolean isMonthDate(final String date) {
        if (date.length() != LENGTH_MONTH_DATE) {
            return false;
        }

        try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN_MONTH);
            dateFormat.setLenient(false);
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return getSwissValue();
    }

    /**
     * Retourne si le String pass� en param�tre est une date valide
     * 
     * @param date
     *            String repr�sentant une date
     * @return true si la date est valide
     */
    public static boolean isValid(final String date) {
        if (isNull(date)) {
            return false;
        }

        if (isGlobazDate(date)) {
            return true;
        } else if (isSwissDate(date)) {
            return true;
        } else if (isSwissMonthDate(date)) {
            return true;
        } else {
            return isMonthDate(date);
        }
    }

    public static boolean isNull(final String date) {
        if (date == null || Date.NULL_DATE_ALIAS.equals(date)) {
            return true;
        }
        return false;
    }

    public String getMois(Locale locale) {
        return new DateFormatSymbols(locale).getMonths()[getNumeroMois() - 1];
    }

    /**
     * Retourne une cha�ne de caract�res correspondant au mois au format mm
     * 
     * @return un string repr�sentant l'ann�e (ex : 12 pour d�cembre)
     */
    public String getMois() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_MONTH);
        return dateFormat.format(getDate());
    }

    /**
     * Retourne une cha�ne de caract�res correspondant au mois au format mm
     * 
     * @return un string repr�sentant l'ann�e (ex : 12 pour d�cembre)
     */
    public String getJour() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_DAY);
        return dateFormat.format(getDate());
    }

    /**
     * Retourne une cha�ne de caract�res correspondant � l'ann�e au format YYYY
     * 
     * @return Un string repr�sentant l'ann�e (ex: 2014)
     */
    public String getAnnee() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_YEAR);
        return dateFormat.format(getDate());
    }

    /**
     * Retourne une cha�ne de caract�res correspondant � l'ann�e au format YYYY
     * 
     * @return Un string repr�sentant l'ann�e (ex: 2014)
     */
    public int getYear() {
        Calendar calPeriode = Calendar.getInstance();
        calPeriode.setTime(date);
        return calPeriode.get(Calendar.YEAR);
    }

    /**
     * Retourne le num�ro du mois courant.
     * 
     * @see Calendar#MONTH
     * @return un chiffre entre 1 et 12, 1 correspondant � janvier.
     */
    public int getNumeroMois() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * Retourne si le mois pass� est trimestriel. Soit Mars, Juin, Septembre ou
     * d�cembre.
     * 
     * @return true si trimestriel
     */
    public boolean isMoisTrimestriel() {
        int numeroMois = getNumeroMois();
        return (numeroMois == MOIS_MARS) || (numeroMois == MOIS_JUIN) || (numeroMois == MOIS_SEPTEMBRE)
                || (numeroMois == MOIS_DECEMBRE);
    }

    /**
     * Ajoute un certain nombre de mois � la date actuelle et retourne une nouvelle date.
     * 
     * @param i Nombre de mois � ajouter
     * @return Retourne une nouvelle date
     */
    public Date addMonth(final int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        calendar.add(Calendar.MONTH, i);
        return new Date(calendar.getTime());
    }

    /**
     * Retourne si la date est une date annuelle ou non. Soit si elle est situ�e au 12�me mois (d�cembre)
     * 
     * @return true si annuelle
     */
    public boolean isMoisAnnuel() {
        int numeroMois = getNumeroMois();
        if (numeroMois == MOIS_DECEMBRE) {
            return true;
        }
        return false;
    }

    /**
     * Retourne la date du premier jour de l'ann�e pour la date actuelle
     * 
     * @return Nouvel objet date. Par exemple ("10.12.2014 -> 01.12.2014")
     */
    public Date getDateOfFirstDayOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 0);

        return new Date(calendar.getTime());
    }

    /**
     * Ajout un certain nombre de jours � la date actuelle et retourne une nouvelle date.
     * 
     * @param i Nombre de jours � rajouter
     * @return Retourne un nouvel objet date
     */
    public Date addDays(final int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        calendar.add(Calendar.DAY_OF_MONTH, i);
        return new Date(calendar.getTime());
    }

    /**
     * Ajoute un certain nombre d'ann�es � la date actuelle et retourne une nouvelle date
     * 
     * @param i Nombre d'ann�e � rajouter
     * @return Retourne un nouvel objet date
     */
    public Date addYear(final int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        calendar.add(Calendar.YEAR, i);
        return new Date(calendar.getTime());
    }

    /**
     * Retourne une date au dernier jour du mois par rapport � la date actuel.
     * 
     * @return Le dernire jour du mois (p.ex : 05.10.2014 -> 31.10.2014)
     */
    public Date getLastDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new Date(calendar.getTime());
    }

    /**
     * Retourne une date au premier jour du mois par rapport � la date actuel.
     * 
     * @return Le premier jour du mois (p. ex : 05.10.2014 -> 01.10.2014)
     */
    public Date getFirstDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return new Date(calendar.getTime());
    }

    /**
     * Retourne une date au premier lundi du mois par rapport � la date actuel.
     * 
     * @return Le premier lundi du mois(p. ex: 03.07.2015 -> 06.07.2015)
     */
    public Date getFirstMonday() {
        return getFirstDay(Calendar.MONDAY);
    }

    Date getFirstDay(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.YEAR, getYear());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        while (calendar.get(Calendar.DAY_OF_WEEK) != day) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return new Date(calendar.getTime());
    }

    /**
     * Retourne la p�riode suivant la date actuelle.
     * Par exemple : "01.12.2015" retournera 1 pour la p�riode allant de janvier � mars.
     * 
     * <ul>
     * <li>1 pour la p�riode allant de janvier � mars
     * <li>2 pour la p�riode allant d'avril � juin
     * <li>3 pour la p�riode allant de juillet � septembre
     * <li>4 pour la p�riode allant d'octobre � d�cembre
     * 
     * @return La p�riode suivant la date actuelle.
     */
    public int getCurrentPeriodeTrimestrielle() {
        int numeroMois = getNumeroMois();
        switch (numeroMois) {
            case MOIS_JANVIER:
            case MOIS_FEVRIER:
            case MOIS_MARS:
                return TRIMESTRE_1;
            case MOIS_AVRIL:
            case MOIS_MAI:
            case MOIS_JUIN:
                return TRIMESTRE_2;
            case MOIS_JUILLET:
            case MOIS_AOUT:
            case MOIS_SEPTEMBRE:
                return TRIMESTRE_3;
            case MOIS_OCTOBRE:
            case MOIS_NOVEMBRE:
            case MOIS_DECEMBRE:
                return TRIMESTRE_4;
            default:
                throw new IllegalStateException("Le num�ro de mois " + numeroMois + " n'est pas existant");
        }
    }

    /**
     * Retourne si la date est une p�riode et non pas une date compl�te.
     * 
     * @return true si periode
     */
    public static boolean isPeriode(final String value) {
        return isSwissMonthDate(value) || isMonthDate(value);
    }

    /**
     * Retourne le dernier jour du mois par rapport � une date.
     * 
     * @param value String repr�sentant une date
     * @return Retourne le dernier jour du mois (p.ex : "201301" => "31.01.2013")
     */
    public static Date lastDayOfMonth(final String value) {
        Date date = new Date(value);
        return date.getLastDayOfMonth();
    }

    /***
     * Retourne le mois �crit en toute lettre � partir du num�ro de mois pass� en param�tre.
     * <ul>
     * <li>1 --> Janvier
     * <li>2 --> F�vrier
     * <li>etc...
     * 
     * @param i Le num�ro de mois
     * @return String repr�sentant le mois en toute lettre (p.ex : janvier)
     */
    public static String getMonthName(final int i, Locale locale) {
        if (locale == null) {
            throw new NullPointerException("La locale ne peut �tre null");
        }
        if (i < 1 || i > 12) {
            throw new IllegalArgumentException("Le num�ro de mois doit �tre entre 1 et 12");
        }
        return new DateFormatSymbols(locale).getMonths()[i - 1];
    }

    /**
     * Retourne l'ann�e courante.
     * 
     * @return int repr�sentant l'ann�e actuelle
     */
    public static int getCurrentYear() {
        Date date = Date.now();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date.getDate());
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Retourne une nouvelle date en changeant le mois actuel avec le mois pass� en param�tre.
     * Janvier correspondant � 0 et D�cembre � 12.
     */
    public Date getMois(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        calendar.set(Calendar.MONTH, month - 1);
        return new Date(calendar.getTime());
    }

    public String getAnneeMois() {
        StringBuilder sb = new StringBuilder();
        sb.append(getAnnee());
        sb.append(getMois());
        return sb.toString();
    }

    /**
     * Retourne le mois et l'ann�e formatt� au format MM.yyyy
     * 
     * @return String repr�sentant l'ann�e et le mois formatt�.
     */
    public String getMoisAnneeFormatte() {
        StringBuilder sb = new StringBuilder();
        sb.append(getMois());
        sb.append(".");
        sb.append(getAnnee());
        return sb.toString();
    }

    public boolean isSunday() {
        return isDayName(Calendar.SUNDAY);
    }

    boolean isDayName(int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) == day;
    }

    /**
     * 
     * @param dateDeDebut
     * @param dateDeFin
     * @return
     */
    public int getNbDaysBetween(Date dateDeComparaison) {
        if (dateDeComparaison == null) {
            return -1;
        }

        long greaterDate = dateDeComparaison.getTime();
        long smallerDate = date.getTime();

        // Si la date actuel est plus grande la date de comparaison, on inverse les roles
        if (after(dateDeComparaison)) {
            greaterDate = date.getTime();
            smallerDate = dateDeComparaison.getTime();
        }

        double msDiff = greaterDate - smallerDate;
        return (int) Math.round(msDiff / (1000 * 60 * 60 * 24));
    }

    /***
     * @see Date#compareTo(Date)
     */
    @Override
    public int compareTo(Date o) {
        return date.compareTo(o.getDate());
    }
}
