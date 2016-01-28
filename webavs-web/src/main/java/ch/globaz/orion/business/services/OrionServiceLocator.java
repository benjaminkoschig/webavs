package ch.globaz.orion.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.orion.business.services.acl.AclService;
import ch.globaz.orion.business.services.dan.DanService;
import ch.globaz.orion.business.services.inscription.InscriptionService;
import ch.globaz.orion.business.services.pucs.PucsService;

public abstract class OrionServiceLocator {
    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static AclService getAclService() throws JadeApplicationServiceNotAvailableException {
        return (AclService) JadeApplicationServiceLocator.getInstance().getServiceImpl(AclService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static DanService getDanService() throws JadeApplicationServiceNotAvailableException {
        return (DanService) JadeApplicationServiceLocator.getInstance().getServiceImpl(DanService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static InscriptionService getInscriptionService() throws JadeApplicationServiceNotAvailableException {
        return (InscriptionService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(InscriptionService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static PucsService getPucsService() throws JadeApplicationServiceNotAvailableException {
        return (PucsService) JadeApplicationServiceLocator.getInstance().getServiceImpl(PucsService.class);
    }
}
