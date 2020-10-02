package ch.globaz.pegasus.business.services.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AutresRevenusException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.FraisGardeException;
import ch.globaz.pegasus.business.models.revenusdepenses.FraisGarde;
import ch.globaz.pegasus.business.models.revenusdepenses.FraisGardeSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

public interface FraisGardeService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     *
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws AutresRevenusException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(FraisGardeSearch search) throws FraisGardeException, JadePersistenceException;

    /**
     * Permet la création d'une entité FraisGarde
     *
     * @param fraisGarde
     *            L'entité FraisGarde à créer
     * @return L'entité FraisGarde créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws FraisGardeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public FraisGarde create(FraisGarde fraisGarde) throws JadePersistenceException, FraisGardeException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité FraisGarde
     *
     * @param autresRevenus
     *            L'entité FraisGarde à supprimer
     * @return L'entité FraisGarde supprimé
     * @throws FraisGardeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public FraisGarde delete(FraisGarde autresRevenus) throws FraisGardeException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité FraisGarde
     *
     * @param idFraisGarde
     *            L'identifiant de l'entité FraisGarde à charger en mémoire
     * @return L'entité FraisGarde chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws FraisGardeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public FraisGarde read(String idFraisGarde) throws JadePersistenceException, FraisGardeException;

    /**
     * Chargement d'un FraisGarde via l'id donnee financiere header
     *
     * @param idDonneeFinanciereHeader
     * @return
     * @throws FraisGardeException
     * @throws JadePersistenceException
     */
    public FraisGarde readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws FraisGardeException,
            JadePersistenceException;

    /**
     * Permet de chercher des FraisGarde selon un modèle de critères.
     *
     * @param fraisGardeSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws FraisGardeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public FraisGardeSearch search(FraisGardeSearch fraisGardeSearch) throws JadePersistenceException,
            FraisGardeException;

    /**
     *
     * Permet la mise à jour d'une entité FraisGarde
     *
     * @param fraisGarde
     *            L'entité FraisGarde à mettre à jour
     * @return L'entité FraisGarde mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws FraisGardeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public FraisGarde update(FraisGarde fraisGarde) throws JadePersistenceException, FraisGardeException,
            DonneeFinanciereException;
}
