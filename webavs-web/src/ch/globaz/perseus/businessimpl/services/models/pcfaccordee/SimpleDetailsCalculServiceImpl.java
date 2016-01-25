/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.pcfaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.models.pcfaccordee.SimpleDetailsCalcul;
import ch.globaz.perseus.business.models.pcfaccordee.SimpleDetailsCalculSearchModel;
import ch.globaz.perseus.business.services.models.pcfaccordee.SimpleDetailsCalculService;
import ch.globaz.perseus.businessimpl.checkers.pcfaccordee.SimpleDetailsCalculChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * @author DDE
 * 
 */
public class SimpleDetailsCalculServiceImpl extends PerseusAbstractServiceImpl implements SimpleDetailsCalculService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.SimpleDemandeService#count(ch.globaz.perseus.business.models
     * .demande.SimpleDemandeSearchModel)
     */
    @Override
    public int count(SimpleDetailsCalculSearchModel simpleDetailsCalculSearchModel) throws JadePersistenceException,
            PCFAccordeeException {
        if (simpleDetailsCalculSearchModel == null) {
            throw new PCFAccordeeException("Unable to count simple detailsCalcul, the model passed is null");
        }
        return JadePersistenceManager.count(simpleDetailsCalculSearchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.pcfaccordee.SimpleDetailsCalculService#create(ch.globaz.perseus.business
     * .models.pcfaccordee.SimpleDetailsCalcul)
     */
    @Override
    public SimpleDetailsCalcul create(SimpleDetailsCalcul simpleDetailsCalcul) throws JadePersistenceException,
            PCFAccordeeException {
        if (simpleDetailsCalcul == null) {
            throw new PCFAccordeeException("Unable to create SimpleDetailsCalcul, the model passed is null !");
        }
        SimpleDetailsCalculChecker.checkForCreate(simpleDetailsCalcul);

        return (SimpleDetailsCalcul) JadePersistenceManager.add(simpleDetailsCalcul);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.pcfaccordee.SimpleDetailsCalculService#delete(ch.globaz.perseus.business
     * .models.pcfaccordee.SimpleDetailsCalcul)
     */
    @Override
    public SimpleDetailsCalcul delete(SimpleDetailsCalcul simpleDetailsCalcul) throws JadePersistenceException,
            PCFAccordeeException {
        if (simpleDetailsCalcul == null) {
            throw new PCFAccordeeException("Unable to delete SimpleDetailsCalcul, the model passed is null !");
        }
        if (simpleDetailsCalcul.isNew()) {
            throw new PCFAccordeeException("Unable to delete SimpleDetailsCalcul, the model passed is new !");
        }
        SimpleDetailsCalculChecker.checkForDelete(simpleDetailsCalcul);

        return (SimpleDetailsCalcul) JadePersistenceManager.delete(simpleDetailsCalcul);
    }

    @Override
    public int delete(SimpleDetailsCalculSearchModel simpleDetailsCalculSearchModel) throws JadePersistenceException,
            PCFAccordeeException {
        if (simpleDetailsCalculSearchModel == null) {
            throw new PCFAccordeeException(
                    "Unable to delete simple SimpleDetailsCalcul, the search model passed is null!");
        }
        return JadePersistenceManager.delete(simpleDetailsCalculSearchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.pcfaccordee.SimpleDetailsCalculService#read(java.lang.String)
     */
    @Override
    public SimpleDetailsCalcul read(String idSimpleDetailsCalcul) throws JadePersistenceException, PCFAccordeeException {
        if (JadeStringUtil.isEmpty(idSimpleDetailsCalcul)) {
            throw new PCFAccordeeException("Unable to read SimpleDetailsCalcul, the id passed is empty !");
        }
        SimpleDetailsCalcul simpleDetailsCalcul = new SimpleDetailsCalcul();
        simpleDetailsCalcul.setId(idSimpleDetailsCalcul);

        return (SimpleDetailsCalcul) JadePersistenceManager.read(simpleDetailsCalcul);
    }

    @Override
    public SimpleDetailsCalculSearchModel search(SimpleDetailsCalculSearchModel simpleDetailsCalculSearchModel)
            throws JadePersistenceException, PCFAccordeeException {
        if (simpleDetailsCalculSearchModel == null) {
            throw new PCFAccordeeException("Unable to search SimpleDetailsCalcul, the search model passed is null");
        }

        return (SimpleDetailsCalculSearchModel) JadePersistenceManager.search(simpleDetailsCalculSearchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.pcfaccordee.SimpleDetailsCalculService#update(ch.globaz.perseus.business
     * .models.pcfaccordee.SimpleDetailsCalcul)
     */
    @Override
    public SimpleDetailsCalcul update(SimpleDetailsCalcul simpleDetailsCalcul) throws JadePersistenceException,
            PCFAccordeeException {
        if (simpleDetailsCalcul == null) {
            throw new PCFAccordeeException("Unable to update SimpleDetailsCalcul, the model passed is null !");
        }
        if (simpleDetailsCalcul.isNew()) {
            throw new PCFAccordeeException("Unable to update SimpleDetailsCalcul, the model passed is new !");
        }
        SimpleDetailsCalculChecker.checkForUpdate(simpleDetailsCalcul);

        return (SimpleDetailsCalcul) JadePersistenceManager.update(simpleDetailsCalcul);
    }

}
