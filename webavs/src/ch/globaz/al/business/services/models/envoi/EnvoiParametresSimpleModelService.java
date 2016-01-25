/**
 * 
 */
package ch.globaz.al.business.services.models.envoi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.exceptions.model.envoi.ALEnvoiParametresException;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModelSearch;

/**
 * service de gestion de la persitance des données pour paramètres envoi
 * 
 * @author dhi
 * 
 */
public interface EnvoiParametresSimpleModelService extends JadeApplicationService {
    /**
     * Compte le nombre d'enregistrement disponibles pour un modèle de recherche donné
     * 
     * @param envoiParametresSearch
     *            modèle de recherche renseigné
     * @return Le nombre d'enregistrement qui satisfont le modèle de recherche
     * @throws ALEnvoiParametresException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence (data)
     */
    public int count(EnvoiParametresSimpleModelSearch envoiParametresSearch) throws ALEnvoiParametresException,
            JadePersistenceException;

    /**
     * Création d'un enregistrement de type EnvoiParametresSimpleModel en base
     * 
     * @param envoiParametres
     *            Le modèle de données à créer, renseigné
     * @return Le modèle de données créé
     * @throws ALEnvoiParametresException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence
     */
    public EnvoiParametresSimpleModel create(EnvoiParametresSimpleModel envoiParametres)
            throws ALEnvoiParametresException, JadePersistenceException;

    /**
     * Effacement d'un enregistrement de type EnvoiParametresSimpleModel en base
     * 
     * @param envoiParametres
     *            Le modèle de données à supprimer
     * @return Le modèle de données supprimé
     * @throws ALEnvoiParametresException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence
     */
    public EnvoiParametresSimpleModel delete(EnvoiParametresSimpleModel envoiParametres)
            throws ALEnvoiParametresException, JadePersistenceException;

    /**
     * Lecture d'un enregistrement de type EnvoiParametresSimpleModel depuis la base
     * 
     * @param idEnvoiParametres
     *            L'id de l'enregistrement à lire
     * @return Le modèle de données renseigné
     * @throws ALEnvoiParametresException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence
     */
    public EnvoiParametresSimpleModel read(String idEnvoiParametres) throws ALEnvoiParametresException,
            JadePersistenceException;

    /**
     * Recherche d'éléments de type EnvoiJobSimpleModel en base
     * 
     * @param envoiParametresSearch
     *            Le modèle de recherche de données renseignés
     * @return Le modèle de recherche complété
     * @throws ALEnvoiParametresException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevée en cas de problèmes de persistence
     */
    public EnvoiParametresSimpleModelSearch search(EnvoiParametresSimpleModelSearch envoiParametresSearch)
            throws ALEnvoiParametresException, JadePersistenceException;

    /**
     * Mise à jour d'un enregistrement de type EnvoiParametresSimpleModel en base
     * 
     * @param envoiParametres
     *            Le modèle de données à mettre à jour
     * @return Le modèle de données mis à jour
     * @throws ALEnvoiParametresException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence
     */
    public EnvoiParametresSimpleModel update(EnvoiParametresSimpleModel envoiParametres)
            throws ALEnvoiParametresException, JadePersistenceException;

}
