/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.lots.IRELot;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ch.globaz.common.domaine.GroupePeriodes;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordeesSearch;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.jade.process.business.enumProcess.JadeProcessStepStateEnum;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.annonce.SimpleCommunicationOCCSearch;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.decision.ForDeleteDecisionSearch;
import ch.globaz.pegasus.business.models.decision.ListDecisions;
import ch.globaz.pegasus.business.models.decision.ListDecisionsSearch;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeaderSearch;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionSuppression;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionSuppressionSearch;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.droit.VersionDroit;
import ch.globaz.pegasus.business.models.droit.VersionDroitSearch;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersementSearch;
import ch.globaz.pegasus.business.models.lot.SimplePrestation;
import ch.globaz.pegasus.business.models.lot.SimplePrestationSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeePlanCalculSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordeeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.decision.DecisionService;
import ch.globaz.pegasus.business.vo.decision.DecisionPcVO;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.EtatDemandeResolver;
import ch.globaz.pegasus.businessimpl.services.models.pcaccordee.PcaPrecedante;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.queryexec.bridge.jade.SCM;
import ch.globaz.utils.periode.GroupePeriodesResolver;
import ch.globaz.utils.periode.GroupePeriodesResolver.EachPeriode;

/**
 * @author SCE 22 sept. 2010
 */
public class DecisionServiceImpl extends PegasusAbstractServiceImpl implements DecisionService {

    public static String PREFIX_DATE_DEBUT_VALIDATION_DECISION_PC = "01.12.";
    public static String PREFIX_DATE_FIN_VALIDATION_DECISION_PC = "31.12.";

    private void devalidationVersionCourante(String idVersionDroit, SimplePCAccordeeSearch simplePCAccordeeSearch,
            SimpleDecisionSuppressionSearch simpleDecisionSuppressionSearch) throws PrestationException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException, OrdreVersementException,
            DroitException, PCAccordeeException, JadeApplicationException, DecisionException {

        boolean isDecisionSuppression = false;

        //
        // recherche des prestations de la version du droit
        SimplePrestationSearch simplePrestationSearch = new SimplePrestationSearch();
        simplePrestationSearch.setForIdVersionDroit(idVersionDroit);
        simplePrestationSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        simplePrestationSearch = PegasusImplServiceLocator.getSimplePrestationService().search(simplePrestationSearch);
        List<String> listeIdPrestations = new ArrayList<String>();
        List<String> listeIdDecisionHeader = new ArrayList<String>();

        for (JadeAbstractModel absModel : simplePrestationSearch.getSearchResults()) {

            SimplePrestation prestation = (SimplePrestation) absModel;
            listeIdPrestations.add(prestation.getIdPrestation());
            // listeIdDecisionHeader.add(prestation.getIdDecisionHeader());
        }

        if (simplePrestationSearch.getSize() > 0) {
            // supprime les ordres de versement
            SimpleOrdreVersementSearch OVSearch = new SimpleOrdreVersementSearch();
            OVSearch.setForInIdPrestation(listeIdPrestations);
            OVSearch.setWhereKey(SimpleOrdreVersementSearch.SUPPRESSION_WHERE_KEY);
            OVSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            PegasusImplServiceLocator.getSimpleOrdreVersementService().delete(OVSearch);

            // supprime les prestations
            PegasusImplServiceLocator.getSimplePrestationService().delete(simplePrestationSearch);
        }

        SimpleVersionDroit simpleVersionDroit = PegasusImplServiceLocator.getSimpleVersionDroitService().read(
                idVersionDroit);

        // modifie PCA validées -> état calculé
        List<String> listeIdPrestationsAccordees = new ArrayList<String>();
        // List<String> listeIdPrestationsAccordeesDateFinForce = new ArrayList<String>();
        for (JadeAbstractModel absModel : simplePCAccordeeSearch.getSearchResults()) {
            SimplePCAccordee pca = (SimplePCAccordee) absModel;
            pca.setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_CALCULE);
            listeIdPrestationsAccordees.add(pca.getIdPrestationAccordee());
            listeIdPrestationsAccordees.add(pca.getIdPrestationAccordeeConjoint());
            pca = PegasusImplServiceLocator.getSimplePCAccordeeService().update(pca);
        }

