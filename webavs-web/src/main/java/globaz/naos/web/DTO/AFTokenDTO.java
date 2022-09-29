package globaz.naos.web.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Data;

import java.util.stream.Stream;

@Data
public class AFTokenDTO {
    private String token;

    public AFTokenDTO(String token) {
        this.token = token;
    }

    @JsonIgnore
    public Boolean isValid() {
        return Stream.of(token).noneMatch(JadeStringUtil::isEmpty);
    }
}
