package ch.globaz.naos.exception;

import globaz.jade.exception.JadeApplicationException;

public class MajorationFraisAdminException extends JadeApplicationException {

    /**
     * Exception utilis� pour la gestion de la majoration des frais d'admin
     *
     * @author ESVE | Cr�� le 05 ao�t 2020
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
