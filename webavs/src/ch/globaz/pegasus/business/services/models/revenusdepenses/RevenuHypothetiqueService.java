package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuHypothetiqueException;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuHypothetique;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuHypothetiqueSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface RevenuHypothetiqueService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws RevenuHypothetiqueException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(RevenuHypothetiqueSearch search) throws RevenuHypothetiqueException, JadePersistenceException;

    /**
     * Permet la création d'une entité RevenuHypothetique
     * 
     * @param RevenuHypothetique
     *            L'entité RevenuHypothetique à créer
     * @return L'entité RevenuHypothetique créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuHypothetiqueException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuHypothetique create(RevenuHypothetique revenuHypothetique) throws JadePersistenceException,
            RevenuHypothetiqueException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité RevenuHypothetique
     * 
     * @param RevenuHypothetique
     *            L'entité RevenuHypothetique à supprimer
     * @return L'entité RevenuHypothetique supprimé
     * @throws RevenuHypothetiqueException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public RevenuHypothetique delete(RevenuHypothetique revenuHypothetique) throws RevenuHypothetiqueException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité RevenuHypothetique
     * 
     * @param idRevenuHypothetique
     *            L'identifiant de l'entité RevenuHypothetique à charger en mémoire
     * @return L'entité RevenuHypothetique chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuHypothetiqueException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuHypothetique read(String idRevenuHypothetique) throws JadePersistenceException,
            RevenuHypothetiqueException;

    /**
     * Chargement d'une RevenuHypothetique via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws RevenuHypothetiqueException
     * @throws JadePersistenceException
     */
    public RevenuHypothetique readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws RevenuHypothetiqueException, JadePersistenceException;

    /**
     * Permet de chercher des RevenuHypothetique selon un modèle de critères.
     * 
     * @param RevenuHypothetiqueSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuHypothetiqueException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuHypothetiqueSearch search(RevenuHypothetiqueSearch revenuHypothetiqueSearch)
            throws JadePersistenceException, RevenuHypothetiqueException;

    /**
     * 
     * Permet la mise à jour d'une entité RevenuHypothetique
     * 
     * @param RevenuHypothetique
     *            L'entité RevenuHypothetique à mettre à jour
     * @return L'entité RevenuHypothetique mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuHypothetiqueException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public RevenuHypothetique update(RevenuHypothetique revenuHypothetique) throws JadePersistenceException,
            RevenuHypothetiqueException, DonneeFinanciereException;
}