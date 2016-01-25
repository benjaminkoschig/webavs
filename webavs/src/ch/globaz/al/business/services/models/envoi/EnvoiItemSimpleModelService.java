/**
 * 
 */
package ch.globaz.al.business.services.models.envoi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.exceptions.model.envoi.ALEnvoiItemException;
import ch.globaz.al.business.models.envoi.EnvoiItemSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiItemSimpleModelSearch;

/**
 * service de gestion de la persitance des données pour les envoi AF
 * 
 * @author dhi
 */
public interface EnvoiItemSimpleModelService extends JadeApplicationService {
    /**
     * Compte le nombre d'enregistrement disponibles pour un modèle de recherche donné
     * 
     * @param envoiItemSearch
     *            modèle de recherche renseigné
     * @return Le nombre d'enregistrement qui satisfont le modèle de recherche
     * @throws ALEnvoiItemException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence (data)
     */
    public int count(EnvoiItemSimpleModelSearch envoiItemSearch) throws ALEnvoiItemException, JadePersistenceException;

    /**
     * Création d'un enregistrement de type EnvoiItemSimpleModel en base
     * 
     * @param envoiItem
     *            Le modèle de données à créer, renseigné
     * @return Le modèle de données créé
     * @throws ALEnvoiItemException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence
     */
    public EnvoiItemSimpleModel create(EnvoiItemSimpleModel envoiItem) throws ALEnvoiItemException,
            JadePersistenceException;

    /**
     * Effacement d'un enregistrement de type EnvoiItemSimpleModel en base
     * 
     * @param envoiItem
     *            Le modèle de données à supprimer
     * @return Le modèle de données supprimé
     * @throws ALEnvoiItemException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence
     */
    public EnvoiItemSimpleModel delete(EnvoiItemSimpleModel envoiItem) throws ALEnvoiItemException,
            JadePersistenceException;

    /**
     * Lecture d'un enregistrement de type EnvoiItemSimpleModel depuis la base
     * 
     * @param idEnvoiItem
     *            L'id de l'enregistrement à lire
     * @return Le modèle de données renseigné
     * @throws ALEnvoiItemException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence
     */
    public EnvoiItemSimpleModel read(String idEnvoiItem) throws ALEnvoiItemException, JadePersistenceException;

    /**
     * Recherche d'éléments de type EnvoiItemSimpleModel en base
     * 
     * @param envoiItemSearch
     *            Le modèle de recherche de données renseignés
     * @return Le modèle de recherche complété
     * @throws ALEnvoiItemException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevée en cas de problèmes de persistence
     */
    public EnvoiItemSimpleModelSearch search(EnvoiItemSimpleModelSearch envoiItemSearch) throws ALEnvoiItemException,
            JadePersistenceException;

    /**
     * Mise à jour d'un enregistrement de type EnvoiItemSimpleModel en base
     * 
     * @param envoiItem
     *            Le modèle de données à mettre à jour
     * @return Le modèle de données mis à jour
     * @throws ALEnvoiItemException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence
     */
    public EnvoiItemSimpleModel update(EnvoiItemSimpleModel envoiItem) throws ALEnvoiItemException,
            JadePersistenceException;

}
