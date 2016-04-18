package ch.globaz.common.sql;

import java.math.BigDecimal;

class IntegerConverter implements ConverterDb<Integer> {

    @Override
    public Integer convert(Object value, String fieldName, String alias) {
        if (value != null) {
            if (Integer.class.equals(value.getClass())) {
                return (Integer) value;
            } else if (BigDecimal.class.equals(value.getClass())) {
                return ((BigDecimal) value).stripTrailingZeros().intValue();
            } else {
                String s = ((String) value).trim();
                if (!s.isEmpty()) {
                    return Integer.valueOf(s);
                }
                return null;
            }
        }
        return null;
    }

    @Override
    public Class<Integer> forType() {
        return Integer.class;
    }

}
