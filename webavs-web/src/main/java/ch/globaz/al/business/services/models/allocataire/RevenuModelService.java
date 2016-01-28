package ch.globaz.al.business.services.models.allocataire;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.allocataire.RevenuModel;
import ch.globaz.al.business.models.allocataire.RevenuSearchModel;

/**
 * Service de gestion de persistance des donn�es des revenu de allocataire
 * 
 * @author PTA
 */
public interface RevenuModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retourn�s par le mod�le de recherche pass� en param�tre
     * 
     * @param revenuSearch
     *            Le mod�le de recherche de revenus
     * @return Le nombre de revenus trouv�s
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int count(RevenuSearchModel revenuSearch) throws JadePersistenceException;

    /**
     * Cr�ation d'un revenu selon le mod�le pass� en param�tre
     * 
     * @param revenuModel
     *            Le mod�le du revenu � cr�er
     * @return RevenuModel Le mod�le de revenu cr��
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RevenuModel create(RevenuModel revenuModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Suppression d'un revenu selon le mod�le pass�e en param�tre
     * 
     * @param revenuModel
     *            Le mod�le du revenu � effacer
     * @return RevenuModel Le mod�le du revenu effac�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RevenuModel delete(RevenuModel revenuModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime les revenus li�s � l'id de l'allocataire pass� en param�tre
     * 
     * @param idAllocataire
     *            L'id de l'allocataire pour lequel il faut supprimer les revenus
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void deleteForIdAllocataire(String idAllocataire) throws JadePersistenceException, JadeApplicationException;

    /**
     * Initialise un revenuModel avec des valeurs par d�faut
     * 
     * @param revenuModel
     *            Le mod�le revenu � initialiser
     * @return Le mod�le revenu initialis�
     * @throws JadeApplicationException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public RevenuModel initModel(RevenuModel revenuModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Lecture d'un revenu en fonction de son id pass� en param�tre
     * 
     * @param idRevenuModel
     *            L'id du revenu � lire
     * @return RevenuModel Le mod�le du revenu lu
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RevenuModel read(String idRevenuModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * 
     * Recherche d'un revenu selon le mod�le de recherche pass� en param�tre
     * 
     * @param revenuSearchModel
     *            Le mod�le de recherche de revenu (contenant les crit�res)
     * @return Le mod�le de recherche contenant les r�sultats
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RevenuSearchModel search(RevenuSearchModel revenuSearchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche le dernier revenu de la personne en fonction de la date pass�e en param�tre
     * 
     * @param date
     *            La date � partir de laquelle chercher le dernier revenu
     * @param idAllocataire
     *            id de l'allocataire pour lequel rechercher le revenu
     * @param isConjoint
     *            indique s'il faut rechercher le revenu de l'allocataire ou celui de son conjoint
     * @return Le revenuModel repr�sentant le r�sulat ou "null" si aucun revenu trouv�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RevenuModel searchDernierRevenu(String date, String idAllocataire, boolean isConjoint)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Mise � jour d'un revenu
     * 
     * @param revenuModel
     *            Le mod�le revenu � mettre � jour
     * @return revenuModel Le mod�le revenu mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RevenuModel update(RevenuModel revenuModel) throws JadeApplicationException, JadePersistenceException;
}
