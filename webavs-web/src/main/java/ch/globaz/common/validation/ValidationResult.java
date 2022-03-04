package ch.globaz.common.validation;

import java.util.HashMap;
import java.util.Map;

public final class ValidationResult {
    Map<String, ValidationError> error;

    public ValidationResult() {
        this.error = new HashMap<>();
    }

    public boolean hasError() {
        return error.size() > 0;
    }

    /**
     * Add an error
     * @param propertie propertie in error
     * @param type type of error
     */
    public void addError(String propertie, ValidationError type) {

    }

    public Map<String, ValidationError> getError(){
        return error;
    }
}
