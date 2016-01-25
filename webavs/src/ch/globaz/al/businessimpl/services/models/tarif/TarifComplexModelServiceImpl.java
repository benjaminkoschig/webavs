/**
 * 
 */
package ch.globaz.al.businessimpl.services.models.tarif;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.tarif.ALTarifComplexModelException;
import ch.globaz.al.business.models.tarif.TarifComplexSearchModel;
import ch.globaz.al.business.services.models.tarif.TarifComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * classe d'implémentation des services de la persistance de PrestationTarifComplexModel
 * 
 * @author PTA
 * 
 */
public class TarifComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements TarifComplexModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.tarif.PrestationTarifComplexModelService #search
     * (ch.globaz.al.business.model.tarif.PrestationTarifComplexSearchModel)
     */
    @Override
    public TarifComplexSearchModel search(TarifComplexSearchModel tarifComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (tarifComplexSearchModel == null) {
            throw new ALTarifComplexModelException(
                    "unable to search dossierDetailComplexModel- the model passed is null");
        }
        return (TarifComplexSearchModel) JadePersistenceManager.search(tarifComplexSearchModel);

    }

}
