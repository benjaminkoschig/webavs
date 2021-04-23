package ch.globaz.common.jadedb;

import ch.globaz.common.jadedb.exception.JadeDataBaseException;
import globaz.globall.db.BStatement;

import java.util.Optional;

public class JadePropertyReader {

    public static Integer readInteger(String column, BStatement statement) {
        return readNumeric(column, statement).map(value -> toInteger(value, column)).orElse(null);
    }

    private static Optional<String> readNumeric(String column, BStatement statement) {
        try {
            String numeric = statement.dbReadNumeric(column);
            if (numeric == null || numeric.length() == 0) {
                return Optional.empty();
            }
            return Optional.of(numeric);
        } catch (Exception e) {
            throw new JadeDataBaseException(e);
        }
    }

    private static Integer toInteger(final String value, final String column) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new JadeDataBaseException("Impossible de convertir en Integer une valeur de la colonne " + column
                                                    + " pour la valeur: " + value);
        }
    }

}