        // modifie prestations accordées -> état calculé
        SimplePrestationsAccordeesSearch simplePrestationsAccordeesSearch = new SimplePrestationsAccordeesSearch();
        simplePrestationsAccordeesSearch.setForInIdPrestation(listeIdPrestationsAccordees);
        simplePrestationsAccordeesSearch.setWhereKey(SimplePrestationsAccordeesSearch.GROUPE_ID_PRESTATION_WHERE_KEY);
        simplePrestationsAccordeesSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        simplePrestationsAccordeesSearch = PegasusImplServiceLocator.getSimplePrestatioAccordeeService().find(
                simplePrestationsAccordeesSearch);
        for (JadeAbstractModel absModel : simplePrestationsAccordeesSearch.getSearchResults()) {
            SimplePrestationsAccordees prestation = (SimplePrestationsAccordees) absModel;
            prestation.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
            // if (listeIdPrestationsAccordeesDateFinForce.contains(prestation.getIdPrestationAccordee())) {
            // prestation.setDateFinDroit(null);
            // }
            PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(prestation);
        }

        // dans le cas ou il n'y aurait pas de prestation, chercher tout de même les éventuelles décisions après calcul
        if (listeIdDecisionHeader.size() == 0) {
            SimpleDecisionApresCalculSearch simpleDecisionApresCalculSearch = new SimpleDecisionApresCalculSearch();
            simpleDecisionApresCalculSearch.setForIdVersionDroit(idVersionDroit);
            simpleDecisionApresCalculSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            simpleDecisionApresCalculSearch = PegasusImplServiceLocator.getSimpleDecisionApresCalculService().search(
                    simpleDecisionApresCalculSearch);
            for (JadeAbstractModel absModel : simpleDecisionApresCalculSearch.getSearchResults()) {
                listeIdDecisionHeader.add(((SimpleDecisionApresCalcul) absModel).getIdDecisionHeader());
            }
        }

        // S'il n'y a pas toujours de prestation, il s'agit d'une décision de suppression
        // Il faut donc trouver les idDecisionHeader de suppression liés à la version du droit
        // if (listeIdDecisionHeader.size() == 0) { --> Correction, une decision de suppression a bien des prestations
        // (1 ou 2)

        for (JadeAbstractModel absModel : simpleDecisionSuppressionSearch.getSearchResults()) {
            listeIdDecisionHeader.add(((SimpleDecisionSuppression) absModel).getIdDecisionHeader());
        }
        if (simpleDecisionSuppressionSearch.getSize() > 0) {
            isDecisionSuppression = true;
        }
        // }

        // On ne met pas à jour la version de droit si elle est à l'Etat ANNULE car sera supprimé plus tard
        if (!IPCDroits.CS_ANNULE.equals(simpleVersionDroit.getCsEtatDroit())) {
            // modifie version du droit courant -> état calculé, ou enregistré si c'est une decision de suppression
            if (isDecisionSuppression) {
                simpleVersionDroit.setCsEtatDroit(IPCDroits.CS_ENREGISTRE);
            } else {
                simpleVersionDroit.setCsEtatDroit(IPCDroits.CS_CALCULE);
            }

            simpleVersionDroit = PegasusImplServiceLocator.getSimpleVersionDroitService().update(simpleVersionDroit);
        }

        // modifie décisions de la version du droit -> état enregistré
        SimpleDecisionHeaderSearch simpleDecisionHeaderSearch = new SimpleDecisionHeaderSearch();
        simpleDecisionHeaderSearch.setWhereKey(SimpleDecisionHeaderSearch.DEVALIDATION_WHERE_KEY);
        simpleDecisionHeaderSearch.setForInIdDecisionHeader(listeIdDecisionHeader);
        simpleDecisionHeaderSearch = PegasusImplServiceLocator.getSimpleDecisionHeaderService().search(
                simpleDecisionHeaderSearch);
        // liste des décision des conjoints si il y en a
        List<String> listeIdDecisionHeaderConjoint = new ArrayList<String>();
        for (JadeAbstractModel absModel : simpleDecisionHeaderSearch.getSearchResults()) {
            SimpleDecisionHeader decision = (SimpleDecisionHeader) absModel;
            decision.setDateValidation("");
            decision.setValidationPar("");
            decision.setCsEtatDecision(IPCDecision.CS_ETAT_ENREGISTRE);
            PegasusImplServiceLocator.getSimpleDecisionHeaderService().update(decision);
            // Si l'id conjoint n'est pas vide (0)
            if (!JadeStringUtil.isBlankOrZero(decision.getIdDecisionConjoint())) {
                listeIdDecisionHeaderConjoint.add(decision.getIdDecisionConjoint());
            }
        }

