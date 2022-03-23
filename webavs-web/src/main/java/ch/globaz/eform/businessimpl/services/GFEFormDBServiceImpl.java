package ch.globaz.eform.businessimpl.services;

import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.business.models.GFEFormModel;
import ch.globaz.eform.business.search.GFEFormSearch;
import ch.globaz.eform.business.services.GFEFormDBService;
import ch.globaz.eform.business.validator.GFEFormValidator;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;

import java.util.Objects;

public class GFEFormDBServiceImpl implements GFEFormDBService {
    @Override
    public int count(GFEFormSearch search) throws JadePersistenceException {
        if (search == null) {
            throw new IllegalArgumentException("Unable to count search, the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public GFEFormModel create(GFEFormModel gfeFormModel) throws JadePersistenceException {
        if (Objects.isNull(gfeFormModel)) throw new IllegalArgumentException("Unable to create gfeFormModel, the model passed is null!");
        if (GFEFormValidator.isExists(gfeFormModel.getId())) throw new IllegalArgumentException("Unable to create gfeFormModel, the model passed is already in database id : " + gfeFormModel.getId() + "!");

        ValidationResult validationResult = gfeFormModel.validating();

        if (validationResult.hasError()) throw new IllegalArgumentException("model contain errors!");
        return (GFEFormModel) JadePersistenceManager.add(gfeFormModel);
    }

    @Override
    public boolean delete(String id) throws JadePersistenceException {
        if (Objects.isNull(id)) throw new IllegalArgumentException("Unable to delete gfeFormModel, the id passed is null!");
        if (!GFEFormValidator.isExists(id)) throw new IllegalArgumentException("Unable to delete gfeFormModel, the model passed is not in database!");

        GFEFormSearch search = new GFEFormSearch();
        search.setByMessageId(id);

        return delete(search) > 0;
    }

    @Override
    public int delete(GFEFormSearch gfeFormSearch) throws JadePersistenceException {
        if (Objects.isNull(gfeFormSearch)) throw new IllegalArgumentException("Unable to delete gfeFormModel, the gfeFormSearch passed is null!");

        return JadePersistenceManager.delete(gfeFormSearch);
    }

    @Override
    public GFEFormModel read(String id) throws JadePersistenceException {
        if (Objects.isNull(id)) throw new IllegalArgumentException("Unable to read gfeFormModel, the id passed is null!");

        GFEFormSearch search = new GFEFormSearch();
        search.setById(id);

        search = search(search);

        if (search.getSearchResults().length == 1) {
            return (GFEFormModel) search.getSearchResults()[0];
        }

        return null;
    }

    @Override
    public GFEFormSearch search(GFEFormSearch gfeFormSearch) throws JadePersistenceException {
        if (Objects.isNull(gfeFormSearch)) throw new IllegalArgumentException("Unable to search gfeFormModel, the search passed is null!");
        return (GFEFormSearch) JadePersistenceManager.search(gfeFormSearch);
    }

    @Override
    public GFEFormModel readWithBlobs(String id) throws JadePersistenceException {
        if (Objects.isNull(id)) throw new IllegalArgumentException("Unable to read gfeFormModel, the id passed is null!");

        GFEFormSearch search = new GFEFormSearch();
        search.setById(id);
        search = searchWithBlobs(search);
        if (search.getSearchResults().length == 1) {
            return (GFEFormModel) search.getSearchResults()[0];
        }

        return null;
    }

    @Override
    public GFEFormSearch searchWithBlobs(GFEFormSearch gfeFormSearch) throws JadePersistenceException{
        if (Objects.isNull(gfeFormSearch)) throw new IllegalArgumentException("Unable to search gfeFormModel, the search passed is null!");
        gfeFormSearch = (GFEFormSearch) JadePersistenceManager.search(gfeFormSearch, true);
        return gfeFormSearch;
    }

    @Override
    public GFEFormModel update(GFEFormModel gfeFormModel) throws JadePersistenceException {
        if (Objects.isNull(gfeFormModel)) throw new IllegalArgumentException("Unable to update gfeFormModel, the model passed is null!");
        return (GFEFormModel) JadePersistenceManager.update(gfeFormModel);
    }
}
