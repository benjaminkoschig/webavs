package ch.globaz.vulpecula.domain.models;

import java.util.Comparator;
import ch.globaz.vulpecula.domain.models.absencejustifiee.TypeAbsenceJustifiee;

public class TypeAbsenceJustifieeComparator implements Comparator<TypeAbsenceJustifiee> {

    @Override
    public int compare(TypeAbsenceJustifiee o1, TypeAbsenceJustifiee o2) {
        int o1Value = Integer.valueOf(o1.getValue());
        int o2Value = Integer.valueOf(o2.getValue());
        return o1Value - o2Value;
    }

}
