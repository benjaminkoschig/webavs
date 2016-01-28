/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.controleurEnvoi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.controleurEnvoi.ControleurEnvoiException;
import ch.globaz.amal.business.exceptions.models.controleurJob.ControleurJobException;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJobSearch;
import ch.globaz.amal.businessimpl.checkers.controleurEnvoi.SimpleControleurJobChecker;

/**
 * @author DHI
 * 
 */
public class SimpleControleurJobServiceImpl implements
        ch.globaz.amal.business.services.models.controleurEnvoi.SimpleControleurJobService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.SimpleControleurJobService#create(ch.globaz.amal.business
     * .models.controleurEnvoi.SimpleControleurJob)
     */
    @Override
    public SimpleControleurJob create(SimpleControleurJob simpleControleurJob) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, ControleurJobException {
        if (simpleControleurJob == null) {
            throw new ControleurJobException("Unable to create a controleur job, the model passed is null");
        }
        SimpleControleurJobChecker.checkForCreate(simpleControleurJob);
        return (SimpleControleurJob) JadePersistenceManager.add(simpleControleurJob);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.SimpleControleurJobService#delete(ch.globaz.amal.business
     * .models.controleurEnvoi.SimpleControleurJob)
     */
    @Override
    public SimpleControleurJob delete(SimpleControleurJob simpleControleurJob) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, ControleurEnvoiException, ControleurJobException {
        if (simpleControleurJob == null) {
            throw new ControleurJobException("Unable to delete a simple controleur job, the model passed is null");
        }
        // check the number of children
        // SimpleControleurEnvoiStatusSearch statusSearch = new SimpleControleurEnvoiStatusSearch();
        // statusSearch.setForIdJob(simpleControleurJob.getIdJob());
        // statusSearch = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().search(statusSearch);
        // if (statusSearch.getSize() > 0) {
        // return simpleControleurJob;
        // } else {
        // SimpleControleurJobChecker.checkForDelete(simpleControleurJob);
        // return (SimpleControleurJob) JadePersistenceManager.delete(simpleControleurJob);
        // }
        SimpleControleurJobChecker.checkForDelete(simpleControleurJob);
        return (SimpleControleurJob) JadePersistenceManager.delete(simpleControleurJob);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.controleurEnvoi.SimpleControleurJobService#read(java.lang.String)
     */
    @Override
    public SimpleControleurJob read(String idJob) throws JadePersistenceException, ControleurJobException {
        if (JadeStringUtil.isEmpty(idJob)) {
            throw new ControleurJobException("Unable to read the job, the id passed is empty");
        }
        SimpleControleurJob simpleControleurJob = new SimpleControleurJob();
        simpleControleurJob.setId(idJob);
        return (SimpleControleurJob) JadePersistenceManager.read(simpleControleurJob);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.SimpleControleurJobService#search(ch.globaz.amal.business
     * .models.controleurEnvoi.SimpleControleurJobSearch)
     */
    @Override
    public SimpleControleurJobSearch search(SimpleControleurJobSearch search) throws JadePersistenceException,
            ControleurJobException {
        if (search == null) {
            throw new ControleurJobException("Unable to search for a job, the model passed is null");
        }
        return (SimpleControleurJobSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.SimpleControleurJobService#update(ch.globaz.amal.business
     * .models.controleurEnvoi.SimpleControleurJob)
     */
    @Override
    public SimpleControleurJob update(SimpleControleurJob simpleControleurJob) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, ControleurJobException {
        if (simpleControleurJob == null) {
            throw new ControleurJobException("Unable to update the job, the model passed is null");
        }
        SimpleControleurJobChecker.checkForUpdate(simpleControleurJob);
        return (SimpleControleurJob) JadePersistenceManager.update(simpleControleurJob);
    }

}
