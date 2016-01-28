package globaz.corvus.helpers.process;

import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.module.compta.deblocage.REModuleComptablePmtPrstBloquee;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.prestation.tools.PRAssert;

/**
 * 
 * @author SCR
 * 
 */
public class REDebloquerMontantRAHandler {

    private APIGestionComptabiliteExterne compta = null;
    private String idDomaine = "";
    private String idRenteAccordee = "";
    private String idSection = "";
    private String idTiersAdrPmt = "";
    private String montantADebloque = "";
    private String refPaiement = "";

    private BSession session = null;

    public REDebloquerMontantRAHandler(BProcess process, BSession session, BTransaction transaction) throws Exception {
        this.session = session;
        init(process, session, transaction);
    }

    /**
     * Chargement de l'enregistrement de la rente accordée
     * 
     * @return
     */
    private RERenteAccordee chargeRenteAccordee(BTransaction transaction) throws Exception {
        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(getSession());
        ra.setIdPrestationAccordee(getIdRenteAccordee());
        ra.retrieve(transaction);
        return ra;
    }

    public void comptabiliser() {
        compta.comptabiliser();
    }

    public FWMemoryLog doTraitement(BProcess process, boolean doOV) throws Exception {

        doValidation();

        RERenteAccordee ra = chargeRenteAccordee(process.getTransaction());

        REEnteteBlocage entete = new REEnteteBlocage();
        entete.setSession(session);
        entete.setIdEnteteBlocage(ra.getIdEnteteBlocage());
        entete.retrieve(process.getTransaction());
        PRAssert.notIsNew(entete, null);

        // MAJ de l'entête...
        FWCurrency montantDebloque = new FWCurrency(entete.getMontantDebloque());
        montantDebloque.add(getMontantADebloque());
        entete.setMontantDebloque(montantDebloque.toString());
        entete.update(process.getTransaction());

        // Effectuer les ecritures en compta
        REModuleComptablePmtPrstBloquee modulecomptablePmtPrstBloquee = REModuleComptablePmtPrstBloquee
                .getInstance(getSession());
        FWMemoryLog logs = modulecomptablePmtPrstBloquee.debloquerRenteAccordee(process, compta, session,
                process.getTransaction(), ra, idSection, montantADebloque, idTiersAdrPmt, idDomaine, refPaiement, doOV);

        // MAJ de la RA....
        if (!entete.isNew()) {
            FWCurrency montantADebloquer = new FWCurrency(entete.getMontantBloque());
            montantADebloquer.sub(entete.getMontantDebloque());
            if (montantADebloquer.isZero()) {
                ra.setIsPrestationBloquee(Boolean.FALSE);
            }
        }
        ra.setTypeDeMiseAJours("0");

        JACalendar cal = new JACalendarGregorian();
        JADate dateEcheance = new JADate(ra.getDateEcheance());
        JADate dateDernierPmt = new JADate(REPmtMensuel.getDateDernierPmt(session));
        if (cal.compare(dateEcheance, dateDernierPmt) == JACalendar.COMPARE_SECONDUPPER) {
            ra.setDateEcheance("");
        }
        ra.update(process.getTransaction());

        // Log Compta
        if (logs.hasErrors()) {
            throw new Exception(session.getLabel("ERREUR_GENERATION_ECRITURES_COMPTABLES"));
        }

        return logs;
    }

    private void doValidation() throws Exception {
        if (JadeStringUtil.isBlankOrZero(getIdSection())) {
            throw new Exception(session.getLabel("ERREUR_CHOIX_SECTION"));
        }

        if (JadeStringUtil.isBlankOrZero(getIdTiersAdrPmt()) || JadeStringUtil.isBlankOrZero(getIdDomaine())) {
            throw new Exception(session.getLabel("ADRESSE_PMT_NON_VALIDE"));
        }

        if (compta == null || compta.getJournal() == null) {
            throw new Exception("Handler not initialised");
        }
    }

    public String getIdDomaine() {
        return idDomaine;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdTiersAdrPmt() {
        return idTiersAdrPmt;
    }

    public String getMontantADebloque() {
        return montantADebloque;
    }

    public String getRefPaiement() {
        return refPaiement;
    }

    public BSession getSession() {
        return session;
    }

    private void init(BProcess process, BSession session, BTransaction transaction) throws Exception {
        compta = REModuleComptablePmtPrstBloquee.getInstance(getSession()).initCompta(process, session, transaction);
    }

    public void setIdDomaine(String idDomaine) {
        this.idDomaine = idDomaine;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdTiersAdrPmt(String idTiersAdrPmt) {
        this.idTiersAdrPmt = idTiersAdrPmt;
    }

    public void setMontantADebloque(String montantADebloque) {
        this.montantADebloque = montantADebloque;
    }

    public void setRefPaiement(String refPaiement) {
        this.refPaiement = refPaiement;
    }

}
