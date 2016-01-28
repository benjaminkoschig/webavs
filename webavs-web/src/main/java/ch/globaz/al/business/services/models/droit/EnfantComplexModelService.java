package ch.globaz.al.business.services.models.droit;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.droit.EnfantComplexModel;
import ch.globaz.al.business.models.droit.EnfantComplexSearchModel;

/**
 * Service de gestion de la persistance des données lié au modèle complexe enfant
 * 
 * @author PTA
 * @see ch.globaz.al.business.models.droit.EnfantComplexModel
 * @see ch.globaz.al.business.models.droit.EnfantComplexSearchModel
 */
public interface EnfantComplexModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retournés par le modèle de recherche passé en paramètre
     * 
     * @param enfantSearchComplex
     *            Modèle contenant les critères de sélection
     * 
     * @return Nombre d'enfant correspondant au modèle de recherche
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int count(EnfantComplexSearchModel enfantSearchComplex) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Enregistre l'enfant passé en paramètre en persistance
     * 
     * @param enfantComplexModel
     *            Modèle à enregistrer
     * 
     * @return Modèle enregistré
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantComplexModel create(EnfantComplexModel enfantComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime l'enfant passé en paramètre
     * 
     * @param enfantComplexModel
     *            Modèle à supprimer
     * 
     * @return Modèle supprimé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantComplexModel delete(EnfantComplexModel enfantComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initilise un nouveau modèle d'enfant
     * 
     * @param enfantComplexModel
     *            le modèle à initialiser
     * @return le modèle initialisé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantComplexModel initModel(EnfantComplexModel enfantComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Charge l'enfant correspondant à l'id passé en paramètre
     * 
     * @param idEnfantComplexModel
     *            id de l'enfant à charger
     * @return le modèle chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantComplexModel read(String idEnfantComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche d'un enfant selon le modèle de recherche passé en paramètre
     * 
     * @param enfantComplexSearchModel
     *            modèle contenant les critères de recherche
     * @return Résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantComplexSearchModel search(EnfantComplexSearchModel enfantComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Met à jour <code>enfantComplexModel</code> en persistance
     * 
     * @param enfantComplexModel
     *            Modèle à mettre à jour
     * @return modèle mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantComplexModel update(EnfantComplexModel enfantComplexModel) throws JadeApplicationException,
            JadePersistenceException;

}
