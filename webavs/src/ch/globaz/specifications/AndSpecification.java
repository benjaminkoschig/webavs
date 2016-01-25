package ch.globaz.specifications;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.utils.Pair;

public class AndSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec1;
    private Specification<T> spec2;

    public AndSpecification(final Specification<T> spec1, final Specification<T> spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    @Override
    public boolean isValid(T t) {
        boolean spec1Ok = spec1.isValid(t);
        boolean spec2Ok = spec2.isValid(t);
        return spec1Ok && spec2Ok;
    }

    @Override
    public List<Pair<SpecificationMessage, List<String>>> getMessage() {
        List<Pair<SpecificationMessage, List<String>>> liste = new ArrayList<Pair<SpecificationMessage, List<String>>>();
        liste.addAll(spec1.getMessage());
        liste.addAll(spec2.getMessage());
        return liste;
    }
}
