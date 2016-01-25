package globaz.osiris.impl.print.itext.list;

import globaz.jade.client.xml.JadeXmlReader;
import globaz.jade.client.xml.JadeXmlReaderException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author dda
 * 
 *         Retrouve les libellés spécifiques des écritures de l'Extrait de compte pour la Caisse Suisse.
 */
public class CACSCSatellites {
    public static final String CODE_GENEVE = "10";

    private static Document doc = null;

    private static final String OSIRIS_CSC_SATELLITES_SOURCE_FILE = "OSIRISCSCSatellites.xml";
    private static final String TAG_CODE = "code";
    private static final String TAG_LANGUE = "langue";
    private static final String TAG_SATELLITE = "satellite";

    private static final String TAG_VALEUR = "valeur";

    public CACSCSatellites() {
        initDocuments();
    }

    /**
     * Recherche l'élément <satelitte/> qui correspond au code et à la langue.
     * 
     * @param elements
     * @param code
     * @param language
     * @return
     */
    private Element findElementSatelitte(NodeList elements, String code, String language) {
        for (int i = 0; i < elements.getLength(); i++) {
            Element e = (Element) elements.item(i);

            if (e.getAttribute(CACSCSatellites.TAG_CODE).equals(code)
                    && e.getAttribute(CACSCSatellites.TAG_LANGUE).equals(language)) {
                return e;
            }
        }

        return null;
    }

    /**
     * Retourne la localité de l'office.
     * 
     * @param code
     * @param language
     * @return
     */
    public String getLocation(String code, String language) {
        Element e = findElementSatelitte(
                CACSCSatellites.doc.getDocumentElement().getElementsByTagName(CACSCSatellites.TAG_SATELLITE), code,
                language);

        if (e != null) {
            return e.getAttribute(CACSCSatellites.TAG_VALEUR);
        } else {
            return "";
        }
    }

    /**
     * Initialise le document XML. Document est statique et ne sera initialiser quune foi pour des raisons de
     * performance.
     * 
     */
    private void initDocuments() {
        if (CACSCSatellites.doc == null) {
            try {
                CACSCSatellites.doc = JadeXmlReader.parseFile(this.getClass().getResourceAsStream(
                        '/' + CACSCSatellites.OSIRIS_CSC_SATELLITES_SOURCE_FILE));
            } catch (JadeXmlReaderException e) {
                e.printStackTrace();
            }
        }
    }
}
