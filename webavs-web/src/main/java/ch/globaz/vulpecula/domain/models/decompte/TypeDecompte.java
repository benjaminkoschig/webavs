package ch.globaz.vulpecula.domain.models.decompte;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumération définissant les types de décomptes présents au BMS.
 * <p>
 * <b>Méthode A :</b> {@link #PERIODIQUE} et {@link #COMPLEMENTAIRE}
 * </p>
 * <p>
 * <b>Méthode B :</b> {@link #CONTROLE_EMPLOYEUR} et {@link #SPECIAL_SALAIRE}
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
     * Retourne le code système représentant le permis de travail
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
     * @return Un type de décompte {@link TypeDecompte}
     */
    public static TypeDecompte fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de type de décompte");
        }

        for (TypeDecompte t : TypeDecompte.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun Type de décompte");
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
