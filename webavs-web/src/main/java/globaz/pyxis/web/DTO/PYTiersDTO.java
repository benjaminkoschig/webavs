package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Data;

import java.util.stream.Stream;

@Data
public class PYTiersDTO {
    private String id = "";

    // Mandatory fields
    private String name;
    private String language;
    private String street;
    private String streetNumber;
    private String postalCode;
    private String locality;

    // Physical person's mandatory fields
    private boolean isPhysicalPerson = false;
    private String title = "";
    private String surname = "";

    // Mandatory for a physical person, impossible for a legal person
    private String nss = "";
    private String birthDate = "";
    private String civilStatus = "";

    // Physical person's optional fields (not possible for a legal person)
    private String deathDate = "";
    private String sex = "";
    private String maidenName = "";
    private String nationality = "";

    // Optional fields
    private String name1 = "";
    private String name2 = "";
    private String taxpayerNumber = "";
    private Boolean isInactive = null;
    private String privateMeansOfCommunication = "";
    private String professionalMeansOfCommunication = "";
    private String mobileMeansOfCommunication = "";
    private String faxMeansOfCommunication = "";
    private String emailMeansOfCommunication = "";
    private String manner = "";
    private String ccpNumber = "";
    private String accountNumber = "";
    private String status = "";
    private String clearing = "";
    private String branchOfficePostalCode = "";
    private String country = "";
    private String currency = "";
    private String meansOfCommunicationType = "";
    private String value = "";
    private String applicationDomain = "";

    public PYTiersDTO() {

    }

    @JsonIgnore
    public Boolean isValid() {
        //TODO: add validation for other fields
        return (Stream.of(name, language, street, streetNumber, postalCode, locality).noneMatch(JadeStringUtil::isEmpty));
    }
}
