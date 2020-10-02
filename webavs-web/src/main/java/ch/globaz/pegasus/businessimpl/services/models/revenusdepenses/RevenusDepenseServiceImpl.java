package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.*;
import ch.globaz.pegasus.business.models.revenusdepenses.*;
import globaz.globall.util.JAException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.revenusdepenses.RevenusDepenseService;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.droit.DonneeFinanciereUtils;

public class RevenusDepenseServiceImpl implements RevenusDepenseService {

    private AllocationsFamiliales copyAllocationsFamiliales(RevenusDepenses revenusDepenses, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        AllocationsFamiliales allocationsFamiliales = new AllocationsFamiliales();
        try {
            allocationsFamiliales.setCaisse(revenusDepenses.getAllocationsFamiliales().getCaisse());
            allocationsFamiliales.setSimpleDonneeFinanciereHeader(revenusDepenses.getSimpleDonneeFinanciereHeader());
            allocationsFamiliales.setSimpleAllocationsFamiliales(revenusDepenses.getAllocationsFamiliales()
                    .getSimpleAllocationsFamiliales());
            allocationsFamiliales = createAllocationsFamiliales(newDroit.getSimpleVersionDroit(), droitMembreFamille,
                    allocationsFamiliales);
            return allocationsFamiliales;
        } catch (AllocationsFamilialesException e) {
            throw new DonneeFinanciereException("Unable to copy the allocationsFamiliales", e);
        }
    }

    private AutresRevenus copyAutresRevenus(RevenusDepenses revenusDepenses, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        AutresRevenus revenus = new AutresRevenus();
        try {
            revenus.setSimpleDonneeFinanciereHeader(revenusDepenses.getSimpleDonneeFinanciereHeader());
            revenus.setSimpleAutresRevenus(revenusDepenses.getSimpleAutresRevenus());
            revenus = createAutresRevenus(newDroit.getSimpleVersionDroit(), droitMembreFamille, revenus);
            return revenus;
        } catch (AutresRevenusException e) {
            throw new DonneeFinanciereException("Unable to copy the autresRevenus", e);
        }
    }

    private ContratEntretienViager copyContratEntretienViager(RevenusDepenses revenusDepenses, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        ContratEntretienViager contratEntretienViager = new ContratEntretienViager();
        try {
            contratEntretienViager.setSimpleDonneeFinanciereHeader(revenusDepenses.getSimpleDonneeFinanciereHeader());
            contratEntretienViager.setSimpleContratEntretienViager(revenusDepenses.getSimpleContratEntretienViager());

            SimpleLibelleContratEntretienViagerSearch libelleContratEntretienViagerSearch = new SimpleLibelleContratEntretienViagerSearch();
            libelleContratEntretienViagerSearch.setForIdContratEntretienViager(revenusDepenses
                    .getSimpleContratEntretienViager().getIdContratEntretienViager());
            try {
                PegasusImplServiceLocator.getSimpleLibelleContratEntretienViagerService().search(
                        libelleContratEntretienViagerSearch);
            } catch (SimpleLibelleContratEntretienViagerException e) {
                throw new DonneeFinanciereException(
                        "Unable to copy the contratEntretienViager, pb to find libelleContrat", e);
            }

            List<SimpleLibelleContratEntretienViager> list = new ArrayList<SimpleLibelleContratEntretienViager>();
            for (JadeAbstractModel model : libelleContratEntretienViagerSearch.getSearchResults()) {
                list.add((SimpleLibelleContratEntretienViager) model);
            }

            contratEntretienViager.setListLiebelleContratEntretienViagers(list);
            contratEntretienViager.setId("");
            contratEntretienViager = createContratEntretienViager(newDroit.getSimpleVersionDroit(), droitMembreFamille,
                    contratEntretienViager);
            return contratEntretienViager;
        } catch (ContratEntretienViagerException e) {
            throw new DonneeFinanciereException("Unable to copy the contratEntretienViager", e);
        }
    }

