package ch.globaz.musca.business.exceptions.models;

import ch.globaz.musca.business.exceptions.FAException;

/**
 * 
 * Classe <code>Exception</code> métier module facturation
 * 
 */
public class ModuleModelException extends FAException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ModuleModelException() {
        super();

    }

    public ModuleModelException(String m) {
        super(m);

    }

    public ModuleModelException(String m, Throwable t) {
        super(m, t);

    }
}
