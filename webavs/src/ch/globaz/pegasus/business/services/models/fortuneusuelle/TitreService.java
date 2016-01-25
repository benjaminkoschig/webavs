package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.TitreException;
import ch.globaz.pegasus.business.models.fortuneusuelle.Titre;
import ch.globaz.pegasus.business.models.fortuneusuelle.TitreSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface TitreService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws TitreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(TitreSearch search) throws TitreException, JadePersistenceException;

    /**
     * Permet la création d'une entité Titre
     * 
     * @param Titre
     *            L'entité Titre à créer
     * @return L'entité Titre créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TitreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Titre create(Titre titre) throws JadePersistenceException, TitreException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité Titre
     * 
     * @param Titre
     *            L'entité Titre à supprimer
     * @return L'entité Titre supprimé
     * @throws TitreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public Titre delete(Titre titre) throws TitreException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité Titre
     * 
     * @param idTitre
     *            L'identifiant de l'entité Titre à charger en mémoire
     * @return L'entité Titre chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TitreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Titre read(String idTitre) throws JadePersistenceException, TitreException;

    /**
     * Chargement d'un Titre via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws TitreException
     * @throws JadePersistenceException
     */
    public Titre readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws TitreException,
            JadePersistenceException;

    /**
     * Permet de chercher des Titre selon un modèle de critères.
     * 
     * @param TitreSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TitreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public TitreSearch search(TitreSearch titreSearch) throws JadePersistenceException, TitreException;

    /**
     * 
     * Permet la mise à jour d'une entité Titre
     * 
     * @param Titre
     *            L'entité Titre à mettre à jour
     * @return L'entité Titre mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TitreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public Titre update(Titre titre) throws JadePersistenceException, TitreException, DonneeFinanciereException;
}