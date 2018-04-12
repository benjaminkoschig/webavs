package ch.globaz.orion.business.models.af;

public enum StatutLigneRecapEnum {
    GENEREE("DETAIL_RECAP_STATUT_LIGNE_GENEREE"),
    A_TRAITER("DETAIL_RECAP_STATUT_LIGNE_A_TRAITER"),
    AUCUN_CHANGEMENT("DETAIL_RECAP_STATUT_LIGNE_AUCUN_CHANGEMENT"),
    TRAITEE("DETAIL_RECAP_STATUT_LIGNE_TRAITEE");

    private String label;

    private StatutLigneRecapEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
