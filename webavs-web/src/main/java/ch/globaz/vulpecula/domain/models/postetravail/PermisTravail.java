package ch.globaz.vulpecula.domain.models.postetravail;

/**
 * Enum�ration repr�sentant le permis de travail propre � un travailleur
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 10 d�c. 2013
 */
public enum PermisTravail {
    PERMIS_CATEGORIE_A(68004001),
    PERMIS_CATEGORIE_B(68004002),
    PERMIS_CATEGORIE_C(68004003),
    PERMIS_CATEGORIE_G_AVEC_RETENUE_IS(68004004),
    PERMIS_CATEGORIE_G_SANS_RETENUE_IS(68004005),
    AUTORISATION_COURTE_DUREE(68004006),
    ADMIS_PROVISOIREMENT_PERMIS_F(68004007),
    REQUERANT_ASILE_PERMIS_N(68004008),
    PERSONNE_A_PROTEGER(68004009),
    SANS_PERMIS_90JOURS(68004010);

    private int value;

    private PermisTravail(final int value) {
        this.value = value;
    }

    /**
     * Retourne le code syst�me repr�sentant le permis de travail
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
     * @return Un �tat du {@link PermisTravail}
     * @throws IllegalArgumentException si le String pass� en param�tre ne correspond pas � la valeur attendue
     */
    public static PermisTravail fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de permis de travail");
        }

        for (PermisTravail p : PermisTravail.values()) {
            if (valueAsInt == p.value) {
                return p;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun permis de travail connu");
    }
}
