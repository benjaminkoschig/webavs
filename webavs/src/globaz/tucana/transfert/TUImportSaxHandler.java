package globaz.tucana.transfert;

import globaz.globall.db.BEntity;
import globaz.jsp.util.GlobazJSPBeanUtil;
import globaz.tucana.exception.process.TUInitImportException;
import globaz.tucana.transfert.config.ITUExportXmlTags;
import globaz.tucana.transfert.config.ITUImportXmlTags;
import java.lang.reflect.InvocationTargetException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.LocatorImpl;

/**
 * Permet la lecture d'un document xml en SAX
 * 
 * @author fgo
 * 
 */
public class TUImportSaxHandler implements ContentHandler {
    private TUImportEntityCollector entityCollector = null;

    private TUImportHandler handler = null;

    // private TUImportEntity entity = null;

    private Locator locator = null;

    private boolean processAttributes = false;

    /**
     * Constructeur
     * 
     * @param _handler
     */
    public TUImportSaxHandler(TUImportHandler _handler) {
        super();
        handler = _handler;
        locator = new LocatorImpl();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    /**
     * Cr�ation d'une BEntity
     * 
     * @param localName
     * @return
     * @throws SAXException
     */
    private BEntity createEntity(String localName) throws SAXException {
        try {
            // charge la classe depuis le classLoader system et en fait une
            // nouvelle instance
            // TODO dire � Lerry que �a ne fonctionne pas ??return (BEntity)
            // ClassLoader.getSystemClassLoader().loadClass(localName).newInstance();
            return (BEntity) Class.forName(localName).newInstance();
        } catch (ClassCastException e) {
            throw new SAXException(new TUInitImportException("Invalid class type : " + localName));
        } catch (ClassNotFoundException e) {
            throw new SAXException(new TUInitImportException("Invalid class name : " + localName));
        } catch (InstantiationException e) {
            throw new SAXException(new TUInitImportException("Invalid instanciation of class name : " + localName));
        } catch (IllegalAccessException e) {
            throw new SAXException(new TUInitImportException("IllegalAccess of class name : " + localName));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    @Override
    public void endDocument() throws SAXException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // en fin d'�l�ment "attributes" on remet le flag � false
        if (ITUExportXmlTags.NODE_ATTRIBUTES.equals(localName)) {
            processAttributes = false;
        } else if (localName.startsWith(ITUExportXmlTags.DOMAIN_NAME)) {
            // en fin d'�l�ment contenant un nom de domaine "globaz" (fin de
            // d�finition de classe)
            // on remonte sur le parent si il existe
            if (entityCollector.hasParentCollector()) {
                entityCollector = entityCollector.getParentCollector();
            }
        } else if (localName.equals(ITUExportXmlTags.NODE_ROOT)) {
            // en fin d'�l�ment "root" on registre l'entityCollector au niveau
            // du handler
            handler.registerEntityCollector(entityCollector);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
     */
    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    /**
     * @return
     */
    public Locator getLocator() {
        return locator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
     */
    @Override
    public void processingInstruction(String target, String data) throws SAXException {
    }

    /**
     * Effectue le traitement pour les attributs de la classe
     * 
     * @param methodName
     * @param atts
     * @throws SAXException
     */
    private void processStartAttributes(String localName, Attributes atts) throws SAXException {
        String value = null;
        // parcourt les attributs de l'�l�ment (pour l'instant qu'un seul, mais
        // bon...)
        for (int i = 0; i < atts.getLength() && value == null; i++) {
            // r�cup�ration de l'attribut "value" de l'�l�ment
            if (ITUImportXmlTags.VALUE_ATTRIBUTE.equals(atts.getLocalName(i))) {
                value = atts.getValue(i);
            }
        }
        // mise � jour de la valeur sur la propri�t� "localName" de la classe
        if (value != null) {
            try {
                GlobazJSPBeanUtil.setProperty(localName, value, entityCollector.getEntity().getEntity());
            } catch (SecurityException e) {
                throw new SAXException(new TUInitImportException("Invalid methode security : " + localName));
            } catch (IllegalArgumentException e) {
                throw new SAXException(new TUInitImportException("Invalid methode argument : " + localName));
            } catch (NoSuchMethodException e) {
                throw new SAXException(new TUInitImportException("Invalid methode : " + localName));
            } catch (IllegalAccessException e) {
                throw new SAXException(new TUInitImportException("Invalid methode access : " + localName));
            } catch (InvocationTargetException e) {
                throw new SAXException(new TUInitImportException("Invalid methode invocation : " + localName));
            }
        }
    }

    /**
     * Traitement effectu� sur les �l�ments commen�ant par le nom de package "globaz"
     * 
     * @param localName
     * @param atts
     * @throws SAXException
     */
    private void processStartClass(String localName, Attributes atts) throws SAXException {
        // cr�ation de l'entityCollector, avec pour parent l'�ventuel
        // entityCollector existant d�j�
        // et comme entit� l'entit� localName
        entityCollector = new TUImportEntityCollector(entityCollector, new TUImportEntity(createEntity(localName)));

        // si l'entityCollector poss�de un parent, r�cup�ration de ce parent en
        // lui ajoutant comme listeEntity
        // l'entityCollector "localName"
        if (entityCollector.hasParentCollector()) {
            entityCollector.getParentCollector().addEntity(entityCollector);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
     */
    @Override
    public void setDocumentLocator(Locator _locator) {
        locator = _locator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
     */
    @Override
    public void skippedEntity(String name) throws SAXException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    @Override
    public void startDocument() throws SAXException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String,
     * org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        // � l'arriv� sur l'�l�ment "attributes"
        if (ITUExportXmlTags.NODE_ATTRIBUTES.equals(localName)) {
            processAttributes = true;
        } else if (processAttributes) {
            // pour tous les �l�ments apr�s attributes
            processStartAttributes(localName, atts);
        } else if (localName.startsWith(ITUExportXmlTags.DOMAIN_NAME)) {
            // si le localName contient le domaine (globaz)
            processStartClass(localName, atts);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
     */
    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }
}
