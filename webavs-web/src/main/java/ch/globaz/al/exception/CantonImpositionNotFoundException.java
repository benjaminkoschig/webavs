package ch.globaz.al.exception;

import globaz.jade.exception.JadeApplicationException;

/**
 * Exception lanc�e lorsqu'on ne trouve pas le canton d'imposition forc�.
 */
public class CantonImpositionNotFoundException  extends JadeApplicationException {

    public CantonImpositionNotFoundException(String message) {
        super(message);
    }
}
