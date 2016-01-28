/**
 * 
 */
package ch.globaz.al.business.services.models.allocataire;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.allocataire.AllocataireModel;
import ch.globaz.al.business.models.allocataire.AllocataireSearchModel;

/**
 * Service de gestion de persistance des donn�es des allocataire de allocataire
 * 
 * @author PTA
 */
public interface AllocataireModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retourn�s par le mod�le de recherche pass� en param�tre
     * 
     * @param allocataireSearch
     *            mod�le contenant les crit�res de recherche
     * @return nombre d'enregistrement correspondant � la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int count(AllocataireSearchModel allocataireSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * cr�ation d'un allocataire selon le mod�le allocataire pass� en param�tre
     * 
     * @param allocataireModel
     *            mod�le � enregistrer
     * @return le mod�le enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireModel create(AllocataireModel allocataireModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Suppression d'un allocataire selon le mod�le allocataire pass� en param�tre
     * 
     * @param allocataireModel
     *            le mod�le � supprimer
     * @return le mod�le supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireModel delete(AllocataireModel allocataireModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * initialisation d'un mod�le allocataire selon le mod�le pass�e en param�tre
     * 
     * @param allocataireModel
     *            le mod�le � initialiser
     * @return le mod�le initialis�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireModel initModel(AllocataireModel allocataireModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture d'un allocataire selon l'identifiant de l'allocataire pass� en param�tre
     * 
     * @param idAllocataireModel
     *            id du mod�le � charger
     * @return le mod�le charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireModel read(String idAllocataireModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Mise � jour d'un allocataire selon le mod�le allocataire pass� en param�tre
     * 
     * @param allocataireModel
     *            le mod�le � mettre � jour
     * @return le mod�le mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireModel update(AllocataireModel allocataireModel) throws JadeApplicationException,
            JadePersistenceException;
}
