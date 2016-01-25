package ch.globaz.specifications;

import java.util.List;
import ch.globaz.utils.Pair;

/**
 * Interface repr�sentant une sp�cification (r�gle m�tier). Seule la m�thode
 * isValid doit �tre impl�ment�e
 * 
 * @param <T>
 */
public interface Specification<T> {
    Specification<T> and(Specification<T> specification);

    boolean isSatisfiedBy(T t) throws UnsatisfiedSpecificationException;

    List<Pair<SpecificationMessage, List<String>>> getMessage();

    boolean isValid(T t);
}
