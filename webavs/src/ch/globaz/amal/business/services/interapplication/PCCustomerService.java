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
 * Services à disposition du client PC
 * 
 * @author dhi
 * 
 */
public interface PCCustomerService extends JadeApplicationService {

    /**
     * Récupération des informations de subsides pour une map période-tiers passée en paramètre
     * 
     * @param periodesTiers
     *            map période-liste tiers. Exemple : 08.2010, 123;124;128 - 08.2011, 123;124
     * @return
     * 
     *         map période-liste tiers - liste subsides.
     */
    public Map<String, Map<String, List<SimpleDetailFamille>>> getAmalSubsidesByPeriodes(
            Map<String, List<String>> periodesTiers);

    /**
     * Récupération des informations de subsides pour une liste de tiers passée en paramètre.
     * 
     * @param idTiersToCheck
     *            Liste d'idTiers à contrôler
     * @param moisAnneeDecision
     *            Année au format "MM.YYYY"
     * @return
     *         HashMap| clé - idTiers | valeur - liste de subside(s)
     */
    public HashMap<String, List<SimpleDetailFamille>> getAmalSubsidesForTiers(List<String> idTiersToCheck,
            String moisAnneeDecision);

    /**
     * Récupération des informations de subsides pour une liste de tiers passée en paramètre et une liste de période à
     * contrôler
     * 
     * @param periodes
     *            List<xx.xxxx> ou xx.xxxx == MM.YYYY
     * @param idTiers
     *            List<idTiers>
     * @return
     *         HashMap<idTiers>-HashMap<période,subsides>
     */
    public HashMap<String, HashMap<String, List<SimpleDetailFamille>>> getAmalSubsidesForTiersByPeriodes(
            List<String> periodes, List<String> idTiers);

}
