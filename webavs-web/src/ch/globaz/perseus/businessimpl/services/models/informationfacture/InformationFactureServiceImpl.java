package ch.globaz.perseus.businessimpl.services.models.informationfacture;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.informationfacture.InformationFactureException;
import ch.globaz.perseus.business.models.informationfacture.InformationFacture;
import ch.globaz.perseus.business.models.informationfacture.InformationFactureSearchModel;
import ch.globaz.perseus.business.models.informationfacture.SimpleInformationFacture;
import ch.globaz.perseus.business.services.models.informationfacture.InformationFactureService;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class InformationFactureServiceImpl implements InformationFactureService {

    @Override
    public int count(InformationFactureSearchModel search) throws InformationFactureException, JadePersistenceException {
        if (search == null) {
            throw new InformationFactureException(
                    "Unable to count information facture, the search model passed is null!");
        }
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        return JadePersistenceManager.count(search);
    }

    @Override
    public InformationFacture create(InformationFacture informationFacture) throws JadePersistenceException,
            InformationFactureException, JadeApplicationServiceNotAvailableException {
        if (informationFacture == null) {
            throw new InformationFactureException("Unable to create information facture, the model passed is null!");
        }
        SimpleInformationFacture simpleInformationFacture = informationFacture.getSimpleInformationFacture();
        simpleInformationFacture = PerseusImplServiceLocator.getSimpleInformationFactureService().create(
                simpleInformationFacture);
        informationFacture.setSimpleInformationFacture(simpleInformationFacture);
        return informationFacture;
    }

    @Override
    public InformationFacture delete(InformationFacture informationFacture) throws JadePersistenceException,
            InformationFactureException, JadeApplicationServiceNotAvailableException {
        if (informationFacture == null) {
            throw new InformationFactureException("Unable to delete information facture, the model passed is null!");
        }
        informationFacture.setSimpleInformationFacture(PerseusImplServiceLocator.getSimpleInformationFactureService()
                .delete(informationFacture.getSimpleInformationFacture()));
        return informationFacture;
    }

    @Override
    public InformationFacture read(String idInformationFacture) throws JadePersistenceException,
            InformationFactureException {
        if (JadeStringUtil.isEmpty(idInformationFacture)) {
            throw new InformationFactureException("Unable to read a information facture, the id passed is null!");
        }
        InformationFacture informationFacture = new InformationFacture();
        informationFacture.setId(idInformationFacture);
        return (InformationFacture) JadePersistenceManager.read(informationFacture);
    }

    @Override
    public InformationFactureSearchModel search(InformationFactureSearchModel searchModel)
            throws JadePersistenceException, InformationFactureException {
        if (searchModel == null) {
            throw new InformationFactureException(
                    "Unable to search information facture, the search model passed is null!");
        }
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        return (InformationFactureSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public InformationFacture update(InformationFacture informationFacture) throws JadePersistenceException,
            InformationFactureException, JadeApplicationServiceNotAvailableException {
        if (informationFacture == null) {
            throw new InformationFactureException("Unable to update information facture, the model passed is null!");
        }
        SimpleInformationFacture simpleInformationFacture = informationFacture.getSimpleInformationFacture();
        simpleInformationFacture = PerseusImplServiceLocator.getSimpleInformationFactureService().update(
                simpleInformationFacture);
        informationFacture.setSimpleInformationFacture(simpleInformationFacture);
        return informationFacture;
    }
}
