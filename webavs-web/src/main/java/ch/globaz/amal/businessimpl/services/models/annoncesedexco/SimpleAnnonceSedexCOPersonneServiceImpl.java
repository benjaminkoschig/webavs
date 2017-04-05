package ch.globaz.amal.businessimpl.services.models.annoncesedexco;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.annoncesedexco.AnnonceSedexCOException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOPersonne;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOPersonneSearch;
import ch.globaz.amal.business.services.models.annoncesedexco.SimpleAnnonceSedexCOPersonneService;

public class SimpleAnnonceSedexCOPersonneServiceImpl implements SimpleAnnonceSedexCOPersonneService {

    @Override
    public SimpleAnnonceSedexCOPersonne create(SimpleAnnonceSedexCOPersonne simpleAnnonceSedexCOPersonne)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException, AnnonceSedexCOException,
            DetailFamilleException {
        if (simpleAnnonceSedexCOPersonne == null) {
            throw new AnnonceSedexCOException(
                    "Unable to create simpleAnnonceSedexCOPersonne in DB, the model passed is null!");
        }
        return (SimpleAnnonceSedexCOPersonne) JadePersistenceManager.add(simpleAnnonceSedexCOPersonne);
    }

    @Override
    public SimpleAnnonceSedexCOPersonne delete(SimpleAnnonceSedexCOPersonne simpleAnnonceSedexCOPersonne)
            throws JadePersistenceException, AnnonceSedexCOException, JadeApplicationServiceNotAvailableException {
        if (simpleAnnonceSedexCOPersonne == null) {
            throw new AnnonceSedexCOException(
                    "Unable to delete simpleAnnonceSedexCOPersonne in DB, the model passed is null!");
        }
        return (SimpleAnnonceSedexCOPersonne) JadePersistenceManager.delete(simpleAnnonceSedexCOPersonne);
    }

    @Override
    public SimpleAnnonceSedexCOPersonne read(String idAnnonceSedexCOPersonne) throws JadePersistenceException,
            AnnonceSedexCOException {
        if (JadeStringUtil.isEmpty(idAnnonceSedexCOPersonne)) {
            throw new AnnonceSedexCOException(
                    "Unable to read the annonce sedex co personne from db, id passed is empty");
        }
        SimpleAnnonceSedexCOPersonne simpleAnnonceSedexCOPersonne = new SimpleAnnonceSedexCOPersonne();
        simpleAnnonceSedexCOPersonne.setId(idAnnonceSedexCOPersonne);
        return (SimpleAnnonceSedexCOPersonne) JadePersistenceManager.read(simpleAnnonceSedexCOPersonne);
    }

    @Override
    public SimpleAnnonceSedexCOPersonneSearch search(
            SimpleAnnonceSedexCOPersonneSearch simpleAnnonceSedexCOPersonneSearch) throws JadePersistenceException,
            AnnonceSedexCOException {
        if (simpleAnnonceSedexCOPersonneSearch == null) {
            throw new AnnonceSedexCOException(
                    "Unable to search simpleAnnonceSedexCOPersonne in DB, the model passed is null!");
        }
        return (SimpleAnnonceSedexCOPersonneSearch) JadePersistenceManager.search(simpleAnnonceSedexCOPersonneSearch);
    }

    @Override
    public SimpleAnnonceSedexCOPersonne update(SimpleAnnonceSedexCOPersonne simpleAnnonceSedexCOPersonne)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException, AnnonceSedexCOException,
            DetailFamilleException {
        if (simpleAnnonceSedexCOPersonne == null) {
            throw new AnnonceSedexCOException(
                    "Unable to update simpleAnnonceSedexCOPersonne in DB, the model passed is null!");
        }
        return (SimpleAnnonceSedexCOPersonne) JadePersistenceManager.update(simpleAnnonceSedexCOPersonne);
    }

}
