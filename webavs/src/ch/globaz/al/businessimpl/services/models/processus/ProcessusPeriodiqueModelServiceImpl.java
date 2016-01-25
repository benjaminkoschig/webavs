package ch.globaz.al.businessimpl.services.models.processus;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.processus.ALProcessusException;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueSearchModel;
import ch.globaz.al.business.services.models.processus.ProcessusPeriodiqueModelService;
import ch.globaz.al.businessimpl.checker.model.processus.ProcessusPeriodiqueModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Classe d'implémentation des services persistence des processus périodique
 * 
 * @author GMO
 * 
 */
public class ProcessusPeriodiqueModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        ProcessusPeriodiqueModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.processus.ProcessusPeriodiqueService
     * #create(ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel)
     */
    @Override
    public ProcessusPeriodiqueModel create(ProcessusPeriodiqueModel processusPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException {

        if (processusPeriodiqueModel == null) {
            throw new ALProcessusException(
                    "ProcessusPeriodiqueServiceImpl#create: unable to create processusPeriodiqueModel - processusPeriodiqueModel is null ");
        }

        ProcessusPeriodiqueModelChecker.validate(processusPeriodiqueModel);

        return (ProcessusPeriodiqueModel) JadePersistenceManager.add(processusPeriodiqueModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.processus.ProcessusPeriodiqueService
     * #delete(ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel)
     */
    @Override
    public ProcessusPeriodiqueModel delete(ProcessusPeriodiqueModel processusPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException {
        if (processusPeriodiqueModel == null) {
            throw new ALProcessusException(
                    "ProcessusPeriodiqueServiceImpl#delete: unable to delete processusPeriodiqueModel - processusPeriodiqueModel is null ");
        }

        ProcessusPeriodiqueModelChecker.validateForDelete(processusPeriodiqueModel);

        return (ProcessusPeriodiqueModel) JadePersistenceManager.delete(processusPeriodiqueModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.processus.ProcessusPeriodiqueService #read(java.lang.String)
     */
    @Override
    public ProcessusPeriodiqueModel read(String idProcessusPeriodique) throws JadeApplicationException,
            JadePersistenceException {
        if (JadeStringUtil.isBlankOrZero(idProcessusPeriodique)) {
            throw new ALProcessusException(
                    "ProcessusPeriodiqueServiceImpl#read: unable to read processusPeriodiqueModel - idProcessusPeriodique is null ");
        }

        ProcessusPeriodiqueModel processusPeriodique = new ProcessusPeriodiqueModel();
        processusPeriodique.setId(idProcessusPeriodique);
        return (ProcessusPeriodiqueModel) JadePersistenceManager.read(processusPeriodique);
    }

    @Override
    public ProcessusPeriodiqueSearchModel search(ProcessusPeriodiqueSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (searchModel == null) {
            throw new ALProcessusException(
                    "ProcessusPeriodiqueServiceImpl#search: unable to search processusPeriodiqueSearchModel - searchModel is null");
        }

        return (ProcessusPeriodiqueSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.processus.ProcessusPeriodiqueService
     * #update(ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel)
     */
    @Override
    public ProcessusPeriodiqueModel update(ProcessusPeriodiqueModel processusPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException {
        if (processusPeriodiqueModel == null) {
            throw new ALProcessusException(
                    "ProcessusPeriodiqueServiceImpl#update: unable to update processusPeriodiqueModel - processusPeriodiqueModel is null ");
        }

        ProcessusPeriodiqueModelChecker.validate(processusPeriodiqueModel);
        return (ProcessusPeriodiqueModel) JadePersistenceManager.update(processusPeriodiqueModel);
    }

}
