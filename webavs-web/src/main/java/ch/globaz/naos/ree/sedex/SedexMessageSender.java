package ch.globaz.naos.ree.sedex;

import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.sedex.message.JadeSedexMessageNotSentException;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xml.sax.SAXException;
import ch.globaz.naos.ree.protocol.ProcessProtocolAndMessages;
import ch.globaz.naos.ree.protocol.SedexTechnicalProtocol;

public interface SedexMessageSender {

    /**
     * Réalise une validation JAXB sur un message unitaire
     * 
     * @param object L'object ä valider
     * @param xsdFileName La xsd à utiliser pour valider le message unitaire
     */
    void validateUnitMessage(Object object) throws ValidationException, SAXException, JAXBException;

    /**
     * Marschall un message complet (Message, header, et liste des messages/éléments)
     * 
     * @param xsdFileName
     * @return Un protocol technique pour le temps d'exécution
     * @throws JadeSedexMessageNotSentException
     * @throws MalformedURLException
     * @throws JAXBValidationError
     * @throws JAXBValidationWarning
     * @throws JAXBException
     * @throws SAXException
     * @throws IOException
     * @throws JadeSedexDirectoryInitializationException
     * @throws DatatypeConfigurationException
     */
    SedexTechnicalProtocol marshallAndValidate(ProcessProtocolAndMessages processProtocolAndMessages)
            throws JadeSedexMessageNotSentException, MalformedURLException, JAXBValidationError, JAXBValidationWarning,
            JAXBException, SAXException, IOException, JadeSedexDirectoryInitializationException,
            DatatypeConfigurationException;

}
