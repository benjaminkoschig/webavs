package ch.globaz.hera.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.hera.business.services.models.famille.MembreFamilleService;
import ch.globaz.hera.business.services.models.famille.PeriodeService;
import ch.globaz.hera.business.services.models.famille.RelationConjointService;

public abstract class HeraServiceLocator {

    /**
     * @return Implémentation du service de gestion des Membres de famille
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static MembreFamilleService getMembreFamilleService() throws JadeApplicationServiceNotAvailableException {
        return (MembreFamilleService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                MembreFamilleService.class);
    }

    /**
     * @return Implémentation du service de gestion des Periode
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static PeriodeService getPeriodeService() throws JadeApplicationServiceNotAvailableException {
        return (PeriodeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(PeriodeService.class);
    }

    /**
     * @return Implémentation du service de gestion des Relations entre conjoints
     * @throws JadeApplicationServiceNotAvailableException
     *             Levée lorsque le service n'est pas disponible - n'arrive qu'en remote
     */
    public static RelationConjointService getRelationConjointService()
            throws JadeApplicationServiceNotAvailableException {
        return (RelationConjointService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                RelationConjointService.class);
    }
}
