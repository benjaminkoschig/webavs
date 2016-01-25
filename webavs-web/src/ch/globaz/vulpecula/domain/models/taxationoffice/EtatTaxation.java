package ch.globaz.vulpecula.domain.models.taxationoffice;

import java.util.ArrayList;
import java.util.List;

/**
 * Repr�sente les diff�rents �tats possibles pour les taxations d'office.
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
     * Retourne le code syst�me repr�sentant la valeur de l'�tat de la taxation d'office (code syst�me).
     * 
     * @return String repr�sentant un code syst�me
     */
    public String getValue() {
        return String.valueOf(value);
    }

    /**
     * Construction de l'�num�ration � partir d'un code syst�me.
     * 
     * @param value
     *            String repr�sentant un code syst�me
     * @return Un type de d�compte {@link EtatTaxation}
     */
    public static EtatTaxation fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de type de taxation");
        }

        for (EtatTaxation t : EtatTaxation.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun type de taxation");
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (EtatTaxation t : EtatTaxation.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}
