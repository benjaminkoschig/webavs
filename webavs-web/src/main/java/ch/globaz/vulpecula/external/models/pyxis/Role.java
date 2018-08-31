package ch.globaz.vulpecula.external.models.pyxis;

import java.util.ArrayList;
import java.util.List;

/**
 * Liste des r�les de tiers.
 *
 * @since WebBMS 0.01.03
 */
public enum Role {
    ADMINISTRATEUR(517041),
    ADMINISTRATION(517003),
    AF(517036),
    AFFILIE(517002),
    AFFILIE_PARITAIRE(517039),
    AFFILIE_PERSONNEL(517040),
    APG(517033),
    ASSURE(517001),
    BANQUE(517004),
    CONTRIBUABLE(517005),
    DEBITEUR(517090),
    IJAI(517034),
    PCF(517045),
    RENTIER(517038),
    BENEFICIAIRE_PRESTATIONS_CONVENTIONNELLES(68902001),
    ASSOCIATION_PROFESSIONNELLE(68902003);

    private int value;

    private Role(final int value) {
        this.value = value;
    }

    /**
     * Retourne le code syst�me repr�sentant le r�le
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
     * @return Un �tat du {@link Role}
     */
    public static Role fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de role");
        }

        for (Role e : Role.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun r�le connu");
    }

    /**
     * Retourne si le code syst�me pass�e en param�tre correspondant bien � un {@link Role}
     *
     * @param value
     *            Code syst�me
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
            Role.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * @return la liste des r�les
     */
    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (Role t : Role.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}
