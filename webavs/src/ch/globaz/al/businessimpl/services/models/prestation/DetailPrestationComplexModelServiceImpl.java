package ch.globaz.al.businessimpl.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.droit.ALDroitModelException;
import ch.globaz.al.business.exceptions.model.prestation.ALDetailPrestationModelException;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;
import ch.globaz.al.business.services.models.prestation.DetailPrestationComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Classe d'implémentation des services <code>DetailPrestationModel</code>
 * 
 * @author jts
 * @see ch.globaz.al.business.services.models.prestation.DetailPrestationGenComplexModelService
 */
public class DetailPrestationComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        DetailPrestationComplexModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.droit.DroitModelService#count(ch
     * .globaz.al.business.models.droit.DroitSearch)
     */
    @Override
    public int count(DetailPrestationComplexSearchModel searchModel) throws JadePersistenceException,
            JadeApplicationException {

        if (searchModel == null) {
            throw new ALDroitModelException(
                    "DetailPrestationComplexModelServiceImpl#count : Unable to count, searchModel is null");
        }

        return JadePersistenceManager.count(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation. DetailPrestationComplexModelService
     * #search(ch.globaz.al.business.models.prestation .DetailPrestationComplexSearchModel)
     */
    @Override
    public DetailPrestationComplexSearchModel search(DetailPrestationComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException {

        if (searchModel == null) {
            throw new ALDetailPrestationModelException(
                    "DetailPrestationComplexModelServiceImpl#search : searchModel is null");
        }

        return (DetailPrestationComplexSearchModel) JadePersistenceManager.search(searchModel);
    }
}
