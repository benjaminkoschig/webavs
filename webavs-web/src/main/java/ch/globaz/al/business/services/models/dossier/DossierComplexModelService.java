package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;

/**
 * Service de gestion de la persistance d'un dossier complexe
 * 
 * @author jts
 * @see ch.globaz.al.business.models.dossier.DossierComplexModel
 */
public interface DossierComplexModelService extends JadeApplicationService {
    /**
     * Cr�er un nouveau <code>dossierComplexModel</code> sur la base de celui pass� en param�tre
     * 
     * @param dossierComplexModel
     *            la r�f�rence du clone
     * @return le dossierComplexModel clon�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierComplexModel
     */
    public DossierComplexModel clone(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Enregistre <code>dossierComplexModel</code> en persistance
     * 
     * @param dossierComplexModel
     *            Dossier � enregistrer en persistance
     * @return Le mod�le ajout� en persistance
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierComplexModel
     */
    public DossierComplexModel create(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime <code>dossierComplexModel</code> de la persistance
     * 
     * @param dossierComplexModel
     *            Dossier � supprimer de la persistance
     * @return Le dossier supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierComplexModel
     */
    public DossierComplexModel delete(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise le mod�le
     * 
     * @param dossierComplexModel
     *            Le mod�le � initialiser
     * @return Le mod�le initialiser
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierComplexModel
     */
    public DossierComplexModel initModel(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * R�cup�re les donn�es du dossier correspondant � <code>idDossier</code>
     * 
     * @param idDossier
     *            Id du dossier � charger
     * @return Le mod�le du dossier charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierComplexModel
     */
    public DossierComplexModel read(String idDossier) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue une recherche selon le mod�le de recherche pass� en param�tre
     * 
     * @param searchModel
     *            mod�le de recherche de dossier
     * @return R�sultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexSearchModel search(DossierComplexSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Effectue une recherche selon le mod�le de recherche pass� en param�tre, les valeurs de codes syst�me sont
     * remplac�es par leur libell�
     * 
     * @param searchModel
     *            mod�le de recherche de lien dossier
     * @return R�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexSearchModel searchWithCsTranslated(DossierComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Mets � jour les donn�es de <code>dossierComplexModel</code> en persistance
     * 
     * @param dossierComplexModel
     *            Dossier � mettre � jour
     * @return Le dossier mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierComplexModel
     */
    public DossierComplexModel update(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException;
}
