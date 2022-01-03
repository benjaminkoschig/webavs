package globaz.ij.helpers.acor;

import globaz.globall.util.JAException;
import lombok.Getter;

/**
 *
 * Une exception lancée lorsque la génération du calcul a posé un problème.
 *
 */
public class IJCalculException extends JAException {

    private static final long serialVersionUID = 1L;
    @Getter
    private String keyMsg = "";

    public IJCalculException() {
        super();
    }
    public IJCalculException(String msg) {
        super(msg);
    }

    public IJCalculException(String msg, String keyMsg) {
        super(msg);
        this.keyMsg = keyMsg;
    }

}
