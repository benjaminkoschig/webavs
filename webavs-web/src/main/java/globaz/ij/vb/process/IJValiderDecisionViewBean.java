/*
 * Créé le 17 nov. 06
 */
package globaz.ij.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public class IJValiderDecisionViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csEtatMiseEnGed = "";
    private String csEtatSEDEX = "";
    private String dateDecision = "";

    private String dateEnvoiSedex = "";
    private String dateMiseEnGed = "";
    private String dateSurDocument = "";
    private String eMailAddress = "";
    private String idDecision = "";
    private String idPrononce = "";

    private Boolean isDecisionValidee = null;
    private Boolean isEnvoyerGED = null;
    private Boolean isEnvoyerSEDEX = null;

    private Boolean isValiderDecision = null;

    public String getCodeLibelleEtatMiseEnGed() {
        return getSession().getCodeLibelle(csEtatMiseEnGed);
    }

    public String getCodeLibelleEtatSEDEX() {
        return getSession().getCodeLibelle(csEtatSEDEX);
    }

    public String getCsEtatMiseEnGed() {
        return csEtatMiseEnGed;
    }

    public String getCsEtatSEDEX() {
        return csEtatSEDEX;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateEnvoiSedex() {
        return dateEnvoiSedex;
    }

    public String getDateMiseEnGed() {
        return dateMiseEnGed;
    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdPrononce() {
        return idPrononce;
    }

    public Boolean getIsDecisionValidee() {
        return isDecisionValidee;
    }

    public Boolean getIsEnvoyerGED() {
        return isEnvoyerGED;
    }

    public Boolean getIsEnvoyerSEDEX() {
        return isEnvoyerSEDEX;
    }

    public Boolean getIsValiderDecision() {
        return isValiderDecision;
    }

    public void setCsEtatMiseEnGed(String csEtatMiseEnGed) {
        this.csEtatMiseEnGed = csEtatMiseEnGed;
    }

    public void setCsEtatSEDEX(String csEtatSEDEX) {
        this.csEtatSEDEX = csEtatSEDEX;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDateEnvoiSedex(String dateEnvoiSedex) {
        this.dateEnvoiSedex = dateEnvoiSedex;
    }

    public void setDateMiseEnGed(String dateMiseEnGed) {
        this.dateMiseEnGed = dateMiseEnGed;
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdPrononce(String idPrononce) {
        this.idPrononce = idPrononce;
    }

    public void setIsDecisionValidee(Boolean isDecisionValidee) {
        this.isDecisionValidee = isDecisionValidee;
    }

    public void setIsEnvoyerGED(Boolean isEnboyerGED) {
        isEnvoyerGED = isEnboyerGED;
    }

    public void setIsEnvoyerSEDEX(Boolean isEnvoyerSEDEX) {
        this.isEnvoyerSEDEX = isEnvoyerSEDEX;
    }

    public void setIsValiderDecision(Boolean isValiderDecision) {
        this.isValiderDecision = isValiderDecision;
    }

    @Override
    public boolean validate() {
        return true;
    }

}