        // MAJ des decisions des conjoints
        if (listeIdDecisionHeaderConjoint.size() > 0) {
            SimpleDecisionHeaderSearch serachConjointDac = new SimpleDecisionHeaderSearch();
            serachConjointDac.setForInIdDecisionHeader(listeIdDecisionHeaderConjoint);
            serachConjointDac = PegasusImplServiceLocator.getSimpleDecisionHeaderService().search(serachConjointDac);

            for (JadeAbstractModel decisionHeader : serachConjointDac.getSearchResults()) {
                SimpleDecisionHeader headerDac = ((SimpleDecisionHeader) decisionHeader);
                headerDac.setDateValidation("");
                headerDac.setCsEtatDecision(IPCDecision.CS_ETAT_ENREGISTRE);
                PegasusImplServiceLocator.getSimpleDecisionHeaderService().update(headerDac);
            }

        }

        listeIdDecisionHeader.addAll(listeIdDecisionHeaderConjoint);

        if (EPCProperties.GESTION_ANNONCES_LAPRAMS.getBooleanValue()) {
            listeIdDecisionHeader.addAll(listeIdDecisionHeaderConjoint);
            if (listeIdDecisionHeader.size() > 0) {
                PegasusImplServiceLocator.getAnnonceLapramsService().deleteByIdsDecision(listeIdDecisionHeader);
            }
        }

