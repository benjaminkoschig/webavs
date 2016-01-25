package ch.globaz.vulpecula.external.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.utils.Pair;
import ch.globaz.vulpecula.util.I18NUtil;

public class ViewException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private List<Pair<SpecificationMessage, List<String>>> messages;

    public ViewException(UnsatisfiedSpecificationException ex) {
        messages = ex.getMessages();
    }

    public ViewException(List<Pair<SpecificationMessage, List<String>>> messages) {
        this.messages = messages;
    }

    public ViewException(SpecificationMessage specificationMessage) {
        messages = new ArrayList<Pair<SpecificationMessage, List<String>>>();
        Pair<SpecificationMessage, List<String>> pair = new Pair<SpecificationMessage, List<String>>(
                specificationMessage, new ArrayList<String>());
        messages.add(pair);
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        for (Pair<SpecificationMessage, List<String>> pair : messages) {
            String message = SpecificationMessage.getMessage(getUserLocale(), pair.getLeft(), pair.getRight());
            sb.append(message);
            sb.append("<br />");
        }
        return sb.toString();
    }

    protected Locale getUserLocale() {
        return I18NUtil.getUserLocale();
    }

}
