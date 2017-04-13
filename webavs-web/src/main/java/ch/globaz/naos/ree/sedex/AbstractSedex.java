package ch.globaz.naos.ree.sedex;

import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.common.Jade;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import ch.globaz.common.exceptions.ValidationException;

public class AbstractSedex {

    /**
     * Version mineur des messages
     */
    protected static final BigInteger MESSAGE_MINOR_VERSION_0 = new BigInteger("0");

    /**
     * Emplacement des fichiers XSD. Utilisé pour la validation JAXB
     */
    private static final String XSD_FOLDER = "/xsd/ree/";

    /**
     * Postfix des fichié généré. Identique au postfix Jade mais étant privé...
     */
    private final static String MARSHALLED_XML = "marshalled.xml";

    private final static Logger logger = LoggerFactory.getLogger(AbstractSedex.class);

    protected int calculerNombreLots(List<?> jaxbMessages, int tailleLot) {
        if (jaxbMessages == null || jaxbMessages.size() == 0) {
            return 0;
        }

        // Calcul du nombre de lot
        int nombreLot = jaxbMessages.size() / tailleLot;
        // Considération du reste du modulo
        if ((nombreLot * tailleLot) < jaxbMessages.size()) {
            nombreLot++;
        }
        return nombreLot;
    }

    protected File marshallCompleteMessage(Object element, String xsdFileName) throws SAXException, JAXBException,
            PropertyException, IOException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        URL url = getClass().getResource(XSD_FOLDER + xsdFileName);

        Schema schema = sf.newSchema(url);
        JAXBContext jc = JAXBContext.newInstance(element.getClass());

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setSchema(schema);

        String filename = Jade.getInstance().getPersistenceDir()
                + JadeFilenameUtil.addFilenameSuffixUID(MARSHALLED_XML);

        File f = new File(filename);
        f.createNewFile();

        try {
            marshaller.setEventHandler(new ValidationEventHandler() {

                @Override
                public boolean handleEvent(ValidationEvent event) {
                    logger.warn("JAXB validation error : " + event.getMessage(), this);
                    return false;
                }
            });
            marshaller.marshal(element, f);

        } catch (JAXBException exception) {
            logger.error("JAXB validation has thrown a JAXBException : " + exception.toString(), exception);
            throw exception;
        }
        return f;
    }

    public void validateUnitMessage(Object element, String xsdFileName) throws ValidationException, SAXException,
            JAXBException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL url = getClass().getResource(XSD_FOLDER + xsdFileName);

        Schema schema = sf.newSchema(url);
        JAXBContext jc = JAXBContext.newInstance(element.getClass());

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setSchema(schema);

        final List<String> validationErrors = new LinkedList<String>();

        try {
            marshaller.setEventHandler(new ValidationEventHandler() {

                @Override
                public boolean handleEvent(ValidationEvent event) {
                    logger.warn("JAXB validation error : " + event.getMessage(), this);
                    validationErrors.add(event.getMessage());
                    return true;
                }

            });

            marshaller.marshal(element, new ByteArrayOutputStream());

        } catch (JAXBException exception) {
            logger.error("JAXB validation has thrown a JAXBException : " + exception.toString(), exception);
            throw exception;
        }

        if (validationErrors.size() > 0) {
            throw new ValidationException(validationErrors);
        }

    }

}
