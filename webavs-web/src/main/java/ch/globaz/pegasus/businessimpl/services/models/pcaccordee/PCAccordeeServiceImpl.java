package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.corvus.business.exceptions.models.RentesAccordeesException;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabilite;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PersonneDansPlanCalculException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.annonce.SimpleCommunicationOCCSearch;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesHomeSearch;
import ch.globaz.pegasus.business.models.creancier.*;
import ch.globaz.pegasus.business.models.decision.DecisionSuppressionSearch;
import ch.globaz.pegasus.business.models.decision.ForDeleteDecision;
import ch.globaz.pegasus.business.models.decision.ForDeleteDecisionSearch;
import ch.globaz.pegasus.business.models.decision.ValidationDecisionSearch;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompenseSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.pcaccordee.*;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeService;
import ch.globaz.pegasus.business.vo.pcaccordee.PCAccordeePlanCalculAndMembreFamilleVO;
import ch.globaz.pegasus.business.vo.pcaccordee.PCAMembreFamilleVO;
import ch.globaz.pegasus.business.vo.pcaccordee.PCAccordeePlanCalculRetenuEnfantsDansCalculVO;
import ch.globaz.pegasus.business.vo.pcaccordee.PCAccordeeVO;
import ch.globaz.pegasus.business.vo.pcaccordee.PlanDeCalculVO;
import ch.globaz.pegasus.businessimpl.checkers.droit.DroitChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.calcul.CalculComparatifServiceImpl;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.suppression.GenerateOvsForSuppression;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ComptabilisationUtil;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import static ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL;
import static ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul.CLE_DEPEN_SEJOUR_MOIS_PARTIEL_TOTAL;

public class PCAccordeeServiceImpl extends PegasusAbstractServiceImpl implements PCAccordeeService {

