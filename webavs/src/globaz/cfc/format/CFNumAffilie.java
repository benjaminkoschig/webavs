package globaz.cfc.format;

import globaz.globall.format.IFormatData;

/**
 * Insérez la description du type ici. Date de création : (14.02.2003 14:25:28)
 * 
 * @author: Administrator
 */
public class CFNumAffilie implements IFormatData {
    /**
     * Insérez la description de la méthode ici. Date de création : (14.04.2003 12:04:25)
     * 
     * @param args
     *            java.lang.String[]
     */
    public static void main(String[] args) {

        CFNumAffilie num = new CFNumAffilie();

        String[] toTest = { "123.456", "ABC.DEF", "347691", "347/591", "34759", "34.759",

        "347591", // ok
                "347.591", // ok
                "100.800", // ok
                "555102" };

        for (int i = 0; i < toTest.length; i++) {
            try {
                num.check(toTest[i]);
                System.out.println("valide : " + num.format(toTest[i]) + " (" + toTest[i] + ")");
            } catch (Exception e) {
                System.out.println(e.getMessage() + " - " + toTest[i]);
            }
        }

    }

    String ERROR = "Format de numéro d'affilié invalide.";

    String ERROR_MODULO = "impossible de calculer le modulo (5 chiffre minium obligatoire)";

    char SEPARATOR_CHAR = '.';

    /**
     * Commentaire relatif au constructeur CFNumAffilie.
     */
    public CFNumAffilie() {
        super();
    }

    @Override
    public String check(Object value) throws Exception {
        String valueStr = (String) value;
        int chiffre6; // crc entré

        // deformatage avant le control du crc
        try {
            valueStr = unformat(valueStr);
        } catch (Exception e) {
            throw new Exception(ERROR);
        }

        // bonne longueur apres deformatage ?
        if (valueStr.length() != 6) {
            throw new Exception(ERROR);
        }

        // separation des chiffres
        try {
            chiffre6 = Integer.parseInt(valueStr.substring(5, 6));

        } catch (Exception e) {
            throw new Exception(ERROR);
        }

        int crc = getModulo(valueStr);
        // test final
        if (crc != chiffre6) {
            throw new Exception(ERROR);
        }

        return null;
    }

    @Override
    public String format(String value) throws Exception {
        if (value == null) {
            return "";
        } else if (value.length() > 3) {
            value = unformat(value);
            value = value.substring(0, 3) + SEPARATOR_CHAR + value.substring(3);
        }

        return value;

    }

    public int getModulo(String valueStr) throws Exception {
        int chiffre1;
        int chiffre2;
        int chiffre3;
        int chiffre4;
        int chiffre5;

        // separation des chiffres
        try {
            chiffre1 = Integer.parseInt(valueStr.substring(0, 1));
            chiffre2 = Integer.parseInt(valueStr.substring(1, 2));
            chiffre3 = Integer.parseInt(valueStr.substring(2, 3));
            chiffre4 = Integer.parseInt(valueStr.substring(3, 4));
            chiffre5 = Integer.parseInt(valueStr.substring(4, 5));

        } catch (Exception e) {
            throw new Exception(ERROR_MODULO);
        }

        // calcul du crc
        chiffre1 *= 2;
        if (chiffre1 > 9) {
            chiffre1 -= 9;
        }
        chiffre3 *= 2;
        if (chiffre3 > 9) {
            chiffre3 -= 9;
        }
        chiffre5 *= 2;
        if (chiffre5 > 9) {
            chiffre5 -= 9;
        }

        int crc = chiffre1 + chiffre2 + chiffre3 + chiffre4 + chiffre5;
        crc %= 10;
        crc = 10 - crc;
        if (crc == 10) {
            crc = 0;
        }

        return crc;

    }

    public String getValidNumber(String num) throws Exception {
        int crc = getModulo(num);
        return format(num + crc);
    }

    @Override
    public String unformat(String value) throws Exception {

        if (value == null) {
            return "";
        }
        String str = "";

        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) != '.') {
                str += value.charAt(i);
            }
        }

        return str;
    }
}
