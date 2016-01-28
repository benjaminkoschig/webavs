package ch.globaz.vulpecula.businessimpl.services.is;

import ch.globaz.vulpecula.business.models.is.ProcessusAFComplexModel;
import ch.globaz.vulpecula.business.models.is.ProcessusAFSearchComplexModel;
import ch.globaz.vulpecula.business.services.is.ProcessusAFService;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;

public class ProcessusAFServiceImpl implements ProcessusAFService {

    @Override
    public ProcessusAFComplexModel getProcessusAF(String idProcessus) {
        ProcessusAFSearchComplexModel searchModel = new ProcessusAFSearchComplexModel();
        searchModel.setForId(idProcessus);
        RepositoryJade.searchFor(searchModel);
        return (ProcessusAFComplexModel) searchModel.getSearchResults()[0];
    }

}
