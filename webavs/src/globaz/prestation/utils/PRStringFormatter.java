package globaz.prestation.utils;

import globaz.jade.client.util.JadeStringUtil;

public class PRStringFormatter {

    public enum StringIndentation {
        LEFT,
        RIGHT;
    }

    public static String indent(String sb, int indentValue, String indentationChar, StringIndentation si) {
        return PRStringFormatter.indent(new StringBuilder(sb), indentValue, indentationChar, si).toString();
    }

    /**
     * Indent une string � la valeur voulue. Si la string est plus longue que la valeur d�finie, elle ne sera pas
     * coup�e.
     * 
     * @param sb
     *            le StringBuilder � indenter
     * @param indentValue
     *            La longueur voulue de la cha�ne indent�e
     * @param si
     *            LEFT -> ajoute l'indenatation � gauche de la string, RIGHT indente � droite de la string
     * @return Le r�f�rence du �StringBuilder pass� en param�tre.
     */
    public static StringBuilder indent(StringBuilder sb, int indentValue, String indentationChar, StringIndentation si) {
        if ((sb != null) && (si != null) && !JadeStringUtil.isEmpty(indentationChar)) {
            if (si.equals(StringIndentation.LEFT)) {
                StringBuilder leftString = new StringBuilder();
                while ((sb.length() + leftString.length()) < indentValue) {
                    leftString.append(indentationChar);
                }
                sb = new StringBuilder(leftString.toString() + sb.toString());
            } else {
                while (sb.length() < indentValue) {
                    sb.append(indentationChar);
                }
            }
        }
        return sb;
    }

    /**
     * Indent la string a gauche avec des espaces
     * 
     * @param sb
     * @param indentValue
     * @return
     */
    public static String indentLeft(String value, int indentValue) {
        return PRStringFormatter.indent(new StringBuilder(value), indentValue, " ", StringIndentation.LEFT).toString();
    }

    public static String indentLeft(String value, int indentValue, String indentationChar) {
        return PRStringFormatter.indent(new StringBuilder(value), indentValue, indentationChar, StringIndentation.LEFT)
                .toString();
    }

    /**
     * Indent la string a droite avec des espaces
     * 
     * @param sb
     * @param indentValue
     * @return
     */
    public static String indentRight(String value, int indentValue) {
        return PRStringFormatter.indent(new StringBuilder(value), indentValue, " ", StringIndentation.RIGHT).toString();
    }

    public static String indentRight(String value, int indentValue, String indentationChar) {
        return PRStringFormatter
                .indent(new StringBuilder(value), indentValue, indentationChar, StringIndentation.RIGHT).toString();
    }
}
