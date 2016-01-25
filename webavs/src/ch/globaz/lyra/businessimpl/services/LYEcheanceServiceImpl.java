package ch.globaz.lyra.businessimpl.services;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.lyra.business.exceptions.LYException;
import ch.globaz.lyra.business.exceptions.LYTechnicalException;
import ch.globaz.lyra.business.models.echeance.LYSimpleEcheance;
import ch.globaz.lyra.business.models.echeance.LYSimpleEcheanceSearchModel;
import ch.globaz.lyra.business.services.LYEcheanceService;

public class LYEcheanceServiceImpl implements LYEcheanceService {

    @Override
    public LYSimpleEcheance read(String idEcheance) throws LYException, JadePersistenceException {

        LYSimpleEcheanceSearchModel searchModel = new LYSimpleEcheanceSearchModel();
        searchModel.setForIdEcheance(idEcheance);

        searchModel = (LYSimpleEcheanceSearchModel) JadePersistenceManager.search(searchModel);

        if (searchModel.getSearchResults().length == 1) {
            return (LYSimpleEcheance) searchModel.getSearchResults()[0];
        }
        return null;
    }

    @Override
    public LYSimpleEcheanceSearchModel search(LYSimpleEcheanceSearchModel searchModel) throws LYException,
            JadePersistenceException {

        if (searchModel == null) {
            throw new LYTechnicalException("search model null");
        }
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchModel = (LYSimpleEcheanceSearchModel) JadePersistenceManager.search(searchModel);

        return searchModel;
    }
}
