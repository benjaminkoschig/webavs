package ch.globaz.common.xml;

import ch.globaz.common.exceptions.CommonTechnicalException;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class JaxbHandler<T> {

    private final Schema schema;
    private final JAXBContext jaxbContext;
    private final URL urlXsdFile;
    private final Function<T, JAXBElement<T>> function;

    private JaxbHandler(String xsdFilePath, Class<T> clazz, Function<T, JAXBElement<T>> function) {
        this.urlXsdFile = JaxbHandler.class.getResource(xsdFilePath);
        this.jaxbContext = buildJaxbContext(clazz);
        this.schema = buildShema(urlXsdFile);
        this.function = function;
    }

    /**
     * Constructeur pour faciliter l'utilisation de jaxb.
     *
     * @param xsdFilePath Le chemin ou se trouve le fichier xsd.
     * @param clazz       La class qui représente l'objet qui va être sérialisé ou désérialisé.
     * @param function    Permet de ne pas avoir cette erreur:
     *                    impossible de sérialiser le type "T" en tant qu'élément car il lui manque une annotation @XmlRootElement
     * @param <T>         Le type qui doit être traité pour la sérialisation et désérialisation.
     *
     * @return
     */
    public static <T> JaxbHandler<T> build(String xsdFilePath, Class<T> clazz, Function<T, JAXBElement<T>> function) {
        return new JaxbHandler<>(xsdFilePath, clazz, function);
    }

    public static <T> JaxbHandler<T> build(String xsdFileName, Class<T> clazz) {
        return new JaxbHandler<>(xsdFileName, clazz, null);
    }

    public List<MessageValidation> validate(T element) {
        final List<MessageValidation> validationErrors = new LinkedList<>();
        Marshaller marshaller = buildMarshaller(validationErrors);
        try {
            if (this.function != null) {
                marshaller.marshal(this.function.apply(element), new ByteArrayOutputStream());
            } else {
                marshaller.marshal(element, new ByteArrayOutputStream());
            }
        } catch (JAXBException exception) {
            LOG.error("JAXB validation has thrown a JAXBException", exception);
            throw new CommonTechnicalException(exception);
        }
        return validationErrors;
    }

    private static <T> JAXBContext buildJaxbContext(Class<T> clazz) {
        try {
            return JAXBContext.newInstance(clazz);
        } catch (JAXBException e) {
            throw new CommonTechnicalException(e);
        }
    }

    private static Schema buildShema(URL xsdFile) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            // to be compliant, completely disable DOCTYPE declaration:
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            return factory.newSchema(xsdFile);
        } catch (SAXException e) {
            throw new CommonTechnicalException(e);
        }
    }

    private Marshaller buildMarshaller(final List<MessageValidation> validationErrors) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setSchema(this.schema);

            marshaller.setEventHandler(event -> {
                validationErrors.add(MessageValidation.of(event));
                return true;
            });
            return marshaller;

        } catch (JAXBException e) {
            throw new CommonTechnicalException(e);
        }
    }
}
