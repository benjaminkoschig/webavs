/*
 * Créé le 15 janv. 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.ccvd.format;

import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CCVDNumAffilie implements IFormatData {
    private static String ERROR_MSG = "Format incorrect";
    public static int NUMBER_LENGTH = 9;
    public static char NUMBER_SEPARATOR_CHAR = '-';

    public static int NUMBER_SEPARATOR_POS = 7;

    /**
     * @param args
     */
    public static void main(String[] args) {

        CCVDNumAffilie num = new CCVDNumAffilie();

        String[] toTest = { "123", "1234", "12345", "123456", "1234567", "12345678", "123456789", "1234567-8",
                "1155570", "1155570-10" };

        for (int i = 0; i < toTest.length; i++) {
            try {
                num.check(toTest[i]);
                System.out.println("valide : " + num.format(toTest[i]) + " (" + toTest[i] + ")");
            } catch (Exception e) {
                System.out.println(e.getMessage() + " - " + num.format(toTest[i]) + " (" + toTest[i] + ")");
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.format.IFormatData#check(java.lang.Object)
     */
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
        valueStr = unformat(valueStr);
        if ((sep != -1 && valueStr.length() < NUMBER_LENGTH) || (sep == -1 && valueStr.length() < NUMBER_SEPARATOR_POS)) {
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
            result.append(value.substring(NUMBER_SEPARATOR_POS));
        } else {
            return value;
        }
        return result.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.format.IFormatData#unformat(java.lang.String)
     */
    @Override
    public String unformat(String value) {
        return JadeStringUtil.removeChar(value, NUMBER_SEPARATOR_CHAR);
    }
}
