package ch.globaz.eform.businessimpl.services;

import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.business.search.GFFormulaireSearch;
import ch.globaz.eform.business.search.GFStatistiqueSearch;
import ch.globaz.eform.business.services.GFEFormDBService;
import ch.globaz.eform.validator.GFEFormValidator;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;

import java.util.Objects;

public class GFEFormDBServiceImpl implements GFEFormDBService {
    @Override
    public int count(GFFormulaireSearch search) throws JadePersistenceException {
        if (search == null) {
            throw new IllegalArgumentException("Unable to count search, the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public GFFormulaireModel create(GFFormulaireModel gfeFormModel, ValidationResult validationResult) throws JadePersistenceException {
        if (Objects.isNull(gfeFormModel)) throw new IllegalArgumentException("Unable to create gfeFormModel, the model passed is null!");
        if (GFEFormValidator.isExists(gfeFormModel.getId())) throw new IllegalArgumentException("Unable to create gfeFormModel, the model passed is already in database id : " + gfeFormModel.getId() + "!");

        gfeFormModel.validating(validationResult);

        //On exécute l'insertion si aucune erreur n'est détecté
        if (!validationResult.hasError()) {
            JadePersistenceManager.add(gfeFormModel);
        }

        return gfeFormModel;
    }

    @Override
    public GFFormulaireModel create(GFFormulaireModel gfeFormModel) throws JadePersistenceException {
        return create(gfeFormModel, new ValidationResult());
    }

    @Override
    public boolean delete(String id) throws JadePersistenceException {
        if (Objects.isNull(id)) throw new IllegalArgumentException("Unable to delete gfeFormModel, the id passed is null!");
        if (!GFEFormValidator.isExists(id)) throw new IllegalArgumentException("Unable to delete gfeFormModel, the model passed is not in database!");

        GFFormulaireSearch search = new GFFormulaireSearch();
        search.setByMessageId(id);

        return delete(search) > 0;
    }

    @Override
    public int delete(GFFormulaireSearch gfeFormSearch) throws JadePersistenceException {
        if (Objects.isNull(gfeFormSearch)) throw new IllegalArgumentException("Unable to delete gfeFormModel, the gfeFormSearch passed is null!");

        return JadePersistenceManager.delete(gfeFormSearch);
    }

    @Override
    public GFFormulaireModel read(String id) throws JadePersistenceException {
        if (Objects.isNull(id)) throw new IllegalArgumentException("Unable to read gfeFormModel, the id passed is null!");

        GFFormulaireSearch search = new GFFormulaireSearch();
        search.setById(id);

        search = search(search);

        if (search.getSearchResults().length == 1) {
            return (GFFormulaireModel) search.getSearchResults()[0];
        }

        return null;
    }

    @Override
    public GFFormulaireSearch search(GFFormulaireSearch gfeFormSearch) throws JadePersistenceException {
        if (Objects.isNull(gfeFormSearch)) throw new IllegalArgumentException("Unable to search gfeFormModel, the search passed is null!");
        return (GFFormulaireSearch) JadePersistenceManager.search(gfeFormSearch);
    }

    @Override
    public GFFormulaireModel readWithBlobs(String id) throws JadePersistenceException {
        if (Objects.isNull(id)) throw new IllegalArgumentException("Unable to read gfeFormModel, the id passed is null!");

        GFFormulaireSearch search = new GFFormulaireSearch();
        search.setById(id);
        search = searchWithBlobs(search);
        if (search.getSearchResults().length == 1) {
            return (GFFormulaireModel) search.getSearchResults()[0];
        }

        return null;
    }

    @Override
    public GFFormulaireSearch searchWithBlobs(GFFormulaireSearch gfeFormSearch) throws JadePersistenceException{
        if (Objects.isNull(gfeFormSearch)) throw new IllegalArgumentException("Unable to search gfeFormModel, the search passed is null!");
        return (GFFormulaireSearch) JadePersistenceManager.search(gfeFormSearch, true);
    }

    @Override
    public GFStatistiqueSearch search(GFStatistiqueSearch GFStatistiqueSearch) throws JadePersistenceException {
        if (Objects.isNull(GFStatistiqueSearch)) throw new IllegalArgumentException("Unable to search gfeFormStatistiqueSearch, the search passed is null!");
        return (GFStatistiqueSearch) JadePersistenceManager.search(GFStatistiqueSearch);
    }

    @Override
    public GFFormulaireModel update(GFFormulaireModel gfeFormModel) throws JadePersistenceException {
        if (Objects.isNull(gfeFormModel)) throw new IllegalArgumentException("Unable to update gfeFormModel, the model passed is null!");
        return (GFFormulaireModel) JadePersistenceManager.update(gfeFormModel);
    }
}
