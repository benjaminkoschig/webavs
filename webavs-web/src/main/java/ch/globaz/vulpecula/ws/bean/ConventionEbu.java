package ch.globaz.vulpecula.ws.bean;

import globaz.jade.client.util.JadeStringUtil;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "convention")
public enum ConventionEbu {
    INDUSTRIE_DU_BOIS(01),
    PLATRERIE_PEINTURE(02),
    TECHNIQUE_DU_BATIMENT(03),
    CONSTRUCTION_METALLIQUE(04),
    ELECTRICITE(05),
    PAYSAGISME(12),
    INTERPROFESSIONNELLE(13);

    private int value;

    private ConventionEbu(int value) {
        this.value = value;
    }

    /**
     * Retourne le code syst�me repr�sentant le type de facturation
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
     * @return Un �tat du {@link ConventionEbu}
     */
    public static ConventionEbu fromValue(String value) {
        if (!JadeStringUtil.isEmpty(value)) {
            Integer valueAsInt = null;
            try {
                valueAsInt = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La valeur " + value
                        + " doit correspondre � un entier repr�sentant un code syst�me de type de facturation");
            }

            for (ConventionEbu e : ConventionEbu.values()) {
                if (valueAsInt == e.value) {
                    return e;
                }
            }
            throw new IllegalArgumentException("La valeur : " + value
                    + " ne correspond � aucun type de qualification connu");

        }
        return null;
    }

    /**
     * Retourne si le code syst�me pass�e en param�tre correspondant bien � un {@link ConventionEbu}
     * 
     * @param value
     *            Code syst�me
     * @return true si valide
     */
    public static boolean isValid(String value) {
        try {
            ConventionEbu.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
