package globaz.phenix.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.divers.CPParametreCanton;
import globaz.phenix.db.divers.CPTableAFI;
import globaz.phenix.db.divers.CPTableAFIManager;
import globaz.phenix.db.divers.CPTableFortune;
import globaz.phenix.db.divers.CPTableFortuneManager;
import globaz.phenix.db.divers.CPTableIndependant;
import globaz.phenix.db.divers.CPTableIndependantManager;
import globaz.phenix.db.divers.CPTableNonActif;
import globaz.phenix.db.divers.CPTableNonActifManager;
import globaz.phenix.db.divers.CPTableRentier;
import globaz.phenix.db.divers.CPTableRentierManager;
import globaz.phenix.db.principale.CPCommentaireRemarqueType;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPCotisationManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.db.principale.CPDonneesCalculManager;
import globaz.phenix.db.principale.CPFortune;
import globaz.phenix.db.principale.CPLienCommentaireRemarqueType;
import globaz.phenix.db.principale.CPLienCommentaireRemarqueTypeManager;
import globaz.phenix.db.principale.CPLienTypeDecisionRemarqueType;
import globaz.phenix.db.principale.CPLienTypeDecisionRemarqueTypeManager;
import globaz.phenix.db.principale.CPRemarqueDecision;
import globaz.phenix.db.principale.CPRemarqueDecisionManager;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.CPUtil;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;

