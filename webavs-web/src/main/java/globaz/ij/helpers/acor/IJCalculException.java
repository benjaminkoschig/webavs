package globaz.ij.helpers.acor;

import globaz.globall.util.JAException;
import lombok.Getter;

/**
 *
 * Une exception lanc�e lorsque la g�n�ration du calcul a pos� un probl�me.
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
