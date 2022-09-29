package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * On utilise cette classe comme un struct simplement pour contenir les données
 * de la liste de contacts dans le JSON
 */
@Data
public class PYContactDTO {
    private String id;
    private String idTiers;
    private String firstName;
    private String lastName;

    @JsonIgnore
    public Boolean isValid() {
        Map<String, String> mapForValidator = new HashMap<>();
        if (this.getId() != null)
            mapForValidator.put("id", this.getId());

        PYValidateDTO.checkIfEmpty(mapForValidator);

        return true;
    }
}
