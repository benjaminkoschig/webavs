/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.pcfaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.models.pcfaccordee.SimplePCFAccordee;
import ch.globaz.perseus.business.models.pcfaccordee.SimplePCFAccordeeSearchModel;
import ch.globaz.perseus.business.services.models.pcfaccordee.SimplePCFAccordeeService;
import ch.globaz.perseus.businessimpl.checkers.pcfaccordee.SimplePCFAccordeeChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * @author DDE
 * 
 */
public class SimplePCFAccordeeServiceImpl extends PerseusAbstractServiceImpl implements SimplePCFAccordeeService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.pcfaccordee.SimplePCFAccordeeService#create(ch.globaz.perseus.business
     * .models.pcfaccordee.SimplePCFAccordee)
     */
    @Override
    public SimplePCFAccordee create(SimplePCFAccordee simplePCFAccordee) throws JadePersistenceException,
            PCFAccordeeException {
        if (simplePCFAccordee == null) {
            throw new PCFAccordeeException("Unable to create SimplePCFAccordee, the model passed is null !");
        }
        SimplePCFAccordeeChecker.checkForCreate(simplePCFAccordee);

        return (SimplePCFAccordee) JadePersistenceManager.add(simplePCFAccordee);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.pcfaccordee.SimplePCFAccordeeService#delete(ch.globaz.perseus.business
     * .models.pcfaccordee.SimplePCFAccordee)
     */
    @Override
    public SimplePCFAccordee delete(SimplePCFAccordee simplePCFAccordee) throws JadePersistenceException,
            PCFAccordeeException {
        if (simplePCFAccordee == null) {
            throw new PCFAccordeeException("Unable to delete SimplePCFAccordee, the model passed is null !");
        }
        if (simplePCFAccordee.isNew()) {
            throw new PCFAccordeeException("Unable to delete SimplePCFAccordee, the model passed is new !");
        }
        SimplePCFAccordeeChecker.checkForDelete(simplePCFAccordee);

        return (SimplePCFAccordee) JadePersistenceManager.delete(simplePCFAccordee);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.pcfaccordee.SimplePCFAccordeeService#read(java.lang.String)
     */
    @Override
    public SimplePCFAccordee read(String idSimplePCFAccordee) throws JadePersistenceException, PCFAccordeeException {
        if (JadeStringUtil.isEmpty(idSimplePCFAccordee)) {
            throw new PCFAccordeeException("Unable to read SimplePCFAccordee, the id passed is empty !");
        }
        SimplePCFAccordee simplePCFAccordee = new SimplePCFAccordee();
        simplePCFAccordee.setId(idSimplePCFAccordee);

        return (SimplePCFAccordee) JadePersistenceManager.read(simplePCFAccordee);
    }

    @Override
    public SimplePCFAccordeeSearchModel search(SimplePCFAccordeeSearchModel simplePCFAccordeeSearchModel)
            throws JadePersistenceException, PCFAccordeeException {
        if (simplePCFAccordeeSearchModel == null) {
            throw new PCFAccordeeException("Unable to search SimplePCFAccordee, the search model passed is null");
        }

        return (SimplePCFAccordeeSearchModel) JadePersistenceManager.search(simplePCFAccordeeSearchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.pcfaccordee.SimplePCFAccordeeService#update(ch.globaz.perseus.business
     * .models.pcfaccordee.SimplePCFAccordee)
     */
    @Override
    public SimplePCFAccordee update(SimplePCFAccordee simplePCFAccordee) throws JadePersistenceException,
            PCFAccordeeException {
        if (simplePCFAccordee == null) {
            throw new PCFAccordeeException("Unable to update SimplePCFAccordee, the model passed is null !");
        }
        if (simplePCFAccordee.isNew()) {
            throw new PCFAccordeeException("Unable to update SimplePCFAccordee, the model passed is new !");
        }
        SimplePCFAccordeeChecker.checkForUpdate(simplePCFAccordee);

        return (SimplePCFAccordee) JadePersistenceManager.update(simplePCFAccordee);
    }

}
