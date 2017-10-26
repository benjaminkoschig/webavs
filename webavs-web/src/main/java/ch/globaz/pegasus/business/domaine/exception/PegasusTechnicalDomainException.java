package ch.globaz.pegasus.business.domaine.exception;

public class PegasusTechnicalDomainException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PegasusTechnicalDomainException(String msg, Throwable t) {
        super(msg, t);
    }

    public PegasusTechnicalDomainException(String msg) {
        super(msg);
    }
}
