package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.dossier.DossierSearchModel;

/**
 * Service de gestion de la persistance des donn�es des dossiers
 * 
 * @author jts
 */
public interface DossierModelService extends JadeApplicationService {

    /**
     * Cr�er un nouveau <code>dossierModel</code> sur la base de celui pass� en param�tre
     * 
     * @param dossierModel
     *            la r�f�rence du clone
     * @return le dossierModel clon�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierModel
     */
    public DossierModel clone(DossierModel dossierModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Enregistre <code>dossierModel</code> en persistance
     * 
     * @param dossierModel
     *            Dossier � enregistrer en persistance
     * 
     * @return DossierModel Le dossier enregistr�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierModel
     */
    public DossierModel create(DossierModel dossierModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime <code>dossierModel</code> de la persistance
     * 
     * @param dossierModel
     *            Dossier � supprimer de la persistance
     * 
     * @return DossierModel Le dossier supprim�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierModel
     */
    public DossierModel delete(DossierModel dossierModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un nouveau mod�le de dossier
     * 
     * @param dossierModel
     *            Le dossier � initialiser
     * @return Le dossier initialis�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @see ch.globaz.al.business.models.dossier.DossierModel
     */
    public DossierModel initModel(DossierModel dossierModel) throws JadeApplicationException;

    /**
     * R�cup�re les donn�es du dossier correspondant � <code>idDossierModel</code>
     * 
     * @param idDossierModel
     *            Id du dossier � charger
     * 
     * @return DossierModel Le mod�le du dossier charg�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierModel
     */
    public DossierModel read(String idDossierModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue une recherche selon le mod�le de recherche pass� en param�tre
     * 
     * @param searchModel
     *            mod�le de recherche de dossier
     * @return R�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierSearchModel search(DossierSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Met � jour <code>dossierModel</code> en persistance
     * 
     * @param dossierModel
     *            Dossier � mettre � jour
     * 
     * @return DossierModel Le dossier mis � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.dossier.DossierModel
     */
    public DossierModel update(DossierModel dossierModel) throws JadeApplicationException, JadePersistenceException;

}