package globaz.corvus.vb.ordresversements;

import globaz.corvus.api.decisions.IREDecision;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.math.BigDecimal;

/**
 * @author PBA
 */
public class REOrdresVersementsViewBean implements FWViewBeanInterface {

    /** Pour l'affichage des menus */
    private String csEtatDecision;
    private String dateNaissance;
    private String detailTiers;
    /** Pour l'affichage des menus */
    private String idDecision;
    /** Pour l'affichage des menus */
    private String idDemandeRente;
    /** Pour l'affichage des menus */
    private String idLot;
    private Long idPrestation;
    /** Pour l'affichage des menus */
    private String idTierBeneficiaire;
    private Long idTierRequerant;
    /** Pour l'affichage des menus */
    private boolean isValidationDecisionAuthorisee;
    private String message;
    private BigDecimal montantInteretsMoratoires;
    /** Pour l'affichage des menus */
    private String montantPrestation;
    private BigDecimal montantPrestationsDejaVersees;
    private BigDecimal montantPrestationsDues;
    private BigDecimal montantSolde;
    private String msgType;
    private BISession session;
    private PRTiersWrapper tiers;

    public REOrdresVersementsViewBean() {
        super();

        csEtatDecision = "";
        dateNaissance = null;
        detailTiers = null;
        idDecision = "";
        idDemandeRente = "";
        idLot = "";
        idPrestation = null;
        idTierBeneficiaire = "";
        idTierRequerant = null;
        isValidationDecisionAuthorisee = false;
        message = null;
        montantInteretsMoratoires = null;
        montantPrestation = "";
        montantPrestationsDejaVersees = null;
        montantPrestationsDues = null;
        montantSolde = null;
        msgType = null;
        session = null;
        tiers = null;
    }

    public String getAppColor() {
        return Jade.getInstance().getWebappBackgroundColor();
    }

    public String getCsEtatDecision() {
        return csEtatDecision;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDetailTiers() {
        return detailTiers;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdLangue() {
        return FWDefaultServletAction.getIdLangueIso(getSession());
    }

    public String getIdLot() {
        return idLot;
    }

    public Long getIdPrestation() {
        return idPrestation;
    }

    public String getIdTierBeneficiaire() {
        return idTierBeneficiaire;
    }

    public Long getIdTierRequerant() {
        return idTierRequerant;
    }

    @Override
    public BISession getISession() {
        return session;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public BigDecimal getMontantInteretsMoratoires() {
        return montantInteretsMoratoires;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public BigDecimal getMontantPrestationsDejaVersees() {
        return montantPrestationsDejaVersees;
    }

    public BigDecimal getMontantPrestationsDues() {
        return montantPrestationsDues;
    }

    public BigDecimal getMontantSolde() {
        return montantSolde;
    }

    @Override
    public String getMsgType() {
        return msgType;
    }

    public BISession getSession() {
        return session;
    }

    public PRTiersWrapper getTiers() {
        return tiers;
    }

    public boolean isDecisionEnAttente() {
        return IREDecision.CS_ETAT_ATTENTE.equals(csEtatDecision);
    }

    public boolean isIdLotValide() {
        return !JadeStringUtil.isBlankOrZero(idLot);
    }

    public boolean isValidationDecisionAuthorisee() {
        return isValidationDecisionAuthorisee;
    }

    public void setCsEtatDecision(String csEtatDecision) {
        this.csEtatDecision = csEtatDecision;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDetailTiers(String detailsTiers) {
        detailTiers = detailsTiers;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdPrestation(Long idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdPrestation(String idPrestation) {
        if (JadeStringUtil.isBlankOrZero(idPrestation)) {
            this.idPrestation = null;
        } else {
            this.idPrestation = Long.parseLong(idPrestation);
        }
    }

    public void setIdTierBeneficiaire(String idTierBeneficiaire) {
        this.idTierBeneficiaire = idTierBeneficiaire;
    }

    public void setIdTierRequerant(Long idTierRequerant) {
        this.idTierRequerant = idTierRequerant;
    }

    public void setIdTierRequerant(String idTierRequerant) {
        if (JadeStringUtil.isBlankOrZero(idTierRequerant)) {
            this.idTierRequerant = null;
        } else {
            this.idTierRequerant = Long.parseLong(idTierRequerant);
        }
    }

    @Override
    public void setISession(BISession newSession) {
        session = newSession;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public void setMontantInteretsMoratoires(BigDecimal montantInteretsMoratoires) {
        this.montantInteretsMoratoires = montantInteretsMoratoires;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public void setMontantPrestationsDejaVersees(BigDecimal montantPrestationsDejaVersees) {
        this.montantPrestationsDejaVersees = montantPrestationsDejaVersees;
    }

    public void setMontantPrestationsDues(BigDecimal montantPrestationsDues) {
        this.montantPrestationsDues = montantPrestationsDues;
    }

    public void setMontantSolde(BigDecimal montantSolde) {
        this.montantSolde = montantSolde;
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setSession(BISession session) {
        this.session = session;
    }

    public void setTiers(PRTiersWrapper tiers) {
        this.tiers = tiers;
    }

    public void setValidationDecisionAuthorisee(boolean isValidationDecisionAuthorisee) {
        this.isValidationDecisionAuthorisee = isValidationDecisionAuthorisee;
    }
}
