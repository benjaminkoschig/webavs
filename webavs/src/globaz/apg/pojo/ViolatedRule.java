/**
 * 
 */
package globaz.apg.pojo;

/**
 * @author dde
 */
public class ViolatedRule {

    /**
     * Si la règle est quittançable par une breakRule
     */
    private Boolean breakable;
    /**
     * Le code de la règle
     */
    private String errorCode;
    /**
     * Une description des tests effectué par la règle
     */
    private String errorMessage;

    private boolean fatalErrorThrown;

    /**
     * @param errorCode
     *            Le code de la règle
     * @param errorMessage
     *            Une description des tests effectué par la règle
     * @param breakable
     *            Si la règle est 'quittançable' par une breakRule
     */
    public ViolatedRule(String errorCode, String errorMessage, Boolean breakable) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.breakable = breakable;
    }

    /**
     * Renseigne si la la règle en erreur peut être quittancée par une breakRule
     * 
     * @return the breakable
     */
    public Boolean getBreakable() {
        return breakable;
    }

    /**
     * @return the errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    public final boolean getFatalErrorThrown() {
        return fatalErrorThrown;
    }

    public final void setFatalErrorThrown(boolean fatalErrorThrown) {
        this.fatalErrorThrown = fatalErrorThrown;
    }

}
