package ch.globaz.prestation.business.services;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.prestation.business.services.models.demande.DemandePrestationService;
import ch.globaz.prestation.business.services.models.recap.RecapitulationPcRfmService;

public abstract class PrestationCommonServiceLocator {

    public static DemandePrestationService getDemandePrestationService()
            throws JadeApplicationServiceNotAvailableException {
        return (DemandePrestationService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                DemandePrestationService.class);
    }

    /**
     * Service permettant de r�cup�rer, pour un mois donn�, la r�capitulation du paiement pour les PC/RFM
     * 
     * @return le service
     * @throws JadeApplicationServiceNotAvailableException
     *             si aucune implementation n'est connue
     */
    public static RecapitulationPcRfmService getRecapitulationPcRfmService()
            throws JadeApplicationServiceNotAvailableException {
        return (RecapitulationPcRfmService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                RecapitulationPcRfmService.class);
    }
}
