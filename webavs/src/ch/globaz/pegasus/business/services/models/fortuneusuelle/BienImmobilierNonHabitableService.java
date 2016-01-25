package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierNonHabitableException;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierNonHabitable;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierNonHabitableSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface BienImmobilierNonHabitableService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws BienImmobilierNonHabitableException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(BienImmobilierNonHabitableSearch search) throws BienImmobilierNonHabitableException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité BienImmobilierNonHabitable
     * 
     * @param BienImmobilierNonHabitable
     *            L'entité BienImmobilierNonHabitable à créer
     * @return L'entité BienImmobilierNonHabitable créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public BienImmobilierNonHabitable create(BienImmobilierNonHabitable bienImmobilierNonHabitable)
            throws JadePersistenceException, BienImmobilierNonHabitableException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité BienImmobilierNonHabitable
     * 
     * @param BienImmobilierNonHabitable
     *            L'entité BienImmobilierNonHabitable à supprimer
     * @return L'entité BienImmobilierNonHabitable supprimé
     * @throws BienImmobilierNonHabitableException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public BienImmobilierNonHabitable delete(BienImmobilierNonHabitable bienImmobilierNonHabitable)
            throws BienImmobilierNonHabitableException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité BienImmobilierNonHabitable
     * 
     * @param idBienImmobilierNonHabitable
     *            L'identifiant de l'entité BienImmobilierNonHabitable à charger en mémoire
     * @return L'entité BienImmobilierNonHabitable chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public BienImmobilierNonHabitable read(String idBienImmobilierNonHabitable) throws JadePersistenceException,
            BienImmobilierNonHabitableException;

    /**
     * Chargement d'un BienImmobilierNonHabitable via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws BienImmobilierNonHabitableException
     * @throws JadePersistenceException
     */
    public BienImmobilierNonHabitable readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws BienImmobilierNonHabitableException, JadePersistenceException;

    /**
     * Permet de chercher des BienImmobilierNonHabitable selon un modèle de critères.
     * 
     * @param BienImmobilierNonHabitableSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public BienImmobilierNonHabitableSearch search(BienImmobilierNonHabitableSearch bienImmobilierNonHabitableSearch)
            throws JadePersistenceException, BienImmobilierNonHabitableException;

    /**
     * 
     * Permet la mise à jour d'une entité BienImmobilierNonHabitable
     * 
     * @param BienImmobilierNonHabitable
     *            L'entité BienImmobilierNonHabitable à mettre à jour
     * @return L'entité BienImmobilierNonHabitable mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public BienImmobilierNonHabitable update(BienImmobilierNonHabitable bienImmobilierNonHabitable)
            throws JadePersistenceException, BienImmobilierNonHabitableException, DonneeFinanciereException;
}