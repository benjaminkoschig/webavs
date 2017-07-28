package globaz.osiris.db.ordres.sepa;

import globaz.osiris.db.ordres.sepa.AbstractSepa.SepaException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CAJaxbUtil {

    public static Document parseDocument(InputStream source) {
        Document doc;

        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            final DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            doc = documentBuilder.parse(source);
        } catch (ParserConfigurationException e) {
            throw new SepaException("XML Parser error: " + e, e);
        } catch (SAXException e) {
            throw new SepaException("XML Error: " + e, e);
        } catch (IOException e) {
            throw new SepaException("IO error trying to parse source stream: " + e, e);
        }

        return doc;
    }

    /**
     * how to convert xml document/node/elements into java objects
     * 
     * @param input a object jaxb
     * @return a document xml
     */
    protected Document marshall(Object input) {
        try {
            final JAXBContext jc = JAXBContext.newInstance(input.getClass());

            final Marshaller marshaller = jc.createMarshaller();
            marshaller.setEventHandler(new DefaultValidationEventHandler());

            final DOMResult result = new DOMResult();
            marshaller.marshal(input, result);

            return result.getNode().getOwnerDocument();
        } catch (JAXBException e) {
            throw new SepaException("unable to convert java object of class " + input.getClass().getName()
                    + " to an xml document: " + e, e);
        }
    }

    /**
     * how to convert xml document/node/elements into java objects
     * 
     * @param doc a document
     * @param clazz class to convert
     * @return the classe converted
     */
    public static <T> T unmarshall(Document doc, Class<? extends T> clazz) {
        try {
            final JAXBContext jc = JAXBContext.newInstance(clazz);

            final Unmarshaller unmarshaller = jc.createUnmarshaller();
            unmarshaller.setEventHandler(new DefaultValidationEventHandler());

            return unmarshaller.unmarshal(doc, clazz).getValue();
        } catch (JAXBException e) {
            throw new SepaException("unable to convert xml document to java object of class " + clazz.getName() + ": "
                    + e, e);
        }
    }

}
