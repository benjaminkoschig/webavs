/**
 * 
 */
package ch.globaz.al.business.services.models.allocataire;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.allocataire.AgricoleModel;
import ch.globaz.al.business.models.allocataire.AgricoleSearchModel;

/**
 * Service de gestion de persistance des donn�es de Agricol de allocataire
 * 
 * @author PTA
 */
public interface AgricoleModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retourn�s par le mod�le de recherche pass� en param�tre
     * 
     * @param agricoleSearch
     *            mod�le contenant les crit�res de recherche
     * @return nombre d'enregistrement correspondant � la recherche
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int count(AgricoleSearchModel agricoleSearch) throws JadePersistenceException;

    /**
     * Cr�e d'un allocataire agricole selon le mod�le agricole pass� en param�tre
     * 
     * @param agricoleModel
     *            mod�le � enregistrer
     * @return le mod�le enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AgricoleModel create(AgricoleModel agricoleModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * suppression d'un allocataire agricole selon le mod�le agricoles pass� en param�tre
     * 
     * @param agricoleModel
     *            le mod�le � supprimer
     * @return le mod�le supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AgricoleModel delete(AgricoleModel agricoleModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Permet la suppression des donn�es agriculteur li�es � un allocataire dont l'id est pass� en param�tre
     * 
     * @param idAllocataire
     *            id de l'allocataire pour lequel supprimer les donn�es
     * @return mod�le contenant les donn�es supprim�es
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AgricoleModel deleteForIdAllocataire(String idAllocataire) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Lecture d'un allocataire agricole en fonction de son id pass� en param�tre
     * 
     * @param idAgricoleModel
     *            id du mod�le � charger
     * @return le mod�le charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AgricoleModel read(String idAgricoleModel) throws JadeApplicationException, JadePersistenceException;

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
    public AgricoleSearchModel search(AgricoleSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * 
     * Mise � jour d'un allocataire agricole selon l'allocataire agricole pass� en param�tre
     * 
     * @param agricoleModel
     *            le mod�le � mettre � jour
     * @return le mod�le mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AgricoleModel update(AgricoleModel agricoleModel) throws JadeApplicationException, JadePersistenceException;

}
