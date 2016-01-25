package ch.globaz.aries.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.common.business.services.ParametreService;
import ch.globaz.common.business.services.TraitementMasseService;

public class AriesServiceLocator {

    public static DecisionCGASService getDecisionCGASService() throws JadeApplicationServiceNotAvailableException {
        return (DecisionCGASService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DecisionCGASService.class);
    }

    public static ParametreService getParametreService() throws JadeApplicationServiceNotAvailableException {
        return (ParametreService) JadeApplicationServiceLocator.getInstance().getServiceImpl(ParametreService.class);
    }

    public static SortieCGASService getSortieCGASService() throws JadeApplicationServiceNotAvailableException {
        return (SortieCGASService) JadeApplicationServiceLocator.getInstance().getServiceImpl(SortieCGASService.class);
    }

    public static TraitementMasseService getTraitementMasseService() throws JadeApplicationServiceNotAvailableException {
        return (TraitementMasseService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                TraitementMasseService.class);
    }

}
