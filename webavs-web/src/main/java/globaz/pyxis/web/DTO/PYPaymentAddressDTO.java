package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Data;

import java.util.Vector;

/**
 * On utilise cette classe comme un struct simplement pour contenir les donn�es
 * de la liste d'adresses de paiement dans le JSON.
 */
@Data
public class PYPaymentAddressDTO {
    private String idPaymentAddress;
    private String idAddressRelatedToPaymentAddress;
    private String domainPaymentAddress;

    private String ccpNumber;
    private String accountNumber;
    private String clearingNumber;
    private String branchOfficePostalCode;
    private String bankCountry;

    private String status;


    /**
     * M�thode pour valider la pr�sence/absence de champs dans le DTO et appeler la m�thode de validation des donn�es.
     *
     * @return true si les donn�es obligatoires d'une adresse sont pr�sentes
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
//                mandatoryParameters.add(paymentAddressDTO.getIdAddress());
            }
        }
        return (mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty));
    }
}
