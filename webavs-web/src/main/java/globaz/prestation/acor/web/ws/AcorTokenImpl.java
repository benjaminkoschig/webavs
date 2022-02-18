package globaz.prestation.acor.web.ws;

import ch.globaz.common.ws.token.Token;
import lombok.Data;

@Data
public class AcorTokenImpl implements Token {
    private String langue;
    private String email;
    private String userId;
}
