package ch.globaz.corvus.process.dnra;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.DateRente;

public class CsvLineParserTest {

    @Test
    public void testNextTockenToDateEmptyValue() throws Exception {
        CsvLineParser csvLineParser = new CsvLineParser(" ");
        assertThat(csvLineParser.nextElementToDateRente("testField")).isNull();
    }

    @Test
    public void testNextTockenToDate() throws Exception {
        CsvLineParser csvLineParser = new CsvLineParser("31122015; ");
        assertThat(csvLineParser.nextElementToDateRente("testField")).isEqualTo(
                new DateRente("31.12.2015", Date.DATE_PATTERN_SWISS));
        assertThat(csvLineParser.nextElementToDateRente("testField")).isNull();
    }

    @Test
    public void testNextTokenNull() throws Exception {
        CsvLineParser csvLineParser = new CsvLineParser(" ");
        assertThat(csvLineParser.nextElementTrim()).isNull();
    }

    @Test
    public void testNextElementTrimMultipe() throws Exception {
        // ;;1;0;;1;10071980;249;;;;;
        CsvLineParser csvLineParser = new CsvLineParser("1;;;2");
        assertThat(csvLineParser.nextElementTrim()).isEqualTo("1");
        assertThat(csvLineParser.nextElementTrim()).isNull();
        assertThat(csvLineParser.nextElementTrim()).isNull();
        assertThat(csvLineParser.nextElementTrim()).isEqualTo("2");
    }

    @Test
    public void testNextElementTrimMultipeEmpty() throws Exception {
        CsvLineParser csvLineParser = new CsvLineParser("1; ; ;2");
        assertThat(csvLineParser.nextElementTrim()).isEqualTo("1");
        assertThat(csvLineParser.nextElementTrim()).isNull();
        assertThat(csvLineParser.nextElementTrim()).isNull();
        assertThat(csvLineParser.nextElementTrim()).isEqualTo("2");
    }

    @Test
    public void testFormatNss() throws Exception {
        CsvLineParser csvLineParser = new CsvLineParser(
                "63349158584;7569849984078;1;1;MARTINOVIC,JOZO;1;27021949;220;;;;;");
        assertThat(csvLineParser.nextElementToNssFormate()).isEqualTo("63349158584");
        assertThat(csvLineParser.nextElementToNssFormate()).isEqualTo("756.9849.9840.78");
    }

    // @Test
    // public void testToDateRente() throws Exception {
    // assertThat(CsvLineParser.toDateRente("16101900", "").getSwissValue()).isEqualTo("1900.01.16");
    // }
}
