package ch.globaz.al.business.services.importation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.models.dossier.CommentaireModel;
import ch.globaz.al.business.models.dossier.CopieComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;

/**
 * Service d'importation des données de dossier
 * 
 * @author jts
 * 
 */
public interface ImportationDossierService extends JadeApplicationService {
    /**
     * Importe les commentaires contenus dans <code>commentaires</code> et les lie au <code>dossier</code>
     * 
     * @param dossier
     *            modèle du dossier auquel lier les commentaires
     * @param commentaires
     *            liste des commentaires à importer
     * @return dossier
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierModel importCommentaires(DossierModel dossier, ArrayList<CommentaireModel> commentaires)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Importation des copies d'avis d'échéance
     * 
     * @param dossier
     *            modèle du dossier auquel il faut lié les copies avis échéances
     * 
     * @return Le dossier passé en paramètre
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierModel importCopiesAvisEcheances(DossierModel dossier) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Importe les copie contenus dans <code>copies</code> et les lie au <code>dossier</code>
     * 
     * @param dossier
     *            modèle du dossier auquel lier les copies
     * @param copies
     *            liste des copies à importer
     * 
     * @return Le dossier passé en paramètre
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierModel importCopiesDecision(DossierModel dossier, ArrayList<CopieComplexModel> copies)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Vérifie si le dossier <code>dossierImport</code> existe déjà en persistance et le crée si nécessaire.
     * 
     * @param dossierImport
     *            dossier Import
     * @param idAllocataire
     *            identifiant de l'allocataire
     * @param numeroDossier
     *            numéro du dossier
     * @return le dossier importé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierModel importDossier(DossierModel dossierImport, String idAllocataire, String numeroDossier)
            throws JadeApplicationException, JadePersistenceException;

}
