package ch.globaz.al.business.services.models.droit;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.models.droit.DroitModel;

/**
 * Service de gestion de la persistance des droits
 * 
 * @author jts
 * @see ch.globaz.al.business.models.droit.DroitComplexModel
 */
public interface DroitComplexModelService extends JadeApplicationService {

    /**
     * Cr�er un nouveau <code>droitComplexModel</code> sur la base de celui pass� en param�tre
     * 
     * @param droitComplexModel
     *            la r�f�rence du clone
     * @param idDossier
     *            le dossier auquel le droit clon� appartiendra
     * @return le droitComplexModel clon�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.droit.DroitComplexModel
     */
    public DroitComplexModel clone(DroitComplexModel droitComplexModel, String idDossier)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Cr�e une copie du mod�le pass� en param�tre. Seul le {@link DroitModel} contenu dans mod�le complexe est
     * r�ellement copi�. Le mod�le de l'enfant est une r�f�rence du mod�le d'origine. Le tiers b�n�ficiaire n'est pas
     * copi�
     * 
     * @param droit
     *            droit � copier
     * @return la copie du droit
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public DroitComplexModel copie(DroitComplexModel droit) throws JadeApplicationException;

    /**
     * Enregistre <code>droitComplexModel</code> en persistance
     * 
     * @param droitComplexModel
     *            Droit � enregistrer en persistance
     * @return Le droit enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.droit.DroitComplexModel
     */
    public DroitComplexModel create(DroitComplexModel droitComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime <code>droitComplexModel</code> de la persistance
     * 
     * @param droitComplexModel
     *            Droit � supprimer de la persistance
     * @return Le droit supprim�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.droit.DroitComplexModel
     */
    public DroitComplexModel delete(DroitComplexModel droitComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise <code>droitComplexModel</code>
     * 
     * @param droitComplexModel
     *            Le droit � initialiser
     * @return Le droit initialis�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.droit.DroitComplexModel
     */
    public DroitComplexModel initModel(DroitComplexModel droitComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * R�cup�re les donn�es correspondant au droit <code>idDroit</code>
     * 
     * @param idDroit
     *            Id du droit � charger
     * @return Le mod�le du droit charg�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.droit.DroitComplexModel
     */
    public DroitComplexModel read(String idDroit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche les droits correspondant au mod�le de recherche <code>droitComplexComplexSearchModel</code>
     * 
     * @param droitComplexSearchModel
     *            Mod�le de recherche de droit contenant les crit�res de recherche souhait�s
     * @return r�sultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.droit.DroitComplexModel
     */
    public DroitComplexSearchModel search(DroitComplexSearchModel droitComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Met � jour <code>droitComplexModel</code> de la persistance
     * 
     * @param droitComplexModel
     *            Le droit � mettre � jour
     * @return Le droit mis � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.droit.DroitComplexModel
     */
    public DroitComplexModel update(DroitComplexModel droitComplexModel) throws JadeApplicationException,
            JadePersistenceException;

}
