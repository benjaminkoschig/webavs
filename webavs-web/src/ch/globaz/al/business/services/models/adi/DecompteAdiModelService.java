package ch.globaz.al.business.services.models.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.models.adi.DecompteAdiSearchModel;

/**
 * Service de gestion de persistance des données de ADI - Décompte
 * 
 * @author PTA
 */
public interface DecompteAdiModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retournés par le modèle de recherche passé en paramètre
     * 
     * @param decompteAdiSearch
     *            modèle contenant les critères de recherche
     * @return nombre d'enregistrement correspondant à la recherche
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public int count(DecompteAdiSearchModel decompteAdiSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * méthode de création d'un décompte ADI
     * 
     * @param decompteAdiModel
     *            modèle à enregistrer
     * @return le modèle enregistré
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DecompteAdiModel create(DecompteAdiModel decompteAdiModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * méthode de suppression d'un décompte ADI
     * 
     * @param decompteAdiModel
     *            le modèle à supprimer
     * @return le modèle supprimé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DecompteAdiModel delete(DecompteAdiModel decompteAdiModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime les décomptes lié à l'en-tête passée en paramètre
     * 
     * @param idEntete
     *            id de l'en-tête de prestation pour laquelle supprimer les décomptes
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @deprecated plus de suppression selon l'id entête
     */
    @Deprecated
    public void deleteForIdEntete(String idEntete) throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialisation d'un décompte
     * 
     * @param decompte
     *            le décompte à initialiser
     * @return le décompte initialisé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DecompteAdiModel initModel(DecompteAdiModel decompte) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Méthode de lecture d'un décompte ADI
     * 
     * @param idDecompteAdiModel
     *            id du modèle à charger
     * @return le modèle chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DecompteAdiModel read(String idDecompteAdiModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche les décompte ADI selon les critères du modèle de recherche
     * 
     * @param searchModel
     *            le modèle de recherche
     * @return le modèle de recherche avec les résultats
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DecompteAdiSearchModel search(DecompteAdiSearchModel searchModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * public DecompteAdiSearchModel search(DecompteAdiSearchModel searchModel) throws JadePersistenceException,
     * JadeApplicationException;
     * 
     * /** Méthode de mis à jour d'un décompte ADI
     * 
     * @param decompteAdiModel
     *            le modèle à mettre à jour
     * @return le modèle mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DecompteAdiModel update(DecompteAdiModel decompteAdiModel) throws JadeApplicationException,
            JadePersistenceException;

}
