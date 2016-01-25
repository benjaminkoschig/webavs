package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;
import ch.globaz.pegasus.business.models.droit.SimpleDroitSearch;

public interface SimpleDroitService extends JadeApplicationService {

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
    public int count(SimpleDroitSearch search) throws DroitException, JadePersistenceException;

    /**
     * Permet la création d'une entité droit. Le droit doit avoir l'id d'une demande de prestation associée qui existe
     * et qui n'est pas déjà associée à un autre droit, sinon une exception est levée.
     * 
     * @param droit
     *            Le droit à créer
     * @return Le droit créé
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDroit create(SimpleDroit droit) throws DroitException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité droit
     * 
     * @param droit
     *            Le droit à supprimer
     * @return Le droit supprimé
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDroit delete(SimpleDroit droit) throws DroitException, JadePersistenceException;

    /**
     * Permet de charger en mémoire un droit
     * 
     * @param idDroit
     *            L'identifiant du droit à charger en mémoire
     * @return Le droit chargé en mémoire
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDroit read(String idDroit) throws DroitException, JadePersistenceException;

    /**
     * Permet d'effectuer une recher
     * 
     * @param search
     * @return
     * @throws DroitException
     * @throws JadePersistenceException
     */
    public SimpleDroitSearch search(SimpleDroitSearch search) throws DroitException, JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité droit
     * 
     * @param droit
     *            Le droit à mettre à jour
     * @return Le droit mis à jour
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDroit update(SimpleDroit droit) throws DroitException, JadePersistenceException;

}
