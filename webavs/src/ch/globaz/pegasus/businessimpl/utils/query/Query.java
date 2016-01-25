package ch.globaz.pegasus.businessimpl.utils.query;

public class Query {
    private String comment;
    private String name;
    private String sql;

    public String getComment() {
        return comment;
    }

    public String getName() {
        return name;
    }

    public String getSql() {
        return sql;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
