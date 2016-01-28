package ch.globaz.eavs.parser;

import globaz.jade.log.JadeLogger;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSFinalNode;
import ch.globaz.eavs.utils.StringUtils;

public class EAVSSaxHandler extends DefaultHandler {
    private final static String DEFAULT_NS = "#DEFAULT_NS#";
    private final static String MODEL_ROOT_PACKAGE = "ch.globaz.eavs.model";
    private EAVSAbstractModel containerElement = null;
    private String currentChars = new String();
    private SaxElement elements = new SaxElement();
    private boolean firstElement;
    private Map metadata = null;
    private String minorVersion = null;
    private EAVSAbstractModel rootElement = null;
    private HashMap targetNamespacePackage = null;

    public EAVSSaxHandler() {
        super();
        targetNamespacePackage = new HashMap();
        firstElement = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        currentChars += new String(ch, start, length);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#endDocument()
     */
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        EAVSAbstractModel element = elements.pop();
        if (EAVSFinalNode.class.isAssignableFrom(element.getClass())) {
            EAVSFinalNode daNode = (EAVSFinalNode) element;
            daNode.setValue(currentChars);
        }
        element.fillMetaData(metadata);
        super.endElement(uri, localName, qName);
        JadeLogger.trace(this, "----> End element");
    }

    public EAVSAbstractModel getContainerElement() {
        return containerElement;
    }

    public String getMinorVersion() {
        return minorVersion;
    }

    public EAVSAbstractModel getRootElement() {
        return rootElement;
    }

    public void setContainerElement(EAVSAbstractModel containerElement) {
        this.containerElement = containerElement;
    }

    void setMetaData(Map metadataToFill) {
        metadata = metadataToFill;
    }

    public void setMinorVersion(String minorVersion) {
        this.minorVersion = minorVersion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#startDocument()
     */
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        JadeLogger.trace(this, "----> Starting document");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String,
     * org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        JadeLogger.trace(this, "------------> Starting element ** uri=[" + uri + "].localName=[" + localName
                + "] - qname=[" + qName + "]");
        // crée le dictionnaire qname[0] <-> nom de package
        if (firstElement) {
            for (int i = 0; i < attributes.getLength(); i++) {
                if (attributes.getQName(i).startsWith("xmlns")) {
                    JadeLogger.trace(this, "--> Defining namespace. Attribute: " + attributes.getQName(i) + ". Value: "
                            + attributes.getValue(i));
                    String namespace = null;
                    boolean isDefaultNS = "xmlns".equals(attributes.getQName(i));
                    if (isDefaultNS) {
                        // defining the default namespace
                        JadeLogger.trace(this, "--> Defining DEFAULT namespace. ");
                        namespace = EAVSSaxHandler.DEFAULT_NS;
                    } else {
                        namespace = attributes.getQName(i).substring("xmlns:".length());
                    }
                    String version = attributes.getValue(i).substring(attributes.getValue(i).lastIndexOf("/") + 1);
                    if (StringUtils.isInteger(version)) {
                        version = "v" + version;
                    } else {
                        version = "common";
                    }
                    String attrValue = attributes.getValue(i);
                    String packageName = attrValue.substring(0, attrValue.lastIndexOf('/'));
                    packageName = packageName.substring(packageName.lastIndexOf('/') + 1);
                    packageName = EAVSSaxHandler.MODEL_ROOT_PACKAGE + "." + StringUtils.removeChar(packageName, '-')
                            + "." + version;
                    JadeLogger.trace(this, namespace + " - " + packageName);
                    targetNamespacePackage.put(namespace, packageName);
                } else if ("minorVersion".equals(attributes.getQName(i))) {
                    setMinorVersion(attributes.getValue(i));
                }
            }
            // this.firstElement = false;
        }
        // creer l'élément par reflexivité
        if (qName.endsWith(":")) {
            throw new SAXException("Impossible de déterminer la classe à instancier à partir du QName " + qName);
        }
        String nameSpace;
        String className;
        if (qName.indexOf(':') < 1) {
            // NS par défaut
            nameSpace = EAVSSaxHandler.DEFAULT_NS;
            className = qName;
        } else {
            // TODO risque de péter IOOBE. Gérer.
            nameSpace = qName.substring(0, qName.indexOf(':'));
            className = qName.substring(qName.indexOf(':') + 1);
        }
        String packageName = (String) targetNamespacePackage.get(nameSpace);
        if (packageName == null) {
            throw new SAXException("Impossible de déterminer le nom de package à partir du QName " + qName);
        }

        String fullClassName = packageName + "." + StringUtils.firstLetterToUpperCase(className);
        EAVSAbstractModel newObj = null;
        try {
            newObj = (EAVSAbstractModel) Class.forName(fullClassName).newInstance();
        } catch (InstantiationException e) {
            throw new SAXException("InstantiationException: impossible de créer une nouvelle instance de la classe "
                    + fullClassName, e);
        } catch (IllegalAccessException e) {
            throw new SAXException("IllegalAccess: impossible de créer une nouvelle instance de la classe "
                    + fullClassName, e);
        } catch (ClassNotFoundException e) {
            throw new SAXException("ClassNotFound: la classe " + fullClassName
                    + " n'existe pas dans le chemin du runtime", e);
        }
        // mettre dans la pile
        elements.push(newObj);
        if (firstElement) {
            firstElement = false;
            containerElement = newObj;
        }
        // le crocher à son parent
        EAVSAbstractModel parent = elements.getParent(newObj);
        if (parent != null) {
            try {
                parent.addChildren(newObj);
            } catch (SecurityException e) {
                throw new SAXException("SecurityException: impossible d'accéder à la méthode pour ajouter l'enfant."
                        + parent.getClass().toString() + " à " + fullClassName, e);
            } catch (NoSuchMethodException e) {
                throw new SAXException("NoSuchMethodException: impossible de trouver la méthode pour ajouter l'enfant."
                        + parent.getClass().toString() + " à " + fullClassName, e);
            } catch (IllegalArgumentException e) {
                throw new SAXException("IllegalArgumentException: Argument illégal pour ajouter l'enfant."
                        + parent.getClass().toString() + " à " + fullClassName, e);
            } catch (IllegalAccessException e) {
                throw new SAXException("IllegalAccessException: Accès à la méthode interdit pour ajouter l'enfant."
                        + parent.getClass().toString() + " à " + fullClassName, e);
            } catch (InvocationTargetException e) {
                throw new SAXException(
                        "InvocationTargetException: Invocation de la méthode interdit pour ajouter l'enfant."
                                + parent.getClass().toString() + " à " + fullClassName, e);
            }
            rootElement = newObj;
        }
        // remettre à zéro le texte à setter à l'élément
        currentChars = new String();
        super.startElement(uri, localName, qName, attributes);
    }
}
