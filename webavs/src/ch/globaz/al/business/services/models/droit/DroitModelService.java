package ch.globaz.al.business.services.models.droit;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.business.models.droit.DroitSearchModel;

/**
 * service de gestion de la persitance des donn�es li�es au droit
 * 
 * @author PTA
 */
public interface DroitModelService extends JadeApplicationService {

    /**
     * Cr�er un nouveau <code>droitModel</code> sur la base de celui pass� en param�tre
     * 
     * /!\ ATTENTION /!\ : NE PAS UTILISER DANS UN AUTRE CADRE QUE LA COPIE DE DOSSIER ACTUELLEMENT EN PLACE, CETTE
     * METHODE NE CREE PAS UN VERITABLE CLONE, IL Y A UN RISQUE DE
     * MODIFICATION DU MODELE DE REFERENCE, UTILISER {@link DroitModelService#copie(DroitModel)}
     * 
     * @param droitModel
     *            - la r�f�rence du clone
     * @param idDossier
     *            - le dossier auquel le droit appartient
     * @return - le droitModel clon�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @deprecated NE PAS UTILISER CETTE METHODE NE CREE PAS UN VERITABLE CLONE, IL Y A UN RISQUE DE
     *             MODIFICATION DU MODELE PAR REFERENCE, UTILISER {@link DroitModelService#copie(DroitModel)}
     */
    @Deprecated
    public DroitModel clone(DroitModel droitModel, String idDossier) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Copie les tous les champs d'un droit except� les champs spy et l'id du droit
     * 
     * @param droitModel
     *            correspond au droit � copier
     * @return droitModel copi�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public DroitModel copie(DroitModel droitModel) throws JadeApplicationException;

    /**
     * Compte le nombre d'enregistrement retourn�s par le mod�le de recherche pass� en param�tre
     * 
     * @param droitSearch
     *            le mod�le de recherche contenant les crit�res de s�lection
     * @return le nombre de droits retourn�s par le mod�le de recherche
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */

    public int count(DroitSearchModel droitSearch) throws JadePersistenceException, JadeApplicationException;

    /**
     * Cr�ation d'un droit en persistance
     * 
     * @param droitModel
     *            le droit � enregistrer
     * @return DroitModel le droit enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DroitModel create(DroitModel droitModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Suppression d'un droit selon le mod�le pass� en param�tre
     * 
     * @param droitModel
     *            le mod�le � supprimer
     * @return DroitModel le mod�le supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DroitModel delete(DroitModel droitModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche des droits actifs par rapport � un dossier et une date sp�cifique
     * 
     * @param idDossier
     *            id du dossier dans lequel les droits devront �tre recherch�s
     * @param date
     *            date � laquelle les droits sont contr�l�s afin de d�finir si ils sont actifs
     * @return List<String> des droits actifs
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public List<DroitModel> findActifs(String idDossier, String date)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException, JadePersistenceException;

    /**
     * Initialisation d'un droit
     * 
     * @param droitModel
     *            le droit � initialiser
     * @return le droit initialis�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DroitModel initModel(DroitModel droitModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Lecture d'un droit en fonction de son id pass� en param�tre
     * 
     * @param idDroitModel
     *            id du droit � charger
     * @return DroitModel le droit charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DroitModel read(String idDroitModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche de droits
     * 
     * @param droitSearchModel
     *            le mod�le de recherche
     * @return les r�sultats charg�s dans le mod�le de recherche DroitSearchModel
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DroitSearchModel search(DroitSearchModel droitSearchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Mise � jour d'un droit en persitance
     * 
     * @param droitModel
     *            le mod�le � mettre � jour
     * @return DroitModel le mod�le mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DroitModel update(DroitModel droitModel) throws JadeApplicationException, JadePersistenceException;
}
