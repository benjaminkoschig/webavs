package ch.globaz.pegasus.businessimpl.services.models.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AssuranceRenteViagereException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleAssuranceRenteViagere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleAssuranceRenteViagereSearch;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.SimpleAssuranceRenteViagereService;
import ch.globaz.pegasus.businessimpl.checkers.fortuneparticuliere.SimpleAssuranceRenteViagereChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleAssuranceRenteViagereServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleAssuranceRenteViagereService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleAssuranceRenteViagereService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleAssuranceRenteViagere)
     */
    @Override
    public SimpleAssuranceRenteViagere create(SimpleAssuranceRenteViagere simpleAssuranceRenteViagere)
            throws JadePersistenceException, AssuranceRenteViagereException {
        if (simpleAssuranceRenteViagere == null) {
            throw new AssuranceRenteViagereException(
                    "Unable to create simpleAssuranceRenteViagere, the model passed is null!");
        }
        SimpleAssuranceRenteViagereChecker.checkForCreate(simpleAssuranceRenteViagere);
        return (SimpleAssuranceRenteViagere) JadePersistenceManager.add(simpleAssuranceRenteViagere);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleAssuranceRenteViagereService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleAssuranceRenteViagere)
     */
    @Override
    public SimpleAssuranceRenteViagere delete(SimpleAssuranceRenteViagere simpleAssuranceRenteViagere)
            throws AssuranceRenteViagereException, JadePersistenceException {
        if (simpleAssuranceRenteViagere == null) {
            throw new AssuranceRenteViagereException(
                    "Unable to delete simpleAssuranceRenteViagere, the model passed is null!");
        }
        if (simpleAssuranceRenteViagere.isNew()) {
            throw new AssuranceRenteViagereException(
                    "Unable to delete simpleAssuranceRenteViagere, the model passed is new!");
        }
        SimpleAssuranceRenteViagereChecker.checkForDelete(simpleAssuranceRenteViagere);
        return (SimpleAssuranceRenteViagere) JadePersistenceManager.delete(simpleAssuranceRenteViagere);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleAssuranceRenteViagereService#deleteParListeIdDoFinH(java.util.List)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleAssuranceRenteViagereSearch search = new SimpleAssuranceRenteViagereSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleAssuranceRenteViagereService#read(java.lang.String)
     */
    @Override
    public SimpleAssuranceRenteViagere read(String idAssuranceRenteViagere) throws JadePersistenceException,
            AssuranceRenteViagereException {
        if (JadeStringUtil.isEmpty(idAssuranceRenteViagere)) {
            throw new AssuranceRenteViagereException(
                    "Unable to read simpleAssuranceRenteViagere, the id passed is not defined!");
        }
        SimpleAssuranceRenteViagere simpleAssuranceRenteViagere = new SimpleAssuranceRenteViagere();
        simpleAssuranceRenteViagere.setId(idAssuranceRenteViagere);
        return (SimpleAssuranceRenteViagere) JadePersistenceManager.read(simpleAssuranceRenteViagere);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleAssuranceRenteViagereService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleAssuranceRenteViagere)
     */
    @Override
    public SimpleAssuranceRenteViagere update(SimpleAssuranceRenteViagere simpleAssuranceRenteViagere)
            throws JadePersistenceException, AssuranceRenteViagereException {
        if (simpleAssuranceRenteViagere == null) {
            throw new AssuranceRenteViagereException(
                    "Unable to update simpleAssuranceRenteViagere, the model passed is null!");
        }
        if (simpleAssuranceRenteViagere.isNew()) {
            throw new AssuranceRenteViagereException(
                    "Unable to update simpleAssuranceRenteViagere, the model passed is new!");
        }
        SimpleAssuranceRenteViagereChecker.checkForUpdate(simpleAssuranceRenteViagere);
        return (SimpleAssuranceRenteViagere) JadePersistenceManager.update(simpleAssuranceRenteViagere);
    }

}
