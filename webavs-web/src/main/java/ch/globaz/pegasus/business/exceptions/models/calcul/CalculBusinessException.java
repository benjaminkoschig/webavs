/**
 * 
 */
package ch.globaz.pegasus.business.exceptions.models.calcul;

/**
 * @author ECO
 * 
 */
public class CalculBusinessException extends CalculException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CalculBusinessException() {
        super();
    }

    public CalculBusinessException(String m) {
        super(m);
    }

    public CalculBusinessException(String m, String... args) {
        super(m, args);
    }

    public CalculBusinessException(String m, Throwable t) {
        super(m, t);
    }

    public CalculBusinessException(String m, Throwable t, String... args) {
        super(m, t, args);
    }

}
