package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamilleSearch;

public interface SimpleDroitMembreFamilleService extends JadeApplicationService {

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
    public int count(SimpleDroitMembreFamilleSearch search) throws DroitException, JadePersistenceException;

    /**
     * Permet la création d'une entité droitMembreFamille.
     * 
     * @param droitMembreFamille
     *            Le droitMembreFamille à créer
     * @return Le droitMembreFamille créé
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDroitMembreFamille create(SimpleDroitMembreFamille droitMembreFamille) throws DroitException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entité droitMembreFamille
     * 
     * @param droitMembreFamille
     *            Le droitMembreFamille à supprimer
     * @return Le droitMembreFamille supprimé
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDroitMembreFamille delete(SimpleDroitMembreFamille droitMembreFamille) throws DroitException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire un droitMembreFamille
     * 
     * @param idDroitMembreFamille
     *            L'identifiant du droitMembreFamille à charger en mémoire
     * @return Le droitMembreFamille chargé en mémoire
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDroitMembreFamille read(String idDroitMembreFamille) throws DroitException, JadePersistenceException;

    /**
     * Permet de chercher des droitMembreFamille selon un modèle de critères.
     * 
     * @param droitMembreFamilleSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleDroitMembreFamilleSearch search(SimpleDroitMembreFamilleSearch droitMembreFamilleSearch)
            throws JadePersistenceException, DroitException;

    /**
     * Permet la mise à jour d'une entité droitMembreFamille
     * 
     * @param droitMembreFamille
     *            Le droitMembreFamille à mettre à jour
     * @return Le droitMembreFamille mis à jour
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDroitMembreFamille update(SimpleDroitMembreFamille droitMembreFamille) throws DroitException,
            JadePersistenceException;

}
