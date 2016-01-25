package ch.globaz.perseus.businessimpl.services.models.qd;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.qd.QDException;
import ch.globaz.perseus.business.models.qd.SimpleQD;
import ch.globaz.perseus.business.models.qd.SimpleQDSearch;
import ch.globaz.perseus.business.services.models.qd.SimpleQDService;
import ch.globaz.perseus.businessimpl.checkers.qd.SimpleQDChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * Implémentation des services d'une sous QD.
 * 
 * @author JSI
 * 
 */
public class SimpleQDServiceImpl extends PerseusAbstractServiceImpl implements SimpleQDService {

    @Override
    public SimpleQD create(SimpleQD qd) throws JadePersistenceException, QDException {
        if (qd == null) {
            throw new QDException("Unable to create a simpleQD, the given model is null.");
        }
        SimpleQDChecker.checkForCreate(qd);
        return (SimpleQD) JadePersistenceManager.add(qd);
    }

    @Override
    public SimpleQD delete(SimpleQD qd) throws JadePersistenceException, QDException {
        if (qd == null) {
            throw new QDException("Unable to delete a simpleQD, the model passed is null!");
        } else if (qd.isNew()) {
            throw new QDException("Unable to delete a simpleQD, the model passed is new!");
        }
        SimpleQDChecker.checkForDelete(qd);
        return (SimpleQD) JadePersistenceManager.delete(qd);
    }

    @Override
    public SimpleQD read(String idQD) throws JadePersistenceException, QDException {
        if (JadeStringUtil.isBlank(idQD)) {
            throw new QDException("Unable to read a simpleQD, the idQD passed is null.");
        }
        SimpleQD qd = new SimpleQD();
        qd.setId(idQD);
        return (SimpleQD) JadePersistenceManager.read(qd);
    }

    @Override
    public SimpleQDSearch search(SimpleQDSearch search) throws QDException, JadePersistenceException {
        if (search == null) {
            throw new QDException("Unable to search simpleQD, the search model passed is null");
        }
        return (SimpleQDSearch) JadePersistenceManager.search(search);
    }

    @Override
    public SimpleQD update(SimpleQD qd) throws JadePersistenceException, QDException {
        if (qd == null) {
            throw new QDException("Unable to create a sous QD, the given model is null.");
        }
        SimpleQDChecker.checkForUpdate(qd);
        return (SimpleQD) JadePersistenceManager.update(qd);
    }
}
