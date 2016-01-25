package globaz.ij.regles;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une classe pour faciliter la recherche des ij qui concernent une période donnée.
 * </p>
 * 
 * @author vre
 */
public class IJIJCalculeeAdapter {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final JACalendar CALENDAR = new JACalendarGregorian();
    public static final int COMPARE_DESUETE = 1;
    public static final int COMPARE_DIFFERENTES = 2;

    public static final int COMPARE_EQUIVALENTES = 0;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private List ijsList;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJIJCalculeeAdapter.
     * 
     * @param ijsList
     *            DOCUMENT ME!
     */
    public IJIJCalculeeAdapter(List ijsList) {
        this.ijsList = ijsList;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * retourne vrai si les ijs comprises dans la période allant de 'dateDebut' a 'dateFin' donneraient a coup sur des
     * résultats de calculs identiques que ceux qui seraient obtenus avec les ijs contenues dans la liste
     * 'ijsComparaison'.
     * 
     * <p>
     * Note: il est possible que cette methode retourne faux alors que les prestations auraient tout-de-meme ete
     * identique.
     * </p>
     * 
     * @param dateDebut
     *            DOCUMENT ME!
     * @param dateFin
     *            DOCUMENT ME!
     * @param ijsComparaison
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut egales pour calcul
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public int compareIJsPourCalcul(String dateDebut, String dateFin, List ijsComparaison) throws Exception {
        List ijsRef = findIjs(dateDebut, dateFin);

        // s'il n'y a pas d'ijs de ref, cela signifie que la base est desuete.
        if (ijsRef.isEmpty()) {
            return COMPARE_DESUETE;
        }

        // si nombre ij différent, de toutes facons on retourne faux
        if (ijsRef.size() != ijsComparaison.size()) {
            return COMPARE_DIFFERENTES;
        }

        for (ListIterator ijs1 = ijsRef.listIterator(), ijs2 = ijsComparaison.listIterator(); ijs1.hasNext();) {
            IJIJCalculee ij1 = (IJIJCalculee) ijs1.next();
            IJIJCalculee ij2 = (IJIJCalculee) ijs2.next();

            if (!ijs1.hasPrevious()) {
                // si les dates de debut de l'une ou l'autre des premieres ij
                // sont apres la dateDebut, retourner faux
                if ((CALENDAR.compare(ij1.getDateDebutDroit(), dateDebut) != JACalendar.COMPARE_FIRSTLOWER)
                        || (CALENDAR.compare(ij2.getDateDebutDroit(), dateDebut) != JACalendar.COMPARE_FIRSTLOWER)) {
                    return COMPARE_DIFFERENTES;
                }
            }

            if (ijs1.hasNext()) {
                // si on a encore des ijs apres, leurs dates de fin doivent être
                // identiques
                if (CALENDAR.compare(ij1.getDateFinDroit(), ij2.getDateFinDroit()) != JACalendar.COMPARE_EQUALS) {
                    return COMPARE_DIFFERENTES;
                }
            }

            if (!ijs1.hasNext()) {
                // si les dates de fin de l'une ou l'autre des dernieres ij sont
                // avant la dateFin, retourner faux
                if ((!JadeStringUtil.isEmpty(ij1.getDateFinDroit()) && (CALENDAR
                        .compare(ij1.getDateFinDroit(), dateFin) != JACalendar.COMPARE_FIRSTUPPER))
                        || (!JadeStringUtil.isEmpty(ij2.getDateFinDroit()) && (CALENDAR.compare(ij2.getDateFinDroit(),
                                dateFin) != JACalendar.COMPARE_FIRSTUPPER))) {
                    return COMPARE_DIFFERENTES;
                }
            }

            // sinon on compare les ij
            if (!ij1.isEgalPourCalcul(ij2)) {
                return COMPARE_DIFFERENTES;
            }
        }

        return COMPARE_EQUIVALENTES;
    }

    /**
     * Retourne la liste des ij pour la période allant de 'dateDebut' a 'dateFin'.
     * 
     * @param dateDebut
     *            DOCUMENT ME!
     * @param dateFin
     *            DOCUMENT ME!
     * 
     * @return une list jamais null peut etre vide
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public List findIjs(String dateDebut, String dateFin) throws Exception {
        ArrayList retValue = new ArrayList();
        JADate debut = new JADate(dateDebut);
        JADate fin = new JADate(dateFin);

        for (Iterator ijs = ijsList.iterator(); ijs.hasNext();) {
            IJIJCalculee ij = (IJIJCalculee) ijs.next();

            if ((JAUtil.isDateEmpty(ij.getDateFinDroit()) || (CALENDAR.compare(new JADate(ij.getDateFinDroit()), debut) != JACalendar.COMPARE_FIRSTLOWER))
                    && (CALENDAR.compare(new JADate(ij.getDateDebutDroit()), fin) != JACalendar.COMPARE_FIRSTUPPER)) {
                retValue.add(ij);
            }
        }

        return retValue;
    }
}
