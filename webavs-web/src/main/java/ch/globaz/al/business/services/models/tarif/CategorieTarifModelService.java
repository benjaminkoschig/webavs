package ch.globaz.al.business.services.models.tarif;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.tarif.CategorieTarifModel;
import ch.globaz.al.business.models.tarif.CategorieTarifSearchModel;

/**
 * Service de gestion de persistance des donn�es de CategorieTarif
 * 
 * @author PTA
 */
public interface CategorieTarifModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retourn�s par le mod�le de recherche pass� en param�tre
     * 
     * @param search
     *            mod�le contenant les crit�res de recherche
     * @return nombre de lignes retourn� par la recherche
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */

    public int count(CategorieTarifSearchModel search) throws JadePersistenceException, JadeApplicationException;

    /**
     * Cr�ation d'une cat�gorie tarif selon le mod�le pass� en param�tre
     * 
     * @param categorieTarifModel
     *            mod�le � enregistrer
     * @return mod�le enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CategorieTarifModel create(CategorieTarifModel categorieTarifModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture d'une cat�gorie de tarif pass� en param�tre selon l'id pass� en param�tre
     * 
     * @param idCategorieTarifModel
     *            Id du mod�le � charger
     * @return le mod�le charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CategorieTarifModel read(String idCategorieTarifModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche d'une cat�gorie tarif selon le mod�le pass� en param�tre
     * 
     * @param categorieTarifSearch
     *            mod�le contenant les crit�res de recherche
     * @return mod�le contenant le r�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CategorieTarifSearchModel search(CategorieTarifSearchModel categorieTarifSearch)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Mise � jour d'une cat�gorie de tarif selon le mod�le pass� en param�tre
     * 
     * @param categorieTarifModel
     *            contenant les donn�es mise � jour
     * @return le mod�le apr�s mise � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CategorieTarifModel update(CategorieTarifModel categorieTarifModel) throws JadeApplicationException,
            JadePersistenceException;

}