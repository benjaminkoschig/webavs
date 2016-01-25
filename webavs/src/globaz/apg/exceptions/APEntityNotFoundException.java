package globaz.apg.exceptions;

public class APEntityNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public APEntityNotFoundException(Class<?> entityClass, String entityId) {
        super(entityClass == null ? "No entity from type : [unknown] with id [" + entityId + "] founded."
                : "No entity from type : [" + entityClass + "] with id [" + entityId + "] founded.");
    }

    public APEntityNotFoundException(Class<?> entityClass, String entityId, Exception parentException) {
        super(entityClass == null ? "No entity from type : [unknown] with id [" + entityId + "] founded."
                : "No entity from type : [" + entityClass + "] with id [" + entityId + "] founded.", parentException);
    }
}
