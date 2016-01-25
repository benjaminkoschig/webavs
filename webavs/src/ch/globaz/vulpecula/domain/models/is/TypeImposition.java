package ch.globaz.vulpecula.domain.models.is;

import java.util.ArrayList;
import java.util.List;

public enum TypeImposition {
    TOUS(0),
    IMPOT_SOURCE(68024001),
    COMMISSION_PERCEPTION(68024002);

    private int value;

    private TypeImposition(int value) {
        this.value = value;
    }

    public String getValue() {
        return String.valueOf(value);
    }

    public static TypeImposition fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La valeur " + value + " ne correspond pas à un type d'imposition");
        }

        for (TypeImposition t : TypeImposition.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucune type d'imposition");
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (TypeImposition t : TypeImposition.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}
