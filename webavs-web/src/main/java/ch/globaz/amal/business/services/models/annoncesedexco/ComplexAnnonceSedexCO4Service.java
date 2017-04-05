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
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO4;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO4Search;

/**
 * @author lbe
 * 
 */
public interface ComplexAnnonceSedexCO4Service extends JadeApplicationService {

    /**
     * 
     * @param search
     * @return
     * @throws AnnonceSedexException
     * @throws JadePersistenceException
     */
    public int count(ComplexAnnonceSedexCO4Search search) throws AnnonceSedexCOException, JadePersistenceException;

    /**
     * 
     * @param idAnnonce
     * @return
     * @throws JadePersistenceException
     * @throws AnnonceSedexException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ComplexAnnonceSedexCO4 read(String idAnnonce) throws JadePersistenceException, AnnonceSedexCOException,
            ContribuableException, FamilleException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException;

    /**
     * 
     * @param search
     * @return
     * @throws JadePersistenceException
     * @throws AnnonceSedexException
     */
    public ComplexAnnonceSedexCO4Search search(ComplexAnnonceSedexCO4Search search) throws JadePersistenceException,
            AnnonceSedexCOException;

}
