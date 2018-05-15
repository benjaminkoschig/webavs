package ch.globaz.orion.business.domaine.demandeacompte;

public enum DemandeModifAcompteType {
    ACOMPTE("1", "DEM_MODIF_ACO_TYPE_ACOMPTE", 605007),
    PROVISOIRE("2", "DEM_MODIF_ACO_TYPE_PROVISOIRE", 605001),
    CORRECTION("3", "DEM_MODIF_ACO_TYPE_CORRECTION", 605003),
    UNDEFINDED("", "", null);

    private String value;
    private String label;
    private Integer cs;

    private DemandeModifAcompteType(String value, String label, Integer cs) {
        this.value = value;
        this.label = label;
        this.cs = cs;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public Integer getCs() {
        return cs;
    }

    public static DemandeModifAcompteType fromValue(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        for (DemandeModifAcompteType enumAcompte : DemandeModifAcompteType.values()) {
            String val = enumAcompte.value;
            if (val.equals(value)) {
                return enumAcompte;
            }
        }
        throw new IllegalArgumentException("La valeur (" + value + ") ne correspond à aucun type connu");
    }
}
