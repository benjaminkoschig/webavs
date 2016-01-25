/**
 * 
 */
package ch.globaz.amal.business.services.interapplication;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;

/**
 * Services � disposition du client PC
 * 
 * @author dhi
 * 
 */
public interface PCCustomerService extends JadeApplicationService {

    /**
     * R�cup�ration des informations de subsides pour une map p�riode-tiers pass�e en param�tre
     * 
     * @param periodesTiers
     *            map p�riode-liste tiers. Exemple : 08.2010, 123;124;128 - 08.2011, 123;124
     * @return
     * 
     *         map p�riode-liste tiers - liste subsides.
     */
    public Map<String, Map<String, List<SimpleDetailFamille>>> getAmalSubsidesByPeriodes(
            Map<String, List<String>> periodesTiers);

    /**
     * R�cup�ration des informations de subsides pour une liste de tiers pass�e en param�tre.
     * 
     * @param idTiersToCheck
     *            Liste d'idTiers � contr�ler
     * @param moisAnneeDecision
     *            Ann�e au format "MM.YYYY"
     * @return
     *         HashMap| cl� - idTiers | valeur - liste de subside(s)
     */
    public HashMap<String, List<SimpleDetailFamille>> getAmalSubsidesForTiers(List<String> idTiersToCheck,
            String moisAnneeDecision);

    /**
     * R�cup�ration des informations de subsides pour une liste de tiers pass�e en param�tre et une liste de p�riode �
     * contr�ler
     * 
     * @param periodes
     *            List<xx.xxxx> ou xx.xxxx == MM.YYYY
     * @param idTiers
     *            List<idTiers>
     * @return
     *         HashMap<idTiers>-HashMap<p�riode,subsides>
     */
    public HashMap<String, HashMap<String, List<SimpleDetailFamille>>> getAmalSubsidesForTiersByPeriodes(
            List<String> periodes, List<String> idTiers);

}
