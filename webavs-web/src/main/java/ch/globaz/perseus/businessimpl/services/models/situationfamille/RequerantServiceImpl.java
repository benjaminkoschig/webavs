package ch.globaz.perseus.businessimpl.services.models.situationfamille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.Requerant;
import ch.globaz.perseus.business.models.situationfamille.RequerantSearchModel;
import ch.globaz.perseus.business.models.situationfamille.SimpleMembreFamille;
import ch.globaz.perseus.business.models.situationfamille.SimpleRequerant;
import ch.globaz.perseus.business.services.models.situationfamille.RequerantService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author vyj
 */
public class RequerantServiceImpl extends PerseusAbstractServiceImpl implements RequerantService {

    @Override
    public int count(RequerantSearchModel search) throws SituationFamilleException, JadePersistenceException {
        if (search == null) {
            throw new SituationFamilleException("Unable to count requerants, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.RequerantService#create(ch.globaz.perseus.business.models.requerant
     * .Requerant )
     */
    @Override
    public Requerant create(Requerant requerant) throws JadePersistenceException, SituationFamilleException {
        if (requerant == null) {
            throw new SituationFamilleException("Unable to create requerant, the given model is null!");
        }

        try {
            SimpleMembreFamille simpleMembreFamille = requerant.getMembreFamille().getSimpleMembreFamille();
            simpleMembreFamille = PerseusImplServiceLocator.getSimpleMembreFamilleService().createOrRead(
                    simpleMembreFamille);
            requerant.getMembreFamille().setSimpleMembreFamille(simpleMembreFamille);

            SimpleRequerant simpleRequerant = requerant.getSimpleRequerant();
            simpleRequerant.setIdMembreFamille(simpleMembreFamille.getId());
            simpleRequerant = PerseusImplServiceLocator.getSimpleRequerantService().createOrRead(simpleRequerant);
            requerant.setSimpleRequerant(simpleRequerant);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not available - " + e.getMessage());
        } catch (SituationFamilleException e) {
            throw new SituationFamilleException("SituationFamilleException during creating : " + e.getMessage(), e);
        }

        return requerant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.RequerantService#delete(ch.globaz.perseus.business.models.requerant
     * .Requerant )
     */
    @Override
    public Requerant delete(Requerant requerant) throws JadePersistenceException, SituationFamilleException {
        if (requerant == null) {
            throw new SituationFamilleException("Unable to delete requerant, the given model is null!");
        }
        try {
            requerant.setSimpleRequerant(PerseusImplServiceLocator.getSimpleRequerantService().delete(
                    requerant.getSimpleRequerant()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not available - " + e.getMessage());
        }

        return requerant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.RequerantService#read(java.lang.String)
     */
    @Override
    public Requerant read(String idRequerant) throws JadePersistenceException, SituationFamilleException {
        if (JadeStringUtil.isEmpty(idRequerant)) {
            throw new SituationFamilleException("Unable to read a requerant, the id passed is null!");
        }
        Requerant requerant = new Requerant();
        requerant.setId(idRequerant);
        return (Requerant) JadePersistenceManager.read(requerant);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.perseus.business.services.models.RequerantService#search(ch.globaz.perseus.business.models.requerant
     * . SearchRequerantModel)
     */
    @Override
    public RequerantSearchModel search(RequerantSearchModel searchModel) throws JadePersistenceException,
            SituationFamilleException {
        if (searchModel == null) {
            throw new SituationFamilleException("Unable to search a requerant, the search model passed is null!");
        }
        return (RequerantSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.RequerantService#update(ch.globaz.perseus.business.models.requerant
     * .Requerant )
     */
    @Override
    public Requerant update(Requerant requerant) throws JadePersistenceException, SituationFamilleException {
        throw new SituationFamilleException("No update possible on a requerant, membreFamille cannot be updated");
    }

}
