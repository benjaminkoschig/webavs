package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.web.exceptions.PYBadRequestException;
import lombok.Data;

import java.util.stream.Stream;

@Data
public class PYTiersDTO {
    private String id = "";

    // Mandatory fields
    private String surname;
    private String language;
    private String street;
    private String streetNumber;
    private String postalCode;
    private String locality;

    // Physical person's mandatory fields
    private Boolean isPhysicalPerson;
    private String title;
    private String name;

    // Mandatory for a physical person, impossible for a legal person
    private String nss;
    private String birthDate;
    private String civilStatus;

    // Physical person's optional fields (not possible for a legal person)
    private String deathDate;
    private String sex;
    private String maidenName;
    private String nationality;

    // Optional fields
    private String name1;
    private String name2;
    private String taxpayerNumber;
    private Boolean isInactive;
    private String privateMeansOfCommunication;
    private String professionalMeansOfCommunication;
    private String mobileMeansOfCommunication;
    private String faxMeansOfCommunication;
    private String emailMeansOfCommunication;
    private String manner;
    private String ccpNumber;
    private String accountNumber;
    private String status;
    private String clearing;
    private String branchOfficePostalCode;
    private String country;
    private String currency;
    private String meansOfCommunicationType;
    private String value;
    private String applicationDomain;

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


    public PYTiersDTO() {

    }

    @JsonIgnore
    public Boolean isValidCreation(){
        // TODO: Decide how we're doing it for page 2 and other fields
        boolean correctFields = false;
        boolean correctValues = false;

        if (Boolean.FALSE.equals(isPhysicalPerson)) {
            correctFields = Stream.of(surname, language, isPhysicalPerson.toString()).noneMatch(JadeStringUtil::isEmpty)
                && Stream.of(nss, birthDate, deathDate, sex, civilStatus, country).allMatch(JadeStringUtil::isEmpty);
            if (!correctFields) {
                throw new PYBadRequestException("Des champs obligatoires sont manquants ou des champs impossibles pour une personne morale ont été renseignés.");
            }
            correctValues = PYValidateDTO.isValidForCreation(this);
        } else if (Boolean.TRUE.equals(isPhysicalPerson)){
            correctFields = Stream.of(title, surname, name, nss, birthDate, civilStatus, language, isPhysicalPerson.toString()).noneMatch(JadeStringUtil::isEmpty);
            if (!correctFields) {
                throw new PYBadRequestException("Des champs obligatoires d'une personne physique sont manquants.");
            }
            correctValues = PYValidateDTO.isValidForCreation(this);
        }

        return correctFields && correctValues;
    }

    @JsonIgnore
    public Boolean isValidUpdate() {
        // TODO: Decide how we're doing it for page 2 and other fields
        boolean correctFields = false;
        boolean correctValues = false;

        if (Boolean.FALSE.equals(isPhysicalPerson)) {
            correctFields = Stream.of(id, isPhysicalPerson.toString()).noneMatch(JadeStringUtil::isEmpty)
                    && Stream.of(nss, birthDate, deathDate, sex, civilStatus, country).allMatch(JadeStringUtil::isEmpty);
            if (!correctFields) {
                throw new PYBadRequestException("Des champs obligatoires sont manquants ou des champs impossibles pour une personne morale ont été renseignés.");
            }
            correctValues = PYValidateDTO.isValidForUpdate(this);
        } else if (Boolean.TRUE.equals(isPhysicalPerson)){
            correctFields = Stream.of(id, isPhysicalPerson.toString()).noneMatch(JadeStringUtil::isEmpty);
            if (!correctFields) {
                throw new PYBadRequestException("Des champs obligatoires d'une personne physique sont manquants.");
            }
            correctValues = PYValidateDTO.isValidForUpdate(this);
        }

        return correctFields && correctValues;
    }
}
