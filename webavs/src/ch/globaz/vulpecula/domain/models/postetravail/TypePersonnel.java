package ch.globaz.vulpecula.domain.models.postetravail;

/**
 * Enumération représentant un type de personnel pour une qualification.
 */
public enum TypePersonnel {
    EXPLOITATION(68010001),
    ADMINISTRATIF(68010002);

    private int value;

    private TypePersonnel(final int value) {
        this.value = value;
    }

    /**
     * Retourne le code système représentant un type de personnel rattaché à une qualification.
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
     *            Code système sous forme de String
     * @return Etat de l'énumération
     */
    public static TypePersonnel fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de qualification");
        }

        for (TypePersonnel q : TypePersonnel.values()) {
            if (valueAsInt == q.value) {
                return q;
            }
        }
        throw new IllegalArgumentException("La valeur (" + value + ") ne correspond à aucune qualification connue");
    }

    /**
     * Retourne si le paramètre correspond bien à un code système de la famille
     * des types de personnel
     * 
     * @param value
     *            Code système
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
            fromValue(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
