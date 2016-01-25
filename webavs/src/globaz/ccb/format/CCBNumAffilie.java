package globaz.ccb.format;

import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import java.text.DecimalFormat;
import java.text.ParseException;

public class CCBNumAffilie implements IFormatData {

    private static String ERROR_MSG = "format du no d'affilié invalide";
    private static String ERROR_TOOLONG = "Numéro d'affilié trop long";
    private static DecimalFormat f = new DecimalFormat("0000000");
    public static int NUMBER_LENGTH = 9;
    public static char NUMBER_SEPARATOR_CHAR = '.';

    /**
     * Constructeur.
     */
    public CCBNumAffilie() {
        super();
    }

    /**
     * Test le format du numéro donné
     * 
     * @param value numéro d'affilié à tester de type String
     * @return toujours null
     * @throws Exception si le numéro n'est pas valide
     */
    @Override
    public String check(Object value) throws Exception {
        String s = value.toString();

        if (JadeStringUtil.isBlank(s)) {
            throw new Exception(CCBNumAffilie.ERROR_MSG);
        }
        try {
            CCBNumAffilie.f.parse(s);
        } catch (ParseException e) {
            throw new Exception(CCBNumAffilie.ERROR_MSG);
        }
        if ((s.length() > CCBNumAffilie.NUMBER_LENGTH) && (s.indexOf(CCBNumAffilie.NUMBER_SEPARATOR_CHAR) == -1)) {
            throw new Exception(CCBNumAffilie.ERROR_TOOLONG);
        }
        return null;
    }

    /**
     * Format le numéro d'affilié donné
     * 
     * @parmam le numéro d'affilié
     * @return le numéro formaté ou vide si le paramètre est null
     */
    @Override
    public String format(String value) {
        if (value == null) {
            return null;
        }
        Number n = null;
        try {
            n = CCBNumAffilie.f.parse(value);
        } catch (ParseException e) {
            return value;
        }
        if (value.length() >= CCBNumAffilie.NUMBER_LENGTH) {
            if (value.indexOf(CCBNumAffilie.NUMBER_SEPARATOR_CHAR) == -1) {
                return CCBNumAffilie.f.format(n.doubleValue() / 100);
            } else {
                return value;
            }
        } else {
            return CCBNumAffilie.f.format(n.doubleValue());
        }
    }

    /**
     * Retourne le numéro d'affilié sans caractère de séparation
     * 
     * @param value le numéro d'affilié avec ou sans caractères de séparation
     * @return le numéro déformaté ou vide si value est null
     */
    @Override
    public String unformat(String value) {
        if (value == null) {
            // retourne vide si null
            return "";
        }
        StringBuffer str = new StringBuffer();

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c != CCBNumAffilie.NUMBER_SEPARATOR_CHAR) {
                str.append(c);
            }
        }

        return str.toString();
    }

}
