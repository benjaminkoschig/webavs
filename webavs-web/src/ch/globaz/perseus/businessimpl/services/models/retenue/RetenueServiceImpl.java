/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.retenue;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueException;
import ch.globaz.perseus.business.models.retenue.Retenue;
import ch.globaz.perseus.business.models.retenue.RetenueSearchModel;
import ch.globaz.perseus.business.models.retenue.SimpleRetenue;
import ch.globaz.perseus.business.models.retenue.SimpleRetenueSearchModel;
import ch.globaz.perseus.business.services.models.retenue.RetenueService;
import ch.globaz.perseus.businessimpl.checkers.retenue.RetenueChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author dde
 * 
 */
public class RetenueServiceImpl extends PerseusAbstractServiceImpl implements RetenueService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.retenue.RetenueService#count(ch.globaz.perseus.business.models.retenue
     * .RetenueSearchModel)
     */
    @Override
    public int count(RetenueSearchModel search) throws RetenueException, JadePersistenceException {
        if (search == null) {
            throw new RetenueException("Unable to count Retenue, the search model is null");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.retenue.RetenueService#create(ch.globaz.perseus.business.models.retenue
     * .Retenue)
     */
    @Override
    public Retenue create(Retenue retenue) throws RetenueException, JadePersistenceException {
        if (retenue == null) {
            throw new RetenueException("Unable to create Retenue, the model passed is null!");
        }
        SimpleRetenue simpleRetenue = retenue.getSimpleRetenue();
        simpleRetenue.setIdPcfAccordee(retenue.getPcfAccordee().getId());

        RetenueChecker.checkForCreate(retenue);

        try {
            simpleRetenue = PerseusImplServiceLocator.getSimpleRetenueService().create(simpleRetenue);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RetenueException("Service not available : " + e.toString(), e);
        }

        retenue.setSimpleRetenue(simpleRetenue);

        return retenue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.retenue.RetenueService#delete(ch.globaz.perseus.business.models.retenue
     * .Retenue)
     */
    @Override
    public Retenue delete(Retenue retenue) throws RetenueException, JadePersistenceException {
        if ((retenue == null) || retenue.isNew()) {
            throw new RetenueException("Unable to create Retenue, the model passed is null or new!");
        }

        RetenueChecker.checkForDelete(retenue);

        try {
            SimpleRetenue simpleRetenue = retenue.getSimpleRetenue();
            simpleRetenue = PerseusImplServiceLocator.getSimpleRetenueService().delete(simpleRetenue);
            retenue.setSimpleRetenue(simpleRetenue);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RetenueException("Service not available : " + e.toString(), e);
        }

        return retenue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.retenue.RetenueService#deleteForPCFAccordee(ch.globaz.perseus.business
     * .models.retenue .Retenue)
     */
    @Override
    public int deleteForPCFAccordee(String idPCFAccordee) throws RetenueException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idPCFAccordee)) {
            throw new RetenueException("Unable to delete Retenue for pcfAccordee, the id passed is null!");
        }

        try {
            SimpleRetenueSearchModel searchModel = new SimpleRetenueSearchModel();
            searchModel.setForIdPcfAccordee(idPCFAccordee);
            return PerseusImplServiceLocator.getSimpleRetenueService().delete(searchModel);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RetenueException("Service not available : " + e.getMessage(), e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.retenue.RetenueService#read(java.lang.String)
     */
    @Override
    public Retenue read(String idRetenue) throws RetenueException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idRetenue)) {
            throw new RetenueException("Unable to read SimpleRetenue, the id passed is empty !");
        }
        Retenue retenue = new Retenue();
        retenue.setId(idRetenue);

        return (Retenue) JadePersistenceManager.read(retenue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.retenue.RetenueService#search(ch.globaz.perseus.business.models.retenue
     * .RetenueSearchModel)
     */
    @Override
    public RetenueSearchModel search(RetenueSearchModel retenueSearch) throws JadePersistenceException,
            RetenueException {
        if (retenueSearch == null) {
            throw new RetenueException("Unable to count Retenue, the search model is null");
        }
        return (RetenueSearchModel) JadePersistenceManager.search(retenueSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.retenue.RetenueService#update(ch.globaz.perseus.business.models.retenue
     * .Retenue)
     */
    @Override
    public Retenue update(Retenue retenue) throws RetenueException, JadePersistenceException {
        if ((retenue == null) || retenue.isNew()) {
            throw new RetenueException("Unable to create Retenue, the model passed is null or new!");
        }
        SimpleRetenue simpleRetenue = retenue.getSimpleRetenue();
        simpleRetenue.setIdPcfAccordee(retenue.getPcfAccordee().getId());

        RetenueChecker.checkForUpdate(retenue);

        try {
            simpleRetenue = PerseusImplServiceLocator.getSimpleRetenueService().update(simpleRetenue);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RetenueException("Service not available : " + e.toString(), e);
        }

        retenue.setSimpleRetenue(simpleRetenue);

        return retenue;
    }
}
