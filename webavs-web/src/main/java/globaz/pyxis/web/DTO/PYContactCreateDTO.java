package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Il est nécessaire d'avoir l'id du Tiers pour pouvoir lier le contact à son tiers lors de la création
 */
@Data
public class PYContactCreateDTO extends PYContactDTO{
    private Vector<PYMeanOfCommunicationDTO> meansOfCommunication = new Vector();

    @JsonIgnore
    public Boolean isValidForCreation() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idTiers", this.getIdTiers());
        mapForValidator.put("firstName", this.getFirstName());
        mapForValidator.put("lastName", this.getLastName());

        for (PYMeanOfCommunicationDTO meanOfCommunicationDTO : this.getMeansOfCommunication()) {
            mapForValidator.put("meanOfCommunicationType", meanOfCommunicationDTO.getMeanOfCommunicationType());
            mapForValidator.put("applicationDomain", meanOfCommunicationDTO.getApplicationDomain());
            mapForValidator.put("meanOfCommunicationValue", meanOfCommunicationDTO.getMeanOfCommunicationValue());
        }

        PYValidateDTO.checkIfEmpty(mapForValidator);

        return true;
    }

    @JsonIgnore
    public Boolean isValidForDeletion() {
        Map<String, String> mapForValidator = new HashMap<>();
        mapForValidator.put("idTiers", this.getIdTiers());
        mapForValidator.put("id", this.getId());

        PYValidateDTO.checkIfEmpty(mapForValidator);

        return true;
    }
}
