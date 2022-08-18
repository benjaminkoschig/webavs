package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Data;

import java.util.Vector;

@Data
public class PYTiersDTO {

    private String id = "";

    // Mandatory fields
    private String surname;
    private String language;
    private String typeAddress;
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
    private String manner;
    private String ccpNumber;
    private String accountNumber;
    private String status;
    private String clearingNumber;
    private String branchOfficePostalCode;
    private String country;
    private String bankCountry;

    private Vector<PYContactDTO> contacts = new Vector();

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
     * Méthode pour valider la présence/absence de champs dans le DTO et appeler la méthode de validation des données.
     * <p>
     * isValidForCreation vérifie l'absence des champs et lance une erreur en cas de problème.
     *
     * @return false si isPhysicalPerson est null, true si les données du DTO sont bonnes pour une création
     */
    @JsonIgnore
    public Boolean isValid() {
        // TODO: Decide how we're doing it for page 2 and other fields

        Vector<String> mandatoryParameters = new Vector<>();
        mandatoryParameters.add(surname);
        mandatoryParameters.add(language);
        mandatoryParameters.add(isPhysicalPerson.toString());

        if (street != null || streetNumber != null || postalCode != null || locality != null) {
            mandatoryParameters.add(street);
            mandatoryParameters.add(streetNumber);
            mandatoryParameters.add(postalCode);
            mandatoryParameters.add(locality);
        }

        if (Boolean.FALSE.equals(isPhysicalPerson)) {
            return (
                    mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty)
                            && PYValidateDTO.isValidForCreation(this)
            );
        } else if (Boolean.TRUE.equals(isPhysicalPerson)) {
            mandatoryParameters.add(title);
            mandatoryParameters.add(name);
            mandatoryParameters.add(nss);
            mandatoryParameters.add(birthDate);
            mandatoryParameters.add(civilStatus);
            return (
                    mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty)
                            && PYValidateDTO.isValidForCreation(this)
            );
        }
        return false;
    }
}