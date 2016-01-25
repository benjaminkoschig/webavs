package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CompteBancaireCCPException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleCompteBancaireCCP;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleCompteBancaireCCPSearch;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.SimpleCompteBancaireCCPService;
import ch.globaz.pegasus.businessimpl.checkers.fortuneusuelle.SimpleCompteBancaireCCPChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleCompteBancaireCCPServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleCompteBancaireCCPService {

    @Override
    public SimpleCompteBancaireCCP create(SimpleCompteBancaireCCP simpleCompteBancaireCCP)
            throws JadePersistenceException, CompteBancaireCCPException {
        if (simpleCompteBancaireCCP == null) {
            throw new CompteBancaireCCPException("Unable to create simpleCompteBancaireCCP, the model passed is null!");
        }
        SimpleCompteBancaireCCPChecker.checkForCreate(simpleCompteBancaireCCP);
        return (SimpleCompteBancaireCCP) JadePersistenceManager.add(simpleCompteBancaireCCP);
    }

    @Override
    public SimpleCompteBancaireCCP delete(SimpleCompteBancaireCCP simpleCompteBancaireCCP)
            throws CompteBancaireCCPException, JadePersistenceException {
        if (simpleCompteBancaireCCP == null) {
            throw new CompteBancaireCCPException(
                    "Unable to delete SimpleCompteBancaireCCPChecker, the model passed is null!");
        }
        if (simpleCompteBancaireCCP.isNew()) {
            throw new CompteBancaireCCPException(
                    "Unable to delete SimpleCompteBancaireCCPChecker, the model passed is new!");
        }
        SimpleCompteBancaireCCPChecker.checkForDelete(simpleCompteBancaireCCP);
        return (SimpleCompteBancaireCCP) JadePersistenceManager.delete(simpleCompteBancaireCCP);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleCompteBancaireCCPSearch search = new SimpleCompteBancaireCCPSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    @Override
    public SimpleCompteBancaireCCP read(String idCompteBancaireCCP) throws JadePersistenceException,
            CompteBancaireCCPException {
        if (JadeStringUtil.isEmpty(idCompteBancaireCCP)) {
            throw new CompteBancaireCCPException(
                    "Unable to read SimpleCompteBancaireCCPChecker, the id passed is not defined!");
        }
        SimpleCompteBancaireCCP simpleCompteBancaireCCP = new SimpleCompteBancaireCCP();
        simpleCompteBancaireCCP.setId(idCompteBancaireCCP);
        return (SimpleCompteBancaireCCP) JadePersistenceManager.read(simpleCompteBancaireCCP);
    }

    @Override
    public SimpleCompteBancaireCCP update(SimpleCompteBancaireCCP simpleCompteBancaireCCP)
            throws JadePersistenceException, CompteBancaireCCPException {
        if (simpleCompteBancaireCCP == null) {
            throw new CompteBancaireCCPException(
                    "Unable to update SimpleCompteBancaireCCPChecker, the model passed is null!");
        }
        if (simpleCompteBancaireCCP.isNew()) {
            throw new CompteBancaireCCPException(
                    "Unable to update SimpleCompteBancaireCCPChecker, the model passed is new!");
        }
        SimpleCompteBancaireCCPChecker.checkForUpdate(simpleCompteBancaireCCP);
        return (SimpleCompteBancaireCCP) JadePersistenceManager.update(simpleCompteBancaireCCP);
    }

}