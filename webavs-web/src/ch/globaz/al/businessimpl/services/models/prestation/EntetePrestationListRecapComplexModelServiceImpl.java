package ch.globaz.al.businessimpl.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.prestation.ALEntetePrestationModelException;
import ch.globaz.al.business.models.prestation.EntetePrestationListRecapComplexSearchModel;
import ch.globaz.al.business.services.models.prestation.EntetePrestationListRecapComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * 
 * Classe d'implémentation des services <code>DetailPrestationModel</code>
 * 
 * @author GMO
 * @see ch.globaz.al.business.services.models.prestation.EntetePrestationListRecapComplexModelService
 * 
 */
public class EntetePrestationListRecapComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        EntetePrestationListRecapComplexModelService {
    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation. EntetePrestationListRecapComplexModelService
     * #search(ch.globaz.al.business. models.prestation.EntetePrestationListRecapComplexSearchModel)
     */
    @Override
    public EntetePrestationListRecapComplexSearchModel search(
            EntetePrestationListRecapComplexSearchModel prestationSearchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (prestationSearchModel == null) {
            throw new ALEntetePrestationModelException(
                    "EntetePrestationListRecapComplexModelServiceImpl#search : unable to search, prestationSearchModel is null");
        }
        return (EntetePrestationListRecapComplexSearchModel) JadePersistenceManager.search(prestationSearchModel);
    }

}
