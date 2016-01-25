package ch.globaz.perseus.businessimpl.services.models.qd;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.qd.QDException;
import ch.globaz.perseus.business.models.qd.SimpleQDAnnuelle;
import ch.globaz.perseus.business.services.models.qd.SimpleQDAnnuelleService;
import ch.globaz.perseus.businessimpl.checkers.qd.SimpleQDAnnuelleChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * Implémentation des services pour une QD principale.
 * 
 * @author JSI
 * 
 */
public class SimpleQDAnnuelleServiceImpl extends PerseusAbstractServiceImpl implements SimpleQDAnnuelleService {

    @Override
    public SimpleQDAnnuelle create(SimpleQDAnnuelle qd) throws JadePersistenceException, QDException {
        if (qd == null) {
            throw new QDException("Unable to create Main QD, given model is null.");
        }
        SimpleQDAnnuelleChecker.checkForCreate(qd);
        return (SimpleQDAnnuelle) JadePersistenceManager.add(qd);
    }

    @Override
    public SimpleQDAnnuelle delete(SimpleQDAnnuelle qd) throws JadePersistenceException, QDException {
        if (qd == null) {
            throw new QDException("Unable to delete a simple QD Main, the model passed is null!");
        } else if (qd.isNew()) {
            throw new QDException("Unable to delete a simple QD Main, the model passed is new!");
        }
        SimpleQDAnnuelleChecker.checkForDelete(qd);
        return (SimpleQDAnnuelle) JadePersistenceManager.delete(qd);
    }

    @Override
    public SimpleQDAnnuelle read(String idQD) throws JadePersistenceException, QDException {
        if (JadeStringUtil.isBlank(idQD)) {
            throw new QDException("Unable to read a simple QD Main, the idFacture passed is null.");
        }
        SimpleQDAnnuelle qd = new SimpleQDAnnuelle();
        qd.setId(idQD);
        return (SimpleQDAnnuelle) JadePersistenceManager.read(qd);
    }

    @Override
    public SimpleQDAnnuelle update(SimpleQDAnnuelle qd) throws JadePersistenceException, QDException {
        if (qd == null) {
            throw new QDException("Unable to create a sous QD, the given model is null.");
        }
        SimpleQDAnnuelleChecker.checkForUpdate(qd);
        return (SimpleQDAnnuelle) JadePersistenceManager.update(qd);
    }
}
