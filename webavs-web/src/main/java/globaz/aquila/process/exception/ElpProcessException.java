package globaz.aquila.process.exception;

import ch.globaz.jade.process.business.exceptions.JadeProcessException;

/**
 * Exception lancée lors d'un problème dans l'exécution du process d'importation des messages depuis ELP-BOX dans les contentieux.
 */
public class ElpProcessException extends JadeProcessException {

    public ElpProcessException(String s, Exception e) {
        super(s,e);
    }

    public ElpProcessException(String s) {
        super(s);
    }
}
