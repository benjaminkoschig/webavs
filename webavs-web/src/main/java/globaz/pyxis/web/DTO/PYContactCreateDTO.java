package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Data;

import java.util.Vector;

/**
 * Il est nécessaire d'avoir l'id du Tiers pour pouvoir lier le contact à son tiers lors de la création
 */
@Data
public class PYContactCreateDTO extends PYContactDTO{
    private String idTiers;

    @JsonIgnore
    public Boolean isValidForCreation() {
        Vector<String> mandatoryParameters = new Vector<>();
        mandatoryParameters.add(this.getIdTiers());
        mandatoryParameters.add(this.getFirstName());
        mandatoryParameters.add(this.getLastName());

        return mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty);
    }

    @JsonIgnore
    public Boolean isValidForDeletion() {
        Vector<String> mandatoryParameters = new Vector<>();
        mandatoryParameters.add(this.getIdTiers());
        mandatoryParameters.add(this.getId());

        return mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty);
    }
}
