package ch.globaz.al.business.services.importation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.models.dossier.CommentaireModel;
import ch.globaz.al.business.models.dossier.CopieComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;

/**
 * Service d'importation des donn�es de dossier
 * 
 * @author jts
 * 
 */
public interface ImportationDossierService extends JadeApplicationService {
    /**
     * Importe les commentaires contenus dans <code>commentaires</code> et les lie au <code>dossier</code>
     * 
     * @param dossier
     *            mod�le du dossier auquel lier les commentaires
     * @param commentaires
     *            liste des commentaires � importer
     * @return dossier
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierModel importCommentaires(DossierModel dossier, ArrayList<CommentaireModel> commentaires)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Importation des copies d'avis d'�ch�ance
     * 
     * @param dossier
     *            mod�le du dossier auquel il faut li� les copies avis �ch�ances
     * 
     * @return Le dossier pass� en param�tre
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierModel importCopiesAvisEcheances(DossierModel dossier) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Importe les copie contenus dans <code>copies</code> et les lie au <code>dossier</code>
     * 
     * @param dossier
     *            mod�le du dossier auquel lier les copies
     * @param copies
     *            liste des copies � importer
     * 
     * @return Le dossier pass� en param�tre
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierModel importCopiesDecision(DossierModel dossier, ArrayList<CopieComplexModel> copies)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * V�rifie si le dossier <code>dossierImport</code> existe d�j� en persistance et le cr�e si n�cessaire.
     * 
     * @param dossierImport
     *            dossier Import
     * @param idAllocataire
     *            identifiant de l'allocataire
     * @param numeroDossier
     *            num�ro du dossier
     * @return le dossier import�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierModel importDossier(DossierModel dossierImport, String idAllocataire, String numeroDossier)
            throws JadeApplicationException, JadePersistenceException;

}
