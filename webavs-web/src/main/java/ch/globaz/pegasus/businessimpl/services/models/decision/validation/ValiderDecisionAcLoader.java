package ch.globaz.pegasus.businessimpl.services.models.decision.validation;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.GroupePeriodes;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.corvus.business.exceptions.models.SimpleRetenuePayementException;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayementSearch;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.AllocationDeNoelException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordee;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordeeSearch;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.dettecomptatcompense.DetteCompenseCompteAnnexe;
import ch.globaz.pegasus.business.models.dettecomptatcompense.DetteCompenseCompteAnnexeSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroitSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeePlanCalculSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoelSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppointSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.pcaccordee.PcaPrecedante;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.businessimpl.utils.decision.AbstractValiderDecision;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.utils.periode.GroupePeriodesResolver;

public class ValiderDecisionAcLoader extends AbstractValiderDecision {

    private PcaForDecompte convertTopcForDecompte(DecisionApresCalcul decisionApresCalcul) {
        PcaForDecompte pca = new PcaForDecompte();
        pca.setMontantPCMensuelle(decisionApresCalcul.getPlanCalcul().getMontantPCMensuelle());
        pca.setSimpleInformationsComptabilite(decisionApresCalcul.getPcAccordee().getSimpleInformationsComptabilite());
        pca.setSimpleInformationsComptabiliteConjoint(decisionApresCalcul.getPcAccordee()
                .getSimpleInformationsComptabiliteConjoint());
        pca.setSimplePCAccordee(decisionApresCalcul.getPcAccordee().getSimplePCAccordee());
        pca.setSimplePrestationsAccordees(decisionApresCalcul.getPcAccordee().getSimplePrestationsAccordees());
        pca.setSimplePrestationsAccordeesConjoint(decisionApresCalcul.getPcAccordee()
                .getSimplePrestationsAccordeesConjoint());
        if (pca.getSimplePCAccordee().getIsDateFinForce()) {
            pca.getSimplePCAccordee().setDateFin(null);
        }
        return pca;
    }

