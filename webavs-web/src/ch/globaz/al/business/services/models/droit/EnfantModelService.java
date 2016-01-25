package ch.globaz.al.business.services.models.droit;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.droit.EnfantModel;
import ch.globaz.al.business.models.droit.EnfantSearchModel;

/**
 * service de gestion de la persistance des données d'un enfant
 * 
 * @author PTA
 */
public interface EnfantModelService extends JadeApplicationService {

    /**
     * Retourne le nombre d'enfants trouvés par le modèle de recherche
     * 
     * @param enfantSearch
     *            Modèle de recherche contenant les critères de sélection
     * @return le nombre d'enfant correspondant à la recherche
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public int count(EnfantSearchModel enfantSearch) throws JadePersistenceException, JadeApplicationException;

    /**
     * Enregistre l'enfant passé en paramètre en persistance
     * 
     * @param enfantModel
     *            Le modèle à enregistrer
     * @return EnfantModel le modèle enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantModel create(EnfantModel enfantModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Suppression d'un enfant en fonction du modèle passée en paramètre
     * 
     * @param enfantModel
     *            le modèle à supprimer
     * @return EnfantModel le modèle supprimé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantModel delete(EnfantModel enfantModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un modèle d'enfant
     * 
     * @param enfantModel
     *            le modèle à initialiser
     * 
     * @return le modèle initialisé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantModel initModel(EnfantModel enfantModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Lecture d'un enfant en fonction de son identifiant passé en paramètre
     * 
     * @param idEnfantModel
     *            Id de l'enfant à charger
     * 
     * @return EnfantModel L'enfant chargé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantModel read(String idEnfantModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Met à jour l'enfant passé en paramètre
     * 
     * @param enfantModel
     *            le modèle à mettre à jour
     * @return EnfantModel le modèle mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantModel update(EnfantModel enfantModel) throws JadeApplicationException, JadePersistenceException;
}
