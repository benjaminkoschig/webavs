package ch.globaz.vulpecula.domain.models.decompte;

import java.util.ArrayList;
import java.util.List;

/***
 * Enum�ration repr�sentant les motifs de prolongation d'un d�compte
 * 
 * @author Paratte Jonas (JPA) | Cr�� le 20 06 2014
 */
public enum MotifProlongation {
    SANS(0),
    TELEPHONE(68018001),
    CONFIRMATION_PAIEMENT(68018002),
    SURSIS_PAIEMENT(68018003),
    AUTRE(68018004);

    private int value;

    private MotifProlongation(final int value) {
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
     * @return Un �tat du {@link MotifProlongation}
     */
    public static MotifProlongation fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de motif de prolongation");
        }

        for (MotifProlongation e : MotifProlongation.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun motif de prolongation connu");
    }

    /**
     * Retourne si le code syst�me pass�e en param�tre correspondant bien � un {@link MotifProlongation}
     * 
     * @param value
     *            Code syst�me
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
            MotifProlongation.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (MotifProlongation t : MotifProlongation.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}
