package ch.globaz.vulpecula.domain.models.postetravail;

/**
 * Enum�ration repr�sentant un type de personnel pour une qualification.
 */
public enum TypePersonnel {
    EXPLOITATION(68010001),
    ADMINISTRATIF(68010002);

    private int value;

    private TypePersonnel(final int value) {
        this.value = value;
    }

    /**
     * Retourne le code syst�me repr�sentant un type de personnel rattach� � une qualification.
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
     *            Code syst�me sous forme de String
     * @return Etat de l'�num�ration
     */
    public static TypePersonnel fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de qualification");
        }

        for (TypePersonnel q : TypePersonnel.values()) {
            if (valueAsInt == q.value) {
                return q;
            }
        }
        throw new IllegalArgumentException("La valeur (" + value + ") ne correspond � aucune qualification connue");
    }

    /**
     * Retourne si le param�tre correspond bien � un code syst�me de la famille
     * des types de personnel
     * 
     * @param value
     *            Code syst�me
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
