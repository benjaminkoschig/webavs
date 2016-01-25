package ch.globaz.common.properties;

import globaz.jade.exception.JadeApplicationException;

public class PropertiesException extends JadeApplicationException {

    private static final long serialVersionUID = -1122715021364865227L;

    public PropertiesException() {
        super();
    }

    public PropertiesException(String m) {
        super(m);
    }

    public PropertiesException(String m, Throwable t) {
        super(m, t);
    }

}
