package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.ContratEntretienViagerException;
import ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViager;
import ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViagerSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface ContratEntretienViagerService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws ContratEntretienViagerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(ContratEntretienViagerSearch search) throws ContratEntretienViagerException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité ContratEntretienViager
     * 
     * @param ContratEntretienViager
     *            L'entité ContratEntretienViager à créer
     * @return L'entité ContratEntretienViager créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContratEntretienViagerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ContratEntretienViager create(ContratEntretienViager contratEntretienViager)
            throws JadePersistenceException, ContratEntretienViagerException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité ContratEntretienViager
     * 
     * @param ContratEntretienViager
     *            L'entité ContratEntretienViager à supprimer
     * @return L'entité ContratEntretienViager supprimé
     * @throws ContratEntretienViagerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public ContratEntretienViager delete(ContratEntretienViager contratEntretienViager)
            throws ContratEntretienViagerException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité ContratEntretienViager
     * 
     * @param idContratEntretienViager
     *            L'identifiant de l'entité ContratEntretienViager à charger en mémoire
     * @return L'entité ContratEntretienViager chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContratEntretienViagerException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ContratEntretienViager read(String idContratEntretienViager) throws JadePersistenceException,
            ContratEntretienViagerException;

    /**
     * Chargement d'un ContratEntretienViager via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws ContratEntretienViagerException
     * @throws JadePersistenceException
     */
    public ContratEntretienViager readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws ContratEntretienViagerException, JadePersistenceException;

    /**
     * Permet de chercher des ContratEntretienViager selon un modèle de critères.
     * 
     * @param ContratEntretienViagerSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContratEntretienViagerException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ContratEntretienViagerSearch search(ContratEntretienViagerSearch contratEntretienViagerSearch)
            throws JadePersistenceException, ContratEntretienViagerException;

    /**
     * 
     * Permet la mise à jour d'une entité ContratEntretienViager
     * 
     * @param ContratEntretienViager
     *            L'entité ContratEntretienViager à mettre à jour
     * @return L'entité ContratEntretienViager mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContratEntretienViagerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public ContratEntretienViager update(ContratEntretienViager contratEntretienViager)
            throws JadePersistenceException, ContratEntretienViagerException, DonneeFinanciereException;
}