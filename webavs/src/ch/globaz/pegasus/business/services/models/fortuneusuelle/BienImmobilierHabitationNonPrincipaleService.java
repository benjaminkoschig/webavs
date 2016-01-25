package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleException;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface BienImmobilierHabitationNonPrincipaleService extends JadeApplicationService,
        AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(BienImmobilierHabitationNonPrincipaleSearch search)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException;

    /**
     * Permet la création d'une entité BienImmobilierHabitationNonPrincipale
     * 
     * @param BienImmobilierHabitationNonPrincipale
     *            L'entité BienImmobilierHabitationNonPrincipale à créer
     * @return L'entité BienImmobilierHabitationNonPrincipale créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public BienImmobilierHabitationNonPrincipale create(
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité BienImmobilierHabitationNonPrincipale
     * 
     * @param BienImmobilierHabitationNonPrincipale
     *            L'entité BienImmobilierHabitationNonPrincipale à supprimer
     * @return L'entité BienImmobilierHabitationNonPrincipale supprimé
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public BienImmobilierHabitationNonPrincipale delete(
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité BienImmobilierHabitationNonPrincipale
     * 
     * @param idBienImmobilierHabitationNonPrincipale
     *            L'identifiant de l'entité BienImmobilierHabitationNonPrincipale à charger en mémoire
     * @return L'entité BienImmobilierHabitationNonPrincipale chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public BienImmobilierHabitationNonPrincipale read(String idBienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException;

    /**
     * Chargement d'une BienImmobilierHabitationNonPrincipale via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws BienImmobilierHabitationNonPrincipaleException
     * @throws JadePersistenceException
     */
    public BienImmobilierHabitationNonPrincipale readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException;

    /**
     * Permet de chercher des BienImmobilierHabitationNonPrincipale selon un modèle de critères.
     * 
     * @param BienImmobilierHabitationNonPrincipaleSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public BienImmobilierHabitationNonPrincipaleSearch search(
            BienImmobilierHabitationNonPrincipaleSearch bienImmobilierHabitationNonPrincipaleSearch)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException;

    /**
     * 
     * Permet la mise à jour d'une entité BienImmobilierHabitationNonPrincipale
     * 
     * @param BienImmobilierHabitationNonPrincipale
     *            L'entité BienImmobilierHabitationNonPrincipale à mettre à jour
     * @return L'entité BienImmobilierHabitationNonPrincipale mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public BienImmobilierHabitationNonPrincipale update(
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException, DonneeFinanciereException;
}