/**
 * Insérez la description du type ici. Date de création : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public final class CPProcessCalculCotisation extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static int DIN2132_2004 = 2004;

    private AFAffiliation affiliation = null;

    private int anneeAvs = 0;

    private boolean baremeDegressif = false;

    private boolean calculForReprise = false;
    private boolean casProrataRentier = false;
    private CACompteAnnexe compteAnnexe = null;
    private AFCotisation cotiAf = null;
    private float cotiAfiMinimum = 0;
    private float cotiIndMinimum = 0;
    private float cotiNacMinimum = 0;
    private String dateAvs = "";
    private globaz.phenix.db.principale.CPDecision decision = null;
    private java.lang.String descriptionTiers = "";
    private CPDonneesBase donneeBase = new CPDonneesBase();
    private java.lang.String idDecision = "";
    private int modeArrondiFad = 0;
    private String montantAnnuel = "0";
    private String montantMensuel = "0";
    private String montantSemestriel = "0";
    private String montantTrimestriel = "0";
    private Boolean revenuAf = null;
    private float revenuAFVD = 0;
    private float revenuNacMin = 0; // valeur par défaut au 1.01.2004
    private TITiersViewBean tiers = new TITiersViewBean();

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessCalculCotisation() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessCalculCotisation(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Calcul des montants de cotisation Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        // Vérifier la décision
        if (JadeStringUtil.isIntegerEmpty(getIdDecision())) {
            getMemoryLog().logMessage("N° de décision vide ", FWMessage.FATAL, getIdDecision());
            return false;
        }
        // Sous controle d'exceptions
        try {
            if (getModeArrondiFad() == 0) {
                setModeArrondiFad(CPToolBox.getModeArrondiFad(getTransaction()));
            }
            CPDecision decis = new CPDecision();
            // Rechercher les données de la décision
            decis.setSession(getSession());
            decis.setIdDecision(getIdDecision());
            decis.retrieve(getTransaction());
            if (decis.hasErrors()) {
                getMemoryLog().logStringBuffer(getTransaction().getErrors(), decis.getClass().getName());
                getMemoryLog().logMessage(getSession().getLabel("CP_JOB_0156"), FWMessage.FATAL,
                        this.getClass().getName());
                return false;
            } else {
                // Ne pas traiter si la décision à l'état valider, facturer ou
                // reprise
                // Aller rechercher le dernier état
                if ((!CPDecision.CS_FACTURATION.equalsIgnoreCase(decis.getDernierEtat()))
                        && (!CPDecision.CS_VALIDATION.equalsIgnoreCase(decis.getDernierEtat()))
                        && (!CPDecision.CS_PB_COMPTABILISATION.equalsIgnoreCase(decis.getDernierEtat()))
                        && (!CPDecision.CS_REPRISE.equalsIgnoreCase(decis.getDernierEtat()))) {
                    // Traitement
                    setDecision(decis);
                    // Récupérer les données encodées
                    getDonneeBase().setSession(getSession());
                    getDonneeBase().setIdDecision(getIdDecision());
                    getDonneeBase().retrieve();
                    if (getDonneeBase().hasErrors()) {
                        this._addError(getTransaction(), decision.getMessage());
                        return false;
                    }
                    // Recherche données du tiers
                    tiers.setSession(getSession());
                    tiers.setIdTiers(decision.getIdTiers());
                    tiers.retrieve(getTransaction());
                    if (tiers.hasErrors()) {
                        getMemoryLog().logMessage(getSession().getLabel("CP_JOB_0157") + " ", FWMessage.FATAL,
                                decision.getIdTiers());
                        return false;
                    }
                    // Partager le log
                    // decision.setMemoryLog(getMemoryLog());
                    // Sortie si abort
                    if (isAborted()) {
                        return false;
                    }
                    // Suppression des anciennes valeurs
                    try {
                        suppressionAnciennesDonnees();
                    } catch (Exception e) {
                        getMemoryLog().logStringBuffer(getTransaction().getErrors(), decision.getIdDecision());
                        getMemoryLog().logMessage(getSession().getLabel("CP_JOB_0158"), FWMessage.AVERTISSEMENT,
                                decision.getIdDecision());
                        return false;
                    }
                    // Recherche de l'idCoti dans naos
                    AFAffiliation aff = new AFAffiliation();
                    aff.setSession(getSession());
                    cotiAf = aff._cotisation(getTransaction(), decision.getIdAffiliation(),
                            CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AVS_AI,
                            decision.getDebutDecision(), decision.getFinDecision(), 1);
                    // Si il ny a pas d'assurance AVS, se baser sur
                    // l'affiliation
                    // Ex: Cas de la FER qui n'a que l'AF (médecin vaudois qui
                    // paye sa cotisation à la caisse des médecins)
                    aff.setAffiliationId(decision.getIdAffiliation());
                    aff.retrieve();
                    setAffiliation(aff);
                    // sauvegarde du compte annexe
                    setCompteAnnexe(CPToolBox.getCompteAnnexe(getSession(), affiliation.getAffilieNumero()));
                    setDescriptionTiers(affiliation.getAffilieNumero() + " " + tiers.getNom());
                    // Calcul selon le type
                    if (decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_IMPUTATION)) {
                        calculBonifMiseEnCompte(this);
                    } else if (decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_REMISE)) {
                        calculRemise(this);
                    } else if (decision.isNonActif()) {
                        calculNonActif(this);
                    } else if (decision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_INDEPENDANT)
                            || decision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_AGRICULTEUR)
                            || decision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_RENTIER)
                            || decision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_TSE)) {
                        /*
                         * Déterminsation si calcul AF pour cas spécial (revenuAutre = revenu af)
                         */
                        if (isRevenuAf() == null) {
                            // Recherche si rrevenuAutre = revenu AF (ex cas
                            // spéciaux AF pour GE)
                            boolean revAf = ((CPApplication) getSession().getApplication()).isRevenuAf();
                            if (revAf) {
                                setRevenuAf(Boolean.TRUE);
                            } else {
                                setRevenuAf(Boolean.FALSE);
                            }
                        }
                        if (isRevenuAf().equals(Boolean.TRUE) && !JadeStringUtil.isEmpty(donneeBase.getRevenuAutre1())) {
                            // Calcul en ignorant revenu autre 1 et en ne créant
                            // pas la coti AF
                            calculIndependant(this, 1);
                            // Calcul en tenant compte uniquement de revenu
                            // autre 1 et en créant uniquement la coti AF
                            calculIndependant(this, 2);
                        } else {
                            // calcul normal
                            calculIndependant(this, 0);
                        }
                    } else {
                        this._addError(getTransaction(),
                                getSession().getLabel("CP_MSG_0160") + " " + decision.getAnneeDecision() + " "
                                        + getSession().getLabel("CP_MSG_0135A") + " " + getDescriptionTiers());
                    }
                    try {
                        createRemarqueAutomatique(getTransaction(), decision);
                    } catch (Exception e) {
                        this._addError(getTransaction(), getSession().getLabel("CP_MSG_0113"));
                        this._addError(getTransaction(), e.getMessage());
                    } // Mettre l'état de la décision à calcul
                    decision.setDernierEtat(CPDecision.CS_CALCUL);
                    // Mise à jour de la décision
                    decision.setAffiliation(getAffiliation());
                    decision.update(getTransaction());
                } else {
                    this._addError(getTransaction(),
                            getSession().getLabel("CP_MSG_0159") + " " + decision.getAnneeDecision()
                                    + getSession().getLabel("CP_MSG_0135A") + " " + getDescriptionTiers());
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        } // Fin de la procédure
        return !isOnError();
    }

    /**
     * Ajout des montants de cotisation Date de création : (25.02.2002 12:58:23)
     * 
     * @param process
     *            BProcess le processus d'exécution
     * @param cotisationMensuelle
     *            float
     * @param idCotisationAssurance
     *            String
     * @param taux
     *            string
     * @param prorata
     *            boolean
     * @param cotiAnnuelle
     *            String
     * @param cotiMinimum
     *            Boolean
     **/
    public void addCotisation(BProcess process, float cotiMensuelle, AFCotisation cotiAf, String taux,
            String cotiAnnuelle, Boolean cotiMinimum) {
        /*
         * l'affilié doit payer le montant annuel sur la période, c'est à dire qu'il doit payer 12 mois même si la
         * période est de janvier à avril sinon dans un tel cas la facturation périodique s'arréterait à avril et il
         * manquerait 8 mois à payer.
         */
        /*
         * Si cotiAnnuelle renseignée => mettre directement ce montant comme cotisation annuelle Par ex: utile pour la
         * cotisation minimum car non divisible par 12...
         */
        try {
            CPCotisation cotisation = new CPCotisation();
            cotisation.setSession(process.getSession());
            cotisation.setIdDecision(decision.getIdDecision());
            cotisation.setIdCotiAffiliation(cotiAf.getCotisationId());
            cotisation.setTaux(taux);
            cotisation.setCotisationMinimum(cotiMinimum);
            cotisation.setPeriodicite(cotiAf.getPeriodicite());
            cotisation.setMontantMensuel(Float.toString(cotiMensuelle));
            // Mettre la date début de l'assurance si elle est supérieure à
            // celle de la décision
            if (BSessionUtil.compareDateFirstGreater(getSession(), cotiAf.getDateDebut(), decision.getDebutDecision())
                    || (BSessionUtil.compareDateFirstLower(getSession(), cotiAf.getDateFin(),
                            decision.getDebutDecision()) && !JadeStringUtil.isEmpty(cotiAf.getDateFin()))) {
                cotisation.setDebutCotisation(cotiAf.getDateDebut());
            } else {
                cotisation.setDebutCotisation(decision.getDebutDecision());
            }
            // Mettre la date fin de l'assurance si elle est inférieure à celle
            // de la décision
            if (!JAUtil.isDateEmpty(cotiAf.getDateFin())) {
                if (BSessionUtil.compareDateFirstLower(getSession(), cotiAf.getDateFin(), decision.getFinDecision())) {
                    cotisation.setFinCotisation(cotiAf.getDateFin());
                } else {
                    cotisation.setFinCotisation(decision.getFinDecision());
                }
            } else {
                cotisation.setFinCotisation(decision.getFinDecision());
            }
            // Détermination de la durée de cotisation
            int dureeCotisation = JACalendar.getMonth(cotisation.getFinCotisation())
                    - JACalendar.getMonth(cotisation.getDebutCotisation()) + 1;
            // Calcul des montants périodiques à partir du montant mensuel
            FWCurrency coti = null;
            FWCurrency varTemp = null;
            coti = new FWCurrency("" + cotiMensuelle);
            if (CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equalsIgnoreCase(cotiAf.getAssurance().getTypeAssurance())) {
                setMontantMensuel(coti.toString());
            }
            // Montant trimestriel
            if (dureeCotisation < 4) {
                if (JadeStringUtil.isBlank(cotiAnnuelle)) {
                    varTemp = new FWCurrency(coti.doubleValue() * dureeCotisation);
                } else {
                    varTemp = new FWCurrency(cotiAnnuelle);
                }
            } else {
                varTemp = new FWCurrency(coti.doubleValue() * 3);
            }
            cotisation.setMontantTrimestriel(varTemp.toString());
            if (CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equalsIgnoreCase(cotiAf.getAssurance().getTypeAssurance())) {
                setMontantTrimestriel(varTemp.toString());
            }
            // Montant semestriel
            if (dureeCotisation < 7) {
                if (JadeStringUtil.isBlank(cotiAnnuelle)) {
                    varTemp = new FWCurrency(coti.doubleValue() * dureeCotisation);
                } else {
                    varTemp = new FWCurrency(cotiAnnuelle);
                }
            } else {
                varTemp = new FWCurrency(coti.doubleValue() * 6);
            }
            if (CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equalsIgnoreCase(cotiAf.getAssurance().getTypeAssurance())) {
                setMontantSemestriel(varTemp.toString());
            }
            cotisation.setMontantSemestriel(String.valueOf(varTemp));
            // Montant annuel
            if (!JadeStringUtil.isBlank(cotiAnnuelle)) {
                varTemp = new FWCurrency(cotiAnnuelle);
            } else {
                varTemp = new FWCurrency(coti.doubleValue() * dureeCotisation);
            }
            if (CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equalsIgnoreCase(cotiAf.getAssurance().getTypeAssurance())) {
                setMontantAnnuel(varTemp.toString());
            }
            cotisation.setMontantAnnuel(varTemp.toString());
            cotisation.setGenreCotisation(cotiAf.getAssurance().getTypeAssurance());
            double valCoti = Double.parseDouble(JANumberFormatter.deQuote(cotisation.getMontantAnnuel()));
            String valeurMin = cotiAf.getAssurance().getParametreAssuranceValeur(
                    CodeSystem.GEN_PARAM_ASS_FRANCHISE_MENSUEL, cotisation.getDebutCotisation(), "");
            if ((null == valeurMin) || JadeStringUtil.isBlankOrZero(valeurMin)) {
                valeurMin = cotiAf.getAssurance().getParametreAssuranceValeur(CodeSystem.GEN_PARAM_ASS_FRANCHISE,
                        cotisation.getDebutCotisation(), "");
            } else {
                valCoti = Double.parseDouble(JANumberFormatter.deQuote(cotisation.getMontantMensuel()));
            }
            if ((null == valeurMin) || JadeStringUtil.isBlankOrZero(valeurMin)) {
                valeurMin = "0";
            }
            double valMin = Double.parseDouble(JANumberFormatter.deQuote(valeurMin));
            // PO 5854 - Ré initialisation des valeurs en cas de recalcul de la décision
            if (CPDecision.CS_FRANCHISE.equalsIgnoreCase(decision.getSpecification())
                    && CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equalsIgnoreCase(cotiAf.getAssurance().getTypeAssurance())) {
                decision.setImpression(Boolean.TRUE);
                decision.setFacturation(Boolean.TRUE);
                decision.setSpecification("");
            }

            if ((valCoti < valMin) && !CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())
                    && !CPDecision.CS_IMPUTATION.equalsIgnoreCase(decision.getTypeDecision())
                    && !CPDecision.CS_REDUCTION.equalsIgnoreCase(decision.getTypeDecision())) {
                if (CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equalsIgnoreCase(cotiAf.getAssurance().getTypeAssurance())) {
                    decision.setSpecification(CPDecision.CS_FRANCHISE);
                    if (!CPDecision.CS_ACOMPTE.equalsIgnoreCase(decision.getTypeDecision())) {
                        decision.setImpression(Boolean.FALSE);
                        decision.setFacturation(Boolean.FALSE);
                    }
                }
                cotisation.setAForceAZero(Boolean.TRUE);
            }
            // PO 9407: Remboursement des cas ou le compteur avs est à 0 mais pas pour une autre assurance
            if (CPDecision.CS_FRANCHISE.equalsIgnoreCase(decision.getSpecification())) {
                String montantFac = "";
                if ((compteAnnexe != null) && !compteAnnexe.isNew()) {
                    montantFac = CPToolBox.rechMontantFacture(getSession(), getTransaction(), compteAnnexe
                            .getIdCompteAnnexe(), cotiAf.getAssurance().getRubriqueId(), getDecision()
                            .getAnneeDecision());
                }
                if (!JadeStringUtil.isBlankOrZero(montantFac)) {
                    decision.setImpression(Boolean.TRUE);
                    decision.setFacturation(Boolean.TRUE);
                }
            }

            // Si assurance = frais et franchise => ne pas facturé de frais
            if (CodeSystem.TYPE_ASS_FRAIS_ADMIN.equalsIgnoreCase(cotiAf.getAssurance().getTypeAssurance())) {
                if (decision.getSpecification().equalsIgnoreCase(CPDecision.CS_FRANCHISE)) {
                    cotisation.setAForceAZero(Boolean.TRUE);
                } else {
                    cotisation.setAForceAZero(Boolean.FALSE);
                }
            }
            if (cotiAf != null) {
                cotisation.setDecision(decision);
                cotisation.add(process.getTransaction());
            }
        } catch (Exception e) {
            this._addError(process.getTransaction(), e.getMessage() + " " + getDescriptionTiers());
        }
    }

    /**
     * Création d'une ligne de cotisation pour pouvoir rembourser le montant déjà facturé
     * 
     * @author hna
     * @param process
     * @param cotiAf
     * @throws NumberFormatException
     */
    protected void addCotisationPourCotisationFacturee(BProcess process, AFCotisation cotiAf)
            throws NumberFormatException {
        if (cotiAf != null) {
            // recherche du montant déjà facturé
            String montantCompteur = CPToolBox.rechMontantFacture(getSession(), getTransaction(), getCompteAnnexe()
                    .getIdCompteAnnexe(), cotiAf.getAssurance().getRubriqueId(), decision.getAnneeDecision());
            // Recherche du taux
            if (!JadeStringUtil.isBlankOrZero(montantCompteur)) {
                // Ajout de la ligne de cotisation
                addCotisation(process, 0, cotiAf, Float.toString(findTaux(cotiAf)), "", Boolean.FALSE);
            }
        }
    }

    /**
     * Calcul des cotisations AF Date de création : (25.02.2002 12:58:23)
     * 
     * @param process
     *            BProcess le processus d'exécution
     * @param revenu
     * @param exempte
     * @param saveDureeDecision
     * @param cotiMinimumAvs
     */
    public void calculAF(BProcess process, float revenuCi, boolean exempte, int saveDureeDecision,
            Boolean cotiMinimumAvs, String typeAssuranceAF) {
        // Sous contrôle d'exception
        float taux = 0;
        Boolean cotiMinimum = Boolean.FALSE;
        float cotiMensuelle = 0;
        try {
            if (!process.getTransaction().hasErrors()) {
                AFAffiliation aff = new AFAffiliation();
                aff.setSession(process.getSession());
                AFCotisation cotiAf = aff._cotisation(process.getTransaction(), decision.getIdAffiliation(),
                        CodeSystem.GENRE_ASS_PERSONNEL, typeAssuranceAF, decision.getDebutDecision(),
                        decision.getFinDecision(), 1);
                if ((cotiAf != null) && !cotiAf.getDateDebut().equalsIgnoreCase(cotiAf.getDateFin())) {
                    // Recherche du mode de calcul de la cotisation AF
                    // Pour simplifier la saisie, au niveau des paramètres : indépendant = ind, tse, rentier,
                    // agriculteur
                    String modeCalculAF = findMethodeCaculAf(cotiAf);
                    // La cotisation AF Soleure se base sur la cotisation avs et
                    // non sur le revenu, il faut que le ontant de la cotisation
                    // AVS soit plus grand que la cotisation minimum
                    boolean afSelonCoti = false;
                    if (CPParametreCanton.CS_AF_SELON_COTI.equalsIgnoreCase(modeCalculAF)) {
                        afSelonCoti = true;
                    }
                    if (afSelonCoti) {
                        if (Boolean.TRUE.equals(cotiMinimumAvs)) {
                            exempte = true;
                        } else {
                            revenuCi = Float.parseFloat(JANumberFormatter.deQuote(getMontantAnnuel()));
                        }
                    } else if (CPParametreCanton.CS_AF_REVENU1.equalsIgnoreCase(modeCalculAF)) {
                        revenuCi = JANumberFormatter.round(revenuAFVD, 100, 0, JANumberFormatter.INF);
                    } else if (CPParametreCanton.CS_AF_REVENU2.equalsIgnoreCase(modeCalculAF)) {
                        // Si revenu2 est renseigné on prend revenu2 sinon c'est le revenu déterminant (revenu 1)
                        if (!JadeStringUtil.isEmpty(donneeBase.getRevenuAutre1())) {
                            revenuCi = Float.parseFloat(JANumberFormatter.deQuote(donneeBase.getRevenuAutre1()));
                        }
                    }
                    if (!exempte) {
                        int dureeCotisation = calculDureeCotisation(cotiAf, decision, afSelonCoti);
                        if ((decision.getProrata().equalsIgnoreCase("2") || decision.getProrata().equalsIgnoreCase("3"))
                                && !afSelonCoti) {
                            if (!casProrataRentier || (saveDureeDecision == 0)) {
                                if (!JadeStringUtil.isBlankOrZero(decision.getNombreMoisTotalDecision())) {
                                    dureeCotisation = Integer.parseInt(decision.getNombreMoisTotalDecision());
                                } else {
                                    dureeCotisation = 12;
                                }
                            }
                        }
                        // Prendre la date la plus récente entre l'assurance et
                        // la décision si l'année de coti = l'année de décision
                        // sinon prendre la date de décision
                        String dateRef = decision.getDebutDecision();
                        if ((Integer.parseInt(decision.getAnneeDecision()) == JACalendar.getYear(cotiAf.getDateDebut()))
                                && BSessionUtil.compareDateFirstGreater(getSession(), cotiAf.getDateDebut(),
                                        decision.getDebutDecision())) {
                            dateRef = cotiAf.getDateDebut();
                        }
                        // Recherche du montant de revenu maximum
                        String varString = cotiAf.getAssurance().getParametreAssuranceValeur(
                                CodeSystem.GEN_PARAM_ASS_REVENU_MAX, dateRef, "");
                        if (null == varString) {
                            if (typeAssuranceAF.equalsIgnoreCase(CodeSystem.TYPE_ASS_COTISATION_AF)) {
                                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0174") + " "
                                        + cotiAf.getAssurance().getAssuranceLibelle(getSession().getIdLangueISO()));
                            } else {
                                varString = "999999999999";
                            }
                        }
                        if (JadeStringUtil.isIntegerEmpty(varString)) {
                            varString = "0";
                        }
                        double valRevMax = Double.parseDouble(JANumberFormatter.deQuote(varString));
                        if (saveDureeDecision != 0) {
                            valRevMax = (valRevMax / 12) * saveDureeDecision;
                        }

                        // K160601_002 - calcul erroné des cotisations AF différentielles
                        valRevMax = determineRevenuMaxSuivantNombreDeMois(varString, valRevMax,
                                decision.getNombreMoisTotalDecision());

                        double valRevenuCi = revenuCi;
                        if (valRevenuCi > valRevMax) {
                            revenuCi = (float) valRevMax;
                        }
                        try {
                            taux = Float.parseFloat(JANumberFormatter.deQuote(cotiAf.getTaux(dateRef, "0")));
                        } catch (Exception e) {
                            taux = 0; // BTC bug affiliation!!! 15.12.2003
                        }
                        float cotiAnnuelleBrut = (revenuCi * taux) / 100;
                        // Recherche de la cotisation mimimum AF
                        if (!baremeDegressif) {
                            if (!CPDecision.CS_RENTIER.equalsIgnoreCase(decision.getGenreAffilie())
                                    || casProrataRentier) {
                                varString = cotiAf.getAssurance().getParametreAssuranceValeur(
                                        CodeSystem.GEN_PARAM_ASS_COTISATION_MIN, dateRef, "");
                                if (null == varString) {
                                    if (typeAssuranceAF.equalsIgnoreCase(CodeSystem.TYPE_ASS_COTISATION_AF)) {
                                        this._addError(
                                                getTransaction(),
                                                getSession().getLabel("CP_MSG_0175")
                                                        + " "
                                                        + cotiAf.getAssurance().getAssuranceLibelle(
                                                                getSession().getIdLangueISO()));
                                    } else {
                                        varString = "0";
                                    }
                                }
                                if (JadeStringUtil.isIntegerEmpty(varString)) {
                                    varString = "0";
                                }
                                double valCotMin = Double.parseDouble(JANumberFormatter.deQuote(varString));
                                if (casProrataRentier && (saveDureeDecision != 0)) {
                                    valCotMin = (valCotMin / 12) * saveDureeDecision;
                                }
                                double valCotiAnnuelleBrut = cotiAnnuelleBrut;
                                // PO 3548: Ne pas prendre le minimum en cas de DIN 1181
                                if ((valCotMin > valCotiAnnuelleBrut)
                                        && Boolean.FALSE.equals(decision.getCotiMinimumPayeEnSalarie())) {
                                    cotiAnnuelleBrut = (float) valCotMin;
                                    cotiMinimum = Boolean.TRUE;
                                    taux = 0;
                                }
                            }
                        }
                        // Calcul de cotisation mensuelle - arrondi au 5 cts
                        cotiMensuelle = cotiAnnuelleBrut / dureeCotisation;
                        cotiMensuelle = JANumberFormatter.round(cotiMensuelle, 0.05, 2, JANumberFormatter.NEAR);
                    }
                    // Création des cotisations dans CPCOTIP
                    addCotisation(process, cotiMensuelle, cotiAf, Float.toString(taux), "", cotiMinimum);
                } else {
                    // PO 2036 et 9407- Recherche et extourne s'il y a un compteur pour la rubrique pour l'année de
                    // décision
                    creationCotisationPourExtrounerCompteurExistant(process, aff, cotiAf, typeAssuranceAF);
                }
            }
        } catch (Exception e) {
            this._addError(process.getTransaction(), e.getMessage() + " " + getDescriptionTiers());
            return;
        }
    }

    /**
     * Permet de déterminer le revenu max suivant le nombre de mois si le nombre de mois est renseigné.
     * 
     * @param revenuMaximumParametre Le revenu maximum paramétré dans l'application
     * @param nombreDeMois Le nombre de mois de la décision
     * @return Le revenu calculé passé en paramètre si le nombre de mois est vide. Le nouveau revenu calculé sinon.
     */
    double determineRevenuMaxSuivantNombreDeMois(String revenuMaximumParametre, double revenuCalcule,
            String nombreDeMois) {

        if (!JadeStringUtil.isBlankOrZero(nombreDeMois)) {
            revenuCalcule = Double.parseDouble(JANumberFormatter.deQuote(revenuMaximumParametre));
            int nbMois = Integer.parseInt(nombreDeMois);
            revenuCalcule = (revenuCalcule / 12) * nbMois;
        }

        return revenuCalcule;
    }

    /**
     * Calcul des cotisations AFI Date de création : (08.12.2006 12:58:23)
     * 
     * @param process
     *            BProcess le processus d'exécution
     * @param revenu
     *            revenu de base pour le calcul
     */
    public void calculAFI(BProcess process, float revenuCi, String revenuDeterminant, float revenuDet, float revenuMin,
            float revenuMax, boolean prorataCi, boolean casProrataRentier, boolean perte, boolean exerciceSur2Annee,
            boolean exempte) {
        float cotiAnnuelleBrut = 0;
        float cotiMensuelle = 0;
        float tauxCalcul = 0;
        Boolean cotiMinimum = Boolean.FALSE;
        CPTableAFI tAFI = new CPTableAFI();
        int moisDebutDecision = 0;
        int moisFinDecision = 0;
        String cotiAnnuelle = "";
        // Sous contrôle d'exception
        try {
            if (!process.getTransaction().hasErrors()) {
                // Détermination de l'âge de l'affilié
                int age = 0;
                try {
                    age = Integer.parseInt(decision.getAnneeDecision())
                            - JACalendar.getYear(getTiers().getDateNaissance());
                } catch (Exception e) {
                    age = 0;
                }
                // l'AFI est valable dès la 21ène année
                if (age >= 21) {
                    AFAffiliation aff = new AFAffiliation();
                    aff.setSession(process.getSession());
                    AFCotisation cotiAfi = aff._cotisation(process.getTransaction(), decision.getIdAffiliation(),
                            CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_AFI, decision.getDebutDecision(),
                            decision.getFinDecision(), 1);
                    if (JadeStringUtil.isBlankOrZero(getMontantMensuel())) {
                        exempte = true;
                    }
                    if ((cotiAfi != null) && !cotiAfi.getDateDebut().equalsIgnoreCase(cotiAfi.getDateFin())) {
                        if (!exempte) {
                            // Exercice sur 2 année doit être pris en compte si
                            // nouveau calcul
                            // et année de début de l'exercice différente de
                            // celle de fin de l'exercice
                            int anneeFinExercice = JACalendar.getYear(getDonneeBase().getFinExercice1());
                            // Forcer la cotisation minimum pour certains cas métiers
                            if (!baremeDegressif) {
                                if (!CPDecision.CS_RENTIER.equalsIgnoreCase(decision.getGenreAffilie())
                                        || casProrataRentier) {
                                    if (((revenuMin > revenuDet) && !exerciceSur2Annee)
                                            || ((revenuMin > revenuDet) && perte)
                                            || ((revenuMin > revenuDet) && exerciceSur2Annee && decision
                                                    .getAnneeDecision().equalsIgnoreCase(
                                                            Integer.toString(anneeFinExercice)))
                                            || ((revenuMin > revenuDet)
                                                    && decision.getPremiereAssurance().equals(new Boolean(true)) && decision
                                                    .getTaxation().equalsIgnoreCase("N"))) {
                                        cotiAnnuelleBrut = getCotiAfiMinimum();
                                        cotiMinimum = Boolean.TRUE;
                                        cotiAnnuelle = Float.toString(cotiAnnuelleBrut);
                                    }
                                }
                            }
                            // sinon cotisation AFI selon tabelle
                            if (!cotiMinimum) {
                                String anneeRevenu = "";
                                if (decision.getTaxation().equalsIgnoreCase("A")) {
                                    anneeRevenu = decision.getAnneeDecision();
                                } else {
                                    anneeRevenu = Integer.toString(anneeFinExercice);
                                }
                                // Recherche taux dans la table des indépendant
                                // qui permet de déterminer la cotisation pour
                                // ce calcul
                                tAFI = returnTableAfi(process, anneeRevenu, revenuDeterminant);
                                if (tAFI == null) {
                                    this._addError(getTransaction(), getSession().getLabel("CP_MSG_0110") + " "
                                            + getDescriptionTiers());
                                    return;
                                }
                                // Calcul de la cotisation annuelle
                                tauxCalcul = Float.parseFloat(JANumberFormatter.deQuote(tAFI.getTaux()));
                                cotiAnnuelleBrut = (revenuCi * tauxCalcul) / 100;
                                cotiAnnuelleBrut = JANumberFormatter.round(cotiAnnuelleBrut, 0.05, 2,
                                        JANumberFormatter.NEAR);
                            }
                            // Détermination si revenu CI et cotisation sont au
                            // prorata
                            moisDebutDecision = JACalendar.getMonth(decision.getDebutDecision());
                            moisFinDecision = JACalendar.getMonth(decision.getFinDecision());
                            // Si rentier pendant l'année de décision et
                            // cotisation minimum et calcul praenumerando
                            // => période de décision dure jusqu'à lâge AVS
                            if (casProrataRentier) {
                                if (cotiMinimum) {
                                    cotiAnnuelleBrut = Float.parseFloat(CPToolBox.getMontantProRata(
                                            decision.getDebutDecision(), dateAvs, cotiAnnuelleBrut));
                                }
                                // this.decision.setFinDecision(this.dateAvs);
                            }
                            // Calcul de cotisation mensuelle
                            // Pour les cas ou l'affilié doit payer la
                            // cotisation annuelle, il faut ramener
                            // la coti mensuelle par rapport à la durée de la
                            // décision (EX: s'il doit payer 1200/an mais que la
                            // décision dure 3 mois => la coti mensuelle =
                            // 1200/3 = 400 Fr.
                            int dureeDecision = (moisFinDecision - moisDebutDecision) + 1;
                            // Coti. annuelle théorique
                            FWCurrency varCurrency = new FWCurrency();
                            varCurrency = new FWCurrency(cotiAnnuelleBrut / 12);
                            cotiMensuelle = varCurrency.floatValue();
                            cotiMensuelle = JANumberFormatter.round(cotiMensuelle, 0.05, 2, JANumberFormatter.NEAR);
                            // Recalcul de la coti. annuelle sur la base de la coti. mensuelle arrondi
                            varCurrency = new FWCurrency(cotiMensuelle * 12);
                            String cotiAnnuelleTheorique = varCurrency.toString();
                            // Calcul de la coti. annuelle selon le cas
                            if (prorataCi) {
                                varCurrency = new FWCurrency(cotiAnnuelleBrut / 12);
                            } else {
                                varCurrency = new FWCurrency(cotiAnnuelleBrut / dureeDecision);
                            }
                            cotiMensuelle = varCurrency.floatValue();
                            // Au dessus du revenu max et pour la cotisation
                            // minimum l'arrondi se fait au 10 centimes les plus
                            // proches sinon infèrieure
                            cotiMensuelle = JANumberFormatter.round(cotiMensuelle, 0.05, 2, JANumberFormatter.NEAR);
                            // Recalcul de la coti. annuelle sur la base de la
                            // coti. mensuelle arrondi
                            varCurrency = new FWCurrency(cotiMensuelle * dureeDecision);
                            // Modif pour afficher le montant de la tabelle
                            // suite aux tests pour démo inforom
                            if (prorataCi) {
                                cotiAnnuelle = varCurrency.toString();
                            } else {
                                cotiAnnuelle = cotiAnnuelleTheorique;
                            }
                            // if((decision.getTaxation().equalsIgnoreCase("N")&&
                            // (moisDebutDecision!=01||moisFinDecision!=12))||prorataCi
                            /*
                             * if ((prorataCi && (cotiAnnuelleBrut != this.getCotiAfiMinimum()) && this.decision
                             * .getTaxation().equalsIgnoreCase("N")) || ((revenuMin > revenuDet) && exerciceSur2Annee &&
                             * this.decision .getAnneeDecision().equalsIgnoreCase(Integer.toString(anneeFinExercice)))
                             * || (!prorataCi && (cotiAnnuelleBrut == this.getCotiAfiMinimum()))) {
                             */
                            if (((revenuMin > revenuDet) && exerciceSur2Annee && decision.getAnneeDecision()
                                    .equalsIgnoreCase(Integer.toString(anneeFinExercice)))
                                    || (!prorataCi && (cotiAnnuelleBrut == getCotiAfiMinimum()))) {
                                if (Integer.parseInt(decision.getNombreMoisTotalDecision()) == 0) {
                                    cotiAnnuelle = Float.toString(cotiAnnuelleBrut);
                                } else {
                                    cotiAnnuelle = "";
                                }
                            } else if ((moisDebutDecision == 01) && (moisFinDecision == 12)) {
                                cotiAnnuelle = "";
                            }
                        }
                        // Création des cotisation d'indépendant
                        addCotisation(process, cotiMensuelle, cotiAfi, tAFI.getTaux(), cotiAnnuelle, cotiMinimum);
                    } else {
                        // PO 2036
                        // Recherche s'il y a un compteur pour la rubrique AFI
                        // <> 0 pour l'année de décision
                        if (getCompteAnnexe() != null) {
                            // Recherche s'il y a eu un cotisation AF
                            cotiAfi = aff._cotisation(process.getTransaction(), decision.getIdAffiliation(),
                                    CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_AFI,
                                    affiliation.getDateDebut(), decision.getFinDecision(), 4);
                            if (cotiAfi != null) {
                                String montantCompteur = CPToolBox.rechMontantFacture(getSession(), getTransaction(),
                                        getCompteAnnexe().getIdCompteAnnexe(), cotiAfi.getAssurance().getRubriqueId(),
                                        decision.getAnneeDecision());
                                if (!JadeStringUtil.isBlankOrZero(montantCompteur)) {
                                    addCotisation(process, 0, cotiAfi, "0", "", Boolean.FALSE);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            this._addError(process.getTransaction(), e.getMessage() + " " + getDescriptionTiers());
            return;
        }
    }

    protected float calculAndSaveFortuneArrondie(float fortuneDet, CPDonneesCalcul donCalcul) throws Exception {
        int entier = (int) fortuneDet / 50000;
        fortuneDet = entier * 50000;
        donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_REV_50000INF, fortuneDet);
        return fortuneDet;
    }

    protected float calculAndSaveFortuneTotale(float fortuneTotal, CPDonneesCalcul donCalcul, float revenu20)
            throws Exception {
        float fortuneDet;
        fortuneDet = fortuneTotal + revenu20;
        donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_FORTUNE_TOTALE, fortuneDet);
        return fortuneDet;
    }

    protected float calculAndSaveFranchisePourRentier(BProcess process, int codeRevenuAf, float franchise,
            CPDonneesCalcul donCalcul, boolean exerciceSur2Annee) throws Exception {
        if (exerciceSur2Annee || CPDecision.CS_RENTIER.equalsIgnoreCase(decision.getGenreAffilie())) {
            if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDonneeBase().getFinExercice1(), dateAvs)) {
                // Détermination du montant de la franchise pour rentier
                franchise = getFranchise(process, exerciceSur2Annee);
                // sauvegarde du montant de la franchise
                if ((codeRevenuAf != 2) && (franchise != 0)) {
                    donCalcul
                            ._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_FRANCHISE, franchise);
                }
            }
        }
        return franchise;
    }

    protected float calculAndSaveRevenu20(float revenuBrut, CPDonneesCalcul donCalcul) throws Exception {
        float revenu20 = revenuBrut * 20;
        donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_REV_20, revenu20);
        return revenu20;
    }

    protected float calculAndSaveRevenuCI(CPDonneesCalcul donCalcul, int dureeDecision, CPTableNonActif tNac,
            boolean prorata) throws NumberFormatException, Exception {
        float revenuCi = Float.parseFloat(JANumberFormatter.deQuote(tNac.getRevenuCi()));
        // Sauvegarde du revenu CI - calcul le ci au prorata selon les
        // cas
        // Ramener le CI au prorata de la période pour les cas
        // postnumerando
        revenuCi = casProrataNonActif(dureeDecision, prorata, revenuCi);
        // AVS payée à un autre caisse -> ne pas stocker le revenu CI
        if (cotiAf != null) {
            // PO 6758 - if (CPDecision.CS_FRANCHISE.equalsIgnoreCase(this.decision.getSpecification())
            // && (this.decision.getFacturation().equals(Boolean.FALSE) || CPDecision.CS_ACOMPTE
            // .equalsIgnoreCase(this.decision.getTypeDecision()))) {
            if (CPDecision.CS_FRANCHISE.equalsIgnoreCase(decision.getSpecification())) {
                revenuCi = 0;
            }
            donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_REV_CI, revenuCi);
        }
        return revenuCi;
    }

    protected float calculAndSaveRevenuMoyen(float revenuBrut, CPDonneesCalcul donCalcul) throws Exception {
        if (decision.getAnneePrise().equalsIgnoreCase("T")) {
            // Sauvegarde du revenu brut moyen
            revenuBrut = revenuBrut / 2;
            donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_REV_MOYEN, revenuBrut);
        }
        // Si conjoint => diviser le revenu et la fortune par 2
        if ((!JadeStringUtil.isIntegerEmpty(decision.getIdConjoint()))
                || (decision.getDivision2().equals(new Boolean(true)))) {
            // Sauvegarde du revenu brut moyen
            revenuBrut = revenuBrut / 2;
            donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_REV_MOYEN, revenuBrut);
        }
        return revenuBrut;
    }

    protected float calculAndSaveRevenuMoyenInd(int codeRevenuAf, float revenuBrut, CPDonneesCalcul donCalcul)
            throws Exception {
        if (decision.getAnneePrise().equalsIgnoreCase("T")) {
            // Sauvegarde du revenu brut moyen
            revenuBrut = revenuBrut / 2;
            if (codeRevenuAf != 2) {
                donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_REV_MOYEN, revenuBrut);
            }
        }
        return revenuBrut;
    }

    /**
     * Calcul de l'assurance chomage (TSE) Date de création : (11.11.2005 12:58:23)
     * 
     * @param process
     *            BProcess le processus d'exécution
     */
    public void calculAssuranceChomage(BProcess process, boolean exempte, int saveDureeDecision) {
        float taux = 0;
        String saveRevenuNet = "";
        String revenuMaxAC = "";
        float cotiMensuelle = 0;
        // Sous contrôle d'exception
        try {
            if (!process.getTransaction().hasErrors()) {
                // Test par rapport à date AVS
                String dateAgeAvs = getTiers().getDateAvs();
                int anneeAvs = JACalendar.getYear(dateAgeAvs);
                int anneeDec = JACalendar.getYear(getDecision().getAnneeDecision());
                // Mettre date max à l'âge avs pour les non actifs.
                if (anneeAvs >= anneeDec) {
                    AFAffiliation aff = new AFAffiliation();
                    aff.setSession(process.getSession());
                    AFCotisation cotiAf = aff._cotisation(process.getTransaction(), decision.getIdAffiliation(),
                            CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AC,
                            decision.getDebutDecision(), decision.getFinDecision(), 1);
                    if ((cotiAf != null)
                            && !cotiAf.getDateDebut().equalsIgnoreCase(cotiAf.getDateFin())
                            && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), cotiAf.getDateDebut(),
                                    dateAgeAvs)) {
                        if (!exempte) {
                            if (anneeAvs == anneeDec) {
                                cotiAf.setDateFin(dateAgeAvs);
                            }
                            // Prendre la date la plus récente entre l'assurance
                            // et la décision si l'année de coti = l'année de décision
                            // sinon prendre la date de décision
                            String dateRef = decision.getDebutDecision();
                            if ((Integer.parseInt(decision.getAnneeDecision()) == JACalendar.getYear(cotiAf
                                    .getDateDebut()))
                                    && BSessionUtil.compareDateFirstGreater(getSession(), cotiAf.getDateDebut(),
                                            decision.getDebutDecision())) {
                                dateRef = cotiAf.getDateDebut();
                            }
                            // Recherche du taux
                            try {
                                taux = Float.parseFloat(JANumberFormatter.deQuote(cotiAf.getTaux(dateRef, "0")));
                            } catch (Exception e) {
                                taux = 0; // BTC bug affiliation!!! 15.12.2003
                            }
                            // recherche du revenu à prendre en compte ->
                            // montant max défini dans paramètre de l'assurance
                            CPDonneesCalcul donneesCalcul = new CPDonneesCalcul();
                            donneesCalcul.setSession(getSession());
                            String revenuNet = JANumberFormatter.deQuote(donneesCalcul.getMontant(
                                    decision.getIdDecision(), CPDonneesCalcul.CS_REV_NET));
                            if (JadeStringUtil.isEmpty(revenuNet)) {
                                revenuNet = "0";
                            }
                            // Recherche du montant de revenu maximum
                            revenuMaxAC = determinePlafondAssuranceChomage(process, dateRef, "PLAFONDAC1");
                            BigDecimal varNum = new BigDecimal(revenuMaxAC);
                            if ((varNum.compareTo(new BigDecimal(0)) == 1)
                                    && (JadeStringUtil.toFloat(revenuNet) > JadeStringUtil.toFloat(revenuMaxAC))) {
                                saveRevenuNet = revenuNet;
                                revenuNet = revenuMaxAC;
                            }
                            // Calcul de la durée de cotisation
                            cotiMensuelle = calculMontantCotisationMensuelle(saveDureeDecision, taux, cotiAf, revenuNet);
                        }
                        addCotisation(this, cotiMensuelle, cotiAf, Float.toString(taux), "", Boolean.FALSE);
                        if (!JadeStringUtil.isEmpty(saveRevenuNet) && !exempte) {
                            calculAssuranceChomage2(process, revenuMaxAC, saveRevenuNet, saveDureeDecision);
                        } else {
                            // Recherche s'il y a eu un cotisation ACII
                            cotiAf = aff._cotisation(process.getTransaction(), decision.getIdAffiliation(),
                                    CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AC2,
                                    affiliation.getDateDebut(), decision.getFinDecision(), 4);
                            if ((cotiAf != null) && (getCompteAnnexe() != null)) {
                                String montantCompteur = CPToolBox.rechMontantFacture(getSession(), getTransaction(),
                                        getCompteAnnexe().getIdCompteAnnexe(), cotiAf.getAssurance().getRubriqueId(),
                                        decision.getAnneeDecision());
                                if (!JadeStringUtil.isBlankOrZero(montantCompteur)
                                        || !JadeStringUtil.isBlankOrZero(cotiAf.getMontantAnnuel())) {
                                    // Recherche du taux de la cotisation pour la période de décision
                                    taux = findTaux(cotiAf);
                                    addCotisation(process, 0, cotiAf, Float.toString(taux), "", Boolean.FALSE);
                                }
                            }
                        }
                    } else {
                        // PO 2036
                        // Recherche s'il y a un compteur pour la rubrique AC <>
                        // 0 pour l'année de décision
                        if (getCompteAnnexe() != null) {
                            // Recherche s'il y a eu une cotisation AF - PO 9407
                            AFAffiliationManager mng = new AFAffiliationManager();
                            mng.setSession(getSession());
                            mng.setForAffilieNumero(affiliation.getAffilieNumero());
                            mng.setForIdTiers(decision.getIdTiers());
                            mng.find();
                            String idAffiliation = "";
                            for (int i = 0; i < mng.getSize() && cotiAf == null; i++) {
                                AFAffiliation autreAffiliation = (AFAffiliation) mng.getEntity(i);
                                idAffiliation = autreAffiliation.getAffiliationId();
                                cotiAf = aff._cotisation(process.getTransaction(), idAffiliation,
                                        CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AC,
                                        affiliation.getDateDebut(), decision.getFinDecision(), 4);

                            }
                            // Test pour éviter de faire le traitement pour l'AC2
                            if (!JadeStringUtil.isEmpty(idAffiliation)) {
                                addCotisationPourCotisationFacturee(process, cotiAf);
                                // Recherche s'il y a eu une cotisation ACII
                                cotiAf = aff._cotisation(process.getTransaction(), idAffiliation,
                                        CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AC2,
                                        affiliation.getDateDebut(), decision.getFinDecision(), 4);
                                addCotisationPourCotisationFacturee(process, cotiAf);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            this._addError(process.getTransaction(), e.getMessage() + " " + getDescriptionTiers());
            return;
        }
    }

    /**
     * Calcul de l'assurance chomage 2 (TSE) : (Revenu (éventuellement plafonné) - plafond AC1) * taux Date de création
     * : (11.11.2005 12:58:23)
     * 
     * @param process
     *            BProcess le processus d'exécution
     * @param plafond
     *            AC1
     * @param revenu
     *            à prendre en compte
     */
    public void calculAssuranceChomage2(BProcess process, String plafondAC1, String revenu, int saveDureeDecision) {
        float taux = 0;
        float cotiMensuelle = 0;
        // Sous contrôle d'exception
        try {
            if (!process.getTransaction().hasErrors()) {
                String dateAgeAvs = getTiers().getDateAvs();
                int anneeAvs = JACalendar.getYear(dateAgeAvs);
                int anneeDec = JACalendar.getYear(getDecision().getAnneeDecision());
                AFAffiliation aff = new AFAffiliation();
                aff.setSession(process.getSession());
                AFCotisation cotiAf = aff._cotisation(process.getTransaction(), decision.getIdAffiliation(),
                        CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AC2,
                        decision.getDebutDecision(), decision.getFinDecision(), 1);
                if ((cotiAf != null) && !cotiAf.getDateDebut().equalsIgnoreCase(cotiAf.getDateFin())) {
                    // Prendre la date la plus récente entre l'assurance et la
                    // décision si l'année de coti = l'année de décision
                    // sinon prendre la date de décision
                    String dateRef = decision.getDebutDecision();
                    if ((Integer.parseInt(decision.getAnneeDecision()) == JACalendar.getYear(cotiAf.getDateDebut()))
                            && BSessionUtil.compareDateFirstGreater(getSession(), cotiAf.getDateDebut(),
                                    decision.getDebutDecision())) {
                        dateRef = cotiAf.getDateDebut();
                    }
                    if (anneeAvs == anneeDec) {
                        cotiAf.setDateFin(dateAgeAvs);
                    }
                    // Recherche du taux
                    try {
                        taux = Float.parseFloat(JANumberFormatter.deQuote(cotiAf.getTaux(dateRef, "0")));
                    } catch (Exception e) {
                        taux = 0; // BTC bug affiliation!!! 15.12.2003
                    }
                    if (taux != 0) {
                        // Recherche du montant de revenu maximum
                        String revenuMaxAC2 = determinePlafondAssuranceChomage(process, dateRef, "PLAFONDAC2");
                        BigDecimal varNum = new BigDecimal(revenuMaxAC2);
                        if (varNum.compareTo(new BigDecimal(0)) == 1) {
                            if (JadeStringUtil.toFloat(revenu) > JadeStringUtil.toFloat(revenuMaxAC2)) {
                                revenu = revenuMaxAC2;
                            }
                            FWCurrency varTemp = new FWCurrency(revenu);
                            varTemp.sub(new FWCurrency(plafondAC1));
                            if (varTemp.floatValue() > 0) {
                                revenu = varTemp.toString();
                                cotiMensuelle = calculMontantCotisationMensuelle(saveDureeDecision, taux, cotiAf,
                                        revenu);
                                addCotisation(this, cotiMensuelle, cotiAf, Float.toString(taux), "", Boolean.FALSE);
                            }
                        } else {
                            // Remboursement de l'AC2 qui aurait déjà été facturée par une autre décision
                            if (getCompteAnnexe() != null) {
                                // Recherche s'il y a eu un cotisation ACII
                                cotiAf = aff._cotisation(process.getTransaction(), decision.getIdAffiliation(),
                                        CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AC2,
                                        affiliation.getDateDebut(), decision.getFinDecision(), 4);
                                addCotisationPourCotisationFacturee(process, cotiAf);
                            }
                        }
                    }
                } else {
                    // PO 2036 et 9407- Recherche et extourne s'il y a un compteur pour la rubrique pour l'année de
                    // décision
                    creationCotisationPourExtrounerCompteurExistant(process, aff, cotiAf,
                            CodeSystem.TYPE_ASS_COTISATION_AC2);
                }
            }
        } catch (Exception e) {
            this._addError(process.getTransaction(), e.getMessage() + " " + getDescriptionTiers());
            return;
        }
    }

    private void creationCotisationPourExtrounerCompteurExistant(BProcess process, AFAffiliation aff,
            AFCotisation cotiAf, String typeAssurance) throws Exception {
        if (getCompteAnnexe() != null) {
            // Recherche s'il y a eu une cotisation AF
            AFAffiliationManager mng = new AFAffiliationManager();
            mng.setSession(getSession());
            mng.setForAffilieNumero(affiliation.getAffilieNumero());
            mng.setForIdTiers(decision.getIdTiers());
            mng.find();
            for (int i = 0; i < mng.getSize() && cotiAf == null; i++) {
                AFAffiliation autreAffiliation = (AFAffiliation) mng.getEntity(i);
                cotiAf = aff._cotisation(process.getTransaction(), autreAffiliation.getAffiliationId(),
                        CodeSystem.GENRE_ASS_PERSONNEL, typeAssurance, affiliation.getDateDebut(),
                        decision.getFinDecision(), 4);

            }
            addCotisationPourCotisationFacturee(process, cotiAf);
        }
    }

    /**
     * Calcul des bonifications ou mises en compte Date de création : (25.02.2002 12:58:23)
     * 
     * @param process
     *            BProcess le processus d'exécution
     */
    public void calculBonifMiseEnCompte(BProcess process) {
        // Sous contrôle d'exception
        try {
            float cotiEncode = 0;
            float revenuCi = 0;
            float cotiCalcule = 0;
            if (!JadeStringUtil.isBlank(getDonneeBase().getCotisation1())) {
                cotiEncode = Float.parseFloat(JANumberFormatter.deQuote(getDonneeBase().getCotisation1()));
            }
            if (!JadeStringUtil.isBlank(getDonneeBase().getRevenu1())) {
                revenuCi = Float.parseFloat(JANumberFormatter.deQuote(getDonneeBase().getRevenu1()));
            }
            if ((cotiEncode != 0) && (revenuCi == 0)) {
                // Si une décision avait déjà était prise pour l'année de la
                // bonification
                // revenuCI = -(Montant CI de l'ancienne décision -
                // ((montantAnnuelAncienneDecision - montant de bonif) * 9,9))
                // Ex: Ancienne décision : cotiAnnuelle=2828 Fr. et revenu CI =
                // 28000
                // Montant bonification = 1009.25
                // Revenu CI = -(28000 - ((2828-1009.25)*99)) = -9994
                globaz.phenix.toolbox.CPToolBox toolBox = new globaz.phenix.toolbox.CPToolBox();
                CPCotisation coti = toolBox.rechercheCotisation(getSession(), getTransaction(), decision);
                if (coti != null) {
                    // recherche ancien montant CI
                    CPDonneesCalcul donneeCalcul = new CPDonneesCalcul();
                    donneeCalcul.setSession(getSession());
                    String varCi = donneeCalcul.getMontant(coti.getIdDecision(), CPDonneesCalcul.CS_REV_CI);
                    if (JadeStringUtil.isEmpty(varCi)) {
                        if (Integer.parseInt(getDecision().getAnneeDecision()) >= 2011) {
                            revenuCi = cotiEncode * (float) 9.71;
                        } else {
                            revenuCi = cotiEncode * (float) 9.9;
                        }
                    } else {
                        float ancienCi = Float.parseFloat(JANumberFormatter.deQuote(donneeCalcul.getMontant(
                                coti.getIdDecision(), CPDonneesCalcul.CS_REV_CI)));
                        // Calcul du CI
                        if (Integer.parseInt(getDecision().getAnneeDecision()) >= 2011) {
                            revenuCi = (Float.parseFloat(JANumberFormatter.deQuote(coti.getMontantAnnuel())) - cotiEncode)
                                    * (float) 9.71;
                        } else {
                            revenuCi = (Float.parseFloat(JANumberFormatter.deQuote(coti.getMontantAnnuel())) - cotiEncode)
                                    * (float) 9.9;
                        }
                        revenuCi = JANumberFormatter.round(revenuCi, 1, 2, JANumberFormatter.NEAR);

                        // Calcul
                        revenuCi = ancienCi - revenuCi;
                    }
                } else {
                    if (Integer.parseInt(getDecision().getAnneeDecision()) >= 2011) {
                        revenuCi = cotiEncode * (float) 9.71;
                    } else {
                        revenuCi = cotiEncode * (float) 9.9;
                    }
                }
            } else if ((revenuCi != 0) && (cotiEncode == 0)) {
                // On a saisi le montant du revenu CI, il faut calculer la
                // cotisation
                if (Integer.parseInt(getDecision().getAnneeDecision()) >= 2016) {
                    cotiCalcule = revenuCi * (float) 0.1025;
                } else if (Integer.parseInt(getDecision().getAnneeDecision()) >= 2011) {
                    cotiCalcule = revenuCi * (float) 0.103;
                } else {
                    cotiCalcule = revenuCi * (float) 0.101;
                }
                cotiCalcule = JANumberFormatter.round(cotiCalcule, 0.05, 2, JANumberFormatter.NEAR);
                donneeBase.setCotisation1(Float.toString(cotiCalcule));
                donneeBase.update(process.getTransaction());
            }
            revenuCi = JANumberFormatter.round(revenuCi, 1, 2, JANumberFormatter.NEAR);
            // Sauvegarde du revenu CI
            CPDonneesCalcul donCalcul = new CPDonneesCalcul();
            donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_REV_CI, revenuCi);
        } catch (Exception e) {
            this._addError(process.getTransaction(), e.getMessage() + " " + getDescriptionTiers());
            return;
        }
    }

    /**
     * Les exceptions avec un message à l'attention de l'utilisateur n'ont pas été labellisées Ceci car avec le
     * fonctionnement actuel du process aucun mail est envoyé et aucun message remonté à l'utilisateur TODO labelliser
     * les exceptions lorsque le fonctionnement du process permettra de remonter un message à l'utilisateur
     */
    private void calculCotisationFederative(int saveDureeDecision) throws Exception {

        // Récupération de la cotisation fédérative
        AFCotisationManager cotiMgr = new AFCotisationManager();
        cotiMgr.setSession(getSession());
        cotiMgr.setForAffiliationId(decision.getAffiliation().getAffiliationId());
        cotiMgr.setDateDebutLessEqual(decision.getFinDecision());
        cotiMgr.setDateFinGreaterEqual(decision.getDebutDecision());
        cotiMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_FEDERATIVE);
        cotiMgr.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
        cotiMgr.find(BManager.SIZE_NOLIMIT);

        // si aucune cotisation fédérative a été trouvée et aucune exception levée
        // l'affilié n'est pas soumis à la cotisation fédérative et il faut sortir
        if (cotiMgr.size() <= 0) {
            return;
        }

        // s'il y a plus d'une cotisation fédérative dans la période de décision --> erreur de paramétrage
        // exemple :
        // décision(01.01.2011 au 31.12.2011) / cotisation fédérative(01.01.2011 au 30.06.2011) / cotisation
        // fédérative(01.07.2011 au 31.12.2011)
        // --> pas accepté
        if (cotiMgr.size() > 1) {
            throw new Exception("Erreur : " + cotiMgr.size()
                    + " cotisations fédératives ont été trouvées dans la période du " + decision.getDebutDecision()
                    + " au " + decision.getFinDecision());
        }

        AFCotisation cotisationFederative = (AFCotisation) cotiMgr.getEntity(0);
        // PO 6853 - Ne pas traiter si date de début = date de fin
        if (cotisationFederative.getDateDebut().equalsIgnoreCase(cotisationFederative.getDateFin())) {
            return;
        }
        // Récupération du revenu déterminant
        String theRevenuDeterminant;
        CPDonneesCalcul theDonneeCalcul = new CPDonneesCalcul();
        theDonneeCalcul.setSession(getSession());
        theRevenuDeterminant = theDonneeCalcul.getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_NET);
        theRevenuDeterminant = JANumberFormatter.deQuote(theRevenuDeterminant);

        if (!JadeNumericUtil.isNumeric(theRevenuDeterminant)) {
            // throw new Exception("unable to calcul cotisation federative because argument revenuDeterminant is null");
            theRevenuDeterminant = "0";
        }
        // calcul de la période de cotisation effective
        // exemple :
        // décision(01.01.2011 au 31.12.2011) & cotisation fédérative(01.07.2011 à ...)
        // --> période de cotisation effective 01.07.2011 au 31.12.2011
        String debutPeriodeCotisation = decision.getDebutDecision();
        if (BSessionUtil.compareDateFirstGreater(getSession(), cotisationFederative.getDateDebut(),
                decision.getDebutDecision())) {
            debutPeriodeCotisation = cotisationFederative.getDateDebut();
        }

        String finPeriodeCotisation = decision.getFinDecision();
        if (!JadeStringUtil.isBlankOrZero(cotisationFederative.getDateFin())
                && BSessionUtil.compareDateFirstLower(getSession(), cotisationFederative.getDateFin(),
                        decision.getFinDecision())) {
            finPeriodeCotisation = cotisationFederative.getDateFin();
        }

        // Calcul de la durée de cotisation
        // PO 7954- 1-11-0
        int dureeCotisation = calculDureeCotisation(cotisationFederative, decision, false);
        dureeCotisation = calculDureeCotisationSelonCasMetier(saveDureeDecision, dureeCotisation);

        // plafonner revenuDeterminant
        theRevenuDeterminant = CPUtil.plafonneRevenuDeterminant(theRevenuDeterminant,
                cotisationFederative.getAssuranceId(), CodeSystem.GEN_PARAM_ASS_REVENU_MAX, finPeriodeCotisation,
                getSession());

        // Récupération du taux variable
        String tauxCotisationFederative = cotisationFederative.getTaux(finPeriodeCotisation, theRevenuDeterminant);

        // si aucun taux variable est trouvé pour la période à cotiser --> erreur paramétrage --> exception
        // le test ci-dessous doit être fait à cause du fonctionnement de la méthode AFCotisation.getTaux(...)
        // En effet, elle catch les exceptions, ajoute une erreur dans la transaction et retourne ""
        // elle retourne également "" si aucun taux est trouvé et aucune exception levée
        if (JadeStringUtil.isEmpty(tauxCotisationFederative)) {
            throw new Exception("Erreur : aucun taux variable trouvé dans la période du " + debutPeriodeCotisation
                    + " au " + finPeriodeCotisation);
        }
        // Calcul de la cotisation annuelle à payer
        // On passe dans la méthode AFTauxAssurance.getValeurTotal() : cas taux variable avec valeurEmployeur et
        // valeurEmploye not empty ("0" mais jamais vide)
        // Dans ce cas précis la méthode retourne le taux en % --> /100
        // Révision arrondi - PO 6756
        float theCotiAnnuel = Float.valueOf(theRevenuDeterminant).floatValue()
                * Float.valueOf(tauxCotisationFederative).floatValue() / 100;
        // arrondi de la cotisation annuelle au 5cts les plus proches
        float theCotiMensuel = theCotiAnnuel / dureeCotisation;
        theCotiMensuel = JANumberFormatter.round(theCotiMensuel, 0.05, 2, JANumberFormatter.NEAR);

        // Ajoute un enregistrement dans la table CPCOTIP
        // cette table contient les cotisations des décisions et est donc entre autres utilisée pour générer la
        // décision(document)
        addCotisation(this, theCotiMensuel, cotisationFederative, tauxCotisationFederative, "", Boolean.FALSE);

    }

    /**
     * Calcul des cotisations CPS autre Date de création : 22.10.2007
     * 
     * @param process
     *            BProcess le processus d'exécution
     * @param revenuCI
     */
    public void calculCPSAutre(BProcess process, float revenuCi, boolean exempte, int saveDureeDecision) {
        // Sous contrôle d'exception
        float taux = 0;
        Boolean cotiMinimum = Boolean.FALSE;
        float cotiMensuelle = 0;
        try {
            if (!process.getTransaction().hasErrors()) {
                AFAffiliation aff = new AFAffiliation();
                aff.setSession(process.getSession());
                AFCotisation cotiAf = aff._cotisation(process.getTransaction(), decision.getIdAffiliation(),
                        CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_CPS_AUTRE, decision.getDebutDecision(),
                        decision.getFinDecision(), 1);
                if ((cotiAf != null) && !cotiAf.getDateDebut().equalsIgnoreCase(cotiAf.getDateFin())) {
                    if (!exempte) {
                        // Pour les rentiers dans l'année et qui paye au prorata
                        // la coti avs
                        // il faut prendre la durée jusqu'à l'âge de retraite
                        // (param saveDureeDecision)
                        // Ex: FVE cas 629.0320111 et 659.026.0121 et AGLAU
                        // 108.621.3
                        int dureeCotisation = calculDureeCotisation(cotiAf, decision, false);
                        dureeCotisation = calculDureeCotisationSelonCasMetier(saveDureeDecision, dureeCotisation);
                        // Prendre la date la plus récente entre l'assurance et
                        // la décision si l'année de coti = l'année de décision
                        // sinon prendre la date de décision
                        String dateRef = decision.getDebutDecision();
                        if ((Integer.parseInt(decision.getAnneeDecision()) == JACalendar.getYear(cotiAf.getDateDebut()))
                                && BSessionUtil.compareDateFirstGreater(getSession(), cotiAf.getDateDebut(),
                                        decision.getDebutDecision())) {
                            dateRef = cotiAf.getDateDebut();
                        }
                        // Recherche du montant de revenu maximum
                        String varString = cotiAf.getAssurance().getParametreAssuranceValeur(
                                CodeSystem.GEN_PARAM_ASS_REVENU_MAX, dateRef, "");
                        if (null == varString) {
                            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0174") + " "
                                    + cotiAf.getAssurance().getAssuranceLibelle(getSession().getIdLangueISO()));
                        }
                        if (JadeStringUtil.isIntegerEmpty(varString)) {
                            varString = "0";
                        }
                        double valRevMax = Double.parseDouble(JANumberFormatter.deQuote(varString));
                        if (saveDureeDecision != 0) {
                            valRevMax = (valRevMax / 12) * saveDureeDecision;
                        }

                        // K160601_002 - calcul erroné des cotisations AF différentielles
                        valRevMax = determineRevenuMaxSuivantNombreDeMois(varString, valRevMax,
                                decision.getNombreMoisTotalDecision());

                        double valRevenuCi = revenuCi;
                        if (valRevenuCi > valRevMax) {
                            revenuCi = (float) valRevMax;
                        }
                        try {
                            taux = Float.parseFloat(JANumberFormatter.deQuote(cotiAf.getTaux(dateRef, "0")));
                        } catch (Exception e) {
                            taux = 0; // BTC bug affiliation!!! 15.12.2003
                        }
                        float cotiAnnuelleBrut = (revenuCi * taux) / 100;
                        // Recherche de la cotisation mimimum AF
                        if (!CPDecision.CS_RENTIER.equalsIgnoreCase(decision.getGenreAffilie()) || casProrataRentier) {
                            varString = cotiAf.getAssurance().getParametreAssuranceValeur(
                                    CodeSystem.GEN_PARAM_ASS_COTISATION_MIN, dateRef, "");
                            if (null == varString) {
                                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0175") + " "
                                        + cotiAf.getAssurance().getAssuranceLibelle(getSession().getIdLangueISO()));
                            }
                            if (JadeStringUtil.isIntegerEmpty(varString)) {
                                varString = "0";
                            }
                            double valCotMin = Double.parseDouble(JANumberFormatter.deQuote(varString));
                            if (casProrataRentier && (saveDureeDecision != 0)) {
                                valCotMin = (valCotMin / 12) * saveDureeDecision;
                            }
                            double valCotiAnnuelleBrut = cotiAnnuelleBrut;
                            // PO 3548: Ne pas prendre le minimum en cas de DIN
                            // 1181
                            if ((valCotMin > valCotiAnnuelleBrut)
                                    && Boolean.FALSE.equals(decision.getCotiMinimumPayeEnSalarie())) {
                                cotiAnnuelleBrut = (float) valCotMin;
                                cotiMinimum = Boolean.TRUE;
                                taux = 0;
                            }
                        }
                        // Calcul de cotisation mensuelle - arrondi au 5 cts
                        cotiMensuelle = cotiAnnuelleBrut / dureeCotisation;
                        cotiMensuelle = JANumberFormatter.round(cotiMensuelle, 0.05, 2, JANumberFormatter.NEAR);
                    }
                    // Création des cotisations dans CPCOTIP
                    addCotisation(process, cotiMensuelle, cotiAf, Float.toString(taux), "", cotiMinimum);
                } else {
                    // PO 2036 et 9407- Recherche et extourne s'il y a un compteur pour la rubrique pour l'année de
                    // décision
                    creationCotisationPourExtrounerCompteurExistant(process, aff, cotiAf, CodeSystem.TYPE_ASS_CPS_AUTRE);
                }
            }
        } catch (Exception e) {
            this._addError(process.getTransaction(), e.getMessage() + " " + getDescriptionTiers());
            return;
        }
    }

    /**
     * Calcul des cotisations CPS Général Date de création : 22.10.2007
     * 
     * @param process
     *            BProcess le processus d'exécution
     * @param revenuCI
     */
    public void calculCPSGeneral(BProcess process, float revenuCi, boolean exempte, int saveDureeDecision) {
        // Sous contrôle d'exception
        float taux = 0;
        Boolean cotiMinimum = Boolean.FALSE;
        float cotiMensuelle = 0;
        try {
            if (!process.getTransaction().hasErrors()) {
                AFAffiliation aff = new AFAffiliation();
                aff.setSession(process.getSession());
                AFCotisation cotiAf = aff._cotisation(process.getTransaction(), decision.getIdAffiliation(),
                        CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_CPS_GENERAL, decision.getDebutDecision(),
                        decision.getFinDecision(), 1);
                if ((cotiAf != null) && !cotiAf.getDateDebut().equalsIgnoreCase(cotiAf.getDateFin())) {
                    if (!exempte) {
                        // Pour les rentiers dans l'année et qui paye au prorata
                        // la coti avs
                        // il faut prendre la durée jusqu'à l'âge de retraite
                        // (param saveDureeDecision)
                        // Ex: FVE cas 629.0320111 et 659.026.0121 et AGLAU
                        // 108.621.3
                        int dureeCotisation = calculDureeCotisation(cotiAf, decision, false);
                        dureeCotisation = calculDureeCotisationSelonCasMetier(saveDureeDecision, dureeCotisation);
                        // Prendre la date la plus récente entre l'assurance et
                        // la décision si l'année de coti = l'année de décision
                        // sinon prendre la date de décision
                        String dateRef = decision.getDebutDecision();
                        if ((Integer.parseInt(decision.getAnneeDecision()) == JACalendar.getYear(cotiAf.getDateDebut()))
                                && BSessionUtil.compareDateFirstGreater(getSession(), cotiAf.getDateDebut(),
                                        decision.getDebutDecision())) {
                            dateRef = cotiAf.getDateDebut();
                        }
                        // Recherche du montant de revenu maximum
                        String varString = cotiAf.getAssurance().getParametreAssuranceValeur(
                                CodeSystem.GEN_PARAM_ASS_REVENU_MAX, dateRef, "");
                        if (null == varString) {
                            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0174") + " "
                                    + cotiAf.getAssurance().getAssuranceLibelle(getSession().getIdLangueISO()));
                        }
                        if (JadeStringUtil.isIntegerEmpty(varString)) {
                            varString = "0";
                        }
                        double valRevMax = Double.parseDouble(JANumberFormatter.deQuote(varString));
                        if (saveDureeDecision != 0) {
                            valRevMax = (valRevMax / 12) * saveDureeDecision;
                        }

                        // K160601_002 - calcul erroné des cotisations AF différentielles
                        valRevMax = determineRevenuMaxSuivantNombreDeMois(varString, valRevMax,
                                decision.getNombreMoisTotalDecision());

                        double valRevenuCi = revenuCi;
                        if (valRevenuCi > valRevMax) {
                            revenuCi = (float) valRevMax;
                        }
                        try {
                            taux = Float.parseFloat(JANumberFormatter.deQuote(cotiAf.getTaux(dateRef, "0")));
                        } catch (Exception e) {
                            taux = 0; // BTC bug affiliation!!! 15.12.2003
                        }
                        float cotiAnnuelleBrut = (revenuCi * taux) / 100;
                        // Recherche de la cotisation mimimum AF
                        if (!CPDecision.CS_RENTIER.equalsIgnoreCase(decision.getGenreAffilie()) || casProrataRentier) {
                            varString = cotiAf.getAssurance().getParametreAssuranceValeur(
                                    CodeSystem.GEN_PARAM_ASS_COTISATION_MIN, dateRef, "");
                            if (null == varString) {
                                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0175") + " "
                                        + cotiAf.getAssurance().getAssuranceLibelle(getSession().getIdLangueISO()));
                            }
                            if (JadeStringUtil.isIntegerEmpty(varString)) {
                                varString = "0";
                            }
                            double valCotMin = Double.parseDouble(JANumberFormatter.deQuote(varString));
                            if (casProrataRentier && (saveDureeDecision != 0)) {
                                valCotMin = (valCotMin / 12) * saveDureeDecision;
                            }
                            double valCotiAnnuelleBrut = cotiAnnuelleBrut;
                            // PO 3548: Ne pas prendre le minimum en cas de DIN 1181
                            if ((valCotMin > valCotiAnnuelleBrut)
                                    && Boolean.FALSE.equals(decision.getCotiMinimumPayeEnSalarie())) {
                                cotiAnnuelleBrut = (float) valCotMin;
                                cotiMinimum = Boolean.TRUE;
                                taux = 0;
                            }
                        }
                        // Calcul de cotisation mensuelle - arrondi au 5 cts
                        cotiMensuelle = cotiAnnuelleBrut / dureeCotisation;
                        cotiMensuelle = JANumberFormatter.round(cotiMensuelle, 0.05, 2, JANumberFormatter.NEAR);
                    }
                    // Création des cotisations dans CPCOTIP
                    addCotisation(process, cotiMensuelle, cotiAf, Float.toString(taux), "", cotiMinimum);
                } else {
                    // PO 2036 et 9407- Recherche et extourne s'il y a un compteur pour la rubrique pour l'année de
                    // décision
                    creationCotisationPourExtrounerCompteurExistant(process, aff, cotiAf,
                            CodeSystem.TYPE_ASS_CPS_GENERAL);
                }
            }
        } catch (Exception e) {
            this._addError(process.getTransaction(), e.getMessage() + " " + getDescriptionTiers());
            return;
        }
    }

    /**
     * Calcul de la durée de cotisation (la décision et l'assurance) Date de création : (25.02.2002 12:58:23)
     * 
     * @param assurance
     *            AFCotisation
     * @param décision
     *            CPDecision
     */
    public int calculDureeCotisation(AFCotisation cotiAf, CPDecision decision, boolean wantPeriodeDecision)
            throws Exception {
        // Calcul de la duree de cotisation
        // Si plusieurs décisions (complémentaire), il faut se baser sur le
        // nombre de mois total de la décision qui a été saisi et celui de
        // l'assurance
        String dateDebut = decision.getDebutDecision();
        String dateFin = decision.getFinDecision();
        if ((!JadeStringUtil.isIntegerEmpty(cotiAf.getDateFin()) && BSessionUtil.compareDateFirstLower(getSession(),
                cotiAf.getDateFin(), dateFin))
                || (BSessionUtil.compareDateFirstGreater(getSession(), cotiAf.getDateDebut(), dateDebut))) {
            return 12;
        }
        int dureeCotisation = 0;
        int moisTotal = Integer.parseInt(decision.getNombreMoisTotalDecision());
        if ((wantPeriodeDecision == false) && (moisTotal > 0)) {
            // Calcul de la période de l'assurance
            int moisDebut = 1;
            int moisFin = 12;
            int anneeAssuranceDebut = JACalendar.getYear(cotiAf.getDateDebut());
            if (anneeAssuranceDebut == Integer.parseInt(decision.getAnneeDecision())) {
                moisDebut = JACalendar.getMonth(cotiAf.getDateDebut());
            }
            if (!JAUtil.isDateEmpty(cotiAf.getDateFin())) {
                int anneeAssuranceFin = JACalendar.getYear(cotiAf.getDateFin());
                if (anneeAssuranceFin == Integer.parseInt(decision.getAnneeDecision())) {
                    moisFin = JACalendar.getMonth(cotiAf.getDateFin());
                }
            }
            // Si la période de l'assurance est < à celle total de la décision
            // saisie => la durée de cotisation
            // est celle de l'assurance sinon celle saisie
            int dureeAssurance = (moisFin - moisDebut) + 1;
            if (dureeAssurance < moisTotal) {
                return 12;
            } else {
                dureeCotisation = moisTotal;
            }
        } else {
            dureeCotisation = JACalendar.getMonth(dateFin) - JACalendar.getMonth(dateDebut) + 1;
        }
        return dureeCotisation;
    }

    protected int calculDureeCotisationSelonCasMetier(int saveDureeDecision, int dureeCotisation)
            throws NumberFormatException {
        if ((decision.getProrata().equalsIgnoreCase("2") || decision.getProrata().equalsIgnoreCase("3"))) {
            if (!casProrataRentier || (saveDureeDecision == 0)) {
                if (!JadeStringUtil.isBlankOrZero(decision.getNombreMoisTotalDecision())) { // PO
                    dureeCotisation = Integer.parseInt(decision.getNombreMoisTotalDecision());
                } else {
                    dureeCotisation = 12;
                }
            }
        }
        return dureeCotisation;
    }

    private float calculFortuneDetaillee(BProcess process, float fortuneTotal, float fortune1, float fortune2,
            float fortune3, float fortune4, float autreFortune, float dette) throws JAException {
        float taux;
        int anneeFortune = JACalendar.getYear(getDonneeBase().getDateFortune());
        // Récupération des données de la fortune
        CPFortune fortune = new CPFortune();
        fortune.setSession(process.getSession());
        fortune.setIdDecision(getIdDecision());
        try {
            fortune.retrieve(process.getTransaction());
            autreFortune = Float.parseFloat(JANumberFormatter.deQuote(fortune.getAutreFortune()));
            dette = Float.parseFloat(JANumberFormatter.deQuote(fortune.getDette()));
            // Calcul suivant montant immobilier 1
            if (!JadeStringUtil.isIntegerEmpty(fortune.getMontantImmobilier1())) {
                // Recherche taux
                CPTableFortuneManager tFortuneManager = new CPTableFortuneManager();
                tFortuneManager.setSession(getSession());
                tFortuneManager.setFromAnneeFortune(Integer.toString(anneeFortune));
                tFortuneManager.setForCanton(fortune.getCanton1());
                tFortuneManager.find();
                if (tFortuneManager.size() == 0) {
                    this._addError(process.getTransaction(), "taux non trouvé: " + decision.getAnneeDecision() + "  "
                            + fortune.getCanton1() + " " + getDescriptionTiers());
                } else {
                    CPTableFortune tFortune = ((CPTableFortune) tFortuneManager.getEntity(0));
                    if (fortune.getTypeTerrain1().equalsIgnoreCase(CPFortune.CS_AGRICOLE)) {
                        taux = Float.parseFloat(JANumberFormatter.deQuote(tFortune.getTauxAgricole()));
                    } else {
                        taux = Float.parseFloat(JANumberFormatter.deQuote(tFortune.getTauxNonAgricole()));
                    }
                    // Calcul fortune pour immobilier 1
                    fortune1 = Float.parseFloat(JANumberFormatter.deQuote(fortune.getMontantImmobilier1()));
                    fortune1 = (fortune1 * taux) / 100;
                }
            }
            // Calcul suivant montant immobilier 2
            if (!JadeStringUtil.isIntegerEmpty(fortune.getMontantImmobilier2())) {
                // Recherche taux
                CPTableFortuneManager tFortuneManager = new CPTableFortuneManager();
                tFortuneManager.setSession(getSession());
                tFortuneManager.setFromAnneeFortune(Integer.toString(anneeFortune));
                tFortuneManager.setForCanton(fortune.getCanton2());
                tFortuneManager.find();
                if (tFortuneManager.size() == 0) {
                    this._addError(process.getTransaction(), "taux non trouvé: " + decision.getAnneeDecision() + "  "
                            + fortune.getCanton2() + " " + getDescriptionTiers());
                } else {
                    CPTableFortune tFortune = ((CPTableFortune) tFortuneManager.getEntity(0));
                    if (fortune.getTypeTerrain2().equalsIgnoreCase(CPFortune.CS_AGRICOLE)) {
                        taux = Float.parseFloat(JANumberFormatter.deQuote(tFortune.getTauxAgricole()));
                    } else {
                        taux = Float.parseFloat(JANumberFormatter.deQuote(tFortune.getTauxNonAgricole()));
                    }
                    // Calcul fortune pour immobilier 2
                    fortune2 = Float.parseFloat(JANumberFormatter.deQuote(fortune.getMontantImmobilier2()));
                    fortune2 = (fortune2 * taux) / 100;
                }
            }
            // Calcul suivant montant immobilier 3
            if (!JadeStringUtil.isIntegerEmpty(fortune.getMontantImmobilier3())) {
                // Recherche taux
                CPTableFortuneManager tFortuneManager = new CPTableFortuneManager();
                tFortuneManager.setSession(getSession());
                tFortuneManager.setFromAnneeFortune(Integer.toString(anneeFortune));
                tFortuneManager.setForCanton(fortune.getCanton3());
                tFortuneManager.find();
                if (tFortuneManager.size() == 0) {
                    this._addError(process.getTransaction(), "taux non trouvé: " + decision.getAnneeDecision() + "  "
                            + fortune.getCanton3() + " " + getDescriptionTiers());
                } else {
                    CPTableFortune tFortune = ((CPTableFortune) tFortuneManager.getEntity(0));
                    if (fortune.getTypeTerrain3().equalsIgnoreCase(CPFortune.CS_AGRICOLE)) {
                        taux = Float.parseFloat(JANumberFormatter.deQuote(tFortune.getTauxAgricole()));
                    } else {
                        taux = Float.parseFloat(JANumberFormatter.deQuote(tFortune.getTauxNonAgricole()));
                    }
                    // Calcul fortune pour immobilier 3
                    fortune3 = Float.parseFloat(JANumberFormatter.deQuote(fortune.getMontantImmobilier3()));
                    fortune3 = (fortune3 * taux) / 100;
                }
            }
            // Calcul suivant montant immobilier 4
            if (!JadeStringUtil.isIntegerEmpty(fortune.getMontantImmobilier4())) {
                // Recherche taux
                CPTableFortuneManager tFortuneManager = new CPTableFortuneManager();
                tFortuneManager.setSession(getSession());
                tFortuneManager.setFromAnneeFortune(Integer.toString(anneeFortune));
                tFortuneManager.setForCanton(fortune.getCanton4());
                tFortuneManager.find();
                if (tFortuneManager.size() == 0) {
                    this._addError(process.getTransaction(), "taux non trouvé: " + decision.getAnneeDecision() + "  "
                            + fortune.getCanton4() + " " + getDescriptionTiers());
                } else {
                    CPTableFortune tFortune = ((CPTableFortune) tFortuneManager.getEntity(0));
                    if (fortune.getTypeTerrain4().equalsIgnoreCase(CPFortune.CS_AGRICOLE)) {
                        taux = Float.parseFloat(JANumberFormatter.deQuote(tFortune.getTauxAgricole()));
                    } else {
                        taux = Float.parseFloat(JANumberFormatter.deQuote(tFortune.getTauxNonAgricole()));
                    }
                    // Calcul fortune pour immobilier 4
                    fortune4 = Float.parseFloat(JANumberFormatter.deQuote(fortune.getMontantImmobilier4()));
                    fortune4 = (fortune4 * taux) / 100;
                }
            }
            fortuneTotal = (fortune1 + fortune2 + fortune3 + fortune4 + autreFortune) - dette;

        } catch (Exception e) {
            this._addError(process.getTransaction(), e.getMessage() + " " + getDescriptionTiers());
        }
        return fortuneTotal;
    }

    protected float calculFortuneTotal(BProcess process, float fortuneTotal, float fortune1, float fortune2,
            float fortune3, float fortune4, float autreFortune, float dette) throws Exception, JAException,
            NumberFormatException {
        if ((((CPApplication) getSession().getApplication()).isCPCALCIMMO())
                && (!decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_DEFINITIVE))
                && (!decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_RECTIFICATION))) {
            // Prendre l'année de la fortune pour le taux immobilier
            fortuneTotal = calculFortuneDetaillee(process, fortuneTotal, fortune1, fortune2, fortune3, fortune4,
                    autreFortune, dette);
        } else
        // Fortune = valeur encodée dans le cas ou c'est non détaillé
        if (!JadeStringUtil.isBlank(getDonneeBase().getFortuneTotale(0))) {
            fortuneTotal = Float.parseFloat(JANumberFormatter.deQuote(getDonneeBase().getFortuneTotale(0)));
        }
        // Mise à jour de la fortune total
        if ((!JadeStringUtil.isIntegerEmpty(decision.getIdConjoint()))
                || (decision.getDivision2().equals(new Boolean(true)))) {
            if (!isCalculForReprise()) {
                fortuneTotal = fortuneTotal / 2;
            }
        }
        if (!getDonneeBase().isNew()) {
            if (fortuneTotal != 0) {
                getDonneeBase().setFortuneTotale(Float.toString(fortuneTotal));
                getDonneeBase().update(process.getTransaction());
            }
            if (isCalculForReprise() && !(new FWCurrency(getDonneeBase().getFortuneTotale(0)).isZero())) {
                fortuneTotal = new Float(JANumberFormatter.deQuote(getDonneeBase().getFortuneTotale(0))).floatValue();
            }
        }
        return fortuneTotal;
    }

    /**
     * Calcul des frais d'administration Date de création : (25.02.2002 12:58:23)
     * 
     * @param process
     *            BProcess le processus d'exécution
     */
    public void calculFraisAdministration(BProcess process) {
        float calcul = 0;
        float taux = 0;
        String arrondiCalcul = "";
        // Sous contrôle d'exception
        try {
            if (!process.getTransaction().hasErrors()) {
                AFAffiliation aff = new AFAffiliation();
                aff.setSession(process.getSession());
                AFCotisation cotiAf = aff._cotisation(process.getTransaction(), decision.getIdAffiliation(),
                        CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_FRAIS_ADMIN, decision.getDebutDecision(),
                        decision.getFinDecision(), 1);
                if ((cotiAf != null) && !cotiAf.getDateDebut().equalsIgnoreCase(cotiAf.getDateFin())) {
                    // Prendre la date la plus récente entre l'assurance et la
                    // décision si l'année de coti = l'année de décision
                    // sinon prendre la date de décision
                    String dateRef = decision.getDebutDecision();
                    if ((Integer.parseInt(decision.getAnneeDecision()) == JACalendar.getYear(cotiAf.getDateDebut()))
                            && BSessionUtil.compareDateFirstGreater(getSession(), cotiAf.getDateDebut(),
                                    decision.getDebutDecision())) {
                        dateRef = cotiAf.getDateDebut();
                    }

                    // Récupération du revenu déterminant
                    String theRevenuDeterminant = "0";

                    if (CodeSystem.CATEGORIE_TAUX_IND_TSE.equalsIgnoreCase(cotiAf.getCategorieTauxId())) {

                        try {

                            float theRevenuMinimumIndependant = CPTableIndependant.getRevenuCiMin(getTransaction(),
                                    decision.getDebutDecision());

                            CPDonneesCalcul theDonneeCalcul = new CPDonneesCalcul();
                            theDonneeCalcul.setSession(getSession());
                            theRevenuDeterminant = theDonneeCalcul.getMontant(decision.getIdDecision(),
                                    CPDonneesCalcul.CS_REV_NET);
                            theRevenuDeterminant = JANumberFormatter.deQuote(theRevenuDeterminant);

                            if (!JadeNumericUtil.isNumeric(theRevenuDeterminant)) {
                                theRevenuDeterminant = "0";
                            }

                            if (new FWCurrency(theRevenuDeterminant).floatValue() < theRevenuMinimumIndependant) {
                                theRevenuDeterminant = new FWCurrency(theRevenuMinimumIndependant).toString();
                            }
                        } catch (Exception e) {
                            throw new Exception(getSession().getLabel("PROBLEM_TO_DETERMINE_REVENU_DETERMINANT") + " "
                                    + decision.getDebutDecision() + " " + e.toString());
                        }
                    }

                    AFTauxAssurance theTauxFraisAdministrationPersonnel = null;

                    try {
                        theTauxFraisAdministrationPersonnel = cotiAf.findTauxWithRecalcul(dateRef,
                                theRevenuDeterminant, true, false, "");
                    } catch (Exception e) {
                        throw new Exception(getSession().getLabel(
                                "FIND_TAUX_FRAIS_ADMINISTRATION_PERSONNELLE_EXCEPTION_THROWED")
                                + e.toString());
                    }

                    if ((theTauxFraisAdministrationPersonnel == null) || theTauxFraisAdministrationPersonnel.isNew()) {
                        throw new Exception(getSession().getLabel("NO_TAUX_FRAIS_ADMINISTRATION_PERSONNELLE_FOUNDED"));
                    }

                    taux = Float.parseFloat(JANumberFormatter.deQuote(theTauxFraisAdministrationPersonnel
                            .getValeurTotal()));

                    if (CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equalsIgnoreCase(theTauxFraisAdministrationPersonnel
                            .getGenreValeur())
                            && CodeSystem.TYPE_CALCUL_MASSE.equalsIgnoreCase(cotiAf.getAssurance().getTypeCalcul())) {
                        float theRevenuDeterminantForCalcul = JANumberFormatter.round(new FWCurrency(
                                theRevenuDeterminant).floatValue() / 12, 1, 0, JANumberFormatter.INF);
                        if (CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())
                                && JadeStringUtil.isEmpty(getMontantMensuel())) {
                            theRevenuDeterminantForCalcul = new FWCurrency(theRevenuDeterminant).floatValue();
                        }
                        calcul = theRevenuDeterminantForCalcul * taux / 100;
                    } else if (CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())
                            && JadeStringUtil.isEmpty(getMontantMensuel())) {
                        calcul = (new FWCurrency(getMontantAnnuel()).floatValue() * taux) / 100;
                    } else {
                        calcul = (new FWCurrency(getMontantMensuel()).floatValue() * taux) / 100;
                    }

                    if (getModeArrondiFad() == 2) {
                        arrondiCalcul = ""
                                + JANumberFormatter.round(Float.parseFloat(Float.toString(calcul)), 0.05, 2,
                                        JANumberFormatter.INF);
                    } else if (getModeArrondiFad() == 3) {
                        arrondiCalcul = ""
                                + JANumberFormatter.round(Float.parseFloat(Float.toString(calcul)), 0.05, 2,
                                        JANumberFormatter.SUP);
                    } else {
                        arrondiCalcul = ""
                                + JANumberFormatter.round(Float.parseFloat(Float.toString(calcul)), 0.05, 2,
                                        JANumberFormatter.NEAR);
                    }
                    // Création des cotisations dans CPCOTIP
                    // Si Remise => prendre directement la coti annuelle * le
                    // taux sauf si c'est la cotisation minimale
                    // Les FAD lorsque c'est la coti minimale doivent être les
                    // mêmes que ceux calculer par une décision.
                    // Ceci est le cas courant sinon c'est autre que la
                    // coti minimale on prend le montant encodé * le taux
                    if (CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())
                            && JadeStringUtil.isEmpty(getMontantMensuel())) {
                        if (Integer.parseInt(getDecision().getAnneeDecision()) != JACalendar.getYear(JACalendar.today()
                                .toString())) {
                            String idRubrique = cotiAf.getAssurance().getRubriqueId();
                            // On prend seulement dans les compteurs des frais si il n'y a pas plus de 1 CHF de
                            // diffèrence
                            // (pb d'arorndi entre ancienne et nouvelle application)
                            float saveCalcul = 0;
                            float saveCompteur = 0;
                            if (!JadeStringUtil.isBlankOrZero(arrondiCalcul)) {
                                saveCalcul = Float.parseFloat(JANumberFormatter.deQuote(arrondiCalcul));
                            }
                            String compteurFad = CPToolBox
                                    .rechMontantFacture(getSession(), getTransaction(), getCompteAnnexe()
                                            .getIdCompteAnnexe(), idRubrique, getDecision().getAnneeDecision());
                            if (!JadeStringUtil.isBlankOrZero(compteurFad)) {
                                saveCompteur = Float.parseFloat(JANumberFormatter.deQuote(compteurFad));
                            }
                            if ((saveCalcul - 1 <= saveCompteur) && (saveCompteur <= saveCalcul + 1)) {
                                arrondiCalcul = Float.toString(saveCompteur);
                            }
                        }
                        addCotisation(process, 0, cotiAf, Float.toString(taux), arrondiCalcul, Boolean.FALSE);
                    } else {
                        addCotisation(process, Float.parseFloat(arrondiCalcul), cotiAf, Float.toString(taux), "",
                                Boolean.FALSE);
                    }
                } else {
                    // PO 2036 et 9407- Recherche et extourne s'il y a un compteur pour la rubrique pour l'année de
                    // décision
                    creationCotisationPourExtrounerCompteurExistant(process, aff, cotiAf,
                            CodeSystem.TYPE_ASS_FRAIS_ADMIN);
                }
            }
        } catch (Exception e) {
            this._addError(process.getTransaction(), e.getMessage() + " " + getDescriptionTiers());
            return;
        }
    }

    /**
     * Calcul des cotisation pour une personne exerçant une activité indépendante Si la variable revenuAFDifferent= true
     * => il ne faut pas prendre la zone revenuAutre pour la détermination du revenu et ne pas générer que la cotiAF
     * celle-ci sera calculée par la méthode calculIndependant() avec codeRevenuAf = 2 Date de création : (25.02.2004
     * 12:58:23)
     * 
     * @param process
     *            BProcess le processus d'exécution
     * @param boolean revenuAFDifferent codeRevenuAf= 0 => calcul normal codeRevenuAf= 1 => calcul avs sans tenir compte
     *        de revenuAutre et ne pas générer la coti AF codeRevenuAf= 2 => calcul uniquement de la coti AF en prenant
     *        en compte uniquement revenuAutre (=revenuAF)
     */
    public void calculIndependant(BProcess process, int codeRevenuAf) {
        float interet = 0;
        float revenuDet = 0;
        float revenuBrut = 0;
        float revenuCi = 0;
        float cotiAnnuelleBrut = 0;
        float cotiMensuelle = 0;
        float revenuMax = 0;
        float revenuMin = 0;
        float franchise = 0;
        float tauxCalcul = 0;
        float montantProrataCiMin = 0;
        CPDonneesCalcul donCalcul = new CPDonneesCalcul();
        CPTableIndependant tInd = new CPTableIndependant();
        CPTableRentier tRentier = new CPTableRentier();
        boolean exerciceSur2Annee = false;
        casProrataRentier = false;
        boolean prorataCi = false;
        boolean perte = false;
        boolean exempte = false;
        boolean isTSE = false;
        Boolean cotiMinimum = Boolean.FALSE;
        int moisDebutDecision = 0;
        int moisFinDecision = 0;
        int saveDureeDecision = 0;
        String cotiAnnuelle = "";
        // Sous contrôle d'exception
        try {
            // Test si affilié assisté, bénéficaire pc etc...
            // => recherche du tiers qui paye (assistance)
            decision.setIdServiceSociale("");
            if (CPToolBox.isAffilieAssiste(getTransaction(), getAffiliation(), decision.getDebutDecision())) {
                String idTiersAssistance = "";
                // Recherche idTiers qui prend en charge les cotisation
                idTiersAssistance = AFParticulariteAffiliation.existeParticulariteOrganesExternesId(getTransaction(),
                        affiliation.getAffiliationId(), decision.getDebutDecision());
                // Recherche de la description du tiers en charge de l'affilié
                if (!JadeStringUtil.isIntegerEmpty(idTiersAssistance)) {
                    decision.setIdServiceSociale(idTiersAssistance);
                }
            }
            // Test si l'affilié est un TSE
            if (decision.getAffiliation().getTypeAffiliation().equalsIgnoreCase(CodeSystem.TYPE_AFFILI_TSE)
                    || decision.getAffiliation().getTypeAffiliation()
                            .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE)) {
                isTSE = true;
            }
            decision.setProrata("0");
            // Exercice sur 2 année doit être pris en compte si nouveau calcul
            // et année de début de l'exercice différente de celle de fin de
            // l'exercice
            int anneeDebutExercice = JACalendar.getYear(getDonneeBase().getDebutExercice1());
            int anneeFinExercice = JACalendar.getYear(getDonneeBase().getFinExercice1());
            if ((anneeDebutExercice != anneeFinExercice) && decision.getTaxation().equalsIgnoreCase("N")
                    && decision.getDebutActivite().equals(new Boolean(true))) {
                exerciceSur2Annee = true;
                decision.setProrata("1");
            }
            String saveDateFinDecision = decision.getFinDecision();
            // Recherche du revenuCI minimum
            float revenuCiMin = CPTableIndependant.getRevenuCiMin(getTransaction(), decision.getDebutDecision());
            // Calcul et sauvegarde du calcul des intérêts du capital
            interet = donCalcul.calculInteretCapital(process, decision, getDonneeBase());
            // Détermination et sauvegarde du revenu brut
            revenuBrut = determinationRevenuBrutIndependant(process, codeRevenuAf);
            revenuBrut = revenuBrut - interet;

            float cotisation1 = translateMontantCotisation(getDonneeBase().getCotisation1());

            float cotisation2 = translateMontantCotisation(getDonneeBase().getCotisation2());

            // Détermination si activité accessoire
            exempte = determinationSiActiviteAccessoire(process, codeRevenuAf, revenuBrut, donCalcul,
                    exerciceSur2Annee, exempte, isTSE, cotisation1, cotisation2);

            dateAvs = tiers.getDateAvs();
            anneeAvs = JACalendar.getYear(dateAvs);
            if (exerciceSur2Annee || CPDecision.CS_RENTIER.equalsIgnoreCase(decision.getGenreAffilie())) {
                if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDonneeBase().getFinExercice1(),
                        dateAvs)) {
                    // Détermination du montant de la franchise pour rentier
                    franchise = getFranchise(process, exerciceSur2Annee);
                    // sauvegarde du montant de la franchise
                    if ((codeRevenuAf != 2) && (franchise != 0)) {
                        donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_FRANCHISE,
                                franchise);
                    }
                }
            }
            revenuBrut = revenuBrut - franchise;
            if (codeRevenuAf != 2) {
                donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_REV_SANS_COTISATION,
                        revenuBrut);
            }

            // Détermination du revenu brut moyen (si la taxation est basée sur
            // 2 années)
            if (decision.getAnneePrise().equalsIgnoreCase("T")) {
                // Sauvegarde du revenu brut moyen
                revenuBrut = revenuBrut + franchise;
                revenuBrut = revenuBrut / 2;
                if (codeRevenuAf != 2) {
                    donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_REV_MOYEN,
                            revenuBrut);
                }
            }

            // Test si année de radiation = année de décision et motif de fin = départ à l'étranger ou décès
            String motifFin = "";
            String anneeRadiation = "";
            if (cotiAf == null) {
                motifFin = affiliation.getMotifFin();
                anneeRadiation = Integer.toString(JACalendar.getYear(affiliation.getDateFin()));
            } else {
                motifFin = cotiAf.getMotifFin();
                anneeRadiation = Integer.toString(JACalendar.getYear(cotiAf.getDateFin()));
            }
            boolean isDepartEtrangerOuDeces = false;
            if ((anneeRadiation.equalsIgnoreCase(decision.getAnneeDecision()) && (CodeSystem.MOTIF_FIN_DECES
                    .equalsIgnoreCase(motifFin) || CodeSystem.MOTIF_FIN_A_ETRANGER.equalsIgnoreCase(motifFin)))
                    || affiliation.getMotifCreation().equalsIgnoreCase(CodeSystem.MOTIF_AFFIL_NON_ACTIF_AL_ETRANGER)) {
                isDepartEtrangerOuDeces = true;
            }
            // Calcul du revenu déterminant: revenu + cotisation
            revenuDet = revenuBrut + cotisation1 + cotisation2;
            // sauvegarde du revenu avant prorata pour la recherche des
            // cotisations pour les cas postnumerando
            float revenuBase = revenuDet;
            // Ramener le revenu au prorata lorsque l'exercice est à cheval sur 2 ans. A faire uniquement pour le mode
            // postnumerando
            revenuDet = handleIncomeForExerciseOn2Years(codeRevenuAf, revenuDet, donCalcul, exerciceSur2Annee);
            // Insertion revenu non arrondi
            if ((revenuDet % 100 != 0) && (codeRevenuAf != 2)) {
                donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_REV_NET_NONARRONDI,
                        revenuDet);
            }
            // Revenu CI = revenuDéterminant arrondi au 100 fr. inférieur
            // Si perte => revenu déterminant = 0
            if (revenuDet < 0) {
                if (exerciceSur2Annee) {
                    perte = true; // Pour cas coti=0 au lieu du minimum
                }
                revenuDet = 0;
                revenuAFVD = 0;
            } else {
                // Pour AF VD, on doit enlever le revenu agricole (revenuAutre) au revenu déterminant
                String varRev = "";
                if (JadeStringUtil.isEmpty(donneeBase.getRevenuAutre1())) {
                    varRev = "0";
                } else {
                    varRev = JANumberFormatter.deQuote(donneeBase.getRevenuAutre1());
                }
                revenuAFVD = revenuDet - Float.parseFloat(varRev);
                // PO 8392
                if (!isTSE || (Integer.parseInt(decision.getAnneeDecision()) < 2012)) { // Inforom 380
                    revenuDet = JANumberFormatter.round(revenuDet, 100, 0, JANumberFormatter.INF);
                }
            }
            // Sauvegarde du revenu net
            if (codeRevenuAf != 2) {
                donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_REV_NET, revenuDet);
            }
            // Recherche du revenu minimum indépendant pour l'année de décision
            revenuMin = CPTableIndependant.getRevenuMin(process.getTransaction(), decision.getDebutDecision());
            revenuCi = revenuDet;
            // Si exercice à cheval sur 2 ans et que c'est son premier exercice et que c'est une perte alors il est
            // exempté
            if (perte && exerciceSur2Annee && decision.getDebutActivite().equals(new Boolean(true))
                    && decision.getAnneeDecision().equalsIgnoreCase(Integer.toString(anneeDebutExercice))) {
                exempte = true;
            }
            /*
             * --------------------------------------------------------------------------- // Détermination des
             * cotisations // --------------------------------------------------------------------------- Pour les
             * rentiers: Si ancien calcul (postnumerando et que son revenu est supérieur au revenu minimum =>
             * cotisationAnnuelle = table des indépendants (selon l'année et le revenu) Si ancien calcul (postnumerando
             * et que son revenu est inférieur au revenu minimum => table des rentiers (barème dégréssif selon l'année
             * et le revenu) Si nouveau calcul (praenumerando et que son revenu est supérieur au revenu minimum =>
             * cotisationAnnuelle = table des indépendants (selon l'année et le revenu) Si ancien calcul (praenumerando
             * et que son revenu est inférieur au revenu minimum et qu'il n'est pas rentier pendant la période décision
             * => cotisationAnnuelle = 0; Si ancien calcul (praenumerando et que son revenu est inférieur au revenu
             * minimum et qu'il est rentier pendant la période décision => cotisationAnnuelle = prorata de la cotisation
             * jusqu'à l'âge avs.
             */
            String revenuDeterminant = "";
            // La base de recherche du revenu différencie entre le mode de
            // taxation postnumerando et preanumerando
            if (exerciceSur2Annee) {
                revenuDeterminant = Float.toString(revenuBase);
            } else {
                revenuDeterminant = Float.toString(revenuDet);
            }

            baremeDegressif = AFParticulariteAffiliation.existeParticularite(getTransaction(),
                    decision.getIdAffiliation(), AFParticulariteAffiliation.CS_BAREME_DEGRESSIF,
                    decision.getDebutDecision())
                    && (revenuMin >= Float.parseFloat(revenuDeterminant));

            // Nouvelle directive au 01.01.2012- Prendre le taux max pour les TSE
            if (isTSE && (Integer.parseInt(decision.getAnneeDecision()) >= 2012)) {
                AFCotisation cotiAf = getAffiliation()._cotisation(process.getTransaction(),
                        decision.getIdAffiliation(), CodeSystem.GENRE_ASS_PERSONNEL,
                        CodeSystem.TYPE_ASS_COTISATION_AVS_AI, decision.getDebutDecision(), decision.getFinDecision(),
                        1);
                if (cotiAf != null) {
                    // Recherche du taux
                    try {
                        tauxCalcul = Float.parseFloat(JANumberFormatter.deQuote(cotiAf.getTaux(
                                decision.getDebutDecision(), "0")));
                    } catch (Exception e) {
                        tauxCalcul = 0; // BTC bug affiliation!!! 15.12.2003
                    }
                }
            }
            // Si exempte => revenu et coti = 0
            if (exempte) {
                cotiAnnuelleBrut = 0;
                revenuCi = 0;
            }
            // Si rentier ou personne ayant déjà payé la coti minimum en tant
            // que salarié et que son revenu
            // est inférieur au revenu minimum
            else if (((tauxCalcul == 0) && ((CPDecision.CS_RENTIER.equalsIgnoreCase(decision.getGenreAffilie())) && (revenuMin > Float
                    .parseFloat(revenuDeterminant))))
                    || (Boolean.TRUE.equals(decision.getCotiMinimumPayeEnSalarie()) && (revenuMin > Float
                            .parseFloat(revenuDeterminant))) || baremeDegressif) {
                // Cotisation pour rentier - Barème dégressif
                CPTableRentierManager tRentierManager = findCotisationRentier(revenuDet);
                if (tRentierManager.size() == 0) {
                    this._addError(
                            process.getTransaction(),
                            "cotisation avs non trouvée: " + decision.getAnneeDecision() + "  "
                                    + Float.toString(revenuCi) + " " + getDescriptionTiers());
                    return;
                } else {
                    tRentier = ((CPTableRentier) tRentierManager.getEntity(0));
                    // Calcul de la cotisation annuelle
                    cotiAnnuelleBrut = Float.parseFloat(JANumberFormatter.deQuote(tRentier.getCotisationAnnuelle()));
                    cotiAnnuelle = Float.toString(cotiAnnuelleBrut);
                    /*
                     * Si l'affilié est à l'avs pendant l'année de décision, il doit au moins payer la cotisation
                     * minimum (jusqu'à l'âge avs) Dans ce cas le revenu CI est égal au revenu CI minimum (également au
                     * prorata jusqu'à l'âge avs
                     */
                    String dateDebut = decision.getDebutDecision();
                    if (Boolean.FALSE.equals(decision.getCotiMinimumPayeEnSalarie()) && !isDepartEtrangerOuDeces) {
                        dateDebut = "01.01." + decision.getAnneeDecision();
                    }
                    float montantProrataCoti = Float.parseFloat(CPToolBox.getMontantProRata(dateDebut,
                            decision.getFinDecision(), cotiAnnuelleBrut));
                    montantProrataCiMin = Float.parseFloat(CPToolBox.getMontantProRata(dateDebut, dateAvs,
                            getCotiIndMinimum()));
                    if ((Integer.parseInt(decision.getAnneeDecision()) == anneeAvs)
                            && BSessionUtil.compareDateFirstLower(getSession(), decision.getDebutDecision(), dateAvs)
                            // PO 8314
                            && decision.getTaxation().equalsIgnoreCase("N")
                            && (montantProrataCoti < montantProrataCiMin)
                            && Boolean.FALSE.equals(decision.getCotiMinimumPayeEnSalarie())) {
                        cotiMinimum = Boolean.TRUE;
                        cotiAnnuelle = "";
                        if (CPDecision.CS_RENTIER.equalsIgnoreCase(decision.getGenreAffilie())) {
                            cotiAnnuelleBrut = montantProrataCiMin;
                            revenuCi = revenuCiMin;
                            if (JACalendar.getMonth(dateAvs) != 12) {
                                casProrataRentier = true;
                            }
                        }
                    }
                }
            }
            // Sinon test pour cotisation minimum
            else if ((!isTSE && ((revenuMin > revenuDet) && !exerciceSur2Annee))
                    || (isTSE && (Integer.parseInt(decision.getAnneeDecision()) < 2012) && ((revenuMin > revenuDet) && !exerciceSur2Annee))
                    || (((revenuMin > revenuDet) && perte))
                    || ((revenuMin > revenuDet) && exerciceSur2Annee && decision.getAnneeDecision().equalsIgnoreCase(
                            Integer.toString(anneeFinExercice)))
                    || ((revenuMin > revenuDet) && decision.getPremiereAssurance().equals(new Boolean(true)) && decision
                            .getTaxation().equalsIgnoreCase("N"))) { // Ex: 4
                // non rentier
                revenuCi = revenuCiMin;
                cotiAnnuelleBrut = getCotiIndMinimum();
                cotiMinimum = Boolean.TRUE;
                cotiAnnuelle = Float.toString(cotiAnnuelleBrut);
            }
            // sinon cotisation indépendant selon tabelle
            else {
                String anneeRevenu = "";
                if (decision.getTaxation().equalsIgnoreCase("A")) {
                    anneeRevenu = decision.getAnneeDecision();
                } else {
                    anneeRevenu = Integer.toString(anneeFinExercice);
                }
                // Recherche taux dans la table des indépendant qui permet de
                // déterminer la cotisation pour ce calcul
                // Nouvelle directive au 01.01.2012- Prendre le taux max pour les TSE
                if (tauxCalcul == 0) {
                    tInd = returnTableIndependant(process, anneeRevenu, revenuDeterminant);
                    if (tInd == null) {
                        this._addError(getTransaction(), getSession().getLabel("CP_MSG_0110") + " "
                                + getDescriptionTiers());
                        return;
                    }
                    // Calcul de la cotisation annuelle
                    if (JadeStringUtil.isEmpty(tInd.getTaux())) {
                        tauxCalcul = 0;
                    } else {
                        tauxCalcul = Float.parseFloat(JANumberFormatter.deQuote(tInd.getTaux()));
                    }
                }
                cotiAnnuelleBrut = (revenuCi * tauxCalcul) / 100;
                cotiAnnuelleBrut = JANumberFormatter.round(cotiAnnuelleBrut, 0.1, 2, JANumberFormatter.INF);
                // Sauvegarde du taux
                if (codeRevenuAf != 2) {
                    donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_TAUX_COTISATION,
                            tauxCalcul);
                }
            }
            // Recherche du revenu maximum indépendant pour l'année de décision
            revenuMax = tInd.getRevenuMax(process, decision.getAnneeDecision());
            // Détermination si revenu CI et cotisation sont au prorata
            moisDebutDecision = JACalendar.getMonth(decision.getDebutDecision());
            moisFinDecision = JACalendar.getMonth(decision.getFinDecision());
            boolean periodeIncomplete = false;
            if ((moisDebutDecision != 01) || (moisFinDecision != 12)) {
                periodeIncomplete = true;
            }
            if (!perte) {
                if (((revenuMin > revenuDet) && decision.getPremiereAssurance().equals(new Boolean(true))
                        && decision.getTaxation().equalsIgnoreCase("N") && periodeIncomplete)
                        || ((revenuMin > revenuDet) && !isDepartEtrangerOuDeces
                                && (2004 <= Integer.parseInt(decision.getAnneeDecision())) && periodeIncomplete)
                        || casProrataRentier
                        || (decision.getTaxation().equalsIgnoreCase("A") && ((moisDebutDecision != 01) || (moisFinDecision != 12)))
                        || ((revenuMin > revenuDet) && (anneeAvs == Integer.parseInt(decision.getAnneeDecision())) && periodeIncomplete) // I100305_000019
                        || (Integer.parseInt(decision.getNombreMoisTotalDecision()) > 0)) {
                    prorataCi = true;
                }
            }
            // Si rentier pendant l'année de décision et cotisation minimum et
            // calcul praenumerando
            // => période de décision dure jusqu'à lâge AVS
            if (casProrataRentier) {
                // moisFinDecision = JACalendar.getMonth(dateAvs);
                decision.setFinDecision(dateAvs);
            }
            // Si auparavant l'affilié n'avait pas versé le minimum et qui'il a un revenu inférieur au revenu minimum
            // => doit payer la coti mminimum depuis le début de l'année
            int saveMoisDebutDecision = moisDebutDecision;
            /*
             * Calcul de cotisation mensuelle : Pour les cas ou l'affilié doit payer la cotisation annuelle, il faut
             * ramener la coti mensuelle par rapport à la durée de la décision (EX: s'il doit payer 1200/an mais que la
             * décision dure 3 mois => la coti mensuelle = 1200/3 = 400 Fr.
             */
            int dureeDecision = (moisFinDecision - moisDebutDecision) + 1;
            // Coti. annuelle théorique
            FWCurrency varCurrency = new FWCurrency();
            // Coti. annuelle théorique
            // Modif le 13.11.2007 - CICICAM cas 198.1062 pour 2007 (pb coti.
            // mensuelle doublement proratisée)
            if (casProrataRentier && (cotiAnnuelleBrut == montantProrataCiMin)) {
                varCurrency = new FWCurrency(cotiAnnuelleBrut / dureeDecision);
            } else {
                varCurrency = new FWCurrency(cotiAnnuelleBrut / 12);
            }
            cotiMensuelle = varCurrency.floatValue();
            // La Coti annuelle minimum 2007 ne suit pas la même logique pour
            // l'arrondi que les autre année #@@¬§§@
            if (revenuCi > revenuMax) {
                cotiMensuelle = JANumberFormatter.round(cotiMensuelle, 0.1, 2, JANumberFormatter.NEAR);
            } else {
                cotiMensuelle = JANumberFormatter.round(cotiMensuelle, 0.1, 2, JANumberFormatter.INF);
            }
            // Recalcul de la coti. annuelle sur la base de la coti. mensuelle
            // arrondi
            String cotiAnnuelleTheorique = CPToolBox.multString(Float.toString(cotiMensuelle), "12");
            // Calcul de la coti. annuelle selon le cas
            if (prorataCi) {
                // Cas spécial remtier en cours d'année avec fin d'activité avant sa retraite
                // Demande de CICICAM 9GQ38865 du 11.06.2007 - Affilié 810.2539
                if (CPDecision.CS_RENTIER.equalsIgnoreCase(decision.getGenreAffilie())
                        && BSessionUtil.compareDateFirstLower(getSession(), getDonneeBase().getFinExercice1(), dateAvs)) {
                    varCurrency = new FWCurrency(cotiAnnuelleBrut
                            / ((JACalendar.getMonth(saveDateFinDecision) - moisDebutDecision) + 1));
                } else if (!JadeStringUtil.isBlankOrZero(getDecision().getNombreMoisTotalDecision())) {
                    varCurrency = new FWCurrency(cotiAnnuelleBrut
                            / Integer.parseInt(getDecision().getNombreMoisTotalDecision()));
                } else if (casProrataRentier && (cotiAnnuelleBrut == montantProrataCiMin)) {
                    varCurrency = new FWCurrency(cotiAnnuelleBrut / dureeDecision);
                } else {
                    varCurrency = new FWCurrency(cotiAnnuelleBrut / 12);
                }
            } else {
                varCurrency = new FWCurrency(cotiAnnuelleBrut / dureeDecision);
            }
            cotiMensuelle = varCurrency.floatValue();
            // Au dessus du revenu max et pour la cotisation minimum l'arrondi
            // se fait au 10 centimes les plus proches sinon infèrieure
            if ((revenuCi > revenuMax) || (cotiAnnuelleBrut == getCotiIndMinimum())) {
                cotiMensuelle = JANumberFormatter.round(cotiMensuelle, 0.1, 2, JANumberFormatter.NEAR);
            } else {
                cotiMensuelle = JANumberFormatter.round(cotiMensuelle, 0.1, 2, JANumberFormatter.INF);
            }
            // Recalcul de la coti. annuelle sur la base de la coti. mensuelle
            // arrondi
            varCurrency = new FWCurrency(cotiMensuelle * dureeDecision);
            if (prorataCi) {
                cotiAnnuelle = varCurrency.toString();
            } else {
                cotiAnnuelle = cotiAnnuelleTheorique;
            }
            /*
             * PO 9138 if ((prorataCi && (cotiAnnuelleBrut != this.getCotiIndMinimum()) && this.decision.getTaxation()
             * .equalsIgnoreCase("N")) || ((revenuMin > revenuDet) && exerciceSur2Annee &&
             * this.decision.getAnneeDecision() .equalsIgnoreCase(Integer.toString(anneeFinExercice))) || (!prorataCi &&
             * (cotiAnnuelleBrut == this.getCotiIndMinimum()))) {
             */
            if (((revenuMin > revenuDet) && exerciceSur2Annee && decision.getAnneeDecision().equalsIgnoreCase(
                    Integer.toString(anneeFinExercice)))
                    || (!prorataCi && (cotiAnnuelleBrut == getCotiIndMinimum()))) {
                if (Integer.parseInt(decision.getNombreMoisTotalDecision()) == 0) {
                    cotiAnnuelle = Float.toString(cotiAnnuelleBrut);
                } else {
                    cotiAnnuelle = "";
                }
            } else if ((moisDebutDecision == 01) && (moisFinDecision == 12)) {
                cotiAnnuelle = "";
            }
            if (prorataCi || casProrataRentier) {
                if (casProrataRentier) {
                    moisFinDecision = JACalendar.getMonth(dateAvs);
                    if (Boolean.TRUE.equals(cotiMinimum)
                            && Boolean.FALSE.equals(decision.getCotiMinimumPayeEnSalarie()) && !isDepartEtrangerOuDeces) {
                        moisDebutDecision = 1;
                    }
                    dureeDecision = (moisFinDecision - moisDebutDecision) + 1;
                }
                varCurrency = new FWCurrency(revenuCi);
                revenuCi = varCurrency.floatValue();
                if (!JadeStringUtil.isBlankOrZero(decision.getNombreMoisTotalDecision())) {
                    revenuCi = JANumberFormatter.round(
                            revenuCi / Integer.parseInt(decision.getNombreMoisTotalDecision()), 1, 0,
                            JANumberFormatter.NEAR);
                } else {
                    revenuCi = JANumberFormatter.round(revenuCi / 12, 1, 0, JANumberFormatter.NEAR);
                }
                revenuCi = revenuCi * dureeDecision;
                if ("0".equalsIgnoreCase(decision.getProrata())) {
                    decision.setProrata("2");
                } else {
                    decision.setProrata("3");
                }
            }
            moisDebutDecision = saveMoisDebutDecision;
            // Restauration de la date de fin de décision initiale car elle a pu être changée pour les besoins du calcul
            decision.setFinDecision(saveDateFinDecision);
            saveDureeDecision = dureeDecision;
            // AVS payée à une autre caisse -> pas de création de la cotisation AVS + ne pas stocker le revenu CI
            if ((getCotiAf() != null)
                    && getCotiAf().getAssurance().getTypeAssurance()
                            .equalsIgnoreCase(CodeSystem.TYPE_ASS_COTISATION_AVS_AI)
                    && !cotiAf.getDateDebut().equalsIgnoreCase(cotiAf.getDateFin())) {
                if (codeRevenuAf != 2) {
                    if (cotiMinimum) { // PO 6345
                        tauxCalcul = 0;
                    }
                    addCotisation(process, cotiMensuelle, cotiAf, Float.toString(tauxCalcul), cotiAnnuelle, cotiMinimum);
                    // PO 6758 - if (CPDecision.CS_FRANCHISE.equalsIgnoreCase(this.decision.getSpecification())
                    // && (this.decision.getFacturation().equals(Boolean.FALSE) || CPDecision.CS_ACOMPTE
                    // .equalsIgnoreCase(this.decision.getTypeDecision()))) {
                    if (CPDecision.CS_FRANCHISE.equalsIgnoreCase(decision.getSpecification())) {
                        revenuCi = 0;
                    }
                    donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_REV_CI, revenuCi);
                }
                if ((codeRevenuAf != 2) && !CPDecision.CS_REDUCTION.equalsIgnoreCase(decision.getTypeDecision())
                        && !CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())
                        && !CPDecision.CS_FRANCHISE.equalsIgnoreCase(decision.getSpecification())) {
                    // Lettre pour couple
                    determinationLettreCouple(process);
                }
            } else {
                setMontantMensuel(Float.toString(cotiMensuelle));
            }
            // Frais administratif
            if (codeRevenuAf != 2) {
                calculFraisAdministration(this);
                // AFI
                calculAFI(process, revenuCi, revenuDeterminant, revenuDet, revenuMin, revenuMax, prorataCi,
                        casProrataRentier, perte, exerciceSur2Annee, exempte);
                // Assurance chômage
                calculAssuranceChomage(this, exempte, saveDureeDecision);
            }
            if (codeRevenuAf != 1) {
                // Calcul AF
                calculAF(process, revenuDet, exempte, saveDureeDecision, cotiMinimum, CodeSystem.TYPE_ASS_COTISATION_AF);
                // Calcul PC famille
                calculAF(process, revenuDet, exempte, saveDureeDecision, cotiMinimum, CodeSystem.TYPE_ASS_PC_FAMILLE);
            }
            if (codeRevenuAf != 2) {
                // Calcul CPS général
                calculCPSGeneral(process, revenuDet, exempte, saveDureeDecision);
                // Calcul CPS autre
                calculCPSAutre(process, revenuDet, exempte, saveDureeDecision);
                // Calcul Lamat
                calculLamat(process, revenuDet, exempte);
            }
            calculCotisationFederative(saveDureeDecision);
        } catch (Exception e) {
            this._addError(process.getTransaction(), e.getMessage() + " " + getDescriptionTiers());
            return;
        }
    }

    public float translateMontantCotisation(String mCotisation) {
        float cotisation = 0;
        if (!JadeStringUtil.isBlank(mCotisation)) {
            cotisation = Float.parseFloat(JANumberFormatter.deQuote(mCotisation));
        }
        return cotisation;
    }

    public boolean determinationSiActiviteAccessoire(BProcess process, int codeRevenuAf, float revenuBrut,
            CPDonneesCalcul donCalcul, boolean exerciceSur2Annee, boolean exempte, boolean isTSE, float cotisation1,
            float cotisation2) throws JAException, Exception {
        float montantActiviteAccessoire = revenuBrut;
        montantActiviteAccessoire = montantActiviteAccessoire + cotisation1 + cotisation2;

        if (exerciceSur2Annee) {
            int moisDebut = JACalendar.getMonth(getDonneeBase().getDebutExercice1());
            int moisFin = JACalendar.getMonth(getDonneeBase().getFinExercice1());
            moisFin = moisFin + 12;
            int nbMois = (moisFin - moisDebut) + 1;

            montantActiviteAccessoire = (revenuBrut * 12) / nbMois;
            montantActiviteAccessoire = JANumberFormatter
                    .round(montantActiviteAccessoire, 1, 0, JANumberFormatter.NEAR);
        }
        // Recherche montant maximum pour activité accessoire
        float revenuActviteAccessoire = Float.parseFloat(FWFindParameter.findParameter(process.getTransaction(),
                "10500130", "REVACTACC", decision.getDebutDecision(), "", 0));
        if (JANumberFormatter.round(montantActiviteAccessoire, 100, 0, JANumberFormatter.INF) <= revenuActviteAccessoire) {
            // Inforom 380 - Pour les TSE, pas de coti si revenu < revenu accessoire sauf si il a la particularité
            // "Forcer calcul"
            if (isTSE
                    && (Integer.parseInt(decision.getAnneeDecision()) >= 2012)
                    && !AFParticulariteAffiliation.existeParticularite(getTransaction(), decision.getIdAffiliation(),
                            AFParticulariteAffiliation.CS_FORCER_CALCUL_TSE, decision.getDebutDecision())) {
                exempte = true;
            }
            // Recherche si activité accessoire
            else if (AFParticulariteAffiliation.existeParticularite(getTransaction(), decision.getIdAffiliation(),
                    AFParticulariteAffiliation.CS_ACTIVITE_ACCESSOIRE, decision.getDebutDecision())) {
                exempte = true;
                // Sauvegarde info activité accesoire dans les données du
                // calcul afin de
                // générer une remarque sur la décision
                if (codeRevenuAf != 2) {
                    donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_ACT_ACCESSOIRE, 0);
                }
            }
        }
        return exempte;
    }

    /**
     * Calcul des cotisations LAMAT Date de création : (25.02.2002 12:58:23)
     * 
     * @param process
     *            BProcess le processus d'exécution
     * @param revenuCI
     */
    public void calculLamat(BProcess process, float revenuCi, boolean exempte) {
        // Sous contrôle d'exception
        float taux = 0;
        float cotiMensuelle = 0;
        try {
            if (!process.getTransaction().hasErrors()) {
                AFAffiliation aff = new AFAffiliation();
                aff.setSession(process.getSession());
                AFCotisation cotiAf = aff._cotisation(process.getTransaction(), decision.getIdAffiliation(),
                        CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_MATERNITE, decision.getDebutDecision(),
                        decision.getFinDecision(), 1);
                if ((cotiAf != null) && !cotiAf.getDateDebut().equalsIgnoreCase(cotiAf.getDateFin())) {
                    if (!exempte) {
                        // Prendre la date la plus récente entre l'assurance et
                        // la décision si l'année de coti = l'année de décision
                        // sinon prendre la date de décision
                        String dateRef = decision.getDebutDecision();
                        if ((Integer.parseInt(decision.getAnneeDecision()) == JACalendar.getYear(cotiAf.getDateDebut()))
                                && BSessionUtil.compareDateFirstGreater(getSession(), cotiAf.getDateDebut(),
                                        decision.getDebutDecision())) {
                            dateRef = cotiAf.getDateDebut();
                        }
                        try {
                            taux = Float.parseFloat(JANumberFormatter.deQuote(cotiAf.getTaux(dateRef,
                                    JANumberFormatter.deQuote(Float.toString(revenuCi)))));

                        } catch (Exception e) {
                            taux = 0; // BTC bug affiliation!!! 15.12.2003
                        }
                        float cotiAnnuelleBrut = (revenuCi * taux) / 100;

                        // Calcul de la duree de cotisation
                        int dureeCotisation = calculDureeCotisation(cotiAf, decision, false);
                        dureeCotisation = calculDureeCotisationSelonCasMetier(0, dureeCotisation);
                        // Calcul de cotisation mensuelle - arrondi au 5 cts
                        cotiMensuelle = cotiAnnuelleBrut / dureeCotisation;
                        cotiMensuelle = JANumberFormatter.round(cotiMensuelle, 0.05, 2, JANumberFormatter.NEAR);
                    }
                    // Création des cotisations dans CPCOTIP
                    addCotisation(process, cotiMensuelle, cotiAf, Float.toString(taux), "", Boolean.FALSE);
                } else {
                    // PO 2036 et 9407- Recherche et extourne s'il y a un compteur pour la rubrique pour l'année de
                    // décision
                    creationCotisationPourExtrounerCompteurExistant(process, aff, cotiAf, CodeSystem.TYPE_ASS_MATERNITE);
                }
            }
        } catch (Exception e) {
            this._addError(process.getTransaction(), e.getMessage() + " " + getDescriptionTiers());
            return;
        }
    }

    protected float calculMontantCotisationMensuelle(int saveDureeDecision, float taux, AFCotisation cotiAf,
            String revenuNet) throws Exception, NumberFormatException {
        float cotiMensuelle;
        int dureeCotisation = calculDureeCotisation(cotiAf, decision, false);
        dureeCotisation = calculDureeCotisationSelonCasMetier(saveDureeDecision, dureeCotisation);
        float cotiAnnuelleBrut = (new FWCurrency(revenuNet).floatValue() * taux) / 100;
        cotiMensuelle = cotiAnnuelleBrut / dureeCotisation;
        cotiMensuelle = JANumberFormatter.round(cotiMensuelle, 0.05, 2, JANumberFormatter.NEAR);
        return cotiMensuelle;
    }

    /**
     * Calcul des cotisation pour un non actif Date de création : (25.02.2002 12:58:23)
     * 
     * @param process
     *            BProcess le processus d'exécution
     */
    public void calculNonActif(BProcess process) {
        float fortuneTotal = 0;
        float fortune1 = 0;
        float fortune2 = 0;
        float fortune3 = 0;
        float fortune4 = 0;
        float autreFortune = 0;
        float dette = 0;

        float fortuneDet = 0;
        float revenuBrut = 0;
        Boolean cotiMinimum = Boolean.FALSE;
        boolean prorata = false;

        // Sous contrôle d'exception
        try {
            CPDonneesCalcul donCalcul = new CPDonneesCalcul();
            // Test si affilié assisté, bénéficaire pc etc...
            determineAffilieAssiste();
            // Rechercher le revenu NA minimum
            initMontantMinimum();
            // Détermination de la période de décision
            int dureeDecision = JACalendar.getMonth(decision.getFinDecision())
                    - JACalendar.getMonth(decision.getDebutDecision()) + 1;
            // Si nombre de mois total de décision n'est pas égal à zéro =>
            // prendre ce nombre de mois
            // pour la détermination du revenu (Ex des cas venant d'un autre
            // canton)
            // sinon prendre la durée de la décision
            int dureeRevenu = Integer.parseInt(decision.getNombreMoisTotalDecision());
            if (dureeRevenu == 0) {
                dureeRevenu = dureeDecision;
            }
            // Détermination de la fortune - Test si fortune détaillée (ex: CFC)
            fortuneTotal = calculFortuneTotal(process, fortuneTotal, fortune1, fortune2, fortune3, fortune4,
                    autreFortune, dette);
            // Détermination du revenu
            revenuBrut = calculRevenuBrutEtPeriodePourNonActif(revenuBrut, donCalcul);
            // Détermination du revenu brut moyen (si la taxation est basée sur
            // 2 années)
            revenuBrut = calculAndSaveRevenuMoyen(revenuBrut, donCalcul);
            // Revenu * 20 + sauvegarde
            float revenu20 = calculAndSaveRevenu20(revenuBrut, donCalcul);
            // Fortune déterminante
            fortuneDet = calculAndSaveFortuneTotale(fortuneTotal, donCalcul, revenu20);
            // Annualisation de la fortune déterminante + arrondi au 50'000 fr.
            // inf.
            fortuneDet = calculAndSaveFortuneArrondie(fortuneDet, donCalcul);
            // ---------------------------------------------------------------------------
            // Détermination des cotisations et du revenu CI
            // ---------------------------------------------------------------------------
            // Recherche cotisation annuelle
            CPTableNonActifManager tNacManager = findCotisationNac(fortuneDet);
            if (tNacManager.size() == 0) {
                this._addError(process.getTransaction(), "cotisation non trouvée: " + decision.getAnneeDecision()
                        + "  " + Float.toString(fortuneDet) + " " + getDescriptionTiers());
                return;
            } else {
                if (cotiAf != null) {
                    CPTableNonActif tNac = ((CPTableNonActif) tNacManager.getEntity(0));
                    setMontantAnnuel(tNac.getCotisationAnnuelle());
                    // Test si cotisation minimum
                    if (Float.parseFloat(JANumberFormatter.deQuote(getMontantAnnuel())) == getCotiNacMinimum()) {
                        cotiMinimum = Boolean.TRUE;
                    }
                    // Dès le 01.01.2004: nouveau calcul: si revenu inférieur ou
                    // égal à 300'000.- et que la durée de
                    // cotisation inférieure à 12 mois: proratiser les montants
                    if (dureeDecision != 12) { // Mandat 556
                        prorata = true;
                    }
                    // Détermination de la la cotisation mensuelle
                    float cotiAnnuelle = Float.parseFloat(JANumberFormatter.deQuote(tNac.getCotisationAnnuelle()));
                    float cotiMensuelle = Float.parseFloat(JANumberFormatter.deQuote(tNac.getCotisationMensuelle()));
                    FWCurrency varCurrency = new FWCurrency();
                    if (prorata) {
                        varCurrency = new FWCurrency(cotiMensuelle * dureeDecision);
                        cotiAnnuelle = varCurrency.floatValue();
                    }
                    // Tester si AVS existante (payée à un autre caisse) -> ne pas créer la cotisation
                    if ((cotiAf != null) && !cotiAf.getDateDebut().equalsIgnoreCase(cotiAf.getDateFin())) {
                        // Création des cotisation d'indépendant
                        String cotiAnnuelleString = Float.toString(cotiAnnuelle);
                        if (cotiAnnuelle == 0) {
                            cotiAnnuelleString = "";
                        }
                        addCotisation(process, cotiMensuelle, cotiAf, "0", cotiAnnuelleString, cotiMinimum);
                        // Si la cotisation salarié est supérieur à 0, il faut
                        // déduire de la cotisation à payer
                        // ce montant de cotisation salarié
                        handleSalarieDispense(cotiMinimum, getMontantAnnuel(), prorata, cotiAnnuelle);
                    }
                    // Calcul et sauvegarde du montant CI
                    float revenuCi = calculAndSaveRevenuCI(donCalcul, dureeDecision, tNac, prorata);
                    // Frais administratif
                    calculFraisAdministration(this);
                    // Calcul AF
                    calculAF(process, revenuCi, false, 0, cotiMinimum, CodeSystem.TYPE_ASS_COTISATION_AF);
                    // Calcul PC famille
                    calculAF(process, revenuCi, false, 0, cotiMinimum, CodeSystem.TYPE_ASS_PC_FAMILLE);
                    // Calcul CPS général
                    calculCPSGeneral(process, revenuCi, false, 0);
                    // Calcul CPS autre
                    calculCPSAutre(process, revenuCi, false, 0);
                    // Calcul Lamat
                    calculLamat(process, revenuCi, false);
                    // Calcul AC2
                    calculAssuranceChomage(process, false, 0);
                    // Remboursement AFI
                    remboursementAFI(process);
                } else {
                    this._addError(process.getTransaction(), " cotisation de l'affiliation non retrouvée "
                            + getDescriptionTiers());
                }
            }
        } catch (Exception e) {
            this._addError(process.getTransaction(), e.getMessage() + " " + getDescriptionTiers());
            return;
        }
    }

    /**
     * Calcul des remises Date de création : (14.04.2008 12:58:23)
     * 
     * @param process
     *            BProcess le processus d'exécution
     */
    public void calculRemise(BProcess process) {
        // Sous contrôle d'exception
        try {
            if (!JadeStringUtil.isBlank(getDonneeBase().getCotisation1())) {
                addCotisation(process, 0, cotiAf, "0", getDonneeBase().getCotisation1(), Boolean.FALSE);
                float cotiEncode = Float.parseFloat(JANumberFormatter.deQuote(getDonneeBase().getCotisation1()));
                // Si Remise => prendre directement la coti annuelle * le taux
                // sauf si c'est la cotisation minimale
                // Les FAD lorsque c'est la coti minimale doivent être les mêmes
                // que ceux calculer par une décision.
                // Ceci est le cas courant sinon c'est c'est autre que la coti
                // minimale on prend le montant encodé * le taux
                FWCurrency varCurrency = new FWCurrency();
                varCurrency = new FWCurrency(cotiEncode / 12);
                float cotiMensuelle = varCurrency.floatValue();
                // Au dessus du revenu max et pour la cotisation minimum
                // l'arrondi se fait au 10 centimes les plus proches sinon
                // infèrieure
                if (cotiEncode == getCotiIndMinimum()) {
                    cotiMensuelle = JANumberFormatter.round(cotiMensuelle, 0.1, 2, JANumberFormatter.NEAR);
                    setMontantMensuel(Float.toString(cotiMensuelle));
                } else {
                    setMontantMensuel("");
                }
                calculFraisAdministration(this);
            }
        } catch (Exception e) {
            this._addError(process.getTransaction(), e.getMessage() + " " + getDescriptionTiers());
            return;
        }
    }

    /**
     * Détermination du revenu brut et de la duréée d'exercice
     * 
     * @param revenuBrut
     * @return
     * @throws NumberFormatException
     */
    protected float calculRevenuBrutEtPeriodePourNonActif(float revenuBrut, CPDonneesCalcul donCalcul) throws Exception {
        // Si revenu basé sur encodage mensuel: (défini dans fichier
        // properties)
        // revenu brut = (revenu1 * nbre de mois) arrondi au franc le plus
        // proche
        // + (revenuAutre1 * nbre de mois) arrondi au franc le plus proche
        // + (si ancien) + (revenu2 * nbre de mois) arrondi au franc le plus
        // proche
        // + (revenuAutre2 * nbre de mois) arrondi au franc le plus proche
        // et nombre de mois de l'exercie = nombre de mois de la décision
        // si nombre de mois = vide ou zéro => cas particulier d'un montant
        // annuel
        // Si revenu basé sur encodage annuel:
        // revenu brut = (revenu1 * revenu2)
        // et nombre de mois de l'exercie = nombre de mois exercice1 +
        // nombre de mois exercice 2
        float revenu1;
        float montantTotalRenteAVS;
        float revenu2;
        float revenuAutre1;
        float revenuAutre2;
        int nbMois = 12;
        if (!JadeStringUtil.isBlank(getDonneeBase().getRevenu1())) {
            revenu1 = Float.parseFloat(JANumberFormatter.deQuote(getDonneeBase().getRevenu1()));
            if (!JadeStringUtil.isIntegerEmpty(getDonneeBase().getNbMoisExercice1())) {
                nbMois = Integer.parseInt(getDonneeBase().getNbMoisExercice1());
            }
            FWCurrency varTemp = new FWCurrency(revenu1);
            double montant = 0;
            montant = (varTemp.doubleValue() / nbMois) * 12;
            revenu1 = (float) JANumberFormatter.round(montant, 1, 0, JANumberFormatter.SUP);
            revenuBrut = revenu1;
        }

        nbMois = 12;
        if (!JadeStringUtil.isBlank(getDonneeBase().getMontantTotalRenteAVS())) {
            montantTotalRenteAVS = Float.parseFloat(JANumberFormatter
                    .deQuote(getDonneeBase().getMontantTotalRenteAVS()));

            if (!JadeStringUtil.isIntegerEmpty(getDonneeBase().getNbMoisExercice1())) {
                nbMois = Integer.parseInt(getDonneeBase().getNbMoisExercice1());
            }

            FWCurrency varTemp = new FWCurrency(montantTotalRenteAVS);
            double montant = 0;
            montant = (varTemp.doubleValue() / nbMois) * 12;
            montantTotalRenteAVS = (float) JANumberFormatter.round(montant, 1, 0, JANumberFormatter.SUP);
            revenuBrut = revenuBrut + montantTotalRenteAVS;
        }

        nbMois = 12;
        if (!JadeStringUtil.isBlank(getDonneeBase().getRevenuAutre1())) {
            revenuAutre1 = Float.parseFloat(JANumberFormatter.deQuote(getDonneeBase().getRevenuAutre1()));
            if (!JadeStringUtil.isIntegerEmpty(getDonneeBase().getNbMoisRevenuAutre1())) {
                nbMois = Integer.parseInt(getDonneeBase().getNbMoisRevenuAutre1());
            }
            FWCurrency varTemp = new FWCurrency(revenuAutre1);
            double montant = 0;
            montant = (varTemp.doubleValue() / nbMois) * 12;
            revenuAutre1 = (float) JANumberFormatter.round(montant, 1, 0, JANumberFormatter.SUP);
            revenuBrut = revenuBrut + revenuAutre1;
        }
        if ("A".equalsIgnoreCase(decision.getTaxation())) {
            nbMois = 12;
            if (!JadeStringUtil.isBlank(getDonneeBase().getRevenu2())) {
                revenu2 = Float.parseFloat(JANumberFormatter.deQuote(getDonneeBase().getRevenu2()));
                if (!JadeStringUtil.isIntegerEmpty(getDonneeBase().getNbMoisExercice2())) {
                    nbMois = Integer.parseInt(getDonneeBase().getNbMoisExercice2());
                }
                FWCurrency varTemp = new FWCurrency(revenu2);
                double montant = 0;
                montant = (varTemp.doubleValue() / nbMois) * 12;
                revenu2 = (float) JANumberFormatter.round(montant, 1, 0, JANumberFormatter.SUP);
                revenuBrut = revenuBrut + revenu2;
            }
            nbMois = 12;
            if (!JadeStringUtil.isBlank(getDonneeBase().getRevenuAutre2())) {
                revenuAutre2 = Float.parseFloat(JANumberFormatter.deQuote(getDonneeBase().getRevenuAutre2()));
                if (!JadeStringUtil.isIntegerEmpty(getDonneeBase().getNbMoisRevenuAutre2())) {
                    nbMois = Integer.parseInt(getDonneeBase().getNbMoisRevenuAutre2());
                }
                FWCurrency varTemp = new FWCurrency(revenuAutre2);
                double montant = 0;
                montant = (varTemp.doubleValue() / nbMois) * 12;
                revenuAutre2 = (float) JANumberFormatter.round(montant, 1, 0, JANumberFormatter.SUP);
                revenuBrut = revenuBrut + revenuAutre2;
            }
        }
        // Sauvegarde du revenu brut
        donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_REV_BRUT, revenuBrut);
        return revenuBrut;
    }

    protected float casProrataNonActif(int dureeDecision, boolean prorata, float revenuCi) {
        FWCurrency varCurrency;
        if (prorata) {
            varCurrency = new FWCurrency(revenuCi);
            revenuCi = varCurrency.floatValue();
            revenuCi = JANumberFormatter.round(revenuCi / 12, 1, 0, JANumberFormatter.NEAR);
            revenuCi = revenuCi * dureeDecision;
            decision.setProrata("2");
        }
        return revenuCi;
    }

    /**
     * Créer les remarques automatiques par rapport aux données de la décision Date de création : (17.11.2005 11:44:23)
     * */
    public void createRemarqueAutomatique(BTransaction transaction, CPDecision decision) throws Exception {
        // Pas de remarque pour lettre salarié et mise en compte car ce sont des
        // lettres BABEL
        if (!CPDecision.CS_SALARIE_DISPENSE.equals(decision.getSpecification())
                && !CPDecision.CS_IMPUTATION.equals(decision.getSpecification())) {
            // Création de ou des remarque(s) si il y a un conjoint
            // Valable uniquement pour non-actif (selon CCJU)
            if (decision.isNonActif()) {
                if ((!decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_IMPUTATION) && !JadeStringUtil
                        .isIntegerEmpty(decision.getIdConjoint())) || decision.getDivision2().equals(new Boolean(true))) {
                    CPLienCommentaireRemarqueTypeManager comManager = new CPLienCommentaireRemarqueTypeManager();
                    comManager.setSession(getSession());
                    comManager.setForIdCommentaire(CPCommentaireRemarqueType.CS_CONJOINT);
                    comManager.setForLangue(decision.loadTiers().getLangue());
                    comManager.find(getTransaction());
                    for (int i = 0; i < comManager.size(); i++) {
                        CPLienCommentaireRemarqueType lienCom = ((CPLienCommentaireRemarqueType) comManager
                                .getEntity(i));
                        // Création de la remarque pour la décision
                        CPRemarqueDecision remaDecision = new CPRemarqueDecision();
                        remaDecision.setSession(getSession());
                        remaDecision.setIdDecision(decision.getIdDecision());
                        remaDecision.setEmplacement(lienCom.getEmplacement());
                        remaDecision.setTexteRemarqueDecision(remplaceParametres(lienCom.getTexte()));
                        remaDecision.add(getTransaction());
                    }
                }
            }
            // Insertion de la remarque concernant le prorata si il y a lieu
            // (exercice sur 2 années)
            // et si le genre d'affilié est différent de non actif.
            if (("1".equalsIgnoreCase(decision.getProrata()) || "3".equalsIgnoreCase(decision.getProrata()))
                    && !decision.isNonActif()) {
                CPLienCommentaireRemarqueTypeManager comManager = new CPLienCommentaireRemarqueTypeManager();
                comManager.setSession(getSession());
                comManager.setForIdCommentaire(CPCommentaireRemarqueType.CS_PRORATA_REVN);
                comManager.setForLangue(decision.loadTiers().getLangue());
                comManager.find(getTransaction());
                for (int i = 0; i < comManager.size(); i++) {
                    CPLienCommentaireRemarqueType lienCom = ((CPLienCommentaireRemarqueType) comManager.getEntity(i));
                    // Création de la remarque pour la décision
                    CPRemarqueDecision remaDecision = new CPRemarqueDecision();
                    remaDecision.setSession(getSession());
                    remaDecision.setIdDecision(decision.getIdDecision());
                    remaDecision.setEmplacement(lienCom.getEmplacement());
                    remaDecision.setTexteRemarqueDecision(remplaceParametres(lienCom.getTexte()));
                    remaDecision.add(getTransaction());
                }
            }
            // Test si activité accessoire
            CPDonneesCalcul donnee = new CPDonneesCalcul();
            donnee.setSession(getSession());
            if (donnee.existeDonnee(decision.getIdDecision(), CPDonneesCalcul.CS_ACT_ACCESSOIRE)) {
                CPLienCommentaireRemarqueTypeManager comManager = new CPLienCommentaireRemarqueTypeManager();
                comManager.setSession(getSession());
                comManager.setForIdCommentaire(CPCommentaireRemarqueType.CS_ACT_ACCESSOIRE);
                comManager.setForLangue(decision.loadTiers().getLangue());
                comManager.find(getTransaction());
                for (int i = 0; i < comManager.size(); i++) {
                    CPLienCommentaireRemarqueType lienCom = ((CPLienCommentaireRemarqueType) comManager.getEntity(i));
                    // Création de la remarque pour la décision
                    CPRemarqueDecision remaDecision = new CPRemarqueDecision();
                    remaDecision.setSession(getSession());
                    remaDecision.setIdDecision(decision.getIdDecision());
                    remaDecision.setEmplacement(lienCom.getEmplacement());
                    remaDecision.setTexteRemarqueDecision(remplaceParametres(lienCom.getTexte()));
                    remaDecision.add(getTransaction());
                }
            }
            // Remarque pour opposition
            if (decision.getOpposition().equals(new Boolean(true))) {
                CPLienCommentaireRemarqueTypeManager comManager = new CPLienCommentaireRemarqueTypeManager();
                comManager.setSession(getSession());
                comManager.setForIdCommentaire(CPCommentaireRemarqueType.CS_OPPOSITION);
                comManager.setForLangue(decision.loadTiers().getLangue());
                comManager.find(getTransaction());
                for (int i = 0; i < comManager.size(); i++) {
                    CPLienCommentaireRemarqueType lienCom = ((CPLienCommentaireRemarqueType) comManager.getEntity(i));
                    // Création de la remarque pour la décision
                    CPRemarqueDecision remaDecision = new CPRemarqueDecision();
                    remaDecision.setSession(getSession());
                    remaDecision.setIdDecision(decision.getIdDecision());
                    remaDecision.setEmplacement(lienCom.getEmplacement());
                    remaDecision.setTexteRemarqueDecision(remplaceParametres(lienCom.getTexte()));
                    remaDecision.add(getTransaction());
                }
            }
            // Remarque pour franchise
            if (CPDecision.CS_FRANCHISE.equalsIgnoreCase(decision.getSpecification())) {
                if (!JadeStringUtil.isBlankOrZero(getMontantAnnuel())) {
                    if (decision.getFacturation().equals(Boolean.FALSE) || decision.isProvisoireMetier()) {
                        CPLienCommentaireRemarqueTypeManager comManager = new CPLienCommentaireRemarqueTypeManager();
                        comManager.setSession(getSession());
                        comManager.setForIdCommentaire(CPCommentaireRemarqueType.CS_EXEMPTE_FRANCHISE);
                        comManager.setForLangue(decision.loadTiers().getLangue());
                        comManager.find(getTransaction());
                        for (int i = 0; i < comManager.size(); i++) {
                            CPLienCommentaireRemarqueType lienCom = ((CPLienCommentaireRemarqueType) comManager
                                    .getEntity(i));
                            // Création de la remarque pour la décision
                            CPRemarqueDecision remaDecision = new CPRemarqueDecision();
                            remaDecision.setSession(getSession());
                            remaDecision.setIdDecision(decision.getIdDecision());
                            remaDecision.setEmplacement(lienCom.getEmplacement());
                            remaDecision.setTexteRemarqueDecision(remplaceParametres(lienCom.getTexte()));
                            remaDecision.add(getTransaction());
                        }
                    }
                }
            }
            // Remarque pour rentier
            if (CPDecision.CS_RENTIER.equalsIgnoreCase(decision.getGenreAffilie())) {
                CPLienCommentaireRemarqueTypeManager comManager = new CPLienCommentaireRemarqueTypeManager();
                comManager.setSession(getSession());
                comManager.setForIdCommentaire(CPCommentaireRemarqueType.CS_RENTIER);
                comManager.setForLangue(decision.loadTiers().getLangue());
                comManager.find(getTransaction());
                for (int i = 0; i < comManager.size(); i++) {
                    CPLienCommentaireRemarqueType lienCom = ((CPLienCommentaireRemarqueType) comManager.getEntity(i));
                    // Création de la remarque pour la décision
                    CPRemarqueDecision remaDecision = new CPRemarqueDecision();
                    remaDecision.setSession(getSession());
                    remaDecision.setIdDecision(decision.getIdDecision());
                    remaDecision.setEmplacement(lienCom.getEmplacement());
                    remaDecision.setTexteRemarqueDecision(remplaceParametres(lienCom.getTexte()));
                    remaDecision.add(getTransaction());
                }
            }
            // Création des remarques automatiques selon le type de décision
            CPLienTypeDecisionRemarqueTypeManager lienManager = new CPLienTypeDecisionRemarqueTypeManager();
            lienManager.setSession(getSession());
            lienManager.setForTypeDecision(decision.getTypeDecision());
            lienManager.setForLangue(decision.loadTiers().getLangue());
            lienManager.find(getTransaction());
            for (int i = 0; i < lienManager.size(); i++) {
                CPLienTypeDecisionRemarqueType lien = ((CPLienTypeDecisionRemarqueType) lienManager.getEntity(i));
                // Création de la remarque pour la décision
                CPRemarqueDecision remaDecision = new CPRemarqueDecision();
                remaDecision.setSession(getSession());
                remaDecision.setIdDecision(decision.getIdDecision());
                remaDecision.setEmplacement(lien.getEmplacement());
                remaDecision.setTexteRemarqueDecision(remplaceParametres(lien.getTexte()));
                if (!JadeStringUtil.isEmpty(remaDecision.getTexteRemarqueDecision())) {
                    remaDecision.add(getTransaction());
                }
            }
            // Insertion remarque pour NAC (avertissant que si il a déjà
            // cotisé...)
            if (decision.isNonActif() && decision.isProvisoireMetier()) {
                CPLienCommentaireRemarqueTypeManager comManager = new CPLienCommentaireRemarqueTypeManager();
                comManager.setSession(getSession());
                comManager.setForIdCommentaire(CPCommentaireRemarqueType.CS_COPIE_ATTESTATION);
                comManager.setForLangue(decision.loadTiers().getLangue());
                comManager.find(getTransaction());
                for (int i = 0; i < comManager.size(); i++) {
                    CPLienCommentaireRemarqueType lienCom = ((CPLienCommentaireRemarqueType) comManager.getEntity(i));
                    // Création de la remarque pour la décision
                    CPRemarqueDecision remaDecision = new CPRemarqueDecision();
                    remaDecision.setSession(getSession());
                    remaDecision.setIdDecision(decision.getIdDecision());
                    remaDecision.setEmplacement(lienCom.getEmplacement());
                    remaDecision.setTexteRemarqueDecision(remplaceParametres(lienCom.getTexte()));
                    remaDecision.add(getTransaction());
                }
            }
            // Création remarque envoi BVR si année de décision >= année en
            // cours
            if ((Integer.parseInt(decision.getAnneeDecision()) >= JACalendar.getYear(JACalendar.todayJJsMMsAAAA()))
                    && !CPDecision.CS_FRANCHISE.equalsIgnoreCase(decision.getSpecification())
                    && !JadeStringUtil.isBlankOrZero(getMontantAnnuel())) {
                CPLienCommentaireRemarqueTypeManager comManager = new CPLienCommentaireRemarqueTypeManager();
                comManager.setSession(getSession());
                comManager.setForIdCommentaire(CPCommentaireRemarqueType.CS_BVR);
                comManager.setForLangue(decision.loadTiers().getLangue());
                comManager.find(getTransaction());
                for (int i = 0; i < comManager.size(); i++) {
                    CPLienCommentaireRemarqueType lienCom = ((CPLienCommentaireRemarqueType) comManager.getEntity(i));
                    // Création de la remarque pour la décision
                    CPRemarqueDecision remaDecision = new CPRemarqueDecision();
                    remaDecision.setSession(getSession());
                    remaDecision.setIdDecision(decision.getIdDecision());
                    remaDecision.setEmplacement(lienCom.getEmplacement());
                    remaDecision.setTexteRemarqueDecision(remplaceParametres(lienCom.getTexte()));
                    remaDecision.add(getTransaction());
                }
            }
            // Revenu AF différent de revenu AVS
            // Insertion de la remarque concernant le prorata si il y a lieu
            // (exercice sur 2 années)
            // et si le genre d'affilié est différent de non actif.
            if ((null != isRevenuAf()) && isRevenuAf().equals(Boolean.TRUE)
                    && !JadeStringUtil.isEmpty(donneeBase.getRevenuAutre1())) {
                CPLienCommentaireRemarqueTypeManager comManager = new CPLienCommentaireRemarqueTypeManager();
                comManager.setSession(getSession());
                comManager.setForIdCommentaire(CPCommentaireRemarqueType.CS_DOUBLE_REV_AF);
                comManager.setForLangue(decision.loadTiers().getLangue());
                comManager.find(getTransaction());
                for (int i = 0; i < comManager.size(); i++) {
                    CPLienCommentaireRemarqueType lienCom = ((CPLienCommentaireRemarqueType) comManager.getEntity(i));
                    // Création de la remarque pour la décision
                    CPRemarqueDecision remaDecision = new CPRemarqueDecision();
                    remaDecision.setSession(getSession());
                    remaDecision.setIdDecision(decision.getIdDecision());
                    remaDecision.setEmplacement(lienCom.getEmplacement());
                    remaDecision.setTexteRemarqueDecision(remplaceParametres(lienCom.getTexte()));
                    remaDecision.add(getTransaction());
                }
            }
            // Assisté
            if (!JadeStringUtil.isIntegerEmpty(decision.getIdServiceSociale())) {
                CPLienCommentaireRemarqueTypeManager comManager = new CPLienCommentaireRemarqueTypeManager();
                comManager.setSession(getSession());
                comManager.setForIdCommentaire(CPCommentaireRemarqueType.CS_ASSISTANCE);
                comManager.setForLangue(decision.loadTiers().getLangue());
                comManager.find(getTransaction());
                for (int i = 0; i < comManager.size(); i++) {
                    CPLienCommentaireRemarqueType lienCom = ((CPLienCommentaireRemarqueType) comManager.getEntity(i));
                    // Création de la remarque pour la décision
                    CPRemarqueDecision remaDecision = new CPRemarqueDecision();
                    remaDecision.setSession(getSession());
                    remaDecision.setIdDecision(decision.getIdDecision());
                    remaDecision.setEmplacement(lienCom.getEmplacement());
                    remaDecision.setTexteRemarqueDecision(remplaceParametres(lienCom.getTexte()));
                    remaDecision.add(getTransaction());
                }
            }
        }
    }

    /**
     * Attache la spécificité Lettre pour couple qui sera utilisé lors de l'impression de la décision Date de création:
     * (16.02.2006 08.53:00)
     */
    public void determinationLettreCouple(BProcess process) throws Exception {
        // Concerne les indépendants qui ont un conjoint qui n'a pas de décision
        // pour la même année ou (coche divisé/2)
        // et dont la cotisation est inférieure au double de la cotisation
        // minimale
        boolean cjt = false;
        // test si cotisation < 2* le minimum
        float mAnnuel = Float.parseFloat(JANumberFormatter.deQuote(getMontantAnnuel()));
        if (mAnnuel < (cotiIndMinimum * 2)) {
            if (!decision.isNonActif() && !decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_IMPUTATION)) {
                if (decision.getDivision2().equals(new Boolean(true))) {
                    cjt = true;
                }
            }
        }
        if (cjt) {
            decision.setSpecification(CPDecision.CS_LETTRE_COUPLE);
            decision.update(process.getTransaction());
        } else if (CPDecision.CS_LETTRE_COUPLE.equalsIgnoreCase(decision.getSpecification())) {
            decision.setSpecification("");
            decision.update(process.getTransaction());
        }
    }

    /**
     * Retourne le revenu brut pour un indépendant Date de création : (18.03.2004 08:47:47)
     * 
     * @return float
     */
    public float determinationRevenuBrutIndependant(BProcess process, int codeRevenuAf) {
        float revenu1 = 0;
        float revenu2 = 0;
        float revenuAutre1 = 0;
        float revenuAutre2 = 0;
        float revenuBrut = 0;
        float nbMoisExercice = 0;
        float nbMoisExercice1 = 0;
        float nbMoisExercice2 = 0;
        try {
            /*
             * codeRevenuAf= 0 => calcul normal codeRevenuAf= 1 => calcul avs sans tenir compte de revenu autre (revenu
             * AF) codeRevenuAf= 2 => calcul uniquement de la coti AF en prenant en compte uniquement revenuAutre
             * (=revenuAF)
             */
            if (!JadeStringUtil.isBlank(getDonneeBase().getRevenu1()) && (codeRevenuAf != 2)) {
                revenu1 = Float.parseFloat(JANumberFormatter.deQuote(getDonneeBase().getRevenu1()));
                revenuBrut = revenu1;
            }
            if (!JadeStringUtil.isBlank(getDonneeBase().getRevenuAutre1()) && (codeRevenuAf != 1)) {
                revenuAutre1 = Float.parseFloat(JANumberFormatter.deQuote(getDonneeBase().getRevenuAutre1()));
                revenuBrut = revenuBrut + revenuAutre1;
            }
            if (!JadeStringUtil.isBlank(getDonneeBase().getRevenu2()) && (codeRevenuAf != 2)) {
                revenu2 = Float.parseFloat(JANumberFormatter.deQuote(getDonneeBase().getRevenu2()));
                revenuBrut = revenuBrut + revenu2;
            }
            if (!JadeStringUtil.isBlank(getDonneeBase().getRevenuAutre2()) && (codeRevenuAf != 1)) {
                revenuAutre2 = Float.parseFloat(JANumberFormatter.deQuote(getDonneeBase().getRevenuAutre2()));
                revenuBrut = revenuBrut + revenuAutre2;
            }

            // Si conjoint => diviser le revenu par 2
            // if ((!JadeStringUtil.isIntegerEmpty(decision.getIdConjoint())) ||
            // (decision.getDivision2().equals(new Boolean(true)))) {
            // // Si conjoint -> sauvegarde revenu du couple
            // donCalcul._sauvegardeCalcul(this, decision.getIdDecision(),
            // CPDonneesCalcul.CS_REV_COUPLE, revenuBrut);
            // revenuBrut = revenuBrut / 2;
            // }
            if (decision.getTaxation().equalsIgnoreCase("A")) {
                // Ramener le revenu au prorata de la période de l'exercice
                if (!decision.getAnneePrise().equalsIgnoreCase("2")) {
                    if (!JAUtil.isDateEmpty(getDonneeBase().getDebutExercice1())
                            && !JAUtil.isDateEmpty(getDonneeBase().getFinExercice1())) {
                        float moisD1 = JACalendar.getMonth(getDonneeBase().getDebutExercice1());
                        float moisF1 = JACalendar.getMonth(getDonneeBase().getFinExercice1());
                        nbMoisExercice1 = (moisF1 - moisD1) + 1;
                    }
                }
                // Année2 prise en compte
                if (!decision.getAnneePrise().equalsIgnoreCase("1")) {
                    if (!JAUtil.isDateEmpty(getDonneeBase().getDebutExercice2())
                            && !JAUtil.isDateEmpty(getDonneeBase().getFinExercice2())) {
                        float moisD2 = JACalendar.getMonth(getDonneeBase().getDebutExercice2());
                        float moisF2 = JACalendar.getMonth(getDonneeBase().getFinExercice2());
                        nbMoisExercice2 = (moisF2 - moisD2) + 1;
                    }
                }
                nbMoisExercice = nbMoisExercice1 + nbMoisExercice2;
                // Sauvegarde du revenu brut
                if (decision.getAnneePrise().equalsIgnoreCase("T")) {
                    revenuBrut = (revenuBrut * nbMoisExercice) / 24;
                } else {
                    revenuBrut = (revenuBrut * nbMoisExercice) / 12;
                }
            }
            return revenuBrut;
        } catch (Exception e) {
            this._addError(process.getTransaction(), " Echec lors du calcul du revenu brut pour "
                    + getDescriptionTiers() + " année " + decision.getAnneeDecision());
            this._addError(getTransaction(), e.getMessage());
            return 0;
        }
    }

    protected void determineAffilieAssiste() throws Exception {
        // Test si affilié assisté, bénéficaire pc etc...
        // => recherche du tiers qui paye (assistance)
        decision.setIdServiceSociale("");
        if (CPToolBox.isAffilieAssiste(getTransaction(), getAffiliation(), decision.getDebutDecision())) {
            String idTiersAssistance = "";
            // Recherche idTiers qui prend en charge les cotisation
            idTiersAssistance = AFParticulariteAffiliation.existeParticulariteOrganesExternesId(getTransaction(),
                    affiliation.getAffiliationId(), decision.getDebutDecision());
            // Recherche de la description du tiers en charge de l'affilié
            if (!JadeStringUtil.isIntegerEmpty(idTiersAssistance)) {
                decision.setIdServiceSociale(idTiersAssistance);
            }
        }
    }

    /**
     * Détermination du revenu selon le plafond de l'assurance chomage
     * 
     * @param process
     * @param dateRef
     * @param typePlafond
     * @return
     * @throws Exception
     * @throws NumberFormatException
     * @throws JAException
     */
    protected String determinePlafondAssuranceChomage(BProcess process, String dateRef, String typePlafond)
            throws Exception, NumberFormatException, JAException {
        String plafond = FWFindParameter.findParameter(process.getTransaction(), "12000006", typePlafond, dateRef, "",
                0);
        // PO
        if (!JadeStringUtil.isBlankOrZero(decision.getNombreMoisTotalDecision())) {
            double montantMax = Double.parseDouble(JANumberFormatter.deQuote(plafond));
            montantMax = JANumberFormatter.round(
                    (montantMax / 12) * Integer.parseInt(decision.getNombreMoisTotalDecision()), 0.05, 2,
                    JANumberFormatter.NEAR);
            plafond = JANumberFormatter.deQuote(JANumberFormatter.format(montantMax, 0.05, 2, JANumberFormatter.NEAR));

        } else if ((JACalendar.getMonth(decision.getDebutDecision()) != 1)
                || (JACalendar.getMonth(decision.getFinDecision()) != 12)) {
            plafond = JANumberFormatter.deQuote(CPToolBox.getMontantProRata(donneeBase.getDebutExercice1(),
                    donneeBase.getFinExercice1(), plafond));
        }
        return plafond;
    }

    protected CPTableNonActifManager findCotisationNac(float fortuneDet) throws Exception {
        CPTableNonActifManager tNacManager = new CPTableNonActifManager();
        tNacManager.setSession(getSession());
        tNacManager.setTilAnneeVigueur(decision.getAnneeDecision());
        // Si l'affilié est à l'assistance => coti minimum donc recherche
        // avec revenu=0
        if (!JadeStringUtil.isIntegerEmpty(decision.getIdServiceSociale())) {
            tNacManager.setTilRevenu("0");
        } else {
            if (fortuneDet <= 0) {
                tNacManager.setTilRevenu(Float.toString(0));
            } else {
                tNacManager.setTilRevenu(Float.toString(fortuneDet));
            }
        }
        tNacManager.find();
        return tNacManager;
    }

    protected CPTableRentierManager findCotisationRentier(float revenuDet) throws Exception {
        CPTableRentierManager tRentierManager = new CPTableRentierManager();
        tRentierManager.setSession(getSession());
        tRentierManager.setFromAnneeRentier(decision.getAnneeDecision());
        tRentierManager.setFromRevenuRentier(Float.toString(revenuDet));
        tRentierManager.find();
        return tRentierManager;
    }

    /**
     * Recherche du mode de calcul de la cotisation AF
     * 
     * @param cotiAf
     * @return
     * @throws Exception
     */

    protected String findMethodeCaculAf(AFCotisation cotiAf) throws Exception {
        String genreAffilie = "";
        if (decision.isNonActif()) {
            genreAffilie = CPDecision.CS_NON_ACTIF;
        } else {
            genreAffilie = CPDecision.CS_INDEPENDANT;
        }
        String modeCalculAF = CPParametreCanton.findCodeWhitTypeCantonAndGenre(getSession(), cotiAf.getAssurance()
                .getAssuranceCanton(), CPParametreCanton.CS_MODE_CALCUL_AF, getDecision().getFinDecision(),
                genreAffilie);
        return modeCalculAF;
    }

    /**
     * Recherche du taux de cotisation pour la période de décision
     * 
     * @param cotiAf
     * @return
     * @throws NumberFormatException
     */

    protected float findTaux(AFCotisation cotiAf) throws NumberFormatException {
        float taux;
        String varTaux = cotiAf.getTaux(decision.getDebutDecision(), "0");
        if ((varTaux == null) || JadeStringUtil.isEmpty(varTaux)) {
            varTaux = "0";
        }
        taux = Float.parseFloat(JANumberFormatter.deQuote(varTaux));
        return taux;
    }

    /**
     * @return
     */
    public AFAffiliation getAffiliation() {
        return affiliation;
    }

    public CACompteAnnexe getCompteAnnexe() {
        return compteAnnexe;
    }

    /**
     * @return
     */
    public AFCotisation getCotiAf() {
        return cotiAf;
    }

    /**
     * Retourne la cotisation minimum Afi pour une année
     * 
     * @return float
     */
    public float getCotiAfiMinimum() throws Exception {
        if (cotiAfiMinimum == 0) {
            cotiAfiMinimum = CPTableAFI.getCotisationMinimum(getTransaction(), decision.getDebutDecision());
        }
        return cotiAfiMinimum;
    }

    /**
     * Retourne la cotisation minimum indépendant pour une année
     * 
     * @return float
     */
    public float getCotiIndMinimum() throws Exception {
        if (cotiIndMinimum == 0) {
            cotiIndMinimum = CPTableIndependant.getCotisationMinimum(getTransaction(), decision.getDebutDecision());
        }
        return cotiIndMinimum;
    }

    public float getCotiNacMinimum() {
        return cotiNacMinimum;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 14:21:36)
     * 
     * @return globaz.phenix.db.principale.CPDecision
     */
    public globaz.phenix.db.principale.CPDecision getDecision() {
        return decision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 10:59:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDescriptionTiers() {
        return descriptionTiers;
    }

    /**
     * @return
     */
    public CPDonneesBase getDonneeBase() {
        return donneeBase;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {

        // Déterminer l'objet du message en fonction du code erreur
        String obj = "";

        if (getMemoryLog().hasErrors()) {
            obj = "Echec lors du calcul de la décision " + getDecision().getAnneeDecision() + " pour "
                    + getDescriptionTiers();
            // else
            // obj = FWMessage.getMessageFromId("5030")+ " " + getIdDecision();
        }

        // Restituer l'objet
        return obj;

    }

    /**
     * Retourne le montant de la franchise AVS Date de création : (18.03.2004 08:47:47)
     * 
     * @return float
     */
    public float getFranchise(BProcess process, boolean exerciceSur2annee) {
        // Pour le mode preanumerando, le montant de la franchise était valable
        // pour toute l'année soit 12* montant mensuel.
        // Depuis le mode postnumerando, le nombre de mois est déterminer depuis
        // le
        // début de la période de décision (ou le début de l'âge AVS si c'est
        // l'année ou
        // l'affilié atteint l'âge AVS) jusqu'à la fin de la decision.
        float mFranchise = 0;
        try {
            // Recherche du montant de franchise mensuel
            mFranchise = Float.parseFloat(FWFindParameter.findParameter(getTransaction(), "10500030", "FRANCHISE",
                    decision.getDebutDecision(), "", 0));
            if (decision.getTaxation().equalsIgnoreCase("A")) {
                return mFranchise * 12;
            } else {
                // Recherche de l'âge AVS
                int moisDebut = 0;
                int moisFin = 0;
                if (exerciceSur2annee) {
                    if (BSessionUtil.compareDateBetweenOrEqual(process.getSession(), getDonneeBase()
                            .getDebutExercice1(), getDonneeBase().getFinExercice1(), dateAvs)) {
                        moisDebut = JACalendar.getMonth(dateAvs) + 1;
                        moisFin = JACalendar.getMonth(getDonneeBase().getFinExercice1());
                        if (JACalendar.getYear(dateAvs) < JACalendar.getYear(getDonneeBase().getFinExercice1())) {
                            moisFin = moisFin + 12;
                        }
                    } else {
                        moisDebut = JACalendar.getMonth(getDonneeBase().getDebutExercice1());
                        moisFin = JACalendar.getMonth(getDonneeBase().getFinExercice1()) + 12;
                    }
                } else {
                    moisDebut = JACalendar.getMonth(decision.getDebutDecision());
                    moisFin = JACalendar.getMonth(decision.getFinDecision());
                    int varNum = Integer.parseInt(decision.getNombreMoisTotalDecision());
                    if (varNum != 0) {
                        // Recaler la date de début et de fin par rapport à la
                        // période totale
                        int vNum = (moisDebut + varNum) - 1;
                        if (vNum <= 12) { // décalage du mois de fin
                            moisFin = vNum;
                        } else { // Décalage du mois de début
                            vNum = (moisFin - varNum) + 1;
                            if (vNum >= 1) {
                                moisDebut = vNum;
                            } else { // Période ne tenant pas dans la décision
                                moisDebut = 1;
                                moisFin = varNum;
                            }
                        }
                    }
                    if (decision.getAnneeDecision().equalsIgnoreCase(Integer.toString(anneeAvs))
                            && BSessionUtil.compareDateFirstLower(getSession(), decision.getDebutDecision(), dateAvs)) {
                        moisDebut = JACalendar.getMonth(dateAvs) + 1;
                    }
                }
                // Calcul de la franchise
                return mFranchise * ((moisFin - moisDebut) + 1);
            }
        } catch (Exception e) {
            this._addError(process.getTransaction(), " Echec lors du calcul de la franchise pour "
                    + getDescriptionTiers() + " année " + decision.getAnneeDecision());
            this._addError(process.getTransaction(), e.getMessage());
            return 0;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 13:55:43)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdDecision() {
        return idDecision;
    }

    public int getModeArrondiFad() {
        return modeArrondiFad;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 13:05:41)
     * 
     * @return float
     */
    public String getMontantAnnuel() {
        return montantAnnuel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 13:04:19)
     * 
     * @return float
     */
    public String getMontantMensuel() {
        return montantMensuel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 13:05:15)
     * 
     * @return float
     */
    public String getMontantSemestriel() {
        return montantSemestriel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 13:04:47)
     * 
     * @return float
     */
    public String getMontantTrimestriel() {
        return montantTrimestriel;
    }

    public float getRevenuNacMin() {
        return revenuNacMin;
    }

    /**
     * @return
     */
    public TITiersViewBean getTiers() {
        return tiers;
    }

    protected float handleIncomeForExerciseOn2Years(int codeRevenuAf, float revenuDet, CPDonneesCalcul donCalcul,
            boolean exerciceSur2Annee) throws Exception, NumberFormatException {
        if (exerciceSur2Annee == true) {
            // Sauvegarde du revenu avant prorata
            if (codeRevenuAf != 2) {
                donCalcul._sauvegardeCalcul(this, decision.getIdDecision(), CPDonneesCalcul.CS_REV_NET_AVANT_PRORATA,
                        revenuDet);
            }
            revenuDet = Float.parseFloat(CPToolBox.annualisationRevenu(getDonneeBase().getDebutExercice1(),
                    getDonneeBase().getFinExercice1(), decision.getAnneeDecision(), Float.toString(revenuDet)));
        }
        return revenuDet;
    }

    /**
     * Détermination si l'affilié a assez payé en tant que salarié
     * 
     * @param cotiMinimum
     * @param montantAnnuel
     * @param prorata
     * @param cotiAnnuelle
     * @throws NumberFormatException
     */
    protected void handleSalarieDispense(Boolean cotiMinimum, String montantAnnuel, boolean prorata, float cotiAnnuelle)
            throws NumberFormatException {
        if (!JadeStringUtil.isBlank(getDonneeBase().getCotisationSalarie())) {
            float cotisationSalarie = Float.parseFloat(JANumberFormatter
                    .deQuote(getDonneeBase().getCotisationSalarie()));
            if (cotisationSalarie > 0) {
                float montantSemestriel = 0;
                if (cotiAnnuelle != 0) {
                    montantSemestriel = cotiAnnuelle / 2;
                } else {
                    montantSemestriel = Float.parseFloat(JANumberFormatter.deQuote(montantAnnuel)) / 2;
                }
                float mMinimum = 0;
                if (cotiMinimum.equals(Boolean.TRUE)) {
                    mMinimum = cotiAnnuelle; // montant minimum
                    // proratisé
                } else {
                    mMinimum = getCotiNacMinimum(); // Sinon doit payer au moins le minimum
                    if (prorata) { // Bug 8768 - Lettre "Payé en tant que salarié" (cot., pers.): La comparaison doit
                                   // tenir compte du prorata dans le cas où la période de décision est incomplète.
                        mMinimum = Float.parseFloat(CPToolBox.getMontantProRata(getDecision().getDebutDecision(),
                                getDecision().getFinDecision(), mMinimum));
                    }
                }
                if ((cotisationSalarie >= montantSemestriel) && (cotisationSalarie >= mMinimum)) {
                    // Si déjà payé plus que le montant semestriel
                    // et que la cotisation minimum,
                    // on indique dans la décision qu'il a payé
                    // suffisement en tant que salarié.
                    decision.setSpecification(CPDecision.CS_SALARIE_DISPENSE);
                }
            }
        }
    }

    /**
     * Initialisation des revenus et cotisations minimum pour non actif
     * 
     * @throws NumberFormatException
     * @throws Exception
     */
    protected void initMontantMinimum() throws NumberFormatException, Exception {
        if (getRevenuNacMin() == 0) {
            setRevenuNacMin(new Float(CPTableNonActif.getRevenuMin(getSession(), decision.getAnneeDecision()))
                    .floatValue());
        }
        if (getCotiNacMinimum() == 0) {
            setCotiNacMinimum(CPTableNonActif.getCotisationMin(getSession(), decision.getAnneeDecision()));
        }
    }

    /**
     * @return
     */
    public boolean isCalculForReprise() {
        return calculForReprise;
    }

    public Boolean isRevenuAf() {
        return revenuAf;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    protected void remboursementAFI(BProcess process) throws Exception {
        if (getCompteAnnexe() != null) {
            // Recherche s'il y a eu une cotisation AF
            AFCotisation cotiAfi = getAffiliation()._cotisation(process.getTransaction(), decision.getIdAffiliation(),
                    CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_AFI, affiliation.getDateDebut(),
                    decision.getFinDecision(), 4);
            if (cotiAfi != null) {
                String montantCompteur = CPToolBox.rechMontantFacture(getSession(), getTransaction(), getCompteAnnexe()
                        .getIdCompteAnnexe(), cotiAfi.getAssurance().getRubriqueId(), decision.getAnneeDecision());
                if (!JadeStringUtil.isBlankOrZero(montantCompteur)) {
                    addCotisation(process, 0, cotiAfi, "0", "", Boolean.FALSE);
                }
            }
        }
    }

    /*
     * replace les paramètres (variables) défini dans les remarques entre {}
     */
    private String remplaceParametres(String remarque) {
        String chaineModifiee = remarque;
        int index1 = chaineModifiee.indexOf("{");
        int index2 = chaineModifiee.indexOf("}");
        boolean pasTrouve = false;
        while ((index1 != -1) && (index2 != -1) && !pasTrouve) {
            try {
                String chaineARemplacer = chaineModifiee.substring(index1, index2 + 1);
                // Pour le genre d'affilié --> avec/sans activité lucrative
                if ("{genreAffilie}".equals(chaineARemplacer)) {
                    if (decision.isNonActif() || CPDecision.CS_ACT_NON_LUCRATIVE.equals(decision.getGenreAffilie())) {
                        chaineModifiee = CPToolBox.replaceString(
                                chaineModifiee,
                                "{genreAffilie}",
                                getSession().getApplication().getLabel("CPDECISION_GENRENONACTIF",
                                        globaz.phenix.translation.CodeSystem.getCode(getSession(), tiers.getLangue())));
                    } else if (CPDecision.CS_TSE.equalsIgnoreCase(decision.getGenreAffilie())
                            || getAffiliation().getTypeAffiliation().equalsIgnoreCase(CodeSystem.TYPE_AFFILI_TSE)
                            || getAffiliation().getTypeAffiliation().equalsIgnoreCase(
                                    CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE)) {
                        chaineModifiee = CPToolBox.replaceString(
                                chaineModifiee,
                                "{genreAffilie}",
                                getSession().getApplication().getLabel("CPDECISION_GENRETSE",
                                        globaz.phenix.translation.CodeSystem.getCode(getSession(), tiers.getLangue())));
                    } else {
                        chaineModifiee = CPToolBox.replaceString(
                                chaineModifiee,
                                "{genreAffilie}",
                                getSession().getApplication().getLabel("CPDECISION_GENREINDEP",
                                        globaz.phenix.translation.CodeSystem.getCode(getSession(), tiers.getLangue())));
                    }
                    // Pour la périodicité, --> mensuellement,
                    // trimestriellement, semestriellement, annuellement
                } else if ("{periodicite}".equals(chaineARemplacer)) {
                    // La périodicité est renseignée dans l'affiliation, donc
                    // selection sur l'affiliation
                    AFAffiliation affiliation = new AFAffiliation();
                    affiliation.setSession(getSession());
                    affiliation.setAffiliationId(decision.getIdAffiliation());
                    affiliation.retrieve(getTransaction());
                    if ((affiliation != null) && !affiliation.isNew()) {
                        // mensuelle
                        if (CodeSystem.PERIODICITE_MENSUELLE.equals(affiliation.getPeriodicite())) {
                            chaineModifiee = CPToolBox.replaceString(
                                    chaineModifiee,
                                    "{periodicite}",
                                    getSession().getApplication().getLabel(
                                            "CPDECISION_PERIODEMENSUEL",
                                            globaz.phenix.translation.CodeSystem.getCode(getSession(),
                                                    tiers.getLangue())));
                            // trimestrielle
                        } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equals(affiliation.getPeriodicite())) {
                            chaineModifiee = CPToolBox.replaceString(
                                    chaineModifiee,
                                    "{periodicite}",
                                    getSession().getApplication().getLabel(
                                            "CPDECISION_PERIODETRIM",
                                            globaz.phenix.translation.CodeSystem.getCode(getSession(),
                                                    tiers.getLangue())));
                            // semestrielle
                        } else if ("802003".equals(affiliation.getPeriodicite())) {
                            chaineModifiee = CPToolBox.replaceString(
                                    chaineModifiee,
                                    "{periodicite}",
                                    getSession().getApplication().getLabel(
                                            "CPDECISION_PERIODESEM",
                                            globaz.phenix.translation.CodeSystem.getCode(getSession(),
                                                    tiers.getLangue())));
                            // annuelle
                        } else {
                            chaineModifiee = CPToolBox.replaceString(
                                    chaineModifiee,
                                    "{periodicite}",
                                    getSession().getApplication().getLabel(
                                            "CPDECISION_PERIODEANNUEL",
                                            globaz.phenix.translation.CodeSystem.getCode(getSession(),
                                                    tiers.getLangue())));
                        }
                    } else {
                        // si on a pas trouve, on sort de la boucle
                        pasTrouve = true;
                    }
                    // donne la date de décision précédente
                } else if ("{dateDecisionPrecedente}".equals(chaineARemplacer)) {
                    CPDecision derniereDecision = decision.loadDerniereDecision(1);
                    if (derniereDecision != null) {
                        String dDecision = CPDecision.getDateDecision(derniereDecision);
                        if (!JAUtil.isDateEmpty(dDecision)) {
                            // type de décision précédente est égale à
                            // notification --> texte: notre notification du ...
                            chaineModifiee = CPToolBox.replaceString(
                                    chaineModifiee,
                                    "{dateDecisionPrecedente}",
                                    (getSession().getApplication().getLabel(
                                            "CPDECISION_PRECEDENTE",
                                            globaz.phenix.translation.CodeSystem.getCode(getSession(),
                                                    tiers.getLangue())) + dDecision));
                        } else {
                            chaineModifiee = "";
                        }
                    } else {
                        // si pas trouvé de décision précédente, effacement de
                        // la remarque
                        chaineModifiee = "";
                    }
                } else if ("{mRevenuNet}".equals(chaineARemplacer)) {
                    String revenuNet = "";
                    CPDonneesCalcul donneesCalcul = new CPDonneesCalcul();
                    donneesCalcul.setSession(getSession());
                    revenuNet = donneesCalcul.getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_NET);
                    if (JadeStringUtil.isBlank(revenuNet)) {
                        chaineModifiee = "";
                    } else {
                        chaineModifiee = CPToolBox.replaceString(chaineModifiee, "{mRevenuNet}", revenuNet);
                    }
                } else if ("{mRevenuAf}".equals(chaineARemplacer)) {
                    chaineModifiee = CPToolBox.replaceString(chaineModifiee, "{mRevenuAf}", getDonneeBase()
                            .getRevenuAutre1());
                } else if ("{mRevenuCI}".equals(chaineARemplacer)) {
                    String revenuCI = "";
                    CPDonneesCalcul donneesCalcul = new CPDonneesCalcul();
                    donneesCalcul.setSession(getSession());
                    revenuCI = donneesCalcul.getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_CI);
                    if (JadeStringUtil.isBlank(revenuCI)) {
                        chaineModifiee = "";
                        // chaineModifiee =
                        // CPToolBox.replaceString(chaineModifiee,
                        // "{mRevenuCI}", "0");
                    } else {
                        chaineModifiee = CPToolBox.replaceString(chaineModifiee, "{mRevenuCI}", revenuCI);
                    }
                } else if ("{nbMoisProrata}".equals(chaineARemplacer)) {
                    String nbMoisProrata = CPToolBox.nbMoisDansPeriode(getDonneeBase().getDebutExercice1(),
                            getDonneeBase().getFinExercice1(), decision.getAnneeDecision());
                    if (JadeStringUtil.isBlank(nbMoisProrata)) {
                        chaineModifiee = "";
                    } else {
                        chaineModifiee = CPToolBox.replaceString(chaineModifiee, "{nbMoisProrata}", nbMoisProrata);
                    }
                } else if ("{anneeDecision}".equals(chaineARemplacer)) {
                    chaineModifiee = CPToolBox.replaceString(chaineModifiee, "{anneeDecision}",
                            decision.getAnneeDecision());
                } else if ("{tiersAssistance}".equals(chaineARemplacer)) {
                    // Recherche de la description du tiers en charge de
                    // l'affilié
                    if (!JadeStringUtil.isIntegerEmpty(decision.getIdServiceSociale())) {
                        TITiersViewBean tiersAssistance = new TITiersViewBean();
                        tiersAssistance.setSession(getSession());
                        tiersAssistance.setIdTiers(decision.getIdServiceSociale());
                        String today = JACalendar.today().toStr(".");
                        String adresseTiersAssistance = "";
                        TIAvoirAdresse avoirAdresse = tiersAssistance.findAvoirAdresse(
                                IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005", today);
                        if (avoirAdresse != null) {
                            TIAdresse adresse = new TIAdresse();
                            adresseTiersAssistance = adresse.getDetailAdresseHorizontale(getSession(),
                                    tiersAssistance.getIdTiers(), avoirAdresse.getIdAdresse());
                        }
                        chaineModifiee = CPToolBox.replaceString(chaineModifiee, "{tiersAssistance}",
                                adresseTiersAssistance);
                    } else {
                        chaineModifiee = "";
                    }
                } else if ("{mRevenuAccessoire}".equals(chaineARemplacer)) {
                    // Recherche de la description du tiers en charge de
                    // l'affilié
                    try {
                        float revenuActviteAccessoire = Float.parseFloat(FWFindParameter.findParameter(
                                getTransaction(), "10500130", "REVACTACC", decision.getDebutDecision(), "", 0));
                        chaineModifiee = CPToolBox.replaceString(chaineModifiee, "{mRevenuAccessoire}",
                                JANumberFormatter.fmt(Float.toString(revenuActviteAccessoire), true, false, true, 0));
                    } catch (Exception e) {
                        chaineModifiee = "";
                    }
                } else {
                    pasTrouve = true;
                }
                // on recherche les prochaines {}
                index1 = chaineModifiee.indexOf("{");
                index2 = chaineModifiee.indexOf("}");
            } catch (Exception e) {
                this._addError(getTransaction(), e.getMessage());
            }
        }
        return chaineModifiee;
    }

    /**
     * Ajout des montants de cotisation Date de création : (25.02.2002 12:58:23)
     * 
     * @param process
     *            BProcess le processus d'exécution
     * @param anneeRevenu
     *            String (année du revenu pour se positionner dans la table)
     * @param revenu
     *            string
     * @return CPTableIndependant
     **/
    public CPTableAFI returnTableAfi(BProcess process, String anneeRevenu, String revenuDeterminant) {
        /*
         * Return l'objet CPTableIndependant qui permet de déterminer le calcul de la cotisation
         */
        try {
            CPTableAFIManager tAfiManager = new CPTableAFIManager();
            tAfiManager.setSession(process.getSession());
            tAfiManager.setFromAnneeAfi(anneeRevenu);
            tAfiManager.setFromRevenuAfi(revenuDeterminant);
            tAfiManager.orderByAnneeDescendant();
            tAfiManager.orderByRevenuDescendant();
            tAfiManager.find();
            if (tAfiManager.size() == 0) {
                this._addError(process.getTransaction(), "cotisation AFI non trouvée: " + anneeRevenu + "  "
                        + revenuDeterminant + " " + getDescriptionTiers());
                return null;
            } else {
                return ((CPTableAFI) tAfiManager.getEntity(0));
            }
        } catch (Exception e) {
            this._addError(process.getTransaction(), " Echec lors de la recherche dans la table AFI pour "
                    + getDescriptionTiers() + " année: " + anneeRevenu + " - Revenu: " + revenuDeterminant);
            this._addError(process.getTransaction(), e.getMessage());
            return null;
        }
    }

    /**
     * Ajout des montants de cotisation Date de création : (25.02.2002 12:58:23)
     * 
     * @param process
     *            BProcess le processus d'exécution
     * @param anneeRevenu
     *            String (année du revenu pour se positionner dans la table)
     * @param revenu
     *            string
     * @return CPTableIndependant
     **/
    public CPTableIndependant returnTableIndependant(BProcess process, String anneeRevenu, String revenuDeterminant) {
        /*
         * Return l'objet CPTableIndependant qui permet de déterminer le calcul de la cotisation
         */
        try {
            CPTableIndependantManager tIndManager = new CPTableIndependantManager();
            tIndManager.setSession(process.getSession());
            tIndManager.setFromAnneeInd(anneeRevenu);
            tIndManager.setFromRevenuInd(revenuDeterminant);
            tIndManager.orderByAnneeDescendant();
            tIndManager.orderByRevenuDescendant();
            tIndManager.find();
            if (tIndManager.size() == 0) {
                this._addError(process.getTransaction(), "cotisation non trouvée: " + anneeRevenu + "  "
                        + revenuDeterminant + " " + getDescriptionTiers());
                return null;
            } else {
                return ((CPTableIndependant) tIndManager.getEntity(0));
            }
        } catch (Exception e) {
            this._addError(process.getTransaction(), " Echec lors de la recherche dans la table pour indépendant pour "
                    + getDescriptionTiers() + " année: " + anneeRevenu + " - Revenu: " + revenuDeterminant);
            this._addError(process.getTransaction(), e.getMessage());
            return null;
        }
    }

    /**
     * @param affiliation
     */
    public void setAffiliation(AFAffiliation affiliation) {
        this.affiliation = affiliation;
    }

    /**
     * @param b
     */
    public void setCalculForReprise(boolean b) {
        calculForReprise = b;
    }

    public void setCompteAnnexe(CACompteAnnexe compteAnnexe) {
        this.compteAnnexe = compteAnnexe;
    }

    /**
     * @param cotisation
     */
    public void setCotiAf(AFCotisation cotisation) {
        cotiAf = cotisation;
    }

    public void setCotiAfiMinimum(float cotiAfiMinimum) {
        this.cotiAfiMinimum = cotiAfiMinimum;
    }

    public void setCotiIndMinimum(float cotiIndMinimum) {
        this.cotiIndMinimum = cotiIndMinimum;
    }

    public void setCotiNacMinimum(float cotiNacMinimum) {
        this.cotiNacMinimum = cotiNacMinimum;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 14:21:36)
     * 
     * @param newDecision
     *            globaz.phenix.db.principale.CPDecision
     */
    public void setDecision(globaz.phenix.db.principale.CPDecision newDecision) {
        decision = newDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 10:59:30)
     * 
     * @param newDescriptionTiers
     *            java.lang.String
     */
    public void setDescriptionTiers(java.lang.String newDescriptionTiers) {
        descriptionTiers = newDescriptionTiers;
    }

    /**
     * @param base
     */
    public void setDonneeBase(CPDonneesBase base) {
        donneeBase = base;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 13:55:43)
     * 
     * @param newIdJournal
     *            java.lang.String
     */
    public void setIdDecision(java.lang.String newIdDecision) {
        idDecision = newIdDecision;
    }

    public void setModeArrondiFad(int modeArrondiFad) {
        this.modeArrondiFad = modeArrondiFad;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 13:05:41)
     * 
     * @param newMontantAnnuel
     *            float
     */
    public void setMontantAnnuel(String newMontantAnnuel) {
        montantAnnuel = newMontantAnnuel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 13:04:19)
     * 
     * @param newMontantMensuel
     *            float
     */
    public void setMontantMensuel(String newMontantMensuel) {
        montantMensuel = newMontantMensuel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 13:05:15)
     * 
     * @param newMontantSemestriel
     *            float
     */
    public void setMontantSemestriel(String newMontantSemestriel) {
        montantSemestriel = newMontantSemestriel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 13:04:47)
     * 
     * @param newMontantTrimestriel
     *            float
     */
    public void setMontantTrimestriel(String newMontantTrimestriel) {
        montantTrimestriel = newMontantTrimestriel;
    }

    public void setRevenuAf(Boolean revenuAf) {
        this.revenuAf = revenuAf;
    }

    public void setRevenuNacMin(float cotiNacMin) {
        revenuNacMin = cotiNacMin;
    }

    /**
     * @param bean
     */
    public void setTiers(TITiersViewBean bean) {
        tiers = bean;
    }

    /*
     * Supprime les anciennes données du calcul
     */
    public void suppressionAnciennesDonnees() throws Exception {
        // Données du calcul
        CPDonneesCalculManager donCalculManager = new CPDonneesCalculManager();
        donCalculManager.setSession(getSession());
        donCalculManager.setForIdDecision(getIdDecision());
        donCalculManager.delete(getTransaction());
        // Cotisation
        CPCotisationManager cotiManager = new CPCotisationManager();
        cotiManager.setSession(getSession());
        cotiManager.setForIdDecision(getIdDecision());
        cotiManager.delete(getTransaction());
        // remarque
        CPRemarqueDecisionManager remaManager = new CPRemarqueDecisionManager();
        remaManager.setSession(getSession());
        remaManager.setForIdDecision(getIdDecision());
        remaManager.delete(getTransaction());
        // Contrôle cotisation salarié
        if (CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(decision.getSpecification())) {
            decision.setSpecification("");
        }
    }

}
