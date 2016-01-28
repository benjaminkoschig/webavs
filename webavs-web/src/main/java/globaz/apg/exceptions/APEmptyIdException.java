package globaz.apg.exceptions;

public class APEmptyIdException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public APEmptyIdException(Class<?> entityClass, String message) {
        super(message + (entityClass == null ? "" : "Entity class : " + entityClass));
    }
}
