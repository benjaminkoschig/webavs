/**
 * 
 */
package ch.globaz.al.businessimpl.services.models.envoi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.envoi.ALEnvoiTemplateException;
import ch.globaz.al.business.models.envoi.EnvoiTemplateSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiTemplateSimpleModelSearch;
import ch.globaz.al.business.services.models.envoi.EnvoiTemplateSimpleModelService;
import ch.globaz.al.businessimpl.checker.model.envoi.EnvoiTemplateSimpleModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * @author dhi
 * 
 */
public class EnvoiTemplateSimpleModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        EnvoiTemplateSimpleModelService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiTemplateSimpleModelService#count(ch.globaz.al.business.models
     * .envoi.EnvoiTemplateSimpleModelSearch)
     */
    @Override
    public int count(EnvoiTemplateSimpleModelSearch envoiTemplateSearch) throws ALEnvoiTemplateException,
            JadePersistenceException {

        if (envoiTemplateSearch == null) {
            throw new ALEnvoiTemplateException(
                    "Unable to count for results, the envoiTemplateSearch passed as parameter is null");
        }

        return JadePersistenceManager.count(envoiTemplateSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiTemplateSimpleModelService#create(ch.globaz.al.business.models
     * .envoi.EnvoiTemplateSimpleModel)
     */
    @Override
    public EnvoiTemplateSimpleModel create(EnvoiTemplateSimpleModel envoiTemplate) throws ALEnvoiTemplateException,
            JadePersistenceException {

        if (envoiTemplate == null) {
            throw new ALEnvoiTemplateException(
                    "Unable to create an envoiTemplate, the envoiTemplate passed as parameter is null");
        }

        EnvoiTemplateSimpleModelChecker.validateForCreate(envoiTemplate);

        return (EnvoiTemplateSimpleModel) JadePersistenceManager.add(envoiTemplate);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiTemplateSimpleModelService#delete(ch.globaz.al.business.models
     * .envoi.EnvoiTemplateSimpleModel)
     */
    @Override
    public EnvoiTemplateSimpleModel delete(EnvoiTemplateSimpleModel envoiTemplate) throws ALEnvoiTemplateException,
            JadePersistenceException {

        if (envoiTemplate == null) {
            throw new ALEnvoiTemplateException(
                    "Unable to delete an envoiTemplate, the envoiTemplate passed as parameter is null");
        }

        EnvoiTemplateSimpleModelChecker.validateForDelete(envoiTemplate);

        return (EnvoiTemplateSimpleModel) JadePersistenceManager.delete(envoiTemplate);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.envoi.EnvoiTemplateSimpleModelService#read(java.lang.String)
     */
    @Override
    public EnvoiTemplateSimpleModel read(String idEnvoiTemplate) throws ALEnvoiTemplateException,
            JadePersistenceException {

        if (JadeStringUtil.isEmpty(idEnvoiTemplate)) {
            throw new ALEnvoiTemplateException("Unable to read an envoiTemplate, the id passed as parameter is empty");
        }

        EnvoiTemplateSimpleModel envoiTemplate = new EnvoiTemplateSimpleModel();
        envoiTemplate.setId(idEnvoiTemplate);

        return (EnvoiTemplateSimpleModel) JadePersistenceManager.read(envoiTemplate);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiTemplateSimpleModelService#search(ch.globaz.al.business.models
     * .envoi.EnvoiTemplateSimpleModelSearch)
     */
    @Override
    public EnvoiTemplateSimpleModelSearch search(EnvoiTemplateSimpleModelSearch envoiTemplateSearch)
            throws ALEnvoiTemplateException, JadePersistenceException {

        if (envoiTemplateSearch == null) {
            throw new ALEnvoiTemplateException(
                    "Unable to search for results, the envoiTemplateSearch passed as parameter is null");
        }

        return (EnvoiTemplateSimpleModelSearch) JadePersistenceManager.search(envoiTemplateSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiTemplateSimpleModelService#update(ch.globaz.al.business.models
     * .envoi.EnvoiTemplateSimpleModel)
     */
    @Override
    public EnvoiTemplateSimpleModel update(EnvoiTemplateSimpleModel envoiTemplate) throws ALEnvoiTemplateException,
            JadePersistenceException {

        if (envoiTemplate == null) {
            throw new ALEnvoiTemplateException(
                    "Unable to update an envoiTemplate, the envoiTemplate passed as parameter is null");
        }

        EnvoiTemplateSimpleModelChecker.validateForUpdate(envoiTemplate);

        return (EnvoiTemplateSimpleModel) JadePersistenceManager.update(envoiTemplate);
    }

}
