package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Data;

import java.util.Vector;

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
        Vector<String> mandatoryParameters = new Vector<>();
        if (this.getId() != null)
            mandatoryParameters.add(this.getId());

        return mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty);
    }
}
