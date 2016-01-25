package globaz.babel.api.helper;

import globaz.jade.client.util.JadeStringUtil;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * <H1>Description</H1>
 * 
 * @author scr TODO : Optimiser le code en utilisant des regexp.
 */
public class CTHtmlConverter {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * <p>
     * HTML TAG ITexts tags
     * 
     * - Fontes sizes
     * 
     * <option value="1">1 (8 pt)</option>\ <option value="2">2 (10 pt)</option>\ <option value="3">3 (12 pt)</option>\
     * <option value="4">4 (14 pt)</option>\ <option value="5">5 (18 pt)</option>\ <option value="6">6 (24 pt)</option>\
     * <option value="7">7 (36 pt)</option>\
     * 
     * <font size="1-7"></font> <style size="8-36"></style> - Paragraphes
     * <p>
     * </p>
     * 
     * Styles - Bold <strong></strong> <style isBold="true"></style> - Italique <em></em> <style
     * isItalic="true"></style> - Souligné <u></u> <style isUnderline="true"></style>
     * 
     * Couleurs <span class="red"></span> <style forecolor="red"></style> <span class="green"></span> <style
     * forecolor="green"></style> <span class="blue"></span> <style forecolor="blue"></style>
     * 
     * </p>
     */
    public static final String htmlToIText(String data, String noCaisse) {

        // Suppression des paragraphes

        // Les paragpaphes sont remplacées par deux retour à la ligne, car
        // apparaissent ainsi dans tinyMCE.

        // <p>&nbsp;</p> et généré par tynyMCE

        // Ex.
        // <p>salut les petiots</p><p>&nbsp;</p>
        // devient
        // salut les petiots\n\n

        data = JadeStringUtil.change(data, "</p><p>&nbsp;</p>", "\n\n");
        data = JadeStringUtil.change(data, "<p>&nbsp;</p>", "\n\n");

        // Si le texte se termine par </p>, on le remplace par une string vide.
        // On supprime le 1er et le dernier <p> si le texte commence et se
        // termine par un paragraphe...
        // Ex.
        // <p>bonjour tout le monde</p>
        // devient
        // bonjour tout le monde

        if (data.endsWith("</p>") && data.length() > 4) {
            data = data.substring(0, data.length() - 4);
        }

        // Pour tous les autres cas...
        // Les paragpaphes sont remplacées par deux retour à la ligne, car
        // apparaissent ainsi dans tinyMCE.
        data = JadeStringUtil.change(data, "<p>", "");
        data = JadeStringUtil.change(data, "</p>", "\n\n");

        // Convertion des retours à la ligne
        data = JadeStringUtil.change(data, "<br />", "\n");
        data = JadeStringUtil.change(data, "<br/>", "\n");

        data = StringEscapeUtils.unescapeHtml(data);

        // Get styled text
        int nbrStyledText = JadeStringUtil.occurrencesOf(data, "<");
        int begin = 0;
        int end = 0;
        StringBuffer result = new StringBuffer();
        boolean first = true;
        String ss = data;

        for (int i = 0; i < nbrStyledText; i++) {
            begin = ss.indexOf("<");
            end = ss.indexOf(">");
            if (end < begin) {
                end = ss.length();
            }
            String replacedString = CTHtmlConverter.replaceStyledText(ss.substring(begin + 1, end), noCaisse);
            result.append(ss.substring(0, begin));

            result.append("<").append(replacedString).append(">");
            ss = ss.substring(end + 1, ss.length());
        }
        result.append(ss);

        String rs = result.toString();
        // Workaround, pas beau du tout !!!!
        //
        // Dans certains cas, il semblerait que IText ne supporte pas les
        // documents stylé avec un \n dans la balise style
        // en fin de texte.
        // Cela pose problème lorsque une ligne supplémetnaire du document va
        // généré une nouvelle page, et dans
        // ce cas, Itext par en boucle --> OutOfMemoryException.

        if (rs != null && rs.endsWith("\n</style>")) {
            if (rs.endsWith("\n\n\n\n\n\n\n\n\n\n\n</style>")) {
                rs = JadeStringUtil.change(rs, "\n\n\n\n\n\n\n\n\n\n\n</style>", "</style>\n\n\n\n\n\n\n\n\n\n\n");
            } else if (rs.endsWith("\n\n\n\n\n\n\n\n\n\n</style>")) {
                rs = JadeStringUtil.change(rs, "\n\n\n\n\n\n\n\n\n\n</style>", "</style>\n\n\n\n\n\n\n\n\n\n");
            } else if (rs.endsWith("\n\n\n\n\n\n\n\n\n</style>")) {
                rs = JadeStringUtil.change(rs, "\n\n\n\n\n\n\n\n\n</style>", "</style>\n\n\n\n\n\n\n\n\n");
            } else if (rs.endsWith("\n\n\n\n\n\n\n\n</style>")) {
                rs = JadeStringUtil.change(rs, "\n\n\n\n\n\n\n\n</style>", "</style>\n\n\n\n\n\n\n\n");
            } else if (rs.endsWith("\n\n\n\n\n\n\n</style>")) {
                rs = JadeStringUtil.change(rs, "\n\n\n\n\n\n\n</style>", "</style>\n\n\n\n\n\n\n");
            } else if (rs.endsWith("\n\n\n\n\n\n</style>")) {
                rs = JadeStringUtil.change(rs, "\n\n\n\n\n\n</style>", "</style>\n\n\n\n\n\n");
            } else if (rs.endsWith("\n\n\n\n\n</style>")) {
                rs = JadeStringUtil.change(rs, "\n\n\n\n\n</style>", "</style>\n\n\n\n\n");
            } else if (rs.endsWith("\n\n\n\n</style>")) {
                rs = JadeStringUtil.change(rs, "\n\n\n\n</style>", "</style>\n\n\n\n");
            } else if (rs.endsWith("\n\n\n</style>")) {
                rs = JadeStringUtil.change(rs, "\n\n\n</style>", "</style>\n\n\n");
            } else if (rs.endsWith("\n\n</style>")) {
                rs = JadeStringUtil.change(rs, "\n\n</style>", "</style>\n\n");
            } else if (rs.endsWith("\n</style>")) {
                rs = JadeStringUtil.change(rs, "\n</style>", "</style>\n");
            }
        } else if (rs != null && rs.endsWith("\n</style></style>")) {
            rs = JadeStringUtil.change(rs, "\n</style></style>", "</style></style>\n");
        } else if (rs != null && rs.endsWith("\n</style></style></style>")) {
            rs = JadeStringUtil.change(rs, "\n</style></style></style>", "</style></style></style>\n");
        } else if (rs != null && rs.endsWith("\n</style></style></style></style>")) {
            rs = JadeStringUtil.change(rs, "\n</style></style></style></style>", "</style></style></style></style>\n");
        } else if (rs != null && rs.endsWith("\n\n</style></style>")) {
            rs = JadeStringUtil.change(rs, "\n\n</style></style>", "</style></style>\n\n");
        } else if (rs != null && rs.endsWith("\n\n</style></style></style>")) {
            rs = JadeStringUtil.change(rs, "\n\n</style></style></style>", "</style></style></style>\n\n");
        } else if (rs != null && rs.endsWith("\n\n</style></style></style></style>")) {
            rs = JadeStringUtil.change(rs, "\n\n</style></style></style></style>",
                    "</style></style></style></style>\n\n");
        } else if (rs != null && rs.endsWith("\n\n\n</style></style>")) {
            rs = JadeStringUtil.change(rs, "\n\n\n</style></style>", "</style></style>\n\n\n");
        } else if (rs != null && rs.endsWith("\n\n\n</style></style></style>")) {
            rs = JadeStringUtil.change(rs, "\n\n\n</style></style></style>", "</style></style></style>\n\n\n");
        } else if (rs != null && rs.endsWith("\n\n\n</style></style></style></style>")) {
            rs = JadeStringUtil.change(rs, "\n\n\n</style></style></style></style>",
                    "</style></style></style></style>\n\n\n");
        }
        return rs;
    }

