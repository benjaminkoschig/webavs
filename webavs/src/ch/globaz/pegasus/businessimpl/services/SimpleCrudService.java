package ch.globaz.pegasus.businessimpl.services;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeSearchSimpleModel;
import globaz.jade.persistence.model.JadeSimpleModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.models.annonce.SimpleSequenceSearch;

public abstract class SimpleCrudService<S extends JadeSearchSimpleModel, M extends JadeSimpleModel, E extends JadeApplicationException> {

    @SuppressWarnings("null")
    public int count(S search) throws E, JadePersistenceException {
        if (search == null) {
            throw new JadePersistenceException("Unable to count " + search.getClass().getSimpleName()
                    + ", the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @SuppressWarnings({ "unchecked", "null" })
    public M create(M model) throws E, JadePersistenceException {
        if (model == null) {
            throw new JadePersistenceException("Unable to create " + model.getClass().getSimpleName()
                    + ", the model passed is null!");
        }
        return (M) JadePersistenceManager.add(model);
    }

    @SuppressWarnings({ "unchecked", "null" })
    public M delete(M model) throws E, JadePersistenceException {
        if (model == null) {
            throw new JadePersistenceException("Unable to delete " + model.getClass().getSimpleName()
                    + ", the model passed is null!");
        }

        return (M) JadePersistenceManager.delete(model);
    }

    @SuppressWarnings("unchecked")
    public List<M> find(S search) throws E, JadePersistenceException {
        this.search(search);
        List<M> list = new ArrayList<M>(search.getSize());
        for (JadeAbstractModel modelAbstractModel : search.getSearchResults()) {
            list.add((M) modelAbstractModel);
        }
        return list;
    }

    protected abstract S newInstanceSearchModel();

    @SuppressWarnings("unchecked")
    public M read(String id) throws E, JadePersistenceException {
        if (id == null) {
            throw new JadePersistenceException("Unable to read id, the id passed is null!. For the service: "
                    + this.getClass().getSimpleName());
        }
        M SimpleSequence;
        try {
            SimpleSequence = (M) this.newInstanceSearchModel().whichModelClass().newInstance();
        } catch (InstantiationException e) {
            throw new JadePersistenceException("Unable to instantiate", e);
        } catch (IllegalAccessException e) {
            throw new JadePersistenceException("IllegalAccess", e);
        }
        SimpleSequence.setId(id);
        return (M) JadePersistenceManager.read(SimpleSequence);

    }

    @SuppressWarnings("null")
    public SimpleSequenceSearch search(S search) throws E, JadePersistenceException {
        if (search == null) {
            throw new JadePersistenceException("Unable to search " + search.getClass().getSimpleName()
                    + ", the model passed is null!");
        }
        return (SimpleSequenceSearch) JadePersistenceManager.search(search);
    }

    @Override
    public String toString() {
        return null;
    }

    @SuppressWarnings({ "unchecked", "null" })
    public M update(M model) throws E, JadePersistenceException {
        if (model == null) {
            throw new JadePersistenceException("Unable to update " + model.getClass().getSimpleName()
                    + ", the model passed is null!");
        }
        return (M) JadePersistenceManager.update(model);
    }
}
