/**
 * @author : scr
 * @date : 18.10.2007
 */
package globaz.corvus.module.compta.deblocage;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.lots.RELotManager;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager;
import globaz.corvus.module.compta.AREModuleComptable;
import globaz.corvus.module.compta.REModuleComptableFactory;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIJournal;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAJournal;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRSession;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;

/**
 * Description Génère les écritures comptables pour le déblocage d'une rente accordée
 * 
 * @author scr
 * 
 */
public class REModuleComptablePmtPrstBloquee extends AREModuleComptable {

    // Class statique, référence par cette instance
    private static REModuleComptablePmtPrstBloquee instance = null;

    /**
     * @return L'instance de cette classe
     * @throws Exception
     */
    public static synchronized REModuleComptablePmtPrstBloquee getInstance(BSession session) throws Exception {
        if (REModuleComptablePmtPrstBloquee.instance == null) {
            REModuleComptablePmtPrstBloquee.instance = new REModuleComptablePmtPrstBloquee(session, true);
        }
        return REModuleComptablePmtPrstBloquee.instance;
    }

    private BSession sessionOsiris = null;

    private REModuleComptablePmtPrstBloquee(BSession session, boolean isGenererEcritureComptable) throws Exception {
        super(isGenererEcritureComptable);
        sessionOsiris = (BSession) PRSession.connectSession(session, CAApplication.DEFAULT_APPLICATION_OSIRIS);

    }

    /**
     * 
     * 
     * 
     * 
     * 
     * @param process
     * @param compta
     * @param session
     * @param transaction
     * @param ra
     * @param idSection
     * @param montant
     * @param idTiersAdrPmt
     * @param idDomaine
     * @param refPmt
     * @param doOV
     * @return An array of 2 memory log. memoryLog[0] = comptaMemoryLog memoryLog[1] = information for mail memoryLog
     * @throws Exception
     */
    public FWMemoryLog debloquerRenteAccordee(BProcess process, APIGestionComptabiliteExterne compta,
            BISession session, BTransaction transaction, RERenteAccordee ra, String idSection, String montant,
            String idTiersAdrPmt, String idDomaine, String refPmt, boolean doOV) throws Exception {

        FWMemoryLog memoryLog = new FWMemoryLog();

        memoryLog.logMessage(((BSession) session).getLabel("DEBLOCAGE_RENTE_ACC"), FWMessage.INFORMATION, this
                .getClass().getName());

        JACalendar cal = new JACalendarGregorian();

        // Récupération du bénéficiaire principal.
        RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager mgr = new RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager();
        mgr.setSession((BSession) session);
        mgr.setForIdRenteAccordee(ra.getIdPrestationAccordee());
        mgr.find(transaction);

        if (mgr.getSize() == 0) {
            throw new Exception(((BSession) session).getLabel("ERREUR_AUCUNE_DEC_TROUVEE_RA")
                    + ra.getIdPrestationAccordee());
        }

        RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions elm = (RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions) mgr
                .get(0);

        REDecisionEntity decision = new REDecisionEntity();
        decision.setSession((BSession) session);
        decision.setIdDecision(elm.getIdDecision());
        decision.retrieve(transaction);
        PRAssert.notIsNew(decision, null);

        PRTiersWrapper tw = initTiersBeneficiairePrincipal((BSession) session, transaction, decision);
        // APIGestionComptabiliteExterne compta = initCompta(process,
        // (BSession)session, transaction, cal);

        // Récupération de l'adresse de paiement par cascade...
        // Si pas trouvé pour le domaine, prendra le domaine standard.
        TIAdressePaiementData adr = null;
        APICompteAnnexe compteAnnexe = null;

        if (doOV) {

            adr = PRTiersHelper.getAdressePaiementData((BSession) session, transaction, idTiersAdrPmt, idDomaine, "",
                    JACalendar.todayJJsMMsAAAA());
            if ((adr == null) || adr.isNew()) {
                throw new Exception(((BSession) session).getLabel("ERREUR_AUCUNE_ADRESSE_PMT_TROUVEE_POUR_TIERS")
                        + decision.getIdTiersBeneficiairePrincipal());
            }

            // récupération du compte annexe RENTIER
            compteAnnexe = retrieveCompteAnnexe((BSession) session, transaction, ra);

            // Initialisation des rubriques
            if (REModuleComptableFactory.getInstance().RO_AVS == null) {
                REModuleComptableFactory.getInstance().initIdsRubriques(sessionOsiris);
            }

        }

        /*
         * 
         * Initialisation de la section
         */
        APISection section = (APISection) sessionOsiris.getAPIFor(APISection.class);
        section.setIdSection(idSection);
        section.retrieve(transaction);

        FWCurrency montantADebloquer = new FWCurrency(montant);
        FWCurrency soldeSct = new FWCurrency(section.getSolde());
        soldeSct.abs();

        // Au maximum, on débloque le solde de la section.
        if (soldeSct.compareTo(montantADebloquer) == -1) {
            montant = montantADebloquer.toString();
        }

        String motifVersement = getMotifVersementDeblocage((BSession) session, tw, refPmt, ra.getCodePrestation(),
                idTiersAdrPmt);
        String dateValeurComptable = getDateValeurComptable((BSession) session, cal);

        if (doOV) {
            memoryLog.logMessage(doOrdreVersement((BSession) session, compta, compteAnnexe.getIdCompteAnnexe(),
                    idSection, montant.toString(), adr.getIdAvoirPaiementUnique(), motifVersement, dateValeurComptable,
                    false));

        } else {

            memoryLog.logMessage(
                    FWMessageFormat.format(((BSession) session).getLabel("DIM_RA_BLOQUE_MNT_DISPO"), montant),
                    FWViewBeanInterface.WARNING, this.getClass().getName());
        }

        return memoryLog;
    }

