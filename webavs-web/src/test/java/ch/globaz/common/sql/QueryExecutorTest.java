package ch.globaz.common.sql;

import static org.fest.assertions.api.Assertions.*;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import com.google.common.base.Function;

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
        executeWithConnection(new Function<Connection, String>() {
            @Override
            public String apply(Connection connection) {
                List<String> names = QueryExecutor.executeQueryList(
                        "select TBNAME from schema.JADEINCR where TBNAME in ('PCPCACC','PCDROIT')", String.class,
                        connection, SCHEMA, null);
                assertEquals(2, names.size());
                assertThat(names).contains("PCPCACC", "PCDROIT");

                List<String> names2 = QueryExecutor.executeQueryList(
                        "select incval from schema.JADEINCR where TBNAME='PCPCACC'", String.class, connection, SCHEMA,
                        null);
                assertTrue(names2.get(0) instanceof String);
                List<Integer> listInt = QueryExecutor.executeQueryList(
                        "select incval from schema.JADEINCR where TBNAME='PCPCACC'", Integer.class, connection, SCHEMA,
                        null);

                assertEquals(1, names2.size());
                assertTrue(listInt.get(0) instanceof Integer);

                return null;
            }
        });
    }

    @Test
    public void testExecuteQueryListForBean() throws Exception {
        executeWithConnection(new Function<Connection, String>() {
            @Override
            public String apply(Connection connection) {
                List<TestBean> inc = QueryExecutor.executeQueryList(
                        "select PLAIDE as s1, PLALIB as s2  from schema.FWLANP order by PLAIDE", TestBean.class,
                        connection, SCHEMA, null);
                assertThat(inc).contains(new TestBean("D", "Deutsch"), new TestBean("F", "Français"),
                        new TestBean("I", "Italiano"));
                return null;
            }
        });
    }

    @Test
    public void testExecuteAggregate() throws Exception {
        executeWithConnection(new Function<Connection, String>() {
            @Override
            public String apply(Connection connection) {
                BigDecimal count = QueryExecutor.executeAggregate(
                        "select count(TBNAME) from schema.JADEINCR where TBNAME='PCPCACC'", SCHEMA, connection);

                assertThat(count).isEqualTo(new BigDecimal(1));
                return null;
            }
        });
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

    @Test
    public void testForInString() throws Exception {
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        assertThat(QueryExecutor.forInString(list)).isEqualTo("'1','2','3'");
    }

    private List<Integer> build(int nbLigne) {
        List<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < nbLigne; i++) {
            values.add(i);
        }
        return values;
    }

    private void executeWithConnection(Function<Connection, String> f) throws ClassNotFoundException, SQLException {
        Connection connection = null;
        try {
            connection = createConnection();
            f.apply(connection);
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

    @Test
    public void testResolveFieldsWithBean() throws Exception {
        List<Field> fields = QueryExecutor.resolveFields(TestBean.class);
        assertThat(fields).isNotEmpty();
        assertThat(fields).hasSize(3);
        assertThat(fields).contains(TestBean.class.getDeclaredField("s1"), TestBean.class.getDeclaredField("s2"),
                TestBean.class.getDeclaredField("bigDecimal"));
    }

    @Test
    public void testResolveFieldsWithString() throws Exception {
        List<Field> fields = QueryExecutor.resolveFields(String.class);
        assertThat(fields).isEmpty();
    }

    @Test(expected = CommonTechnicalException.class)
    public void testCreateListCheckConverters() throws Exception {
        ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

        Set<ConverterDb<?>> converters = new HashSet<ConverterDb<?>>();
        converters.add(new ConverterDb<String>() {

            @Override
            public String convert(Object value, String fieldName, String alias) {
                return null;
            }

            @Override
            public Class<String> forType() {
                return String.class;
            }
        });
        converters.add(new ConverterDb<String>() {

            @Override
            public String convert(Object value, String fieldName, String alias) {
                return null;
            }

            @Override
            public Class<String> forType() {
                return String.class;
            }
        });
        QueryExecutor.createList(TestBean.class, result, converters);
    }

}
