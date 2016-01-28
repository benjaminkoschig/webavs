package globaz.amal.vb.process;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshallerHandler;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.NamespaceSupport;
import org.xml.sax.helpers.XMLFilterImpl;
import ch.globaz.amal.business.exceptions.models.uploadfichierreprise.AMUploadFichierRepriseException;

/**
 * This object implements XMLFilter and monitors the incoming SAX
 * events. Once it hits a Contribuable element, it creates a new
 * unmarshaller and unmarshals one purchase order.
 * 
 * <p>
 * Once finished unmarshalling it, we will process it, then move on to the next contribuable.
 */
public abstract class AMAbstractProcessUpload extends XMLFilterImpl {
    /**
     * We will create unmarshallers from this context.
     */
    // final enlevé
    protected JAXBContext context;

    /**
     * Counter for numbers &lt;Contribuable&gt;[...]&lt;/Contribuable&gt; processing
     */
    protected int cpt = 0;

    /**
     * Remembers the depth of the elements as we forward
     * SAX events to a JAXB unmarshaller.
     */
    protected int depth;

    /**
     * Keeps a reference to the locator object so that we can later
     * pass it to a JAXB unmarshaller.
     */
    protected Locator locator;

    protected String namespace = null;

    /**
     * Used to keep track of in-scope namespace bindings.
     * 
     * For JAXB unmarshaller to correctly unmarshal documents, it needs
     * to know all the effective namespace declarations.
     */
    protected NamespaceSupport namespaces = new NamespaceSupport();

    private String objNameToIterate = "";
    private Object objToIterate = null;

    /**
     * Reference to the unmarshaller which is unmarshalling
     * an object.
     */
    protected UnmarshallerHandler unmarshallerHandler;

    public AMAbstractProcessUpload() {
    }

    public AMAbstractProcessUpload(JAXBContext context) {
        this.context = context;
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        super.endElement(namespaceURI, localName, qName);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        namespaces.popContext();

        super.endPrefixMapping(prefix);
    }

    public String getObjNameToIterate() {
        return objNameToIterate;
    }

    public Object getObjToIterate() {
        return objToIterate;
    }

    public abstract void process(ch.globaz.amal.xmlns.am_0001._1.Contribuable contribuable) throws JAXBException,
            AMUploadFichierRepriseException, JadeApplicationServiceNotAvailableException, JadePersistenceException;

    @Override
    public void setDocumentLocator(Locator locator) {
        super.setDocumentLocator(locator);
        this.locator = locator;
    }

    public void setObjNameToIterate(String objNameToIterate) {
        this.objNameToIterate = objNameToIterate;
    }

    public void setObjToIterate(Object objToIterate) {
        this.objToIterate = objToIterate;
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        super.startElement(namespaceURI, localName, qName, atts);
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        namespaces.pushContext();
        namespaces.declarePrefix(prefix, uri);

        super.startPrefixMapping(prefix, uri);
    }
}
