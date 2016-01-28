package ch.globaz.pegasus.businessimpl.services.models.dossier;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.api.IPRDemande;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.models.dossier.Dossier;
import ch.globaz.pegasus.business.models.dossier.DossierRCListSearch;
import ch.globaz.pegasus.business.models.dossier.DossierSearch;
import ch.globaz.pegasus.business.services.models.dossier.DossierService;
import ch.globaz.pegasus.businessimpl.checkers.dossier.DossierChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.prestation.business.exceptions.models.DemandePrestationException;
import ch.globaz.prestation.business.models.demande.SimpleDemandePrestation;
import ch.globaz.prestation.business.services.PrestationCommonServiceLocator;

public class DossierServiceImpl extends PegasusAbstractServiceImpl implements DossierService {

    @Override
    public int count(DossierSearch search) throws DossierException, JadePersistenceException {
        if (search == null) {
            throw new DossierException("Unable to count dossiers, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public Dossier create(Dossier dossier) throws DossierException, JadePersistenceException,
            DemandePrestationException {
        if (dossier == null) {
            throw new DossierException("Unable to create dossier, the given model is null!");
        }

        try {
            SimpleDemandePrestation simpleDm = dossier.getDemandePrestation().getDemandePrestation();
            simpleDm.setIdTiers(dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers());
            simpleDm.setEtat(IPRDemande.CS_ETAT_OUVERT);
            simpleDm.setTypeDemande(IPRDemande.CS_TYPE_PC);

            dossier.setDemandePrestation(PrestationCommonServiceLocator.getDemandePrestationService().createOrRead(
                    dossier.getDemandePrestation()));
            dossier.getDossier().setIdDemandePrestation(dossier.getDemandePrestation().getId());
            DossierChecker.checkForCreate(dossier);

            PegasusImplServiceLocator.getSimpleDossierService().create(dossier.getDossier());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DossierException("Service not available - " + e.getMessage());
        }

        return dossier;
    }

    @Override
    public Dossier delete(Dossier dossier) throws DossierException, JadePersistenceException {
        if (dossier == null) {
            throw new DossierException("Unable to delete dossier, the given model is null!");
        }
        try {
            dossier.setDossier(PegasusImplServiceLocator.getSimpleDossierService().delete(dossier.getDossier()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DossierException("Service not available - " + e.getMessage());
        }

        return dossier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.dossier.DossierService#read (java.lang.String)
     */
    @Override
    public Dossier read(String idDossier) throws JadePersistenceException, DossierException {
        if (JadeStringUtil.isEmpty(idDossier)) {
            throw new DossierException("Unable to read dossier, the id passed is null!");
        }
        Dossier dossier = new Dossier();
        dossier.setId(idDossier);
        return (Dossier) JadePersistenceManager.read(dossier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.dossier.DossierService#search
     * (ch.globaz.pegasus.business.models.dossier.DossierSearch)
     */
    @Override
    public DossierSearch search(DossierSearch dossierSearch) throws JadePersistenceException, DossierException {
        if (dossierSearch == null) {
            throw new DossierException("Unable to search dossier, the search model passed is null!");
        }
        return (DossierSearch) JadePersistenceManager.search(dossierSearch);
    }

    @Override
    public DossierRCListSearch searchRCList(DossierRCListSearch dossierSearch) throws JadePersistenceException,
            DossierException {
        if (dossierSearch == null) {
            throw new DossierException("Unable to search DossierRCListSearch, the search model passed is null!");
        }
        return (DossierRCListSearch) JadePersistenceManager.search(dossierSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.dossier.DossierService#update
     * (ch.globaz.pegasus.business.models.dossier.Dossier)
     */
    @Override
    public Dossier update(Dossier dossier) throws JadePersistenceException, DossierException {
        if (dossier == null) {
            throw new DossierException("Unable to update dossier, the given model is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleDossierService().update(dossier.getDossier());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DossierException("Service not available - " + e.getMessage());
            // e.printStackTrace();
        }

        return dossier;
    }
}
