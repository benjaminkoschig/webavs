package globaz.corvus.db.deblocage;

public class REDeblocageException extends RuntimeException {

    public REDeblocageException() {
        super();
    }

    public REDeblocageException(String m) {
        super(m);
    }

    public REDeblocageException(String m, Throwable t) {
        super(m, t);
    }
}