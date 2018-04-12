package ch.globaz.orion.business.models.af;

public enum StatutRecapAfWebAvsEnum {
    GENEREE("DETAIL_RECAP_STATUT_GENEREE"),
    A_TRAITER("DETAIL_RECAP_STATUT_A_TRAITER"),
    TRAITEE("DETAIL_RECAP_STATUT_TRAITEE"),
    AUCUN_CHANGEMENT("DETAIL_RECAP_STATUT_AUCUN_CHANGEMENT"),
    CLOTUREE("DETAIL_RECAP_STATUT_CLOTUREE");

    private String label;

    private StatutRecapAfWebAvsEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
