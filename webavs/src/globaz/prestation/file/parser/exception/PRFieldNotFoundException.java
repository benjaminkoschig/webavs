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
public class PRFieldNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for PRFieldNotFoundException.
     */
    public PRFieldNotFoundException() {
        super("Field not found exception!!!");
    }

    /**
     * Constructor for PRFieldNotFoundException.
     * 
     * @param s
     */
    public PRFieldNotFoundException(String s) {
        super("Field not found exception!!! : " + s);
    }

}
