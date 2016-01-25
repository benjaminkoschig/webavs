package ch.globaz.al.business.exceptions.ctrlexport;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour le contrôle d'exportation des droits
 * 
 * @author GMO
 * 
 */
public class ALCtrlExportException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALCtrlExportException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALCtrlExportException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALCtrlExportException(String m, Throwable t) {
        super(m, t);

    }

}
