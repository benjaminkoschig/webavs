/**
 * 
 */
package ch.globaz.al.business.services.models.envoi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModel;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModelSearch;

/**
 * @author dhi
 * 
 */
public interface EnvoiTemplateComplexModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'élément correspondant au modèle de recherche
     * 
     * @param envoiTemplateSearch
     *            Le modèle de recherche renseigné
     * @return Le modèle de recherche renseigné et complété
     * @throws JadePersistenceException
     *             Levée en cas de problème de persistence des données
     * @throws JadeApplicationException
     *             Levée en cas de problème métier
     */
    public int count(EnvoiTemplateComplexModelSearch envoiTemplateSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Création d'un élément de type EnvoiTemplateComplexModel. Touche FormuleList et EnvoiTemplateSimpleModel
     * 
     * @param envoiTemplate
     *            Le modèle de données renseigné, à créer
     * @return Le modèle de données EnvoiTemplateComplexModel créé
     * @throws JadeApplicationException
     *             Levée en cas de problème métier
     * @throws JadePersistenceException
     *             Levée en cas de problème de persistence
     */
    public EnvoiTemplateComplexModel create(EnvoiTemplateComplexModel envoiTemplate) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Effacement de type EnvoiTemplateComplexModel en db
     * 
     * @param envoiTemplate
     *            Le modèle de données renseigné, à supprimer
     * @return Le modèle de données supprimé
     * @throws JadeApplicationException
     *             Levée en cas de problème métier
     * @throws JadePersistenceException
     *             Levée en cas de problème de persistence
     */
    public EnvoiTemplateComplexModel delete(EnvoiTemplateComplexModel envoiTemplate) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture d'un enregistrement de type EnvoiTemplateComplexModel
     * 
     * @param idEnvoiTemplateComplexModel
     *            L'id de l'enregistrement à lire
     * @return Le modèle de données renseigné
     * @throws JadeApplicationException
     *             Levée en cas de problème métier
     * @throws JadePersistenceException
     *             Levée en cas de problème de persistence
     */
    public EnvoiTemplateComplexModel read(String idEnvoiTemplateComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche de données de type EnvoiTemplateComplexModel en fonction d'un modèle de recherche
     * 
     * @param envoiTemplateSearch
     *            Le modèle de recherche renseigné
     * @return Le modèle de recherche renseigné et complété
     * @throws JadeApplicationException
     *             Levée en cas de problème métier
     * @throws JadePersistenceException
     *             Levée en cas de problème de persistence
     */
    public EnvoiTemplateComplexModelSearch search(EnvoiTemplateComplexModelSearch envoiTemplateSearch)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Mise à jour d'un enregistrement de type EnvoiTemplateComplexModel
     * 
     * @param envoiTemplate
     *            Le modèle de données à jour, à inscrire en db
     * @return Le modèle de données mis à jour
     * @throws JadeApplicationException
     *             Levée en cas de problème métier
     * @throws JadePersistenceException
     *             Levée en cas de problème de persistence
     */
    public EnvoiTemplateComplexModel update(EnvoiTemplateComplexModel envoiTemplate) throws JadeApplicationException,
            JadePersistenceException;

}
