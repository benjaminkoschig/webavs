package ch.globaz.perseus.businessimpl.services.models.situationfamille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.SimpleRequerant;
import ch.globaz.perseus.business.models.situationfamille.SimpleRequerantSearchModel;
import ch.globaz.perseus.business.services.models.situationfamille.SimpleRequerantService;
import ch.globaz.perseus.businessimpl.checkers.situationfamille.SimpleRequerantChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * TODO Javadoc
 * 
 * @author vyj
 */
public class SimpleRequerantServiceImpl extends PerseusAbstractServiceImpl implements SimpleRequerantService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.requerant.SimpleRequerantService#create(ch.globaz.perseus.
     * business.models .requerant.SimpleRequerant)
     */
    @Override
    public SimpleRequerant create(SimpleRequerant requerant) throws JadePersistenceException, SituationFamilleException {
        if (requerant == null) {
            throw new SituationFamilleException("Unable to create a simple requerant, the model passed is null!");
        }
        SimpleRequerantChecker.checkForCreate(requerant);
        return (SimpleRequerant) JadePersistenceManager.add(requerant);
    }

    @Override
    public SimpleRequerant createOrRead(SimpleRequerant requerant) throws JadePersistenceException,
            SituationFamilleException {
        if (requerant == null) {
            throw new SituationFamilleException("Unable to create a simple requerant, the model passed is null!");
        }
        SimpleRequerantSearchModel simpleRequerantSearchModel = new SimpleRequerantSearchModel();
        simpleRequerantSearchModel.setForIdMembreFamille(requerant.getIdMembreFamille());
        simpleRequerantSearchModel = (SimpleRequerantSearchModel) JadePersistenceManager
                .search(simpleRequerantSearchModel);
        if (simpleRequerantSearchModel.getSize() == 0) {
            return create(requerant);
        } else {
            return (SimpleRequerant) simpleRequerantSearchModel.getSearchResults()[0];
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.requerant.SimpleRequerantService#delete(ch.globaz.perseus.
     * business.models .requerant.SimpleRequerant)
     */
    @Override
    public SimpleRequerant delete(SimpleRequerant requerant) throws JadePersistenceException, SituationFamilleException {
        if (requerant == null) {
            throw new SituationFamilleException("Unable to delete a simple requerant, the model passed is null!");
        } else if (requerant.isNew()) {
            throw new SituationFamilleException("Unable to delete a simple requerant, the model passed is new!");
        }
        SimpleRequerantChecker.checkForDelete(requerant);
        return (SimpleRequerant) JadePersistenceManager.delete(requerant);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.requerant.SimpleRequerantService#read(java.lang.String)
     */
    @Override
    public SimpleRequerant read(String idRequerant) throws JadePersistenceException, SituationFamilleException {
        if (JadeStringUtil.isEmpty(idRequerant)) {
            throw new SituationFamilleException("Unable to read a simple requerant, the id passed is null!");
        }
        SimpleRequerant requerant = new SimpleRequerant();
        requerant.setId(idRequerant);
        return (SimpleRequerant) JadePersistenceManager.read(requerant);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.requerant.SimpleRequerantService#update(ch.globaz.perseus.
     * business.models .requerant.SimpleRequerant)
     */
    @Override
    public SimpleRequerant update(SimpleRequerant requerant) throws JadePersistenceException, SituationFamilleException {
        if (requerant == null) {
            throw new SituationFamilleException("Unable to update a simple requerant, the model passed is null!");
        } else if (requerant.isNew()) {
            throw new SituationFamilleException("Unable to update a simple requerant, the model passed is new!");
        }
        SimpleRequerantChecker.checkForUpdate(requerant);
        return (SimpleRequerant) JadePersistenceManager.update(requerant);
    }

}
