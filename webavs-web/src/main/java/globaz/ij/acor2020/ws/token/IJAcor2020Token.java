package globaz.ij.acor2020.ws.token;

import ch.globaz.common.acor.Acor2020Token;
import lombok.Data;

@Data
public class IJAcor2020Token implements Acor2020Token {
    private String userId;
    private String langue;
}
