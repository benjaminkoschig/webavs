/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.reprise;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.amal.business.models.reprise.ContribuableReprise;
import ch.globaz.amal.business.models.reprise.ContribuableRepriseSearch;
import ch.globaz.amal.business.models.reprise.SimpleContribuableInfoReprise;
import ch.globaz.amal.business.models.reprise.SimpleContribuableInfoRepriseSearch;
import ch.globaz.amal.business.models.reprise.SimpleContribuableReprise;
import ch.globaz.amal.business.models.reprise.SimpleContribuableRepriseSearch;
import ch.globaz.amal.business.models.reprise.SimpleFamilleReprise;
import ch.globaz.amal.business.models.reprise.SimpleFamilleRepriseSearch;
import ch.globaz.amal.business.models.reprise.SimpleInnerMFReprise;
import ch.globaz.amal.business.models.reprise.SimpleInnerMFRepriseSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService;

/**
 * @author DHI
 * 
 */
public class ContribuableRepriseServiceImpl implements ContribuableRepriseService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#count(ch.globaz.amal.business.models
     * .reprise.ContribuableRepriseSearch)
     */
    @Override
    public int count(ContribuableRepriseSearch search) throws Exception, JadePersistenceException {
        if (search == null) {
            throw new Exception("Modele passed is null");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#count(ch.globaz.amal.business.models
     * .reprise.SimpleContribuableInfoRepriseSearch)
     */
    @Override
    public int count(SimpleContribuableInfoRepriseSearch search) throws Exception, JadePersistenceException {
        if (search == null) {
            throw new Exception("Modele passed is null");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#count(ch.globaz.amal.business.models
     * .reprise.SimpleContribuableRepriseSearch)
     */
    @Override
    public int count(SimpleContribuableRepriseSearch search) throws Exception, JadePersistenceException {
        if (search == null) {
            throw new Exception("Modele passed is null");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#count(ch.globaz.amal.business.models
     * .reprise.SimpleFamilleRepriseSearch)
     */
    @Override
    public int count(SimpleFamilleRepriseSearch search) throws Exception, JadePersistenceException {
        if (search == null) {
            throw new Exception("Modele passed is null");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#count(ch.globaz.amal.business.models
     * .reprise.SimpleInnerMFRepriseSearch)
     */
    @Override
    public int count(SimpleInnerMFRepriseSearch search) throws Exception, JadePersistenceException {
        if (search == null) {
            throw new Exception("Modele passed is null");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#create(ch.globaz.amal.business.models
     * .reprise.SimpleInnerMFReprise)
     */
    @Override
    public SimpleInnerMFReprise create(SimpleInnerMFReprise simpleInner) throws JadePersistenceException, Exception {
        if (simpleInner == null) {
            throw new Exception("Modele passed to create a SimpleInnerMFReprise record is null");
        }
        return (SimpleInnerMFReprise) JadePersistenceManager.add(simpleInner);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#deleteSimpleInnerMF(ch.globaz.amal
     * .business.models.reprise.SimpleInnerMFReprise)
     */
    @Override
    public SimpleInnerMFReprise deleteSimpleInnerMF(SimpleInnerMFReprise innerMF) throws JadePersistenceException,
            Exception {
        if (innerMF == null) {
            throw new Exception("Unable to delete the SimpleInnerMFReprise record, the model passed is null");
        }
        // Cas spécial pas de spy... on chinde
        innerMF.setSpy("non");
        return (SimpleInnerMFReprise) JadePersistenceManager.delete(innerMF);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#getNbEnCoursEnregistrements
     */
    @Override
    public String getNbEnCoursEnregistrements() {
        SimpleInnerMFRepriseSearch innerMFSearch = new SimpleInnerMFRepriseSearch();
        innerMFSearch.setForIdInnerMF("-2");
        try {
            innerMFSearch = AmalServiceLocator.getContribuableRepriseService().search(innerMFSearch);
        } catch (Exception ex) {
            return "0";
        }
        if ((innerMFSearch.getSize() < 1)) {
            return "0";
        } else {
            SimpleInnerMFReprise innerMFFound = (SimpleInnerMFReprise) innerMFSearch.getSearchResults()[0];
            return innerMFFound.getIdTiers();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#getNbTotalEnregistrements
     */
    @Override
    public String getNbTotalEnregistrements() {
        SimpleInnerMFRepriseSearch innerMFSearch = new SimpleInnerMFRepriseSearch();
        innerMFSearch.setForIdInnerMF("-1");
        try {
            innerMFSearch = AmalServiceLocator.getContribuableRepriseService().search(innerMFSearch);
        } catch (Exception ex) {
            return "0";
        }
        if ((innerMFSearch.getSize() < 1)) {
            return "0";
        } else {
            SimpleInnerMFReprise innerMFFound = (SimpleInnerMFReprise) innerMFSearch.getSearchResults()[0];
            return innerMFFound.getIdTiers();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#getRepriseStatus
     */
    @Override
    public String getRepriseStatus() {
        SimpleInnerMFRepriseSearch innerMFSearch = new SimpleInnerMFRepriseSearch();
        innerMFSearch.setForIdInnerMF("0");
        try {
            innerMFSearch = AmalServiceLocator.getContribuableRepriseService().search(innerMFSearch);
        } catch (Exception ex) {
            return "1";
        }
        if ((innerMFSearch.getSize() < 1)) {
            return "1";
        } else {
            SimpleInnerMFReprise innerMFFound = (SimpleInnerMFReprise) innerMFSearch.getSearchResults()[0];
            return innerMFFound.getIdTiers();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#readContribuable(java.lang.String)
     */
    @Override
    public ContribuableReprise readContribuable(String idContribuable) throws JadePersistenceException, Exception {
        if (JadeStringUtil.isBlankOrZero(idContribuable)) {
            throw new Exception("Unable to read a Contribuable Reprise record, the id passed is empty");
        }
        ContribuableReprise contribuable = new ContribuableReprise();
        contribuable.setId(idContribuable);
        return (ContribuableReprise) JadePersistenceManager.read(contribuable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#readFamille(java.lang.String)
     */
    @Override
    public SimpleFamilleReprise readFamille(String idFamille) throws JadePersistenceException, Exception {
        if (JadeStringUtil.isBlankOrZero(idFamille)) {
            throw new Exception("Unable to read a SimpleFamilleReprise record, the id passed is empty");
        }
        SimpleFamilleReprise famille = new SimpleFamilleReprise();
        famille.setId(idFamille);
        return (SimpleFamilleReprise) JadePersistenceManager.read(famille);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#readInner(java.lang.String)
     */
    @Override
    public SimpleInnerMFReprise readInner(String idInnerMF) throws JadePersistenceException, Exception {
        if (JadeStringUtil.isEmpty(idInnerMF)) {
            throw new Exception("Unable to read a SimpleInnerMFReprise record, the id passed is empty");
        }
        SimpleInnerMFReprise innerMF = new SimpleInnerMFReprise();
        innerMF.setId(idInnerMF);
        return (SimpleInnerMFReprise) JadePersistenceManager.read(innerMF);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#readSimpleContribuable(java.lang.String
     * )
     */
    @Override
    public SimpleContribuableReprise readSimpleContribuable(String idContribuable) throws JadePersistenceException,
            Exception {
        if (JadeStringUtil.isBlankOrZero(idContribuable)) {
            throw new Exception("Unable to read a SimpleContribuableReprise record, the id passed is empty");
        }
        SimpleContribuableReprise contribuable = new SimpleContribuableReprise();
        contribuable.setId(idContribuable);
        return (SimpleContribuableReprise) JadePersistenceManager.read(contribuable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#readSimpleContribuableInfo(java.lang
     * .String)
     */
    @Override
    public SimpleContribuableInfoReprise readSimpleContribuableInfo(String idContribuable)
            throws JadePersistenceException, Exception {
        if (JadeStringUtil.isBlankOrZero(idContribuable)) {
            throw new Exception("Unable to read a SimpleContribuableInfoReprise record, the id passed is empty");
        }
        SimpleContribuableInfoReprise contribuable = new SimpleContribuableInfoReprise();
        contribuable.setId(idContribuable);
        return (SimpleContribuableInfoReprise) JadePersistenceManager.read(contribuable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#search(ch.globaz.amal.business.models
     * .reprise.ContribuableRepriseSearch)
     */
    @Override
    public ContribuableRepriseSearch search(ContribuableRepriseSearch search) throws Exception,
            JadePersistenceException {
        if (search == null) {
            throw new Exception("Unable to search ContribuableReprise records, the model passed is null");
        }
        return (ContribuableRepriseSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#search(ch.globaz.amal.business.models
     * .reprise.SimpleContribuableInfoRepriseSearch)
     */
    @Override
    public SimpleContribuableInfoRepriseSearch search(SimpleContribuableInfoRepriseSearch search) throws Exception,
            JadePersistenceException {
        if (search == null) {
            throw new Exception("Unable to search SimpleContribuableInfoReprise records, the model passed is null");
        }
        return (SimpleContribuableInfoRepriseSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#search(ch.globaz.amal.business.models
     * .reprise.SimpleContribuableRepriseSearch)
     */
    @Override
    public SimpleContribuableRepriseSearch search(SimpleContribuableRepriseSearch search) throws Exception,
            JadePersistenceException {
        if (search == null) {
            throw new Exception("Unable to search SimpleContribuableReprise records, the model passed is null");
        }
        return (SimpleContribuableRepriseSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#search(ch.globaz.amal.business.models
     * .reprise.SimpleFamilleRepriseSearch)
     */
    @Override
    public SimpleFamilleRepriseSearch search(SimpleFamilleRepriseSearch search) throws Exception,
            JadePersistenceException {
        if (search == null) {
            throw new Exception("Unable to search SimpleFamilleReprise records, the model passed is null");
        }
        return (SimpleFamilleRepriseSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#search(ch.globaz.amal.business.models
     * .reprise.SimpleInnerMFRepriseSearch)
     */
    @Override
    public SimpleInnerMFRepriseSearch search(SimpleInnerMFRepriseSearch search) throws Exception,
            JadePersistenceException {
        if (search == null) {
            throw new Exception("Unable to search SimpleInnerMFReprise records, the model passed is null");
        }
        return (SimpleInnerMFRepriseSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#updateSimpleContribuable(ch.globaz
     * .amal.business.models.reprise.SimpleContribuableReprise)
     */
    @Override
    public SimpleContribuableReprise updateSimpleContribuable(SimpleContribuableReprise contribuable)
            throws JadePersistenceException, Exception {
        if (contribuable == null) {
            throw new Exception("Unable to update the SimpleContribuableReprise record, the model passed is null");
        }
        return (SimpleContribuableReprise) JadePersistenceManager.update(contribuable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#updateSimpleFamille(ch.globaz.amal
     * .business.models.reprise.SimpleFamilleReprise)
     */
    @Override
    public SimpleFamilleReprise updateSimpleFamille(SimpleFamilleReprise famille) throws JadePersistenceException,
            Exception {
        if (famille == null) {
            throw new Exception("Unable to update the SimpleFamilleReprise record, the model passed is null");
        }
        return (SimpleFamilleReprise) JadePersistenceManager.update(famille);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.reprise.ContribuableRepriseService#updateSimpleInnerMF(ch.globaz.amal
     * .business.models.reprise.SimpleInnerMFReprise)
     */
    @Override
    public SimpleInnerMFReprise updateSimpleInnerMF(SimpleInnerMFReprise innerMF) throws JadePersistenceException,
            Exception {
        if (innerMF == null) {
            throw new Exception("Unable to update the SimpleInnerMFReprise record, the model passed is null");
        }
        // Cas spécial pas de spy... on chinde
        innerMF.setSpy("oui");
        return (SimpleInnerMFReprise) JadePersistenceManager.update(innerMF);
    }

}
