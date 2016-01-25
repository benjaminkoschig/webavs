package ch.globaz.vulpecula.businessimpl.services.is;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.vulpecula.business.models.is.TauxImpositionSearchSimpleModel;
import ch.globaz.vulpecula.business.models.is.TauxImpositionSimpleModel;
import ch.globaz.vulpecula.business.services.is.TauxImpositionServiceCRUD;

public class TauxImpositionServiceCRUDImpl implements TauxImpositionServiceCRUD {

    @Override
    public TauxImpositionSimpleModel create(TauxImpositionSimpleModel arg0) throws JadeApplicationException,
            JadePersistenceException {
        return (TauxImpositionSimpleModel) JadePersistenceManager.add(arg0);
    }

    @Override
    public TauxImpositionSimpleModel delete(TauxImpositionSimpleModel arg0) throws JadeApplicationException,
            JadePersistenceException {
        return (TauxImpositionSimpleModel) JadePersistenceManager.delete(arg0);
    }

    @Override
    public TauxImpositionSimpleModel read(String arg0) throws JadeApplicationException, JadePersistenceException {
        TauxImpositionSimpleModel model = new TauxImpositionSimpleModel();
        model.setId(arg0);
        return (TauxImpositionSimpleModel) JadePersistenceManager.read(model);
    }

    @Override
    public TauxImpositionSimpleModel update(TauxImpositionSimpleModel arg0) throws JadeApplicationException,
            JadePersistenceException {
        return (TauxImpositionSimpleModel) JadePersistenceManager.update(arg0);
    }

    @Override
    public int count(TauxImpositionSearchSimpleModel searchModel) throws JadeApplicationException,
            JadePersistenceException {
        return JadePersistenceManager.count(searchModel);
    }

    @Override
    public TauxImpositionSearchSimpleModel search(TauxImpositionSearchSimpleModel searchModel)
            throws JadeApplicationException, JadePersistenceException {
        return (TauxImpositionSearchSimpleModel) JadePersistenceManager.search(searchModel);
    }

}
