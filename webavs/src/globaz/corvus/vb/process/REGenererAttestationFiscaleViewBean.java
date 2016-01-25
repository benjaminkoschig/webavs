package globaz.corvus.vb.process;

import globaz.commons.nss.NSUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

public class REGenererAttestationFiscaleViewBean extends PRAbstractViewBeanSupport {

    private String anneeAttestations = "";
    private String dateImpressionAttJJMMAAA = "";
    private String dateImpressionAttMMAAA = "";
    private String displaySendToGed = "0";
    private String eMailAddress = "";
    private Boolean isDepuisDemande = Boolean.FALSE;
    private Boolean isGenerationMultiple = Boolean.FALSE;
    private Boolean isSendToGed = Boolean.FALSE;
    private String NSS = "";
    private String nssA = "";
    private String nssDe = "";

    public String getAnneeAttestations() {
        return anneeAttestations;
    }

    public String getDateImpressionAttJJMMAAA() {
        return dateImpressionAttJJMMAAA;
    }

    public String getDateImpressionAttMMAAA() {
        return dateImpressionAttMMAAA;
    }

    public String getDisplaySendToGed() {
        return displaySendToGed;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public Boolean getIsDepuisDemande() {
        return isDepuisDemande;
    }

    public Boolean getIsGenerationMultiple() {
        return isGenerationMultiple;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    public String getNSS() {
        return NSS;
    }

    public String getNssA() {
        return nssA;
    }

    public String getNssDe() {
        return nssDe;
    }

    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     * 
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNSS(), isNNSS().equals("true") ? true : false);
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getNSS())) {
            return "";
        }

        if (getNSS().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    public void setAnneeAttestations(String anneeAttestations) {
        this.anneeAttestations = anneeAttestations;
    }

    public void setDateImpressionAttJJMMAAA(String dateImpressionAttJJMMAAA) {
        this.dateImpressionAttJJMMAAA = dateImpressionAttJJMMAAA;
    }

    public void setDateImpressionAttMMAAA(String dateImpressionAttMMAAA) {
        this.dateImpressionAttMMAAA = dateImpressionAttMMAAA;
    }

    public void setDisplaySendToGed(String displaySendToGed) {
        this.displaySendToGed = displaySendToGed;
    }

    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    public void setIsDepuisDemande(Boolean isDepuisDemande) {
        this.isDepuisDemande = isDepuisDemande;
    }

    public void setIsGenerationMultiple(Boolean isGenerationMultiple) {
        this.isGenerationMultiple = isGenerationMultiple;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setNSS(String nSS) {
        NSS = nSS;
    }

    public void setNssA(String nssA) {
        this.nssA = nssA;
    }

    public void setNssDe(String nssDe) {
        this.nssDe = nssDe;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
