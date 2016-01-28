package ch.globaz.al.businessimpl.services.models.processus;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.processus.ALProcessusException;
import ch.globaz.al.business.models.processus.TraitementPeriodiqueModel;
import ch.globaz.al.business.models.processus.TraitementPeriodiqueSearchModel;
import ch.globaz.al.business.services.models.processus.TraitementPeriodiqueModelService;
import ch.globaz.al.businessimpl.checker.model.processus.TraitementPeriodiqueModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Classe d'implémentation des services de persistence de TraitementPeriodiqueModel
 * 
 * @author GMO
 * 
 */
public class TraitementPeriodiqueModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        TraitementPeriodiqueModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.processus. TraitementPeriodiqueModelService
     * #create(ch.globaz.al.business.models.processus.TraitementPeriodiqueModel)
     */
    @Override
    public TraitementPeriodiqueModel create(TraitementPeriodiqueModel traitementPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException {
        if (traitementPeriodiqueModel == null) {
            throw new ALProcessusException(
                    "TraitementPeriodiqueModelServiceImpl#create:unable to create traitementPeriodiqueModel - the model passed is null");
        }
        TraitementPeriodiqueModelChecker.validate(traitementPeriodiqueModel);
        return (TraitementPeriodiqueModel) JadePersistenceManager.add(traitementPeriodiqueModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.processus. TraitementPeriodiqueModelService
     * #delete(ch.globaz.al.business.models.processus.TraitementPeriodiqueModel)
     */
    @Override
    public TraitementPeriodiqueModel delete(TraitementPeriodiqueModel traitementPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException {
        if (traitementPeriodiqueModel == null) {
            throw new ALProcessusException(
                    "TraitementPeriodiqueModelServiceImpl#delete: unable to delete traitementPeriodiqueModel - the model passed is null");
        }

        TraitementPeriodiqueModelChecker.validateForDelete(traitementPeriodiqueModel);
        return (TraitementPeriodiqueModel) JadePersistenceManager.delete(traitementPeriodiqueModel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.processus. TraitementPeriodiqueModelService#read(java.lang.String)
     */
    @Override
    public TraitementPeriodiqueModel read(String idTraitementPeriodique) throws JadeApplicationException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idTraitementPeriodique)) {
            throw new ALProcessusException(
                    "TraitementPeriodiqueModelServiceImpl#read: unable to read the model- the id passed is null");
        }

        TraitementPeriodiqueModel traitementPeriodiqueModel = new TraitementPeriodiqueModel();
        traitementPeriodiqueModel.setId(idTraitementPeriodique);

        return (TraitementPeriodiqueModel) JadePersistenceManager.read(traitementPeriodiqueModel);

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.models.processus. TraitementPeriodiqueModelService
     * #search(ch.globaz.al.business.models.processus .TraitementPeriodiqueSearchModel)
     */
    @Override
    public TraitementPeriodiqueSearchModel search(TraitementPeriodiqueSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (searchModel == null) {
            throw new ALProcessusException(
                    "TraitementPeriodiqueModelServiceImpl#search: unable to search TraitementPeriodiqueModel - the model passed is null");
        }

        return (TraitementPeriodiqueSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.processus. TraitementPeriodiqueModelService
     * #update(ch.globaz.al.business.models.processus.TraitementPeriodiqueModel)
     */
    @Override
    public TraitementPeriodiqueModel update(TraitementPeriodiqueModel traitementPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException {
        if (traitementPeriodiqueModel == null) {
            throw new ALProcessusException(
                    "TraitementPeriodiqueModelServiceImpl#update: unable to update traitementPeriodiqueModel - the model passed is null");
        }

        TraitementPeriodiqueModelChecker.validate(traitementPeriodiqueModel);

        return (TraitementPeriodiqueModel) JadePersistenceManager.update(traitementPeriodiqueModel);

    }

}
