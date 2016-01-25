package globaz.ij.calendar;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.ij.application.IJApplication;
import java.util.Iterator;

/**
 * Descpription.
 * 
 * <p>
 * Calendrier Mensuel
 * </p>
 * 
 * <p>
 * Creation d'un calendrier 'IJ' pour la saisie des bases d'indemnisations. L'unité de base est le jour. Le calendrier
 * IJ initialise un mois complet décomposée en semaines et en jours.
 * </p>
 * 
 * @author scr Date de création 13 sept. 05
 */
public class IJCalendar {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param args
     *            DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            BSession session = (BSession) ((IJApplication) GlobazSystem.getApplication("ij")).newSession("globazf",
                    "ssiiadm");
            IJCalendar cal = new IJCalendar(session, 5, 2005, 1, 31);

            System.out.println(cal.toString());

            System.out.println(cal.toACORString());
            cal.setValeur("1111111222222222222222222222222");
            System.out.println(cal.toString());
            System.out.println(cal.toACORString());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.exit(0);
    }

    IJMois ijMois = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    BSession session = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJCalendar.
     * 
     * @param session
     *            La session
     * @param month
     *            Le mois du calendrier
     * @param year
     *            L'année du calendrier
     * @param jourDebut
     *            DOCUMENT ME!
     * @param jourFin
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJCalendar(BSession session, int month, int year, int jourDebut, int jourFin) throws Exception {
        this.session = session;
        ijMois = new IJMois(session, month, year, jourDebut, jourFin);
    }

    /**
     * getter pour l'attribut mois.
     * 
     * @return la valeur courante de l'attribut mois
     */
    public IJMois getMois() {
        return ijMois;
    }

    private int getNombreJoursActif() {
        int counter = 0;

        for (Iterator iter = ijMois.getListSemaines().iterator(); iter.hasNext();) {
            IJSemaine semaine = (IJSemaine) iter.next();

            for (Iterator iterator = semaine.getListJours().iterator(); iterator.hasNext();) {
                IJJour jour = (IJJour) iterator.next();

                if (jour.isActif()) {
                    counter++;
                }
            }
        }

        return counter;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param mois
     */
    public void setMois(IJMois mois) {
        ijMois = mois;
    }

    /**
     * Met à jours les données du calendrier (la valeur pour chacun des jours actifs du mois) à partir d'une chaîne de
     * caractère.
     * 
     * @param data
     *            String contenant les valeurs des jours. La longueur de cette chaîne doit correspondre au nombre de
     *            jours dans le mois.
     * 
     * @throws Exception
     *             En cas d'erreur
     */
    public void setValeur(String data) throws Exception {
        if (data.length() != getNombreJoursActif()) {
            throw new Exception("Impossible d'assigner les valeurs au calendrier : " + getMois().getLibelleMois());
        }

        int counter = 0;

        for (Iterator iter = ijMois.getListSemaines().iterator(); iter.hasNext();) {
            IJSemaine semaine = (IJSemaine) iter.next();

            for (Iterator iterator = semaine.getListJours().iterator(); iterator.hasNext();) {
                IJJour jour = (IJJour) iterator.next();

                if (jour.isActif()) {
                    char valeur = data.charAt(counter);

                    jour.setValeur(String.valueOf(valeur));
                    counter++;
                }
            }
        }
    }

    /**
     * Retourne la string à envoyer à ACOR pour le calcul du décompte d'une IJ.
     * 
     * @return String
     */
    public String toACORString() {
        StringBuffer sb = new StringBuffer();

        for (Iterator iter = ijMois.getListSemaines().iterator(); iter.hasNext();) {
            IJSemaine semaine = (IJSemaine) iter.next();

            for (Iterator iterator = semaine.getListJours().iterator(); iterator.hasNext();) {
                IJJour jour = (IJJour) iterator.next();

                if (jour.isActif()) {
                    sb.append(jour.getValeur());
                }
            }
        }

        return sb.toString();
    }

    /**
     * toString() method.
     * 
     * @return String
     */
    @Override
    public String toString() {
        return getMois().toString();
    }
}
