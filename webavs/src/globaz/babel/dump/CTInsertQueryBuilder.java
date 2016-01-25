package globaz.babel.dump;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Une classe pour construire des requetes SQL de type insert pour des entity de type componant (relation 1-n).
 * 
 * @author vre
 */
public class CTInsertQueryBuilder {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private StringBuffer colNames = new StringBuffer();
    private StringBuffer colValues = new StringBuffer();
    private String parentIdColName;
    private String schema;
    private String selfIdColName;
    private String tableName;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance of CTInsertionWriter.
     */
    CTInsertQueryBuilder() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param colName
     *            DOCUMENT ME!
     * @param colValue
     *            DOCUMENT ME!
     */
    public void addCol(String colName, String colValue) {
        colNames.append(", ");
        colNames.append(colName);

        colValues.append(", ");
        colValues.append(colValue);
    }

    void printInsertQuery(PrintWriter out, String forcedParentId, int parentId, String parentBase, int selfId,
            String selfBase, String binf) throws IOException {
        if (tableName == null) {
            throw new IllegalStateException("tableName doit être renseigné");
        }

        if (schema == null) {
            throw new IllegalStateException("schema doit être renseigné©");
        }

        if (parentIdColName == null) {
            throw new IllegalStateException("parentIdColName doit être renseigné©");
        }

        if (selfIdColName == null) {
            throw new IllegalStateException("selfIdColName doit être renseigné©");
        }

        out.print("INSERT INTO ");
        out.print(schema);
        out.print(tableName);
        out.print(" (");
        out.print(parentIdColName);
        out.print(", ");
        out.print(selfIdColName);

        if (colNames.length() > 0) {
            out.print(colNames.toString());
        }

        out.print(") VALUES (");

        if (forcedParentId == null) {
            if ((binf != null) && (binf.length() != 0)) {
                out.print(binf);
                out.print("+");
            }

            if (parentBase != null) {
                out.print(parentBase);
                out.print("+");
            }

            out.print(parentId);
        } else {
            out.print(forcedParentId);
        }

        out.print(", ");

        if ((binf != null) && (binf.length() != 0)) {
            out.print(binf);
            out.print("+");
        }

        if (selfBase != null) {
            out.print(selfBase);
            out.print("+");
        }

        out.print(selfId);

        if (colValues.length() > 0) {
            out.print(colValues.toString());
        }

        out.println(");");
    }

    CTInsertQueryBuilder reset() {
        schema = tableName = parentIdColName = selfIdColName = null;
        colNames.setLength(0);
        colValues.setLength(0);

        return this;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param parentIdColName
     *            DOCUMENT ME!
     */
    public void setParentIdColName(String parentIdColName) {
        this.parentIdColName = parentIdColName;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     */
    public void setSchema(String schema) {
        this.schema = schema;

        if (schema.charAt(schema.length() - 1) != '.') {
            this.schema += '.';
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param selfIdColName
     *            DOCUMENT ME!
     */
    public void setSelfIdColName(String selfIdColName) {
        this.selfIdColName = selfIdColName;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param tableName
     *            DOCUMENT ME!
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
