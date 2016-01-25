/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.annonce;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annonce.AnnoncesCaisse;
import ch.globaz.amal.business.models.annonce.AnnoncesCaisseSearch;
import ch.globaz.amal.business.models.annonce.SimpleAnnonce;
import ch.globaz.amal.business.models.annonce.SimpleAnnonceSearch;
import ch.globaz.amal.business.services.models.annonce.SimpleAnnonceService;
import ch.globaz.amal.businessimpl.checkers.annonce.SimpleAnnonceChecker;

/**
 * @author DHI
 * 
 */
public class SimpleAnnonceServiceImpl implements SimpleAnnonceService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.annonce.SimpleAnnonceService#create(ch.globaz.amal.business.models.annonce
     * .SimpleAnnonce)
     */
    @Override
    public SimpleAnnonce create(SimpleAnnonce simpleAnnonce) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, AnnonceException, DetailFamilleException {
        if (simpleAnnonce == null) {
            throw new AnnonceException("Unable to create simpleAnnonce, the model passed is null!");
        }
        SimpleAnnonceChecker.checkForCreate(simpleAnnonce);
        return (SimpleAnnonce) JadePersistenceManager.add(simpleAnnonce);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.annonce.SimpleAnnonceService#delete(ch.globaz.amal.business.models.annonce
     * .SimpleAnnonce)
     */
    @Override
    public SimpleAnnonce delete(SimpleAnnonce simpleAnnonce) throws JadePersistenceException, AnnonceException,
            DetailFamilleException, JadeApplicationServiceNotAvailableException {
        if (simpleAnnonce == null) {
            throw new AnnonceException("Unable to delete simpleAnnonce, the model passed is null!");
        }
        SimpleAnnonceChecker.checkForDelete(simpleAnnonce);
        return (SimpleAnnonce) JadePersistenceManager.delete(simpleAnnonce);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.annonce.SimpleAnnonceService#read(java.lang.String)
     */
    @Override
    public SimpleAnnonce read(String idAnnonce) throws JadePersistenceException, AnnonceException {
        if (JadeStringUtil.isEmpty(idAnnonce)) {
            throw new AnnonceException("Unable to read the annonce, id passed is empty");
        }
        SimpleAnnonce simpleAnnonce = new SimpleAnnonce();
        simpleAnnonce.setId(idAnnonce);
        return (SimpleAnnonce) JadePersistenceManager.read(simpleAnnonce);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.annonce.SimpleAnnonceService#readAnnonce(java.lang.String)
     */
    @Override
    public AnnoncesCaisse readAnnonce(String idAnnonce) throws JadePersistenceException, AnnonceException {
        if (JadeStringUtil.isEmpty(idAnnonce)) {
            throw new AnnonceException("Unable to read the annonce, id passed is empty");
        }
        AnnoncesCaisse annonce = new AnnoncesCaisse();
        annonce.setId(idAnnonce);
        return (AnnoncesCaisse) JadePersistenceManager.read(annonce);
    }

    @Override
    public AnnoncesCaisseSearch search(AnnoncesCaisseSearch search) throws JadePersistenceException, AnnonceException {
        if (search == null) {
            throw new AnnonceException("Unable to search a simple annonce, the search model passed is null");
        }
        return (AnnoncesCaisseSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.annonce.SimpleAnnonceService#search(ch.globaz.amal.business.models.annonce
     * .SimpleAnnonceSearch)
     */
    @Override
    public SimpleAnnonceSearch search(SimpleAnnonceSearch search) throws JadePersistenceException, AnnonceException {
        if (search == null) {
            throw new AnnonceException("Unable to search a simple annonce, the search model passed is null");
        }
        return (SimpleAnnonceSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.annonce.SimpleAnnonceService#update(ch.globaz.amal.business.models.annonce
     * .SimpleAnnonce)
     */
    @Override
    public SimpleAnnonce update(SimpleAnnonce simpleAnnonce) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, AnnonceException, DetailFamilleException {
        if (simpleAnnonce == null) {
            throw new AnnonceException("Unable to update the simple annonce, the model passed is null");
        }
        SimpleAnnonceChecker.checkForUpdate(simpleAnnonce);
        return (SimpleAnnonce) JadePersistenceManager.update(simpleAnnonce);
    }

}
