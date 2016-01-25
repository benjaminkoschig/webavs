package ch.globaz.pegasus.businessimpl.utils;

public class RequerantConjoint<T> {
    private T conjoint;
    private T requerant;

    public T getConjoint() {
        return this.conjoint;
    }

    public T getRequerant() {
        return this.requerant;
    }

    public void setConjoint(T conjoint) {
        this.conjoint = conjoint;
    }

    public void setRequerant(T requerant) {
        this.requerant = requerant;
    }

    public boolean isRequerantEmpty() {
        return this.requerant == null;
    }

    public boolean isConjointEmpty() {
        return this.conjoint == null;
    }
}
