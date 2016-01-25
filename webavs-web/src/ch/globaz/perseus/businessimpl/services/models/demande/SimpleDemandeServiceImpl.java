package ch.globaz.perseus.businessimpl.services.models.demande;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.models.demande.SimpleDemande;
import ch.globaz.perseus.business.models.demande.SimpleDemandeSearchModel;
import ch.globaz.perseus.business.services.models.demande.SimpleDemandeService;
import ch.globaz.perseus.businessimpl.checkers.demande.SimpleDemandeChecker;

/**
 * @author DDE
 * 
 */
public class SimpleDemandeServiceImpl implements SimpleDemandeService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.SimpleDemandeService#count(ch.globaz.perseus.business.models
     * .demande.SimpleDemandeSearchModel)
     */
    @Override
    public int count(SimpleDemandeSearchModel searchModel) throws JadePersistenceException, DemandeException {
        if (searchModel == null) {
            throw new DemandeException("Unable to count simple demande, the model passed is null");
        }
        return JadePersistenceManager.count(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.SimpleDemandeService#create(ch.globaz.perseus.business.models
     * .demande.SimpleDemande)
     */
    @Override
    public SimpleDemande create(SimpleDemande demande) throws JadePersistenceException, DemandeException {
        if (demande == null) {
            throw new DemandeException("Unable to create a simple Demande, the model passed is null");
        }
        SimpleDemandeChecker.checkForCreate(demande);
        return (SimpleDemande) JadePersistenceManager.add(demande);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.SimpleDemandeService#delete(ch.globaz.perseus.business.models
     * .demande.SimpleDemande)
     */
    @Override
    public SimpleDemande delete(SimpleDemande demande) throws JadePersistenceException, DemandeException {
        if (demande == null) {
            throw new DemandeException("Unable to delete a simple demande, the model passed is null!");
        } else if (demande.isNew()) {
            throw new DemandeException("Unable to delete a simple demande, the model passed is new!");
        }
        SimpleDemandeChecker.checkForDelete(demande);
        return (SimpleDemande) JadePersistenceManager.delete(demande);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.demande.SimpleDemandeService#read(java.lang.String)
     */
    @Override
    public SimpleDemande read(String idDemande) throws JadePersistenceException, DemandeException {
        if (JadeStringUtil.isEmpty(idDemande)) {
            throw new DemandeException("Unable to read a simple demande, the id passed is null!");
        }
        SimpleDemande demande = new SimpleDemande();
        demande.setId(idDemande);
        return (SimpleDemande) JadePersistenceManager.read(demande);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.SimpleDemandeService#search(ch.globaz.perseus.business.models
     * .demande.SimpleDemandeSearchModel)
     */
    @Override
    public SimpleDemandeSearchModel search(SimpleDemandeSearchModel searchModel) throws JadePersistenceException,
            DemandeException {
        if (searchModel == null) {
            throw new DemandeException("Unable to search simple demande, the model passed is null");
        }
        return (SimpleDemandeSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.demande.SimpleDemandeService#update(ch.globaz.perseus.business.models
     * .demande.SimpleDemande)
     */
    @Override
    public SimpleDemande update(SimpleDemande demande) throws JadePersistenceException, DemandeException {
        if (demande == null) {
            throw new DemandeException("Unable to update a simple demande, the model passed is null!");
        } else if (demande.isNew()) {
            throw new DemandeException("Unable to update a simple demande, the model passed is new!");
        }
        SimpleDemandeChecker.checkForUpdate(demande);
        return (SimpleDemande) JadePersistenceManager.update(demande);
    }

}
