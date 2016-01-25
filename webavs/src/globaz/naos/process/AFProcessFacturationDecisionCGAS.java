package globaz.naos.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAModulePassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageModule;
import globaz.musca.db.facturation.FAPassageModuleManager;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.utils.CAUtil;
import globaz.phenix.application.CPApplication;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.aries.business.constantes.ARDecisionEtat;
import ch.globaz.aries.business.constantes.ARDecisionType;
import ch.globaz.aries.business.constantes.ARSortieEtat;
import ch.globaz.aries.business.models.ComplexSortieCGASDecisionCGASAffiliation;
import ch.globaz.aries.business.models.ComplexSortieCGASDecisionCGASAffiliationSearchModel;
import ch.globaz.aries.business.models.DecisionCGASSearchModel;
import ch.globaz.aries.business.models.SimpleDecisionCGAS;
import ch.globaz.aries.business.services.AriesServiceLocator;

/**
 * Processus de facturation des désisions CGAS
 * 
 * @author bjo
 * 
 */
public class AFProcessFacturationDecisionCGAS extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Retourne une session OSIRIS
     * 
     * @param session
     * @return
     * @throws RemoteException
     * @throws Exception
     */
    private static BSession getSessionOsiris(BSession session) throws RemoteException, Exception {
        return (BSession) GlobazSystem.getApplication("OSIRIS").newSession(session);
    }

    private String idModuleFacturation = "";

    private IFAPassage passageFacturation;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        boolean processState = true;

        try {
            // On ouvre un nouveau journal de facturation. Se fait dans une transaction spécifique car doit se faire
            // dans tous les cas
            openNewPassageIfNotExist(passageFacturation);

            // recherche du dernier passage de facturation périodique
            FAPassage dernierPassagePeriodique = findDernierPassagePeriodique();

            // facturation des décision CGAS (sans les réctificatives)
            List<SimpleDecisionCGAS> listDecisionFacturee = facturerDecisionCgas(passageFacturation,
                    dernierPassagePeriodique);

            // facturation des sorties CGAS
            List<ComplexSortieCGASDecisionCGASAffiliation> listSortieFacturee = facturerSortieCgas(passageFacturation,
                    dernierPassagePeriodique);

            // mise à jour des décisions et des sorties facturées
            if (!getSession().hasErrors() && !getSession().getCurrentThreadTransaction().hasErrors() && !isOnError()) {
                updateDecisions(listDecisionFacturee);
                updateSorties(listSortieFacturee);
            }
        } catch (Exception e) {
            getTransaction().addErrors(e.getMessage());
            getMemoryLog().logMessage(getSession().getLabel("PROCESS_FACTURATION_DECISION_CGAS_ERROR"),
                    FWMessage.FATAL, this.getClass().toString());
            processState = false;
        }

        return processState;
    }

    private String addDaysSkipWeekend(String date, int nbJour) throws Exception {
        JACalendarGregorian calendrier = new JACalendarGregorian();

        while (nbJour > 0) {
            date = calendrier.addDays(date, 1);
            if (!calendrier.isWeekend(date)) {
                nbJour--;
            }
        }

        return date;
    }

    private String calculerFinPeriodeAfact(String debutPeriode, int nbMoisAFacturer) {
        if (JadeStringUtil.isBlank(debutPeriode)) {
            throw new IllegalArgumentException("debutPeriode must not be null !!!");
        }

        if (nbMoisAFacturer <= 0) {
            return debutPeriode;
        }
        String finPeriode = JadeDateUtil.addMonths(debutPeriode, nbMoisAFacturer - 1);
        return JadeDateUtil.getLastDateOfMonth(finPeriode);
    }

    private int calculerNbMoisAFacturer(SimpleDecisionCGAS decision, String periodiciteAffiliation,
            IFAPassage dernierPassagePeriodique) throws Exception {

        // String periodiciteAff = affiliation.getPeriodicite();
        int moisDernierPassagePeriodique = JACalendar.getMonth(dernierPassagePeriodique.getDatePeriode());
        int moisDebutDecision = JACalendar.getMonth(decision.getDateDebut());
        int moisFinDecision = JACalendar.getMonth(decision.getDateFin());
        int nbMoisDecision = (moisFinDecision - moisDebutDecision) + 1;
        int nbMoisAFacturer = 0;

        int anneeDecision = Integer.valueOf(decision.getAnnee());
        int anneeDernierPassagePeriodique = JACalendar.getYear(dernierPassagePeriodique.getDatePeriode());

        if (anneeDernierPassagePeriodique < anneeDecision) {
            return 0;
        } else if (anneeDernierPassagePeriodique > anneeDecision) {
            return nbMoisDecision;
        }

        if (moisDebutDecision > moisDernierPassagePeriodique) {
            // pas de rétroactif à facturer
            nbMoisAFacturer = 0;
        } else if (moisFinDecision <= moisDernierPassagePeriodique) {
            // cas rétroactif il faut facturer toute la décision
            nbMoisAFacturer = nbMoisDecision;
        } else {

            if (periodiciteAffiliation.equals(CodeSystem.PERIODICITE_MENSUELLE)) {
                nbMoisAFacturer = (moisDernierPassagePeriodique - moisDebutDecision) + 1;
            } else if (periodiciteAffiliation.equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {

                int moisDerniereTrimestrielle = 0;
                if (moisDernierPassagePeriodique < 3) {
                    moisDerniereTrimestrielle = 0;
                } else if (moisDernierPassagePeriodique < 6) {
                    moisDerniereTrimestrielle = 3;
                } else if (moisDernierPassagePeriodique < 9) {
                    moisDerniereTrimestrielle = 6;
                } else if (moisDernierPassagePeriodique < 12) {
                    moisDerniereTrimestrielle = 9;
                } else {
                    moisDerniereTrimestrielle = 12;
                }

                nbMoisAFacturer = (moisDerniereTrimestrielle - moisDebutDecision) + 1;

                // il est possible que la dernière facturation trimestrielle soit inférieure au mois de début de la
                // décision
                // dans ce cas il n'y a pas de rétroactif à facturer
                if (nbMoisAFacturer < 0) {
                    nbMoisAFacturer = 0;
                }

            } else if (periodiciteAffiliation.equals(CodeSystem.PERIODICITE_ANNUELLE)) {

                if (moisDernierPassagePeriodique < 12) {
                    // pas de rétroactif à gérer la périodique s'occupe du cas
                    nbMoisAFacturer = 0;
                } else {
                    // plus de périodique il faut facturer la totalité de la décision
                    nbMoisAFacturer = nbMoisDecision;
                }
            } else {
                throw new IllegalStateException("la périodicité d'affiliation n a pas été trouvée : "
                        + decision.getIdAffiliation());
            }

        }

        return nbMoisAFacturer;
    }

    /**
     * Génération d'un afact en facturation. Si les paramètres montantInitial et montantDejaFacture ne sont pas null on
     * crée un afact en tableau
     * 
     * @param idPassage
     * @param enteteFacture
     * @param decisionCgas
     * @param assurance
     * @param montantFacture
     * @param debutPeriode
     * @param finPeriode
     * @param montantInitial
     * @param montantDejaFacture
     * @throws Exception
     */
    private void createAfact(String idPassage, FAEnteteFacture enteteFacture, SimpleDecisionCGAS decisionCgas,
            AFAssurance assurance, String montantFacture, String debutPeriode, String finPeriode,
            String montantInitial, String montantDejaFacture) throws Exception {

        if (new FWCurrency(montantFacture).doubleValue() != 0) {
            // création de l'afact
            FAAfact afact = new FAAfact();
            afact.setISession(getSession());
            afact.setIdEnteteFacture(enteteFacture.getIdEntete());
            afact.setIdPassage(idPassage);
            afact.setIdModuleFacturation(idModuleFacturation);
            afact.setIdTypeAfact(CodeSystem.TYPE_FACT_FACT_STANDARD);
            afact.setNonImprimable(Boolean.FALSE);
            afact.setNonComptabilisable(Boolean.FALSE);
            afact.setAQuittancer(Boolean.FALSE);
            afact.setAnneeCotisation(decisionCgas.getAnnee());
            afact.setLibelle(assurance.getAssuranceLibelle());
            afact.setIdRubrique(assurance.getRubriqueId());
            afact.setDebutPeriode(debutPeriode);
            afact.setFinPeriode(finPeriode);
            afact.setNumCaisse("");
            afact.setAffichtaux(Boolean.FALSE);
            afact.setMontantFacture(Double.toString(JANumberFormatter.round(
                    new FWCurrency(montantFacture).doubleValue(), 0.05, 2, JANumberFormatter.NEAR)));

            // si afact en tableau on set le montant initial et le montant déjà facturé
            if (montantInitial != null) {
                afact.setMontantInitial(Double.toString(JANumberFormatter.round(
                        new FWCurrency(montantInitial).doubleValue(), 0.05, 2, JANumberFormatter.NEAR)));
                afact.setIdTypeAfact(FAAfact.CS_AFACT_TABLEAU);
            }
            if (montantDejaFacture != null) {
                afact.setMontantDejaFacture(Double.toString(JANumberFormatter.round(
                        new FWCurrency(montantDejaFacture).doubleValue(), 0.05, 2, JANumberFormatter.NEAR)));
            }
            try {
                afact.add();
            } catch (Exception e) {
                throw new Exception("unable to create afact for idDecision : " + decisionCgas.getIdDecision());
            }
        }
    }

    /**
     * Permet de facturer les décisions CGAS du passage de facturation associé au process (ne traite pas les
     * rectificatives)
     * 
     * @param anneePassageFacturation
     * @param dernierPassagePeriodique
     * @throws Exception
     */
    private List<SimpleDecisionCGAS> facturerDecisionCgas(IFAPassage passageFactu, FAPassage dernierPassagePeriodique)
            throws Exception {
        List<SimpleDecisionCGAS> listDecisionCgasFacturee = new ArrayList<SimpleDecisionCGAS>();
        int anneePassageFacturation = JACalendar.getYear(passageFacturation.getDateFacturation());

        // recherche des décisions CGAS à traiter. Ne pas prendre les réctificatives car traitées via les sorties
        DecisionCGASSearchModel decisionCgasSearchModel = new DecisionCGASSearchModel();
        decisionCgasSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        decisionCgasSearchModel.setForIdPassageFacturation(passageFactu.getIdPassage());
        decisionCgasSearchModel.setForEtat(ARDecisionEtat.VALIDEE.getCodeSystem());
        decisionCgasSearchModel.setInType(ARDecisionType.getListTypeNonRectif());
        decisionCgasSearchModel = findDecisionCgas(decisionCgasSearchModel);

        // Parcours des décisions CGAS
        for (JadeAbstractModel abstractModel : decisionCgasSearchModel.getSearchResults()) {
            SimpleDecisionCGAS decisionCgas = (SimpleDecisionCGAS) abstractModel;

            AFAffiliation affiliation = readAffiliation(decisionCgas.getIdAffiliation());

            int anneeDecision = new Integer(decisionCgas.getAnnee());

            AFAssurance assurance = findAssuranceCGAS();
            String idRole = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication());

            // si il s'agit d'une décision pour une année antérieure à l'année du passage de facturation actuel
            // on facture la totalité de la décision (rétroactif)
            if (anneeDecision < anneePassageFacturation) {

                FAEnteteFacture enteteFacture = findEnteteFacturation(passageFactu.getIdPassage(),
                        affiliation.getIdTiers(), affiliation.getAffilieNumero(), idRole);
                createAfact(passageFactu.getIdPassage(), enteteFacture, decisionCgas, assurance,
                        decisionCgas.getCotisationPeriode(), decisionCgas.getDateDebut(), decisionCgas.getDateFin(),
                        null, null);

            }
            // si il s'agit d'une décision pour l'année en cours
            // on facture en fonction de la périodicité de l'affiliation et du dernier passage périodique effectué
            else if (anneeDecision == anneePassageFacturation) {
                int nbMoisAFacturer = calculerNbMoisAFacturer(decisionCgas, affiliation.getPeriodicite(),
                        dernierPassagePeriodique);

                FWCurrency montantAFacturer = new FWCurrency(JANumberFormatter.round(
                        new FWCurrency(decisionCgas.getCotisationMensuelle()).doubleValue() * nbMoisAFacturer, 0.05, 2,
                        JANumberFormatter.NEAR));

                FAEnteteFacture enteteFacture = findEnteteFacturation(passageFactu.getIdPassage(),
                        affiliation.getIdTiers(), affiliation.getAffilieNumero(), idRole);
                String debutPeriode = decisionCgas.getDateDebut();
                String finPeriode = calculerFinPeriodeAfact(debutPeriode, nbMoisAFacturer);
                createAfact(passageFactu.getIdPassage(), enteteFacture, decisionCgas, assurance,
                        montantAFacturer.toString(), debutPeriode, finPeriode, null, null);

            }

            // mise à jour de l'état de la décision
            decisionCgas.setEtat(ARDecisionEtat.COMPTABILISEE.getCodeSystem());
            listDecisionCgasFacturee.add(decisionCgas);
        }
        return listDecisionCgasFacturee;
    }

    /**
     * Permet de facturer les sorties CGAS du passage de facturation associé au process
     * 
     * @param anneePassageFacturation
     * @param dernierPassagePeriodique
     * @throws Exception
     */
    private List<ComplexSortieCGASDecisionCGASAffiliation> facturerSortieCgas(IFAPassage passageFactu,
            FAPassage dernierPassagePeriodique) throws Exception {
        List<ComplexSortieCGASDecisionCGASAffiliation> listSortieCgasFacturee = new ArrayList<ComplexSortieCGASDecisionCGASAffiliation>();
        int anneePassageFacturation = JACalendar.getYear(passageFacturation.getDateFacturation());

        ComplexSortieCGASDecisionCGASAffiliationSearchModel complexSortieCgasSearchModel = new ComplexSortieCGASDecisionCGASAffiliationSearchModel();
        complexSortieCgasSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        complexSortieCgasSearchModel.setForIdPassageFacturation(passageFactu.getIdPassage());
        complexSortieCgasSearchModel.setForEtat(ARSortieEtat.NON_COMPTABILISEE.getCodeSystem());
        complexSortieCgasSearchModel = findComplexSortieCgas(complexSortieCgasSearchModel);

        // parcours des sorties CGAS
        for (JadeAbstractModel abstractModel : complexSortieCgasSearchModel.getSearchResults()) {
            ComplexSortieCGASDecisionCGASAffiliation complexSortieCgas = (ComplexSortieCGASDecisionCGASAffiliation) abstractModel;

            int anneeSortie = new Integer(complexSortieCgas.getDecisionCgas().getAnnee());

            AFAssurance assurance = findAssuranceCGAS();
            String idRole = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication());

            // si il s'agit d'une sortie pour une année antérieure à l'année du passage de facturation actuel
            // on facture la totalité de la sortie (montant de l'extourne)
            if (anneeSortie < anneePassageFacturation) {

                FAEnteteFacture enteteFacture = findEnteteFacturation(passageFactu.getIdPassage(), complexSortieCgas
                        .getAffiliation().getIdTiers(), complexSortieCgas.getAffiliation().getAffilieNumero(), idRole);
                createAfact(passageFactu.getIdPassage(), enteteFacture, complexSortieCgas.getDecisionCgas(), assurance,
                        complexSortieCgas.getSortieCgas().getMontantExtourne(), complexSortieCgas.getDecisionCgas()
                                .getDateDebut(), complexSortieCgas.getDecisionCgas().getDateFin(), null, null);

            } else if ((anneeSortie == anneePassageFacturation)
                    && ARDecisionEtat.SUPPRIMEE.equals(complexSortieCgas.getDecisionCgas().getEtat())) {

                // montant déjà facturé
                CACompteur compteur = readCompteur(assurance.getRubriqueId(), complexSortieCgas.getAffiliation()
                        .getAffilieNumero(), Integer.toString(anneePassageFacturation), idRole);

                // si compteur est null cela veut dire qu'on a jamais facturé quelque chose à l'affilié
                FWCurrency montantDejaFacture = null;
                if (compteur == null) {
                    montantDejaFacture = new FWCurrency(0);
                } else {
                    montantDejaFacture = new FWCurrency(compteur.getCumulCotisation());
                    montantDejaFacture.negate();
                }

                FAEnteteFacture enteteFacture = findEnteteFacturation(passageFactu.getIdPassage(), complexSortieCgas
                        .getAffiliation().getIdTiers(), complexSortieCgas.getAffiliation().getAffilieNumero(), idRole);
                createAfact(passageFactu.getIdPassage(), enteteFacture, complexSortieCgas.getDecisionCgas(), assurance,
                        montantDejaFacture.toString(), complexSortieCgas.getDecisionCgas().getDateDebut(),
                        complexSortieCgas.getDecisionCgas().getDateFin(), null, null);

            }
            // si c'est pour l'année en cours on fait un afact en tableau
            else if (anneeSortie == anneePassageFacturation) {
                int nbMoisAFacturer = calculerNbMoisAFacturer(complexSortieCgas.getDecisionCgas(), complexSortieCgas
                        .getAffiliation().getPeriodicite(), dernierPassagePeriodique);

                // montant initial pour le tableau d'afact
                FWCurrency montantInitial = new FWCurrency(JANumberFormatter.round(new FWCurrency(complexSortieCgas
                        .getDecisionCgas().getCotisationMensuelle()).doubleValue() * nbMoisAFacturer, 0.05, 2,
                        JANumberFormatter.NEAR));

                // montant déjà facturé
                CACompteur compteur = readCompteur(assurance.getRubriqueId(), complexSortieCgas.getAffiliation()
                        .getAffilieNumero(), Integer.toString(anneePassageFacturation), idRole);

                // si compteur est null cela veut dire qu'on a jamais facturé quelque chose à l'affilié
                FWCurrency montantDejaFacture = null;
                if (compteur == null) {
                    montantDejaFacture = new FWCurrency(0);
                } else {
                    montantDejaFacture = new FWCurrency(compteur.getCumulCotisation());
                }

                // montant à facturer
                FWCurrency montantAFacturer = new FWCurrency(JANumberFormatter.round(montantInitial.doubleValue()
                        - montantDejaFacture.doubleValue(), 0.05, 2, JANumberFormatter.NEAR));

                String debutPeriode = complexSortieCgas.getDecisionCgas().getDateDebut();
                String finPeriode = calculerFinPeriodeAfact(debutPeriode, nbMoisAFacturer);
                FAEnteteFacture enteteFacture = findEnteteFacturation(passageFactu.getIdPassage(), complexSortieCgas
                        .getAffiliation().getIdTiers(), complexSortieCgas.getAffiliation().getAffilieNumero(), idRole);
                createAfact(passageFactu.getIdPassage(), enteteFacture, complexSortieCgas.getDecisionCgas(), assurance,
                        montantAFacturer.toString(), debutPeriode, finPeriode, montantInitial.toString(),
                        montantDejaFacture.toString());

            }

            // mise à jour de l'état de la sortie cgas
            complexSortieCgas.getSortieCgas().setEtat(ARSortieEtat.COMPTABILISEE.getCodeSystem());
            listSortieCgasFacturee.add(complexSortieCgas);
        }
        return listSortieCgasFacturee;
    }

    private AFAssurance findAssuranceCGAS() throws Exception {
        try {
            AFAssuranceManager assuranceManager = new AFAssuranceManager();
            assuranceManager.setSession(getSession());
            assuranceManager.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
            assuranceManager.setForTypeAssurance(CodeSystem.TYPE_ASS_CGAS);
            assuranceManager.find();
            if (assuranceManager.getSize() == 1) {
                return (AFAssurance) assuranceManager.getFirstEntity();
            } else {
                throw new Exception("assurance cgas not found !");
            }
        } catch (Exception e) {
            throw new Exception("unable to find assurance CGAS");
        }
    }

    private ComplexSortieCGASDecisionCGASAffiliationSearchModel findComplexSortieCgas(
            ComplexSortieCGASDecisionCGASAffiliationSearchModel complexSortieCgasSearchModel) throws Exception {
        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // appel du service
            complexSortieCgasSearchModel = AriesServiceLocator.getSortieCGASService().search(
                    complexSortieCgasSearchModel);
        } catch (Exception e) {
            throw new Exception("unable to find complexSortieCgas");
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        return complexSortieCgasSearchModel;
    }

    /**
     * Retourne le compte annexe de l'affilié
     * 
     * @param numeroAffilie
     * @param idRole
     * @return
     * @throws Exception
     */
    private CACompteAnnexe findCompteAnnexe(String numeroAffilie, String idRole) throws Exception {
        CACompteAnnexeManager compteAnnexeManager = new CACompteAnnexeManager();
        compteAnnexeManager.setSession(getSession());
        compteAnnexeManager.setForIdRole(idRole);
        compteAnnexeManager.setForIdExterneRole(numeroAffilie);
        try {
            compteAnnexeManager.find();
        } catch (Exception e) {
            throw new Exception("unable to find compte annexe for numeroAffilie : " + numeroAffilie);
        }

        if (compteAnnexeManager.size() > 0) {
            return (CACompteAnnexe) compteAnnexeManager.getFirstEntity();
        } else {
            return null;
        }
    }

    /**
     * Retourne la liste des décisions CGAS par rapport au searchModel passé en paramètre
     * 
     * @param decisionCgasSearchModel
     * @return
     * @throws Exception
     */
    private DecisionCGASSearchModel findDecisionCgas(DecisionCGASSearchModel decisionCgasSearchModel) throws Exception {
        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // appel du service
            decisionCgasSearchModel = AriesServiceLocator.getDecisionCGASService().search(decisionCgasSearchModel);
        } catch (Exception e) {
            throw new Exception("unable to find DecisionCgas");
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        return decisionCgasSearchModel;
    }

    /**
     * Retourne le dernier passage périodique CAP/CGAS. retourne null si aucun passage trouvé
     * 
     * @return
     * @throws Exception
     */
    private FAPassage findDernierPassagePeriodique() throws Exception {
        FAPassage passagePeriodique = null;

        FAPassageModuleManager passageModuleManager = new FAPassageModuleManager();
        passageModuleManager.setSession(getSession());
        passageModuleManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_PERIODIQUE_CAP_CGAS);
        passageModuleManager.setForExceptStatus(FAPassage.CS_ETAT_ANNULE);
        passageModuleManager.changeManagerSize(BManager.SIZE_NOLIMIT);
        passageModuleManager.orderByDateFacturationDecroissant();
        passageModuleManager.find();
        if (passageModuleManager.size() > 0) {
            for (int i = 0; i < passageModuleManager.size(); i++) {
                FAPassageModule passageModule = (FAPassageModule) passageModuleManager.get(i);
                if (FAModulePassage.CS_ACTION_GENERE.equals(passageModule.getIdAction())
                        || FAModulePassage.CS_ACTION_COMPTABILISE.equals(passageModule.getIdAction())) {
                    passagePeriodique = new FAPassage();
                    passagePeriodique.setSession(getSession());
                    passagePeriodique.setIdPassage(passageModule.getIdPassage());
                    passagePeriodique.retrieve();
                    break;
                }
            }
        }
        return passagePeriodique;
    }

    /**
     * Retourne l'entête de facturation (en crée une si celle-ci n'existe pas)
     * 
     * @param idPassage
     * @param idTiers
     * @param numeroAffilie
     * @param idRole
     * @return
     * @throws Exception
     */
    private FAEnteteFacture findEnteteFacturation(String idPassage, String idTiers, String numeroAffilie, String idRole)
            throws Exception {
        try {
            // Recherche de l'entête facture
            FAEnteteFactureManager enteteFactureManager = new FAEnteteFactureManager();
            enteteFactureManager.setSession(getSession());
            enteteFactureManager.setForIdPassage(idPassage);
            enteteFactureManager.setForIdTiers(idTiers);
            enteteFactureManager.setForIdExterneRole(numeroAffilie);
            enteteFactureManager.find();

            // si aucune entete trouvée -> création de l'entete
            if (enteteFactureManager.size() == 0) {
                FAEnteteFacture nouvelleEnteteFacture = new FAEnteteFacture();
                nouvelleEnteteFacture.setSession(getSession());
                nouvelleEnteteFacture.setIdPassage(idPassage);
                nouvelleEnteteFacture.setIdTiers(idTiers);
                nouvelleEnteteFacture.setIdRole(idRole);
                nouvelleEnteteFacture.setIdExterneRole(numeroAffilie);
                nouvelleEnteteFacture.setIdTypeFacture(APISection.ID_TYPE_SECTION_DECOMPTE_CAP_CGAS);
                nouvelleEnteteFacture.setIdSousType(APISection.ID_CATEGORIE_SECTION_DECISION_CAP_CGAS);

                // création du numéro de section unique
                BSession sessionOsiris = AFProcessFacturationDecisionCGAS.getSessionOsiris(getSession());
                String numeroFacture = CAUtil.creerNumeroSectionUnique(sessionOsiris, getTransaction(), idRole,
                        numeroAffilie, nouvelleEnteteFacture.getIdTypeFacture(),
                        new Integer(JACalendar.getYear(JACalendar.todayJJsMMsAAAA())).toString(),
                        nouvelleEnteteFacture.getIdSousType());
                nouvelleEnteteFacture.setIdExterneFacture(numeroFacture);
                nouvelleEnteteFacture.setNonImprimable(new Boolean(false));
                nouvelleEnteteFacture.initDefaultPlanValue(idRole);
                nouvelleEnteteFacture.add();

                return nouvelleEnteteFacture;
            } else {
                FAEnteteFacture enteteFacture = (FAEnteteFacture) enteteFactureManager.getFirstEntity();
                return enteteFacture;
            }
        } catch (Exception e) {
            throw new Exception("unable to read or create entete facture");
        }
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("PROCESS_FACTURATION_DECISION_CGAS_ERREUR");
        } else {
            return getSession().getLabel("PROCESS_FACTURATION_DECISION_CGAS_SUCCES");
        }
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    public IFAPassage getPassageFacturation() {
        return passageFacturation;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Ouvre un nouveau passage de facturation pour la facturation des décisions CAP/CGAS si il n'en existe pas déjà un.
     * ATTENTION, cette méthode utilise sa propre transaction et la commit directement
     * 
     * @param passageFactu
     * @throws Exception
     */
    private void openNewPassageIfNotExist(IFAPassage passageFactu) throws Exception {
        BTransaction transaction = null;
        try {
            transaction = new BTransaction(getSession());
            transaction.openTransaction();

            int nbJourToAdd = ((CPApplication) GlobazSystem.getApplication("PHENIX")).getAddJourDateFacturation();
            FAPassageModuleManager modulePassageManager = new FAPassageModuleManager();
            modulePassageManager.setSession(getSession());
            modulePassageManager.setForStatus(FAPassage.CS_ETAT_OUVERT);
            modulePassageManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_DECISION_CAP_CGAS);
            modulePassageManager.find(transaction);

            // si aucun passage ouvert -> on en ouvre un
            if (modulePassageManager.size() == 0) {
                FAPassage newPassage = new FAPassage();
                newPassage.copyDataFromEntity((FAPassage) passageFactu);
                newPassage.setIdPassage(null);
                newPassage.setStatus(FAPassage.CS_ETAT_OUVERT);
                newPassage.setEstVerrouille(false);
                newPassage.setDateFacturation(addDaysSkipWeekend(passageFactu.getDateFacturation(), nbJourToAdd));
                newPassage.add(transaction);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            getMemoryLog().logMessage(getSession().getLabel("PROCESS_FACTURATION_DECISION_CGAS_JOURNAL_NOT_CREATE"),
                    FWMessage.INFORMATION, this.getClass().toString());
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    private AFAffiliation readAffiliation(String idAffiliation) throws Exception {
        try {
            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession(getSession());
            affiliation.setId(idAffiliation);
            affiliation.retrieve();
            return affiliation;
        } catch (Exception e) {
            throw new Exception("unable to retrieve affiliation for id :" + idAffiliation);
        }
    }

    /**
     * Retourne le compteur de l'affilié
     * 
     * @param rubriqueId
     * @param numeroAffilie
     * @param annee
     * @param idRole
     * @return
     * @throws Exception
     */
    private CACompteur readCompteur(String rubriqueId, String numeroAffilie, String annee, String idRole)
            throws Exception {
        // chargement du compte annexe si le compte annexe n'existe pas on retourne null
        CACompteAnnexe compteAnnexe = findCompteAnnexe(numeroAffilie, idRole);
        if (compteAnnexe == null) {
            return null;
        }

        // chargement du compteur
        CACompteur compteur = new CACompteur();
        compteur.setSession(getSession());
        compteur.setAlternateKey(CACompteur.AK_CPTA_RUB_ANNEE);
        compteur.setAnnee(annee);
        compteur.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
        compteur.setIdRubrique(rubriqueId);
        try {
            compteur.retrieve();
        } catch (Exception e) {
            throw new Exception("unable to find compteur for numeroAffilie : " + numeroAffilie);
        }

        return compteur;
    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }

    public void setPassageFacturation(IFAPassage passageFacturation) {
        this.passageFacturation = passageFacturation;
    }

    /**
     * Mise à jour d'une liste de décisions (ne recalcul pas la cotisation)
     * 
     * @param listDecision
     * @throws Exception
     */
    private void updateDecisions(List<SimpleDecisionCGAS> listDecision) throws Exception {
        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // parcours de la liste et mise à jour des décisions
            for (SimpleDecisionCGAS decision : listDecision) {
                // appel du service
                AriesServiceLocator.getDecisionCGASService().updateWithoutCalculCotis(decision);
            }
        } catch (Exception e) {
            throw new Exception("unable to update list of decisionCgas");
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    /**
     * Mise à jour d'une liste de sorties
     * 
     * @param listSortie
     * @throws Exception
     */
    private void updateSorties(List<ComplexSortieCGASDecisionCGASAffiliation> listSortie) throws Exception {
        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // parcours de la liste et mise à jour des sorties
            for (ComplexSortieCGASDecisionCGASAffiliation complexSortie : listSortie) {

                if (!ARDecisionEtat.SUPPRIMEE.equals(complexSortie.getDecisionCgas().getEtat())) {

                    // appel du service
                    complexSortie.getDecisionCgas().setEtat(ARDecisionEtat.COMPTABILISEE.getCodeSystem());
                    complexSortie.setDecisionCgas(AriesServiceLocator.getDecisionCGASService()
                            .updateWithoutCalculCotis(complexSortie.getDecisionCgas()));

                }

                complexSortie.setSortieCgas(AriesServiceLocator.getSortieCGASService().update(
                        complexSortie.getSortieCgas()));
            }
        } catch (Exception e) {
            throw new Exception("unable to update list decisionCgas");
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

}
