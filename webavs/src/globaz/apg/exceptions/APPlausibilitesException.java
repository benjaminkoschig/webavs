/**
 * 
 */
package globaz.apg.exceptions;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * @author DDE
 */
public class APPlausibilitesException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> errorsList = new ArrayList<String>();

    /**
     * @param m
     */
    public APPlausibilitesException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public APPlausibilitesException(String m, Throwable t) {
        super(m, t);
    }

    /**
     * @return the errorsList
     */
    public List<String> getErrorsList() {
        return errorsList;
    }

    /**
     * @param errorsList
     *            the errorsList to set
     */
    public void setErrorsList(List<String> errorsList) {
        this.errorsList = errorsList;
    }

}
