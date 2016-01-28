package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeIndependanteException;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependanteEtendu;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependanteEtenduSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.RevenuActiviteLucrativeIndependanteEtenduService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class RevenuActiviteLucrativeIndependanteEtenduServiceImpl extends PegasusAbstractServiceImpl implements
        RevenuActiviteLucrativeIndependanteEtenduService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuActiviteLucrativeIndependanteService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .RevenuActiviteLucrativeIndependanteSearch)
     */
    @Override
    public int count(RevenuActiviteLucrativeIndependanteEtenduSearch search)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException {
        if (search == null) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to count RevenuActiviteLucrativeIndependanteEtendu, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses.
     * RevenuActiviteLucrativeIndependanteService#read(java.lang.String)
     */
    @Override
    public RevenuActiviteLucrativeIndependanteEtendu read(String idRevenuActiviteLucrativeIndependante)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException {
        if (JadeStringUtil.isEmpty(idRevenuActiviteLucrativeIndependante)) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to read RevenuActiviteLucrativeIndependanteEtendu, the id passed is null!");
        }
        RevenuActiviteLucrativeIndependanteEtendu RevenuActiviteLucrativeIndependante = new RevenuActiviteLucrativeIndependanteEtendu();
        RevenuActiviteLucrativeIndependante.setId(idRevenuActiviteLucrativeIndependante);
        return (RevenuActiviteLucrativeIndependanteEtendu) JadePersistenceManager
                .read(RevenuActiviteLucrativeIndependante);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuActiviteLucrativeIndependanteService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .RevenuActiviteLucrativeIndependanteSearch)
     */
    @Override
    public RevenuActiviteLucrativeIndependanteEtenduSearch search(
            RevenuActiviteLucrativeIndependanteEtenduSearch revenuActiviteLucrativeIndependanteSearch)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException {
        if (revenuActiviteLucrativeIndependanteSearch == null) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to search RevenuActiviteLucrativeIndependanteEtendu, the search model passed is null!");
        }
        return (RevenuActiviteLucrativeIndependanteEtenduSearch) JadePersistenceManager
                .search(revenuActiviteLucrativeIndependanteSearch);
    }

}
