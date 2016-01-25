package ch.globaz.pegasus.business.exceptions;

/**
 * 
 * @author BSC
 * 
 */
public class NotImplementedException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NotImplementedException() {
        super();
    }

    public NotImplementedException(String m) {
        super(m);
    }

    public NotImplementedException(String m, Throwable t) {
        super(m, t);
    }

}
