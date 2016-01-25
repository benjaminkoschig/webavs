/*
 * Créé le 7 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.prestation.tools;

import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class PRCalcul {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Détermine si deux nombres à virgule flottante sont égaux à la précision de la JVM.
     * 
     * <p>
     * Cette méthode ne convient QUE pour des cas particuliers, PAS pour des nombres issus d'un calcul. En effet, on
     * compare les chaines dans leur representation en double !!!! Il peut y avoir des inconsistences si les nombres
     * sont obtenus apres une série de calculs (faites l'essai, 100.0* (1.0/100.0) != 1.0 !!!)
     * </p>
     * 
     * @param d1
     *            DOCUMENT ME!
     * @param d2
     *            DOCUMENT ME!
     * 
     * @return vrai si les deux nombres sont egaux ou sont deux chaines vides, faux sinon (yc pas des nombres).
     */
    public static final boolean isDEgaux(String d1, String d2) {
        if (JadeStringUtil.isBlank(d1) || JadeStringUtil.isBlank(d2)) {
            return JadeStringUtil.isDecimalEmpty(d1) && JadeStringUtil.isDecimalEmpty(d2);
        }

        try {
            return Double.parseDouble(d2) == Double.parseDouble(d1);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * retourne vrai si deux entiers sont égaux.
     * 
     * @param d1
     *            DOCUMENT ME!
     * @param d2
     *            DOCUMENT ME!
     * 
     * @return vrai si deux entiers sont egaux ou tous les deux chaines vides, faux sinon (yc pas des nombres).
     */
    public static final boolean isIEgaux(String i1, String i2) {
        if (JadeStringUtil.isBlank(i1) || JadeStringUtil.isBlank(i2)) {
            return JadeStringUtil.isIntegerEmpty(i1) && JadeStringUtil.isIntegerEmpty(i2);
        }

        try {
            return Integer.parseInt(i2) == Integer.parseInt(i1);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Multiplie deux valeurs
     * 
     * @param op1
     *            operande 1
     * @param op2
     *            operande 2
     * 
     * @return le resultat de la multiplication
     * 
     * @throws NumberFormatException
     *             si les chaînes ne contiennent pas des double valables.
     */
    public static final double multiply(String op1, String op2) throws NumberFormatException {
        return Double.parseDouble(op1) * Double.parseDouble(op2);
    }

    /**
     * Calcule le pourcentage d'une valeur.
     * 
     * @param valeur
     *            une valeur à réduire
     * @param pourcentSur100
     *            une valeur de pourcentage entre 0 et 100
     * 
     * @return la valeur réduite
     * 
     * @throws NumberFormatException
     *             si les chaînes ne contiennent pas des double valables.
     */
    public static final double pourcentage100(String valeur, String pourcentSur100) throws NumberFormatException {
        return Double.parseDouble(valeur) * Double.parseDouble(pourcentSur100) / 100;
    }

    /**
     * calcule le quotient de deux nombres (double)
     * 
     * @param numerateur
     *            le numerateur de la fraction
     * @param denominateur
     *            le dénominateur de la fraction
     * 
     * @return le resultat de la division du numerateur par le denominateur.
     * 
     * @throws NumberFormatException
     *             DOCUMENT ME!
     */
    public static final double quotient(String numerateur, String denominateur) throws NumberFormatException {
        return Double.parseDouble(numerateur) / Double.parseDouble(denominateur);
    }

    /**
     * calcule le quotient de deux nombres (double) et multiplie le resultat par 100.
     * 
     * @param numerateur
     *            le numerateur de la fraction
     * @param denominateur
     *            le dénominateur de la fraction
     * 
     * @return le resultat de la division du numerateur par le denominateur le tout multiplie par 100
     * 
     * @throws NumberFormatException
     *             DOCUMENT ME!
     */
    public static final double quotientX100(String numerateur, String denominateur) throws NumberFormatException {
        return quotient(numerateur, denominateur) * 100d;
    }

    /**
     * Retourne la valeur arrondie d'une valeur reel transmise en argument.
     * 
     * @param reel
     *            le reel dont il faut calculer l'arrondi
     * 
     * @return la valeur arrondie.
     */
    public static final String tronque(String reel) {
        int pos = reel.indexOf(".");

        if (pos > 0) {
            return reel.substring(0, pos);
        } else {
            return reel;
        }
    }
}
