package ch.globaz.al.exception;

import globaz.jade.exception.JadeApplicationException;

/**
 * Exception lancée lorsque la rubrique comptable n'est pas renseigné.
 * Actuellement utilisée uniquement dans le cadre des impôts à la source.
 */
public class RubriqueComptableNotFoundException extends JadeApplicationException {

    public RubriqueComptableNotFoundException(String message) {
        super(message);
    }

}