    private String idCreancier = "";

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeService
     * #count(ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch)
     */
    @Override
    public int count(PCAccordeeSearch search) throws PCAccordeeException, JadePersistenceException {
        if (search == null) {
            throw new PCAccordeeException("Unable to count pcAccordee, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public int countMembreFamilleForPlanRetenu(String idPcal) throws JadePersistenceException, PCAccordeeException {
        if (idPcal == null) {
            throw new PCAccordeeException(
                    "Unable to count the numbers of MembreFamille for PlanRetenu, the idPlanCalcul is null!!");
        }
        PlanDeCalculWitMembreFamilleSearch search = new PlanDeCalculWitMembreFamilleSearch();
        search.setForIdPcal(idPcal);
        search.setForComprisPcal(true);
        return JadePersistenceManager.count(search);
    }

    @Override
    public PCAccordee create(PCAccordee pcAccordee) throws JadePersistenceException, PCAccordeeException {
        if (pcAccordee == null) {
            throw new PCAccordeeException("Unable to create pcAccordee, the model passed is null!");
        }
        try {
            pcAccordee.setSimplePCAccordee(
                    PegasusImplServiceLocator.getSimplePCAccordeeService().create(pcAccordee.getSimplePCAccordee()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PCAccordeeException("Service not available - " + e.getMessage());
        }
        return pcAccordee;
    }

    @Override
    public PCAccordee delete(PCAccordee pcAccordee)
            throws PCAccordeeException, JadePersistenceException, JadeApplicationException {
        if (pcAccordee == null) {
            throw new PCAccordeeException("Unable to delete pcAccordee, the model passed is null!");
        }

        ValidationDecisionSearch validationDecisionSearch = new ValidationDecisionSearch();
        validationDecisionSearch.setForIdPca(pcAccordee.getSimplePCAccordee().getId());
        // try {
        // int count = PegasusImplServiceLocator.getValidationDecisionService().count(validationDecisionSearch);
        // if (count > 0) {
        deleteDecisionsApresCalculByIdPdAccordee(pcAccordee.getSimplePCAccordee().getIdPCAccordee(),
                pcAccordee.getSimpleVersionDroit().getIdVersionDroit());
        // }
        // } catch (DecisionException e1) {
        // throw new PCAccordeeException("Unable to count decision - " + e1.getMessage());
        // }

        // nettoyage des decisions de suppression
        DecisionSuppressionSearch decisionSuppressionSearch = new DecisionSuppressionSearch();
        decisionSuppressionSearch.setForIdVersionDroit(pcAccordee.getSimpleVersionDroit().getIdVersionDroit());
        decisionSuppressionSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        PegasusImplServiceLocator.getDecisionSuppressionService().delete(decisionSuppressionSearch);

        deleteCreancierByIdPcAccordee(pcAccordee.getSimplePCAccordee().getIdPCAccordee());
        deleteCreanceByIdPcAccordee(pcAccordee.getSimplePCAccordee().getIdPCAccordee());


        // suppression des dettes comptat compensés
        SimpleDetteComptatCompenseSearch detteComptatCompenseSearch = new SimpleDetteComptatCompenseSearch();
        detteComptatCompenseSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        detteComptatCompenseSearch.setForIdVersionDroit(pcAccordee.getSimpleVersionDroit().getIdVersionDroit());
        PegasusImplServiceLocator.getSimpleDetteComptatCompenseService().delete(detteComptatCompenseSearch);

        // Suppression des prestations, si conjoint...
        if (!JadeStringUtil.isBlank(pcAccordee.getSimplePrestationsAccordeesConjoint().getIdPrestationAccordee())) {
            deletePrestation(pcAccordee, IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
        }
        // et requérant de toute façon
        deletePrestation(pcAccordee, IPCDroits.CS_ROLE_FAMILLE_REQUERANT);

        deletePlanDeCalcule(pcAccordee.getSimplePCAccordee().getIdPCAccordee());

        SimpleJoursAppointSearch simpleJoursAppointSearch = new SimpleJoursAppointSearch();
        simpleJoursAppointSearch.setForIdPCAccordee(pcAccordee.getSimplePCAccordee().getIdPCAccordee());
        simpleJoursAppointSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        PegasusImplServiceLocator.getSimpleJoursAppointService().delete(simpleJoursAppointSearch);

        SimpleAllocationNoelSearch search = new SimpleAllocationNoelSearch();
        search.setForIdPcAccordee(pcAccordee.getSimplePCAccordee().getIdPCAccordee());
        PegasusImplServiceLocator.getSimpleAllocationDeNoelService().delete(search);

        try {
            pcAccordee.setSimplePCAccordee(
                    PegasusImplServiceLocator.getSimplePCAccordeeService().delete(pcAccordee.getSimplePCAccordee()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PCAccordeeException("Service not available - " + e.getMessage());
        }
        return null;
    }


    @Override
    public void deleteByIdVersionDroit(Droit droit)
            throws JadePersistenceException, PCAccordeeException, JadeApplicationException {

        if ((droit == null) || droit.isNew()) {
            throw new DroitException("Cannot delete from an non existing or empty droit");
        }

        List<String> idPca = new ArrayList<String>();
        if (DroitChecker.isDeletable(droit.getSimpleVersionDroit())) {
            PCAccordeeSearch pcAccordeeSearch = new PCAccordeeSearch();
            pcAccordeeSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            pcAccordeeSearch.setForVersionDroit(droit.getSimpleVersionDroit().getId());
            PegasusImplServiceLocator.getPCAccordeeService().search(pcAccordeeSearch);
            if (pcAccordeeSearch.getSize() > 0) {
                for (JadeAbstractModel d : pcAccordeeSearch.getSearchResults()) {
                    PCAccordee pcAccordee = (PCAccordee) d;
                    delete(pcAccordee);
                    idPca.add(pcAccordee.getId());
                }
            }

            /*
             * if(idPca.size()>0){ SimpleJoursAppointSearch search = new SimpleJoursAppointSearch();
             * search.setInIdsPca(idPca); PegasusImplServiceLocator.getSimpleJoursAppointService().delete(search) }
             */
        }
    }




    @Override
    public void deleteByIdVersionDroit(String idDroit) throws JadePersistenceException, JadeApplicationException {
        PCAccordeeSearch pcAccordeeSearch = new PCAccordeeSearch();
        pcAccordeeSearch.setForVersionDroit(idDroit);
        PegasusImplServiceLocator.getPCAccordeeService().search(pcAccordeeSearch);
        if (pcAccordeeSearch.getSize() > 0) {
            for (JadeAbstractModel d : pcAccordeeSearch.getSearchResults()) {
                PCAccordee pcAccordee = (PCAccordee) d;
                delete(pcAccordee);
            }
        }
    }

    private void deleteCreanceByIdPcAccordee(String idPcAccordee)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, PCAccordeeException {
        SimpleCreanceAccordeeSearch simpleCreanceAccordeeSearch = new SimpleCreanceAccordeeSearch();
        simpleCreanceAccordeeSearch.setForIdPcAccordee(idPcAccordee);
        try {
            PegasusServiceLocator.getCreanceAccordeeService().deleteWithSearchModele(simpleCreanceAccordeeSearch);
        } catch (CreancierException e1) {
            throw new PCAccordeeException("Unable to delete the creance accordee", e1);
        }
    }

    private void deleteCreanceById(String idCreance)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, PCAccordeeException {
        SimpleCreanceAccordeeSearch simpleCreanceAccordeeSearch = new SimpleCreanceAccordeeSearch();
        simpleCreanceAccordeeSearch.setForIdCreaanceAccordee(idCreance);
        try {
            PegasusServiceLocator.getCreanceAccordeeService().deleteWithSearchModele(simpleCreanceAccordeeSearch);
        } catch (CreancierException e1) {
            throw new PCAccordeeException("Unable to delete the creance accordee", e1);
        }
    }

    private void deleteCreancierByIdPcAccordee(String idPCAccordee) throws JadeApplicationServiceNotAvailableException, CreancierException, JadePersistenceException {
        CreanceAccordeeSearch creanceAccordeeSearch = new CreanceAccordeeSearch();
        creanceAccordeeSearch.setForIdPCAccordee(idPCAccordee);
        creanceAccordeeSearch = PegasusImplServiceLocator.getCreanceAccordeeService().search(creanceAccordeeSearch);
        List<String> creancierSup = new ArrayList<>();
        for (JadeAbstractModel model : creanceAccordeeSearch.getSearchResults()) {
            CreanceAccordee simpleCreance = (CreanceAccordee) model;
            if(simpleCreance.getSimpleCreancier().getIsCalcule()
                    && !creancierSup.contains(simpleCreance.getSimpleCreancier().getIdCreancier())){
                creancierSup.add(simpleCreance.getSimpleCreancier().getIdCreancier());
                PegasusImplServiceLocator.getSimpleCreancierService().deleteWithoutControl(
                        simpleCreance.getSimpleCreancier());
            }

        }
    }


    private void deleteDecisionsApresCalculByIdPdAccordee(String idPcAccordee, String idVersionDroit)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException, PCAccordeeException,
            AnnonceException, PrestationException, PropertiesException {

        if (JadeStringUtil.isBlankOrZero(idPcAccordee)) {
            throw new AnnonceException("Unable to delete annoce ths idPcAccordee is empty");
        }

        ForDeleteDecisionSearch search = new ForDeleteDecisionSearch();

        search.setForIdPcAccordee(idPcAccordee);
        search.setInCsTypeDecsion(new ArrayList<String>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                this.add(IPCDecision.CS_TYPE_ADAPTATION_AC);
                this.add(IPCDecision.CS_TYPE_OCTROI_AC);
                this.add(IPCDecision.CS_TYPE_PARTIEL_AC);
                this.add(IPCDecision.CS_TYPE_REFUS_AC);
            }
        });

        // search = (ForDeleteDecisionSearch) JadePersistenceManager.search(search);

        if (EPCProperties.GESTION_ANNONCES_LAPRAMS.getBooleanValue()
                || EPCProperties.GESTION_COMMUNICATION_OCC.getBooleanValue()) {
            List<ForDeleteDecision> decisions = PersistenceUtil.search(search, search.whichModelClass());

            if (search.getSize() > 0) {
                List<String> listeIdDecisionHeader = new ArrayList<String>();

                for (ForDeleteDecision dc : decisions) {
                    listeIdDecisionHeader.add(dc.getIdDecisionHeader());
                }

                if (EPCProperties.GESTION_ANNONCES_LAPRAMS.getBooleanValue()) {
                    if (listeIdDecisionHeader.size() > 0) {
                        PegasusImplServiceLocator.getAnnonceLapramsService().deleteByIdsDecision(listeIdDecisionHeader);
                    }
                }

                if (EPCProperties.GESTION_COMMUNICATION_OCC.getBooleanValue()) {
                    SimpleCommunicationOCCSearch searchOcc = new SimpleCommunicationOCCSearch();
                    searchOcc.setForIdVersionDroit(idVersionDroit);
                    PegasusImplServiceLocator.getSimpleCommunicationOCCService().delete(searchOcc);
                }
            }
        }
        try {
            search = (ForDeleteDecisionSearch) JadePersistenceManager.search(search);

            PegasusImplServiceLocator.getDecisionApresCalculService().delete(search);
        } catch (DecisionException e) {
            throw new PCAccordeeException("Unable to delete the decision by this idPcAccordee: " + idPcAccordee, e);
        }
    }

    private void deleteJoursAppoint(SimpleJoursAppoint simpleJoursAppoint)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        PegasusImplServiceLocator.getSimpleJoursAppointService().delete(simpleJoursAppoint);

    }

    private void deletePlanDeCalcule(String idPcAccordee)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        SimplePlanDeCalculSearch planCalculSearch = new SimplePlanDeCalculSearch();
        planCalculSearch.setForIdPCAccordee(idPcAccordee);
        planCalculSearch = PegasusImplServiceLocator.getSimplePlanDeCalculService().search(planCalculSearch);

        List<String> ids = new ArrayList<String>(planCalculSearch.getSize());
        for (JadeAbstractModel model : planCalculSearch.getSearchResults()) {
            SimplePlanDeCalcul planCalcul = (SimplePlanDeCalcul) model;
            ids.add(planCalcul.getId());
            // Obliger de suprmier tuple par tuple a parceque 'il y un blob !!
            PegasusImplServiceLocator.getSimplePlanDeCalculService().delete(planCalcul);
        }

        // PegasusImplServiceLocator.getSimplePlanDeCalculService().delete(planCalculSearch);

        if (ids.size() != 0) {
            SimplePersonneDansPlanCalculSearch personneSearch = new SimplePersonneDansPlanCalculSearch();
            personneSearch.setInIdPlanDeCalcul(ids);
            PegasusImplServiceLocator.getSimplePersonneDansPlanCalculService().delete(personneSearch);

        }
    }

    private void deletePrestation(PCAccordee pcAccordee, String membre)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException,
            PCAccordeeException {

        if (!JadeStringUtil.isNull(pcAccordee.getSimplePCAccordee().getIdPrestationAccordee())) {

            SimplePrestationsAccordees prestation = null;

            // intstanciation de la simplePrestationsAccordees selon le membre de famille, si 2 simplePRest.
            if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
                prestation = PegasusImplServiceLocator.getSimplePrestatioAccordeeService()
                        .read(pcAccordee.getSimplePCAccordee().getIdPrestationAccordee());
            } else if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT)) {
                prestation = PegasusImplServiceLocator.getSimplePrestatioAccordeeService()
                        .read(pcAccordee.getSimplePCAccordee().getIdPrestationAccordeeConjoint());
            } else {
                throw new PCAccordeeException("Role famille (" + membre + ") should be either requerant or conjoint!!");
            }

            String idInfoComptable = prestation.getIdInfoCompta();

            PegasusImplServiceLocator.getSimplePrestatioAccordeeService().delete(prestation);

            // suppression d'eventuels informations comptabilite
            if (idInfoComptable != null) {
                SimpleInformationsComptabilite simpleInformationsComptabilite = CorvusServiceLocator
                        .getSimpleInformationsComptabiliteService().read(idInfoComptable);
                if (!simpleInformationsComptabilite.isNew()) {
                    CorvusServiceLocator.getSimpleInformationsComptabiliteService()
                            .delete(simpleInformationsComptabilite);
                }
            }
        }

    }

