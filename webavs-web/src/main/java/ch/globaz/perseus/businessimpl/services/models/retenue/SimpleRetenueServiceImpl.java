/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.retenue;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueException;
import ch.globaz.perseus.business.models.retenue.SimpleRetenue;
import ch.globaz.perseus.business.models.retenue.SimpleRetenueSearchModel;
import ch.globaz.perseus.business.services.models.retenue.SimpleRetenueService;
import ch.globaz.perseus.businessimpl.checkers.retenue.SimpleRetenueChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * @author dde
 * 
 */
public class SimpleRetenueServiceImpl extends PerseusAbstractServiceImpl implements SimpleRetenueService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.SimpleRetenueService#create(ch.globaz.perseus.business.models.retenue
     * .SimpleRetenue)
     */
    @Override
    public SimpleRetenue create(SimpleRetenue retenue) throws RetenueException, JadePersistenceException {
        if (retenue == null) {
            throw new RetenueException("Unable to create SimpleRetenue, the model passed is null !");
        }
        SimpleRetenueChecker.checkForCreate(retenue);

        return (SimpleRetenue) JadePersistenceManager.add(retenue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.SimpleRetenueService#delete(ch.globaz.perseus.business.models.retenue
     * .SimpleRetenue)
     */
    @Override
    public SimpleRetenue delete(SimpleRetenue retenue) throws RetenueException, JadePersistenceException {
        if ((retenue == null) || retenue.isNew()) {
            throw new RetenueException("Unable to create SimpleRetenue, the model passed is null or new!");
        }
        SimpleRetenueChecker.checkForDelete(retenue);

        return (SimpleRetenue) JadePersistenceManager.delete(retenue);
    }

    @Override
    public int delete(SimpleRetenueSearchModel retenueSearchModel) throws RetenueException, JadePersistenceException {
        if (retenueSearchModel == null) {
            throw new RetenueException("Unable to delete SimpleRetenue, the searchModel passed is null");
        }

        return JadePersistenceManager.delete(retenueSearchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.SimpleRetenueService#read(java.lang.String)
     */
    @Override
    public SimpleRetenue read(String idRetenue) throws RetenueException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idRetenue)) {
            throw new RetenueException("Unable to read SimpleRetenue, the id passed is empty !");
        }
        SimpleRetenue simpleRetenue = new SimpleRetenue();
        simpleRetenue.setId(idRetenue);

        return (SimpleRetenue) JadePersistenceManager.read(simpleRetenue);
    }

    @Override
    public SimpleRetenueSearchModel search(SimpleRetenueSearchModel searchModel) throws RetenueException,
            JadePersistenceException {
        if (searchModel == null) {
            throw new RetenueException("Unable to search simpleRetenue, the model passed is null");
        }

        return (SimpleRetenueSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.SimpleRetenueService#update(ch.globaz.perseus.business.models.retenue
     * .SimpleRetenue)
     */
    @Override
    public SimpleRetenue update(SimpleRetenue retenue) throws RetenueException, JadePersistenceException {
        if ((retenue == null) || retenue.isNew()) {
            throw new RetenueException("Unable to create SimpleRetenue, the model passed is null or new!");
        }
        SimpleRetenueChecker.checkForUpdate(retenue);

        return (SimpleRetenue) JadePersistenceManager.update(retenue);
    }

}