    /**
     * Recherche les allocations de noel. Retourne null si aucune allocation n'est trouvé ou si la version du droit est
     * plus grand que 1
     * 
     * @param pcAccordeePlanCalcul
     * @return
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private List<SimpleAllocationNoel> findAllocationNoel(String noVersion, List<String> idsPca)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Seulement dans le cas de version de droit initial on donne un retro
        if (ValiderDecisionUtils.isDroitInitial(noVersion)) {
            try {
                SimpleAllocationNoelSearch search = new SimpleAllocationNoelSearch();
                search.setInIdsPcAccordee(idsPca);
                PegasusImplServiceLocator.getSimpleAllocationDeNoelService().search(search);
                if (idsPca.size() < search.getSize()) {
                    throw new PCAccordeeException("Too many allocation de noel was found four this ...");
                }
                return PersistenceUtil.typeSearch(search, search.whichModelClass());
            } catch (AllocationDeNoelException e) {
                throw new PCAccordeeException("Unable to search the allocation de noel", e);
            }
        }
        return new ArrayList<SimpleAllocationNoel>();
    }

    private void findAndSetAllocationNoelIfUsed(ValiderDecisionAcData data, List<String> idsPca)
            throws PropertiesException, PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        if (EPCProperties.ALLOCATION_NOEL.getBooleanValue()) {
            data.setAllocationsNoel(findAllocationNoel(data.getNoVersionDroit(), idsPca));
        } else {
            data.setAllocationsNoel(new ArrayList<SimpleAllocationNoel>());
        }
    }

    private List<CreanceAccordee> findCreancier(String idVersionDroit) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, DecisionException {
        List<CreanceAccordee> list = new ArrayList<CreanceAccordee>();
        CreanceAccordeeSearch search = new CreanceAccordeeSearch();
        search.setForIdVersionDroit(idVersionDroit);
        try {
            search = PegasusServiceLocator.getCreanceAccordeeService().search(search);
            list = PersistenceUtil.typeSearch(search, search.whichModelClass());
        } catch (CreancierException e) {
            throw new DecisionException("Unable to search the cranceAccordee", e);
        }
        return list;
    }

    private List<String> findIdPca(List<PcaForDecompte> pcasNew) {
        List<String> ids = new ArrayList<String>();
        for (PcaForDecompte pca : pcasNew) {
            ids.add(pca.getSimplePCAccordee().getIdPCAccordee());
        }
        return ids;
    }

    private List<DetteCompenseCompteAnnexe> findListDetteEnCompta(SimpleVersionDroit versionDroit)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException, JadePersistenceException {

        DetteCompenseCompteAnnexeSearch search = new DetteCompenseCompteAnnexeSearch();
        search.setForIdVersionDroit(versionDroit.getIdVersionDroit());
        search.setForIdDroit(versionDroit.getIdDroit());

        List<DetteCompenseCompteAnnexe> list = PersistenceUtil.search(search, search.whichModelClass());

        return list;
    }

    /**
     * Permet de trouver les copies et les pca supprimé qui ont été créer lors de la nouvelle version du droit. On est
     * obliger de faire un select supplémentaire car ces pca ne sont pas liées à une décisions.
     * 
     * @param idDroit
     * @param noVersion
     * @return
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private List<PCAccordee> findPcaCopieAndDeleted(String idDroit, String noVersion) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // recherche des pca pour la version du droit avec un id pcaParent (pca copié)
        PCAccordeeSearch search = new PCAccordeeSearch();
        search.setForIdDroit(idDroit);
        search.setForNoVersionDroit(noVersion);
        search.setForIsDeleted(true);
        search.setWhereKey("forPCACopieAndSupprimerToUpdate");
        search = PegasusImplServiceLocator.getPCAccordeeService().search(search);
        return PersistenceUtil.typeSearch(search, search.whichModelClass());
    }

    private List<PcaForDecompte> findPcaToReplaced(GroupePeriodes periodes, String idDroit, String noVersionDroitCourant)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        List<PcaForDecompte> list = PcaPrecedante.findPcaToReplaced(periodes, idDroit, noVersionDroitCourant);
        for (PcaForDecompte pcaForDecompte : list) {
            if (pcaForDecompte.getSimplePCAccordee().getIsDateFinForce()) {
                pcaForDecompte.getSimplePCAccordee().setDateFin(null);
            }
        }
        return list;
    }

    private SimpleVersionDroit findSimpleVersionDroitReplaced(String idDroit, String noVersionDroit)
            throws JadePersistenceException, DroitException, JadeApplicationServiceNotAvailableException {
        if (!ValiderDecisionUtils.isDroitInitial(noVersionDroit)) {
            SimpleVersionDroitSearch search = new SimpleVersionDroitSearch();
            search.setForNoVersionDroit(String.valueOf(Integer.valueOf(noVersionDroit) - 1));
            search.setForIdDroit(idDroit);
            search = PegasusImplServiceLocator.getSimpleVersionDroitService().search(search);
            if (search.getSize() == 1) {
                SimpleVersionDroit simpleVersionDroit = (SimpleVersionDroit) search.getSearchResults()[0];
                return simpleVersionDroit;
            } else {
                throw new DroitException("Unbale to find the old version droit with this values: noVersion:"
                        + noVersionDroit + ", idDroit:" + idDroit);
            }
        }
        return null;
    }

    /**
     * @param pcas
     * @param dateDebut
     * @return liste des id des pca qui sersont utilisé pour trouvé les jours d'appoint.
     */
    private List<String> generateListIdPcaForJourAppoint(List<PcaForDecompte> pcas, String dateDebut) {
        List<String> ids = new ArrayList<String>();
        for (PcaForDecompte pca : pcas) {
            // on filtre les anciennes pca qui ne sont pas comprsie dans la plage de calcule.
            // On fait ceci pour ne pas creer des restitutions des jours d'appoint.
            if (JadeDateUtil.isDateMonthYearBefore(dateDebut, pca.getSimplePCAccordee().getDateDebut())
                    || pca.getSimplePCAccordee().getDateDebut().equals(dateDebut)) {
                ids.add(pca.getSimplePCAccordee().getIdPCAccordee());
            }
        }
        return ids;
    }

    private List<PcaForDecompte> generatePca(List<DecisionApresCalcul> decisionsAc) {
        List<PcaForDecompte> list = new ArrayList<PcaForDecompte>();
        List<String> ids = new ArrayList<String>();
        for (DecisionApresCalcul dc : decisionsAc) {
            // On filtre les pca double pour les DOM2R
            if (!ids.contains(dc.getPcAccordee().getSimplePCAccordee().getId())) {
                ids.add(dc.getPcAccordee().getSimplePCAccordee().getId());
                list.add(convertTopcForDecompte(dc));
            }
        }
        return list;
    }

