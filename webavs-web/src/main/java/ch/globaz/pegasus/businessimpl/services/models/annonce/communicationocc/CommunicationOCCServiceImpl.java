/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.annonce.communicationocc;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.annonce.CommunicationOCC;
import ch.globaz.pegasus.business.models.annonce.CommunicationOCCSearch;
import ch.globaz.pegasus.business.services.models.annonce.communicationocc.CommunicationOCCService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author eco
 * 
 */
public class CommunicationOCCServiceImpl extends PegasusAbstractServiceImpl implements CommunicationOCCService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.lot.CommunicationOCCService#count(ch.globaz.pegasus.business.models
     * .lot.CommunicationOCCSearch)
     */
    @Override
    public int count(CommunicationOCCSearch search) throws PrestationException, JadePersistenceException {
        if (search == null) {
            throw new PrestationException("Unable to count prestation, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.lot.CommunicationOCCService#read(java.lang.String)
     */
    @Override
    public CommunicationOCC read(String idCommunicationOCC) throws JadePersistenceException, PrestationException {
        if (JadeStringUtil.isEmpty(idCommunicationOCC)) {
            throw new PrestationException("Unable to read communication OCC, the id passed is null!");
        }
        CommunicationOCC communicationOCC = new CommunicationOCC();
        communicationOCC.setId(idCommunicationOCC);
        return (CommunicationOCC) JadePersistenceManager.read(communicationOCC);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.lot.CommunicationOCCService#search(ch.globaz.pegasus.business.models
     * .lot.CommunicationOCCSearch)
     */
    @Override
    public CommunicationOCCSearch search(CommunicationOCCSearch search) throws JadePersistenceException,
            PrestationException {
        if (search == null) {
            throw new PrestationException("Unable to search communicationOCC, the search model passed is null!");
        }
        return (CommunicationOCCSearch) JadePersistenceManager.search(search);
    }

}
