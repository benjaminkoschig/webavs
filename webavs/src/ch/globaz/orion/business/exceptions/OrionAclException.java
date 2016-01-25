package ch.globaz.orion.business.exceptions;

public class OrionAclException extends OrionException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public OrionAclException() {
        super();
    }

    public OrionAclException(String m) {
        super(m);
    }

    public OrionAclException(String m, Throwable t) {
        super(m, t);
    }

}
