package ch.globaz.perseus.businessimpl.services.models.situationfamille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.SimpleMembreFamille;
import ch.globaz.perseus.business.models.situationfamille.SimpleMembreFamilleSearchModel;
import ch.globaz.perseus.business.services.models.situationfamille.SimpleMembreFamilleService;
import ch.globaz.perseus.businessimpl.checkers.situationfamille.SimpleMembreFamilleChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * TODO Javadoc
 * 
 * @author vyj
 */
public class SimpleMembreFamilleServiceImpl extends PerseusAbstractServiceImpl implements SimpleMembreFamilleService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.membreFamille.SimpleMembreFamilleService#create(ch.globaz.perseus.
     * business.models .membreFamille.SimpleMembreFamille)
     */
    @Override
    public SimpleMembreFamille create(SimpleMembreFamille membreFamille) throws JadePersistenceException,
            SituationFamilleException {
        if (membreFamille == null) {
            throw new SituationFamilleException("Unable to create a simple membreFamille, the model passed is null!");
        }
        SimpleMembreFamilleChecker.checkForCreate(membreFamille);
        return (SimpleMembreFamille) JadePersistenceManager.add(membreFamille);
    }

    @Override
    public SimpleMembreFamille createOrRead(SimpleMembreFamille membreFamille) throws JadePersistenceException,
            SituationFamilleException {
        if (membreFamille == null) {
            throw new SituationFamilleException("Unable to create a simple membreFamille, the model passed is null!");
        }
        SimpleMembreFamilleSearchModel simpleMembreFamilleSearchModel = new SimpleMembreFamilleSearchModel();
        simpleMembreFamilleSearchModel.setForIdTiers(membreFamille.getIdTiers());
        simpleMembreFamilleSearchModel = (SimpleMembreFamilleSearchModel) JadePersistenceManager
                .search(simpleMembreFamilleSearchModel);
        if (simpleMembreFamilleSearchModel.getSize() == 0) {
            return create(membreFamille);
        } else {
            // Le seul champ qui pourrait changer est le champ isAI donc faire l'update par rapport à ce que l'on vient
            // de recevoir
            SimpleMembreFamille oldMF = (SimpleMembreFamille) simpleMembreFamilleSearchModel.getSearchResults()[0];
            oldMF.setIsAI(membreFamille.getIsAI());
            return update(oldMF);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.membreFamille.SimpleMembreFamilleService#delete(ch.globaz.perseus.
     * business.models .membreFamille.SimpleMembreFamille)
     */
    @Override
    public SimpleMembreFamille delete(SimpleMembreFamille membreFamille) throws JadePersistenceException,
            SituationFamilleException {
        if (membreFamille == null) {
            throw new SituationFamilleException("Unable to delete a simple membreFamille, the model passed is null!");
        } else if (membreFamille.isNew()) {
            throw new SituationFamilleException("Unable to delete a simple membreFamille, the model passed is new!");
        }
        SimpleMembreFamilleChecker.checkForDelete(membreFamille);
        return (SimpleMembreFamille) JadePersistenceManager.delete(membreFamille);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.membreFamille.SimpleMembreFamilleService#read(java.lang.String)
     */
    @Override
    public SimpleMembreFamille read(String idMembreFamille) throws JadePersistenceException, SituationFamilleException {
        if (JadeStringUtil.isEmpty(idMembreFamille)) {
            throw new SituationFamilleException("Unable to read a simple membreFamille, the id passed is null!");
        }
        SimpleMembreFamille membreFamille = new SimpleMembreFamille();
        membreFamille.setId(idMembreFamille);
        return (SimpleMembreFamille) JadePersistenceManager.read(membreFamille);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.membreFamille.SimpleMembreFamilleService#update(ch.globaz.perseus.
     * business.models .membreFamille.SimpleMembreFamille)
     */
    @Override
    public SimpleMembreFamille update(SimpleMembreFamille membreFamille) throws JadePersistenceException,
            SituationFamilleException {
        if (membreFamille == null) {
            throw new SituationFamilleException("Unable to update a simple membreFamille, the model passed is null!");
        } else if (membreFamille.isNew()) {
            throw new SituationFamilleException("Unable to update a simple membreFamille, the model passed is new!");
        }
        SimpleMembreFamilleChecker.checkForUpdate(membreFamille);
        return (SimpleMembreFamille) JadePersistenceManager.update(membreFamille);
    }

}
