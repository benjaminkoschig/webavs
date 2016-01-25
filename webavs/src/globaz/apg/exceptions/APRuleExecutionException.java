package globaz.apg.exceptions;

public class APRuleExecutionException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public APRuleExecutionException(String message) {
        super(message);
    }

    public APRuleExecutionException(Exception exception) {
        super(exception);
    }

    public APRuleExecutionException(String message, Exception exception) {
        super(message, exception);
    }
}
