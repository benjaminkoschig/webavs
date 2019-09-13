package ch.globaz.common.process.byitem;

import java.util.*;

public abstract class ProcessItem implements Runnable {
    private LinkedHashMap<String, List<String>> errors = new LinkedHashMap<String, List<String>>();
    private Throwable exception;

    public abstract void treat() throws Exception;

    public abstract String getDescription();

    public Throwable getException() {
        return exception;
    }

    public void catchException(Throwable exception) {
        this.exception = exception;
    }

    public void addErrors(String message) {
        errors.put(message, new ArrayList<String>());
    }

    public void addErrors(String message, String... params) {
        errors.put(message, Arrays.asList(params));
    }

    public boolean hasError() {
        return !errors.isEmpty();
    }

    public boolean hasErrorOrException() {
        if (hasException()) {
            return true;
        }
        return hasError();
    }

    public boolean hasException() {
        return exception != null;
    }

    public LinkedHashMap<String, List<String>> getErrors() {
        return errors;
    }

    @Override
    public void run() {
        try {
            treat();
        } catch (Throwable e) {
            catchException(e);
        }
    }
}
