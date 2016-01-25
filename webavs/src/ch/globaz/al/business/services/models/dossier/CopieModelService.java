package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.CopieModel;
import ch.globaz.al.business.models.dossier.CopieSearchModel;

/**
 * Service de gestion de la persistance des données des copies de dossier
 * 
 * @author jts
 */
public interface CopieModelService extends JadeApplicationService {

    /**
     * Effectue une copie du modèle passé en paramètre
     * 
     * @param copieModel
     *            modèle à copier
     * @return copieMoel
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieModel copy(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Compte le nombre d'enregistrement retournés par le modèle de recherche passé en paramètre
     * 
     * @param copieSearch
     *            Modèle de recherche contenant les critères
     * @return nombre de copies correspondant aux critères de recherche
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * 
     * @see ch.globaz.al.business.models.dossier.CopieModel
     */
    public int count(CopieSearchModel copieSearch) throws JadePersistenceException, JadeApplicationException;

    /**
     * Enregistre <code>copieModel</code> en persistance
     * 
     * @param copieModel
     *            Copie à enregistrer
     * @return CopieModel Copie enregistrée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.dossier.CopieModel
     */
    public CopieModel create(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime <code>copieModel</code> de la persistance
     * 
     * @param copieModel
     *            Copie à supprimer
     * @return CopieModel Copie supprimée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.dossier.CopieModel
     */
    public CopieModel delete(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise une copie
     * 
     * @param copieModel
     *            Copie à initialiser
     * @return Copie initialiser
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.dossier.CopieModel
     */
    public CopieModel initModel(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Récupère les données de la copie correspondant à <code>idCopie</code>
     * 
     * @param idCopie
     *            Id de la copie à charger
     * @return CopieModel Copie chargée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.dossier.CopieModel
     */
    public CopieModel read(String idCopie) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche
     * 
     * @param copieSearchModel
     *            modèle pour la recherche
     * @return modèle de recherche de copie
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieSearchModel search(CopieSearchModel copieSearchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Met à jour les données de <code>copieModel</code> en persistance
     * 
     * @param copieModel
     *            Copie à mettre à jour
     * @return CopieModel copie mise à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.dossier.CopieModel
     */
    public CopieModel update(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException;

}