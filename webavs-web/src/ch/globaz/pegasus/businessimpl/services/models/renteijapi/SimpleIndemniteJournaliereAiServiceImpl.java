package ch.globaz.pegasus.businessimpl.services.models.renteijapi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IndemniteJournaliereAiException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleIndemniteJournaliereAi;
import ch.globaz.pegasus.business.models.renteijapi.SimpleIndemniteJournaliereAiSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.renteijapi.SimpleIndemniteJournaliereAiService;
import ch.globaz.pegasus.businessimpl.checkers.renteijapi.SimpleIndemniteJournaliereAiChecker;

public class SimpleIndemniteJournaliereAiServiceImpl extends PegasusServiceLocator implements
        SimpleIndemniteJournaliereAiService {

    /**
     * Création d'une indemiteJournalierAi
     */
    @Override
    public SimpleIndemniteJournaliereAi create(SimpleIndemniteJournaliereAi simpleIndemniteJournaliereAi)
            throws IndemniteJournaliereAiException, JadePersistenceException {
        if (simpleIndemniteJournaliereAi == null) {
            throw new IndemniteJournaliereAiException(
                    "Unable to create simpleIndemniteJournaliereAi, the model passed is null!");
        }
        SimpleIndemniteJournaliereAiChecker.checkForCreate(simpleIndemniteJournaliereAi);

        return (SimpleIndemniteJournaliereAi) JadePersistenceManager.add(simpleIndemniteJournaliereAi);
    }

    /**
     * Supression d'une indemnite journaliereAi
     */
    @Override
    public SimpleIndemniteJournaliereAi delete(SimpleIndemniteJournaliereAi simpleIndemniteJournaliereAi)
            throws IndemniteJournaliereAiException, JadePersistenceException {
        // Check si model pas null
        if (simpleIndemniteJournaliereAi == null) {
            throw new IndemniteJournaliereAiException(
                    "Unable to delete simpleIndemniteJournaliereAi, the model passed is null!");
        }
        // check si model pas new
        if (simpleIndemniteJournaliereAi.isNew()) {
            throw new IndemniteJournaliereAiException(
                    "Unable to delelte indemniteJournalierai, the model passed is new!");
        }
        SimpleIndemniteJournaliereAiChecker.checkForDelete(simpleIndemniteJournaliereAi);
        return (SimpleIndemniteJournaliereAi) JadePersistenceManager.delete(simpleIndemniteJournaliereAi);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.renteijapi. SimpleIndemniteJournaliereAiService
     * #deleteParListeIdDoFinH(java.lang.String)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleIndemniteJournaliereAiSearch search = new SimpleIndemniteJournaliereAiSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);

    }

    /**
     * Lecture d'une indemniteJournalierai
     */
    @Override
    public SimpleIndemniteJournaliereAi read(String idIndemniteJournaliereAi) throws IndemniteJournaliereAiException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idIndemniteJournaliereAi)) {
            throw new IndemniteJournaliereAiException("Unable to read the model, the id passed is null!");
        }
        SimpleIndemniteJournaliereAi simpleIndemniteJournaliereAi = new SimpleIndemniteJournaliereAi();
        simpleIndemniteJournaliereAi.setId(idIndemniteJournaliereAi);

        return (SimpleIndemniteJournaliereAi) JadePersistenceManager.read(simpleIndemniteJournaliereAi);
    }

    /**
     * MIse à jour d'une indemniteJournalierai
     */
    @Override
    public SimpleIndemniteJournaliereAi update(SimpleIndemniteJournaliereAi simpleIndemniteJournaliereAi)
            throws IndemniteJournaliereAiException, JadePersistenceException {
        // check si model pas null
        if (simpleIndemniteJournaliereAi == null) {
            throw new IndemniteJournaliereAiException(
                    "Unable to update the simpleIndemniteJournaliereAi, the model passed is null!");
        }
        // check si model pas new
        if (simpleIndemniteJournaliereAi.isNew()) {
            throw new IndemniteJournaliereAiException(
                    "Unable to update indemniteJournalierai, the model passed is new!");
        }
        SimpleIndemniteJournaliereAiChecker.checkForUpdate(simpleIndemniteJournaliereAi);
        return (SimpleIndemniteJournaliereAi) JadePersistenceManager.update(simpleIndemniteJournaliereAi);
    }

}
