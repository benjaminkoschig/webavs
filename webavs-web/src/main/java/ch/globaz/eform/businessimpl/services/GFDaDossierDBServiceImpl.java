package ch.globaz.eform.businessimpl.services;

import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.search.GFDaDossierSearch;
import ch.globaz.eform.business.services.GFDaDossierDBService;
import ch.globaz.eform.validator.GFDaDossierValidator;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;

import java.util.Objects;

public class GFDaDossierDBServiceImpl implements GFDaDossierDBService {
    @Override
    public int count(GFDaDossierSearch search) throws JadePersistenceException {
        if (search == null) {
            throw new IllegalArgumentException("Unable to count search, the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public GFDaDossierModel create(GFDaDossierModel gfDaDossierModel, ValidationResult validationResult) throws JadePersistenceException {
        if (Objects.isNull(gfDaDossierModel)) {
            throw new IllegalArgumentException("Unable to create gfeFormModel, the model passed is null!");
        }
        if (Objects.isNull(validationResult)) {
            throw new IllegalArgumentException("Unable to create validationResult, the result passed is null!");
        }
        if (GFDaDossierValidator.isExists(gfDaDossierModel.getMessageId())) {
            throw new IllegalArgumentException("Unable to create gfDaDossierModel, the model passed is already in database id : " + gfDaDossierModel.getMessageId() + "!");
        }

        gfDaDossierModel.validating(validationResult);

        //On exécute l'insertion si aucune erreur n'est détecté
        if (!validationResult.hasError()) {
            JadePersistenceManager.add(gfDaDossierModel);
        }

        return gfDaDossierModel;
    }

    @Override
    public GFDaDossierModel create(GFDaDossierModel gfeFormModel) throws JadePersistenceException {
        return create(gfeFormModel, new ValidationResult());
    }

    @Override
    public boolean delete(String messageId) throws JadePersistenceException {
        if (Objects.isNull(messageId)) {
            throw new IllegalArgumentException("Unable to delete GFDaDossierModel, the message id passed is null!");
        }
        if (!GFDaDossierValidator.isExists(messageId)) {
            throw new IllegalArgumentException("Unable to delete GFDaDossierModel, the model passed is not in database!");
        }

        GFDaDossierSearch search = new GFDaDossierSearch();
        search.setByMessageId(messageId);

        return delete(search) > 0;
    }

    @Override
    public int delete(GFDaDossierSearch search) throws JadePersistenceException {
        if (Objects.isNull(search)) {
            throw new IllegalArgumentException("Unable to delete gfDaDossierSearch, the search passed is null!");
        }

        return JadePersistenceManager.delete(search);
    }

    @Override
    public GFDaDossierModel read(String id) throws JadePersistenceException {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Unable to read GFDaDossierModel, the id passed is null!");
        }

        GFDaDossierSearch search = new GFDaDossierSearch();
        search.setById(id);

        search = search(search);

        if (search.getSearchResults().length == 1) {
            return (GFDaDossierModel) search.getSearchResults()[0];
        }

        return null;
    }

    @Override
    public GFDaDossierSearch search(GFDaDossierSearch search) throws JadePersistenceException {
        if (Objects.isNull(search)) {
            throw new IllegalArgumentException("Unable to search GFDaDossierSearch, the search passed is null!");
        }
        return (GFDaDossierSearch) JadePersistenceManager.search(search);
    }

    @Override
    public GFDaDossierModel update(GFDaDossierModel model) throws JadePersistenceException {
        if (Objects.isNull(model)) {
            throw new IllegalArgumentException("Unable to update GFDaDossierModel, the model passed is null!");
        }
        return (GFDaDossierModel) JadePersistenceManager.update(model);
    }
}
