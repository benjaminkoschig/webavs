package ch.globaz.al.businessimpl.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.al.business.exceptions.model.prestation.ALRecapitulatifEntrepriseImpressionModelException;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexSearchModel;
import ch.globaz.al.business.services.models.prestation.RecapitulatifEntrepriseImpressionComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * 
 /** Classe d'implémentation des services <code>RecapitulatifEntrepriseImpressionComplexModel</code>
 * 
 * @author PTA
 * @see ch.globaz.al.business.services.models.prestation.RecapitulatifEntrepriseImpressionComplexModelService
 */

public class RecapitulatifEntrepriseImpressionComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        RecapitulatifEntrepriseImpressionComplexModelService {

    @Override
    public RecapitulatifEntrepriseImpressionComplexSearchModel search(
            RecapitulatifEntrepriseImpressionComplexSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionComplexModelServiceImpl#search : searchModel is null");
        }
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        return (RecapitulatifEntrepriseImpressionComplexSearchModel) JadePersistenceManager.search(searchModel);

    }

}
