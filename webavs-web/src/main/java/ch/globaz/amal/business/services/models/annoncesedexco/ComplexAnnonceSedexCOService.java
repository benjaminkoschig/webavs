/**
 * 
 */
package ch.globaz.amal.business.services.models.annoncesedexco;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.exceptions.models.annoncesedexco.AnnonceSedexCOException;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCOSearch;

/**
 * @author cbu
 * 
 */
public interface ComplexAnnonceSedexCOService extends JadeApplicationService {

    /**
     * 
     * @param search
     * @return
     * @throws AnnonceSedexException
     * @throws JadePersistenceException
     */
    public int count(ComplexAnnonceSedexCOSearch search) throws AnnonceSedexCOException, JadePersistenceException;

    /**
     * 
     * @param idAnnonce
     * @return
     * @throws JadePersistenceException
     * @throws AnnonceSedexException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ComplexAnnonceSedexCO read(String idAnnonce) throws JadePersistenceException, AnnonceSedexCOException,
            ContribuableException, FamilleException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException;

    /**
     * 
     * @param search
     * @return
     * @throws JadePersistenceException
     * @throws AnnonceSedexException
     */
    public ComplexAnnonceSedexCOSearch search(ComplexAnnonceSedexCOSearch search) throws JadePersistenceException,
            AnnonceSedexCOException;

}
