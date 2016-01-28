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
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.utils.CAUtil;
import globaz.phenix.application.CPApplication;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.auriga.business.constantes.AUDecisionEtat;
import ch.globaz.auriga.business.constantes.AUDecisionType;
import ch.globaz.auriga.business.constantes.AUParametrePlageValeur;
import ch.globaz.auriga.business.constantes.AUSortieEtat;
import ch.globaz.auriga.business.models.ComplexSortieCAPDecisionCAPAffiliation;
import ch.globaz.auriga.business.models.ComplexSortieCAPDecisionCAPAffiliationSearchModel;
import ch.globaz.auriga.business.models.DecisionCAPSearchModel;
import ch.globaz.auriga.business.models.SimpleDecisionCAP;
import ch.globaz.auriga.business.services.AurigaServiceLocator;
import ch.globaz.auriga.business.services.DecisionCAPService;
import ch.globaz.auriga.web.application.AUApplication;
import ch.globaz.common.business.services.ParametreService;

/**
 * Processus de facturation des désisions CAP
 * 
 * @author bjo
 * 
 */
public class AFProcessFacturationDecisionCAP extends BProcess {

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

    private String idModuleFacturation;

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

            // facturation des décision CAP (sans les réctificatives)
            List<SimpleDecisionCAP> listDecisionFacturee = facturerDecisionCap(passageFacturation,
                    dernierPassagePeriodique);

            // facturation des sorties CAP
            List<ComplexSortieCAPDecisionCAPAffiliation> listSortieFacturee = facturerSortieCap(passageFacturation,
                    dernierPassagePeriodique);

