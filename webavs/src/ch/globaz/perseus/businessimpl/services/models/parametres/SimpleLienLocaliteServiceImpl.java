package ch.globaz.perseus.businessimpl.services.models.parametres;

/**
 * MBO
 */

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.parametres.ParametresException;
import ch.globaz.perseus.business.models.parametres.SimpleLienLocalite;
import ch.globaz.perseus.business.services.models.parametres.SimpleLienLocaliteService;
import ch.globaz.perseus.businessimpl.checkers.parametres.SimpleLienLocaliteChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

public class SimpleLienLocaliteServiceImpl extends PerseusAbstractServiceImpl implements SimpleLienLocaliteService {

    @Override
    public SimpleLienLocalite create(SimpleLienLocalite simpleLienLocalite) throws JadePersistenceException,
            ParametresException {
        if (simpleLienLocalite == null) {
            throw new ParametresException("Unable to create a simple LienLocalite, the model passed is null");
        }
        SimpleLienLocaliteChecker.checkForCreate(simpleLienLocalite);
        return (SimpleLienLocalite) JadePersistenceManager.add(simpleLienLocalite);
    }

    @Override
    public SimpleLienLocalite delete(SimpleLienLocalite simpleLienLocalite) throws JadePersistenceException,
            ParametresException {
        if (simpleLienLocalite == null) {
            throw new ParametresException("Unable to delete a simple LienLocalite, the model passed is null");
        }
        if (simpleLienLocalite.isNew()) {
            throw new ParametresException("Unable to delete a simple LienLocalite, the model passed is new!");
        }
        SimpleLienLocaliteChecker.checkForDelete(simpleLienLocalite);
        return (SimpleLienLocalite) JadePersistenceManager.delete(simpleLienLocalite);
    }

    @Override
    public SimpleLienLocalite read(String idLienLocalite) throws JadePersistenceException, ParametresException {
        if (idLienLocalite == null) {
            throw new ParametresException("Unable to read a simple LienLocalite, the model passed is null!");
        }
        SimpleLienLocalite simpleLienLocalite = new SimpleLienLocalite();
        simpleLienLocalite.setId(idLienLocalite);
        return (SimpleLienLocalite) JadePersistenceManager.read(simpleLienLocalite);
    }

    @Override
    public SimpleLienLocalite update(SimpleLienLocalite simpleLienLocalite) throws JadePersistenceException,
            ParametresException {
        if (simpleLienLocalite == null) {
            throw new ParametresException("Unable to update a simple LienLocalite, the model passed is null !");
        }
        if (simpleLienLocalite.isNew()) {
            throw new ParametresException("Unable to update a simple LienLocalite, the model passed is new!");
        }
        SimpleLienLocaliteChecker.checkForUpdate(simpleLienLocalite);
        return (SimpleLienLocalite) JadePersistenceManager.update(simpleLienLocalite);
    }

}
