package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CapitalLPPException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleCapitalLPP;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleCapitalLPPSearch;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.SimpleCapitalLPPService;
import ch.globaz.pegasus.businessimpl.checkers.fortuneusuelle.SimpleCapitalLPPChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleCapitalLPPServiceImpl extends PegasusAbstractServiceImpl implements SimpleCapitalLPPService {

    @Override
    public SimpleCapitalLPP create(SimpleCapitalLPP simpleCapitalLPP) throws JadePersistenceException,
            CapitalLPPException {
        if (simpleCapitalLPP == null) {
            throw new CapitalLPPException("Unable to create simpleCapitalLPP, the model passed is null!");
        }
        SimpleCapitalLPPChecker.checkForCreate(simpleCapitalLPP);
        return (SimpleCapitalLPP) JadePersistenceManager.add(simpleCapitalLPP);
    }

    @Override
    public SimpleCapitalLPP delete(SimpleCapitalLPP simpleCapitalLPP) throws CapitalLPPException,
            JadePersistenceException {
        if (simpleCapitalLPP == null) {
            throw new CapitalLPPException("Unable to delete SimpleCapitalLPPChecker, the model passed is null!");
        }
        if (simpleCapitalLPP.isNew()) {
            throw new CapitalLPPException("Unable to delete SimpleCapitalLPPChecker, the model passed is new!");
        }
        SimpleCapitalLPPChecker.checkForDelete(simpleCapitalLPP);
        return (SimpleCapitalLPP) JadePersistenceManager.delete(simpleCapitalLPP);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleCapitalLPPSearch search = new SimpleCapitalLPPSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    @Override
    public SimpleCapitalLPP read(String idCapitalLPP) throws JadePersistenceException, CapitalLPPException {
        if (JadeStringUtil.isEmpty(idCapitalLPP)) {
            throw new CapitalLPPException("Unable to read SimpleCapitalLPPChecker, the id passed is not defined!");
        }
        SimpleCapitalLPP simpleCapitalLPP = new SimpleCapitalLPP();
        simpleCapitalLPP.setId(idCapitalLPP);
        return (SimpleCapitalLPP) JadePersistenceManager.read(simpleCapitalLPP);
    }

    @Override
    public SimpleCapitalLPP update(SimpleCapitalLPP simpleCapitalLPP) throws JadePersistenceException,
            CapitalLPPException {
        if (simpleCapitalLPP == null) {
            throw new CapitalLPPException("Unable to update SimpleCapitalLPPChecker, the model passed is null!");
        }
        if (simpleCapitalLPP.isNew()) {
            throw new CapitalLPPException("Unable to update SimpleCapitalLPPChecker, the model passed is new!");
        }
        SimpleCapitalLPPChecker.checkForUpdate(simpleCapitalLPP);
        return (SimpleCapitalLPP) JadePersistenceManager.update(simpleCapitalLPP);
    }

}