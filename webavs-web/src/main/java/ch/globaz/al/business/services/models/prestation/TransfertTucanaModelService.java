package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.TransfertTucanaModel;
import ch.globaz.al.business.models.prestation.TransfertTucanaSearchModel;

/**
 * service de gestion des données inhérentes à TransfertTucana dans Prestation
 * 
 * @author PTA
 * 
 */
public interface TransfertTucanaModelService extends JadeApplicationService {

    /**
     * Retourne le nombre d'enregistrements Tucana trouvés par le modèle de recherche
     * 
     * @param search
     *            modèle contenant les critères de recherche
     * @return nombre d'enregistrement correspondant à la recherche
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public int count(TransfertTucanaSearchModel search) throws JadeApplicationException, JadePersistenceException;

    /**
     * Création d'un TransfertTucana selon le modèle passé en paramètre
     * 
     * @param transTucanaModel
     *            modèle à enregistrer
     * @return le modèle enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TransfertTucanaModel create(TransfertTucanaModel transTucanaModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Suppression d'un TransfertTucana selon le modèle passé en paramètre
     * 
     * @param transTucanaModel
     *            le modèle à supprimer
     * @return le modèle supprimé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TransfertTucanaModel delete(TransfertTucanaModel transTucanaModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture d'un TransfertTucana selon l'id passé en paramètre
     * 
     * @param idtransTucanaModel
     *            id du modèle à charger
     * @return le modèle chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TransfertTucanaModel read(String idtransTucanaModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Effectue une recherche selon le modèle de recherche passé en paramètre
     * 
     * @param searchModel
     *            modèle de recherche de transfert Tucana
     * @return Résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TransfertTucanaSearchModel search(TransfertTucanaSearchModel search) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Mise à jour d'un TransfertTucana selon le modèle passé en paramètre
     * 
     * @param transTucanaModel
     *            le modèle à mettre à jour
     * @return le modèle mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TransfertTucanaModel update(TransfertTucanaModel transTucanaModel) throws JadeApplicationException,
            JadePersistenceException;
}
