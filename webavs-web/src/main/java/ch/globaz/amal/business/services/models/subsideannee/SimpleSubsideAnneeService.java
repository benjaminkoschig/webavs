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
     * Permet de copier les paramètres d'une année
     * 
     * @param simpleSubsideAnnee
     * @return l'entité crée
     * @throws SubsideAnneeException
     * @throws JadePersistenceException
     */
    public void copyParams(String yearToCopy, String newYear) throws SubsideAnneeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

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
    public int count(SimpleSubsideAnneeSearch search) throws SubsideAnneeException, JadePersistenceException;

    /**
     * Creation d'une entité en base de donnée
     * 
     * @param simpleSubsideAnnee
     * @return l'entité crée
     * @throws SubsideAnneeException
     * @throws JadePersistenceException
     */
    public SimpleSubsideAnnee create(SimpleSubsideAnnee simpleSubsideAnnee) throws SubsideAnneeException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entité
     * 
     * @param simpleSubsideAnnee
     * @return l'entité supprimé
     * @throws JadePersistenceException
     * @throws SubsideAnneeException
     */
    public SimpleSubsideAnnee delete(SimpleSubsideAnnee simpleSubsideAnnee) throws JadePersistenceException,
            SubsideAnneeException;

    /**
     * Permet le chargement d'une entité
     * 
     * @param idSubsideAnnee
     * @return l'entité chargé
     * @throws JadePersistenceException
     * @throws SubsideAnneeException
     */
    public SimpleSubsideAnnee read(String idSubsideAnnee) throws JadePersistenceException, SubsideAnneeException;

    /**
     * Permet la recherche d'entités
     * 
     * @param subsideAnneeSearch
     * @return le resultat de la recherche
     * @throws JadePersistenceException
     * @throws SubsideAnneeException
     */
    public SimpleSubsideAnneeSearch search(SimpleSubsideAnneeSearch subsideAnneeSearch)
            throws JadePersistenceException, SubsideAnneeException;

    /**
     * Permet la recherche d'entités depuis un appel AJAX
     * 
     * @param subsideAnneeSearch
     * @return le resultat de la recherche
     * @throws JadePersistenceException
     * @throws SubsideAnneeException
     */
    public ArrayList<SimpleSubsideAnnee> searchAJAX(String year) throws JadePersistenceException, SubsideAnneeException;

    /**
     * Update d'une entité en base de donnée
     * 
     * @param simpleSubsideAnnee
     * @return l'entité crée
     * @throws SubsideAnneeException
     * @throws JadePersistenceException
     */
    public SimpleSubsideAnnee update(SimpleSubsideAnnee simpleSubsideAnnee) throws SubsideAnneeException,
            JadePersistenceException;
}
