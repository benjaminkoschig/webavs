package ch.globaz.pegasus.businessimpl.services.models.parametre;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.ForfaitsPrimesAssuranceMaladie;
import ch.globaz.pegasus.business.models.parametre.ForfaitsPrimesAssuranceMaladieSearch;
import ch.globaz.pegasus.business.models.parametre.SimpleForfaitPrimesAssuranceMaladie;
import ch.globaz.pegasus.business.services.models.parametre.ForfaitsPrimesAssuranceMaladieService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * @author DMA
 * @date 12 nov. 2010
 */
public class ForfaitsPrimesAssuranceMaladieServiceImpl extends PegasusAbstractServiceImpl implements
        ForfaitsPrimesAssuranceMaladieService {

    @Override
    public int count(ForfaitsPrimesAssuranceMaladieSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException {
        if (search == null) {
            throw new ForfaitsPrimesAssuranceMaladieException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public ForfaitsPrimesAssuranceMaladie create(ForfaitsPrimesAssuranceMaladie forfaitsPrimesAssuranceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        SimpleForfaitPrimesAssuranceMaladie forfaitAssMal = PegasusImplServiceLocator
                .getSimpleForfaitPrimesAssuranceMaladieService().create(
                        forfaitsPrimesAssuranceMaladie.getSimpleForfaitPrimesAssuranceMaladie());
        forfaitsPrimesAssuranceMaladie.setSimpleForfaitPrimesAssuranceMaladie(forfaitAssMal);
        return forfaitsPrimesAssuranceMaladie;
    }

    @Override
    public ForfaitsPrimesAssuranceMaladie delete(ForfaitsPrimesAssuranceMaladie forfaitsPrimesAssuranceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        SimpleForfaitPrimesAssuranceMaladie simpleForfaitAssMal = forfaitsPrimesAssuranceMaladie
                .getSimpleForfaitPrimesAssuranceMaladie();
        simpleForfaitAssMal = PegasusImplServiceLocator.getSimpleForfaitPrimesAssuranceMaladieService().delete(
                simpleForfaitAssMal);
        forfaitsPrimesAssuranceMaladie.setSimpleForfaitPrimesAssuranceMaladie(simpleForfaitAssMal);
        return forfaitsPrimesAssuranceMaladie;
    }

    @Override
    public ForfaitsPrimesAssuranceMaladie read(String idForfaitPrimesAssuranceMaladie) throws JadePersistenceException,
            ForfaitsPrimesAssuranceMaladieException {
        if (idForfaitPrimesAssuranceMaladie == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to read idForfaitPrimesAssuranceMaladie, the id passed is null!");
        }
        ForfaitsPrimesAssuranceMaladie forfaitsPrimesAssuranceMaladie = new ForfaitsPrimesAssuranceMaladie();
        forfaitsPrimesAssuranceMaladie.setId(idForfaitPrimesAssuranceMaladie);
        return (ForfaitsPrimesAssuranceMaladie) JadePersistenceManager.read(forfaitsPrimesAssuranceMaladie);
    }

    @Override
    public ForfaitsPrimesAssuranceMaladieSearch search(ForfaitsPrimesAssuranceMaladieSearch search)
            throws JadePersistenceException, ForfaitsPrimesAssuranceMaladieException {
        if (search == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to search forfaitsPrimesAssuranceMaladie, the model passed is null!");
        }
        if (!JadeStringUtil.isEmpty(search.getForDateValable())) {
            search.setWhereKey(ForfaitsPrimesAssuranceMaladieSearch.WITH_DATE_VALABLE_LE);
        }

        return (ForfaitsPrimesAssuranceMaladieSearch) JadePersistenceManager.search(search);
    }

    @Override
    public ForfaitsPrimesAssuranceMaladie update(ForfaitsPrimesAssuranceMaladie forfaitsPrimesAssuranceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        SimpleForfaitPrimesAssuranceMaladie simpleForfaitAssMal = forfaitsPrimesAssuranceMaladie
                .getSimpleForfaitPrimesAssuranceMaladie();
        simpleForfaitAssMal = PegasusImplServiceLocator.getSimpleForfaitPrimesAssuranceMaladieService().update(
                simpleForfaitAssMal);
        forfaitsPrimesAssuranceMaladie.setSimpleForfaitPrimesAssuranceMaladie(simpleForfaitAssMal);
        return forfaitsPrimesAssuranceMaladie;
    }
}
