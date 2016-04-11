package ch.globaz.common.sql;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;

public class SQLWriterTest {

    @Test
    public void testSelect() throws Exception {
        assertThat(SQLWriter.newWriter().select().toSql()).isEqualTo("select");
    }

    @Test
    public void testSelectFields() throws Exception {
        assertThat(SQLWriter.newWriter().select("field1, field2").toSql()).isEqualTo("select field1, field2");
    }

    @Test
    public void testFrom() throws Exception {
        assertThat(SQLWriter.newWriter().from("table").toSql()).isEqualTo(" from table");
    }

    @Test
    public void testWhere() throws Exception {
        assertThat(SQLWriter.newWriter().where("field1 == field2").toSql()).isEqualTo(" where field1 == field2");
    }

    @Test
    public void testWhereWithParam() throws Exception {
        assertThat(SQLWriter.newWriter().where("field1 == ? or filed2 = ?", "param").toSql()).isEqualTo(
                " where field1 == param or filed2 = param");
    }

    @Test
    public void testJoin() throws Exception {
        assertThat(SQLWriter.newWriter().join("table1 on tabl1.id = table2.fk").toSql()).isEqualTo(
                " inner join table1 on tabl1.id = table2.fk");
    }

    @Test
    public void testReplaceSchema() throws Exception {
        assertThat(
                SQLWriter.newWriter("hl.").from("schema.table2 as t2").join("schema.table1 as t1 on t1.id = t2.fk")
                        .toSql()).isEqualTo(" from hl.table2 as t2 inner join hl.table1 as t1 on t1.id = t2.fk");
    }

    @Test
    public void testAnd() throws Exception {
        assertThat(SQLWriter.newWriter().and("toto = tata").toSql()).isEqualTo(" toto = tata");
        assertThat(SQLWriter.newWriter().where().and("toto = tata").toSql()).isEqualTo(" where toto = tata");
        assertThat(SQLWriter.newWriter().where("t1=t2").and("toto = tata").toSql()).isEqualTo(
                " where t1=t2 and toto = tata");
    }

    @Test
    public void testAndWithParam() throws Exception {
        assertThat(SQLWriter.newWriter().and("toto = ?", null).toSql()).isEqualTo("");
        assertThat(SQLWriter.newWriter().and("toto = ?", "kk").toSql()).isEqualTo(" toto = kk");
    }

    @Test
    public void testReplaceParam() throws Exception {
        assertThat(SQLWriter.newWriter().replaceParam("toto = ? and tata = ?", "kk")).isEqualTo(
                "toto = kk and tata = kk");
    }

    @Test
    public void testIsNotEmpty() throws Exception {
        assertThat(SQLWriter.newWriter().isNotEmpty(null)).isFalse();
        assertThat(SQLWriter.newWriter().isNotEmpty("")).isFalse();
        assertThat(SQLWriter.newWriter().isNotEmpty("d")).isTrue();
    }
}
