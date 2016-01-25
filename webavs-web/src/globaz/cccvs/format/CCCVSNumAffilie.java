package globaz.cccvs.format;

import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

/**
 * Classe permettant de vérifier la bonne syntaxe des numéros d'affiliés de la caisse CCVS ou de les formatter.
 * 
 * <pre>
 * Un test JUnit a été créer suite à la modification afin de valider les changements. (CCCVSNumAffilieTest)
 * </pre>
 * 
 * @author Inconnu - Modification par dcl
 * @see Crée le 15.12.2006
 * @since Modifié le 13.02.2011
 * @version 1.1
 */
public class CCCVSNumAffilie implements IFormatData {

    private static final int MAX_DIGITS_PART_3 = 6;
    private static final int MAX_DIGITS_TOTAL = 12;

    // Les différentes parties composant un numéro d'affilié
    private static final int PART_1_BEG = 0;
    private static final int PART_1_END = 3;
    private static final int PART_2_BEG = 3;
    private static final int PART_2_END = 6;
    private static final int PART_3_BEG = 6;
    private static final int PART_3_END = 12;

    private static final char SEPARATOR_CHAR = '.';

    /**
     * Méthode permettant de vérifier si le numéro d'affilié passé en paramètre est valide selon les conventions de la
     * CCVS en traversant une batterie de tests.
     * 
     * @param value Le numéro de l'affilié
     * @return null car n'est de toute manière pas utilisé dans les classes appelantes.
     */
    @Override
    public String check(Object value) throws Exception {
        if (value == null) {
            throw new Exception("The value can not be null");
        }

        String valueStr = unformat((String) value);

        // Si la valeur du paramètre ne dépasse pas 12 chiffres.
        if (valueStr.length() > CCCVSNumAffilie.MAX_DIGITS_TOTAL) {
            throw new Exception("The value can not exceed" + CCCVSNumAffilie.MAX_DIGITS_TOTAL
                    + " digits (without punctations)");
        }

        // Si tout s'est bien passé, on retourne null
        return null;
    }

    /**
     * Méthode permettant de formatter le numéro d'affilié passé en paramètre selon les conventions de la CCVS.
     * 
     * <pre>
     * Les numéros sont dans ce format XXX.YYY.Z à XXX.YYY.ZZZZZZ
     * (FIRST_PART)  : XXX est le numéro de la commune
     * (SECOND_PART) : YYY est le numéro composant le nom + le prénom selon un mapping
     * (THIRD_PART)  : Z à ZZZZZZ le numéro incrémenté pour les affiliations, jusqu'à 6 chiffres acceptés
     * </pre>
     * 
     * @param value Le numéro de l'affilié.
     * @return Le numéro formatté.
     */
    @Override
    public String format(String value) {
        if (value == null) {
            return null;
        }

        StringBuffer finalResult = new StringBuffer();
        String formatedValue = unformat(value);

        // Si la valeur dépasse plus que 3 chiffres (ex : 021XXX OU 021XXXXXX)
        if (formatedValue.length() > CCCVSNumAffilie.PART_1_END) {
            finalResult.append(formatedValue.substring(CCCVSNumAffilie.PART_1_BEG, CCCVSNumAffilie.PART_1_END));
            finalResult.append(CCCVSNumAffilie.SEPARATOR_CHAR);

            // Si la valeur dépasse plus que 6 chiffres (ex : 021341XXXX)
            if (formatedValue.length() > CCCVSNumAffilie.PART_2_END) {
                finalResult.append(formatedValue.substring(CCCVSNumAffilie.PART_2_BEG, CCCVSNumAffilie.PART_2_END));
                finalResult.append(CCCVSNumAffilie.SEPARATOR_CHAR);

                // Si la longueur de la dernière partie du numéro est plus petit ou égal à 6 chiffres
                if (formatedValue.substring(CCCVSNumAffilie.PART_3_BEG).length() <= CCCVSNumAffilie.MAX_DIGITS_PART_3) {
                    // valeur : XXX.YYY.Z à XXX.YYY.ZZZZZZ
                    finalResult.append(formatedValue.substring(CCCVSNumAffilie.PART_3_BEG));
                } else {
                    // M. Heritier (CCVS) ne veux pas excéder 6 chiffres pour la dernière partie du numéro
                    JadeLogger.info(formatedValue,
                            "Too many digits for last part of the affiliate number, we truncated it");

                    // valeur : XXX.YYY.ZZZZZZ (tronquer)
                    finalResult.append(formatedValue.substring(CCCVSNumAffilie.PART_3_BEG, CCCVSNumAffilie.PART_3_END));
                }
            } else {
                // valeur : XXX.Y à XXX.YYY
                finalResult.append(formatedValue.substring(CCCVSNumAffilie.PART_2_BEG));
            }
        } else {
            // valeur : X à XXX
            finalResult.append(formatedValue.substring(CCCVSNumAffilie.PART_1_BEG));
        }
        return finalResult.toString();
    }

    /**
     * Méthode permettant de nettoyer une chaine de caractère en lui supprimant les caractères de séparation.
     * 
     * @param value Le numéro d'affilié à traiter.
     * @return Le numéro de l'affilié sans le(s) caractère(s) de séparation.
     */
    @Override
    public String unformat(String value) {
        return JadeStringUtil.removeChar(value, CCCVSNumAffilie.SEPARATOR_CHAR);
    }
}
