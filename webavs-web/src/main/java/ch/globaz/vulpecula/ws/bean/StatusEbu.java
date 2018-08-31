package ch.globaz.vulpecula.ws.bean;

import java.util.ArrayList;
import java.util.List;

public enum StatusEbu {

    REFUSE(68038003),
    MODIFIE(68038004),
    NO_DIFF(68038002),
    AJOUTE(68038005),
    A_TRAITER(68038001),
    TRAITE(68038006);

    private int value;

    private StatusEbu(final int value) {
        this.value = value;
    }

    public String getValue() {
        return String.valueOf(value);
    }

    public static StatusEbu fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système d'etat de décompte");
        }

        for (StatusEbu status : StatusEbu.values()) {
            if (valueAsInt == status.value) {
                return status;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun état de décompte connu");
    }

    public static boolean isValid(final String value) {
        try {
            StatusEbu.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (StatusEbu status : StatusEbu.values()) {
            types.add(String.valueOf(status.value));
        }
        return types;
    }

}
