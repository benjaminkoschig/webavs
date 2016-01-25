package ch.globaz.al.business.services.models.attribut;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;
import ch.globaz.al.business.models.attribut.AttributEntiteSearchModel;

/**
 * Service de gestion de persistance des attributs li�s � une entit� pr�cise
 * 
 * @author GMO
 * 
 */
public interface AttributEntiteModelService extends JadeApplicationService {
    /**
     * Cr�ation d'un attribut entit� selon le mod�le pass�e en param�tre
     * 
     * @param attributEntiteModel
     *            Le mod�le attribut entit� � cr�er
     * @return AttributEntiteModel Le mod�le attribut entit� cr��
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public AttributEntiteModel create(AttributEntiteModel attributEntiteModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Suppression d'un attribut entit� selon le mod�le pass�e en param�tre
     * 
     * @param attributEntiteModel
     *            Le mod�le attribut entit� � effacer
     * @return AttributEntiteModel Le mod�le attribut entit� effac�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public AttributEntiteModel delete(AttributEntiteModel attributEntiteModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche l'attribut voulu pour un affili� donn�
     * 
     * @param nomAttr
     *            l'attribut recherch�
     * @param idAffiliation
     *            l'id de l'affiliation
     * @return le mod�le de l'attribut
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AttributEntiteModel getAttributAffilie(String nomAttr, String idAffiliation)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche l'attribut voulu pour un affili� donn�
     * 
     * @param nomAttr
     *            l'attribut recherch�
     * @param numeroAffilie
     *            le num�ro de l'affili� dont on veut l'attribut
     * @return le mod�le de l'attribut
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AttributEntiteModel getAttributAffilieByNumAffilie(String nomAttr, String numeroAffilie)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche d'un attribut selon les crit�res du mod�le de recherche pass� en param�tre
     * 
     * @param searchModel
     *            Le mod�le de recherche contenant les crit�res
     * @return Le mod�le de recherche contenant les r�sultats
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AttributEntiteSearchModel search(AttributEntiteSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Modification d'un attribut entit� selon le mod�le pass�e en param�tre
     * 
     * @param attributEntiteModel
     *            Le mod�le attribut entit� � modifier
     * @return AttributEntiteModel Le mod�le attribut entit� modifi�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public AttributEntiteModel update(AttributEntiteModel attributEntiteModel) throws JadeApplicationException,
            JadePersistenceException;

}