    /**
     * Permet de générer une liste de periode afin de pouvoir déterminer les dates min et date max
     * 
     * @param data
     * @return
     */
    private GroupePeriodes generatePeriode(ValiderDecisionAcData data) {
        GroupePeriodes periodes = GroupePeriodesResolver.genearateListPeriode(data.getDecisionsApresCalcul(),
                new GroupePeriodesResolver.EachPeriode<DecisionApresCalcul>() {
                    @Override
                    public String[] dateDebutFin(DecisionApresCalcul t) {
                        String dateFin = t.getPcAccordee().getSimplePCAccordee().getDateFin();

                        if (t.getPcAccordee().getSimplePCAccordee().getIsDateFinForce()) {
                            dateFin = null;
                        }

                        return new String[] { t.getPcAccordee().getSimplePCAccordee().getDateDebut(), dateFin };
                    }
                });
        return periodes;
    }

    private boolean hasOnlyRefus(String idDemande) throws PCAccordeeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        PCAccordeePlanCalculSearch search = new PCAccordeePlanCalculSearch();
        search.setWhereKey(PCAccordeePlanCalculSearch.FOR_CURRENT_VERSIONED_ETAT_PLAN_CALCULE_NOT_EQUALS);
        search.setForCsEtatPlanCalcul(IPCValeursPlanCalcul.STATUS_REFUS);
        search.setForIdDemande(idDemande);
        int nb = PegasusImplServiceLocator.getPCAccordeeService().count(search);
        return (nb == 0);
    }

    private boolean isPcaCopie(PcaForDecompte pca) {
        return !JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getIdPcaParent());
    }

    /**
     * @param idVersionDroit
     * @return
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     * @throws JadeApplicationException
     */
    public ValiderDecisionAcData load(String idVersionDroit) throws JadePersistenceException,
            JadeNoBusinessLogSessionError, JadeApplicationException {
        ValiderDecisionAcData data = new ValiderDecisionAcData();

        data.setDecisionApresCalculs(searchDecisionAprsCalcule(idVersionDroit));

        setProperties(data);

        GroupePeriodes periodes = generatePeriode(data);
        setDateDebutAndDateFin(periodes, data);

        data.setDateDecision(data.getDecisionsApresCalcul().get(0).getDecisionHeader().getSimpleDecisionHeader()
                .getDateDecision());
        data.setDateProchainPaiement(PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt());

        data.setPcasNew(generatePca(data.getDecisionsApresCalcul()));

        for (PcaForDecompte pca : data.getPcasNew()) {
            if (pca.getSimplePCAccordee().getIsDateFinForce()) {
                data.setDateFinForce("12."
                        + JadeDateUtil.convertDateMonthYear(pca.getSimplePrestationsAccordees().getDateDebutDroit())
                                .substring(3));
            }
        }

        data.setRentuesPayements(searchRetenuesPaiement(data.getPcasNew()));

        data.setSimpleDemande(data.getDecisionsApresCalcul().get(0).getVersionDroit().getDemande().getSimpleDemande());
        data.setSimpleVersionDroitNew(data.getDecisionsApresCalcul().get(0).getVersionDroit().getSimpleVersionDroit());
        data.setRequerant(resolveRequerant(data.getDecisionsApresCalcul()));
        data.setConjoint(resolveConjoint(data.getDecisionsApresCalcul()));

        data.setPcasReplaced(findPcaToReplaced(periodes, data.getIdDroit(), data.getNoVersionDroit()));

        data.setHasDemandeOnlyPcaRefus(hasOnlyRefus(data.getSimpleDemande().getIdDemande()));

        List<PCAccordee> pcaCopieSupprimee = findPcaCopieAndDeleted(data.getIdDroit(), data.getNoVersionDroit());
        List<PCAccordee> pcaSupprimer = new ArrayList<PCAccordee>();
        List<PCAccordee> pcaCopies = new ArrayList<PCAccordee>();
        for (PCAccordee pca : pcaCopieSupprimee) {
            if (pca.getSimplePCAccordee().getIsSupprime()) {
                pcaSupprimer.add(pca);
            } else if (!JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getIdPcaParent())) {
                pcaCopies.add(pca);
            } else {
                throw new DecisionException("The pca must be a copie or delete id pca: "
                        + pca.getSimplePCAccordee().getId());
            }
        }

        data.setPcasSupprimee(pcaSupprimer);
        data.setPcaCopie(pcaCopies);

        data.setSimpleVersionDroitReplaced(findSimpleVersionDroitReplaced(data.getIdDroit(), data.getNoVersionDroit()));

        List<String> idsPca = findIdPca(data.getPcasNew());
        findAndSetAllocationNoelIfUsed(data, idsPca);

        setJoursAppoints(data);

        data.setCreanciers(findCreancier(idVersionDroit));
        data.setDettes(findListDetteEnCompta(data.getSimpleVersionDroitNew()));

        return data;
    }

    private PersonneEtendueComplexModel resolveConjoint(List<DecisionApresCalcul> decisionApresCalculs) {
        return resolvePersonne(decisionApresCalculs, IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
    }

    private String resolveDateDebutPlageDeCalacul(List<PcaForDecompte> pcas) {
        String dateDebut = "12.2999";
        for (PcaForDecompte pca : pcas) {

            if (!isPcaCopie(pca)
                    && JadeDateUtil.isDateMonthYearBefore(pca.getSimplePCAccordee().getDateDebut(), dateDebut)) {
                dateDebut = pca.getSimplePCAccordee().getDateDebut();
            }
        }
        return dateDebut;
    }

    private PersonneEtendueComplexModel resolvePersonne(List<DecisionApresCalcul> decisionApresCalculs,
            String csRoleFamille) {
        for (DecisionApresCalcul dc : decisionApresCalculs) {
            if (csRoleFamille.equals(dc.getPcAccordee().getSimplePCAccordee().getCsRoleBeneficiaire())) {
                return dc.getPcAccordee().getPersonneEtendue();
            }
        }
        return null;
    }

    private PersonneEtendueComplexModel resolveRequerant(List<DecisionApresCalcul> decisionApresCalculs) {
        return resolvePersonne(decisionApresCalculs, IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
    }

    private List<DecisionApresCalcul> searchDecisionAprsCalcule(String idVersionDroit) throws JadePersistenceException,
            DecisionException, JadeApplicationServiceNotAvailableException {
        DecisionApresCalculSearch search = new DecisionApresCalculSearch();
        search.setForIdVersionDroit(idVersionDroit);
        search.setForExcludeCsEtatDecisionValide(IPCDecision.CS_ETAT_DECISION_VALIDE);
        search.setWhereKey("forValidationDecision");
        search = PegasusServiceLocator.getDecisionApresCalculService().search(search);
        return PersistenceUtil.typeSearch(search, search.whichModelClass());
    }

    private List<SimpleJoursAppoint> searchJoursAppoint(List<PcaForDecompte> pcasNew, String dateDebut)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleJoursAppointSearch search = new SimpleJoursAppointSearch();
        search.setInIdsPca(generateListIdPcaForJourAppoint(pcasNew, dateDebut));
        if (search.getInIdsPca().size() > 0) {
            PegasusImplServiceLocator.getSimpleJoursAppointService().search(search);
            return PersistenceUtil.typeSearch(search, search.whichModelClass());
        }
        return new ArrayList<SimpleJoursAppoint>();
    }

    private List<SimpleRetenuePayement> searchRetenuesPaiement(List<PcaForDecompte> list)
            throws SimpleRetenuePayementException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        List<String> idsPca = new ArrayList<String>();
        for (PcaForDecompte pca : list) {
            idsPca.add(pca.getSimplePCAccordee().getIdPrestationAccordee());
        }
        SimpleRetenuePayementSearch search = new SimpleRetenuePayementSearch();
        search.setInIdsRente(idsPca);
        search = CorvusServiceLocator.getSimpleRetenuePayementService().search(search);
        return PersistenceUtil.typeSearch(search, search.whichModelClass());
    }

    private void setDateDebutAndDateFin(GroupePeriodes periodes, ValiderDecisionAcData data) {
        data.setDateDebut(periodes.getDateDebutMin());
        data.setDateDebutMax(periodes.getDateDebutMax());
        if (periodes.hasDateFinNullValue()) {
            data.setDateFin(null);
        } else {
            data.setDateFin(periodes.getDateFinMax());
        }
    }

    private void setJoursAppoints(ValiderDecisionAcData data) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        String dateDebut = resolveDateDebutPlageDeCalacul(data.getPcasNew());
        if (data.getUseJourAppoints()) {
            data.setJoursAppointNew(searchJoursAppoint(data.getPcasNew(), dateDebut));
            data.setJoursAppointReplaced(searchJoursAppoint(data.getPcasReplaced(), dateDebut));
        }

    }

    private void setProperties(ValiderDecisionAcData data) throws PropertiesException {
        data.setUseJourAppoints(EPCProperties.GESTION_JOURS_APPOINTS.getBooleanValue());
        data.setNbMonthBetween(Integer.parseInt(EPCProperties.MONTH_BETWEEN.getValue()));
        data.setHasAllocationNoel(EPCProperties.ALLOCATION_NOEL.getBooleanValue());
        data.setCurrentUserId(JadeThread.currentUserId());
    }
}
