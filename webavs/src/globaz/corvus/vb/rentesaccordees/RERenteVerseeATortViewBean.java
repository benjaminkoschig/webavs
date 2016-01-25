package globaz.corvus.vb.rentesaccordees;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.decisions.IREDecision;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import java.util.Collection;
import ch.globaz.corvus.utils.rentesverseesatort.REApercuRenteVerseeATort;

public class RERenteVerseeATortViewBean implements FWViewBeanInterface {

    private Collection<REApercuRenteVerseeATort> apercues;
    /** Pour l'affichage des menus */
    private String csEtatDecision;
    /** Pour l'affichage des menus */
    private String csEtatRenteAccordee;
    /** Pour l'affichage des menus */
    private String csTypeBasesCalcul;
    /** Pour l'affichage des menus */
    private String dateFinDroit;
    private boolean decisionSupprimee;
    /** Pour l'affichage des menus */
    private String idBaseCalcul;
    /** Pour l'affichage des menus */
    private String idDecision;
    private Long idDemandeRente;
    /** Pour l'affichage des menus */
    private String idLot;
    /** Pour l'affichage des menus */
    private String idPrestation;
    /** Pour l'affichage des menus */
    private String idRenteAccordee;
    /** Pour l'affichage des menus */
    private String idRenteCalculee;
    /** Pour l'affichage des menus */
    private String idTierBeneficiaire;
    /** Pour l'affichage des menus */
    private String idTierRequerant;
    /** Pour l'affichage des menus */
    private String idTiersBeneficiaire;
    /** Pour l'affichage des menus */
    private String idTiersVueGlobale;
    /** Pour l'affichage des menus */
    private String isPreparationDecisionValide;
    private boolean isValidationDecisionAuthorisee;
    /** Pour l'affichage des menus */
    private String menuOptionToLoad;
    private String message;
    private boolean modificationPossible;
    /** Pour l'affichage des menus */
    private String montantPrestation;
    /** Pour l'affichage des menus */
    private String montantRenteAccordee;
    private String msgType;
    private BSession session;
    private boolean supprimerLaDecisionSiModification;

    public RERenteVerseeATortViewBean() {
        super();

        apercues = null;
        idDemandeRente = null;
        message = null;
        msgType = null;
        session = null;

        decisionSupprimee = false;

        csEtatDecision = "";
        csEtatRenteAccordee = "";
        csTypeBasesCalcul = "";
        dateFinDroit = "";
        idBaseCalcul = "";
        idDecision = "";
        idLot = "";
        idPrestation = "";
        idRenteAccordee = "";
        idRenteCalculee = "";
        idTierBeneficiaire = "";
        idTierRequerant = "";
        idTiersBeneficiaire = "";
        idTiersVueGlobale = "";
        isPreparationDecisionValide = "";
        menuOptionToLoad = "";
        montantPrestation = "";
        montantRenteAccordee = "";
        supprimerLaDecisionSiModification = false;
    }

    public Collection<REApercuRenteVerseeATort> getApercues() {
        return apercues;
    }

    public String getAppColor() {
        return Jade.getInstance().getWebappBackgroundColor();
    }

    public String getCsEtatDecision() {
        return csEtatDecision;
    }

    public String getCsEtatRenteAccordee() {
        return csEtatRenteAccordee;
    }

