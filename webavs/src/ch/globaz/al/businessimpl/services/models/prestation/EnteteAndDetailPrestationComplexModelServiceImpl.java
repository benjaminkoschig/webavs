package ch.globaz.al.businessimpl.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.prestation.ALDetailPrestationModelException;
import ch.globaz.al.business.models.prestation.EnteteAndDetailPrestationComplexSearchModel;
import ch.globaz.al.business.services.models.prestation.EnteteAndDetailPrestationComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Classe d'implémentation des services <code>DetailPrestationModel</code>
 * 
 * @author jts
 * @see ch.globaz.al.business.services.models.prestation.DetailPrestationGenComplexModelService
 */
public class EnteteAndDetailPrestationComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        EnteteAndDetailPrestationComplexModelService {

    @Override
    public EnteteAndDetailPrestationComplexSearchModel search(EnteteAndDetailPrestationComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException {

        if (searchModel == null) {
            throw new ALDetailPrestationModelException(
                    "DetailPrestationComplexModelServiceImpl#search : searchModel is null");
        }

        return (EnteteAndDetailPrestationComplexSearchModel) JadePersistenceManager.search(searchModel);
    }
}
