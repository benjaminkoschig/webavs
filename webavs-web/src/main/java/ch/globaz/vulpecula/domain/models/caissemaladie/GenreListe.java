package ch.globaz.vulpecula.domain.models.caissemaladie;

/**
 * Enum�ration repr�sentant les diff�rents types de liste pour les caisses maladies
 * 
 */
public enum GenreListe {
    ADMISSION(68021001),
    DEMISSION(68021002);

    private int value;

    private GenreListe(final int value) {
        this.value = value;
    }

    /**
     * Construction de l'�num�ration � partir d'un code syst�me
     * 
     * @param value Code syst�me sous forme de String
     * @return Type de liste
     */
    public static GenreListe fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me d'une liste de caisse maladie");
        }

        for (GenreListe e : GenreListe.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun etat connu");
    }
}
