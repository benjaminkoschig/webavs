package globaz.osiris.eservices.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Data;

import java.util.Objects;
import java.util.stream.Stream;

@Data
public class ESTokenDTO {
    private String token;

    public ESTokenDTO(String token) {
        this.token = token;
    }

    @JsonIgnore
    public Boolean isValid() {
        return Stream.of(token).noneMatch(JadeStringUtil::isEmpty);
    }
}
