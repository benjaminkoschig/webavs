package ch.globaz.pegasus.rpc.domaine;

public enum TypeAnnonce {
    PARTIEL,
    COMPLET,
    ANNULATION;

    public boolean isPartiel() {
        return TypeAnnonce.PARTIEL.equals(this);
    }

    public boolean isComplet() {
        return TypeAnnonce.COMPLET.equals(this);
    }

    public boolean isAnnulation() {
        return TypeAnnonce.ANNULATION.equals(this);
    }
}
