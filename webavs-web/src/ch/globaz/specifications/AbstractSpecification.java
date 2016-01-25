package ch.globaz.specifications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ch.globaz.utils.Pair;

public abstract class AbstractSpecification<T> implements Specification<T> {
    protected List<Pair<SpecificationMessage, List<String>>> message = new ArrayList<Pair<SpecificationMessage, List<String>>>();

    @Override
    public Specification<T> and(final Specification<T> specification) {
        return new AndSpecification<T>(this, specification);
    }

    @Override
    public final boolean isSatisfiedBy(T t) throws UnsatisfiedSpecificationException {
        boolean satisfied = isValid(t);
        boolean state = satisfied && !hasMessages();
        if (!state) {
            throw new UnsatisfiedSpecificationException(getMessage());
        }
        return true;
    }

    public boolean hasMessages() {
        return !getMessage().isEmpty();
    }

    public int getNbMessages() {
        return getMessage().size();
    }

    public void addMessage(SpecificationMessage specificationMessage) {
        addMessage(specificationMessage, new String[] {});

    }

    public void addMessage(SpecificationMessage specificationMessage, String... parameters) {
        Pair<SpecificationMessage, List<String>> pair = new Pair<SpecificationMessage, List<String>>(
                specificationMessage, Arrays.asList(parameters));
        message.add(pair);
    }

    @Override
    public List<Pair<SpecificationMessage, List<String>>> getMessage() {
        return message;
    }

    protected boolean isMessageEmpty() {
        if (message.isEmpty()) {
            return true;
        }
        return false;
    }
}