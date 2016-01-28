package ch.globaz.param.business.exceptions.models;

import ch.globaz.al.business.exceptions.ALException;
import ch.globaz.param.business.exceptions.ParamException;
import ch.globaz.param.business.vo.KeyNameParameter;

public class ParameterModelException extends ParamException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String UNDEFINED_KEY = "UNDEFINED";

    /**
     * @see ALException#ALException(String m)
     */

    public ParameterModelException(KeyNameParameter keyParam, String m) {
        super(keyParam, m);

    }

    /**
     * @see ALException#ALException(String m, Throwable t)
     */
    public ParameterModelException(KeyNameParameter keyParam, String m, Exception e) {
        super(keyParam, m, e);

    }
}
