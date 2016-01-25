/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AutreFortuneMobiliereException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleAutreFortuneMobiliere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleAutreFortuneMobiliereSearch;

/**
 * @author BSC
 * 
 */
public interface SimpleAutreFortuneMobiliereService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� simpleAutreFortuneMobiliere
     * 
     * @param simpleAutreFortuneMobiliere
     *            L'entit� simpleAutreFortuneMobiliere � cr�er
     * @return L'entit� simpleAutreFortuneMobiliere cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreFortuneMobiliereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAutreFortuneMobiliere create(SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere)
            throws JadePersistenceException, AutreFortuneMobiliereException;

    /**
     * Permet la suppression d'une entit� simpleAutreFortuneMobiliere
     * 
     * @param simpleAutreFortuneMobiliere
     *            L'entit� simpleAutreFortuneMobiliere � supprimer
     * @return L'entit� simpleAutreFortuneMobiliere supprim�
     * @throws AutreFortuneMobiliereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleAutreFortuneMobiliere delete(SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere)
            throws AutreFortuneMobiliereException, JadePersistenceException;

    /**
     * Permet l'effacement de simpleAutreFortuneMobiliere en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les
     *            simpleAutreFortuneMobiliere
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� simpleAutreFortuneMobiliere
     * 
     * @param idAutreFortuneMobiliere
     *            L'identifiant de l'entit� simpleAutreFortuneMobiliere � charger en m�moire
     * @return L'entit� simpleAutreFortuneMobiliere charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreFortuneMobiliereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAutreFortuneMobiliere read(String idAutreFortuneMobiliere) throws JadePersistenceException,
            AutreFortuneMobiliereException;

    /**
     * Permet de chercher des simpleAutreFortuneMobiliere selon un mod�le de crit�res.
     * 
     * @param searchModel
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     * @throws AutreFortuneMobiliereException
     */
    public SimpleAutreFortuneMobiliereSearch search(SimpleAutreFortuneMobiliereSearch searchModel)
            throws JadePersistenceException, AutreFortuneMobiliereException;

    /**
     * 
     * Permet la mise � jour d'une entit� simpleAutreFortuneMobiliere
     * 
     * @param simpleAutreFortuneMobiliere
     *            L'entit� simpleAutreFortuneMobiliere � mettre � jour
     * @return L'entit� simpleAutreFortuneMobiliere mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreFortuneMobiliereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAutreFortuneMobiliere update(SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere)
            throws JadePersistenceException, AutreFortuneMobiliereException;
}
