package globaz.tucana.util;

import globaz.globall.db.BEntity;
import globaz.tucana.exception.process.TUInitExportException;
import globaz.tucana.transfert.config.ITUExportXmlTags;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Classe permettant d'encoder un document en XML
 * 
 * @author fgo
 * 
 */
public class DomEncoder {
    /**
     * Cr�ation de l'�l�ment root. Le nom de l'�l�ment est d�fini dans l'interface ITUExportXmlTags
     * 
     * @param encoder
     */
    private static void createRootElement(DomEncoder encoder) {
        encoder.root = encoder.document.createElement(ITUExportXmlTags.NODE_ROOT);

    }

    /**
     * Permet de terminer le document correctement en rattachant l'�l�ment root au document
     * 
     * @param encoder
     */
    public static void endEncoder(DomEncoder encoder) {
        encoder.document.appendChild(encoder.root);
    }

    /**
     * Initialisation de l'instance de DomEncoder et d�finition de l'�l�ment root
     * 
     * @param encoder
     * @throws TUInitExportException
     */
    private static void initialize(DomEncoder encoder) throws TUInitExportException {
        DocumentBuilderFactory entite = DocumentBuilderFactory.newInstance();
        DocumentBuilder constructeur;
        try {
            constructeur = entite.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new TUInitExportException("DomEncoder.initialize()", e);
        }
        encoder.document = constructeur.newDocument();
        createRootElement(encoder);
        encoder.root = encoder.document.createElement(ITUExportXmlTags.NODE_ROOT);
    }

    /**
     * R�cup�re une instance
     * 
     * @return retourne une instance de la classe DomEncoder
     * @throws TUInitExportException
     */
    public static DomEncoder newInstance() throws TUInitExportException {
        DomEncoder encoder = new DomEncoder();
        initialize(encoder);
        return encoder;
    }

    /**
     * Permet la r�cup�ration des attributs publiques de la classe afin de les g�rer en �l�ments et les rattacher au
     * niveau de l'�l�ment p�re
     * 
     * @param theDocument
     * @param elementAttributes
     * @param method
     * @param entity
     * @throws TUInitExportException
     */
    private static void parseMethod(Document theDocument, Element elementAttributes, Method method, BEntity entity)
            throws TUInitExportException {
        try {
            // Regarde si la m�thode est de type "get" ou "is", si c'est le cas,
            // la traite
            if ((method.getName().indexOf("get") == 0 && String.class.equals(method.getReturnType()))
                    || (method.getName().indexOf("is") == 0 && (boolean.class.equals(method.getReturnType()) || Boolean.class
                            .equals(method.getReturnType())))) {
                // D�fini le pr�fix de la m�thode
                String prefix = method.getName().indexOf("get") == 0 ? "get" : "is";

                // R�cup�re le nom
                String name = method
                        .getName()
                        .substring(method.getName().indexOf(prefix) + prefix.length(),
                                method.getName().indexOf(prefix) + prefix.length() + 1).toLowerCase();
                name += method.getName().substring(method.getName().indexOf(prefix) + prefix.length() + 1);
                // Cr�e le nouvel �l�ment avec le nom de l'attribut courant
                Element currentElement = theDocument.createElement(name);
                // Lui d�fini un attribut "value" qui contient la valeur
                // contenue dans l'objet pour l'attribut courante
                currentElement.setAttribute("value", method.invoke(entity, new Object[] {}).toString());
                // Ajoute � l'�l�ment p�re l'�l�ment nouvellement cr��
                elementAttributes.appendChild(currentElement);
            }
        } catch (IllegalArgumentException e) {
            throw new TUInitExportException("DomEncoder.parseMethod() : arguments erron�s", e);
        } catch (IllegalAccessException e) {
            throw new TUInitExportException("DomEncoder.parseMethod() : acc�s ill�gal", e);
        } catch (InvocationTargetException e) {
            throw new TUInitExportException("DomEncoder.parseMethod() : erreur lors de l'invocation du taget", e);
        }
    }

    private Document document = null;

    private Element root = null;

    /**
     * Constructeur
     */
    private DomEncoder() {
        super();
    }

    /**
     * Retourne le document
     * 
     * @return
     */
    public Document getDocument() {
        return document;
    }

    /**
     * R�cup�re l'attribut root de la classe
     * 
     * @return
     */
    public Element getRoot() {
        return root;
    }

    /**
     * Cr�er un �l�ment et ses enfants (propri�t�s) � partir d'une entit�e
     * 
     * @param entity
     * @param document
     * @return
     * @throws TUInitExportException
     */
    public Element makeElement(BEntity entity, Document document) throws TUInitExportException {
        Element elementClasse = document.createElement(entity.getClass().getName());
        Element elementAttributes = document.createElement(ITUExportXmlTags.NODE_ATTRIBUTES);
        Method[] methods = entity.getClass().getDeclaredMethods();
        // Pour chaque m�thode
        for (int i = 0; i < methods.length; i++) {
            // On la parse
            parseMethod(document, elementAttributes, methods[i], entity);
        }
        elementClasse.appendChild(elementAttributes);
        return elementClasse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        } catch (TransformerConfigurationException e) {
            return e.toString();
        } catch (TransformerException e) {
            return e.toString();
        }
        return writer.toString();
    }
}
