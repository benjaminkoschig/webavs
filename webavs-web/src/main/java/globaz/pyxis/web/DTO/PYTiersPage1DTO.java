package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Data;

import java.util.Vector;

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
        Vector<String> mandatoryParameters = new Vector<>();
        mandatoryParameters.add(id);
        mandatoryParameters.add(modificationDate);
        mandatoryParameters.add(isPhysicalPerson.toString());

        return (
                mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty)
                && PYValidateDTO.isValidPage1(this)
        );
    }/**
     * Méthode pour valider la présence/absence de champs dans le DTO et appeler la méthode de validation des données.
     * <p>
     * isValidForCreation vérifie l'absence des champs et lance une erreur en cas de problème.
     *
     * @return false si isPhysicalPerson est null, true si les données du DTO sont bonnes pour une création
     */
    @JsonIgnore
    public Boolean isValidForCreation() {
        Vector<String> mandatoryParameters = new Vector<>();
        mandatoryParameters.add(this.getSurname());
        mandatoryParameters.add(this.getLanguage());
        mandatoryParameters.add(this.getIsPhysicalPerson().toString());

        if (Boolean.FALSE.equals(this.getIsPhysicalPerson())) {
            return (
                    mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty)
                            && PYValidateDTO.isValidPage1(this)
            );
        } else if (Boolean.TRUE.equals(this.getIsPhysicalPerson())) {
            mandatoryParameters.add(this.getTitle());
            mandatoryParameters.add(this.getName());
            mandatoryParameters.add(this.getNss());
            mandatoryParameters.add(this.getBirthDate());
            mandatoryParameters.add(this.getCivilStatus());
            return (
                    mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty)
                            && PYValidateDTO.isValidPage1(this)
            );
        }
        return false;
    }
}
