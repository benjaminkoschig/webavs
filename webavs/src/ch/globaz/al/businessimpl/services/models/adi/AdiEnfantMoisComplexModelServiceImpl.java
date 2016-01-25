package ch.globaz.al.businessimpl.services.models.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.adi.ALAdiEnfantMoisModelException;
import ch.globaz.al.business.models.adi.AdiEnfantMoisComplexSearchModel;
import ch.globaz.al.business.services.models.adi.AdiEnfantMoisComplexModelService;

/**
 * Implémentation des services de la persistance AdiEnfantMoisComplexModel
 * 
 * @author GMO
 * 
 */
public class AdiEnfantMoisComplexModelServiceImpl implements AdiEnfantMoisComplexModelService {
    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.adi.AdiEnfantMoisComplexModelService
     * #search(ch.globaz.al.business.models.adi.AdiEnfantMoisComplexSearchModel)
     */
    @Override
    public AdiEnfantMoisComplexSearchModel search(AdiEnfantMoisComplexSearchModel adiEnfantMoisComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (adiEnfantMoisComplexSearchModel == null) {
            throw new ALAdiEnfantMoisModelException(
                    "AdiEnfantMoisComplexModelServiceImpl#search: unable to search, adiEnfantMoisComplexSearchModel is null");
        }
        return (AdiEnfantMoisComplexSearchModel) JadePersistenceManager.search(adiEnfantMoisComplexSearchModel);
    }

}
