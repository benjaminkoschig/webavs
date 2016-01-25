package globaz.aquila.service;

import globaz.aquila.service.config.COConfigurationService;
import globaz.aquila.service.historique.COHistoriqueService;
import globaz.aquila.service.taxes.COTaxeService;
import globaz.aquila.service.tiers.COTiersService;
import globaz.aquila.service.transition.COCancelTransitionService;
import globaz.aquila.service.transition.COExecuteTransitionService;
import globaz.aquila.service.transition.COTransitionActionService;
import java.util.HashMap;

/**
 * <H1>Description</H1>
 * <p>
 * Un annuaire des services pour l'application Aquila
 * </p>
 * 
 * @author vre
 */
public class COServiceLocator {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final HashMap CLASS_TO_SERVICE = new HashMap();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Retourne le service des actions de transitions.
     * 
     * @return une instance unique de {@link COTransitionActionService}
     */
    public static final COTransitionActionService getActionService() {
        COTransitionActionService transitionService = (COTransitionActionService) COServiceLocator.CLASS_TO_SERVICE
                .get(COTransitionActionService.class);

        if (transitionService == null) {
            transitionService = new COTransitionActionService();
            COServiceLocator.CLASS_TO_SERVICE.put(COTransitionActionService.class, transitionService);
        }

        return transitionService;
    }

    /**
     * Retourne le service d'annulation de transition.
     * 
     * @return une instance unique de {@link COCancelTransitionService}
     */
    public static final COCancelTransitionService getCancelTransitionService() {
        COCancelTransitionService service = (COCancelTransitionService) COServiceLocator.CLASS_TO_SERVICE
                .get(COCancelTransitionService.class);

        if (service == null) {
            service = new COCancelTransitionService();
            COServiceLocator.CLASS_TO_SERVICE.put(COCancelTransitionService.class, service);
        }

        return service;
    }

    /**
     * Retourne le service des options de configuration.
     * 
     * @return une instance unique de {@link COExecuteTransitionService}
     */
    public static final COConfigurationService getConfigService() {
        COConfigurationService service = (COConfigurationService) COServiceLocator.CLASS_TO_SERVICE
                .get(COConfigurationService.class);

        if (service == null) {
            service = new COConfigurationService();
            COServiceLocator.CLASS_TO_SERVICE.put(COConfigurationService.class, service);
        }

        return service;
    }

    /**
     * Retourne le service des historiques.
     * 
     * @return une instance unique de {@link COHistoriqueService}
     */
    public static final COHistoriqueService getHistoriqueService() {
        COHistoriqueService service = (COHistoriqueService) COServiceLocator.CLASS_TO_SERVICE
                .get(COHistoriqueService.class);

        if (service == null) {
            service = new COHistoriqueService();
            COServiceLocator.CLASS_TO_SERVICE.put(COHistoriqueService.class, service);
        }

        return service;
    }

    /**
     * Retourne le service des taxes.
     * 
     * @return une instance unique de {@link COTaxeService}
     */
    public static final COTaxeService getTaxeService() {
        COTaxeService service = (COTaxeService) COServiceLocator.CLASS_TO_SERVICE.get(COTaxeService.class);

        if (service == null) {
            service = new COTaxeService();
            COServiceLocator.CLASS_TO_SERVICE.put(COTaxeService.class, service);
        }

        return service;
    }

    /**
     * Retourne un service des tiers.
     * 
     * @return une instance NON UNIQUE de {@link COTiersService}
     */
    public static final COTiersService getTiersService() {
        return new COTiersService();
    }

    /**
     * Retourne le service des transitions.
     * 
     * @return une instance unique de {@link COExecuteTransitionService}
     */
    public static final COExecuteTransitionService getTransitionService() {
        COExecuteTransitionService transitionService = (COExecuteTransitionService) COServiceLocator.CLASS_TO_SERVICE
                .get(COExecuteTransitionService.class);

        if (transitionService == null) {
            transitionService = new COExecuteTransitionService();
            COServiceLocator.CLASS_TO_SERVICE.put(COExecuteTransitionService.class, transitionService);
        }

        return transitionService;
    }
}
