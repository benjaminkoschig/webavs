package ch.globaz.prestation.business.services.models.demande;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.prestation.business.exceptions.models.DemandePrestationException;
import ch.globaz.prestation.business.models.demande.DemandePrestation;
import ch.globaz.prestation.business.models.demande.DemandePrestationSearch;

public interface DemandePrestationService extends JadeApplicationService {

    public int count(DemandePrestationSearch dpSearch) throws DemandePrestationException, JadePersistenceException;

    public DemandePrestation create(DemandePrestation demandePrestation) throws JadePersistenceException,
            DemandePrestationException;

    /**
     * @param demandePrestation
     * @return
     * @throws JadePersistenceException
     * @throws DemandePrestationException
     */
    public DemandePrestation createOrRead(DemandePrestation demandePrestation) throws JadePersistenceException,
            DemandePrestationException;

    // Etc.
    public DemandePrestation read(String idDemandePrestation) throws JadePersistenceException,
            DemandePrestationException;

    /**
     * @param dossierSearch
     * @return
     * @throws JadePersistenceException
     * @throws DossierException
     */
    public DemandePrestationSearch search(DemandePrestationSearch demandePrestationSearch)
            throws JadePersistenceException, DemandePrestationException;

}
