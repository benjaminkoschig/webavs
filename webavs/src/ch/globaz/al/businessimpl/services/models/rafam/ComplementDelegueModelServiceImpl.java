package ch.globaz.al.businessimpl.services.models.rafam;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.models.rafam.ComplementDelegueModel;
import ch.globaz.al.business.models.rafam.ComplementDelegueSearchModel;
import ch.globaz.al.business.services.models.rafam.ComplementDelegueModelService;
import ch.globaz.al.businessimpl.checker.model.rafam.ComplementDelegueModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

public class ComplementDelegueModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        ComplementDelegueModelService {

    @Override
    public int count(ComplementDelegueSearchModel search) throws JadeApplicationException, JadePersistenceException {
        if (search == null) {
            throw new ALAnnonceRafamException("ComplementDelegueModelServiceImpl#count : search is null");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public ComplementDelegueModel create(ComplementDelegueModel model) throws JadeApplicationException,
            JadePersistenceException {
        if (model == null) {
            throw new ALAnnonceRafamException(
                    "Unable to add model (ComplementDelegueModel) - the model passed is null!");
        }

        ComplementDelegueModelChecker.validate(model);
        return (ComplementDelegueModel) JadePersistenceManager.add(model);
    }

    @Override
    public ComplementDelegueModel delete(ComplementDelegueModel model) throws JadeApplicationException,
            JadePersistenceException {
        if (model == null) {
            throw new ALAnnonceRafamException(
                    "ComplementDelegueModelServiceImpl#delete : Unable to delete ComplementDelegueModel the model passed is null");
        }
        if (model.isNew()) {
            throw new ALAnnonceRafamException(
                    "ComplementDelegueModelServiceImpl#delete : Unable to delete ComplementDelegueModel, the id passed is new");
        }

        ComplementDelegueModelChecker.validateForDelete(model);

        return (ComplementDelegueModel) JadePersistenceManager.delete(model);

    }

    @Override
    public ComplementDelegueModel read(String idAnnonce) throws JadeApplicationException, JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idAnnonce)) {
            throw new ALAnnonceRafamException("ComplementDelegueModelServiceImpl#read : idAnnonce is not an integer");
        }

        ComplementDelegueModel model = new ComplementDelegueModel();
        model.setId(idAnnonce);
        return (ComplementDelegueModel) JadePersistenceManager.read(model);
    }

    @Override
    public ComplementDelegueSearchModel search(ComplementDelegueSearchModel search) throws JadeApplicationException,
            JadePersistenceException {
        if (search == null) {
            throw new ALAnnonceRafamException("ComplementDelegueModelServiceImpl#search : search is null");
        }

        return (ComplementDelegueSearchModel) JadePersistenceManager.search(search);
    }

    @Override
    public ComplementDelegueModel update(ComplementDelegueModel model) throws JadeApplicationException,
            JadePersistenceException {
        if (model == null) {
            throw new ALAnnonceRafamException("ComplementDelegueModelServiceImpl#update : model is null");
        }

        if (model.isNew()) {
            throw new ALAnnonceRafamException("ComplementDelegueModelServiceImpl#update : model is new");

        }

        ComplementDelegueModelChecker.validate(model);
        return (ComplementDelegueModel) JadePersistenceManager.update(model);
    }

}
