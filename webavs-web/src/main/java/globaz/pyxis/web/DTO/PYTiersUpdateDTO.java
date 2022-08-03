package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Data;

import java.util.Vector;
import java.util.stream.Stream;

@Data
public class PYTiersUpdateDTO extends PYTiersDTO {

    private String modificationDate;

    // TODO: Maybe add the reason for modification (this implies also implementing an enum for possible reasons since they're all system codes)
    //private String modificationReason;

    /**
     * Méthode pour valider la présence/absence de champs dans le DTO et appeler la méthode de validation des données.
     *
     * isValidForUpdate vérifie l'absence des champs et lance une erreur en cas de problème.
     *
     * @return false si isPhysicalPerson est null, true si les données du DTO sont bonnes pour un update
     */
    @Override
    @JsonIgnore
    public Boolean isValid() {
        // TODO: Decide how we're doing it for page 2 and other fields

        Vector<String> mandatoryParameters = new Vector<>();
        mandatoryParameters.add(this.getId());
        mandatoryParameters.add(this.getIsPhysicalPerson().toString());
        mandatoryParameters.add(this.getModificationDate());

        return (
            mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty)
            && PYValidateDTO.isValidForUpdate(this)
        );
    }
}
