package ch.globaz.common.sql;

import globaz.jade.client.util.JadeNumericUtil;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import ch.globaz.common.business.exceptions.CommonTechnicalException;

class Converters {

    /**
     * Used to instanciate the simple date formatter
     * 
     * @param pattern
     *            The date pattern to match
     * @return The simple date formatter
     */
    static SimpleDateFormat newFormatter(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setLenient(false);
        return formatter;
    }

    /**
     * Used to read date field content
     * 
     * @param rs
     *            resultSet that contains the result of the query
     * @param fieldName
     *            The field name that we want to extract the value from resultSet
     * @return The string that match the date field's value in "dd.MM.yyyy" format
     * @throws CommonTechnicalException
     *             Thrown if problem during value extraction, either because the resultset or the fieldname are not
     *             defined, or because the object type is not supported
     */
    static String dbReadDateAMJ(Object value, String fieldName) {
        try {
            if ((value == null) || JadeNumericUtil.isEmptyOrZero(value.toString())) {
                return "";
            }
            if (value instanceof BigDecimal) {
                Date date = newFormatter("yyyyMMdd").parse(((BigDecimal) value).toString());
                return newFormatter("dd.MM.yyyy").format(date);
            }
            if (value instanceof String) {
                Date date = newFormatter("yyyyMMdd").parse((String) value);
                return newFormatter("dd.MM.yyyy").format(date);
            }
        } catch (ParseException e) {
            throw new CommonTechnicalException("Problem to parse date from field'" + fieldName + "' (value=" + value
                    + ")", e);
        }
        throw new CommonTechnicalException("The field's type from field '" + fieldName
                + "' is currently not supported (value=" + value + ")");
    }

    /**
     * Used to read date formatted as month.year field content
     * 
     * @param rs
     *            resultSet that contains the result of the query
     * @param fieldName
     *            The field name that we want to extract the value from resultSet
     * @return The string that match the date field's value in "MM.yyyy" format
     * @throws CommonTechnicalException
     *             Thrown if problem during value extraction, either because the resultset or the fieldname are not
     *             defined, or because the object type is not supported
     */
    static String dbReadDateAM(Object value, String fieldName) {
        try {
            if ((value == null) || JadeNumericUtil.isEmptyOrZero(value.toString())) {
                return "";
            }
            if (value instanceof BigDecimal) {
                Date date = newFormatter("yyyyMM").parse(((BigDecimal) value).toString());
                return newFormatter("MM.yyyy").format(date);
            }
            if (value instanceof String) {
                Date date = newFormatter("yyyyMM").parse((String) value);
                return newFormatter("MM.yyyy").format(date);
            }
        } catch (ParseException e) {
            throw new CommonTechnicalException("Problem to parse date as (mm.yyyy) from field'" + fieldName + "'", e);
        }
        throw new CommonTechnicalException("The field's type from field '" + fieldName
                + "' is currently not supported (value=" + value + ")");
    }

}
