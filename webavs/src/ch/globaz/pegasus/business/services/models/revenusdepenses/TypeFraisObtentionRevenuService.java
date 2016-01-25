package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.TypeFraisObtentionRevenuException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenuSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.TypeFraisObtentionRevenu;

public interface TypeFraisObtentionRevenuService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws TypeFraisObtentionRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleTypeFraisObtentionRevenuSearch search) throws TypeFraisObtentionRevenuException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité TypeFraisObtentionRevenu
     * 
     * @param TypeFraisObtentionRevenu
     *            L'entité TypeFraisObtentionRevenu à créer
     * @return L'entité TypeFraisObtentionRevenu créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TypeFraisObtentionRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public TypeFraisObtentionRevenu create(TypeFraisObtentionRevenu typeFraisObtentionRevenu)
            throws JadePersistenceException, TypeFraisObtentionRevenuException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité TypeFraisObtentionRevenu
     * 
     * @param TypeFraisObtentionRevenu
     *            L'entité TypeFraisObtentionRevenu à supprimer
     * @return L'entité TypeFraisObtentionRevenu supprimé
     * @throws TypeFraisObtentionRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public TypeFraisObtentionRevenu delete(TypeFraisObtentionRevenu typeFraisObtentionRevenu)
            throws TypeFraisObtentionRevenuException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité TypeFraisObtentionRevenu
     * 
     * @param idTypeFraisObtentionRevenu
     *            L'identifiant de l'entité TypeFraisObtentionRevenu à charger en mémoire
     * @return L'entité TypeFraisObtentionRevenu chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TypeFraisObtentionRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public TypeFraisObtentionRevenu read(String idTypeFraisObtentionRevenu) throws JadePersistenceException,
            TypeFraisObtentionRevenuException;

    /**
     * Permet de chercher des TypeFraisObtentionRevenu selon un modèle de critères.
     * 
     * @param SimpleTypeFraisObtentionRevenuSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TypeFraisObtentionRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleTypeFraisObtentionRevenuSearch search(
            SimpleTypeFraisObtentionRevenuSearch typeFraisObtentionRevenuSearch) throws JadePersistenceException,
            TypeFraisObtentionRevenuException;

    /**
     * 
     * Permet la mise à jour d'une entité TypeFraisObtentionRevenu
     * 
     * @param TypeFraisObtentionRevenu
     *            L'entité TypeFraisObtentionRevenu à mettre à jour
     * @return L'entité TypeFraisObtentionRevenu mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TypeFraisObtentionRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public TypeFraisObtentionRevenu update(TypeFraisObtentionRevenu typeFraisObtentionRevenu)
            throws JadePersistenceException, TypeFraisObtentionRevenuException, DonneeFinanciereException;
}