    /**
     * Ex.
     * 
     * today = 10.08.2007 date dernier pmt = 08.2007 return 10.08.2007
     * 
     * 
     * today = 31.08.2007 date dernier pmt = 09.2007 return 01.09.2007
     * 
     * 
     * @param session
     * @param cal
     * @return Date valeur comptable au format jj.mm.aaaa
     * @throws JAException
     */
    private String getDateValeurComptable(BSession session, JACalendar cal) throws JAException {

        JADate todayMMxAAAA = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(JACalendar.todayJJsMMsAAAA()));
        JADate dateDernierPmt = new JADate(REPmtMensuel.getDateDernierPmt(session));

        if (cal.compare(todayMMxAAAA, dateDernierPmt) == JACalendar.COMPARE_FIRSTLOWER) {
            return "01." + PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateDernierPmt.toStrAMJ());
        } else {
            return JACalendar.todayJJsMMsAAAA();
        }

    }

    /**
     * 
     * Retourne le dernier journal de diminution ouvert en CA. Retourne null si aucun journal trouvé ou si le journal a
     * déjà été comptabilisé.
     * 
     * @param session
     * @param sessionOsiris
     * @param transaction
     * @return le dernier journal de diminution ouvert en CA. Peut retourné null
     * @throws Exception
     */
    private APIJournal getJournalRentesBloquees(BSession session, BSession sessionOsiris, BTransaction transaction)
            throws Exception {

        RELotManager mgr = new RELotManager();
        mgr.setSession(session);
        mgr.setForCsType(IRELot.CS_TYP_LOT_DEBLOCAGE_RA);
        mgr.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
        mgr.setOrderBy(RELot.FIELDNAME_ID_LOT + " DESC ");
        mgr.find(transaction, 1);

        if (!mgr.isEmpty()) {
            APIJournal jrn = (APIJournal) sessionOsiris.getAPIFor(APIJournal.class);
            jrn.setIdJournal(((RELot) mgr.getFirstEntity()).getIdJournalCA());
            jrn.retrieve(transaction);
            if (!jrn.isNew() && (CAJournal.OUVERT.equals(jrn.getEtat()) || CAJournal.TRAITEMENT.equals(jrn.getEtat()))) {

                return jrn;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public APIGestionComptabiliteExterne initCompta(BProcess process, BSession session, BTransaction transaction)
            throws Exception {
        APIGestionComptabiliteExterne compta = null;

        // instanciation du processus de compta
        JACalendar cal = new JACalendarGregorian();
        compta = (APIGestionComptabiliteExterne) sessionOsiris.getAPIFor(APIGestionComptabiliteExterne.class);
        String dateValeurComptable = getDateValeurComptable(session, cal);
        compta.setDateValeur(dateValeurComptable);
        compta.setEMailAddress(process.getEMailAddress());

        FWMemoryLog comptaMemoryLog = new FWMemoryLog();
        comptaMemoryLog.setSession(sessionOsiris);
        compta.setMessageLog(comptaMemoryLog);

        compta.setSendCompletionMail(false);
        compta.setTransaction(transaction);
        String libelle = (session).getLabel("JOURNAL_PMT_RENTES_BLOQUEES");
        if (libelle.length() > 20) {
            libelle = libelle.substring(0, 20);
        }
        compta.setLibelle(libelle);
        compta.setProcess(process);
        compta.createJournal();

        RELot lot = new RELot();
        lot.setSession(session);
        lot.setCsEtatLot(IRELot.CS_ETAT_LOT_VALIDE);
        lot.setCsTypeLot(IRELot.CS_TYP_LOT_DEBLOCAGE_RA);
        lot.setCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
        lot.setDescription(libelle);
        lot.setIdJournalCA(compta.getJournal().getIdJournal());
        lot.add(transaction);
        return compta;
    }

    private PRTiersWrapper initTiersBeneficiairePrincipal(BSession session, BTransaction transaction,
            REDecisionEntity decision) throws Exception {
        String idTiersBeneficiairePrincipal = getIdTiersBeneficiairePrincipal(decision, transaction);
        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, idTiersBeneficiairePrincipal);

        REPrestations prst = decision.getPrestation(transaction);
        if (prst == null) {
            throw new Exception((session).getLabel("ERREUR_AUCUNE_PREST_POUR_DEC") + decision.getIdDecision());
        }

        return tw;
    }

    private APICompteAnnexe retrieveCompteAnnexe(BSession session, BTransaction transaction, RERenteAccordee ra)
            throws Exception {
        String idCA = ra.loadInformationsComptabilite().getIdCompteAnnexe();
        BISession sessionOsiris = PRSession.connectSession(session, "OSIRIS");
        APICompteAnnexe compteAnnexe = (APICompteAnnexe) sessionOsiris.getAPIFor(APICompteAnnexe.class);
        compteAnnexe.setIdCompteAnnexe(idCA);
        compteAnnexe.retrieve(transaction);

        if (compteAnnexe == null) {
            throw new Exception("Impossible de récupérer le compte annexe, contrôlez les logs pour plus de détails.");
        }

        return compteAnnexe;
    }
}
