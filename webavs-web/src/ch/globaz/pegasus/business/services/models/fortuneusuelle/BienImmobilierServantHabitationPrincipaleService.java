package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleException;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierServantHabitationPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface BienImmobilierServantHabitationPrincipaleService extends JadeApplicationService,
        AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(BienImmobilierServantHabitationPrincipaleSearch search)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException;

    /**
     * Permet la création d'une entité BienImmobilierServantHabitationPrincipale
     * 
     * @param BienImmobilierServantHabitationPrincipale
     *            L'entité BienImmobilierServantHabitationPrincipale à créer
     * @return L'entité BienImmobilierServantHabitationPrincipale créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public BienImmobilierServantHabitationPrincipale create(
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité BienImmobilierServantHabitationPrincipale
     * 
     * @param BienImmobilierServantHabitationPrincipale
     *            L'entité BienImmobilierServantHabitationPrincipale à supprimer
     * @return L'entité BienImmobilierServantHabitationPrincipale supprimé
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public BienImmobilierServantHabitationPrincipale delete(
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité BienImmobilierServantHabitationPrincipale
     * 
     * @param idBienImmobilierServantHabitationPrincipale
     *            L'identifiant de l'entité BienImmobilierServantHabitationPrincipale à charger en mémoire
     * @return L'entité BienImmobilierServantHabitationPrincipale chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public BienImmobilierServantHabitationPrincipale read(String idBienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException;

    /**
     * Chargement d'une BienImmobilierServantHabitationPrincipale via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws BienImmobilierServantHabitationPrincipaleException
     * @throws JadePersistenceException
     */
    public BienImmobilierServantHabitationPrincipale readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException;

    /**
     * Permet de chercher des BienImmobilierServantHabitationPrincipale selon un modèle de critères.
     * 
     * @param BienImmobilierServantHabitationPrincipaleSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public BienImmobilierServantHabitationPrincipaleSearch search(
            BienImmobilierServantHabitationPrincipaleSearch bienImmobilierServantHabitationPrincipaleSearch)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException;

    /**
     * 
     * Permet la mise à jour d'une entité BienImmobilierServantHabitationPrincipale
     * 
     * @param BienImmobilierServantHabitationPrincipale
     *            L'entité BienImmobilierServantHabitationPrincipale à mettre à jour
     * @return L'entité BienImmobilierServantHabitationPrincipale mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public BienImmobilierServantHabitationPrincipale update(
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException,
            DonneeFinanciereException;
}