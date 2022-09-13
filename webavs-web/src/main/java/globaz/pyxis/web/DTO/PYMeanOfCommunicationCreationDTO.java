package globaz.pyxis.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Data;

import java.util.Vector;

@Data
public class PYMeanOfCommunicationCreationDTO extends PYMeanOfCommunicationDTO{
    private String idContact;

    @JsonIgnore
    public Boolean isValid() {
        Vector<String> mandatoryParameters = new Vector<>();
        mandatoryParameters.add(this.getMeanOfCommunicationType());
        mandatoryParameters.add(this.getApplicationDomain());
        mandatoryParameters.add(this.getIdContact());

        return mandatoryParameters.stream().noneMatch(JadeStringUtil::isEmpty);
    }
}
