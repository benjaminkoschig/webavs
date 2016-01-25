package ch.globaz.specifications;

import java.util.List;
import ch.globaz.utils.Pair;

/**
 * Interface représentant une spécification (règle métier). Seule la méthode
 * isValid doit être implémentée
 * 
 * @param <T>
 */
public interface Specification<T> {
    Specification<T> and(Specification<T> specification);

    boolean isSatisfiedBy(T t) throws UnsatisfiedSpecificationException;

    List<Pair<SpecificationMessage, List<String>>> getMessage();

    boolean isValid(T t);
}
