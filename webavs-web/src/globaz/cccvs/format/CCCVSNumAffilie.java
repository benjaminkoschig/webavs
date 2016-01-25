package globaz.cccvs.format;

import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

/**
 * Classe permettant de v�rifier la bonne syntaxe des num�ros d'affili�s de la caisse CCVS ou de les formatter.
 * 
 * <pre>
 * Un test JUnit a �t� cr�er suite � la modification afin de valider les changements. (CCCVSNumAffilieTest)
 * </pre>
 * 
 * @author Inconnu - Modification par dcl
 * @see Cr�e le 15.12.2006
 * @since Modifi� le 13.02.2011
 * @version 1.1
 */
public class CCCVSNumAffilie implements IFormatData {

    private static final int MAX_DIGITS_PART_3 = 6;
    private static final int MAX_DIGITS_TOTAL = 12;

    // Les diff�rentes parties composant un num�ro d'affili�
    private static final int PART_1_BEG = 0;
    private static final int PART_1_END = 3;
    private static final int PART_2_BEG = 3;
    private static final int PART_2_END = 6;
    private static final int PART_3_BEG = 6;
    private static final int PART_3_END = 12;

    private static final char SEPARATOR_CHAR = '.';

    /**
     * M�thode permettant de v�rifier si le num�ro d'affili� pass� en param�tre est valide selon les conventions de la
     * CCVS en traversant une batterie de tests.
     * 
     * @param value Le num�ro de l'affili�
     * @return null car n'est de toute mani�re pas utilis� dans les classes appelantes.
     */
    @Override
    public String check(Object value) throws Exception {
        if (value == null) {
            throw new Exception("The value can not be null");
        }

        String valueStr = unformat((String) value);

        // Si la valeur du param�tre ne d�passe pas 12 chiffres.
        if (valueStr.length() > CCCVSNumAffilie.MAX_DIGITS_TOTAL) {
            throw new Exception("The value can not exceed" + CCCVSNumAffilie.MAX_DIGITS_TOTAL
                    + " digits (without punctations)");
        }

        // Si tout s'est bien pass�, on retourne null
        return null;
    }

    /**
     * M�thode permettant de formatter le num�ro d'affili� pass� en param�tre selon les conventions de la CCVS.
     * 
     * <pre>
     * Les num�ros sont dans ce format XXX.YYY.Z � XXX.YYY.ZZZZZZ
     * (FIRST_PART)  : XXX est le num�ro de la commune
     * (SECOND_PART) : YYY est le num�ro composant le nom + le pr�nom selon un mapping
     * (THIRD_PART)  : Z � ZZZZZZ le num�ro incr�ment� pour les affiliations, jusqu'� 6 chiffres accept�s
     * </pre>
     * 
     * @param value Le num�ro de l'affili�.
     * @return Le num�ro formatt�.
     */
    @Override
    public String format(String value) {
        if (value == null) {
            return null;
        }

        StringBuffer finalResult = new StringBuffer();
        String formatedValue = unformat(value);

        // Si la valeur d�passe plus que 3 chiffres (ex : 021XXX OU 021XXXXXX)
        if (formatedValue.length() > CCCVSNumAffilie.PART_1_END) {
            finalResult.append(formatedValue.substring(CCCVSNumAffilie.PART_1_BEG, CCCVSNumAffilie.PART_1_END));
            finalResult.append(CCCVSNumAffilie.SEPARATOR_CHAR);

            // Si la valeur d�passe plus que 6 chiffres (ex : 021341XXXX)
            if (formatedValue.length() > CCCVSNumAffilie.PART_2_END) {
                finalResult.append(formatedValue.substring(CCCVSNumAffilie.PART_2_BEG, CCCVSNumAffilie.PART_2_END));
                finalResult.append(CCCVSNumAffilie.SEPARATOR_CHAR);

                // Si la longueur de la derni�re partie du num�ro est plus petit ou �gal � 6 chiffres
                if (formatedValue.substring(CCCVSNumAffilie.PART_3_BEG).length() <= CCCVSNumAffilie.MAX_DIGITS_PART_3) {
                    // valeur : XXX.YYY.Z � XXX.YYY.ZZZZZZ
                    finalResult.append(formatedValue.substring(CCCVSNumAffilie.PART_3_BEG));
                } else {
                    // M. Heritier (CCVS) ne veux pas exc�der 6 chiffres pour la derni�re partie du num�ro
                    JadeLogger.info(formatedValue,
                            "Too many digits for last part of the affiliate number, we truncated it");

                    // valeur : XXX.YYY.ZZZZZZ (tronquer)
                    finalResult.append(formatedValue.substring(CCCVSNumAffilie.PART_3_BEG, CCCVSNumAffilie.PART_3_END));
                }
            } else {
                // valeur : XXX.Y � XXX.YYY
                finalResult.append(formatedValue.substring(CCCVSNumAffilie.PART_2_BEG));
            }
        } else {
            // valeur : X � XXX
            finalResult.append(formatedValue.substring(CCCVSNumAffilie.PART_1_BEG));
        }
        return finalResult.toString();
    }

    /**
     * M�thode permettant de nettoyer une chaine de caract�re en lui supprimant les caract�res de s�paration.
     * 
     * @param value Le num�ro d'affili� � traiter.
     * @return Le num�ro de l'affili� sans le(s) caract�re(s) de s�paration.
     */
    @Override
    public String unformat(String value) {
        return JadeStringUtil.removeChar(value, CCCVSNumAffilie.SEPARATOR_CHAR);
    }
}
