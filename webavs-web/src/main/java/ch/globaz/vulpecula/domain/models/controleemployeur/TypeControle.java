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
     * Retourne le code syst�me repr�sentant le type de contr�le
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
     * @return Un �tat du {@link EtatDecompte}
     */
    public static TypeControle fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de type de contr�le employeur");
        }

        for (TypeControle e : TypeControle.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun type de contr�le");
    }

    /**
     * Retourne si le code syst�me pass�e en param�tre correspondant bien � un {@link TypeControle}
     * 
     * @param value
     *            Code syst�me
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
