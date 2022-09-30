package ch.globaz.common.xml;

import ch.globaz.common.exceptions.CommonTechnicalException;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.common.Jade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class JaxbHandler<T> {
    private URL urlXsdFile;
    private Schema schema;
    private final JAXBContext jaxbContext;
    private final Function<T, JAXBElement<T>> function;

    private JaxbHandler(String xsdFilePath, Class<T> clazz, Function<T, JAXBElement<T>> function) {
        if (!StringUtils.isBlank(xsdFilePath)) {
            this.urlXsdFile = JaxbHandler.class.getResource(xsdFilePath);
            this.schema = buildShema();
        }
        this.jaxbContext = buildJaxbContext(clazz);
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

    public static <T> JaxbHandler<T> build(Class<T> clazz) {
        return new JaxbHandler<>(null, clazz, null);
    }

    public MessagesValidation validate(T element) {
        final List<MessageValidation> meassages = new LinkedList<>();
        Marshaller marshaller = buildMarshaller(meassages);
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

        return MessagesValidation.of(meassages, element.getClass());
    }

    public Path marshal(T element, Path outputFile) {
        try {
            JAXBContext jc = JAXBContext.newInstance(element.getClass());

            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setSchema(schema);
            marshaller.setEventHandler(new ValidationEventHandler() {

                @Override
                public boolean handleEvent(ValidationEvent event) {
                    LOG.warn("JAXB validation error : " + event.getMessage(), this);
                    return false;
                }
            });
            marshaller.marshal(element, outputFile.toFile());
        } catch (JAXBException e) {
            LOG.error("JAXB validation has thrown a JAXBException :", e);
            throw new RuntimeException(e);
        }
        return outputFile;
    }

    public Path marshal(T element) {
        Path xmlOutput = Paths.get(Jade.getInstance().getPersistenceDir() + JadeFilenameUtil.addFilenameSuffixUID("marshalled.xml"));
        return marshal(element, xmlOutput);
    }

    private static <T> JAXBContext buildJaxbContext(Class<T> clazz) {
        try {
            return JAXBContext.newInstance(clazz);
        } catch (JAXBException e) {
            throw new CommonTechnicalException(e);
        }
    }

    private Schema buildShema() {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            // to be compliant, completely disable DOCTYPE declaration:
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            return factory.newSchema(this.urlXsdFile);
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
