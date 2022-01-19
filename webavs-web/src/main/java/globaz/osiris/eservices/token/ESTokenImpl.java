package globaz.osiris.eservices.token;

import globaz.prestation.acor.web.ws.Token;
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
