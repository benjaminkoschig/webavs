package ch.globaz.common.sql;

import static org.fest.assertions.api.Assertions.*;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
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

    @Test
    public void testSplitSizeLimitEqualSizeList() throws Exception {
        List<List<Integer>> list = QueryExecutor.split(build(10), 10);
        assertThat(list).hasSize(1);
        assertThat(list.get(0)).hasSize(10);
    }

    @Test
    public void testSplitNoLimitSizeListSmaler() throws Exception {
        List<List<Integer>> list = QueryExecutor.split(build(10), 100);
        assertThat(list).hasSize(1);
        assertThat(list.get(0)).hasSize(10);
    }

    @Test
    public void testSplitLimitSizeListBiggerWithSameModule() throws Exception {
        List<List<Integer>> list = QueryExecutor.split(build(10), 5);
        assertThat(list).hasSize(2);
        assertThat(list.get(0)).contains(0, 1, 2, 3, 4);
        assertThat(list.get(1)).contains(5, 6, 7, 8, 9);
    }

    @Test
    public void testSplitLimitSizeListBiggerWithNotSameModule() throws Exception {
        List<List<Integer>> list = QueryExecutor.split(build(12), 5);
        assertThat(list).hasSize(3);
        assertThat(list.get(0)).contains(0, 1, 2, 3, 4);
        assertThat(list.get(1)).contains(5, 6, 7, 8, 9);
        assertThat(list.get(2)).contains(10, 11);
    }

    @Test
    public void testSplitLimitSize() throws Exception {
        List<List<Integer>> list = QueryExecutor.split(build(10000), 2000);
        assertThat(list).hasSize(5);
    }

    private List<Integer> build(int nbLigne) {
        List<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < nbLigne; i++) {
            values.add(i);
        }
        return values;
    }

    @Test
    public void testForInString() throws Exception {
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        assertThat(QueryExecutor.forInString(list)).isEqualTo("'1','2','3'");
    }
}
