package globaz.bms.format;

import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Gestion du format du numéro d'affiliation pour le bms
 * 
 * @author: Chasse !
 */
public class BMSNumAffilie implements IFormatData {

    private static String ERROR_MSG = "Affilie number, wrong format";
    public static int NUMBER_CORPSE_LENGTH = 7;
    public static int NUMBER_LENGTH = 9;
    public static int NUMBER_LENGTH_TOTAL = 13;
    public static char ASSOCIES_SEPARATOR_CHAR = '.';
    public static char CONVENTION_SEPARATOR_CHAR = '-';

    public static int ASSOCIES_SEPARATOR_POS = 7;
    public static int CONVENTION_SEPARATOR_POS = 10;

    /**
     * Constructeur.
     */
    public BMSNumAffilie() {
        super();
    }

    /**
     * Test le format du numéro donné
     * 
     * @param value
     *            numéro d'affilié à tester de type String
     * @return toujours null
     * @throws Exception
     *             si le numéro n'est pas valide
     */
    @Override
    public String check(final Object value) throws Exception {
        if (value == null) {
            throw new Exception(BMSNumAffilie.ERROR_MSG + " : null value");
        }
        String valueStr = unformat((String) value);
        // bonne longueur ?
        if (valueStr.length() < BMSNumAffilie.NUMBER_LENGTH) {
            throw new Exception(BMSNumAffilie.ERROR_MSG + " : " + (String) value);
        }
        // TODO est-ce que d'autres plausibilité doivent être implémentée?
        return null;
    }

    /**
     * Formate le numéro d'affilié donné
     * 
     * @parmam le numéro d'affilié
     * @return le numéro formaté ou vide si le paramètre est null
     */
    @Override
    public String format(String value) {
        if (value == null || value.length() == 0) {
            return null;
        }

        value = unformat(value);
        StringBuffer result = new StringBuffer();
        if (value.length() <= BMSNumAffilie.ASSOCIES_SEPARATOR_POS) {
            result.append(JadeStringUtil.fillWithZeroes(value, BMSNumAffilie.ASSOCIES_SEPARATOR_POS));
        } else if (value.length() > BMSNumAffilie.ASSOCIES_SEPARATOR_POS) {
            result.append(value.substring(0, BMSNumAffilie.ASSOCIES_SEPARATOR_POS));
            result.append(BMSNumAffilie.ASSOCIES_SEPARATOR_CHAR);
            result.append(JadeStringUtil.substring(value, BMSNumAffilie.ASSOCIES_SEPARATOR_POS, 2));
            result.append(BMSNumAffilie.CONVENTION_SEPARATOR_CHAR);
            result.append(JadeStringUtil.substring(value, BMSNumAffilie.ASSOCIES_SEPARATOR_POS + 2, 2));
        } else {
            return value;
        }
        return result.toString();
    }

    /**
     * Retourne le numéro d'affilié sans caractère de séparation
     * 
     * @param value
     *            le numéro d'affilié avec ou sans caractères de séparation
     * @return le numéro déformaté ou vide si value est null
     */
    @Override
    public String unformat(final String value) {
        if (value == null) {
            // retourne vide si null
            return "";
        }
        return value.replace(".", "").replace("-", "").trim();
    }
}
