package ch.globaz.vulpecula.domain.models.syndicat;

/**
 * Enumération représentant les différents types de liste pour les syndicats
 * 
 */
public enum ListeSyndicat {
    TRAVAILLEURS_SYNDICAT(68022001),
    TRAVAILLEURS_SALAIRE_SYNDICAT(68022002),
    TRAVAILLEURS_PAIEMENT_SYNDICAT(68022003),
    TRAVAILLEURS_SANS_SYNDICAT(68022004);

    private int value;

    private ListeSyndicat(final int value) {
        this.value = value;
    }

    /**
     * Construction de l'énumération à partir d'un code système
     * 
     * @param value Code système sous forme de String
     * @return Type de liste
     */
    public static ListeSyndicat fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de type liste de syndicat");
        }

        for (ListeSyndicat e : ListeSyndicat.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun etat connu");
    }
}
