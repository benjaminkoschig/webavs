package ch.globaz.common.sql;

import static org.fest.assertions.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class SQLWriterTest {

    @Test
    public void testSelectFields() throws Exception {
        assertThat(SQLWriter.write().select("field1, field2").toSql()).isEqualTo("select field1, field2");
    }

    @Test
    public void testFrom() throws Exception {
        assertThat(SQLWriter.write().from("table").toSql()).isEqualTo(" from table");
    }

    @Test
    public void testWhere() throws Exception {
        assertThat(SQLWriter.write().where("field1 == field2").toSql()).isEqualTo(" where field1 == field2");
    }

    @Test
    public void testWhereWithParam() throws Exception {
        assertThat(SQLWriter.write().where("field1 == '?'", "param").toSql()).isEqualTo(" where field1 == 'param'");
    }

    @Test
    public void testJoin() throws Exception {
        assertThat(SQLWriter.write().join("table1 on tabl1.id = table2.fk").toSql()).isEqualTo(
                " inner join table1 on tabl1.id = table2.fk");
    }

    @Test
    public void testLeftJoin() throws Exception {
        assertThat(SQLWriter.write().leftJoin("table1 on tabl1.id = table2.fk").toSql()).isEqualTo(
                " left join table1 on tabl1.id = table2.fk");
    }

    @Test
    public void testRightJoin() throws Exception {
        assertThat(SQLWriter.write().rightJoin("table1 on tabl1.id = table2.fk").toSql()).isEqualTo(
                " right join table1 on tabl1.id = table2.fk");
    }

    @Test
    public void testReplaceSchema() throws Exception {
        assertThat(
                SQLWriter.write("hl.").from("schema.table2 as t2").join("schema.table1 as t1 on t1.id = t2.fk").toSql())
                .isEqualTo(" from hl.table2 as t2 inner join hl.table1 as t1 on t1.id = t2.fk");
    }

    @Test
    public void testAnd() throws Exception {
        assertThat(SQLWriter.write().and("toto = tata").toSql()).isEqualTo(" toto = tata");
        assertThat(SQLWriter.write().where().and("toto = tata").toSql()).isEqualTo(" where toto = tata");
        assertThat(SQLWriter.write().where("t1=t2").and("toto = tata").toSql()).isEqualTo(
                " where t1=t2 and toto = tata");
    }

    @Test
    public void testAndWithParam() throws Exception {
        String param = null;
        assertThat(SQLWriter.write().and("toto = ?", param).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().and("toto = ?", "kk").toSql()).isEqualTo(" toto = kk");
    }

    @Test
    public void testAndWithParams() throws Exception {
        assertThat(SQLWriter.write().and("in (?,?,?)", "3", "2", "1").toSql()).isEqualTo(" in (3,2,1)");
        assertThat(SQLWriter.write().and("in (?)", new ArrayList<String>()).toSql()).isEqualTo("");
        List<String> l = new ArrayList<String>();
        l.add("1");
        l.add("2");
        l.add("3");
        assertThat(SQLWriter.write().and("in (?,?,?)", l).toSql()).isEqualTo(" in (1,2,3)");
    }

    @Test
    public void testOr() throws Exception {
        assertThat(SQLWriter.write().or("toto = tata").toSql()).isEqualTo(" toto = tata");
        assertThat(SQLWriter.write().where().or("toto = tata").toSql()).isEqualTo(" where toto = tata");
        assertThat(SQLWriter.write().where("t1=t2").or("toto = tata").toSql()).isEqualTo(" where t1=t2 or toto = tata");
    }

    @Test
    public void testOrWithParam() throws Exception {
        String param = null;
        assertThat(SQLWriter.write().or("toto = ?", param).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().or("toto = ?", "kk").toSql()).isEqualTo(" toto = kk");
    }

    @Test
    public void testOrWithParams() throws Exception {
        assertThat(SQLWriter.write().or("in (?,?,?)", "3", "2", "1").toSql()).isEqualTo(" in (3,2,1)");
        assertThat(SQLWriter.write().or("in (?)", new ArrayList<String>()).toSql()).isEqualTo("");
        List<String> l = new ArrayList<String>();
        l.add("1");
        l.add("2");
        l.add("3");
        assertThat(SQLWriter.write().or("in (?,?,?)", l).toSql()).isEqualTo(" in (1,2,3)");
    }

    @Test
    public void testIsNotEmpty() throws Exception {
        String param = null;
        assertThat(SQLWriter.write().isNotEmpty(param)).isFalse();
        assertThat(SQLWriter.write().isNotEmpty("")).isFalse();
        assertThat(SQLWriter.write().isNotEmpty("d")).isTrue();
        assertThat(SQLWriter.write().isNotEmpty("", "", "", "")).isFalse();
        assertThat(SQLWriter.write().isNotEmpty(param, param, param, param)).isFalse();

    }

    @Test
    public void testToStringForIn() throws Exception {
        List<String> l = new ArrayList<String>();
        l.add("1");
        assertThat(SQLWriter.toStringForIn(l)).isEqualTo("'1'");
        l.add("2");
        assertThat(SQLWriter.toStringForIn(l)).isEqualTo("'1','2'");
        l.add("3");
        assertThat(SQLWriter.toStringForIn(l)).isEqualTo("'1','2','3'");
    }

    @Test
    public void testAppend() throws Exception {
        assertThat(SQLWriter.write().append("(").toSql()).isEqualTo("(");
        assertThat(SQLWriter.write().append("(").append(")").toSql()).isEqualTo("( )");
    }

    @Test
    public void testAppendWithParams() throws Exception {
        // assertThat(SQLWriter.write().append("select * from", null).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().append("select * from", "").toSql()).isEqualTo("");
        assertThat(SQLWriter.write().append("select * from where t1 = '?'", "1").toSql()).isEqualTo(
                "select * from where t1 = '1'");

    }

    @Test
    public void testIsQueryEmpty() throws Exception {
        assertThat(SQLWriter.write().isQueryEmpty()).isTrue();
        assertThat(SQLWriter.write().append(" ").isQueryEmpty()).isFalse();

    }

    @Test
    public void testCountCharToReplaceZeroResult() throws Exception {
        assertThat(SQLWriter.write().countCharToReplace("")).isZero();
        assertThat(SQLWriter.write().countCharToReplace("select * from toto where t1=t2")).isZero();
    }

    @Test
    public void testCountCharToReplaceWitheResult() throws Exception {
        assertThat(SQLWriter.write().countCharToReplace("?")).isEqualTo(1);
        assertThat(SQLWriter.write().countCharToReplace("select * from toto where t1=? and t2 = ? and t3='?'"))
                .isEqualTo(3);
    }

    @Test
    public void testCheckMatchParmasZeroCharToReplaceOne() throws Exception {
        try {
            SQLWriter.write().checkMatchParams("?", 0);
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException e) {
            assertThat(e)
                    .hasMessage(
                            "Unabeld to replace the ? with parmas. The number (1) of the ? not match with the number of parmas (0)");
        }
    }

    @Test
    public void testCheckMatchParmasOneCharToReplaceFor() throws Exception {
        try {
            SQLWriter.write().checkMatchParams("?,?,?,?)", 1);
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException e) {
            assertThat(e)
                    .hasMessage(
                            "Unabeld to replace the ? with parmas. The number (4) of the ? not match with the number of parmas (1)");
        }
    }

    @Test
    public void testCheckMatchParmasOneCharToReplaceZero() throws Exception {
        try {
            SQLWriter.write().checkMatchParams("", 1);
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException e) {
            assertThat(e)
                    .hasMessage(
                            "Unabeld to replace the ? with parmas. The number (0) of the ? not match with the number of parmas (1)");
        }
    }

    @Test
    public void testCheckMatchParmasTowCharToReplaceOne() throws Exception {
        try {
            SQLWriter.write().checkMatchParams("?", 2);

            // SQLWriter.write("use ") + schema +";"

            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException e) {
            assertThat(e)
                    .hasMessage(
                            "Unabeld to replace the ? with parmas. The number (1) of the ? not match with the number of parmas (2)");
        }
    }

    @Test
    public void testToSql() throws Exception {
        assertThat(SQLWriter.write().append(" ? ", "toto").toSql()).isEqualTo(" toto ");
        assertThat(SQLWriter.write("schema").append(" ? ", "toto").toSql()).isEqualTo(" toto ");
    }

    @Test
    public void testEqual() throws Exception {
        assertThat(SQLWriter.write().and("tableName").equal("toto").toSql()).isEqualTo(" tableName='toto'");
    }

    @Test
    public void testCurrentIndex() throws Exception {
        assertThat(SQLWriter.write().append("12345").currentIndex()).isEqualTo(5);
    }

    @Test
    public void testRollback() throws Exception {
        assertThat(SQLWriter.write().and("toeosisu").rollback().toSql()).isEqualTo("");
        assertThat(SQLWriter.write().append("toeosisu").and("sjsjsksk").rollback().toSql()).isEqualTo("toeosisu");
    }

    @Test
    public void testInWithRollback() throws Exception {
        String id = null;
        assertThat(SQLWriter.write().and("toeosisu").in(id).and("sjsjsksk").equal(5).toSql()).isEqualTo(" sjsjsksk=5");
    }

    @Test
    public void testLike() throws Exception {
        assertThat(SQLWriter.write().and("COL").like("%toto").toSql()).isEqualTo(" COL like '%toto'");
    }

}
