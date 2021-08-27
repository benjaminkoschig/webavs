package globaz.ij.acor2020.ws.token;

import globaz.prestation.acor.acor2020.ws.Acor2020Token;
import lombok.Data;

@Data
public class IJAcor2020Token implements Acor2020Token {
    private String userId;
    private String langue;
    private String email;
}
