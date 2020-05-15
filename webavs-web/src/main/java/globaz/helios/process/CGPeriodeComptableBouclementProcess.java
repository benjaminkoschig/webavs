package globaz.helios.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.api.ICGJournal;
import globaz.helios.db.avs.CGSecteurAVS;
import globaz.helios.db.avs.CGSecteurAVSManager;
import globaz.helios.db.bouclement.CGBouclement;
import globaz.helios.db.bouclement.CGBouclementManager;
import globaz.helios.db.comptes.CGBilanListViewBean;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGCompteManager;
import globaz.helios.db.comptes.CGControleCompteNouvelExerciceManager;
import globaz.helios.db.comptes.CGEcritureListViewBean;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.comptes.CGJournalManager;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGMouvementCompteListViewBean;
import globaz.helios.db.comptes.CGMouvementCompteViewBean;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.db.comptes.CGPlanComptableListViewBean;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.db.comptes.CGSolde;
import globaz.helios.db.comptes.CGSoldeManager;
import globaz.helios.db.ecritures.CGGestionEcritureViewBean;
import globaz.helios.db.lynx.CGLynxJournalManager;
import globaz.helios.db.osiris.CGOsirisJournal;
import globaz.helios.db.osiris.CGOsirisJournalManager;
import globaz.helios.helpers.ecritures.CGGestionEcritureAdd;
import globaz.helios.helpers.ecritures.utils.CGGestionEcritureUtils;
import globaz.helios.parser.CGBilanParser;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.properties.JadePropertiesService;
import globaz.lynx.db.journal.LXJournal;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import ch.globaz.helios.business.exceptions.CGPeriodeComptableException;

/**
 * Date de création : (20.03.2003 14:48:16)
 * 
 * @revision SCO 19 janv. 2010
 */
public class CGPeriodeComptableBouclementProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String BOUCLEMENT_CHECK_LYNX_JOURNAUX = "bouclementCheckLynxJournaux";
    private static final String BOUCLEMENT_CHECK_OSIRIS_JOURNAUX = "bouclementCheckOsirisJournaux";
    private static final String INFO_FIN = "FIN";

    private static final String LABEL_CLOTURE_ANUNELLE_COMPTE_BILAN_COMPTE_CLOTURE_INEXISTANT = "CLOTURE_ANUNELLE_COMPTE_BILAN_COMPTE_CLOTURE_INEXISTANT";
    private static final String LABEL_CLOTURE_ANUNELLE_COMPTE_BILAN_ERROR = "CLOTURE_ANUNELLE_COMPTE_BILAN_ERROR";

    private static final String LABEL_CLOTURE_ANUNELLE_COMPTE_BILAN_OK = "CLOTURE_ANUNELLE_COMPTE_BILAN_OK";

    private static final String LABEL_CLOTURE_ANUNELLE_COMPTE_BILAN_TOTAL_TRANSFERT_NON_NUL = "CLOTURE_ANUNELLE_COMPTE_BILAN_TOTAL_TRANSFERT_NON_NUL";

    private static final String LABEL_CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_COMPTE_CONTRE_ECRITURE_INEXISTANT = "CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_COMPTE_CONTRE_ECRITURE_INEXISTANT";
    private static final String LABEL_OUVERTURE_PERIODE_SUIVANTE_CLOTUREE = "OUVERTURE_PERIODE_SUIVANTE_CLOTUREE";
    private static final String LABEL_OUVERTURE_PERIODE_SUIVANTE_DEJA_EXISTANTE = "OUVERTURE_PERIODE_SUIVANTE_DEJA_EXISTANTE";
    private final static String LABEL_PREFIX = "BOUCLEMENT_";
    private static final String LABEL_REOUVERTURE_SOLDES_COMPTE_OUVERTURE_INEXISTANT = "REOUVERTURE_SOLDES_COMPTE_OUVERTURE_INEXISTANT";
    private static final String SECTEUR_EXPLOITATION_2110 = "2110";
    private static final String SECTEUR_EXPLOITATION_2180 = "2180";
    private static final String SECTEUR_EXPLOITATION_2189 = "2189";
    private static final String SECTEUR_EXPLOITATION_UNTIL = "2199";

    private CGBouclement bouclement = null;
    private CGExerciceComptable exercice = null;
    private CGExerciceComptable exerciceSuiv = null;
    private boolean hasPeriodePrec = true;
    private String idCompteResultatChargeSecteur2 = null;
    private String idCompteResultatProduitSecteur2 = null;
    private String idPeriodeComptable = "";

    private CGJournal journalClot = null;
    private CGJournal journalClot2 = null;
    private CGJournal journalClot3 = null;
    private CGJournal journalExtourne = null;
    private CGJournal journalPandemie = null;

    private CGMandat mandat = null;

    private CGPeriodeComptable periode = null;
    private CGPeriodeComptable periodeClot = null;

    private CGPeriodeComptable periodePrec = null;

    private Boolean quittancerWarning = new Boolean(false);

    /**
     * Commentaire relatif au constructeur CGPeriodeComptableBouclementProcess.
     */
    public CGPeriodeComptableBouclementProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur CGPeriodeComptableBouclementProcess.
     * 
     * @param parent
     *            globaz.globall.db.BProcess
     */
    public CGPeriodeComptableBouclementProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur CGPeriodeComptableBouclementProcess.
     * 
     * @param session globaz.globall.db.BSession
     */
    public CGPeriodeComptableBouclementProcess(BSession session) {
        super(session);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {

        BTransaction transaction = getTransaction();

        setProgressScaleValue(20);

        if (transaction.hasErrors()) {
            this.error("TRANSACTION_ERROR", transaction.getErrors().toString());
            return false;
        }

        this.info("DEBUT");

        try {
            if (!init()) {
                this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
                return false;
            }
        } catch (Exception e) {
            this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
            return false;
        }

        incProgressCounter();

        // Gestion de l'abort
        if (isAborted()) {
            return false;
        }

        try {
            /**
             * À Décommenter en cas de création d'un journal pandémie.
             *
             */
//            String isPandemie = JadePropertiesService.getInstance().getProperty("helios.pandemie.isSecteur_218X");
//
//            if (isPandemie == null) {
//                throw new Exception(label("PROPERTY_PANDEMIE"));
//            }
//            if (isPandemie.equals("true")) {
//                creationJournalPandemie();
//            }
            comptabiliserJournaux();
            incProgressCounter();
        } catch (Exception e) {
            this.error("JOURNAUX_NON_COMPTABILISES");
            this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
            return false;
        }

        // Gestion de l'abort
        if (isAborted()) {
            return false;
        }

        try {
            if (!controleCompteActifPassif()) {
                this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
                return false;
            } else {
                incProgressCounter();
            }
        } catch (Exception e) {
            this.error("CONTROLE_COMPTE_ACTIF_PASSIF_ERROR");
            this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
            return false;
        }

        this.info("CONTROLE_COMPTE_ACTIF_PASSIF_OK");

        // Gestion de l'abort
        if (isAborted()) {
            return false;
        }

        if (mandat.isEstComptabiliteAVS().booleanValue()) {
            try {
                testOsirisJournaux();
                testLynxJournaux();
                incProgressCounter();
                // Gestion de l'abort
                if (isAborted()) {
                    return false;
                }

                // Bug 5292 - Test déplacé après l'ouverture du journal de cloture.
                // this.testTotalDebitCreditCompteExploitation();
                // this.incProgressCounter();
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR,
                        getSession().getLabel("GLOBAL_PERIODE") + " N°" + getIdPeriodeComptable());
                this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
                return false;
            }
        } else {
            // this.incProgressCounter();
            incProgressCounter();
        }

        // Gestion de l'abort
        if (isAborted()) {
            return false;
        }

        this.info("COMPTABILISTION_JOURNAUX_OK");

        if (mandat.isEstComptabiliteAVS().booleanValue() && mandat.isControleCompte1106().booleanValue()
                && bouclement.isBouclementMensuelAVS().booleanValue()) {
            try {
                controleCompte_1106_2740();
            } catch (Exception e) {
                this.error("CONTROLE_COMPTE_1106_2740_ERROR", e.getMessage());
                this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
                return false;
            }
            if (isAborted()) {
                return false;
            }
            this.info("CONTROLE_COMPTE_1106_2740_OK");
        }
        incProgressCounter();

        // Gestion de l'abort
        if (isAborted()) {
            return false;
        }

        try {
            annulationJournalPrecedent();
            incProgressCounter();
        } catch (Exception e) {
            this.error("SUPRESSION_JOURNAL_PREC_ERROR", e.getMessage());
            this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
            return false;
        }

        // Gestion de l'abort
        if (isAborted()) {
            return false;
        }

        this.info("SUPRESSION_JOURNAL_PREC_OK");

        try {
            ouvertureJournalClot();
            incProgressCounter();
        } catch (Exception e) {
            this.error("OUVERTURE_JOURNAL_CLOTURE_ERROR", e.getMessage());
            this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
            return false;
        }

        this.info("OUVERTURE_JOURNAL_CLOTURE_OK");

        // Bug 5292 - Test déplacé après l'ouverture du journal de cloture.
        if (mandat.isEstComptabiliteAVS().booleanValue()) {
            try {
                testTotalDebitCreditCompteExploitation(); // Déplacé après ouverture du journal de cloture
                incProgressCounter();
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR,
                        getSession().getLabel("GLOBAL_PERIODE") + " N°" + getIdPeriodeComptable());
                this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
                return false;
            }
        } else {
            incProgressCounter();
        }

        try {
            ouverturePeriodeSuiv(transaction);
            incProgressCounter();
        } catch (Exception e) {
            this.error("OUVERTURE_PERIODE_SUIVANTE_ERROR", e.getMessage());
            this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
            return false;
        }

        this.info("OUVERTURE_PERIODE_SUIVANTE_OK");

        // Gestion de l'abort
        if (isAborted()) {
            return false;
        }

        if (mandat.isEstComptabiliteAVS().booleanValue() && mandat.isVentilerCompte1102().booleanValue()
                && bouclement.isBouclementMensuelAVS().booleanValue()) {
            try {
                ventilationCompte_2000_1102_000();
            } catch (Exception e) {
                this.error("VENTILATION_COMPTE_2000_1102_0000_ERROR", e.getMessage());
                this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
                return false;
            }

            // Gestion de l'abort
            if (isAborted()) {
                return false;
            }

            this.info("VENTILATION_COMPTE_2000_1102_0000_OK");
        }
        incProgressCounter();

        // Gestion de l'abort
        if (isAborted()) {
            return false;
        }

        if (mandat.isEstComptabiliteAVS().booleanValue() && bouclement.isBouclementMensuelAVS().booleanValue()) {
            try {
                clotureCompteExploitation_AVS_AI();
            } catch (Exception e) {
                this.error("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ERROR", e.getMessage());
                this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
                return false;
            }

            // Gestion de l'abort
            if (isAborted()) {
                return false;
            }

            this.info("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_OK");
        }
        incProgressCounter();

        // Gestion de l'abort
        if (isAborted()) {
            return false;
        }

        if (mandat.isEstComptabiliteAVS().booleanValue()
                && (bouclement.isBouclementMensuelAVS().booleanValue() || bouclement.isBouclementAnnuelAVS()
                        .booleanValue())) {
            try {
                clotureCompteExploitationAdministrationAutresTaches();
            } catch (Exception e) {
                this.error("CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_ERROR", e.getMessage());
                this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
                return false;
            }

            this.info("CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_OK");
        }
        incProgressCounter();

        // Gestion de l'abort
        if (isAborted()) {
            return false;
        }

        if (mandat.isEstComptabiliteAVS().booleanValue()
                && (bouclement.isBouclementMensuelAVS().booleanValue() || bouclement.isBouclementAnnuelAVS()
                        .booleanValue())) {
            try {
                lissageSecteur_1990();
            } catch (Exception e) {
                this.error("LISSAGE_SECTEUR_1990_ERROR", e.getMessage());
                this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
                return false;
            }

            this.info("LISSAGE_SECTEUR_1990_OK");
        }
        incProgressCounter();

        // Gestion de l'abort
        if (isAborted()) {
            return false;
        }

        if (mandat.isEstComptabiliteAVS().booleanValue() && bouclement.isBouclementAnnuelAVS().booleanValue()) {
            try {
                clotureAnnuelleAVSCompteAdministrationExploitation();
            } catch (Exception e) {
                this.error("CLOTURE_ANUNELLE_AVS_COMPTE_ADMIN_EXPLOIT_ERROR", e.getMessage());
                this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
                return false;
            }

            this.info("CLOTURE_ANUNELLE_AVS_COMPTE_ADMIN_EXPLOIT_OK");
        }
        incProgressCounter();

        // Gestion de l'abort
        if (isAborted()) {
            return false;
        }

        if (!mandat.isEstComptabiliteAVS().booleanValue() && !bouclement.isBouclementAnnuelAVS().booleanValue()
                && periode.isAnnuel()) {
            // Bug 5443
            try {
                clotureComptesNonBilanNonAvs();
                incProgressCounter();
                clotureAndReouvertureComptesBilanNonAvs();
                incProgressCounter();
            } catch (Exception e) {
                this.error(CGPeriodeComptableBouclementProcess.LABEL_CLOTURE_ANUNELLE_COMPTE_BILAN_ERROR,
                        e.getMessage());
                this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
                return false;
            }
            this.info(CGPeriodeComptableBouclementProcess.LABEL_CLOTURE_ANUNELLE_COMPTE_BILAN_OK);
        } else {
            incProgressCounter();
            incProgressCounter();
        }

        // Gestion de l'abort
        if (isAborted()) {
            return false;
        }

        if (mandat.isEstComptabiliteAVS().booleanValue() && bouclement.isBouclementAnnuelAVS().booleanValue()) {
            try {
                clotureOuvertureAnnuelleAVSCompteBilan();
            } catch (Exception e) {
                this.error(CGPeriodeComptableBouclementProcess.LABEL_CLOTURE_ANUNELLE_COMPTE_BILAN_ERROR,
                        e.getMessage());
                this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
                return false;
            }

            this.info(CGPeriodeComptableBouclementProcess.LABEL_CLOTURE_ANUNELLE_COMPTE_BILAN_OK);
        }
        incProgressCounter();

        // Gestion de l'abort
        if (isAborted()) {
            return false;
        }

        if (mandat.isEstComptabiliteAVS().booleanValue() && bouclement.isBouclementAnnuelAVS().booleanValue()) {
            try {
                equilibrageCompteChargeProduitSecteur2();
            } catch (Exception e) {
                this.error("CLOTURE_ANUNELLE_AVS_EQUILIBR_CPT_SECT_2_ERROR", e.getMessage());
                this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
                return false;
            }

            this.info("CLOTURE_ANUNELLE_AVS_EQUILIBR_CPT_SECT_2_OK");
        }
        incProgressCounter();

        // Gestion de l'abort
        if (isAborted()) {
            return false;
        }

        try {
            comptabilisationJournauxCloture();
            incProgressCounter();
        } catch (Exception e) {
            this.error("COMPTABILISATION_JOURNAUX_ERROR", e.getMessage());
            this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
            return false;
        }

        this.info("COMPTABILISATION_JOURNAUX_OK");

        // Gestion de l'abort
        if (isAborted()) {
            return false;
        }

        try {
            periode.setEstCloture(new Boolean(true));
            periode.update();

            if (periode.isAnnuel()) {

                // cloture de la periode de cloture
                if (periodeClot != null) {
                    periodeClot.setEstCloture(new Boolean(true));
                    periodeClot.update(transaction);
                }

                exercice.setEstCloture(new Boolean(true));
                exercice.update(transaction);
            }
        } catch (Exception e) {
            this.error("MISE_A_JOUR_ERROR", e.getMessage());
            this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
            return false;
        }

        // //Edition des listes
        // if (nbErrors==0) {
        // try {
        // editionListes();
        // } catch (Exception e) {
        // error("EDITION_LISTES_ERROR", e.getMessage());
        // info("FIN");
        // return false;
        // }
        //
        // info("EDITION_LISTES_OK");
        // }

        this.info(CGPeriodeComptableBouclementProcess.INFO_FIN);
        incProgressCounter();
        return true;
    }

    private void creationJournalPandemie() throws Exception {
        Map<String, FWCurrency> cumulDoit218x = new HashMap<>();
        Map<String, FWCurrency> cumulAvoir218x = new HashMap<>();
        int nbreEcriture = 0;
        CGPlanComptableManager planManager = new CGPlanComptableManager();
        CGEcritureListViewBean ecriManager = null;
        planManager.setSession(getSession());
        planManager.setFromSecteur(CGPeriodeComptableBouclementProcess.SECTEUR_EXPLOITATION_2180);
        planManager.setUntilSecteur(CGPeriodeComptableBouclementProcess.SECTEUR_EXPLOITATION_2189);
        planManager.setForIdMandat(exercice.getIdMandat());
        planManager.setForEstPeriode(new Boolean(true));
        planManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        planManager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
        planManager.setReqDomaine(CGCompte.CS_COMPTE_EXPLOITATION);
        planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        for (int i = 0; (i < planManager.size()) && !isAborted(); i++) {
            CGPlanComptableViewBean plan = (CGPlanComptableViewBean) planManager.getEntity(i);

            CGJournalManager journalManager = new CGJournalManager();
            journalManager.setSession(getSession());
            journalManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
            journalManager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
            // journalManager.setExceptIdJournal(journalClot.getIdJournal());
            journalManager.setExceptIdEtat(ICGJournal.CS_ETAT_ANNULE);
            // ***********************************************************************************
            // COMPLETEMENT FAUX !!!
            journalManager.setExceptIdTypeJournal(CGJournal.CS_TYPE_SYSTEME);
            // ***********************************************************************************

            journalManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            for (int j = 0; j < journalManager.size(); j++) {
                CGJournal journal = (CGJournal) journalManager.getEntity(j);
                ecriManager = new CGEcritureListViewBean();
                ecriManager.setSession(getSession());
                ecriManager.setForIdCompte(plan.getIdCompte());
                ecriManager.setForIdJournal(journal.getIdJournal());
                ecriManager.setForIdMandat(exercice.getIdMandat());
                ecriManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
                ecriManager.wantForEstActive(true);
                ecriManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
                nbreEcriture = nbreEcriture + ecriManager.size();
                for (int k = 0; k < ecriManager.size(); k++) {
                    //cumulDoit
                    if (!cumulDoit218x.containsKey(plan.getIdSecteurAVS())) {
                        cumulDoit218x.put(plan.getIdSecteurAVS(), new FWCurrency());
                    }
                    FWCurrency cumulCurrency = cumulDoit218x.get(plan.getIdSecteurAVS());
                    cumulCurrency.add(((CGEcritureViewBean) ecriManager.getEntity(k)).getDoit());
                    cumulDoit218x.put(plan.getIdSecteurAVS(), cumulCurrency);
                    //cumulAvoir
                    if (!cumulAvoir218x.containsKey(plan.getIdSecteurAVS())) {
                        cumulAvoir218x.put(plan.getIdSecteurAVS(), new FWCurrency());
                    }
                    cumulCurrency = cumulAvoir218x.get(plan.getIdSecteurAVS());
                    cumulCurrency.add(((CGEcritureViewBean) ecriManager.getEntity(k)).getAvoir());
                    cumulAvoir218x.put(plan.getIdSecteurAVS(), cumulCurrency);
                }
            }
            setProgressDescription("idPlan:" + plan.getId());
        }
        if (isAborted()) {
            return;
        }
        if (nbreEcriture > 0) {
            annulationJournalPandemie();
            ouvertureJournalClotPandemie();
            for (String key : cumulDoit218x.keySet()) {
                FWCurrency currencyDoit218x = cumulDoit218x.get(key);
                if (!currencyDoit218x.isZero()) {
                    String libelle = getLibelleToFit(50,
                            label("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_LABEL_ECRITURE_CUMUL_DOIT_AVS_PANDEMIE"));
                    addEcritureDoubleToJournalPandemie(libelle, currencyDoit218x, key + ".4900.0000", "2000.2101.0000");
                    this.info("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ECRITURE_CUMUL_DOIT_AVS_OK");
                } else {
                    this.warn("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ECRITURE_CUMUL_DOIT_AVS_ERROR");
                }
            }
            for (String key : cumulAvoir218x.keySet()) {
                FWCurrency currencyAvoir218x = cumulAvoir218x.get(key);
                if (!currencyAvoir218x.isZero()) {
                    String libelle = getLibelleToFit(50,
                            label("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_LABEL_ECRITURE_CUMUL_AVOIR_AVS_PANDEMIE"));
                    addEcritureDoubleToJournalPandemie(libelle, currencyAvoir218x, "2000.2101.0000", key + ".3900.0000");
                    this.info("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ECRITURE_CUMUL_AVOIR_AVS_OK");
                } else {
                    this.warn("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ECRITURE_CUMUL_AVOIR_AVS_ERROR");
                }
            }
        }
    }


    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {
        if ((getIdPeriodeComptable() == null) || getIdPeriodeComptable().equals("")
                || getIdPeriodeComptable().equals("0")) {
            this._addError(global("PERIODE_INVALID"));
            return;
        }

        if (!init()) {
            return;
        }

        if (getParent() == null) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
            } else {
                setSendCompletionMail(true);
            }

            setControleTransaction(true);
        }

        if (getSession().hasErrors()) {
            abort();
        }
    }

    /**
     * Ajoute les écriture pour cloture annuelle AVS des autres tâches.
     * 
     * @param secteur
     * @param idCompteResultatCharge
     * @param idCompteResultatProduit
     * @param forDomaine
     * @throws Exception
     */
    private void addEcritureClotureAnnuelleAVSCompteAdministrationExploitation(CGSecteurAVS secteur,
            String idCompteResultatCharge, String idCompteResultatProduit, String forDomaine) throws Exception {
        // Parcours des comptes de charge pour le secteur et cumul des soldes :
        CGCompteManager compteManager = new CGCompteManager();
        compteManager.setSession(getSession());
        compteManager.setForIdMandat(mandat.getIdMandat());
        compteManager.setForIdSecteurAVS(secteur.getIdSecteurAVS());
        compteManager.setForIdDomaine(forDomaine);

        compteManager.setForIdGenre(CGCompte.CS_GENRE_CHARGE);
        compteManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        for (int j = 0; j < compteManager.size(); j++) {
            CGCompte compte = (CGCompte) compteManager.getEntity(j);
            FWCurrency solde = CGSolde.computeSoldeCumule(exercice.getIdExerciceComptable(), compte.getIdCompte(),
                    periode.getIdPeriodeComptable(), "0", getSession(), true);
            FWCurrency soldeMonnaieEtrangere = CGSolde.computeSoldeCumuleMonnaie(exercice.getIdExerciceComptable(),
                    compte.getIdCompte(), periode.getIdPeriodeComptable(), "0", getSession(), true);

            if (!solde.isZero()) {

                // ouverture du journal de clôture de la période de clôture (idJournal3)
                ouvertureJournalClot3();

                // transfert de solde sur le compte de résultat :
                String libelle = getLibelleToFit(50,
                        label("CLOTURE_ANUNELLE_AVS_COMPTE_ADMIN_EXPLOIT_LABEL_ECRITURE_TRANSFERT"));
                addEcritureDoubleToJournal3(libelle, solde, soldeMonnaieEtrangere, compte.getIdCompte(),
                        idCompteResultatCharge);
            }
        }

        compteManager.setForIdGenre(CGCompte.CS_GENRE_PRODUIT);
        compteManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        for (int j = 0; j < compteManager.size(); j++) {
            CGCompte compte = (CGCompte) compteManager.getEntity(j);
            FWCurrency solde = CGSolde.computeSoldeCumule(exercice.getIdExerciceComptable(), compte.getIdCompte(),
                    periodeClot.getIdPeriodeComptable(), "0", getSession(), true);
            FWCurrency soldeMonnaieEtrangere = CGSolde.computeSoldeCumuleMonnaie(exercice.getIdExerciceComptable(),
                    compte.getIdCompte(), periodeClot.getIdPeriodeComptable(), "0", getSession(), true);

            if (!solde.isZero()) {

                // ouverture du journal de clôture de la période de clôture (idJournal3)
                ouvertureJournalClot3();

                // transfert de solde sur le compte de résultat :
                String libelle = getLibelleToFit(50,
                        label("CLOTURE_ANUNELLE_AVS_COMPTE_ADMIN_EXPLOIT_LABEL_ECRITURE_TRANSFERT"));
                addEcritureDoubleToJournal3(libelle, solde, soldeMonnaieEtrangere, compte.getIdCompte(),
                        idCompteResultatProduit);
            }
        }
    }

    /**
     * Ajout d'un écriture double sur le journal3.
     * 
     * @param libelle
     * @param solde
     * @param idCompte1
     * @param idCompte2
     * @throws Exception
     */
    private void addEcritureDoubleToJournal3(String libelle, FWCurrency solde, FWCurrency soldeMonnaieEtrangere,
            String idCompte1, String idCompte2) throws Exception {
        addEcritures(periode.getDateFin(), periode.getIdJournal3(), periode.getIdExerciceComptable(), libelle, solde,
                soldeMonnaieEtrangere, idCompte1, idCompte2, true);
    }

    /**
     * Ajout d'un écriture double sur le journal clot.
     * 
     * @param libelle
     * @param solde
     * @param idExterneCompteCredite
     * @param idExterneCompteDebite
     * @throws Exception
     */
    private void addEcritureDoubleToJournalClot(String libelle, FWCurrency solde, String idExterneCompteCredite,
            String idExterneCompteDebite) throws Exception {
        addEcrituresDebitCredit(periode.getDateFin(), journalClot.getIdJournal(), periode.getIdExerciceComptable(),
                libelle, solde, idExterneCompteCredite, idExterneCompteDebite);
    }

    private void addEcritureDoubleToJournalPandemie(String libelle, FWCurrency solde, String idExterneCompteCredite,
                                                    String idExterneCompteDebite) throws Exception {
        addEcrituresDebitCredit(periode.getDateFin(), journalPandemie.getIdJournal(), periode.getIdExerciceComptable(),
                libelle, solde, idExterneCompteCredite, idExterneCompteDebite);
    }

    /**
     * Ajout d'une écriture en utilisant CGGestionEcritureAdd
     * 
     * @param dateFin
     * @param libelle
     * @param solde
     * @param idCompte1
     * @param idCompte2
     * @param generateDetteAvoir
     * @param idJournal
     * @throws Exception
     */
    private void addEcritures(String dateFin, String journal, String idExerciceComptable, String libelle,
            FWCurrency solde, FWCurrency soldeMonnaieEtrangere, String idCompte1, String idCompte2,
            boolean generateDetteAvoir) throws Exception {
        CGGestionEcritureViewBean ecritures = new CGGestionEcritureViewBean();
        ecritures.setSession(getSession());

        ecritures.setDateValeur(dateFin);
        ecritures.setIdJournal(journal);

        ArrayList<CGEcritureViewBean> ecrituresList = new ArrayList<CGEcritureViewBean>();

        CGEcritureViewBean ecritureCrebit = new CGEcritureViewBean();
        CGEcritureViewBean ecritureDebit = new CGEcritureViewBean();

        ecritureCrebit.setLibelle(libelle);
        ecritureDebit.setLibelle(libelle);

        if (solde.isPositive()) {
            ecritureCrebit.setIdCompte(idCompte1);
            ecritureDebit.setIdCompte(idCompte2);
        } else {
            solde.negate();
            ecritureCrebit.setIdCompte(idCompte2);
            ecritureDebit.setIdCompte(idCompte1);
        }

        ecritureCrebit.setCodeDebitCredit(CodeSystem.CS_CREDIT);
        ecritureCrebit.setMontant(solde.toString());

        if ((soldeMonnaieEtrangere != null) && !soldeMonnaieEtrangere.isZero()) {
            ecritureCrebit.setMontantMonnaie(soldeMonnaieEtrangere.toString());
        }

        ecritureDebit.setCodeDebitCredit(CodeSystem.CS_DEBIT);
        ecritureDebit.setMontant(solde.toString());

        if ((soldeMonnaieEtrangere != null) && !soldeMonnaieEtrangere.isZero()) {
            ecritureDebit.setMontantMonnaie(soldeMonnaieEtrangere.toString());
        }

        ecritureCrebit.setIdExerciceComptable(idExerciceComptable);
        ecritureDebit.setIdExerciceComptable(idExerciceComptable);
        ecritureCrebit.setIdExterneCompte(CGGestionEcritureUtils.getIdExterneCompte(getSession(), ecritureCrebit));
        ecritureDebit.setIdExterneCompte(CGGestionEcritureUtils.getIdExterneCompte(getSession(), ecritureDebit));

        ecrituresList.add(ecritureCrebit);
        ecrituresList.add(ecritureDebit);

        ecritures.setEcritures(ecrituresList);

        CGGestionEcritureAdd.addEcritures(getSession(), getTransaction(), ecritures, generateDetteAvoir);
    }

    /**
     * Ajoute une écriture pour les idexterne compte crédité et débité passées en paramètre.
     * 
     * @param dateFin
     * @param journal
     * @param idExerciceComptable
     * @param libelle
     * @param solde
     * @param idExterneCredit
     * @param idExterneDebit
     * @throws Exception
     */
    private void addEcrituresDebitCredit(String dateFin, String journal, String idExerciceComptable, String libelle,
            FWCurrency solde, String idExterneCredit, String idExterneDebit) throws Exception {
        CGGestionEcritureViewBean ecritures = new CGGestionEcritureViewBean();
        ecritures.setSession(getSession());

        ecritures.setDateValeur(dateFin);
        ecritures.setIdJournal(journal);

        ArrayList<CGEcritureViewBean> ecrituresList = new ArrayList<CGEcritureViewBean>();

        CGEcritureViewBean ecritureCrebit = new CGEcritureViewBean();
        CGEcritureViewBean ecritureDebit = new CGEcritureViewBean();
        ecritureCrebit.setLibelle(libelle);
        ecritureDebit.setLibelle(libelle);
        ecritureCrebit.setIdExerciceComptable(idExerciceComptable);
        ecritureDebit.setIdExerciceComptable(idExerciceComptable);
        ecritureCrebit.setCodeDebitCredit(CodeSystem.CS_CREDIT);
        ecritureDebit.setCodeDebitCredit(CodeSystem.CS_DEBIT);

        // Bug 5384. Si solde négatif, mettre le solde en absolu et inverser les comptes débit et crédit.
        if (solde.isNegative()) {
            solde.abs();
            ecritureCrebit.setIdExterneCompte(idExterneDebit);
            ecritureDebit.setIdExterneCompte(idExterneCredit);
            ecritureCrebit.setIdCompte(getIdCompte(idExterneDebit, idExerciceComptable));
            ecritureDebit.setIdCompte(getIdCompte(idExterneCredit, idExerciceComptable));
        } else {
            ecritureCrebit.setIdExterneCompte(idExterneCredit);
            ecritureDebit.setIdExterneCompte(idExterneDebit);
            ecritureCrebit.setIdCompte(getIdCompte(idExterneCredit, idExerciceComptable));
            ecritureDebit.setIdCompte(getIdCompte(idExterneDebit, idExerciceComptable));
        }
        ecritureCrebit.setMontant(solde.toString());
        ecritureDebit.setMontant(solde.toString());

        ecrituresList.add(ecritureCrebit);
        ecrituresList.add(ecritureDebit);

        ecritures.setEcritures(ecrituresList);
        CGGestionEcritureAdd.addEcritures(getSession(), getTransaction(), ecritures, true);
    }

    /**
     * Annulation d'un journalCG.
     * 
     * @param idJournal
     * @throws Exception
     */
    private void annulationJournal(String idJournal) throws Exception {
        CGJournal journal = new CGJournal();
        journal.setSession(getSession());
        journal.setIdJournal(idJournal);
        journal.retrieve(getTransaction());

        if (!journal.isNew() && !journal.getIdEtat().equals(ICGJournal.CS_ETAT_ANNULE)) {
            CGJournalAnnulerProcess process = new CGJournalAnnulerProcess(this);
            process.setSession(getSession());
            process.setTransaction(getTransaction());
            process.setSendCompletionMail(false);
            process.setSendMailOnError(false);
            process.setMemoryLog(getMemoryLog());
            process.setIdJournal(journal.getIdJournal());

            process.executeProcess();

            if (journal.getSession().hasErrors()) {
                throw new Exception(journal.getSession().getErrors().toString());
            }
        }
    }

    private void annulationJournalPandemie() throws Exception {
//        CGJournal journal = new CGJournal();
//        journal.setSession(getSession());
//        journal.setLibelle(getLibelleToFit(40, label("LABEL_JOURNAL_CLOTURE_PANDEMIE") + " " + periode.getFullDescription()));
//
//        journal.retrieve(getTransaction());
//

        CGJournalManager journalManager = new CGJournalManager();
        journalManager.setSession(getSession());
        journalManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        journalManager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
        journalManager.setFromLibelle(getLibelleToFit(40, label("LABEL_JOURNAL_CLOTURE_PANDEMIE") + " " + periode.getFullDescription()));
        journalManager.setUntilLibelle(getLibelleToFit(40, label("LABEL_JOURNAL_CLOTURE_PANDEMIE") + " " + periode.getFullDescription()));
        journalManager.setExceptIdEtat(ICGJournal.CS_ETAT_ANNULE);
        // ***********************************************************************************
        // COMPLETEMENT FAUX !!!
        journalManager.setExceptIdTypeJournal(CGJournal.CS_TYPE_SYSTEME);
        // ***********************************************************************************

        journalManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
            if(journalManager.size()>1){
                throw new Exception(label("ERROR_JOURNAL_DOUBLE_PANDEMIE"));
            }else if(journalManager.size()==1){
                CGJournal journal = (CGJournal) journalManager.getFirstEntity();
            CGJournalAnnulerProcess process = new CGJournalAnnulerProcess(this);
            process.setSession(getSession());
            process.setTransaction(getTransaction());
            process.setSendCompletionMail(false);
            process.setSendMailOnError(false);
            process.setMemoryLog(getMemoryLog());
            process.setIdJournal(journal.getIdJournal());
            process.executeProcess();
                if (journal.getSession().hasErrors()) {
                    throw new Exception(journal.getSession().getErrors().toString());
                }
            }

    }

    /**
     * 3. Annulation du journal de clôture précédent (commun)
     * 
     * @see U:\05 - Projets\Redévelopp.AVS\Analyse\Helios\Analyse\Helios - les traitements.htm
     * @throws Exception
     */
    private void annulationJournalPrecedent() throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(periode.getIdJournal())) {
            annulationJournal(periode.getIdJournal());
            periode.setIdJournal("0");
            periode.update(getTransaction());
        }

        if (!JadeStringUtil.isIntegerEmpty(periode.getIdJournal2())) {
            annulationJournal(periode.getIdJournal2());
            periode.setIdJournal2("0");
            periode.update(getTransaction());
        }

        if (!JadeStringUtil.isIntegerEmpty(periode.getIdJournal3())) {
            annulationJournal(periode.getIdJournal3());
            periode.setIdJournal3("0");
            periode.update(getTransaction());
        }
    }

    /**
     * 15. Clôture des comptes (bouclement de type annuel, comptabilité non AVS) <br/>
     * 17. Réouverture des soldes (bouclement de type annuel, comptabilité non AVS)
     * 
     * @see U:\05 - Projets\Redévelopp.AVS\Analyse\Helios\Analyse\Helios - les traitements.htm
     * @throws Exception
     */
    private void clotureAndReouvertureComptesBilanNonAvs() throws Exception {
        ouvertureJournalClot3();

        CGPlanComptableListViewBean planManager = new CGPlanComptableListViewBean();
        planManager.setSession(getSession());
        planManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        planManager.setForIdMandat(mandat.getIdMandat());

        planManager.setReqGenreCompte(CGCompte.CS_GENRE_CLOTURE);
        planManager.setReqDomaine(CGCompte.CS_COMPTE_BILAN);
        planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        if ((planManager.size() != 1) || (planManager.hasErrors())) {
            throw new Exception(
                    label(CGPeriodeComptableBouclementProcess.LABEL_CLOTURE_ANUNELLE_COMPTE_BILAN_COMPTE_CLOTURE_INEXISTANT));
        }

        CGPlanComptableListViewBean planOuvertureManager = new CGPlanComptableListViewBean();
        planOuvertureManager.setSession(getSession());
        planOuvertureManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        planOuvertureManager.setForIdMandat(mandat.getIdMandat());

        planOuvertureManager.setReqGenreCompte(CGCompte.CS_GENRE_OUVERTURE);
        planOuvertureManager.setReqDomaine(CGCompte.CS_COMPTE_BILAN);
        planOuvertureManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        if ((planOuvertureManager.size() != 1) || (planOuvertureManager.hasErrors())) {
            throw new Exception(
                    label(CGPeriodeComptableBouclementProcess.LABEL_REOUVERTURE_SOLDES_COMPTE_OUVERTURE_INEXISTANT));
        }

        String idCompteOuverture = ((CGPlanComptableViewBean) planOuvertureManager.getFirstEntity()).getIdCompte();
        CGPeriodeComptable periodeOuverture = null;
        // Si la période comptable est annuelle, on recherche si on trouve une période annuelle
        if (CGPeriodeComptable.CS_ANNUEL.equals(periode.getIdTypePeriode())) {
            try {
                periodeOuverture = openPeriodeOuverture(periode.getIdTypePeriode());
            } catch (CGPeriodeComptableException e) {
                // Si on ne trouve pas de période annuelle, on recherche une période mensuelle
                periodeOuverture = openPeriodeOuverture(CGPeriodeComptable.CS_MENSUEL);
            }
        } else {
            periodeOuverture = openPeriodeOuverture(periode.getIdTypePeriode());
        }

        FWCurrency totalTransfert = new FWCurrency();

        CGPlanComptableViewBean planResultat = (CGPlanComptableViewBean) planManager.getFirstEntity();

        transfertSoldeCompteActifPassifNonAvs(idCompteOuverture, periodeOuverture, totalTransfert, planResultat);

        transfertSoldeComptaResultatNonAvs(idCompteOuverture, periodeOuverture, totalTransfert, planResultat);

    }

    /**
     * 12. Clôture du compte d'exploitation / administration (bouclement annuel AVS)
     * 
     * La clôture du compte d'exploitation et d'administration consiste à solder les comptes de charges et de produits
     * par l'intermédiaire du compte de résultat.
     * 
     * @see U:\05 - Projets\Redévelopp.AVS\Analyse\Helios\Analyse\Helios - les traitements.htm
     * @throws Exception
     */
    private void clotureAnnuelleAVSCompteAdministrationExploitation() throws Exception {
        CGSecteurAVSManager secteurManager = new CGSecteurAVSManager();
        secteurManager.setSession(getSession());
        secteurManager.setForIdMandat(exercice.getIdMandat());
        secteurManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        for (int i = 0; i < secteurManager.size(); i++) {
            CGSecteurAVS secteur = (CGSecteurAVS) secteurManager.getEntity(i);
            if (!secteur.isClotureManuelle().booleanValue()) {
                if (secteur.isCompteAdministration().booleanValue() || secteur.isCompteExploitation().booleanValue()) {

                    // Récupération du compte de résultat pour le secteur :
                    // Par défaut, c'est le même compte, excepté dans le cas spécial du secteur 2, qui possède 2 comptes
                    // de résultat
                    // spécifique pour les charges et les produits

                    if (secteur.getIdSecteurAVS().charAt(0) == '9') {
                        // Secteur 9

                        CGPlanComptableListViewBean planManager = new CGPlanComptableListViewBean();
                        planManager.setSession(getSession());
                        planManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
                        planManager.setForIdMandat(mandat.getIdMandat());
                        planManager.setForIdExterne("9990.91" + secteur.getIdSecteurAVS().charAt(1)
                                + secteur.getIdSecteurAVS().charAt(2) + "." + secteur.getIdSecteurAVS().charAt(3)
                                + "000");
                        planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
                        if (planManager.size() != 1) {
                            throw (new Exception(
                                    label("CLOTURE_ANUNELLE_AVS_COMPTE_ADMIN_EXPLOIT_COMPTE_RESULTAT_INEXISTANT")
                                            + secteur.getIdSecteurAVS()));
                        }
                        String idCompteResultatCharge = ((CGPlanComptableViewBean) planManager.getFirstEntity())
                                .getIdCompte();

                        addEcritureClotureAnnuelleAVSCompteAdministrationExploitation(secteur, idCompteResultatCharge,
                                idCompteResultatCharge, CGCompte.CS_COMPTE_ADMINISTRATION);
                    } else {
                        // Secteur 1-8
                        if (secteur.isCompteAdministration().booleanValue()) {
                            clotureAnnuelleAVSCompteAdministrationExploitationNonSecteur9(secteur,
                                    CGCompte.CS_COMPTE_ADMINISTRATION);
                        }

                        if (secteur.isCompteExploitation().booleanValue()) {
                            clotureAnnuelleAVSCompteAdministrationExploitationNonSecteur9(secteur,
                                    CGCompte.CS_COMPTE_EXPLOITATION);
                        }
                    }
                }
            } else {
                this.warn("CLOTURE_ANUNELLE_AVS_COMPTE_ADMIN_EXPLOIT_CLOTURE_MANUELLE", secteur.getIdSecteurAVS());
            }
        }
    }

    /**
     * Clotue annuelle avs des comptes administration et exploitation non secteur 9
     * 
     * @param secteur
     * @param forDomaine
     * @throws Exception
     */
    private void clotureAnnuelleAVSCompteAdministrationExploitationNonSecteur9(CGSecteurAVS secteur, String forDomaine)
            throws Exception {
        CGPlanComptableListViewBean planManager = new CGPlanComptableListViewBean();
        planManager.setSession(getSession());
        planManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        planManager.setForIdMandat(mandat.getIdMandat());
        planManager.setForIdSecteurAVS(secteur.getIdSecteurResultat());
        planManager.setReqDomaine(forDomaine);

        String idCompteResultatCharge;
        String idCompteResultatProduit;

        // Cas spécial : SECTEUR 2 et 3 : possède 2 compte de résultat, un pour les charges et un pour les produits
        if ((secteur.getIdSecteurAVS().charAt(0) == '2') || (secteur.getIdSecteurAVS().charAt(0) == '3')) {

            // le compte de résultat (2990,3990) himself !!! -> on ne le traite pas, il doit déjà être équilibré
            // Cela suppose que personne n'a créer des écritures à la mano dedans.
            // if (secteur.getIdSecteurResultat()==null || "0".equals(secteur.getIdSecteurResultat())) {
            if ("2990".equals(secteur.getIdSecteurAVS()) || "3990".equals(secteur.getIdSecteurAVS())) {
                return;
            }

            planManager.setReqGenreCompte(CGCompte.CS_GENRE_CHARGE);
            planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
            if (planManager.size() != 1) {
                throw (new Exception(label("CLOTURE_ANUNELLE_AVS_COMPTE_ADMIN_EXPLOIT_COMPTE_RESULTAT_INEXISTANT")
                        + secteur.getIdSecteurAVS()));
            }
            idCompteResultatCharge = ((CGPlanComptableViewBean) planManager.getFirstEntity()).getIdCompte();
            idCompteResultatChargeSecteur2 = idCompteResultatCharge;

            planManager.setReqGenreCompte(CGCompte.CS_GENRE_PRODUIT);
            planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
            if (planManager.size() != 1) {
                throw (new Exception(label("CLOTURE_ANUNELLE_AVS_COMPTE_ADMIN_EXPLOIT_COMPTE_RESULTAT_INEXISTANT")
                        + secteur.getIdSecteurAVS()));
            }
            idCompteResultatProduit = ((CGPlanComptableViewBean) planManager.getFirstEntity()).getIdCompte();
            idCompteResultatProduitSecteur2 = idCompteResultatProduit;
        } else {
            planManager.setReqGenreCompte(CGCompte.CS_GENRE_RESULTAT);
            planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
            if (planManager.size() != 1) {
                throw (new Exception(label("CLOTURE_ANUNELLE_AVS_COMPTE_ADMIN_EXPLOIT_COMPTE_RESULTAT_INEXISTANT")
                        + secteur.getIdSecteurAVS()));
            }
            idCompteResultatCharge = ((CGPlanComptableViewBean) planManager.getFirstEntity()).getIdCompte();
            idCompteResultatProduit = idCompteResultatCharge;
        }

        addEcritureClotureAnnuelleAVSCompteAdministrationExploitation(secteur, idCompteResultatCharge,
                idCompteResultatProduit, forDomaine);

    }

    /**
     * 10. Clôture du compte des autres tâches (bouclement mensuel et annuel AVS)
     *
     * @param secteur
     * @param compteCloture
     * @param contreEcriture
     * @param estPeriode
     * @param requestDomaine
     * @param libelleDoitLabel
     * @param libelleAvoirLabel
     * @param warnDoitLabel
     * @param warnAvoirLabel
     * @throws Exception
     * @see U:\05 - Projets\Redévelopp.AVS\Analyse\Helios\Analyse\Helios - les traitements.htm
     */
    private void clotureCompteAutresTaches(CGSecteurAVS secteur, String compteCloture, String contreEcriture,
            boolean estPeriode, String requestDomaine, String libelleDoitLabel, String libelleAvoirLabel,
            String warnDoitLabel, String warnAvoirLabel) throws Exception {
        FWCurrency cumulDoit = new FWCurrency();
        FWCurrency cumulAvoir = new FWCurrency();

        CGPlanComptableManager planManager = new CGPlanComptableManager();
        planManager.setSession(getSession());
        planManager.setForIdSecteurAVS(secteur.getIdSecteurAVS());
        planManager.setForIdMandat(exercice.getIdMandat());

        planManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());

        planManager.setForEstPeriode(new Boolean(estPeriode));
        if (estPeriode) {
            planManager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
        }

        planManager.setReqDomaine(requestDomaine);

        planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        for (int j = 0; j < planManager.size(); j++) {
            CGPlanComptableViewBean plan = (CGPlanComptableViewBean) planManager.getEntity(j);
            if (plan.getIdGenre().equals(CGCompte.CS_GENRE_PRODUIT)
                    || plan.getIdGenre().equals(CGCompte.CS_GENRE_CHARGE)) {
                CGSoldeManager soldeManager = new CGSoldeManager();
                soldeManager.setSession(getSession());
                soldeManager.setForIdCompte(plan.getIdCompte());

                soldeManager.setForEstPeriode(new Boolean(estPeriode));
                if (estPeriode) {
                    soldeManager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
                }

                soldeManager.setForIdMandat(exercice.getIdMandat());
                soldeManager.setForIdExerComptable(exercice.getIdExerciceComptable());

                soldeManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
                if (soldeManager.size() != 1) {
                    warnNoPrefixe("GLOBAL_SOLDE_MANQUANT", plan.getIdExterne());
                } else {
                    cumulDoit.add(((CGSolde) soldeManager.getFirstEntity()).getDoitProvisoire());
                    cumulAvoir.add(((CGSolde) soldeManager.getFirstEntity()).getAvoirProvisoire());
                }
            }
        }

        if (!cumulDoit.isZero()) {
            String libelle = getLibelleToFit(50, label(libelleDoitLabel) + secteur.getIdSecteurAVS());
            addEcritureDoubleToJournalClot(libelle, cumulDoit, compteCloture, contreEcriture);
        } else {
            if (!JadeStringUtil.isBlank(warnDoitLabel)) {
                this.warn(warnDoitLabel, secteur.getIdSecteurAVS());
            }
        }

        if (!cumulAvoir.isZero()) {
            cumulAvoir.negate();

            String libelle = getLibelleToFit(50, label(libelleAvoirLabel) + secteur.getIdSecteurAVS());
            addEcritureDoubleToJournalClot(libelle, cumulAvoir, contreEcriture, compteCloture);
        } else {
            if (!JadeStringUtil.isBlank(warnAvoirLabel)) {
                this.warn(warnAvoirLabel, secteur.getIdSecteurAVS());
            }
        }
    }

    /**
     * 9. Clôture du compte d'exploitation AVS / AI (bouclement mensuel AVS)
     * 
     * @throws Exception
     * @see U:\05 - Projets\Redévelopp.AVS\Analyse\Helios\Analyse\Helios - les traitements.htm
     */
    private void clotureCompteExploitation_AVS_AI() throws Exception {
        clotureCompteExploitationAi();
        setProgressDescription("clotureCompteExploitationAi ok");
        if (isAborted()) {
            return;
        }
        clotureCompteExploitationAvs();
        setProgressDescription("clotureCompteExploitationAvs ok");
    }

    /**
     * 10. Clôture du compte d'exploitation / administration des autres tâches (bouclement mensuel et annuel AVS)
     * 
     * @throws Exception
     * @see U:\05 - Projets\Redévelopp.AVS\Analyse\Helios\Analyse\Helios - les traitements.htm
     */
    private void clotureCompteExploitationAdministrationAutresTaches() throws Exception {
        CGSecteurAVSManager secteurManager = new CGSecteurAVSManager();
        secteurManager.setSession(getSession());
        secteurManager.setForIdMandat(exercice.getIdMandat());
        secteurManager.setFromSecteur("4000");
        secteurManager.setUntilSecteur("9999");
        secteurManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        for (int i = 0; i < secteurManager.size(); i++) {
            CGSecteurAVS secteur = (CGSecteurAVS) secteurManager.getEntity(i);
            if (!secteur.isClotureManuelle().booleanValue()
                    && (secteur.isCompteExploitation().booleanValue() || secteur.isCompteAdministration()
                            .booleanValue())) {

                if (bouclement.isBouclementMensuelAVS().booleanValue()
                        && secteur.isCompteExploitation().booleanValue()
                        && (secteur.getIdTypeTache().equals(CGSecteurAVS.CS_CAF_AGENCE) || secteur.getIdTypeTache()
                                .equals(CGSecteurAVS.CS_AUTRE_TACHE_AGENCE))) {
                    String compteCloture = getIdExterneCompteCloture(secteur, CGCompte.CS_COMPTE_EXPLOITATION);
                    String contreEcriture = getIdExterneContreEcriture(secteur,
                            (secteur.getIdSecteurBilan().equals("0")) ? "0000" : secteur.getIdSecteurBilan()
                                    + ".2130.0000");

                    String libelleDoitLabel = "CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_LABEL_ECRITURE_EXPLOITATION";
                    String warnDoitLabel = "CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_ECRITURE_EXPLOITATION_ERROR";
                    String libelleAvoirLabel = "CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_LABEL_CONTRE_ECRITURE_EXPLOITATION";
                    String warnAvoirLabel = "CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_CONTRE_ECRITURE_EXPLOITATION_ERROR";

                    clotureCompteAutresTaches(secteur, compteCloture, contreEcriture, true,
                            CGCompte.CS_COMPTE_EXPLOITATION, libelleDoitLabel, libelleAvoirLabel, warnDoitLabel,
                            warnAvoirLabel);
                }

                if ((bouclement.isBouclementMensuelAVS().booleanValue() || bouclement.isBouclementAnnuelAVS()
                        .booleanValue())
                        && secteur.isCompteAdministration().booleanValue()
                        && (secteur.getIdTypeTache().equals(CGSecteurAVS.CS_CAF_AGENCE) || secteur.getIdTypeTache()
                                .equals(CGSecteurAVS.CS_AUTRE_TACHE_AGENCE))) {
                    String compteCloture = getIdExterneCompteCloture(secteur, CGCompte.CS_COMPTE_ADMINISTRATION);
                    String contreEcriture = getIdExterneContreEcriture(secteur,
                            (secteur.getIdSecteurBilan().equals("0")) ? "0000" : secteur.getIdSecteurBilan()
                                    + ".2130.0000");

                    String libelleDoitLabel = "CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_LABEL_ECRITURE_ADMINISTRATION_AGENCE";
                    String warnDoitLabel = "CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_ECRITURE_ADMINISTRATION_AGENCE_ERROR";
                    String libelleAvoirLabel = "CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_LABEL_CONTRE_ECRITURE_ADMINISTRATION_AGENCE";
                    String warnAvoirLabel = "CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_CONTRE_ECRITURE_ADMINISTRATION_AGENCE_ERROR";

                    clotureCompteAutresTaches(secteur, compteCloture, contreEcriture, true,
                            CGCompte.CS_COMPTE_ADMINISTRATION, libelleDoitLabel, libelleAvoirLabel, warnDoitLabel,
                            warnAvoirLabel);
                }

                if (bouclement.isBouclementAnnuelAVS().booleanValue()
                        && secteur.isCompteExploitation().booleanValue()
                        && (secteur.getIdSecteurAVS().charAt(0) != '9')
                        && (secteur.getIdTypeTache().equals(CGSecteurAVS.CS_CAF_PROPRE) || secteur.getIdTypeTache()
                                .equals(CGSecteurAVS.CS_AUTRE_TACHE_PROPRE))) {
                    String compteCloture = getIdExterneCompteCloture(secteur, CGCompte.CS_COMPTE_EXPLOITATION);

                    String contreEcriture;
                    if (secteur.getIdSecteurAVS().charAt(0) == '4') {
                        contreEcriture = getIdExterneContreEcriture(secteur,
                                (secteur.getIdSecteurBilan().equals("0")) ? "0000" : secteur.getIdSecteurBilan()
                                        + ".2140.0000");
                    } else {
                        contreEcriture = getIdExterneContreEcriture(secteur,
                                (secteur.getIdSecteurBilan().equals("0")) ? "0000" : secteur.getIdSecteurBilan()
                                        + ".2902.0000");
                    }

                    String libelleDoitLabel = "CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_LABEL_ECRITURE_ADMINISTRATION";
                    String libelleAvoirLabel = "CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_LABEL_CONTRE_ECRITURE_ADMINISTRATION";

                    clotureCompteAutresTaches(secteur, compteCloture, contreEcriture, false,
                            CGCompte.CS_COMPTE_EXPLOITATION, libelleDoitLabel, libelleAvoirLabel, null, null);
                }

                if (bouclement.isBouclementAnnuelAVS().booleanValue()
                        && secteur.isCompteAdministration().booleanValue()
                        && (secteur.getIdSecteurAVS().charAt(0) != '9')
                        && (secteur.getIdTypeTache().equals(CGSecteurAVS.CS_CAF_PROPRE) || secteur.getIdTypeTache()
                                .equals(CGSecteurAVS.CS_AUTRE_TACHE_PROPRE))) {
                    String compteCloture = getIdExterneCompteCloture(secteur, CGCompte.CS_COMPTE_ADMINISTRATION);

                    String contreEcriture;
                    if (secteur.getIdSecteurAVS().charAt(0) == '4') {
                        contreEcriture = getIdExterneContreEcriture(secteur,
                                (secteur.getIdSecteurBilan().equals("0")) ? "0000" : secteur.getIdSecteurBilan()
                                        + ".2140.0000");
                    } else {
                        contreEcriture = getIdExterneContreEcriture(secteur,
                                (secteur.getIdSecteurBilan().equals("0")) ? "0000" : secteur.getIdSecteurBilan()
                                        + ".2902.0000");
                    }

                    String libelleDoitLabel = "CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_LABEL_ECRITURE_ADMINISTRATION";
                    String libelleAvoirLabel = "CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_LABEL_CONTRE_ECRITURE_ADMINISTRATION";

                    clotureCompteAutresTaches(secteur, compteCloture, contreEcriture, false,
                            CGCompte.CS_COMPTE_ADMINISTRATION, libelleDoitLabel, libelleAvoirLabel, null, null);
                }

                if (bouclement.isBouclementAnnuelAVS().booleanValue()
                        && secteur.isCompteAdministration().booleanValue()
                        && (secteur.getIdSecteurAVS().charAt(0) == '9')) {
                    String compteCloture = "9990.91" + secteur.getIdSecteurAVS().charAt(1)
                            + secteur.getIdSecteurAVS().charAt(2) + "." + secteur.getIdSecteurAVS().charAt(3) + "000";

                    String compteContreEcriture = "";
                    CGPlanComptableManager planManager = new CGPlanComptableManager();
                    planManager.setSession(getSession());
                    planManager.setForIdMandat(exercice.getIdMandat());
                    planManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
                    planManager.setForIdExterne((secteur.getIdSecteurBilan().equals("0")) ? "0000" : secteur
                            .getIdSecteurBilan() + ".2902.0000");
                    planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

                    if (planManager.size() != 1) {
                        throw (new Exception(
                                label("CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_COMPTE_CLOTURE_INEXISTANT")
                                        + secteur.getIdSecteurBilan()));
                    }

                    compteContreEcriture = ((CGPlanComptableViewBean) planManager.getFirstEntity()).getIdExterne();

                    String libelleDoitLabel = "CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_LABEL_ECRITURE_ADMINISTRATION";
                    String libelleAvoirLabel = "CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_LABEL_CONTRE_ECRITURE_ADMINISTRATION";

                    clotureCompteAutresTaches(secteur, compteCloture, compteContreEcriture, false,
                            CGCompte.CS_COMPTE_ADMINISTRATION, libelleDoitLabel, libelleAvoirLabel, null, null);
                }

            }

        }

    }

    /**
     * Clôture compte d'exploitation AI. Secteur 3800
     * 
     * @throws Exception
     */
    private void clotureCompteExploitationAi() throws Exception {
        clotureCompteExploitationAiForSecteur("3800", "2130.3200.0000", "2130.3200.0000", new FWCurrency(),
                new FWCurrency());
        setProgressDescription("clotureCompteExploitationAiForSecteur 3800 ok");
        if (isAborted()) {
            return;
        }
        FWCurrency cumulDoit = new FWCurrency();
        FWCurrency cumulAvoir = new FWCurrency();
        clotureCompteExploitationAiForSecteur("3810", "2130.3225.0000", "2130.3225.0000", cumulDoit, cumulAvoir);
        setProgressDescription("clotureCompteExploitationAiForSecteur 3810 ok");
        if (isAborted()) {
            return;
        }
        cumulDoit = new FWCurrency();
        cumulAvoir = new FWCurrency();
        clotureCompteExploitationAiForSecteur("3820", "2130.3227.0000", "2130.3227.0000", cumulDoit, cumulAvoir);
        setProgressDescription("clotureCompteExploitationAiForSecteur 3820 ok");
        if (isAborted()) {
            return;
        }
        cumulDoit = new FWCurrency();
        cumulAvoir = new FWCurrency();
        clotureCompteExploitationAiForSecteur("3830", "2130.3228.0000", "2130.3228.0000", cumulDoit, cumulAvoir);
        setProgressDescription("clotureCompteExploitationAiForSecteur 3830 ok");
    }

    /**
     * Clôture compte d'exploitation AI par secteur.
     * 
     * @param idExterneSecteur
     * @param idExterneCompteDebite
     * @param idExterneCompteCrediteCumul
     * @param cumulDoit
     * @param cumulAvoir
     * @throws Exception
     */
    private void clotureCompteExploitationAiForSecteur(String idExterneSecteur, String idExterneCompteDebite,
            String idExterneCompteCrediteCumul, FWCurrency cumulDoit, FWCurrency cumulAvoir) throws Exception {
        CGPlanComptableManager planManager = new CGPlanComptableManager();
        planManager.setSession(getSession());

        planManager.setForIdMandat(exercice.getIdMandat());
        planManager.setForEstPeriode(new Boolean(true));
        planManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        planManager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
        planManager.setReqDomaine(CGCompte.CS_COMPTE_ADMINISTRATION);

        planManager.setBeginWithIdExterne(idExterneSecteur);
        planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        for (int i = 0; i < planManager.size(); i++) {
            CGPlanComptableViewBean plan = (CGPlanComptableViewBean) planManager.getEntity(i);

            CGJournalManager journalManager = new CGJournalManager();
            journalManager.setSession(getSession());
            journalManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
            journalManager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
            journalManager.setExceptIdJournal(journalClot.getIdJournal());

            // ***********************************************************************************
            // COMPLETEMENT FAUX !!!
            journalManager.setExceptIdTypeJournal(CGJournal.CS_TYPE_SYSTEME);
            // ***********************************************************************************

            journalManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            for (int j = 0; j < journalManager.size(); j++) {
                CGJournal journal = (CGJournal) journalManager.getEntity(j);
                CGEcritureListViewBean ecriManager = new CGEcritureListViewBean();
                ecriManager.setSession(getSession());
                ecriManager.setForIdCompte(plan.getIdCompte());
                ecriManager.setForIdJournal(journal.getIdJournal());
                ecriManager.setForIdMandat(exercice.getIdMandat());
                ecriManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
                ecriManager.wantForEstActive(true);
                ecriManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
                for (int k = 0; k < ecriManager.size(); k++) {
                    cumulDoit.add(((CGEcritureViewBean) ecriManager.getEntity(k)).getDoit());
                    cumulAvoir.add(((CGEcritureViewBean) ecriManager.getEntity(k)).getAvoir());
                }
            }
        }

        if (!cumulDoit.isZero()) {
            String libelle = getLibelleToFit(50,
                    label("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_LABEL_ECRITURE_CUMUL_DOIT_AI"));
            addEcritureDoubleToJournalClot(libelle, cumulDoit, "3990.6900.0000", idExterneCompteDebite);
            this.info("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ECRITURE_CUMUL_DOIT_AI_OK");
        } else {
            this.warn("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ECRITURE_CUMUL_DOIT_AI_ERROR");
        }

        if (!cumulAvoir.isZero()) {
            String libelle = getLibelleToFit(50,
                    label("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_LABEL_ECRITURE_CUMUL_AVOIR_AI"));
            addEcritureDoubleToJournalClot(libelle, cumulAvoir, idExterneCompteCrediteCumul, "3990.5900.0000");
            this.info("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ECRITURE_CUMUL_AVOIR_AI_OK");
        } else {
            this.warn("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ECRITURE_CUMUL_AVOIR_AI_ERROR");
        }
    }

    /**
     * Clôture compte d'exploitation AVS.
     * 
     * @throws Exception
     */
    private void clotureCompteExploitationAvs() throws Exception {
        FWCurrency cumulDoit = new FWCurrency();
        FWCurrency cumulAvoir = new FWCurrency();
        Map<String,FWCurrency> cumulDoit218x = new HashMap<>();
        Map<String,FWCurrency> cumulAvoir218x = new HashMap<>();

        CGPlanComptableManager planManager = new CGPlanComptableManager();
        planManager.setSession(getSession());
        planManager.setFromSecteur(CGPeriodeComptableBouclementProcess.SECTEUR_EXPLOITATION_2110);
        planManager.setUntilSecteur(CGPeriodeComptableBouclementProcess.SECTEUR_EXPLOITATION_UNTIL);
        planManager.setForIdMandat(exercice.getIdMandat());
        planManager.setForEstPeriode(new Boolean(true));
        planManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        planManager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
        planManager.setReqDomaine(CGCompte.CS_COMPTE_EXPLOITATION);
        planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        for (int i = 0; (i < planManager.size()) && !isAborted(); i++) {
            CGPlanComptableViewBean plan = (CGPlanComptableViewBean) planManager.getEntity(i);

            CGJournalManager journalManager = new CGJournalManager();
            journalManager.setSession(getSession());
            journalManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
            journalManager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
            // journalManager.setExceptIdJournal(journalClot.getIdJournal());
            journalManager.setExceptIdEtat(ICGJournal.CS_ETAT_ANNULE);

            // ***********************************************************************************
            // COMPLETEMENT FAUX !!!
            journalManager.setExceptIdTypeJournal(CGJournal.CS_TYPE_SYSTEME);
            // ***********************************************************************************

            journalManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            for (int j = 0; j < journalManager.size(); j++) {
                CGJournal journal = (CGJournal) journalManager.getEntity(j);

                CGEcritureListViewBean ecriManager = new CGEcritureListViewBean();
                ecriManager.setSession(getSession());
                ecriManager.setForIdCompte(plan.getIdCompte());
                ecriManager.setForIdJournal(journal.getIdJournal());
                ecriManager.setForIdMandat(exercice.getIdMandat());
                ecriManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
                ecriManager.wantForEstActive(true);
                ecriManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
                for (int k = 0; k < ecriManager.size(); k++) {
                    String isPandemie = JadePropertiesService.getInstance().getProperty("helios.pandemie.isSecteur_218X");
                    if(isPandemie== null){
                        throw new Exception(label("PROPERTY_PANDEMIE"));
                    }
                    if(isPandemie.equals("true")){
                        if(plan.getIdSecteurAVS().substring(0,3).contains("218")){
                            //cumulDoit
                            if(!cumulDoit218x.containsKey(plan.getIdSecteurAVS())){
                                cumulDoit218x.put(plan.getIdSecteurAVS(),new FWCurrency());
                            }
                            FWCurrency cumulCurrency = cumulDoit218x.get(plan.getIdSecteurAVS());
                            cumulCurrency.add(((CGEcritureViewBean) ecriManager.getEntity(k)).getDoit());
                            cumulDoit218x.put(plan.getIdSecteurAVS(),cumulCurrency);
                            //cumulAvoir
                            if(!cumulAvoir218x.containsKey(plan.getIdSecteurAVS())){
                                cumulAvoir218x.put(plan.getIdSecteurAVS(),new FWCurrency());
                            }
                            cumulCurrency = cumulAvoir218x.get(plan.getIdSecteurAVS());
                            cumulCurrency.add(((CGEcritureViewBean) ecriManager.getEntity(k)).getAvoir());
                            cumulAvoir218x.put(plan.getIdSecteurAVS(),cumulCurrency);
                        }else{
                    cumulDoit.add(((CGEcritureViewBean) ecriManager.getEntity(k)).getDoit());
                    cumulAvoir.add(((CGEcritureViewBean) ecriManager.getEntity(k)).getAvoir());
                }
                    }else{
                        cumulDoit.add(((CGEcritureViewBean) ecriManager.getEntity(k)).getDoit());
                        cumulAvoir.add(((CGEcritureViewBean) ecriManager.getEntity(k)).getAvoir());
                    }
                }
            }
            setProgressDescription("idPlan:" + plan.getId());
        }

        if (isAborted()) {
            return;
        }

        if (!cumulDoit.isZero()) {
            String libelle = getLibelleToFit(50,
                    label("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_LABEL_ECRITURE_CUMUL_DOIT_AVS"));
            addEcritureDoubleToJournalClot(libelle, cumulDoit, "2990.4900.0000", "2000.2100.0000");
            this.info("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ECRITURE_CUMUL_DOIT_AVS_OK");
        } else {
            this.warn("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ECRITURE_CUMUL_DOIT_AVS_ERROR");
        }

        if (!cumulAvoir.isZero()) {
            String libelle = getLibelleToFit(50,
                    label("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_LABEL_ECRITURE_CUMUL_AVOIR_AVS"));
            addEcritureDoubleToJournalClot(libelle, cumulAvoir, "2000.2100.0000", "2990.3900.0000");
            this.info("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ECRITURE_CUMUL_AVOIR_AVS_OK");
        } else {
            this.warn("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ECRITURE_CUMUL_AVOIR_AVS_ERROR");
        }
        for(String key : cumulDoit218x.keySet()){
            FWCurrency currencyDoit218x = cumulDoit218x.get(key);
            if (!currencyDoit218x.isZero()) {
                String libelle = getLibelleToFit(50,
                        label("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_LABEL_ECRITURE_CUMUL_DOIT_AVS_PANDEMIE"));
                addEcritureDoubleToJournalClot(libelle, currencyDoit218x, key+".4900.0000", "2000.2101.0000");
                this.info("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ECRITURE_CUMUL_DOIT_AVS_OK");
            } else {
                this.warn("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ECRITURE_CUMUL_DOIT_AVS_ERROR");
            }
        }
        for(String key : cumulAvoir218x.keySet()){
            FWCurrency currencyAvoir218x = cumulAvoir218x.get(key);
            if (!currencyAvoir218x.isZero()) {
                String libelle = getLibelleToFit(50,
                        label("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_LABEL_ECRITURE_CUMUL_AVOIR_AVS_PANDEMIE"));
                addEcritureDoubleToJournalClot(libelle, currencyAvoir218x, "2000.2101.0000", key+".3900.0000");
                this.info("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ECRITURE_CUMUL_AVOIR_AVS_OK");
            } else {
                this.warn("CLOTURE_COMPTE_EXPLOITATION_AVS_AI_ECRITURE_CUMUL_AVOIR_AVS_ERROR");
            }
        }
    }

    /**
     * 13. Clôture des comptes d'exploitation, d'administration, d'investissement (autres comptabilités)
     * 
     * @see U:\05 - Projets\Redévelopp.AVS\Analyse\Helios\Analyse\Helios - les traitements.htm
     * @throws Exception
     */
    private void clotureComptesNonBilanNonAvs() throws Exception {
        ouvertureJournalClot3();

        CGPlanComptableListViewBean planManager = new CGPlanComptableListViewBean();
        planManager.setSession(getSession());
        planManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        planManager.setForIdMandat(mandat.getIdMandat());

        planManager.setReqGenreCompte(CGCompte.CS_GENRE_RESULTAT);
        planManager.setReqDomaineNot(CGCompte.CS_COMPTE_BILAN);
        planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        if ((planManager.size() != 1) || (planManager.hasErrors())) {
            throw new Exception(
                    label(CGPeriodeComptableBouclementProcess.LABEL_CLOTURE_ANUNELLE_COMPTE_BILAN_COMPTE_CLOTURE_INEXISTANT));
        }

        FWCurrency totalTransfert = new FWCurrency();

        CGPlanComptableViewBean planResultat = (CGPlanComptableViewBean) planManager.getFirstEntity();

        CGCompteManager compteManager = new CGCompteManager();
        compteManager.setSession(getSession());
        compteManager.setForIdMandat(mandat.getIdMandat());
        compteManager.setForIdDomaine(planResultat.getIdDomaine());

        ArrayList<String> genreCompte = new ArrayList<String>();
        genreCompte.add(CGCompte.CS_GENRE_CHARGE);
        genreCompte.add(CGCompte.CS_GENRE_PRODUIT);

        compteManager.setForIdGenreIn(genreCompte);
        compteManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        for (int j = 0; j < compteManager.size(); j++) {
            CGCompte compte = (CGCompte) compteManager.getEntity(j);
            FWCurrency solde = CGSolde.computeSoldeCumule(exercice.getIdExerciceComptable(), compte.getIdCompte(),
                    periode.getIdPeriodeComptable(), "0", getSession(), true);
            FWCurrency soldeMonnaieEtrangere = CGSolde.computeSoldeCumuleMonnaie(exercice.getIdExerciceComptable(),
                    compte.getIdCompte(), periode.getIdPeriodeComptable(), "0", getSession(), true);

            if (!solde.isZero()) {
                totalTransfert.add(solde);

                transfertSoldeToCompteCloture(planResultat.getIdCompte(), compte, solde, soldeMonnaieEtrangere, true);
            }
        }

    }

    /**
     * Method clotureOuvertureAnnuelleAVSCompteBilan.
     * 
     * La clôture des comptes consiste à solder tous les comptes de bilan par l'intermédiaire d'un compte de clôture.
     * 
     * <br/>
     * <br/>
     * 14. Clôture des comptes (bouclement annuel AVS) <br/>
     * 16. Réouverture des solde (bouclement annuel AVS)
     * 
     * @see U:\05 - Projets\Redévelopp.AVS\Analyse\Helios\Analyse\Helios - les traitements.htm
     * @throws Exception
     */
    private void clotureOuvertureAnnuelleAVSCompteBilan() throws Exception {

        FWCurrency totalTransfert = new FWCurrency();
        CGSecteurAVSManager secteurManager = new CGSecteurAVSManager();
        secteurManager.setSession(getSession());
        secteurManager.setForIdMandat(exercice.getIdMandat());
        secteurManager.setExistCompteBilan(new Boolean(true));
        secteurManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        // ouverture du journal de clôture de la période de clôture (idJournal3)
        ouvertureJournalClot3();

        CGPeriodeComptable periodeOuverture = openPeriodeOuverture(CGPeriodeComptable.CS_MENSUEL);

        for (int i = 0; i < secteurManager.size(); i++) {
            CGSecteurAVS secteur = (CGSecteurAVS) secteurManager.getEntity(i);
            if (!secteur.isClotureManuelle().booleanValue()) {

                // Récupération du compte de clôture pour le secteur :
                CGPlanComptableListViewBean planManager = new CGPlanComptableListViewBean();
                planManager.setSession(getSession());
                planManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
                planManager.setForIdMandat(mandat.getIdMandat());
                planManager.setForIdSecteurAVS(secteur.getIdSecteurBilan());

                planManager.setReqGenreCompte(CGCompte.CS_GENRE_CLOTURE);
                planManager.setReqDomaine(CGCompte.CS_COMPTE_BILAN);
                planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

                if (planManager.size() != 1) {
                    throw (new Exception(
                            label(CGPeriodeComptableBouclementProcess.LABEL_CLOTURE_ANUNELLE_COMPTE_BILAN_COMPTE_CLOTURE_INEXISTANT)
                                    + secteur.getIdSecteurBilan()));
                }
                String idCompteCloture = ((CGPlanComptableViewBean) planManager.getFirstEntity()).getIdCompte();

                // Récupération du compte d'ouverture pour le secteur :
                planManager.setReqGenreCompte(CGCompte.CS_GENRE_OUVERTURE);
                planManager.setReqDomaine(CGCompte.CS_COMPTE_BILAN);
                planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
                if (planManager.size() != 1) {
                    throw (new Exception(
                            label(CGPeriodeComptableBouclementProcess.LABEL_REOUVERTURE_SOLDES_COMPTE_OUVERTURE_INEXISTANT)
                                    + secteur.getIdSecteurBilan()));
                }
                String idCompteOuverture = ((CGPlanComptableViewBean) planManager.getFirstEntity()).getIdCompte();

                // Parcours des comptes de actif pour le secteur et cumul des soldes :
                CGCompteManager compteManager = new CGCompteManager();
                compteManager.setSession(getSession());
                compteManager.setForIdMandat(mandat.getIdMandat());
                compteManager.setForIdSecteurAVS(secteur.getIdSecteurAVS());
                compteManager.setForIdDomaine(CGCompte.CS_COMPTE_BILAN);

                compteManager.setForIdGenre(CGCompte.CS_GENRE_ACTIF);
                compteManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
                for (int j = 0; j < compteManager.size(); j++) {
                    CGCompte compte = (CGCompte) compteManager.getEntity(j);
                    FWCurrency solde = CGSolde.computeSoldeCumule(exercice.getIdExerciceComptable(),
                            compte.getIdCompte(), periode.getIdPeriodeComptable(), "0", getSession(), true);
                    FWCurrency soldeMonnaieEtrangere = CGSolde.computeSoldeCumuleMonnaie(
                            exercice.getIdExerciceComptable(), compte.getIdCompte(), periode.getIdPeriodeComptable(),
                            "0", getSession(), true);

                    if (!solde.isZero()) {

                        // somme de tous les transferts
                        totalTransfert.add(solde);

                        solde = transfertSoldeToCompteCloture(idCompteCloture, compte, solde, soldeMonnaieEtrangere,
                                false);
                        transfertSoldeToCompteOuverture(periodeOuverture, idCompteOuverture, compte, solde,
                                soldeMonnaieEtrangere, false);
                    }
                }

                compteManager.setForIdGenre(CGCompte.CS_GENRE_PASSIF);
                compteManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
                for (int j = 0; j < compteManager.size(); j++) {
                    CGCompte compte = (CGCompte) compteManager.getEntity(j);
                    FWCurrency solde = CGSolde.computeSoldeCumule(exercice.getIdExerciceComptable(),
                            compte.getIdCompte(), periode.getIdPeriodeComptable(), "0", getSession(), true);
                    FWCurrency soldeMonnaieEtrangere = CGSolde.computeSoldeCumuleMonnaie(
                            exercice.getIdExerciceComptable(), compte.getIdCompte(), periode.getIdPeriodeComptable(),
                            "0", getSession(), true);

                    if (!solde.isZero()) {

                        // somme de tous les transferts
                        totalTransfert.add(solde);

                        solde = transfertSoldeToCompteCloture(idCompteCloture, compte, solde, soldeMonnaieEtrangere,
                                false);
                        transfertSoldeToCompteOuverture(periodeOuverture, idCompteOuverture, compte, solde,
                                soldeMonnaieEtrangere, false);
                    }
                }

            } else {
                this.warn("CLOTURE_ANUNELLE_AVS_COMPTE_BILAN_CLOTURE_MANUELLE", secteur.getIdSecteurAVS());
            }
        }

        if (!totalTransfert.isZero()) {
            throw (new Exception(
                    label(CGPeriodeComptableBouclementProcess.LABEL_CLOTURE_ANUNELLE_COMPTE_BILAN_TOTAL_TRANSFERT_NON_NUL)));
        }
    }

    /**
     * Method comptabilisationJournauxCloture.
     * 
     * Comptabilisation des journaux de clôture. Les journaux dans l'état ANNULE ne sont pas comptabilisé.
     * 
     * <br/>
     * <br/>
     * 18. Comptabilisation définitive des journaux (commune)
     * 
     * @see U:\05 - Projets\Redévelopp.AVS\Analyse\Helios\Analyse\Helios - les traitements.htm
     * @throws Exception
     */
    private void comptabilisationJournauxCloture() throws Exception {
        if ((journalClot != null) && !journalClot.isNew() && !ICGJournal.CS_ETAT_ANNULE.equals(journalClot.getIdEtat())) {
            journalClot.comptabiliser(this);
        }

        if ((journalClot2 != null) && !journalClot2.isNew()
                && !ICGJournal.CS_ETAT_ANNULE.equals(journalClot2.getIdEtat())) {
            journalClot2.comptabiliser(this);
        } else if (bouclement.isBouclementAnnuelAVS().booleanValue() && (journalExtourne != null)
                && !journalExtourne.isNew() && !ICGJournal.CS_ETAT_ANNULE.equals(journalExtourne.getIdEtat())) {
            journalExtourne.comptabiliser(this);
        }

        if ((journalClot3 != null) && !journalClot3.isNew()
                && !ICGJournal.CS_ETAT_ANNULE.equals(journalClot3.getIdEtat())) {
            journalClot3.comptabiliser(this);
        }
    }

    /**
     * Comptabiliser les journaux de l'exercice comptable encore ouvert sur la periode.
     * 
     * @throws Exception
     */
    private void comptabiliserJournaux() throws Exception {
        CGJournalManager journalMgr = new CGJournalManager();
        journalMgr.setSession(getSession());
        journalMgr.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        journalMgr.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
        journalMgr.setForIdEtat(ICGJournal.CS_ETAT_OUVERT);
        journalMgr.find(getTransaction(), BManager.SIZE_NOLIMIT);

        for (int i = 0; i < journalMgr.size(); i++) {
            // Gestion de l'abort
            if (isAborted()) {
                return;
            }
            CGJournal journal = (CGJournal) journalMgr.getEntity(i);
            journal.comptabiliser(getTransaction());
        }

        // Tous les journaux de la période doivent être soit comptabilisé soit dans l'état annulé.
        // Les journaux dans l'état annulé seront ignoré et non comptabilisé
        journalMgr.setForIdEtat("");
        journalMgr.find(getTransaction(), BManager.SIZE_NOLIMIT);
        for (int i = 0; i < journalMgr.size(); i++) {
            // Gestion de l'abort
            if (isAborted()) {
                return;
            }
            if (!ICGJournal.CS_ETAT_COMPTABILISE.equals(((CGJournal) journalMgr.getEntity(i)).getIdEtat())
                    && !ICGJournal.CS_ETAT_ANNULE.equals(((CGJournal) journalMgr.getEntity(i)).getIdEtat())) {

                throw new Exception("Il reste des journaux non comptabilisé (etat en erreur ou partiel).");
            }
        }
    }

    /**
     * 2. Contrôle du compte 1106 et 2740 (bouclement mensuel AVS)
     * 
     *
     * @see U:\05 - Projets\Redévelopp.AVS\Analyse\Helios\Analyse\Helios - les traitements.htm
     * @throws Exception
     */
    private void controleCompte_1106_2740() throws Exception {
        CGSecteurAVSManager AVSmanager = new CGSecteurAVSManager();
        AVSmanager.setSession(getSession());
        AVSmanager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        for (int i = 0; (i < AVSmanager.size()) && !isAborted(); i++) {
            CGSecteurAVS secteur = (CGSecteurAVS) AVSmanager.getEntity(i);
            if (secteur.isCompteBilan().booleanValue()) {
                CGPlanComptableManager manager1 = new CGPlanComptableManager();
                FWCurrency solde_1106 = new FWCurrency();
                manager1.setSession(getSession());
                manager1.setForIdExterne(secteur.getIdSecteurAVS() + ".1106.0000");
                manager1.setForIdExerciceComptable(exercice.getIdExerciceComptable());
                manager1.setForEstPeriode(new Boolean(true));
                manager1.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
                manager1.find(getTransaction(), BManager.SIZE_NOLIMIT);

                // Gestion de l'abort
                if (isAborted()) {
                    return;
                }

                CGPlanComptableManager manager2 = new CGPlanComptableManager();
                FWCurrency solde_2740 = new FWCurrency();
                manager2.setSession(getSession());
                manager2.setForIdExterne(secteur.getIdSecteurAVS() + ".2740.0000");
                manager2.setForIdExerciceComptable(exercice.getIdExerciceComptable());
                manager2.setForEstPeriode(new Boolean(true));
                manager2.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
                manager2.find(getTransaction(), BManager.SIZE_NOLIMIT);

                // Gestion de l'abort
                if (isAborted()) {
                    return;
                }

                if (manager1.size() > 1) {
                    throw (new Exception(label("CTRL_1106_2740_SOLDE_1106_NON_UNIQUE")));
                }

                if (manager2.size() > 1) {
                    throw (new Exception(label("CTRL_1106_2740_SOLDE_2740_NON_UNIQUE")));
                }

                if (manager1.size() == 1) {
                    solde_1106 = new FWCurrency(((CGPlanComptableViewBean) manager1.getFirstEntity()).getSolde());
                }

                if (manager2.size() == 1) {
                    solde_2740 = new FWCurrency(((CGPlanComptableViewBean) manager2.getFirstEntity()).getSolde());
                }

                solde_2740.negate();
                if (solde_1106.compareTo(solde_2740) != 0) {
                    throw (new Exception(label("CTRL_1106_2740_SOLDES_INEGAUX")));
                }
            }
        }

    }

    /**
     * Mandat : inforom096 <br>
     * -------------------------------------------------------- <br>
     * Test si il y a des comptes passif avec un solde au debit <br>
     * ou si il y a des comptes actifs avec un solde au credit <br>
     * 
     * @throws Exception
     */
    private boolean controleCompteActifPassif() throws Exception {

        boolean auMoinsUneErreur = false;

        CGBilanListViewBean manager = new CGBilanListViewBean();
        manager.setSession(getSession());
        manager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        manager.setForEstPeriode(new Boolean(true));

        // On veut la période comptable et toutes celles depuis le début de l'année
        manager.setReqForListPeriodesComptable(periode.getIdPeriodeComptable());
        manager.setInclurePeriodesPrec(Boolean.TRUE);

        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        ArrayList<?> list = CGBilanParser.getLinesToBouclement(manager, exercice);

        for (int i = 0; (i < list.size()) && !isAborted(); i++) {
            globaz.helios.parser.CGBilanLine line = (globaz.helios.parser.CGBilanLine) list.get(i);

            FWCurrency actif = new FWCurrency(line.getSoldeActif());
            FWCurrency passif = new FWCurrency(line.getSoldePassif());

            if (CGCompte.CS_GENRE_PASSIF.equals(line.getIdGenre()) && passif.isNegative()) {
                auMoinsUneErreur = true;
                this.warn("COMPTE_PASSIF_SOLDE_DEBIT", line.getIdExterne());
            } else if (CGCompte.CS_GENRE_ACTIF.equals(line.getIdGenre()) && actif.isNegative()) {
                auMoinsUneErreur = true;
                this.warn("COMPTE_ACTIF_SOLDE_CREDIT", line.getIdExterne());
            }
        }

        // for (int i = 0; i < manager.size() && !isAborted(); i++) {
        // CGPlanComptableViewBean bean = (CGPlanComptableViewBean) manager.getEntity(i);
        //
        // FWCurrency avoir = new FWCurrency(bean.getAvoir());
        // FWCurrency doit = new FWCurrency(bean.getDoit());
        //
        // if(bean.isComptePassif() && !doit.isZero()) { //Le compte est passif, on recherche un montant au débit.
        // auMoinsUneErreur = true;
        // warn("COMPTE_PASSIF_SOLDE_DEBIT", bean.getIdExterne());
        //
        // }else if (bean.isCompteActif() && !avoir.isZero()){ //Le compte est actif, on recherche un solde au crédit
        // auMoinsUneErreur = true;
        // warn("COMPTE_ACTIF_SOLDE_CREDIT", bean.getIdExterne());
        // }
        // }

        if (auMoinsUneErreur && !getQuittancerWarning().booleanValue()) {
            this.error("CONTROLE_COMPTE_ACTIF_PASSIF_NOK");
            return false;
        }

        return true;
    }

    /**
     * Bug 5292
     * 
     * Crée les écrtures nécessaire pour annuler le montant négatif.
     * 
     * @param plan
     * @param totalDebit
     * @param totalCredit
     * @throws Exception
     */
    private void createEcrituresCorrectionMvtNegatif(CGPlanComptableViewBean plan, FWCurrency totalDebit,
            FWCurrency totalCredit) throws Exception {
        if ((journalClot != null) && !journalClot.isNew() && !ICGJournal.CS_ETAT_ANNULE.equals(journalClot.getIdEtat())) {
            // Bug 5292
            // - Utiliser le journal de cloture
            // - Créer les écritures pour passer le montant en positif

            if (totalDebit.isNegative()) {
                totalDebit.negate();
                addEcritureDoubleToJournalClot(getSession().getLabel("EXPLOITATION_TOTAL_MVT_NEGATIF_LIBELLE"),
                        totalDebit, plan.getIdExterne(), plan.getIdExterne());

                // Mettre info dans le mail : le compte et montant. Bug 5383
                getMemoryLog().logMessage(
                        getSession().getLabel("EXPLOITATION_TOTAL_MVT_NEGATIF_LIBELLE") + " : " + plan.getIdExterne()
                                + " " + totalDebit.toStringFormat(), FWMessage.INFORMATION,
                        getSession().getLabel("GLOBAL_PERIODE") + " N°" + getIdPeriodeComptable());
            }
            if (totalCredit.isNegative()) {
                totalCredit.negate();
                addEcritureDoubleToJournalClot(getSession().getLabel("EXPLOITATION_TOTAL_MVT_NEGATIF_LIBELLE"),
                        totalCredit, plan.getIdExterne(), plan.getIdExterne());

                // Mettre info dans le mail : le compte et montant. Bug 5383
                getMemoryLog().logMessage(
                        getSession().getLabel("EXPLOITATION_TOTAL_MVT_NEGATIF_LIBELLE") + " : " + plan.getIdExterne()
                                + " " + totalCredit.toStringFormat(), FWMessage.INFORMATION,
                        getSession().getLabel("GLOBAL_PERIODE") + " N°" + getIdPeriodeComptable());
            }
        } else {
            throw new Exception(getSession().getLabel("EXPLOITATION_TOTAL_MVT_DEBIT_CREDIT_NEGATIF") + " ("
                    + plan.getIdExterne() + ")");
        }
    }

    /**
     * Creation de l'exercice suivant si n'existe pas.
     * 
     * @param transaction
     * @param dateDebutExerciceSuiv
     * @param dateFinExerciceSuiv
     * @throws Exception
     */
    private void createExerciceSuivant(BTransaction transaction, JADate dateDebutExerciceSuiv,
            JADate dateFinExerciceSuiv) throws Exception {
        this.info("OUVERTURE_PERIODE_SUIVANTE_CREATION_EXERCICE");
        exerciceSuiv = new CGExerciceComptable();
        exerciceSuiv.setSession(getSession());
        exerciceSuiv.setDateDebut(dateDebutExerciceSuiv.toString());
        exerciceSuiv.setDateFin(dateFinExerciceSuiv.toString());
        exerciceSuiv.setEstCloture(new Boolean(false));
        exerciceSuiv.setIdMandat(exercice.getIdMandat());
        exerciceSuiv.add(transaction);
    }

    /**
     * Création de la période annuelle.
     * 
     * @param transaction
     * @param periodeComptable
     * @return
     * @throws Exception
     */
    private CGPeriodeComptable createPeriodeAnnuelle(BTransaction transaction, String periodeComptable)
            throws Exception {
        CGPeriodeComptable periodeAnnu = new CGPeriodeComptable();
        periodeAnnu.setSession(getSession());

        periodeAnnu.setIdTypePeriode(periodeComptable);
        periodeAnnu.setIdExerciceComptable(exercice.getIdExerciceComptable());
        periodeAnnu.setDateDebut(exercice.getDateDebut());
        periodeAnnu.setDateFin(exercice.getDateFin());

        CGBouclementManager man = new CGBouclementManager();
        man.setSession(getSession());
        man.setForIdMandat(mandat.getIdMandat());

        if (exercice.getMandat().isEstComptabiliteAVS().booleanValue()) {
            man.setForIdTypeBouclement(CGBouclement.CS_BOUCLEMENT_ANNUEL_AVS);
            man.find(transaction, BManager.SIZE_NOLIMIT);
            if (man.size() == 0) {
                throw (new Exception(label("OUVERTURE_PERIODE_SUIVANTE_BOUCLEMENT_ANNUEL_AVS_INEXISTANT")));
            }
        } else {
            man.setForIdTypeBouclement(CGBouclement.CS_BOUCLEMENT_ANNUEL);
            man.find(transaction, BManager.SIZE_NOLIMIT);
            if (man.size() == 0) {
                throw (new Exception(label("OUVERTURE_PERIODE_SUIVANTE_BOUCLEMENT_ANNUEL_INEXISTANT")));
            }
        }

        periodeAnnu.setIdBouclement(((CGBouclement) man.getFirstEntity()).getIdBouclement());
        periodeAnnu.setEstCloture(new Boolean(false));
        periodeAnnu.add(transaction);

        return periodeAnnu;
    }

    /**
     * Create periode suivant.
     * 
     * @param transaction
     * @param dateDebutPeriodeSuiv
     * @param dateFinPeriodeSuiv
     * @param idExerciceComptable
     * @throws Exception
     */
    private void createPeriodeSuivante(globaz.globall.db.BTransaction transaction, JADate dateDebutPeriodeSuiv,
            JADate dateFinPeriodeSuiv, String idExerciceComptable) throws Exception {
        CGPeriodeComptable perSuiv = new CGPeriodeComptable();
        perSuiv.setSession(getSession());
        perSuiv.setIdExerciceComptable(idExerciceComptable);
        perSuiv.setIdTypePeriode(periode.getIdTypePeriode());
        perSuiv.setIdBouclement(periode.getIdBouclement());
        perSuiv.setDateDebut(dateDebutPeriodeSuiv.toString());
        perSuiv.setDateFin(dateFinPeriodeSuiv.toString());
        perSuiv.setEstCloture(new Boolean(false));
        perSuiv.add(transaction);
        this.info("OUVERTURE_PERIODE_SUIVANTE_CREATION_PERIODE");
    }

    /**
     * Method doEcritureLissage1990.
     * 
     * Effectue les ecritures pour le lissage du secteur 1990, avec no compte se termiant par '9'
     * 
     * @param beginWithIdExterne
     *            8 premier numéro des comptes à lisser (ex. 1990.1207)
     * @param compte1990_120s_xxx9
     *            Comptes 120s à clôturer pour lissage
     * @param compte1990_220s_xxx9
     *            Comptes 220s à clôturer pour lisser
     */
    private void doEcritureLissage1990(String s, CGPlanComptableViewBean compte1990_120s_xxx9,
            CGPlanComptableViewBean compte1990_220s_xxx9, CGPeriodeComptable periode) throws Exception {
        // Calcul de la somme des soldes cumulé de tous les comptes 120s et 220s
        CGPlanComptableManager planMgr1990_120s = new CGPlanComptableManager();
        planMgr1990_120s.setSession(getSession());
        planMgr1990_120s.setForIdMandat(exercice.getIdMandat());
        planMgr1990_120s.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        planMgr1990_120s.setBeginWithIdExterne("1990.120" + s);
        planMgr1990_120s.setForIdPeriodeComptable(null);
        planMgr1990_120s.setForEstPeriode(new Boolean(true));
        planMgr1990_120s.find(getTransaction(), BManager.SIZE_NOLIMIT);

        Map<String, FWCurrency> mapComptesALisser = new HashMap<String, FWCurrency>();

        FWCurrency sommeSoldeCumul = new FWCurrency(0);
        for (int k = 0; k < planMgr1990_120s.size(); k++) {
            CGPlanComptableViewBean compte_1990_120s = (CGPlanComptableViewBean) planMgr1990_120s.getEntity(k);

            FWCurrency soldeCumule = CGSolde.computeSoldeCumule(exercice.getIdExerciceComptable(),
                    compte_1990_120s.getIdCompte(), periode.getIdPeriodeComptable(), "0", getSession(), true);
            if (!soldeCumule.isZero()) {
                mapComptesALisser.put(compte_1990_120s.getIdExterne(), soldeCumule);
                sommeSoldeCumul.add(soldeCumule);
            }
        }

        CGPlanComptableManager planMgr1990_220s = new CGPlanComptableManager();
        planMgr1990_220s.setSession(getSession());
        planMgr1990_220s.setForIdMandat(exercice.getIdMandat());
        planMgr1990_220s.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        planMgr1990_220s.setBeginWithIdExterne("1990.220" + s);
        planMgr1990_220s.setForIdPeriodeComptable(null);
        planMgr1990_220s.setForEstPeriode(new Boolean(true));
        planMgr1990_220s.find(getTransaction(), BManager.SIZE_NOLIMIT);

        for (int k = 0; k < planMgr1990_220s.size(); k++) {
            CGPlanComptableViewBean compte_1990_220s = (CGPlanComptableViewBean) planMgr1990_220s.getEntity(k);

            FWCurrency soldeCumule = CGSolde.computeSoldeCumule(exercice.getIdExerciceComptable(),
                    compte_1990_220s.getIdCompte(), periode.getIdPeriodeComptable(), "0", getSession(), true);
            if (!soldeCumule.isZero()) {
                mapComptesALisser.put(compte_1990_220s.getIdExterne(), soldeCumule);
                sommeSoldeCumul.add(soldeCumule);
            }
        }

        if (mapComptesALisser.size() > 0) {
            Set<String> keys = mapComptesALisser.keySet();

            String idExterneCpt = "";
            if (sommeSoldeCumul.isPositive()) {
                idExterneCpt = compte1990_120s_xxx9.getIdExterne();
            } else {
                idExterneCpt = compte1990_220s_xxx9.getIdExterne();
            }

            // ////////////////////////////////////////////////////////////////////////////////////////
            // Récupération de la période pour extourne
            // La période pour extourne est déjà créer par la méthode ouverturePeriodeSuiv().

            // Si période actuelle est la période anuelle, récupérer la période de janvier.
            // Si periode actuelle == décembre, récupéré la période anuelle
            // Sinon, récupérer la période suivante
            // La période ou seront extourner les contres-écritures
            CGPeriodeComptable periodeExtourne = getPeriodeExtournePourLissage(periode);
            // L'id de l'exercice ou seront extourner les contres-écritures
            String idExerciceLissage = getIdExercExtournePourLissage(periode);
            // Le journal ou seront extourner les contres-écritures
            CGJournal journalExt = ouvertureJournalExtournePourLissage(periodeExtourne, idExerciceLissage);

            for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
                String key = iter.next();

                FWCurrency solde = mapComptesALisser.get(key);
                // Bug 5689
                FWCurrency soldeNegatif = new FWCurrency(solde.doubleValue());
                soldeNegatif.negate();

                addEcrituresDebitCredit(periode.getDateDebut(), journalClot.getIdJournal(),
                        periode.getIdExerciceComptable(),
                        getLibelleToFit(50, label("LISSAGE_SECTEUR_1990_LABEL_ECRITURE_REFLETE_CUMUL")), solde, key,
                        idExterneCpt);

                addEcrituresDebitCredit(periodeExtourne.getDateFin(), journalExt.getIdJournal(),
                        periodeExtourne.getIdExerciceComptable(),
                        getLibelleToFit(50, label("LISSAGE_SECTEUR_1990_LABEL_ECRITURE_REFLETE_CUMUL")), soldeNegatif,
                        key, idExterneCpt);
            }
        }
    }

    /**
     * Equilibrage des comptes de charges et produit du secteur 2. <br/>
     * <br/>
     * Les comptes de résultat de charges et produits du secteur 2 (2990.3900.0000 & 2990.4900.0000) doivent avoir des
     * montants opposés pour la période de cloture. <br/>
     * Il faut mettre ces soldes à zéro en transferant le solde d'un compte dans l'autre.
     * 
     * @throws Exception
     */
    private void equilibrageCompteChargeProduitSecteur2() throws Exception {
        if ((idCompteResultatChargeSecteur2 != null) && (idCompteResultatProduitSecteur2 != null)) {

            FWCurrency solde = CGSolde.computeSoldeCumule(exercice.getIdExerciceComptable(),
                    idCompteResultatChargeSecteur2, periodeClot.getIdPeriodeComptable(), "0", getSession(), true);
            FWCurrency soldeMonnaieEtrangere = CGSolde.computeSoldeCumuleMonnaie(exercice.getIdExerciceComptable(),
                    idCompteResultatChargeSecteur2, periodeClot.getIdPeriodeComptable(), "0", getSession(), true);

            if (!solde.isZero()) {
                // ouverture du journal de clôture de la période de clôture (idJournal3)
                ouvertureJournalClot3();

                // transfert de solde sur le compte de résultat :
                String libelle = getLibelleToFit(50,
                        label("CLOTURE_ANUNELLE_AVS_COMPTE_ADMIN_EXPLOIT_LABEL_ECRITURE_TRANSFERT"));
                addEcritureDoubleToJournal3(libelle, solde, soldeMonnaieEtrangere, idCompteResultatProduitSecteur2,
                        idCompteResultatChargeSecteur2);
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 14:35:53)
     * 
     * @param msg String
     */
    private void error(String msg) {
        this.error(msg, "");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 14:35:53)
     * 
     * @param msg String
     */
    private void error(String codeLabel, String msg) {
        getMemoryLog().logMessage(
                getSession().getLabel(CGPeriodeComptableBouclementProcess.LABEL_PREFIX + codeLabel) + msg,
                FWViewBeanInterface.ERROR, getSession().getLabel("GLOBAL_PERIODE") + " N°" + getIdPeriodeComptable());
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (periode != null) {
            if (getMemoryLog().getErrorLevel() == FWMessage.ERREUR) {
                return label("OBJET_MAIL_ERREUR") + " " + periode.getFullDescription() + ", ID "
                        + periode.getIdPeriodeComptable();
            } else if ((getMemoryLog().getErrorLevel() == FWMessage.INFORMATION)
                    || (getMemoryLog().getErrorLevel() == FWMessage.AVERTISSEMENT) || !getMemoryLog().hasMessages()) {
                return label("OBJET_MAIL_OK") + " " + periode.getFullDescription() + ", ID "
                        + periode.getIdPeriodeComptable();
            } else {
                return label("OBJET_MAIL_FATAL") + " " + periode.getFullDescription() + ", ID "
                        + periode.getIdPeriodeComptable();
            }
        } else {
            if (getMemoryLog().hasErrors() || getSession().hasErrors()) {
                return label("OBJET_MAIL_ERREUR");
            } else {
                return label("OBJET_MAIL_OK");
            }
        }
    }

    /**
     * Retrouve l'idcompte en fonction de l'idexterne et de l'exercice comptable.
     * 
     * @param idExterne
     * @param idExerciceComptable
     * @return
     * @throws Exception
     */
    private String getIdCompte(String idExterne, String idExerciceComptable) throws Exception {
        CGPlanComptableManager manager = new CGPlanComptableManager();
        manager.setSession(getSession());

        manager.setForIdExterne(idExterne);
        manager.setForIdExerciceComptable(idExerciceComptable);

        manager.find(getTransaction());

        if (manager.hasErrors() || manager.isEmpty()) {
            throw new Exception(getSession().getLabel("AUCUN_COMPTE_RESOLU") + " " + idExterne);
        }

        return ((CGPlanComptableViewBean) manager.getFirstEntity()).getIdCompte();

    }

    /**
     * Method getIdExercExtournePourLissage.
     * <p>
     * Retourne l'id de l'exercice d'extourne pour les contre-écritures du lissage du secteur 1990
     * 
     * @param periode ou seront extourner les contres-écritures de lissage
     * @return String l'id de l'exercice comptable ou seront extourner les contre-écritures
     */
    private String getIdExercExtournePourLissage(CGPeriodeComptable periode) {
        if (periode.isMensuel()) {
            // On boucle une periode mensuelle
            return exercice.getIdExerciceComptable();
        } else {
            // On veut récupérer la période de janvier de l'exercice suivant
            return exerciceSuiv.getIdExerciceComptable();
        }
    }

    /**
     * Retourne l'id externe du compte de cloture du domaine spécifié.
     * 
     * @param secteur
     * @param domaine
     * @return
     * @throws Exception
     */
    private String getIdExterneCompteCloture(CGSecteurAVS secteur, String domaine) throws Exception {
        String compteCloture;
        CGPlanComptableManager planManager = new CGPlanComptableManager();
        planManager.setSession(getSession());
        planManager.setForIdMandat(exercice.getIdMandat());
        planManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        planManager.setForIdSecteurAVS(secteur.getIdSecteurResultat());
        planManager.setReqDomaine(domaine);
        planManager.setReqGenreCompte(CGCompte.CS_GENRE_RESULTAT);
        planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        if (planManager.size() != 1) {
            throw (new Exception(
                    label("CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_COMPTE_CLOTURE_INEXISTANT")
                            + secteur.getIdSecteurAVS()));
        }

        compteCloture = ((CGPlanComptableViewBean) planManager.getFirstEntity()).getIdExterne();
        return compteCloture;
    }

    /**
     * Retourne l'id externe du compte de la contre écriture pour le masque id externe spécifié.
     * 
     * @param secteur
     * @param forIdExterne
     * @return
     * @throws Exception
     */
    private String getIdExterneContreEcriture(CGSecteurAVS secteur, String forIdExterne) throws Exception {
        CGPlanComptableManager tempManager = new CGPlanComptableManager();
        tempManager.setSession(getSession());
        tempManager.setForIdMandat(exercice.getIdMandat());
        tempManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        tempManager.setForIdExterne(forIdExterne);
        tempManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        if (tempManager.size() != 1) {
            throw (new Exception(
                    label(CGPeriodeComptableBouclementProcess.LABEL_CLOTURE_COMPTE_EXPLOITATION_ADMINISTRATION_AUTRES_TACHES_COMPTE_CONTRE_ECRITURE_INEXISTANT)
                            + secteur.getIdSecteurBilan()));
        }

        return ((CGPlanComptableViewBean) tempManager.getFirstEntity()).getIdExterne();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2003 14:53:42)
     * 
     * @return String
     */
    public String getIdPeriodeComptable() {
        return idPeriodeComptable;
    }

    /**
     * Method getLibelleToFit.
     * <p>
     * Retourne le libelle en le troncant à maxSize char si plus grand.
     * 
     * @param maxSize
     * @param libelle
     * @return
     */
    private String getLibelleToFit(int maxSize, String libelle) {
        if (libelle == null) {
            return "";
        } else if (libelle.length() <= maxSize) {
            return libelle;
        } else {
            return libelle.substring(0, maxSize - 1);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.04.2003 14:37:25)
     * 
     * @return CGPeriodeComptable
     */
    public CGPeriodeComptable getPeriode() {
        if (periode == null) {
            try {
                periode = new CGPeriodeComptable();
                periode.setSession(getSession());
                periode.setIdPeriodeComptable(getIdPeriodeComptable());
                periode.retrieve(getTransaction());
            } catch (Exception e) {
                // do nothing
            }
        }
        return periode;
    }

    /**
     * Method getPeriodeExtournePourLissage.
     * 
     * Retourne la période d'extourne pour les contre-écritures du lissage du secteur 1990
     * 
     * @param periode
     *            La période ou seront extourner les contres-écritures
     * 
     * @return CGPeriodeComptable la période d'extourne
     * 
     * @throws Exception
     */
    private CGPeriodeComptable getPeriodeExtournePourLissage(CGPeriodeComptable periode) throws Exception {
        // Si période actuelle est la période anuelle, récupérer la période de janvier.
        // Si periode actuelle == décembre, récupéré la période anuelle
        // Sinon, récupérer la période suivante

        CGPeriodeComptable result = null;
        if (periode.isAnnuel()) {
            result = periode.retrieveNextPeriode(CGPeriodeComptable.CS_MENSUEL, getTransaction());
        } else {
            result = periode.retrieveNextPeriodeContigue(getTransaction());
            JADate dateFin = new JADate(periode.getDateFin());
            // On est dans le cas d'une période en fin d'année
            if ((dateFin.getMonth() == 12) && (dateFin.getDay() == 31)) {
                if (!result.isAnnuel()) {
                    throw new Exception(
                            "getPeriodeExtournePourLissage(): impossible de récupérer la période anuelle !!!");
                }
            }
        }

        if ((result == null) || result.isNew()) {
            throw new Exception("getPeriodeExtournePourLissage(): impossible de récupérer la période d'extourne !!!");
        }
        return result;
    }

    public Boolean getQuittancerWarning() {
        return quittancerWarning;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 18:36:40)
     * 
     *
     * @return String
     * @param codeLabel
     *            String
     */
    private String global(String codeLabel) {
        return getSession().getLabel("GLOBAL_" + codeLabel);
    }

    /**
     * @param codeLabel
     *            String
     */
    private void info(String codeLabel) {
        this.info(codeLabel, "");
    }

    /**
     * @param codeLabel
     *            label
     * @param msg
     *            message aditionnel au label
     */
    private void info(String codeLabel, String msg) {
        getMemoryLog().logMessage(
                getSession().getLabel(CGPeriodeComptableBouclementProcess.LABEL_PREFIX + codeLabel) + msg,
                FWMessage.INFORMATION, getSession().getLabel("GLOBAL_PERIODE") + " N°" + getIdPeriodeComptable());
    }

    /**
     * Initialisation des objects pour le process
     * 
     * @return
     * @throws Exception
     */
    private boolean init() throws Exception {
        periode = new CGPeriodeComptable();
        periode.setSession(getSession());
        periode.setIdPeriodeComptable(getIdPeriodeComptable());
        periode.retrieve(getTransaction());

        if (periode.isNew()) {
            this._addError(global("PERIODE_INEXISTANT"));
            return false;
        }

        exercice = periode.getExerciceComptable();
        if ((exercice == null) || exercice.isNew()) {
            this._addError(global("EXERCICE_INEXISTANT"));
            return false;
        }

        mandat = exercice.getMandat();
        if ((mandat == null) || mandat.isNew()) {
            this._addError(global("MANDAT_INEXISTANT"));
            return false;
        }

        bouclement = new CGBouclement();
        bouclement.setSession(getSession());
        bouclement.setIdBouclement(periode.getIdBouclement());
        bouclement.setIdMandat(exercice.getIdMandat());
        bouclement.retrieve(getTransaction());
        if ((bouclement == null) || bouclement.isNew()) {
            this._addError(label("BOUCLEMENT_INEXISTANT"));
        }

        if (periode.isEstCloture().booleanValue()) {
            this._addError(label("PERIODE_CLOTUREE"));
            return false;
        }

        if (exercice.isEstCloture().booleanValue()) {
            this._addError(label("EXERCICE_CLOTURE"));
            return false;
        }

        if (periode.isCloture()) {
            this._addError("!!! Il est interdit de boucler la periode de clôture !!!");
            return false;
        }

        if (mandat.isEstVerrouille().booleanValue()) {
            this._addError(label("MANDAT_VEROUILLE"));
            return false;
        }

        periodePrec = periode.retrieveLastPeriodeContigue(getTransaction());
        if ((periodePrec == null) || periodePrec.isNew()) {
            hasPeriodePrec = false;
            this.warn("PERIODE_PREC_INEXISTANTE");
        }

        CGExerciceComptable exercicePrec = null;
        if (hasPeriodePrec && !periodePrec.getIdExerciceComptable().equals(periode.getIdExerciceComptable())) {
            exercicePrec = periodePrec.getExerciceComptable();
            if ((exercicePrec == null) || exercicePrec.isNew()) {
                this._addError("EXERCICE_PRECEDENT_INEXISTANT");
                return false;
            }
        }

        if (hasPeriodePrec) {
            if ((periodePrec != null) && !periodePrec.isEstCloture().booleanValue()) {
                this._addError(label("PER_PREC_NON_CLOTURE"));
                return false;
            }

            if (!periodePrec.isCloture() && (exercicePrec != null) && !exercicePrec.isEstCloture().booleanValue()) {
                this._addError(label("PER_PREC_NON_CLOTURE"));
                return false;
            }

            if ((exercicePrec != null) && !exercicePrec.isEstCloture().booleanValue()) {
                this._addError(label("EXER_PREC_NON_CLOTURE"));
                return false;
            }
        }

        return true;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        if ((bouclement != null) && bouclement.isBouclementAnnuelAVS().booleanValue()) {
            return GlobazJobQueue.UPDATE_LONG;
        } else {
            return GlobazJobQueue.UPDATE_SHORT;
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 18:36:40)
     * 
     *
     * @return String
     * @param codeLabel
     *            String
     */
    private String label(String codeLabel) {
        return getSession().getLabel(CGPeriodeComptableBouclementProcess.LABEL_PREFIX + codeLabel);
    }

    /**
     * 11. Lissage du secteur 1990 (bouclement mensuel et annuel Avs) <br/>
     * <br/>
     * Le lissage du secteur 1990 permet de présenter dans les comptes un solde unique, <br/>
     * dette ou avoir, envers un secteur comptable.
     * 
     * @see U:\05 - Projets\Redévelopp.AVS\Analyse\Helios\Analyse\Helios - les traitements.htm
     * @throws Exception
     */
    private void lissageSecteur_1990() throws Exception {
        // Lissage des comptes 1990.120X.XXX0
        CGPlanComptableManager planManager = new CGPlanComptableManager();
        planManager.setSession(getSession());
        planManager.setForIdMandat(exercice.getIdMandat());
        planManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        planManager.setBeginWithIdExterne("1990.120");
        planManager.setEndWithIdExterne("0");
        planManager.setForIdPeriodeComptable(null);
        planManager.setForEstPeriode(new Boolean(true));
        planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        if (planManager.size() == 0) {
            this.warn("LISSAGE_SECTEUR_1990_AUCUN_COMPTE_1990_120X_XXX0");
        } else {
            for (int i = 0; i < planManager.size(); i++) {
                CGPlanComptableViewBean compte120X = (CGPlanComptableViewBean) planManager.getEntity(i);
                // CGSolde solde = new CGSolde();
                // solde.setSession(getSession());
                // solde.setIdSolde(compte120X.getIdSolde());
                // solde.retrieve(getTransaction());
                // if (solde.isNew()) {
                // solde.setIdExerComptable(exercice.getIdExerciceComptable());
                // solde.setIdCompte(compte120X.getIdCompte());
                // solde.setIdPeriodeComptable(getIdPeriodeComptable());
                // solde.setEstPeriode(new Boolean(true));
                // }
                //

                FWCurrency soldeCumul1 = CGSolde.computeSoldeCumule(exercice.getIdExerciceComptable(),
                        compte120X.getIdCompte(), periode.getIdPeriodeComptable(), "0", getSession(), true);

                if (!soldeCumul1.isZero()) {
                    String idExterne = "1990.220" + compte120X.getIdExterne().substring(8, 14);
                    CGPlanComptableManager planManager2 = new CGPlanComptableManager();
                    planManager2.setSession(getSession());
                    planManager2.setForIdMandat(exercice.getIdMandat());
                    planManager2.setForIdExerciceComptable(exercice.getIdExerciceComptable());
                    planManager2.setForIdExterne(idExterne);
                    planManager2.setForIdPeriodeComptable(null);
                    planManager2.setForEstPeriode(new Boolean(true));
                    planManager2.find(getTransaction(), BManager.SIZE_NOLIMIT);
                    if (planManager2.size() == 0) {
                        throw (new Exception(label("LISSAGE_SECTEUR_1990_COMPTE_1990_220X_INEXISTANT") + idExterne));
                    }
                    CGPlanComptableViewBean compte220X = (CGPlanComptableViewBean) planManager2.getFirstEntity();
                    // CGSolde solde2 = new CGSolde();
                    // solde2.setSession(getSession());
                    // solde2.setIdSolde(compte220X.getIdSolde());
                    // solde2.retrieve(getTransaction());
                    //
                    // if (solde2.isNew()) {
                    // solde2.setIdExerComptable(exercice.getIdExerciceComptable());
                    // solde2.setIdCompte(compte220X.getIdCompte());
                    // solde2.setIdPeriodeComptable(getIdPeriodeComptable());
                    // solde2.setEstPeriode(new Boolean(true));
                    // }

                    // FWCurrency soldeCumul2 = new FWCurrency(solde2.computeSoldeCumule(false));
                    FWCurrency soldeCumul2 = CGSolde.computeSoldeCumule(exercice.getIdExerciceComptable(),
                            compte220X.getIdCompte(), periode.getIdPeriodeComptable(), "0", getSession(), true);
                    if (!soldeCumul2.isZero()) {
                        FWCurrency cumul = new FWCurrency(soldeCumul1.toString());
                        cumul.add(soldeCumul2);

                        if (cumul.isPositive()) {
                            FWCurrency montant = new FWCurrency(soldeCumul2.toString());
                            montant.negate();

                            String libelle = getLibelleToFit(50,
                                    label("LISSAGE_SECTEUR_1990_LABEL_ECRITURE_REFLETE_CUMUL"));
                            addEcritureDoubleToJournalClot(libelle, montant, compte120X.getIdExterne(), ""
                                    + compte120X.getIdExterne().charAt(8) + compte120X.getIdExterne().charAt(10)
                                    + compte120X.getIdExterne().charAt(11) + compte120X.getIdExterne().charAt(12)
                                    + ".2201.0000");

                            // Contre écriture
                            libelle = getLibelleToFit(50, label("LISSAGE_SECTEUR_1990_LABEL_ECRITURE_COMPTE_A_ZERO"));
                            addEcritureDoubleToJournalClot(libelle, montant, "" + compte120X.getIdExterne().charAt(8)
                                    + compte120X.getIdExterne().charAt(10) + compte120X.getIdExterne().charAt(11)
                                    + compte120X.getIdExterne().charAt(12) + ".1201.0000", compte220X.getIdExterne());
                        } else {
                            String libelle = getLibelleToFit(50,
                                    label("LISSAGE_SECTEUR_1990_LABEL_ECRITURE_REFLETE_CUMUL"));
                            addEcritureDoubleToJournalClot(
                                    libelle,
                                    soldeCumul1,
                                    "" + compte120X.getIdExterne().charAt(8) + compte120X.getIdExterne().charAt(10)
                                            + compte120X.getIdExterne().charAt(11)
                                            + compte120X.getIdExterne().charAt(12) + ".1201.0000",
                                    compte220X.getIdExterne());

                            // Contre écriture
                            libelle = getLibelleToFit(50, label("LISSAGE_SECTEUR_1990_LABEL_ECRITURE_COMPTE_A_ZERO"));
                            addEcritureDoubleToJournalClot(libelle, soldeCumul1, compte120X.getIdExterne(), ""
                                    + compte120X.getIdExterne().charAt(8) + compte120X.getIdExterne().charAt(10)
                                    + compte120X.getIdExterne().charAt(11) + compte120X.getIdExterne().charAt(12)
                                    + ".2201.0000");
                        }
                    }
                }
            }
        }

        // Lissage des comptes 1990.120s.xxx9

        // Secteur no avs à traiter : 5,6,7,8
        for (int i = 5; i <= 8; i++) {

            String s = String.valueOf(i);
            planManager = new CGPlanComptableManager();
            planManager.setSession(getSession());
            planManager.setForIdExterne("1990.120" + s + ".9999");
            planManager.setSession(getSession());
            planManager.setForIdMandat(exercice.getIdMandat());
            planManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
            planManager.setForEstPeriode(new Boolean(false));
            planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            if (planManager.size() == 0) {
                this.warn("LISSAGE_SECTEUR_1990_AUCUN_COMPTE_1990_120X_XXX9", "1990.120" + s + ".9999");
            } else {
                CGPlanComptableViewBean compte1990_120s_xxx9 = (CGPlanComptableViewBean) planManager.getFirstEntity();

                planManager.setForIdExterne("1990.220" + s + ".9999");
                planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

                if (planManager.size() == 0) {
                    this.warn("LISSAGE_SECTEUR_1990_AUCUN_COMPTE_1990_120X_XXX9", "1990.220" + s + ".9999");
                } else {
                    CGPlanComptableViewBean compte1990_220s_xxx9 = (CGPlanComptableViewBean) planManager
                            .getFirstEntity();
                    doEcritureLissage1990(s, compte1990_120s_xxx9, compte1990_220s_xxx9, periode);
                }
            }
        }

        if ((journalExtourne != null) && bouclement.isBouclementMensuelAVS().booleanValue()) {
            journalExtourne.comptabiliser(this);
        }
    }

    /**
     * Ouverture du journal de ouverture de la période de janvier (idJournal2). <br/>
     * Récupération de la période de janvier.
     * 
     * @return
     * @throws Exception
     */
    private CGPeriodeComptable openPeriodeOuverture(String periodeComptableType) throws Exception {
        CGPeriodeComptable periodeOuverture = periode.retrieveNextPeriode(periodeComptableType, getTransaction());
        if ((periodeOuverture != null) && !periodeOuverture.isNew()) {
            ouvertureJournalClot2(periodeOuverture);
        } else {
            throw new CGPeriodeComptableException("Periode Janvier inexistante !!!");
        }

        return periodeOuverture;
    }

    /**
     * 4. Ouverture d'un nouveau journal de clôture (commun)
     * 
     *
     * @see U:\05 - Projets\Redévelopp.AVS\Analyse\Helios\Analyse\Helios - les traitements.htm
     * @throws Exception
     */
    private void ouvertureJournalClot() throws Exception {
        journalClot = new CGJournal();
        journalClot.setSession(getSession());
        journalClot.setDateValeur(periode.getDateFin());
        java.util.Calendar cal = new java.util.GregorianCalendar();
        JADate date = new JADate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
        journalClot.setDate(date.toStr("."));
        journalClot.setIdExerciceComptable(exercice.getIdExerciceComptable());
        journalClot.setIdPeriodeComptable(periode.getIdPeriodeComptable());
        journalClot.setEstPublic(new Boolean(true));
        journalClot.setEstConfidentiel(new Boolean(false));
        journalClot.setIdTypeJournal(CGJournal.CS_TYPE_AUTOMATIQUE);
        journalClot.setProprietaire(getSession().getUserId());
        journalClot
                .setLibelle(getLibelleToFit(40, label("LABEL_JOURNAL_CLOTURE") + " " + periode.getFullDescription()));
        journalClot.add(getTransaction());

        periode.setIdJournal(journalClot.getIdJournal());
        periode.update(getTransaction());

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 16:47:15)
     * 
     * @exception Exception
     *                La description de l'exception.
     */
    private void ouvertureJournalClot2(CGPeriodeComptable periodeOuverture) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(periode.getIdJournal2())) {
            journalClot2 = new CGJournal();
            journalClot2.setSession(getSession());
            journalClot2.setDateValeur(periodeOuverture.getDateDebut());
            journalClot2.setDate(periodeOuverture.getDateDebut());
            journalClot2.setIdPeriodeComptable(periodeOuverture.getIdPeriodeComptable());
            journalClot2.setIdExerciceComptable(periodeOuverture.getIdExerciceComptable());
            journalClot2.setEstPublic(new Boolean(true));
            journalClot2.setEstConfidentiel(new Boolean(false));
            journalClot2.setIdTypeJournal(CGJournal.CS_TYPE_AUTOMATIQUE);
            journalClot2.setProprietaire(getSession().getUserId());
            journalClot2.setLibelle(getLibelleToFit(40,
                    label("LABEL_JOURNAL_CLOTURE2") + " " + periodeOuverture.getFullDescription()));
            journalClot2.add(getTransaction());
            this.info("CREATION_JOURNAL2");

            periode.setIdJournal2(journalClot2.getIdJournal());
            periode.update(getTransaction());
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 16:47:15)
     * 
     * @exception Exception
     *                La description de l'exception.
     */
    private void ouvertureJournalClot3() throws Exception {
        if (JadeStringUtil.isIntegerEmpty(periode.getIdJournal3())) {
            journalClot3 = new CGJournal();
            journalClot3.setSession(getSession());
            journalClot3.setDateValeur(periodeClot.getDateFin());
            journalClot3.setDate(periodeClot.getDateFin());
            journalClot3.setIdPeriodeComptable(periodeClot.getIdPeriodeComptable());
            journalClot3.setIdExerciceComptable(exercice.getIdExerciceComptable());
            journalClot3.setEstPublic(new Boolean(true));
            journalClot3.setEstConfidentiel(new Boolean(false));
            journalClot3.setIdTypeJournal(CGJournal.CS_TYPE_AUTOMATIQUE);
            journalClot3.setProprietaire(getSession().getUserId());
            journalClot3.setLibelle(getLibelleToFit(40,
                    label("LABEL_JOURNAL_CLOTURE3") + " " + periodeClot.getFullDescription()));
            journalClot3.add(getTransaction());
            this.info("CREATION_JOURNAL3");

            periode.setIdJournal3(journalClot3.getIdJournal());
            periode.update(getTransaction());
        }
    }

    private void ouvertureJournalClotPandemie() throws Exception {
        journalPandemie = new CGJournal();
        journalPandemie.setSession(getSession());
        journalPandemie.setDateValeur(periode.getDateFin());
        java.util.Calendar cal = new java.util.GregorianCalendar();
        JADate date = new JADate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
        journalPandemie.setDate(date.toStr("."));
        journalPandemie.setIdExerciceComptable(exercice.getIdExerciceComptable());
        journalPandemie.setIdPeriodeComptable(periode.getIdPeriodeComptable());
        journalPandemie.setEstPublic(new Boolean(true));
        journalPandemie.setEstConfidentiel(new Boolean(false));
        journalPandemie.setIdTypeJournal(CGJournal.CS_TYPE_AUTOMATIQUE);
        journalPandemie.setProprietaire(getSession().getUserId());
        journalPandemie
                .setLibelle(getLibelleToFit(40, label("LABEL_JOURNAL_CLOTURE_PANDEMIE") + " " + periode.getFullDescription()));
        journalPandemie.add(getTransaction());

    }

    private CGJournal ouvertureJournalExtournePourLissage(CGPeriodeComptable periodeExtourne, String idExerciceLissage)
            throws Exception {
        if (journalExtourne == null) {
            journalExtourne = new CGJournal();
            journalExtourne.setSession(getSession());
            journalExtourne.setDateValeur(periodeExtourne.getDateFin());
            journalExtourne.setDate(periodeExtourne.getDateFin());
            journalExtourne.setIdPeriodeComptable(periodeExtourne.getIdPeriodeComptable());
            journalExtourne.setIdExerciceComptable(idExerciceLissage);
            journalExtourne.setEstPublic(new Boolean(false));
            journalExtourne.setEstConfidentiel(new Boolean(false));
            journalExtourne.setIdTypeJournal(CGJournal.CS_TYPE_AUTOMATIQUE);
            journalExtourne.setProprietaire(getSession().getUserId());
            journalExtourne.setLibelle(getLibelleToFit(40,
                    label("LABEL_JOURNAL_CLOTURE2") + " " + periodeExtourne.getFullDescription()));
            journalExtourne.add(getTransaction());
            this.info("CREATION_JOURNAL_EXTOURNE_1990_120X_XXX0");

            // Dans le cas d'un bouclement mensuel avs, idJournal3 n'est pas utilisé. On l'affecte donc ici pour le
            // journal
            // d'extourne des écriture de lissage
            if (bouclement.isBouclementMensuelAVS().booleanValue()) {
                if ((periode.getIdJournal3() != null) && !periode.getIdJournal3().equals("")
                        && !periode.getIdJournal3().equals("0")) {
                    throw new Exception(
                            "Erreur création du journal de cloture pour lissage, idJournal3 déja affecté !!!");
                }

                periode.setIdJournal3(journalExtourne.getIdJournal());
            }
            // Dans le cas d'un bouclement annuel avs, idJournal2 n'est pas utilisé. On l'affecte donc ici pour le
            // journal
            // d'extourne des écriture de lissage
            else {
                if ((periode.getIdJournal2() != null) && !periode.getIdJournal2().equals("")
                        && !periode.getIdJournal2().equals("0")) {
                    throw new Exception(
                            "Erreur création du journal de cloture pour lissage, idJournal2 déja affecté !!!");
                }

                periode.setIdJournal2(journalExtourne.getIdJournal());
            }
            periode.update(getTransaction());
        }

        return journalExtourne;
    }

    /**
     * Method ouverturePeriodeSuiv. <br/>
     * <br/>
     * Ordonnancement des periodes : M1/M2/M3/T1/M4/M5/M6/T2/S1/M7/M8/M9/T3/M10/M11/M12/T4/S2/AN/CL <br/>
     * <br/>
     * Ouverture de la période suivante : <br/>
     * <br/>
     * si periode = août: - ouvre septembre (si existe pas) <br/>
     * <br/>
     * si periode de fin d'année (M12, T4, S2, AN, CL) : <br/>
     * - creation periode annuelle sur exercice courant (si existe pas) <br/>
     * - creation de l'exercice suivant (si existe pas) <br/>
     * - si periode annuelle (AN) : <br/>
     * - creation periode de cloture (si existe pas) <br/>
     * - creation periode du même type sur exercice suivant. <br/>
     * <br/>
     * <br/>
     * Voire points : <br/>
     * 5. Ouverture de la période suivante (commun) <br/>
     * 6. Ouverture de l'exercice suivant (commun) <br/>
     * 5 & 6. Ouverture de la période suivante et de l'exercice suivant (commun)
     * 
     * @see U:\05 - Projets\Redévelopp.AVS\Analyse\Helios\Analyse\Helios - les traitements.htm
     * @param transaction
     * @throws Exception
     */
    private void ouverturePeriodeSuiv(globaz.globall.db.BTransaction transaction) throws Exception {
        if (periode.isCloture()) {
            throw new Exception("ouverturePeriodeSuiv(): ne doit pas etre appelee pour la periode de cloture!");
        }

        // On calcule les dates de début/fin de la période/exercice suivant
        Calendar calendar = new java.util.GregorianCalendar();
        JADate dateDebutPeriodeSuiv = new JADate(periode.getDateFin());
        JADate dateFinPeriodeSuiv = new JADate(periode.getDateFin());

        calendar.set(Calendar.YEAR, dateDebutPeriodeSuiv.getYear());
        calendar.set(Calendar.MONTH, dateDebutPeriodeSuiv.getMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dateDebutPeriodeSuiv.getDay());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        dateDebutPeriodeSuiv.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        dateDebutPeriodeSuiv.setMonth(calendar.get(Calendar.MONTH) + 1);
        dateDebutPeriodeSuiv.setYear(calendar.get(Calendar.YEAR));

        if (periode.isMensuel()) {
            calendar.add(Calendar.MONTH, 1);
        } else if (periode.isSemestriel()) {
            calendar.add(Calendar.MONTH, 6);
        } else if (periode.isTrimestriel()) {
            calendar.add(Calendar.MONTH, 3);
        } else if (periode.isAnnuel()) {
            calendar.add(Calendar.MONTH, 12);
        }

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        dateFinPeriodeSuiv.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        dateFinPeriodeSuiv.setMonth(calendar.get(Calendar.MONTH) + 1);
        dateFinPeriodeSuiv.setYear(calendar.get(Calendar.YEAR));

        JADate dateDebutExerciceSuiv = new JADate(exercice.getDateFin());
        JADate dateFinExerciceSuiv = new JADate(exercice.getDateFin());

        calendar.set(Calendar.YEAR, dateDebutExerciceSuiv.getYear());
        calendar.set(Calendar.MONTH, dateDebutExerciceSuiv.getMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dateDebutExerciceSuiv.getDay());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        dateDebutExerciceSuiv.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        dateDebutExerciceSuiv.setMonth(calendar.get(Calendar.MONTH) + 1);
        dateDebutExerciceSuiv.setYear(calendar.get(Calendar.YEAR));
        calendar.add(Calendar.MONTH, 12);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        dateFinExerciceSuiv.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        dateFinExerciceSuiv.setMonth(calendar.get(Calendar.MONTH) + 1);
        dateFinExerciceSuiv.setYear(calendar.get(Calendar.YEAR));

        // Test si période en fin d'année : M12, T4, S2 ou AN (CL Impossible, car on ne boucle pas une periode de type
        // cloture) !!!!
        if ("12".equals(periode.getCode()) || CGPeriodeComptable.CS_CODE_TRIMESTRE_4.equals(periode.getCode())
                || CGPeriodeComptable.CS_CODE_SEMESTRE_2.equals(periode.getCode()) || periode.isAnnuel()) {
            if (periode.isAnnuel()) {
                // Si la période est de type annuel, on récupère/crée la période de clôture
                periodeClot = periode.retrieveNextPeriodeContigue(CGPeriodeComptable.CS_CLOTURE, transaction);

                if (periodeClot == null) {
                    this.info("OUVERTURE_PERIODE_SUIVANTE_CREATION_PERIODE_CLOTURE");
                    periodeClot = createPeriodeAnnuelle(transaction, CGPeriodeComptable.CS_CLOTURE);
                }

                if (periodeClot.isEstCloture().booleanValue()) {
                    throw (new Exception(label("OUVERTURE_PERIODE_SUIVANTE_PERIODE_CLOTURE_CLOTUREE")));
                }
            } else {
                // La période courante n'est pas la période anuelle -> création de la période anuelle si existe pas
                CGPeriodeComptable periodeAnnu = periode.retrieveNextPeriodeContigue(CGPeriodeComptable.CS_ANNUEL,
                        transaction);
                if (periodeAnnu == null) {
                    createPeriodeAnnuelle(transaction, CGPeriodeComptable.CS_ANNUEL);
                }
            }

            // Tout d'abord on essaye d'ouvrir l'exercice suivant
            // On vérifie s'il n'existe pas déjà
            exerciceSuiv = exercice.retrieveNextExerciceContigu(transaction);
            if ((exerciceSuiv == null) || exerciceSuiv.isNew()) {
                createExerciceSuivant(transaction, dateDebutExerciceSuiv, dateFinExerciceSuiv);
            } else {
                if (exerciceSuiv.isEstCloture().booleanValue()) {
                    throw (new Exception(label("OUVERTURE_PERIODE_SUIVANTE_EXERCICE_CLOTURE")));
                }
            }

            // Bug 5386 Tester l'ouverture des comptes
            CGControleCompteNouvelExerciceManager managerCheck = new CGControleCompteNouvelExerciceManager();
            managerCheck.setSession(getSession());
            managerCheck.setCurrentIdExercice(exercice.getIdExerciceComptable());
            managerCheck.setNextIdExercice(exerciceSuiv.getIdExerciceComptable());
            managerCheck.find(transaction);

            if (managerCheck.size() != 0) {
                Iterator<CGPlanComptableViewBean> it = managerCheck.iterator();
                while (it.hasNext()) {
                    CGPlanComptableViewBean compte = it.next();
                    this.warn("COMPTES_A_ROUVRIR_EXERCICE_SUIVANT_ERROR", compte.getIdExterne());
                }
                throw (new Exception(label("COMPTES_A_ROUVRIR_EXERCICE_SUIVANT_ERROR")));
            }

            // Attention avec les périodes annuelles : on créée une période annuelle sur l'exercice suivant
            // uniquement si on travaille que avec des périodes annuelles.
            // S'il existe d'autre type de périodes et que la période suivante n'existe pas, on CRASHE,
            // car le systéme DOIT avoir créé automatiquement la période suivante.
            // *******************
            // Par exemple, On boucle décembre qui créée janvier PUIS on boucle la période annuelle.
            // Si en bouclant la période annuelle il n'y a pas de période suivante (cad janvier) et que décembre existe
            // problèmes !!
            // *******************
            boolean createPeriodeSuiv = true;
            CGPeriodeComptable perSuiv = null;

            if (periode.isAnnuel()) {
                CGPeriodeComptableManager periodeManager = new CGPeriodeComptableManager();
                periodeManager.setSession(getSession());
                periodeManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
                HashSet<String> tempSet = new HashSet<String>();
                tempSet.add(CGPeriodeComptable.CS_CLOTURE);
                tempSet.add(CGPeriodeComptable.CS_ANNUEL);
                periodeManager.setExceptIdTypePeriode(tempSet);
                periodeManager.find(transaction, BManager.SIZE_NOLIMIT);
                if (periodeManager.size() != 0) {
                    perSuiv = periodeClot.retrieveNextPeriodeContigue(transaction);
                    if ((perSuiv == null) || perSuiv.isNew()) {
                        throw (new Exception(label("OUVERTURE_PERIODE_SUIVANTE_TYPE_ANNUEL_SUIVANTE_INEXISTANTE")));
                    } else {
                        createPeriodeSuiv = false;
                    }
                }
            } else {
                perSuiv = periode.retrieveNextPeriode(periode.getIdTypePeriode(), transaction);
                // Si la periode suivante de l'exercice suivant est de type : "01" ou S1 ou T1 --> elle existe
                if ((perSuiv != null) && !perSuiv.isNew()
                        && exerciceSuiv.getIdExerciceComptable().equals(perSuiv.getIdExerciceComptable())) {

                    if ("01".equals(perSuiv.getCode())
                            || CGPeriodeComptable.CS_CODE_SEMESTRE_1.equals(perSuiv.getCode())
                            || CGPeriodeComptable.CS_CODE_TRIMESTRE_1.equals(perSuiv.getCode())) {
                        createPeriodeSuiv = false;
                    }
                }
            }

            if (createPeriodeSuiv) {
                // On est dans le cas d'une période en fin d'année
                // Cas de la derniere periode de l'années, création de la périodes suivante de janvier de l'annee
                // suivante.
                perSuiv = periode.retrieveNextPeriode(periode.getIdTypePeriode(), transaction);
                if ((perSuiv == null) || perSuiv.isNew()) {
                    createPeriodeSuivante(transaction, dateDebutPeriodeSuiv, dateFinPeriodeSuiv,
                            exerciceSuiv.getIdExerciceComptable());
                } else {
                    this.info(CGPeriodeComptableBouclementProcess.LABEL_OUVERTURE_PERIODE_SUIVANTE_DEJA_EXISTANTE);
                    if (perSuiv.isEstCloture().booleanValue()) {
                        throw (new Exception(
                                label(CGPeriodeComptableBouclementProcess.LABEL_OUVERTURE_PERIODE_SUIVANTE_CLOTUREE)));
                    }

                }
            }
        } else {
            // Pas une periode de fin d'année
            CGPeriodeComptable perSuiv = periode.retrieveNextPeriodeContigue(periode.getIdTypePeriode(), transaction);

            // Si la période suivante n'existe pas ou n'est pas de même type, on la créée
            if (perSuiv == null) {
                createPeriodeSuivante(transaction, dateDebutPeriodeSuiv, dateFinPeriodeSuiv,
                        exercice.getIdExerciceComptable());
            } else {
                this.info(CGPeriodeComptableBouclementProcess.LABEL_OUVERTURE_PERIODE_SUIVANTE_DEJA_EXISTANTE);
                if (perSuiv.isEstCloture().booleanValue()) {
                    throw (new Exception(
                            label(CGPeriodeComptableBouclementProcess.LABEL_OUVERTURE_PERIODE_SUIVANTE_CLOTUREE)));
                }
            }
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2003 14:53:42)
     * 
     * @param newIdJournal
     *            String
     */
    public void setIdPeriodeComptable(String newidPeriodeComptable) {
        idPeriodeComptable = newidPeriodeComptable;
    }

    public void setQuittancerWarning(Boolean quittancerWarning) {
        this.quittancerWarning = quittancerWarning;
    }

    /**
     * Contrôle l'état des journaux de comptabilité fournisseur.
     * 
     * @throws Exception
     */
    private void testLynxJournaux() throws Exception {
        if (new Boolean(getSession().getApplication().getProperty(
                CGPeriodeComptableBouclementProcess.BOUCLEMENT_CHECK_LYNX_JOURNAUX)).booleanValue()) {
            CGLynxJournalManager manager = new CGLynxJournalManager();
            manager.setSession(getSession());

            ArrayList<String> etatNotIn = new ArrayList<String>();
            etatNotIn.add(LXJournal.CS_ETAT_ANNULE);
            etatNotIn.add(LXJournal.CS_ETAT_COMPTABILISE);

            manager.setForEtatNotIn(etatNotIn);
            manager.setDateValeurFrom(periode.getDateDebut());
            manager.setDateValeurUntil(periode.getDateFin());

            manager.find(getTransaction());

            if (!manager.isEmpty()) {
                throw new Exception(label("LYNX_JOURNAUX_NON_COMPTABILISES"));
            }
        }
    }

    /**
     * Contrôle l'état des journaux de comptabilité auxiliaire.
     * 
     * @throws Exception
     */
    private void testOsirisJournaux() throws Exception {
        if (new Boolean(getSession().getApplication().getProperty(
                CGPeriodeComptableBouclementProcess.BOUCLEMENT_CHECK_OSIRIS_JOURNAUX)).booleanValue()) {
            CGOsirisJournalManager manager = new CGOsirisJournalManager();
            manager.setSession(getSession());

            ArrayList<String> etatNotIn = new ArrayList<String>();
            etatNotIn.add(CGOsirisJournal.ETAT_ANNULE);
            etatNotIn.add(CGOsirisJournal.ETAT_COMPTABILISE);
            manager.setForEtatNotIn(etatNotIn);
            manager.setDateValeurFrom(periode.getDateDebut());
            manager.setDateValeurUntil(periode.getDateFin());

            manager.find(getTransaction());

            if (!manager.isEmpty()) {
                throw new Exception(label("OSIRIS_JOURNAUX_NON_COMPTABILISES"));
            }
        }
    }

    /**
     * Le total débit (ou crédit) d'un compte ne peut pas être annoncé à la centrale avec un montant négatif.
     * 
     * @throws Exception
     */
    private void testTotalDebitCreditCompteExploitation() throws Exception {
        CGPlanComptableManager planManager = new CGPlanComptableManager();
        planManager.setSession(getSession());

        planManager.setForEstPeriode(new Boolean(true));
        planManager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
        planManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());

        // Bug 5383 - Elargir à l'ensemble des comptes (pas que le compte d'exploitation)
        // planManager.setReqDomaine(CGCompte.CS_COMPTE_EXPLOITATION);

        // planManager.setFromSecteur(CGPeriodeComptableBouclementProcess.SECTEUR_EXPLOITATION_2110);
        // planManager.setUntilSecteur(CGPeriodeComptableBouclementProcess.SECTEUR_EXPLOITATION_2999);

        planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        for (int i = 0; (i < planManager.size()) && !isAborted(); i++) {
            CGPlanComptableViewBean plan = (CGPlanComptableViewBean) planManager.get(i);

            CGMouvementCompteListViewBean manager = new CGMouvementCompteListViewBean();
            manager.setSession(getSession());

            manager.setReqComptabilite(CodeSystem.CS_PROVISOIRE);
            manager.wantForEstActive(true);
            manager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
            manager.setForIdMandat(exercice.getIdMandat());
            manager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
            manager.setForIdCompte(plan.getIdCompte());

            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            FWCurrency totalDebit = new FWCurrency();
            FWCurrency totalCredit = new FWCurrency();
            for (int j = 0; (j < manager.size()) && !isAborted(); j++) {
                CGMouvementCompteViewBean mouvements = (CGMouvementCompteViewBean) manager.get(j);

                totalDebit.add(mouvements.getDoit());
                totalCredit.add(mouvements.getAvoir());
            }

            if (totalDebit.isNegative() || totalCredit.isNegative()) {
                createEcrituresCorrectionMvtNegatif(plan, totalDebit, totalCredit);
            }
        }
    }

    /**
     * Transfert des comptes résultats (tous domaine) vers les comptes d'ouverture et clôture du Bilan.
     * 
     * @param idCompteOuverture
     * @param periodeOuverture
     * @param totalTransfert
     * @param planResultat
     * @throws Exception
     */
    private void transfertSoldeComptaResultatNonAvs(String idCompteOuverture, CGPeriodeComptable periodeOuverture,
            FWCurrency totalTransfert, CGPlanComptableViewBean planResultat) throws Exception {
        CGCompteManager compteManager = new CGCompteManager();
        compteManager.setSession(getSession());
        compteManager.setForIdMandat(mandat.getIdMandat());

        compteManager.setForIdGenre(CGCompte.CS_GENRE_RESULTAT);
        compteManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        for (int j = 0; j < compteManager.size(); j++) {
            CGCompte compte = (CGCompte) compteManager.getEntity(j);
            FWCurrency solde = CGSolde.computeSoldeCumule(exercice.getIdExerciceComptable(), compte.getIdCompte(),
                    journalClot3.getIdPeriodeComptable(), "0", getSession(), true);
            FWCurrency soldeMonnaieEtrangere = CGSolde.computeSoldeCumuleMonnaie(exercice.getIdExerciceComptable(),
                    compte.getIdCompte(), journalClot3.getIdPeriodeComptable(), "0", getSession(), true);

            if (!solde.isZero()) {
                totalTransfert.add(solde);

                solde = transfertSoldeToCompteCloture(planResultat.getIdCompte(), compte, solde, soldeMonnaieEtrangere,
                        true);
                transfertSoldeToCompteOuverture(periodeOuverture, idCompteOuverture, compte, solde,
                        soldeMonnaieEtrangere, true);
            }
        }
    }

    /**
     * Transfert des comptes actifs et passifs du Bilan vers les comptes d'ouverture et clôture du Bilan.
     * 
     * @param idCompteOuverture
     * @param periodeOuverture
     * @param totalTransfert
     * @param planResultat
     * @throws Exception
     */
    private void transfertSoldeCompteActifPassifNonAvs(String idCompteOuverture, CGPeriodeComptable periodeOuverture,
            FWCurrency totalTransfert, CGPlanComptableViewBean planResultat) throws Exception {
        CGCompteManager compteManager = new CGCompteManager();
        compteManager.setSession(getSession());
        compteManager.setForIdMandat(mandat.getIdMandat());
        compteManager.setForIdDomaine(planResultat.getIdDomaine());

        ArrayList<String> genreCompte = new ArrayList<String>();
        genreCompte.add(CGCompte.CS_GENRE_ACTIF);
        genreCompte.add(CGCompte.CS_GENRE_PASSIF);

        compteManager.setForIdGenreIn(genreCompte);
        compteManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        for (int j = 0; j < compteManager.size(); j++) {
            CGCompte compte = (CGCompte) compteManager.getEntity(j);
            FWCurrency solde = CGSolde.computeSoldeCumule(exercice.getIdExerciceComptable(), compte.getIdCompte(),
                    periode.getIdPeriodeComptable(), "0", getSession(), true);
            FWCurrency soldeMonnaieEtrangere = CGSolde.computeSoldeCumuleMonnaie(exercice.getIdExerciceComptable(),
                    compte.getIdCompte(), periode.getIdPeriodeComptable(), "0", getSession(), true);

            if (!solde.isZero()) {
                totalTransfert.add(solde);

                solde = transfertSoldeToCompteCloture(planResultat.getIdCompte(), compte, solde, soldeMonnaieEtrangere,
                        true);
                transfertSoldeToCompteOuverture(periodeOuverture, idCompteOuverture, compte, solde,
                        soldeMonnaieEtrangere, true);
            }
        }
    }

    /**
     * Transfert de solde sur le compte de clôture.
     * 
     * @param idCompteCloture
     * @param compte
     * @param solde
     * @param generateDetteAvoir
     * @return
     * @throws Exception
     */
    private FWCurrency transfertSoldeToCompteCloture(String idCompteCloture, CGCompte compte, FWCurrency solde,
            FWCurrency soldeMonnaieEtrangere, boolean generateDetteAvoir) throws Exception {
        FWCurrency result = new FWCurrency(solde.toString());
        addEcritures(periode.getDateFin(), periode.getIdJournal3(), periode.getIdExerciceComptable(),
                getLibelleToFit(50, label("CLOTURE_ANUNELLE_COMPTE_BILAN_LABEL_SOLDE_BALANCE")), solde,
                soldeMonnaieEtrangere, compte.getIdCompte(), idCompteCloture, generateDetteAvoir);
        return result;
    }

    /**
     * Transfert de solde sur le compte d'ouverture.
     * 
     * @param periodeOuverture
     * @param idCompteOuverture
     * @param compte
     * @param solde
     * @param generateDetteAvoir
     * @throws Exception
     */
    private void transfertSoldeToCompteOuverture(CGPeriodeComptable periodeOuverture, String idCompteOuverture,
            CGCompte compte, FWCurrency solde, FWCurrency soldeMonnaieEtrangere, boolean generateDetteAvoir)
            throws Exception {
        addEcritures(periodeOuverture.getDateDebut(), periode.getIdJournal2(),
                periodeOuverture.getIdExerciceComptable(),
                getLibelleToFit(50, label("REOUVERTURE_SOLDES_LABEL_ECRITURE_TRANSFERT")), solde,
                soldeMonnaieEtrangere, idCompteOuverture, compte.getIdCompte(), generateDetteAvoir);
    }

    /**
     * 8. Ventilation du compte 2000.1102.0000 (bouclement mensuel AVS)
     * 
     * @throws Exception
     */
    private void ventilationCompte_2000_1102_000() throws Exception {
        CGPlanComptableManager planManager = new CGPlanComptableManager();
        planManager.setSession(getSession());
        planManager.setForIdExterneLike("2%.1102.%");
        planManager.setForEstPeriode(new Boolean(true));
        planManager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
        planManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        planManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        if (planManager.isEmpty()) {
            this.warn("VENTILATION_COMPTE_2000_1102_0000_SOLDE_NUL");
            return;
        }

        for (int j = 0; (j < planManager.size()) && !isAborted(); j++) {
            CGPlanComptableViewBean plan = (CGPlanComptableViewBean) planManager.get(j);

            CGSoldeManager soldeManager = new CGSoldeManager();
            soldeManager.setSession(getSession());
            soldeManager.setForIdCompte(plan.getIdCompte());
            soldeManager.setForIdExerComptable(exercice.getIdExerciceComptable());
            soldeManager.setForIdMandat(exercice.getIdMandat());
            soldeManager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
            soldeManager.setForEstPeriode(new Boolean(true));
            soldeManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
            if (soldeManager.size() > 1) {
                throw (new Exception(label("VENTILATION_COMPTE_2000_1102_0000_SOLDE_ERROR")));
            }

            if (soldeManager.size() == 1) {
                FWCurrency solde = (((CGSolde) soldeManager.getFirstEntity()).computeSoldeCumule(true));

                if ((solde != null) && !solde.isZero()) {
                    CGSecteurAVSManager secteurManager = new CGSecteurAVSManager();
                    secteurManager.setSession(getSession());
                    secteurManager.setForIdMandat(exercice.getIdMandat());
                    secteurManager.setForTauxVentilationDefini(new Boolean(true));
                    secteurManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

                    for (int i = 0; i < secteurManager.size(); i++) {
                        CGSecteurAVS secteur = (CGSecteurAVS) secteurManager.getEntity(i);

                        BigDecimal ventilation = (new BigDecimal(secteur.getTauxVentilation()))
                                .multiply(new BigDecimal(solde.toString()));
                        ventilation = ventilation.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_DOWN);

                        String libelle = getLibelleToFit(50, label("VENTILATION_COMPTE_2000_1102_0000_LABEL_ECRITURE")
                                + " " + plan.getIdExterne());
                        addEcritureDoubleToJournalClot(libelle, new FWCurrency(JANumberFormatter.format(ventilation)),
                                plan.getIdExterne(), secteur.getIdSecteurAVS() + ".1102.0000");

                        // Création du journal N°2
                        // ouverture du journal d'ouverture de la période suivante.

                        // Si periode de décembre, la période d'ouverture == janvier exercice suivant
                        // Récupération de la période suivante (janvier si periode courante == décembre)
                        CGPeriodeComptable periodeOuverture = periode.retrieveNextPeriode(
                                CGPeriodeComptable.CS_MENSUEL, getTransaction());
                        if ((periodeOuverture != null) && !periodeOuverture.isNew()) {
                            ouvertureJournalClot2(periodeOuverture);
                        } else {
                            throw new Exception(
                                    "clotureOuvertureAnnuelleAVSCompteBilan(): Periode suivante inexistante !!!");
                        }

                        addEcrituresDebitCredit(
                                periodeOuverture.getDateDebut(),
                                periode.getIdJournal2(),
                                periodeOuverture.getIdExerciceComptable(),
                                getLibelleToFit(50, label("VENTILATION_COMPTE_2000_1102_0000_LABEL_ECRITURE_EXTOURNE")
                                        + " " + plan.getIdExterne()),
                                new FWCurrency(JANumberFormatter.format(ventilation)), secteur.getIdSecteurAVS()
                                        + ".1102.0000", plan.getIdExterne());
                    }
                }
            } else {
                this.warn("VENTILATION_COMPTE_2000_1102_0000_SOLDE_NUL");
            }
            setProgressDescription("id:" + plan.getId());
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 14:35:53)
     * 
     * @param msg
     *            String
     */
    private void warn(String msg) {
        this.warn(msg, "");
    }

    // *******************************************************
    // Getter
    // *******************************************************

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 14:35:53)
     * 
     * @param msg
     *            String
     */
    private void warn(String codeLabel, String msg) {
        getMemoryLog().logMessage(
                getSession().getLabel(CGPeriodeComptableBouclementProcess.LABEL_PREFIX + codeLabel) + msg,
                FWMessage.AVERTISSEMENT, getSession().getLabel("GLOBAL_PERIODE") + " N°" + getIdPeriodeComptable());
    }

    // *******************************************************
    // Setter
    // *******************************************************

    private void warnNoPrefixe(String codeLabel, String msg) {
        getMemoryLog().logMessage(getSession().getLabel(codeLabel) + msg, FWMessage.AVERTISSEMENT,
                getSession().getLabel("GLOBAL_PERIODE") + " N°" + getIdPeriodeComptable());
    }
}
