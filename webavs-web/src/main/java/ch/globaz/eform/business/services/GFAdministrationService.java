package ch.globaz.eform.business.services;

import ch.globaz.eform.business.search.GFAdministrationSearch;
import globaz.jade.service.provider.application.JadeApplicationService;

public interface GFAdministrationService extends JadeApplicationService {
    GFAdministrationSearch find(GFAdministrationSearch search);
}
