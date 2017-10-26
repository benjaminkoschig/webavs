package ch.globaz.hera.domaine.relationconjoint;

public enum TypeRelation {
    MARIE,
    DIVORCE,
    SEPARE_DE_FAIT,
    SEPARE_JUDICIAIREMENT,
    INDEFINIE,
    ENFANT_COMMUN,
    NONE;

    public boolean isConjointMarie() {
        return MARIE.equals(this);
    }

    public boolean isConjointDivorce() {
        return DIVORCE.equals(this);
    }

    public boolean isSepareDeFait() {
        return SEPARE_DE_FAIT.equals(this);
    }

    public boolean isSePare() {
        return SEPARE_JUDICIAIREMENT.equals(this);
    }

    public boolean isNone() {
        return NONE.equals(this);
    }
}