    public List<PcaForDecompte> findPcaPrecedante(String dateMax, String dateMin, String idDemande,
                                                  String noVersionDroitCourant)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return PcaPrecedante.findPcaToReplaced(dateMax, dateMin, idDemande, noVersionDroitCourant);
    }

    @Override
    public SimplePlanDeCalcul findSimplePlanCalculeRetenu(String idPCAccordee)
            throws PCAccordeeException, JadePersistenceException {
        SimplePlanDeCalculSearch search = new SimplePlanDeCalculSearch();
        search.setForIdPCAccordee(idPCAccordee);
        search.setForIsPlanRetenu(true);
        ArrayList<SimplePlanDeCalcul> pl;

        pl = searchPlanCalcul(search);
        if (pl.size() > 0) {
            return pl.get(0);
        } else {
            return null;
        }
    }

    @Override
    public CalculMontantRetroActif getCalculMontantRetroActif(String idDemande, String noVersionDroit)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (idDemande == null) {
            throw new PCAccordeeException("Unable to getMapMontantRetroActif, the idDemande is null!");
        }
        if (noVersionDroit == null) {
            throw new PCAccordeeException("Unable to getMapMontantRetroActif, the noVersionDroit is null!");
        }
        CalculMontantRetroActif calculMontantRetroActif;
        try {
            calculMontantRetroActif = new CalculMontantRetroActif(idDemande, noVersionDroit);
        } catch (PmtMensuelException e) {
            throw new PCAccordeeException("Exception With pmtMensuel", e);
        } catch (DecisionException e) {
            throw new PCAccordeeException("Exception With decision", e);
        }
        return calculMontantRetroActif;
    }

    @Override
    public Map<String, String> getMapMontantRetroActif(PCAccordee pcAccordee)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (pcAccordee == null) {
            throw new PCAccordeeException("Unable to getMapMontantRetroActif, the model passed is null!");
        }
        return this.getMapMontantRetroActif(pcAccordee.getSimpleDroit().getIdDemandePC(),
                pcAccordee.getSimpleVersionDroit().getNoVersion());
    }

    @Override
    public Map<String, String> getMapMontantRetroActif(String idDemande, String noVersionDroit)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (idDemande == null) {
            throw new PCAccordeeException("Unable to getMapMontantRetroActif, the idDemande is null!");
        }
        if (noVersionDroit == null) {
            throw new PCAccordeeException("Unable to getMapMontantRetroActif, the noVersionDroit is null!");
        }
        CalculMontantRetroActif calculMontantRetroActif;
        try {
            calculMontantRetroActif = new CalculMontantRetroActif(idDemande, noVersionDroit);
        } catch (PmtMensuelException e) {
            throw new PCAccordeeException("Exception With pmtMensuel", e);
        } catch (DecisionException e) {
            throw new PCAccordeeException("Exception With decision", e);
        }
        return calculMontantRetroActif.getMapMontantRetro();
    }

    @Override
    public Map<String, String> getMapMontantRetroActifBrut(String idDemande, String noVersionDroit)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (idDemande == null) {
            throw new PCAccordeeException("Unable to getMapMontantRetroActif, the idDemande is null!");
        }
        if (noVersionDroit == null) {
            throw new PCAccordeeException("Unable to getMapMontantRetroActif, the noVersionDroit is null!");
        }
        CalculMontantRetroActif calculMontantRetroActif;
        try {
            calculMontantRetroActif = new CalculMontantRetroActif(idDemande, noVersionDroit);
        } catch (PmtMensuelException e) {
            throw new PCAccordeeException("Exception With pmtMensuel", e);
        } catch (DecisionException e) {
            throw new PCAccordeeException("Exception With decision", e);
        }
        return calculMontantRetroActif.getMapMontantRetroBrut();
    }

    @Override
    public void loadJoursAppoint(PCAccordee pcAccordee)
            throws JadePersistenceException, PCAccordeeException, JadeApplicationServiceNotAvailableException {
        // get jours appoint
        SimpleJoursAppointSearch appointSearch = new SimpleJoursAppointSearch();
        appointSearch.setForIdPCAccordee(pcAccordee.getSimplePCAccordee().getIdPCAccordee());
        appointSearch = PegasusImplServiceLocator.getSimpleJoursAppointService().search(appointSearch);
        for (JadeAbstractModel absDonnee : appointSearch.getSearchResults()) {
            pcAccordee.getListeJoursAppoint().add((SimpleJoursAppoint) absDonnee);
        }
    }

    private SimplePlanDeCalcul loadPlanCalculRetenu(String idPca) throws JadePersistenceException {
        SimplePlanDeCalculSearch pcalSearch = new SimplePlanDeCalculSearch();
        pcalSearch.setForIdPCAccordee(idPca);
        pcalSearch.setForIsPlanRetenu(true);
        pcalSearch = (SimplePlanDeCalculSearch) JadePersistenceManager.search(pcalSearch);

        return (SimplePlanDeCalcul) pcalSearch.getSearchResults()[0];
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeService #read(java.lang.String)
     */
    @Override
    public ListPCAccordee read(String idPCAccordee) throws JadePersistenceException, PCAccordeeException {
        if (JadeStringUtil.isEmpty(idPCAccordee)) {
            throw new PCAccordeeException("Unable to read pc accordee, the id passed is null!");
        }
        ListPCAccordee pcAccordee = new ListPCAccordee();
        pcAccordee.setId(idPCAccordee);
        return (ListPCAccordee) JadePersistenceManager.read(pcAccordee);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeService #readDetail(java.lang.String)
     */
    @Override
    public PCAccordee readDetail(String idPCAccordee) throws JadePersistenceException, PCAccordeeException {
        try {
            if (JadeStringUtil.isEmpty(idPCAccordee)) {
                throw new PCAccordeeException("Unable to read pc accordee, the id passed is null!");
            }
            PCAccordee pcAccordee = new PCAccordee();
            pcAccordee.setId(idPCAccordee);
            pcAccordee = (PCAccordee) JadePersistenceManager.read(pcAccordee);

            loadJoursAppoint(pcAccordee);

            return pcAccordee;
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PCAccordeeException("Service not available!", e);
        }
    }

    @Override
    public List<PCAccordee> findPcaForPeriode(List<String> idsPca) throws PCAccordeeException {

        if (idsPca == null || idsPca.size() == 0) {
            throw new PCAccordeeException("The list of id's pca cannot be null or empty!");
        }

        try {
            PCAccordeeSearch search = new PCAccordeeSearch();

            search.setInIdPCAccordee(idsPca);

            return PersistenceUtil.search(search);

        } catch (JadePersistenceException e) {
            throw new PCAccordeeException(e.toString());
        }

    }

    /**
     * Chargement de la pca et on set le plan retenu
     *
     * @param idPCAccordee
     * @return
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     */
    @Override
    public PCAccordee readDetailwithPcal(String idPCAccordee) throws PCAccordeeException, JadePersistenceException {

        ArrayList<SimplePlanDeCalcul> list = new ArrayList<SimplePlanDeCalcul>();
        list.add(loadPlanCalculRetenu(idPCAccordee));

        PCAccordee pcAccordee = readDetail(idPCAccordee);
        pcAccordee.setPlanCalculs(list);

        return pcAccordee;
    }

    @Override
    public PCAccordeeIdMembresRetenusSearch search(PCAccordeeIdMembresRetenusSearch search)
            throws PCAccordeeException, JadePersistenceException {
        if (search == null) {
            throw new PCAccordeeException(
                    "Unable to search pcAccordeeIdMembresRetenusSearch, the model passed is null!");
        }

        return (PCAccordeeIdMembresRetenusSearch) JadePersistenceManager.search(search);
    }

    @Override
    public PCAccordeePlanCalculSearch search(PCAccordeePlanCalculSearch pcAccordeePlanCalculSearch)
            throws PCAccordeeException, JadePersistenceException {
        if (pcAccordeePlanCalculSearch == null) {
            throw new PCAccordeeException("Unable to search pcAccordeePlanCalculSearch, the model passed is null!");
        }
        return (PCAccordeePlanCalculSearch) JadePersistenceManager.search(pcAccordeePlanCalculSearch);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeService
     * #search(ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch)
     */
    @Override
    public PCAccordeeSearch search(PCAccordeeSearch pcAccordeeSearch)
            throws JadePersistenceException, PCAccordeeException {
        if (pcAccordeeSearch == null) {
            throw new PCAccordeeException("Unable to search pcaccordee, the search model passed is null!");
        }
        return (PCAccordeeSearch) JadePersistenceManager.search(pcAccordeeSearch);
    }

    @Override
    public PCAWithCalculMembreFamilleAndPrestationSearch search(
            PCAWithCalculMembreFamilleAndPrestationSearch pcaWithCalculMembreFamilleAndPrestationSearch)
            throws PCAccordeeException, JadePersistenceException {
        if (pcaWithCalculMembreFamilleAndPrestationSearch == null) {
            throw new PCAccordeeException(
                    "Unable to search pcaWithCalculMembreFamilleAndPrestationSearch, the model passed is null!");
        }
        return (PCAWithCalculMembreFamilleAndPrestationSearch) JadePersistenceManager
                .search(pcaWithCalculMembreFamilleAndPrestationSearch);
    }

    @Override
    public PlanDeCalculWitMembreFamilleSearch search(
            PlanDeCalculWitMembreFamilleSearch planDeCalculWitMembreFamilleSearch)
            throws PersonneDansPlanCalculException, JadePersistenceException {
        if (planDeCalculWitMembreFamilleSearch == null) {
            throw new PersonneDansPlanCalculException(
                    "Unable to search planDeCalculWitMembreFamilleSearch, the model passed is null!");
        }
        return (PlanDeCalculWitMembreFamilleSearch) JadePersistenceManager.search(planDeCalculWitMembreFamilleSearch);
    }

    @Override
    public VersionDroitPCAPlanDeCaculeSearch search(VersionDroitPCAPlanDeCaculeSearch versionDroitPCAPlanDeCaculeSearch)
            throws PCAccordeeException, JadePersistenceException {

        if (versionDroitPCAPlanDeCaculeSearch == null) {
            throw new PCAccordeeException(
                    "Unable to search versionDroitPCAPlanDeCacule, the search model passed is null!");
        }

        versionDroitPCAPlanDeCaculeSearch = (VersionDroitPCAPlanDeCaculeSearch) JadePersistenceManager
                .search(versionDroitPCAPlanDeCaculeSearch);
        return versionDroitPCAPlanDeCaculeSearch;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeService
     * #search(ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch)
     */
    @Override
    public ListPCAccordeeSearch searchForList(ListPCAccordeeSearch pcAccordeeSearch)
            throws JadePersistenceException, PCAccordeeException {
        if (pcAccordeeSearch == null) {
            throw new PCAccordeeException("Unable to search pcaccordee, the search model passed is null!");
        }
        // Récupération de toutes les lignes, avec plan de calcul compris
        pcAccordeeSearch = (ListPCAccordeeSearch) JadePersistenceManager.search(pcAccordeeSearch);

        return pcAccordeeSearch;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeService
     * #searchPCAccordeePlanCalculRetenuEnfants(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public List<PCAccordeePlanCalculRetenuEnfantsDansCalculVO> searchPCAccordeePlanCalculRetenuEnfants(String idTiers,
                                                                                                       String dateDebut, String dateFin, String csEtat)
            throws PCAccordeeException, JadePersistenceException, PersonneDansPlanCalculException {

        PCAccordeeSearch pcaSearch = new PCAccordeeSearch();
        pcaSearch.setForIdTiers(idTiers);
        pcaSearch.setForCsEtat(csEtat);
        pcaSearch.setForDateDebut(dateDebut);
        pcaSearch.setForDateFin(dateFin);
        pcaSearch.setWhereKey(PCAccordeeSearch.FOR_SEARCH_WITH_DATE);
        pcaSearch = this.search(pcaSearch);

        List<PCAccordeePlanCalculRetenuEnfantsDansCalculVO> result = new ArrayList<PCAccordeePlanCalculRetenuEnfantsDansCalculVO>();

        for (int i = 0; i < pcaSearch.getSearchResults().length; i++) {
            PCAccordee pca = (PCAccordee) pcaSearch.getSearchResults()[i];
            PCAccordeeVO pcaVO = new PCAccordeeVO(pca.getSimplePCAccordee().getCsGenrePC(),
                    pca.getSimplePCAccordee().getCsTypePC(), pca.getSimplePCAccordee().getDateDebut(),
                    pca.getSimplePCAccordee().getDateFin(), pca.getSimplePCAccordee().getCodeRente(),
                    pca.getSimplePCAccordee().getIsCalculManuel());

            PlanDeCalculVO planDeCalculVO = null;
            List<PersonneDansPlanCalcul> personnesDansCalculList = new ArrayList<PersonneDansPlanCalcul>();

            // recherche du plan de calcul retenu
            SimplePlanDeCalculSearch spdcSearch = new SimplePlanDeCalculSearch();
            spdcSearch.setForIdPCAccordee(pca.getSimplePCAccordee().getIdPCAccordee());
            spdcSearch.setForIsPlanRetenu(Boolean.TRUE);

            try {
                spdcSearch = PegasusImplServiceLocator.getSimplePlanDeCalculService().search(spdcSearch);
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new PCAccordeeException(e.toString());
            }

            if (spdcSearch.getSize() == 1) {
                SimplePlanDeCalcul planDeCalcul = (SimplePlanDeCalcul) spdcSearch.getSearchResults()[0];

                planDeCalculVO = new PlanDeCalculVO(planDeCalcul.getMontantPCMensuelle(),
                        planDeCalcul.getExcedentPCAnnuel(), planDeCalcul.getIsPlanRetenu());

                // recherche des enfants compris dans le plan de calcul retenu
                PersonneDansPlanCalculSearch edcSearch = new PersonneDansPlanCalculSearch();
                edcSearch.setForIdPlanDeCalcul(planDeCalcul.getIdPlanDeCalcul());

                try {
                    edcSearch = PegasusServiceLocator.getEnfantDansCalculService().search(edcSearch);
                } catch (JadeApplicationServiceNotAvailableException e) {
                    throw new PCAccordeeException(e.toString());
                }

                for (int j = 0; j < edcSearch.getSearchResults().length; j++) {
                    if (((PersonneDansPlanCalcul) edcSearch.getSearchResults()[j]).getSimplePersonneDansPlanCalcul()
                            .getIsComprisDansCalcul().booleanValue()) {
                        personnesDansCalculList.add((PersonneDansPlanCalcul) edcSearch.getSearchResults()[j]);

                        // enfantsDansCalcul.add(new EnfantDansCalculVO(edc.getDroitMembreFamille().getMembreFamille()
                        // .getCsCanton(),
                        // edc.getDroitMembreFamille().getMembreFamille().getPersonneEtendue() == null ? "" : edc
                        // .getDroitMembreFamille().getMembreFamille().getPersonneEtendue().getPersonne()
                        // .getEtatCivil(), edc.getDroitMembreFamille().getMembreFamille().getCsNationalite(),
                        // edc.getDroitMembreFamille().getMembreFamille().getCsSexe(), edc.getDroitMembreFamille()
                        // .getMembreFamille().getDateDeces(), edc.getDroitMembreFamille().getMembreFamille()
                        // .getDateNaissance(), edc.getDroitMembreFamille().getMembreFamille().getNom(), edc
                        // .getDroitMembreFamille().getMembreFamille().getPersonneEtendue() == null ? "" : edc
                        // .getDroitMembreFamille().getMembreFamille().getPersonneEtendue()
                        // .getPersonneEtendue().getNumAvsActuel(), edc.getDroitMembreFamille()
                        // .getMembreFamille().getPrenom()));
                    }
                }

            } else if (spdcSearch.getSize() > 1) {
                throw new PCAccordeeException("Plusieurs plan de calcul retenu dans la PC accordee");
            }

            result.add(
                    new PCAccordeePlanCalculRetenuEnfantsDansCalculVO(pcaVO, planDeCalculVO, personnesDansCalculList));
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeService#searchPCAccordeeWithCalculeRetenuVO(java
     * .lang.String, java.lang.String)
     */
    @Override
    public List<PCAccordeePlanCalculAndMembreFamilleVO> searchPCAccordeeWithCalculeRetenuVO(
            String idTiersMembreFamille, String dateValable) throws PCAccordeeException, JadePersistenceException {
        if (idTiersMembreFamille == null) {
            throw new PCAccordeeException("Unable to searchCalculeRetenuVO, the idTiersMembreFamille passed is null!");
        }

        if (dateValable == null) {
            throw new PCAccordeeException("Unable to searchCalculeRetenuVO, the dateValable passed is null!");
        }

        PCAWithCalculMembreFamilleAndPrestationSearch search = new PCAWithCalculMembreFamilleAndPrestationSearch();
        PCAccordeePlanCalculAndMembreFamilleVO pcaccordeePlanCalculAndMembreFamilleVO = null;
        String idPca = null;
        search.setForIdTiersMembreFamille(idTiersMembreFamille);
        search.setWhereKey(
                PCAWithCalculMembreFamilleAndPrestationSearch.WITH_PCA_VALIDE_PLAN_CALCUL_RETENU_AND_DATE_VALABLE);
        search.setIsPlanRetenu(Boolean.TRUE);
        search.setForDateValable(dateValable);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setForCsEtatPcAccordee(IPCPCAccordee.CS_ETAT_PCA_VALIDE);
        search = this.search(search);

        List<PCAccordeePlanCalculAndMembreFamilleVO> listPCA = new ArrayList<PCAccordeePlanCalculAndMembreFamilleVO>();

        for (JadeAbstractModel model : search.getSearchResults()) {
            PCAWithCalculMembreFamilleAndPrestation pca = (PCAWithCalculMembreFamilleAndPrestation) model;

            if (!pca.getSimplePCAccordee().getId().equals(idPca)) {
                if (idPca != null) {
                    listPCA.add(pcaccordeePlanCalculAndMembreFamilleVO);
                }
                pcaccordeePlanCalculAndMembreFamilleVO = new PCAccordeePlanCalculAndMembreFamilleVO();
                pcaccordeePlanCalculAndMembreFamilleVO.setCsGenrePC(pca.getSimplePCAccordee().getCsGenrePC());
                pcaccordeePlanCalculAndMembreFamilleVO.setCsTypePC(pca.getSimplePCAccordee().getCsTypePC());
                pcaccordeePlanCalculAndMembreFamilleVO.setDateDebut(pca.getSimplePCAccordee().getDateDebut());
                pcaccordeePlanCalculAndMembreFamilleVO.setDateFin(pca.getSimplePCAccordee().getDateFin());
                pcaccordeePlanCalculAndMembreFamilleVO.setIdDossier(pca.getIdDossier());

                pcaccordeePlanCalculAndMembreFamilleVO.setExcedentPCAnnuel(
                        pca.getPlanDeCalculWitMembreFamille().getSimplePlanDeCalcul().getExcedentPCAnnuel());

                pcaccordeePlanCalculAndMembreFamilleVO.setMontantPCMensuelle(
                        pca.getPlanDeCalculWitMembreFamille().getSimplePlanDeCalcul().getMontantPCMensuelle());
                pcaccordeePlanCalculAndMembreFamilleVO.setMontantPrimeMoyenAssMaladie(
                        pca.getPlanDeCalculWitMembreFamille().getSimplePlanDeCalcul().getPrimeMoyenneAssMaladie());

                pcaccordeePlanCalculAndMembreFamilleVO
                        .setCsEtatPC(pca.getPlanDeCalculWitMembreFamille().getSimplePlanDeCalcul().getEtatPC());

                pcaccordeePlanCalculAndMembreFamilleVO
                        .setIdTiersBeneficiair(pca.getSimplePrestationsAccordees().getIdTiersBeneficiaire());

                pcaccordeePlanCalculAndMembreFamilleVO.setIdPcAccordee(pca.getSimplePCAccordee().getId());
                pcaccordeePlanCalculAndMembreFamilleVO
                        .setIdVersionDroitPC(pca.getSimplePCAccordee().getIdVersionDroit());
                pcaccordeePlanCalculAndMembreFamilleVO.setReforme(pca.getPlanDeCalculWitMembreFamille().getSimplePlanDeCalcul().getReformePc());
            }

            PCAMembreFamilleVO membreFamilleVO = new PCAMembreFamilleVO();
            membreFamilleVO.setCsRoleFamillePC(pca.getPlanDeCalculWitMembreFamille().getDroitMembreFamille()
                    .getSimpleDroitMembreFamille().getCsRoleFamillePC());
            membreFamilleVO.setIdTiers(pca.getPlanDeCalculWitMembreFamille().getDroitMembreFamille().getMembreFamille()
                    .getSimpleMembreFamille().getIdTiers());
            membreFamilleVO.setNss(pca.getPlanDeCalculWitMembreFamille().getDroitMembreFamille().getMembreFamille()
                    .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());
            membreFamilleVO
                    .setNom(pca.getPlanDeCalculWitMembreFamille().getDroitMembreFamille().getMembreFamille().getNom());
            membreFamilleVO.setPrenom(
                    pca.getPlanDeCalculWitMembreFamille().getDroitMembreFamille().getMembreFamille().getPrenom());
            membreFamilleVO.setDateNaissance(pca.getPlanDeCalculWitMembreFamille().getDroitMembreFamille()
                    .getMembreFamille().getDateNaissance());
            membreFamilleVO.setCsNationalite(pca.getPlanDeCalculWitMembreFamille().getDroitMembreFamille()
                    .getMembreFamille().getCsNationalite());
            membreFamilleVO.setCsSexe(
                    pca.getPlanDeCalculWitMembreFamille().getDroitMembreFamille().getMembreFamille().getCsSexe());
            membreFamilleVO.setIsComprisDansCalcul(
                    pca.getPlanDeCalculWitMembreFamille().getSimplePersonneDansPlanCalcul().getIsComprisDansCalcul());
            membreFamilleVO.setIsRentier(pca.getPlanDeCalculWitMembreFamille().getSimplePersonneDansPlanCalcul().getIsRentier());
            pcaccordeePlanCalculAndMembreFamilleVO.getListMembreFamilleVO().add(membreFamilleVO);
            idPca = pca.getId();
        }

        if (null != pcaccordeePlanCalculAndMembreFamilleVO) {
            listPCA.add(pcaccordeePlanCalculAndMembreFamilleVO);
        }

        return listPCA;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeService#searchPCAIdMembreFamilleRetenuSearch(
     * ch.globaz.pegasus.business.models.pcaccordee.PCAIdMembreFamilleRetenuSearch)
     */
    @Override
    public PCAIdMembreFamilleRetenuSearch searchPCAIdMembreFamilleRetenuSearch(
            PCAIdMembreFamilleRetenuSearch pcaIdMembreFamilleRetenuSearch)
            throws PCAccordeeException, JadePersistenceException {

        if (pcaIdMembreFamilleRetenuSearch == null) {
            throw new PCAccordeeException("Unable to search PCAIdMembreFamilleRetenuSearch, the model passed is null!");
        }
        return (PCAIdMembreFamilleRetenuSearch) JadePersistenceManager.search(pcaIdMembreFamilleRetenuSearch);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeService
     * #search(ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch)
     */
    // public void searchPlanCalcul(String idTiers)
    // throws JadePersistenceException, PCAccordeeException {
    //
    // PCAccordeeSearch pcaSearch = new PCAccordeeSearch();
    // pcaSearch.setForIdTiers(idTiers);
    // pcaSearch = this.search(pcaSearch);
    // for (int i = 0; i < pcaSearch.getSearchResults().length; i++) {
    // PCAccordee pca = (PCAccordee) pcaSearch.getSearchResults()[i];
    // // String NomPrenom = pca.
    // }
    // }
    @Override
    public ArrayList<SimplePlanDeCalcul> searchPlanCalcul(SimplePlanDeCalculSearch search)
            throws JadePersistenceException, PCAccordeeException {

        // SimplePlanDeCalculSearch search = new SimplePlanDeCalculSearch();
        // search.setForIdPCAccordee(idPCA);

        if (search == null) {
            throw new PCAccordeeException("Unable to search for PlanDeCalcul, the search model passed is null!!");
        }

        // liste pour depot des plan de calculs
        ArrayList<SimplePlanDeCalcul> listSearchForReturn = new ArrayList<SimplePlanDeCalcul>();
        try {
            search = PegasusImplServiceLocator.getSimplePlanDeCalculService().search(search);

            // Iteration sur touts les resultats
            for (JadeAbstractModel searchPCAL : search.getSearchResults()) {

                listSearchForReturn.add((SimplePlanDeCalcul) searchPCAL);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return listSearchForReturn;
    }

    @Override
    public PCAccordee update(PCAccordee pcAccordee) throws JadePersistenceException, PCAccordeeException {
        if (pcAccordee == null) {
            throw new PCAccordeeException("Unable to update PCAccordee, the model passed is null!");
        }

        try {

            try {
                String idInfoCompta = pcAccordee.getSimplePrestationsAccordees().getIdInfoCompta();

                pcAccordee.setSimpleInformationsComptabilite(
                        (CorvusServiceLocator.getSimpleInformationsComptabiliteService()
                                .update(pcAccordee.getSimpleInformationsComptabilite())));

                // Si conjoint (DOM2Rentes)
                // on fait ce test pour ne pas updater 2 fois l'info comptat, car on aurais un message d'erreur car le
                // spy change
                if (!pcAccordee.getSimpleInformationsComptabiliteConjoint().isNew() && !(idInfoCompta
                        .equals(pcAccordee.getSimplePrestationsAccordeesConjoint().getIdInfoCompta()))) {
                    pcAccordee.setSimpleInformationsComptabiliteConjoint(
                            (CorvusServiceLocator.getSimpleInformationsComptabiliteService()
                                    .update(pcAccordee.getSimpleInformationsComptabiliteConjoint())));
                }

            } catch (RentesAccordeesException e) {
                throw new PCAccordeeException("Unable to update the informationComtable", e);
            }

            pcAccordee.setSimplePCAccordee(
                    PegasusImplServiceLocator.getSimplePCAccordeeService().update(pcAccordee.getSimplePCAccordee()));

            try {
                pcAccordee.setSimplePrestationsAccordees(PegasusImplServiceLocator.getSimplePrestatioAccordeeService()
                        .update(pcAccordee.getSimplePrestationsAccordees()));
                // Si conjoint (DOM2Rentes)
                if (!JadeStringUtil
                        .isBlankOrZero(pcAccordee.getSimplePrestationsAccordeesConjoint().getIdPrestationAccordee())) {
                    pcAccordee.setSimplePrestationsAccordeesConjoint(
                            PegasusImplServiceLocator.getSimplePrestatioAccordeeService()
                                    .update(pcAccordee.getSimplePrestationsAccordeesConjoint()));
                }

            } catch (JadeApplicationException e) {
                throw new PCAccordeeException("Unable to update the prestationsAccordee", e);
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PCAccordeeException("Service not available - " + e.getMessage());
        }

        return pcAccordee;
    }

    @Override
    public SimplePlanDeCalcul updatePlanDecalculeRetenu(String idPlanDeCalcul)
            throws JadePersistenceException, PCAccordeeException {

        if (JadeStringUtil.isEmpty(idPlanDeCalcul)) {
            throw new PCAccordeeException("Unable to updatePlanDecalculeRetenu, the idPlanDeCalcul passed is null!");
        }
        SimplePlanDeCalcul simplePlanDeCalcul = null;

        try {
            simplePlanDeCalcul = PegasusImplServiceLocator.getSimplePlanDeCalculService().read(idPlanDeCalcul);
            SimplePlanDeCalculSearch simplePlanDeCalculSearch = new SimplePlanDeCalculSearch();
            simplePlanDeCalculSearch.setForIsPlanRetenu(true);
            simplePlanDeCalculSearch.setForIdPCAccordee(simplePlanDeCalcul.getIdPCAccordee());
            simplePlanDeCalculSearch = PegasusImplServiceLocator.getSimplePlanDeCalculService().search(simplePlanDeCalculSearch);
            PCAccordee pcAccordee = PegasusServiceLocator.getPCAccordeeService().readDetail(simplePlanDeCalcul.getIdPCAccordee());
            if (simplePlanDeCalculSearch.getSize() > 0) {
                if (simplePlanDeCalculSearch.getSize() == 1) {

                    try {
                        if (DroitChecker.isDeletable(pcAccordee.getSimpleVersionDroit())) {
                            PCAccordeeSearch pcAccordeeSearch = new PCAccordeeSearch();
                            pcAccordeeSearch.setForVersionDroit(pcAccordee.getSimpleVersionDroit().getIdVersionDroit());
                            PegasusImplServiceLocator.getPCAccordeeService().search(pcAccordeeSearch);
                            if (pcAccordeeSearch.getSize() > 0) {
                                for (JadeAbstractModel d : pcAccordeeSearch.getSearchResults()) {
                                    PCAccordee pcAccordee1 = (PCAccordee) d;
                                    /**
                                     * Supprimer les répartitions des créances si c'est pas des versement homes
                                     * Sinon recalculer la répartition selon les nouveaux montants.
                                     */
                                    try {
                                        CreanceAccordeeSearch search = new CreanceAccordeeSearch();
                                        search.setForIdPCAccordee(pcAccordee1.getId());
                                        search = PegasusServiceLocator.getCreanceAccordeeService().search(search);
                                        for (JadeAbstractModel abstractModel : search.getSearchResults()) {
                                            CreanceAccordee creanceAccordee = (CreanceAccordee) abstractModel;
                                            if (creanceAccordee.getSimpleCreancier().getIsHome()) {
                                                if(simplePlanDeCalcul.getIdPCAccordee().equals(pcAccordee1.getSimplePCAccordee().getIdPCAccordee())){
                                                    creanceAccordee = recalculMontant(creanceAccordee,pcAccordee,simplePlanDeCalcul);
                                                    creanceAccordee.setSimplePCAccordee(pcAccordee.getSimplePCAccordee());
                                                    PegasusServiceLocator.getCreanceAccordeeService().update(creanceAccordee);
                                                }
                                            }else{
                                                deleteCreanceById(creanceAccordee.getId());
                                            }
                                        }
                                    } catch (Exception e) {
                                        throw new PCAccordeeException(e.getMessage());
                                    }
                                }
                            }
                            try {
                                PegasusImplServiceLocator.getCleanDecisionService()
                                        .deleteDecisionsApresCalculForVersion(
                                                pcAccordee.getSimpleVersionDroit().getIdVersionDroit());
                            } catch (DecisionException e) {
                                throw new PCAccordeeException("Unable to delete decision", e);
                            }
                        }
                    } catch (DroitException e) {
                        throw new PCAccordeeException("Unable to delete creance", e);
                    }

                    SimplePlanDeCalcul simplePlanDeCalculOld = (SimplePlanDeCalcul) simplePlanDeCalculSearch
                            .getSearchResults()[0];
                    simplePlanDeCalculOld = PegasusImplServiceLocator.getSimplePlanDeCalculService()
                            .read(simplePlanDeCalculOld.getId());
                    simplePlanDeCalculOld.setIsPlanRetenu(false);
                    PegasusImplServiceLocator.getSimplePlanDeCalculService().update(simplePlanDeCalculOld);

                    simplePlanDeCalcul.setIsPlanRetenu(true);
                    PegasusImplServiceLocator.getSimplePlanDeCalculService().update(simplePlanDeCalcul);

                    // On met à jour le plan de calcul non retenu si nécessaire.
                    updatePlanDeCalculNonRetenu(simplePlanDeCalcul.getIdPCAccordee(), simplePlanDeCalcul.getReformePc());

                    try {
                        if (pcAccordee.getSimplePrestationsAccordeesConjoint() != null && !JadeStringUtil
                                .isBlankOrZero(pcAccordee.getSimplePCAccordee().getIdPrestationAccordeeConjoint())) {
                            BigDecimal[] montants = ComptabilisationUtil
                                    .splitMontant(new BigDecimal(simplePlanDeCalcul.getMontantPCMensuelle()));
                            SimplePrestationsAccordees simplePrestationsAccordees = pcAccordee
                                    .getSimplePrestationsAccordees();
                            simplePrestationsAccordees.setMontantPrestation(montants[0].toString());
                            PegasusImplServiceLocator.getSimplePrestatioAccordeeService()
                                    .update(simplePrestationsAccordees);
                            pcAccordee.getSimplePrestationsAccordeesConjoint()
                                    .setMontantPrestation(montants[1].toString());
                            PegasusImplServiceLocator.getSimplePrestatioAccordeeService()
                                    .update(pcAccordee.getSimplePrestationsAccordeesConjoint());
                        } else {
                            SimplePrestationsAccordees simplePrestationsAccordees = pcAccordee
                                    .getSimplePrestationsAccordees();
                            simplePrestationsAccordees.setMontantPrestation(simplePlanDeCalcul.getMontantPCMensuelle());
                            PegasusImplServiceLocator.getSimplePrestatioAccordeeService()
                                    .update(simplePrestationsAccordees);
                        }
                    } catch (JadeApplicationException e) {
                        throw new PCAccordeeException("Unable to update the prestationAccordee", e);
                    }

                } else {
                    throw new PCAccordeeException("Unable to update the planDeCalcule too many vlaues");
                }

            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PCAccordeeException("Service not available - " + e.getMessage());
        }
        return simplePlanDeCalcul;
    }

    private CreanceAccordee recalculMontant(CreanceAccordee creanceAccordee, PCAccordee pcaNew, SimplePlanDeCalcul simplePlanDeCalcul) throws Exception{
        Float montantMensuelHomeNew =  Float.parseFloat(simplePlanDeCalcul.getMontantPrixHome()) / 12.0f;

        Float montantPCMensuel = Float.parseFloat(simplePlanDeCalcul.getMontantPCMensuelle());
        String byteArrayToString = null;
        byteArrayToString = new String(simplePlanDeCalcul.getResultatCalcul());
        TupleDonneeRapport tupleRoot = PegasusImplServiceLocator.getCalculPersistanceService().deserialiseDonneesCcXML(
                byteArrayToString);
        Float montantDepensesPersonnel = tupleRoot.getValeurEnfant(CLE_DEPEN_DEPPERSO_TOTAL)/ 12.0f;
        if(montantMensuelHomeNew == 0.f){
            montantMensuelHomeNew = tupleRoot.getValeurEnfant(CLE_DEPEN_SEJOUR_MOIS_PARTIEL_TOTAL)/ 12.0f;
        }
        String dateDebut = "";
        String dateFin = "";
        if (JadeStringUtil.isBlankOrZero(pcaNew.getSimplePCAccordee().getDateFin())) {
            dateFin = JadeDateUtil.getLastDateOfMonth(PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt());
        } else {
            dateFin = JadeDateUtil.getLastDateOfMonth(pcaNew.getSimplePCAccordee().getDateFin());
        }
        dateDebut = JadeDateUtil.getFirstDateOfMonth(pcaNew.getSimplePCAccordee().getDateDebut());
        int nbreMois = JadeDateUtil.getNbMonthsBetween(dateDebut, dateFin);
        Float montantAverser = 0.f;
        if (montantMensuelHomeNew + montantDepensesPersonnel > montantPCMensuel) {
            montantAverser = montantPCMensuel - (montantDepensesPersonnel);
            montantAverser = montantAverser * nbreMois;
            montantAverser = new BigDecimal(montantAverser).setScale(0, RoundingMode.UP).floatValue();
        } else {
            montantAverser = new BigDecimal(montantAverser).setScale(0, RoundingMode.UP).floatValue();
            if (montantAverser.floatValue() + montantDepensesPersonnel > montantPCMensuel) {
                montantAverser = montantAverser - 1;
            }
            montantAverser = montantAverser * nbreMois;
        }
        SimpleCreanceAccordee simpleCreanceAccordee = creanceAccordee.getSimpleCreanceAccordee();
        simpleCreanceAccordee.setMontant(montantAverser.toString());
        creanceAccordee.setSimpleCreanceAccordee(simpleCreanceAccordee);
        return creanceAccordee;

    }

    /**
     * Mise à jour du plan de calcul non retenu.
     *
     * @param idPCAccordee : id de la PCA
     * @param isReforme : vrai si on est sur un calcul réforme
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     */
    private void updatePlanDeCalculNonRetenu(String idPCAccordee, boolean isReforme) throws JadeApplicationServiceNotAvailableException, PCAccordeeException, JadePersistenceException {
        SimplePlanDeCalculSearch simplePlanDeCalculSearch = new SimplePlanDeCalculSearch();
        simplePlanDeCalculSearch.setForIsPlanNonRetenu(true);
        simplePlanDeCalculSearch.setForIdPCAccordee(idPCAccordee);
        simplePlanDeCalculSearch = PegasusImplServiceLocator.getSimplePlanDeCalculService().search(simplePlanDeCalculSearch);
        if (simplePlanDeCalculSearch.getSize() == 1) {
            SimplePlanDeCalcul oldPlanDeCalculNonRetenu = (SimplePlanDeCalcul) simplePlanDeCalculSearch.getSearchResults()[0];
            // on met à jour l'ancien plan de calcul non retenu.
            oldPlanDeCalculNonRetenu = PegasusImplServiceLocator.getSimplePlanDeCalculService().read(oldPlanDeCalculNonRetenu.getId());
            oldPlanDeCalculNonRetenu.setIsPlanNonRetenu(false);
            PegasusImplServiceLocator.getSimplePlanDeCalculService().update(oldPlanDeCalculNonRetenu);
            SimplePlanDeCalculSearch simplePlanDeCalculNonRetenuSearch = new SimplePlanDeCalculSearch();
            simplePlanDeCalculNonRetenuSearch.setForIdPCAccordee(idPCAccordee);
            simplePlanDeCalculNonRetenuSearch.setForReformePC(!isReforme);
            simplePlanDeCalculNonRetenuSearch = PegasusImplServiceLocator.getSimplePlanDeCalculService().search(simplePlanDeCalculNonRetenuSearch);
            if (simplePlanDeCalculNonRetenuSearch.getSize() > 0){
                // on met à jour le nouveau plan de calcul non retenu.
                SimplePlanDeCalcul newPlanDeCalculNonRetenu = (SimplePlanDeCalcul) simplePlanDeCalculNonRetenuSearch.getSearchResults()[0];
                newPlanDeCalculNonRetenu = PegasusImplServiceLocator.getSimplePlanDeCalculService().read(newPlanDeCalculNonRetenu.getId());
                newPlanDeCalculNonRetenu.setIsPlanNonRetenu(true);
                PegasusImplServiceLocator.getSimplePlanDeCalculService().update(newPlanDeCalculNonRetenu);
            }
        }
    }

    @Override
    public Map<String, List<Map<String, Object>>> xxVisuel(String idDossier)
            throws JadePersistenceException, PCAccordeeException {

        // ListPCAccordeeSearch pcAccordeeSearch = new ListPCAccordeeSearch();
        // pcAccordeeSearch.setForIdDossier(idDossier);
        // pcAccordeeSearch = this.searchForList(pcAccordeeSearch);
        List<Map<String, Object>> lisPca = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> listDemande = new ArrayList<Map<String, Object>>();

        Map<String, SimpleDemande> mapDemande = new HashMap<String, SimpleDemande>();

        DemandePcaPersonneDansCalSearch demandePcaPersonneDansCalSearch = new DemandePcaPersonneDansCalSearch();
        demandePcaPersonneDansCalSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        demandePcaPersonneDansCalSearch.setForIdDossier(idDossier);
        List<DemandePcaPersonneDansCal> list2 = PersistenceUtil.search(demandePcaPersonneDansCalSearch,
                demandePcaPersonneDansCalSearch.whichModelClass());

        // Regroupement des pca avec le personne comprise dans le calcule
        Map<String, List<DemandePcaPersonneDansCal>> mapPCA = JadeListUtil.groupBy(list2,
                new JadeListUtil.Key<DemandePcaPersonneDansCal>() {
                    @Override
                    public String exec(DemandePcaPersonneDansCal e) {
                        return e.getSimpleDemande().getIdDemande() + "_" + e.getSimpleVersionDroit().getNoVersion()
                                + "_" + e.getSimplePCAccordee().getDateDebut() + "_"
                                + e.getSimplePCAccordee().getDateFin();
                    }
                });

        for (Entry<String, List<DemandePcaPersonneDansCal>> entry1 : mapPCA.entrySet()) {
            Set<PersonneEtendueComplexModel> personnesDansCalRequerant = new LinkedHashSet<PersonneEtendueComplexModel>();
            Set<PersonneEtendueComplexModel> personnesDansCalConjoint = new LinkedHashSet<PersonneEtendueComplexModel>();

            DemandePcaPersonneDansCal pcaConjoint = null;
            DemandePcaPersonneDansCal pcaRequerant = null;

            for (DemandePcaPersonneDansCal pca : entry1.getValue()) {
                if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(pca.getSimplePCAccordee().getCsRoleBeneficiaire())) {
                    pcaRequerant = pca;
                    personnesDansCalRequerant.add(pca.getMembreFamille().getMembreFamille().getPersonneEtendue());
                } else if (IPCDroits.CS_ROLE_FAMILLE_CONJOINT
                        .equals(pca.getSimplePCAccordee().getCsRoleBeneficiaire())) {
                    pcaConjoint = pca;
                    personnesDansCalConjoint.add(pca.getMembreFamille().getMembreFamille().getPersonneEtendue());
                }
            }

            Map<String, Object> map = new HashMap<String, Object>();
            pcaRequerant.getSimplePCAccordee().getDateDebut();
            pcaRequerant.getSimplePCAccordee().getDateDebut();
            Calendar calDatFin = null;
            String dateFin = null;
            int lastDay = 0;

            if (!JadeStringUtil.isBlankOrZero(pcaRequerant.getSimplePCAccordee().getDateFin())) {
                calDatFin = JadeDateUtil.getGlobazCalendar("01." + pcaRequerant.getSimplePCAccordee().getDateFin());
                lastDay = calDatFin.getActualMaximum(Calendar.DAY_OF_MONTH);
                dateFin = lastDay + "." + pcaRequerant.getSimplePCAccordee().getDateFin();
            } else {
                map.put("courant", true);
                if (JadeStringUtil.isBlankOrZero(pcaRequerant.getSimpleDemande().getDateFin())) {
                    if (JadeDateUtil.isDateMonthYearBefore(pcaRequerant.getSimplePCAccordee().getDateDebut(),
                            dateFin)) {
                        calDatFin = JadeDateUtil
                                .getGlobazCalendar("01." + pcaRequerant.getSimplePCAccordee().getDateDebut());
                        lastDay = calDatFin.getActualMaximum(Calendar.DAY_OF_MONTH);
                        dateFin = lastDay + "." + pcaRequerant.getSimplePCAccordee().getDateDebut();
                    } else {
                        calDatFin = JadeDateUtil.getGlobazCalendar(JACalendar.todayJJsMMsAAAA());
                        lastDay = calDatFin.getActualMaximum(Calendar.DAY_OF_MONTH);
                        dateFin = lastDay + "." + JACalendar.todayJJsMMsAAAA().substring(3);
                    }
                } else {
                    calDatFin = JadeDateUtil.getGlobazCalendar("0." + pcaRequerant.getSimpleDemande().getDateFin());
                    lastDay = calDatFin.getActualMaximum(Calendar.DAY_OF_MONTH);
                    dateFin = lastDay + "." + pcaRequerant.getSimpleDemande().getDateFin();
                }
            }
            if (pcaConjoint != null) {
                Map<String, Object> mapConjoint = new HashMap<String, Object>();

                mapConjoint.put("montant", pcaConjoint.getSimplePlanDeCalcul().getMontantPCMensuelle());
                mapConjoint.put("etat", pcaConjoint.getSimplePCAccordee().getCsEtatPC());
                mapConjoint.put("labelEtat", BSessionUtil.getSessionFromThreadContext()
                        .getCodeLibelle(pcaConjoint.getSimplePCAccordee().getCsEtatPC()));
                mapConjoint.put("labelGenre", BSessionUtil.getSessionFromThreadContext()
                        .getCodeLibelle(pcaConjoint.getSimplePCAccordee().getCsGenrePC()));
                mapConjoint.put("labelType", BSessionUtil.getSessionFromThreadContext()
                        .getCodeLibelle(pcaConjoint.getSimplePCAccordee().getCsTypePC()));
                mapConjoint.put("csRoleBeneficiaire", pcaConjoint.getSimplePCAccordee().getCsRoleBeneficiaire());
                mapConjoint.put("labelRoleBeneficiaire", BSessionUtil.getSessionFromThreadContext()
                        .getCodeLibelle(pcaConjoint.getSimplePCAccordee().getCsRoleBeneficiaire()));
                map.put("conjoint", mapConjoint);
                map.put("personneDansCal", personnesDansCalConjoint);
            }

            map.put("version", pcaRequerant.getSimpleVersionDroit().getNoVersion());
            map.put("dateDebut", "01." + pcaRequerant.getSimplePCAccordee().getDateDebut());
            map.put("dateFin", dateFin);
            map.put("idDemande", pcaRequerant.getSimpleDemande().getIdDemande());

            map.put("montant", pcaRequerant.getSimplePlanDeCalcul().getMontantPCMensuelle());
            map.put("etat", pcaRequerant.getSimplePCAccordee().getCsEtatPC());
            map.put("labelEtat", BSessionUtil.getSessionFromThreadContext()
                    .getCodeLibelle(pcaRequerant.getSimplePCAccordee().getCsEtatPC()));
            map.put("labelGenre", BSessionUtil.getSessionFromThreadContext()
                    .getCodeLibelle(pcaRequerant.getSimplePCAccordee().getCsGenrePC()));
            map.put("labelType", BSessionUtil.getSessionFromThreadContext()
                    .getCodeLibelle(pcaRequerant.getSimplePCAccordee().getCsTypePC()));
            map.put("csRoleBeneficiaire", pcaRequerant.getSimplePCAccordee().getCsRoleBeneficiaire());
            map.put("labelRoleBeneficiaire", BSessionUtil.getSessionFromThreadContext()
                    .getCodeLibelle(pcaRequerant.getSimplePCAccordee().getCsRoleBeneficiaire()));
            map.put("idPcaParent", pcaRequerant.getSimplePCAccordee().getIdPcaParent());
            map.put("etatPlanCalPC", pcaRequerant.getSimplePlanDeCalcul().getEtatPC());
            map.put("labelEtatPlanCalPC", BSessionUtil.getSessionFromThreadContext()
                    .getCodeLibelle(pcaRequerant.getSimplePlanDeCalcul().getEtatPC()));

            if (!mapDemande.containsKey(pcaRequerant.getSimpleDemande().getIdDemande())) {
                mapDemande.put(pcaRequerant.getSimpleDemande().getIdDemande(), pcaRequerant.getSimpleDemande());
            }
            map.put("personneDansCal", personnesDansCalRequerant);
            lisPca.add(map);

        }

        for (SimpleDemande de : mapDemande.values()) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (!JadeStringUtil.isBlankOrZero(de.getDateDebut())) {
                Calendar calDatFin = null;
                map.put("dateDebut", "01." + de.getDateDebut());
                String dateFin = null;
                if (!JadeStringUtil.isBlankOrZero(de.getDateFin())) {
                    calDatFin = JadeDateUtil.getGlobazCalendar("01." + de.getDateFin());
                    int lastDay = calDatFin.getActualMaximum(Calendar.DAY_OF_MONTH);
                    dateFin = lastDay + "." + de.getDateFin();
                } else {
                    calDatFin = JadeDateUtil.getGlobazCalendar(JACalendar.todayJJsMMsAAAA());
                    int lastDay = calDatFin.getActualMaximum(Calendar.DAY_OF_MONTH);
                    dateFin = lastDay + "." + JACalendar.todayJJsMMsAAAA().substring(3);
                    map.put("courant", true);
                }
                map.put("dateFin", dateFin);
                map.put("idDemande", de.getIdDemande());
                map.put("etat", de.getCsEtatDemande());

                map.put("labelEtat", BSessionUtil.getSessionFromThreadContext().getCodeLibelle(de.getCsEtatDemande()));
                map.put("title", de.getIdDemande());
                map.put("dateDepot", de.getDateDepot());
                map.put("gestionnaire", de.getIdGestionnaire());
                map.put("isFratrie", de.getIsFratrie());
                map.put("dateProchaineRevision", de.getDateProchaineRevision());
                map.put("typeDemande", BSessionUtil.getSessionFromThreadContext().getCodeLibelle(de.getTypeDemande()));

                listDemande.add(map);
            }
        }

        Map<String, List<Map<String, Object>>> returnMap = new HashMap<String, List<Map<String, Object>>>();

        returnMap.put("pcas", lisPca);
        returnMap.put("demandes", listDemande);

        return returnMap;
    }

    @Override
    public int count(PCAccordeePlanCalculSearch search) throws PCAccordeeException, JadePersistenceException {
        if (search == null) {
            throw new PCAccordeeException("Unable to count pcAccordee, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public PCAccordee findLastestPca(String idDemande) throws PCAccordeeException, JadePersistenceException {
        PCAccordeeSearch search = new PCAccordeeSearch();
        search.setWhereKey(PCAccordeeSearch.FOR_PCA_WITH_DATE_MAX);
        search.setForIdDemande(idDemande);
        search = this.search(search);
        PCAccordee pca = null;
        if (search.getSize() > 0) {
            pca = (PCAccordee) search.getSearchResults()[0];
        }
        return pca;
    }

    @Override
    public BigDecimal calculerMontantRestitution(String idDroit, String numVersionDroit, String dateSuppressionDroit)
            throws OrdreVersementException, PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, PmtMensuelException {
        String dateDernierPmt = PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();

        PCAccordeeSearch oldPcaSearch = new PCAccordeeSearch();
        oldPcaSearch.setForIdDroit(idDroit);
        oldPcaSearch.setForDateValable(dateSuppressionDroit);
        oldPcaSearch.setForNoVersionDroit(numVersionDroit);
        oldPcaSearch.setWhereKey(PCAccordeeSearch.FOR_PCA_REPLACEC_BY_DECISION_SUPPRESSION);
        oldPcaSearch.setOrderKey("forDateDebutAsc");
        oldPcaSearch = PegasusServiceLocator.getPCAccordeeService().search(oldPcaSearch);

        List<PCAccordee> pcas = PersistenceUtil.typeSearch(oldPcaSearch);
        GenerateOvsForSuppression generateOvs = new GenerateOvsForSuppression(dateSuppressionDroit, dateDernierPmt);
        generateOvs.generateOv(pcas);
        BigDecimal montantTotalRestitution = generateOvs.getMontantTotalRestitution();

        List<SimpleJoursAppoint> listeJoursAppoint = new ArrayList<SimpleJoursAppoint>();
        for (PCAccordee pca : pcas) {
            if (pca.getSimplePCAccordee().getHasJoursAppoint()) {
                SimpleJoursAppointSearch search = new SimpleJoursAppointSearch();
                search.setForIdPCAccordee(pca.getId());
                search = PegasusImplServiceLocator.getSimpleJoursAppointService().search(search);
                listeJoursAppoint = PersistenceUtil.typeSearch(search, search.whichModelClass());

                JADate dateDebutPCHome = null;
                JADate dateSupression = null;
                try {
                    dateDebutPCHome = new JADate(pca.getSimplePCAccordee().getDateDebut());
                    dateSupression = new JADate(dateSuppressionDroit);
                } catch (JAException e) {
                    throw new PCAccordeeException("Service not available - " + e.getMessage());
                }
                JACalendarGregorian cal = new JACalendarGregorian();
                if (cal.compare(dateSupression, dateDebutPCHome) == JACalendar.COMPARE_FIRSTLOWER) {
                    montantTotalRestitution = montantTotalRestitution
                            .add(new BigDecimal(listeJoursAppoint.get(0).getMontantTotal()));
                }

            }

        }

        return montantTotalRestitution;
    }
}
