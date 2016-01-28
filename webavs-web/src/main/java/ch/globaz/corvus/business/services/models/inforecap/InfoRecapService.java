package ch.globaz.corvus.business.services.models.inforecap;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.corvus.business.exceptions.models.InfoRecapException;
import ch.globaz.corvus.business.models.recapinfo.SimpleInfoRecapSearch;

public interface InfoRecapService extends JadeApplicationService {
    public SimpleInfoRecapSearch search(SimpleInfoRecapSearch search) throws InfoRecapException,
            JadePersistenceException;
}
