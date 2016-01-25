package globaz.phenix.toolbox;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.controller.FWController;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParameters;
import globaz.globall.parameters.FWParametersManager;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAStringFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.db.comptes.CARubrique;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.db.inscriptions.CIJournalManager;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPCotisationManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliation;
import globaz.phenix.db.principale.CPDecisionAffiliationCotisation;
import globaz.phenix.db.principale.CPDecisionAffiliationCotisationManager;
import globaz.phenix.db.principale.CPDecisionAffiliationTiers;
import globaz.phenix.db.principale.CPDecisionAgenceCommunale;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (30.04.2003 11:50:10)
 * 
 * @author: Administrator
 */
public class CPToolBox {
    // Mapping idCanton::User
    private static Map<String, String> m_cantons = new HashMap<String, String>();

    /**
     * Retourne le code spécial pour les CI selon la décision
     * 
     * @param CPDecision
     *            decision
     */
    static public String _returnCodeSpecial(CPDecision decision) throws java.lang.Exception {
        // Code spécial 01
        /*
         * Le 14.10.2011 - Test 1-7-0 if (CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())) { return
         * CIEcriture.CS_COTISATION_MINIMALE; } else
         */
        if (decision.isNonActif() && !JadeStringUtil.isIntegerEmpty(decision.getIdServiceSociale())) {
            return CIEcriture.CS_COTISATION_MINIMALE;
        } else if (CPDecision.CS_RENTIER.equalsIgnoreCase(decision.getGenreAffilie()) && !decision.isNonActif()) {
            return CIEcriture.CS_NONFORMATTEUR_INDEPENDANT;
        }
        return "";
    }

    /**
     * Retourne le code spécial pour les CI selon la décision
     * 
     * @param CPDecision
     *            decision
     */
    static public String _returnCodeSpecial(CPDecisionAffiliation decision) throws java.lang.Exception {
        // Code spécial 01
        /*
         * Le 14.10.2011 - Test 1-7-0 if (CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())) { return
         * CIEcriture.CS_COTISATION_MINIMALE; } else
         */
        if (decision.isNonActif() && !JadeStringUtil.isIntegerEmpty(decision.getIdServiceSociale())) {
            return CIEcriture.CS_COTISATION_MINIMALE;
        } else if (CPDecision.CS_RENTIER.equalsIgnoreCase(decision.getGenreAffilie()) && !decision.isNonActif()) {
            return CIEcriture.CS_NONFORMATTEUR_INDEPENDANT;
        }
        return "";
    }

    /**
     * Ajout ou radiation d'une cotisation pour une période donnée Dans le cas de la radiation, il faut penser à rouvrir
     * la cotisation Ex: Cotisation de 01.01.1996 à 0 devient si l'on doit stopper pour 2003 : 01.01.1996 au 31.12.2002
     * et 01.01.2004 à 0
     * 
     * @param String
     *            idAffiliation
     * @param String
     *            genre
     * @param String
     *            debutPeriode
     * @param String
     *            finPeriode
     * @param int operation (1 pour création, 2 pour radiation, 3 suppression)
     */
    public static void ajoutRadiationCotisation(String idAffiliation, String genre, String debutPeriode,
            String finPeriode, int operation, BSession session) throws Exception {
        // Création assurance pour la période donnée
        BSession sessionNaos = new BSession(AFApplication.DEFAULT_APPLICATION_NAOS);
        session.connectSession(sessionNaos);
        AFAffiliation aff = new AFAffiliation();
        aff.setSession(sessionNaos);
        if (operation == 1) {
            // Recherche cotisation avs pour avoir id planaffiliation, id
            // adhesion, id plancaisse
            AFCotisation newCoti = aff._cotisation(session.getCurrentThreadTransaction(), idAffiliation,
                    CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AVS_AI, debutPeriode, finPeriode, 1);
            // Recherche id assurance pour le genre
            AFAssuranceManager assMng = new AFAssuranceManager();
            assMng.setSession(sessionNaos);
            assMng.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
            assMng.setForTypeAssurance(genre);
            assMng.find();
            if (assMng.size() > 0) {
                // Insertion assurance
                newCoti.setAssuranceId(((AFAssurance) assMng.getFirstEntity()).getAssuranceId());
                newCoti.setMontantMensuel("");
                newCoti.setMontantTrimestriel("");
                newCoti.setMontantSemestriel("");
                newCoti.setMontantAnnuel("");
                newCoti.setAnneeDecision("");
                newCoti.setDateDebut(debutPeriode);
                newCoti.setDateFin(finPeriode);
                newCoti.setMotifFin(CodeSystem.MOTIF_FIN_DIV);
                newCoti.add(session.getCurrentThreadTransaction());
            }
        } else if (operation == 2) { // Radiation
            AFCotisation newCoti = aff._cotisation(session.getCurrentThreadTransaction(), idAffiliation,
                    CodeSystem.GENRE_ASS_PERSONNEL, genre, debutPeriode, finPeriode, 1);
            // Radiation pour l'exercice
            if (newCoti != null) {
                // Save date fin
                String saveDateFin = newCoti.getDateFin();
                String saveMotifFin = newCoti.getMotifFin();
                String dateFin = new JACalendarGregorian().addDays(debutPeriode, -1);
                if (BSessionUtil.compareDateFirstGreater(session, dateFin, newCoti.getDateDebut())) {
                    newCoti.setDateFin(dateFin);
                } else {
                    newCoti.setDateFin(newCoti.getDateDebut());
                }
                newCoti.setMotifFin(CodeSystem.MOTIF_FIN_DIV);
                newCoti.update(session.getCurrentThreadTransaction());
                // ré-ouverture à partir de l'exercice + 1 jour si affiliation
                // non radiée
                newCoti.setMotifFin(saveMotifFin);
                newCoti.setDateDebut(new JACalendarGregorian().addDays(finPeriode, 1));
                newCoti.setDateFin(saveDateFin);
                if (JadeStringUtil.isBlankOrZero(newCoti.getDateFin())
                        || BSessionUtil.compareDateFirstGreater(sessionNaos, newCoti.getDateFin(),
                                newCoti.getDateDebut())) {
                    newCoti.add(session.getCurrentThreadTransaction());
                }
            }
        } else if (operation == 3) { // Suppression création assurance
            // Recherche cotisation avs pour avoir id planaffiliation, id
            // adhesion, id plancaisse
            AFCotisation newCoti = aff._cotisation(session.getCurrentThreadTransaction(), idAffiliation,
                    CodeSystem.GENRE_ASS_PERSONNEL, genre, debutPeriode, finPeriode, 1);
            if (newCoti != null) {
                newCoti.delete(session.getCurrentThreadTransaction());
            }
        } else if (operation == 4) {
            // Suppression radiation => Annulation de ce qui a été fait avec
            // operation == 2
            // Suppression ré-ouverture période cotisation à partir de
            // l'exercice + 1 jour + Mise à jour de la période de l'exercice
            AFCotisation newCoti = aff._cotisation(session.getCurrentThreadTransaction(), idAffiliation,
                    CodeSystem.GENRE_ASS_PERSONNEL, genre, new JACalendarGregorian().addDays(finPeriode, 1),
                    new JACalendarGregorian().addDays(finPeriode, 2), 1);
            if (newCoti != null) {
                String saveDateFin = newCoti.getDateFin();
                String saveMotifFin = newCoti.getMotifFin();
                newCoti.delete(session.getCurrentThreadTransaction());
                newCoti = aff._cotisation(session.getCurrentThreadTransaction(), idAffiliation,
                        CodeSystem.GENRE_ASS_PERSONNEL, genre, new JACalendarGregorian().addDays(debutPeriode, -2),
                        new JACalendarGregorian().addDays(debutPeriode, -1), 1);
                if (newCoti == null) {
                    // Rechercher cas ou date début cotisation = date fin
                    // cotisation
                    newCoti = aff._cotisation(session.getCurrentThreadTransaction(), idAffiliation,
                            CodeSystem.GENRE_ASS_PERSONNEL, genre, debutPeriode, debutPeriode, 1);
                }
                if (newCoti != null) {
                    newCoti.setDateFin(saveDateFin);
                    newCoti.setMotifFin(saveMotifFin);
                    newCoti.update(session.getCurrentThreadTransaction());
                }
            }
        }
        if (!session.getCurrentThreadTransaction().hasErrors()) {
            session.getCurrentThreadTransaction().commit();
        } else {
            session.getCurrentThreadTransaction().rollback();
        }
    }

    /**
     * Recherche de l'année limite (utiler par ex pour l'inscription CI) - Paramètre.
     * 
     * @return String
     */
    public static String anneeLimite(BTransaction transaction) throws Exception {
        int anneeEncours = JACalendar.getYear(JACalendar.todayJJsMMsAAAA());
        String varAnnee = FWFindParameter.findParameter(transaction, "10500010", "ANNEELIMIT", "", "", 0);
        int anneeRelative = 0;
        if (!JadeStringUtil.isBlankOrZero(varAnnee)) {
            anneeRelative = Integer.parseInt(FWFindParameter.findParameter(transaction, "10500010", "ANNEELIMIT", "",
                    "", 0));
        }
        return Integer.toString(anneeEncours + anneeRelative);
    }

    /**
     * Return true si la cotisation a été calculée par la génération d'une communication fiscale
     * et si le montant est différent de zéro
     * Ne concerne que les indépendants
     * 
     * @param montantCotisation
     * @return
     * @throws Exception
     */
    public static boolean isCotisationCalculeeSelonDispositionLegale(String genreAffiliation, String idCommunication,
            String montantCotisation) throws Exception {
        if ((CodeSystem.TYPE_AFFILI_INDEP.equalsIgnoreCase(genreAffiliation) || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY
                .equalsIgnoreCase(genreAffiliation)) && !JadeStringUtil.isBlankOrZero(montantCotisation)) {
            return true;
        }

        return false;

    }

    /**
     * Method annualisationRevenu Annualise un montant selon une période définie sur 1 ou 2 ans en tenant compte lorsque
     * l'exercice est sur 2 ans que le total des 2 décisions faisant référence à l'exercice corresponde au total saisi
     * 
     * @param dateDebut
     * @param dateFin
     * @param anneeDeBase
     * @param montant
     * @return String
     */
    public static String annualisationRevenu(String dateDebutExercice, String dateFinExercice, String anneeDeBase,
            String montant) throws Exception {

        if (!JadeStringUtil.isEmpty(montant)) {
            montant = JANumberFormatter.deQuote(montant);
            float montantAnnualise = Float.parseFloat(montant);
            int moisDebut = 1;
            int moisFin = 12;
            int anneeDebut = 0;
            int anneeFin = 0;
            int anneeBase = 0;
            int nbMois = 0;
            int ecart = 0;
            anneeDebut = JACalendar.getYear(dateDebutExercice);
            anneeFin = JACalendar.getYear(dateFinExercice);
            // Si année de base est vide => anneeDebut = anneeFin, annualisation sur une année
            if (JadeStringUtil.isBlankOrZero(anneeDeBase)) {
                anneeBase = anneeDebut;
            } else {
                anneeBase = Integer.parseInt(anneeDeBase);
            }
            int exe = 0;
            // Détermination du nombre de mois de l'exercice compris dans
            // l'année de base
            if (anneeDebut == anneeBase) {
                moisDebut = JACalendar.getMonth(dateDebutExercice);
            } else {
                moisDebut = 1;
            }
            if (anneeFin == anneeBase) {
                exe = 2;
                moisFin = JACalendar.getMonth(dateFinExercice);
            } else {
                moisFin = 12;
            }
            ecart = (moisFin - moisDebut) + 1;
            // Détermination de la période de l'exercice
            moisDebut = JACalendar.getMonth(dateDebutExercice);
            moisFin = JACalendar.getMonth(dateFinExercice);
            if (anneeDebut != anneeFin) {
                moisFin = moisFin + 12;
            }
            nbMois = (moisFin - moisDebut) + 1;
            // Lorsque l'exercice est défini sur 2 ans, il fait référence
            // à 2 décisions. Pour éviter que le total de ces 2 décisions soit
            // différnt avac le total CI sans prorata (à cause des arrondis
            // au 100fr. inférieur), il faut prendre pour la 2ème décision la
            // différence
            // entre le total CI et le revenu déterminé pour la 1ère décision
            if (exe == 2) {
                float totalCI = JANumberFormatter.round(Float.parseFloat(montant), 100, 0, JANumberFormatter.INF);
                // Calcul prorata sur la 1ère décision (1ère année)
                int ecart1 = nbMois - ecart;
                montantAnnualise = (montantAnnualise * ecart1) / nbMois;
                // Prendre en compte l'arrondi de la 1ère décision
                montantAnnualise = JANumberFormatter.round(montantAnnualise, 100, 0, JANumberFormatter.INF);
                montantAnnualise = totalCI - montantAnnualise;
            } else {
                montantAnnualise = (montantAnnualise * ecart) / nbMois;
                montantAnnualise = JANumberFormatter.round(montantAnnualise, 100, 0, JANumberFormatter.INF);
            }
            return Float.toString(montantAnnualise);
        } else {
            return "0";
        }
    }

