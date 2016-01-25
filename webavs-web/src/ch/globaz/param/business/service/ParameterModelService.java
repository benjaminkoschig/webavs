package ch.globaz.param.business.service;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.models.ParameterSearchModel;

/**
 * Service de gestion de persistance des données des paramètres
 * 
 * @author GMO
 * 
 */
public interface ParameterModelService extends JadeApplicationService {
    /**
     * Enregistre <code>parameterModel</code> en persistance
     * 
     * @param parameterModel
     *            paramètre à enregistrer en persistance
     * 
     * @return ParameterModel Le paramètre enregistré
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.param.business.models.ParameterModel
     */
    public ParameterModel create(ParameterModel parameterModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime <code>parameterModel</code> de la persistance
     * 
     * @param parameterModel
     *            Paramètre à supprimer de la persistance
     * 
     * @return ParameterModel Le paramètre supprimé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.param.business.models.ParameterModel
     */
    public ParameterModel delete(ParameterModel parameterModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Permet de récupérer un paramètre en spécifiant son nom
     * 
     * @param application
     *            - l'application à laquelle le paramètre est destiné
     * @param name
     *            - le nom du paramètre
     * @param date
     *            - la date de validité du paramètre
     * @return le modèle représentant le paramètre trouvé
     * @see ch.globaz.param.business.models.ParameterModel
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ParameterModel getParameterByName(String application, String name, String date)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Permet de récupérer un paramètre en spécifiant son nom
     * 
     * @param application
     *            - l'application à laquelle le paramètre est destiné
     * @param name
     *            - le nom du paramètre
     * @param date
     *            - la date de validité du paramètre
     * @param plageValue
     *            - la valeur qui doit se trouver dans la plage du paramètre
     * @return le modèle représentant le paramètre trouvé
     * @see ch.globaz.param.business.models.ParameterModel
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ParameterModel getParameterByName(String application, String name, String date, String plageValue)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Récupère les données du paramètre correspondant à <code>idParameterModel</code>
     * 
     * @param idParameterModel
     *            Id du paramètre à charger
     * 
     * @return ParameterModel Le modèle du paramètre chargé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.param.business.models.ParameterModel
     */
    public ParameterModel read(String idParameterModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche des paramètres
     * 
     * @param parameterSearchModel
     *            Le modèle de recherche
     * @return Le modèle de recherche avec les résultats
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ParameterSearchModel search(ParameterSearchModel parameterSearchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Met à jour <code>parameterModel</code> en persistance
     * 
     * @param parameterModel
     *            Dossier à mettre à jour
     * 
     * @return ParameterModel Le paramètre mis à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.param.business.models.ParameterModel
     */
    public ParameterModel update(ParameterModel parameterModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Met à jour <code>parameterModel</code> selon les paramètres passés
     * 
     * @param idParam
     *            id du paramètre à mettre à jour
     * @param startValidity
     *            nouvelle valeur du champ début de validité
     * @param alphaValue
     *            nouvelle valeur du champ alphavalue
     * @param numValue
     *            nouvelle valeur du champ numvalue
     * @return ParameterModel Le paramètre mis à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.param.business.models.ParameterModel
     */
    public ParameterModel updateValidityAndValues(String idParam, String startValidity, String alphaValue,
            String numValue) throws JadeApplicationException, JadePersistenceException;

}
