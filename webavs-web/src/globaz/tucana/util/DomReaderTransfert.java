package globaz.tucana.util;

import globaz.tucana.exception.transform.TUDomReaderException;
import globaz.tucana.transfert.config.ITUExportConfigXmlTags;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Lecture DOM d'un document correspondant à une arborescence de liens descendant entre classes
 * 
 * @author fgo
 * 
 */
public class DomReaderTransfert {

    /**
     * Récupértion des attributs d'un élément
     * 
     * @param root
     * @return
     */
    public static Collection getAttributes(Element root) {
        Collection container = new ArrayList();
        readRoot(root, container);
        return container;
    }

    /**
     * Retourne un document par rapport à un inputStream source
     * 
     * @param source
     * @return
     * @throws TUDomReaderException
     */
    public static Document getDocument(InputStream source) throws TUDomReaderException {
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // création d'un constructeur de documents
            DocumentBuilder constructeur = factory.newDocumentBuilder();
            // lecture du contenu d'un fichier XML avec DOM
            document = constructeur.parse(new InputSource(source));
        } catch (ParserConfigurationException pce) {
            throw new TUDomReaderException(
                    "DomReaderTransfert.getDocument : Erreur de configuration du parseur DOM lors de l'appel à fabrique.newDocumentBuilder()",
                    pce);
        } catch (SAXException se) {
            throw new TUDomReaderException(
                    "DomReaderTransfert.getDocument : Erreur lors du parsing du document lors de l'appel à construteur.parse(xml)",
                    se);
        } catch (IOException ioe) {
            throw new TUDomReaderException(
                    "DomReaderTransfert.getDocument : Erreur d'entrée/sortie lors de l'appel à construteur.parse(xml)",
                    ioe);
        } catch (Exception e) {
            throw new TUDomReaderException(
                    "DomReaderTransfert.getDocument : Erreur lors de la lecture du fichier de paramètre", e);
        }
        return document;
    }

    /**
     * Permet de récupérer le contenu "texte" entre 2 balise <balise>texte</balise>
     * 
     * @param node
     * @param container
     */
    private static void readeChild(Node node, Collection container) {
        readeNodes(node.getChildNodes(), container);
    }

    /**
     * Lecture des noeuds d'un élément
     * 
     * @param nodes
     * @param container
     */
    private static void readeNodes(NodeList nodes, Collection container) {

        for (int i = 0; i < nodes.getLength(); i++) {
            // récupération d'un noeud
            Node node = nodes.item(i);
            // si la dépendance est l'élément root on met à vide la dépendance
            String dependance = node.getParentNode().getNodeName().equalsIgnoreCase(ITUExportConfigXmlTags.NODE_ROOT) ? ""
                    : node.getParentNode().getNodeName();
            TUBalise balise = new TUBalise(node.getNodeName(), dependance);
            // lecture des enfants de ce noeud (permet de récupérer le texte
            // entre élément
            readeChild(node, container);
            if (node.hasAttributes()) {
                NamedNodeMap attrs = node.getAttributes();
                for (int j = 0; j < attrs.getLength(); j++) {
                    Node attr = attrs.item(j);
                    Map map = new TreeMap();
                    map.put(attr.getNodeName(), attr.getNodeValue());
                    balise.addAttributes(map);
                }
                container.add(balise);
            }
        }
    }

    /**
     * Lecture de l'élément root
     * 
     * @param root
     * @param container
     */
    private static void readRoot(Element root, Collection container) {
        NodeList nodes = root.getChildNodes();
        // lecture des nodes
        readeNodes(nodes, container);
    }
}
