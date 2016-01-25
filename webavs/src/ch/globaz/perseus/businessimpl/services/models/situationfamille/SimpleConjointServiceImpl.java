package ch.globaz.perseus.businessimpl.services.models.situationfamille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.SimpleConjoint;
import ch.globaz.perseus.business.models.situationfamille.SimpleConjointSearchModel;
import ch.globaz.perseus.business.services.models.situationfamille.SimpleConjointService;
import ch.globaz.perseus.businessimpl.checkers.situationfamille.SimpleConjointChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * TODO Javadoc
 * 
 * @author vyj
 */
public class SimpleConjointServiceImpl extends PerseusAbstractServiceImpl implements SimpleConjointService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.conjoint.SimpleConjointService#create(ch.globaz.perseus.
     * business.models .conjoint.SimpleConjoint)
     */
    @Override
    public SimpleConjoint create(SimpleConjoint conjoint) throws JadePersistenceException, SituationFamilleException {
        if (conjoint == null) {
            throw new SituationFamilleException("Unable to create a simple conjoint, the model passed is null!");
        }
        SimpleConjointChecker.checkForCreate(conjoint);
        return (SimpleConjoint) JadePersistenceManager.add(conjoint);
    }

    @Override
    public SimpleConjoint createOrRead(SimpleConjoint conjoint) throws JadePersistenceException,
            SituationFamilleException {
        if (conjoint == null) {
            throw new SituationFamilleException("Unable to create a simple conjoint, the model passed is null!");
        }
        SimpleConjointSearchModel simpleConjointSearchModel = new SimpleConjointSearchModel();
        simpleConjointSearchModel.setForIdMembreFamille(conjoint.getIdMembreFamille());
        simpleConjointSearchModel = (SimpleConjointSearchModel) JadePersistenceManager
                .search(simpleConjointSearchModel);
        if (simpleConjointSearchModel.getSize() == 0) {
            return create(conjoint);
        } else {
            return (SimpleConjoint) simpleConjointSearchModel.getSearchResults()[0];
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.conjoint.SimpleConjointService#delete(ch.globaz.perseus.
     * business.models .conjoint.SimpleConjoint)
     */
    @Override
    public SimpleConjoint delete(SimpleConjoint conjoint) throws JadePersistenceException, SituationFamilleException {
        if (conjoint == null) {
            throw new SituationFamilleException("Unable to delete a simple conjoint, the model passed is null!");
        } else if (conjoint.isNew()) {
            throw new SituationFamilleException("Unable to delete a simple conjoint, the model passed is new!");
        }
        SimpleConjointChecker.checkForDelete(conjoint);
        return (SimpleConjoint) JadePersistenceManager.delete(conjoint);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.conjoint.SimpleConjointService#read(java.lang.String)
     */
    @Override
    public SimpleConjoint read(String idConjoint) throws JadePersistenceException, SituationFamilleException {
        if (JadeStringUtil.isEmpty(idConjoint)) {
            throw new SituationFamilleException("Unable to read a simple conjoint, the id passed is null!");
        }
        SimpleConjoint conjoint = new SimpleConjoint();
        conjoint.setId(idConjoint);
        return (SimpleConjoint) JadePersistenceManager.read(conjoint);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.conjoint.SimpleConjointService#update(ch.globaz.perseus.
     * business.models .conjoint.SimpleConjoint)
     */
    @Override
    public SimpleConjoint update(SimpleConjoint conjoint) throws JadePersistenceException, SituationFamilleException {
        if (conjoint == null) {
            throw new SituationFamilleException("Unable to update a simple conjoint, the model passed is null!");
        } else if (conjoint.isNew()) {
            throw new SituationFamilleException("Unable to update a simple conjoint, the model passed is new!");
        }
        SimpleConjointChecker.checkForUpdate(conjoint);
        return (SimpleConjoint) JadePersistenceManager.update(conjoint);
    }

}
