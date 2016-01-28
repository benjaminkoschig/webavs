package ch.globaz.perseus.businessimpl.services.models.echeance;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.EcheanceLibreException;
import ch.globaz.perseus.business.models.echeance.EcheanceLibre;
import ch.globaz.perseus.business.models.echeance.EcheanceLibreSearchModel;
import ch.globaz.perseus.business.models.echeance.SimpleEcheanceLibre;
import ch.globaz.perseus.business.services.models.echeance.EcheanceLibreService;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class EcheanceLibreServiceImpl implements EcheanceLibreService {

    @Override
    public int count(EcheanceLibreSearchModel search) throws EcheanceLibreException, JadePersistenceException {
        if (search == null) {
            throw new EcheanceLibreException("Unable to count echeance libre, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public EcheanceLibre create(EcheanceLibre echeanceLibre) throws JadePersistenceException, EcheanceLibreException,
            JadeApplicationServiceNotAvailableException {
        if (echeanceLibre == null) {
            throw new EcheanceLibreException("Unable to create echeance libre, the model passed is null!");
        }
        SimpleEcheanceLibre simpleEcheanceLibre = echeanceLibre.getSimpleEcheanceLibre();
        simpleEcheanceLibre = PerseusImplServiceLocator.getSimpleEcheanceLibreService().create(simpleEcheanceLibre);
        echeanceLibre.setSimpleEcheanceLibre(simpleEcheanceLibre);
        return echeanceLibre;
    }

    @Override
    public EcheanceLibre delete(EcheanceLibre echeanceLibre) throws JadePersistenceException, EcheanceLibreException,
            JadeApplicationServiceNotAvailableException {
        if (echeanceLibre == null) {
            throw new EcheanceLibreException("Unable to delete echeance libre, the model passed is null!");
        }
        echeanceLibre.setSimpleEcheanceLibre(PerseusImplServiceLocator.getSimpleEcheanceLibreService().delete(
                echeanceLibre.getSimpleEcheanceLibre()));
        return echeanceLibre;
    }

    @Override
    public EcheanceLibre read(String idEcheanceLibre) throws JadePersistenceException, EcheanceLibreException {
        if (JadeStringUtil.isEmpty(idEcheanceLibre)) {
            throw new EcheanceLibreException("Unable to read a echeance libre, the id passed is null!");
        }
        EcheanceLibre echeanceLibre = new EcheanceLibre();
        echeanceLibre.setId(idEcheanceLibre);
        return (EcheanceLibre) JadePersistenceManager.read(echeanceLibre);
    }

    @Override
    public EcheanceLibreSearchModel search(EcheanceLibreSearchModel searchModel) throws JadePersistenceException,
            EcheanceLibreException {
        if (searchModel == null) {
            throw new EcheanceLibreException("Unable to search echeance libre, the search model passed is null!");
        }
        return (EcheanceLibreSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public EcheanceLibre update(EcheanceLibre echeanceLibre) throws JadePersistenceException, EcheanceLibreException,
            JadeApplicationServiceNotAvailableException {
        if (echeanceLibre == null) {
            throw new EcheanceLibreException("Unable to update echeance libre, the model passed is null!");
        }
        SimpleEcheanceLibre simpleEcheanceLibre = echeanceLibre.getSimpleEcheanceLibre();
        simpleEcheanceLibre = PerseusImplServiceLocator.getSimpleEcheanceLibreService().update(simpleEcheanceLibre);
        echeanceLibre.setSimpleEcheanceLibre(simpleEcheanceLibre);
        return echeanceLibre;
    }

}
