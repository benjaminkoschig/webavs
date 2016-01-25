package globaz.fer.format;

import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Gestion du format du num�ro d'affiliation de la FER.
 * 
 * @author: David Girardin
 */
public class FERNumAffilie implements IFormatData {

    private static String ERROR_MSG = "wrong format";
    public static int NUMBER_LENGTH = 8;
    public static char NUMBER_SEPARATOR_CHAR = '.';
    public static int NUMBER_SEPARATOR_POS = 3;
    public static char TYPE_SEPARATOR_CHAR = '-';

    public static int TYPE_SEPARATOR_POS = 6;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.04.2003 12:04:25)
     * 
     * @param args
     *            java.lang.String[]
     */
    public static void main(String[] args) {

        FERNumAffilie num = new FERNumAffilie();

        String[] toTest = { "123", // 000.123
                "1234", // 001.234
                "12345", // 012.345
                "123456", // 123.456
                "1234567", // 123.456-07
                "12345678", // 123.456-78

                "12.3456", // nok
                "123.456", // ok 123.456
                "123.456.78", // ok 123.456-78
                "123.456-78" // ok
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
    public FERNumAffilie() {
        super();
    }

    /**
     * Test le format du num�ro donn�
     * 
     * @param value
     *            num�ro d'affili� � tester de type String
     * @return toujours null
     * @throws Exception
     *             si le num�ro n'est pas valide
     */
    @Override
    public String check(Object value) throws Exception {
        if (JadeStringUtil.isBlank((String) value)) {
            throw new Exception(ERROR_MSG);
        }
        String valueStr = (String) value;
        int sep = valueStr.indexOf(NUMBER_SEPARATOR_CHAR);
        if (sep != -1 && sep != NUMBER_SEPARATOR_POS) {
            throw new Exception(ERROR_MSG);
        }
        sep = valueStr.indexOf(TYPE_SEPARATOR_CHAR);
        if (sep != -1 && sep != TYPE_SEPARATOR_POS + 1) {
            throw new Exception(ERROR_MSG);
        }
        /*
         * String valueStr = unformat((String)value); // bonne longueur ? if (valueStr.length() < NUMBER_LENGTH) { throw
         * new Exception (ERROR_MSG); }
         */
        // TODO est-ce que d'autres plausibilit� du FC doivent �tre impl�ment�e?
        return null;
    }

    /**
     * Format le num�ro d'affili� donn�
     * 
     * @parmam le num�ro d'affili�
     * @return le num�ro format� ou vide si le param�tre est null
     */
    @Override
    public String format(String value) {
        if (value == null) {
            return null;
        }
        value = unformat(value);
        StringBuffer result = new StringBuffer(value);
        // ajouter les z�ros � gauche
        while (result.length() < TYPE_SEPARATOR_POS) {
            result.insert(0, '0');
        }
        if (result.length() == TYPE_SEPARATOR_POS + 1) {
            // 7 chiffres, ajouter un z�ro � la fin
            result.insert(TYPE_SEPARATOR_POS, '0');
        } /*
           * else if (result.length()==TYPE_SEPARATOR_POS){ result.append("00"); }
           */
        result.insert(NUMBER_SEPARATOR_POS, NUMBER_SEPARATOR_CHAR);
        if (result.length() > TYPE_SEPARATOR_POS + 1) {
            result.insert(TYPE_SEPARATOR_POS + 1, TYPE_SEPARATOR_CHAR);
        }
        /*
         * if(value.length()==NUMBER_SEPARATOR_POS) { // nombre � six chiffres, �jout de 00
         * result.append(value.substring(0,NUMBER_SEPARATOR_POS)); result.append(NUMBER_SEPARATOR_CHAR);
         * if(value.length()>TYPE_SEPARATOR_POS) { result.append(value.substring(
         * NUMBER_SEPARATOR_POS,TYPE_SEPARATOR_POS)); result.append(TYPE_SEPARATOR_CHAR);
         * result.append(value.substring(TYPE_SEPARATOR_POS)); } else {
         * result.append(value.substring(NUMBER_SEPARATOR_POS)); } } else { result.append(value); }
         */

        value = result.toString();
        return value;
    }

    /**
     * Retourne le num�ro d'affili� sans caract�re de s�paration
     * 
     * @param value
     *            le num�ro d'affili� avec ou sans caract�res de s�paration
     * @return le num�ro d�format� ou vide si value est null
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
            if (c != NUMBER_SEPARATOR_CHAR && c != TYPE_SEPARATOR_CHAR) {
                str.append(c);
            }
        }

        return str.toString();
    }
}
