package ch.globaz.amal.businessimpl.services.models.annoncesedexco;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.annoncesedexco.AnnonceSedexCOException;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO2;
import ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO2Search;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.business.services.models.annoncesedexco.ComplexAnnonceSedexCO2Service;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class ComplexAnnonceSedexCO2ServiceImpl implements ComplexAnnonceSedexCO2Service {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.annoncesedexco.ComplexAnnonceSedexCOService#count(ch.globaz.amal.business
     * .models.annoncesedex.ComplexAnnonceSedexSearch)
     */
    @Override
    public int count(ComplexAnnonceSedexCO2Search search) throws AnnonceSedexCOException, JadePersistenceException {
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
    public ComplexAnnonceSedexCO2 read(String idAnnonce) throws JadePersistenceException, AnnonceSedexCOException,
            ContribuableException, FamilleException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException {
        if (JadeStringUtil.isEmpty(idAnnonce)) {
            throw new AnnonceSedexCOException("Unable to read the annonce sedex from db, id passed is empty");
        }
        ComplexAnnonceSedexCO2 toReturn = new ComplexAnnonceSedexCO2();
        // Read the simple annonce
        SimpleAnnonceSedexCO simpleAnnonceSedexCO = new SimpleAnnonceSedexCO();
        simpleAnnonceSedexCO.setId(idAnnonce);
        simpleAnnonceSedexCO = (SimpleAnnonceSedexCO) JadePersistenceManager.read(simpleAnnonceSedexCO);
        toReturn.setSimpleAnnonceSedexCO(simpleAnnonceSedexCO);

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
    public ComplexAnnonceSedexCO2Search search(ComplexAnnonceSedexCO2Search search) throws JadePersistenceException,
            AnnonceSedexCOException {
        if (search == null) {
            throw new AnnonceSedexCOException(
                    "Unable to search a complex annonce sedex co , the search model passed is null");
        }
        return (ComplexAnnonceSedexCO2Search) JadePersistenceManager.search(search);
    }

}
