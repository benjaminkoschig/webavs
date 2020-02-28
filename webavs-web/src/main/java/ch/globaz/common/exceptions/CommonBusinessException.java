package ch.globaz.common.exceptions;

import globaz.jade.exception.JadeApplicationException;


/**
 * Exception lancée lorsqu'un problème métier intervient.
 */
public class CommonBusinessException extends JadeApplicationException {

    public CommonBusinessException(String message) {
        super(message);
    }
}
