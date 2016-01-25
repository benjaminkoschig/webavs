package ch.globaz.pegasus.business.services.models.parametre;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaits;
import ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaitsSearch;

public interface SimpleZoneForfaitsService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleZoneForfaitsSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité SimpleZoneForfaits
     * 
     * @param SimpleZoneForfaits
     *            La simpleZoneForfaits métier à créer
     * @return simpleZoneForfaits créé
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeNoBusinessLogSessionError
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleZoneForfaits create(SimpleZoneForfaits simpleZoneForfaits)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError;

    /**
     * Permet la suppression d'une entité simpleZoneForfaits
     * 
     * @param SimpleZoneForfaits
     *            La simpleZoneForfaits métier à supprimer
     * @return supprimé
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeNoBusinessLogSessionError
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleZoneForfaits delete(SimpleZoneForfaits simpleZoneForfaits)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError;

    /**
     * Permet de charger en mémoire une simpleZoneForfaits PC
     * 
     * @param idsimpleZoneForfaits
     *            L'identifiant de simpleZoneForfaits à charger en mémoire
     * @return simpleZoneForfaits chargée en mémoire
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleZoneForfaits read(String idSimpleZoneForfaits) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet de chercher des SimpleZoneForfaits selon un modèle de critères.
     * 
     * @param simpleZoneForfaitsSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleZoneForfaitsSearch search(SimpleZoneForfaitsSearch simpleZoneForfaitsSearch)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleZoneForfaits
     * 
     * @param SimpleZoneForfaits
     *            Le modele à mettre à jour
     * @return simpleZoneForfaits mis à jour
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleZoneForfaits update(SimpleZoneForfaits simpleZoneForfaits)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

}