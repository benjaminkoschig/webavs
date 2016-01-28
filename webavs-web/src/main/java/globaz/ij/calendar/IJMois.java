package globaz.ij.calendar;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Descpription.
 * 
 * <p>
 * Creation d'un mois IJ. Un mois IJ comporte une date de début et une date de fin et est composée de semaines,
 * elles-mêmes composée de jours.
 * </p>
 * 
 * @author scr Date de création 12 sept. 05
 */

public class IJMois {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // Date de début du mois
    private JADate dateDebut = null;

    // Date de fin du mois
    private JADate dateFin = null;

    // Les semaines contenues dans le mois.
    private List listSemaines = null;
    private BSession session = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJMois.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param month
     *            DOCUMENT ME!
     * @param year
     *            DOCUMENT ME!
     * @param jourDebut
     *            DOCUMENT ME!
     * @param jourFin
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJMois(BSession session, int month, int year, int jourDebut, int jourFin) throws Exception {
        this.session = session;
        listSemaines = new LinkedList();

        dateDebut = new JADate();
        dateFin = new JADate();
        init(month, year, jourDebut, jourFin);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Ajoute une semaine au mois.
     * 
     * @param semaine
     */
    public void addSemaine(IJSemaine semaine) {
        listSemaines.add(semaine);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public JADate getDateDebut() {
        return dateDebut;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public JADate getDateFin() {
        return dateFin;
    }

    /**
     * getter pour l'attribut libelle mois.
     * 
     * @return Description du mois.
     */
    public String getLibelleMois() {
        return JACalendar.getMonthName(getDateDebut().getMonth(), session.getIdLangueISO()) + " - "
                + String.valueOf(getDateDebut().getYear());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public List getListSemaines() {
        return listSemaines;
    }

    /**
     * @return
     */
    public BSession getSession() {
        return session;
    }

    /**
     * Initialise un mois IJ.
     * 
     * @param month
     *            Le mois. Valeur possible : 1 -> 12; 1 == Janvier, 12 == Décembre
     * @param year
     *            L'année
     * @param jourDebut
     *            DOCUMENT ME!
     * @param jourFin
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             En cas d'erreur
     */
    private void init(int month, int year, int jourDebut, int jourFin) throws Exception {
        dateDebut.setDay(jourDebut);
        dateDebut.setMonth(month);
        dateDebut.setYear(year);

        JACalendarGregorian jaCal = new JACalendarGregorian();

        dateFin.setDay(jourFin);
        dateFin.setMonth(month);
        dateFin.setYear(year);

        // Init du calendar avec la date de début du mois.
        Calendar cal = IJCalendarHelper.getCalendarInstance();

        cal.set(Calendar.DAY_OF_MONTH, jourDebut);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);

        int posJoursSemaineDD = cal.get(Calendar.DAY_OF_WEEK);

        // Le mois comporte X jours
        int nombreJoursMois = dateFin.getDay() - dateDebut.getDay() + 1;

        // Le calendrier IJ commence le lundi, alors que le calendrier gregorian
        // commence le dimanche !!!
        // Convertion de la Position du Jour de la Semaine dans le calendrier IJ

        // Lundi : position = 1
        // Mardi : position = 2
        // ...
        posJoursSemaineDD = IJCalendarHelper.convertPJSGregorianToIJCalendar(posJoursSemaineDD);

        int nombreJoursTraite = 0;

        IJSemaine semaine = new IJSemaine();

        JADate dateDebutSemaine = jaCal.addDays(dateDebut, ((posJoursSemaineDD - 1) * (-1)));

        semaine.setDateDebut(dateDebutSemaine);
        semaine.setDateFin(jaCal.addDays(dateDebutSemaine, 6));

        JADate currentDay = jaCal.addDays(dateDebutSemaine, 0);

        // Traitement de la 1ère semaine :
        for (int i = 1; i <= 7; i++) {
            IJJour jour = new IJJour();

            if (i < posJoursSemaineDD) {
                jour.setDate(currentDay);
                jour.setValeur(IJJour.VALEUR_EMPTY);
                jour.setActif(false);
            } else {
                jour.setDate(currentDay);
                jour.setValeur(IJJour.VALEUR_EMPTY);
                jour.setActif((jaCal.compare(dateDebut, currentDay) != JACalendar.COMPARE_FIRSTUPPER)
                        && (jaCal.compare(currentDay, dateFin) != JACalendar.COMPARE_FIRSTUPPER));
                nombreJoursTraite++;
            }

            semaine.addJour(jour);
            currentDay = jaCal.addDays(currentDay, 1);
        }

        // Ajout de la 1ere semaine au mois
        addSemaine(semaine);

        // Traitement des semaines suivantes
        while (nombreJoursMois > nombreJoursTraite) {
            semaine = new IJSemaine();
            semaine.setDateDebut(currentDay);
            semaine.setDateFin(jaCal.addDays(currentDay, 6));

            for (int i = 1; i <= 7; i++) {
                IJJour jour = new IJJour();

                if (nombreJoursTraite < nombreJoursMois) {
                    jour.setDate(currentDay);
                    jour.setValeur(IJJour.VALEUR_EMPTY);
                    jour.setActif(true);
                    nombreJoursTraite++;
                } else {
                    jour.setDate(currentDay);
                    jour.setValeur(IJJour.VALEUR_EMPTY);
                    jour.setActif(false);
                }

                semaine.addJour(jour);
                currentDay = jaCal.addDays(currentDay, 1);
            }

            addSemaine(semaine);
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param date
     */
    public void setDateFin(JADate date) {
        dateFin = date;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(getLibelleMois()).append("\n");

        for (Iterator iter = listSemaines.iterator(); iter.hasNext();) {
            IJSemaine sem = (IJSemaine) iter.next();

            sb.append(sem.tostring()).append("\n");
        }

        return sb.toString();
    }
}
