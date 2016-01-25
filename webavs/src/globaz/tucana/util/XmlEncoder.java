package globaz.tucana.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Xml encoder, Crée une chaîne de caractère XML spécifiée de la manière suivante :<br/>
 * <br/>
 * &lt;object classname=&quot; le nom de la classe&quot;&gt; <br/>
 * &#160;&#160;&#160;&#160;&lt;nom_attribut value=&quot;la valeur de l'attribut&quot;&gt; <br/>
 * &lt;/object&gt; <br/>
 * <br/>
 * 
 * Il y a autant d'attributs qu'il y a de méthodes de type <code>get</code> implémenté dans l'objet
 * 
 * @author vyj
 * 
 * @version 1.0 created the 27 janv. 2005
 */
public class XmlEncoder {
    /**
     * L'object à parser
     */
    private Object toParse = null;

    /**
     * Constructor for XmlEncoder
     * 
     * @param _toParse
     *            L'object à parser
     */
    public XmlEncoder(Object _toParse) {
        super();
        toParse = _toParse;
    }

    /**
     * Permet de parser en format XML l'object passé en paramètre au constructeur.
     * 
     * @param theDocument
     *            Le document dans lequel il faut ajouter les informations de parsing
     * @throws IllegalArgumentException
     *             Levée dans le cas où l'objet à parser n'est pas défini
     */
    private void parse(Document theDocument) throws IllegalArgumentException {
        if (toParse != null) {
            // Parse l'objet
            parseObject(theDocument);
        } else {
            throw new IllegalArgumentException("Object to parse is null");
        }
    }

    /**
     * Permet de parser en format XML l'object passé en paramètre au constructeur.
     * 
     * @param path
     *            Chemin d'accès au fichier de sauvegarde (si il n'existe pas, le créée)
     * @throws ParserConfigurationException
     *             Levée dans le cas de problème lors de la configuration du parser xml
     * @throws FactoryConfigurationError
     *             Levée en cas de problème lors de l'initialisation de la factory de builder de documents
     * @throws SAXException
     *             Levée dans le cas de problème lors de la lecture du document
     * @throws IOException
     *             Levée en cas de problème dans la gestion des flux I/O
     * @throws TransformerFactoryConfigurationError
     *             Levée dans le cas de problème lors de la configuration de la factory du transformer xml
     * @throws TransformerException
     *             Levée dans le cas de problème lors de la transformation xml (écriture du fichier dans notre cas)
     * @throws IllegalArgumentException
     *             Levée dans le cas où l'objet à parser n'est pas défini
     */
    public Document parse(String path) throws ParserConfigurationException, FactoryConfigurationError, SAXException,
            IOException, TransformerFactoryConfigurationError, TransformerException, IllegalArgumentException {
        // Déclare les flux
        Document theDocument = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            // Déclare les objets permettant la lecture du fichier passé en
            // paramètre si il existe

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            try {
                // Essaie de lire le fichier et instancie le document xml si le
                // fichier existe
                fis = new FileInputStream(path);
                theDocument = builder.parse(fis);
            } catch (FileNotFoundException e) {
                // Si le fichier n'existe pas, alors créée un nouveau document
                theDocument = builder.newDocument();
                Element rootElement = theDocument.createElement("all-objects");
                theDocument.appendChild(rootElement);
            }
            // Parse l'objet et l'ajoute au document
            parse(theDocument);

            // Ecrit le document sur le disque, pour ce faire, utilise le
            // transformer xml. Il nécessite une source définie par le document
            // xml
            // (-> instancie un object de type DOMSource) et un objet qui défini
            // où
            // écrire le résultat (-> instanciation d'un objet StreamResult sur
            // la
            // base d'un FileOutputStream défini par le path passé en paramètre=
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            fos = new FileOutputStream(path);
            Source source = new DOMSource(theDocument);
            Result result = new StreamResult(fos);
            transformer.transform(source, result);

        } finally {
            // Pour finir, ferme les flux ouverts
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return theDocument;
    }

    /**
     * Permet de gérer un attribut en fonction de sa méthode get
     * 
     */
    /**
     * Permet de gérer un attribut en fonction de sa méthode get
     * 
     * @param theDocument
     *            Le document xml père
     * @param element
     *            L'élément père de l'élément à créé pour cet attribut
     * @param method
     *            La méthode d'accès utilisée pour récupérer le nom de l'attribut et sa valeur
     */
    private void parseMethod(Document theDocument, Element element, Method method) {
        try {
            // Regarde si la méthode est de type "get" ou "is", si c'est le cas,
            // la traite
            if ((method.getName().indexOf("get") == 0 && String.class.equals(method.getReturnType()))
                    || (method.getName().indexOf("is") == 0 && (boolean.class.equals(method.getReturnType()) || Boolean.class
                            .equals(method.getReturnType())))) {
                // Défini le préfix de la méthode
                String prefix = method.getName().indexOf("get") == 0 ? "get" : "is";

                // Récupère le nom
                String name = method
                        .getName()
                        .substring(method.getName().indexOf(prefix) + prefix.length(),
                                method.getName().indexOf(prefix) + prefix.length() + 1).toLowerCase();
                name += method.getName().substring(method.getName().indexOf(prefix) + prefix.length() + 1);
                // Crée le nouvel élément avec le nom de l'attribut courant
                Element currentElement = theDocument.createElement(name);
                // Lui défini un attribut "value" qui contient la valeur
                // contenue dans l'objet pour l'attribut courante
                currentElement.setAttribute("value", method.invoke(toParse, new Object[] {}).toString());
                // Ajoute à l'élément père l'élément nouvellement créé
                element.appendChild(currentElement);
            }
        } catch (IllegalArgumentException e) {
            // Alors ne traite pas l'information
        } catch (IllegalAccessException e) {
            // Alors ne traite pas l'information
        } catch (InvocationTargetException e) {
            // Alors ne traite pas l'information
        }
    }

    /**
     * Permet de parser l'objet
     * 
     * @param theDocument
     *            Le document dans lequel il faut ajouter les informations de parsing
     */
    private void parseObject(Document theDocument) {
        // Crée l'élément objet
        Element currentObjectElement = theDocument.createElement("object");
        // Ajoute le nouvel élément à l'élément racine du document
        theDocument.getDocumentElement().appendChild(currentObjectElement);
        // Lui donne le nom de la classe
        currentObjectElement.setAttribute("classname", toParse.getClass().getName());
        // Lui défini quant il a été ajouté
        currentObjectElement.setAttribute("date",
                new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));

        // Recherche toutes les méthodes que l'objet déclare
        // Method[] methods = toParse.getClass().getMethods();
        /*
         * Mis en commentaire ci-dessus, car ne veut que les méthodes de la classe et non celles héritées
         */
        Method[] methods = toParse.getClass().getDeclaredMethods();
        // Pour chaque méthode
        for (int i = 0; i < methods.length; i++) {
            // On la parse
            parseMethod(theDocument, currentObjectElement, methods[i]);
        }
    }
}