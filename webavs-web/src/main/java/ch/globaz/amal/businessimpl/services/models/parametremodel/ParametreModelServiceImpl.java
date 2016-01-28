/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.parametremodel;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.parametreModel.ParametreModelException;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplex;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplexSearch;
import ch.globaz.amal.business.models.parametremodel.SimpleParametreModel;
import ch.globaz.amal.business.models.parametremodel.SimpleParametreModelSearch;
import ch.globaz.amal.business.services.models.parametremodel.ParametreModelService;
import ch.globaz.amal.businessimpl.checkers.parametremodel.SimpleParametreModelChecker;
import ch.globaz.envoi.business.exceptions.models.parametrageEnvoi.FormuleListException;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * @author LFO
 * 
 */
public class ParametreModelServiceImpl implements ParametreModelService {

    @Override
    public ParametreModelComplex create(ParametreModelComplex parametreModelComplex) throws JadePersistenceException,
            ParametreModelException {
        if (parametreModelComplex == null) {
            throw new ParametreModelException("Unable to create parametreModel, the given model is null!");
        }
        try {

            FormuleList formuleList = ENServiceLocator.getFormuleListService().create(
                    parametreModelComplex.getFormuleList());
            parametreModelComplex.setFormuleList(formuleList);

            SimpleParametreModelSearch simpleParametreModelSearch = new SimpleParametreModelSearch();
            simpleParametreModelSearch.setForCodeSystemeFormule(formuleList.getDefinitionformule().getCsDocument());
            simpleParametreModelSearch = (SimpleParametreModelSearch) JadePersistenceManager
                    .search(simpleParametreModelSearch);

            if (simpleParametreModelSearch.getSize() == 0) {
                // Création
                SimpleParametreModel simpleParametreModel = parametreModelComplex.getSimpleParametreModel();
                simpleParametreModel.setIdFormule(formuleList.getFormule().getIdFormule());
                simpleParametreModel = (SimpleParametreModel) JadePersistenceManager.add(simpleParametreModel);
                parametreModelComplex.setSimpleParametreModel(simpleParametreModel);
            } else {
                // Link
                SimpleParametreModel currentParametreModel = (SimpleParametreModel) simpleParametreModelSearch
                        .getSearchResults()[0];
                currentParametreModel.setIdFormule(formuleList.getFormule().getIdFormule());
                currentParametreModel = (SimpleParametreModel) JadePersistenceManager.update(currentParametreModel);
                parametreModelComplex.setSimpleParametreModel(currentParametreModel);
            }

        } catch (FormuleListException e) {
            throw new ParametreModelException("Unable to create parametreModel, the given model is null!");
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ParametreModelException("Service not available - " + e.getMessage());
        }

        return parametreModelComplex;
    }

    @Override
    public ParametreModelComplex delete(ParametreModelComplex parametreModelComplex) throws JadePersistenceException,
            ParametreModelException {
        if (parametreModelComplex == null) {
            throw new ParametreModelException("Unable to delete parametreModel, the given model is null!");
        }

        try {
            // Pas de suppression de simpleparametremodel >> reprise as/400 à sauvegarder
            FormuleList formuleList = parametreModelComplex.getFormuleList();
            formuleList = ENServiceLocator.getFormuleListService().delete(formuleList);
            parametreModelComplex.setFormuleList(formuleList);
        } catch (FormuleListException e) {
            throw new ParametreModelException("Unable to delete parametreModel, the given model is null!");
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ParametreModelException("Service not available - " + e.getMessage());
        }

        return parametreModelComplex;

    }

    @Override
    public ParametreModelComplex read(String idParametreModel) throws JadePersistenceException,
            ParametreModelException, JadeApplicationServiceNotAvailableException {
        if (JadeStringUtil.isBlankOrZero(idParametreModel)) {
            throw new ParametreModelException("Unable to search parametreModel, the idParametreModel passed is null!");
        }

        ParametreModelComplex parametreModelComplex = new ParametreModelComplex();
        parametreModelComplex.setId(idParametreModel);
        return (ParametreModelComplex) JadePersistenceManager.read(parametreModelComplex);
    }

    @Override
    public ParametreModelComplexSearch search(ParametreModelComplexSearch parametreModelComplexSearch)
            throws JadePersistenceException, ParametreModelException {
        if (parametreModelComplexSearch == null) {
            throw new ParametreModelException("Unable to search parametreModel, the given model is null!");
        }
        parametreModelComplexSearch.setDefinedSearchSize(0);
        return (ParametreModelComplexSearch) JadePersistenceManager.search(parametreModelComplexSearch);
    }

    @Override
    public ParametreModelComplex update(ParametreModelComplex parametreModelComplex) throws JadePersistenceException,
            ParametreModelException {

        if (parametreModelComplex == null) {
            throw new ParametreModelException("Unable to update parametreModel, the given model is null!");
        }
        try {
            SimpleParametreModel simpleParametreModel = parametreModelComplex.getSimpleParametreModel();
            SimpleParametreModelChecker.checkForUpdate(simpleParametreModel);
            simpleParametreModel = (SimpleParametreModel) JadePersistenceManager.update(simpleParametreModel);
            parametreModelComplex.setSimpleParametreModel(simpleParametreModel);

            FormuleList formuleList = ENServiceLocator.getFormuleListService().update(
                    parametreModelComplex.getFormuleList());
            parametreModelComplex.setFormuleList(formuleList);
        } catch (FormuleListException e) {
            throw new ParametreModelException("Unable to update parametreModel, the given model is null!");
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ParametreModelException("Service not available - " + e.getMessage());
        }

        return parametreModelComplex;
    }

}
