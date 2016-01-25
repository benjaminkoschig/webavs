package ch.globaz.pegasus.business.services.models.home;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.models.home.SimplePrixChambre;
import ch.globaz.pegasus.business.models.home.SimplePrixChambreSearch;

public interface SimplePrixChambreService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimplePrixChambreSearch search) throws PrixChambreException, JadePersistenceException;

    /**
     * Permet la création d'une entité prixChambre
     * 
     * @param prixChambre
     *            Le prixChambre à créer
     * @return Le prixChambre créé
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePrixChambre create(SimplePrixChambre prixChambre) throws PrixChambreException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entité prixChambre
     * 
     * @param prixChambre
     *            Le prisChambre à supprimer
     * @return Le prixChambre supprimé
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePrixChambre delete(SimplePrixChambre prixChambre) throws PrixChambreException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire un prixChambre
     * 
     * @param idPrixChambre
     *            L'identifiant du prixChambre à charger en mémoire
     * @return Le prixChambre chargé en mémoire
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePrixChambre read(String idPrixChambre) throws PrixChambreException, JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité prixChambre
     * 
     * @param prixChambre
     *            Le prixChambre à mettre à jour
     * @return Le prixChambre mis à jour
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePrixChambre update(SimplePrixChambre prixChambre) throws PrixChambreException,
            JadePersistenceException;
}
