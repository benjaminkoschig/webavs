package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AutresDettesProuveesException;
import ch.globaz.pegasus.business.models.fortuneusuelle.AutresDettesProuvees;
import ch.globaz.pegasus.business.models.fortuneusuelle.AutresDettesProuveesSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface AutresDettesProuveesService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws AutresDettesProuveesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(AutresDettesProuveesSearch search) throws AutresDettesProuveesException, JadePersistenceException;

    /**
     * Permet la création d'une entité AutresDettesProuvees
     * 
     * @param AutresDettesProuvees
     *            L'entité AutresDettesProuvees à créer
     * @return L'entité AutresDettesProuvees créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AutresDettesProuvees create(AutresDettesProuvees autresDettesProuvees) throws JadePersistenceException,
            AutresDettesProuveesException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité AutresDettesProuvees
     * 
     * @param AutresDettesProuvees
     *            L'entité AutresDettesProuvees à supprimer
     * @return L'entité AutresDettesProuvees supprimé
     * @throws AutresDettesProuveesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public AutresDettesProuvees delete(AutresDettesProuvees autresDettesProuvees) throws AutresDettesProuveesException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité AutresDettesProuvees
     * 
     * @param idAutresDettesProuvees
     *            L'identifiant de l'entité AutresDettesProuvees à charger en mémoire
     * @return L'entité AutresDettesProuvees chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AutresDettesProuvees read(String idAutresDettesProuvees) throws JadePersistenceException,
            AutresDettesProuveesException;

    /**
     * Chargement d'une AutresDettesProuvees via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AutresDettesProuveesException
     * @throws JadePersistenceException
     */
    public AutresDettesProuvees readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws AutresDettesProuveesException, JadePersistenceException;

    /**
     * Permet de chercher des AutresDettesProuvees selon un modèle de critères.
     * 
     * @param AutresDettesProuveesSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AutresDettesProuveesSearch search(AutresDettesProuveesSearch autresDettesProuveesSearch)
            throws JadePersistenceException, AutresDettesProuveesException;

    /**
     * 
     * Permet la mise à jour d'une entité AutresDettesProuvees
     * 
     * @param AutresDettesProuvees
     *            L'entité AutresDettesProuvees à mettre à jour
     * @return L'entité AutresDettesProuvees mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public AutresDettesProuvees update(AutresDettesProuvees autresDettesProuvees) throws JadePersistenceException,
            AutresDettesProuveesException, DonneeFinanciereException;
}