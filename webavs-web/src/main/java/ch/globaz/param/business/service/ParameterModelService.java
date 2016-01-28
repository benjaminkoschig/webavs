package ch.globaz.param.business.service;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.models.ParameterSearchModel;

/**
 * Service de gestion de persistance des donn�es des param�tres
 * 
 * @author GMO
 * 
 */
public interface ParameterModelService extends JadeApplicationService {
    /**
     * Enregistre <code>parameterModel</code> en persistance
     * 
     * @param parameterModel
     *            param�tre � enregistrer en persistance
     * 
     * @return ParameterModel Le param�tre enregistr�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.param.business.models.ParameterModel
     */
    public ParameterModel create(ParameterModel parameterModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime <code>parameterModel</code> de la persistance
     * 
     * @param parameterModel
     *            Param�tre � supprimer de la persistance
     * 
     * @return ParameterModel Le param�tre supprim�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.param.business.models.ParameterModel
     */
    public ParameterModel delete(ParameterModel parameterModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Permet de r�cup�rer un param�tre en sp�cifiant son nom
     * 
     * @param application
     *            - l'application � laquelle le param�tre est destin�
     * @param name
     *            - le nom du param�tre
     * @param date
     *            - la date de validit� du param�tre
     * @return le mod�le repr�sentant le param�tre trouv�
     * @see ch.globaz.param.business.models.ParameterModel
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ParameterModel getParameterByName(String application, String name, String date)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Permet de r�cup�rer un param�tre en sp�cifiant son nom
     * 
     * @param application
     *            - l'application � laquelle le param�tre est destin�
     * @param name
     *            - le nom du param�tre
     * @param date
     *            - la date de validit� du param�tre
     * @param plageValue
     *            - la valeur qui doit se trouver dans la plage du param�tre
     * @return le mod�le repr�sentant le param�tre trouv�
     * @see ch.globaz.param.business.models.ParameterModel
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ParameterModel getParameterByName(String application, String name, String date, String plageValue)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * R�cup�re les donn�es du param�tre correspondant � <code>idParameterModel</code>
     * 
     * @param idParameterModel
     *            Id du param�tre � charger
     * 
     * @return ParameterModel Le mod�le du param�tre charg�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.param.business.models.ParameterModel
     */
    public ParameterModel read(String idParameterModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche des param�tres
     * 
     * @param parameterSearchModel
     *            Le mod�le de recherche
     * @return Le mod�le de recherche avec les r�sultats
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ParameterSearchModel search(ParameterSearchModel parameterSearchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Met � jour <code>parameterModel</code> en persistance
     * 
     * @param parameterModel
     *            Dossier � mettre � jour
     * 
     * @return ParameterModel Le param�tre mis � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.param.business.models.ParameterModel
     */
    public ParameterModel update(ParameterModel parameterModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Met � jour <code>parameterModel</code> selon les param�tres pass�s
     * 
     * @param idParam
     *            id du param�tre � mettre � jour
     * @param startValidity
     *            nouvelle valeur du champ d�but de validit�
     * @param alphaValue
     *            nouvelle valeur du champ alphavalue
     * @param numValue
     *            nouvelle valeur du champ numvalue
     * @return ParameterModel Le param�tre mis � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.param.business.models.ParameterModel
     */
    public ParameterModel updateValidityAndValues(String idParam, String startValidity, String alphaValue,
            String numValue) throws JadeApplicationException, JadePersistenceException;

}
