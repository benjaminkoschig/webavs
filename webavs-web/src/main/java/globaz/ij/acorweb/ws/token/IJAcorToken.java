package globaz.ij.acorweb.ws.token;

import globaz.prestation.acor.web.ws.AcorToken;
import lombok.Data;

@Data
public class IJAcorToken implements AcorToken {
    private String userId;
    private String langue;
    private String email;
}
