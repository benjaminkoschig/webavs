package ch.globaz.vulpecula.domain.models.postetravail;

/**
 * Enumération représentant le permis de travail propre à un travailleur
 * 
 * @author Arnaud Geiser (AGE) | Créé le 10 déc. 2013
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
     * @return Un état du {@link PermisTravail}
     * @throws IllegalArgumentException si le String passé en paramètre ne correspond pas à la valeur attendue
     */
    public static PermisTravail fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de permis de travail");
        }

        for (PermisTravail p : PermisTravail.values()) {
            if (valueAsInt == p.value) {
                return p;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun permis de travail connu");
    }
}
