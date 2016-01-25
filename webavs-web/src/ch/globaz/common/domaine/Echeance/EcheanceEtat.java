package ch.globaz.common.domaine.Echeance;

public enum EcheanceEtat implements CodeSysteme<EcheanceEtat> {

    PLANIFIEE("52204001"),
    ECHUE("52204002"),
    TRAITEE("52204003"),
    ANNULEE("52204004");

    private String id;

    EcheanceEtat(String id) {
        this.id = id;
    }

    @Override
    public String getCodeSysteme() {
        return id;
    }

}
