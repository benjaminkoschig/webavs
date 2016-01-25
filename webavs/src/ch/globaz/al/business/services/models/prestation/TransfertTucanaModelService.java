package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.TransfertTucanaModel;
import ch.globaz.al.business.models.prestation.TransfertTucanaSearchModel;

/**
 * service de gestion des donn�es inh�rentes � TransfertTucana dans Prestation
 * 
 * @author PTA
 * 
 */
public interface TransfertTucanaModelService extends JadeApplicationService {

    /**
     * Retourne le nombre d'enregistrements Tucana trouv�s par le mod�le de recherche
     * 
     * @param search
     *            mod�le contenant les crit�res de recherche
     * @return nombre d'enregistrement correspondant � la recherche
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public int count(TransfertTucanaSearchModel search) throws JadeApplicationException, JadePersistenceException;

    /**
     * Cr�ation d'un TransfertTucana selon le mod�le pass� en param�tre
     * 
     * @param transTucanaModel
     *            mod�le � enregistrer
     * @return le mod�le enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TransfertTucanaModel create(TransfertTucanaModel transTucanaModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Suppression d'un TransfertTucana selon le mod�le pass� en param�tre
     * 
     * @param transTucanaModel
     *            le mod�le � supprimer
     * @return le mod�le supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TransfertTucanaModel delete(TransfertTucanaModel transTucanaModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture d'un TransfertTucana selon l'id pass� en param�tre
     * 
     * @param idtransTucanaModel
     *            id du mod�le � charger
     * @return le mod�le charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TransfertTucanaModel read(String idtransTucanaModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Effectue une recherche selon le mod�le de recherche pass� en param�tre
     * 
     * @param searchModel
     *            mod�le de recherche de transfert Tucana
     * @return R�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TransfertTucanaSearchModel search(TransfertTucanaSearchModel search) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Mise � jour d'un TransfertTucana selon le mod�le pass� en param�tre
     * 
     * @param transTucanaModel
     *            le mod�le � mettre � jour
     * @return le mod�le mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TransfertTucanaModel update(TransfertTucanaModel transTucanaModel) throws JadeApplicationException,
            JadePersistenceException;
}
