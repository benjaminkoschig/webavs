package ch.globaz.vulpecula.ws.bean;

import java.util.ArrayList;
import java.util.List;

public enum StatusAnnonceTravailleurEbu {

    EN_COURS(511110),
    TRAITE(511111),
    REFUSE(511112),
    AUTO(511113);

    private int value;

    private StatusAnnonceTravailleurEbu(final int value) {
        this.value = value;
    }

    public String getValue() {
        return String.valueOf(value);
    }

    public static StatusAnnonceTravailleurEbu fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de StatusAnnonceTravailleurEbu");
        }

        for (StatusAnnonceTravailleurEbu status : StatusAnnonceTravailleurEbu.values()) {
            if (valueAsInt == status.value) {
                return status;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun StatusAnnonceTravailleurEbu connu");
    }

    public static boolean isValid(final String value) {
        try {
            StatusAnnonceTravailleurEbu.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (StatusAnnonceTravailleurEbu status : StatusAnnonceTravailleurEbu.values()) {
            types.add(String.valueOf(status.value));
        }
        return types;
    }

}
