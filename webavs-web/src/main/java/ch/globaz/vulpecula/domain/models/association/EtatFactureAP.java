package ch.globaz.vulpecula.domain.models.association;

import java.util.ArrayList;
import java.util.List;

/***
 * Enum�ration repr�sentant l'�tat de facturation des associations professionnelles
 * 
 * @author Paratte Jonas
 */
public enum EtatFactureAP {
    A_VALIDER(68033001),
    VALIDE(68033002),
    GENERE(68033003),
    COMPTABILISE(68033004),
    EN_ERREUR(68033005),
    SUPPRIME(68033006),
    REFUSE(68033007);

    private int value;

    private EtatFactureAP(final int value) {
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
     * @return Un �tat du {@link EtatFactureAP}
     */
    public static EtatFactureAP fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me d'etat de d�compte");
        }

        for (EtatFactureAP e : EtatFactureAP.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun �tat de d�compte connu");
    }

    /**
     * Retourne si le code syst�me pass�e en param�tre correspondant bien � un {@link EtatFactureAP}
     * 
     * @param value
     *            Code syst�me
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
            EtatFactureAP.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (EtatFactureAP t : EtatFactureAP.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}
