package ch.globaz.param.businessimpl.service;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.param.business.exceptions.models.ParameterModelException;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.models.ParameterSearchModel;
import ch.globaz.param.business.service.ParamServiceLocator;
import ch.globaz.param.business.service.ParameterModelService;
import ch.globaz.param.business.vo.KeyNameParameter;

/**
 * 
 * classe d'implémentation des services de ParameterModel
 * 
 * @author GMO
 * 
 */
public class ParameterModelServiceImpl implements ParameterModelService {
    @Override
    public ParameterModel create(ParameterModel parameterModel) throws JadeApplicationException,
            JadePersistenceException {
        if (parameterModel == null) {
            throw new ParameterModelException(new KeyNameParameter(ParameterModelException.UNDEFINED_KEY),
                    "Unable to add model (parameterModel) - the model passed is null!");
        }
        // TODO Valide le modèle
        // ParameterModelChecker.validate(parameterModel);
        // L'ajoute en persistence
        return (ParameterModel) JadePersistenceManager.add(parameterModel);
    }

    @Override
    public ParameterModel delete(ParameterModel parameterModel) throws JadeApplicationException,
            JadePersistenceException {
        if (parameterModel == null) {
            throw new ParameterModelException(new KeyNameParameter(ParameterModelException.UNDEFINED_KEY),
                    "Unable to delete model (parameterModel) - the model passed is null!");
        }
        // TODO Valide le modèle
        // ParameterModelChecker.validate(parameterModel);
        // L'ajoute en persistence
        return (ParameterModel) JadePersistenceManager.delete(parameterModel);
    }

