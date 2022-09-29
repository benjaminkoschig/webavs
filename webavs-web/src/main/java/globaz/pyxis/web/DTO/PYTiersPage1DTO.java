package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class PYTiersPage1DTO {
    private String id = "";

    // Mandatory fields
    private String surname;
    private String language;

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
    private String modificationDate;
    // TODO: Maybe add the reason for modification (this implies also implementing an enum for possible reasons since they're all system codes)
    //private String modificationReason;

    /**
     * Méthode pour valider la présence/absence de champs dans le DTO et appeler la méthode de validation des données.
     * <p>
     * isValidForCreation vérifie l'absence des champs et lance une erreur en cas de problème.
     *
     * @return false si isPhysicalPerson est null, true si les données du DTO sont bonnes pour une création
     */
    @JsonIgnore
    public Boolean isValidForUpdate() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("id", this.id);
        mapForValidator.put("modificationDate", this.modificationDate);
        mapForValidator.put("isPhysicalPerson", this.isPhysicalPerson.toString());

        PYValidateDTO.checkIfEmpty(mapForValidator);

        return PYValidateDTO.isValidPage1(this);
    }/**
     * Méthode pour valider la présence/absence de champs dans le DTO et appeler la méthode de validation des données.
     * <p>
     * isValidForCreation vérifie l'absence des champs et lance une erreur en cas de problème.
     *
     * @return false si isPhysicalPerson est null, true si les données du DTO sont bonnes pour une création
     */
    @JsonIgnore
    public Boolean isValidForCreation() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("surname", this.getSurname());
        mapForValidator.put("language", this.getLanguage());
        mapForValidator.put("isPhysicalPerson", this.getIsPhysicalPerson().toString());

        PYValidateDTO.checkIfEmpty(mapForValidator);

        if (Boolean.FALSE.equals(this.getIsPhysicalPerson())) {
            return PYValidateDTO.isValidPage1(this);
        } else if (Boolean.TRUE.equals(this.getIsPhysicalPerson())) {
            mapForValidator.put("title", this.getTitle());
            mapForValidator.put("name", this.getName());
            mapForValidator.put("nss", this.getNss());
            mapForValidator.put("birthDate", this.getBirthDate());
            mapForValidator.put("civilStatus", this.getCivilStatus());

            PYValidateDTO.checkIfEmpty(mapForValidator);

            return PYValidateDTO.isValidPage1(this);
        }
        return false;
    }
}
