package ch.globaz.lyra.businessimpl.services;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.lyra.business.exceptions.LYException;
import ch.globaz.lyra.business.exceptions.LYTechnicalException;
import ch.globaz.lyra.business.models.historique.LYSimpleHistorique;
import ch.globaz.lyra.business.models.historique.LYSimpleHistoriqueSearchModel;
import ch.globaz.lyra.business.services.LYHistoriqueEcheancesService;

public class LYHistoriqueEcheancesServiceImpl implements LYHistoriqueEcheancesService {

    @Override
    public void add(LYSimpleHistorique historique) throws LYException, JadePersistenceException {
        if (historique == null) {
            throw new LYTechnicalException("entity null");
        }
        JadePersistenceManager.add(historique);
    }

    @Override
    public LYSimpleHistorique read(String idHistorique) throws LYException, JadePersistenceException {

        LYSimpleHistoriqueSearchModel searchModel = new LYSimpleHistoriqueSearchModel();
        searchModel.setForIdHistorique(idHistorique);

        searchModel = (LYSimpleHistoriqueSearchModel) JadePersistenceManager.search(searchModel);

        if (searchModel.getSearchResults().length == 1) {
            return (LYSimpleHistorique) searchModel.getSearchResults()[0];
        }
        return null;
    }

    @Override
    public LYSimpleHistoriqueSearchModel search(LYSimpleHistoriqueSearchModel searchModel) throws LYException,
            JadePersistenceException {

        if (searchModel == null) {
            throw new LYTechnicalException("search model null");
        }
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchModel = (LYSimpleHistoriqueSearchModel) JadePersistenceManager.search(searchModel);

        return searchModel;
    }
}
