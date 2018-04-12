package ch.globaz.orion.business.domaine.demandeacompte;

public enum DemandeModifAcompteStatut {
    A_TRAITER("1", "DEM_MODIF_ACO_STATUT_A_TRAITER"),
    VALIDE("2", "DEM_MODIF_ACO_STATUT_VALIDE"),
    REFUSE("3", "DEM_MODIF_ACO_STATUT_REFUSE"),
    COMPTABILISE("4", "DEM_MODIF_ACO_STATUT_COMPTABILISE"),
    UNDEFINDED("", "");

    private String value;
    private String label;

    private DemandeModifAcompteStatut(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static DemandeModifAcompteStatut fromValue(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        for (DemandeModifAcompteStatut enumAcompte : DemandeModifAcompteStatut.values()) {
            String val = enumAcompte.value;
            if (val.equals(value)) {
                return enumAcompte;
            }
        }
        throw new IllegalArgumentException("La valeur (" + value + ") ne correspond à aucun statut connu");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