    /**
     * Permet de savoir si la caisse en question est la CCCVS
     * 
     * @return true si la caisse est la CCCVS, false sinon
     */
    public static boolean isCaisseCCCVS() throws Exception {
        CPApplication app = (CPApplication) GlobazServer.getCurrentSystem().getApplication(
                CPApplication.DEFAULT_APPLICATION_PHENIX);
        return app.isCaisseCCCVS();
    }

    /**
     * Method annualisationRevenu Annualise un montant selon une période définie sur 1 ou 2 ans en un montant au prorata
     * de la nouvelle période Ex: montant 15000 sur ancienne période de 15 mois donne 9000 si nouvelle période = 9 mois
     * 
     * @param dateDebutOldPeriode
     * @param dateFinOldPeriode
     * @param dateDebutNewPeriode
     * @param dateFinNewPeriode
     * @param montantInitial
     * @return String
     */
    public static String annualisationRevenu(String dateDebutOldExercice, String dateFinOldExercice,
            String dateDebutNewExercice, String dateFinNewExercice, String montant, String oldNbMois) throws Exception {
        int nbMoisOldExercice = 0;
        if (!JadeStringUtil.isEmpty(montant)) {
            montant = JANumberFormatter.deQuote(montant);
            FWCurrency cur = new FWCurrency(montant);
            int montantAnnualise = cur.intValue();
            // nombre de mois ancien exercice
            if (JadeStringUtil.isBlankOrZero(oldNbMois)) {
                int anneeDebutOldExercice = JACalendar.getYear(dateDebutOldExercice);
                int anneeFinOldExercice = JACalendar.getYear(dateFinOldExercice);
                // Détermination du nombre de mois de l'ancien exercice
                int moisDebutOldExercice = JACalendar.getMonth(dateDebutOldExercice);
                int moisFinOldExercice = JACalendar.getMonth(dateFinOldExercice);
                if (anneeDebutOldExercice != anneeFinOldExercice) {
                    moisFinOldExercice = moisFinOldExercice + 12;
                }
                nbMoisOldExercice = (moisFinOldExercice - moisDebutOldExercice) + 1;
            } else {
                nbMoisOldExercice = Integer.parseInt(oldNbMois);
            }
            // nombre de mois nouvel exercice
            int anneeDebutNewExercice = JACalendar.getYear(dateDebutNewExercice);
            int anneeFinNewExercice = JACalendar.getYear(dateFinNewExercice);
            // Détermination du nombre de mois de l'ancien exercice
            int moisDebutNewExercice = JACalendar.getMonth(dateDebutNewExercice);
            int moisFinNewExercice = JACalendar.getMonth(dateFinNewExercice);
            if (anneeDebutNewExercice != anneeFinNewExercice) {
                moisFinNewExercice = moisFinNewExercice + 12;
            }
            int nbMoisNewExercice = (moisFinNewExercice - moisDebutNewExercice) + 1;
            montantAnnualise = (montantAnnualise * nbMoisNewExercice) / nbMoisOldExercice;
            // montantAnnualise = JANumberFormatter.round(montantAnnualise, 100,
            // 0, JANumberFormatter.INF);
            return Integer.toString(montantAnnualise);
        } else {
            return "0";
        }
    }

    /*
     * Calcul les frais d'administration pour une décision d'imputation période donnée
     */
    public static String calculFraisImputation(String montant, CPDecision decision, BProcess process) throws Exception {
        float calcul = 0;
        float taux = 0;
        String arrondiCalcul = "";
        // Sous contrôle d'exception
        AFAffiliation aff = new AFAffiliation();
        aff.setSession(process.getSession());
        AFCotisation cotiAf = aff._cotisation(process.getTransaction(), decision.getIdAffiliation(),
                CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_FRAIS_ADMIN, decision.getDebutDecision(),
                decision.getFinDecision(), 1);
        if (cotiAf != null) {
            // Prendre la date la plus récente entre l'assurance et la décision
            // si l'année de coti = l'année de décision
            // sinon prendre la date de décision
            String dateRef = decision.getDebutDecision();
            if ((Integer.parseInt(decision.getAnneeDecision()) == JACalendar.getYear(cotiAf.getDateDebut()))
                    && BSessionUtil.compareDateFirstGreater(process.getSession(), cotiAf.getDateDebut(),
                            decision.getDebutDecision())) {
                dateRef = cotiAf.getDateDebut();
            }
            try {
                taux = Float.parseFloat(JANumberFormatter.deQuote(cotiAf.getTaux(dateRef, "0")));
            } catch (Exception e) {
                taux = 3; // BTC bug affiliation!!! 15.12.2003
            }
            calcul = (new FWCurrency(montant).floatValue() * taux) / 100;
            arrondiCalcul = ""
                    + JANumberFormatter
                            .round(Float.parseFloat(Float.toString(calcul)), 0.05, 2, JANumberFormatter.NEAR);
        }
        return arrondiCalcul;
    }

    /*
     * Calcul les frais d'administration pour une décision d'imputation période donnée
     */
    public static String calculFraisImputation(String montant, CPDecisionAffiliationTiers etatDecision, BProcess process)
            throws Exception {
        CPDecision decision = new CPDecision();
        decision.setSession(process.getSession());
        decision.setIdDecision(etatDecision.getIdDecision());
        decision.retrieve();
        return CPToolBox.calculFraisImputation(montant, decision, process);
    }

    /*
     * Calcul les frais d'administration pour une décision d'imputation période donnée
     */
    public static String calculFraisImputation(String montant, CPDecisionAgenceCommunale agenceDecision,
            BProcess process) throws Exception {
        CPDecision decision = new CPDecision();
        decision.setSession(process.getSession());
        decision.setIdDecision(agenceDecision.getIdDecision());
        decision.retrieve();
        return CPToolBox.calculFraisImputation(montant, decision, process);
    }

