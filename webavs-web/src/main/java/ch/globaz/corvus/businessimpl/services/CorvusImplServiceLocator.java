package ch.globaz.corvus.businessimpl.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.corvus.business.services.models.blocage.SimpleEnteteBlocageService;

public class CorvusImplServiceLocator extends CorvusServiceLocator {
    public static SimpleEnteteBlocageService getSimpleEnteteBlocageService()
            throws JadeApplicationServiceNotAvailableException {
        return (SimpleEnteteBlocageService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleEnteteBlocageService.class);
    }
}
