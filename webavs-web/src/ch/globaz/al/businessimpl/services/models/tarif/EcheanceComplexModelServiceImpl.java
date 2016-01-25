package ch.globaz.al.businessimpl.services.models.tarif;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.tarif.ALEcheanceModelException;
import ch.globaz.al.business.models.tarif.EcheanceComplexSearchModel;
import ch.globaz.al.business.services.models.tarif.EcheanceComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * 
 * @author PTA
 * 
 */
public class EcheanceComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        EcheanceComplexModelService {

    @Override
    public EcheanceComplexSearchModel search(EcheanceComplexSearchModel echeanceComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException {

        if (echeanceComplexSearchModel == null) {
            throw new ALEcheanceModelException();
        }

        return (EcheanceComplexSearchModel) JadePersistenceManager.search(echeanceComplexSearchModel);
    }

}
