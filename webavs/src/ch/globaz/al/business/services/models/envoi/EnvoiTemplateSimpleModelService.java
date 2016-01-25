/**
 * 
 */
package ch.globaz.al.business.services.models.envoi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.exceptions.model.envoi.ALEnvoiTemplateException;
import ch.globaz.al.business.models.envoi.EnvoiTemplateSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiTemplateSimpleModelSearch;

/**
 * service de gestion de la persitance des données pour les templates envoi AF
 * 
 * @author dhi
 */
public interface EnvoiTemplateSimpleModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement disponibles pour un modèle de recherche donné
     * 
     * @param envoiTemplateSearch
     *            modèle de recherche renseigné
     * @return Le nombre d'enregistrement qui satisfont le modèle de recherche
     * @throws ALEnvoiTemplateException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence (data)
     */
    public int count(EnvoiTemplateSimpleModelSearch envoiTemplateSearch) throws ALEnvoiTemplateException,
            JadePersistenceException;

    /**
     * Création d'un enregistrement de type EnvoiTemplateSimpleModel en base
     * 
     * @param envoiTemplate
     *            Le modèle de données à créer, renseigné
     * @return Le modèle de données créé
     * @throws ALEnvoiTemplateException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence
     */
    public EnvoiTemplateSimpleModel create(EnvoiTemplateSimpleModel envoiTemplate) throws ALEnvoiTemplateException,
            JadePersistenceException;

    /**
     * Effacement d'un enregistrement de type EnvoiTemplateSimpleModel en base
     * 
     * @param envoiTemplate
     *            Le modèle de données à supprimer
     * @return Le modèle de données supprimé
     * @throws ALEnvoiTemplateException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence
     */
    public EnvoiTemplateSimpleModel delete(EnvoiTemplateSimpleModel envoiTemplate) throws ALEnvoiTemplateException,
            JadePersistenceException;

    /**
     * Lecture d'un enregistrement de type EnvoiTemplateSimpleModel depuis la base
     * 
     * @param idEnvoiTemplate
     *            L'id de l'enregistrement à lire
     * @return Le modèle de données renseigné
     * @throws ALEnvoiTemplateException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence
     */
    public EnvoiTemplateSimpleModel read(String idEnvoiTemplate) throws ALEnvoiTemplateException,
            JadePersistenceException;

    /**
     * Recherche d'éléments de type EnvoiTemplateSimpleModel en base
     * 
     * @param envoiTemplateSearch
     *            Le modèle de recherche de données renseignés
     * @return Le modèle de recherche complété
     * @throws ALEnvoiTemplateException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevée en cas de problèmes de persistence
     */
    public EnvoiTemplateSimpleModelSearch search(EnvoiTemplateSimpleModelSearch envoiTemplateSearch)
            throws ALEnvoiTemplateException, JadePersistenceException;

    /**
     * Mise à jour d'un enregistrement de type EnvoiTemplateSimpleModel en base
     * 
     * @param envoiTemplate
     *            Le modèle de données à mettre à jour
     * @return Le modèle de données mis à jour
     * @throws ALEnvoiTemplateException
     *             Soulevé en cas de problèmes métier
     * @throws JadePersistenceException
     *             Soulevé en cas de problèmes de persistence
     */
    public EnvoiTemplateSimpleModel update(EnvoiTemplateSimpleModel envoiTemplate) throws ALEnvoiTemplateException,
            JadePersistenceException;

}
