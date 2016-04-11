package ch.globaz.common.sql;

/**
 * Le but de cette class est de permettre de créer une requête ou une partie de requête.
 * 
 * @author dma
 * 
 */
public class SQLWriter {

    private StringBuffer query = new StringBuffer();
    private String schema;
    private boolean mustAddOperator = false;

    private SQLWriter(String schema) {
        this.schema = schema;
    }

    public static SQLWriter newWriter() {
        return new SQLWriter(null);
    }

    public static SQLWriter newWriter(String schema) {
        return new SQLWriter(schema);
    }

    public SQLWriter select() {
        query.append("select");
        return this;
    }

    public SQLWriter select(String sql) {
        query.append("select ").append(sql);
        return this;
    }

    public SQLWriter where(String sql) {
        this.where().query.append(" ").append(sql);
        mustAddOperator = true;
        return this;
    }

    public SQLWriter where(String sql, String param) {
        this.where().query.append(" ").append(sql.replaceAll("\\?", param));
        mustAddOperator = true;
        return this;
    }

    public SQLWriter where() {
        query.append(" where");
        return this;
    }

    public SQLWriter or(String sql) {
        addOpertor(sql, "or");
        return this;
    }

    public SQLWriter or(String sql, String param) {
        if (isNotEmpty(param)) {
            this.or(sql.replaceAll("\\?", param));
        }
        return this;
    }

    public SQLWriter and(String sql) {
        addOpertor(sql, "and");
        return this;
    }

    public SQLWriter and(String sql, String param) {
        if (isNotEmpty(param)) {
            this.and(sql.replaceAll("\\?", param));
        }
        return this;
    }

    public SQLWriter from(String sql) {
        query.append(" from ").append(sql);
        return this;
    }

    public SQLWriter join(String sql) {
        query.append(" inner join ").append(sql);
        return this;
    }

    public SQLWriter leftJoin(String sql) {
        query.append(" left join ").append(sql);
        return this;
    }

    public SQLWriter rightJoin(String sql) {
        query.append(" right join ").append(sql);
        return this;
    }

    /**
     * @return le SQL si un schéma a été définit celui-ci sera automatiquement remplacé.
     */
    public String toSql() {
        if (schema != null) {
            return query.toString().replaceAll("schema.", schema);
        }
        return query.toString();
    }

    void addOpertor(String sql, String operator) {
        if (mustAddOperator) {
            query.append(" ").append(operator);
        }
        query.append(" ").append(sql);
        mustAddOperator = true;
    }

    boolean isNotEmpty(String param) {
        return param != null && param.length() > 0;
    }

    String replaceParam(String sql, String param) {
        return sql.replaceAll("\\?", param);
    }
}
