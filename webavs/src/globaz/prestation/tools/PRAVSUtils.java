package globaz.prestation.tools;

import globaz.globall.util.JAException;
import globaz.prestation.tools.impl.PRAVS11ChiffresUtils;
import globaz.prestation.tools.impl.PRNSS13ChiffresUtils;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe fournissant quelques m�thodes utiles pour l'extraction d'informations du num�ro AVS.
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
     * retourne une instance de PRAVSUtils sp�cifique au no AVS 'noAVS'
     * 
     * @param noAVS
     *            le no AVS de l'assur�
     * 
     * @return une instance de PRAVSUtils adapt�e au format du no AVS 'noAVS'
     * 
     * @throws JAException
     *             si le format n'est pas reconnu ou non support�.
     */
    public static final PRAVSUtils getInstance(String noAVS) throws JAException {
        if (PRStringUtils.extraireDigits(noAVS).length() == 11) {
            return PRAVS_11CHIFFRES;
        } else {
            return PRAVS_13CHIFFRES;
        }
    }

    /**
     * retourne vrai si l'assur� portant le no AVS 'noAVS' est de nationalit� suisse.
     * 
     * @param noAVS
     *            le no AVS de l'assur�
     * 
     * @return vrai si l'assur� est de nationalit� suisse.
     */
    public abstract boolean isSuisse(String noAVS);

    /**
     * Retourne un it�rateur sur une suite de no AVS bidons, uniques mais dont le format est correct.
     * 
     * <p>
     * Deux instances distinctes d'it�rateurs renvoy�es par cette m�thode peuvent renvoyer les m�mes no AVS.
     * </p>
     * 
     * @return un it�rateur, jamais null, JAMAIS VIDE !!! (ou du moins raisonnablement peu de chances qu'il le soit).
     */
    public abstract Iterator iteratorNoAVSBidon();

    /**
     * retourne un no AVS bidon mais dont le format est correct et surtout dont la chance qu'il soit retourn� par
     * l'it�rateur de la m�thode iteratorNoAVSBidon est raisonnablement faible.
     * 
     * @return DOCUMENT ME!
     */
    public abstract String noAVSBidon();
}
