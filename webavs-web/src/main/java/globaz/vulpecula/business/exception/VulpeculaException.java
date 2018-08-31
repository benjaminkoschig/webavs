package globaz.vulpecula.business.exception;

import globaz.jade.exception.JadeApplicationException;

public class VulpeculaException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public VulpeculaException() {
    }

    /**
     * @param m
     */
    public VulpeculaException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public VulpeculaException(String m, Throwable t) {
        super(m, t);
    }
}
