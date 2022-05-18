package ch.globaz.hera.domaine.relationconjoint;

public enum TypeRelation {
    MARIE,
    LPART,
    DIVORCE,
    LPART_DISSOUS,
    SEPARE_DE_FAIT,
    LPART_SEPARE_DE_FAIT,
    SEPARE_JUDICIAIREMENT,
    LPART_SEPARE_JUDICIAIREMENT,
    INDEFINIE,
    ENFANT_COMMUN,
    NONE;

    public boolean isConjointMarie() {
        return MARIE.equals(this);
    }

    public boolean isConjointLPart() {
        return LPART.equals(this);
    }

    public boolean isConjointDivorce() {
        return DIVORCE.equals(this);
    }

    public boolean isConjointLPartDissous() {
        return LPART_DISSOUS.equals(this);
    }

    public boolean isSepareDeFait() {
        return SEPARE_DE_FAIT.equals(this);
    }

    public boolean isLPartSepareDeFait() {
        return LPART_SEPARE_DE_FAIT.equals(this);
    }

    public boolean isSepare() {
        return SEPARE_JUDICIAIREMENT.equals(this);
    }

    public boolean isLPartSepare() {
        return LPART_SEPARE_JUDICIAIREMENT.equals(this);
    }

    public boolean isNone() {
        return NONE.equals(this);
    }
}
