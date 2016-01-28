package ch.globaz.al.business.services.models.tauxMonnaieEtrangere;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel;
import ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereSearchModel;

/**
 * Service de gestion de la persistance des donn�es de TauxMonnaieEtrangereModel
 * 
 * @author PTA
 * 
 */
public interface TauxMonnaieEtrangereModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retourn�s par le mod�le de recherche pass� en param�tre
     * 
     * @param tauxMonnaieSearch
     *            Mod�le de recherche contenant les crit�res
     * @return nombre de copies correspondant aux crit�res de recherche
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * 
     * @see ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel
     */

    public int count(TauxMonnaieEtrangereSearchModel tauxMonnaieSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Enregistre <code>TauxMonnaieEtrangereModel</code> en persistance
     * 
     * @param tauxMonnaieEtrangere
     *            taux � enregistrer
     * @return TauxMonnaieEtrangereModel enregistr�e
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel
     * 
     */
    public TauxMonnaieEtrangereModel create(TauxMonnaieEtrangereModel tauxMonnaieEtrangere)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime le mod�le <code>TauxMonnaieEtrangereModel</code> en persistance
     * 
     * @param tauxMonnaieEtrangere
     *            tau � supprimer
     * @return TauxMonnaieEtrangereMode taux supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel
     */
    public TauxMonnaieEtrangereModel delete(TauxMonnaieEtrangereModel tauxMonnaieEtrangere)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * R�cup�re les donn�es du taux de monnaie �trang�re correspondant � <code>idDossierModel</code>
     * 
     * @param idTauxMonnaierEtrangere
     *            identifiant du taux de la monnaie �trang�re
     * @return TauxMonnaieEtrangerModel le mod�le du taux charg�e
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel
     */
    public TauxMonnaieEtrangereModel read(String idTauxMonnaierEtrangere) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Effectue une recherche selon le mod�le <code>TauxMonnaieEtrangereSearchModel</code> de recherche pass� en
     * param�tre
     * 
     * @param tauxMonnaieEtrangereSearch
     *            mod�le de recherche du taux
     * @return R�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TauxMonnaieEtrangereSearchModel search(TauxMonnaieEtrangereSearchModel tauxMonnaieEtrangereSearch)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Met � jour le mod�le <code>TauxMonnaieEtrangereModel</code> en persistance
     * 
     * @param tauxMonnaieEtrangere
     *            taux � mettre jour
     * @return TauxMonnaieEtrangereModel model du taux mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel
     */
    public TauxMonnaieEtrangereModel update(TauxMonnaieEtrangereModel tauxMonnaieEtrangere)
            throws JadeApplicationException, JadePersistenceException;

}
