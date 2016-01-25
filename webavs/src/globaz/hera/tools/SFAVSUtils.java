package globaz.hera.tools;

import globaz.globall.util.JAException;
import globaz.hera.tools.impl.SFAVS11ChiffresUtils;
import globaz.hera.tools.impl.SFNSS13ChiffresUtils;
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
public abstract class SFAVSUtils {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final SFAVSUtils PRAVS_11CHIFFRES = new SFAVS11ChiffresUtils();
    private static final SFAVSUtils PRAVS_13CHIFFRES = new SFNSS13ChiffresUtils();

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
    public static final SFAVSUtils getInstance(String noAVS) throws JAException {
        if (SFStringUtils.extraireDigits(noAVS).length() == 11) {
            return PRAVS_11CHIFFRES;
        } else {
            return PRAVS_13CHIFFRES;
        }
    }

    /**
     * retourne le sexe de l'assur� portant le no AVS 'noAVS'
     * 
     * @param noAVS
     *            le no AVS de l'assur�
     * 
     * @return le code syst�me du sexe
     */
    public abstract String getSexe(String noAVS);

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
