package ch.globaz.vulpecula.domain.models.syndicat;

/**
 * Enum�ration repr�sentant les diff�rents types de liste pour les syndicats
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
     * Construction de l'�num�ration � partir d'un code syst�me
     * 
     * @param value Code syst�me sous forme de String
     * @return Type de liste
     */
    public static ListeSyndicat fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de type liste de syndicat");
        }

        for (ListeSyndicat e : ListeSyndicat.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun etat connu");
    }
}
