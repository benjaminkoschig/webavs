package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AssuranceVieException;
import ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVie;
import ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVieSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface AssuranceVieService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws AssuranceVieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(AssuranceVieSearch search) throws AssuranceVieException, JadePersistenceException;

    /**
     * Permet la création d'une entité AssuranceVie
     * 
     * @param AssuranceVie
     *            L'entité AssuranceVie à créer
     * @return L'entité AssuranceVie créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceVieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AssuranceVie create(AssuranceVie assuranceVie) throws JadePersistenceException, AssuranceVieException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité AssuranceVie
     * 
     * @param AssuranceVie
     *            L'entité AssuranceVie à supprimer
     * @return L'entité AssuranceVie supprimé
     * @throws AssuranceVieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public AssuranceVie delete(AssuranceVie assuranceVie) throws AssuranceVieException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité AssuranceVie
     * 
     * @param idAssuranceVie
     *            L'identifiant de l'entité AssuranceVie à charger en mémoire
     * @return L'entité AssuranceVie chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceVieException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AssuranceVie read(String idAssuranceVie) throws JadePersistenceException, AssuranceVieException;

    /**
     * Chargement d'une AssuranceVie via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AssuranceVieException
     * @throws JadePersistenceException
     */
    public AssuranceVie readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws AssuranceVieException,
            JadePersistenceException;

    /**
     * Permet de chercher des AssuranceVie selon un modèle de critères.
     * 
     * @param AssuranceVieSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceVieException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AssuranceVieSearch search(AssuranceVieSearch assuranceVieSearch) throws JadePersistenceException,
            AssuranceVieException;

    /**
     * 
     * Permet la mise à jour d'une entité AssuranceVie
     * 
     * @param AssuranceVie
     *            L'entité AssuranceVie à mettre à jour
     * @return L'entité AssuranceVie mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceVieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public AssuranceVie update(AssuranceVie assuranceVie) throws JadePersistenceException, AssuranceVieException,
            DonneeFinanciereException;
}