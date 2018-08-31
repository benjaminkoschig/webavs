package ch.globaz.vulpecula.domain.models.decompte;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum�ration d�finissant les types de d�comptes pr�sents au BMS.
 * <p>
 * <b>M�thode A :</b> {@link #PERIODIQUE} et {@link #COMPLEMENTAIRE}
 * </p>
 * <p>
 * <b>M�thode B :</b> {@link #CONTROLE_EMPLOYEUR} et {@link #SPECIAL_SALAIRE}
 * </p>
 * 
 */
public enum TypeDecompte {
    PERIODIQUE(68014001),
    COMPLEMENTAIRE(68014002),
    CONTROLE_EMPLOYEUR(68014003),
    SPECIAL_SALAIRE(68014004),
    SPECIAL_CAISSE(68014006),
    CPP(68014005);

    private int value;

    private TypeDecompte(int value) {
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
    public static TypeDecompte fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de type de d�compte");
        }

        for (TypeDecompte t : TypeDecompte.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun Type de d�compte");
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (TypeDecompte t : TypeDecompte.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }

    public boolean isPeriodique() {
        return PERIODIQUE.equals(this);
    }

    public boolean isComplementaire() {
        return COMPLEMENTAIRE.equals(this);
    }

    public boolean isCPP() {
        return CPP.equals(this);
    }

    public boolean isSpecialSalaire() {
        return SPECIAL_SALAIRE.equals(this);
    }

    public boolean isSpecialCaisse() {
        return SPECIAL_CAISSE.equals(this);
    }

    public boolean isTraiterAsSpecial() {
        return isCPP() || isSpecialSalaire() || isSpecialCaisse();
    }
}
