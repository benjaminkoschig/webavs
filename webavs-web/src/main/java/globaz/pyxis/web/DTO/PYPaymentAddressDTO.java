package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.pyxis.web.exceptions.PYBadRequestException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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

    private static final String PATTERN_CCP = "\\d{2}\\-\\d{6}\\-\\d{1}";

    private static final Logger logger = LoggerFactory.getLogger(PYValidateDTO.class);


    /**
     * Méthode pour valider la présence/absence de champs dans le DTO et appeler la méthode de validation des données.
     *
     * @return true si les données obligatoires d'une adresse sont présentes
     */
    @JsonIgnore
    public Boolean isValid(PYTiersDTO dto) {
        Map<String, String> mapForValidator = new HashMap<>();

        for (PYPaymentAddressDTO paymentAddressDTO : dto.getPaymentAddress()) {
            if (paymentAddressDTO.getCcpNumber() != null) {
                mapForValidator.put("ccpNumber", paymentAddressDTO.getCcpNumber());
            } else if (paymentAddressDTO.getAccountNumber() != null || paymentAddressDTO.getClearingNumber() != null || this.getBranchOfficePostalCode() != null || paymentAddressDTO.getIdAddressRelatedToPaymentAddress() != null) {
                mapForValidator.put("accountNumber", paymentAddressDTO.getAccountNumber());
                mapForValidator.put("clearingNumber", paymentAddressDTO.getClearingNumber());
                mapForValidator.put("branchOfficePostalCode", paymentAddressDTO.getBranchOfficePostalCode());
            }
            PYValidateDTO.checkIfEmpty(mapForValidator);
        }
        return true;
    }

    @JsonIgnore
    public Boolean isValidForCreation() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idTiers", this.getIdTiers());
        mapForValidator.put("modificationDate", this.getModificationDate());
        mapForValidator.put("idAddressRelatedToPaymentAddress", this.getIdAddressRelatedToPaymentAddress());

        if (this.getAccountNumber() != null || this.getClearingNumber() != null || this.getBranchOfficePostalCode() != null) {
            mapForValidator.put("accountNumber", this.getAccountNumber());
        } else {
            mapForValidator.put("ccpNumber", this.getCcpNumber());
        }

        if (this.getAccountNumber() != null) {
            mapForValidator.put("accountNumber", this.getAccountNumber());
            mapForValidator.put("clearingNumber", this.getClearingNumber());
            mapForValidator.put("branchOfficePostalCode", this.getBranchOfficePostalCode());
        } else if (this.getCcpNumber() != null) {
            mapForValidator.put("ccpNumber", this.getCcpNumber());
            checkCCP(this.getCcpNumber());
        }

        return true;
    }

    @JsonIgnore
    public Boolean isValidForUpdate() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idTiers", this.getIdTiers());
        mapForValidator.put("modificationDate", this.getModificationDate());

        if (this.getAccountNumber() != null) {
            mapForValidator.put("accountNumber", this.getAccountNumber());
            mapForValidator.put("clearingNumber", this.getClearingNumber());
            mapForValidator.put("branchOfficePostalCode", this.getBranchOfficePostalCode());
        } else if (this.getCcpNumber() != null) {
            mapForValidator.put("ccpNumber", this.getCcpNumber());
            checkCCP(this.getCcpNumber());
        }

        PYValidateDTO.checkIfEmpty(mapForValidator);

        return true;
    }

    @JsonIgnore
    public Boolean isValidForDeletion() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idPaymentAddress", this.getIdPaymentAddress());

        PYValidateDTO.checkIfEmpty(mapForValidator);

        return true;
    }

    private static final void checkCCP(String ccp) throws PYBadRequestException {
        if (!Pattern.matches(PATTERN_CCP, ccp)) {
            logger.error("Erreur lors de la validation du CCP. Elle doit être au format xx-xxxxxx-x.");
            throw new PYBadRequestException("Erreur lors de la validation du CCP. Elle doit être au format xx-xxxxxx-x.");
        }
    }
}
