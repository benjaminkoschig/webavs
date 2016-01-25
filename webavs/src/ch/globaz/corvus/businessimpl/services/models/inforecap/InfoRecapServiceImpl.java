package ch.globaz.corvus.businessimpl.services.models.inforecap;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.corvus.business.exceptions.models.InfoRecapException;
import ch.globaz.corvus.business.models.recapinfo.SimpleInfoRecapSearch;
import ch.globaz.corvus.business.services.models.inforecap.InfoRecapService;

public class InfoRecapServiceImpl implements InfoRecapService {

    @Override
    public SimpleInfoRecapSearch search(SimpleInfoRecapSearch search) throws InfoRecapException,
            JadePersistenceException {
        if (search == null) {
            throw new InfoRecapException("Unable to search SimpleInfoRecap, the search model passed is null!");
        }
        return (SimpleInfoRecapSearch) JadePersistenceManager.search(search);
    }
}
