package ch.globaz.helios.businessimpl.services;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.helios.business.models.MandatSimpleModel;
import ch.globaz.helios.business.models.MandatSimpleModelSearch;
import ch.globaz.helios.business.services.MandatSimpleModelService;

/**
 * @author sel
 * 
 */
public class MandatSimpleModelServiceImpl extends ComptaGeneraleAbstractServiceImpl implements MandatSimpleModelService {

    /**
     * service description
     */
    private static final String SERVICE_DESCRIPTION = "mandat";

    /**
	 *
	 */
    @Override
    public int count(MandatSimpleModelSearch searchModel) throws JadeApplicationException, JadePersistenceException {
        if (searchModel == null) {
            throw new JadePersistenceException("Unable to count " + MandatSimpleModelServiceImpl.SERVICE_DESCRIPTION
                    + ", the search model passed is null !");
        }

        return JadePersistenceManager.count(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.service.provider.application.JadeCrudService#create(java.lang.Object)
     */
    @Override
    public MandatSimpleModel create(MandatSimpleModel entity) throws JadeApplicationException, JadePersistenceException {
        // if (entity == null) {
        // throw new JadePersistenceException("Unable to create " + MandatSimpleModelServiceImpl.SERVICE_DESCRIPTION
        // + ", the model passed is null!");
        // }
        //
        // // SimpleAssureurMaladieChecker.checkForCreate(entity);
        //
        // JadePersistenceManager.add(entity);
        //
        // return entity;
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.service.provider.application.JadeCrudService#delete(java.lang.Object)
     */
    @Override
    public MandatSimpleModel delete(MandatSimpleModel entity) throws JadeApplicationException, JadePersistenceException {
        // if (entity == null) {
        // throw new JadePersistenceException("Unable to delete " + MandatSimpleModelServiceImpl.SERVICE_DESCRIPTION
        // + ", the model passed is null!");
        // }
        //
        // // SimpleAssureurMaladieChecker.checkForDelete(entity);
        //
        // JadePersistenceManager.delete(entity);
        // return new MandatSimpleModel();
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.service.provider.application.JadeCrudService#read(java.lang.String)
     */
    @Override
    public MandatSimpleModel read(String idEntity) throws JadeApplicationException, JadePersistenceException {
        if (idEntity == null) {
            throw new JadePersistenceException("Unable to read  " + MandatSimpleModelServiceImpl.SERVICE_DESCRIPTION
                    + ", the id passed is null!");
        }

        MandatSimpleModel model = new MandatSimpleModel();
        model.setId(idEntity);

        return (MandatSimpleModel) JadePersistenceManager.read(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.service.provider.application.JadeCrudService#search(java.lang.Object)
     */
    @Override
    public MandatSimpleModelSearch search(MandatSimpleModelSearch searchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new JadePersistenceException("Unable to search " + MandatSimpleModelServiceImpl.SERVICE_DESCRIPTION
                    + ", the search model passed is null!");
        }

        return (MandatSimpleModelSearch) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.service.provider.application.JadeCrudService#update(java.lang.Object)
     */
    @Override
    public MandatSimpleModel update(MandatSimpleModel entity) throws JadeApplicationException, JadePersistenceException {
        // if (entity == null) {
        // throw new JadePersistenceException("Unable to update " + MandatSimpleModelServiceImpl.SERVICE_DESCRIPTION
        // + ", the model passed is null!");
        // }
        //
        // // SimpleAssureurMaladieChecker.checkForUpdate(entity);
        // JadePersistenceManager.update(entity);
        //
        // return entity;
        return null;
    }
}