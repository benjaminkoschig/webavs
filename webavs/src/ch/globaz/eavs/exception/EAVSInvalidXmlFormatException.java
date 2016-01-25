package ch.globaz.eavs.exception;

public class EAVSInvalidXmlFormatException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public EAVSInvalidXmlFormatException() {
        super();
    }

    public EAVSInvalidXmlFormatException(String message) {
        super(message);
    }

    public EAVSInvalidXmlFormatException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public EAVSInvalidXmlFormatException(Throwable cause) {
        super(cause);
    }

}