    public String getCsTypeBasesCalcul() {
        return csTypeBasesCalcul;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getErrorMsgFormatte() {
        return message;
    }

    public String getIdBaseCalcul() {
        return idBaseCalcul;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public Long getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdLangue() {
        return FWDefaultServletAction.getIdLangueIso(getSession());
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdRenteCalculee() {
        return idRenteCalculee;
    }

    public String getIdTierBeneficiaire() {
        return idTierBeneficiaire;
    }

    public String getIdTierRequerant() {
        return idTierRequerant;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getIdTiersVueGlobale() {
        return idTiersVueGlobale;
    }

    @Override
    public BISession getISession() {
        return session;
    }

    public String getIsPreparationDecisionValide() {
        return isPreparationDecisionValide;
    }

    public String getMenuOptionToLoad() {
        return menuOptionToLoad;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public String getMontantRenteAccordee() {
        return montantRenteAccordee;
    }

    @Override
    public String getMsgType() {
        return msgType;
    }

    public BSession getSession() {
        return session;
    }

    public boolean isAfficherMenuRenteAccordee() {
        return "rentesaccordees".equals(menuOptionToLoad);
    }

    public boolean isAfficherPointMenuAnnoncePonctuelle() {
        return (IREPrestationAccordee.CS_ETAT_VALIDE.equals(csEtatRenteAccordee) || IREPrestationAccordee.CS_ETAT_PARTIEL
                .equals(csEtatRenteAccordee)) && JadeStringUtil.isBlankOrZero(dateFinDroit);
    }

    public boolean isDecisionEnAttente() {
        return IREDecision.CS_ETAT_ATTENTE.equals(csEtatDecision);
    }

    public boolean isDecisionSupprimee() {
        return decisionSupprimee;
    }

    public boolean isDiminutionRenteAccordeeAutorisee() {
        return (IREPrestationAccordee.CS_ETAT_AJOURNE.equals(csEtatRenteAccordee)
                || IREPrestationAccordee.CS_ETAT_CALCULE.equals(csEtatRenteAccordee) || IREPrestationAccordee.CS_ETAT_DIMINUE
                    .equals(csEtatRenteAccordee))
                || (!JadeStringUtil.isBlankOrZero(dateFinDroit))
                || !isValidationDecisionAuthorisee;
    }

    public boolean isIdLotValide() {
        return (idLot != null) && JadeStringUtil.isBlankOrZero(idLot.toString());
    }

    public boolean isModificationPossible() {
        return modificationPossible;
    }

    public boolean isPreparationDecisionValide() {
        return "true".equals(isPreparationDecisionValide)
                && !IREPrestationAccordee.CS_ETAT_VALIDE.equals(csEtatRenteAccordee);
    }

    public boolean isSupprimerLaDecisionSiModification() {
        return supprimerLaDecisionSiModification;
    }

    public boolean isValidationDecisionAuthorisee() {
        return isValidationDecisionAuthorisee;
    }

    public void setApercues(Collection<REApercuRenteVerseeATort> apercues) {
        this.apercues = apercues;
    }

    public void setCsEtatDecision(String csEtatDecision) {
        this.csEtatDecision = csEtatDecision;
    }

    public void setCsEtatRenteAccordee(String csEtatRenteAccordee) {
        this.csEtatRenteAccordee = csEtatRenteAccordee;
    }

    public void setCsTypeBasesCalcul(String csTypeBasesCalcul) {
        this.csTypeBasesCalcul = csTypeBasesCalcul;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setDecisionSupprimee(boolean decisionSupprimee) {
        this.decisionSupprimee = decisionSupprimee;
    }

    public void setIdBaseCalcul(String idBaseCalcul) {
        this.idBaseCalcul = idBaseCalcul;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDemandeRente(Long idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIdRenteCalculee(String idRenteCalculee) {
        this.idRenteCalculee = idRenteCalculee;
    }

    public void setIdTierBeneficiaire(String idTierBeneficiaire) {
        this.idTierBeneficiaire = idTierBeneficiaire;
    }

    public void setIdTierRequerant(String idTierRequerant) {
        this.idTierRequerant = idTierRequerant;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setIdTiersVueGlobale(String idTiersVueGlobale) {
        this.idTiersVueGlobale = idTiersVueGlobale;
    }

    @Override
    public void setISession(BISession newSession) {
        setSession((BSession) newSession);
    }

    public void setIsPreparationDecisionValide(String isPreparationDecisionValide) {
        this.isPreparationDecisionValide = isPreparationDecisionValide;
    }

    public void setMenuOptionToLoad(String menuOptionToLoad) {
        this.menuOptionToLoad = menuOptionToLoad;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public void setModificationPossible(boolean modificationPossible) {
        this.modificationPossible = modificationPossible;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public void setMontantRenteAccordee(String montantRenteAccordee) {
        this.montantRenteAccordee = montantRenteAccordee;
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setSupprimerLaDecisionSiModification(boolean supprimerLaDecisionSiModification) {
        this.supprimerLaDecisionSiModification = supprimerLaDecisionSiModification;
    }

    public void setValidationDecisionAuthorisee(boolean isValidationDecisionAuthorisee) {
        this.isValidationDecisionAuthorisee = isValidationDecisionAuthorisee;
    }
}