            // mise à jour des décisions et des sorties facturées
            if (!getSession().hasErrors() && !getSession().getCurrentThreadTransaction().hasErrors() && !isOnError()) {
                updateDecisions(listDecisionFacturee);
                updateSorties(listSortieFacturee);
            }
        } catch (Exception e) {
            getTransaction().addErrors(e.getMessage());
            getMemoryLog().logMessage(getSession().getLabel("PROCESS_FACTURATION_DECISION_CAP_ERROR"), FWMessage.FATAL,
                    this.getClass().toString());
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

    private int calculerNbMoisAFacturer(SimpleDecisionCAP decision, String periodiciteAffiliation,
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
     * @param decisionCap
     * @param assurance
     * @param montantFacture
     * @param debutPeriode
     * @param finPeriode
     * @param montantInitial
     * @param montantDejaFacture
     * @throws Exception
     */
    private void createAfact(String idPassage, FAEnteteFacture enteteFacture, SimpleDecisionCAP decisionCap,
            AFAssurance assurance, String montantFacture, String debutPeriode, String finPeriode,
            String montantInitial, String montantDejaFacture, Integer nbMoisAFacturer, boolean calculAFRectif,
            boolean isExtourneAnneeCourante, boolean isExtourneAnneeAntrieure) throws Exception {

        // utilisé lors de la facturation d'une décision rétroactive (facturation de la totalité de la décision
        // cotisationPeriode)
        // afin de retrouver le montant AF initial
        if (nbMoisAFacturer == null) {
            // le nb de mois à facturer est le nombre de mois entre le début et la fin de la décision
            int moisDebutDecision = JACalendar.getMonth(decisionCap.getDateDebut());
            int moisFinDecision = JACalendar.getMonth(decisionCap.getDateFin());
            nbMoisAFacturer = (moisFinDecision - moisDebutDecision) + 1;
        }

        // Cas avec AF
        if (isAssuranceCAPWithAF(assurance)) {
            String idTypeAfactAF = CodeSystem.TYPE_FACT_FACT_STANDARD;
            double montantAFDejaFacture = 0;
            CARubrique rubriqueAllocationEnfant = loadRubriqueAllocationEnfantCAP(decisionCap.getAnnee());

            // Cas radiation avec extourne d'une décision pour l'année courante
            // On rembourse ce qui a été facturé
            // l'utilisateur reprendra une décision allant du 01.01.annééRadiation jusq'à la fin du mois correspondant à
            // la date de radiation
            if (isExtourneAnneeCourante) {
                CACompteur compteurAF = readCompteur(rubriqueAllocationEnfant.getIdRubrique(),
                        enteteFacture.getIdExterneRole(), decisionCap.getAnnee(), enteteFacture.getIdRole());
                if (compteurAF != null) {
                    montantAFDejaFacture = Double.valueOf(compteurAF.getCumulCotisation());
                    double montantAFForExtourne = montantAFDejaFacture * -1;
                    createAfactAFCAPForExtourne(idPassage, enteteFacture.getIdEntete(), decisionCap.getAnnee(),
                            debutPeriode, finPeriode, montantAFForExtourne, idTypeAfactAF, idModuleFacturation,
                            rubriqueAllocationEnfant.getIdRubrique());
                }

            }
            // Cas radiation avec extourne d'une décision pour une année antérieure
            // Comme les compteurs des années antérieures n'ont pas été repris on extourne simplement la totalité de la
            // décision (cotisationPeriode)
            // Afin de splitter l'extourne en une partie cotisation et une partie AF il faut ressortir le montant AF de
            // la décision
            // Le montantAFInitial est positif. On le rend négatif afin d'augmenter le montant remboursé
            // puis dans createAfactAFCAP on refait * -1 ce qui produit un afact de restitution des AF versées
            else if (isExtourneAnneeAntrieure) {
                double montantAFInitial = 0;
                montantAFInitial = getMontantAFAFacturer(decisionCap, nbMoisAFacturer);
                montantAFInitial = montantAFInitial * -1;

                FWCurrency montantFactureCotisationCorrige = new FWCurrency(Double.valueOf(montantFacture)
                        .doubleValue() + montantAFInitial);
                montantFacture = montantFactureCotisationCorrige.toStringFormat();

                createAfactAFCAP(idPassage, enteteFacture.getIdEntete(), decisionCap.getAnnee(), debutPeriode,
                        finPeriode, montantAFInitial, Math.abs(montantAFDejaFacture), idTypeAfactAF,
                        idModuleFacturation, rubriqueAllocationEnfant.getIdRubrique());

            }
            // Cas facturation de décisions et facturation de décisions réctificatives
            else {

                double montantAFInitial = 0;

                // décision réctificative pour une année antérieure
                // on facture la différence entre la cotisationPeriodeDecisionRectificative et
                // cotisationPeriodeDecisionRectifiee
                // afin de splitter cette différence en un montant de cotisation et un montant AF
                // il faut extraire le montant AF qui correspond à montantAFDecisionRectificative -
                // montantAFDecisionRectifiee
                if (!JadeStringUtil.isBlankOrZero(decisionCap.getIdDecisionRectifiee()) && calculAFRectif) {
                    montantAFInitial = getMontantAFAFacturerForSortieRectificative(decisionCap);
                }
                // décisions pour une année antérieure
                // décisions pour l'année en cours
                // décisions réctificatives pour l'année en cours
                // il s'agit de 3 cas où on retrouve le montant AF initial d'après la décision et le nombre de mois à
                // facturer
                else {
                    montantAFInitial = getMontantAFAFacturer(decisionCap, nbMoisAFacturer);
                }

                // décisions réctificatives pour l'année en cours
                // seul cas où on crée un afact en tableau
                // afin de montrer ce qu'on aurait dû facturer (nouvelle décision)
                // ce qui a déjà été facturé (compteurs)
                // et ce qu'il reste à facturer (différence)
                if ((montantInitial != null) && (montantDejaFacture != null)) {

                    idTypeAfactAF = FAAfact.CS_AFACT_TABLEAU;

                    FWCurrency montantInitialCotisationCorrige = new FWCurrency(Double.valueOf(montantInitial)
                            .doubleValue() + montantAFInitial);
                    FWCurrency montantFactureCotisationCorrige = new FWCurrency(
                            montantInitialCotisationCorrige.doubleValue()
                                    - Double.valueOf(montantDejaFacture).doubleValue());
                    montantInitial = montantInitialCotisationCorrige.toStringFormat();
                    montantFacture = montantFactureCotisationCorrige.toStringFormat();

                    CACompteur compteurAF = readCompteur(rubriqueAllocationEnfant.getIdRubrique(),
                            enteteFacture.getIdExterneRole(), decisionCap.getAnnee(), enteteFacture.getIdRole());
                    if (compteurAF != null) {
                        montantAFDejaFacture = Double.valueOf(compteurAF.getCumulCotisation());
                    }
                }
                // décisions pour une année antérieure
                // décisions pour l'année en cours
                else {

                    FWCurrency montantFactureCotisationCorrige = new FWCurrency(Double.valueOf(montantFacture)
                            .doubleValue() + montantAFInitial);
                    montantFacture = montantFactureCotisationCorrige.toStringFormat();

                }

                createAfactAFCAP(idPassage, enteteFacture.getIdEntete(), decisionCap.getAnnee(), debutPeriode,
                        finPeriode, montantAFInitial, Math.abs(montantAFDejaFacture), idTypeAfactAF,
                        idModuleFacturation, rubriqueAllocationEnfant.getIdRubrique());
            }
        }

        if (new FWCurrency(montantFacture).doubleValue() != 0) {

            // création de l'afact
            FAAfact afact = new FAAfact();
            afact.setISession(getSession());
            afact.setIdEnteteFacture(enteteFacture.getIdEntete());
            afact.setIdPassage(idPassage);
            afact.setIdModuleFacturation(getIdModuleFacturation());
            afact.setIdTypeAfact(CodeSystem.TYPE_FACT_FACT_STANDARD);
            afact.setNonImprimable(Boolean.FALSE);
            afact.setNonComptabilisable(Boolean.FALSE);
            afact.setAQuittancer(Boolean.FALSE);
            afact.setAnneeCotisation(decisionCap.getAnnee());
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
                throw new Exception("unable to create afact for idDecision : " + decisionCap.getIdDecision());
            }
        }
    }

    private void createAfactAFCAP(String idPassage, String idEnteteFacture, String anneeCotisation,
            String debutPeriode, String finPeriode, double montantAFInitial, double montantAFDejaFactureAbsoluteValue,
            String typeAfact, String idModuleFacturation, String idRubriqueAllocationEnfant) throws Exception {

        double montantAFAFacturer = montantAFInitial - montantAFDejaFactureAbsoluteValue;

        if (montantAFAFacturer != 0) {

            FAAfact lineFacture = new FAAfact();
            lineFacture.setISession(getSession());
            lineFacture.setIdEnteteFacture(idEnteteFacture);
            lineFacture.setIdPassage(idPassage);
            lineFacture.setIdModuleFacturation(idModuleFacturation);
            lineFacture.setIdTypeAfact(typeAfact);
            lineFacture.setNonImprimable(Boolean.FALSE);
            lineFacture.setNonComptabilisable(Boolean.FALSE);
            lineFacture.setAQuittancer(Boolean.FALSE);
            lineFacture.setAnneeCotisation(anneeCotisation);
            lineFacture.setIdRubrique(idRubriqueAllocationEnfant);
            lineFacture.setDebutPeriode(debutPeriode);
            lineFacture.setFinPeriode(finPeriode);

            // Comme le montantAFInitial et le montantAFDejaFactureAbsoluteValue sont positifs
            // Il faut faire * -1 pour l'afact puisqu'il s'agit d'une allocation
            // Si montantAFAFacturer est négatif cela signifie que trop d'AF ont été versée et il faut donc avoir un
            // afact
            // positif
            lineFacture.setMontantFacture(Double.toString(JANumberFormatter.round(montantAFAFacturer * -1, 0.05, 2,
                    JANumberFormatter.NEAR)));

            // décisions réctificatives pour l'année en cours
            // comme on travail en positif il est normal de faire * -1 puisqu'il s'agit d'une allocation
            // exemple :
            // montant initial : 400 (AF à verser)
            // montant déjà versé : 300
            // montant restant à verser : 100
            // pour l'afact -400(montant initial) -300(déjà versé) -100(à verser)
            if (FAAfact.CS_AFACT_TABLEAU.equalsIgnoreCase(typeAfact)) {
                lineFacture.setMontantInitial(Double.toString(JANumberFormatter.round(montantAFInitial * -1, 0.05, 2,
                        JANumberFormatter.NEAR)));
                lineFacture.setMontantDejaFacture(Double.toString(JANumberFormatter.round(
                        montantAFDejaFactureAbsoluteValue * -1, 0.05, 2, JANumberFormatter.NEAR)));
            }

            lineFacture.add(getTransaction());

        }

    }

    private void createAfactAFCAPForExtourne(String idPassage, String idEnteteFacture, String anneeCotisation,
            String debutPeriode, String finPeriode, double montantAFAFacturer, String typeAfact,
            String idModuleFacturation, String idRubriqueAllocationEnfant) throws Exception {

        if (montantAFAFacturer != 0) {

            FAAfact lineFacture = new FAAfact();
            lineFacture.setISession(getSession());
            lineFacture.setIdEnteteFacture(idEnteteFacture);
            lineFacture.setIdPassage(idPassage);
            lineFacture.setIdModuleFacturation(idModuleFacturation);
            lineFacture.setIdTypeAfact(typeAfact);
            lineFacture.setNonImprimable(Boolean.FALSE);
            lineFacture.setNonComptabilisable(Boolean.FALSE);
            lineFacture.setAQuittancer(Boolean.FALSE);
            lineFacture.setAnneeCotisation(anneeCotisation);
            lineFacture.setIdRubrique(idRubriqueAllocationEnfant);
            lineFacture.setDebutPeriode(debutPeriode);
            lineFacture.setFinPeriode(finPeriode);

            lineFacture.setMontantFacture(Double.toString(JANumberFormatter.round(montantAFAFacturer, 0.05, 2,
                    JANumberFormatter.NEAR)));

            lineFacture.add(getTransaction());

        }

    }

    /**
     * Permet de facturer les décisions CAP du passage de facturation associé au process. ATTENTION, cette méhtode ne
     * traite pas les décisions rectificatives (celles-ci sont traitées via la facturation des sorties)
     * 
     * @param anneePassageFacturation
     * @param dernierPassagePeriodique
     * @throws Exception
     */
    private List<SimpleDecisionCAP> facturerDecisionCap(IFAPassage passageFactu, FAPassage dernierPassagePeriodique)
            throws Exception {
        List<SimpleDecisionCAP> listDecisionCapFacturee = new ArrayList<SimpleDecisionCAP>();
        int anneePassageFacturation = JACalendar.getYear(passageFactu.getDateFacturation());

        // recherche des décisions CAP à traiter. Ne pas prendre les réctificatives car traitées via les sorties
        DecisionCAPSearchModel decisionCapSearchModel = new DecisionCAPSearchModel();
        decisionCapSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        decisionCapSearchModel.setForIdPassageFacturation(passageFactu.getIdPassage());
        decisionCapSearchModel.setForEtat(AUDecisionEtat.VALIDEE.getCodeSystem());
        decisionCapSearchModel.setInType(AUDecisionType.getListTypeNonRectif());
        decisionCapSearchModel = findDecisionCap(decisionCapSearchModel);

        // Parcours des décisions CAP
        for (JadeAbstractModel abstractModel : decisionCapSearchModel.getSearchResults()) {
            SimpleDecisionCAP decisionCap = (SimpleDecisionCAP) abstractModel;

            // recherche de l'affiliation liée
            AFAffiliation affiliation = readAffiliation(decisionCap.getIdAffiliation());

            int anneeDecision = new Integer(decisionCap.getAnnee());

            AFAssurance assurance = readAssurance(decisionCap.getIdAssurance());
            String idRole = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication());

            // si il s'agit d'une décision pour une année antérieure à l'année du passage de facturation actuel
            // on facture la totalité de la décision (cotisationPeriode)
            if (anneeDecision < anneePassageFacturation) {

                FAEnteteFacture enteteFacture = findEnteteFacturation(passageFactu.getIdPassage(),
                        affiliation.getIdTiers(), affiliation.getAffilieNumero(), idRole);
                createAfact(passageFactu.getIdPassage(), enteteFacture, decisionCap, assurance,
                        decisionCap.getCotisationPeriode(), decisionCap.getDateDebut(), decisionCap.getDateFin(), null,
                        null, null, false, false, false);

            }
            // si il s'agit d'une décision pour l'année en cours
            // on facture en fonction de la périodicité de l'affiliation et du dernier passage périodique effectué
            else if (anneeDecision == anneePassageFacturation) {
                int nbMoisAFacturer = calculerNbMoisAFacturer(decisionCap, affiliation.getPeriodicite(),
                        dernierPassagePeriodique);

                FWCurrency montantAFacturer = new FWCurrency(JANumberFormatter.round(
                        new FWCurrency(decisionCap.getCotisationMensuelle()).doubleValue() * nbMoisAFacturer, 0.05, 2,
                        JANumberFormatter.NEAR));

                String debutPeriode = decisionCap.getDateDebut();
                String finPeriode = calculerFinPeriodeAfact(debutPeriode, nbMoisAFacturer);
                FAEnteteFacture enteteFacture = findEnteteFacturation(passageFactu.getIdPassage(),
                        affiliation.getIdTiers(), affiliation.getAffilieNumero(), idRole);
                createAfact(passageFactu.getIdPassage(), enteteFacture, decisionCap, assurance,
                        montantAFacturer.toString(), debutPeriode, finPeriode, null, null, nbMoisAFacturer, false,
                        false, false);

            }

            // mise à jour de l'état de la décision
            decisionCap.setEtat(AUDecisionEtat.COMPTABILISEE.getCodeSystem());
            listDecisionCapFacturee.add(decisionCap);
        }
        return listDecisionCapFacturee;
    }

    /**
     * Permet de facturer les sorties CAP du passage de facturation associé au process
     * 
     * @param anneePassageFacturation
     * @param dernierPassagePeriodique
     * @throws Exception
     */
    private List<ComplexSortieCAPDecisionCAPAffiliation> facturerSortieCap(IFAPassage passageFactu,
            FAPassage dernierPassagePeriodique) throws Exception {
        List<ComplexSortieCAPDecisionCAPAffiliation> listSortieCapFacturee = new ArrayList<ComplexSortieCAPDecisionCAPAffiliation>();
        int anneePassageFacturation = JACalendar.getYear(passageFactu.getDateFacturation());

        ComplexSortieCAPDecisionCAPAffiliationSearchModel complexSortieCapSearchModel = new ComplexSortieCAPDecisionCAPAffiliationSearchModel();
        complexSortieCapSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        complexSortieCapSearchModel.setForIdPassageFacturation(passageFactu.getIdPassage());
        complexSortieCapSearchModel.setForEtat(AUSortieEtat.NON_COMPTABILISEE.getCodeSystem());
        complexSortieCapSearchModel = findComplexSortieCap(complexSortieCapSearchModel);

        // parcours des sorties CAP
        for (JadeAbstractModel abstractModel : complexSortieCapSearchModel.getSearchResults()) {
            ComplexSortieCAPDecisionCAPAffiliation complexSortieCap = (ComplexSortieCAPDecisionCAPAffiliation) abstractModel;

            int anneeSortie = new Integer(complexSortieCap.getDecisionCap().getAnnee());

            AFAssurance assurance = readAssurance(complexSortieCap.getDecisionCap().getIdAssurance());
            String idRole = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication());

            // cas radiation avec une sortie pour une année antérieure à l'année du passage de facturation actuel
            // on extourne la décision en facturant le montant de l'extourne qui correspond à la cotisation période de
            // la décision
            if ((anneeSortie < anneePassageFacturation)
                    && AUDecisionEtat.SUPPRIMEE.equals(complexSortieCap.getDecisionCap().getEtat())) {

                FAEnteteFacture enteteFacture = findEnteteFacturation(passageFactu.getIdPassage(), complexSortieCap
                        .getAffiliation().getIdTiers(), complexSortieCap.getAffiliation().getAffilieNumero(), idRole);
                createAfact(passageFactu.getIdPassage(), enteteFacture, complexSortieCap.getDecisionCap(), assurance,
                        complexSortieCap.getSortieCap().getMontantExtourne(), complexSortieCap.getDecisionCap()
                                .getDateDebut(), complexSortieCap.getDecisionCap().getDateFin(), null, null, null,
                        true, false, true);

            }
            // cas d'une décision réctificative pour une année antérieure
            // on facture le montant de l'extourne qui correspond à cotisationPeriodeDecisionRectificative -
            // cotisationPeriodeDecisionRectifiee
            else if (anneeSortie < anneePassageFacturation) {

                FAEnteteFacture enteteFacture = findEnteteFacturation(passageFactu.getIdPassage(), complexSortieCap
                        .getAffiliation().getIdTiers(), complexSortieCap.getAffiliation().getAffilieNumero(), idRole);
                createAfact(passageFactu.getIdPassage(), enteteFacture, complexSortieCap.getDecisionCap(), assurance,
                        complexSortieCap.getSortieCap().getMontantExtourne(), complexSortieCap.getDecisionCap()
                                .getDateDebut(), complexSortieCap.getDecisionCap().getDateFin(), null, null, null,
                        true, false, false);

            }
            // cas radiation avec une sortie pour l'année courante
            // On extourne le montant facturé en se basant sur le compteur
            else if ((anneeSortie == anneePassageFacturation)
                    && AUDecisionEtat.SUPPRIMEE.equals(complexSortieCap.getDecisionCap().getEtat())) {

                // montant déjà facturé
                CACompteur compteur = readCompteur(assurance.getRubriqueId(), complexSortieCap.getAffiliation()
                        .getAffilieNumero(), Integer.toString(anneeSortie), idRole);

                // si compteur est null cela veut dire qu'on a jamais facturé quelque chose à l'affilié
                FWCurrency montantDejaFacture = null;
                if (compteur == null) {
                    montantDejaFacture = new FWCurrency(0);
                } else {
                    montantDejaFacture = new FWCurrency(compteur.getCumulCotisation());
                    montantDejaFacture.negate();
                }

                FAEnteteFacture enteteFacture = findEnteteFacturation(passageFactu.getIdPassage(), complexSortieCap
                        .getAffiliation().getIdTiers(), complexSortieCap.getAffiliation().getAffilieNumero(), idRole);
                createAfact(passageFactu.getIdPassage(), enteteFacture, complexSortieCap.getDecisionCap(), assurance,
                        montantDejaFacture.toString(), complexSortieCap.getDecisionCap().getDateDebut(),
                        complexSortieCap.getDecisionCap().getDateFin(), null, null, null, false, true, false);

            }
            // cas d'une décision réctificative pour l'année en cours
            // on calcul le montant qu'on devrait facturé d'après la décision réctificative
            // puis on soustrait à ce montant le montant déjà facturé en se basant sur le compteur
            // et on facture la différence
            else if (anneeSortie == anneePassageFacturation) {
                int nbMoisAFacturer = calculerNbMoisAFacturer(complexSortieCap.getDecisionCap(), complexSortieCap
                        .getAffiliation().getPeriodicite(), dernierPassagePeriodique);

                // montant initial pour le tableau d'afact
                FWCurrency montantInitial = new FWCurrency(JANumberFormatter.round(new FWCurrency(complexSortieCap
                        .getDecisionCap().getCotisationMensuelle()).doubleValue() * nbMoisAFacturer, 0.05, 2,
                        JANumberFormatter.NEAR));

                // montant déjà facturé
                CACompteur compteur = readCompteur(assurance.getRubriqueId(), complexSortieCap.getAffiliation()
                        .getAffilieNumero(), Integer.toString(anneeSortie), idRole);

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

                String debutPeriode = complexSortieCap.getDecisionCap().getDateDebut();
                String finPeriode = calculerFinPeriodeAfact(debutPeriode, nbMoisAFacturer);
                FAEnteteFacture enteteFacture = findEnteteFacturation(passageFactu.getIdPassage(), complexSortieCap
                        .getAffiliation().getIdTiers(), complexSortieCap.getAffiliation().getAffilieNumero(), idRole);
                createAfact(passageFactu.getIdPassage(), enteteFacture, complexSortieCap.getDecisionCap(), assurance,
                        montantAFacturer.toString(), debutPeriode, finPeriode, montantInitial.toString(),
                        montantDejaFacture.toString(), nbMoisAFacturer, false, false, false);

            }

            // mise à jour de l'état de la sortie cap
            complexSortieCap.getSortieCap().setEtat(AUSortieEtat.COMPTABILISEE.getCodeSystem());
            listSortieCapFacturee.add(complexSortieCap);
        }
        return listSortieCapFacturee;
    }

    private ComplexSortieCAPDecisionCAPAffiliationSearchModel findComplexSortieCap(
            ComplexSortieCAPDecisionCAPAffiliationSearchModel complexSortieCapSearchModel) throws Exception {
        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // appel du service
            complexSortieCapSearchModel = AurigaServiceLocator.getSortieCAPService()
                    .search(complexSortieCapSearchModel);
        } catch (Exception e) {
            throw new Exception("unable to find complexSortieCap");
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        return complexSortieCapSearchModel;
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
     * Retourne la liste des décisions CAP par rapport au searchModel passé en paramètre
     * 
     * @param decisionCapSearchModel
     * @return
     * @throws Exception
     */
    private DecisionCAPSearchModel findDecisionCap(DecisionCAPSearchModel decisionCapSearchModel) throws Exception {
        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // appel du service
            decisionCapSearchModel = AurigaServiceLocator.getDecisionCAPService().search(decisionCapSearchModel);
        } catch (Exception e) {
            throw new Exception("unable to find DecisionCap");
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        return decisionCapSearchModel;
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
                BSession sessionOsiris = AFProcessFacturationDecisionCAP.getSessionOsiris(getSession());
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
            return getSession().getLabel("PROCESS_FACTURATION_DECISION_CAP_ERREUR");
        } else {
            return getSession().getLabel("PROCESS_FACTURATION_DECISION_CAP_SUCCES");
        }
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    private double getMontantAFAFacturer(SimpleDecisionCAP decisionCAP, int nbMoisAFacturer) throws Exception {

        try {

            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            DecisionCAPService decisionCAPService = AurigaServiceLocator.getDecisionCAPService();

            double montantAFAFacturer = decisionCAPService.getMontantAFAFacturer(decisionCAP, nbMoisAFacturer);

            return montantAFAFacturer;

        } finally {

            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());

        }

    }

    private double getMontantAFAFacturerForSortieRectificative(SimpleDecisionCAP decisionCAPRectificative)
            throws Exception {

        try {

            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            DecisionCAPService decisionCAPService = AurigaServiceLocator.getDecisionCAPService();
            SimpleDecisionCAP decisionCAPRectifiee = decisionCAPService.read(decisionCAPRectificative
                    .getIdDecisionRectifiee());

            double montantAFAFacturer = decisionCAPService.getMontantAFAFacturerForSortieRectificative(
                    decisionCAPRectificative, decisionCAPRectifiee);

            return montantAFAFacturer;

        } finally {

            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());

        }

    }

    public IFAPassage getPassageFacturation() {
        return passageFacturation;
    }

    private boolean isAssuranceCAPWithAF(AFAssurance theAssurance) throws Exception {

        return CodeSystem.TYPE_ASS_CAP_10.equalsIgnoreCase(theAssurance.getTypeAssurance())
                || CodeSystem.TYPE_ASS_CAP_20.equalsIgnoreCase(theAssurance.getTypeAssurance());

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private CARubrique loadRubriqueAllocationEnfantCAP(String anneeCotisation) throws Exception {
        try {

            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            ParametreService parametreService = AurigaServiceLocator.getParametreService();
            String numeroRubriqueAllocationEnfantCAP = parametreService.getValeurAlpha(
                    AUParametrePlageValeur.RUBRIQUE_ALLOCATION_ENFANT, "01.01." + anneeCotisation,
                    AUApplication.DEFAULT_APPLICATION_AURIGA);

            CARubrique rubriqueAllocationEnfantCAP = new CARubrique();
            rubriqueAllocationEnfantCAP.setSession(getSession());
            rubriqueAllocationEnfantCAP.setIdExterne(numeroRubriqueAllocationEnfantCAP);
            rubriqueAllocationEnfantCAP.setAlternateKey(APIRubrique.AK_IDEXTERNE);
            rubriqueAllocationEnfantCAP.retrieve(getTransaction());

            return rubriqueAllocationEnfantCAP;

        } finally {

            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());

        }

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
            modulePassageManager.setForExceptIdPassage(passageFactu.getIdPassage());
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
            getMemoryLog().logMessage(getSession().getLabel("PROCESS_FACTURATION_DECISION_CAP_JOURNAL_NOT_CREATE"),
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

    private AFAssurance readAssurance(String idAssurance) throws Exception {
        try {
            AFAssurance assurance = new AFAssurance();
            assurance.setSession(getSession());
            assurance.setId(idAssurance);
            assurance.retrieve();
            return assurance;
        } catch (Exception e) {
            throw new Exception("unable to read assurance for id :" + idAssurance);
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
        // chargement du compte annexe, si le compte annexe n'existe pas on retourne null
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
     * Mise à jour d'une liste de décisions (ne recalcul pas la cotisation de la décision)
     * 
     * @param listDecision
     * @throws Exception
     */
    private void updateDecisions(List<SimpleDecisionCAP> listDecision) throws Exception {
        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // parcours de la liste et mise à jour des décisions
            for (SimpleDecisionCAP decision : listDecision) {
                // appel du service
                AurigaServiceLocator.getDecisionCAPService().updateWithoutCalculCotis(decision);
            }
        } catch (Exception e) {
            throw new Exception("unable to update list of decisionCap");
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    /**
     * Mise à jour d'une liste de sorties. Met également la décision joint à l'état "comptablisée"
     * 
     * @param listSortie
     * @throws Exception
     */
    private void updateSorties(List<ComplexSortieCAPDecisionCAPAffiliation> listSortie) throws Exception {
        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // parcours de la liste et mise à jour des sorties
            for (ComplexSortieCAPDecisionCAPAffiliation complexSortie : listSortie) {

                if (!AUDecisionEtat.SUPPRIMEE.equals(complexSortie.getDecisionCap().getEtat())) {
                    // appel du service
                    complexSortie.getDecisionCap().setEtat(AUDecisionEtat.COMPTABILISEE.getCodeSystem());
                    complexSortie.setDecisionCap(AurigaServiceLocator.getDecisionCAPService().updateWithoutCalculCotis(
                            complexSortie.getDecisionCap()));
                }

                complexSortie.setSortieCap(AurigaServiceLocator.getSortieCAPService().update(
                        complexSortie.getSortieCap()));
            }
        } catch (Exception e) {
            throw new Exception("unable to update list decisionCap");
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

}
