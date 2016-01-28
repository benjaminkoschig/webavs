package ch.globaz.al.business.services.models.droit;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.droit.EnfantModel;
import ch.globaz.al.business.models.droit.EnfantSearchModel;

/**
 * service de gestion de la persistance des donn�es d'un enfant
 * 
 * @author PTA
 */
public interface EnfantModelService extends JadeApplicationService {

    /**
     * Retourne le nombre d'enfants trouv�s par le mod�le de recherche
     * 
     * @param enfantSearch
     *            Mod�le de recherche contenant les crit�res de s�lection
     * @return le nombre d'enfant correspondant � la recherche
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public int count(EnfantSearchModel enfantSearch) throws JadePersistenceException, JadeApplicationException;

    /**
     * Enregistre l'enfant pass� en param�tre en persistance
     * 
     * @param enfantModel
     *            Le mod�le � enregistrer
     * @return EnfantModel le mod�le enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantModel create(EnfantModel enfantModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Suppression d'un enfant en fonction du mod�le pass�e en param�tre
     * 
     * @param enfantModel
     *            le mod�le � supprimer
     * @return EnfantModel le mod�le supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantModel delete(EnfantModel enfantModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise un mod�le d'enfant
     * 
     * @param enfantModel
     *            le mod�le � initialiser
     * 
     * @return le mod�le initialis�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantModel initModel(EnfantModel enfantModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Lecture d'un enfant en fonction de son identifiant pass� en param�tre
     * 
     * @param idEnfantModel
     *            Id de l'enfant � charger
     * 
     * @return EnfantModel L'enfant charg�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantModel read(String idEnfantModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Met � jour l'enfant pass� en param�tre
     * 
     * @param enfantModel
     *            le mod�le � mettre � jour
     * @return EnfantModel le mod�le mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EnfantModel update(EnfantModel enfantModel) throws JadeApplicationException, JadePersistenceException;
}
