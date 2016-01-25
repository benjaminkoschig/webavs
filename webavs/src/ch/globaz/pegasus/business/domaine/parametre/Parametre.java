package ch.globaz.pegasus.business.domaine.parametre;

import ch.globaz.common.domaine.Date;

public interface Parametre<E> {
    public abstract Date getDateDebut();

    public abstract Date getDateFin();

    public abstract E getType();

}
