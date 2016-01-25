/**
 * 
 */
package ch.globaz.al.businessimpl.services.models.envoi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.envoi.ALEnvoiItemException;
import ch.globaz.al.business.models.envoi.EnvoiComplexModel;
import ch.globaz.al.business.models.envoi.EnvoiComplexModelSearch;
import ch.globaz.al.business.models.envoi.EnvoiItemSimpleModel;
import ch.globaz.al.business.services.models.envoi.EnvoiComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * @author dhi
 * 
 */
public class EnvoiComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements EnvoiComplexModelService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiComplexModelService#count(ch.globaz.al.business.models.envoi
     * .EnvoiComplexModelSearch)
     */
    @Override
    public int count(EnvoiComplexModelSearch envoiComplexSearch) throws JadePersistenceException,
            JadeApplicationException {

        if (envoiComplexSearch == null) {
            throw new ALEnvoiItemException(
                    "Unable to count for results, the envoiComplexSearch passed as parameter is null");
        }

        return JadePersistenceManager.count(envoiComplexSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiComplexModelService#delete(ch.globaz.al.business.models.envoi
     * .EnvoiComplexModel)
     */
    @Override
    public EnvoiComplexModel delete(EnvoiComplexModel envoiComplex) throws JadeApplicationException,
            JadePersistenceException {

        if (envoiComplex == null) {
            throw new ALEnvoiItemException(
                    "Unable to delete an envoiComplex, the envoiComplex passed as parameter is null");
        }

        EnvoiItemSimpleModel envoiItem = ALImplServiceLocator.getEnvoiItemSimpleModelService().delete(
                envoiComplex.getEnvoiItemSimpleModel());
        envoiComplex.setEnvoiItemSimpleModel(envoiItem);

        return envoiComplex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.envoi.EnvoiComplexModelService#read(java.lang.String)
     */
    @Override
    public EnvoiComplexModel read(String idEnvoiComplexModel) throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(idEnvoiComplexModel)) {
            throw new ALEnvoiItemException("Unable to read an EnvoiComplexModel, the id passed as parameter is empty");
        }

        EnvoiComplexModelSearch envoiSearch = new EnvoiComplexModelSearch();
        envoiSearch.setForIdEnvoi(idEnvoiComplexModel);
        envoiSearch = (EnvoiComplexModelSearch) JadePersistenceManager.search(envoiSearch);
        if (envoiSearch.getSize() == 1) {
            return (EnvoiComplexModel) envoiSearch.getSearchResults()[0];
        } else {
            return new EnvoiComplexModel();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiComplexModelService#search(ch.globaz.al.business.models.envoi
     * .EnvoiComplexModelSearch)
     */
    @Override
    public EnvoiComplexModelSearch search(EnvoiComplexModelSearch envoiComplexSearch) throws JadeApplicationException,
            JadePersistenceException {

        if (envoiComplexSearch == null) {
            throw new ALEnvoiItemException(
                    "Unable to search for results, the envoiComplexSearch passed as parameter is null");
        }

        return (EnvoiComplexModelSearch) JadePersistenceManager.search(envoiComplexSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiComplexModelService#update(ch.globaz.al.business.models.envoi
     * .EnvoiComplexModel)
     */
    @Override
    public EnvoiComplexModel update(EnvoiComplexModel envoiComplex) throws JadeApplicationException,
            JadePersistenceException {

        if (envoiComplex == null) {
            throw new ALEnvoiItemException(
                    "Unable to delete an envoiComplex, the envoiComplex passed as parameter is null");
        }

        EnvoiItemSimpleModel envoiItem = ALImplServiceLocator.getEnvoiItemSimpleModelService().update(
                envoiComplex.getEnvoiItemSimpleModel());
        envoiComplex.setEnvoiItemSimpleModel(envoiItem);

        return envoiComplex;
    }

}
