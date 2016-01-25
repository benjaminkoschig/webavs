/**
 * 
 */
package ch.globaz.al.businessimpl.services.models.envoi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.envoi.ALEnvoiParametresException;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModelSearch;
import ch.globaz.al.business.services.models.envoi.EnvoiParametresSimpleModelService;
import ch.globaz.al.businessimpl.checker.model.envoi.EnvoiParametresSimpleModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * @author dhi
 * 
 */
public class EnvoiParametresSimpleModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        EnvoiParametresSimpleModelService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiParametresSimpleModelService#count(ch.globaz.al.business.models
     * .envoi.EnvoiParametresSimpleModelSearch)
     */
    @Override
    public int count(EnvoiParametresSimpleModelSearch envoiParametresSearch) throws ALEnvoiParametresException,
            JadePersistenceException {
        if (envoiParametresSearch == null) {
            throw new ALEnvoiParametresException(
                    "Unable to count for results, the envoiParametresSearch passed as parameter is null");
        }

        return JadePersistenceManager.count(envoiParametresSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiParametresSimpleModelService#create(ch.globaz.al.business.models
     * .envoi.EnvoiParametresSimpleModel)
     */
    @Override
    public EnvoiParametresSimpleModel create(EnvoiParametresSimpleModel envoiParametres)
            throws ALEnvoiParametresException, JadePersistenceException {

        if (envoiParametres == null) {
            throw new ALEnvoiParametresException(
                    "Unable to create an envoiTemplate, the envoiParametres passed as parameter is null");
        }

        EnvoiParametresSimpleModelChecker.validateForCreate(envoiParametres);

        return (EnvoiParametresSimpleModel) JadePersistenceManager.add(envoiParametres);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiParametresSimpleModelService#delete(ch.globaz.al.business.models
     * .envoi.EnvoiParametresSimpleModel)
     */
    @Override
    public EnvoiParametresSimpleModel delete(EnvoiParametresSimpleModel envoiParametres)
            throws ALEnvoiParametresException, JadePersistenceException {

        if (envoiParametres == null) {
            throw new ALEnvoiParametresException(
                    "Unable to delete an envoiParametres, the envoiParametres passed as parameter is null");
        }

        EnvoiParametresSimpleModelChecker.validateForDelete(envoiParametres);

        return (EnvoiParametresSimpleModel) JadePersistenceManager.delete(envoiParametres);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.envoi.EnvoiParametresSimpleModelService#read(java.lang.String)
     */
    @Override
    public EnvoiParametresSimpleModel read(String idEnvoiParametres) throws ALEnvoiParametresException,
            JadePersistenceException {

        if (JadeStringUtil.isEmpty(idEnvoiParametres)) {
            throw new ALEnvoiParametresException(
                    "Unable to read an EnvoiParametresSimpleModel, the id passed as parameter is empty");
        }

        EnvoiParametresSimpleModel envoiParametres = new EnvoiParametresSimpleModel();
        envoiParametres.setId(idEnvoiParametres);

        return (EnvoiParametresSimpleModel) JadePersistenceManager.read(envoiParametres);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiParametresSimpleModelService#search(ch.globaz.al.business.models
     * .envoi.EnvoiParametresSimpleModelSearch)
     */
    @Override
    public EnvoiParametresSimpleModelSearch search(EnvoiParametresSimpleModelSearch envoiParametresSearch)
            throws ALEnvoiParametresException, JadePersistenceException {

        if (envoiParametresSearch == null) {
            throw new ALEnvoiParametresException(
                    "Unable to search for results, the envoiParametresSearch passed as parameter is null");
        }

        return (EnvoiParametresSimpleModelSearch) JadePersistenceManager.search(envoiParametresSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiParametresSimpleModelService#update(ch.globaz.al.business.models
     * .envoi.EnvoiParametresSimpleModel)
     */
    @Override
    public EnvoiParametresSimpleModel update(EnvoiParametresSimpleModel envoiParametres)
            throws ALEnvoiParametresException, JadePersistenceException {

        if (envoiParametres == null) {
            throw new ALEnvoiParametresException(
                    "Unable to update an envoiParametres, the envoiParametres passed as parameter is null");
        }

        EnvoiParametresSimpleModelChecker.validateForUpdate(envoiParametres);

        return (EnvoiParametresSimpleModel) JadePersistenceManager.update(envoiParametres);
    }

}
