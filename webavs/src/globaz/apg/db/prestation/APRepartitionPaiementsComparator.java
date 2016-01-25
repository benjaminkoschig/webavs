/*
 * Créé le 2 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.prestation;

import globaz.jade.client.util.JadeStringUtil;
import java.util.Comparator;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APRepartitionPaiementsComparator implements Comparator {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Trie les répartitions de paiements par id croissant en groupant les répartitions filles juste après leur parent.
     * 
     * <h4>Copie de java.util.Comparator#compare(java.lang.Object, java.lang.Object)</h4>
     * 
     * <p>
     * Compares its two arguments for order. Returns a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.
     * </p>
     * 
     * <p>
     * The implementor must ensure that <tt>sgn(compare(x, y)) == -sgn(compare(y, x))</tt> for all <tt>x</tt> and
     * <tt>y</tt>. (This implies that <tt>compare(x, y)</tt> must throw an exception if and only if <tt>compare(y,
     * x)</tt> throws an exception.)
     * </p>
     * 
     * <p>
     * The implementor must also ensure that the relation is transitive: <tt>((compare(x, y)&gt;0) &amp;&amp;
     * (compare(y, z)&gt;0))</tt> implies <tt>compare(x, z)&gt;0</tt>.
     * </p>
     * 
     * <p>
     * Finally, the implementer must ensure that <tt>compare(x, y)==0</tt> implies that <tt>sgn(compare(x,
     * z))==sgn(compare(y, z))</tt> for all <tt>z</tt>.
     * </p>
     * 
     * <p>
     * It is generally the case, but <i>not</i> strictly required that <tt>(compare(x, y)==0) == (x.equals(y))</tt>.
     * Generally speaking, any comparator that violates this condition should clearly indicate this fact. The
     * recommended language is "Note: this comparator imposes orderings that are inconsistent with equals."
     * </p>
     * 
     * @param o1
     *            the first object to be compared.
     * @param o2
     *            the second object to be compared.
     * 
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
     *         than the second.
     * 
     * @throws RuntimeException
     *             si une erreur survient lors de la transformation en entier des identifiants
     */
    @Override
    public int compare(Object o1, Object o2) {
        APRepartitionPaiements rp1 = (APRepartitionPaiements) o1;
        APRepartitionPaiements rp2 = (APRepartitionPaiements) o2;

        try {
            int id1 = Integer.parseInt(rp1.getIdRepartitionBeneficiairePaiement());
            int id2 = Integer.parseInt(rp2.getIdRepartitionBeneficiairePaiement());

            if (JadeStringUtil.isEmpty(rp1.getIdParent())) {
                if (JadeStringUtil.isEmpty(rp2.getIdParent())) {
                    return naturalCompare(id1, id2);
                } else {
                    return forceLessCompare(id1, Integer.parseInt(rp2.getIdParent()));
                }
            } else {
                int idp1 = Integer.parseInt(rp1.getIdParent());

                if (JadeStringUtil.isEmpty(rp2.getIdParent())) {
                    return forceMoreCompare(idp1, id2);
                } else {
                    int idp2 = Integer.parseInt(rp2.getIdParent());

                    if (idp1 == idp2) {
                        return naturalCompare(id1, id2);
                    } else if (idp1 > idp2) {
                        return forceMoreCompare(idp1, id2);
                    } else {
                        return forceLessCompare(id1, idp2);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private int forceLessCompare(int i1, int i2) {
        if (i1 > i2) {
            return 1;
        } else {
            return -1;
        }
    }

    private int forceMoreCompare(int i1, int i2) {
        if (i1 < i2) {
            return -1;
        } else {
            return 1;
        }
    }

    private int naturalCompare(int i1, int i2) {
        if (i1 < i2) {
            return -1;
        } else if (i1 > i2) {
            return 1;
        } else {
            return 0;
        }
    }
}
