/**
 * 
 */
package ch.globaz.al.businessimpl.services.models.envoi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.constantes.ALCSEnvoi;
import ch.globaz.al.business.exceptions.model.envoi.ALEnvoiItemException;
import ch.globaz.al.business.models.envoi.EnvoiItemSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiItemSimpleModelSearch;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModelSearch;
import ch.globaz.al.business.services.models.envoi.EnvoiItemSimpleModelService;
import ch.globaz.al.businessimpl.checker.model.envoi.EnvoiItemSimpleModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * @author dhi
 * 
 */
public class EnvoiItemSimpleModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        EnvoiItemSimpleModelService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiItemSimpleModelService#count(ch.globaz.al.business.models.envoi
     * .EnvoiItemSimpleModelSearch)
     */
    @Override
    public int count(EnvoiItemSimpleModelSearch envoiItemSearch) throws ALEnvoiItemException, JadePersistenceException {

        if (envoiItemSearch == null) {
            throw new ALEnvoiItemException(
                    "Unable to count for results, the envoiItemSearch passed as parameter is null");
        }

        return JadePersistenceManager.count(envoiItemSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiItemSimpleModelService#create(ch.globaz.al.business.models.envoi
     * .EnvoiItemSimpleModel)
     */
    @Override
    public EnvoiItemSimpleModel create(EnvoiItemSimpleModel envoiItem) throws ALEnvoiItemException,
            JadePersistenceException {

        if (envoiItem == null) {
            throw new ALEnvoiItemException("Unable to create an envoiItem, the envoiItem passed as parameter is null");
        }

        EnvoiItemSimpleModelChecker.validateForCreate(envoiItem);

        return (EnvoiItemSimpleModel) JadePersistenceManager.add(envoiItem);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiItemSimpleModelService#delete(ch.globaz.al.business.models.envoi
     * .EnvoiItemSimpleModel)
     */
    @Override
    public EnvoiItemSimpleModel delete(EnvoiItemSimpleModel envoiItem) throws ALEnvoiItemException,
            JadePersistenceException {

        if (envoiItem == null) {
            throw new ALEnvoiItemException("Unable to delete an envoiItem, the envoiItem passed as parameter is null");
        }

        EnvoiItemSimpleModelChecker.validateForDelete(envoiItem);

        // DELETE THE .DOC FILE
        if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            String filePath = "";
            EnvoiParametresSimpleModelSearch searchModel = new EnvoiParametresSimpleModelSearch();
            searchModel.setForCsTypeParametre(ALCSEnvoi.SHARED_PATH_FROM_APPLICATION_SERVER);
            try {
                searchModel = ALImplServiceLocator.getEnvoiParametresSimpleModelService().search(searchModel);
            } catch (Exception ex) {
                JadeLogger.error(this,
                        "Path from Client to the shared directory not found. Exception : " + ex.toString());
            }
            if (searchModel.getSize() == 1) {
                EnvoiParametresSimpleModel currentParametres = (EnvoiParametresSimpleModel) searchModel
                        .getSearchResults()[0];
                filePath = currentParametres.getValeurParametre();
            } else {
                JadeLogger.error(this, "Path from Client to the shared directory not found.");
            }
            if (!JadeStringUtil.isEmpty(filePath)) {
                try {
                    JadeFsFacade.delete(filePath + envoiItem.getEnvoiFileName());
                } catch (Exception ex) {
                    JadeLogger.error(this, "Error deleting the file " + (filePath + envoiItem.getEnvoiFileName())
                            + " : " + ex.toString());
                }
            }
        }

        return (EnvoiItemSimpleModel) JadePersistenceManager.delete(envoiItem);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.envoi.EnvoiItemSimpleModelService#read(java.lang.String)
     */
    @Override
    public EnvoiItemSimpleModel read(String idEnvoiItem) throws ALEnvoiItemException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(idEnvoiItem)) {
            throw new ALEnvoiItemException("Unable to read an envoiItem, the id passed as parameter is empty");
        }

        EnvoiItemSimpleModel envoiItem = new EnvoiItemSimpleModel();
        envoiItem.setId(idEnvoiItem);

        return (EnvoiItemSimpleModel) JadePersistenceManager.read(envoiItem);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiItemSimpleModelService#search(ch.globaz.al.business.models.envoi
     * .EnvoiItemSimpleModelSearch)
     */
    @Override
    public EnvoiItemSimpleModelSearch search(EnvoiItemSimpleModelSearch envoiItemSearch) throws ALEnvoiItemException,
            JadePersistenceException {

        if (envoiItemSearch == null) {
            throw new ALEnvoiItemException(
                    "Unable to search for results, the envoiItemSearch passed as parameter is null");
        }

        return (EnvoiItemSimpleModelSearch) JadePersistenceManager.search(envoiItemSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.models.envoi.EnvoiItemSimpleModelService#update(ch.globaz.al.business.models.envoi
     * .EnvoiItemSimpleModel)
     */
    @Override
    public EnvoiItemSimpleModel update(EnvoiItemSimpleModel envoiItem) throws ALEnvoiItemException,
            JadePersistenceException {

        if (envoiItem == null) {
            throw new ALEnvoiItemException("Unable to update an envoiItem, the envoiItem passed as parameter is null");
        }

        EnvoiItemSimpleModelChecker.validateForUpdate(envoiItem);

        return (EnvoiItemSimpleModel) JadePersistenceManager.update(envoiItem);
    }

}
