package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Data;

import java.util.Vector;

/**
 * DTO utilis� pour la cr�ation et l'update avec toutes les donn�es d'un tiers (y.c. adresses, adresses de paiement, contacts...).
 * <p>
 * Il est pr�f�rable d'utiliser les updates "atomiques" pour mettre � jour les tables du tiers une � une, au lieu de toutes � la fois avec cette classe.
 *
 * @see PYTiersPage1DTO
 * @see PYAddressDTO
 * @see PYPaymentAddressDTO
 * @see PYContactDTO
 * @see PYMeanOfCommunicationDTO
 */
@Data
public class PYTiersDTO extends PYTiersPage1DTO {
    private Vector<PYContactCreateDTO> contacts = new Vector();
    private Vector<PYAddressDTO> addresses = new Vector();
    private Vector<PYPaymentAddressDTO> paymentAddress = new Vector<>();
    // CCVS-only fields
    // Optional fields
    private String tiersName;
    private String tiersSurname;

    // Mandatory for a physical person, optional for a legal person
    private String relationshipWithTiers;
    private String relationshipType;
    private String validFrom;
    private String validTo;

    // Mandatory for a physical person, impossible for a legal person
    private String partnerNSS;
    private String partnershipCivilStatus;

    // Optional for a physical person, impossible for a legal person
    private String partnershipFrom;
    private String partnershipTo;

    /**
     * M�thode pour valider la pr�sence/absence de champs dans le DTO et appeler la m�thode de validation des donn�es.
     * <p>
     * isValidForCreation v�rifie l'absence des champs et lance une erreur en cas de probl�me.
     *
     * @return false si isPhysicalPerson est null, true si les donn�es du DTO sont bonnes pour une cr�ation
     */
    @Override
    @JsonIgnore
    public Boolean isValidForCreation() {
        Vector<String> mandatoryParameters = new Vector<>();
        mandatoryParameters.add(this.getSurname());
        mandatoryParameters.add(this.getLanguage());
        mandatoryParameters.add(this.getIsPhysicalPerson().toString());

        PYAddressDTO pyAddressDTO = new PYAddressDTO();
        PYPaymentAddressDTO pyPaymentAddressDTO = new PYPaymentAddressDTO();

        Boolean isValidContact = true, isValidMeansOfCommunication = true;
        contacts = this.getContacts();
        for (PYContactCreateDTO contactCreateDTO : contacts) {
            isValidContact = contactCreateDTO.isValid();
            Vector<PYMeanOfCommunicationDTO> meanOfCommunicationDTOS = contactCreateDTO.getMeansOfCommunication();
            for (PYMeanOfCommunicationDTO meanOfCommunicationDTO : meanOfCommunicationDTOS) {
                isValidMeansOfCommunication = meanOfCommunicationDTO.isValid();
            }
        }

        if (Boolean.FALSE.equals(this.getIsPhysicalPerson())) {
            return (
                    mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty)
                            && pyAddressDTO.isValid(this)
                            && pyPaymentAddressDTO.isValid(this)
                            && isValidContact
                            && isValidMeansOfCommunication
                            && PYValidateDTO.isValidForCreation(this)
            );
        } else if (Boolean.TRUE.equals(this.getIsPhysicalPerson())) {
            mandatoryParameters.add(this.getTitle());
            mandatoryParameters.add(this.getName());
            mandatoryParameters.add(this.getNss());
            mandatoryParameters.add(this.getBirthDate());
            mandatoryParameters.add(this.getCivilStatus());
            return (
                    mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty)
                            && pyAddressDTO.isValid(this)
                            && pyPaymentAddressDTO.isValid(this)
                            && isValidContact
                            && isValidMeansOfCommunication
                            && PYValidateDTO.isValidForCreation(this)
            );
        }
        return false;
    }
}