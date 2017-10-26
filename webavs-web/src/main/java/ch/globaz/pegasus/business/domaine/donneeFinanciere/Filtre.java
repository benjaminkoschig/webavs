package ch.globaz.pegasus.business.domaine.donneeFinanciere;

public interface Filtre<T> {
    public boolean condition(T t);
}
