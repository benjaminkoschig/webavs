package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroitSearch;

public interface SimpleVersionDroitService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleVersionDroitSearch search) throws DroitException, JadePersistenceException;

    /**
     * Permet la création d'une entité versionDroit. Le versionDroit doit avoir l'id d'une demande de prestation
     * associée qui existe et qui n'est pas déjà associée à un autre versionDroit, sinon une exception est levée.
     * 
     * @param versionDroit
     *            Le versionDroit à créer
     * @return Le versionDroit créé
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleVersionDroit create(SimpleVersionDroit versionDroit) throws DroitException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité versionDroit
     * 
     * @param versionDroit
     *            Le versionDroit à supprimer
     * @return Le versionDroit supprimé
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleVersionDroit delete(SimpleVersionDroit versionDroit) throws DroitException, JadePersistenceException;

    /**
     * Permet de charger en mémoire un versionDroit
     * 
     * @param idVersionDroit
     *            L'identifiant du versionDroit à charger en mémoire
     * @return Le versionDroit chargé en mémoire
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleVersionDroit read(String idVersionDroit) throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher des simpleVersionDroit selon un modèle de critères.
     * 
     * @param SimpleVersionDroitSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleVersionDroitSearch search(SimpleVersionDroitSearch search) throws JadePersistenceException,
            DroitException;

    /**
     * Permet la mise à jour d'une entité versionDroit
     * 
     * @param versionDroit
     *            Le versionDroit à mettre à jour
     * @return Le versionDroit mis à jour
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleVersionDroit update(SimpleVersionDroit versionDroit) throws DroitException, JadePersistenceException;

}
