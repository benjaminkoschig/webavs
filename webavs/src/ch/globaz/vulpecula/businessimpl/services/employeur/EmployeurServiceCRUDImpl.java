/**
 *
 */
package ch.globaz.vulpecula.businessimpl.services.employeur;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import org.apache.commons.lang.NotImplementedException;
import ch.globaz.vulpecula.business.models.employeur.EmployeurComplexModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurSearchComplexModel;
import ch.globaz.vulpecula.business.services.employeur.EmployeurServiceCRUD;

/**
 * @author sel
 * 
 */
public class EmployeurServiceCRUDImpl implements EmployeurServiceCRUD {
    /**
     * service description
     */
    private static final String SERVICE_DESCRIPTION = "employeur";

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.jade.service.provider.application.JadeCrudService#count(java.lang
     * .Object)
     */
    @Override
    public int count(final EmployeurSearchComplexModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new JadePersistenceException("Unable to count " + EmployeurServiceCRUDImpl.SERVICE_DESCRIPTION
                    + ", the search model passed is null !");
        }

        return JadePersistenceManager.count(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.jade.service.provider.application.JadeCrudService#create(java.
     * lang.Object)
     */
    @Override
    public EmployeurComplexModel create(final EmployeurComplexModel entity) throws JadeApplicationException,
            JadePersistenceException {
        throw new NotImplementedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.jade.service.provider.application.JadeCrudService#delete(java.
     * lang.Object)
     */
    @Override
    public EmployeurComplexModel delete(final EmployeurComplexModel entity) throws JadeApplicationException,
            JadePersistenceException {
        throw new NotImplementedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.jade.service.provider.application.JadeCrudService#read(java.lang
     * .String)
     */
    @Override
    public EmployeurComplexModel read(final String idEntity) throws JadeApplicationException, JadePersistenceException {
        if (idEntity == null) {
            throw new JadePersistenceException("Unable to read  " + EmployeurServiceCRUDImpl.SERVICE_DESCRIPTION
                    + ", the id passed is null!");
        }

        EmployeurComplexModel model = new EmployeurComplexModel();
        model.setId(idEntity);

        return (EmployeurComplexModel) JadePersistenceManager.read(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.jade.service.provider.application.JadeCrudService#search(java.
     * lang.Object)
     */
    @Override
    public EmployeurSearchComplexModel search(final EmployeurSearchComplexModel searchModel)
            throws JadeApplicationException, JadePersistenceException {

        if (searchModel == null) {
            throw new JadePersistenceException("Unable to search " + EmployeurServiceCRUDImpl.SERVICE_DESCRIPTION
                    + ", the search model passed is null!");
        }

        return (EmployeurSearchComplexModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.jade.service.provider.application.JadeCrudService#update(java.
     * lang.Object)
     */
    @Override
    public EmployeurComplexModel update(final EmployeurComplexModel employeur) throws JadeApplicationException,
            JadePersistenceException {
        throw new NotImplementedException();
    }
}
