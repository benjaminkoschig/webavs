/**
 * 
 */
package ch.globaz.amal.business.services.models.parametreapplication;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.amal.business.exceptions.models.parametreapplication.ParametreApplicationException;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplication;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplicationSearch;

/**
 * @author dhi
 * 
 */
public interface SimpleParametreApplicationService extends JadeApplicationService {

    /**
     * Renseignement du nombre d'enregistrement présent selon les paramètres de recherches
     * 
     * @param search
     *            paramètres de recherche de SimpleParametreApplicationSearch
     * @return Le nombre d'enregistrement trouvé
     * @throws ParametreApplicationException
     * @throws JadePersistenceException
     */
    public int count(SimpleParametreApplicationSearch search) throws ParametreApplicationException,
            JadePersistenceException;

    /**
     * Création d'un enregistrement de type SimpleParametreApplication
     * 
     * @param simpleParametreApplication
     *            Le modèle de données renseigné
     * @return Le modèle de données renseigné, après création en DB (isNew = false et PSPY, CSPY créés)
     * @throws ParametreApplicationException
     * @throws JadePersistenceException
     */
    public SimpleParametreApplication create(SimpleParametreApplication simpleParametreApplication)
            throws ParametreApplicationException, JadePersistenceException;

    /**
     * Effacement d'un enregistrement de type SimpleParametreApplication
     * 
     * @param simpleParametreApplication
     *            Le modèle de données renseigné, à effacer
     * @return
     * @throws JadePersistenceException
     * @throws ParametreApplicationException
     */
    public SimpleParametreApplication delete(SimpleParametreApplication simpleParametreApplication)
            throws JadePersistenceException, ParametreApplicationException;

    /**
     * Lecture d'un enregistrement de type SimpleParametreApplication
     * 
     * @param idParametreApplication
     *            L'id de l'enregistrement à lire
     * @return Le modèle de données renseigné
     * @throws JadePersistenceException
     * @throws ParametreApplicationException
     */
    public SimpleParametreApplication read(String idParametreApplication) throws JadePersistenceException,
            ParametreApplicationException;

    /**
     * Recherche d'un enregistrement de type SimpleParametreApplication, selon modèle de recherche
     * 
     * @param parametreApplicationSearch
     *            Le modèle de recherche renseigné avec les paramètres de recherche
     * @return Le modèle de recherche renseigné avec les résultats obtenu
     * @throws JadePersistenceException
     * @throws ParametreApplicationException
     */
    public SimpleParametreApplicationSearch search(SimpleParametreApplicationSearch parametreApplicationSearch)
            throws JadePersistenceException, ParametreApplicationException;

    /**
     * Mise à jour d'un enregistrement selon modèle de données SimpleParametreApplication
     * 
     * @param simpleParametreApplication
     *            modèle de données renseigné
     * @return le même modèle de données renseigné
     * @throws ParametreApplicationException
     * @throws JadePersistenceException
     */
    public SimpleParametreApplication update(SimpleParametreApplication simpleParametreApplication)
            throws ParametreApplicationException, JadePersistenceException;

}
