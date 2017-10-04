package ch.globaz.corvus.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.corvus.business.services.models.decisions.DecisionService;
import ch.globaz.corvus.business.services.models.demande.DemandeRenteService;
import ch.globaz.corvus.business.services.models.inforecap.InfoRecapService;
import ch.globaz.corvus.business.services.models.lots.LotService;
import ch.globaz.corvus.business.services.models.pcaccordee.SimpleRetenuePayementService;
import ch.globaz.corvus.business.services.models.rentesaccordees.REDiminutionRenteEnfantService;
import ch.globaz.corvus.business.services.models.rentesaccordees.RenteAccordeeService;
import ch.globaz.corvus.business.services.models.rentesaccordees.SimpleInformationsComptabiliteService;
import ch.globaz.corvus.business.services.models.ventilation.SimpleVentilationService;
import ch.globaz.corvus.businessimpl.services.models.decisions.DecisionServiceImpl;
import ch.globaz.corvus.businessimpl.services.models.demande.DemandeRenteServiceImpl;
import ch.globaz.corvus.businessimpl.services.models.rentesaccordees.RenteAccordeeServiceImpl;
import ch.globaz.pyxis.business.services.PyxisCrudServiceLocator;

public abstract class CorvusServiceLocator {

    /**
     * Retourne le service regroupant les méthodes utilitaires concernant les décisions des rentes
     * 
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static DecisionService getDecisionService() throws JadeApplicationServiceNotAvailableException {
        return new DecisionServiceImpl(CorvusCrudServiceLocator.getOrdresVersementCrudService(),
                CorvusCrudServiceLocator.getSoldePourRestitutionCrudService(),
                PyxisCrudServiceLocator.getTiersCrudService());
    }

    /**
     * @return le service regroupant les méthodes utilitaires pour les demandes de rentes
     */
    public static DemandeRenteService getDemandeRenteService() {
        return new DemandeRenteServiceImpl();
    }

    /**
     * Retourne le service permettant de diminuer les rentes pour enfant dans l'échéancier.
     * 
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static REDiminutionRenteEnfantService getEcheanceService()
            throws JadeApplicationServiceNotAvailableException {
        return (REDiminutionRenteEnfantService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                REDiminutionRenteEnfantService.class);
    }

    /**
     * @return Implémentation du service des infos récapes
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static InfoRecapService getInfoRecapService() throws JadeApplicationServiceNotAvailableException {
        return (InfoRecapService) JadeApplicationServiceLocator.getInstance().getServiceImpl(InfoRecapService.class);
    }

    /**
     * @return Implémentation du service de gestion des Lot
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static LotService getLotService() throws JadeApplicationServiceNotAvailableException {
        return (LotService) JadeApplicationServiceLocator.getInstance().getServiceImpl(LotService.class);
    }

    /**
     * 
     * @return le service regroupant les méthodes utilitaires pour les prestations accordées
     */
    public static RenteAccordeeService getRenteAccordeeService() {
        return new RenteAccordeeServiceImpl();
    }

    /**
     * 
     * @return Implémentation du service de gestion des Lot
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static SimpleInformationsComptabiliteService getSimpleInformationsComptabiliteService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleInformationsComptabiliteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleInformationsComptabiliteService.class);
    }

    public static SimpleRetenuePayementService getSimpleRetenuePayementService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleRetenuePayementService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleRetenuePayementService.class);
    }

    public static SimpleVentilationService getSimpleVentilationService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleVentilationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleVentilationService.class);
    }
}
