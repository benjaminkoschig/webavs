/**
 * 
 */
package ch.globaz.al.business.services.models.envoi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.envoi.EnvoiComplexModel;
import ch.globaz.al.business.models.envoi.EnvoiComplexModelSearch;

/**
 * @author dhi
 * 
 */
public interface EnvoiComplexModelService extends JadeApplicationService {

    /**
     * Permet de compter le nombre de résultats suivant le modèle de recherche
     * 
     * @param envoiComplexSearch
     *            modèle de recherche renseigné
     * @return modèle de recherche renseigné et complété
     * @throws JadePersistenceException
     *             levée en cas de problème de persistence
     * @throws JadeApplicationException
     *             levée ne cas de problème métier
     */
    public int count(EnvoiComplexModelSearch envoiComplexSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Permet d'effacer un enregistrement envoi particulier
     * 
     * @param envoiComplex
     *            modèle de données renseigné
     * @return
     * @throws JadeApplicationException
     *             levée en cas de problème métier
     * @throws JadePersistenceException
     *             levée en cas de problème de persistence
     */
    public EnvoiComplexModel delete(EnvoiComplexModel envoiComplex) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture et mise en mémoire des informations relatives à un envoi
     * 
     * @param idEnvoiComplexModel
     *            id de l'envoi à lire
     * @return un modèle de données renseigné
     * @throws JadeApplicationException
     *             levée en cas de problème métier
     * @throws JadePersistenceException
     *             levée en cas de problème de persistence
     */
    public EnvoiComplexModel read(String idEnvoiComplexModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche d'envois en fonction d'un modèle de recherche renseigné
     * 
     * @param envoiComplexSearch
     *            le modèle de recherche renseigné
     * @return le modèle de recherche renseigné et complété
     * @throws JadeApplicationException
     *             levée en cas de problème métier
     * @throws JadePersistenceException
     *             levée en cas de problème de persistence
     */
    public EnvoiComplexModelSearch search(EnvoiComplexModelSearch envoiComplexSearch) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Mise à jour d'un envoi complex
     * 
     * @param envoiComplex
     *            le modèle à mettre à jour
     * @return le modèle mis à jour
     * @throws JadeApplicationException
     *             levée en cas de problème métier
     * @throws JadePersistenceException
     *             levée en cas de problème de persistence
     */
    public EnvoiComplexModel update(EnvoiComplexModel envoiComplex) throws JadeApplicationException,
            JadePersistenceException;

}
