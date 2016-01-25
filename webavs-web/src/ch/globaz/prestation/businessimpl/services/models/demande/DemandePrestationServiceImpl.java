package ch.globaz.prestation.businessimpl.services.models.demande;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.prestation.db.demandes.PRDemande;
import ch.globaz.prestation.business.exceptions.models.DemandePrestationException;
import ch.globaz.prestation.business.models.demande.DemandePrestation;
import ch.globaz.prestation.business.models.demande.DemandePrestationSearch;
import ch.globaz.prestation.business.services.models.demande.DemandePrestationService;
import ch.globaz.prestation.businessimpl.checkers.demande.DemandePrestationChecker;
import ch.globaz.prestation.businessimpl.services.PrestationCommonAbstractServiceImpl;

/**
 * @author ECO
 * 
 */
/**
 * @author ECO
 * 
 */
/**
 * @author ECO
 * 
 */
/**
 * @author ECO
 * 
 */
/**
 * @author ECO
 * 
 */
public class DemandePrestationServiceImpl extends PrestationCommonAbstractServiceImpl implements
        DemandePrestationService {

    @Override
    public int count(DemandePrestationSearch search) throws DemandePrestationException, JadePersistenceException {
        if (search == null) {
            throw new DemandePrestationException("Unable to count demandePrestations, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.prestation.services.demande.DemandePrestationService#create
     * (ch.globaz.prestation.business.models.demande.DemandePrestation)
     */
    @Override
    public DemandePrestation create(DemandePrestation demandePrestation) throws JadePersistenceException,
            DemandePrestationException {
        if (demandePrestation == null) {
            throw new DemandePrestationException("Unable to create demande prestation. the given model is null.");
        }

        DemandePrestationChecker.checkForCreate(demandePrestation.getDemandePrestation());

        PRDemande demande = parse(demandePrestation);

        try {
            demande.add();
        } catch (Exception e) {
            throw new DemandePrestationException("Creation of DemandePrestation failed: " + e.getMessage());
        }

        demandePrestation.getDemandePrestation().setIdDemande(demande.getIdDemande());

        return demandePrestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.prestation.business.services.models.demande. DemandePrestationService
     * #createOrRead(ch.globaz.prestation.business.models .demande.DemandePrestation)
     */
    @Override
    public DemandePrestation createOrRead(DemandePrestation demandePrestation) throws JadePersistenceException,
            DemandePrestationException {
        // Check param not null!
        if (!(JadeStringUtil.isEmpty(demandePrestation.getDemandePrestation().getIdTiers())
                || JadeStringUtil.isEmpty(demandePrestation.getDemandePrestation().getTypeDemande()) || JadeStringUtil
                    .isEmpty(demandePrestation.getDemandePrestation().getEtat()))) {

            DemandePrestationSearch dpSearch = new DemandePrestationSearch();
            dpSearch.setForIdTiers(demandePrestation.getDemandePrestation().getIdTiers());
            dpSearch.setForTypeDemande(demandePrestation.getDemandePrestation().getTypeDemande());
            dpSearch = search(dpSearch);

            if (dpSearch.getSize() == 1) {
                demandePrestation = (DemandePrestation) dpSearch.getSearchResults()[0];
            } else if (dpSearch.getSize() == 0) {
                // créer une nouvelle demande de prestation
                demandePrestation = create(demandePrestation);
            } else {
                // erreur
                throw new DemandePrestationException(
                        "Multiple results of DemandePrestation were found where one was expected");
            }

        }

        return demandePrestation;
    }

    private PRDemande parse(DemandePrestation demandePrestation) {
        PRDemande demande = new PRDemande();
        demande.setEtat(demandePrestation.getDemandePrestation().getEtat());
        demande.setIdTiers(demandePrestation.getDemandePrestation().getIdTiers());
        demande.setTypeDemande(demandePrestation.getDemandePrestation().getTypeDemande());

        return demande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.prestation.services.demande.DemandePrestationService#read(java .lang.String)
     */
    @Override
    public DemandePrestation read(String idDemandePrestation) throws JadePersistenceException,
            DemandePrestationException {
        if (JadeStringUtil.isEmpty(idDemandePrestation)) {
            throw new DemandePrestationException("Unable to read demande prestation, the id passed is null!");
        }
        DemandePrestation demandePrest = new DemandePrestation();
        demandePrest.setId(idDemandePrestation);
        return (DemandePrestation) JadePersistenceManager.read(demandePrest);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.prestation.services.demande.DemandePrestationService#search
     * (ch.globaz.prestation.business.models.demande.DemandePrestationSearch)
     */
    @Override
    public DemandePrestationSearch search(DemandePrestationSearch demandePrestationSearch)
            throws JadePersistenceException, DemandePrestationException {
        if (demandePrestationSearch == null) {
            throw new DemandePrestationException(
                    "Unable to search demande prestation, the search model passed is null!");
        }
        return (DemandePrestationSearch) JadePersistenceManager.search(demandePrestationSearch);
    }

}
