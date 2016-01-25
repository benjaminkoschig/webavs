package ch.globaz.al.businessimpl.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.al.business.exceptions.model.prestation.ALRecapitulatifEntrepriseListModelException;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseListComplexSearchModel;
import ch.globaz.al.business.services.models.prestation.RecapitulatifEntrepriseListComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

public class RecapitulatifEntrepriseListComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        RecapitulatifEntrepriseListComplexModelService {

    @Override
    public RecapitulatifEntrepriseListComplexSearchModel search(
            RecapitulatifEntrepriseListComplexSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new ALRecapitulatifEntrepriseListModelException(
                    "RecapitulatifEntrepriseListComplexModelServiceImpl#search : searchModel is null");
        }
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        return (RecapitulatifEntrepriseListComplexSearchModel) JadePersistenceManager.search(searchModel);
    }

}
