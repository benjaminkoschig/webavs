package globaz.pavo.db.compte;

import java.util.Comparator;

/**
 * Comparateur des annonces en suspens. Date de création : (18.12.2002 09:17:32)
 * 
 * @author: Administrator
 */
public class CIAnnonceWrapperComparator implements Comparator {
    private boolean ciOuvert;

    /**
     * Création du comparateur d'annonces en suspens pour un assuré.
     * 
     * @param ciOuvert
     *            doit être à true si le ci concerné est ouvert
     */
    public CIAnnonceWrapperComparator(boolean ciOuvert) {
        super();
        this.ciOuvert = ciOuvert;
    }

    /**
     * Compares its two arguments for order. Returns a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.
     * <p>
     * 
     * The implementor must ensure that <tt>sgn(compare(x, y)) ==
     * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>. (This implies that <tt>compare(x, y)</tt> must throw
     * an exception if and only if <tt>compare(y, x)</tt> throws an exception.)
     * <p>
     * 
     * The implementor must also ensure that the relation is transitive:
     * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies <tt>compare(x, z)&gt;0</tt>.
     * <p>
     * 
     * Finally, the implementer must ensure that <tt>compare(x, y)==0</tt> implies that
     * <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all <tt>z</tt>.
     * <p>
     * 
     * It is generally the case, but <i>not</i> strictly required that <tt>(compare(x, y)==0) == (x.equals(y))</tt>.
     * Generally speaking, any comparator that violates this condition should clearly indicate this fact. The
     * recommended language is "Note: this comparator imposes orderings that are inconsistent with equals."
     * 
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
     *         than the second.
     * @throws ClassCastException
     *             if the arguments' types prevent them from being compared by this Comparator.
     */
    @Override
    public int compare(Object o1, Object o2) {
        String[] sort;
        if (ciOuvert) {
            sort = CIAnnonceSuspens.TRI_CI_OUVERT;
        } else {
            sort = CIAnnonceSuspens.TRI_CI_NON_OUVERT;
        }
        int o1Pos = getPos(sort, ((CIAnnonceWrapper) o1).getIdTypeTraitement());
        int o2Pos = getPos(sort, ((CIAnnonceWrapper) o2).getIdTypeTraitement());
        int result = o1Pos - o2Pos;
        if (result == 0) {
            // si même traitement, on met la caisse princ. en premier
            try {
                if (((CIAnnonceWrapper) o1).isCaissePrincipale()) {
                    result = -1;
                } else {
                    result = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = 1;
            }

        }
        return result;
    }

    /**
     * Recherche le poids du type de l'enregistrement. Date de création : (18.12.2002 09:44:33)
     * 
     * @return le poids (la position) du type d'enregistrement
     * @param list
     *            l'ordre de tri
     * @param type
     *            le type d'enregistrement à comparer
     */
    private int getPos(String[] list, String type) {
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(type)) {
                return i;
            }
        }
        return -1;
    }
}
