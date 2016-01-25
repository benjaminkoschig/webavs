/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.annoncesedex;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedexSearch;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.annoncesedex.ComplexAnnonceSedexService;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author dhi
 * 
 */
public class ComplexAnnonceSedexServiceImpl implements ComplexAnnonceSedexService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.annoncesedex.ComplexAnnonceSedexService#count(ch.globaz.amal.business
     * .models.annoncesedex.ComplexAnnonceSedexSearch)
     */
    @Override
    public int count(ComplexAnnonceSedexSearch search) throws AnnonceSedexException, JadePersistenceException {
        if (search == null) {
            throw new AnnonceSedexException("Unable to count annonces, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.annoncesedex.ComplexAnnonceSedexService#read(java.lang.String)
     */
    @Override
    public ComplexAnnonceSedex read(String idAnnonce) throws JadePersistenceException, AnnonceSedexException,
            ContribuableException, FamilleException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException {
        if (JadeStringUtil.isEmpty(idAnnonce)) {
            throw new AnnonceSedexException("Unable to read the annonce sedex from db, id passed is empty");
        }
        ComplexAnnonceSedex toReturn = new ComplexAnnonceSedex();
        // Read the simple annonce
        SimpleAnnonceSedex simpleAnnonceSedex = new SimpleAnnonceSedex();
        simpleAnnonceSedex.setId(idAnnonce);
        simpleAnnonceSedex = (SimpleAnnonceSedex) JadePersistenceManager.read(simpleAnnonceSedex);
        toReturn.setSimpleAnnonceSedex(simpleAnnonceSedex);
        // Read the idDetailFamille
        SimpleDetailFamille detailFamille = new SimpleDetailFamille();
        if (!JadeStringUtil.isEmpty(simpleAnnonceSedex.getIdDetailFamille())) {
            detailFamille.setId(simpleAnnonceSedex.getIdDetailFamille());
            detailFamille = (SimpleDetailFamille) JadePersistenceManager.read(detailFamille);
        }
        toReturn.setSimpleDetailFamille(detailFamille);
        // Read simple famille
        SimpleFamille currentFamille = new SimpleFamille();
        if (JadeStringUtil.isEmpty(detailFamille.getIdFamille())) {
            currentFamille.setId(detailFamille.getIdFamille());
            currentFamille = (SimpleFamille) JadePersistenceManager.read(currentFamille);
        }
        toReturn.setSimpleFamille(currentFamille);
        // Read contribuable
        Contribuable currentContribuable = new Contribuable();
        if (!JadeStringUtil.isEmpty(simpleAnnonceSedex.getIdContribuable())) {
            currentContribuable = AmalServiceLocator.getContribuableService().read(
                    simpleAnnonceSedex.getIdContribuable());
        }
        toReturn.setContribuable(currentContribuable);
        // read administration
        AdministrationComplexModel caisse = new AdministrationComplexModel();
        if (!JadeStringUtil.isEmpty(simpleAnnonceSedex.getIdTiersCM())) {
            caisse = TIBusinessServiceLocator.getAdministrationService().read(simpleAnnonceSedex.getIdTiersCM());
        }
        toReturn.setCaisseMaladie(caisse);

        return toReturn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.annoncesedex.ComplexAnnonceSedexService#search(ch.globaz.amal.business
     * .models.contribuable.ContribuableSearch)
     */
    @Override
    public ComplexAnnonceSedexSearch search(ComplexAnnonceSedexSearch search) throws JadePersistenceException,
            AnnonceSedexException {
        if (search == null) {
            throw new AnnonceSedexException("Unable to search a simple annonce sedex , the search model passed is null");
        }
        return (ComplexAnnonceSedexSearch) JadePersistenceManager.search(search);
    }

}
