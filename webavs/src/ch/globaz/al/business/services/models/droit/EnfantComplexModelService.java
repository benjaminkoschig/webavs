package ch.globaz.al.business.services.models.droit;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.droit.EnfantComplexModel;
import ch.globaz.al.business.models.droit.EnfantComplexSearchModel;

/**
 * Service de gestion de la persistance des donn�es li� au mod�le complexe enfant
 * 
 * @author PTA
 * @see ch.globaz.al.business.models.droit.EnfantComplexModel
 * @see ch.globaz.al.business.models.droit.EnfantComplexSearchModel
 */
public interface EnfantComplexModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retourn�s par le mod�le de recherche pass� en param�tre
     * 
     * @param enfantSearchComplex
     *            Mod�le contenant les crit�res de s�lection
     * 
     * @return Nombre d'enfant correspondant au mod�le de recherche
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int count(EnfantComplexSearchModel enfantSearchComplex) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Enregistre l'enfant pass� en param�tre en persistance
     * 
     * @param enfantComplexModel
     *            Mod�le � enregistrer
     * 
     * @return Mod�le enregistr�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantComplexModel create(EnfantComplexModel enfantComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime l'enfant pass� en param�tre
     * 
     * @param enfantComplexModel
     *            Mod�le � supprimer
     * 
     * @return Mod�le supprim�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantComplexModel delete(EnfantComplexModel enfantComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initilise un nouveau mod�le d'enfant
     * 
     * @param enfantComplexModel
     *            le mod�le � initialiser
     * @return le mod�le initialis�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantComplexModel initModel(EnfantComplexModel enfantComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Charge l'enfant correspondant � l'id pass� en param�tre
     * 
     * @param idEnfantComplexModel
     *            id de l'enfant � charger
     * @return le mod�le charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantComplexModel read(String idEnfantComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche d'un enfant selon le mod�le de recherche pass� en param�tre
     * 
     * @param enfantComplexSearchModel
     *            mod�le contenant les crit�res de recherche
     * @return R�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantComplexSearchModel search(EnfantComplexSearchModel enfantComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Met � jour <code>enfantComplexModel</code> en persistance
     * 
     * @param enfantComplexModel
     *            Mod�le � mettre � jour
     * @return mod�le mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantComplexModel update(EnfantComplexModel enfantComplexModel) throws JadeApplicationException,
            JadePersistenceException;

}
