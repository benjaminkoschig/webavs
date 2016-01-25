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
 * Processus de facturation des d�sisions CAP
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

            // On ouvre un nouveau journal de facturation. Se fait dans une transaction sp�cifique car doit se faire
            // dans tous les cas
            openNewPassageIfNotExist(passageFacturation);

            // recherche du dernier passage de facturation p�riodique
            FAPassage dernierPassagePeriodique = findDernierPassagePeriodique();

            // facturation des d�cision CAP (sans les r�ctificatives)
            List<SimpleDecisionCAP> listDecisionFacturee = facturerDecisionCap(passageFacturation,
                    dernierPassagePeriodique);

            // facturation des sorties CAP
            List<ComplexSortieCAPDecisionCAPAffiliation> listSortieFacturee = facturerSortieCap(passageFacturation,
                    dernierPassagePeriodique);

            // mise � jour des d�cisions et des sorties factur�es
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
            // pas de r�troactif � facturer
            nbMoisAFacturer = 0;
        } else if (moisFinDecision <= moisDernierPassagePeriodique) {
            // cas r�troactif il faut facturer toute la d�cision
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

                // il est possible que la derni�re facturation trimestrielle soit inf�rieure au mois de d�but de la
                // d�cision
                // dans ce cas il n'y a pas de r�troactif � facturer
                if (nbMoisAFacturer < 0) {
                    nbMoisAFacturer = 0;
                }

            } else if (periodiciteAffiliation.equals(CodeSystem.PERIODICITE_ANNUELLE)) {

                if (moisDernierPassagePeriodique < 12) {
                    // pas de r�troactif � g�rer la p�riodique s'occupe du cas
                    nbMoisAFacturer = 0;
                } else {
                    // plus de p�riodique il faut facturer la totalit� de la d�cision
                    nbMoisAFacturer = nbMoisDecision;
                }
            } else {
                throw new IllegalStateException("la p�riodicit� d'affiliation n a pas �t� trouv�e : "
                        + decision.getIdAffiliation());
            }

        }

        return nbMoisAFacturer;
    }

    /**
     * G�n�ration d'un afact en facturation. Si les param�tres montantInitial et montantDejaFacture ne sont pas null on
     * cr�e un afact en tableau
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

        // utilis� lors de la facturation d'une d�cision r�troactive (facturation de la totalit� de la d�cision
        // cotisationPeriode)
        // afin de retrouver le montant AF initial
        if (nbMoisAFacturer == null) {
            // le nb de mois � facturer est le nombre de mois entre le d�but et la fin de la d�cision
            int moisDebutDecision = JACalendar.getMonth(decisionCap.getDateDebut());
            int moisFinDecision = JACalendar.getMonth(decisionCap.getDateFin());
            nbMoisAFacturer = (moisFinDecision - moisDebutDecision) + 1;
        }

        // Cas avec AF
        if (isAssuranceCAPWithAF(assurance)) {
            String idTypeAfactAF = CodeSystem.TYPE_FACT_FACT_STANDARD;
            double montantAFDejaFacture = 0;
            CARubrique rubriqueAllocationEnfant = loadRubriqueAllocationEnfantCAP(decisionCap.getAnnee());

            // Cas radiation avec extourne d'une d�cision pour l'ann�e courante
            // On rembourse ce qui a �t� factur�
            // l'utilisateur reprendra une d�cision allant du 01.01.ann��Radiation jusq'� la fin du mois correspondant �
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
            // Cas radiation avec extourne d'une d�cision pour une ann�e ant�rieure
            // Comme les compteurs des ann�es ant�rieures n'ont pas �t� repris on extourne simplement la totalit� de la
            // d�cision (cotisationPeriode)
            // Afin de splitter l'extourne en une partie cotisation et une partie AF il faut ressortir le montant AF de
            // la d�cision
            // Le montantAFInitial est positif. On le rend n�gatif afin d'augmenter le montant rembours�
            // puis dans createAfactAFCAP on refait * -1 ce qui produit un afact de restitution des AF vers�es
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
            // Cas facturation de d�cisions et facturation de d�cisions r�ctificatives
            else {

                double montantAFInitial = 0;

                // d�cision r�ctificative pour une ann�e ant�rieure
                // on facture la diff�rence entre la cotisationPeriodeDecisionRectificative et
                // cotisationPeriodeDecisionRectifiee
                // afin de splitter cette diff�rence en un montant de cotisation et un montant AF
                // il faut extraire le montant AF qui correspond � montantAFDecisionRectificative -
                // montantAFDecisionRectifiee
                if (!JadeStringUtil.isBlankOrZero(decisionCap.getIdDecisionRectifiee()) && calculAFRectif) {
                    montantAFInitial = getMontantAFAFacturerForSortieRectificative(decisionCap);
                }
                // d�cisions pour une ann�e ant�rieure
                // d�cisions pour l'ann�e en cours
                // d�cisions r�ctificatives pour l'ann�e en cours
                // il s'agit de 3 cas o� on retrouve le montant AF initial d'apr�s la d�cision et le nombre de mois �
                // facturer
                else {
                    montantAFInitial = getMontantAFAFacturer(decisionCap, nbMoisAFacturer);
                }

                // d�cisions r�ctificatives pour l'ann�e en cours
                // seul cas o� on cr�e un afact en tableau
                // afin de montrer ce qu'on aurait d� facturer (nouvelle d�cision)
                // ce qui a d�j� �t� factur� (compteurs)
                // et ce qu'il reste � facturer (diff�rence)
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
                // d�cisions pour une ann�e ant�rieure
                // d�cisions pour l'ann�e en cours
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

            // cr�ation de l'afact
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

            // si afact en tableau on set le montant initial et le montant d�j� factur�
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
            // Si montantAFAFacturer est n�gatif cela signifie que trop d'AF ont �t� vers�e et il faut donc avoir un
            // afact
            // positif
            lineFacture.setMontantFacture(Double.toString(JANumberFormatter.round(montantAFAFacturer * -1, 0.05, 2,
                    JANumberFormatter.NEAR)));

            // d�cisions r�ctificatives pour l'ann�e en cours
            // comme on travail en positif il est normal de faire * -1 puisqu'il s'agit d'une allocation
            // exemple :
            // montant initial : 400 (AF � verser)
            // montant d�j� vers� : 300
            // montant restant � verser : 100
            // pour l'afact -400(montant initial) -300(d�j� vers�) -100(� verser)
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
     * Permet de facturer les d�cisions CAP du passage de facturation associ� au process. ATTENTION, cette m�htode ne
     * traite pas les d�cisions rectificatives (celles-ci sont trait�es via la facturation des sorties)
     * 
     * @param anneePassageFacturation
     * @param dernierPassagePeriodique
     * @throws Exception
     */
    private List<SimpleDecisionCAP> facturerDecisionCap(IFAPassage passageFactu, FAPassage dernierPassagePeriodique)
            throws Exception {
        List<SimpleDecisionCAP> listDecisionCapFacturee = new ArrayList<SimpleDecisionCAP>();
        int anneePassageFacturation = JACalendar.getYear(passageFactu.getDateFacturation());

        // recherche des d�cisions CAP � traiter. Ne pas prendre les r�ctificatives car trait�es via les sorties
        DecisionCAPSearchModel decisionCapSearchModel = new DecisionCAPSearchModel();
        decisionCapSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        decisionCapSearchModel.setForIdPassageFacturation(passageFactu.getIdPassage());
        decisionCapSearchModel.setForEtat(AUDecisionEtat.VALIDEE.getCodeSystem());
        decisionCapSearchModel.setInType(AUDecisionType.getListTypeNonRectif());
        decisionCapSearchModel = findDecisionCap(decisionCapSearchModel);

        // Parcours des d�cisions CAP
        for (JadeAbstractModel abstractModel : decisionCapSearchModel.getSearchResults()) {
            SimpleDecisionCAP decisionCap = (SimpleDecisionCAP) abstractModel;

            // recherche de l'affiliation li�e
            AFAffiliation affiliation = readAffiliation(decisionCap.getIdAffiliation());

            int anneeDecision = new Integer(decisionCap.getAnnee());

            AFAssurance assurance = readAssurance(decisionCap.getIdAssurance());
            String idRole = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication());

            // si il s'agit d'une d�cision pour une ann�e ant�rieure � l'ann�e du passage de facturation actuel
            // on facture la totalit� de la d�cision (cotisationPeriode)
            if (anneeDecision < anneePassageFacturation) {

                FAEnteteFacture enteteFacture = findEnteteFacturation(passageFactu.getIdPassage(),
                        affiliation.getIdTiers(), affiliation.getAffilieNumero(), idRole);
                createAfact(passageFactu.getIdPassage(), enteteFacture, decisionCap, assurance,
                        decisionCap.getCotisationPeriode(), decisionCap.getDateDebut(), decisionCap.getDateFin(), null,
                        null, null, false, false, false);

            }
            // si il s'agit d'une d�cision pour l'ann�e en cours
            // on facture en fonction de la p�riodicit� de l'affiliation et du dernier passage p�riodique effectu�
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

            // mise � jour de l'�tat de la d�cision
            decisionCap.setEtat(AUDecisionEtat.COMPTABILISEE.getCodeSystem());
            listDecisionCapFacturee.add(decisionCap);
        }
        return listDecisionCapFacturee;
    }

    /**
     * Permet de facturer les sorties CAP du passage de facturation associ� au process
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

            // cas radiation avec une sortie pour une ann�e ant�rieure � l'ann�e du passage de facturation actuel
            // on extourne la d�cision en facturant le montant de l'extourne qui correspond � la cotisation p�riode de
            // la d�cision
            if ((anneeSortie < anneePassageFacturation)
                    && AUDecisionEtat.SUPPRIMEE.equals(complexSortieCap.getDecisionCap().getEtat())) {

                FAEnteteFacture enteteFacture = findEnteteFacturation(passageFactu.getIdPassage(), complexSortieCap
                        .getAffiliation().getIdTiers(), complexSortieCap.getAffiliation().getAffilieNumero(), idRole);
                createAfact(passageFactu.getIdPassage(), enteteFacture, complexSortieCap.getDecisionCap(), assurance,
                        complexSortieCap.getSortieCap().getMontantExtourne(), complexSortieCap.getDecisionCap()
                                .getDateDebut(), complexSortieCap.getDecisionCap().getDateFin(), null, null, null,
                        true, false, true);

            }
            // cas d'une d�cision r�ctificative pour une ann�e ant�rieure
            // on facture le montant de l'extourne qui correspond � cotisationPeriodeDecisionRectificative -
            // cotisationPeriodeDecisionRectifiee
            else if (anneeSortie < anneePassageFacturation) {

                FAEnteteFacture enteteFacture = findEnteteFacturation(passageFactu.getIdPassage(), complexSortieCap
                        .getAffiliation().getIdTiers(), complexSortieCap.getAffiliation().getAffilieNumero(), idRole);
                createAfact(passageFactu.getIdPassage(), enteteFacture, complexSortieCap.getDecisionCap(), assurance,
                        complexSortieCap.getSortieCap().getMontantExtourne(), complexSortieCap.getDecisionCap()
                                .getDateDebut(), complexSortieCap.getDecisionCap().getDateFin(), null, null, null,
                        true, false, false);

            }
            // cas radiation avec une sortie pour l'ann�e courante
            // On extourne le montant factur� en se basant sur le compteur
            else if ((anneeSortie == anneePassageFacturation)
                    && AUDecisionEtat.SUPPRIMEE.equals(complexSortieCap.getDecisionCap().getEtat())) {

                // montant d�j� factur�
                CACompteur compteur = readCompteur(assurance.getRubriqueId(), complexSortieCap.getAffiliation()
                        .getAffilieNumero(), Integer.toString(anneeSortie), idRole);

                // si compteur est null cela veut dire qu'on a jamais factur� quelque chose � l'affili�
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
            // cas d'une d�cision r�ctificative pour l'ann�e en cours
            // on calcul le montant qu'on devrait factur� d'apr�s la d�cision r�ctificative
            // puis on soustrait � ce montant le montant d�j� factur� en se basant sur le compteur
            // et on facture la diff�rence
            else if (anneeSortie == anneePassageFacturation) {
                int nbMoisAFacturer = calculerNbMoisAFacturer(complexSortieCap.getDecisionCap(), complexSortieCap
                        .getAffiliation().getPeriodicite(), dernierPassagePeriodique);

                // montant initial pour le tableau d'afact
                FWCurrency montantInitial = new FWCurrency(JANumberFormatter.round(new FWCurrency(complexSortieCap
                        .getDecisionCap().getCotisationMensuelle()).doubleValue() * nbMoisAFacturer, 0.05, 2,
                        JANumberFormatter.NEAR));

                // montant d�j� factur�
                CACompteur compteur = readCompteur(assurance.getRubriqueId(), complexSortieCap.getAffiliation()
                        .getAffilieNumero(), Integer.toString(anneeSortie), idRole);

                // si compteur est null cela veut dire qu'on a jamais factur� quelque chose � l'affili�
                FWCurrency montantDejaFacture = null;
                if (compteur == null) {
                    montantDejaFacture = new FWCurrency(0);
                } else {
                    montantDejaFacture = new FWCurrency(compteur.getCumulCotisation());
                }

                // montant � facturer
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

            // mise � jour de l'�tat de la sortie cap
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
     * Retourne le compte annexe de l'affili�
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
     * Retourne la liste des d�cisions CAP par rapport au searchModel pass� en param�tre
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
     * Retourne le dernier passage p�riodique CAP/CGAS. retourne null si aucun passage trouv�
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
     * Retourne l'ent�te de facturation (en cr�e une si celle-ci n'existe pas)
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
            // Recherche de l'ent�te facture
            FAEnteteFactureManager enteteFactureManager = new FAEnteteFactureManager();
            enteteFactureManager.setSession(getSession());
            enteteFactureManager.setForIdPassage(idPassage);
            enteteFactureManager.setForIdTiers(idTiers);
            enteteFactureManager.setForIdExterneRole(numeroAffilie);
            enteteFactureManager.find();

            // si aucune entete trouv�e -> cr�ation de l'entete
            if (enteteFactureManager.size() == 0) {
                FAEnteteFacture nouvelleEnteteFacture = new FAEnteteFacture();
                nouvelleEnteteFacture.setSession(getSession());
                nouvelleEnteteFacture.setIdPassage(idPassage);
                nouvelleEnteteFacture.setIdTiers(idTiers);
                nouvelleEnteteFacture.setIdRole(idRole);
                nouvelleEnteteFacture.setIdExterneRole(numeroAffilie);
                nouvelleEnteteFacture.setIdTypeFacture(APISection.ID_TYPE_SECTION_DECOMPTE_CAP_CGAS);
                nouvelleEnteteFacture.setIdSousType(APISection.ID_CATEGORIE_SECTION_DECISION_CAP_CGAS);

                // cr�ation du num�ro de section unique
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
     * Ouvre un nouveau passage de facturation pour la facturation des d�cisions CAP/CGAS si il n'en existe pas d�j� un.
     * ATTENTION, cette m�thode utilise sa propre transaction et la commit directement
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
     * Retourne le compteur de l'affili�
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
     * Mise � jour d'une liste de d�cisions (ne recalcul pas la cotisation de la d�cision)
     * 
     * @param listDecision
     * @throws Exception
     */
    private void updateDecisions(List<SimpleDecisionCAP> listDecision) throws Exception {
        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // parcours de la liste et mise � jour des d�cisions
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
     * Mise � jour d'une liste de sorties. Met �galement la d�cision joint � l'�tat "comptablis�e"
     * 
     * @param listSortie
     * @throws Exception
     */
    private void updateSorties(List<ComplexSortieCAPDecisionCAPAffiliation> listSortie) throws Exception {
        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // parcours de la liste et mise � jour des sorties
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
