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
     * Copie une ann�e de param�tre pour en cr�er une nouvelle
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
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleParametreAnnuelSearch search) throws ParametreAnnuelException, JadePersistenceException;

    /**
     * Creation d'une entit� en base de donn�e
     * 
     * @param simpleParametreAnnuel
     * @return l'entit� cr�e
     * @throws ParametreAnnuelException
     * @throws JadePersistenceException
     */
    public SimpleParametreAnnuel create(SimpleParametreAnnuel simpleParametreAnnuel) throws ParametreAnnuelException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entit�
     * 
     * @param simpleParametreAnnuel
     * @return l'entit� supprim�
     * @throws JadePersistenceException
     * @throws ParametreAnnuelException
     */
    public SimpleParametreAnnuel delete(SimpleParametreAnnuel simpleParametreAnnuel) throws JadePersistenceException,
            ParametreAnnuelException;

    /**
     * Permet le chargement d'une entit�
     * 
     * @param idParametreAnnuel
     * @return l'entit� charg�
     * @throws JadePersistenceException
     * @throws ParametreAnnuelException
     */
    public SimpleParametreAnnuel read(String idParametreAnnuel) throws JadePersistenceException,
            ParametreAnnuelException;

    /**
     * Permet la recherche d'entit�s
     * 
     * @param parametreAnnuelSearch
     * @return le resultat de la recherche
     * @throws JadePersistenceException
     * @throws ParametreAnnuelException
     */
    public SimpleParametreAnnuelSearch search(SimpleParametreAnnuelSearch parametreAnnuelSearch)
            throws JadePersistenceException, ParametreAnnuelException;

    /**
     * Permet la recherche d'entit�s avec une ann�e et un type (pour les recherches AJAX
     * 
     * @param parametreAnnuelSearch
     * @return le resultat de la recherche
     * @throws JadePersistenceException
     * @throws ParametreAnnuelException
     */
    public SimpleParametreAnnuel searchAJAX(String year, String type) throws JadePersistenceException,
            ParametreAnnuelException;

    /**
     * Update d'une entit� en base de donn�e
     * 
     * @param simpleParametreAnnuel
     * @return l'entit� cr�e
     * @throws ParametreAnnuelException
     * @throws JadePersistenceException
     */
    public SimpleParametreAnnuel update(SimpleParametreAnnuel simpleParametreAnnuel) throws ParametreAnnuelException,
            JadePersistenceException;

}
