package globaz.prestation.acor.web.ws;

import lombok.Data;

@Data
public class AcorTokenImpl implements Token {
    private String langue;
    private String email;
    private String userId;
}
