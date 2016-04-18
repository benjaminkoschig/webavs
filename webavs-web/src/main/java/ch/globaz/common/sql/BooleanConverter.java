package ch.globaz.common.sql;

import globaz.jade.persistence.sql.JadeSqlConstantes;

class BooleanConverter implements ConverterDb<Boolean> {

    @Override
    public Boolean convert(Object value, String fieldName, String alias) {
        return JadeSqlConstantes.DB_BOOLEAN_TRUE.equals(String.valueOf(value));
    }

    @Override
    public Class<Boolean> forType() {
        return Boolean.class;
    }

}
