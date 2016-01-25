/*
 * Créé le 17 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hermes.print.itext.util;

import globaz.hermes.print.itext.HEExtraitAnnonce_Doc;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.StringTokenizer;

/**
 * @author ald Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class HEExtraitComparatorCaisse implements Comparator, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Object o1, Object o2) {
        ArrayList a1 = new ArrayList();
        ArrayList a2 = new ArrayList();
        StringTokenizer tokenList1 = new StringTokenizer(o1.toString(), HEExtraitAnnonce_Doc.KEY_SEPARATOR);
        StringTokenizer tokenList2 = new StringTokenizer(o2.toString(), HEExtraitAnnonce_Doc.KEY_SEPARATOR);
        while (tokenList1.hasMoreTokens()) {
            a1.add(tokenList1.nextToken());
        }
        while (tokenList2.hasMoreTokens()) {
            a2.add(tokenList2.nextToken());
        }
        Double d1 = null;
        Double d2 = null;
        try {
            d1 = new Double(a1.get(a1.size() - 1).toString());
        } catch (Exception e) {
            return a1.get(1).toString().compareTo(a2.get(1).toString());
        }
        try {
            d2 = new Double(a2.get(a2.size() - 1).toString());
        } catch (Exception e) {
            return a1.get(1).toString().compareTo(a2.get(1).toString());
        }
        // On verifie les exeptions
        if (d1 == null && d2 == null) {
            return a1.get(1).toString().compareTo(a2.get(1).toString());
        } else if (d1 == null) {
            return 1;
        } else if (d2 == null) {
            return -1;
        }

        // Il n y a pas d'exception
        int res = d1.compareTo(d2);
        if (res == 0) {
            return a1.get(1).toString().compareTo(a2.get(1).toString());
        } else {
            return res;
        }
    }
}
