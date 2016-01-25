package ch.globaz.vulpecula.external.models.hercule;

import java.util.ArrayList;
import java.util.List;

public enum InteretsMoratoires {
    AUTOMATIQUE(229001),
    SOUMIS(229002),
    EXEMPTE(229003),
    MANUEL(229004),
    A_CONTROLER(229005);

    private int value;

    private InteretsMoratoires(int value) {
        this.value = value;
    }

    /**
     * Retourne le code système représentant le permis de travail
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
     * @return Un type de décompte {@link InteretsMoratoires}
     */
    public static InteretsMoratoires fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système d'interet moratoire.");
        }

        for (InteretsMoratoires t : InteretsMoratoires.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun intéret moratoire");
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (InteretsMoratoires t : InteretsMoratoires.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }

    /**
     * Retourne si la valeur passée en paramètre est un code système valide
     * représentant un {@link InteretsMoratoires}.
     * 
     * @param value
     *            Code système
     * @return true si valide
     */
    public static boolean isValid(String value) {
        try {
            InteretsMoratoires.fromValue(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
