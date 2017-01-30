package FamilleException;

import ch.globaz.amal.business.exceptions.AmalException;

public class SimplePersonneANePasPoursuivreException extends AmalException {

    private static final long serialVersionUID = -3568143969738636942L;

    public SimplePersonneANePasPoursuivreException() {
        super();
    }

    public SimplePersonneANePasPoursuivreException(String m) {
        super(m);
    }

    public SimplePersonneANePasPoursuivreException(String m, Throwable t) {
        super(m, t);
    }

}
