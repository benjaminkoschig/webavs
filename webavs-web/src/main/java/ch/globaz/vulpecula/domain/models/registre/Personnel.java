package ch.globaz.vulpecula.domain.models.registre;

import java.util.ArrayList;
import java.util.List;

/***
 * Enumération représentant le type de personnel
 * 
 * @author Arnaud Geiser (AGE) | Créé le 10 déc. 2013
 */
public enum Personnel {
    EXPLOITATION(68011001),
    ADMINISTRATIF(68011002);

    private int value;

    private Personnel(int value) {
        this.value = value;
    }

    /**
     * Retourne le code système représentant le type de personnel
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
     * @return Un état du {@link Personnel}
     */
    public static Personnel fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La valeur " + value
                    + " doit correspondre à un entier représentant un code système de personnel");
        }

        for (Personnel e : Personnel.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun personnel connu");
    }

    /**
     * Retourne si le code système passée en paramètre correspondant bien à un {@link Personnel}
     * 
     * @param value
     *            Code système
     * @return true si valide
     */
    public static boolean isValid(String value) {
        try {
            Personnel.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (Personnel t : Personnel.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}