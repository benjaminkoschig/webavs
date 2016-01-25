package ch.globaz.vulpecula.domain.models.registre;

public enum TypeFacturation {
    VERSEMENT(68023001),
    NOTE_DE_CREDIT(68023002);

    private int value;

    private TypeFacturation(int value) {
        this.value = value;
    }

    /**
     * Retourne le code syst�me repr�sentant le type de facturation
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
     * @return Un �tat du {@link TypeFacturation}
     */
    public static TypeFacturation fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La valeur " + value
                    + " doit correspondre � un entier repr�sentant un code syst�me de type de facturation");
        }

        for (TypeFacturation e : TypeFacturation.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur : " + value
                + " ne correspond � aucun type de qualification connu");
    }

    /**
     * Retourne si le code syst�me pass�e en param�tre correspondant bien � un {@link TypeFacturation}
     * 
     * @param value
     *            Code syst�me
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