    private static String replaceStyledText(String data, String noCaisse) {

        if (data == null || data.trim().length() == 0) {
            return data;
        }

        // Specific Caisse dependant

        // CCJU
        if ("150".equals(noCaisse)) {
            // Il faut remplacer la police Gill Sans MT par Gill Sans MT_CCJU
            data = JadeStringUtil.change(data, "Gill Sans MT", "Gill Sans MT_CCJU");
        }

        // Style replacement
        data = JadeStringUtil.change(data, "strong", "style isBold=\"true\"");

        if (data.startsWith("u ") || data.length() == 1) {
            data = JadeStringUtil.change(data, "u", "style isUnderline=\"true\"");
        }

        // End tags
        if (data.startsWith("/")) {
            return "/style";
        }

        data = JadeStringUtil.change(data, "em", "style isItalic=\"true\"");
        data = JadeStringUtil.change(data, "span", "style");

        // Color replacement
        data = JadeStringUtil.change(data, "class=\"red\"", "forecolor=\"red\"");
        data = JadeStringUtil.change(data, "class=\"green\"", "forecolor=\"green\"");
        data = JadeStringUtil.change(data, "class=\"blue\"", "forecolor=\"blue\"");

        // Font size
        data = JadeStringUtil.change(data, "font", "style");

        // Font family
        data = JadeStringUtil.change(data, "face", "fontName");

        // Font size
        data = JadeStringUtil.change(data, "size=\"1\"", "size=\"8\"");
        data = JadeStringUtil.change(data, "size=\"2\"", "size=\"10\"");
        data = JadeStringUtil.change(data, "size=\"3\"", "size=\"12\"");
        data = JadeStringUtil.change(data, "size=\"4\"", "size=\"14\"");
        data = JadeStringUtil.change(data, "size=\"5\"", "size=\"18\"");
        data = JadeStringUtil.change(data, "size=\"6\"", "size=\"24\"");
        data = JadeStringUtil.change(data, "size=\"7\"", "size=\"36\"");

        return data;

    }
}
