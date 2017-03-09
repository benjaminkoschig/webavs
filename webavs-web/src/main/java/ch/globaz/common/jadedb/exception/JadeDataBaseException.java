package ch.globaz.common.jadedb.exception;

public class JadeDataBaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JadeDataBaseException() {
        super();
    }

    public JadeDataBaseException(String message) {
        super(message);

    }

    public JadeDataBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public JadeDataBaseException(Throwable cause) {
        super(cause);
    }
}
