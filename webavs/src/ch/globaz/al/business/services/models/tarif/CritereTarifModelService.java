package ch.globaz.al.business.services.models.tarif;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.tarif.CritereTarifModel;
import ch.globaz.al.business.models.tarif.CritereTarifSearchModel;

/**
 * 
 * Service de gestion de persistance des donn�es de CritereTarif
 * 
 * @author PTA
 */
public interface CritereTarifModelService extends JadeApplicationService {

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
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int count(CritereTarifSearchModel search) throws JadePersistenceException, JadeApplicationException;

    /**
     * Cr�ation de crit�re tarif selon le mod�le pass� en param�tre
     * 
     * @param critereTarifModel
     *            mod�le � enregistrer
     * @return mod�le enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CritereTarifModel create(CritereTarifModel critereTarifModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture d'un crit�re tarif selon l'id pass� en param�tre
     * 
     * @param idCritereTarifModel
     *            Id du mod�le � charger
     * @return le mod�le charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CritereTarifModel read(String idCritereTarifModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche d'un crit�re tarif selon le mod�le pass� en param�tre
     * 
     * @param critereTarifSearch
     *            mod�le contenant les crit�res de recherche
     * @return mod�le contenant le r�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CritereTarifSearchModel search(CritereTarifSearchModel critereTarifSearch) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Mise � jour d'un crit�re tarif selon le mod�le pass� en param�tre
     * 
     * @param critereTarifModel
     *            contenant les donn�es mise � jour
     * @return le mod�le apr�s mise � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CritereTarifModel update(CritereTarifModel critereTarifModel) throws JadeApplicationException,
            JadePersistenceException;
}
