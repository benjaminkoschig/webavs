/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.annoncesedex;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedexSearch;
import ch.globaz.amal.business.services.models.annoncesedex.SimpleAnnonceSedexService;
import ch.globaz.amal.businessimpl.checkers.annoncesedex.SimpleAnnonceSedexChecker;

/**
 * @author dhi
 * 
 */
public class SimpleAnnonceSedexServiceImpl implements SimpleAnnonceSedexService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.annoncesedex.SimpleAnnonceSedexservice#create(ch.globaz.amal.business
     * .models.annoncesedex.SimpleAnnonceSedex)
     */
    @Override
    public SimpleAnnonceSedex create(SimpleAnnonceSedex simpleAnnonceSedex) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, AnnonceSedexException, DetailFamilleException {
        if (simpleAnnonceSedex == null) {
            throw new AnnonceSedexException("Unable to create simpleAnnonceSedex in DB, the model passed is null!");
        }
        SimpleAnnonceSedexChecker.checkForCreate(simpleAnnonceSedex);
        return (SimpleAnnonceSedex) JadePersistenceManager.add(simpleAnnonceSedex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.annoncesedex.SimpleAnnonceSedexservice#delete(ch.globaz.amal.business
     * .models.annoncesedex.SimpleAnnonceSedex)
     */
    @Override
    public SimpleAnnonceSedex delete(SimpleAnnonceSedex simpleAnnonceSedex) throws JadePersistenceException,
            AnnonceSedexException, JadeApplicationServiceNotAvailableException {
        if (simpleAnnonceSedex == null) {
            throw new AnnonceSedexException("Unable to delete simpleAnnonceSedex into DB, the model passed is null!");
        }
        SimpleAnnonceSedexChecker.checkForDelete(simpleAnnonceSedex);
        return (SimpleAnnonceSedex) JadePersistenceManager.delete(simpleAnnonceSedex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.annoncesedex.SimpleAnnonceSedexservice#read(java.lang.String)
     */
    @Override
    public SimpleAnnonceSedex read(String idAnnonceSedex) throws JadePersistenceException, AnnonceSedexException {
        if (JadeStringUtil.isEmpty(idAnnonceSedex)) {
            throw new AnnonceSedexException("Unable to read the annonce sedex from db, id passed is empty");
        }
        SimpleAnnonceSedex simpleAnnonceSedex = new SimpleAnnonceSedex();
        simpleAnnonceSedex.setId(idAnnonceSedex);
        return (SimpleAnnonceSedex) JadePersistenceManager.read(simpleAnnonceSedex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.annoncesedex.SimpleAnnonceSedexservice#search(ch.globaz.amal.business
     * .models.annoncesedex.SimpleAnnonceSedexSearch)
     */
    @Override
    public SimpleAnnonceSedexSearch search(SimpleAnnonceSedexSearch search) throws JadePersistenceException,
            AnnonceSedexException {
        if (search == null) {
            throw new AnnonceSedexException("Unable to search a simple annonce sedex , the search model passed is null");
        }
        return (SimpleAnnonceSedexSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.annoncesedex.SimpleAnnonceSedexservice#update(ch.globaz.amal.business
     * .models.annoncesedex.SimpleAnnonceSedex)
     */
    @Override
    public SimpleAnnonceSedex update(SimpleAnnonceSedex simpleAnnonceSedex) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, AnnonceSedexException, DetailFamilleException {
        if (simpleAnnonceSedex == null) {
            throw new AnnonceSedexException("Unable to update the simple annonce sedex, the model passed is null");
        }
        SimpleAnnonceSedexChecker.checkForUpdate(simpleAnnonceSedex);
        return (SimpleAnnonceSedex) JadePersistenceManager.update(simpleAnnonceSedex);
    }

}
