package globaz.aquila.process;

import globaz.globall.db.BTransaction;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <h1>Description</h1>
 * <p>
 * Une classe pour construire des requetes SQL de type insert.
 * </p>
 * 
 * @author vre
 * @see ICOExportableSQL
 */
public class COInsertQueryBuilder {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private StringBuffer cols = new StringBuffer();
    private String schema;

    private BTransaction transaction;
    private StringBuffer values = new StringBuffer();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Ajoute une colonne à cette requête d'insertion SQL.
     * 
     * @param columnName
     *            le nom de la colonne
     * @param columnValue
     *            la valeur de la colonne FORMATTEE (par exemple, avec des guillemets si string)
     */
    public void addColumn(String columnName, String columnValue) {
        if (cols.length() > 0) {
            cols.append(",");
            values.append(",");
        }

        cols.append(columnName);
        values.append(columnValue);
    }

    /**
     * Exporte l'entité donnée en tant que requête SQL dans le writer donné.
     * 
     * @param out
     *            le writer dans lequel écrire
     * @param exportable
     *            l'entité à exporter
     * @throws IOException
     */
    public void printInsertQuery(PrintWriter out, ICOExportableSQL exportable) throws IOException {
        values.setLength(0);
        cols.setLength(0);

        exportable.export(this, transaction);

        out.print("INSERT INTO ");
        out.print(schema);
        out.print(exportable.getTableName());
        out.print(" (");

        if (cols.length() > 0) {
            out.print(cols.toString());
        }

        out.print(") VALUES (");

        if (values.length() > 0) {
            out.print(values.toString());
        }

        out.println(");");
    }

    /**
     * Réinitialise le builder.
     * 
     * @param schema
     * @param transaction
     * @return COInsertQueryBuilder Réinitialise le builder.
     */
    public COInsertQueryBuilder reset(String schema, BTransaction transaction) {
        this.transaction = transaction;
        this.schema = schema;

        if ((schema != null) && !schema.endsWith(".")) {
            this.schema += ".";
        }

        return this;
    }
}
