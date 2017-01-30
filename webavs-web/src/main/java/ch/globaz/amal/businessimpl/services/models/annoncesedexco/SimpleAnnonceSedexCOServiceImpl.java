package ch.globaz.amal.businessimpl.services.models.annoncesedexco;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.annoncesedexco.AnnonceSedexCOException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOSearch;
import ch.globaz.amal.business.services.models.annoncesedexco.SimpleAnnonceSedexCOService;

public class SimpleAnnonceSedexCOServiceImpl implements SimpleAnnonceSedexCOService {

    @Override
    public SimpleAnnonceSedexCO create(SimpleAnnonceSedexCO simpleAnnonceSedexCO) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, AnnonceSedexCOException, DetailFamilleException {
        if (simpleAnnonceSedexCO == null) {
            throw new AnnonceSedexCOException("Unable to create simpleAnnonceSedexCO in DB, the model passed is null!");
        }
        return (SimpleAnnonceSedexCO) JadePersistenceManager.add(simpleAnnonceSedexCO);
    }

    @Override
    public SimpleAnnonceSedexCO delete(SimpleAnnonceSedexCO simpleAnnonceSedexCO) throws JadePersistenceException,
            AnnonceSedexCOException, JadeApplicationServiceNotAvailableException {
        if (simpleAnnonceSedexCO == null) {
            throw new AnnonceSedexCOException(
                    "Unable to delete simpleAnnonceSedexCO into DB, the model passed is null!");
        }
        return (SimpleAnnonceSedexCO) JadePersistenceManager.delete(simpleAnnonceSedexCO);
    }

    @Override
    public SimpleAnnonceSedexCO read(String idAnnonceSedexCO) throws JadePersistenceException, AnnonceSedexCOException {
        if (JadeStringUtil.isEmpty(idAnnonceSedexCO)) {
            throw new AnnonceSedexCOException("Unable to read the annonce sedex co from db, id passed is empty");
        }
        SimpleAnnonceSedexCO simpleAnnonceSedexCO = new SimpleAnnonceSedexCO();
        simpleAnnonceSedexCO.setId(idAnnonceSedexCO);
        return (SimpleAnnonceSedexCO) JadePersistenceManager.read(simpleAnnonceSedexCO);
    }

    @Override
    public SimpleAnnonceSedexCOSearch search(SimpleAnnonceSedexCOSearch search) throws JadePersistenceException,
            AnnonceSedexCOException {
        if (search == null) {
            throw new AnnonceSedexCOException(
                    "Unable to search a simple annonce sedexco , the search model passed is null");
        }
        return (SimpleAnnonceSedexCOSearch) JadePersistenceManager.search(search);
    }

    @Override
    public SimpleAnnonceSedexCO update(SimpleAnnonceSedexCO simpleAnnonceSedexCO) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, AnnonceSedexCOException, DetailFamilleException {
        if (simpleAnnonceSedexCO == null) {
            throw new AnnonceSedexCOException("Unable to update the simple annonce sedexco, the model passed is null");
        }
        return (SimpleAnnonceSedexCO) JadePersistenceManager.update(simpleAnnonceSedexCO);
    }

}
