package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.DetailPrestationSearchModel;

/**
 * Service de gestion de persistance des donn�es des detailPrestation de prestation
 * 
 * @author PTA
 */
public interface DetailPrestationModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retourn�s par le mod�le de recherche pass� en param�tre
     * 
     * @param detailPrestSearch
     *            mod�le contenant les crit�res de recherche
     * @return nombre d'enregistrement correspondant � la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int count(DetailPrestationSearchModel detailPrestSearch) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * 
     * Cr�ation d'un d�tail de prestation selon le mod�le pass� en param�tre
     * 
     * @param detailPrestationModel
     *            mod�le � enregistrer
     * @return le mod�le enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DetailPrestationModel create(DetailPrestationModel detailPrestationModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Suppression d'un d�tail de prestation selon le mod�le pass� en param�tre
     * 
     * @param detailPrestationModel
     *            le mod�le � supprimer
     * @return le mod�le supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DetailPrestationModel delete(DetailPrestationModel detailPrestationModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime les d�tails li�s � l'en-t�te dont l'id est pass� en param�tre
     * 
     * @param idEntete
     *            id de l'en-t�te
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void deleteForIdEntete(String idEntete) throws JadePersistenceException, JadeApplicationException;

    /**
     * Lecture d'un d�tail de prestation selon son id pass� en param�tre
     * 
     * @param idDetailPrestationModel
     *            id du mod�le � charger
     * @return le mod�le charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DetailPrestationModel read(String idDetailPrestationModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche les d�tail de prestation correspondant aux crit�res d�finis dans le mod�le de recherche
     * 
     * @param searchModel
     *            Le mod�le de recherche
     * @return Le mod�le de recherche contenant les r�sultats
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DetailPrestationSearchModel search(DetailPrestationSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Mise d'un d�tail de prestation selon le mod�le pass� en param�tre
     * 
     * @param detailPrestationModel
     *            le mod�le � mettre � jour
     * @return le mod�le mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DetailPrestationModel update(DetailPrestationModel detailPrestationModel) throws JadeApplicationException,
            JadePersistenceException;
}
