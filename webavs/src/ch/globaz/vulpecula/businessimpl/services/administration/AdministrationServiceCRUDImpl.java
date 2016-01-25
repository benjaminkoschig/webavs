/**
 * 
 */
package ch.globaz.vulpecula.businessimpl.services.administration;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import org.apache.commons.lang.NotImplementedException;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.vulpecula.business.services.administration.AdministrationServiceCRUD;

/**
 * implémentation Jade des services pour les administrations
 * 
 * @since Web@BMS 0.01.01
 */
public class AdministrationServiceCRUDImpl implements AdministrationServiceCRUD {

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.jade.service.provider.application.JadeCrudService#count(java.lang
     * .Object)
     */
    @Override
    public int count(AdministrationSearchComplexModel search) throws JadeApplicationException, JadePersistenceException {
        throw new NotImplementedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.jade.service.provider.application.JadeCrudService#create(java.
     * lang.Object)
     */
    @Override
    public AdministrationComplexModel create(AdministrationComplexModel entity) throws JadeApplicationException,
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
    public AdministrationComplexModel delete(AdministrationComplexModel entity) throws JadeApplicationException,
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
    public AdministrationComplexModel read(String idEntity) throws JadeApplicationException, JadePersistenceException {
        return TIBusinessServiceLocator.getAdministrationService().read(idEntity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.jade.service.provider.application.JadeCrudService#search(java.
     * lang.Object)
     */
    @Override
    public AdministrationSearchComplexModel search(AdministrationSearchComplexModel search)
            throws JadeApplicationException, JadePersistenceException {
        return (AdministrationSearchComplexModel) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.jade.service.provider.application.JadeCrudService#update(java.
     * lang.Object)
     */
    @Override
    public AdministrationComplexModel update(AdministrationComplexModel entity) throws JadeApplicationException,
            JadePersistenceException {
        throw new NotImplementedException();
    }

}
