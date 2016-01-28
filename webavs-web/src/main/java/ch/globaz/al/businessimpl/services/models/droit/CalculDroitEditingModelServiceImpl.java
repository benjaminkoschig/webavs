package ch.globaz.al.businessimpl.services.models.droit;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.droit.ALCalculDroitEditingModelException;
import ch.globaz.al.business.models.droit.CalculDroitEditingModel;
import ch.globaz.al.business.models.droit.CalculDroitEditingSearchModel;
import ch.globaz.al.business.services.models.droit.CalculDroitEditingModelService;
import ch.globaz.al.businessimpl.checker.model.droit.CalculDroitEditingModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Classe d'implémentation des services liées au model CalculDroitBusinessModel
 * 
 * @author pta
 * 
 */

public class CalculDroitEditingModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        CalculDroitEditingModelService {

    @Override
    public CalculDroitEditingModel create(CalculDroitEditingModel calculDroitModel) throws JadeApplicationException,
            JadePersistenceException {
        // contrôle paramètre
        if (calculDroitModel == null) {
            throw new ALCalculDroitEditingModelException(
                    "CalculDroitEditingModelServiceImpl#create: Unable to add model (calculDroitModel) - the model passed is null");
        }
        // contrôle de validation des données

        CalculDroitEditingModelChecker.validate(calculDroitModel);
        return (CalculDroitEditingModel) JadePersistenceManager.add(calculDroitModel);
    }

    @Override
    public CalculDroitEditingModel delete(CalculDroitEditingModel calculDroitModel) throws JadeApplicationException,
            JadePersistenceException {
        // contrôle du paramètre
        if (calculDroitModel == null) {
            throw new ALCalculDroitEditingModelException(
                    "CalculDroitEditingModelServiceImpl#create: Unable to delet model (calculDroitModel) - the model passed is null");
        }
        if (calculDroitModel.isNew()) {
            throw new ALCalculDroitEditingModelException(
                    "CalculDroitEditingModelServiceImpl#delete : Unable to remove model (calculDroitModel) - the model passed is new!");
        }
        // suppression
        return (CalculDroitEditingModel) JadePersistenceManager.delete(calculDroitModel);
    }

    @Override
    public CalculDroitEditingSearchModel search(CalculDroitEditingSearchModel calculDroitSearchModel)
            throws JadeApplicationException, JadePersistenceException {
        if (calculDroitSearchModel == null) {
            throw new ALCalculDroitEditingModelException(
                    "CalculDroitEditingModelServiceImpl#search: Unable to search model (calculDroitSearchModel) - the model passed is null");
        }
        // recherche
        return (CalculDroitEditingSearchModel) JadePersistenceManager.search(calculDroitSearchModel);
    }

    @Override
    public CalculDroitEditingModel update(CalculDroitEditingModel calculDroitModel) throws JadeApplicationException,
            JadePersistenceException {
        // TODO Auto-generated method stub
        return calculDroitModel;
    }

}
