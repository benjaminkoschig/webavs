/**
 * 
 */
package ch.globaz.amal.business.services.models.parametreannuel;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.parametreannuel.ParametreAnnuelException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuel;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch;

/**
 * @author CBU
 * 
 */
public interface SimpleParametreAnnuelService extends JadeApplicationService {

    /**
     * Copie une année de paramètre pour en créer une nouvelle
     * 
     * @param yearToCopy
     * @param newYear
     * @throws ParametreAnnuelException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public void copyParams(String yearToCopy, String newYear) throws ParametreAnnuelException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleParametreAnnuelSearch search) throws ParametreAnnuelException, JadePersistenceException;

    /**
     * Creation d'une entité en base de donnée
     * 
     * @param simpleParametreAnnuel
     * @return l'entité crée
     * @throws ParametreAnnuelException
     * @throws JadePersistenceException
     */
    public SimpleParametreAnnuel create(SimpleParametreAnnuel simpleParametreAnnuel) throws ParametreAnnuelException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entité
     * 
     * @param simpleParametreAnnuel
     * @return l'entité supprimé
     * @throws JadePersistenceException
     * @throws ParametreAnnuelException
     */
    public SimpleParametreAnnuel delete(SimpleParametreAnnuel simpleParametreAnnuel) throws JadePersistenceException,
            ParametreAnnuelException;

    /**
     * Permet le chargement d'une entité
     * 
     * @param idParametreAnnuel
     * @return l'entité chargé
     * @throws JadePersistenceException
     * @throws ParametreAnnuelException
     */
    public SimpleParametreAnnuel read(String idParametreAnnuel) throws JadePersistenceException,
            ParametreAnnuelException;

    /**
     * Permet la recherche d'entités
     * 
     * @param parametreAnnuelSearch
     * @return le resultat de la recherche
     * @throws JadePersistenceException
     * @throws ParametreAnnuelException
     */
    public SimpleParametreAnnuelSearch search(SimpleParametreAnnuelSearch parametreAnnuelSearch)
            throws JadePersistenceException, ParametreAnnuelException;

    /**
     * Permet la recherche d'entités avec une année et un type (pour les recherches AJAX
     * 
     * @param parametreAnnuelSearch
     * @return le resultat de la recherche
     * @throws JadePersistenceException
     * @throws ParametreAnnuelException
     */
    public SimpleParametreAnnuel searchAJAX(String year, String type) throws JadePersistenceException,
            ParametreAnnuelException;

    /**
     * Update d'une entité en base de donnée
     * 
     * @param simpleParametreAnnuel
     * @return l'entité crée
     * @throws ParametreAnnuelException
     * @throws JadePersistenceException
     */
    public SimpleParametreAnnuel update(SimpleParametreAnnuel simpleParametreAnnuel) throws ParametreAnnuelException,
            JadePersistenceException;

}