    /**
     * Retoune le montant facturé pour un tiers et une année et une rubrique (idExterne)
     * 
     * @return String montant
     */
    public static String compteurRubrique(BSession session, BTransaction trans, String idCompteAnnexe, String rubrique,
            String annee) {
        try {
            // Recherche de l'id rubrique
            CARubrique rubr = new CARubrique();
            rubr.setSession(session);
            rubr.setAlternateKey(APIRubrique.AK_IDEXTERNE);
            rubr.setIdExterne(rubrique);
            if (trans == null) {
                rubr.retrieve();
            } else {
                rubr.retrieve(trans);
            }
            return CPToolBox.rechMontantFacture(session, trans, idCompteAnnexe, rubr.getIdRubrique(), annee);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /*
     * Contrôle si la décision peut être facturé. Ex: Erreur si la préiode de décision est comprise dans une périodique
     * en cours de traitement return false si il y a erreur.
     */
    public static boolean controlePeriodique(BSession session, String debutDecision, String finDecision,
            FAPassage passage, String anneeEncours, AFAffiliation affi) throws JAException, Exception {
        String finMaxAutorise;
        if (passage == null) {
            return true;
        }
        int moisPeriode = JACalendar.getMonth(passage.getDatePeriode());
        // si cas mensuel

        if (CodeSystem.PERIODICITE_MENSUELLE.equalsIgnoreCase(affi.getPeriodicite())) {
            int moisMax = moisPeriode - 1;
            if (moisPeriode == 1) {
                finMaxAutorise = "01.00." + anneeEncours; // validation
                // impossible
            } else {
                if (moisMax < 10) {
                    finMaxAutorise = "31.0" + moisMax + "." + anneeEncours;
                } else {
                    finMaxAutorise = "31." + moisMax + "." + anneeEncours;
                }
            }
        } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equalsIgnoreCase(affi.getPeriodicite())) {
            int moisMax = moisPeriode - 3;
            if (moisPeriode <= 3) {
                finMaxAutorise = "01.00." + anneeEncours; // validation
                // impossible
            } else {
                if (moisMax < 10) {
                    finMaxAutorise = "31.0" + moisMax + "." + anneeEncours;
                } else {
                    finMaxAutorise = "31." + moisMax + "." + anneeEncours;
                }
            }
        } else {
            // Périodicité annuelle
            finMaxAutorise = "01.00." + anneeEncours; // validation impossible
        }
        if (BSessionUtil.compareDateFirstGreater(session, finDecision, finMaxAutorise)
                && BSessionUtil.compareDateFirstGreater(session, "31." + passage.getDatePeriode(), debutDecision)) {
            return false;

        }
        return true;
    }

    public static String conversionTypeAffiliationEnGenreAffilie(AFAffiliation affiliation) {
        if (CodeSystem.TYPE_AFFILI_TSE.equalsIgnoreCase(affiliation.getTypeAffiliation())
                || (CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE.equalsIgnoreCase(affiliation.getTypeAffiliation()))) {
            return CPDecision.CS_TSE;
        } else if (CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(affiliation.getTypeAffiliation())) {
            return CPDecision.CS_NON_ACTIF;
        } else if (CodeSystem.TYPE_AFFILI_INDEP.equalsIgnoreCase(affiliation.getTypeAffiliation())
                || (CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equalsIgnoreCase(affiliation.getTypeAffiliation()))) {
            if (globaz.naos.translation.CodeSystem.BRANCHE_ECO_AGRICULTURE.equalsIgnoreCase(affiliation
                    .getBrancheEconomique())) {
                return CPDecision.CS_AGRICULTEUR;
            } else {
                return CPDecision.CS_INDEPENDANT;
            }
        } else if (CodeSystem.TYPE_AFFILI_NON_SOUMIS.equalsIgnoreCase(affiliation.getTypeAffiliation())) {
            return CPDecision.CS_NON_SOUMIS;
        } else {
            return "";
        }
    }

    /*
     * Formatage du n° de caisse lorsque la zone est sur 6 caractères
     */
    public static String formatCaisseAvs(String numCaisse) {
        try {
            int nCaisse = Integer.parseInt(numCaisse) / 1000;
            int nAgence = Integer.parseInt(numCaisse) % 1000;
            if (nAgence == 0) {
                return Integer.toString(nCaisse);
            } else {
                return Integer.toString(nCaisse) + "." + Integer.toString(nAgence);
            }
        } catch (Exception e) {
            return numCaisse;
        }
    }

    public static String formatDate(String date, int format) {
        if (!JadeStringUtil.isBlankOrZero(date)) {
            if (format == 1) {
                // Return format AAAAMMJJ JJ.MM.AAAA
                date = JadeStringUtil.substring(date, 6, 2) + "." + JadeStringUtil.substring(date, 4, 2) + "."
                        + JadeStringUtil.substring(date, 0, 4);
            } else if (format == 2) {
                // Return format AAAA-MM-JJ JJ.MM.AAAA
                date = JadeStringUtil.substring(date, 8, 2) + "." + JadeStringUtil.substring(date, 5, 2) + "."
                        + JadeStringUtil.substring(date, 0, 4);
            }
        }
        return date;
    }

    /**
     * foramate la chaîne de caratère en gras pour iText
     * 
     * @param Bession
     * @param String
     * @return String
     */
    public static String formatGras(BSession session, String chaine) {
        try {
            // Recherche dans les lables le code iText indiquant le gras
            String debutBalise = session.getApplication().getLabel("STYLE_GRAS", "FR");
            String finBalise = session.getApplication().getLabel("FIN_STYLE", "FR");
            return debutBalise + chaine + finBalise;
        } catch (Exception e) {
            return chaine;
        }
    }

    public static String formattedInt(String champ, int grandeurMax) {
        String format = "";
        int taille = 0;
        taille = champ.length();
        format = champ;
        if (champ.equals("") || (champ == null)) {
            format = "0";
            taille = 1;
        }
        for (int i = taille; i < grandeurMax; i++) {
            format = " " + format;
        }
        return format;
    }

    public static String formattedNbre(String champ, int grandeurMax) {
        String format = "";
        int taille = 0;
        taille = champ.length();
        format = champ;
        for (int i = taille; i < grandeurMax; i++) {
            format = "0" + format;
        }
        return format;
    }

    /*
     * Compléte un String par un caractère donné (carDonne) jusqu'à la longueur grandeurMax Ex champ = 123,
     * grandeurMax=15, chiffreDonne=9 => return 123999999999999 utilie nottamment pour remplir de blanc une zone fixe ou
     * formatter une zone pour équivalent like
     */
    public static String formattedString(String champ, int grandeurMax, char carDonne) {
        String format = "";
        int taille = 0;
        taille = champ.length();
        format = champ;
        if (taille > grandeurMax) {
            return champ.substring(0, grandeurMax);
        } else {
            for (int i = taille; i < grandeurMax; i++) {
                format = format + carDonne;
            }
        }
        return format;
    }

    public static String getAnneePeriodeFiscale(BSession session, String idIfd) {
        CPPeriodeFiscale periodeFiscale = null;
        try {
            periodeFiscale = new CPPeriodeFiscale();
            periodeFiscale.setSession(session);
            periodeFiscale.setIdIfd(idIfd);
            periodeFiscale.retrieve();
        } catch (Exception e) {
            periodeFiscale = null;
        }
        if (periodeFiscale != null) {
            return periodeFiscale.getAnneeDecisionDebut();
        } else {
            return "";
        }
    }

    public static CACompteAnnexe getCompteAnnexe(BSession session, String numAffilie) throws Exception {
        if (!JadeStringUtil.isEmpty(numAffilie)) {
            String role = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(session.getApplication());
            // Extraction du compte annexe
            CACompteAnnexe compte = new CACompteAnnexe();
            compte.setSession(session);
            compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
            compte.setIdRole(role);
            compte.setIdExterneRole(numAffilie);
            compte.wantCallMethodBefore(false);
            compte.retrieve();
            return compte;
        } else {
            return null;
        }
    }

    /**
     * retourne le revenu pour l'inscription au ci des irrecouvrables en cot.pers.
     * 
     * @param annee
     *            l'annee dont on veut le revenu
     * @return un tableau jamais null, peut-être vide, contenant le ou les revenus et cotisations de l'annee
     *         correspondantes, et si le tiers est indépendant ou non actif
     */
    public static List<CPDataDecision> getDataDecisionPourInscriptionIrrec(BSession session, String annee,
            String idtiers) throws Exception {
        // Recherche des décisions actives pour le tiers et l'année
        CPDecisionManager decisionManager = new CPDecisionManager();
        decisionManager.setSession(session);
        decisionManager.setForIdTiers(idtiers);
        decisionManager.setNotInTypeDecision(CPDecision.CS_IMPUTATION + "," + CPDecision.CS_REMISE + ","
                + CPDecision.CS_REDUCTION);
        decisionManager.setForAnneeDecision(annee);
        decisionManager.setForIsActive(Boolean.TRUE);
        decisionManager.orderByIdDecision();
        decisionManager.find();

        List<CPDataDecision> dataDecisionList = new ArrayList<CPDataDecision>();
        CPDataDecision dataDecision = null;
        for (int i = 0; i < decisionManager.size(); i++) {
            CPDecision decision = (CPDecision) decisionManager.getEntity(i);
            String dateFacturation = decision.getDateFacturation();
            Boolean isComplementaire = new Boolean(decision.getComplementaire());

            // Recherche du revenu CI
            CPDonneesCalcul donneeCalcul = new CPDonneesCalcul();
            donneeCalcul.setSession(session);
            String revenuCiStr = donneeCalcul.getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_CI);
            BigDecimal revenuCi = new BigDecimal(0);
            if (!JadeStringUtil.isBlankOrZero(revenuCiStr)) {
                revenuCi = new BigDecimal(JANumberFormatter.deQuote(revenuCiStr));
            }

            // Recherche du montant de cotisation - La coti AVS est la première
            // qui est générée
            CPCotisationManager cotisationManager = new CPCotisationManager();
            cotisationManager.setSession(session);
            cotisationManager.setForIdDecision(decision.getIdDecision());
            cotisationManager.find();
            BigDecimal cotisationAvs = new BigDecimal(0);
            if (cotisationManager.size() > 0) {
                String cotisationAvsStr = ((CPCotisation) cotisationManager.getEntity(0)).getMontantAnnuel();
                cotisationAvs = new BigDecimal(JANumberFormatter.deQuote(cotisationAvsStr));
            }

            // définir le genre de décision
            String genre = "";
            if (CPDecision.CS_NON_ACTIF.equals(decision.getGenreAffilie())) {
                genre = CPDecision.CS_NON_ACTIF;
            } else if (CPDecision.CS_INDEPENDANT.equals(decision.getGenreAffilie())) {
                genre = CPDecision.CS_INDEPENDANT;
            } else if (CPDecision.CS_TSE.equals(decision.getGenreAffilie())) {
                genre = CPDecision.CS_TSE;
            } else if (CPDecision.CS_RENTIER.equals(decision.getGenreAffilie())) {
                genre = CPDecision.CS_RENTIER;
            } else if (CPDecision.CS_AGRICULTEUR.equals(decision.getGenreAffilie())) {
                genre = CPDecision.CS_AGRICULTEUR;
            } else if (CPDecision.CS_ETUDIANT.equals(decision.getGenreAffilie())) {
                genre = CPDecision.CS_ETUDIANT;
            } else {
                throw new Exception("le genre de décision n'a pas pu être déterminé !");
            }

            String dateDebutDecision = decision.getDebutDecision();
            String dateFinDecision = decision.getFinDecision();

            dataDecision = new CPDataDecision(cotisationAvs, dateFacturation, genre, isComplementaire, revenuCi,
                    dateDebutDecision, dateFinDecision);

            dataDecisionList.add(dataDecision);

        }
        return dataDecisionList;
    }

    /**
     * Method getExtourneProRata Retourne le montant annuel à rembourser au prorata
     * 
     * @param nbMoisExtourne
     * @param dateDebut
     * @param dateFin
     * @param montantIn
     * @return double
     */
    public static String getExtourneProrata(int nbMoisExtourne, String dateDebut, String dateFin, String montantAnnee) {

        if ("".equals(montantAnnee) || "0".equals(montantAnnee)) {
            return "0";
        }
        double montant = Double.parseDouble(montantAnnee);
        int moisDebut = 1;
        int moisFin = 12;
        try {
            moisDebut = JACalendar.getMonth(dateDebut);
            moisFin = JACalendar.getMonth(dateFin);
        } catch (JAException je) {
            je.printStackTrace();

            moisDebut = 1;
            moisFin = 12;
        }
        int nbPeriode = (moisFin - moisDebut) + 1;
        // Correction problème arrondi pour FAD -> éviter de faire le calcul si nombre de mois de la
        // décision = nombre de mois à extourner
        if (nbPeriode == nbMoisExtourne) {
            return montantAnnee;
        }
        // division par 12 mois et arrondi au 5 centimes
        FWCurrency varTemp = new FWCurrency(montant * nbMoisExtourne);
        montant = varTemp.doubleValue() / nbPeriode;
        montant = JANumberFormatter.round(montant, 0.05, 2, JANumberFormatter.NEAR);
        // rembourser le montant au nombre de mois non-soumis à cotisation
        return Double.toString(montant);
    }

    /**
     * Retourne le libellé d'un code system dans la langue d'un tiers Date de création : (15.03.2004 11:59:50)
     * 
     * @return java.lang.String
     */
    public static String getLibelleCodeSystem(BSession session, String idCode, String langueTiers) throws Exception {

        String libelle = "";
        FWParametersUserCode code = new FWParametersUserCode();
        code.setSession(session);
        code.setIdCodeSysteme(idCode);
        String fwLangue = "F";
        if (IConstantes.CS_TIERS_LANGUE_ANGLAIS.equals(langueTiers)) {
            fwLangue = "E";
        } else if (IConstantes.CS_TIERS_LANGUE_ALLEMAND.equals(langueTiers)) {
            fwLangue = "D";
        } else if (IConstantes.CS_TIERS_LANGUE_ROMANCHE.equals(langueTiers)) {
            fwLangue = "R";
        } else if (IConstantes.CS_TIERS_LANGUE_ITALIEN.equals(langueTiers)) {
            fwLangue = "I";
        }

        code.setIdLangue(fwLangue);
        code.retrieve();
        if (code.getSession().hasErrors()) {
            System.out.println(code.getSession().getErrors().toString());
        }
        libelle = code.getLibelle();

        if (libelle == null) {
            libelle = "";
        }
        return libelle;
    }

    static public String getLibEtatCivil(BSession session, String codeEtatCivil) {
        String lib = "";
        if (!JadeStringUtil.isEmpty(codeEtatCivil)) {
            if (codeEtatCivil.equalsIgnoreCase("1")) {
                lib = session.getCodeLibelle("515001");
            } else if (codeEtatCivil.equalsIgnoreCase("2")) {
                lib = session.getCodeLibelle("515002");
            } else if (codeEtatCivil.equalsIgnoreCase("3")) {
                lib = session.getCodeLibelle("515004");
            } else if (codeEtatCivil.equalsIgnoreCase("4")) {
                lib = session.getCodeLibelle("515003");
            } else if (codeEtatCivil.equalsIgnoreCase("5")) {
                lib = session.getCodeLibelle("515005");
            } else if (codeEtatCivil.equalsIgnoreCase("6")) {
                lib = session.getCodeLibelle("515006");
            } else if (codeEtatCivil.equalsIgnoreCase("7")) {
                lib = session.getCodeLibelle("515007");
            } else {
                lib = session.getLabel("INCONNU");
            }
        }
        return lib;
    }

    static public String getLibSexe(BSession session, String codeSexe) {
        String libSex = "";
        if (!JadeStringUtil.isEmpty(codeSexe)) {
            if (codeSexe.equalsIgnoreCase("1")) {
                libSex = session.getCodeLibelle(IConstantes.CS_PERSONNE_SEXE_HOMME);
            } else if (codeSexe.equalsIgnoreCase("2")) {
                libSex = session.getCodeLibelle(IConstantes.CS_PERSONNE_SEXE_FEMME);
            } else {
                libSex = session.getLabel("INCONNU");
            }
        }
        return libSex;
    }

    public static int getModeArrondiFad(BTransaction transaction) {
        int modeArrondi = 0;
        try {
            modeArrondi = Integer.parseInt(FWFindParameter.findParameter(transaction, "10500160", "MODEARRFAD", "", "",
                    0));
        } catch (Exception e) {
            modeArrondi = 0;
        }
        return modeArrondi;
    }

