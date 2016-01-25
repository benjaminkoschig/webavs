package ch.globaz.pegasus.businessimpl.services.models.renteijapi;

import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCApiAvsAi;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AllocationImpotentException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreApiException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreRenteException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IjApgException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IndemniteJournaliereAiException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.MembreFamilleAllocationImpotentException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotent;
import ch.globaz.pegasus.business.models.renteijapi.AutreApi;
import ch.globaz.pegasus.business.models.renteijapi.AutreRente;
import ch.globaz.pegasus.business.models.renteijapi.IjApg;
import ch.globaz.pegasus.business.models.renteijapi.IndemniteJournaliereAi;
import ch.globaz.pegasus.business.models.renteijapi.MembreFamilleAllocationImpotent;
import ch.globaz.pegasus.business.models.renteijapi.MembreFamilleAllocationImpotentSearch;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi;
import ch.globaz.pegasus.business.models.renteijapi.RenteIjApi;
import ch.globaz.pegasus.business.models.renteijapi.RenteIjApiSearch;
import ch.globaz.pegasus.business.models.renteijapi.RenteMembreFamilleCalculeFieldSearch;
import ch.globaz.pegasus.business.models.renteijapi.RenteMembreFamilleCalculeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.renteijapi.RenteIjApiService;
import ch.globaz.pegasus.business.vo.donneeFinanciere.RenteAvsAiVO;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.droit.DonneeFinanciereUtils;

public class RenteIjApiServiceImpl extends PegasusServiceLocator implements RenteIjApiService {

    private AllocationImpotent copyAllocationsImpotent(RenteIjApi renteIjApiNew, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        AllocationImpotent allocationImpotent = new AllocationImpotent();
        try {
            allocationImpotent.setSimpleDonneeFinanciereHeader(renteIjApiNew.getSimpleDonneeFinanciereHeader());
            allocationImpotent.setSimpleAllocationImpotent(renteIjApiNew.getSimpleAllocationImpotent());
            allocationImpotent = createAllocationImpotent(newDroit.getSimpleVersionDroit(), droitMembreFamille,
                    allocationImpotent);
            return allocationImpotent;
        } catch (AllocationImpotentException e) {
            throw new DonneeFinanciereException("Unable to copy the allocationImpotent", e);
        }
    }

    private AutreApi copyAutreApi(RenteIjApi renteIjApiNew, Droit newDroit, DroitMembreFamille droitMembreFamille)
            throws DonneeFinanciereException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        AutreApi autreApi = new AutreApi();

        autreApi.setSimpleDonneeFinanciereHeader(renteIjApiNew.getSimpleDonneeFinanciereHeader());
        autreApi.setSimpleAutreApi(renteIjApiNew.getSimpleAutreApi());
        try {
            autreApi = createAutreApi(newDroit.getSimpleVersionDroit(), droitMembreFamille, autreApi);
        } catch (AutreApiException e) {
            throw new DonneeFinanciereException("Unable to copy the autreApi", e);
        }

