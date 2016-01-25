/**
 * 
 */
package ch.globaz.amal.business.services.models.parametremodel;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.parametreModel.ParametreModelException;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplex;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplexSearch;

/**
 * @author CBU
 * 
 */
public interface ParametreModelService extends JadeApplicationService {

    /**
     * 
     * Permet la creation d'une entit� formule
     * 
     * @param ParametreModelComplex
     *            La formule est cr�e
     * @return La formule est cr�e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ParametreModelException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */

    public ParametreModelComplex create(ParametreModelComplex parametreModelComplex) throws JadePersistenceException,
            ParametreModelException;

    /**
     * Permet la recherche des formules correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws ParametreModelExceptionException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public ParametreModelComplex delete(ParametreModelComplex parametreModelComplex) throws JadePersistenceException,
            ParametreModelException;

    /**
     * Permet de charger en m�moire un parametreModel
     * 
     * @param idParametreModel
     *            L'identifiant du parametreModel � charger en m�moire
     * @return Le parametreModel charg� en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws parametreModel
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ParametreModelComplex read(String idParametreModel) throws JadePersistenceException,
            ParametreModelException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la recherche des formules correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws ParametreModelExceptionException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public ParametreModelComplexSearch search(ParametreModelComplexSearch parametreModelComplexSearch)
            throws JadePersistenceException, ParametreModelException;

    /*	*//**
     * 
     * Permet la mise � jour d'une entit� formule
     * 
     * @param ParametreModelComplex
     *            La formule est mise � jour
     * @return La formule mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ParametreModelExceptionException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */

    public ParametreModelComplex update(ParametreModelComplex parametreModelComplex) throws JadePersistenceException,
            ParametreModelException;

}
