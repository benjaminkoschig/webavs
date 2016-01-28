package ch.globaz.al.business.services.rafam.sedex;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.service.provider.application.JadeApplicationService;
import javax.xml.datatype.DatatypeConfigurationException;

/**
 * Service d'envoi des annonces RAFam
 * 
 * @author jts
 * 
 */
public interface ExportAnnoncesRafamService extends JadeApplicationService {

    /**
     * Envoie un message d'annonces RAFam.
     * 
     * @throws JadeSedexDirectoryInitializationException
     *             Exception lev�e si d�p�t Sedex ne peut �tre trouv�
     * @throws DatatypeConfigurationException
     *             Exception lev�e si l'en-t�te ne peut �tre initialis�e
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     */
    public void sendMessage() throws DatatypeConfigurationException, JadeApplicationException,
            JadePersistenceException, JadeSedexDirectoryInitializationException;

    /**
     * Envoie un message d'annonces RAFAM pour les employeur d�l�gu�s
     * 
     * @throws DatatypeConfigurationException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeSedexDirectoryInitializationException
     */
    public void sendMessageDelegue() throws DatatypeConfigurationException, JadeApplicationException,
            JadePersistenceException, JadeSedexDirectoryInitializationException;
}
