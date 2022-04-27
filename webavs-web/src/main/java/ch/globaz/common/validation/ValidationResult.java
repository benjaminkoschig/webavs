package ch.globaz.common.validation;

import globaz.globall.db.BSession;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class ValidationResult {
    public static final class Error {
        private final String property;
        private final String source;
        private final ValidationError error;

        public Error(String property, ValidationError error) {
            this.property = property;
            this.error = error;
            this.source = "";
        }

        public Error(String property, String source, ValidationError error) {
            this.property = property;
            this.source = source;
            this.error = error;
        }

        public String getProperty() {
            return property;
        }

        public String getSource() {
            return source;
        }

        public ValidationError getError() {
            return error;
        }

        public String getDesignation(BSession session) {
            return String.format(session.getLabel(error.toString()), property);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Error error = (Error) o;
            return Objects.equals(property, error.property);
        }

        @Override
        public int hashCode() {
            return Objects.hash(property);
        }
    }

    private final Set<Error> errors;

    public ValidationResult() {
        this.errors = new HashSet<>();
    }

    public boolean hasError() {
        return errors.size() > 0;
    }

    /**
     * Add an error
     * @param propertie propertie in error
     * @param type type of error
     */
    public void addError(String propertie, ValidationError type) {
        this.errors.add(new Error(propertie, type));
    }

    public void addError(String propertie, String source, ValidationError type) {
        this.errors.add(new Error(propertie, source, type));
    }

    public Set<Error> getErrors(){
        return errors;
    }
}
