package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.CopieModel;
import ch.globaz.al.business.models.dossier.CopieSearchModel;

/**
 * Service de gestion de la persistance des donn�es des copies de dossier
 * 
 * @author jts
 */
public interface CopieModelService extends JadeApplicationService {

    /**
     * Effectue une copie du mod�le pass� en param�tre
     * 
     * @param copieModel
     *            mod�le � copier
     * @return copieMoel
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieModel copy(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Compte le nombre d'enregistrement retourn�s par le mod�le de recherche pass� en param�tre
     * 
     * @param copieSearch
     *            Mod�le de recherche contenant les crit�res
     * @return nombre de copies correspondant aux crit�res de recherche
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * 
     * @see ch.globaz.al.business.models.dossier.CopieModel
     */
    public int count(CopieSearchModel copieSearch) throws JadePersistenceException, JadeApplicationException;

    /**
     * Enregistre <code>copieModel</code> en persistance
     * 
     * @param copieModel
     *            Copie � enregistrer
     * @return CopieModel Copie enregistr�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.dossier.CopieModel
     */
    public CopieModel create(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime <code>copieModel</code> de la persistance
     * 
     * @param copieModel
     *            Copie � supprimer
     * @return CopieModel Copie supprim�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.dossier.CopieModel
     */
    public CopieModel delete(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise une copie
     * 
     * @param copieModel
     *            Copie � initialiser
     * @return Copie initialiser
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.dossier.CopieModel
     */
    public CopieModel initModel(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * R�cup�re les donn�es de la copie correspondant � <code>idCopie</code>
     * 
     * @param idCopie
     *            Id de la copie � charger
     * @return CopieModel Copie charg�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.dossier.CopieModel
     */
    public CopieModel read(String idCopie) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche
     * 
     * @param copieSearchModel
     *            mod�le pour la recherche
     * @return mod�le de recherche de copie
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CopieSearchModel search(CopieSearchModel copieSearchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Met � jour les donn�es de <code>copieModel</code> en persistance
     * 
     * @param copieModel
     *            Copie � mettre � jour
     * @return CopieModel copie mise � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.dossier.CopieModel
     */
    public CopieModel update(CopieModel copieModel) throws JadeApplicationException, JadePersistenceException;

}