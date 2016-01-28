package ch.globaz.perseus.businessimpl.services.models.impotsource;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.impotsource.TauxException;
import ch.globaz.perseus.business.models.impotsource.SimpleBareme;
import ch.globaz.perseus.business.models.impotsource.SimpleBaremeSearchModel;
import ch.globaz.perseus.business.services.models.impotsource.SimpleBaremeService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

public class SimpleBaremeServiceImpl extends PerseusAbstractServiceImpl implements SimpleBaremeService {
    @Override
    public SimpleBareme create(SimpleBareme simpleBareme) throws JadePersistenceException, TauxException {
        if (simpleBareme == null) {
            throw new TauxException("Unable to create a simpleBareme, the model passed is null");
        }

        return (SimpleBareme) JadePersistenceManager.add(simpleBareme);
    }

    @Override
    public SimpleBareme delete(SimpleBareme simpleBareme) throws JadePersistenceException, TauxException {
        if (simpleBareme == null) {
            throw new TauxException("Unable to delete a simpleBareme, the model passed is null");
        }
        if (simpleBareme.isNew()) {
            throw new TauxException("Unable to delete a simpleBareme, the model passed is null");
        }

        return (SimpleBareme) JadePersistenceManager.add(simpleBareme);
    }

    @Override
    public SimpleBareme read(String idSimpleBareme) throws JadePersistenceException, TauxException {
        if (JadeStringUtil.isEmpty(idSimpleBareme)) {
            throw new TauxException("Unable to read a simpleBareme, the id passed is null!");
        }
        SimpleBareme simpleBareme = new SimpleBareme();
        simpleBareme.setId(idSimpleBareme);
        return (SimpleBareme) JadePersistenceManager.read(simpleBareme);
    }

    @Override
    public SimpleBaremeSearchModel search(SimpleBaremeSearchModel searchModel) throws JadePersistenceException,
            TauxException {
        if (searchModel == null) {
            throw new TauxException("Unable to search a simpleBareme, the search model passed is null!");
        }

        return (SimpleBaremeSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public SimpleBareme update(SimpleBareme simpleBareme) throws JadePersistenceException, TauxException {
        if (simpleBareme == null) {
            throw new TauxException("Unable to update a simpleBareme, the model passed is null");
        }
        if (simpleBareme.isNew()) {
            throw new TauxException("Unable to update a simpleBareme, the model passed is null");
        }

        return (SimpleBareme) JadePersistenceManager.add(simpleBareme);
    }

}
