package ch.globaz.pegasus.business.services.models.annonce.communicationocc;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.annonce.CommunicationOCC;
import ch.globaz.pegasus.business.models.annonce.CommunicationOCCSearch;

public interface CommunicationOCCService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws PrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(CommunicationOCCSearch search) throws PrestationException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une communication OCC
     * 
     * @param idCommunication
     *            L'identifiant de la communication à charger en mémoire
     * @return La communication chargée en mémoire
     * @throws PrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public CommunicationOCC read(String idCommunicationOCC) throws JadePersistenceException, PrestationException;

    /**
     * Permet de chercher des communictions selon un modèle de critères.
     * 
     * @param search
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public CommunicationOCCSearch search(CommunicationOCCSearch search) throws JadePersistenceException,
            PrestationException;
}
