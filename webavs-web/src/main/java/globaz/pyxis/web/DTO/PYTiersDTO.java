package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
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

    /**
     * Méthode pour valider la présence/absence de champs dans le DTO dans un premier temps,
     * et appeler la méthode de validation des données dans un second temps
     *
     * isValidForCreation est appelée avant la vérification d'absence des champs car elle permet une gestion des erreurs plus fine.
     * Elle ne fait toutefois rien de plus qui n'aie déjà été fait.
     *
     * @return false si isPhysicalPerson est null, true si les données du DTO sont bonnes pour une création
     */
    @JsonIgnore
    public Boolean isValidCreation(){
        // TODO: Decide how we're doing it for page 2 and other fields

        if (Boolean.FALSE.equals(isPhysicalPerson)) {
            return (
                Stream.of(surname, language, isPhysicalPerson.toString()).noneMatch(JadeStringUtil::isEmpty)
                && PYValidateDTO.isValidForCreation(this)
                // The third test is just a safety net, it doesn't check anything more that hasn't been tested yet
                && Stream.of(nss, birthDate, deathDate, sex, civilStatus, country).allMatch(JadeStringUtil::isEmpty) // This comes after isValidForCreation because that method allows for finner error handling
            );
        } else if (Boolean.TRUE.equals(isPhysicalPerson)){
            return (
                Stream.of(title, surname, name, nss, birthDate, civilStatus, language, isPhysicalPerson.toString()).noneMatch(JadeStringUtil::isEmpty)
                && PYValidateDTO.isValidForCreation(this)
            );
        }
        return false;
    }


    /**
     * Méthode pour valider la présence/absence de champs dans le DTO dans un premier temps,
     * et appeler la méthode de validation des données dans un second temps.
     *
     * isValidForUpdate est appelée avant la vérification d'absence des champs car elle permet une gestion des erreurs plus fine.
     * Elle ne fait toutefois rien de plus qui n'aie déjà été fait.
     *
     * @return false si isPhysicalPerson est null, true si les données du DTO sont bonnes pour un update
     */
    @JsonIgnore
    public Boolean isValidUpdate() {
        // TODO: Decide how we're doing it for page 2 and other fields

        if (Boolean.FALSE.equals(isPhysicalPerson)) {
            return (
                Stream.of(id, isPhysicalPerson.toString()).noneMatch(JadeStringUtil::isEmpty)
                && PYValidateDTO.isValidForUpdate(this)
                // The third test is just a safety net, it doesn't check anything more that hasn't been tested yet
                && Stream.of(nss, birthDate, deathDate, sex, civilStatus, country).allMatch(JadeStringUtil::isEmpty) // This comes after isValidForCreation because that method allows for finner error handling
            );
        } else if (Boolean.TRUE.equals(isPhysicalPerson)){
            return (
                Stream.of(id, isPhysicalPerson.toString()).noneMatch(JadeStringUtil::isEmpty)
                && PYValidateDTO.isValidForUpdate(this)
            );
        }
        return false;
    }
}
