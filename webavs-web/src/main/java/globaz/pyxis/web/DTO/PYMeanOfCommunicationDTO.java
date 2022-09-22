package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Data;

import java.util.Vector;

/**
 * On utilise cette classe comme un struct simplement pour contenir les données
 * de la liste des moyens de communication d'un contact dans le JSON
 */
@Data
public class PYMeanOfCommunicationDTO {
    private String idContact;
    private String meanOfCommunicationType;
    private String meanOfCommunicationValue;
    private String applicationDomain;

    @JsonIgnore
    public Boolean isValid() {
        Vector<String> mandatoryParameters = new Vector<>();

        if (this.getMeanOfCommunicationType() != null || this.getApplicationDomain() != null || this.getIdContact() != null) {
            mandatoryParameters.add(this.getMeanOfCommunicationType());
            mandatoryParameters.add(this.getApplicationDomain());
            mandatoryParameters.add(this.getIdContact());
        }

        return mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty);
    }
}