        return autreApi;
    }

    private AutreRente copyAutreRente(RenteIjApi renteIjApiNew, Droit newDroit, DroitMembreFamille droitMembreFamille)
            throws DonneeFinanciereException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        AutreRente autreRente = new AutreRente();

        autreRente.setSimpleDonneeFinanciereHeader(renteIjApiNew.getSimpleDonneeFinanciereHeader());
        autreRente.setSimpleAutreRente(renteIjApiNew.getSimpleAutreRente());
        try {
            autreRente = createAutreRente(newDroit.getSimpleVersionDroit(), droitMembreFamille, autreRente);
        } catch (AutreRenteException e) {
            throw new DonneeFinanciereException("Unable to copy the autreApi", e);
        }

        return autreRente;
    }

    private IjApg copyIjApg(RenteIjApi renteIjApiNew, Droit newDroit, DroitMembreFamille droitMembreFamille)
            throws DonneeFinanciereException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        IjApg ijApg = new IjApg();

        ijApg.setSimpleDonneeFinanciereHeader(renteIjApiNew.getSimpleDonneeFinanciereHeader());
        ijApg.setSimpleIjApg(renteIjApiNew.getIjApg().getSimpleIjApg());
        ijApg.setTiersFournisseurPrestation(renteIjApiNew.getIjApg().getTiersFournisseurPrestation());
        try {
            ijApg = createIjApg(newDroit.getSimpleVersionDroit(), droitMembreFamille, ijApg);
        } catch (IjApgException e) {
            throw new DonneeFinanciereException("Unable to copy the autreApi", e);
        }

        return ijApg;
    }

    private IndemniteJournaliereAi copyIndemniteJournaliereAi(RenteIjApi renteIjApiNew, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        IndemniteJournaliereAi indemniteJournaliereAi = new IndemniteJournaliereAi();

        indemniteJournaliereAi.setSimpleDonneeFinanciereHeader(renteIjApiNew.getSimpleDonneeFinanciereHeader());
        indemniteJournaliereAi.setSimpleIndemniteJournaliereAi(renteIjApiNew.getSimpleIndemniteJournaliereAi());
        try {
            indemniteJournaliereAi = createIndemniteJournaliereAi(newDroit.getSimpleVersionDroit(), droitMembreFamille,
                    indemniteJournaliereAi);
        } catch (IndemniteJournaliereAiException e) {
            throw new DonneeFinanciereException("Unable to copy the autreApi", e);
        }

        return indemniteJournaliereAi;
    }

    private RenteAvsAi copyRenteAvsAi(RenteIjApi renteIjApiNew, Droit newDroit, DroitMembreFamille droitMembreFamille)
            throws DonneeFinanciereException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        RenteAvsAi renteAvsAi = new RenteAvsAi();

        renteAvsAi.setSimpleDonneeFinanciereHeader(renteIjApiNew.getSimpleDonneeFinanciereHeader());
        renteAvsAi.setSimpleRenteAvsAi(renteIjApiNew.getSimpleRenteAvsAi());
        try {
            renteAvsAi = createRenteAvsAi(newDroit.getSimpleVersionDroit(), droitMembreFamille, renteAvsAi);
        } catch (RenteAvsAiException e) {
            throw new DonneeFinanciereException("Unable to copy the autreApi", e);
        }

        return renteAvsAi;
    }

    @Override
    public void copyRenteIjApi(Droit newDroit, Droit oldDroit, DroitMembreFamilleSearch droitMembreFamilleSearch)
            throws DonneeFinanciereException {
        try {
            RenteIjApiSearch searchModel = new RenteIjApiSearch();
            List<String> inCsTypeDonneeFinancierer = new ArrayList<String>();
            inCsTypeDonneeFinancierer.add(IPCApiAvsAi.CS_TYPE_DONNEE_FINANCIERE);
            inCsTypeDonneeFinancierer.add(IPCDroits.CS_AUTRES_API);
            inCsTypeDonneeFinancierer.add(IPCDroits.CS_AUTRES_RENTES);
            inCsTypeDonneeFinancierer.add(IPCDroits.CS_IJAI);
            inCsTypeDonneeFinancierer.add(IPCDroits.CS_RENTE_AVS_AI);// 64007001 //64007001
            inCsTypeDonneeFinancierer.add(IPCDroits.CS_INDEMNITES_JOURNLIERES_APG);

            searchModel.setInCsTypeDonneeFinancierer(inCsTypeDonneeFinancierer);
            searchModel.setWhereKey(RenteIjApiSearch.FOR_ALL_VALABLE_LE);
            searchModel.setForIdDroit(oldDroit.getSimpleDroit().getIdDroit());
            try {
                searchModel.setForDateValable(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(newDroit.getDemande()
                        .getSimpleDemande().getDateDepot()));
            } catch (JAException e) {
                throw new DonneeFinanciereException("Unable to convert Date dépôt to MM.AAAA format.", e);
            }

            searchModel = PegasusServiceLocator.getDroitService().searchRenteIjApi(searchModel);

            // RenteIjApi renteIjApiOld = null;
            RenteIjApi renteIjApiNew = null;
            String csType = null;
            DroitMembreFamille droitMembreFamille = null;
            for (JadeAbstractModel model : searchModel.getSearchResults()) {
                try {

                    renteIjApiNew = (RenteIjApi) JadePersistenceUtil.clone(model);

                    droitMembreFamille = DonneeFinanciereUtils.findTheDroitMembreFamilleInTheSarchModle(
                            droitMembreFamilleSearch, renteIjApiNew.getMembreFamilleEtendu().getDroitMembreFamille()
                                    .getSimpleDroitMembreFamille().getIdMembreFamilleSF());

                    // On ne copie pas la donné si le membre de famille n'a pas été trouvée
                    if (droitMembreFamille != null) {
                        csType = renteIjApiNew.getSimpleDonneeFinanciereHeader().getCsTypeDonneeFinanciere();

                        renteIjApiNew.setSimpleDonneeFinanciereHeader(DonneeFinanciereUtils
                                .copySimpleDonneFianciere(renteIjApiNew.getSimpleDonneeFinanciereHeader()));

                        if ((renteIjApiNew.getSimpleAllocationImpotent() != null)
                                && IPCApiAvsAi.CS_TYPE_DONNEE_FINANCIERE.equals(csType)) {
                            copyAllocationsImpotent(renteIjApiNew, newDroit, droitMembreFamille);
                        }
                        if ((renteIjApiNew.getSimpleAutreApi() != null) && IPCDroits.CS_AUTRES_API.equals(csType)) {
                            copyAutreApi(renteIjApiNew, newDroit, droitMembreFamille);
                        }
                        if ((renteIjApiNew.getSimpleAutreRente() != null) && IPCDroits.CS_AUTRES_RENTES.equals(csType)) {
                            copyAutreRente(renteIjApiNew, newDroit, droitMembreFamille);
                        }
                        if ((renteIjApiNew.getSimpleIndemniteJournaliereAi() != null)
                                && IPCDroits.CS_IJAI.equals(csType)) {
                            copyIndemniteJournaliereAi(renteIjApiNew, newDroit, droitMembreFamille);
                        }
                        if ((renteIjApiNew.getSimpleRenteAvsAi() != null) && IPCDroits.CS_RENTE_AVS_AI.equals(csType)) {
                            copyRenteAvsAi(renteIjApiNew, newDroit, droitMembreFamille);
                        }
                        if ((renteIjApiNew.getIjApg() != null)
                                && IPCDroits.CS_INDEMNITES_JOURNLIERES_APG.equals(csType)) {
                            copyIjApg(renteIjApiNew, newDroit, droitMembreFamille);
                        }
                    }

                } catch (JadeCloneModelException e) {
                    throw new DonneeFinanciereException("Unable to clone for the copy the renteIjApi", e);
                }
            }
        } catch (DroitException e) {
            throw new DonneeFinanciereException("Unable to search the renteIjApi for the copy", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Service not available - " + e.getMessage());
        } catch (JadePersistenceException e) {
            throw new DonneeFinanciereException(
                    "Unable to copy the donneeFinanciere (renteIjApi) probleme with the persistence" + e);
        }
    }

    @Override
    public AllocationImpotent createAllocationImpotent(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, AllocationImpotent newAllocationImpotent)
            throws JadePersistenceException, AllocationImpotentException, DonneeFinanciereException {

        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new AllocationImpotentException(
                    "Unable to create Allocation impotent, the droitMembreFamille is null or new");
        }
        if (newAllocationImpotent == null) {
            throw new AllocationImpotentException("Unable to create Allocation impotent, the model is null");
        }
        if ((simpleVersionDroit == null)) {
            throw new AllocationImpotentException("Unable to create Allocation impotent, the droit is null");
        }

        newAllocationImpotent.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCApiAvsAi.CS_TYPE_DONNEE_FINANCIERE);
        try {
            newAllocationImpotent.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, newAllocationImpotent.getSimpleDonneeFinanciereHeader()));

            newAllocationImpotent = PegasusImplServiceLocator.getAllocationImpotentService().create(
                    newAllocationImpotent);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AllocationImpotentException("Service not available - " + e.getMessage());
        }
        return newAllocationImpotent;
    }

    @Override
    public AutreApi createAutreApi(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            AutreApi newAutreApi) throws AutreApiException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new AutreApiException("Unable to create AutreApi, the droitMembreFamille is null or new");
        }
        if (newAutreApi == null) {
            throw new AutreApiException("Unable to create AutreApi, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new AutreApiException("Unable to create AutreApi, the droit");
        }

        try {
            newAutreApi.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_AUTRES_API);

            newAutreApi.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getDonneeFinanciereHeaderService()
                    .setDonneeFinanciereHeaderForCreation(simpleVersionDroit, droitMembreFamille,
                            newAutreApi.getSimpleDonneeFinanciereHeader()));

            newAutreApi = PegasusImplServiceLocator.getAutreApiService().create(newAutreApi);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutreApiException("Service not available - " + e.toString(), e);
        }
        return newAutreApi;
    }

    @Override
    public AutreRente createAutreRente(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            AutreRente newAutreRente) throws JadePersistenceException, AutreRenteException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new AutreRenteException("Unable to create autreRente, the droitMembreFamille is null or new");
        }
        if (newAutreRente == null) {
            throw new AutreRenteException("Unable to create autreRente, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new AutreRenteException("Unable to create autreRente, the simpleVersionDroit is null");
        }

        try {
            newAutreRente.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_AUTRES_RENTES);

            newAutreRente.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getDonneeFinanciereHeaderService()
                    .setDonneeFinanciereHeaderForCreation(simpleVersionDroit, droitMembreFamille,
                            newAutreRente.getSimpleDonneeFinanciereHeader()));

            newAutreRente = PegasusImplServiceLocator.getAutreRenteService().create(newAutreRente);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutreRenteException("Service not available - " + e.getMessage());
        }

        return newAutreRente;
    }

    @Override
    public IjApg createIjApg(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille, IjApg ijApg)
            throws JadePersistenceException, DonneeFinanciereException, IjApgException {

        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new IjApgException("Unable to create ijApg, the droitMembreFamille is null or new");
        }
        if (ijApg == null) {
            throw new IjApgException("Unable to create ijApg, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new IjApgException("Unable to create ijApg, the droit is null ");
        }

        ijApg.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_INDEMNITES_JOURNLIERES_APG);

        try {
            ijApg.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getDonneeFinanciereHeaderService()
                    .setDonneeFinanciereHeaderForCreation(simpleVersionDroit, droitMembreFamille,
                            ijApg.getSimpleDonneeFinanciereHeader()));

            ijApg = PegasusImplServiceLocator.getIjApgService().create(ijApg);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new IjApgException("Service not available - " + e.getMessage());
        }
        return ijApg;
    }

    @Override
    public IndemniteJournaliereAi createIndemniteJournaliereAi(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, IndemniteJournaliereAi newIndemniteJournaliereAi)
            throws JadePersistenceException, IndemniteJournaliereAiException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new IndemniteJournaliereAiException(
                    "Unable to create newIndemniteJournaliereAi, the droitMembreFamille is null or new");
        }
        if (newIndemniteJournaliereAi == null) {
            throw new IndemniteJournaliereAiException("Unable to create newIndemniteJournaliereAi, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new IndemniteJournaliereAiException("Unable to create newIndemniteJournaliereAi, the droit is null");
        }

        newIndemniteJournaliereAi.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_IJAI);

        try {
            newIndemniteJournaliereAi.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, newIndemniteJournaliereAi.getSimpleDonneeFinanciereHeader()));

            newIndemniteJournaliereAi = PegasusImplServiceLocator.getIndemniteJournaliereAiService().create(
                    newIndemniteJournaliereAi);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new IndemniteJournaliereAiException("Service not available - " + e.getMessage());
        }
        return newIndemniteJournaliereAi;
    }

    @Override
    public RenteAvsAi createRenteAvsAi(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            RenteAvsAi newRenteAvsAi) throws JadePersistenceException, RenteAvsAiException, DonneeFinanciereException {

        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new RenteAvsAiException("Unable to create Rente avs ai, the droitMembreFamille is null or new");
        }
        if (newRenteAvsAi == null) {
            throw new RenteAvsAiException("Unable to create Rente avs ai, the model is null");
        }
        if ((simpleVersionDroit == null)) {
            throw new RenteAvsAiException("Unable to create Rente avs ai, the simpleVersionDroit is null");
        }

        newRenteAvsAi.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_RENTE_AVS_AI);

        try {
            newRenteAvsAi.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getDonneeFinanciereHeaderService()
                    .setDonneeFinanciereHeaderForCreation(simpleVersionDroit, droitMembreFamille,
                            newRenteAvsAi.getSimpleDonneeFinanciereHeader()));

            newRenteAvsAi = PegasusImplServiceLocator.getRenteAvsAiService().create(newRenteAvsAi);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RenteAvsAiException("Service not available - " + e.getMessage());
        }
        return newRenteAvsAi;
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere, String typeDonneFinianciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (typeDonneFinianciere.contains("-" + IPCApiAvsAi.CS_TYPE_DONNEE_FINANCIERE + "-")) {
            PegasusImplServiceLocator.getSimpleAllocationImpotentService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_AUTRES_API + "-")) {
            PegasusImplServiceLocator.getSimpleAutreApiService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_AUTRES_RENTES + "-")) {
            PegasusImplServiceLocator.getSimpleAutreRenteService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_INDEMNITES_JOURNLIERES_APG + "-")) {
            PegasusImplServiceLocator.getSimpleIjApgService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_IJAI + "-")) {
            PegasusImplServiceLocator.getSimpleIndemniteJournaliereAiService().deleteParListeIdDoFinH(
                    idsDonneFinanciere);
        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_RENTE_AVS_AI + "-")) {
            PegasusImplServiceLocator.getSimpleRenteAvsAiService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }
    }

    @Override
    public MembreFamilleAllocationImpotent retrieveDegreAllocationImpotent(String idTiers, String idVersionDroit,
            String date) throws MembreFamilleAllocationImpotentException, JadePersistenceException {

        if (JadeStringUtil.isBlankOrZero(idTiers) || JadeStringUtil.isBlankOrZero(idVersionDroit)
                || JadeStringUtil.isBlankOrZero(date)) {
            throw new MembreFamilleAllocationImpotentException(
                    "Unable to search membreFamilleAllocationImpotent, the idTiers or the idVersionDroit or the date is null!");
        } else {
            MembreFamilleAllocationImpotentSearch membreFamilleAllocationImpotentSearch = new MembreFamilleAllocationImpotentSearch();
            membreFamilleAllocationImpotentSearch.setForIdTiers(idTiers);
            membreFamilleAllocationImpotentSearch.setForIdVersionDroit(idVersionDroit);
            membreFamilleAllocationImpotentSearch.setForDate(date);

            membreFamilleAllocationImpotentSearch = (MembreFamilleAllocationImpotentSearch) JadePersistenceManager
                    .search(membreFamilleAllocationImpotentSearch);
            if (membreFamilleAllocationImpotentSearch.getSize() == 1) {
                return (MembreFamilleAllocationImpotent) membreFamilleAllocationImpotentSearch.getSearchResults()[0];
            } else {
                if (membreFamilleAllocationImpotentSearch.getSize() > 1) {
                    throw new MembreFamilleAllocationImpotentException(
                            "More than one allocation impotent found with these parameters! idTiers : " + idTiers
                                    + " / idVersionDroit : " + idVersionDroit + " / date : " + date);
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public RenteIjApiSearch search(RenteIjApiSearch search) throws JadePersistenceException, RenteAvsAiException {
        if (search == null) {
            throw new RenteAvsAiException("Unable to search renteAvsAiException, the model passed is null!");
        }

        return (RenteIjApiSearch) JadePersistenceManager.search(search);
    }

    @Override
    public List<RenteAvsAiVO> searchRenteAvsAiByIdPCAccordee(String idPCAccodee, String dateValable)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, RenteAvsAiException {
        RenteIjApi renteIjApi = null;
        List<String> listCsType = new ArrayList<String>();
        listCsType.add(IPCDroits.CS_RENTE_AVS_AI);

        RenteIjApiSearch search = new RenteIjApiSearch();
        search.setForIdPCAccordee(idPCAccodee);
        search.setInCsTypeDonneeFinancierer(listCsType);
        search.setForDateValable(dateValable);
        search.setForIsSupprime(Boolean.FALSE);
        search.setWhereKey(RenteIjApiSearch.FOR_PC_ACCORDEE_AND_DATE_VALABLE);

        List<RenteAvsAiVO> list = new ArrayList<RenteAvsAiVO>();
        RenteAvsAiVO renteAvsAiVO = null;
        try {
            PegasusServiceLocator.getDroitService().searchRenteIjApi(search);
        } catch (DroitException e) {
            throw new RenteAvsAiException("unable to search the renteAvsAi", e);
        }
        for (JadeAbstractModel model : search.getSearchResults()) {
            renteIjApi = (RenteIjApi) model;
            renteAvsAiVO = new RenteAvsAiVO();
            renteAvsAiVO.setCsRoleFamillePc(renteIjApi.getMembreFamilleEtendu().getDroitMembreFamille()
                    .getSimpleDroitMembreFamille().getCsRoleFamillePC());
            renteAvsAiVO.setCsTypePc(renteIjApi.getSimpleRenteAvsAi().getCsTypePc());
            renteAvsAiVO.setCsTypeRente(renteIjApi.getSimpleRenteAvsAi().getCsTypeRente());
            renteAvsAiVO.setDateDebut(renteIjApi.getSimpleDonneeFinanciereHeader().getDateDebut());
            renteAvsAiVO.setDateFin(renteIjApi.getSimpleDonneeFinanciereHeader().getDateFin());
            renteAvsAiVO.setDateDepot(renteIjApi.getSimpleRenteAvsAi().getDateDepot());
            renteAvsAiVO.setDateDecision(renteIjApi.getSimpleRenteAvsAi().getDateDecision());
            renteAvsAiVO.setDateEcheance(renteIjApi.getSimpleRenteAvsAi().getDateEcheance());
            renteAvsAiVO.setIdTiers(renteIjApi.getMembreFamilleEtendu().getDroitMembreFamille().getMembreFamille()
                    .getSimpleMembreFamille().getIdTiers());
            renteAvsAiVO.setMontant(renteIjApi.getSimpleRenteAvsAi().getMontant());
            renteAvsAiVO.setNss(renteIjApi.getMembreFamilleEtendu().getDroitMembreFamille().getMembreFamille()
                    .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());
            renteAvsAiVO
                    .setNom(renteIjApi.getMembreFamilleEtendu().getDroitMembreFamille().getMembreFamille().getNom());
            renteAvsAiVO.setPrenom(renteIjApi.getMembreFamilleEtendu().getDroitMembreFamille().getMembreFamille()
                    .getPrenom());
            renteAvsAiVO.setDateNaissance(renteIjApi.getMembreFamilleEtendu().getDroitMembreFamille()
                    .getMembreFamille().getDateNaissance());
            renteAvsAiVO.setCsSexe(renteIjApi.getMembreFamilleEtendu().getDroitMembreFamille().getMembreFamille()
                    .getCsSexe());
            renteAvsAiVO.setCsNationalite(renteIjApi.getMembreFamilleEtendu().getDroitMembreFamille()
                    .getMembreFamille().getCsNationalite());
            list.add(renteAvsAiVO);
        }
        return list;
    }

    @Override
    public RenteMembreFamilleCalculeFieldSearch searchRenteMembreFamilleCalcule(
            RenteMembreFamilleCalculeFieldSearch search) throws JadePersistenceException, RenteAvsAiException {
        if (search == null) {
            throw new RenteAvsAiException("Unable to searchRenteAvsAiByIdPCAccordee search, the model passed is null!");
        }

        return (RenteMembreFamilleCalculeFieldSearch) JadePersistenceManager.search(search);
    }

    @Override
    public RenteMembreFamilleCalculeSearch searchRenteMembreFamilleCalcule(RenteMembreFamilleCalculeSearch search)
            throws JadePersistenceException, RenteAvsAiException {

        if (search == null) {
            throw new RenteAvsAiException("Unable to searchRenteAvsAiByIdPCAccordee search, the model passed is null!");
        }

        return (RenteMembreFamilleCalculeSearch) JadePersistenceManager.search(search);
    }

}