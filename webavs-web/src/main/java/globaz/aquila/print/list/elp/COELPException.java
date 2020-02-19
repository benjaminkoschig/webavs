package globaz.aquila.print.list.elp;

public class COELPException extends Exception {

    public COELPException() {
        super();
    }

    public COELPException(String message, Exception e) {
        super(message + e.getMessage());
    }

    public COELPException(String message, Throwable cause) {
        super(message, cause);
    }

    public COELPException(String message) {
        super(message);
    }

    public COELPException(Throwable cause) {
        super(cause);
    }
}
