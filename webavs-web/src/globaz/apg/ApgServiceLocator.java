package globaz.apg;

import globaz.apg.business.service.APAnnoncesRapgService;
import globaz.apg.business.service.APDroitAPGService;
import globaz.apg.business.service.APEntityService;
import globaz.apg.business.service.APMontantJournalierApgService;
import globaz.apg.business.service.APPlausibilitesApgService;
import globaz.apg.business.service.APRulesService;
import globaz.apg.business.service.APSalaireJournalierApgService;
import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public final class ApgServiceLocator {

    private ApgServiceLocator() {

    }

    /**
     * @return Le service responsable de la manipulation des entités en base de données
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static APEntityService getEntityService() throws JadeApplicationServiceNotAvailableException {
        return (APEntityService) JadeApplicationServiceLocator.getInstance().getServiceImpl(APEntityService.class);
    }

    public static APDroitAPGService getDroitAPGService() throws JadeApplicationServiceNotAvailableException {
        return (APDroitAPGService) JadeApplicationServiceLocator.getInstance().getServiceImpl(APDroitAPGService.class);
    }

    public static APAnnoncesRapgService getAnnoncesRapgService() throws JadeApplicationServiceNotAvailableException {
        return (APAnnoncesRapgService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                APAnnoncesRapgService.class);
    }

    public static APMontantJournalierApgService getMontantJournalierService()
            throws JadeApplicationServiceNotAvailableException {
        return (APMontantJournalierApgService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                APMontantJournalierApgService.class);
    }

    public static APPlausibilitesApgService getPlausibilitesApgService()
            throws JadeApplicationServiceNotAvailableException {
        return (APPlausibilitesApgService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                APPlausibilitesApgService.class);
    }

    public static APSalaireJournalierApgService getSalaireJournalierService()
            throws JadeApplicationServiceNotAvailableException {
        return (APSalaireJournalierApgService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                APSalaireJournalierApgService.class);
    }

    /**
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée dans le cas où le service n'est pas accessible
     */
    public static APRulesService getRulesService() throws JadeApplicationServiceNotAvailableException {
        return (APRulesService) JadeApplicationServiceLocator.getInstance().getServiceImpl(APRulesService.class);
    }
}
