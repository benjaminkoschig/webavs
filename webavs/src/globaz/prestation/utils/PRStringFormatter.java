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
     * Indent une string à la valeur voulue. Si la string est plus longue que la valeur définie, elle ne sera pas
     * coupée.
     * 
     * @param sb
     *            le StringBuilder à indenter
     * @param indentValue
     *            La longueur voulue de la chaîne indentée
     * @param si
     *            LEFT -> ajoute l'indenatation à gauche de la string, RIGHT indente à droite de la string
     * @return Le référence du £StringBuilder passé en paramètre.
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
