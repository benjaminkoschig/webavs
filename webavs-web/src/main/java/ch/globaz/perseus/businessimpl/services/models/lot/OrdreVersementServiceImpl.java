package ch.globaz.perseus.businessimpl.services.models.lot;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.models.lot.OrdreVersement;
import ch.globaz.perseus.business.models.lot.OrdreVersementSearchModel;
import ch.globaz.perseus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.perseus.business.services.models.lot.OrdreVersementService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * 
 * @author MBO
 * 
 */

public class OrdreVersementServiceImpl extends PerseusAbstractServiceImpl implements OrdreVersementService {

    @Override
    public int count(OrdreVersementSearchModel search) throws LotException, JadePersistenceException {
        if (search == null) {
            throw new LotException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public OrdreVersement create(OrdreVersement ordreVersement) throws JadePersistenceException, LotException {
        if (ordreVersement == null) {
            throw new LotException("Unable to create ordreVersement, the given model is null !");
        }

        try {
            SimpleOrdreVersement simpleOrdreVersement = ordreVersement.getSimpleOrdreVersement();
            simpleOrdreVersement.setIdPrestation(ordreVersement.getSimplePrestation().getId());
            simpleOrdreVersement = PerseusImplServiceLocator.getSimpleOrdreVersementService().create(
                    simpleOrdreVersement);
            ordreVersement.setSimpleOrdreVersement(simpleOrdreVersement);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new LotException("Service not available -" + e.getMessage());
        }
        return ordreVersement;
    }

    @Override
    public OrdreVersement delete(OrdreVersement ordreVersement) throws JadePersistenceException, LotException {
        if (ordreVersement == null) {
            throw new LotException("Unable to delete ordreVersement, the given model is null !");
        }

        try {
            ordreVersement.setSimpleOrdreVersement(PerseusImplServiceLocator.getSimpleOrdreVersementService().delete(
                    ordreVersement.getSimpleOrdreVersement()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new LotException("Service not available - " + e.getMessage());
        }
        return ordreVersement;
    }

    @Override
    public OrdreVersement read(String idOrdreVersement) throws JadePersistenceException, LotException {
        if (JadeStringUtil.isEmpty(idOrdreVersement)) {
            throw new LotException("Unable to read OrdreVersement, the id passed is null !");
        }
        OrdreVersement ordreVersement = new OrdreVersement();
        ordreVersement.setId(idOrdreVersement);
        return (OrdreVersement) JadePersistenceManager.read(ordreVersement);
    }

    @Override
    public OrdreVersementSearchModel search(OrdreVersementSearchModel searchModel) throws JadePersistenceException,
            LotException {
        if (searchModel == null) {
            throw new LotException("Unable to search OrdreVersement, the search model passed is null !");
        }
        return (OrdreVersementSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public OrdreVersement update(OrdreVersement ordreVersement) throws JadePersistenceException, LotException {
        if (ordreVersement == null) {
            throw new LotException("Unable to update ordreVersement, the given model is null!");
        }

        try {

            SimpleOrdreVersement simpleOrdreVersement = ordreVersement.getSimpleOrdreVersement();
            simpleOrdreVersement.setIdPrestation(ordreVersement.getSimplePrestation().getId());
            ordreVersement.setSimpleOrdreVersement(PerseusImplServiceLocator.getSimpleOrdreVersementService().update(
                    simpleOrdreVersement));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new LotException("Service not available - " + e.getMessage());
        }

        return ordreVersement;
    }

}
