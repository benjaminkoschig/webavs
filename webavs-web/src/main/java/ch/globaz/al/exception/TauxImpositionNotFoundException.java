package ch.globaz.al.exception;

import ch.globaz.vulpecula.domain.models.common.Date;

public class TauxImpositionNotFoundException extends Exception {
    private static final long serialVersionUID = -8246401272781424090L;
    private final Date date;
    private final String canton;
    private final String type;

    public TauxImpositionNotFoundException(Date date, String canton, String type) {
        super();
        this.date = date;
        this.canton = canton;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public String getCanton() {
        return canton;
    }

    public String getType() {
        return type;
    }
}
