package ch.globaz.pegasus.rpc.businessImpl.sedex;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.exceptions.ValidationException;

public class JaxbHandler<T> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSedex.class);

    private final Schema schema;
    private final JAXBContext jaxbContext;
    private final Class<T> clazz;
    private final String xsdFileName;

    /**
     * Emplacement des fichiers XSD. Utilisé pour la validation JAXB
     */
    private static final String XSD_FOLDER = "/xsd/rpc/";

    private JaxbHandler(String xsdFileName, Class<T> clazz) {
        this.clazz = clazz;
        this.xsdFileName = xsdFileName;
        this.schema = buildShema(xsdFileName);
        this.jaxbContext = buildJaxbContext(clazz);
    }

    public static <T> JaxbHandler<T> build(String xsdFileName, Class<T> clazz) {
        return new JaxbHandler<T>(xsdFileName, clazz);
        // return new JaxbHandler<T>(buildShema(xsdFileName), buildJaxbContext(clazz));
    }

    public File marshall(T element, String filename) throws ValidationException {

        final List<String> validationErrors = new LinkedList<String>();

        Marshaller marshaller = buildMarshaller(validationErrors);
        File f = createFile(filename);
        try {
            marshaller.marshal(element, f);
        } catch (JAXBException exception) {
            FileUtils.deleteQuietly(f);
            logger.error("JAXB validation has thrown a JAXBException : " + exception.toString(), exception);
            throw new CommonTechnicalException(exception);
        }
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }
        return f;
    }

    public void validate(T element) throws ValidationException {
        final List<String> validationErrors = new LinkedList<String>();
        Marshaller marshaller = buildMarshaller(validationErrors);
        try {
            marshaller.marshal(element, new ByteArrayOutputStream());
        } catch (JAXBException exception) {
            logger.error("JAXB validation has thrown a JAXBException : " + exception.toString(), exception);
            throw new CommonTechnicalException(exception);
        }
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }
    }

    private static <T> JAXBContext buildJaxbContext(Class<T> clazz) {
        try {
            return JAXBContext.newInstance(clazz);
        } catch (JAXBException e) {
            throw new CommonTechnicalException(e);
        }
    }

    private static Schema buildShema(String xsdFileName) {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL url = JaxbHandler.class.getResource(XSD_FOLDER + xsdFileName);
        try {
            return sf.newSchema(url);
        } catch (SAXException e) {
            throw new CommonTechnicalException(e);
        }
    }

    private File createFile(String filename) {
        File f = new File(filename);
        try {
            Boolean fileExiste = f.createNewFile();
            if (!fileExiste) {
                throw new CommonTechnicalException("The file: " + filename + " already exist !");
            }
        } catch (IOException e) {
            throw new CommonTechnicalException("Unabled to create this file:" + filename, e);
        }
        return f;
    }

    private Marshaller buildMarshaller(final List<String> validationErrors) {
        try {
            Marshaller marshaller = this.jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setSchema(this.schema);

            marshaller.setEventHandler(new ValidationEventHandler() {

                @Override
                public boolean handleEvent(ValidationEvent event) {
                    logger.warn("JAXB validation error : " + event.getMessage(), this);
                    validationErrors.add(event.getMessage());
                    return true;
                }

            });
            return marshaller;

        } catch (JAXBException e) {
            throw new CommonTechnicalException(e);
        }
    }
}
