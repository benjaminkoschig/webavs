/**
 * 
 */
package ch.globaz.al.business.services.models.envoi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.exceptions.model.envoi.ALEnvoiJobException;
import ch.globaz.al.business.models.envoi.EnvoiJobSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiJobSimpleModelSearch;

/**
 * service de gestion de la persitance des données pour les jobs envoi AF
 * 
 * @author dhi
 */
public interface EnvoiJobSimpleModelService extends JadeApplicationService {
    /**
     * Compte le nombre d'enregistrement disponibles pour un modèle de recherche donné
     * 
     * @param envoiJobSearch
     *            modèle de recherche renseigné
     * @return Le nombre d'enregistrement qui satisfont le modèle de recherche
     * @throws ALEnvoiJobException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence (data)
     */
    public int count(EnvoiJobSimpleModelSearch envoiJobSearch) throws ALEnvoiJobException, JadePersistenceException;

    /**
     * Création d'un enregistrement de type EnvoiJobSimpleModel en base
     * 
     * @param envoiJob
     *            Le modèle de données à créer, renseigné
     * @return Le modèle de données créé
     * @throws ALEnvoiJobException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence
     */
    public EnvoiJobSimpleModel create(EnvoiJobSimpleModel envoiJob) throws ALEnvoiJobException,
            JadePersistenceException;

    /**
     * Effacement d'un enregistrement de type EnvoiJobSimpleModel en base
     * 
     * @param envoiJob
     *            Le modèle de données à supprimer
     * @return Le modèle de données supprimé
     * @throws ALEnvoiJobException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence
     */
    public EnvoiJobSimpleModel delete(EnvoiJobSimpleModel envoiJob) throws ALEnvoiJobException,
            JadePersistenceException;

    /**
     * Lecture d'un enregistrement de type EnvoiJobSimpleModel depuis la base
     * 
     * @param idEnvoiJob
     *            L'id de l'enregistrement à lire
     * @return Le modèle de données renseigné
     * @throws ALEnvoiJobException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence
     */
    public EnvoiJobSimpleModel read(String idEnvoiJob) throws ALEnvoiJobException, JadePersistenceException;

    /**
     * Recherche d'éléments de type EnvoiJobSimpleModel en base
     * 
     * @param envoiJobSearch
     *            Le modèle de recherche de données renseignés
     * @return Le modèle de recherche complété
     * @throws ALEnvoiJobException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevée en cas de problèmes de persistence
     */
    public EnvoiJobSimpleModelSearch search(EnvoiJobSimpleModelSearch envoiJobSearch) throws ALEnvoiJobException,
            JadePersistenceException;

    /**
     * Mise à jour d'un enregistrement de type EnvoiJobSimpleModel en base
     * 
     * @param envoiJob
     *            Le modèle de données à mettre à jour
     * @return Le modèle de données mis à jour
     * @throws ALEnvoiJobException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence
     */
    public EnvoiJobSimpleModel update(EnvoiJobSimpleModel envoiJob) throws ALEnvoiJobException,
            JadePersistenceException;

}
