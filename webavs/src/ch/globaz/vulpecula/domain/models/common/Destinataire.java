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
     * @return Un type de d�compte {@link TypeDecompte}
     */
    public static Destinataire fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La valeur doit correspondre � un entier repr�sentant un code syst�me");
        }

        for (Destinataire t : Destinataire.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun Type de d�compte");
    }
}
