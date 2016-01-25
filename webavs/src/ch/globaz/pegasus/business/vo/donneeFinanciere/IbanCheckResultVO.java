package ch.globaz.pegasus.business.vo.donneeFinanciere;

/**
 * VO contenant les resultats de la validation d'un IBAN
 * 
 * @author bsc
 * 
 */
public class IbanCheckResultVO {
    /**
     * la description de l'etablissement bancaire lie a un iban valide
     */
    private String bankDescription = "";
    /**
     * Contient la version formatee de l'Iban si celui si isCheckable vaut TRUE
     */
    private String formattedIban = "";

    /**
     * les iban ne sont verifie que s'ils commencent pas CH
     */
    private Boolean isCheckable = Boolean.FALSE;

    /**
     * l'iban donne est-il un iban ch valide
     */
    private Boolean isValidChIban = Boolean.FALSE;

    public String getBankDescription() {
        return bankDescription;
    }

    public String getFormattedIban() {
        return formattedIban;
    }

    public Boolean getIsCheckable() {
        return isCheckable;
    }

    public Boolean getIsValidChIban() {
        return isValidChIban;
    }

    public void setBankDescription(String bankDescription) {
        this.bankDescription = bankDescription;
    }

    public void setFormattedIban(String formattedIban) {
        this.formattedIban = formattedIban;
    }

    public void setIsCheckable(Boolean isCheckable) {
        this.isCheckable = isCheckable;
    }

    public void setIsValidChIban(Boolean isValidChIban) {
        this.isValidChIban = isValidChIban;
    }

}
