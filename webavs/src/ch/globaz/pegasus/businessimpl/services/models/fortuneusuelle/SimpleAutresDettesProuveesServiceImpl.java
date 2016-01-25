package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AutresDettesProuveesException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleAutresDettesProuvees;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleAutresDettesProuveesSearch;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.SimpleAutresDettesProuveesService;
import ch.globaz.pegasus.businessimpl.checkers.fortuneusuelle.SimpleAutresDettesProuveesChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleAutresDettesProuveesServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleAutresDettesProuveesService {

    @Override
    public SimpleAutresDettesProuvees create(SimpleAutresDettesProuvees simpleAutresDettesProuvees)
            throws JadePersistenceException, AutresDettesProuveesException {
        if (simpleAutresDettesProuvees == null) {
            throw new AutresDettesProuveesException(
                    "Unable to create simpleAutresDettesProuvees, the model passed is null!");
        }
        SimpleAutresDettesProuveesChecker.checkForCreate(simpleAutresDettesProuvees);
        return (SimpleAutresDettesProuvees) JadePersistenceManager.add(simpleAutresDettesProuvees);
    }

    @Override
    public SimpleAutresDettesProuvees delete(SimpleAutresDettesProuvees simpleAutresDettesProuvees)
            throws AutresDettesProuveesException, JadePersistenceException {
        if (simpleAutresDettesProuvees == null) {
            throw new AutresDettesProuveesException(
                    "Unable to delete SimpleAutresDettesProuveesChecker, the model passed is null!");
        }
        if (simpleAutresDettesProuvees.isNew()) {
            throw new AutresDettesProuveesException(
                    "Unable to delete SimpleAutresDettesProuveesChecker, the model passed is new!");
        }
        SimpleAutresDettesProuveesChecker.checkForDelete(simpleAutresDettesProuvees);
        return (SimpleAutresDettesProuvees) JadePersistenceManager.delete(simpleAutresDettesProuvees);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleAutresDettesProuveesSearch search = new SimpleAutresDettesProuveesSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    @Override
    public SimpleAutresDettesProuvees read(String idAutresDettesProuvees) throws JadePersistenceException,
            AutresDettesProuveesException {
        if (JadeStringUtil.isEmpty(idAutresDettesProuvees)) {
            throw new AutresDettesProuveesException(
                    "Unable to read SimpleAutresDettesProuveesChecker, the id passed is not defined!");
        }
        SimpleAutresDettesProuvees simpleAutresDettesProuvees = new SimpleAutresDettesProuvees();
        simpleAutresDettesProuvees.setId(idAutresDettesProuvees);
        return (SimpleAutresDettesProuvees) JadePersistenceManager.read(simpleAutresDettesProuvees);
    }

    @Override
    public SimpleAutresDettesProuvees update(SimpleAutresDettesProuvees simpleAutresDettesProuvees)
            throws JadePersistenceException, AutresDettesProuveesException {
        if (simpleAutresDettesProuvees == null) {
            throw new AutresDettesProuveesException(
                    "Unable to update SimpleAutresDettesProuveesChecker, the model passed is null!");
        }
        if (simpleAutresDettesProuvees.isNew()) {
            throw new AutresDettesProuveesException(
                    "Unable to update SimpleAutresDettesProuveesChecker, the model passed is new!");
        }
        SimpleAutresDettesProuveesChecker.checkForUpdate(simpleAutresDettesProuvees);
        return (SimpleAutresDettesProuvees) JadePersistenceManager.update(simpleAutresDettesProuvees);
    }

}