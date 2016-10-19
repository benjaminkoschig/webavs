package ch.globaz.orion.ws.exceptions;

import javax.xml.ws.WebFault;

@WebFault(name = "WebavsException")
public class WebAvsException extends Exception {

    private static final long serialVersionUID = -6380859791100051706L;

    public WebAvsException(String message) {
        super(message);
    }

    public WebAvsException(String message, Throwable t) {
        super(message, t);
    }

}
