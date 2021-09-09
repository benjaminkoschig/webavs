package globaz.ij.acor.adapter;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORException;

/**
 * <H1>Description</H1>
 *
 * @author vre
 */
public class IJAttestationsJoursAdapter {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final JACalendar CALENDAR = new JACalendarGregorian();

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String attestationsJours = "";
    private boolean calendrier;
    private String dateDebutPeriode = "";
    private String dateFinPeriode = "";
    private String nbJoursExternes = "0";
    private String nbJoursInternes = "0";
    private String nbJoursInterruption = "0";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJAttestationsJoursAdapter.
     *
     * @param base
     *            DOCUMENT ME!
     * @param ij
     *            DOCUMENT ME!
     *
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public IJAttestationsJoursAdapter(IJBaseIndemnisation base, IJIJCalculee ij) throws PRACORException {
        try {
            int decalageDebut = 0;
            int decalageFin = 0;

            setDateDebutPeriode(base.getDateDebutPeriode());
            setDateFinPeriode(base.getDateFinPeriode());

            if (CALENDAR.compare(ij.getDateDebutDroit(), base.getDateDebutPeriode()) == JACalendar.COMPARE_FIRSTUPPER) {
                decalageDebut = (int) CALENDAR.daysBetween(base.getDateDebutPeriode(), ij.getDateDebutDroit());
                setDateDebutPeriode(ij.getDateDebutDroit());
            }

            if (!JAUtil.isDateEmpty(ij.getDateFinDroit())
                    && (CALENDAR.compare(ij.getDateFinDroit(), base.getDateFinPeriode()) == JACalendar.COMPARE_FIRSTLOWER)) {
                decalageFin = (int) CALENDAR.daysBetween(ij.getDateFinDroit(), base.getDateFinPeriode());
                setDateFinPeriode(ij.getDateFinDroit());
            }

            // calcul du facteur pour le prorata des jours
            double facteur = 0.0;

            if ((decalageDebut > 0) || (decalageFin > 0)) {
                JADate date = new JADate(base.getDateDebutPeriode());
                int joursMois = CALENDAR.daysInMonth(date.getMonth(), date.getYear());

                facteur = (joursMois - decalageDebut - decalageFin) / ((double) joursMois);

                if (!JadeStringUtil.isIntegerEmpty(base.getNombreJoursInterruption())) {
                    setNbJoursInterruption(String.valueOf((int) Math.round(facteur * toIntOrZero(base.getNombreJoursInterruption()))));
                }
            } else {
                if (!JadeStringUtil.isIntegerEmpty(base.getNombreJoursInterruption())) {
                    setNbJoursInterruption(String.valueOf(toIntOrZero(base.getNombreJoursInterruption())));
                }
            }

            if (JadeStringUtil.isIntegerEmpty(base.getNombreJoursExterne())
                    && JadeStringUtil.isIntegerEmpty(base.getNombreJoursInterne())) {
                calendrier = true;

                // c'est le calendrier qui a ete rempli, on decoupe le
                // calendrier
                String calendrier = base.getAttestationJours();

                if (decalageDebut > 0) {
                    calendrier = calendrier.substring(decalageDebut);
                }

                if (decalageFin > 0) {
                    calendrier = calendrier.substring(0, calendrier.length() - decalageFin);
                }

                setAttestationsJours(calendrier);
                int nbrJoursInterne = 0;
                int nbrJoursExterne = 0;
                for (int i = 0; i < attestationsJours.length(); ++i) {
                    if (IIJBaseIndemnisation.IJ_CALENDAR_INTERNE.equals(String.valueOf(attestationsJours.charAt(i)))) {
                        ++nbrJoursInterne;
                    } else if (IIJBaseIndemnisation.IJ_CALENDAR_EXTERNE.equals(String.valueOf(attestationsJours
                                                                                                      .charAt(i)))) {
                        ++nbrJoursExterne;
                    }
                }
                setNbJoursExternes(String.valueOf(nbrJoursExterne));
                setNbJoursInternes(String.valueOf(nbrJoursInterne));

            } else {
                calendrier = false;

                // c'est le nombre de jours qui a ete renseigne, on calcule au
                // prorata
                if ((decalageDebut > 0) || (decalageFin > 0)) {

                    int joursExt = toIntOrZero(base.getNombreJoursExterne());
                    int joursInt = toIntOrZero(base.getNombreJoursInterne());

                    if (!JadeStringUtil.isEmpty(base.getNombreJoursInterne())) {
                        joursInt = (int) Math.round(facteur * joursInt);
                    }

                    if (!JadeStringUtil.isEmpty(base.getNombreJoursExterne())) {
                        joursExt = (int) Math.round(facteur * joursExt);
                    }

                    setNbJoursExternes(String.valueOf(joursExt));
                    setNbJoursInternes(String.valueOf(joursInt));
                } else {
                    setNbJoursExternes(base.getNombreJoursExterne());
                    setNbJoursInternes(base.getNombreJoursInterne());
                }
            }
        } catch (Exception e) {
            throw new PRACORException("impossible de découper le calendrier", e);
        }
    }

    private int toIntOrZero(final String value) {
        if (JadeStringUtil.isBlankOrZero(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut attestations jours.
     *
     * @return la valeur courante de l'attribut attestations jours
     */
    public String getAttestationsJours() {
        return attestationsJours;
    }

    /**
     * getter pour l'attribut date debut periode.
     *
     * @return la valeur courante de l'attribut date debut periode
     */
    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    /**
     * getter pour l'attribut date fin periode.
     *
     * @return la valeur courante de l'attribut date fin periode
     */
    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    /**
     * getter pour l'attribut nb jours externes.
     *
     * @return la valeur courante de l'attribut nb jours externes
     */
    public String getNbJoursExternes() {
        return nbJoursExternes;
    }

    /**
     * getter pour l'attribut nb jours internes.
     *
     * @return la valeur courante de l'attribut nb jours internes
     */
    public String getNbJoursInternes() {
        return nbJoursInternes;
    }

    /**
     * getter pour l'attribut nb jours interruption.
     *
     * @return la valeur courante de l'attribut nb jours interruption
     */
    public String getNbJoursInterruption() {
        return nbJoursInterruption;
    }

    /**
     * @return
     */
    public boolean isCalendrier() {
        return calendrier;
    }

    /**
     * setter pour l'attribut attestations jours.
     *
     * @param attestationsJours
     *            une nouvelle valeur pour cet attribut
     */
    public void setAttestationsJours(String attestationsJours) {
        this.attestationsJours = attestationsJours;
    }

    /**
     * @param b
     */
    public void setCalendrier(boolean b) {
        calendrier = b;
    }

    /**
     * setter pour l'attribut date debut periode.
     *
     * @param dateDebutPeriode
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutPeriode(String dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    /**
     * setter pour l'attribut date fin periode.
     *
     * @param dateFinPeriode
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    /**
     * setter pour l'attribut nb jours externes.
     *
     * @param nbJoursExternes
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbJoursExternes(String nbJoursExternes) {
        this.nbJoursExternes = nbJoursExternes;
    }

    /**
     * setter pour l'attribut nb jours internes.
     *
     * @param nbJoursInternes
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbJoursInternes(String nbJoursInternes) {
        this.nbJoursInternes = nbJoursInternes;
    }

    /**
     * setter pour l'attribut nb jours interruption.
     *
     * @param nbJoursInterruption
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbJoursInterruption(String nbJoursInterruption) {
        this.nbJoursInterruption = nbJoursInterruption;
    }

}
