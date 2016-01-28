package ch.globaz.pegasus.businessimpl.services.process.adaptation;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.exceptions.models.process.RenteAdapationDemandeException;
import ch.globaz.pegasus.business.models.process.adaptation.RenteAdapationDemande;
import ch.globaz.pegasus.business.models.process.adaptation.RenteAdapationDemandeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.process.adaptation.RenteAdapationDemandeService;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class RenteAdapationDemandeServiceImpl implements RenteAdapationDemandeService {

    @Override
    public int count(RenteAdapationDemandeSearch search) throws RenteAdapationDemandeException,
            JadePersistenceException {
        if (search == null) {
            throw new RenteAdapationDemandeException("Unable to count search, the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public RenteAdapationDemande create(RenteAdapationDemande renteAdapationDemande)
            throws RenteAdapationDemandeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {

        if (renteAdapationDemande == null) {
            throw new RenteAdapationDemandeException(
                    "Unable to create renteAdapationDemande, the model passed is null!");
        }

        if (renteAdapationDemande.getSimpleDemandeCentrale().isNew()) {
            renteAdapationDemande.setSimpleDemandeCentrale(PegasusImplServiceLocator.getSimpleDemandeCentraleService()
                    .create(renteAdapationDemande.getSimpleDemandeCentrale()));
        }

        renteAdapationDemande.getSimpleRenteAdaptation().setIdDemandeCentral(
                renteAdapationDemande.getSimpleDemandeCentrale().getIdDemandeCentral());

        renteAdapationDemande.setSimpleRenteAdaptation(PegasusServiceLocator.getSimpleRenteAdaptationService().create(
                renteAdapationDemande.getSimpleRenteAdaptation()));

        return renteAdapationDemande;

    }

    @Override
    public RenteAdapationDemande delete(RenteAdapationDemande renteAdapationDemande)
            throws RenteAdapationDemandeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        PegasusServiceLocator.getSimpleRenteAdaptationService()
                .delete(renteAdapationDemande.getSimpleRenteAdaptation());
        PegasusImplServiceLocator.getSimpleDemandeCentraleService().delete(
                renteAdapationDemande.getSimpleDemandeCentrale());
        return renteAdapationDemande;
    }

    @Override
    public Map<String, List<RenteAdapationDemande>> findByIdProcess(String idExecutionProcess)
            throws RenteAdapationDemandeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        RenteAdapationDemandeSearch search = new RenteAdapationDemandeSearch();
        search.setForIdProcess(idExecutionProcess);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search = PegasusServiceLocator.getRenteAdapationDemandeService().search(search);
        List<RenteAdapationDemande> list = PersistenceUtil.typeSearch(search, search.whichModelClass());

        Map<String, List<RenteAdapationDemande>> rentes = JadeListUtil.groupBy(list, new Key<RenteAdapationDemande>() {
            @Override
            public String exec(RenteAdapationDemande e) {
                return e.getSimpleDemandeCentrale().getIdDemandePC();
            }
        });

        return rentes;
    }

    @Override
    public RenteAdapationDemande read(String idRenteAdapationDemande) throws RenteAdapationDemandeException,
            JadePersistenceException {
        if (idRenteAdapationDemande == null) {
            throw new RenteAdapationDemandeException(
                    "Unable to read idRenteAdapationDemande, the model passed is null!");
        }
        RenteAdapationDemande RenteAdapationDemande = new RenteAdapationDemande();
        RenteAdapationDemande.setId(idRenteAdapationDemande);
        return (RenteAdapationDemande) JadePersistenceManager.read(RenteAdapationDemande);

    }

    @Override
    public RenteAdapationDemandeSearch search(RenteAdapationDemandeSearch renteAdapationDemandeSearch)
            throws RenteAdapationDemandeException, JadePersistenceException {
        if (renteAdapationDemandeSearch == null) {
            throw new RenteAdapationDemandeException(
                    "Unable to search renteAdapationDemandeSearch, the model passed is null!");
        }
        return (RenteAdapationDemandeSearch) JadePersistenceManager.search(renteAdapationDemandeSearch);
    }

    @Override
    public RenteAdapationDemande update(RenteAdapationDemande renteAdapationDemande)
            throws RenteAdapationDemandeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        if (renteAdapationDemande == null) {
            throw new RenteAdapationDemandeException(
                    "Unable to update renteAdapationDemande, the model passed is null!");
        }
        renteAdapationDemande.setSimpleDemandeCentrale(PegasusImplServiceLocator.getSimpleDemandeCentraleService()
                .update(renteAdapationDemande.getSimpleDemandeCentrale()));

        renteAdapationDemande.setSimpleRenteAdaptation(PegasusServiceLocator.getSimpleRenteAdaptationService().create(
                renteAdapationDemande.getSimpleRenteAdaptation()));
        PegasusImplServiceLocator.getSimpleDemandeCentraleService().update(
                renteAdapationDemande.getSimpleDemandeCentrale());

        return renteAdapationDemande;

    }

}