    private CotisationsPsal copyCotisationsPsal(RevenusDepenses revenusDepenses, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        CotisationsPsal cotisationsPsal = new CotisationsPsal();
        try {
            cotisationsPsal.setSimpleDonneeFinanciereHeader(revenusDepenses.getSimpleDonneeFinanciereHeader());
            cotisationsPsal.setSimpleCotisationsPsal(revenusDepenses.getCotisationsPsal().getSimpleCotisationsPsal());
            cotisationsPsal.setCaisse(revenusDepenses.getCotisationsPsal().getCaisse());
            cotisationsPsal = createCotisationsPsal(newDroit.getSimpleVersionDroit(), droitMembreFamille,
                    cotisationsPsal);
            return cotisationsPsal;
        } catch (CotisationsPsalException e) {
            throw new DonneeFinanciereException("Unable to copy the cotisationsPsal", e);
        }
    }
    //REFORME PC
    private FraisGarde copyFraisGarde(RevenusDepenses revenusDepenses, Droit newDroit,
                                                DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        FraisGarde fraisGarde = new FraisGarde();
        try {
            fraisGarde.setSimpleDonneeFinanciereHeader(revenusDepenses.getSimpleDonneeFinanciereHeader());
            fraisGarde.setSimpleFraisGarde(revenusDepenses.getSimpleFraisGarde());
            fraisGarde = createFraisGarde(newDroit.getSimpleVersionDroit(), droitMembreFamille, fraisGarde);
            return fraisGarde;
        } catch (FraisGardeException e) {
            throw new DonneeFinanciereException("Unable to copy the fraisGarde", e);
        }
    }


    private PensionAlimentaire copyPensionAlimentaire(RevenusDepenses revenusDepenses, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        PensionAlimentaire pensionAlimentaire = new PensionAlimentaire();
        try {

            pensionAlimentaire.setSimpleDonneeFinanciereHeader(revenusDepenses.getSimpleDonneeFinanciereHeader());
            pensionAlimentaire.setSimplePensionAlimentaire(revenusDepenses.getPensionAlimentaire()
                    .getSimplePensionAlimentaire());
            pensionAlimentaire.setTiers(revenusDepenses.getPensionAlimentaire().getTiers());
            pensionAlimentaire = createPensionAlimentaire(newDroit.getSimpleVersionDroit(), droitMembreFamille,
                    pensionAlimentaire);
            return pensionAlimentaire;
        } catch (PensionAlimentaireException e) {
            throw new DonneeFinanciereException("Unable to copy the pensionAlimentaire", e);
        }
    }

    private RevenuActiviteLucrativeDependante copyRevenuActiviteLucrativeDependante(RevenusDepenses revenusDepenses,
            Droit newDroit, DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        RevenuActiviteLucrativeDependante revenuActiviteLucrativeIndependante = new RevenuActiviteLucrativeDependante();
        try {
            revenuActiviteLucrativeIndependante.setSimpleDonneeFinanciereHeader(revenusDepenses
                    .getSimpleDonneeFinanciereHeader());
            revenuActiviteLucrativeIndependante.setSimpleRevenuActiviteLucrativeDependante(revenusDepenses
                    .getRevenuActiviteLucrativeDependante().getSimpleRevenuActiviteLucrativeDependante());
            revenuActiviteLucrativeIndependante.setEmployeur(revenusDepenses.getRevenuActiviteLucrativeDependante()
                    .getEmployeur());
            revenuActiviteLucrativeIndependante.setSimpleAffiliation(revenusDepenses
                    .getRevenuActiviteLucrativeDependante().getSimpleAffiliation());

            SimpleTypeFraisObtentionRevenuSearch simpleTypeFraisObtentionRevenuSearch = new SimpleTypeFraisObtentionRevenuSearch();
            simpleTypeFraisObtentionRevenuSearch
                    .setForIdRevenuActiviteLucrativeDependante(revenuActiviteLucrativeIndependante.getId());
            try {
                PegasusImplServiceLocator.getSimpleTypeFraisObtentionRevenuService().search(
                        simpleTypeFraisObtentionRevenuSearch);
            } catch (SimpleTypeFraisObtentionRevenuException e) {
                throw new DonneeFinanciereException(
                        "Unable to copy the revenuActiviteLucrativeIndependante, pb to find typeFrais", e);
            }

            List<SimpleTypeFraisObtentionRevenu> list = new ArrayList<SimpleTypeFraisObtentionRevenu>();
            for (JadeAbstractModel model : simpleTypeFraisObtentionRevenuSearch.getSearchResults()) {
                list.add((SimpleTypeFraisObtentionRevenu) model);
            }
            revenuActiviteLucrativeIndependante.setListTypeDeFrais(list);
            revenuActiviteLucrativeIndependante.setId("");
            revenuActiviteLucrativeIndependante = createRevenuActiviteLucrativeDependante(
                    newDroit.getSimpleVersionDroit(), droitMembreFamille, revenuActiviteLucrativeIndependante);
            return revenuActiviteLucrativeIndependante;
        } catch (RevenuActiviteLucrativeDependanteException e) {
            throw new DonneeFinanciereException("Unable to copy the revenuHypothetique", e);
        }
    }

