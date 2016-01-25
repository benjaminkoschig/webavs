package globaz.corvus.db.attestationsFiscales;

import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author SCR
 * 
 */
public class RETemporaryAttestationFiscale {

    private int counter = 0;
    private String idRenteAccordee = "0";
    protected BPreparedStatement insert;
    protected BStatement statement;

    private String tableName = "";

    /**
     * Initialise le Prepared Statement servant insert.
     */
    public RETemporaryAttestationFiscale(BTransaction transaction) throws Exception {
        insert = new BPreparedStatement(transaction);
        insert.prepareStatement(getPreparedSqlQuery(transaction));
    }

    /**
     * Termine le prepare statement.
     * 
     * @throws Exception
     */
    public void closeStatement() throws Exception {
        insert.closePreparedStatement();
    }

    public void createTable(BTransaction transaction, String tableName) throws Exception {
        statement = new BStatement(transaction);
        statement.createStatement();

        if (counter > 5) {
            throw new Exception("unable to create temporary table " + tableName);
        }

        this.tableName = tableName + transaction.getISession().getUserName();
        if (counter > 0) {
            this.tableName += counter;
        }
        String s = "CREATE TABLE " + Jade.getInstance().getDefaultJdbcSchema() + "." + tableName
                + " (ID_RA NUMERIC(15));";

        try {
            statement.execute(s);
        } catch (Exception e) {
            transaction.clearErrorBuffer();
            createTable(transaction, tableName);
        } finally {
            counter++;
        }
    }

    public void dropTable(BTransaction transaction) throws Exception {

        try {
            if (statement == null) {
                throw new Exception("Statement cannot be null, dropTable aborted !!!");
            }
            statement.executeQuery("DROP TABLE " + Jade.getInstance().getDefaultJdbcSchema() + "TEMP_RA;");
        } finally {
            if (statement != null) {
                statement.closeStatement();
            }
            if (insert != null) {
                insert.closePreparedStatement();
            }
        }
    }

    /**
     * Exécute la query INSERT.
     * 
     * @return
     * @throws SQLException
     */
    public void executeInsert(BTransaction transaction) throws Exception {
        insert.execute();
    }

    public ResultSet executeSQL(String idRenteAccordee) throws Exception {

        String sql = "SELECT ID_RA FROM  " + Jade.getInstance().getDefaultJdbcSchema() + "." + tableName;
        sql += " WHERE ID_RA = " + idRenteAccordee;

        return statement.executeQuery(sql);
    }

    /**
     * Efface puis remplit les variables à insérer en base de données.
     * 
     * @throws Exception
     */
    public void fillVariables() throws Exception {
        insert.clearParameters();
        insert.setInt(1, JadeStringUtil.parseInt(getIdRenteAccordee(), 0));
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public BPreparedStatement getInsert() {
        return insert;
    }

    /**
     * Contruit la requête INSERT.
     * 
     * @param transaction
     * @return
     */
    private String getPreparedSqlQuery(BTransaction transaction) {

        String sql = "INSERT INTO " + Jade.getInstance().getDefaultJdbcSchema() + "." + "TEMP_RA" + "(";
        sql += "ID_RA ";
        sql += ") VALUES ( ? )";
        return sql;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setInsert(BPreparedStatement insert) {
        this.insert = insert;
    }

}
