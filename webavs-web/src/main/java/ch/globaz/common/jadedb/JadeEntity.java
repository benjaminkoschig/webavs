package ch.globaz.common.jadedb;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class JadeEntity extends BEntity {

    private static final long serialVersionUID = 1L;
    private transient BStatement statement;

    protected abstract void writeProperties();

    protected abstract void readProperties();

    protected abstract Class<? extends TableDefinition> getTableDef();

    public abstract String getIdEntity();

    public abstract void setIdEntity(String id);

    public void persist(TransactionWrapper transaction) {
        try {
            this.save(transaction.getTransaction());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdEntity(_incCounter(transaction, "0"));
    }

    @Override
    protected final void _writeProperties(BStatement statement) throws Exception {
        this.statement = statement;
        writeProperties();
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        this.statement = statement;
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        this.statement = statement;
        writePrimaryKey();
    }

    @Override
    protected final void _readProperties(BStatement statement) throws Exception {
        this.statement = statement;
        readProperties();
    }

    protected void writeKey(TableDefinition tableDefinition, Object value) {
        statement.writeKey(tableDefinition.getColumn(), String.valueOf(value));
    }

    protected void writeKey(String column, Integer value) {
        statement.writeKey(column, String.valueOf(value));
    }

    public Date readDateTime(TableDefinition tableDefinition) {
        return this.readDateTime(tableDefinition.getColumn());
    }

    @SuppressWarnings("unchecked")
    public <T> T read(TableDefinition tableDefinition) {
        if (Integer.class.equals(tableDefinition.getType())) {
            return (T) readInteger(tableDefinition.getColumn());
        } else if (Double.class.equals(tableDefinition.getType())) {
            return (T) readDouble(tableDefinition.getColumn());
        } else if (Long.class.equals(tableDefinition.getType())) {
            return (T) readLong(tableDefinition.getColumn());
        } else if (String.class.equals(tableDefinition.getType())) {
            return (T) readString(tableDefinition.getColumn());
        } else if (Date.class.equals(tableDefinition.getType())) {
            return (T) readDate(tableDefinition.getColumn());
        } else if (Boolean.class.equals(tableDefinition.getType())) {
            return (T) readBoolean(tableDefinition.getColumn());
        } else if (BigDecimal.class.equals(tableDefinition.getType())) {
            return (T) readBigDecimal(tableDefinition.getColumn());
        } else {
            throw new RuntimeException("Type no pris en charge pour la colonne suivante: " + tableDefinition);
        }
    }

    private BigDecimal readBigDecimal(String column) {
        try {
            return statement.dbReadBigDecimal(column);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long readLong(String column) {
        String v = null;
        try {
            v = statement.dbReadNumeric(column);
            if (v == null || v.length() == 0) {
                return null;
            }
            return Long.valueOf(v);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Impossible de convertir en Long une valeur de la colonne " + column
                    + " pour la valeur: " + v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Double readDouble(String column) {
        String v = null;
        try {
            v = statement.dbReadNumeric(column);
            if (v == null || v.length() == 0) {
                return null;
            }
            return Double.valueOf(v);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Impossible de convertir en Double une valeur de la colonne " + column
                    + " pour la valeur: " + v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Integer readInteger(String column) {
        String v = null;
        try {
            v = statement.dbReadNumeric(column);
            if (v == null || v.length() == 0) {
                return null;
            }
            return Integer.valueOf(v);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Impossible de convertir en integer une valeur de la colonne " + column
                    + " pour la valeur: " + v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean readBoolean(String column) {
        try {
            return statement.dbReadBoolean(column);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de convertir en boolean une valeur de la colonne " + column);
        }
    }

    public Date readDateTime(String column) {
        String value = readString(column);
        try {
            if (value == null || "0".equals(value) || value.isEmpty()) {
                return null;
            }
            return newFormatter("yyyyMMddHHmmssSS").parse(value);
        } catch (ParseException e) {
            throw new RuntimeException("Problem to parse date from column'" + column + "' (value=" + value + ")", e);
        }
    }

    public Date readDate(String column) {
        String value = readString(column);
        try {
            if (value == null || "0".equals(value) || value.isEmpty()) {
                return null;
            }
            return newFormatter("yyyyMMdd").parse(value);
        } catch (ParseException e) {
            throw new RuntimeException("Problem to parse date from column'" + column + "' (value=" + value + ")", e);
        }
    }

    public String readString(TableDefinition tableDefinition) {
        return this.readString(tableDefinition.getColumn());
    }

    public String readString(String column) {
        try {
            return statement.dbReadString(column);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void writeBigDecimal(String column, BigDecimal value) {
        statement.writeField(column, _dbWriteBigDecimal(statement.getTransaction(), value.toPlainString()));
    }

    void writeBigDecimal(String column, BigDecimal value, String desc) {
        statement.writeField(column, _dbWriteBigDecimal(statement.getTransaction(), value.toPlainString(), desc));
    }

    void writeNumeric(String column, String value, String dec) {
        statement.writeField(column, _dbWriteNumeric(statement.getTransaction(), value, dec));
    }

    void writeNumeric(String column, Number value) {
        statement.writeField(column, numberToString(value));
    }

    void writeDate(String column, String date, String desc) {
        statement.writeField(column, _dbWriteDateAMJ(statement.getTransaction(), date, desc));
    }

    void writeDate(String column, Date date, String desc) {
        if (column == null) {
            statement.writeField(column, null);
        } else {
            statement.writeField(column, newFormatter("yyyyMMdd").format(date));
        }
    }

    void writeDateTime(String column, Date date, String desc) {
        if (date == null) {
            statement.writeField(column, null);
        } else {
            statement.writeField(column, newFormatter("yyyyMMddHHmmssSS").format(date));
        }
    }

    void writeString(String column, String value, String desc) {
        statement.writeField(column, _dbWriteString(statement.getTransaction(), value, desc));
    }

    void writeBoolean(String column, boolean value, String desc) {
        statement.writeField(column, _dbWriteBoolean(statement.getTransaction(), value, desc));
    }

    private String numberToString(Number value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public void write(TableDefinition def, Object value) {
        this.write(def.getColumn(), value, def.toString());
    }

    public void writeDateTime(TableDefinition def, Date date) {
        this.writeDateTime(def.getColumn(), date, def.toString());
    }

    public void writeNumber(TableDefinition def, Object value) {
        writeNumeric(def.getColumn(), String.valueOf(value), def.toString());
    }

    public void write(String column, Object value, String desc) {
        if (value == null) {
            statement.writeField(column, null);
        } else if (value instanceof String) {
            writeString(column, (String) value, desc);
        } else if (value instanceof Integer || value instanceof Long || value instanceof Double) {
            writeNumeric(column, (Number) value);
        } else if (value instanceof Boolean) {
            writeBoolean(column, (Boolean) value, desc);
        } else if (value instanceof Date) {
            writeDate(column, (Date) value, desc);
        } else if (value instanceof BigDecimal) {
            writeBigDecimal(column, (BigDecimal) value, desc);
        } else {
            throw new RuntimeException("Type d'objet (" + value.getClass() + ") non pris en charge pour la valeur:"
                    + value + " de la colonne: " + column);
        }
    }

    public void writeStringAsNumeric(TableDefinition tableDefinition, Object value) {
        String v = null;
        if (value != null) {
            v = (String) value;
        }
        writeNumeric(tableDefinition.getColumn(), v, tableDefinition.toString());
    }

    @Override
    public final String _getTableName() {
        if (getTableDef().isEnum()) {
            TableDefinition tableDefinition = (getTableDef().getEnumConstants()[0]);
            return tableDefinition.getTableName();
        }
        return null;
    }

    private static SimpleDateFormat newFormatter(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setLenient(false);
        return formatter;
    }

    protected final void writePrimaryKey() {
        Field field = resolveFieldPrimaryKey();
        TableDefinition defPrimaryKey = resolveDefPK();
        try {
            this.writeKey(defPrimaryKey, field.get(this));
            writeNumber(defPrimaryKey, field.get(this));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // public void setIdEntity(Integer id) {
    // Field field = resolveFieldPrimaryKey();
    // try {
    // field.set(this, id);
    // } catch (IllegalArgumentException e) {
    // throw new RuntimeException(e);
    // } catch (IllegalAccessException e) {
    // throw new RuntimeException(e);
    // }
    // }

    // public Integer getIdEntity() {
    // Field field = resolveFieldPrimaryKey();
    // try {
    // return (Integer) field.get(this);
    // } catch (IllegalArgumentException e) {
    // throw new RuntimeException(e);
    // } catch (IllegalAccessException e) {
    // throw new RuntimeException(e);
    // }
    // }

    Field resolveFieldPrimaryKey() {
        TableDefinition defPrimaryKey = resolveDefPK();
        try {
            Field field = this.getClass().getDeclaredField(defPrimaryKey.getColumn().toLowerCase());
            field.setAccessible(true);
            return field;
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private TableDefinition resolveDefPK() {
        TableDefinition[] values = getTableDef().getEnumConstants();
        TableDefinition defPrimaryKey = null;
        for (TableDefinition tableDefinition : values) {
            if (tableDefinition.isPrimaryKey()) {
                defPrimaryKey = tableDefinition;
            }
        }
        return defPrimaryKey;
    }

    public String createSelectField(String alias) {
        TableDefinition[] values = getTableDef().getEnumConstants();
        StringWriter writer = new StringWriter();
        boolean addComma = false;
        for (TableDefinition def : values) {
            if (addComma) {
                writer.append(", ");
            } else {
                addComma = true;
            }
            writer.append(def.getColumn()).append(" as ");
            if (alias != null) {
                writer.append(alias);
            }
            writer.append(def.toString());
        }
        return writer.toString();
    }
}