    /**
     * Method getMontantProRata Retourne le montant annuel au prorata de la période passée en paramètre
     * 
     * @param String
     *            dateDebut
     * @param String
     *            dateFin
     * @param float montantAPRoratiser
     * @return String
     */
    public static String getMontantProRata(String dateDebut, String dateFin, float montantAnnee) {
        return CPToolBox.getMontantProRata(dateDebut, dateFin, Float.toString(montantAnnee));
    }

    /**
     * Method getMontantProRata Retourne le montant annuel au prorata de la période passée en paramètre
     * 
     * @param String
     *            dateDebut
     * @param String
     *            dateFin
     * @param String
     *            montantAnnee
     * @return String
     */
    public static String getMontantProRata(String dateDebut, String dateFin, String montantAnnee) {
        if (!JadeStringUtil.isEmpty(montantAnnee)) {
            montantAnnee = JANumberFormatter.deQuote(montantAnnee);
            // oter les quotes
            double montant = Double.parseDouble(montantAnnee);
            int moisDebut = 1;
            int moisFin = 12;
            int anneeDebut = 0;
            int anneeFin = 0;
            try {
                moisDebut = JACalendar.getMonth(dateDebut);
                moisFin = JACalendar.getMonth(dateFin);
                anneeDebut = JACalendar.getMonth(dateDebut);
                anneeFin = JACalendar.getMonth(dateFin);
            } catch (JAException je) {
                je.printStackTrace();

                moisDebut = 1;
                moisFin = 12;
            }
            if ((moisDebut == moisFin) && (anneeDebut != anneeFin)) {
                return montantAnnee;
            } else if ((moisFin - moisDebut) == 11) {
                return montantAnnee; //
            }
            // division par 12 mois et arrondi au 5 centimes
            montant = JANumberFormatter.round(montant / 12, 0.05, 2, JANumberFormatter.NEAR);

            montant *= ((moisFin - moisDebut) + 1);

            // Afficher le montant au nombre de mois soumis à cotisation

            return JANumberFormatter.format(montant, 0.05, 2, JANumberFormatter.NEAR);
        } else {
            return "0";
        }
    }

    /**
     * Methode getUserByCanton. Recherche un utilisateur en fonction du canton spécifié.
     * 
     * @param idCanton
     *            l'id du canton
     * @param aSession
     *            pour pouvoir faire la recherche...
     * @return String l'utilisateur résultant de la recherche. Peut être null si l'id du canton est inconnu.
     */
    public final static String getUserByCanton(String idCanton, BTransaction transaction) {
        String searchResult = null;
        if (CPToolBox.m_cantons.isEmpty()) {
            final String cleDiffereeMappingCanton = "DECMAPUC";
            final String applicationName = "PHENIX";
            final String idCode = "610000";
            FWParametersManager parametres = new FWParametersManager();
            parametres.setSession(transaction.getSession());
            parametres.setForApplication(applicationName);
            parametres.setForIdCode(idCode);
            try {
                parametres.find(transaction);
            } catch (Exception e) {
                searchResult = "Failed to load UserByCanton.\n" + e.toString();
                e.printStackTrace();
                // TODO: définir que faire si exception
            }
            int size = parametres.getSize();
            for (int i = 0; i < size; i++) {
                FWParameters tmpEntity = (FWParameters) parametres.getEntity(i);
                if (cleDiffereeMappingCanton.equals(tmpEntity.getIdCleDiffere())) {
                    // contrôle
                    String valeurNumerique = tmpEntity.getValeurNumerique();
                    if (valeurNumerique != null) {
                        valeurNumerique = new StringTokenizer(valeurNumerique, ".").nextToken();
                    }
                    CPToolBox.m_cantons.put(valeurNumerique, tmpEntity.getValeurAlpha());
                }
            }
        }
        if (searchResult == null) {
            if (CPToolBox.m_cantons.containsKey(idCanton)) {
                searchResult = CPToolBox.m_cantons.get(idCanton);
            }
        }
        return searchResult;
    }

