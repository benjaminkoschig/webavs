package ch.globaz.perseus.businessimpl.services.models.informationfacture;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.informationfacture.InformationFactureException;
import ch.globaz.perseus.business.models.informationfacture.SimpleInformationFacture;
import ch.globaz.perseus.business.models.informationfacture.SimpleInformationFactureSearchModel;
import ch.globaz.perseus.business.services.models.informationfacture.SimpleInformationFactureService;
import ch.globaz.perseus.businessimpl.checkers.informationfacture.SimpleInformationFactureChecker;

public class SimpleInformationFactureServiceImpl implements SimpleInformationFactureService {

    @Override
    public int count(SimpleInformationFactureSearchModel searchModel) throws JadePersistenceException,
            InformationFactureException {
        if (searchModel == null) {
            throw new InformationFactureException(
                    "Unable to count simple information facture, the model passed is null");
        }
        return JadePersistenceManager.count(searchModel);
    }

    @Override
    public SimpleInformationFacture create(SimpleInformationFacture informationFacture)
            throws JadePersistenceException, InformationFactureException {
        if (informationFacture == null) {
            throw new InformationFactureException("Unable to create information facture, the model passed is null");
        }
        SimpleInformationFactureChecker.checkForCreate(informationFacture);
        return (SimpleInformationFacture) JadePersistenceManager.add(informationFacture);
    }

    @Override
    public SimpleInformationFacture delete(SimpleInformationFacture informationiFacture)
            throws JadePersistenceException, InformationFactureException {
        if (informationiFacture == null) {
            throw new InformationFactureException("Unable to delete information facture, the model passed is null");
        }
        return (SimpleInformationFacture) JadePersistenceManager.delete(informationiFacture);
    }

    @Override
    public SimpleInformationFacture read(String idInformationFacture) throws JadePersistenceException,
            InformationFactureException {
        if (idInformationFacture == null) {
            throw new InformationFactureException("Unable to read simple information facture, the id passed is null");
        }
        SimpleInformationFacture simpleInformationFacture = new SimpleInformationFacture();
        simpleInformationFacture.setId(idInformationFacture);
        return (SimpleInformationFacture) JadePersistenceManager.read(simpleInformationFacture);
    }

    @Override
    public SimpleInformationFactureSearchModel search(SimpleInformationFactureSearchModel searchModel)
            throws JadePersistenceException, InformationFactureException {
        if (searchModel == null) {
            throw new InformationFactureException("Unable to search information facture, the model passed is null");
        }
        return (SimpleInformationFactureSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public SimpleInformationFacture update(SimpleInformationFacture informationFacture)
            throws JadePersistenceException, InformationFactureException {
        if (informationFacture == null) {
            throw new InformationFactureException(
                    "Unable to update simple information facture, the model passed is null");
        }
        SimpleInformationFactureChecker.checkForUpdate(informationFacture);
        return (SimpleInformationFacture) JadePersistenceManager.update(informationFacture);
    }

}
