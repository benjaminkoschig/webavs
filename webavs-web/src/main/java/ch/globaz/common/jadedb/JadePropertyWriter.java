package ch.globaz.common.jadedb;

import globaz.globall.db.BStatement;

public class JadePropertyWriter {
    public static void writeNumeric(String column, Number value, BStatement statement) {
        statement.writeField(column, numberToString(value));
    }

    private static String numberToString(Number value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

}
