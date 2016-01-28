package ch.globaz.common.sql;

import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import org.junit.Test;

public class QueryExecutorTest {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.ibm.db2.jcc.DB2Driver";
    static final String DB_URL = "jdbc:db2://sglobdev2.ju.globaz.ch:50005/dev2prod";
    static final String SCHEMA = "CCJUWEB";

    // Database credentials
    static final String USER = "db2inst1";
    static final String PASS = "db2inst1";

    @Test
    public void testExecuteQueryList() throws Exception {
        Connection connection = null;
        try {
            connection = createConnection();
            List<String> names = QueryExecutor.executeQueryList(
                    "select TBNAME from schema.JADEINCR where TBNAME='PCPCACC'", String.class, connection, SCHEMA);

            List<String> names2 = QueryExecutor.executeQueryList(
                    "select incval from schema.JADEINCR where TBNAME='PCPCACC'", String.class, connection, SCHEMA);

            assertEquals(1, names.size());

            assertTrue(names2.get(0) instanceof String);

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private static Connection createConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;

        Class.forName(JDBC_DRIVER);
        System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        System.out.println("Database created successfully...");

        return conn;
    }

}
