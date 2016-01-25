package globaz.hermes.print.itext.util;

import globaz.hermes.print.itext.HEExtraitAnnonceBean;
import java.io.Serializable;
import java.util.Comparator;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HEExtraitComparator implements Comparator, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(Object, Object)
     */
    @Override
    public int compare(Object o1, Object o2) {
        HEExtraitAnnonceBean e1 = (HEExtraitAnnonceBean) o1;
        HEExtraitAnnonceBean e2 = (HEExtraitAnnonceBean) o2;
        int result = 0;
        if (e1.equals(e2)) {
            return 0;
        }

        if ((result = compareAnnee(e1, e2)) == 0) {
            // Si annee egal on doit tester la caisse
            if ((result = compareCaisse(e1, e2)) == 0) {
                // Si caisse egal on doit tester extourne
                if ((result = compareCodeExtourne(e1, e2)) == 0) {
                    // Si clef extourne egal on doit le mois
                    if ((result = compareMois(e1, e2)) == 0) {
                        // Si mois egal on doit tester motif
                        return compareMotif(e1, e2);
                    }
                }
            }
        }

        /*
         * // Ancienne version if((result = compareAnnee(e1, e2))==0) { // Si annee egal on doit tester le mois
         * if((result = compareMois(e1, e2))==0) { // Si mois egal on doit tester le motif if((result = compareMotif(e1,
         * e2)) == 0) { // Si motif egal on doit tester la caisse return compareCaisse(e1, e2); } } }
         */
        // un cas de difference est apparu
        return result;
    }

    private int compareAnnee(HEExtraitAnnonceBean e1, HEExtraitAnnonceBean e2) {
        return compareInteger(e1.getAnneeCotti(), e2.getAnneeCotti());
    }

    private int compareCaisse(HEExtraitAnnonceBean e1, HEExtraitAnnonceBean e2) {
        Double d1 = null;
        Double d2 = null;
        try {
            d1 = new Double(e1.getNumeroCaisse());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            d2 = new Double(e2.getNumeroCaisse());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        // On verify les exeptions pour les cas sans annee
        if (d1 == null && d2 == null) {
            return 0;
        } else if (d1 == null) {
            return 1;
        } else if (d2 == null) {
            return -1;
        }

        // Il n y a pas d'exeception
        return d1.compareTo(d2);
    }

    private int compareCodeExtourne(HEExtraitAnnonceBean e1, HEExtraitAnnonceBean e2) {
        return compareInteger("" + e1.getClefExtourne(), "" + e2.getClefExtourne());
    }

    private int compareInteger(String s1, String s2) {
        Integer i1 = null;
        Integer i2 = null;
        try {
            i1 = new Integer(s1);
        } catch (NumberFormatException e) {
            // e.printStackTrace();
        }
        try {
            i2 = new Integer(s2);
        } catch (NumberFormatException e) {
            // e.printStackTrace();
        }
        // On verify les exeptions pour les cas sans annee
        if (i1 == null && i2 == null) {
            return 0;
        } else if (i1 == null) {
            return 1;
        } else if (i2 == null) {
            return -1;
        }
        // Il n y a pas d'exeception
        return i1.compareTo(i2);
    }

    private int compareMois(HEExtraitAnnonceBean e1, HEExtraitAnnonceBean e2) {
        return compareInteger(("" + e1.getMoisCottiDebut() + e1.getMoisCottiFin()),
                ("" + e2.getMoisCottiDebut() + e2.getMoisCottiFin()));
    }

    private int compareMotif(HEExtraitAnnonceBean e1, HEExtraitAnnonceBean e2) {
        return compareInteger(e1.getMotif(), e2.getMotif());
    }
}
