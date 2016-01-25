package ch.globaz.specifications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import ch.globaz.utils.Pair;

/**
 * Exception retournant une liste de messages relatives à des spécifications invalides (plausibilités).
 * 
 */
public class UnsatisfiedSpecificationException extends Exception {
    private static final long serialVersionUID = 1L;

    private List<Pair<SpecificationMessage, List<String>>> messages;

    public UnsatisfiedSpecificationException() {
        messages = new ArrayList<Pair<SpecificationMessage, List<String>>>();
    }

    public UnsatisfiedSpecificationException(List<Pair<SpecificationMessage, List<String>>> messages) {
        this.messages = messages;
    }

    public UnsatisfiedSpecificationException(SpecificationMessage specificationMessage, String... parametres) {
        this();
        addMessage(specificationMessage, parametres);
    }

    public UnsatisfiedSpecificationException merge(UnsatisfiedSpecificationException exception) {
        UnsatisfiedSpecificationException ex = new UnsatisfiedSpecificationException();
        ex.messages.addAll(messages);
        ex.messages.addAll(exception.messages);
        return ex;
    }

    public List<Pair<SpecificationMessage, List<String>>> getMessages() {
        return messages;
    }

    public void addMessage(SpecificationMessage specificationMessage, String... parametres) {
        messages.add(new Pair<SpecificationMessage, List<String>>(specificationMessage, Arrays.asList(parametres)));
    }

    public boolean isEmpty() {
        return messages.size() == 0;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        for (Pair<SpecificationMessage, List<String>> pair : messages) {
            String message = SpecificationMessage.getMessage(Locale.FRENCH, pair.getLeft(), pair.getRight());
            sb.append(message);
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }
}
