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
     * Permet la creation d'une entité formule
     * 
     * @param ParametreModelComplex
     *            La formule est crée
     * @return La formule est crée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ParametreModelException
     *             Levée en cas de problème métier dans l'exécution du service
     */

    public ParametreModelComplex create(ParametreModelComplex parametreModelComplex) throws JadePersistenceException,
            ParametreModelException;

    /**
     * Permet la recherche des formules correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return Le modèle de critère avec les résultats
     * @throws ParametreModelExceptionException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public ParametreModelComplex delete(ParametreModelComplex parametreModelComplex) throws JadePersistenceException,
            ParametreModelException;

    /**
     * Permet de charger en mémoire un parametreModel
     * 
     * @param idParametreModel
     *            L'identifiant du parametreModel à charger en mémoire
     * @return Le parametreModel chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws parametreModel
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ParametreModelComplex read(String idParametreModel) throws JadePersistenceException,
            ParametreModelException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la recherche des formules correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return Le modèle de critère avec les résultats
     * @throws ParametreModelExceptionException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public ParametreModelComplexSearch search(ParametreModelComplexSearch parametreModelComplexSearch)
            throws JadePersistenceException, ParametreModelException;

    /*	*//**
     * 
     * Permet la mise à jour d'une entité formule
     * 
     * @param ParametreModelComplex
     *            La formule est mise à jour
     * @return La formule mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ParametreModelExceptionException
     *             Levée en cas de problème métier dans l'exécution du service
     */

    public ParametreModelComplex update(ParametreModelComplex parametreModelComplex) throws JadePersistenceException,
            ParametreModelException;

}
