package globaz.apg.exceptions;

public class APToManyEntityFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public APToManyEntityFoundException(Class<?> entityClass, String entityId) {
        super(entityClass == null ? "To many entities from type : [unknown] with id [" + entityId + "] founded."
                : "To many entities from type : [" + entityClass.getClass().getName() + "] with id [" + entityId
                        + "] founded.");
    }
}
