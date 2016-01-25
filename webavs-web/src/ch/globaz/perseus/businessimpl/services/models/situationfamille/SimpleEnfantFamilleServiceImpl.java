package ch.globaz.perseus.businessimpl.services.models.situationfamille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.SimpleEnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.SimpleEnfantFamilleSearchModel;
import ch.globaz.perseus.business.services.models.situationfamille.SimpleEnfantFamilleService;
import ch.globaz.perseus.businessimpl.checkers.situationfamille.SimpleEnfantFamilleChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * @author vyj
 */
public class SimpleEnfantFamilleServiceImpl extends PerseusAbstractServiceImpl implements SimpleEnfantFamilleService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.situationfamille.SimpleEnfantFamilleService#count(ch.globaz.perseus
     * .business.models.situationfamille.SimpleEnfantFamilleSearchModel)
     */
    @Override
    public int count(SimpleEnfantFamilleSearchModel searchModel) throws JadePersistenceException,
            SituationFamilleException {
        if (searchModel == null) {
            throw new SituationFamilleException("Unable to count simpleEnfantFamille, the search model passed is null!");
        }

        return JadePersistenceManager.count(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.situationfamille.SimpleEnfantFamilleService#create(ch.globaz.perseus
     * .business.models.situationfamille.SimpleEnfantFamille)
     */
    @Override
    public SimpleEnfantFamille create(SimpleEnfantFamille simpleEnfantfamille) throws JadePersistenceException,
            SituationFamilleException {
        if (simpleEnfantfamille == null) {
            throw new SituationFamilleException("Unable to create a simple enfantFamille, the model passed is null!");
        }
        SimpleEnfantFamilleChecker.checkForCreate(simpleEnfantfamille);
        return (SimpleEnfantFamille) JadePersistenceManager.add(simpleEnfantfamille);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.situationfamille.SimpleEnfantFamilleService#create(ch.globaz.perseus
     * .business.models.situationfamille.SimpleEnfantFamille)
     */
    @Override
    public SimpleEnfantFamille createForRP(SimpleEnfantFamille simpleEnfantfamille) throws JadePersistenceException,
            SituationFamilleException {
        if (simpleEnfantfamille == null) {
            throw new SituationFamilleException("Unable to create a simple enfantFamille, the model passed is null!");
        }
        return (SimpleEnfantFamille) JadePersistenceManager.add(simpleEnfantfamille);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.situationfamille.SimpleEnfantFamilleService#delete(ch.globaz.perseus
     * .business.models.situationfamille.SimpleEnfantFamille)
     */
    @Override
    public SimpleEnfantFamille delete(SimpleEnfantFamille simpleEnfantfamille) throws JadePersistenceException,
            SituationFamilleException {
        if (simpleEnfantfamille == null) {
            throw new SituationFamilleException("Unable to delete a simple Enfantfamille, the model passed is null!");
        } else if (simpleEnfantfamille.isNew()) {
            throw new SituationFamilleException("Unable to delete a simple Enfantfamille, the model passed is new!");
        }
        SimpleEnfantFamilleChecker.checkForDelete(simpleEnfantfamille);
        return (SimpleEnfantFamille) JadePersistenceManager.delete(simpleEnfantfamille);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.situationfamille.SimpleEnfantFamilleService#delete(ch.globaz.perseus
     * .business.models.situationfamille.SimpleEnfantFamilleSearchModel)
     */
    @Override
    public int delete(SimpleEnfantFamilleSearchModel searchModel) throws JadePersistenceException,
            SituationFamilleException {
        if (searchModel == null) {
            throw new SituationFamilleException(
                    "Unable to delete simpleEnfantFamilles, the search model passed is null!");
        }
        return JadePersistenceManager.delete(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.EnfantService#read(java.lang.String)
     */
    @Override
    public SimpleEnfantFamille read(String idSimpleEnfantFamille) throws JadePersistenceException,
            SituationFamilleException {
        if (JadeStringUtil.isEmpty(idSimpleEnfantFamille)) {
            throw new SituationFamilleException("Unable to read a SimpleEnfantFamille, the id passed is null!");
        }
        SimpleEnfantFamille simpleEnfantFamille = new SimpleEnfantFamille();
        simpleEnfantFamille.setId(idSimpleEnfantFamille);
        return (SimpleEnfantFamille) JadePersistenceManager.read(simpleEnfantFamille);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.situationfamille.SimpleEnfantFamilleService#search(ch.globaz.perseus
     * .business.models.situationfamille.SimpleEnfantFamilleSearchModel)
     */
    @Override
    public SimpleEnfantFamilleSearchModel search(SimpleEnfantFamilleSearchModel searchModel)
            throws JadePersistenceException, SituationFamilleException {
        if (searchModel == null) {
            throw new SituationFamilleException(
                    "Unable to search simpleEnfantFamille, the search model passed is null!");
        }

        return (SimpleEnfantFamilleSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.situationfamille.SimpleEnfantFamilleService#update(ch.globaz.perseus
     * .business.models.situationfamille.SimpleEnfantFamille)
     */
    @Override
    public SimpleEnfantFamille update(SimpleEnfantFamille simpleEnfantfamille) throws JadePersistenceException,
            SituationFamilleException {
        if (simpleEnfantfamille == null) {
            throw new SituationFamilleException("Unable to update a simple Enfantfamille, the model passed is null!");
        } else if (simpleEnfantfamille.isNew()) {
            throw new SituationFamilleException("Unable to update a simple Enfantfamille, the model passed is new!");
        }
        SimpleEnfantFamilleChecker.checkForUpdate(simpleEnfantfamille);
        return (SimpleEnfantFamille) JadePersistenceManager.update(simpleEnfantfamille);
    }

}
