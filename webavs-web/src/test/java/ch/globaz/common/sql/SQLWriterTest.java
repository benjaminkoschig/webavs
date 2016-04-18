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
    public void testReplace() throws Exception {
        assertThat(SQLWriter.write().replace("toto = ? and tata = ?", "kk", "bb")).isEqualTo("toto = kk and tata = bb");
    }

    @Test
    public void testIsNotEmpty() throws Exception {
        String param = null;
        assertThat(SQLWriter.write().isNotEmpty(param)).isFalse();
        assertThat(SQLWriter.write().isNotEmpty("")).isFalse();
        assertThat(SQLWriter.write().isNotEmpty("d")).isTrue();
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
    public void testIsQueryEmpty() throws Exception {
        assertThat(SQLWriter.write().isQueryEmpty()).isTrue();
        assertThat(SQLWriter.write().append(" ").isQueryEmpty()).isFalse();

    }

}
