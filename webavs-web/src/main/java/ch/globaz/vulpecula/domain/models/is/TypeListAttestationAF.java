package ch.globaz.vulpecula.domain.models.is;

import java.util.ArrayList;
import java.util.List;

public enum TypeListAttestationAF {
    ALLOCATAIRE(68027001),
    FISC(68027002);

    private int value;

    private TypeListAttestationAF(int value) {
        this.value = value;
    }

    public String getValue() {
        return String.valueOf(value);
    }

    public static TypeListAttestationAF fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La valeur " + value
                    + " ne correspond pas à une liste d'attestation AF valide");
        }

        for (TypeListAttestationAF t : TypeListAttestationAF.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun type de liste");
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (TypeListAttestationAF t : TypeListAttestationAF.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}
