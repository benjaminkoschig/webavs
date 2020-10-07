package ch.globaz.pegasus.businessimpl.services.models.creancier;

import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.creancier.*;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordeeSearch;
import ch.globaz.pegasus.business.services.models.creancier.SimpleCreancierHystoriqueService;
import ch.globaz.pegasus.businessimpl.checkers.creancier.SimpleCreanceAccordeeChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class SimpleCreancierHystoriqueServiceImpl implements SimpleCreancierHystoriqueService {

    @Override
    public int count(SimpleCreancierHystoriqueSearch search) throws CreancierException, JadePersistenceException {
        if (search == null) {
            throw new CreancierException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleCreancierHystorique create(SimpleCreancier simpleCreancier, SimpleCreanceAccordee simpleCreanceAccordee) throws CreancierException,
            JadePersistenceException {
        if (simpleCreancier == null ||simpleCreanceAccordee  == null  ) {
            throw new CreancierException("Unable to create simpleCreanceAccordeeHyst, the model passed is null!");
        }
        /*
         * MAP Créancier
         */
        SimpleCreancierHystorique simpleCreancierHystorique = new SimpleCreancierHystorique();
        simpleCreancierHystorique.setCsEtat(simpleCreancier.getCsEtat());
        simpleCreancierHystorique.setCsTypeCreance(simpleCreancier.getCsTypeCreance());
        simpleCreancierHystorique.setIdAffilieAdressePaiment(simpleCreancier.getIdAffilieAdressePaiment());
        simpleCreancierHystorique.setIdCreancier(simpleCreancier.getIdCreancier());
        simpleCreancierHystorique.setIdDemande(simpleCreancier.getIdDemande());
        simpleCreancierHystorique.setIdDomaineApplicatif(simpleCreancier.getIdDomaineApplicatif());
        simpleCreancierHystorique.setIdTiers(simpleCreancier.getIdTiers());
        simpleCreancierHystorique.setIdTiersAdressePaiement(simpleCreancier.getIdTiersAdressePaiement());
        simpleCreancierHystorique.setIdTiersRegroupement(simpleCreancier.getIdTiersRegroupement());
        simpleCreancierHystorique.setIsBloque(simpleCreancier.getIsBloque());
        simpleCreancierHystorique.setMontantCreancier(simpleCreancier.getMontant());
        simpleCreancierHystorique.setReferencePaiement(simpleCreancier.getReferencePaiement());
        simpleCreancierHystorique.setIsCalcule(simpleCreancier.getIsCalcule());
        simpleCreancierHystorique.setIsHome(simpleCreancier.getIsHome());
        simpleCreancierHystorique.setPspyCreancier(simpleCreancier.getSpy());
        simpleCreancierHystorique.setCspyCreancier(simpleCreancier.getCreationSpy());
        /*
         * MAP créance accordée
         */
        simpleCreancierHystorique.setIdCreanceAccordee(simpleCreanceAccordee.getId());
        simpleCreancierHystorique.setIdOrdreVersement(simpleCreanceAccordee.getIdOrdreVersement());
        simpleCreancierHystorique.setIdPCAccordee(simpleCreanceAccordee.getIdPCAccordee());
        simpleCreancierHystorique.setMontantCreancieAccordee(simpleCreanceAccordee.getMontant());
        simpleCreancierHystorique.setCspyCreanceAccordee(simpleCreanceAccordee.getCreationSpy());
        simpleCreancierHystorique.setPspyCreanceAccordee(simpleCreanceAccordee.getSpy());


        return (SimpleCreancierHystorique) JadePersistenceManager.add(simpleCreancierHystorique);
    }

    @Override
    public SimpleCreancierHystorique delete(SimpleCreancierHystorique simpleCreanceAccordeeHyst) throws CreancierException,
            JadePersistenceException {
        if (simpleCreanceAccordeeHyst == null) {
            throw new CreancierException("Unable to delete simpleCreanceAccordeeHyst, the model passed is null!");
        }

        return (SimpleCreancierHystorique) JadePersistenceManager.delete(simpleCreanceAccordeeHyst);
    }

    public void delete(SimpleCreancierHystoriqueSearch simpleCreanceAccordeeHystSearch) throws CreancierException,
            JadePersistenceException {
        if (simpleCreanceAccordeeHystSearch == null) {
            throw new CreancierException("Unable to delete simpleCreanceAccordeeHystSearch, the model passed is null!");
        }

        JadePersistenceManager.delete(simpleCreanceAccordeeHystSearch);
    }


    @Override
    public SimpleCreancierHystorique read(String idSimpleCreancierHystorique) throws CreancierException,
            JadePersistenceException {
        if (idSimpleCreancierHystorique == null) {
            throw new CreancierException("Unable to read idSimpleCreancierHystorique, the model passed is null!");
        }
        SimpleCreancierHystorique simpleCreanceAccordeeHyst = new SimpleCreancierHystorique();
        simpleCreanceAccordeeHyst.setId(idSimpleCreancierHystorique);
        return (SimpleCreancierHystorique) JadePersistenceManager.read(simpleCreanceAccordeeHyst);
    }

    @Override
    public SimpleCreancierHystoriqueSearch search(SimpleCreancierHystoriqueSearch simpleCreanceAccordeeHystSearch)
            throws CreancierException, JadePersistenceException {
        if (simpleCreanceAccordeeHystSearch == null) {
            throw new CreancierException("Unable to search simpleCreanceAccordeeHystSearch, the model passed is null!");
        }
        return (SimpleCreancierHystoriqueSearch) JadePersistenceManager.search(simpleCreanceAccordeeHystSearch);
    }

    @Override
    public SimpleCreancierHystorique update(SimpleCreancierHystorique simpleCreanceAccordeeHyst) throws CreancierException,
            JadePersistenceException {
        if (simpleCreanceAccordeeHyst == null) {
            throw new CreancierException("Unable to update simpleCreanceAccordeeHyst, the model passed is null!");
        }
        return (SimpleCreancierHystorique) JadePersistenceManager.update(simpleCreanceAccordeeHyst);
    }

}
