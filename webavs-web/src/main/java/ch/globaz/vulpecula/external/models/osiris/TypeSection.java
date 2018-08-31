package ch.globaz.vulpecula.external.models.osiris;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.external.models.pyxis.Role;

/**
 * Liste des types de sections possibles
 * 
 * @since WebBMS 0.01.03
 */
public enum TypeSection {
    AF(15),
    ANCIENNE_FACTURE(8),
    APG(16),
    ARD(96),
    AVANCES(7),
    BLOCAGE(6),
    BULLETIN_NEUTRE(81),
    DECISION_PC(75),
    DECOMPTE_CAP_CGAS(88),
    DECOMPTE_COTISATION(1),
    ETUDIANTS(2),
    IJAI(21),
    PC(78),
    PRESTATIONS_CONVENTIONNELLES(29),
    RENTE_AVS_AI(4),
    RESTITUTION(26),
    RETOUR(5),
    RFM(76),
    ASSOCIATION(10),
    CPP(11);

    private int value;

    private TypeSection(final int value) {
        this.value = value;
    }

    /**
     * Retourne le code syst�me repr�sentant le type de section
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
     * @return Un �tat du {@link TypeSection}
     */
    public static TypeSection fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de type de section");
        }

        for (TypeSection e : TypeSection.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun type de section connu");
    }

    /**
     * Retourne si le code syst�me pass�e en param�tre correspondant bien � un {@link TypeSection}
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
        for (TypeSection t : TypeSection.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }

}