    @Override
    public ParameterModel getParameterByName(String application, String name, String date)
            throws JadeApplicationException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(application)) {
            throw new ParameterModelException(new KeyNameParameter(ParameterModelException.UNDEFINED_KEY),
                    "Unable to get the parameter - application is not defined");
        }
        if (JadeStringUtil.isEmpty(name)) {
            throw new ParameterModelException(new KeyNameParameter(ParameterModelException.UNDEFINED_KEY),
                    "Unable to get the parameter - name is not defined");
        }
        if (JadeStringUtil.isEmpty(date)) {
            throw new ParameterModelException(new KeyNameParameter(ParameterModelException.UNDEFINED_KEY),
                    "Unable to get the parameter - validity date is not defined");
        }
        ParameterSearchModel searchModel = new ParameterSearchModel();
        searchModel.setForIdApplParametre(application);
        searchModel.setForDateDebutValidite(date);
        searchModel.setForIdCleDiffere(name);
        searchModel = search(searchModel);
        if (searchModel.getSize() == 0) {
            throw new ParameterModelException(new KeyNameParameter(name),
                    "Unable to get the parameter - no parameter found (" + name + ", " + date + ")");
        }

        // if (searchModel.getSize() > 1) {
        // throw new ParameterException(
        // "Unable to get the parameter - more one parameter found");
        // }

        return (ParameterModel) searchModel.getSearchResults()[0];
    }

    @Override
    public ParameterModel getParameterByName(String application, String name, String date, String plageValue)
            throws JadeApplicationException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(application)) {
            throw new ParameterModelException(new KeyNameParameter(ParameterModelException.UNDEFINED_KEY),
                    "Unable to get the parameter - application is not defined");
        }
        if (JadeStringUtil.isEmpty(name)) {
            throw new ParameterModelException(new KeyNameParameter(ParameterModelException.UNDEFINED_KEY),
                    "Unable to get the parameter - name is not defined");
        }
        if (JadeStringUtil.isEmpty(date)) {
            throw new ParameterModelException(new KeyNameParameter(ParameterModelException.UNDEFINED_KEY),
                    "Unable to get the parameter - validity date is not defined");
        }
        if (JadeStringUtil.isEmpty(plageValue)) {
            throw new ParameterModelException(new KeyNameParameter(ParameterModelException.UNDEFINED_KEY),
                    "Unable to get the parameter - plageValue is not defined");
        }
        ParameterSearchModel searchModel = new ParameterSearchModel();
        searchModel.setForIdApplParametre(application);
        searchModel.setForDateDebutValidite(date);
        searchModel.setForIdCleDiffere(name);
        searchModel.setForPlageValParametre(plageValue);
        searchModel = search(searchModel);
        if (searchModel.getSize() == 0) {
            throw new ParameterModelException(new KeyNameParameter(name),
                    "Unable to get the parameter - no parameter found");
        }
        if (searchModel.getSize() > 1) {
            throw new ParameterModelException(new KeyNameParameter(name),
                    "Unable to get the parameter - more one parameter found");
        }

        return (ParameterModel) searchModel.getSearchResults()[0];
    }

    @Override
    public ParameterModel read(String idParameterModel) throws JadeApplicationException, JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idParameterModel)) {
            throw new ParameterModelException(new KeyNameParameter(ParameterModelException.UNDEFINED_KEY),
                    "Unable to read model (parameterModel) - the id passed is not defined!");
        }
        ParameterModel parameterModel = new ParameterModel();
        parameterModel.setId(idParameterModel);
        return (ParameterModel) JadePersistenceManager.read(parameterModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.param.business.service.ParameterModelService#search(ch.globaz
     * .param.business.models.ParameterSearchModel)
     */
    @Override
    public ParameterSearchModel search(ParameterSearchModel parameterSearchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (parameterSearchModel == null) {
            throw new ParameterModelException(new KeyNameParameter(ParameterModelException.UNDEFINED_KEY),
                    "unable to search parameterSearchModel - the model passed is null");
        }
        return (ParameterSearchModel) JadePersistenceManager.search(parameterSearchModel);
    }

    @Override
    public ParameterModel update(ParameterModel parameterModel) throws JadeApplicationException,
            JadePersistenceException {
        if (parameterModel == null) {
            throw new ParameterModelException(new KeyNameParameter(ParameterModelException.UNDEFINED_KEY),
                    "Unable to update model (parameterModel) - the model passed is null!");
        }
        // TODO Valide le modèle
        // ParameterModelChecker.validate(parameterModel);
        // L'ajoute en persistence
        return (ParameterModel) JadePersistenceManager.update(parameterModel);
    }

    @Override
    public ParameterModel updateValidityAndValues(String idParam, String startValidity, String alphaValue,
            String numValue) throws JadeApplicationException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idParam)) {
            throw new ParameterModelException(new KeyNameParameter(ParameterModelException.UNDEFINED_KEY),
                    "Unable to update model (parameterModel) - idParam passed is undefined!");
        }
        if (JadeStringUtil.isEmpty(startValidity)) {
            throw new ParameterModelException(new KeyNameParameter(ParameterModelException.UNDEFINED_KEY),
                    "Unable to update model (parameterModel) - startValidity passed is undefined!");
        }
        if (JadeStringUtil.isEmpty(alphaValue)) {
            throw new ParameterModelException(new KeyNameParameter(ParameterModelException.UNDEFINED_KEY),
                    "Unable to update model (parameterModel) - alphaValue passed is undefined!");
        }
        if (JadeStringUtil.isEmpty(numValue)) {
            throw new ParameterModelException(new KeyNameParameter(ParameterModelException.UNDEFINED_KEY),
                    "Unable to update model (parameterModel) - numValue passed is undefined!");
        }

        ParameterModel paramToUpdate = ParamServiceLocator.getParameterModelService().read(idParam);

        paramToUpdate.setDateDebutValidite(startValidity);
        // HACK voir parametres.js HACK COMMENT, ligne 38
        if ("$true".equals(alphaValue) || "$false".equals(alphaValue)) {
            alphaValue = alphaValue.substring(1);
        }
        paramToUpdate.setValeurAlphaParametre(alphaValue);
        paramToUpdate.setValeurNumParametre(numValue);
        // TODO Valide le modèle
        // ParameterModelChecker.validate(parameterModel);
        // L'ajoute en persistence
        return ParamServiceLocator.getParameterModelService().update(paramToUpdate);
    }
}
