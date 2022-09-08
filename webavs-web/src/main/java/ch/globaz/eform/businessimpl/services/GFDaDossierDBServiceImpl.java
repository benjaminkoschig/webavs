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
            throw new IllegalArgumentException("Impossible de compter l'objet search passé est nul!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public GFDaDossierModel create(GFDaDossierModel gfDaDossierModel, ValidationResult validationResult) throws JadePersistenceException {
        if (Objects.isNull(gfDaDossierModel)) {
            throw new IllegalArgumentException("Impossible de créer l'enregistrement en base, le model passé est null!");
        }
        if (Objects.isNull(validationResult)) {
            throw new IllegalArgumentException("Impossible de créer l'enregistrement en base, Le ValidationResult passé est null!");
        }
        if (GFDaDossierValidator.isExists(gfDaDossierModel.getMessageId())) {
            throw new IllegalArgumentException("Impossible de créer l'enregistrement en base, L'enregistrement existe déjà : " + gfDaDossierModel.getMessageId() + "!");
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
            throw new IllegalArgumentException("Impossible de supprimer l'enregistrement en base, l'id du message passé est null!");
        }
        if (!GFDaDossierValidator.isExists(messageId)) {
            throw new IllegalArgumentException("Impossible de supprimer l'enregistrement en base, l'enregistrement n'a pas été trouvé!");
        }

        GFDaDossierSearch search = new GFDaDossierSearch();
        search.setByMessageId(messageId);

        return delete(search) > 0;
    }

    @Override
    public int delete(GFDaDossierSearch search) throws JadePersistenceException {
        if (Objects.isNull(search)) {
            throw new IllegalArgumentException("Impossible de supprimer l'enregistrement en base, le model de critère passé is null!");
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
            throw new IllegalArgumentException("Unable to search GFDaDossierSearch, the search passed est null!");
        }
        return (GFDaDossierSearch) JadePersistenceManager.search(search);
    }

    @Override
    public GFDaDossierModel update(GFDaDossierModel model) throws JadePersistenceException {
        return update(model, new ValidationResult());
    }

    @Override
    public GFDaDossierModel update(GFDaDossierModel model, ValidationResult result) throws JadePersistenceException {
        if (Objects.isNull(model)) {
            throw new IllegalArgumentException("Impossible de mettre à jour l'enregistrement en base, le model passé est null!");
        }
        if (Objects.isNull(result)) {
            throw new IllegalArgumentException("Impossible de mettre à jour l'enregistrement en base, le ValidationResult passé est null!");
        }

        model.validating(result);

        if (!result.hasError()) {
            JadePersistenceManager.update(model);
        }

        return model;
    }
}
