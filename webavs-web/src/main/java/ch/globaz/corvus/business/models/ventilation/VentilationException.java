/**
 * 
 */
package ch.globaz.corvus.business.models.ventilation;

import ch.globaz.corvus.business.exceptions.CorvusException;

/**
 * @author est
 * 
 */
public class VentilationException extends CorvusException {

    private static final long serialVersionUID = -4722252910362398084L;

    public VentilationException(String m) {
        super(m);
    }

    public VentilationException(String m, Throwable t) {
        super(m, t);
    }
}
