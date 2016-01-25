/**
 * 
 */
package ch.globaz.al.business.services.languesAllocAffilies;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;

/**
 * Service permettant de récupérer la langue de l'affilié, de l'allocataire
 * 
 * @author PTA
 * 
 */
// TODO renommerr en langueDocumentAffilieServieInfo
public interface LangueAllocAffilieService extends JadeApplicationService {
    /**
     * Méthode qui retourne une hashmap contenant la langue de l'allocataire et celle de l'affilié
     * 
     * @param idTiersAlloc
     *            identifiant du tiers allocataire
     * @param idTiersAffilie
     *            identififiant du tiers affilié
     * @return
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
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
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String langueTiers(String idTiers) throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne la langue d'un afffilié
     * 
     * @param numAffilie
     * @return
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    // TODO voir si utilisé
    public String langueTiersAffilie(String numAffilie) throws JadeApplicationException, JadePersistenceException;

    /**
     * retourne la langue d'un tiers
     * 
     * @param idTiers
     *            identifiant du tiers pour lequel il faut rechercher la langue
     * @param numAffilie
     *            numéro de l'affilié numéro de l'affilié
     * @return La langue du tiers allocataire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String langueTiersAlloc(String idTiers, String numAffilie) throws JadeApplicationException,
            JadePersistenceException;

}
