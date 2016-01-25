package globaz.prestation.file.parser.exception;

/**
 * Classe : type_conteneur
 * 
 * Description :
 * 
 * Date de création: 2 mars 04
 * 
 * @author scr
 * 
 */
public class PRLineNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for PRFieldNotFoundException.
     */
    public PRLineNotFoundException() {
        super("Line not found exception !!!");
    }

    /**
     * Constructor for PRFieldNotFoundException.
     * 
     * @param s
     */
    public PRLineNotFoundException(String s) {
        super(s);
    }

}
