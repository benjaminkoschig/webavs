package globaz.osiris.eservices.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
        return Stream.of(token).allMatch(Objects::nonNull);
    }
}
