package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AssuranceVieException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleAssuranceVie;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleAssuranceVieSearch;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.SimpleAssuranceVieService;
import ch.globaz.pegasus.businessimpl.checkers.fortuneusuelle.SimpleAssuranceVieChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleAssuranceVieServiceImpl extends PegasusAbstractServiceImpl implements SimpleAssuranceVieService {

    @Override
    public SimpleAssuranceVie create(SimpleAssuranceVie simpleAssuranceVie) throws JadePersistenceException,
            AssuranceVieException {
        if (simpleAssuranceVie == null) {
            throw new AssuranceVieException("Unable to create simpleAssuranceVie, the model passed is null!");
        }
        SimpleAssuranceVieChecker.checkForCreate(simpleAssuranceVie);
        return (SimpleAssuranceVie) JadePersistenceManager.add(simpleAssuranceVie);
    }

    @Override
    public SimpleAssuranceVie delete(SimpleAssuranceVie simpleAssuranceVie) throws AssuranceVieException,
            JadePersistenceException {
        if (simpleAssuranceVie == null) {
            throw new AssuranceVieException("Unable to delete SimpleAssuranceVieChecker, the model passed is null!");
        }
        if (simpleAssuranceVie.isNew()) {
            throw new AssuranceVieException("Unable to delete SimpleAssuranceVieChecker, the model passed is new!");
        }
        SimpleAssuranceVieChecker.checkForDelete(simpleAssuranceVie);
        return (SimpleAssuranceVie) JadePersistenceManager.delete(simpleAssuranceVie);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleAssuranceVieSearch search = new SimpleAssuranceVieSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    @Override
    public SimpleAssuranceVie read(String idAssuranceVie) throws JadePersistenceException, AssuranceVieException {
        if (JadeStringUtil.isEmpty(idAssuranceVie)) {
            throw new AssuranceVieException("Unable to read SimpleAssuranceVieChecker, the id passed is not defined!");
        }
        SimpleAssuranceVie simpleAssuranceVie = new SimpleAssuranceVie();
        simpleAssuranceVie.setId(idAssuranceVie);
        return (SimpleAssuranceVie) JadePersistenceManager.read(simpleAssuranceVie);
    }

    @Override
    public SimpleAssuranceVie update(SimpleAssuranceVie simpleAssuranceVie) throws JadePersistenceException,
            AssuranceVieException {
        if (simpleAssuranceVie == null) {
            throw new AssuranceVieException("Unable to update SimpleAssuranceVieChecker, the model passed is null!");
        }
        if (simpleAssuranceVie.isNew()) {
            throw new AssuranceVieException("Unable to update SimpleAssuranceVieChecker, the model passed is new!");
        }
        SimpleAssuranceVieChecker.checkForUpdate(simpleAssuranceVie);
        return (SimpleAssuranceVie) JadePersistenceManager.update(simpleAssuranceVie);
    }

}