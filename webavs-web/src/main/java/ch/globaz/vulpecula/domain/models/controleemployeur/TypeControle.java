package ch.globaz.vulpecula.domain.models.controleemployeur;

import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;

public enum TypeControle {
    FINAL(68034001),
    PERIODIQUE(68034002),
    SANS_CONTROLE(68034003);

    private int value;

    private TypeControle(final int value) {
        this.value = value;
    }

    /**
     * Retourne le code système représentant le type de contrôle
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
     * @return Un état du {@link EtatDecompte}
     */
    public static TypeControle fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de type de contrôle employeur");
        }

        for (TypeControle e : TypeControle.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun type de contrôle");
    }

    /**
     * Retourne si le code système passée en paramètre correspondant bien à un {@link TypeControle}
     * 
     * @param value
     *            Code système
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
            TypeControle.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
