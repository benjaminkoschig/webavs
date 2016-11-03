package ch.globaz.common.process.byitem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ProcessItem implements Runnable {
    private Map<String, List<String>> errors = new HashMap<String, List<String>>();
    private Exception exception;

    public abstract void treat() throws Exception;

    public abstract String getDescription();

    public Exception getException() {
        return exception;
    }

    public void catchException(Exception exception) {
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

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    @Override
    public void run() {
        try {
            treat();
        } catch (Exception e) {
            catchException(e);
        }
    }
}
