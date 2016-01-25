package ch.globaz.param.business.exceptions;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.param.business.vo.KeyNameParameter;

/**
 * Classe mère de toutes les exceptions métier relatives aux paramètres
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
