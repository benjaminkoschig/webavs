package ch.globaz.al.businessimpl.services.models.periodeAF;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.periodeAF.ALPeriodeAFException;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;
import ch.globaz.al.business.models.periodeAF.PeriodeAFSearchModel;
import ch.globaz.al.business.services.models.periodeAF.PeriodeAFModelService;
import ch.globaz.al.businessimpl.checker.model.periodeAF.PeriodeAFModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation des services persistence liés à la période AF
 * 
 * @author GMO
 * 
 */
public class PeriodeAFModelServiceImpl extends ALAbstractBusinessServiceImpl implements PeriodeAFModelService {

    @Override
    public PeriodeAFModel create(PeriodeAFModel periodeAFModel) throws JadeApplicationException,
            JadePersistenceException {
        if (periodeAFModel == null) {
            throw new ALPeriodeAFException(
                    "PeriodeAFModelServiceImpl#create: unable to create - the model passed si null");
        }

        PeriodeAFModelChecker.validate(periodeAFModel);
        return (PeriodeAFModel) JadePersistenceManager.add(periodeAFModel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.periodeAF.PeriodeAFModelService #read(java.lang.String)
     */
    @Override
    public PeriodeAFModel read(String idPeriodeAF) throws JadeApplicationException, JadePersistenceException {
        if (idPeriodeAF == null) {
            throw new ALPeriodeAFException("PeriodeAFModelServiceImpl#read: unable to read - the id passed si null");
        }

        PeriodeAFModel periodeAFModel = new PeriodeAFModel();
        periodeAFModel.setId(idPeriodeAF);
        return (PeriodeAFModel) JadePersistenceManager.read(periodeAFModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.periodeAF.PeriodeAFModelService
     * #search(ch.globaz.al.business.models.periodeAF.PeriodeAFSearchModel)
     */
    @Override
    public PeriodeAFSearchModel search(PeriodeAFSearchModel periodeSearchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (periodeSearchModel == null) {
            throw new ALPeriodeAFException(
                    "PeriodeAFModelServiceImpl#search: unable to search - the model passed is null");
        }

        return (PeriodeAFSearchModel) JadePersistenceManager.search(periodeSearchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.periodeAF.PeriodeAFModelService
     * #upate(ch.globaz.al.business.models.periodeAF.PeriodeAFModel)
     */
    @Override
    public PeriodeAFModel upate(PeriodeAFModel periodeAFModel) throws JadeApplicationException,
            JadePersistenceException {
        if (periodeAFModel == null) {
            throw new ALPeriodeAFException(
                    "PeriodeAFModelServiceImpl#update: unable to update - the model passed is null");
        }
        return (PeriodeAFModel) JadePersistenceManager.update(periodeAFModel);
    }

}
