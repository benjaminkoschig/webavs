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
 * Xml encoder, Cr�e une cha�ne de caract�re XML sp�cifi�e de la mani�re suivante :<br/>
 * <br/>
 * &lt;object classname=&quot; le nom de la classe&quot;&gt; <br/>
 * &#160;&#160;&#160;&#160;&lt;nom_attribut value=&quot;la valeur de l'attribut&quot;&gt; <br/>
 * &lt;/object&gt; <br/>
 * <br/>
 * 
 * Il y a autant d'attributs qu'il y a de m�thodes de type <code>get</code> impl�ment� dans l'objet
 * 
 * @author vyj
 * 
 * @version 1.0 created the 27 janv. 2005
 */
public class XmlEncoder {
    /**
     * L'object � parser
     */
    private Object toParse = null;

    /**
     * Constructor for XmlEncoder
     * 
     * @param _toParse
     *            L'object � parser
     */
    public XmlEncoder(Object _toParse) {
        super();
        toParse = _toParse;
    }

    /**
     * Permet de parser en format XML l'object pass� en param�tre au constructeur.
     * 
     * @param theDocument
     *            Le document dans lequel il faut ajouter les informations de parsing
     * @throws IllegalArgumentException
     *             Lev�e dans le cas o� l'objet � parser n'est pas d�fini
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
     * Permet de parser en format XML l'object pass� en param�tre au constructeur.
     * 
     * @param path
     *            Chemin d'acc�s au fichier de sauvegarde (si il n'existe pas, le cr��e)
     * @throws ParserConfigurationException
     *             Lev�e dans le cas de probl�me lors de la configuration du parser xml
     * @throws FactoryConfigurationError
     *             Lev�e en cas de probl�me lors de l'initialisation de la factory de builder de documents
     * @throws SAXException
     *             Lev�e dans le cas de probl�me lors de la lecture du document
     * @throws IOException
     *             Lev�e en cas de probl�me dans la gestion des flux I/O
     * @throws TransformerFactoryConfigurationError
     *             Lev�e dans le cas de probl�me lors de la configuration de la factory du transformer xml
     * @throws TransformerException
     *             Lev�e dans le cas de probl�me lors de la transformation xml (�criture du fichier dans notre cas)
     * @throws IllegalArgumentException
     *             Lev�e dans le cas o� l'objet � parser n'est pas d�fini
     */
    public Document parse(String path) throws ParserConfigurationException, FactoryConfigurationError, SAXException,
            IOException, TransformerFactoryConfigurationError, TransformerException, IllegalArgumentException {
        // D�clare les flux
        Document theDocument = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            // D�clare les objets permettant la lecture du fichier pass� en
            // param�tre si il existe

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            try {
                // Essaie de lire le fichier et instancie le document xml si le
                // fichier existe
                fis = new FileInputStream(path);
                theDocument = builder.parse(fis);
            } catch (FileNotFoundException e) {
                // Si le fichier n'existe pas, alors cr��e un nouveau document
                theDocument = builder.newDocument();
                Element rootElement = theDocument.createElement("all-objects");
                theDocument.appendChild(rootElement);
            }
            // Parse l'objet et l'ajoute au document
            parse(theDocument);

            // Ecrit le document sur le disque, pour ce faire, utilise le
            // transformer xml. Il n�cessite une source d�finie par le document
            // xml
            // (-> instancie un object de type DOMSource) et un objet qui d�fini
            // o�
            // �crire le r�sultat (-> instanciation d'un objet StreamResult sur
            // la
            // base d'un FileOutputStream d�fini par le path pass� en param�tre=
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
     * Permet de g�rer un attribut en fonction de sa m�thode get
     * 
     */
    /**
     * Permet de g�rer un attribut en fonction de sa m�thode get
     * 
     * @param theDocument
     *            Le document xml p�re
     * @param element
     *            L'�l�ment p�re de l'�l�ment � cr�� pour cet attribut
     * @param method
     *            La m�thode d'acc�s utilis�e pour r�cup�rer le nom de l'attribut et sa valeur
     */
    private void parseMethod(Document theDocument, Element element, Method method) {
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
                currentElement.setAttribute("value", method.invoke(toParse, new Object[] {}).toString());
                // Ajoute � l'�l�ment p�re l'�l�ment nouvellement cr��
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
        // Cr�e l'�l�ment objet
        Element currentObjectElement = theDocument.createElement("object");
        // Ajoute le nouvel �l�ment � l'�l�ment racine du document
        theDocument.getDocumentElement().appendChild(currentObjectElement);
        // Lui donne le nom de la classe
        currentObjectElement.setAttribute("classname", toParse.getClass().getName());
        // Lui d�fini quant il a �t� ajout�
        currentObjectElement.setAttribute("date",
                new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));

        // Recherche toutes les m�thodes que l'objet d�clare
        // Method[] methods = toParse.getClass().getMethods();
        /*
         * Mis en commentaire ci-dessus, car ne veut que les m�thodes de la classe et non celles h�rit�es
         */
        Method[] methods = toParse.getClass().getDeclaredMethods();
        // Pour chaque m�thode
        for (int i = 0; i < methods.length; i++) {
            // On la parse
            parseMethod(theDocument, currentObjectElement, methods[i]);
        }
    }
}