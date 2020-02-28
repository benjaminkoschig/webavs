package ch.globaz.common.exceptions;

import globaz.jade.exception.JadeApplicationException;


/**
 * Exception lanc�e lorsqu'un probl�me m�tier intervient.
 */
public class CommonBusinessException extends JadeApplicationException {

    public CommonBusinessException(String message) {
        super(message);
    }
}
