package ch.globaz.common.persistence;

import globaz.globall.db.BEntity;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.persistence.model.JadeAbstractModel;
import ch.globaz.pegasus.businessimpl.utils.OldPersistence;

public abstract class OldPersistenceToNew<M extends JadeAbstractModel, B extends BEntity, E extends JadeApplicationException> {

    private enum OPERATION {
        ADD,
        DELETE,
        UPDATE
    };

    protected abstract M _adapt(B bean) throws IllegalArgumentException;

    protected abstract B _adapt(M model) throws IllegalArgumentException;

    protected abstract E getException(String message, Exception e);

    private M _perform(final OPERATION operation, final M model) throws E {
        if (model == null) {
            throw new IllegalArgumentException(
                    "An error occured, model cannot be null - SimpleInformationComptabiliteServiceImpl." + operation
                            + "(...)");
        }
        OldPersistence<M> pers = new OldPersistence<M>() {
            @Override
            public M action() throws Exception {

                B bean = null;
                M modelReturn = null;

                bean = OldPersistenceToNew.this._adapt(model);

                switch (operation) {
                    case ADD:
                        bean.add();
                        modelReturn = OldPersistenceToNew.this._adapt(bean);
                        break;

                    case UPDATE:
                        bean.update();
                        modelReturn = OldPersistenceToNew.this._adapt(bean);
                        break;

                    case DELETE:
                        bean.delete();
                        break;

                }
                return modelReturn;
            }
        };

        try {
            return pers.execute();
        } catch (Exception e) {
            throw this.getException("An unexpected error occured", e);
        }
    }

    public M create(M m) throws E {
        if (m == null) {
            throw new IllegalArgumentException("Unable to create, the model passed is null!");
        }

        return _perform(OPERATION.ADD, m);
    }

    public M delete(M m) throws E {
        if (m == null) {
            throw new IllegalArgumentException("Unable to delete, the model passed is null!");
        }
        return _perform(OPERATION.DELETE, m);
    }

    public M update(M m) throws E {
        if (m == null) {
            throw new IllegalArgumentException(
                    "Unable to update SimpleInformationsComptabilite, the model passed is null!");
        }

        return _perform(OPERATION.UPDATE, m);
    }
}
