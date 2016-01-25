package globaz.osiris.file.paiement.exception;

/**
 * Classe : type_conteneur Description : Date de création: 2 mars 04
 * 
 * @author scr
 */
public class FieldNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for FieldNotFoundException.
     */
    public FieldNotFoundException() {
        super("Field not found exception!!!");
    }

    /**
     * Constructor for FieldNotFoundException.
     * 
     * @param s
     */
    public FieldNotFoundException(String s) {
        super(s);
    }

}
