package ch.globaz.al.exception;

import globaz.jade.exception.JadeApplicationException;

/**
 * Exception lancée lorsqu'on ne trouve pas le canton d'imposition forcé.
 */
public class CantonImpositionNotFoundException  extends JadeApplicationException {

    public CantonImpositionNotFoundException(String message) {
        super(message);
    }
}
