package ch.globaz.orion.business.models.af;


public enum UniteTempsEnum {
    MOIS("DETAIL_RECAP_UNITE_MOIS"),
    JOUR("DETAIL_RECAP_UNITE_JOUR"),
    HEURE("DETAIL_RECAP_UNITE_HEURE");

    private String label;

    private UniteTempsEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
