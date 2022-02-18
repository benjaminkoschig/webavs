package globaz.osiris.eservices.token;

import ch.globaz.common.ws.token.Token;
import lombok.Data;

@Data
public class ESTokenImpl implements Token {
    private String dateDemande;
    private String timeDemande;
    private String timeStampGedo;

    private String langue;
    private String email;
    private String userId;
}
