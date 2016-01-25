package ch.globaz.pegasus.business.services.models.home;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.models.home.PrixChambre;
import ch.globaz.pegasus.business.models.home.PrixChambreSearch;

public interface PrixChambreService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(PrixChambreSearch search) throws PrixChambreException, JadePersistenceException;

    /**
     * Permet la création d'une entité prixChambre
     * 
     * @param prixChambre
     *            Le prixChambre à créer
     * @return Le prixChambre créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PrixChambre create(PrixChambre prixChambre) throws JadePersistenceException, PrixChambreException;

    /**
     * Permet la suppression d'une entité prixChambre
     * 
     * @param prixChambre
     *            Le prixChambre à supprimer
     * @return Le prixChambre supprimé
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public PrixChambre delete(PrixChambre prixChambre) throws PrixChambreException, JadePersistenceException;

    /**
     * Permet de charger en mémoire un prixChambre
     * 
     * @param idPrixChambre
     *            L'identifiant du prixChambre à charger en mémoire
     * @return Le prixChambre chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PrixChambre read(String idPrixChambre) throws JadePersistenceException, PrixChambreException;

    /**
     * Permet de chercher des prixChambre selon un modèle de critères.
     * 
     * @param prixChambreSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PrixChambreSearch search(PrixChambreSearch prixChambreSearch) throws JadePersistenceException,
            PrixChambreException;

    /**
     * 
     * Permet la mise à jour d'une entité prixChambre
     * 
     * @param prixChambre
     *            Le prixChambre à mettre à jour
     * @return Le prixChambre mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrixChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PrixChambre update(PrixChambre prixChambre) throws JadePersistenceException, PrixChambreException;

}
