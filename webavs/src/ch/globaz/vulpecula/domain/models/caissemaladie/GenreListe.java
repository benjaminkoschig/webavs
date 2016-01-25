package ch.globaz.vulpecula.domain.models.caissemaladie;

/**
 * Enumération représentant les différents types de liste pour les caisses maladies
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
     * Construction de l'énumération à partir d'un code système
     * 
     * @param value Code système sous forme de String
     * @return Type de liste
     */
    public static GenreListe fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système d'une liste de caisse maladie");
        }

        for (GenreListe e : GenreListe.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun etat connu");
    }
}
