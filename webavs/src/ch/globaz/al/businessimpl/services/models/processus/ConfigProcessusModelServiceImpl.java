package ch.globaz.al.businessimpl.services.models.processus;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.processus.ALProcessusException;
import ch.globaz.al.business.models.processus.ConfigProcessusModel;
import ch.globaz.al.business.models.processus.ConfigProcessusSearchModel;
import ch.globaz.al.business.services.models.processus.ConfigProcessusModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Classe d'implémentation des services configuration processus
 * 
 * @author GMO
 * 
 */
public class ConfigProcessusModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        ConfigProcessusModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.processus.ConfigProcessusService #read(java.lang.String)
     */
    @Override
    public ConfigProcessusModel read(String idConfig) throws JadeApplicationException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idConfig)) {
            throw new ALProcessusException(
                    "ConfigProcessusServiceImpl#read: unable to read ConfigProcessusModel - the id passed is null");
        }
        ConfigProcessusModel configProcessusModel = new ConfigProcessusModel();
        configProcessusModel.setId(idConfig);
        return (ConfigProcessusModel) JadePersistenceManager.read(configProcessusModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.processus.ConfigProcessusService #search
     * (ch.globaz.al.business.models.processus.ConfigProcessusSearchModel)
     */
    @Override
    public ConfigProcessusSearchModel search(ConfigProcessusSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new ALProcessusException(
                    "ConfigProcessusServiceImpl#search: unable to search ConfigProcessusModel - the model passed is null");
        }

        return (ConfigProcessusSearchModel) JadePersistenceManager.search(searchModel);

    }
}
