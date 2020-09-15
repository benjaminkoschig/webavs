package ch.globaz.naos.exception;

import globaz.jade.exception.JadeApplicationException;

public class MajorationFraisAdminException extends JadeApplicationException {

    /**
     * Exception utilisé pour la gestion de la majoration des frais d'admin
     *
     * @author ESVE | Créé le 05 août 2020
     *
     */
    private static final long serialVersionUID = 1L;

    public MajorationFraisAdminException(String message) {
        super(message);
    }

    public MajorationFraisAdminException(String msg, Exception e) {
        super(msg, e);
    }
}