    private RevenuActiviteLucrativeIndependante copyRevenuActiviteLucrativeIndependante(
            RevenusDepenses revenusDepenses, Droit newDroit, DroitMembreFamille droitMembreFamille)
            throws DonneeFinanciereException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante = new RevenuActiviteLucrativeIndependante();
        try {
            revenuActiviteLucrativeIndependante.setSimpleDonneeFinanciereHeader(revenusDepenses
                    .getSimpleDonneeFinanciereHeader());
            revenuActiviteLucrativeIndependante.setSimpleRevenuActiviteLucrativeIndependante(revenusDepenses
                    .getRevenuActiviteLucrativeIndependante().getSimpleRevenuActiviteLucrativeIndependante());
            revenuActiviteLucrativeIndependante.setCaisse(revenusDepenses.getRevenuActiviteLucrativeIndependante()
                    .getCaisse());
            revenuActiviteLucrativeIndependante.setTiersAffilie(revenusDepenses
                    .getRevenuActiviteLucrativeIndependante().getTiersAffilie());
            revenuActiviteLucrativeIndependante.setSimpleAffiliation(revenusDepenses
                    .getRevenuActiviteLucrativeIndependante().getSimpleAffiliation());
            revenuActiviteLucrativeIndependante.setCaisse(revenusDepenses.getRevenuActiviteLucrativeIndependante()
                    .getCaisse());

            revenuActiviteLucrativeIndependante = createRevenuActiviteLucrativeIndependante(
                    newDroit.getSimpleVersionDroit(), droitMembreFamille, revenuActiviteLucrativeIndependante);
            return revenuActiviteLucrativeIndependante;
        } catch (RevenuActiviteLucrativeIndependanteException e) {
            throw new DonneeFinanciereException("Unable to copy the revenuHypothetique", e);
        }
    }

    private RevenuHypothetique copyRevenuHypothetique(RevenusDepenses revenusDepenses, Droit newDroit,
            DroitMembreFamille droitMembreFamille) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        RevenuHypothetique revenuHypothetique = new RevenuHypothetique();
        try {
            revenuHypothetique.setSimpleDonneeFinanciereHeader(revenusDepenses.getSimpleDonneeFinanciereHeader());
            revenuHypothetique.setSimpleRevenuHypothetique(revenusDepenses.getSimpleRevenuHypothetique());
            revenuHypothetique = createRevenuHypothetique(newDroit.getSimpleVersionDroit(), droitMembreFamille,
                    revenuHypothetique);
            return revenuHypothetique;
        } catch (RevenuHypothetiqueException e) {
            throw new DonneeFinanciereException("Unable to copy the contratEntretienViager", e);
        }
    }

    @Override
    public void copyRevenusDepense(Droit newDroit, Droit oldDroit, DroitMembreFamilleSearch droitMembreFamilleSearch)
            throws DonneeFinanciereException {
        try {
            List<String> lisCsType = new ArrayList<String>();

            lisCsType.add(IPCDroits.CS_AUTRES_REVENUS);
            lisCsType.add(IPCDroits.CS_CONTRAT_ENTRETIEN_VIAGER);
            lisCsType.add(IPCDroits.CS_REVENU_HYPOTHETIQUE);
            lisCsType.add(IPCDroits.CS_ALLOCATIONS_FAMILIALLES);
            lisCsType.add(IPCDroits.CS_COTISATIONS_PSAL);
            lisCsType.add(IPCDroits.CS_PENSIONS_ALIMENTAIRES);
            lisCsType.add(IPCDroits.CS_REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE);
            lisCsType.add(IPCDroits.CS_REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE);

            RevenusDepensesSearch searchModel = new RevenusDepensesSearch();
            searchModel.setWhereKey(RevenusDepensesSearch.FOR_ALL_VALABLE_LE);
            searchModel.setInCsTypeDonneeFinancierer(lisCsType);
            searchModel.setForIdDroit(oldDroit.getSimpleDroit().getIdDroit());
            try {
                searchModel.setForDateValable(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(newDroit.getDemande()
                        .getSimpleDemande().getDateDepot()));
            } catch (JAException e) {
                throw new DonneeFinanciereException("Unable to convert Date dépôt to MM.AAAA format.", e);
            }

            searchModel = PegasusServiceLocator.getDroitService().searchRevenusDepenses(searchModel);

            RevenusDepenses revenusDepenses = null;
            String csType = null;
            DroitMembreFamille droitMembreFamille = null;
            for (JadeAbstractModel model : searchModel.getSearchResults()) {
                try {

                    revenusDepenses = (RevenusDepenses) JadePersistenceUtil.clone(model);

                    droitMembreFamille = DonneeFinanciereUtils.findTheDroitMembreFamilleInTheSarchModle(
                            droitMembreFamilleSearch, revenusDepenses.getMembreFamilleEtendu().getDroitMembreFamille()
                                    .getSimpleDroitMembreFamille().getIdMembreFamilleSF());

                    // On ne copie pas la donné si le membre de famille n'a pas été trouvée
                    if (droitMembreFamille != null) {
                        csType = revenusDepenses.getSimpleDonneeFinanciereHeader().getCsTypeDonneeFinanciere();

                        revenusDepenses.setSimpleDonneeFinanciereHeader(DonneeFinanciereUtils
                                .copySimpleDonneFianciere(revenusDepenses.getSimpleDonneeFinanciereHeader()));

                        if ((revenusDepenses.getSimpleAutresRevenus() != null)
                                && IPCDroits.CS_AUTRES_REVENUS.equals(csType)) {
                            copyAutresRevenus(revenusDepenses, newDroit, droitMembreFamille);

                        }
                        if ((revenusDepenses.getSimpleContratEntretienViager() != null)
                                && IPCDroits.CS_CONTRAT_ENTRETIEN_VIAGER.equals(csType)) {
                            revenusDepenses.getSimpleContratEntretienViager()
                                    .setIdContratEntretienViager(model.getId());
                            copyContratEntretienViager(revenusDepenses, newDroit, droitMembreFamille);

                        }
                        if ((revenusDepenses.getSimpleRevenuHypothetique() != null)
                                && IPCDroits.CS_REVENU_HYPOTHETIQUE.equals(csType)) {
                            copyRevenuHypothetique(revenusDepenses, newDroit, droitMembreFamille);

                        }
                        if ((revenusDepenses.getAllocationsFamiliales() != null)
                                && IPCDroits.CS_ALLOCATIONS_FAMILIALLES.equals(csType)) {
                            copyAllocationsFamiliales(revenusDepenses, newDroit, droitMembreFamille);
                        }
                        if ((revenusDepenses.getCotisationsPsal() != null)
                                && IPCDroits.CS_COTISATIONS_PSAL.equals(csType)) {
                            copyCotisationsPsal(revenusDepenses, newDroit, droitMembreFamille);
                        }
                        if ((revenusDepenses.getSimpleFraisGarde() != null)
                                && IPCDroits.CS_FRAIS_GARDE.equals(csType)) {
                            copyFraisGarde(revenusDepenses, newDroit, droitMembreFamille);
                        }
                        if ((revenusDepenses.getPensionAlimentaire() != null)
                                && IPCDroits.CS_PENSIONS_ALIMENTAIRES.equals(csType)) {
                            copyPensionAlimentaire(revenusDepenses, newDroit, droitMembreFamille);

                        }
                        if ((revenusDepenses.getRevenuActiviteLucrativeDependante() != null)
                                && IPCDroits.CS_REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE.equals(csType)) {
                            revenusDepenses.getRevenuActiviteLucrativeDependante().setId(model.getId());
                            copyRevenuActiviteLucrativeDependante(revenusDepenses, newDroit, droitMembreFamille);

                        }
                        if ((revenusDepenses.getRevenuActiviteLucrativeIndependante() != null)
                                && IPCDroits.CS_REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE.equals(csType)) {
                            copyRevenuActiviteLucrativeIndependante(revenusDepenses, newDroit, droitMembreFamille);
                        }
                    }
                } catch (JadeCloneModelException e) {
                    throw new DonneeFinanciereException("Unable to clone (habitat) for the copy ", e);
                }
            }
        } catch (DroitException e) {
            throw new DonneeFinanciereException("Unable to search the renteIjApi for the copy", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Service not available - " + e.getMessage());
        } catch (JadePersistenceException e) {
            throw new DonneeFinanciereException(
                    "Unable to copy the donneeFinanciere (habitat) probleme with the persistence" + e);
        }
    }

    @Override
    public AllocationsFamiliales createAllocationsFamiliales(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, AllocationsFamiliales allocationsFamiliales)
            throws AllocationsFamilialesException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DonneeFinanciereException(
                    "Unable to create allocationsFamiliales, the droitMembreFamille is null or new");
        }
        if (allocationsFamiliales == null) {
            throw new DonneeFinanciereException("Unable to create allocationsFamiliales, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new DonneeFinanciereException(
                    "Unable to create allocationsFamiliales, the simpleVersionDroit is null");
        }

        allocationsFamiliales.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCDroits.CS_ALLOCATIONS_FAMILIALLES);

        try {
            allocationsFamiliales.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, allocationsFamiliales.getSimpleDonneeFinanciereHeader()));

            allocationsFamiliales = PegasusImplServiceLocator.getAllocationsFamilialesService().create(
                    allocationsFamiliales);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Service not available - " + e.getMessage());
        }

        return allocationsFamiliales;
    }

    @Override
    public AutresRevenus createAutresRevenus(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, AutresRevenus autresRevenus) throws AutresRevenusException,
            JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new AutresRevenusException("Unable to create AutresRevenus, the droitMembreFamille is null or new");
        }
        if (autresRevenus == null) {
            throw new AutresRevenusException("Unable to create AutresRevenus, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new AutresRevenusException("Unable to create AutresRevenus, the simpleVersionDroit is null");
        }

        autresRevenus.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_AUTRES_REVENUS);

        try {
            autresRevenus.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getDonneeFinanciereHeaderService()
                    .setDonneeFinanciereHeaderForCreation(simpleVersionDroit, droitMembreFamille,
                            autresRevenus.getSimpleDonneeFinanciereHeader()));

            autresRevenus = PegasusImplServiceLocator.getAutresRevenusService().create(autresRevenus);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutresRevenusException("Service not available - " + e.getMessage());
        }

        return autresRevenus;

    }

    @Override
    public ContratEntretienViager createContratEntretienViager(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, ContratEntretienViager newContratEntretienViager)
            throws ContratEntretienViagerException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new ContratEntretienViagerException(
                    "Unable to create allocationsFamiliales, the droitMembreFamille is null or new");
        }
        if (newContratEntretienViager == null) {
            throw new ContratEntretienViagerException("Unable to create allocationsFamiliales, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new ContratEntretienViagerException(
                    "Unable to create allocationsFamiliales, the simpleVersionDroit is null");
        }

        newContratEntretienViager.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCDroits.CS_CONTRAT_ENTRETIEN_VIAGER);

        try {
            newContratEntretienViager.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, newContratEntretienViager.getSimpleDonneeFinanciereHeader()));

            newContratEntretienViager = PegasusImplServiceLocator.getContratEntretienViagerService().create(
                    newContratEntretienViager);
            List<SimpleLibelleContratEntretienViager> listLibelle = new ArrayList<SimpleLibelleContratEntretienViager>();
            for (SimpleLibelleContratEntretienViager model : newContratEntretienViager
                    .getListLiebelleContratEntretienViagers()) {
                model.setIdContratEntretienViager(newContratEntretienViager.getId());
                try {
                    listLibelle.add(PegasusImplServiceLocator.getSimpleLibelleContratEntretienViagerService().create(
                            model));
                } catch (SimpleLibelleContratEntretienViagerException e) {
                    throw new ContratEntretienViagerException(
                            "Unable to create allocationsFamiliales, problem with the libelleContratViagere");
                }

            }
            newContratEntretienViager.setListLiebelleContratEntretienViagers(listLibelle);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ContratEntretienViagerException("Service not available - " + e.getMessage());
        }

        return newContratEntretienViager;
    }

    @Override
    public CotisationsPsal createCotisationsPsal(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, CotisationsPsal cotisationsPsal) throws CotisationsPsalException,
            JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DonneeFinanciereException(
                    "Unable to create cotisationsPsal, the droitMembreFamille is null or new");
        }
        if (cotisationsPsal == null) {
            throw new DonneeFinanciereException("Unable to create cotisationsPsal, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new DonneeFinanciereException("Unable to create cotisationsPsal, the simpleVersionDroit is null");
        }

        cotisationsPsal.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_COTISATIONS_PSAL);

        try {
            cotisationsPsal.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, cotisationsPsal.getSimpleDonneeFinanciereHeader()));

            cotisationsPsal = PegasusImplServiceLocator.getCotisationsPsalService().create(cotisationsPsal);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Service not available - " + e.getMessage());
        }

        return cotisationsPsal;
    }

    @Override
    public PensionAlimentaire createPensionAlimentaire(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, PensionAlimentaire pensionAlimentaire)
            throws PensionAlimentaireException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new PensionAlimentaireException(
                    "Unable to create pensionAlimentaire, the droitMembreFamille is null or new");
        }
        if (pensionAlimentaire == null) {
            throw new PensionAlimentaireException("Unable to create pensionAlimentaire, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new PensionAlimentaireException("Unable to create pensionAlimentaire, the simpleVersionDroit is null");
        }

        pensionAlimentaire.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCDroits.CS_PENSIONS_ALIMENTAIRES);

        try {
            pensionAlimentaire.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, pensionAlimentaire.getSimpleDonneeFinanciereHeader()));

            pensionAlimentaire = PegasusImplServiceLocator.getPensionAlimentaireService().create(pensionAlimentaire);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PensionAlimentaireException("Service not available - " + e.getMessage());
        }

        return pensionAlimentaire;
    }

    @Override
    public RevenuActiviteLucrativeDependante createRevenuActiviteLucrativeDependante(
            SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            RevenuActiviteLucrativeDependante newRevenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "Unable to create revenuActiviteLucrativeDependante, the droitMembreFamille is null or new");
        }
        if (newRevenuActiviteLucrativeDependante == null) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "Unable to create revenuActiviteLucrativeDependante, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "Unable to create revenuActiviteLucrativeDependante, the simpleVersionDroit is null");
        }

        newRevenuActiviteLucrativeDependante.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCDroits.CS_REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE);

        try {
            newRevenuActiviteLucrativeDependante
                    .setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getDonneeFinanciereHeaderService()
                            .setDonneeFinanciereHeaderForCreation(simpleVersionDroit, droitMembreFamille,
                                    newRevenuActiviteLucrativeDependante.getSimpleDonneeFinanciereHeader()));

            newRevenuActiviteLucrativeDependante = PegasusImplServiceLocator
                    .getRevenuActiviteLucrativeDependanteService().create(newRevenuActiviteLucrativeDependante);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RevenuActiviteLucrativeDependanteException("Service not available - " + e.getMessage());
        }

        return newRevenuActiviteLucrativeDependante;
    }

    @Override
    public RevenuActiviteLucrativeIndependante createRevenuActiviteLucrativeIndependante(
            SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            RevenuActiviteLucrativeIndependante newRevenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to create revenuActiviteLucrativeIndependante, the droitMembreFamille is null or new");
        }
        if (newRevenuActiviteLucrativeIndependante == null) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to create revenuActiviteLucrativeIndependante, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to create revenuActiviteLucrativeIndependante, the simpleVersionDroit is null or new");
        }

        newRevenuActiviteLucrativeIndependante.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCDroits.CS_REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE);

        try {
            newRevenuActiviteLucrativeIndependante.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille,
                            newRevenuActiviteLucrativeIndependante.getSimpleDonneeFinanciereHeader()));

            newRevenuActiviteLucrativeIndependante = PegasusImplServiceLocator
                    .getRevenuActiviteLucrativeIndependanteService().create(newRevenuActiviteLucrativeIndependante);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RevenuActiviteLucrativeIndependanteException("Service not available - " + e.getMessage());
        }
        return newRevenuActiviteLucrativeIndependante;
    }

    @Override
    public RevenuHypothetique createRevenuHypothetique(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, RevenuHypothetique newRevenuHypothetique)
            throws RevenuHypothetiqueException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new RevenuHypothetiqueException(
                    "Unable to create RevenuHypothetique, the droitMembreFamille is null or new");
        }
        if (newRevenuHypothetique == null) {
            throw new RevenuHypothetiqueException("Unable to create RevenuHypothetique, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new RevenuHypothetiqueException("Unable to create RevenuHypothetique, the simpleVersionDroit is null");
        }

        newRevenuHypothetique.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(
                IPCDroits.CS_REVENU_HYPOTHETIQUE);

        try {
            newRevenuHypothetique.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, newRevenuHypothetique.getSimpleDonneeFinanciereHeader()));

            newRevenuHypothetique = PegasusImplServiceLocator.getRevenuHypothetiqueService().create(
                    newRevenuHypothetique);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RevenuHypothetiqueException("Service not available - " + e.getMessage());
        }
        return newRevenuHypothetique;
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere, String typeDonneFinianciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            RevenuActiviteLucrativeDependanteException {
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_ALLOCATIONS_FAMILIALLES + "-")) {
            PegasusImplServiceLocator.getSimpleAllocationsFamilialesService()
                    .deleteParListeIdDoFinH(idsDonneFinanciere);
        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_AUTRES_REVENUS + "-")) {
            PegasusImplServiceLocator.getSimpleAutresRevenusService().deleteParListeIdDoFinH(idsDonneFinanciere);
        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_CONTRAT_ENTRETIEN_VIAGER + "-")) {
            PegasusImplServiceLocator.getSimpleContratEntretienViagerService().deleteParListeIdDoFinH(
                    idsDonneFinanciere);

        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_COTISATIONS_PSAL + "-")) {
            PegasusImplServiceLocator.getSimpleCotisationsPsalService().deleteParListeIdDoFinH(idsDonneFinanciere);

        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_FRAIS_GARDE + "-")) {
            PegasusImplServiceLocator.getSimpleFraisGardeService().deleteParListeIdDoFinH(idsDonneFinanciere);

        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_PENSIONS_ALIMENTAIRES + "-")) {
            PegasusImplServiceLocator.getSimplePensionAlimentaireService().deleteParListeIdDoFinH(idsDonneFinanciere);

        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE + "-")) {
            PegasusImplServiceLocator.getRevenuActiviteLucrativeDependanteService().deleteParListeIdDoFinH(
                    idsDonneFinanciere);

        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE + "-")) {
            PegasusImplServiceLocator.getSimpleRevenuActiviteLucrativeIndependanteService().deleteParListeIdDoFinH(
                    idsDonneFinanciere);

        }
        if (typeDonneFinianciere.contains("-" + IPCDroits.CS_REVENU_HYPOTHETIQUE + "-")) {
            PegasusImplServiceLocator.getSimpleRevenuHypothetiqueService().deleteParListeIdDoFinH(idsDonneFinanciere);

        }
    }
    //REFORME PC
    @Override
    public FraisGarde createFraisGarde(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille, FraisGarde fraisGarde)
            throws FraisGardeException, JadePersistenceException, DonneeFinanciereException {
        if ((droitMembreFamille == null) || droitMembreFamille.isNew()) {
            throw new DonneeFinanciereException(
                    "Unable to create fraisGarde, the droitMembreFamille is null or new");
        }
        if (fraisGarde == null) {
            throw new DonneeFinanciereException("Unable to create fraisGarde, the model is null");
        }
        if (simpleVersionDroit == null) {
            throw new DonneeFinanciereException("Unable to create fraisGarde, the simpleVersionDroit is null");
        }

        fraisGarde.getSimpleDonneeFinanciereHeader().setCsTypeDonneeFinanciere(IPCDroits.CS_FRAIS_GARDE);

        try {
            fraisGarde.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getDonneeFinanciereHeaderService().setDonneeFinanciereHeaderForCreation(simpleVersionDroit,
                            droitMembreFamille, fraisGarde.getSimpleDonneeFinanciereHeader()));

            fraisGarde = PegasusImplServiceLocator.getFraisGardeService().create(fraisGarde);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Service not available - " + e.getMessage());
        }

        return fraisGarde;
    }

}
