package globaz.phenix.db.principale;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.api.IFAPassage;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.ServicesFacturation;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.comptes.CARubrique;
import globaz.pavo.db.compte.CICompteIndividuelUtil;
import globaz.phenix.application.CPApplication;
import globaz.phenix.interfaces.IDecision;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import globaz.pyxis.db.tiers.TIRole;
import java.math.BigDecimal;

public class CPSortie extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // AJOUT MANUEL
    private globaz.pyxis.api.ITITiers _tiers = null;

    /**
     * Fichier CPSORTP
     */

    // variable de travail
    private AFAffiliation affiliation = null;
    /** (IAANNE) */
    private String annee = "";
    /** (IZBCHK) */
    // changement manuel: par défaut mettre le flag a true
    private Boolean checked = new Boolean(true);
    /** (MAIAFF) */
    private String idAffiliation = "";
    /** (IAIDEC) */
    private java.lang.String idDecision = "";
    /** (EBIPAS) */
    private String idPassage = "";
    /** (IZISOR) */
    private String idSortie = "";
    /** (HTITIE) */
    private String idTiers = "";
    /** (IZBREC) */
    private Boolean isRecap = new Boolean(false);
    /** (IZBSPE) */
    private Boolean isSpecial = new Boolean(false);
    /** (IZMMCI) */
    private String montantCI = "";
    /** (IZTMOT) */
    private String motif = "";
    /** (MALNAF) */
    private String noAffilie = "";

    private String nomTiers = "";
    private String referenceFacture = "";

    public CPSortie() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdSortie(this._incCounter(transaction, idSortie));

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        BStatement statement = new BStatement(transaction);
        statement.createStatement();
        statement.execute("DELETE FROM " + _getCollection() + "CPMOSOP WHERE " + _getCollection() + "CPMOSOP.IZISOR="
                + getIdSortie());
        statement.closeStatement();
        statement = null;
    }

    /*
     * Création d'un enreg de sortie pour la CSC
     */
    protected void _createSortie(BTransaction transaction, BSession sessionPhenix, IDecision decision,
            AFAffiliation affiliation, FAPassage passage, boolean prorata, boolean remboursement, int nbMoisExtourne,
            String dateFin, boolean anneeEnCoursTraite, CACompteAnnexe compte, boolean modifDateDebutAff)
            throws java.lang.Exception {

        String montantCI = "";
        String montant = "";
        IFAPassage dernierePeriodique = null;
        if (affiliation.getTypeAffiliation().equalsIgnoreCase(CodeSystem.TYPE_AFFILI_NON_ACTIF)
                || affiliation.getTypeAffiliation().equalsIgnoreCase(CodeSystem.TYPE_AFFILI_SELON_ART_1A)) {
            ServicesFacturation.getDernierPassageFacturation(affiliation.getSession(), transaction,
                    FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS + ", "
                            + FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS_NAC);
        } else {
            ServicesFacturation.getDernierPassageFacturation(affiliation.getSession(), transaction,
                    FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS + ", "
                            + FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS_IND);
        }
        // Création des sortie
        CPSortie sortieDecision = new CPSortie();
        sortieDecision.setSession(sessionPhenix);
        if (modifDateDebutAff) {
            sortieDecision.setIsSpecial(new Boolean(false));
        } else {
            sortieDecision.setIsSpecial(new Boolean(true));
        }
        sortieDecision.setChecked(new Boolean(true));
        sortieDecision.setAnnee(decision.getAnneeDecision());
        sortieDecision.setReferenceFacture(affiliation.getSession().getUserId());
        sortieDecision.setIdAffiliation(decision.getIdAffiliation());
        sortieDecision.setIdTiers(decision.getIdTiers());
        sortieDecision.setNoAffilie(affiliation.getAffilieNumero());
        sortieDecision.setIdPassage(passage.getIdPassage());
        sortieDecision.setMotif(affiliation.getMotifFin());
        sortieDecision.setIsRecap(new Boolean(false));
        sortieDecision.setIdDecision(decision.getIdDecision());
        // CI
        CPDonneesCalcul donnee = new CPDonneesCalcul();
        donnee.setSession(sessionPhenix);
        if (CPDecision.CS_REMISE.equals(decision.getTypeDecision())) {
            TIHistoriqueAvsManager histManager = new TIHistoriqueAvsManager();
            histManager.setSession(affiliation.getSession());
            histManager.setForIdTiers(decision.getIdTiers());
            histManager.orderByNumAvs();
            histManager.find();
            BigDecimal montantEnCI = new BigDecimal("0");
            String numAvs = "";
            String numAvsBk = "";
            for (int i = 0; i < histManager.size(); i++) {
                TIHistoriqueAvs histo = ((TIHistoriqueAvs) histManager.getEntity(i));
                numAvs = histo.getNumAvs();
                if (!numAvs.equalsIgnoreCase(numAvsBk) && !"506006".equalsIgnoreCase(histo.getMotifHistorique())) {
                    numAvsBk = numAvs;
                    CICompteIndividuelUtil compteIndividuel = new CICompteIndividuelUtil();
                    compteIndividuel.setSession(affiliation.getSession());
                    montantEnCI = montantEnCI.add(compteIndividuel.getSommeParAnneeCotPers(numAvs,
                            decision.getAnneeDecision(), false));
                }
            }
            montantCI = montantEnCI.toString();

        } else {
            montantCI = donnee.getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_CI);
        }
        if (!JadeStringUtil.isEmpty(montantCI)) {
            if (remboursement) {
                if (CPDecision.CS_IMPUTATION.equals(decision.getTypeDecision())) {
                    sortieDecision.setMontantCI(montantCI);
                } else {
                    sortieDecision.setMontantCI("-" + montantCI);
                }
            } else {
                if (CPDecision.CS_IMPUTATION.equals(decision.getTypeDecision())) {
                    sortieDecision.setMontantCI("-" + montantCI);
                } else {
                    sortieDecision.setMontantCI(montantCI);
                }
            }
            if (prorata) {
                // Calcul du nombre de mois de la période
                int moisDebut = JACalendar.getMonth(decision.getDebutDecision());
                int moisFin = JACalendar.getMonth(decision.getFinDecision());
                FWCurrency varCurrency = new FWCurrency(sortieDecision.getMontantCI());
                float varTemp = varCurrency.floatValue();
                varTemp = varTemp * nbMoisExtourne;
                varTemp = JANumberFormatter.round(varTemp / (moisFin - moisDebut + 1), 1, 0, JANumberFormatter.NEAR);
                sortieDecision.setMontantCI(String.valueOf(varTemp));
            }
        }
        sortieDecision.add(transaction);
        // Lecture des cotisations à rembourser
        if (CPDecision.CS_IMPUTATION.equals(decision.getTypeDecision())) {
            // Recherche de la rubrique d'imputation
            String refRubrique = "";
            CAReferenceRubrique refRub = new CAReferenceRubrique();

            refRub.setSession(sessionPhenix);
            refRubrique = refRub.getRubriqueByCodeReference(APIReferenceRubrique.IMPUTATION_DE_COTISATION_PERSONNELLE)
                    .getIdRubrique();
            /*
             * Ne plus aller rechercher le montant dans les compteurs car il peut y avoir plusieurs Imputation pour une
             * même année.
             */

            /*
             * montant = CPToolBox.rechMontantFacture(sessionPhenix, transaction, compte.getIdCompteAnnexe(),
             * refRubrique, decision.getAnneeDecision());
             */

            CPDonneesBase base = new CPDonneesBase();
            base.setSession(sessionPhenix);
            base.setIdDecision(decision.getIdDecision());
            base.retrieve();
            if (!base.isNew()) {
                montant = base.getCotisation1();
            }
            CPSortieMontant montantSortie = new CPSortieMontant();
            montantSortie.setSession(sessionPhenix);
            montantSortie.setIdSortie(sortieDecision.getIdSortie());
            montantSortie.setDebutSortie(decision.getDebutDecision());
            montantSortie.setFinSortie(decision.getFinDecision());
            if (!JadeStringUtil.isBlankOrZero(montant)) {
                if (!remboursement) {
                    montant = CPToolBox.multString(montant, "-1");
                }
                montantSortie.setMontant(montant);
                montantSortie.setAssurance(CPToolBox.multString(refRubrique, "-1"));
                montantSortie.add(transaction);
            }
            // Si frais sur imputation
            if (((CPApplication) sessionPhenix.getApplication()).isRemboursementFraisAdmin()) {
                // Recherche du compteur frais imputation
                refRubrique = refRub.getRubriqueByCodeReference(
                        APIReferenceRubrique.IMPUTATION_DE_FAD_COTISATION_PERSONNELLE).getIdRubrique();
                AFAffiliation aff = new AFAffiliation();
                aff.setSession(sessionPhenix);
                AFCotisation cotiAf = aff._cotisation(transaction, decision.getIdAffiliation(),
                        CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_FRAIS_ADMIN, affiliation.getDateDebut(),
                        decision.getFinDecision(), 1);
                String dateRef = decision.getDebutDecision();
                if ((cotiAf != null)
                        && (Integer.parseInt(decision.getAnneeDecision()) == JACalendar.getYear(cotiAf.getDateDebut()))
                        && BSessionUtil.compareDateFirstGreater(sessionPhenix, cotiAf.getDateDebut(),
                                decision.getDebutDecision())) {
                    dateRef = cotiAf.getDateDebut();
                }
                float taux = 0;
                try {
                    if (cotiAf == null) {
                        taux = 1;
                    } else {
                        taux = Float.parseFloat(JANumberFormatter.deQuote(cotiAf.getTaux(dateRef, "0")));
                    }
                } catch (Exception e) {
                    taux = 3;
                }
                float calcul = (new FWCurrency(montant).floatValue() * taux) / 100;
                montant = ""
                        + JANumberFormatter.round(Float.parseFloat(Float.toString(calcul)), 0.05, 2,
                                JANumberFormatter.NEAR);
                if (!JadeStringUtil.isBlankOrZero(montant)) {
                    montantSortie.setIdSortieMontant("");
                    montantSortie.setMontant(montant);
                    montantSortie.setAssurance(CPToolBox.multString(refRubrique, "-1"));
                    montantSortie.add(transaction);
                }
            }
        } else {
            // Ne rien générer pour les décisions complémentaires car les
            // montants on déjà été
            // extournés sur la première décision
            if (!Boolean.TRUE.equals(decision.getComplementaire())) {
                CPCotisationManager cotiManager = new CPCotisationManager();
                cotiManager.setSession(sessionPhenix);
                cotiManager.setForIdDecision(decision.getIdDecision());
                cotiManager.find(transaction);
                for (int i = 0; i < cotiManager.size(); i++) {
                    String varDateFin = "";
                    CPCotisation coti = (CPCotisation) cotiManager.getEntity(i);
                    // Correction problème arrondi pour FAD -> éviter de faire le calcul si nombre de mois de la
                    // décision = nombre de mois à extourner
                    int moisPeriode = JACalendar.getMonth(coti.getFinCotisation())
                            - JACalendar.getMonth(coti.getDebutCotisation()) + 1;
                    if (moisPeriode == nbMoisExtourne) {
                        prorata = false;
                    }
                    // Si année > année en cours => ne rien remboursé car rien
                    // n'a encore été facturé
                    if (Integer.parseInt(decision.getAnneeDecision()) <= JACalendar.today().getYear()) {
                        CPSortieMontant montantSortie = new CPSortieMontant();
                        montantSortie.setSession(sessionPhenix);
                        montantSortie.setIdSortie(sortieDecision.getIdSortie());
                        if (!CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())) {
                            // Si année en cours => aller prendre le montant qui a
                            // été facturé pour la cotisation
                            // car la décision n'a pas encore été entiérement
                            // facturé.
                            // A ne faire qu'une seule fois pour l'année
                            if (Integer.parseInt(decision.getAnneeDecision()) == JACalendar.today().getYear()) {
                                if (anneeEnCoursTraite) {
                                    montant = "";
                                } else {
                                    if (!remboursement) {
                                        // Pour l'année en cours, refacturer jusqu'à
                                        // la dernière périodique traité (se baser
                                        // sur la facturation des cot. pers.)
                                        // Détermination du nombre de mois à
                                        // facturer
                                        int nbMoisPeriode = 0;
                                        if (coti.getPeriodicite()
                                                .equalsIgnoreCase(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                                            nbMoisPeriode = 3;
                                        } else if (coti.getPeriodicite().equalsIgnoreCase(
                                                CodeSystem.PERIODICITE_MENSUELLE)) {
                                            nbMoisPeriode = 1;
                                        } else {
                                            nbMoisPeriode = 12;
                                        } // Recherche du nombre de mois à facturer
                                        CPToolBox toolBox = new CPToolBox();
                                        int nbMois = 0;
                                        // Si Etudiant => facturer le montant annuel
                                        // (facturation immédinate) car
                                        // l'affiliation
                                        // n'est pas mise à jour => pas de
                                        // périodique
                                        if (CPDecision.CS_ETUDIANT.equalsIgnoreCase(decision.getGenreAffilie())) {
                                            nbMois = 99;
                                        } else {
                                            nbMois = toolBox.nbMoisFacture(affiliation.getSession(), coti, affiliation,
                                                    dernierePeriodique);
                                        }
                                        if (nbMois > 0) {
                                            // Si le nombre de mois n'est pas
                                            // divisible par le nombre de mois de la
                                            // période
                                            // de facturation de l'affilié, il faut
                                            // prendre le montant mensuel.
                                            // Calcul du nombre de période à
                                            // facturer
                                            int nbPeriode = 0;
                                            if ((nbMois % nbMoisPeriode) == 0) {
                                                nbPeriode = nbMois / nbMoisPeriode;
                                            } else {
                                                nbPeriode = nbMois;
                                            }
                                            if (nbPeriode > 0) {
                                                // Calcul du montant initial
                                                float varMontant = 0;
                                                // Si tout est rétro => prendre le
                                                // montant annuel de la cotisation -
                                                // Indiquer par nbMois=99
                                                if (nbMois == 99) {
                                                    if (!JadeStringUtil.isBlank(coti.getMontantAnnuel())) {
                                                        varMontant = Float.parseFloat(JANumberFormatter.deQuote(coti
                                                                .getMontantAnnuel()));
                                                    }
                                                    nbPeriode = 1;
                                                } else {
                                                    if ((nbMois % nbMoisPeriode) == 0) {
                                                        if (coti.getPeriodicite().equalsIgnoreCase(
                                                                CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                                                            if (!JadeStringUtil.isBlank(coti.getMontantTrimestriel())) {
                                                                varMontant = Float.parseFloat(JANumberFormatter
                                                                        .deQuote(coti.getMontantTrimestriel()));
                                                            }
                                                        } else if (coti.getPeriodicite().equalsIgnoreCase(
                                                                CodeSystem.PERIODICITE_MENSUELLE)) {
                                                            if (!JadeStringUtil.isBlank(coti.getMontantMensuel())) {
                                                                varMontant = Float.parseFloat(JANumberFormatter
                                                                        .deQuote(coti.getMontantMensuel()));
                                                            }
                                                        } else {
                                                            if (!JadeStringUtil.isBlank(coti.getMontantAnnuel())) {
                                                                varMontant = Float.parseFloat(JANumberFormatter
                                                                        .deQuote(coti.getMontantAnnuel()));
                                                            }
                                                        }
                                                    } else if (!JadeStringUtil.isBlank(coti.getMontantMensuel())) {
                                                        varMontant = Float.parseFloat(JANumberFormatter.deQuote(coti
                                                                .getMontantMensuel()));
                                                    }
                                                } // -- calcul
                                                FWCurrency montantInt = new FWCurrency(varMontant * nbPeriode);
                                                varMontant = Float.parseFloat(montantInt.toString());
                                                // Forcer le montant initial à 0 si
                                                // il est infèrieur au montant
                                                // minimum défini dans l'assurance
                                                if (Boolean.TRUE.equals(coti.getAForceAZero())) {
                                                    varMontant = 0;
                                                }
                                                montant = Float.toString(varMontant);
                                            } else {
                                                montant = "";
                                            }
                                            if (nbMois != 99) {
                                                JACalendar cal = new JACalendarGregorian();
                                                varDateFin = cal.lastInMonth(cal.addMonths(decision.getDebutDecision(),
                                                        nbMois - 1));
                                            }
                                        }
                                    } else {
                                        if (compte != null) {
                                            montant = CPToolBox.rechMontantFacture(sessionPhenix, transaction, compte
                                                    .getIdCompteAnnexe(), AFCotisation._getRubrique(
                                                    coti.getIdCotiAffiliation(), sessionPhenix), decision
                                                    .getAnneeDecision());
                                        }
                                    }
                                }
                            } else if (prorata) {
                                // Correction problème arrondi pour FAD -> éviter de faire le calcul si nombre de mois
                                // de la
                                // décision = nombre de mois à extourner
                                montant = CPToolBox.getExtourneProrata(nbMoisExtourne, decision.getDebutDecision(),
                                        decision.getFinDecision(), JANumberFormatter.deQuote(coti.getMontantAnnuel()));
                            } else {
                                if (!remboursement) {
                                    montant = coti.getMontantAnnuel();
                                } else {
                                    if (compte != null) {
                                        montant = CPToolBox.rechMontantFacture(sessionPhenix, transaction,
                                                compte.getIdCompteAnnexe(),
                                                AFCotisation._getRubrique(coti.getIdCotiAffiliation(), sessionPhenix),
                                                decision.getAnneeDecision());
                                    }
                                }
                            }
                            // Création des montants de la sortie
                            if (!JadeStringUtil.isEmpty(montant)) {
                                float tstMontant = JadeStringUtil.toFloat(JANumberFormatter.deQuote(montant));
                                if (remboursement && (tstMontant > 0)) {
                                    montant = "-" + montant;
                                }
                                montantSortie.setMontant(montant);
                                montantSortie.setAssurance(coti.getIdCotiAffiliation());
                                montantSortie.setDebutSortie(decision.getDebutDecision());
                                if (!JadeStringUtil.isBlankOrZero(varDateFin)) {
                                    montantSortie.setFinSortie(varDateFin); // Cas
                                    // de
                                    // refacturation
                                    // de
                                    // l'année
                                    // en
                                    // cours
                                } else {
                                    montantSortie.setFinSortie(dateFin);
                                }
                                montantSortie.add(transaction);
                            }
                        }
                        // Sortie des remises et réductions
                        if (CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())
                                || CPDecision.CS_REDUCTION.equalsIgnoreCase(decision.getTypeDecision())) {
                            // Recherche ref rubrique remise qui se trouve dans
                            // les paramtrère d'assurance de NAOS
                            CAReferenceRubrique refRub = new CAReferenceRubrique();
                            refRub.setSession(sessionPhenix);
                            AFCotisation cotiAf = new AFCotisation();
                            cotiAf.setSession(sessionPhenix);
                            cotiAf.setCotisationId(coti.getIdCotiAffiliation());
                            cotiAf.retrieve(transaction);
                            String rubExterne = "";
                            // Remise
                            rubExterne = cotiAf.getAssurance().getParametreAssuranceValeur(
                                    CodeSystem.GEN_PARAM_ASS_REMISE, coti.getDebutCotisation(), "");
                            // Recherche idRubrique (OSIRIS)
                            if (!JadeStringUtil.isEmpty(rubExterne)) {
                                CARubrique rubrique = new CARubrique();
                                rubrique.setSession(sessionPhenix);
                                rubrique.setIdExterne(rubExterne);
                                rubrique.setAlternateKey(APIRubrique.AK_IDEXTERNE);
                                rubrique.retrieve(transaction);
                                if (!rubrique.isNew()) {
                                    montant = CPToolBox.rechMontantFacture(sessionPhenix, transaction,
                                            compte.getIdCompteAnnexe(), rubrique.getIdRubrique(),
                                            decision.getAnneeDecision());
                                    if (!JadeStringUtil.isBlankOrZero(montant)) {
                                        montantSortie.setMontant(CPToolBox.multString(montant, "-1"));
                                        // Pour différencier entre l'id
                                        // assurance et l'id rubrique, on met en
                                        // négatif la rubrique
                                        // Ceci est utile pour la facturation
                                        // des sorties
                                        montantSortie
                                                .setAssurance(CPToolBox.multString(rubrique.getIdRubrique(), "-1"));
                                        montantSortie.setIdSortieMontant("");
                                        montantSortie.add(transaction);
                                    }
                                }
                            }
                            // Réduction
                            rubExterne = cotiAf.getAssurance().getParametreAssuranceValeur(
                                    CodeSystem.GEN_PARAM_ASS_REDUCTION, coti.getDebutCotisation(), "");
                            // Recherche idRubrique (OSIRIS)
                            if (!JadeStringUtil.isEmpty(rubExterne)) {
                                CARubrique rubrique = new CARubrique();
                                rubrique.setSession(sessionPhenix);
                                rubrique.setIdExterne(rubExterne);
                                rubrique.setAlternateKey(APIRubrique.AK_IDEXTERNE);
                                rubrique.retrieve();
                                if (!rubrique.isNew()) {
                                    montant = CPToolBox.rechMontantFacture(sessionPhenix, transaction,
                                            compte.getIdCompteAnnexe(), rubrique.getIdRubrique(),
                                            decision.getAnneeDecision());
                                    if (!JadeStringUtil.isBlankOrZero(montant)) {
                                        montantSortie.setMontant(CPToolBox.multString(montant, "-1"));
                                        // Pour différencier entre l'id
                                        // assurance et l'id rubrique, on met en
                                        // négatif la rubrique
                                        // Ceci est utile pour la facturation
                                        // des sorties
                                        montantSortie
                                                .setAssurance(CPToolBox.multString(rubrique.getIdRubrique(), "-1"));
                                        montantSortie.setIdSortieMontant("");
                                        montantSortie.add(transaction);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String _getDateSortie(boolean special) {
        try {
            if (affiliation == null) {
                affiliation = new AFAffiliation();
                affiliation.setSession(getSession());
                affiliation.setAffiliationId(getIdAffiliation());
                affiliation.setIdTiers(getIdTiers());
                affiliation.retrieve();
            }
            if (special) {
                return (affiliation.getDateFin());
            } else {
                return (affiliation.getDateDebut());
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String _getMotifSortie() {
        try {
            if (affiliation == null) {
                affiliation = new AFAffiliation();
                affiliation.setSession(getSession());
                affiliation.setAffiliationId(getIdAffiliation());
                affiliation.setIdTiers(getIdTiers());
                affiliation.retrieve();
            }
            return (affiliation.getMotifFin());
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return "";
        }
    }

    /**
     * Renvoie le nom de la table
     */

    @Override
    protected String _getTableName() {
        return "CPSORTP";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 11:12:44)
     * 
     * @return globaz.pyxis.api.ITITiers
     */
    protected ITITiers _getTiers() {
        return this._getTiers(null);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.02.2003 19:23:58)
     * 
     * @return globaz.pyxis.api.ITIPersonneAvs
     * @param idRole
     *            java.lang.String
     * @param id
     *            java.lang.String
     */
    protected ITITiers _getTiers(BTransaction transaction) {
        // Si cache vide
        if (_tiers == null) {
            try {
                CPApplication app = (CPApplication) getSession().getApplication();
                FAApplication appMusca = (FAApplication) app.getApplicationMusca();
                if (transaction == null) {
                    _tiers = appMusca.getTiersByRole(getSession(), TIRole.CS_AFFILIE, getNoAffilie(), "", new String[] {
                            "getIdTiers", "getDesignation1", "getDesignation2", "getAdresseAsString", "getLangue",
                            "getLocalite", "getNumAvsActuel", "findIdAdressePaiement" });
                } else {
                    _tiers = appMusca.getTiersByRole(transaction, TIRole.CS_AFFILIE, getNoAffilie(), "", new String[] {
                            "getIdTiers", "getDesignation1", "getDesignation2", "getAdresseAsString", "getLangue",
                            "getLocalite", "getNumAvsActuel", "findIdAdressePaiement" });
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
                e.printStackTrace();
                _addError(transaction, e.getMessage());
            }
        }
        return _tiers;
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idSortie = statement.dbReadNumeric("IZISOR");
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        idPassage = statement.dbReadNumeric("EBIPAS");
        idTiers = statement.dbReadNumeric("HTITIE");
        noAffilie = statement.dbReadString("MALNAF");
        montantCI = statement.dbReadNumeric("IZMMCI", 2);
        motif = statement.dbReadNumeric("IZTMOT");
        checked = statement.dbReadBoolean("IZBCHK");
        isRecap = statement.dbReadBoolean("IZBREC");
        isSpecial = statement.dbReadBoolean("IZBSPE");
        annee = statement.dbReadNumeric("IAANNE");
        idDecision = statement.dbReadNumeric("IAIDEC");
        referenceFacture = statement.dbReadString("IZRFAC");

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */

    @Override
    protected void _validate(BStatement statement) {
        try {
            if (!JadeStringUtil.isEmpty(getIdPassage())) {
                FAPassage passage = new FAPassage();
                passage.setSession(getSession());
                passage.setIdPassage(getIdPassage());
                passage.retrieve();
                if ((passage == null) || passage.isNew()) {
                    _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0039"));
                }
            } else {
                _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0040"));
            }

            if (!JadeStringUtil.isEmpty(getIdAffiliation())) {
                AFAffiliation affilie = new AFAffiliation();
                affilie.setSession(getSession());
                affilie.setId(getIdAffiliation());
                affilie.retrieve();
                if ((affilie == null) || affilie.isNew()) {
                    _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0041"));
                }
            } else {
                _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0042"));
            }

            if (JadeStringUtil.isEmpty(getAnnee())) {
                _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0043"));
            }
            if ((getSession() != null) && JadeStringUtil.isBlankOrZero(getReferenceFacture())) {
                setReferenceFacture(getSession().getUserId());
            }
        } catch (Exception e) {
            _addError(statement.getTransaction(), e.getMessage().toString());
        }
    }

    /**
	
	 */

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IZISOR", this._dbWriteNumeric(statement.getTransaction(), getIdSortie(), ""));
    }

    /**
     * write
     */

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField("IZISOR", this._dbWriteNumeric(statement.getTransaction(), getIdSortie(), "idSortie"));
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getIdAffiliation(), "idAffiliation"));
        statement.writeField("EBIPAS", this._dbWriteNumeric(statement.getTransaction(), getIdPassage(), "idPassage"));
        statement.writeField("HTITIE", this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField("MALNAF", this._dbWriteString(statement.getTransaction(), getNoAffilie(), "noAffilie"));
        statement.writeField("IZMMCI", this._dbWriteNumeric(statement.getTransaction(), getMontantCI(), "montantCI"));
        statement.writeField("IZTMOT", this._dbWriteNumeric(statement.getTransaction(), getMotif(), "motif"));
        statement.writeField("IZBCHK", this._dbWriteBoolean(statement.getTransaction(), getChecked(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "checked"));
        statement.writeField("IZBREC", this._dbWriteBoolean(statement.getTransaction(), getIsRecap(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "isRecap"));
        statement.writeField("IZBSPE", this._dbWriteBoolean(statement.getTransaction(), getIsSpecial(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "isSpecial"));
        statement.writeField("IAANNE", this._dbWriteNumeric(statement.getTransaction(), getAnnee(), "annee"));
        statement.writeField("IAIDEC", this._dbWriteNumeric(statement.getTransaction(), getIdDecision(), "idDecision"));
        statement.writeField("IZRFAC",
                this._dbWriteString(statement.getTransaction(), getReferenceFacture(), "referenceFacture"));
    }

    /**
     * Returns the annee.
     * 
     * @return String
     */
    public String getAnnee() {
        return annee;
    }

    public Boolean getChecked() {
        return checked;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 11:24:17)
     * 
     * @return java.lang.String
     */
    public String getDescriptionTiers() {
        return getNoAffilie() + " " + getNomTiers();
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * Returns the idDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdDecision() {
        return idDecision;
    }

    public String getIdPassage() {
        return idPassage;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */

    public String getIdSortie() {
        return idSortie;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsRecap() {
        return isRecap;
    }

    public Boolean getIsSpecial() {
        return isSpecial;
    }

    public String getMontantCI() {
        return montantCI;
    }

    public String getMotif() {
        return motif;
    }

    public String getNoAffilie() {
        return noAffilie;
    }

    public String getNomTiers() {
        return nomTiers;
    }

    public String getReferenceFacture() {
        return referenceFacture;
    }

    /**
     * Sets the annee.
     * 
     * @param annee
     *            The annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setChecked(Boolean newChecked) {
        checked = newChecked;
    }

    public void setIdAffiliation(String newIdAffiliation) {
        idAffiliation = newIdAffiliation;
    }

    /**
     * Sets the idDecision.
     * 
     * @param idDecision
     *            The idDecision to set
     */
    public void setIdDecision(java.lang.String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdPassage(String newIdPassage) {
        idPassage = newIdPassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */

    public void setIdSortie(String newIdSortie) {
        idSortie = newIdSortie;
    }

    public void setIdTiers(String newIdTiers) {
        idTiers = newIdTiers;
    }

    public void setIsRecap(Boolean newIsRecap) {
        isRecap = newIsRecap;
    }

    public void setIsSpecial(Boolean newIsSpecial) {
        isSpecial = newIsSpecial;
    }

    public void setMontantCI(String newMontantCI) {
        montantCI = newMontantCI;
    }

    public void setMotif(String newMotif) {
        motif = newMotif;
    }

    public void setNoAffilie(String newNoAffilie) {
        noAffilie = newNoAffilie;
    }

    /**
     * @param string
     */
    public void setNomTiers(String string) {
        nomTiers = string;
    }

    public void setReferenceFacture(String referenceFacture) {
        this.referenceFacture = referenceFacture;
    }

}