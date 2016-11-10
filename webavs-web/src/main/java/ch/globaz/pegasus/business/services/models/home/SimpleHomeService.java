package ch.globaz.pegasus.business.services.models.home;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.models.home.SimpleHome;
import ch.globaz.pegasus.business.models.home.SimpleHomeSearch;

public interface SimpleHomeService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleHomeSearch search) throws HomeException, JadePersistenceException;

    /**
     * Permet la création d'une entité home
     * 
     * @param home
     *            Le home à créer
     * @return Le home créé
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleHome create(SimpleHome home) throws HomeException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité home
     * 
     * @param home
     *            Le home à supprimer
     * @return Le home supprimé
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleHome delete(SimpleHome home) throws HomeException, JadePersistenceException;

    /**
     * Permet de charger en mémoire un home
     * 
     * @param idDossier
     *            L'identifiant du home à charger en mémoire
     * @return Le home chargé en mémoire
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleHome read(String idHome) throws HomeException, JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité home
     * 
     * @param home
     *            Le home à mettre à jour
     * @return Le home mis à jour
     * @throws HomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleHome update(SimpleHome home) throws HomeException, JadePersistenceException;

    /**
     * 
     * @param search le modèle rechercher
     * @return le résultat de la requete
     * @throws HomeException si problème "métier"
     * @throws JadePersistenceException si problème "technique"
     */
    public SimpleHomeSearch search(SimpleHomeSearch search) throws HomeException, JadePersistenceException;
}