        if (EPCProperties.GESTION_COMMUNICATION_OCC.getBooleanValue()) {
            SimpleCommunicationOCCSearch search = new SimpleCommunicationOCCSearch();
            listeIdDecisionHeader.addAll(listeIdDecisionHeaderConjoint);
            search.setForIdVersionDroit(idVersionDroit);
            PegasusImplServiceLocator.getSimpleCommunicationOCCService().delete(search);
        }

    }

    private void devalidationVersionPrecedente(List<PcaForDecompte> pcasReplaced,
            SimpleVersionDroit simpleVersionDroitPrecedente) throws JadePersistenceException, DroitException,
            JadeApplicationServiceNotAvailableException, PCAccordeeException, JadeApplicationException {

        // version du droit -> état validé
        simpleVersionDroitPrecedente.setCsEtatDroit(IPCDroits.CS_VALIDE);
        PegasusImplServiceLocator.getSimpleVersionDroitService().update(simpleVersionDroitPrecedente);
        // PCA historisés -> état validé

        for (PcaForDecompte pca : pcasReplaced) {
            // pca non supprimé
            if (!pca.getSimplePCAccordee().getIsSupprime()) {
                pca.getSimplePCAccordee().setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_VALIDE);
                PegasusImplServiceLocator.getSimplePCAccordeeService().update(pca.getSimplePCAccordee());
                pca.getSimplePrestationsAccordees().setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
                if (JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getDateFin())) {
                    pca.getSimplePrestationsAccordees().setDateFinDroit("0");
                }
                PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(
                        pca.getSimplePrestationsAccordees());

                if (!JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getIdPrestationAccordeeConjoint())) {
                    pca.getSimplePrestationsAccordeesConjoint().setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
                    if (JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getDateFin())) {
                        pca.getSimplePrestationsAccordeesConjoint().setDateFinDroit("0");
                    }
                    PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(
                            pca.getSimplePrestationsAccordeesConjoint());
                }
            }
        }

    }

    private boolean hasDateFinForce(List<SimplePCAccordee> pcas) {
        if (pcas != null) {
            for (SimplePCAccordee pca : pcas) {
                if (pca.getIsDateFinForce()) {
                    return true;
                }
            }
        }
        return false;

    }

    @Override
    public void devalideDecisions(String idDroit, String idVersionDroit, String noVersion) throws DecisionException {
        devalideDecisions(idDroit, idVersionDroit, noVersion, false);
    }

    @Override
    public void devalideDecisions(String idDroit, String idVersionDroit, String noVersion, Boolean forAnnulation)
            throws DecisionException {
        try {

            VersionDroitSearch searchDroitSearch = new VersionDroitSearch();
            searchDroitSearch.setWhereKey(VersionDroitSearch.CURRENT_VERSION);
            searchDroitSearch.setForIdDroit(idDroit);
            PegasusServiceLocator.getDroitService().searchVersionDroit(searchDroitSearch);
            if (searchDroitSearch.getSize() > 0) {
                VersionDroit versionDroit = (VersionDroit) searchDroitSearch.getSearchResults()[0];
                if (!(IPCDroits.CS_VALIDE.equals(versionDroit.getSimpleVersionDroit().getCsEtatDroit())
                        || IPCDroits.CS_COURANT_VALIDE.equals(versionDroit.getSimpleVersionDroit().getCsEtatDroit()) || IPCDroits.CS_ANNULE
                            .equals(versionDroit.getSimpleVersionDroit().getCsEtatDroit()))) {
                    JadeThread.logError(this.getClass().getName(), "pegasus.deValidationDecision.droitExistant");
                }
            }
            if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {

                boolean isDecisionSuppression = false;

                SimpleDecisionSuppressionSearch simpleDecisionSuppressionSearch = new SimpleDecisionSuppressionSearch();
                simpleDecisionSuppressionSearch.setForIdVersionDroit(idVersionDroit);
                simpleDecisionSuppressionSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                simpleDecisionSuppressionSearch = PegasusImplServiceLocator.getSimpleDecisionSuppressionService()
                        .search(simpleDecisionSuppressionSearch);

                isDecisionSuppression = simpleDecisionSuppressionSearch.getSize() > 0;
                VersionDroit vd = PegasusServiceLocator.getDroitService().readVersionDroit(idVersionDroit);
                if (isDecisionSuppression && !forAnnulation) {
                    boolean isLastDemande = PegasusServiceLocator.getDemandeService().isLastDemande(vd.getDemande());
                    if (!isLastDemande) {
                        JadeThread.logError(this.getClass().getName(), "pegasus.deValidationDecision.demandeNontLast");
                        return;
                        // throw new DecisionException(BSessionUtil.getSessionFromThreadContext().getLabel(
                        // ));
                    }
                }

                SimplePCAccordeeSearch simplePCAccordeeSearch = new SimplePCAccordeeSearch();
                simplePCAccordeeSearch.setForIdVersionDroit(idVersionDroit);
                simplePCAccordeeSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                simplePCAccordeeSearch = PegasusImplServiceLocator.getSimplePCAccordeeService().search(
                        simplePCAccordeeSearch);

                List<SimplePCAccordee> newPcas = PersistenceUtil.typeSearch(simplePCAccordeeSearch);

                List<PcaForDecompte> pcasReplaced = null;

                devalidationVersionCourante(idVersionDroit, simplePCAccordeeSearch, simpleDecisionSuppressionSearch);

                if (isDecisionSuppression) {
                    pcasReplaced = findPcaReplacedForSuppression(idDroit, noVersion, newPcas);
                } else {
                    pcasReplaced = findPcaReplaced(idDroit, noVersion, newPcas);
                }

                SimpleVersionDroit simpleVersionDroitPrecedente = findVersionDroitPrecedant(idDroit, noVersion);
                SimpleDemande simpleDemande = vd.getDemande().getSimpleDemande();

                if (isDecisionSuppression || hasDateFinForce(newPcas)) {
                    simpleDemande.setDateFin("");
                }

                if (simpleVersionDroitPrecedente == null) {
                    simpleDemande.setCsEtatDemande(IPCDemandes.CS_EN_ATTENTE_CALCUL);
                } else {
                    devalidationVersionPrecedente(pcasReplaced, simpleVersionDroitPrecedente);
                    DecisionApresCalculSearch search = new DecisionApresCalculSearch();
                    search.setForIdVersionDroit(simpleVersionDroitPrecedente.getIdVersionDroit());
                    List<DecisionApresCalcul> decisions = PersistenceUtil.search(search);
                    if (decisions.size() > 0) {
                        simpleDemande.setCsEtatDemande(EtatDemandeResolver.resolvedEtatDemandeForDevalidation(
                                decisions, hasOnlyRefus(decisions.get(0).getVersionDroit().getDemande()
                                        .getSimpleDemande().getIdDemande())));
                    } else {
                        SimpleDecisionSuppressionSearch decisionSuppressionSearch = new SimpleDecisionSuppressionSearch();
                        decisionSuppressionSearch
                                .setForIdVersionDroit(simpleVersionDroitPrecedente.getIdVersionDroit());
                        int nb = PegasusImplServiceLocator.getSimpleDecisionSuppressionService().count(
                                decisionSuppressionSearch);
                        if (nb == 0) {
                            throw new DecisionException("Unable to find the old decision with this idVersionDroit:"
                                    + simpleVersionDroitPrecedente.getIdVersionDroit());
                        }
                    }
                }

                if (!forAnnulation) {
                    simpleDemande = PegasusImplServiceLocator.getSimpleDemandeService().update(simpleDemande);
                }
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available!", e);
        } catch (JadePersistenceException e) {
            throw new DecisionException("A persistence exception happened!", e);
        } catch (PegasusException e) {
            throw new DecisionException("An exception happened!", e);
        } catch (JadeApplicationException e) {
            throw new DecisionException("A Jade exception happened!", e);
        }
    }

    @Override
    public boolean hasOnlyRefus(String idDemande) throws PCAccordeeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        PCAccordeePlanCalculSearch search = new PCAccordeePlanCalculSearch();
        search.setWhereKey(PCAccordeePlanCalculSearch.FOR_CURRENT_VERSIONED_ETAT_PLAN_CALCULE_NOT_EQUALS);
        search.setForCsEtatPlanCalcul(IPCValeursPlanCalcul.STATUS_REFUS);
        search.setForIdDemande(idDemande);
        int nb = PegasusImplServiceLocator.getPCAccordeeService().count(search);
        return (nb == 0);
    }

    private SimpleVersionDroit findVersionDroitPrecedant(String idDroit, String noVersion) throws DroitException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        SimpleVersionDroit simpleVersionDroitPrecedente = null;
        // recherche la version précédente
        VersionDroitSearch versionDroitSearch = new VersionDroitSearch();
        versionDroitSearch.setForIdDroit(idDroit);
        versionDroitSearch.setForNoVersionDroit(String.valueOf(Integer.parseInt(noVersion) - 1));
        versionDroitSearch.setDefinedSearchSize(1);
        versionDroitSearch.setWhereKey(VersionDroitSearch.DERNIERE_VERSION_HISTORISEE_WHERE_KEY);
        versionDroitSearch.setOrderKey(VersionDroitSearch.DERNIERE_VERSION_HISTORISEE_ORDER_KEY);
        versionDroitSearch = PegasusServiceLocator.getDroitService().searchVersionDroit(versionDroitSearch);

        if (versionDroitSearch.getSize() > 0) {
            VersionDroit versionDroitPrecedente = (VersionDroit) versionDroitSearch.getSearchResults()[0];
            simpleVersionDroitPrecedente = versionDroitPrecedente.getSimpleVersionDroit();
        }
        return simpleVersionDroitPrecedente;
    }

    private List<PcaForDecompte> findPcaReplaced(String idDroit, String noVersion, List<SimplePCAccordee> newPcas)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        List<PcaForDecompte> pcasReplaced = PcaPrecedante.findPcaToReplaced(newPcas,
                new EachPeriode<SimplePCAccordee>() {
                    @Override
                    public String[] dateDebutFin(SimplePCAccordee t) {
                        return new String[] { t.getDateDebut(), t.getDateFin() };
                    }
                }, idDroit, noVersion);
        return pcasReplaced;
    }

    private List<PcaForDecompte> findPcaReplacedForSuppression(String idDroit, String noVersion,
            List<SimplePCAccordee> newPcas) throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        GroupePeriodes periodes = GroupePeriodesResolver.genearateListPeriode(newPcas,
                new EachPeriode<SimplePCAccordee>() {
                    @Override
                    public String[] dateDebutFin(SimplePCAccordee t) {
                        return new String[] { t.getDateDebut(), t.getDateFin() };
                    }
                });
        String dateMax = null;
        try {
            String dateDernierPmt = PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
            dateMax = JadeDateUtil.addMonths("01." + dateDernierPmt, 1).substring(3);
        } catch (PmtMensuelException e) {
            throw new PCAccordeeException("Unabeld to getDateProchainPmt", e);
        }

        List<PcaForDecompte> pcasReplaced = PcaPrecedante.findPcaToReplaced(periodes.getDateFinMax(), dateMax, idDroit,
                noVersion);
        return pcasReplaced;
    }

    @Override
    public boolean isDecisionDevalidable(SimpleDecisionHeader simpleDecisionHeader,
            SimpleVersionDroit simpleVersionDroit) throws LotException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, PrestationException {

        boolean isLotOuvert = false;
        SimplePrestationSearch simplePrestationSearch = new SimplePrestationSearch();
        simplePrestationSearch.setForIdPrestation(simpleDecisionHeader.getIdPrestation());
        simplePrestationSearch = PegasusImplServiceLocator.getSimplePrestationService().search(simplePrestationSearch);
        if (simplePrestationSearch.getSize() > 0) {
            SimplePrestation simplePrestation = (SimplePrestation) simplePrestationSearch.getSearchResults()[0];
            SimpleLot lot = CorvusServiceLocator.getLotService().read(simplePrestation.getIdLot());
            if (IRELot.CS_ETAT_LOT_OUVERT.equals(lot.getCsEtat())) {
                isLotOuvert = true;
            }
        } else {
            // s'il n'y a pas de lots, chercher l'état du droit
            isLotOuvert = IPCDroits.CS_VALIDE.equals(simpleVersionDroit.getCsEtatDroit());
        }
        return isLotOuvert;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision.DecisionService# readDecision(java.lang.String)
     */
    @Override
    public ListDecisions readDecision(String idDecision) throws DecisionException, JadePersistenceException {

        ListDecisions decision = new ListDecisions();
        try {
            decision = PegasusImplServiceLocator.getDecisionBusinessService().read(idDecision);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.getMessage());
        }
        return decision;

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision.DecisionService# searchDecisions
     * (ch.globaz.pegasus.business.models.decision.DecisionSearch)
     */
    @Override
    public ListDecisionsSearch searchDecisions(ListDecisionsSearch listDecisionsSearch) throws DecisionException,
            JadePersistenceException {

        if (listDecisionsSearch == null) {
            throw new DecisionException("Unable to search decisions, the search model passed is null!");
        }
        return (ListDecisionsSearch) JadePersistenceManager.search(listDecisionsSearch);
    }

    @Override
    public List<DecisionPcVO> searchDecisionsByDateValidation(List<String> datesValidations) throws DecisionException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {

        if ((datesValidations == null) || (datesValidations.size() == 0)) {
            throw new DecisionException("Unable to search decision(s), the list passed is null or empty");
        }

        ListDecisionsSearch decisionsSearch = new ListDecisionsSearch();

        // on exclu les décisions de refus sans calcul et d'adaptation
        List<String> forNotCsTypeDecisionList = new ArrayList<String>();
        forNotCsTypeDecisionList.add(IPCDecision.CS_TYPE_REFUS_SC);
        forNotCsTypeDecisionList.add(IPCDecision.CS_TYPE_ADAPTATION_AC);
        decisionsSearch.setNotInCsTypeDecision(forNotCsTypeDecisionList);
        decisionsSearch.setWhereKey("forDatesValidations");
        decisionsSearch.setOrderKey("byTiersAndValidationDates");
        decisionsSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        // on set la liste des dates
        decisionsSearch.setInDatesValidations(datesValidations);

        List<DecisionPcVO> listeRetour = searchDecisionsForAdaptation(decisionsSearch);

        return listeRetour;
    }

    private List<DecisionPcVO> searchDecisionsForAdaptation(ListDecisionsSearch decisionsSearch)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException, DecisionException {
        ListDecisions decision;
        // lance la recherche
        try {
            decisionsSearch = PegasusServiceLocator.getDecisionService().searchDecisions(decisionsSearch);
        } catch (DecisionException e) {
            throw new DecisionException("unable to search the decision", e);
        }
        ArrayList<DecisionPcVO> listeRetour = new ArrayList<DecisionPcVO>();

        // on itere et on remplit la liste de retour
        for (JadeAbstractModel model : decisionsSearch.getSearchResults()) {
            DecisionPcVO objVO = new DecisionPcVO();
            decision = ((ListDecisions) model);
            objVO.setCsTypeDecision(decision.getDecisionHeader().getSimpleDecisionHeader().getCsTypeDecision());
            objVO.setCsMotif(decision.getCsMotifDecSup());
            objVO.setCsSousMotif(decision.getCsSousMotifDecSup());

            if (IPCDecision.CS_TYPE_SUPPRESSION_SC.equalsIgnoreCase(decision.getDecisionHeader()
                    .getSimpleDecisionHeader().getCsTypeDecision())) {
                objVO.setDateDebut(decision.getDateSuppression());
                objVO.setDateFin("");
            } else {
                objVO.setDateDebut(decision.getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision());
                objVO.setDateFin(decision.getDecisionHeader().getSimpleDecisionHeader().getDateFinDecision());
            }

            objVO.setIdDecision(decision.getId());
            objVO.setIdTiersBeneficiaire(decision.getDecisionHeader().getSimpleDecisionHeader()
                    .getIdTiersBeneficiaire());
            objVO.setIdDecisionConjoint(decision.getDecisionHeader().getSimpleDecisionHeader().getIdDecisionConjoint());
            objVO.setNoDecision(decision.getDecisionHeader().getSimpleDecisionHeader().getNoDecision());
            objVO.setNss(decision.getDecisionHeader().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());
            objVO.setIdVersionDroitApc(decision.getIdVersionDroitApc());
            objVO.setIdVersionDroitSup(decision.getIdVersionDroitSup());
            listeRetour.add(objVO);
        }
        return listeRetour;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.decision.DecisionService#searchDecisionsByDateValidation(java.util
     * .List)
     */
    @Override
    public List<DecisionPcVO> searchDecisionsAdaptation() throws DecisionException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {

        ListDecisionsSearch decisionsSearch = new ListDecisionsSearch();

        // on exclu les décisions de refus, sans calcul
        List<String> forNotCsTypeDecisionList = new ArrayList<String>();
        forNotCsTypeDecisionList.add(IPCDecision.CS_TYPE_REFUS_SC);
        decisionsSearch.setNotInCsTypeDecision(forNotCsTypeDecisionList);

        decisionsSearch.setWhereKey("forDatesValidations");
        decisionsSearch.setOrderKey("byTiersAndValidationDates");
        decisionsSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        decisionsSearch.setForCsTypeDecision(IPCDecision.CS_TYPE_ADAPTATION_AC);

        String date = getMaxDateDebutDroit();
        decisionsSearch.setForDateDebutDroit(date);

        List<DecisionPcVO> listeRetour = searchDecisionsForAdaptation(decisionsSearch);

        return listeRetour;
    }

    private String getMaxDateDebutDroit() throws JadePersistenceException {
        // Récupération de la date de début de droit la plus grande pour les décisions d'adaptation
        String sql = "SELECT MAX(DHDDDR) AS maxDate FROM {schema}.PCDECHEA WHERE DHTTYP = "
                + IPCDecision.CS_TYPE_ADAPTATION_AC;

        sql = sql.replace("{schema}", JadePersistenceUtil.getDbSchema());

        ArrayList<HashMap<String, Object>> result = PersistenceUtil.executeQuery(sql, this.getClass());

        String date = "";
        if (result.isEmpty() || result.size() > 1) {
            throw new JadePersistenceException("Pas de date de début de droit MAX trouvée.");
        } else {
            String rsDate = String.valueOf(result.get(0).get("MAXDATE"));
            date = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(rsDate);
        }

        return date;
    }

    @Override
    public ForDeleteDecisionSearch searchForDelete(ForDeleteDecisionSearch search) throws DecisionException,
            JadePersistenceException {
        if (search == null) {
            throw new DecisionException("Unable to searchForDelete search, the model passed is null!");
        }
        return (ForDeleteDecisionSearch) JadePersistenceManager.search(search);
    }

    @Override
    public boolean isAdaptationAnnuelleNotValidate(String nextDate) throws JadePersistenceException {
        // récupère le statut de l'étape "valider les décisions" du traitement d'adaptation prévu à la date en paramètre
        String sql = "SELECT COUNT(*) as NB FROM SCHEMA.JAPRSTEP INNER JOIN SCHEMA.JAPRPROP on SCHEMA.JAPRSTEP.IDEXPR = SCHEMA.JAPRPROP.IDEXPR "
                + " INNER JOIN SCHEMA.JAPREXPR on SCHEMA.JAPREXPR.IDEXPR = SCHEMA.JAPRSTEP.IDEXPR"
                + " WHERE PROKEY = 'DATE_ADAPTATION'"
                + " AND SCHEMA.JAPREXPR.EXNAME = 'Pegasus.AdaptationPC' "
                + " AND SCHEMA.JAPRSTEP.CSSTAT = '"
                + JadeProcessStepStateEnum.VALIDATE.toString()
                + "'"
                + " AND PROVAL = '" + nextDate + "' AND KEYSTP = '6'";
        List<Integer> listCount = SCM.newInstance(Integer.class).query(sql).execute();
        Integer count = listCount.get(0);
        return count == 0;
    }
}
