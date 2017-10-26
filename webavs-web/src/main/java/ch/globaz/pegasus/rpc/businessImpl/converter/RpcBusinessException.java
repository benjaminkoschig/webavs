package ch.globaz.pegasus.rpc.businessImpl.converter;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception triggered when business needs not completed
 * 
 * @author cel
 * 
 */
public class RpcBusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String labelMessage;
    private final List<Object> params;

    public RpcBusinessException(String labelMessage) {
        this.labelMessage = labelMessage;
        params = new ArrayList<Object>();
    }

    public RpcBusinessException(String labelMessage, Object... params) {
        this.params = new ArrayList<Object>();
        for (Object param : params) {
            this.params.add(param);
        }
        this.labelMessage = labelMessage;
    }

    public String getLabelMessage() {
        return labelMessage;
    }

    public List<Object> getParams() {
        return params;
    }

}
