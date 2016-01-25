package ch.globaz.vulpecula.domain.models.common;

import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;

public enum Destinataire {
    INTERNE(68028001),
    ALLOCATAIRE(68028002);

    private int value;

    private Destinataire(int value) {
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
     * @return Un type de décompte {@link TypeDecompte}
     */
    public static Destinataire fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La valeur doit correspondre à un entier représentant un code système");
        }

        for (Destinataire t : Destinataire.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun Type de décompte");
    }
}
