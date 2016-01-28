package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.SimpleTypeFraisObtentionRevenuException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenu;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenuSearch;

public interface SimpleTypeFraisObtentionRevenuService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleTypeFraisObtentionRevenu
     * 
     * @param SimpleTypeFraisObtentionRevenu
     *            L'entité SimpleTypeFraisObtentionRevenu à créer
     * @return L'entité SimpleTypeFraisObtentionRevenu créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws SimpleTypeFraisObtentionRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleTypeFraisObtentionRevenu create(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu)
            throws JadePersistenceException, SimpleTypeFraisObtentionRevenuException;

    /**
     * Permet la suppression d'une entité SimpleTypeFraisObtentionRevenu
     * 
     * @param SimpleTypeFraisObtentionRevenu
     *            L'entité SimpleTypeFraisObtentionRevenu à supprimer
     * @return L'entité SimpleTypeFraisObtentionRevenu supprimé
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws SimpleTypeFraisObtentionRevenuException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleTypeFraisObtentionRevenu delete(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu)
            throws SimpleTypeFraisObtentionRevenuException, JadePersistenceException;

    /**
     * Permet la suppression de plusieur entité. Attention aucun checker est lancer pour cette fonction
     * 
     * @param SimpleTypeFraisObtentionRevenu
     *            L'entité SimpleTypeFraisObtentionRevenu à supprimer
     * @return L'entité SimpleTypeFraisObtentionRevenu supprimé
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws SimpleTypeFraisObtentionRevenuException
     *             Levée en cas de problème dans la couche de persistence
     */
    public void delete(SimpleTypeFraisObtentionRevenuSearch searchType) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimpleTypeFraisObtentionRevenu
     * 
     * @param idTypeFraisObtentionRevenu
     *            L'identifiant de l'entité SimpleTypeFraisObtentionRevenu à charger en mémoire
     * @return L'entité SimpleTypeFraisObtentionRevenu chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws SimpleTypeFraisObtentionRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleTypeFraisObtentionRevenu read(String idTypeFraisObtentionRevenu) throws JadePersistenceException,
            SimpleTypeFraisObtentionRevenuException;

    /**
     * Permet la recherche des entités SimpleTypeFraisObtentionRevenu
     * 
     * @param simpleTypeFraisObtentionRevenuSearch
     * @return
     * @throws SimpleTypeFraisObtentionRevenuException
     * @throws JadePersistenceException
     */
    public SimpleTypeFraisObtentionRevenuSearch search(
            SimpleTypeFraisObtentionRevenuSearch simpleTypeFraisObtentionRevenuSearch)
            throws SimpleTypeFraisObtentionRevenuException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleTypeFraisObtentionRevenu
     * 
     * @param SimpleTypeFraisObtentionRevenu
     *            L'entité SimpleTypeFraisObtentionRevenu à mettre à jour
     * @return L'entité SimpleTypeFraisObtentionRevenu mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws SimpleTypeFraisObtentionRevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleTypeFraisObtentionRevenu update(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu)
            throws JadePersistenceException, SimpleTypeFraisObtentionRevenuException;

}
