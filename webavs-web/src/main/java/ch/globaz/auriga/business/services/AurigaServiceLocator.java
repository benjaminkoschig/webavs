package ch.globaz.auriga.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.common.business.services.ParametreService;

public abstract class AurigaServiceLocator {
    public static DecisionCAPService getDecisionCAPService() throws JadeApplicationServiceNotAvailableException {
        return (DecisionCAPService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(DecisionCAPService.class);
    }

    public static EnfantDecisionCAPService getEnfantDecisionCAPService()
            throws JadeApplicationServiceNotAvailableException {
        return (EnfantDecisionCAPService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                EnfantDecisionCAPService.class);
    }

    public static ParametreService getParametreService() throws JadeApplicationServiceNotAvailableException {
        return (ParametreService) JadeApplicationServiceLocator.getInstance().getServiceImpl(ParametreService.class);
    }

    public static SortieCAPService getSortieCAPService() throws JadeApplicationServiceNotAvailableException {
        return (SortieCAPService) JadeApplicationServiceLocator.getInstance().getServiceImpl(SortieCAPService.class);
    }
}
