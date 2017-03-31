package ch.globaz.common.sql;

public class SQLWriterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SQLWriterException(String message) {
        super(message);
    }

    public SQLWriterException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLWriterException(Throwable nestedException) {
        super(nestedException);
    }
}