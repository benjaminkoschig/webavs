/**
 * 
 */
package ch.globaz.amal.business.services.models.annoncesedex;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedexSearch;

/**
 * @author dhi
 * 
 */
public interface ComplexAnnonceSedexService extends JadeApplicationService {

    /**
     * 
     * @param search
     * @return
     * @throws AnnonceSedexException
     * @throws JadePersistenceException
     */
    public int count(ComplexAnnonceSedexSearch search) throws AnnonceSedexException, JadePersistenceException;

    /**
     * 
     * @param idAnnonce
     * @return
     * @throws JadePersistenceException
     * @throws AnnonceSedexException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ComplexAnnonceSedex read(String idAnnonce) throws JadePersistenceException, AnnonceSedexException,
            ContribuableException, FamilleException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException;

    /**
     * 
     * @param search
     * @return
     * @throws JadePersistenceException
     * @throws AnnonceSedexException
     */
    public ComplexAnnonceSedexSearch search(ComplexAnnonceSedexSearch search) throws JadePersistenceException,
            AnnonceSedexException;

}
