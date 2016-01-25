package ch.globaz.vulpecula.domain.models.decompte;

import java.util.ArrayList;
import java.util.List;

/***
 * Enum�ration repr�sentant l'�tat d'un d�compte d�fini dans le lot 6
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 10 d�c. 2013
 */
public enum EtatDecompte {
    GENERE(68012001),
    OUVERT(68012002),
    RECEPTIONNE(68012003),
    VALIDE(68012004),
    ERREUR(68012005),
    RECTIFIE(68012006),
    COMPTABILISE(68012007),
    ANNULE(68012008),
    TAXATION_DOFFICE(68012009),
    FACTURATION(68012010),
    SOMMATION(68012011);

    private int value;

    private EtatDecompte(final int value) {
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
     * @return Un �tat du {@link EtatDecompte}
     */
    public static EtatDecompte fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me d'etat de d�compte");
        }

        for (EtatDecompte e : EtatDecompte.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun �tat de d�compte connu");
    }

    /**
     * Retourne si le code syst�me pass�e en param�tre correspondant bien � un {@link EtatDecompte}
     * 
     * @param value
     *            Code syst�me
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
            EtatDecompte.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (EtatDecompte t : EtatDecompte.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}
