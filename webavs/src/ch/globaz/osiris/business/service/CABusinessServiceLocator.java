package ch.globaz.osiris.business.service;

import globaz.jade.service.provider.JadeApplicationServiceLocator;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

/**
 * @author SCO 18 mai 2010
 */
public class CABusinessServiceLocator {
    public static CompteAnnexeService getCompteAnnexeService() throws JadeApplicationServiceNotAvailableException {
        return (CompteAnnexeService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CompteAnnexeService.class);
    }

    public static JournalService getJournalService() throws JadeApplicationServiceNotAvailableException {
        return (JournalService) JadeApplicationServiceLocator.getInstance().getServiceImpl(JournalService.class);
    }

    public static OrdreGroupeService getOrdreGroupeService() throws JadeApplicationServiceNotAvailableException {
        return (OrdreGroupeService) JadeApplicationServiceLocator.getInstance()
                .getServiceImpl(OrdreGroupeService.class);
    }

    public static SectionService getSectionService() throws JadeApplicationServiceNotAvailableException {
        return (SectionService) JadeApplicationServiceLocator.getInstance().getServiceImpl(SectionService.class);
    }

    public static CompteCourantService getCompteCourantService() throws JadeApplicationServiceNotAvailableException {
        return (CompteCourantService) JadeApplicationServiceLocator.getInstance().getServiceImpl(
                CompteCourantService.class);
    }

}
