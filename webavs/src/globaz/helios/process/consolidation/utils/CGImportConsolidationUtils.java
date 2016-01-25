package globaz.helios.process.consolidation.utils;

import org.w3c.dom.Element;

public class CGImportConsolidationUtils {

    private static final int FIRST_ITEM = 0;

    /**
     * Retourne la valeur du premier enfant de l'élément.
     * 
     * @param e
     * @param tag
     * @return
     * @throws Exception
     */
    public static String getChildFirstValue(Element e, String tag) throws Exception {
        Element child = getFirstChildElement(e, tag);

        if (child.hasChildNodes()) {
            return child.getFirstChild().getNodeValue();
        } else {
            return null;
        }
    }

    /**
     * Retourne le premier enfant correspondant au tag souhaité.
     * 
     * @param parent
     * @param tag
     * @return
     * @throws Exception
     */
    private static Element getFirstChildElement(Element parent, String tag) throws Exception {
        return (Element) parent.getElementsByTagName(tag).item(FIRST_ITEM);
    }

    /**
     * Retourne l'id de l'écriture double/collective.
     * 
     * @param e
     * @return
     */
    public static String getPeriodeCode(Element e) {
        if (e != null) {
            return e.getAttribute("code");
        } else {
            return new String();
        }
    }
}
