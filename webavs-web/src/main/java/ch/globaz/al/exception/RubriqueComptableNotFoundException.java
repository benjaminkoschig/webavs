package ch.globaz.al.exception;

import globaz.jade.exception.JadeApplicationException;

/**
 * Exception lanc�e lorsque la rubrique comptable n'est pas renseign�.
 * Actuellement utilis�e uniquement dans le cadre des imp�ts � la source.
 */
public class RubriqueComptableNotFoundException extends JadeApplicationException {

    public RubriqueComptableNotFoundException(String message) {
        super(message);
    }

}
