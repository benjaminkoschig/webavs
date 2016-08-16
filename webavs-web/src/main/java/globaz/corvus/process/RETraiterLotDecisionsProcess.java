/*
 */
package globaz.corvus.process;

import globaz.corvus.api.avances.IREAvances;
import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.api.ordresversements.IRESoldePourRestitution;
import globaz.corvus.api.prestations.IREPrestations;
import globaz.corvus.api.retenues.IRERetenues;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.avances.REAvance;
import globaz.corvus.db.avances.REAvanceManager;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REValidationDecisions;
import globaz.corvus.db.decisions.REValidationDecisionsManager;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.ordresversements.RESoldePourRestitution;
import globaz.corvus.db.ordresversements.RESoldePourRestitutionManager;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager;
import globaz.corvus.db.retenues.RERetenuesPaiement;
import globaz.corvus.module.compta.IREModuleComptable;
import globaz.corvus.module.compta.REModuleComptableFactory;
import globaz.corvus.utils.REPmtMensuel;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JATime;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.external.IntRole;
import globaz.osiris.utils.CAUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRSession;
import globaz.prestation.utils.ged.PRGedUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Process effectuant les écritures comptable.
 * 
 * @author SCR
 */
public class RETraiterLotDecisionsProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private APIGestionComptabiliteExterne compta = null;

    private FWMemoryLog comptaMemoryLog = null;
    private String dateComptable = "";
    private String dateEcheancePaiement = "";
    private String emailObject = "";
    private String idJournalCA = null;
    private String idLot = "";
    private String idOrganeExecution = "";
    private RELot lot = null;
    /**
     * Stocke les sections par clé unique, pour éviter d'en créer plusieurs pour une même décision
     */
    private Map<String, APISection> mapSections = new HashMap<String, APISection>();
    private String numeroOG = "";
    private BISession sessionOsiris = null;

    private String isoCsTypeAvis = "";
    private String isoGestionnaire = "";
    private String isoHightPriority = "";

    public RETraiterLotDecisionsProcess() {
        super();
    }

    public RETraiterLotDecisionsProcess(BProcess parent) {
        super(parent);
    }

    public RETraiterLotDecisionsProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        comptaMemoryLog = new FWMemoryLog();

        boolean succes = true;
        List<REDecisionEntity> listDecision = new ArrayList<REDecisionEntity>();
        BTransaction transaction = getTransaction();

        REPrestationsManager mgr = null;
        REPrestations prestation = null;

        try {

            _validate();

            // Traitement initial
            // Si toutes les décisions débutent le mois prochain ou si le lot ne contient aucune prestations, ne pas
            // créer de journal en CA.

            mgr = new REPrestationsManager();
            mgr.setSession(getSession());
            mgr.setForIdLot(getIdLot());
            mgr.find(transaction, BManager.SIZE_NOLIMIT);
            boolean isEcritureComptable = false;
            if (mgr.getSize() == 0) {
                isEcritureComptable = false;
            } else {
                for (int i = 0; i < mgr.size(); i++) {
                    prestation = (REPrestations) mgr.getEntity(i);
                    if (!JadeStringUtil.isBlankOrZero(prestation.getMontantPrestation())) {
                        isEcritureComptable = true;
                        break;
                    }
                }
            }

            RELot lot = new RELot();
            lot.setSession(getSession());
            lot.setIdLot(getIdLot());
            lot.retrieve(transaction);
            PRAssert.notIsNew(lot, "");

            // instanciation du processus de compta
            sessionOsiris = PRSession.connectSession(getSession(), CAApplication.DEFAULT_APPLICATION_OSIRIS);

            if (isEcritureComptable) {

                comptaMemoryLog = initCompta();
            } else {
                compta = null;
            }

            REModuleComptableFactory factory = REModuleComptableFactory.getInstance();
            factory.initIdsRubriques(sessionOsiris);

            // Début du traitement....
            lot.setCsEtatLot(IRELot.CS_ETAT_LOT_EN_TRAITEMENT);
            lot.update(transaction);

            mgr.find(transaction, BManager.SIZE_NOLIMIT);
            for (int ii = 0; ii < mgr.size(); ii++) {
                prestation = (REPrestations) mgr.getEntity(ii);

                REDecisionEntity decision = new REDecisionEntity();
                decision.setSession(getSession());
                decision.setIdDecision(prestation.getIdDecision());
                decision.retrieve(transaction);

                if (!IREDecision.CS_ETAT_VALIDE.equals(decision.getCsEtat())) {
                    throw new Exception(getSession().getLabel("ERREUR_DECISION_PAS_VALIDEE") + decision.getIdDecision());
                }

                listDecision.add(decision);

                // Récupération du bénéficiaire principal.

                IREModuleComptable[] mcs = factory.getModules(decision, isEcritureComptable);
                for (IREModuleComptable mc : mcs) {
                    getMemoryLog().logMessage(
                            mc.doTraitement(this, compta, getSession(), transaction, decision, getDateComptable(),
                                    lot.getIdLot(), getDateEcheancePaiement()));
                }

                // MAJ de la prestation
                prestation.setCsEtat(IREPrestations.CS_ETAT_PRE_DEFINITIF);
                prestation.update(transaction);

                desactiverAvances(getSession(), transaction, decision);
                doMAJRetenuesFacturesARestituer(getSession(), transaction, prestation, lot, comptaMemoryLog);

                // On réinitialise les sections par décision.
                // Il est possible d'avoir 2 décisions pour une même personne, et dans ce cas, les sections doivent être
                // différente.
            }

            if (comptaMemoryLog.hasErrors()) {
                throw new Exception("Error in compta : " + comptaMemoryLog.toString());
            }

            if (compta != null) {
                compta.comptabiliser();
                doPreparerOG(getIdOrganeExecution(), getNumeroOG(), getDateEcheancePaiement(), lot.getDescription());
            }

            // MAJ du lot
            lot.setCsEtatLot(IRELot.CS_ETAT_LOT_VALIDE);
            lot.setDateEnvoiLot(dateComptable);

            // Si aucune écriture comptable, le journal en CA n'est pas créé donc pourra être null.
            if (compta != null) {
                lot.setIdJournalCA(idJournalCA);
            }

            lot.update(transaction);
            transaction.commit();

        } catch (Exception e) {
            succes = false;
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            if (transaction.hasErrors()) {
                getMemoryLog().logMessage(transaction.getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }
            if (getSession().hasErrors()) {
                getMemoryLog().logMessage(getSession().getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }

            try {
                transaction.rollback();
            } catch (Exception e1) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            }
            return false;
        } finally {

            if (succes) {
                try {
                    mettreDecisionEnGED(listDecision,
                            (BSession) PRSession.connectSession(getSession(), REApplication.DEFAULT_APPLICATION_CORVUS));
                } catch (Exception e) {
                    succes = false;
                    e.printStackTrace();
                }
            }

            if (succes) {
                emailObject = getSession().getLabel("COMPTABILISATION_DU_LOT_NO") + getIdLot() + " : STATUT = SUCCES";
            } else {
                emailObject = getSession().getLabel("COMPTABILISATION_DU_LOT_NO") + getIdLot() + " : STATUT = ERREUR";
            }
            try {
                transaction.closeTransaction();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return true;
    }

    private static void mettreDecisionEnGED(List<REDecisionEntity> listDecision, BSession session) throws Exception {
        // Lancer l'impression de la décision + Mise en GED

        for (REDecisionEntity decision : listDecision) {
            // Mise en GED
            // Inforom 529
            // Une décision peu avoir plusieurs numéros en fonction du type, il faut donc tous les tester
            ArrayList<String> docNumbers = new ArrayList<String>();
            docNumbers.add(IRENoDocumentInfoRom.DECISION_DE_RENTES);
            docNumbers.add(IRENoDocumentInfoRom.DECISION_DE_RENTES_API);
            docNumbers.add(IRENoDocumentInfoRom.DECISION_DE_RENTES_INVALIDITE);
            docNumbers.add(IRENoDocumentInfoRom.DECISION_DE_RENTES_SURVIVANT);
            docNumbers.add(IRENoDocumentInfoRom.DECISION_DE_RENTES_VIEILLESSE);
            if (PRGedUtils.isDocumentsInGed(docNumbers, session)) {

                REImprimerDecisionProcess imprimerDecision = new REImprimerDecisionProcess();
                imprimerDecision.setSession(session);
                imprimerDecision.setIdDecision(decision.getIdDecision());
                imprimerDecision.setIdDemandeRente(decision.getIdDemandeRente());
                imprimerDecision.setDateDocument(decision.getDateDecision());
                imprimerDecision.setEMailAddress(decision.getAdresseEMail());
                imprimerDecision.setIsSendToGed(true);
                imprimerDecision.setIsOnlySendGED(Boolean.TRUE);

                BProcessLauncher.start(imprimerDecision, false);

            }
        }
    }

    @Override
    protected void _validate() throws Exception {
        super._validate();

        RELot lot = new RELot();
        lot.setSession(getSession());
        lot.setIdLot(getIdLot());
        lot.retrieve(getTransaction());
        if (lot.isNew()) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_LOT_NON_RENSEIGNE"));
            return;
        }
        if (IRELot.CS_ETAT_LOT_VALIDE.equals(lot.getCsEtatLot())) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_LOT_DEJA_VALIDE"));
        }

        if (!IRELot.CS_TYP_LOT_DECISION.equals(lot.getCsTypeLot())) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_LOT_TYPE_DEC_UNIQUEMENT"));
        }

        if (IRELot.CS_ETAT_LOT_EN_TRAITEMENT.equals(lot.getCsEtatLot())) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_LOT_EN_TRAITEMENT"));
        }

        if (JadeStringUtil.isBlankOrZero(getDateEcheancePaiement())) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_DATE_ECHEANCE_NON_RENSEIGNEE"));
        }

        if (JadeStringUtil.isBlankOrZero(getDateComptable())) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_DATE_COMPTABLE_NON_RENSEIGNEE"));
        }

        JADate dEch = new JADate(getDateEcheancePaiement());
        JADate dComptable = new JADate(getDateComptable());

        JACalendar cal = new JACalendarGregorian();
        if (cal.compare(dEch, dComptable) == JACalendar.COMPARE_FIRSTLOWER) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_DATE_ECHEANCE_INF_DATE_COMPTABLE"));
        }

        JADate today = new JADate(JACalendar.todayJJsMMsAAAA());

        if (cal.compare(dEch, today) != JACalendar.COMPARE_FIRSTUPPER) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_DATE_ECHEANCE_SUP_DATE_JOURS"));
        }

        // BZ 5459
        if (!JadeNumericUtil.isInteger(getNumeroOG())
                || (((Integer.parseInt(getNumeroOG())) < 1) || (Integer.parseInt(getNumeroOG()) > 99))) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_NUMERO_OG_OBLIGATOIRE"));
        }
    }

    /**
     * Recherche de toutes les rentes accordée liées à la décision.<br />
     * Pour chacun des bénéficiaire de ces RA, on récupère ses éventuelles avances, et on les désactives.
     */
    private void desactiverAvances(BSession session, BTransaction transaction, REDecisionEntity decision)
            throws Exception {

        REValidationDecisionsManager mgr = new REValidationDecisionsManager();
        mgr.setSession(session);
        mgr.setForIdDecision(decision.getIdDecision());
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        List<String> idsTiers = new ArrayList<String>();

        for (int i = 0; i < mgr.size(); i++) {

            REValidationDecisions validDec = (REValidationDecisions) mgr.getEntity(i);
            REPrestationDue prstDue = new REPrestationDue();
            prstDue.setSession(session);
            prstDue.setIdPrestationDue(validDec.getIdPrestationDue());
            prstDue.retrieve(transaction);
            PRAssert.notIsNew(prstDue, null);

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(session);
            ra.setIdPrestationAccordee(prstDue.getIdRenteAccordee());
            ra.retrieve(transaction);
            PRAssert.notIsNew(ra, null);

            if (!idsTiers.contains(ra.getIdTiersBeneficiaire())) {
                idsTiers.add(ra.getIdTiersBeneficiaire());
            }
        }

        String listIdTiers = "";
        Iterator<String> iter = idsTiers.iterator();
        while (iter.hasNext()) {
            String idt = iter.next();
            if (iter.hasNext()) {
                listIdTiers += idt + ", ";
            } else {
                listIdTiers += idt;
            }
        }

        REAvanceManager avmgr = new REAvanceManager();
        avmgr.setSession(session);
        avmgr.setForIdTiersIn(listIdTiers);
        avmgr.setForCsEtatAcomptesDifferentDe(IREAvances.CS_ETAT_ACOMPTE_TERMINE);
        avmgr.find(transaction);
        for (int i = 0; i < avmgr.size(); i++) {
            REAvance avance = (REAvance) avmgr.get(i);
            avance.retrieve(transaction);
            avance.setCsEtatAcomptes(IREAvances.CS_ETAT_ACOMPTE_TERMINE);
            // bz-5397
            avance.wantCallValidate(false);
            avance.update(transaction);
        }

        avmgr.setForCsEtat1erAcomptesDifferentDe(IREAvances.CS_ETAT_ACOMPTE_TERMINE);
        avmgr.find(transaction);
        for (int i = 0; i < avmgr.size(); i++) {
            REAvance avance = (REAvance) avmgr.get(i);
            avance.retrieve(transaction);
            avance.setCsEtat1erAcompte(IREAvances.CS_ETAT_ACOMPTE_TERMINE);
            // bz-5397
            avance.wantCallValidate(false);
            avance.update(transaction);
        }
    }

    protected void doMAJRetenuesFacturesARestituer(BSession session, BTransaction transaction,
            REPrestations prestation, RELot lot, FWMemoryLog log) throws Exception {

        // MAJ des retenues / facture à restituer. le cas échéant....
        RESoldePourRestitutionManager mgr = new RESoldePourRestitutionManager();
        mgr.setSession(getSession());
        mgr.setForIdPrestation(prestation.getIdPrestation());
        mgr.find(transaction, BManager.SIZE_NOLIMIT);
        for (int i = 0; i < mgr.size(); i++) {
            RESoldePourRestitution elm = (RESoldePourRestitution) mgr.getEntity(i);

            REOrdresVersements ov = new REOrdresVersements();
            ov.setSession(getSession());
            ov.setIdOrdreVersement(elm.getIdOrdreVersement());
            ov.retrieve(transaction);
            PRAssert.notIsNew(ov, "OV lié au solde pour restit non trouvé. SPR.id = " + elm.getIdSoldePourRestitution());

            if (IRESoldePourRestitution.CS_RETENUES.equals(elm.getCsTypeRestitution())) {
                // Recherche de la rente en cours pour ce tiers...
                // On recherche une rente en cours pour ce membre de famille

                RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager mgr3 = new RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager();
                mgr3.setSession(getSession());
                mgr3.setForIdDecision(prestation.getIdDecision());
                mgr3.find(transaction, BManager.SIZE_NOLIMIT);

                // La retenue sera automatiquement créé sur la 1ère rente en cours de la décision
                RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions renteCourante = null;

                // bz-4456

                // bz-6151
                // On récupère la 1ère rente active de la décision, c'est à dire sans date de fin de droit
                if (!mgr3.isEmpty()) {
                    for (int j = 0; j < mgr3.size(); j++) {
                        renteCourante = (RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions) mgr3.getEntity(j);
                        if (JadeStringUtil.isBlankOrZero(renteCourante.getDateFinDroit())) {
                            break;
                        } else {
                            renteCourante = null;
                        }
                    }
                }

                if (renteCourante == null) {
                    throw new Exception(
                            "Erreur, Aucune RA en cours trouvée. Retenue impossible. idPrestation/idDecision = "
                                    + prestation.getIdPrestation() + "/" + prestation.getIdDecision());
                }

                // On crée la retenue sur facture existante
                RERetenuesPaiement retenue = new RERetenuesPaiement();
                retenue.setSession(session);

                // création de l'idExterne
                retenue.setCsTypeRetenue(IRERetenues.CS_TYPE_FACTURE_EXISTANTE);
                JACalendar cal = new JACalendarGregorian();
                JADate dateProchainPmt = new JADate(REPmtMensuel.getDateDernierPmt(session));
                dateProchainPmt = cal.addMonths(dateProchainPmt, 1);
                retenue.setDateDebutRetenue(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateProchainPmt.toStrAMJ()));
                retenue.setIdDomaineApplicatif(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
                retenue.setIdRenteAccordee(renteCourante.getIdPrestationAccordee());

                String idSection = ov.getIdSection();
                String idCategorie = null;
                if (IREOrdresVersements.CS_TYPE_DETTE_RENTE_AVANCES.equals(ov.getCsType())) {
                    idCategorie = APISection.ID_CATEGORIE_SECTION_AVANCE;
                } else if (IREOrdresVersements.CS_TYPE_DETTE_RENTE_DECISION.equals(ov.getCsType())) {
                    idCategorie = APISection.ID_CATEGORIE_SECTION_DECISION;
                } else if (IREOrdresVersements.CS_TYPE_DETTE_RENTE_PRST_BLOQUE.equals(ov.getCsType())) {
                    idCategorie = APISection.ID_CATEGORIE_SECTION_PRESTATIONS_BLOQUEES;
                } else if (IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION.equals(ov.getCsType())) {
                    idCategorie = APISection.ID_CATEGORIE_SECTION_RESTITUTIONS;
                } else if (IREOrdresVersements.CS_TYPE_DETTE_RENTE_RETOUR.equals(ov.getCsType())) {
                    idCategorie = APISection.ID_CATEGORIE_SECTION_RETOUR;
                } else if ((IREOrdresVersements.CS_TYPE_DETTE.equals(ov.getCsType()))) {
                    idCategorie = APISection.ID_CATEGORIE_SECTION_RESTITUTIONS;
                }

                APISection section = null;
                if (!JadeStringUtil.isBlankOrZero(idSection)) {

                    section = (APISection) PRSession.connectSession(session, "OSIRIS").getAPIFor(APISection.class);

                    section.setIdSection(idSection);
                    section.retrieve(transaction);
                }
                // si la section n'est pas défini par ce biais
                if ((section == null) || section.isNew()) {
                    // recherche de la section comptable
                    String idCA = renteCourante.loadInformationsComptabilite().getIdCompteAnnexe();
                    if (JadeStringUtil.isBlankOrZero(idCA)) {
                        throw new Exception("Aucun compte annexe trouvé idCA/idRA " + idCA + "/"
                                + renteCourante.getIdPrestationAccordee());
                    }

                    String idExterneRole = PRTiersHelper.getTiersParId(session, renteCourante.getIdTiersBeneficiaire())
                            .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                    section = retrieveSection(transaction, prestation.getIdDemandeRente(), idExterneRole, idCA,
                            idCategorie);

                    idSection = section.getIdSection();
                }

                retenue.setIdExterne(section.getCompteAnnexe().getIdExterneRole());
                retenue.setRole(section.getCompteAnnexe().getIdRole());
                retenue.setNoFacture(section.getIdExterne());
                retenue.setIdTypeSection(section.getTypeSection().getIdTypeSection());

                retenue.setReferenceInterne(idSection);
                retenue.setMontantDejaRetenu("0");
                retenue.setMontantRetenuMensuel(elm.getMontantMensuelARetenir());
                retenue.setMontantTotalARetenir(elm.getMontant());

                retenue.add(transaction);

                elm.retrieve(transaction);
                elm.setIdRetenue(retenue.getIdRetenue());
                elm.wantCallValidate(false);
                elm.update(transaction);

                // mise à jour de la prestation accordée
                REPrestationsAccordees pa = new REPrestationsAccordees();
                pa.setSession(getSession());
                pa.setIdPrestationAccordee(renteCourante.getIdPrestationAccordee());
                pa.retrieve(transaction);

                pa.setIsRetenues(Boolean.TRUE);
                pa.update(transaction);
            }
        }
    }

    private void doPreparerOG(String idOG, String numeroOG, String dateEcheancePaiement, String description)
            throws Exception {
        String libelleOG = description;
        if (compta != null) {
            getMemoryLog().logMessage("Préparation de l'OG : " + (new JATime(JACalendar.now())).toStr(":"),
                    FWMessage.INFORMATION, "");
            int n = Integer.parseInt(numeroOG);
            if (n < 10) {
                libelleOG = "OPAE 0" + n + "-" + libelleOG;
            } else {
                libelleOG = "OPAE " + n + "-" + libelleOG;
            }
            compta.preparerOrdreGroupe(idOG, String.valueOf(n), dateEcheancePaiement, CAOrdreGroupe.VERSEMENT,
                    CAOrdreGroupe.NATURE_RENTES_AVS_AI, libelleOG, isoCsTypeAvis, isoGestionnaire, isoHightPriority);
        }
    }

    public String getDateComptable() {
        return dateComptable;
    }

    public String getDateEcheancePaiement() {
        return dateEcheancePaiement;
    }

    @Override
    protected String getEMailObject() {
        return emailObject;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    private String getKeySection(String idDemandeRente, String idExterneRole, String idCategorieSection, String idCA) {
        return idDemandeRente + "-" + idExterneRole + "-" + idCategorieSection + "-" + idCA;
    }

    public String getNumeroOG() {
        return numeroOG;
    }

    private FWMemoryLog initCompta() throws Exception {

        compta = (APIGestionComptabiliteExterne) sessionOsiris.getAPIFor(APIGestionComptabiliteExterne.class);
        compta.setDateValeur(dateComptable);
        compta.setEMailAddress(getEMailAddress());
        comptaMemoryLog.setSession((BSession) sessionOsiris);
        compta.setMessageLog(comptaMemoryLog);

        compta.setSendCompletionMail(false);
        compta.setTransaction(getTransaction());
        compta.setProcess(this);
        if (lot != null) {
            compta.setLibelle(lot.getDescription());
        } else {
            compta.setLibelle("Décision rentes - " + getDateComptable());
        }

        // Doit être stocké à cet instant, car l'appel à compta.getJournal() après comptabilisation retourne un journal
        // null car n'est plus dans l'état ouvert.
        idJournalCA = compta.createJournal().getIdJournal();

        return comptaMemoryLog;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public APISection retrieveSection(BTransaction transaction, String idDemandeRente, String idExterneRole,
            String idCompteAnnexe, String idCategorieSection) throws Exception {

        if (mapSections.containsKey(getKeySection(idDemandeRente, idExterneRole, idCategorieSection, idCompteAnnexe))) {

            return mapSections.get(getKeySection(idDemandeRente, idExterneRole, idCategorieSection, idCompteAnnexe));
        }

        else {
            String typeSection = null;
            if (APISection.ID_CATEGORIE_SECTION_RETOUR.equals(idCategorieSection)) {
                typeSection = APISection.ID_TYPE_SECTION_RETOUR;
            } else if (APISection.ID_CATEGORIE_SECTION_RESTITUTIONS.equals(idCategorieSection)) {
                typeSection = APISection.ID_TYPE_SECTION_RESTITUTION;

            } else if (APISection.ID_CATEGORIE_SECTION_PRESTATIONS_BLOQUEES.equals(idCategorieSection)) {
                typeSection = APISection.ID_TYPE_SECTION_BLOCAGE;
            } else if (APISection.ID_CATEGORIE_SECTION_DECISION.equals(idCategorieSection)) {
                typeSection = APISection.ID_TYPE_SECTION_RENTE_AVS_AI;
            } else if (APISection.ID_CATEGORIE_SECTION_AVANCE.equals(idCategorieSection)) {
                typeSection = APISection.ID_TYPE_SECTION_AVANCES;
            }

            else {
                throw new Exception("Unsupporter idCategorieSection : " + idCategorieSection);
            }

            // on créé un numéro de facture unique qui servira a créer la section
            String noFacture = CAUtil
                    .creerNumeroSectionUnique(sessionOsiris, transaction, IntRole.ROLE_RENTIER, idExterneRole,
                            typeSection, String.valueOf(new JADate(dateComptable).getYear()), idCategorieSection);

            if (compta == null) {
                initCompta();
            }

            APISection section = compta.getSectionByIdExterne(idCompteAnnexe, typeSection, noFacture,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, null);

            mapSections.put(getKeySection(idDemandeRente, idExterneRole, idCategorieSection, idCompteAnnexe), section);
            return section;
        }
    }

    /**
     * setter pour l'attribut date comptable
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateComptable(String string) {
        dateComptable = string;
    }

    public void setDateEcheancePaiement(String dateEcheancePaiement) {
        this.dateEcheancePaiement = dateEcheancePaiement;
    }

    /**
     * setter pour l'attribut no lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdLot(String string) {
        idLot = string;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setNumeroOG(String numeroOG) {
        this.numeroOG = numeroOG;
    }

    public String getIsoCsTypeAvis() {
        return isoCsTypeAvis;
    }

    public void setIsoCsTypeAvis(String isoCsTypeAvis) {
        this.isoCsTypeAvis = isoCsTypeAvis;
    }

    public String getIsoGestionnaire() {
        return isoGestionnaire;
    }

    public void setIsoGestionnaire(String isoGestionnaire) {
        this.isoGestionnaire = isoGestionnaire;
    }

    public String getIsoHightPriority() {
        return isoHightPriority;
    }

    public void setIsoHightPriority(String isoHightPriority) {
        this.isoHightPriority = isoHightPriority;
    }

}
