package ch.globaz.amal.businessimpl.services.models.annoncesedexco;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.annoncesedexco.AnnonceSedexCOException;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCOSearch;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.business.models.contribuable.SimpleContribuable;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.services.models.annoncesedexco.ComplexAnnonceSedexCOService;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class ComplexAnnonceSedexCOServiceImpl implements ComplexAnnonceSedexCOService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.annoncesedexco.ComplexAnnonceSedexCOService#count(ch.globaz.amal.business
     * .models.annoncesedex.ComplexAnnonceSedexSearch)
     */
    @Override
    public int count(ComplexAnnonceSedexCOSearch search) throws AnnonceSedexCOException, JadePersistenceException {
        if (search == null) {
            throw new AnnonceSedexCOException("Unable to count annonces, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.annoncesedexco.ComplexAnnonceSedexCOService#read(java.lang.String)
     */
    @Override
    public ComplexAnnonceSedexCO read(String idAnnonce) throws JadePersistenceException, AnnonceSedexCOException,
            ContribuableException, FamilleException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException {
        if (JadeStringUtil.isEmpty(idAnnonce)) {
            throw new AnnonceSedexCOException("Unable to read the annonce sedex from db, id passed is empty");
        }
        ComplexAnnonceSedexCO toReturn = new ComplexAnnonceSedexCO();
        // Read the simple annonce
        SimpleAnnonceSedexCO simpleAnnonceSedexCO = new SimpleAnnonceSedexCO();
        simpleAnnonceSedexCO.setId(idAnnonce);
        simpleAnnonceSedexCO = (SimpleAnnonceSedexCO) JadePersistenceManager.read(simpleAnnonceSedexCO);
        toReturn.setSimpleAnnonceSedexCO(simpleAnnonceSedexCO);
        // Read simple famille
        SimpleFamille currentFamille = new SimpleFamille();
        if (JadeStringUtil.isEmpty(simpleAnnonceSedexCO.getIdFamille())) {
            currentFamille.setId(simpleAnnonceSedexCO.getIdFamille());
            currentFamille = (SimpleFamille) JadePersistenceManager.read(currentFamille);
        }
        toReturn.setSimpleFamille(currentFamille);
        // Read contribuable
        SimpleContribuable currentContribuable = new SimpleContribuable();
        if (!JadeStringUtil.isEmpty(simpleAnnonceSedexCO.getIdContribuable())) {
            currentContribuable = (SimpleContribuable) JadePersistenceManager.read(currentContribuable);
        }
        toReturn.setSimpleContribuable(currentContribuable);
        // read administration
        AdministrationComplexModel caisse = new AdministrationComplexModel();
        if (!JadeStringUtil.isEmpty(simpleAnnonceSedexCO.getIdTiersCM())) {
            caisse = TIBusinessServiceLocator.getAdministrationService().read(simpleAnnonceSedexCO.getIdTiersCM());
        }
        toReturn.setCaisseMaladie(caisse);

        return toReturn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.annoncesedexco.ComplexAnnonceSedexCOService#search(ch.globaz.amal.business
     * .models.contribuable.ContribuableSearch)
     */
    @Override
    public ComplexAnnonceSedexCOSearch search(ComplexAnnonceSedexCOSearch search) throws JadePersistenceException,
            AnnonceSedexCOException {
        if (search == null) {
            throw new AnnonceSedexCOException(
                    "Unable to search a complex annonce sedex co , the search model passed is null");
        }
        return (ComplexAnnonceSedexCOSearch) JadePersistenceManager.search(search);
    }

}
