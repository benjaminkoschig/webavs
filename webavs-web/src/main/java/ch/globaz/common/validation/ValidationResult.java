package ch.globaz.common.validation;

import globaz.globall.db.BSession;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

    public static final class Warn {
        private final String property;
        private final String source;
        private final ValidationError error;

        public Warn(String property, ValidationError error) {
            this.property = property;
            this.error = error;
            this.source = "";
        }

        public Warn(String property, String source, ValidationError error) {
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
            Warn warn = (Warn) o;
            return Objects.equals(property, warn.property) && Objects.equals(source, warn.source) && error == warn.error;
        }

        @Override
        public int hashCode() {
            return Objects.hash(property, source, error);
        }
    }


    private final Set<Error> errors;
    private final Set<Warn> warns;
    private final Map<String, Object> infos;

    public ValidationResult() {
        this.errors = new HashSet<>();
        this.warns = new HashSet<>();
        this.infos = new HashMap<>();
    }

    public boolean hasError() {
        return errors.size() > 0;
    }

    public boolean hasWarn() {
        return warns.size() > 0;
    }

    public boolean hasInfo() {
        return infos.size() > 0;
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

    public void addWarn(String propertie, ValidationError type) {
        this.warns.add(new Warn(propertie, type));
    }

    public void addWarn(String propertie, String source, ValidationError type) {
        this.warns.add(new Warn(propertie, source, type));
    }

    public void addInfo(String propertie, Object value) {
        this.infos.put(propertie, value);
    }


    public Set<Error> getErrors(){
        return errors;
    }

    public Set<Warn> getWarns(){
        return warns;
    }

    public Map<String, Object> getInfos(){
        return infos;
    }
}
