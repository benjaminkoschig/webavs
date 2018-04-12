package ch.globaz.orion.business.domaine.pucs;

public enum DeclarationSalaireType {
    PRINCIPALE(0),
    COMPLEMENTAIRE(1);

    private Integer value;

    private DeclarationSalaireType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public boolean isPrincipale() {
        return PRINCIPALE.equals(this);
    }

    public boolean isComplementaire() {
        return COMPLEMENTAIRE.equals(this);
    }
}
