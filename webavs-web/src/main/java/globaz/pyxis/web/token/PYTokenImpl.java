package globaz.pyxis.web.token;

import ch.globaz.common.ws.token.Token;
import lombok.Data;

@Data
public class PYTokenImpl implements Token {
    private String dateDemande;
    private String timeDemande;

    private String langue;
    private String email;
    private String userId;
}
