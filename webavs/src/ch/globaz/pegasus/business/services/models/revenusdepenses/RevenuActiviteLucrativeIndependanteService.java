package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeIndependanteException;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependante;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependanteSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface RevenuActiviteLucrativeIndependanteService extends JadeApplicationService,
        AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(RevenuActiviteLucrativeIndependanteSearch search)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException;

    /**
     * Permet la création d'une entité RevenuActiviteLucrativeIndependante
     * 
     * @param RevenuActiviteLucrativeIndependante
     *            L'entité RevenuActiviteLucrativeIndependante à créer
     * @return L'entité RevenuActiviteLucrativeIndependante créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuActiviteLucrativeIndependante create(
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante) throws JadePersistenceException,
            RevenuActiviteLucrativeIndependanteException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité RevenuActiviteLucrativeIndependante
     * 
     * @param RevenuActiviteLucrativeIndependante
     *            L'entité RevenuActiviteLucrativeIndependante à supprimer
     * @return L'entité RevenuActiviteLucrativeIndependante supprimé
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public RevenuActiviteLucrativeIndependante delete(
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité RevenuActiviteLucrativeIndependante
     * 
     * @param idRevenuActiviteLucrativeIndependante
     *            L'identifiant de l'entité RevenuActiviteLucrativeIndependante à charger en mémoire
     * @return L'entité RevenuActiviteLucrativeIndependante chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuActiviteLucrativeIndependante read(String idRevenuActiviteLucrativeIndependante)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException;

    /**
     * Chargement d'une RevenuActiviteLucrativeIndependante via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws RevenuActiviteLucrativeIndependanteException
     * @throws JadePersistenceException
     */
    public RevenuActiviteLucrativeIndependante readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException;

    /**
     * Permet de chercher des RevenuActiviteLucrativeIndependante selon un modèle de critères.
     * 
     * @param RevenuActiviteLucrativeIndependanteSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuActiviteLucrativeIndependanteSearch search(
            RevenuActiviteLucrativeIndependanteSearch revenuActiviteLucrativeIndependanteSearch)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException;

    /**
     * 
     * Permet la mise à jour d'une entité RevenuActiviteLucrativeIndependante
     * 
     * @param RevenuActiviteLucrativeIndependante
     *            L'entité RevenuActiviteLucrativeIndependante à mettre à jour
     * @return L'entité RevenuActiviteLucrativeIndependante mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public RevenuActiviteLucrativeIndependante update(
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante) throws JadePersistenceException,
            RevenuActiviteLucrativeIndependanteException, DonneeFinanciereException;
}