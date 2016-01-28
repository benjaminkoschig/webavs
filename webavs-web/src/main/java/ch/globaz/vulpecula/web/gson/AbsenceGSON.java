package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;
import ch.globaz.vulpecula.domain.models.decompte.Absence;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.TypeAbsence;

/**
 * @author Arnaud Geiser (AGE) | Créé le 30 avr. 2014
 * 
 */
public class AbsenceGSON implements Serializable {
    private static final long serialVersionUID = 3837248260596799868L;
    public String id;
    public String typeAbsence;

    public Absence convertToDomain(final DecompteSalaire decompteSalaire) {
        Absence absence = new Absence();

        absence.setType(TypeAbsence.fromValue(typeAbsence));

        return absence;
    }
}
