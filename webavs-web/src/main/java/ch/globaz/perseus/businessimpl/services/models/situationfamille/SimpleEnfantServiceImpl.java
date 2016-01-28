package ch.globaz.perseus.businessimpl.services.models.situationfamille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.SimpleEnfant;
import ch.globaz.perseus.business.models.situationfamille.SimpleEnfantSearchModel;
import ch.globaz.perseus.business.services.models.situationfamille.SimpleEnfantService;
import ch.globaz.perseus.businessimpl.checkers.situationfamille.SimpleEnfantChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * TODO Javadoc
 * 
 * @author vyj
 */
public class SimpleEnfantServiceImpl extends PerseusAbstractServiceImpl implements SimpleEnfantService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.enfant.SimpleEnfantService#create(ch.globaz.perseus.
     * business.models .enfant.SimpleEnfant)
     */
    @Override
    public SimpleEnfant create(SimpleEnfant enfant) throws JadePersistenceException, SituationFamilleException {
        if (enfant == null) {
            throw new SituationFamilleException("Unable to create a simple enfant, the model passed is null!");
        }
        SimpleEnfantChecker.checkForCreate(enfant);
        return (SimpleEnfant) JadePersistenceManager.add(enfant);
    }

    @Override
    public SimpleEnfant createOrRead(SimpleEnfant enfant) throws JadePersistenceException, SituationFamilleException {
        if (enfant == null) {
            throw new SituationFamilleException("Unable to create a simple enfant, the model passed is null!");
        }
        SimpleEnfantSearchModel simpleEnfantSearchModel = new SimpleEnfantSearchModel();
        simpleEnfantSearchModel.setForIdMembreFamille(enfant.getIdMembreFamille());
        simpleEnfantSearchModel = (SimpleEnfantSearchModel) JadePersistenceManager.search(simpleEnfantSearchModel);
        if (simpleEnfantSearchModel.getSize() == 0) {
            return create(enfant);
        } else {
            return (SimpleEnfant) simpleEnfantSearchModel.getSearchResults()[0];
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.enfant.SimpleEnfantService#delete(ch.globaz.perseus.
     * business.models .enfant.SimpleEnfant)
     */
    @Override
    public SimpleEnfant delete(SimpleEnfant enfant) throws JadePersistenceException, SituationFamilleException {
        if (enfant == null) {
            throw new SituationFamilleException("Unable to delete a simple enfant, the model passed is null!");
        } else if (enfant.isNew()) {
            throw new SituationFamilleException("Unable to delete a simple enfant, the model passed is new!");
        }
        SimpleEnfantChecker.checkForDelete(enfant);
        return (SimpleEnfant) JadePersistenceManager.delete(enfant);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.enfant.SimpleEnfantService#read(java.lang.String)
     */
    @Override
    public SimpleEnfant read(String idEnfant) throws JadePersistenceException, SituationFamilleException {
        if (JadeStringUtil.isEmpty(idEnfant)) {
            throw new SituationFamilleException("Unable to read a simple enfant, the id passed is null!");
        }
        SimpleEnfant enfant = new SimpleEnfant();
        enfant.setId(idEnfant);
        return (SimpleEnfant) JadePersistenceManager.read(enfant);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.enfant.SimpleEnfantService#update(ch.globaz.perseus.
     * business.models .enfant.SimpleEnfant)
     */
    @Override
    public SimpleEnfant update(SimpleEnfant enfant) throws JadePersistenceException, SituationFamilleException {
        if (enfant == null) {
            throw new SituationFamilleException("Unable to update a simple enfant, the model passed is null!");
        } else if (enfant.isNew()) {
            throw new SituationFamilleException("Unable to update a simple enfant, the model passed is new!");
        }
        SimpleEnfantChecker.checkForUpdate(enfant);
        return (SimpleEnfant) JadePersistenceManager.update(enfant);
    }

}
