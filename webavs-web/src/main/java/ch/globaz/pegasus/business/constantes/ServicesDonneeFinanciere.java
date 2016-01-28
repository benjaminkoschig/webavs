/**
 * 
 */
package ch.globaz.pegasus.business.constantes;

import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.business.models.fortuneparticuliere.BetailSearch;
import ch.globaz.pegasus.business.models.fortuneparticuliere.PretEnversTiersSearch;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleBetail;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimplePretEnversTiers;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleVehicule;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * @author ECO
 * 
 *         Classe utilitaire pour retrouve le modele et le service d'une donnee financiere à partir de son type (code
 *         systeme)
 */
public class ServicesDonneeFinanciere {

    private static Map mappingServicesDonneeFinanciere = new HashMap();

    static {
        try {
            ServicesDonneeFinanciere.mappingServicesDonneeFinanciere.put(IPCDroits.CS_PRETS_ENVERS_TIERS, new Object[] {
                    SimplePretEnversTiers.class, PegasusImplServiceLocator.getSimplePretEnversTiersService(),
                    PretEnversTiersSearch.class });
            ServicesDonneeFinanciere.mappingServicesDonneeFinanciere.put(IPCDroits.CS_BETAIL, new Object[] {
                    SimpleBetail.class, PegasusImplServiceLocator.getSimpleBetailService(), BetailSearch.class });
            ServicesDonneeFinanciere.mappingServicesDonneeFinanciere.put(IPCDroits.CS_VEHICULE, new Object[] {
                    SimpleVehicule.class, PegasusImplServiceLocator.getSimpleVehiculeService() });
        } catch (JadeApplicationServiceNotAvailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * retourne l'objet, modele ou service selon l'index, correspondant au type de la donnee financiere.
     * 
     * @param codeSystem
     *            type de la donnee financiere
     * @param idx
     *            index de l'objet à récupérer (0:simple modele,1:service simple)
     * @return objet correspondant, ou null si type inexistant
     */
    private static Object getMapDonneeFinanciere(String codeSystem, int idx) {

        Object[] mapping = (Object[]) ServicesDonneeFinanciere.mappingServicesDonneeFinanciere.get(codeSystem);
        if (mapping != null) {
            return mapping[idx];
        } else {
            return null;
        }
    }

    /**
     * Retourne la classe du simple modele de la donnee financiere
     * 
     * @param codeSystem
     *            type de la donnee financiere
     * @return classe du simple modele ou null
     */
    public static Class getSimpleDonneeFinanciereClass(String codeSystem) {
        return (Class) ServicesDonneeFinanciere.getMapDonneeFinanciere(codeSystem, 0);
    }

    /**
     * Retourne le service du simple modele de la donnee financiere
     * 
     * @param codeSystem
     *            type de la donnee financiere
     * @return service du simple modele
     */
    public static JadeApplicationService getSimpleDonneeFinanciereService(String codeSystem) {
        return (JadeApplicationService) ServicesDonneeFinanciere.getMapDonneeFinanciere(codeSystem, 1);
    }

}
