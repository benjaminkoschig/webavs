package ch.globaz.pegasus.rpc.businessImpl;

public class RpcTechnicalException extends RuntimeException {

    public RpcTechnicalException() {
        super();
    }

    public RpcTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcTechnicalException(String message) {
        super(message);
    }

    public RpcTechnicalException(Throwable cause) {
        super(cause);
    }

}
