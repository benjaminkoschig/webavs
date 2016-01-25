package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.TypeFraisObtentionRevenuException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenuSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.TypeFraisObtentionRevenu;

public interface TypeFraisObtentionRevenuService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws TypeFraisObtentionRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleTypeFraisObtentionRevenuSearch search) throws TypeFraisObtentionRevenuException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� TypeFraisObtentionRevenu
     * 
     * @param TypeFraisObtentionRevenu
     *            L'entit� TypeFraisObtentionRevenu � cr�er
     * @return L'entit� TypeFraisObtentionRevenu cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TypeFraisObtentionRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public TypeFraisObtentionRevenu create(TypeFraisObtentionRevenu typeFraisObtentionRevenu)
            throws JadePersistenceException, TypeFraisObtentionRevenuException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� TypeFraisObtentionRevenu
     * 
     * @param TypeFraisObtentionRevenu
     *            L'entit� TypeFraisObtentionRevenu � supprimer
     * @return L'entit� TypeFraisObtentionRevenu supprim�
     * @throws TypeFraisObtentionRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public TypeFraisObtentionRevenu delete(TypeFraisObtentionRevenu typeFraisObtentionRevenu)
            throws TypeFraisObtentionRevenuException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� TypeFraisObtentionRevenu
     * 
     * @param idTypeFraisObtentionRevenu
     *            L'identifiant de l'entit� TypeFraisObtentionRevenu � charger en m�moire
     * @return L'entit� TypeFraisObtentionRevenu charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TypeFraisObtentionRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public TypeFraisObtentionRevenu read(String idTypeFraisObtentionRevenu) throws JadePersistenceException,
            TypeFraisObtentionRevenuException;

    /**
     * Permet de chercher des TypeFraisObtentionRevenu selon un mod�le de crit�res.
     * 
     * @param SimpleTypeFraisObtentionRevenuSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TypeFraisObtentionRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleTypeFraisObtentionRevenuSearch search(
            SimpleTypeFraisObtentionRevenuSearch typeFraisObtentionRevenuSearch) throws JadePersistenceException,
            TypeFraisObtentionRevenuException;

    /**
     * 
     * Permet la mise � jour d'une entit� TypeFraisObtentionRevenu
     * 
     * @param TypeFraisObtentionRevenu
     *            L'entit� TypeFraisObtentionRevenu � mettre � jour
     * @return L'entit� TypeFraisObtentionRevenu mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TypeFraisObtentionRevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public TypeFraisObtentionRevenu update(TypeFraisObtentionRevenu typeFraisObtentionRevenu)
            throws JadePersistenceException, TypeFraisObtentionRevenuException, DonneeFinanciereException;
}