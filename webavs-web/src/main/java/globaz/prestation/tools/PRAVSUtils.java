package globaz.prestation.tools;

import globaz.globall.util.JAException;
import globaz.prestation.tools.impl.PRAVS11ChiffresUtils;
import globaz.prestation.tools.impl.PRNSS13ChiffresUtils;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe fournissant quelques méthodes utiles pour l'extraction d'informations du numéro AVS.
 * </p>
 * 
 * @author vre
 */
public abstract class PRAVSUtils {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final PRAVSUtils PRAVS_11CHIFFRES = new PRAVS11ChiffresUtils();
    private static final PRAVSUtils PRAVS_13CHIFFRES = new PRNSS13ChiffresUtils();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * retourne une instance de PRAVSUtils spécifique au no AVS 'noAVS'
     * 
     * @param noAVS
     *            le no AVS de l'assuré
     * 
     * @return une instance de PRAVSUtils adaptée au format du no AVS 'noAVS'
     * 
     * @throws JAException
     *             si le format n'est pas reconnu ou non supporté.
     */
    public static final PRAVSUtils getInstance(String noAVS) throws JAException {
        if (PRStringUtils.extraireDigits(noAVS).length() == 11) {
            return PRAVS_11CHIFFRES;
        } else {
            return PRAVS_13CHIFFRES;
        }
    }

    /**
     * retourne vrai si l'assuré portant le no AVS 'noAVS' est de nationalité suisse.
     * 
     * @param noAVS
     *            le no AVS de l'assuré
     * 
     * @return vrai si l'assuré est de nationalité suisse.
     */
    public abstract boolean isSuisse(String noAVS);

    /**
     * Retourne un itérateur sur une suite de no AVS bidons, uniques mais dont le format est correct.
     * 
     * <p>
     * Deux instances distinctes d'itérateurs renvoyées par cette méthode peuvent renvoyer les mêmes no AVS.
     * </p>
     * 
     * @return un itérateur, jamais null, JAMAIS VIDE !!! (ou du moins raisonnablement peu de chances qu'il le soit).
     */
    public abstract Iterator iteratorNoAVSBidon();

    /**
     * retourne un no AVS bidon mais dont le format est correct et surtout dont la chance qu'il soit retourné par
     * l'itérateur de la méthode iteratorNoAVSBidon est raisonnablement faible.
     * 
     * @return DOCUMENT ME!
     */
    public abstract String noAVSBidon();
}
