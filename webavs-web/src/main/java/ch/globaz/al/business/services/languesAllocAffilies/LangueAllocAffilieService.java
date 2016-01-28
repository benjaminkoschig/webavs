/**
 * 
 */
package ch.globaz.al.business.services.languesAllocAffilies;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;

/**
 * Service permettant de r�cup�rer la langue de l'affili�, de l'allocataire
 * 
 * @author PTA
 * 
 */
// TODO renommerr en langueDocumentAffilieServieInfo
public interface LangueAllocAffilieService extends JadeApplicationService {
    /**
     * M�thode qui retourne une hashmap contenant la langue de l'allocataire et celle de l'affili�
     * 
     * @param idTiersAlloc
     *            identifiant du tiers allocataire
     * @param idTiersAffilie
     *            identififiant du tiers affili�
     * @return
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public HashMap<String, String> languesAffilieAlloc(String idTiersAlloc, String numAffilie)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * retourne la langue d'un tiers allocataire
     * 
     * @param idTiers
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String langueTiers(String idTiers) throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne la langue d'un afffili�
     * 
     * @param numAffilie
     * @return
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    // TODO voir si utilis�
    public String langueTiersAffilie(String numAffilie) throws JadeApplicationException, JadePersistenceException;

    /**
     * retourne la langue d'un tiers
     * 
     * @param idTiers
     *            identifiant du tiers pour lequel il faut rechercher la langue
     * @param numAffilie
     *            num�ro de l'affili� num�ro de l'affili�
     * @return La langue du tiers allocataire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String langueTiersAlloc(String idTiers, String numAffilie) throws JadeApplicationException,
            JadePersistenceException;

}
