package ch.globaz.vulpecula.businessimpl.services.decompte;

public enum DifferenceCotisationSalaire {
    NONE(null),
    DIFFERENCE_COTISATIONS("difference_cotisations"),
    DIFFERENCE_ANNEE("difference_annee");

    private String raison;

    private DifferenceCotisationSalaire(String raison) {
        this.raison = raison;
    }

    public String getRaison() {
        return raison;
    }
}
