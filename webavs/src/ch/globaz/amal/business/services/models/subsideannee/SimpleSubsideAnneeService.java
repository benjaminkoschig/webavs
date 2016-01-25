/**
 * 
 */
package ch.globaz.amal.business.services.models.subsideannee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.exceptions.models.subsideannee.SubsideAnneeException;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnnee;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnneeSearch;

/**
 * @author CBU
 * 
 */
public interface SimpleSubsideAnneeService extends JadeApplicationService {

    /**
     * Permet de copier les param�tres d'une ann�e
     * 
     * @param simpleSubsideAnnee
     * @return l'entit� cr�e
     * @throws SubsideAnneeException
     * @throws JadePersistenceException
     */
    public void copyParams(String yearToCopy, String newYear) throws SubsideAnneeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

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
    public int count(SimpleSubsideAnneeSearch search) throws SubsideAnneeException, JadePersistenceException;

    /**
     * Creation d'une entit� en base de donn�e
     * 
     * @param simpleSubsideAnnee
     * @return l'entit� cr�e
     * @throws SubsideAnneeException
     * @throws JadePersistenceException
     */
    public SimpleSubsideAnnee create(SimpleSubsideAnnee simpleSubsideAnnee) throws SubsideAnneeException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entit�
     * 
     * @param simpleSubsideAnnee
     * @return l'entit� supprim�
     * @throws JadePersistenceException
     * @throws SubsideAnneeException
     */
    public SimpleSubsideAnnee delete(SimpleSubsideAnnee simpleSubsideAnnee) throws JadePersistenceException,
            SubsideAnneeException;

    /**
     * Permet le chargement d'une entit�
     * 
     * @param idSubsideAnnee
     * @return l'entit� charg�
     * @throws JadePersistenceException
     * @throws SubsideAnneeException
     */
    public SimpleSubsideAnnee read(String idSubsideAnnee) throws JadePersistenceException, SubsideAnneeException;

    /**
     * Permet la recherche d'entit�s
     * 
     * @param subsideAnneeSearch
     * @return le resultat de la recherche
     * @throws JadePersistenceException
     * @throws SubsideAnneeException
     */
    public SimpleSubsideAnneeSearch search(SimpleSubsideAnneeSearch subsideAnneeSearch)
            throws JadePersistenceException, SubsideAnneeException;

    /**
     * Permet la recherche d'entit�s depuis un appel AJAX
     * 
     * @param subsideAnneeSearch
     * @return le resultat de la recherche
     * @throws JadePersistenceException
     * @throws SubsideAnneeException
     */
    public ArrayList<SimpleSubsideAnnee> searchAJAX(String year) throws JadePersistenceException, SubsideAnneeException;

    /**
     * Update d'une entit� en base de donn�e
     * 
     * @param simpleSubsideAnnee
     * @return l'entit� cr�e
     * @throws SubsideAnneeException
     * @throws JadePersistenceException
     */
    public SimpleSubsideAnnee update(SimpleSubsideAnnee simpleSubsideAnnee) throws SubsideAnneeException,
            JadePersistenceException;
}
