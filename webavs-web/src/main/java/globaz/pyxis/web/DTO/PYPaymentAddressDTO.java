package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Data;

import java.util.Vector;

/**
 * On utilise cette classe comme un struct simplement pour contenir les données
 * de la liste d'adresses de paiement dans le JSON.
 */
@Data
public class PYPaymentAddressDTO {
    private String idTiers;
    private String modificationDate;
    private String idPaymentAddress;
    private String idAddressRelatedToPaymentAddress;
    private String domainPaymentAddress;

    private String ccpNumber;
    private String accountNumber;
    private String clearingNumber;
    private String branchOfficePostalCode;
    private String bankCountry;


    /**
     * Méthode pour valider la présence/absence de champs dans le DTO et appeler la méthode de validation des données.
     *
     * @return true si les données obligatoires d'une adresse sont présentes
     */
    @JsonIgnore
    public Boolean isValid(PYTiersDTO dto) {
        Vector<String> mandatoryParameters = new Vector<>();

        for (PYPaymentAddressDTO paymentAddressDTO : dto.getPaymentAddress()) {
            if (paymentAddressDTO.getCcpNumber() != null) {
                mandatoryParameters.add(paymentAddressDTO.getCcpNumber());
            } else if (paymentAddressDTO.getAccountNumber() != null || paymentAddressDTO.getClearingNumber() != null || paymentAddressDTO.getIdAddressRelatedToPaymentAddress() != null) {
                mandatoryParameters.add(paymentAddressDTO.getAccountNumber());
                mandatoryParameters.add(paymentAddressDTO.getClearingNumber());
                mandatoryParameters.add(paymentAddressDTO.getBranchOfficePostalCode());
            }
        }
        return (mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty));
    }

    @JsonIgnore
    public Boolean isValidForCreation() {
        Vector<String> mandatoryParameters = new Vector<>();
        mandatoryParameters.add(this.getIdTiers());
        mandatoryParameters.add(this.getModificationDate());
        if (this.getAccountNumber() != null) {
            mandatoryParameters.add(this.getAccountNumber());
            mandatoryParameters.add(this.getClearingNumber());
            mandatoryParameters.add(this.getBranchOfficePostalCode());
        } else if (this.getCcpNumber() != null)
            mandatoryParameters.add(this.getCcpNumber());

        return mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty);
    }

    @JsonIgnore
    public Boolean isValidForUpdate() {
        Vector<String> mandatoryParameters = new Vector<>();
        mandatoryParameters.add(this.getIdTiers());
        mandatoryParameters.add(this.getModificationDate());
        if (this.getAccountNumber() != null) {
            mandatoryParameters.add(this.getAccountNumber());
            mandatoryParameters.add(this.getClearingNumber());
            mandatoryParameters.add(this.getBranchOfficePostalCode());
        } else if (this.getCcpNumber() != null)
            mandatoryParameters.add(this.getCcpNumber());

        return mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty);
    }

    @JsonIgnore
    public Boolean isValidForDeletion() {
        Vector<String> mandatoryParameters = new Vector<>();
        mandatoryParameters.add(this.getIdPaymentAddress());

        return mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty);
    }
}
