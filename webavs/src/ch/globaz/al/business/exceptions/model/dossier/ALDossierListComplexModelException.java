/**
 * 
 */
package ch.globaz.al.business.exceptions.model.dossier;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour les dossierList liée aux dossiers
 * 
 * @author PTA
 * 
 */
public class ALDossierListComplexModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDossierListComplexModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDossierListComplexModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDossierListComplexModelException(String m, Throwable t) {
        super(m, t);
    }
}
