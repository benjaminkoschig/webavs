package globaz.ij.calendar;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;

/**
 * Descpription Définition d'un jour IJ. Un jour IJ contient les informations saisie dans les bases d'indemnisations.
 * Date --> Date du jour Etat --> Actif si le jour fais partie du mois auquel il appartient. Valeur --> Valeur assignée
 * au jour. elles-mêmes composée de jours.</p>
 * 
 * 
 * @author scr Date de création 12 sept. 05
 */
public class IJJour {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /** DOCUMENT ME! */
    public static final String VALEUR_EMPTY = ".";

    /** DOCUMENT ME! */
    public static final String VALEUR_EXTERNE = "2";

    /** DOCUMENT ME! */
    public static final String VALEUR_INTERNE = "1";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private JACalendar cal = null;
    private JADate date = null;
    private boolean isActif = false;
    private String valeur = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJJour.
     */
    public IJJour() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public JACalendar getCal() {
        return cal;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public JADate getDate() {
        return date;
    }

    /**
     * retourne comme clé, le no du jours.
     * 
     * @return la valeur courante de l'attribut key, null si le jour n'est pas actif.
     */
    public String getKey() {
        if (isActif()) {
            return String.valueOf(getDate().getDay());
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getValeur() {
        return valeur;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public boolean isActif() {
        return isActif;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param b
     */
    public void setActif(boolean b) {
        isActif = b;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param calendar
     */
    public void setCal(JACalendar calendar) {
        cal = calendar;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param date
     */
    public void setDate(JADate date) {
        this.date = date;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void setValeur(String string) throws Exception {
        if (string.equalsIgnoreCase(VALEUR_EMPTY) || string.equalsIgnoreCase(VALEUR_EXTERNE)
                || string.equalsIgnoreCase(VALEUR_INTERNE)) {
            valeur = string;
        } else {
            throw new Exception("Valeur indéfinie [ " + string + " ]");
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String tostring() {
        return "[" + ((isActif == true) ? "O" : "X") + "/" + getValeur() + "]";
    }
}
