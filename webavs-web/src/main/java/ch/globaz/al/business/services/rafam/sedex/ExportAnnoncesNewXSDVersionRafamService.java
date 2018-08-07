package ch.globaz.al.business.services.rafam.sedex;

import javax.xml.datatype.DatatypeConfigurationException;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service d'envoi des annonces RAFam
 *
 * @author jts
 *
 */
public interface ExportAnnoncesNewXSDVersionRafamService extends JadeApplicationService {

    /**
     * Envoie un message d'annonces RAFam avec la nouvelle version des schémas XSD (4.1).
     *
     * @throws JadeSedexDirectoryInitializationException
     *             Exception levée si dépôt Sedex ne peut être trouvé
     * @throws DatatypeConfigurationException
     *             Exception levée si l'en-tête ne peut être initialisée
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     *
     */
    public void sendMessage() throws DatatypeConfigurationException, JadeApplicationException, JadePersistenceException,
            JadeSedexDirectoryInitializationException;

    /**
     * Envoie un message d'annonces RAFAM pour les employeur délégués RAFam avec la nouvelle version des schémas XSD
     * (4.1).
     *
     * @throws DatatypeConfigurationException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeSedexDirectoryInitializationException
     */
    public void sendMessageDelegue() throws DatatypeConfigurationException, JadeApplicationException,
            JadePersistenceException, JadeSedexDirectoryInitializationException;
}
