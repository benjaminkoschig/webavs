package ch.globaz.param.business.exceptions;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.param.business.vo.KeyNameParameter;

/**
 * Classe m�re de toutes les exceptions m�tier relatives aux param�tres
 * 
 * @author gmo
 * 
 */
public class ParamException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private KeyNameParameter keyParameter = null;

    public ParamException(KeyNameParameter keyParam, String message) {
        super(message);
        keyParameter = keyParam;
    }

    public ParamException(KeyNameParameter keyParam, String msg, Exception e) {
        super(msg, e);
        keyParameter = keyParam;
    }

    public KeyNameParameter getKeyParameter() {
        return keyParameter;
    }

}
