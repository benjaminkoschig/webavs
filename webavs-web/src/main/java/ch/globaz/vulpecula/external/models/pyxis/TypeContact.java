package ch.globaz.vulpecula.external.models.pyxis;

import java.util.ArrayList;
import java.util.List;

/***
 * Enumération représentant le type de qualification
 * 
 * @author Jonas Paratte (JPA) | Créé le 13 juillet 2016
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
     * Retourne le code système représentant le type de qualification
     * 
     * @return String représentant un code système
     */
    public String getValue() {
        return String.valueOf(value);
    }

    /**
     * Construction de l'énumération à partir d'un code système
     * 
     * @param value
     *            String représentant un code système
     * @return Un état du {@link TypeContact}
     */
    public static TypeContact fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de type de qualification");
        }

        for (TypeContact e : TypeContact.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur : " + value
                + " ne correspond à aucun type de qualification connu");
    }

    /**
     * Retourne si le code système passée en paramètre correspondant bien à un {@link TypeContact}
     * 
     * @param value
     *            Code système
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