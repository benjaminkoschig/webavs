package globaz.osiris.external;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.external.models.pyxis.Role;

/**
 * Liste des types de sections possibles
 *
 * @since WebBMS 0.01.03
 */
public enum TypeSection {
    DECOMPTE_COTISATION(1),
    ETUDIANTS(2),
    RENTE_AVS_AI(4),
    RETOUR(5),
    BLOCAGE(6),
    AVANCES(7),
    ANCIENNE_FACTURE(8),
    AF(15),
    APG(16),
    IJAI(21),
    IRRECOUVRABLE(24),
    RECOUVREMENT_IRRECOUVRABLE(25),
    RESTITUTION(26),
    RESTITUTION_RENTE_EX_FGI(27),
    RESTITUTION_PC_EX_FGI(28),
    RESTITUTION_RFM_EX_FGI(29),
    FCF(61),
    ALLOCATION_MATERNITE_CANTONALE(62),
    FACTURATION_ORGANES_EXTERNES(69),
    PAIEMENT_PRINCIPAL(70),
    DECISION(71),
    DECISION_PC(75),
    DECISION_RFM(76),
    AVANCES_RFM(77),
    DECISION_PCF_RFM_RFG(78),
    AVANCES_PC(79),
    PC(78),
    DECOMPTE_CAP_CGAS(88),
    ARD(96);

    private int value;

    private TypeSection(final int value) {
        this.value = value;
    }

    /**
     * Retourne le code système représentant le type de section
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
     * @return Un état du {@link TypeSection}
     */
    public static TypeSection fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de type de section");
        }

        for (TypeSection e : TypeSection.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun type de section connu");
    }

    /**
     * Retourne si le code système passée en paramètre correspondant bien à un {@link TypeSection}
     *
     * @param value
     *            Code système
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
     * @return la liste des rôles
     */
    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (TypeSection t : TypeSection.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }

}
