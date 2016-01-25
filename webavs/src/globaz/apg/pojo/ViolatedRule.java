/**
 * 
 */
package globaz.apg.pojo;

/**
 * @author dde
 */
public class ViolatedRule {

    /**
     * Si la r�gle est quittan�able par une breakRule
     */
    private Boolean breakable;
    /**
     * Le code de la r�gle
     */
    private String errorCode;
    /**
     * Une description des tests effectu� par la r�gle
     */
    private String errorMessage;

    private boolean fatalErrorThrown;

    /**
     * @param errorCode
     *            Le code de la r�gle
     * @param errorMessage
     *            Une description des tests effectu� par la r�gle
     * @param breakable
     *            Si la r�gle est 'quittan�able' par une breakRule
     */
    public ViolatedRule(String errorCode, String errorMessage, Boolean breakable) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.breakable = breakable;
    }

    /**
     * Renseigne si la la r�gle en erreur peut �tre quittanc�e par une breakRule
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
