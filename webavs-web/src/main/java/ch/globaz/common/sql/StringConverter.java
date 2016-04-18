package ch.globaz.common.sql;

class StringConverter implements ConverterDb<String> {

    @Override
    public String convert(Object value, String fieldName, String alias) {
        String val = String.valueOf(value).trim();
        if ("0".equals(val)) {
            val = null;
        }
        val = escapeSpecialChar(val);
        return val;
    }

    @Override
    public Class<String> forType() {
        return String.class;
    }

    private static String escapeSpecialChar(String val) {
        if (val != null) {
            val = val.replaceAll("¬", "\'").replaceAll("¢", "\"");
        }
        return val;
    }

}
