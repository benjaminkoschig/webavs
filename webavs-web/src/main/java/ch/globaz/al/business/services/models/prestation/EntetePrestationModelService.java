package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.List;

/**
 * Service de gestion de persistance des donn�es des en-t�te de prestation
 * 
 * @author PTA
 */
public interface EntetePrestationModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retourn�s par le mod�le de recherche pass� en param�tre
     * 
     * @param enteteSearch
     *            Mod�le contenant les crit�res de recherche
     * @return Nombre d'en-t�tes correspondant aux crit�res de recherche du mod�le
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public int count(EntetePrestationSearchModel enteteSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Cr�ation d'un ent�te de prestation selon le mod�le pass�e en param�tre
     * 
     * @param entetePrestModel
     *            Le mod�le � enregistrer
     * @return Le mod�le enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EntetePrestationModel create(EntetePrestationModel entetePrestModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Suppression d'une ent�te de prestation selon le mod�le pass� en param�tre
     * 
     * @param entetePrestModel
     *            Le mod�le � supprimer
     * @return Le mod�le supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EntetePrestationModel delete(EntetePrestationModel entetePrestModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime les prestations li�es � la r�cap dont l'id est pass� en param La r�cap elle-m�me ne sera pas supprim�e,
     * puisque ce service est appel� depuis le service delete de r�cap
     * 
     * @param idRecap
     *            id de la r�cap
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void deleteForIdRecap(String idRecap) throws JadePersistenceException, JadeApplicationException;

    /**
     * Lecture d'une ent�te de prestation selon son id pass� en param�tre
     * 
     * @param idEntetePrestModel
     *            Id de l'en-t�te � charger
     * @return EntetePrestaonModel
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EntetePrestationModel read(String idEntetePrestModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche les ent�tes de prestation r�pondant aux crit�res d�finis dans le mod�le de recherche
     * 
     * @param entetePrestationSearchModel
     *            Le mod�le de recherche
     * @return Le mod�le de recherche contenant les r�sultats
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EntetePrestationSearchModel search(EntetePrestationSearchModel entetePrestationSearchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Mise � jour d'une ent�te de prestation selon le mod�le pass� en param�tre
     * 
     * @param entetePrestModel
     *            Le mod�le � mettre � jour
     * @return EntetePrestationModel Le mod�le mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EntetePrestationModel update(EntetePrestationModel entetePrestModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * M�tohde permettant de rechercher les prestations comptabilis�es d'un dossier pour une p�riode donn�e
     *
     * @param idDossier L'ID du dossier contenant les prestations
     * @param periode La p�riode de la prestation (MM.yyyy)
     * @return Le mod�le de recherche contentant les prestations d'une p�riode donn� d'un dossier
     */
    public List<EntetePrestationModel> searchEntetesPrestationsComptabilisees(String idDossier, String periode) throws JadeApplicationException, JadePersistenceException;
}
