package ch.globaz.naos.businessimpl.service;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.naos.business.model.AffiliationComplexModel;
import ch.globaz.naos.business.model.AffiliationComplexModelSearch;
import ch.globaz.naos.business.service.AffiliationComplexService;

public class AffiliationComplexServiceImpl implements AffiliationComplexService {

    @Override
    public int count(AffiliationComplexModelSearch search) throws Exception, JadePersistenceException {
        if (search == null) {
            throw new Exception("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public AffiliationComplexModel read(String idAffiliation) throws Exception {
        if (idAffiliation == null) {
            throw new Exception("Unable to read idAffiliationComplexService, the model passed is null!");
        }
        AffiliationComplexModel affiliationComplexModel = new AffiliationComplexModel();
        affiliationComplexModel.setId(idAffiliation);
        return (AffiliationComplexModel) JadePersistenceManager.read(affiliationComplexModel);
    }

    @Override
    public AffiliationComplexModelSearch search(AffiliationComplexModelSearch search) throws JadePersistenceException,
            Exception {
        if (search == null) {
            throw new Exception("Unable to search search, the model passed is null!");
        }
        return (AffiliationComplexModelSearch) JadePersistenceManager.search(search);
    }

}
