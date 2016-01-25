package ch.globaz.pegasus.businessimpl.services.models.monnaieetrangere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangere;
import ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangereSearch;
import ch.globaz.pegasus.business.services.models.monnaieetrangere.MonnaieEtrangereService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * @author SCE
 */
public class MonnaieEtrangereServiceImpl extends PegasusAbstractServiceImpl implements MonnaieEtrangereService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.monnaieetrangere. MonnaieEtrangereService
     * #count(ch.globaz.pegasus.business.models.monnaieetrangere .MonnaieEtrangereSearch)
     */
    @Override
    public int count(MonnaieEtrangereSearch monnaieEtrangereSearch) throws MonnaieEtrangereException,
            JadePersistenceException {
        if (monnaieEtrangereSearch == null) {
            throw new MonnaieEtrangereException("Unable to count monnaiesetrangeres, the search model passed is null!");
        }

        return JadePersistenceManager.count(monnaieEtrangereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.monnaieetrangere. MonnaieEtrangereService
     * #create(ch.globaz.pegasus.business.models.monnaieetrangere .MonnaieEtrangere)
     */
    @Override
    public MonnaieEtrangere create(MonnaieEtrangere monnaieEtrangere) throws JadePersistenceException,
            MonnaieEtrangereException {
        if (monnaieEtrangere == null) {
            throw new MonnaieEtrangereException("Unable to create monnaiesetrangeres, the model passed is null!");
        }

        try {
            monnaieEtrangere.setSimpleMonnaieEtrangere(PegasusImplServiceLocator.getSimpleMonnaieEtrangereService()
                    .create(monnaieEtrangere.getSimpleMonnaieEtrangere()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new MonnaieEtrangereException("Service not available - " + e.getMessage());
        }
        return monnaieEtrangere;

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.monnaieetrangere. MonnaieEtrangereService
     * #delete(ch.globaz.pegasus.business.models.monnaieetrangere .MonnaieEtrangere)
     */
    @Override
    public MonnaieEtrangere delete(MonnaieEtrangere monnaieEtrangere) throws MonnaieEtrangereException,
            JadePersistenceException {
        if (monnaieEtrangere == null) {
            throw new MonnaieEtrangereException("Unable to delete monnaieEtrangere, the model passed is null!");
        }
        try {
            monnaieEtrangere.setSimpleMonnaieEtrangere(PegasusImplServiceLocator.getSimpleMonnaieEtrangereService()
                    .delete(monnaieEtrangere.getSimpleMonnaieEtrangere()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new MonnaieEtrangereException("Service not available - " + e.getMessage());
        }
        return monnaieEtrangere;
    }

    @Override
    public MonnaieEtrangere read(String idMonnaieEtrangere) throws JadePersistenceException, MonnaieEtrangereException {

        if (JadeStringUtil.isEmpty(idMonnaieEtrangere)) {
            throw new MonnaieEtrangereException("Unable to read monnaieetrangere, the id passed is null!");
        }
        MonnaieEtrangere monnaieEtrangere = new MonnaieEtrangere();
        monnaieEtrangere.setId(idMonnaieEtrangere);
        return (MonnaieEtrangere) JadePersistenceManager.read(monnaieEtrangere);
    }

    @Override
    public MonnaieEtrangereSearch search(MonnaieEtrangereSearch monnaieEtrangereSearch)
            throws JadePersistenceException, MonnaieEtrangereException {
        if (monnaieEtrangereSearch == null) {
            throw new MonnaieEtrangereException("Unable to search monnaieetrangere, the search model passed is null!");
        }
        // Where key forDateValable
        monnaieEtrangereSearch.setWhereKey(JadeStringUtil.isEmpty(monnaieEtrangereSearch.getForDateValable()) ? null
                : "withDateValable");
        return (MonnaieEtrangereSearch) JadePersistenceManager.search(monnaieEtrangereSearch);

    }

    @Override
    public MonnaieEtrangere update(MonnaieEtrangere monnaieEtrangere) throws JadePersistenceException,
            MonnaieEtrangereException {
        if (monnaieEtrangere == null) {
            throw new MonnaieEtrangereException("Unable to update monnaieetrangere, the model passed is null!");
        }
        try {
            PegasusImplServiceLocator.getSimpleMonnaieEtrangereService().update(
                    monnaieEtrangere.getSimpleMonnaieEtrangere());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new MonnaieEtrangereException("Service not available - " + e.toString(), e);
        }
        return monnaieEtrangere;
    }

}
