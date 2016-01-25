package ch.globaz.al.business.services.models.allocataire;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.allocataire.AllocataireComplexSearchModel;

/**
 * interface de d�claration des services de AllocataireComplexMOdel
 * 
 * @author PTA
 */
public interface AllocataireComplexModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'allocataires correspondant aux crit�res contenu dans le mod�le de recherche
     * <code>allocataireComplexSearch</code>
     * 
     * @param allocataireComplexSearch
     *            selon mod�le AllocataireComplexSearchModel
     * @return r�sultat de la recherche
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public int count(AllocataireComplexSearchModel allocataireComplexSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Enregistre le mod�le d'allocataire pass� en param�tre en persistance
     * 
     * @param allocataireComplex
     *            mod�le � enregistrer
     * 
     * @return mod�le enregistr�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireComplexModel create(AllocataireComplexModel allocataireComplex) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * 
     * @param allocataireComplex
     *            le mod�le � supprimer
     * @return le mod�le supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireComplexModel delete(AllocataireComplexModel allocataireComplex) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise un mod�le d'allocataire
     * 
     * @param allocataireComplexModel
     *            le mod�le � initialis�
     * @return le mod�le initialis�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireComplexModel initModel(AllocataireComplexModel allocataireComplexModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge un allocataire
     * 
     * @param idAllocataireComplexModel
     *            identifiant de l'allocataire � charger
     * @return le mod�le charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireComplexModel read(String idAllocataireComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Effectue une recherche d'allocataires selon les crit�res contenu dans le mod�le de recherche
     * <code>allocataireComplexSearch</code>
     * 
     * @param allocataireComplexModel
     *            selon mod�le AllocataireComplexSearchModel
     * @return r�sultat de la recherche
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AllocataireComplexSearchModel search(AllocataireComplexSearchModel allocataireComplexModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Met � jour le mod�le d'allocataire pass� en param�tre en persistance
     * 
     * @param allocataireComplex
     *            mod�le � mettre � jour
     * @return le mod�le mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AllocataireComplexModel update(AllocataireComplexModel allocataireComplex) throws JadeApplicationException,
            JadePersistenceException;
}
