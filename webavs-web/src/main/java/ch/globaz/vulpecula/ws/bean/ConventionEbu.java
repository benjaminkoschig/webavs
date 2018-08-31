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
     * Retourne le code système représentant le type de facturation
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
     * @return Un état du {@link ConventionEbu}
     */
    public static ConventionEbu fromValue(String value) {
        if (!JadeStringUtil.isEmpty(value)) {
            Integer valueAsInt = null;
            try {
                valueAsInt = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La valeur " + value
                        + " doit correspondre à un entier représentant un code système de type de facturation");
            }

            for (ConventionEbu e : ConventionEbu.values()) {
                if (valueAsInt == e.value) {
                    return e;
                }
            }
            throw new IllegalArgumentException("La valeur : " + value
                    + " ne correspond à aucun type de qualification connu");

        }
        return null;
    }

    /**
     * Retourne si le code système passée en paramètre correspondant bien à un {@link ConventionEbu}
     * 
     * @param value
     *            Code système
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
