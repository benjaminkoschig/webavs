package ch.globaz.pegasus.business.services.models.habitat;

import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.SejourMoisPartielHomeException;
import ch.globaz.pegasus.business.models.habitat.SejourMoisPartielHome;
import ch.globaz.pegasus.business.models.habitat.SejourMoisPartielHomeSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

public interface SejourMoisPartielHomeService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws LoyerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    int count(SejourMoisPartielHomeSearch search) throws SejourMoisPartielHomeException, JadePersistenceException;

    /**
     * Permet la création d'une entité simpleIndemniteJournaliere.
     * 
     * @param sejourMoisPartielHome
     *            La renteAvsAi à créer
     * @return le sejourMoisPartielHome créé
     * @throws SejourMoisPartielHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    SejourMoisPartielHome create(SejourMoisPartielHome sejourMoisPartielHome) throws SejourMoisPartielHomeException,
            DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité simpleIndemniteJournaliere
     * 
     * @param sejourMoisPartielHome
     *            le sejourMoisPartielHome à supprimer
     * @return le sejourMoisPartielHome supprimé
     * @throws SejourMoisPartielHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DonneeFinanciereException
     */
    SejourMoisPartielHome delete(SejourMoisPartielHome sejourMoisPartielHome) throws SejourMoisPartielHomeException,
            JadePersistenceException, DonneeFinanciereException;
    
    /**
     * Permet de charger en mémoire un versionDroit
     * 
     * @param idSejourMoisPartielHome
     *            L'identifiant de le sejourMoisPartielHomee à charger en mémoire
     * @return le sejourMoisPartielHome chargé en mémoire
     * @throws SejourMoisPartielHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    SejourMoisPartielHome read(String idSejourMoisPartielHome) throws SejourMoisPartielHomeException,
            JadePersistenceException;

    /**
     * Chargement d'une SejourMoisPartielHome via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws SejourMoisPartielHomeException
     * @throws JadePersistenceException
     */
    SejourMoisPartielHome readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws SejourMoisPartielHomeException, JadePersistenceException;

    /**
     * Permet la recherche d'après les paramètres de recherche
     * 
     * @param sejourMoisPartielHomeSearch
     * @return La recherche effectué
     * @throws SejourMoisPartielHomeException
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     */

    SejourMoisPartielHomeSearch search(SejourMoisPartielHomeSearch sejourMoisPartielHomeSearch)
            throws SejourMoisPartielHomeException, DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité simpleIndemniteJournaliere
     * 
     * @param sejourMoisPartielHome
     *            le sejourMoisPartielHome à mettre à jour
     * @return le sejourMoisPartielHome mis à jour
     * @throws SejourMoisPartielHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    SejourMoisPartielHome update(SejourMoisPartielHome sejourMoisPartielHome) throws SejourMoisPartielHomeException,
            DonneeFinanciereException, JadePersistenceException;

}
