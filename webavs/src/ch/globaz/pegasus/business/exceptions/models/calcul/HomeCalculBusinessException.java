package ch.globaz.pegasus.business.exceptions.models.calcul;

import java.util.ArrayList;

public class HomeCalculBusinessException extends CalculBusinessException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static String formatArrayListAsStringError(ArrayList<String> errorList) {
        StringBuilder errorListAsString = new StringBuilder("");

        for (String error : errorList) {
            errorListAsString.append(error).append(" ");
        }
        return errorListAsString.toString();
    }

    public HomeCalculBusinessException() {
        super();
    }

    public HomeCalculBusinessException(ArrayList<String> ml) {
        super(HomeCalculBusinessException.formatArrayListAsStringError(ml));
    }

    public HomeCalculBusinessException(String m) {
        super(m);
    }

    public HomeCalculBusinessException(String m, String... args) {
        super(m, args);
    }

    public HomeCalculBusinessException(String m, Throwable t) {
        super(m, t);
    }

    public HomeCalculBusinessException(String m, Throwable t, String... args) {
        super(m, t, args);
    }
}
