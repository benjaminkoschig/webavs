package ch.globaz.vulpecula.external.models.pyxis;

import java.util.ArrayList;
import java.util.List;

/***
 * Enum�ration repr�sentant le type de qualification
 * 
 * @author Jonas Paratte (JPA) | Cr�� le 13 juillet 2016
 */
public enum TypeContact {
    PRIVE(511001),
    PROFESSIONNEL(511002),
    PORTABLE(511003),
    FAX(511004),
    EMAIL(511005),
    CONTACT(511006),
    SITE(511007),
    EBUSINESS(511008);

    private int value;

    private TypeContact(int value) {
        this.value = value;
    }

    /**
     * Retourne le code syst�me repr�sentant le type de qualification
     * 
     * @return String repr�sentant un code syst�me
     */
    public String getValue() {
        return String.valueOf(value);
    }

    /**
     * Construction de l'�num�ration � partir d'un code syst�me
     * 
     * @param value
     *            String repr�sentant un code syst�me
     * @return Un �tat du {@link TypeContact}
     */
    public static TypeContact fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de type de qualification");
        }

        for (TypeContact e : TypeContact.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur : " + value
                + " ne correspond � aucun type de qualification connu");
    }

    /**
     * Retourne si le code syst�me pass�e en param�tre correspondant bien � un {@link TypeContact}
     * 
     * @param value
     *            Code syst�me
     * @return true si valide
     */
    public static boolean isValid(String value) {
        try {
            TypeContact.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (TypeContact t : TypeContact.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}