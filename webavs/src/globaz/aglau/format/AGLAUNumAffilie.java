/*
 * Cr�� le 15 d�c. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.aglau.format;

import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author ald
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class AGLAUNumAffilie implements IFormatData {
    private static String ERROR_MSG = "Format incorrect";
    public static int NUMBER_LENGTH = 7;
    public static char NUMBER_SEPARATOR_CHAR = '.';
    public static int NUMBER_SEPARATOR_POS = 3;

    public static int NUMBER_SEPARATOR_POS2 = 6;

    public static void main(String[] args) {

        AGLAUNumAffilie num = new AGLAUNumAffilie();

        String[] toTest = { "123", // 000.123
                "1234", // 001.234
                "12345", // 012.345
                "123456", // 123.456
                "1234567", // 123.456-07
                "12345678", // 123.456-78

                "12.3456", // nok
                "123.456", // nok
                "123.456.78", // nok
                "123.456.789" // ok
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

    @Override
    public String check(Object value) throws Exception {
        if (JadeStringUtil.isEmpty((String) value)) {
            throw new Exception(ERROR_MSG);
        }
        String valueStr = (String) value;
        int sep = JadeStringUtil.indexOf(valueStr, NUMBER_SEPARATOR_CHAR, 0);
        if (sep != -1 && sep != NUMBER_SEPARATOR_POS) {
            throw new Exception(ERROR_MSG);
        }
        int sep2 = JadeStringUtil.indexOf(valueStr, NUMBER_SEPARATOR_CHAR, sep);
        if (sep2 != -1 && sep2 != NUMBER_SEPARATOR_POS) {
            throw new Exception(ERROR_MSG);
        }
        valueStr = unformat(valueStr);
        if (valueStr.length() < NUMBER_LENGTH) {
            throw new Exception(ERROR_MSG);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.format.IFormatData#format(java.lang.String)
     */
    @Override
    public String format(String value) {
        if (value == null) {
            return null;
        }
        value = unformat(value);
        StringBuffer result = new StringBuffer();
        if (value.length() > NUMBER_SEPARATOR_POS) {
            result.append(value.substring(0, NUMBER_SEPARATOR_POS));
            result.append(NUMBER_SEPARATOR_CHAR);
            if (value.length() > NUMBER_SEPARATOR_POS2) {
                result.append(value.substring(NUMBER_SEPARATOR_POS, NUMBER_SEPARATOR_POS2));
                result.append(NUMBER_SEPARATOR_CHAR);
                result.append(value.substring(NUMBER_SEPARATOR_POS2));
            } else {
                result.append(value.substring(NUMBER_SEPARATOR_POS));
            }
        } else {
            return value;
        }
        return result.toString();
    }

    @Override
    public String unformat(String value) {
        return JadeStringUtil.removeChar(value, NUMBER_SEPARATOR_CHAR);
    }
}
