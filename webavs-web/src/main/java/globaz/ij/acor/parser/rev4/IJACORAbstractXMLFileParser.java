package globaz.ij.acor.parser.rev4;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.prestation.acor.PRACORException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJACORAbstractXMLFileParser {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * construit une map contenant tous les nodes enfant de element indexes par leur nom
     * 
     * @param element
     * 
     * @return
     */
    protected static final Map childrenMap(Node element) {
        HashMap retValue = new HashMap();

        for (int idChild = 0; idChild < element.getChildNodes().getLength(); ++idChild) {
            Node node = element.getChildNodes().item(idChild);

            retValue.put(node.getNodeName(), node);
        }

        return retValue;
    }

    /**
     * retourne le node enfant portant le nom childeNodeName
     * 
     * @param parent
     * @param childNodeName
     * 
     * @return
     * 
     * @throws PRACORException
     */
    protected static final Node findChildNode(Node parent, String childNodeName) throws PRACORException {
        NodeList children = parent.getChildNodes();
        Node child;

        for (int idChild = 0; idChild < children.getLength(); ++idChild) {
            child = children.item(idChild);

            if (child.getNodeName().equals(childNodeName)) {
                return child;
            }
        }

        throw new PRACORException("format de fichier xml invalide: impossible de trouver une balise: " + childNodeName);
    }

    /**
     * retourne le node de nom name ou lance une exception si required est vrai et qu'il n'y a pas de node de ce nom.
     * 
     * @param nodes
     * @param name
     * @param required
     * 
     * @return
     * 
     * @throws PRACORException
     */
    protected static final Node getNode(Map nodes, String name, boolean required) throws PRACORException {
        Node retValue = (Node) nodes.get(name);

        if (required && (retValue == null)) {
            throw new PRACORException("format de fichier xml invlaide: balise requise " + name);
        }

        return retValue;
    }

    /**
     * retourne la valeur en tant que date du node ou chaine vide si node inexistant ou lance une exception si required
     * est vrai et qu'il n'y a pas de node de ce nom.
     * 
     * @param nodes
     * @param nodeName
     * @param childName
     *            DOCUMENT ME!
     * @param required
     * 
     * @return
     * 
     * @throws PRACORException
     */
    protected static final String getNodeChildDateValue(Map nodes, String nodeName, String childName, boolean required)
            throws PRACORException {
        String date = getNodeChildValue(nodes, nodeName, childName, required);

        if (!JAUtil.isDateEmpty(date)) {
            try {
                return new JADate(date).toStr(".");
            } catch (JAException e) {
                throw new PRACORException("format de date invalide: " + date);
            }
        } else {
            return date;
        }
    }

    /**
     * retourne la valeur du node enfant d'un node ou chaine vide si node inexistant ou lance une exception si required
     * est vrai et qu'il n'y a pas de nodes de ce nom.
     * 
     * @param nodes
     * @param nodeName
     * @param childName
     *            DOCUMENT ME!
     * @param required
     * 
     * @return
     * 
     * @throws PRACORException
     */
    protected static final String getNodeChildValue(Map nodes, String nodeName, String childName, boolean required)
            throws PRACORException {
        Node parent = getNode(nodes, nodeName, required);

        if (parent != null) {
            Node child = findChildNode(parent, childName);

            if (child != null) {
                return getNodeValue(child);
            } else {
                if (required) {
                    throw new PRACORException("format de fichier xml invlaide: balise requise " + childName);
                }
            }
        } else {
            if (required) {
                throw new PRACORException("format de fichier xml invlaide: balise requise " + nodeName);
            }
        }

        return "";
    }

    /**
     * retourne la valeur en tant que date du node ou chaine vide si node inexistant ou lance une exception si required
     * est vrai et qu'il n'y a pas de node de ce nom.
     * 
     * @param nodes
     * @param name
     * @param required
     * 
     * @return
     * 
     * @throws PRACORException
     */
    protected static final String getNodeDateValue(Map nodes, String name, boolean required) throws PRACORException {
        String date = getNodeValue(nodes, name, required);

        if (!JAUtil.isDateEmpty(date)) {
            try {
                return new JADate(date).toStr(".");
            } catch (JAException e) {
                throw new PRACORException("format de date invalide: " + date);
            }
        } else {
            return date;
        }
    }

    /**
     * retourne la valeur du node ou chaine vide si node inexistant ou lance une exception si required est vrai et qu'il
     * n'y a pas de node de ce nom.
     * 
     * @param nodes
     * @param name
     * @param required
     * 
     * @return
     * 
     * @throws PRACORException
     */
    protected static final String getNodeValue(Map nodes, String name, boolean required) throws PRACORException {
        Node node = getNode(nodes, name, required);

        if (node != null) {
            return getNodeValue(node);
        } else {
            if (required) {
                throw new PRACORException("format de fichier xml invlaide: balise requise " + name);
            } else {
                return "";
            }
        }
    }

    /**
     * getter pour l'attribut node value
     * 
     * @param node
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut node value
     */
    protected static final String getNodeValue(Node node) {
        if (node.hasChildNodes()) {
            node.normalize();

            return node.getFirstChild().getNodeValue();
        } else {
            return "";
        }
    }

    /**
     * charge un document xml en memoire
     * 
     * @param reader
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    protected static final Document loadDocument(Reader reader) throws PRACORException {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.parse(new InputSource(reader));
        } catch (Exception e) {
            throw new PRACORException("impossible de lire le fichier xml");
        }
    }

    /**
     * sauve dans la base ou lance une exception si la sauvegarde est impossible.
     * 
     * @param session
     * @param entity
     * 
     * @throws PRACORException
     */
    protected static final void save(BSession session, BEntity entity) throws PRACORException {
        entity.setSession(session);

        try {
            entity.add();
        } catch (Exception e) {
            throw new PRACORException("impossible de sauver dans la base", e);
        }
    }
}
