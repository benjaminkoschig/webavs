package ch.globaz.vulpecula.domain.models.taxationoffice;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente les différents états possibles pour les taxations d'office.
 * 
 * @author age
 * 
 */
public enum EtatTaxation {
    SAISI(68019001),
    VALIDE(68019002),
    FACTURATION(68019003),
    COMPTABILISE(68019004),
    ANNULE(68019005);

    private int value;

    private EtatTaxation(int value) {
        this.value = value;
    }

    /**
     * Retourne le code système représentant la valeur de l'état de la taxation d'office (code système).
     * 
     * @return String représentant un code système
     */
    public String getValue() {
        return String.valueOf(value);
    }

    /**
     * Construction de l'énumération à partir d'un code système.
     * 
     * @param value
     *            String représentant un code système
     * @return Un type de décompte {@link EtatTaxation}
     */
    public static EtatTaxation fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de type de taxation");
        }

        for (EtatTaxation t : EtatTaxation.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun type de taxation");
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (EtatTaxation t : EtatTaxation.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}
