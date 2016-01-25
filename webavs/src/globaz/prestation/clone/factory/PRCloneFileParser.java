/*
 * Créé le 28 avr. 05
 * 
 * Description :
 */
package globaz.prestation.clone.factory;

import globaz.prestation.clone.xml.data.IPRXmlElement;
import globaz.prestation.clone.xml.data.PRXmlCloneElement;
import globaz.prestation.clone.xml.data.PRXmlLinkedClassElement;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * <H1>Description</H1>
 * 
 * 
 * @author scr
 * 
 *         <p>
 *         Descpription
 *         </p>
 */
public class PRCloneFileParser {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String ATTRIBUT_CLASS = "class";
    private static final String ATTRIBUT_FOR_ID_PARENT = "forIdParent";
    private static final String ATTRIBUT_ID = "id";

    private static final String ATTRIBUT_ID_PARENT = "idParent";
    private static final String ATTRIBUT_MANAGER = "manager";

    private static final String ATTRIBUT_RELATION_TYPE = "relationType";
    private static final String TAG_CLONE = "clone";
    private static final String TAG_LINKED_CLASS = "linkedClass";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Chargement du fichier xml des définitions de clonage.
     * 
     * @param fileName
     *            DOCUMENT ME!
     * @param id
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public PRXmlCloneElement loadFile(String fileName, String id) throws Exception {
        InputStream inp = PRCloneFileParser.class.getResourceAsStream("/" + fileName);
        InputSource is = new InputSource(inp);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(is);

        if (document == null) {
            throw (new Exception("Impossible de lire le fichier : " + fileName));
        }

        if (id == null) {
            throw (new Exception("Référence unique non renseignée"));
        }

        NodeList nodes = document.getElementsByTagName(TAG_CLONE);

        IPRXmlElement cloneElement = null;

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            // récupération des attribut
            if ((node.getAttributes() != null)
                    && id.equals(node.getAttributes().getNamedItem(ATTRIBUT_ID).getNodeValue())) {
                cloneElement = new PRXmlCloneElement();

                String ref = node.getAttributes().getNamedItem(ATTRIBUT_ID).getNodeValue();
                String cloneClassName = node.getAttributes().getNamedItem(ATTRIBUT_CLASS).getNodeValue();

                ((PRXmlCloneElement) cloneElement).setClassName(cloneClassName);
                ((PRXmlCloneElement) cloneElement).setId(ref);

                // classes liées...
                NodeList childs = node.getChildNodes();

                for (int c = 0; c < childs.getLength(); c++) {
                    if (childs.item(c).getNodeName().equals(TAG_LINKED_CLASS)) {
                        cloneElement = parseLinkedClass(childs.item(c), cloneElement);
                    }
                }
            }
        }

        return (PRXmlCloneElement) cloneElement;
    }

    private IPRXmlElement parseLinkedClass(Node linkedClass, IPRXmlElement cloneElement) {
        IPRXmlElement element = new PRXmlLinkedClassElement();

        if (linkedClass.getAttributes().getNamedItem(ATTRIBUT_CLASS) != null) {
            String linkedClassName = linkedClass.getAttributes().getNamedItem(ATTRIBUT_CLASS).getNodeValue();
            String linkedClassIdParent = linkedClass.getAttributes().getNamedItem(ATTRIBUT_ID_PARENT).getNodeValue();
            String linkedClassForIdParent = linkedClass.getAttributes().getNamedItem(ATTRIBUT_FOR_ID_PARENT)
                    .getNodeValue();
            String relationType = linkedClass.getAttributes().getNamedItem(ATTRIBUT_RELATION_TYPE).getNodeValue();
            String manager = linkedClass.getAttributes().getNamedItem(ATTRIBUT_MANAGER).getNodeValue();

            ((PRXmlLinkedClassElement) element).setClassName(linkedClassName);
            ((PRXmlLinkedClassElement) element).setIdParent(linkedClassIdParent);
            ((PRXmlLinkedClassElement) element).setForIdParent(linkedClassForIdParent);
            ((PRXmlLinkedClassElement) element).setManagerClassName(manager);
            ((PRXmlLinkedClassElement) element).setRelationType(relationType);

            cloneElement.addXmlElement(element);

            NodeList childs = linkedClass.getChildNodes();

            for (int c = 0; c < childs.getLength(); c++) {
                if (childs.item(c).getNodeName().equals(TAG_LINKED_CLASS)) {
                    element = parseLinkedClass(childs.item(c), element);
                }
            }
        }

        return cloneElement;
    }
}
