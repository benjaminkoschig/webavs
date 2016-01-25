package ch.globaz.al.business.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * Classe <code>Exception</code> mère de toutes les exceptions générées par le métier de l'application des allocations
 * familiales
 * 
 * @author jts
 */
public abstract class ALException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with null as its detail message. The cause is not initialized, and may subsequently be
     * initialized by a call to Throwable.initCause(java.lang.Throwable).
     */
    public ALException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently
     * be initialized by a call to Throwable.initCause(java.lang.Throwable).
     * 
     * @param m
     *            the detail message. The detail message is saved for later retrieval by the Throwable.getMessage()
     *            method.
     */
    public ALException(String m) {
        super(m);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * 
     * Note that the detail message associated with cause is not automatically incorporated in this exception's detail
     * message.
     * 
     * @param m
     *            the detail message. The detail message is saved for later retrieval by the Throwable.getMessage()
     *            method.
     * @param t
     *            the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is
     *            permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ALException(String m, Throwable t) {
        super(m, t);
    }

}
