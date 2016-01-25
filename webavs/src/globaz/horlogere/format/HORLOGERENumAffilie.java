package globaz.horlogere.format;

import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Gestion du format du numéro d'affiliation de la FER.
 * 
 * @author: David Girardin
 */
public class HORLOGERENumAffilie implements IFormatData {

    private static String ERROR_MSG = "wrong format";
    public static int NUMBER_LENGTH = 7;
    public static char NUMBER_SEPARATOR_CHAR = '.';

    public static int NUMBER_SEPARATOR_POS = 3;

    /**
     * Insérez la description de la méthode ici. Date de création : (14.04.2003 12:04:25)
     * 
     * @param args
     *            java.lang.String[]
     */
    public static void main(String[] args) {

        HORLOGERENumAffilie num = new HORLOGERENumAffilie();

        String[] toTest = { "123", "1234", "12345", "123456", "1234567", // ok
                "12.34567", // ok
                "123.4567", // ok
                "123.45678", // ok

        };

        for (int i = 0; i < toTest.length; i++) {
            try {
                num.check(toTest[i]);
                System.out.println("valide : " + num.format(toTest[i]) + " (" + toTest[i] + ")");
            } catch (Exception e) {
                System.out.println(e.getMessage() + " - " + num.format(toTest[i]) + " (" + toTest[i] + ")");
            }
        }

    }

    /**
     * Constructeur.
     */
    public HORLOGERENumAffilie() {
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
    public String check(Object value) throws Exception {
        if (value == null) {
            throw new Exception(ERROR_MSG);
        }
        String valueStr = unformat((String) value);
        // bonne longueur ?
        if (valueStr.length() > NUMBER_LENGTH) {
            throw new Exception(ERROR_MSG);
        }

        // TODO est-ce que d'autres plausibilité doivent être implémentée?
        return valueStr;
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

        // Padding a droite
        value = JadeStringUtil.rightJustifyInteger(value, NUMBER_LENGTH);

        value = unformat(value);
        StringBuffer result = new StringBuffer();
        if (value.length() > NUMBER_SEPARATOR_POS) {
            result.append(value.substring(0, NUMBER_SEPARATOR_POS));
            result.append(NUMBER_SEPARATOR_CHAR);
            result.append(value.substring(NUMBER_SEPARATOR_POS));
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
    public String unformat(String value) {
        if (value == null) {
            // retourne vide si null
            return "";
        }
        StringBuffer str = new StringBuffer();

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c != NUMBER_SEPARATOR_CHAR) {
                str.append(c);
            }
        }

        return str.toString();
    }
}
