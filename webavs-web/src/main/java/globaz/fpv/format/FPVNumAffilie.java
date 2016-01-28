package globaz.fpv.format;

import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;

public class FPVNumAffilie implements IFormatData {
    private static String ERROR_MSG = "Format du no d'affilié incorrect - falsche Abrechnugsnummer";
    public static int FORMATTED_LENGTH = 9;
    public static char NUMBER_SEPARATOR_CHAR = '.';
    public static int NUMBER_SEPARATOR_POS = 7;
    public static int UNFORMATTED_LENGTH = 8;

    public static void main(String[] args) {

        FPVNumAffilie num = new FPVNumAffilie();

        String[] toTest = { "123", // 000123
                "1234", // 001234
                "12345", // 012345
                "123456", // 123456
                "1234567", // 123456.7
                "12345678", // 123453.78

                "12.3456", // nok
                "123456.", // ok 123456
                "123456.78", // ok 123.456-78
                "12345678", // ok
                "5687410", // 056874.10
                "12710" // 012710
        };

        for (int i = 0; i < toTest.length; i++) {
            try {
                num.check(toTest[i]);
                System.out.println("valide : " + num.format(toTest[i]) + " (" + toTest[i] + ")");
            } catch (Exception e) {
                System.out.println(e.getMessage() + " - " + toTest[i] + ")");
            }
        }
    }

    /*
     * Constructeur par défaut
     */
    public FPVNumAffilie() {
    }

    /*
     * Contrôle du numéro
     * 
     * @see globaz.globall.format.IFormatData#check(java.lang.Object)
     */
    @Override
    public String check(Object value) throws Exception {
        if (JadeStringUtil.isBlank((String) value)) {
            throw new Exception(ERROR_MSG);
        }
        String valueStr = (String) value;
        int sep = valueStr.indexOf(NUMBER_SEPARATOR_CHAR);
        if (sep != -1 && sep != NUMBER_SEPARATOR_POS - 1) {
            throw new Exception(ERROR_MSG);
        }
        //
        return null;
    }

    /*
     * Formattage du numéro d'affilié
     * 
     * @see globaz.globall.format.IFormatData#format(java.lang.String)
     */
    @Override
    public String format(String value) throws Exception {
        if (value == null) {
            return null;
        }

        // BZ 4294 - Prestations
        if (value.indexOf("?") > 0) {
            return value;
        }

        value = unformat(value);
        if (value.length() > UNFORMATTED_LENGTH) {
            value = value.substring(0, UNFORMATTED_LENGTH);
        }
        StringBuffer result = new StringBuffer(value);
        if (result.length() >= NUMBER_SEPARATOR_POS) {
            while (result.length() < UNFORMATTED_LENGTH) {
                result.insert(0, '0');
            }
            result.insert(NUMBER_SEPARATOR_POS - 1, NUMBER_SEPARATOR_CHAR);
        } else {
            // Ajouter des zéros à gauche si inférieur au caractère de
            // séparation
            while (result.length() < NUMBER_SEPARATOR_POS - 1) {
                result.insert(0, '0');
            }
            // Ajouter le caractère de séparation si nécessaire
            if (result.length() >= NUMBER_SEPARATOR_POS) {
                result.insert(NUMBER_SEPARATOR_POS - 1, NUMBER_SEPARATOR_CHAR);
            }
        }
        // retour
        return result.toString();
    }

    @Override
    public String unformat(String value) throws Exception {
        return JadeStringUtil.removeChar(value, NUMBER_SEPARATOR_CHAR);
    }
}
