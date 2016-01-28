package globaz.ij.calendar;

import globaz.globall.util.JADate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Descpription
 * 
 * <p>
 * Creation d'une semaine IJ. Une semaine IJ comporte une date de début et une date de fin et est composée de jours.
 * </p>
 * 
 * @author scr Date de création 12 sept. 05
 */
public class IJSemaine {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private JADate dateDebut = null;
    private JADate dateFin = null;

    private List listJours = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJSemaine.
     */
    public IJSemaine() {
        listJours = new LinkedList();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param jour
     */
    public void addJour(IJJour jour) {
        listJours.add(jour);
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

    public String getDescription() {
        return dateDebut.toStr(".") + " - " + dateFin.toStr(".");
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public List getListJours() {
        return listJours;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param date
     */
    public void setDateDebut(JADate date) {
        dateDebut = date;
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
    public String tostring() {
        StringBuffer sb = new StringBuffer();
        sb.append(getDateDebut() + " - " + getDateFin() + " : ");

        for (Iterator iter = listJours.iterator(); iter.hasNext();) {
            IJJour jour = (IJJour) iter.next();
            sb.append(jour.tostring());
        }

        return sb.toString();
    }
}
