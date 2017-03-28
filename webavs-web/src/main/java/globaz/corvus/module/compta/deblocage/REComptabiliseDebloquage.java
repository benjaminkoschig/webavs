package globaz.corvus.module.compta.deblocage;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.deblocage.REDeblocageVersement;
import globaz.corvus.db.deblocage.REDeblocageVersementService;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.lots.RELotManager;
import globaz.corvus.module.compta.AREModuleComptable;
import globaz.corvus.module.compta.REModuleComptableFactory;
import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.api.BIMessageLog;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRSession;
import java.util.List;
import com.google.common.base.Joiner;

public class REComptabiliseDebloquage extends AREModuleComptable {

    private BSession sessionOsiris;
    private BSession session;

    public REComptabiliseDebloquage(BSession session) throws Exception {
        super(true);
        this.session = session;
        sessionOsiris = (BSession) PRSession.connectSession(session, CAApplication.DEFAULT_APPLICATION_OSIRIS);
    }

    public void comptabilise(BProcess process, Long idLot) throws Exception {

        RELot lot = retriveLot();
        APIGestionComptabiliteExterne compta = initCompta(process);
        lot.setIdJournalCA(compta.getJournal().getIdJournal());
        BIMessageLog log = compta.getMessageLog();

        REDeblocageVersementService deblocageVersementService = new REDeblocageVersementService(session);
        List<REDeblocageVersement> deblocageVersements = deblocageVersementService.searchByIdLot(idLot);

        String dateComptable = getDateValeurComptable();

        for (REDeblocageVersement versement : deblocageVersements) {
            PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, versement.getIdTiersBeneficiaire().toString());
            String motifVersement = getMotifVersementDeblocage(session, tw, versement.getRefPaiement(),

            versement.getCodeRenteAccordee(), versement.getIdTiersAdressePaiement().toString());
            if (versement.getType().isCreancier() || versement.getType().isVersementBeneficiaire()) {
                // if ((adr == null) || adr.isNew()) {
                // throw new Exception(session.getLabel("ERREUR_AUCUNE_ADRESSE_PMT_TROUVEE_POUR_TIERS")
                // + versement.getIdTiersBeneficiaire());
                // }
                log.logMessage(doOrdreVersement(session, compta, versement.getIdCompteAnnexe(), versement
                        .getLigneDeblocageVentilation().getIdSectionSource().toString(), versement.getMontant()
                        .toString(), versement.getIdTiersAdressePaiement(), motifVersement, dateComptable, false));
            } else if (versement.getType().isDetteEnCompta()) {
                doEcriture(session, compta, versement.getMontant().toStringValue(),
                        REModuleComptableFactory.getInstance().COMPENSATION, versement.getIdCompteAnnexe(), versement
                                .getLigneDeblocageVentilation().getIdSectionSource().toString(), dateComptable,
                        motifVersement);

                doEcriture(session, compta, versement.getMontant().negate().toStringValue(),
                        REModuleComptableFactory.getInstance().COMPENSATION, versement.getIdCompteAnnexe(), versement
                                .getLigneDeblocageVentilation().getIdSectionSource().toString(), dateComptable,
                        motifVersement);
            }

        }
        compta.comptabiliser();
        // ecritures.add(this.generateEcriture(SectionPegasus.DECISION_PC, APIEcriture.DEBIT,
        // APIReferenceRubrique.COMPENSATION_RENTES, montant, null, compteAnnexe.getIdCompteAnnexe(),
        // TypeEcriture.DETTE, ov));
        //
        // //positif=>
        // ecritures.add(this.generateEcriture(null, APIEcriture.CREDIT, APIReferenceRubrique.COMPENSATION_RENTES,
        // montant, section, section.getIdCompteAnnexe(), TypeEcriture.DETTE, ov));
    }

    private RELot retriveLot() throws Exception {
        RELotManager mgr = new RELotManager();
        mgr.setSession(session);
        mgr.setForCsEtatDiffentDe(IRELot.CS_ETAT_LOT_OUVERT);
        mgr.setForCsType(IRELot.CS_TYP_LOT_DEBLOCAGE_RA);
        mgr.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
        mgr.setOrderBy(RELot.FIELDNAME_ID_LOT + " DESC ");
        mgr.find(1);
        return (RELot) mgr.getFirstEntity();
    }

    private APIGestionComptabiliteExterne initCompta(BProcess process) throws Exception {
        APIGestionComptabiliteExterne compta = (APIGestionComptabiliteExterne) sessionOsiris
                .getAPIFor(APIGestionComptabiliteExterne.class);
        compta.setDateValeur(getDateValeurComptable());
        compta.setEMailAddress(process.getEMailAddress());
        compta.setSendCompletionMail(false);
        String libelle = (session).getLabel("JOURNAL_PMT_RENTES_BLOQUEES");
        if (libelle.length() > 20) {
            libelle = libelle.substring(0, 20);
        }
        compta.setLibelle(libelle);
        compta.setProcess(process);
        compta.setTransaction(session.getCurrentThreadTransaction());
        compta.createJournal();
        return compta;
    }

    /**
     * Ex.
     * 
     * today = 10.08.2007 date dernier pmt = 08.2007 return 10.08.2007
     * 
     * 
     * today = 31.08.2007 date dernier pmt = 09.2007 return 01.09.2007
     * 
     * @return Date valeur comptable au format jj.mm.aaaa
     * @throws JAException
     */
    private String getDateValeurComptable() throws JAException {
        JACalendar cal = new JACalendarGregorian();
        JADate todayMMxAAAA = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(JACalendar.todayJJsMMsAAAA()));
        JADate dateDernierPmt = new JADate(REPmtMensuel.getDateDernierPmt(session));

        if (cal.compare(todayMMxAAAA, dateDernierPmt) == JACalendar.COMPARE_FIRSTLOWER) {
            return "01." + PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateDernierPmt.toStrAMJ());
        } else {
            return JACalendar.todayJJsMMsAAAA();
        }
    }

    private List<CACompteAnnexe> retrieveCompteAnnexe(List<String> idsCompteAnnexe) throws Exception {

        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.setSession(session);
        manager.setForIdCompteAnnexeIn(Joiner.on(",").join(idsCompteAnnexe));
        manager.find(BManager.SIZE_NOLIMIT);

        return manager.toList();
    }
}