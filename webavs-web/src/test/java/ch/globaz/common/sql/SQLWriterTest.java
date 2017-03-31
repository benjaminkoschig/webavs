package ch.globaz.common.sql;

import static org.fest.assertions.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
        assertThat(SQLWriter.write().where("field1 == '?'", Arrays.asList("p1")).toSql()).isEqualTo(
                " where field1 == 'p1'");
        assertThat(SQLWriter.write().where("field1 == '?' and field2 == '?'", "p1", "p2").toSql()).isEqualTo(
                " where field1 == 'p1' and field2 == 'p2'");
        assertThat(SQLWriter.write().where("field1 == '?' and field2 == '?'", Arrays.asList("p1", "p2")).toSql())
                .isEqualTo(" where field1 == 'p1' and field2 == 'p2'");
    }

    @Test
    public void testJoin() throws Exception {
        assertThat(SQLWriter.write().join("table1 on tabl1.id = table2.fk").toSql()).isEqualTo(
                " inner join table1 on tabl1.id = table2.fk");
        assertThat(SQLWriter.write().join(true, "table1 on tabl1.id = table2.fk").toSql()).isEqualTo(
                " inner join table1 on tabl1.id = table2.fk");
        assertThat(SQLWriter.write().join(false, "table1 on tabl1.id = table2.fk").toSql()).isEqualTo("");
    }

    @Test
    public void testLeftJoin() throws Exception {
        assertThat(SQLWriter.write().leftJoin("table1 on tabl1.id = table2.fk").toSql()).isEqualTo(
                " left join table1 on tabl1.id = table2.fk");
        assertThat(SQLWriter.write().leftJoin(true, "table1 on tabl1.id = table2.fk").toSql()).isEqualTo(
                " left join table1 on tabl1.id = table2.fk");
        assertThat(SQLWriter.write().leftJoin(false, "table1 on tabl1.id = table2.fk").toSql()).isEqualTo("");
    }

    @Test
    public void testRightJoin() throws Exception {
        assertThat(SQLWriter.write().rightJoin("table1 on tabl1.id = table2.fk").toSql()).isEqualTo(
                " right join table1 on tabl1.id = table2.fk");
        assertThat(SQLWriter.write().rightJoin(true, "table1 on tabl1.id = table2.fk").toSql()).isEqualTo(
                " right join table1 on tabl1.id = table2.fk");
        assertThat(SQLWriter.write().rightJoin(false, "table1 on tabl1.id = table2.fk").toSql()).isEqualTo("");
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
    public void testAndWithTableDefinition() throws Exception {
        assertThat(SQLWriter.write().and(TableDefTest.COL1).toSql()).isEqualTo(" schema.TABLE.COL1");
    }

    @Test
    public void testAndWithParam() throws Exception {
        String param = null;
        assertThat(SQLWriter.write().and("toto = ?", param).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().and("toto = ?", "kk").toSql()).isEqualTo(" toto = kk");
    }

    @Test
    public void testAndWithConditionAndParam() throws Exception {
        String param = null;
        assertThat(SQLWriter.write().and(false, "toto = ?", param).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().and(true, "toto = ?", param).toSql()).isEqualTo("");

        assertThat(SQLWriter.write().and(false, "toto = ?", "kk").toSql()).isEqualTo("");
        assertThat(SQLWriter.write().and(true, "toto = ?", "kk").toSql()).isEqualTo(" toto = kk");

    }

    @Test
    public void testAndWithParams() throws Exception {
        List<String> l = null;
        assertThat(SQLWriter.write().and("in (?)", l).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().and("in (?)", new ArrayList<String>()).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().and("in (?,?,?)", "3", "2", "1").toSql()).isEqualTo(" in (3,2,1)");
        l = new ArrayList<String>();
        l.add("1");
        l.add("2");
        l.add("3");
        assertThat(SQLWriter.write().and("in (?,?,?)", l).toSql()).isEqualTo(" in (1,2,3)");
    }

    @Test
    public void testAndWithParamsInteger() throws Exception {
        Integer i = null;
        assertThat(SQLWriter.write().and("in (?,?,?)", 3, 2, 1).toSql()).isEqualTo(" in (3,2,1)");
        assertThat(SQLWriter.write().and("in (?)", i).toSql()).isEqualTo("");
    }

    @Test
    public void testAndWithConditionAndParamsInteger() throws Exception {
        Integer i = null;
        assertThat(SQLWriter.write().and(false, "in (?,?,?)", 3, 2, 1).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().and(true, "in (?,?,?)", 3, 2, 1).toSql()).isEqualTo(" in (3,2,1)");

        assertThat(SQLWriter.write().and(false, "in (?)", i).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().and(true, "in (?)", i).toSql()).isEqualTo("");
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
        List<String> l = null;
        assertThat(SQLWriter.write().or("in (?)", l).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().or("in (?)", new ArrayList<String>()).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().or("in (?,?,?)", "3", "2", "1").toSql()).isEqualTo(" in (3,2,1)");
        l = new ArrayList<String>();
        l.add("1");
        l.add("2");
        l.add("3");
        assertThat(SQLWriter.write().or("in (?,?,?)", l).toSql()).isEqualTo(" in (1,2,3)");
    }

    @Test
    public void testOrWithParamsInteger() throws Exception {
        Integer i = null;
        assertThat(SQLWriter.write().or("in (?,?,?)", 3, 2, 1).toSql()).isEqualTo(" in (3,2,1)");
        assertThat(SQLWriter.write().or("in (?)", i).toSql()).isEqualTo("");
    }

    @Test
    public void testIsNotEmpty() throws Exception {
        String param = null;
        String[] p = new String[0];
        assertThat(SQLWriter.write().isNotEmpty(param)).isFalse();
        assertThat(SQLWriter.write().isNotEmpty(p)).isFalse();
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
        assertThat(SQLWriter.toStringForIn("1")).isEqualTo("'1'");
        assertThat(SQLWriter.toStringForIn("1", "2", "3")).isEqualTo("'1','2','3'");
    }

    @Test
    public void testToStringForInInteger() throws Exception {
        List<Integer> l = new ArrayList<Integer>();
        l.add(1);
        assertThat(SQLWriter.toStrForIn(l)).isEqualTo("1");
        l.add(2);
        assertThat(SQLWriter.toStrForIn(l)).isEqualTo("1,2");
        l.add(3);
        assertThat(SQLWriter.toStrForIn(l)).isEqualTo("1,2,3");
        assertThat(SQLWriter.toStringForIn(1, 2, 3)).isEqualTo("1,2,3");
    }

    @Test
    public void testInForString() throws Exception {
        List<String> l = new ArrayList<String>();
        assertThat(SQLWriter.write().inForString(l).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().inForString(null).toSql()).isEqualTo("");
        l.add("1");
        assertThat(SQLWriter.write().inForString(l).toSql()).isEqualTo(" in ('1')");
        l.add("2");
        assertThat(SQLWriter.write().inForString(l).toSql()).isEqualTo(" in ('1','2')");
        l.add("3");
        assertThat(SQLWriter.write().inForString(l).toSql()).isEqualTo(" in ('1','2','3')");
    }

    @Test
    public void testInWithList() throws Exception {
        List<Integer> l = null;
        assertThat(SQLWriter.write().in(l).toSql()).isEqualTo("");
        l = new ArrayList<Integer>();
        assertThat(SQLWriter.write().in(l).toSql()).isEqualTo("");

        l.add(1);
        assertThat(SQLWriter.write().in(l).toSql()).isEqualTo(" in (1)");
        l.add(2);
        assertThat(SQLWriter.write().in(l).toSql()).isEqualTo(" in (1,2)");
        l.add(3);
        assertThat(SQLWriter.write().in(l).toSql()).isEqualTo(" in (1,2,3)");
    }

    @Test
    public void testInWithString() throws Exception {
        String s = null;
        assertThat(SQLWriter.write().in(s).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().in("").toSql()).isEqualTo("");
        assertThat(SQLWriter.write().in("p").toSql()).isEqualTo(" in (p)");
        assertThat(SQLWriter.write().in("'t','t'").toSql()).isEqualTo(" in ('t','t')");
        assertThat(SQLWriter.write().in("'t','t'").toSql()).isEqualTo(" in ('t','t')");

    }

    @Test
    public void testAppend() throws Exception {
        assertThat(SQLWriter.write().append("(").toSql()).isEqualTo("(");
        assertThat(SQLWriter.write().append("(").append(")").toSql()).isEqualTo("( )");
        assertThat(SQLWriter.write().append(true, "(").toSql()).isEqualTo("(");
        assertThat(SQLWriter.write().append(false, "(").toSql()).isEqualTo("");
    }

    @Test
    public void testAppendWithParams() throws Exception {
        String s = null;
        Integer i = null;

        // assertThat(SQLWriter.write().append("select * from", null).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().append("select * from", s).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().append("select * from", i).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().append("select * from t = ?", 2).toSql()).isEqualTo("select * from t = 2");

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
    public void testEqualString() throws Exception {
        String n = null;
        assertThat(SQLWriter.write().and("col").equal("toto").toSql()).isEqualTo(" col='toto'");
        assertThat(SQLWriter.write().and("col").equal(n).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().and("col").equal("'P\"").toSql()).isEqualTo(" col='¬P¢'");
    }

    @Test
    public void testEqualInteger() throws Exception {
        Integer n = null;
        assertThat(SQLWriter.write().and("col").equal(1).toSql()).isEqualTo(" col=1");
        assertThat(SQLWriter.write().and("col").equal(n).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().and("col").equal("").toSql()).isEqualTo("");
    }

    @Test
    public void testEqualCodeSystem() throws Exception {
        CodeSystemTest test = null;
        assertThat(SQLWriter.write().and("col").equal(CodeSystemTest.TEST_1).toSql()).isEqualTo(" col=1");
        assertThat(SQLWriter.write().and("col").equal(CodeSystemTest.TEST_2).toSql()).isEqualTo(" col=2");
        assertThat(SQLWriter.write().and("col").equal(test).toSql()).isEqualTo("");
    }

    @Test
    public void testEqualForDate() throws Exception {
        String date = null;
        assertThat(SQLWriter.write().and("col").equalForDate(date).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().and("col").equalForDate("").toSql()).isEqualTo("");
        assertThat(SQLWriter.write().and("col").equalForDate("10.2015").toSql()).isEqualTo(" col=201510");
        assertThat(SQLWriter.write().and("col").equalForDate("01.10.2015").toSql()).isEqualTo(" col=20151001");
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
        String s = null;
        assertThat(SQLWriter.write().and("COL").like(s).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().and("COL").like("").toSql()).isEqualTo("");
        assertThat(SQLWriter.write().and("COL").like("%toto%").toSql()).isEqualTo(" COL like '%toto%'");
        assertThat(SQLWriter.write().and("COL").like("%'P\"%").toSql()).isEqualTo(" COL like '%¬P¢%'");
    }

    @Test
    public void testMax() throws Exception {
        assertThat(SQLWriter.write().select().max("TOTO").toSql()).isEqualTo("select max(TOTO) ");
    }

    @Test
    public void testMaxtTableDefinition() throws Exception {
        assertThat(SQLWriter.write().select().max(TableDefTest.COL1).toSql()).isEqualTo(
                "select max(schema.TABLE.COL1) ");
    }

    @Test
    public void testWithSchema() throws Exception {
        assertThat(SQLWriter.writeWithSchema().select().max("TOTO").toSql()).isEqualTo("select max(schema.TOTO) ");
    }

    @Test
    public void testFullLike() throws Exception {
        String s = null;
        assertThat(SQLWriter.writeWithSchema().and("COL").fullLike("").toSql()).isEqualTo("");
        assertThat(SQLWriter.writeWithSchema().and("COL").fullLike(s).toSql()).isEqualTo("");
        assertThat(SQLWriter.writeWithSchema().and("COL").fullLike("P").toSql()).isEqualTo(" schema.COL like '%P%'");
        assertThat(SQLWriter.writeWithSchema().and("COL").fullLike("'P\"").toSql()).isEqualTo(
                " schema.COL like '%¬P¢%'");
    }

    @Test
    public void testIsNullOrZero() throws Exception {
        assertThat(SQLWriter.write().isNullOrZero("COL").toSql()).isEqualTo(" (COL IS NULL OR COL=0)");
    }

    @Test
    public void testIsNotNullOrZero() throws Exception {
        assertThat(SQLWriter.write().isNotNullOrZero("COL").toSql()).isEqualTo(" (COL IS NOT NULL AND COL<>0)");
    }

    @Test
    public void testDatToGlobazFormat() throws Exception {
        assertThat(SQLWriter.write().datToGlobazFormat("10.2015")).isEqualTo(201510);
        assertThat(SQLWriter.write().datToGlobazFormat("2.2015")).isEqualTo(201502);
        assertThat(SQLWriter.write().datToGlobazFormat("01.10.2015")).isEqualTo(20151001);
        assertThat(SQLWriter.write().datToGlobazFormat("1.10.2015")).isEqualTo(20151001);
    }

    @Test
    public void testFields() throws Exception {
        assertThat(SQLWriter.write().fields("test", TableDefTest.class).toSql()).isEqualTo(
                " TEST.COL1 AS TEST_COL1, TEST.COL2 AS TEST_COL2");
    }

    @Test
    public void testFieldsWithSpys() throws Exception {
        assertThat(SQLWriter.write().fields("test", TableDefTestWithSpys.class).toSql()).isEqualTo(
                " TEST.COL1 AS TEST_COL1, TEST.COL2 AS TEST_COL2, TEST.CSPY AS TEST_CSPY, TEST.PSPY AS TEST_PSPY");
    }

    @Test
    public void testEqualForNumber() throws Exception {
        assertThat(SQLWriter.write().and("col").equalForNumber("1").toSql()).isEqualTo(" col=1");
        assertThat(SQLWriter.write().and("col").equalForNumber(null).toSql()).isEqualTo("");
        assertThat(SQLWriter.write().and("col").equalForNumber("").toSql()).isEqualTo("");
    }

    @Test
    public void testCurrentIndexAfterOperator() throws Exception {
        assertThat(
                SQLWriter.write().select().fields("*").from("TABLE").where().and("col").equalForNumber("1").and("col2")
                        .equalForNumber("2").currentIndexAfterOperator()).isEqualTo(43);
    }

    @Test
    public void testFullLikeCaseInsensitive() throws Exception {
        String s = null;
        assertThat(SQLWriter.writeWithSchema().locale(Locale.FRENCH).and("COL").fullLikeCaseInsensitive("").toSql())
                .isEqualTo("");
        assertThat(SQLWriter.writeWithSchema().locale(Locale.FRENCH).and("COL").fullLikeCaseInsensitive(s).toSql())
                .isEqualTo("");
        assertThat(SQLWriter.writeWithSchema().locale(Locale.FRENCH).and("COL").fullLikeCaseInsensitive("P").toSql())
                .isEqualTo(" UPPER( schema.COL)  like '%P%'");
        assertThat(SQLWriter.writeWithSchema().locale(Locale.FRENCH).and("COL").fullLikeCaseInsensitive("Rose").toSql())
                .isEqualTo(" UPPER( schema.COL)  like '%ROSE%'");
        try {
            SQLWriter.writeWithSchema().and("COL").fullLikeCaseInsensitive("Rose").toSql();
        } catch (RuntimeException e) {
            assertThat(e).isInstanceOf(SQLWriterException.class);
            assertThat(e).hasMessage(
                    "Unabled to perform a full like case insensitve without a Locale. Please use locale(...) function");
        }
    }

}
