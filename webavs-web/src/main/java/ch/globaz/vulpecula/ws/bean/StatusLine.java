package ch.globaz.vulpecula.ws.bean;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlEnumValue;

public enum StatusLine {
    @XmlEnumValue("UNCHANGED")
    UNCHANGED(521110),
    @XmlEnumValue("TO_ADD")
    TO_ADD(521111),
    @XmlEnumValue("TO_MODIFY")
    TO_MODIFY(521112),
    @XmlEnumValue("TO_DELETE")
    TO_DELETE(521113),
    @XmlEnumValue("MODIFIED")
    MODIFIED(521114),
    @XmlEnumValue("DELETED")
    DELETED(521115),
    @XmlEnumValue("ADDED")
    ADDED(521116),
    @XmlEnumValue("ERROR")
    ERROR(521117),
    @XmlEnumValue("TO_DELETE_PENDING")
    TO_DELETE_PENDING(521118);

    private int value;

    private StatusLine(final int value) {
        this.value = value;
    }

    public String getValue() {
        return String.valueOf(value);
    }

    public static StatusLine fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de StatusLine");
        }

        for (StatusLine status : StatusLine.values()) {
            if (valueAsInt == status.value) {
                return status;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun StatusLine connu");
    }

    public static boolean isValid(final String value) {
        try {
            StatusLine.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (StatusLine status : StatusLine.values()) {
            types.add(String.valueOf(status.value));
        }
        return types;
    }
}
