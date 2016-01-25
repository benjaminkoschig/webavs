package ch.globaz.al.businessimpl.services.models.processus;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.processus.ALProcessusException;
import ch.globaz.al.business.models.processus.TraitementHistoriqueModel;
import ch.globaz.al.business.models.processus.TraitementHistoriqueSearchModel;
import ch.globaz.al.business.services.models.processus.TraitementHistoriqueModelService;
import ch.globaz.al.businessimpl.checker.model.processus.TraitementHistoriqueModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

// TODO (lot 2) à terminer
public class TraitementHistoriqueModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        TraitementHistoriqueModelService {
    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.processus. TraitementHistoriqueModelService
     * #create(ch.globaz.al.business.models.processus.TraitementHistoriqueModel)
     */
    @Override
    public TraitementHistoriqueModel create(TraitementHistoriqueModel model) throws JadeApplicationException,
            JadePersistenceException {
        if (model == null) {
            throw new ALProcessusException(
                    "TraitementHistoriqueModelServiceImpl#create: unable to create traitementHistoriqueModel - model is null ");
        }

        TraitementHistoriqueModelChecker.validate(model);

        return (TraitementHistoriqueModel) JadePersistenceManager.add(model);
    }

    @Override
    public TraitementHistoriqueModel delete(TraitementHistoriqueModel model) throws JadeApplicationException,
            JadePersistenceException {
        if (model == null) {
            throw new ALProcessusException(
                    "TraitementHistoriqueModelServiceImpl#create: unable to delete traitementHistoriqueModel - model is null ");
        }

        TraitementHistoriqueModelChecker.validateForDelete(model);
        return (TraitementHistoriqueModel) JadePersistenceManager.delete(model);
    }

    @Override
    public TraitementHistoriqueModel read(String idModel) throws JadeApplicationException, JadePersistenceException {
        // TODO (lot 2) Auto-generated method stub
        return null;
    }

    @Override
    public TraitementHistoriqueSearchModel search(TraitementHistoriqueSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException {
        // TODO (lot 2) Auto-generated method stub
        return null;
    }

    @Override
    public TraitementHistoriqueModel update(TraitementHistoriqueModel model) throws JadeApplicationException,
            JadePersistenceException {
        // TODO (lot 2) Auto-generated method stub
        return null;
    }

}
