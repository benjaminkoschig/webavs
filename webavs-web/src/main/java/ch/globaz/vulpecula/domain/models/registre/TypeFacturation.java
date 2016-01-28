package ch.globaz.vulpecula.domain.models.registre;

public enum TypeFacturation {
    VERSEMENT(68023001),
    NOTE_DE_CREDIT(68023002);

    private int value;

    private TypeFacturation(int value) {
        this.value = value;
    }

    /**
     * Retourne le code système représentant le type de facturation
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
     * @return Un état du {@link TypeFacturation}
     */
    public static TypeFacturation fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La valeur " + value
                    + " doit correspondre à un entier représentant un code système de type de facturation");
        }

        for (TypeFacturation e : TypeFacturation.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur : " + value
                + " ne correspond à aucun type de qualification connu");
    }

    /**
     * Retourne si le code système passée en paramètre correspondant bien à un {@link TypeFacturation}
     * 
     * @param value
     *            Code système
     * @return true si valide
     */
    public static boolean isValid(String value) {
        try {
            TypeFacturation.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
