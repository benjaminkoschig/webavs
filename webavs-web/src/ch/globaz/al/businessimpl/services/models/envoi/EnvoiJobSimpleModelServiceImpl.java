/**
 * 
 */
package ch.globaz.al.businessimpl.services.models.envoi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.envoi.ALEnvoiJobException;
import ch.globaz.al.business.models.envoi.EnvoiJobSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiJobSimpleModelSearch;
import ch.globaz.al.business.services.models.envoi.EnvoiJobSimpleModelService;
import ch.globaz.al.businessimpl.checker.model.envoi.EnvoiJobSimpleModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * @author dhi
 * 
 */
public class EnvoiJobSimpleModelServiceImpl extends ALAbstractBusinessServiceImpl implements EnvoiJobSimpleModelService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiJobSimpleModelService#count(ch.globaz.al.business.models.envoi
     * .EnvoiJobSimpleModelSearch)
     */
    @Override
    public int count(EnvoiJobSimpleModelSearch envoiJobSearch) throws ALEnvoiJobException, JadePersistenceException {

        if (envoiJobSearch == null) {
            throw new ALEnvoiJobException("Unable to count for results, the envoiJobSearch passed as parameter is null");
        }

        return JadePersistenceManager.count(envoiJobSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiJobSimpleModelService#create(ch.globaz.al.business.models.envoi
     * .EnvoiJobSimpleModel)
     */
    @Override
    public EnvoiJobSimpleModel create(EnvoiJobSimpleModel envoiJob) throws ALEnvoiJobException,
            JadePersistenceException {

        if (envoiJob == null) {
            throw new ALEnvoiJobException(
                    "Unable to create an envoiTemplate, the envoiTemplate passed as parameter is null");
        }

        EnvoiJobSimpleModelChecker.validateForCreate(envoiJob);

        return (EnvoiJobSimpleModel) JadePersistenceManager.add(envoiJob);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiJobSimpleModelService#delete(ch.globaz.al.business.models.envoi
     * .EnvoiJobSimpleModel)
     */
    @Override
    public EnvoiJobSimpleModel delete(EnvoiJobSimpleModel envoiJob) throws ALEnvoiJobException,
            JadePersistenceException {

        if (envoiJob == null) {
            throw new ALEnvoiJobException("Unable to delete an envoiJob, the envoiJob passed as parameter is null");
        }

        EnvoiJobSimpleModelChecker.validateForDelete(envoiJob);

        return (EnvoiJobSimpleModel) JadePersistenceManager.delete(envoiJob);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.envoi.EnvoiJobSimpleModelService#read(java.lang.String)
     */
    @Override
    public EnvoiJobSimpleModel read(String idEnvoiJob) throws ALEnvoiJobException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(idEnvoiJob)) {
            throw new ALEnvoiJobException("Unable to read an envoiJob, the id passed as parameter is empty");
        }

        EnvoiJobSimpleModel envoiJob = new EnvoiJobSimpleModel();
        envoiJob.setId(idEnvoiJob);

        return (EnvoiJobSimpleModel) JadePersistenceManager.read(envoiJob);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiJobSimpleModelService#search(ch.globaz.al.business.models.envoi
     * .EnvoiJobSimpleModelSearch)
     */
    @Override
    public EnvoiJobSimpleModelSearch search(EnvoiJobSimpleModelSearch envoiJobSearch) throws ALEnvoiJobException,
            JadePersistenceException {

        if (envoiJobSearch == null) {
            throw new ALEnvoiJobException(
                    "Unable to search for results, the envoiJobSearch passed as parameter is null");
        }

        return (EnvoiJobSimpleModelSearch) JadePersistenceManager.search(envoiJobSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiJobSimpleModelService#update(ch.globaz.al.business.models.envoi
     * .EnvoiJobSimpleModel)
     */
    @Override
    public EnvoiJobSimpleModel update(EnvoiJobSimpleModel envoiJob) throws ALEnvoiJobException,
            JadePersistenceException {

        if (envoiJob == null) {
            throw new ALEnvoiJobException("Unable to update an envoiJob, the envoiJob passed as parameter is null");
        }

        EnvoiJobSimpleModelChecker.validateForUpdate(envoiJob);

        return (EnvoiJobSimpleModel) JadePersistenceManager.update(envoiJob);
    }

}
