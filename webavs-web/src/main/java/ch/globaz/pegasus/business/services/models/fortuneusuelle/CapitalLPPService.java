package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CapitalLPPException;
import ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPP;
import ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPPSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface CapitalLPPService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws CapitalLPPException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(CapitalLPPSearch search) throws CapitalLPPException, JadePersistenceException;

    /**
     * Permet la création d'une entité CapitalLPP
     * 
     * @param CapitalLPP
     *            L'entité CapitalLPP à créer
     * @return L'entité CapitalLPP créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CapitalLPPException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public CapitalLPP create(CapitalLPP capitalLPP) throws JadePersistenceException, CapitalLPPException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité CapitalLPP
     * 
     * @param CapitalLPP
     *            L'entité CapitalLPP à supprimer
     * @return L'entité CapitalLPP supprimé
     * @throws CapitalLPPException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public CapitalLPP delete(CapitalLPP capitalLPP) throws CapitalLPPException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité CapitalLPP
     * 
     * @param idCapitalLPP
     *            L'identifiant de l'entité CapitalLPP à charger en mémoire
     * @return L'entité CapitalLPP chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CapitalLPPException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public CapitalLPP read(String idCapitalLPP) throws JadePersistenceException, CapitalLPPException;

    /**
     * Chargement d'une CapitalLPP via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws CapitalLPPException
     * @throws JadePersistenceException
     */
    public CapitalLPP readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws CapitalLPPException,
            JadePersistenceException;

    /**
     * Permet de chercher des CapitalLPP selon un modèle de critères.
     * 
     * @param CapitalLPPSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CapitalLPPException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public CapitalLPPSearch search(CapitalLPPSearch capitalLPPSearch) throws JadePersistenceException,
            CapitalLPPException;

    /**
     * 
     * Permet la mise à jour d'une entité CapitalLPP
     * 
     * @param CapitalLPP
     *            L'entité CapitalLPP à mettre à jour
     * @return L'entité CapitalLPP mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CapitalLPPException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public CapitalLPP update(CapitalLPP capitalLPP) throws JadePersistenceException, CapitalLPPException,
            DonneeFinanciereException;
}