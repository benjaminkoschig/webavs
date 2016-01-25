package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.SimpleTypeFraisObtentionRevenuException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenu;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenuSearch;

public interface SimpleTypeFraisObtentionRevenuService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimpleTypeFraisObtentionRevenu
     * 
     * @param SimpleTypeFraisObtentionRevenu
     *            L'entit� SimpleTypeFraisObtentionRevenu � cr�er
     * @return L'entit� SimpleTypeFraisObtentionRevenu cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws SimpleTypeFraisObtentionRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleTypeFraisObtentionRevenu create(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu)
            throws JadePersistenceException, SimpleTypeFraisObtentionRevenuException;

    /**
     * Permet la suppression d'une entit� SimpleTypeFraisObtentionRevenu
     * 
     * @param SimpleTypeFraisObtentionRevenu
     *            L'entit� SimpleTypeFraisObtentionRevenu � supprimer
     * @return L'entit� SimpleTypeFraisObtentionRevenu supprim�
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws SimpleTypeFraisObtentionRevenuException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleTypeFraisObtentionRevenu delete(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu)
            throws SimpleTypeFraisObtentionRevenuException, JadePersistenceException;

    /**
     * Permet la suppression de plusieur entit�. Attention aucun checker est lancer pour cette fonction
     * 
     * @param SimpleTypeFraisObtentionRevenu
     *            L'entit� SimpleTypeFraisObtentionRevenu � supprimer
     * @return L'entit� SimpleTypeFraisObtentionRevenu supprim�
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws SimpleTypeFraisObtentionRevenuException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public void delete(SimpleTypeFraisObtentionRevenuSearch searchType) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimpleTypeFraisObtentionRevenu
     * 
     * @param idTypeFraisObtentionRevenu
     *            L'identifiant de l'entit� SimpleTypeFraisObtentionRevenu � charger en m�moire
     * @return L'entit� SimpleTypeFraisObtentionRevenu charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws SimpleTypeFraisObtentionRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleTypeFraisObtentionRevenu read(String idTypeFraisObtentionRevenu) throws JadePersistenceException,
            SimpleTypeFraisObtentionRevenuException;

    /**
     * Permet la recherche des entit�s SimpleTypeFraisObtentionRevenu
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
     * Permet la mise � jour d'une entit� SimpleTypeFraisObtentionRevenu
     * 
     * @param SimpleTypeFraisObtentionRevenu
     *            L'entit� SimpleTypeFraisObtentionRevenu � mettre � jour
     * @return L'entit� SimpleTypeFraisObtentionRevenu mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws SimpleTypeFraisObtentionRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleTypeFraisObtentionRevenu update(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu)
            throws JadePersistenceException, SimpleTypeFraisObtentionRevenuException;

}
