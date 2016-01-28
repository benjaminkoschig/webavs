package ch.globaz.pegasus.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.services.models.parametre.ForfaitPrimeAssuranceMaladieLocaliteService;
import ch.globaz.pegasus.business.services.models.parametre.ForfaitsPrimesAssuranceMaladieService;
import ch.globaz.pegasus.business.services.models.parametre.SimpleZoneForfaitsService;
import ch.globaz.pegasus.business.services.models.parametre.ZoneLocaliteService;

/**
 * @author DMA
 * @date 16 nov. 2010
 */
public class ParametreServicesLocator {

    /**
     * @return Implémentation du service SimpleZoneForfaits
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    protected ParametreServicesLocator() {

    }

    public ForfaitPrimeAssuranceMaladieLocaliteService getForfaitPrimeAssuranceMaladieLocaliteService()
            throws JadeApplicationServiceNotAvailableException {
        return (ForfaitPrimeAssuranceMaladieLocaliteService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(ForfaitPrimeAssuranceMaladieLocaliteService.class);
    }

    public ForfaitsPrimesAssuranceMaladieService getForfaitsPrimesAssuranceMaladieService()
            throws JadeApplicationServiceNotAvailableException {
        return (ForfaitsPrimesAssuranceMaladieService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ForfaitsPrimesAssuranceMaladieService.class);
    }

    public SimpleZoneForfaitsService getSimpleZoneForfaitsService() throws JadeApplicationServiceNotAvailableException {
        return (SimpleZoneForfaitsService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                SimpleZoneForfaitsService.class);
    }

    public ZoneLocaliteService getZoneLocaliteService() throws JadeApplicationServiceNotAvailableException {
        return (ZoneLocaliteService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                ZoneLocaliteService.class);
    }
}
