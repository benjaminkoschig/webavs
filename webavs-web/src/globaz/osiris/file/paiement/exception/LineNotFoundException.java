package globaz.osiris.file.paiement.exception;

/**
 * Classe : type_conteneur Description : Date de création: 2 mars 04
 * 
 * @author scr
 */
public class LineNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for FieldNotFoundException.
     */
    public LineNotFoundException() {
        super("Line not found exception !!!");
    }

    /**
     * Constructor for FieldNotFoundException.
     * 
     * @param s
     */
    public LineNotFoundException(String s) {
        super(s);
    }

}