    public static int getWeekNumber(JADate date) {
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(date.getYear(), date.getMonth(), date.getDay());
        return myCalendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * Return true si l'affilié a une particularité ou un moti qui indique que ses cotisations ne lui sont pas
     * directement facturées
     * 
     * @param transaction
     * @param AFAffiliation
     *            affiliation
     * @param String
     *            date
     * @return boolean
     */
    public static boolean isAffilieAssiste(BTransaction transaction, AFAffiliation affiliation, String date)
            throws Exception {
        // Si à l'aide sociale
        boolean assiste = false;
        try {
            return AFParticulariteAffiliation.existeParticulariteOrganesExternes(transaction,
                    affiliation.getAffiliationId(), date);
        } catch (Exception e) {
            assiste = false;
        }
        return assiste;
    }

    public static boolean isAffilieAssiste(BTransaction transaction, AFAffiliation affiliation, String dateDeb,
            String dateFin) throws Exception {
        // Si à l'aide sociale
        boolean assiste = false;
        try {
            if (AFParticulariteAffiliation.existeParticulariteOrganesExternes(transaction,
                    affiliation.getAffiliationId(), dateDeb, dateFin, "") == null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            assiste = false;
        }
        return assiste;
    }

    /**
     * Return true si les genres sont différents (tient compte entre IND et REN etc)
     * 
     * @param String
     *            genre1
     * @param String
     *            genre2
     * @return boolean
     */

    public static boolean isGenreDifferent(String genre1, String genre2) {
        if (!genre1.equalsIgnoreCase(genre2)) {
            if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(genre1) || CPDecision.CS_ETUDIANT.equalsIgnoreCase(genre1)) {
                if (!CPDecision.CS_NON_ACTIF.equalsIgnoreCase(genre2)
                        && !CPDecision.CS_ETUDIANT.equalsIgnoreCase(genre2)) {
                    return true;
                }
            } else if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(genre1)
                    || CPDecision.CS_RENTIER.equalsIgnoreCase(genre1)) {
                if (!CPDecision.CS_INDEPENDANT.equalsIgnoreCase(genre2)
                        && !CPDecision.CS_RENTIER.equalsIgnoreCase(genre2)) {
                    return true;
                }
            } else if (CPDecision.CS_AGRICULTEUR.equalsIgnoreCase(genre1)
                    || CPDecision.CS_RENTIER.equalsIgnoreCase(genre1)) {
                if (!CPDecision.CS_AGRICULTEUR.equalsIgnoreCase(genre2)
                        && !CPDecision.CS_RENTIER.equalsIgnoreCase(genre2)) {
                    return true;
                }
            } else if (CPDecision.CS_TSE.equalsIgnoreCase(genre1) || CPDecision.CS_RENTIER.equalsIgnoreCase(genre1)) {
                if (!CPDecision.CS_TSE.equalsIgnoreCase(genre2) && !CPDecision.CS_RENTIER.equalsIgnoreCase(genre2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 04.03.2005: BTC: Cette méthode retourne pour quel type de décision, la décision peut être considérée comme
     * provisoire au point de vue métier
     * 
     * @param myDecision
     * @return
     */
    public static boolean isProvisoireMetier(CPDecisionAgenceCommunale decision) {
        if (CPDecision.CS_PROVISOIRE.equalsIgnoreCase(decision.getTypeDecision())
                || CPDecision.CS_ACOMPTE.equalsIgnoreCase(decision.getTypeDecision())
                || CPDecision.CS_CORRECTION.equalsIgnoreCase(decision.getTypeDecision())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Création ou "suppression" d'une assurance pour une période Ex: Passage de IND à AFI ou inversement (création ou
     * mettre date de fin à AFI
     * 
     * @param transaction
     * @param AFAffiliation
     *            affiliation
     * @param String
     *            genre de cotisation à insérer ou annuler
     * @param String
     *            début
     * @param String
     *            fin
     * @param int mode (cération ou suppression)
     * @return boolean
     */
    public static boolean miseAjourAssurance(BTransaction transaction, AFAffiliation affiliation, String date)
            throws Exception {
        // Si à l'aide sociale
        boolean assiste = false;
        try {
            return AFParticulariteAffiliation.existeParticulariteOrganesExternes(transaction,
                    affiliation.getAffiliationId(), date);
        } catch (Exception e) {
            assiste = false;
        }
        return assiste;
    }

    /*
     * Mise à jour du code actif pour les décisions d'une affiliation
     */
    public static void miseAjourDecisionActive(BTransaction transaction, AFAffiliation affiliation, String annee,
            int anneeLimite) throws Exception {
        ArrayList<CPDecision> listDecision = new ArrayList<CPDecision>();
        String anneeBk = "";
        // Lecture des décisions de l'affiliation par année, date de décision,
        // id décision
        // Ne prendre que celle qui sont comptabilisées ou migrées
        CPDecisionManager decManager = new CPDecisionManager();
        decManager.setSession(affiliation.getSession());
        decManager.setForIdAffiliation(affiliation.getAffiliationId());
        decManager.setNotInTypeDecision(CPDecision.CS_IMPUTATION + ", " + CPDecision.CS_REMISE);
        if (!JadeStringUtil.isEmpty(annee)) {
            decManager.setForAnneeDecision(annee);
        }
        decManager.orderByAnneeDecision();
        decManager.orderByDateComptabilisation(); // PO 8100
        // decManager.orderByDateDecision();
        decManager.orderByIdDecision();
        decManager.changeManagerSize(0);
        decManager.find(transaction);
        boolean complementaireActif = false;
        for (int i = 0; i < decManager.size(); i++) {
            CPDecision myDecision = (CPDecision) decManager.getEntity(i);
            boolean ok = true;
            // Si l'année de décision est > à l'année limite
            // => la décision doit être inactive (Ex:2007 alors que l'on est en
            // 2006
            if ((Integer.parseInt(myDecision.getAnneeDecision()) > anneeLimite)
                    || "610001".equalsIgnoreCase(myDecision.getSpecification())) {
                ok = false;
            } else {
                // Si l'état n'est pas comptabilisé ou migration => inactif
                if (!CPDecision.CS_FACTURATION.equalsIgnoreCase(myDecision.getDernierEtat())
                        && !CPDecision.CS_PB_COMPTABILISATION.equalsIgnoreCase(myDecision.getDernierEtat())
                        && !CPDecision.CS_REPRISE.equalsIgnoreCase(myDecision.getDernierEtat())) {
                    ok = false;
                } else if (BSessionUtil.compareDateFirstGreaterOrEqual(affiliation.getSession(),
                        affiliation.getDateDebut(), myDecision.getFinDecision())
                        || (BSessionUtil.compareDateFirstLowerOrEqual(affiliation.getSession(),
                                affiliation.getDateFin(), myDecision.getDebutDecision()) && !JadeStringUtil
                                .isEmpty(affiliation.getDateFin()))) {
                    ok = false;
                    anneeBk = myDecision.getAnneeDecision();
                    listDecision.add(myDecision);

                } // si décision mise en compte -> actif
                else {
                    // Effacer le tableau dans le cas de changement d'année
                    if (!myDecision.getAnneeDecision().equalsIgnoreCase(anneeBk)) {
                        anneeBk = myDecision.getAnneeDecision();
                        listDecision = new ArrayList<CPDecision>();
                    }
                    Iterator<CPDecision> iter = listDecision.iterator();
                    // Ne pas prendre les décisions dont la date comprend celle
                    // qui vient d'être traitée
                    // pour le même genre.
                    while (iter.hasNext() && ok) {
                        CPDecision element = iter.next();
                        // if(element.getGenreAffilie().equalsIgnoreCase(myDecision.getGenreAffilie()))
                        // {
                        if ((BSessionUtil.compareDateFirstGreaterOrEqual(affiliation.getSession(),
                                myDecision.getDebutDecision(), element.getDebutDecision()) && BSessionUtil
                                .compareDateFirstLower(affiliation.getSession(), myDecision.getDebutDecision(),
                                        element.getFinDecision()))
                                || (BSessionUtil.compareDateFirstLowerOrEqual(affiliation.getSession(),
                                        myDecision.getFinDecision(), element.getFinDecision()) && BSessionUtil
                                        .compareDateFirstLower(affiliation.getSession(), myDecision.getDebutDecision(),
                                                element.getFinDecision()))) {
                            if (complementaireActif) {
                                ok = true;
                            } else {
                                ok = false;
                                // }
                            }
                        }
                    }
                }
            }
            if (ok) {
                // Stockage de la décision à traiter
                listDecision.add(myDecision);
                myDecision.setActive(new Boolean(true));
                if (myDecision.getComplementaire().equals(Boolean.TRUE)) {
                    complementaireActif = true;
                } else {
                    complementaireActif = false;
                }
            } else {
                myDecision.setActive(new Boolean(false));
            }
            myDecision.wantCallValidate(false);
            myDecision.update(transaction);
        }
    }

    public static String getListCantonEtSedex(HttpSession httpSession, String idCanton, boolean vide) {
        BSession bsession = (BSession) ((FWController) httpSession.getAttribute("objController")).getSession();

        StringBuffer options = new StringBuffer();
        try {
            if (vide) {
                options.append("<option value=\"\"></option> ");
            }
            FWParametersSystemCodeManager manager = new FWParametersSystemCodeManager();
            manager.setSession(bsession);
            manager.setForGroupLike("PYCANTON");
            manager.setForIdLangue(bsession.getIdLangue());
            manager.setForActif(Boolean.TRUE);
            manager.find();
            // Ajout du code Sedex qui ne se trouve pas dans les cantons
            options.append("<option ");
            options.append("selected ");
            options.append("value=\"" + CPJournalRetour.CS_CANTON_SEDEX + "\">");
            options.append("Sedex");
            for (int i = 0; i < manager.size(); i++) {
                FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                options.append("<option ");
                if (code.getIdCode().equals(idCanton) || (manager.size() == 1)) {
                    options.append("selected ");
                }
                options.append("value=\"" + code.getIdCode() + "\">");
                options.append(code.getCurrentCodeUtilisateur().getLibelle());

                options.append("</option>");
            }
        } catch (Exception ex) {
            JadeLogger.error(null, ex);
        }
        return options.toString();
    }

    /*
     * Mise à jour du code actif pour les imputations
     */
    public static void miseAjourImputation(BTransaction transaction, AFAffiliation affiliation, String annee,
            int anneeLimite) throws Exception {
        String anneeBk = "";
        // Lecture des décisions de l'affiliation par année, date de décision,
        // id décision
        // Ne prendre que celle qui sont comptabilisées ou migrées
        CPDecisionManager decManager = new CPDecisionManager();
        decManager.setSession(affiliation.getSession());
        decManager.setForIdAffiliation(affiliation.getAffiliationId());
        decManager.setForTypeDecision(CPDecision.CS_IMPUTATION);
        if (!JadeStringUtil.isEmpty(annee)) {
            decManager.setForAnneeDecision(annee);
        }
        decManager.orderByAnneeDecision();
        decManager.orderByDateComptabilisation(); // PO 8100
        // decManager.orderByDateDecision();
        decManager.orderByIdDecision();
        decManager.changeManagerSize(0);
        decManager.find(transaction);
        boolean annuleEtRemplace = false;
        for (int i = 0; i < decManager.size(); i++) {
            CPDecision myDecision = (CPDecision) decManager.getEntity(i);
            boolean ok = true;
            // Réinitialiser les valeurs de test si il y a un changement d'année
            if (!myDecision.getAnneeDecision().equalsIgnoreCase(anneeBk)) {
                anneeBk = myDecision.getAnneeDecision();
                annuleEtRemplace = false;
            }
            // Si décision avec particularité "Ayant payé comme salarié", les imputations doivent être inactives
            CPDecisionManager decSalarie = new CPDecisionManager();
            decSalarie.setForAnneeDecision(myDecision.getAnneeDecision());
            decSalarie.setSession(affiliation.getSession());
            decSalarie.setForIsActive(Boolean.TRUE);
            decSalarie.setForIdAffiliation(myDecision.getIdAffiliation());
            decSalarie.setForSpecification(CPDecision.CS_SALARIE_DISPENSE);
            if (decSalarie.getCount() > 0) {
                annuleEtRemplace = true;
            }
            if (annuleEtRemplace) {
                ok = false;
            } else if (Integer.parseInt(myDecision.getAnneeDecision()) > anneeLimite) {
                // Si l'année de décision est > à l'année limite
                // => la décision doit être inactive (Ex:2007 alors que l'on est
                // en 2006
                ok = false;
            } else {
                // Si l'état n'est pas comptabilisé ou migration => inactif
                if (!CPDecision.CS_FACTURATION.equalsIgnoreCase(myDecision.getDernierEtat())
                        && !CPDecision.CS_PB_COMPTABILISATION.equalsIgnoreCase(myDecision.getDernierEtat())
                        && !CPDecision.CS_REPRISE.equalsIgnoreCase(myDecision.getDernierEtat())) {
                    ok = false;
                } else if (BSessionUtil.compareDateFirstGreaterOrEqual(affiliation.getSession(),
                        affiliation.getDateDebut(), myDecision.getFinDecision())
                        || (BSessionUtil.compareDateFirstLowerOrEqual(affiliation.getSession(),
                                affiliation.getDateFin(), myDecision.getDebutDecision()) && !JadeStringUtil
                                .isEmpty(affiliation.getDateFin()))) {
                    ok = false;
                }
            }
            // Si une imputation a "Annule et remplace" => toutes les suivantes
            // sont inactives
            if (!annuleEtRemplace && myDecision.getComplementaire().equals(Boolean.TRUE)) {
                annuleEtRemplace = true;
            }
            if (ok) {
                myDecision.setActive(new Boolean(true));
            } else {
                myDecision.setActive(new Boolean(false));
            }
            myDecision.wantCallValidate(false);
            myDecision.update(transaction);
        }
    }

    /*
     * Mise à jour du code actif pour les imputations
     */
    public static void miseAjourRemise(BTransaction transaction, AFAffiliation affiliation, String annee,
            int anneeLimite) throws Exception {
        String anneeBk = "";
        // Lecture des décisions de l'affiliation par année, date de décision,
        // id décision
        // Ne prendre que celle qui sont comptabilisées ou migrées
        CPDecisionManager decManager = new CPDecisionManager();
        decManager.setSession(affiliation.getSession());
        decManager.setForIdAffiliation(affiliation.getAffiliationId());
        decManager.setForTypeDecision(CPDecision.CS_REMISE);
        if (!JadeStringUtil.isEmpty(annee)) {
            decManager.setForAnneeDecision(annee);
        }
        decManager.orderByAnneeDecision();
        decManager.orderByDateComptabilisation(); // PO 8100
        // decManager.orderByDateDecision();
        decManager.orderByIdDecision();
        decManager.changeManagerSize(0);
        decManager.find(transaction);
        for (int i = 0; i < decManager.size(); i++) {
            CPDecision myDecision = (CPDecision) decManager.getEntity(i);
            boolean ok = true;
            // Réinitialiser les valeurs de test si il y a un changement d'année
            if (!myDecision.getAnneeDecision().equalsIgnoreCase(anneeBk)) {
                anneeBk = myDecision.getAnneeDecision();
            }
            if (Integer.parseInt(myDecision.getAnneeDecision()) > anneeLimite) {
                // Si l'année de décision est > à l'année limite
                // => la décision doit être inactive (Ex:2007 alors que l'on est
                // en 2006
                ok = false;
            } else {
                // Si l'état n'est pas comptabilisé ou migration => inactif
                if (!CPDecision.CS_FACTURATION.equalsIgnoreCase(myDecision.getDernierEtat())
                        && !CPDecision.CS_PB_COMPTABILISATION.equalsIgnoreCase(myDecision.getDernierEtat())
                        && !CPDecision.CS_REPRISE.equalsIgnoreCase(myDecision.getDernierEtat())) {
                    ok = false;
                } else if (BSessionUtil.compareDateFirstGreaterOrEqual(affiliation.getSession(),
                        affiliation.getDateDebut(), myDecision.getFinDecision())
                        || (BSessionUtil.compareDateFirstLowerOrEqual(affiliation.getSession(),
                                affiliation.getDateFin(), myDecision.getDebutDecision()) && !JadeStringUtil
                                .isEmpty(affiliation.getDateFin()))) {
                    ok = false;
                }
            }
            if (ok) {
                myDecision.setActive(new Boolean(true));
            } else {
                myDecision.setActive(new Boolean(false));
            }
            myDecision.wantCallValidate(false);
            myDecision.update(transaction);
        }
    }

    /*
     * Multiplie 2 string
     */
    public static String multString(String montant, String multiple) {
        BigDecimal bMontant = new BigDecimal(JANumberFormatter.deQuote(montant));
        bMontant = bMontant.multiply(new BigDecimal(JANumberFormatter.deQuote(multiple)));
        return bMontant.toString();
    }

    /**
     * Method nbMoisDansPeriode retourne le nombre de mois d'une année contenu dans une période
     * 
     * @param dateDebutPeriode
     * @param dateFinPeriode
     * @param anneeReference
     * @return String
     */
    public static String nbMoisDansPeriode(String dateDebutPeriode, String dateFinPeriode, String anneeReference) {
        try {
            if (JadeStringUtil.isBlankOrZero(dateFinPeriode)) {
                dateFinPeriode = "31.12.9999";
            }
            int anneeDebut = JACalendar.getYear(dateDebutPeriode);
            int anneeFin = JACalendar.getYear(dateFinPeriode);
            int moisDebut = JACalendar.getMonth(dateDebutPeriode);
            int moisFin = JACalendar.getMonth(dateFinPeriode);
            // Détermination du nombre de mois de l'exercice compris dans
            // l'année de base
            if ((anneeDebut == Integer.parseInt(anneeReference)) && (anneeDebut == anneeFin)) {
                return Integer.toString((moisFin - moisDebut) + 1);
            } else if (anneeDebut == Integer.parseInt(anneeReference)) {
                return Integer.toString((12 - moisDebut) + 1);
            } else {
                return Integer.toString(moisFin);
            }
        } catch (JAException je) {
            je.printStackTrace();
            return "";
        }
    }

    /**
     * Method nbMoisPeriode nombre de mois de la période
     * 
     * @param dateDebut
     * @param dateFin
     * @return String
     */
    public static String nbMoisPeriode(String dateDebutPeriode, String dateFinPeriode) {
        int moisDebut = 1;
        int moisFin = 12;
        int anneeDebut = 0;
        int anneeFin = 0;
        try {
            moisDebut = JACalendar.getMonth(dateDebutPeriode);
            moisFin = JACalendar.getMonth(dateFinPeriode);
            anneeDebut = JACalendar.getYear(dateDebutPeriode);
            anneeFin = JACalendar.getYear(dateFinPeriode);
            // Détermination du nombre de mois de l'exercice compris dans
            // l'année de base
            if (anneeDebut != anneeFin) {
                moisFin = moisFin + 12;
            }
            return Integer.toString((moisFin - moisDebut) + 1);
        } catch (JAException je) {
            je.printStackTrace();
            return "";
        }
    }

    /**
     * Method prorataInteret Calcul du prorata du montant selon la durée de l'exercice
     * 
     * @param dateDebut
     * @param dateFin
     * @param montant
     * @return String
     */
    public static String prorataInteret(String dateDebutExercice, String dateFinExercice, String montant) {

        double montantProrata = Double.parseDouble(montant);
        int moisDebut = 1;
        int moisFin = 12;
        int anneeDebut = 0;
        int anneeFin = 0;
        int ecart = 0;
        try {
            moisDebut = JACalendar.getMonth(dateDebutExercice);
            moisFin = JACalendar.getMonth(dateFinExercice);
            anneeDebut = JACalendar.getYear(dateDebutExercice);
            anneeFin = JACalendar.getYear(dateFinExercice);
            // Détermination du nombre de mois de l'exercice compris dans
            // l'année de base
            if (anneeDebut != anneeFin) {
                moisFin = moisFin + 12;
            }
            ecart = (moisFin - moisDebut) + 1;
            // Annualiser
            montantProrata = (montantProrata * ecart) / 12;
            return Double.toString(montantProrata);
        } catch (JAException je) {
            je.printStackTrace();
            return "";
        }
    }

    /**
     * Retourne l'entity du conjoint pour un tiers à une date donnée
     * 
     * @param session
     * @param idTiers
     * @param date
     * @return TITiersViewBean
     */
    public static TITiersViewBean rechercheConjoint(BSession session, String idTiers, String date) throws Exception {
        TITiersViewBean conjoint = null;
        String idConjoint = CPToolBox.rechercheIdConjoint(session, idTiers, date);
        // PO 6509
        if (!JadeStringUtil.isEmpty(idConjoint) && !idConjoint.equalsIgnoreCase("100")) {
            conjoint = new TITiersViewBean();
            conjoint.setIdTiers(idConjoint);
            conjoint.setSession(session);
            conjoint.retrieve();
        }
        return conjoint;
    }

    /**
     * Recherche la cotisation active pour une affiliation et pour une année Si le genre n'est pas renseigné, la
     * première cotisation créé de la décision correspondante sera prise. Date de création : (01.06.2010 11:54:08)
     * 
     * @param session
     *            BSession
     * @param trans
     *            BTransaction
     * @param idAffiliation
     *            String
     * @param annee
     *            String
     */
    public static CPDecisionAffiliationCotisation rechercheCotisationActive(BSession session, BTransaction trans,
            String idAffiliation, String annee, String genreCotisation) throws Exception {
        CPDecisionAffiliationCotisationManager afCotiMng = new CPDecisionAffiliationCotisationManager();
        afCotiMng.setSession(session);
        afCotiMng.setForIdAffiliation(idAffiliation);
        afCotiMng.setIsActive(Boolean.TRUE);
        afCotiMng.setForAnneeDecision(annee);
        afCotiMng.setForGenreCotisation(genreCotisation);
        afCotiMng.oderByIdCotisationDesc();
        afCotiMng.find(1);
        if (afCotiMng.size() > 0) {
            return (CPDecisionAffiliationCotisation) afCotiMng.getFirstEntity();
        } else {
            return null;
        }
    }

    /**
     * Retourne l' id du conjoint pour un tiers à une date donnée
     * 
     * @param session
     * @param idTiers
     * @param date
     * @return idConjoint
     */
    public static String rechercheIdConjoint(BSession session, String idTiers, String date) throws Exception {
        String idConjoint = "";
        TICompositionTiersManager cjt = new TICompositionTiersManager();
        cjt.setSession(session);
        // Recherche du conjoint
        // acr: selon directive AVS: Le mariage doit être considéré depuis le
        // début de l'année du mariage (division par deux pour toute l'année).
        // Le divorce doit être considéré depuis le début de l'année du divorce
        // (individuel pour toute l'année).
        // Ce qui signifie que l'on va chercher le conjoint au 31.12 de l'année
        // de la décision
        cjt.setForIdTiersEnfantParent(idTiers);
        cjt.setForTypeLien(TICompositionTiers.CS_CONJOINT);
        cjt.setForDateEntreDebutEtFin(date);
        cjt.find();
        if (cjt.size() > 0) { // Formatage conjoint
            if (((TICompositionTiers) cjt.getEntity(0)).getIdTiersParent().equals(idTiers)) {
                idConjoint = ((TICompositionTiers) cjt.getEntity(0)).getIdTiersEnfant();
            } else {
                idConjoint = ((TICompositionTiers) cjt.getEntity(0)).getIdTiersParent();
            }
        }
        return idConjoint;
    }

    /**
     * Recherche le journal Ci de type décision personnelle qui a l'état ouvert
     * 
     * @param BSession
     */
    public static CIJournal rechercheJournalCi(BSession session) throws Exception {
        CIJournalManager journalManager = new CIJournalManager();
        journalManager.setSession(session);
        journalManager.setForIdTypeInscription(CIJournal.CS_ASSURANCE_FACULTATIVE);
        journalManager.find();
        for (int i = 0; i < journalManager.size(); i++) {
            CIJournal journalCi = ((CIJournal) journalManager.getEntity(i));
            if (journalCi.getIdEtat().equalsIgnoreCase(CIJournal.CS_OUVERT)
                    || journalCi.getIdEtat().equalsIgnoreCase(CIJournal.CS_PARTIEL)) {
                return journalCi;
            }
        }
        return null;
    }

    /**
     * Retoune le montant facturé pour un tiers et une année et une rubrique
     * 
     * @return String montant
     */
    public static String rechMontantFacture(BSession session, BTransaction trans, String idCompteAnnexe,
            String idRubrique, String annee) {
        try {
            // Recherche du montant
            CACompteur compteur = new CACompteur();
            compteur.setSession(session);
            compteur.setAlternateKey(1);
            compteur.setAnnee(annee);
            compteur.setIdCompteAnnexe(idCompteAnnexe);
            compteur.setIdRubrique(idRubrique);
            if (trans == null) {
                compteur.retrieve();
            } else {
                compteur.retrieve(trans);
            }
            if (compteur.isNew() || compteur.hasErrors()) {
                return "0";
            } else {
                return compteur.getCumulCotisation();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * Remplace une chaîne par une autre à l'intérieur d'une chaine de caractères
     * 
     * @return le résultat du remplacement
     * @param baseString
     *            la chaine contenant le texte à traiter
     * @param textToReplace
     *            le texte à remplacer dans la chaine de base
     * @param replaceWith
     *            le texte de remplacement
     */
    public final static String replaceString(String baseString, String textToReplace, String replaceWith) {
        if ((JadeStringUtil.isEmpty(baseString)) || (JadeStringUtil.isEmpty(textToReplace)) || (replaceWith == null)) {
            return baseString;
        }
        StringBuffer buffer = new StringBuffer();
        int index = -1;
        while ((index = baseString.indexOf(textToReplace)) != -1) {
            buffer.append(baseString.substring(0, index));
            buffer.append(replaceWith);
            baseString = baseString.substring(index + textToReplace.length());
        }
        buffer.append(baseString);
        return buffer.toString();
    }

    /**
     * Retourne l'affilion d'un tiers ou d'un n° d'affilié pour une année Test du type de d'affiliation selon le genre
     * de la décision
     */
    public static AFAffiliation returnAffiliation(BSession session, BTransaction transaction, String numAffilie,
            String annee, String genreDem, int modeRecherche) throws Exception {
        AFAffiliation affiliation = new AFAffiliation();
        affiliation.setSession(session);
        if (JadeStringUtil.isIntegerEmpty(genreDem) || CPDecision.CS_NON_ACTIF.equals(genreDem)
                || CPDecision.CS_ETUDIANT.equals(genreDem)) {
            affiliation = affiliation._retourAffiliation(transaction, numAffilie, annee,
                    CodeSystem.TYPE_AFFILI_NON_ACTIF, modeRecherche);
            // Si null -> recherche NA selon art. 1A...
            if (affiliation == null) {
                affiliation = new AFAffiliation();
                affiliation.setSession(session);
                affiliation = affiliation._retourAffiliation(transaction, numAffilie, annee,
                        CodeSystem.TYPE_AFFILI_SELON_ART_1A, modeRecherche);
            } else {
                return affiliation;
            }
        }
        if (JadeStringUtil.isIntegerEmpty(genreDem) || CPDecision.CS_TSE.equals(genreDem)) {
            affiliation = new AFAffiliation();
            affiliation.setSession(session);
            affiliation = affiliation._retourAffiliation(transaction, numAffilie, annee, CodeSystem.TYPE_AFFILI_TSE,
                    modeRecherche);
            if (affiliation == null) {
                affiliation = new AFAffiliation();
                affiliation.setSession(session);
                affiliation = affiliation._retourAffiliation(transaction, numAffilie, annee,
                        CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE, modeRecherche);
            }
        }
        // Test sur indépendant si on a rien trouvé (affiliation==null) lorsque
        // le genre demandé est blanc
        // ou différent de non actif
        if (((affiliation == null) || JadeStringUtil.isEmpty(affiliation.getAffiliationId()))) {
            if (!CPDecision.CS_NON_ACTIF.equals(genreDem) && !CPDecision.CS_ETUDIANT.equals(genreDem)) { // différent
                // indépendant
                affiliation = new AFAffiliation();
                affiliation.setSession(session);
                affiliation = affiliation._retourAffiliation(transaction, numAffilie, annee,
                        CodeSystem.TYPE_AFFILI_INDEP, modeRecherche);
                // Si null -> recherche indépendant et employeur
                if (affiliation == null) {
                    affiliation = new AFAffiliation();
                    affiliation.setSession(session);
                    affiliation = affiliation._retourAffiliation(transaction, numAffilie, annee,
                            CodeSystem.TYPE_AFFILI_INDEP_EMPLOY, modeRecherche);
                } else {
                    return affiliation;
                }
                // Recherche TSE pour les fisc qui les communiquent sous
                // indépendant (ex JURA) ==> recherche si assurance chomage
                if (affiliation == null) {
                    affiliation = new AFAffiliation();
                    affiliation.setSession(session);
                    affiliation = affiliation._retourAffiliation(transaction, numAffilie, annee,
                            CodeSystem.TYPE_AFFILI_TSE, modeRecherche);
                    if (affiliation == null) {
                        affiliation = new AFAffiliation();
                        affiliation.setSession(session);
                        affiliation = affiliation._retourAffiliation(transaction, numAffilie, annee,
                                CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE, modeRecherche);
                    }
                    if (affiliation != null) {
                        // Recherche si cotisation AC
                        AFCotisation cotiAf = affiliation._cotisation(transaction, affiliation.getAffiliationId(),
                                CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AC, "01.01" + annee,
                                "31.12" + annee, 1);
                        if (cotiAf == null) {
                            affiliation = null;
                        }
                    }
                }
            }
        }
        return affiliation;
    }

    /**
     * Retourne l'affilion ou d'un n° d'affilié pour une période Test du type de d'affiliation selon le genre de la
     * décision
     */
    public static AFAffiliation returnAffiliation(BSession session, BTransaction transaction, String numAffilie,
            String debutPeriode, String finPeriode, String genreDem, int modeRecherche) throws Exception {
        AFAffiliation affiliation = new AFAffiliation();
        affiliation.setSession(session);
        if (JadeStringUtil.isIntegerEmpty(genreDem) || CPDecision.CS_NON_ACTIF.equals(genreDem)
                || CPDecision.CS_ETUDIANT.equals(genreDem)) {
            affiliation = affiliation._retourAffiliation(transaction, numAffilie, debutPeriode, finPeriode,
                    CodeSystem.TYPE_AFFILI_NON_ACTIF, modeRecherche);
            // Si null -> recherche NA selon art. 1A...
            if (affiliation == null) {
                affiliation = new AFAffiliation();
                affiliation.setSession(session);
                affiliation = affiliation._retourAffiliation(transaction, numAffilie, debutPeriode, finPeriode,
                        CodeSystem.TYPE_AFFILI_SELON_ART_1A, modeRecherche);
            } else {
                return affiliation;
            }
        }
        if (JadeStringUtil.isIntegerEmpty(genreDem) || CPDecision.CS_TSE.equals(genreDem)) {
            affiliation = new AFAffiliation();
            affiliation.setSession(session);
            affiliation = affiliation._retourAffiliation(transaction, numAffilie, debutPeriode, finPeriode,
                    CodeSystem.TYPE_AFFILI_TSE, modeRecherche);
            if (affiliation == null) {
                affiliation = new AFAffiliation();
                affiliation.setSession(session);
                affiliation = affiliation._retourAffiliation(transaction, numAffilie, debutPeriode, finPeriode,
                        CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE, modeRecherche);
            }
        }
        // Test sur indépendant si on a rien trouvé (affiliation==null) lorsque
        // le genre demandé est blanc
        // ou différent de non actif
        if (((affiliation == null) || JadeStringUtil.isEmpty(affiliation.getAffiliationId()))) {
            if (!CPDecision.CS_NON_ACTIF.equals(genreDem) && !CPDecision.CS_ETUDIANT.equals(genreDem)) { // différent
                // indépendant
                affiliation = new AFAffiliation();
                affiliation.setSession(session);
                affiliation = affiliation._retourAffiliation(transaction, numAffilie, debutPeriode, finPeriode,
                        CodeSystem.TYPE_AFFILI_INDEP, modeRecherche);
                // Si null -> recherche indépendant et employeur
                if (affiliation == null) {
                    affiliation = new AFAffiliation();
                    affiliation.setSession(session);
                    affiliation = affiliation._retourAffiliation(transaction, numAffilie, debutPeriode, finPeriode,
                            CodeSystem.TYPE_AFFILI_INDEP_EMPLOY, modeRecherche);
                } else {
                    return affiliation;
                }
                // Recherche TSE pour les fisc qui les communiquent sous
                // indépendant (ex JURA) ==> recherche si assurance chomage
                if (affiliation == null) {
                    affiliation = new AFAffiliation();
                    affiliation.setSession(session);
                    affiliation = affiliation._retourAffiliation(transaction, numAffilie, debutPeriode, finPeriode,
                            CodeSystem.TYPE_AFFILI_TSE, modeRecherche);
                    if (affiliation == null) {
                        affiliation = new AFAffiliation();
                        affiliation.setSession(session);
                        affiliation = affiliation._retourAffiliation(transaction, numAffilie, debutPeriode, finPeriode,
                                CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE, modeRecherche);
                    }
                    if (affiliation != null) {
                        // Recherche si cotisation AC
                        AFCotisation cotiAf = affiliation._cotisation(transaction, affiliation.getAffiliationId(),
                                CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AC, debutPeriode,
                                finPeriode, 1);
                        if (cotiAf == null) {
                            affiliation = null;
                        }
                    }
                }
            }
        }
        return affiliation;
    }

    public static String returnAffilieNumero(BSession session, String idAffiliation) {
        AFAffiliation aff = new AFAffiliation();
        aff.setSession(session);
        aff.setAffiliationId(idAffiliation);
        try {
            aff.retrieve();
            if (!aff.isNew() && (aff != null)) {
                return aff.getAffilieNumero();
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    /**
     * Retourne le nombre d'affilion d'un tiers ou d'un n° d'affilié pour une année Test du type de d'affiliation selon
     * le genre de la décision
     */
    public static int returnNbAffiliation(BSession session, BTransaction transaction, String numAffilie, String annee,
            String genreDem, int modeRecherche) throws Exception {
        int nb = 0;
        AFAffiliation affiliation = new AFAffiliation();
        affiliation.setSession(session);
        if (JadeStringUtil.isIntegerEmpty(genreDem) || CPDecision.CS_NON_ACTIF.equals(genreDem)
                || CPDecision.CS_ETUDIANT.equals(genreDem)) {
            nb = affiliation._retourNbAffiliation(transaction, numAffilie, annee, CodeSystem.TYPE_AFFILI_NON_ACTIF,
                    modeRecherche);
            // Si null -> recherche NA selon art. 1A...
            if (nb == 0) {
                affiliation = new AFAffiliation();
                affiliation.setSession(session);
                nb = affiliation._retourNbAffiliation(transaction, numAffilie, annee,
                        CodeSystem.TYPE_AFFILI_SELON_ART_1A, modeRecherche);
            } else {
                return nb;
            }
        }
        if (JadeStringUtil.isIntegerEmpty(genreDem) || CPDecision.CS_TSE.equals(genreDem)) {
            affiliation = new AFAffiliation();
            affiliation.setSession(session);
            nb = affiliation._retourNbAffiliation(transaction, numAffilie, annee, CodeSystem.TYPE_AFFILI_TSE,
                    modeRecherche);
            if (nb == 0) {
                affiliation = new AFAffiliation();
                affiliation.setSession(session);
                nb = affiliation._retourNbAffiliation(transaction, numAffilie, annee,
                        CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE, modeRecherche);
            }
        }
        // Test sur indépendant si on a rien trouvé (affiliation==null) lorsque
        // le genre demandé est blanc
        // ou différent de non actif
        if (((nb == 0) || JadeStringUtil.isEmpty(affiliation.getAffiliationId()))) {
            if (!CPDecision.CS_NON_ACTIF.equals(genreDem) && !CPDecision.CS_ETUDIANT.equals(genreDem)) { // différent
                // indépendant
                affiliation = new AFAffiliation();
                affiliation.setSession(session);
                nb = affiliation._retourNbAffiliation(transaction, numAffilie, annee, CodeSystem.TYPE_AFFILI_INDEP,
                        modeRecherche);
                // Si null -> recherche indépendant et employeur
                if (nb == 0) {
                    affiliation = new AFAffiliation();
                    affiliation.setSession(session);
                    nb = affiliation._retourNbAffiliation(transaction, numAffilie, annee,
                            CodeSystem.TYPE_AFFILI_INDEP_EMPLOY, modeRecherche);
                } else {
                    return nb;
                }
                // Recherche TSE pour les fisc qui les communiquent sous
                // indépendant (ex JURA) ==> recherche si assurance chomage
                if (nb == 0) {
                    affiliation = new AFAffiliation();
                    affiliation.setSession(session);
                    nb = affiliation._retourNbAffiliation(transaction, numAffilie, annee, CodeSystem.TYPE_AFFILI_TSE,
                            modeRecherche);
                    if (nb == 0) {
                        affiliation = new AFAffiliation();
                        affiliation.setSession(session);
                        nb = affiliation._retourNbAffiliation(transaction, numAffilie, annee,
                                CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE, modeRecherche);
                    }
                }
            }
        }
        return nb;
    }

    /**
     * Retourne le nombre de décision pour une année, une affiliation, un type et selon le code actif
     * 
     * @param session
     * @param anneeDecision
     * @param idAffiliation
     * @param typeDecision
     * @param active
     * @return
     * @throws Exception
     */
    public static int returnNombreDecision(BSession session, String anneeDecision, String idAffiliation,
            String typeDecision, Boolean active) throws Exception {

        if (JadeStringUtil.isBlankOrZero(idAffiliation) && JadeStringUtil.isBlankOrZero(anneeDecision)
                && JadeStringUtil.isBlankOrZero(typeDecision)) {
            throw new Exception("Insufficient number of arguments");
        }

        CPDecisionManager mng = new CPDecisionManager();
        mng.setSession(session);
        mng.setForAnneeDecision(anneeDecision);
        mng.setForIdAffiliation(idAffiliation);
        mng.setForTypeDecision(typeDecision);
        mng.setForIsActive(active);
        mng.find();
        return mng.getSize();
    }

    public static void suppressionFacture(BSession session, BTransaction transaction, CPDecision decision)
            throws Exception {
        // Suppression entete facture et ligne de facture (afact)
        FAEnteteFactureManager entFactureManager = new FAEnteteFactureManager();
        entFactureManager.setSession(session);
        entFactureManager.setForIdTiers(decision.getIdTiers());
        entFactureManager.setForIdPassage(decision.getIdPassage());
        if (CPDecision.CS_ETUDIANT.equalsIgnoreCase(decision.getGenreAffilie())) {
            entFactureManager.setForIdTypeFacture(APISection.ID_TYPE_SECTION_ETUDIANTS);
        } else {
            entFactureManager.setForIdTypeFacture(APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION);
        }
        // Osiris CS_DECISION_COT_PERS
        entFactureManager.setForIdRole(CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                session.getApplication()));
        boolean factureParAnnee = ((CPApplication) session.getApplication()).isFactureParAnnee();
        if (factureParAnnee) {
            entFactureManager.setLikeIdExterneFacture(decision.getAnneeDecision());
        }
        entFactureManager.find(transaction);
        if (entFactureManager.size() > 0) {
            FAEnteteFacture entete = new FAEnteteFacture();
            entete.setSession(session);
            entete.setIdEntete(((FAEnteteFacture) entFactureManager.getEntity(0)).getIdEntete());
            entete.retrieve();
            if (!entete.isNew()) {
                // Recherche des lignes de facture
                FAAfactManager afactManager = new FAAfactManager();
                afactManager.setSession(session);
                afactManager.setForIdEnteteFacture(entete.getIdEntete());
                afactManager.find(transaction);
                for (int i = 0; i < afactManager.getSize(); i++) {
                    FAAfact afact = (FAAfact) afactManager.getEntity(i);
                    afact.wantCallMethodAfter(false);
                    afact.delete(transaction);
                }
                entete.delete(transaction);
            }
        }
    }

    /**
     * Retourne un numéro sans format
     * 
     * @param String
     * @return String
     * @throws Exception
     */
    public static String unFormat(String chaineFormat) throws Exception {
        String numSansFormat = JAStringFormatter.unFormat(chaineFormat, "", ".");
        numSansFormat = JAStringFormatter.unFormat(numSansFormat, "", "/");
        numSansFormat = JAStringFormatter.unFormat(numSansFormat, "", "-");
        numSansFormat = JadeStringUtil.removeChar(numSansFormat, ' ');
        return numSansFormat = JAStringFormatter.unFormat(numSansFormat, "", "_");
    }

    /**
     * Commentaire relatif au constructeur CPToolBox.
     */
    public CPToolBox() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 11:37:07)
     * 
     * @return globaz.pyxis.db.tiers.TITiersViewBean
     */
    public globaz.pyxis.db.tiers.TITiersViewBean getTiers(BSession session, String idTiers) {
        // TODO: enregistrement déjà chargé ?
        TITiersViewBean tiers = null;
        if (tiers == null) {
            // liste pas encore chargée, on la charge
            tiers = new globaz.pyxis.db.tiers.TITiersViewBean();
            tiers.setSession(session);
        }
        if (!JadeStringUtil.isIntegerEmpty(idTiers)) {
            try {
                tiers.setIdTiers(idTiers);
                tiers.retrieve();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tiers;
    }

    /**
     * Retourne le nombre de mois à facturer pour une cotisation Date de création : (30.04.2003 14:14:07)
     * 
     * @return int
     * @param session
     *            globaz.globall.db.BSession
     * @param transaction
     *            globaz.globall.db.BTransaction
     * @param cotisation
     *            globaz.phenix.db.principale.CPCotisation
     * @param affiliation
     *            globaz.naos.db.affiliation.AFAffiliation
     * @param passage
     *            globaz.musca.api.IFAPassage
     */
    public int nbMoisFacture(BSession session, CPCotisation cotisation, AFAffiliation affiliation, IFAPassage passage) {
        // Si l'année de la décision concerne l'année en cours, il faut calculer
        // le nombre
        // de mois selon la dernière facturation périodique (afin de ne pas
        // facturer à double)
        try {
            int anneeDecision = JACalendar.getYear(cotisation.getDebutCotisation());
            int moisDebut = 0;
            int moisFin = 0;
            // Si l'affilié est radié pendant la période de décision, les dates
            // de début
            // et de fin doivent être prise par rapport à la cotisation de
            // l'affiliation
            AFCotisation cotiAf = new AFCotisation();
            cotiAf.setSession(session);
            cotiAf.setCotisationId(cotisation.getIdCotiAffiliation());
            cotiAf.retrieve();
            int anneeDebutCotisAf = JACalendar.getYear(cotiAf.getDateDebut());
            int anneeFinAcotiAf = JACalendar.getYear(cotiAf.getDateFin());
            if ((anneeDebutCotisAf == anneeDecision)
                    && (BSessionUtil.compareDateFirstGreater(session, cotiAf.getDateDebut(),
                            cotisation.getDebutCotisation()))) {
                moisDebut = JACalendar.getMonth(cotiAf.getDateDebut());
            } else {
                moisDebut = JACalendar.getMonth(cotisation.getDebutCotisation());
            }
            if ((anneeFinAcotiAf == anneeDecision)
                    && (BSessionUtil.compareDateFirstLower(session, cotiAf.getDateFin(), cotisation.getFinCotisation()))) {
                moisFin = JACalendar.getMonth(cotiAf.getDateFin());
            } else {
                moisFin = JACalendar.getMonth(cotisation.getFinCotisation());
            }

            int ecart = 0;

            if (passage != null) {
                // Si l'anné de la dernière facturation périodique est plus
                // grande que la date de fin de la décision => tout est rétro
                int anneeCotisation = JACalendar.getYear(cotisation.getFinCotisation());
                int anneePassage = JACalendar.getYear(passage.getDatePeriode());
                if (anneeCotisation < anneePassage) {
                    return 99; // => Il sera pris le montant de la cotisation
                    // annuelle
                }
                // Si la date de début de décision > date de dernière
                // facturation
                // => ne rien facturer, le cas sera pris lors de la périodique
                else if (BSessionUtil.compareDateFirstGreater(session, cotisation.getDebutCotisation(),
                        "01." + passage.getDatePeriode())) {
                    return 0;
                }
                // Détermination de la période minimum que le passage doit avoir
                // pour que la cotisation soit entièrement retro active.
                // La détermination de cette date doit tenir compte de la
                // périodicité.
                // Ex: Si dernierPassage= 02.2007 => si coti = 1.01.07 au
                // 31.01.07 => rétro total pour cas mensuel mais 0 si
                // trimestriel
                int moisMaxPossible = 12;
                int moisMinPossible = 0;
                int moisPassage = JACalendar.getMonth(passage.getDatePeriode());
                if (CodeSystem.PERIODICITE_MENSUELLE.equalsIgnoreCase(cotiAf.getPeriodicite())) {
                    moisMaxPossible = moisPassage;
                } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equalsIgnoreCase(cotiAf.getPeriodicite())) {
                    if (moisPassage < 3) {
                        moisMaxPossible = 0;
                    } else if (moisPassage < 6) {
                        moisMaxPossible = 3;
                    } else if (moisPassage < 9) {
                        moisMaxPossible = 6;
                    } else if (moisPassage < 12) {
                        moisMaxPossible = 9;
                    } else {
                        moisMaxPossible = 12;
                    }

                } else if (CodeSystem.PERIODICITE_ANNUELLE.equalsIgnoreCase(cotiAf.getPeriodicite())) {
                    if (!JadeStringUtil.isIntegerEmpty(cotiAf.getTraitementMoisAnnee())) {
                        moisMinPossible = Integer
                                .parseInt(JadeStringUtil.substring(cotiAf.getTraitementMoisAnnee(), 4));
                    } else {
                        moisMinPossible = 12;
                    }
                }
                if ((moisFin <= moisMaxPossible) && (moisPassage >= moisMinPossible)) {
                    return 99; // => Il sera pris le montant de la cotisation
                    // annuelle
                } else if ((moisPassage + 1) < moisMinPossible) {
                    return 0;
                } else {
                    // sinon il faut prendre jusqu'à la dernière facturation
                    // afin
                    // de ne pas facturer à double avec la facturation
                    // périodique.
                    int anneeFacturation = JACalendar.getYear(passage.getDatePeriode());
                    moisFin = JACalendar.getMonth(passage.getDatePeriode());
                    // Test si la dernière facturation concerne la périodicité
                    // de l'affiliation
                    // si C'est pas le cas redéterminer le mois de fin.
                    if (cotiAf.getPeriodicite().equalsIgnoreCase(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                        moisFin = (moisFin / 3) * 3;
                    } else if (cotisation.getPeriodicite().equalsIgnoreCase("802003")) {
                        moisFin = (moisFin / 6) * 6;
                    } else if (cotiAf.getPeriodicite().equalsIgnoreCase(CodeSystem.PERIODICITE_ANNUELLE)) {
                        moisFin = (moisFin / 12) * 12;
                    }
                    if (anneeFacturation < anneeDecision) {
                        moisDebut = moisDebut + 12;
                        ecart = moisDebut - moisFin;
                        return ecart;
                    } else {
                        ecart = (moisFin - moisDebut) + 1;
                        return ecart;
                    }
                }
            } else {
                // Aucune facturation périodique de faite
                return 0;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Retourne le nombre de mois à facturer pour une cotisation Date de création : (30.04.2003 14:14:07)
     * 
     * @return int
     * @param session
     *            globaz.globall.db.BSession
     * @param transaction
     *            globaz.globall.db.BTransaction
     * @param cotisation
     *            globaz.phenix.db.principale.CPCotisation
     * @param affiliation
     *            globaz.naos.db.affiliation.AFAffiliation
     * @param passage
     *            globaz.musca.api.IFAPassage
     */
    public int nbMoisFacture(BSession session, CPDecisionAffiliationTiers decision, AFAffiliation affiliation,
            IFAPassage passage) {
        // Si l'année de la décision concerne l'année en cours, il faut calculer
        // le nombre
        // de mois selon la dernière facturation périodique (afin de ne pas
        // facturer à double)
        try {
            int anneeDecision = JACalendar.getYear(decision.getDebutDecision());
            int moisDebut = 0;
            int moisFin = 0;
            // Si l'affilié est radié pendant la période de décision, les dates
            // de début
            // et de fin doivent être prise par rapport à la cotisation de
            // l'affiliation
            moisDebut = JACalendar.getMonth(decision.getDebutDecision());
            moisFin = JACalendar.getMonth(decision.getFinDecision());
            int ecart = 0;
            if (passage != null) {
                // Si l'anné de la dernière facturation périodique est plus
                // grande que la date de fin de la décision => tout est rétro
                int anneeFinDecision = JACalendar.getYear(decision.getFinDecision());
                int anneePassage = JACalendar.getYear(passage.getDatePeriode());
                if (anneeFinDecision < anneePassage) {
                    return 99; // => Il sera pris le montant de la cotisation
                    // annuelle
                }
                // Si la date de début de décision > date de dernière
                // facturation
                // => ne rien facturer, le cas sera pris lors de la périodique
                else if (BSessionUtil.compareDateFirstGreater(session, decision.getDebutDecision(),
                        "01." + passage.getDatePeriode())) {
                    return 0;
                }
                // Détermination de la période minimum que le passage doit avoir
                // pour que la cotisation soit entièrement retro active.
                // La détermination de cette date doit tenir compte de la
                // périodicité.
                // Ex: Si dernierPassage= 02.2007 => si coti = 1.01.07 au
                // 31.01.07 => rétro total pour cas mensuel mais 0 si
                // trimestriel
                int moisMaxPossible = 12;
                int moisMinPossible = 0;
                int moisPassage = JACalendar.getMonth(passage.getDatePeriode());
                if (CodeSystem.PERIODICITE_MENSUELLE.equalsIgnoreCase(affiliation.getPeriodicite())) {
                    moisMaxPossible = moisPassage;
                } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equalsIgnoreCase(affiliation.getPeriodicite())) {
                    if (moisPassage < 3) {
                        moisMaxPossible = 0;
                    } else if (moisPassage < 6) {
                        moisMaxPossible = 3;
                    } else if (moisPassage < 9) {
                        moisMaxPossible = 6;
                    } else if (moisPassage < 12) {
                        moisMaxPossible = 9;
                    } else {
                        moisMaxPossible = 12;
                    }

                } else if (CodeSystem.PERIODICITE_ANNUELLE.equalsIgnoreCase(affiliation.getPeriodicite())) {
                    moisMinPossible = 13;
                }
                if ((moisFin <= moisMaxPossible) && (moisPassage > moisMinPossible)) {
                    return 99; // => Il sera pris le montant de la cotisation
                    // annuelle
                } else {
                    // sinon il faut prendre jusqu'à la dernière facturation
                    // afin
                    // de ne pas facturer à double avec la facturation
                    // périodique.
                    int anneeFacturation = JACalendar.getYear(passage.getDatePeriode());
                    moisFin = JACalendar.getMonth(passage.getDatePeriode());
                    // Test si la dernière facturation concerne la périodicité
                    // de l'affiliation
                    // si C'est pas le cas redéterminer le mois de fin.
                    if (affiliation.getPeriodicite().equalsIgnoreCase(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                        moisFin = (moisFin / 3) * 3;
                    } else if (affiliation.getPeriodicite().equalsIgnoreCase("802003")) {
                        moisFin = (moisFin / 6) * 6;
                    } else if (affiliation.getPeriodicite().equalsIgnoreCase(CodeSystem.PERIODICITE_ANNUELLE)) {
                        moisFin = (moisFin / 12) * 12;
                    }
                    if (anneeFacturation < anneeDecision) {
                        moisDebut = moisDebut + 12;
                        ecart = moisDebut - moisFin;
                        return ecart;
                    } else {
                        ecart = (moisFin - moisDebut) + 1;
                        return ecart;
                    }
                }
            } else {
                // Aucune facturation périodique de faite
                return 0;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Retourne l'ancienne cotisation d'un affilié par rapport à une décision utilisée par ex pour mettre l'ancien
     * montant sur une facture Date de création : (30.04.2003 11:54:08)
     * 
     * @param session
     *            BSession
     * @param trans
     *            BTransaction
     * @param decision
     *            CPDecision
     */
    public CPCotisation rechercheCotisation(BSession session, BTransaction trans, CPDecision decision) {
        try {
            // décision précédente = dernière décision facturée pour une année
            // et de même type
            CPDecisionManager decManager = new CPDecisionManager();
            decManager.setSession(session);
            decManager.setForGenreAffilie(decision.getGenreAffilie());
            decManager.setForIdTiers(decision.getIdTiers());
            decManager.setForAnneeDecision(decision.getAnneeDecision());
            decManager.setInEtat(CPDecision.CS_FACTURATION + ", " + CPDecision.CS_PB_COMPTABILISATION);
            decManager.orderByAnneeDecision();
            decManager.orderByIdDecision();
            decManager.find();
            for (int i = 0; i < decManager.size(); i++) {
                CPDecision decisionLue = (CPDecision) decManager.getEntity(i);
                if (decision.getGenreAffilie().equalsIgnoreCase(decisionLue.getGenreAffilie())
                        && !CPDecision.CS_IMPUTATION.equalsIgnoreCase(decisionLue.getTypeDecision())) {
                    // Recherche idCoti pour cot. pers. (naos)
                    String idCoti = AFAffiliation._idCotisation(trans, decision.getIdAffiliation(),
                            CodeSystem.TYPE_ASS_COTISATION_AVS_AI, decision.getDebutDecision(),
                            decision.getFinDecision());
                    if (!JadeStringUtil.isEmpty(idCoti)) {
                        // Recherche de la coti. annuelle
                        CPCotisation coti = new CPCotisation();
                        coti.setSession(session);
                        coti.setIdDecision(decisionLue.getIdDecision());
                        coti.setIdCotiAffiliation(idCoti);
                        coti.setAlternateKey(1);
                        coti.retrieve(trans);
                        if (!coti.isNew()) {
                            return coti;
                        }
                    }
                }
            }
            return null;
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
            return null;
        }
    }
}
