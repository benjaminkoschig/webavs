package globaz.corvus.acor2020.parser;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class REConverterUtilsTest {

    @Test
    public void formatIntToStringWithTwoChar_avecParametreNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = REConverterUtils.formatIntToStringWithTwoChar(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatIntToStringWithTwoChar_avecChiffre_renvoiStringSur2Caracteres() {
        // arrange
        // act
        String result = REConverterUtils.formatIntToStringWithTwoChar(1);

        // assert
        assertThat(result).isEqualTo("01");
    }

    @Test
    public void formatIntToStringWithTwoChar_avecNbInf100_renvoiStringSur2Caracteres() {
        // arrange
        // act
        String result = REConverterUtils.formatIntToStringWithTwoChar(20);

        // assert
        assertThat(result).isEqualTo("20");
    }

    @Test
    public void formatIntToStringWithTwoChar_avecNbSup100_renvoiStringSur3Caracteres() {
        // arrange
        // act
        String result = REConverterUtils.formatIntToStringWithTwoChar(100);

        // assert
        assertThat(result).isEqualTo("100");
    }

    @Test
    public void formatIntToStringWithSixChar_avecParametreNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = REConverterUtils.formatIntToStringWithSixChar(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatIntToStringWithSixChar_avecNbInf100000_renvoiStringSur6Caracteres() {
        // arrange
        // act
        String result = REConverterUtils.formatIntToStringWithSixChar(100);

        // assert
        assertThat(result).isEqualTo("000100");
    }

    @Test
    public void formatIntToStringWithSixChar_avecNbSup1000000_renvoiStringSur7Caracteres() {
        // arrange
        // act
        String result = REConverterUtils.formatIntToStringWithSixChar(2000000);

        // assert
        assertThat(result).isEqualTo("2000000");
    }

    @Test
    public void formatFloatToStringWithTwoDecimal_avecParametreNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = REConverterUtils.formatFloatToStringWithTwoDecimal(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatFloatToStringWithTwoDecimal_avecChiffre_renvoiStringAuFormatXX_XX() {
        // arrange
        // act
        String result = REConverterUtils.formatFloatToStringWithTwoDecimal(1f);

        // assert
        assertThat(result).isEqualTo("01.00");
    }

    @Test
    public void formatFloatToStringWithTwoDecimal_avecFloatXXX_X_renvoiStringAuFormatXXX_XX() {
        // arrange
        // act
        String result = REConverterUtils.formatFloatToStringWithTwoDecimal(111.1f);

        // assert
        assertThat(result).isEqualTo("111.10");
    }

    @Test
    public void formatFloatToStringWithTwoDecimal_avecFloatXX_XXX_renvoiStringAuFormatXX_XX() {
        // arrange
        // act
        String result = REConverterUtils.formatFloatToStringWithTwoDecimal(11.111f);

        // assert
        assertThat(result).isEqualTo("11.11");
    }


    @Test
    public void formatRequiredLong_avecNull_renvoi0() {
        // arrange
        // act
        long result = REConverterUtils.formatRequiredLong(null);

        // assert
        assertThat(result).isEqualTo(0l);
    }

    @Test
    public void formatRequiredLong_avecStringNonConvertible_renvoi0() {
        // arrange
        // act
        long result = REConverterUtils.formatRequiredLong("test");

        // assert
        assertThat(result).isEqualTo(0l);
    }

    @Test
    public void formatRequiredLong_avecNombre_renvoiNombre() {
        // arrange
        // act
        long result = REConverterUtils.formatRequiredLong("10");

        // assert
        assertThat(result).isEqualTo(10l);
    }

    @Test
    public void formatRequiredInteger_avecNull_renvoi0() {
        // arrange
        // act
        int result = REConverterUtils.formatRequiredInteger(null);

        // assert
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void formatRequiredInteger_avecStringNonConvertible_renvoi0() {
        // arrange
        // act
        int result = REConverterUtils.formatRequiredInteger("test");

        // assert
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void formatRequiredInteger_avecNombre_renvoiNombre() {
        // arrange
        // act
        int result = REConverterUtils.formatRequiredInteger("10");

        // assert
        assertThat(result).isEqualTo(10);
    }

    @Test
    public void formatRequiredBigDecimal_avecNull_renvoi0() {
        // arrange
        // act
        BigDecimal result = REConverterUtils.formatRequiredBigDecimal(null);

        // assert
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void formatRequiredBigDecimal_avecStringNonConvertible_renvoi0() {
        // arrange
        // act
        BigDecimal result = REConverterUtils.formatRequiredBigDecimal("test");

        // assert
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void formatRequiredBigDecimal_avecNombre_renvoiNombre() {
        // arrange
        // act
        BigDecimal result = REConverterUtils.formatRequiredBigDecimal("10");

        // assert
        assertThat(result).isEqualTo(new BigDecimal(10));
    }

    @Test
    public void formatRequiredBigDecimalDuree_avecNull_renvoi0() {
        // arrange
        // act
        BigDecimal result = REConverterUtils.formatRequiredBigDecimalDuree(null);

        // assert
        assertThat(result).isEqualTo(new BigDecimal("00.00"));
    }

    @Test
    public void formatRequiredBigDecimalDuree_avecStringNonConvertible_renvoi0() {
        // arrange
        // act
        BigDecimal result = REConverterUtils.formatRequiredBigDecimalDuree("test");

        // assert
        assertThat(result).isEqualTo(new BigDecimal("00.00"));
    }

    @Test
    public void formatRequiredBigDecimalDuree_avecChiffre_renvoiChiffre() {
        // arrange
        // act
        BigDecimal result = REConverterUtils.formatRequiredBigDecimalDuree("1");

        // assert
        assertThat(result).isEqualTo(new BigDecimal("1"));
    }

    @Test
    public void formatRequiredBigDecimalNoDecimal_avecNull_renvoi0() {
        // arrange
        // act
        BigDecimal result = REConverterUtils.formatRequiredBigDecimalNoDecimal(null);

        // assert
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void formatRequiredBigDecimalNoDecimal_avecStringNonConvertible_renvoi0() {
        // arrange
        // act
        BigDecimal result = REConverterUtils.formatRequiredBigDecimalNoDecimal("test");

        // assert
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void formatRequiredBigDecimalNoDecimal_avecDecimalX_X_renvoiDecimalX() {
        // arrange
        // act
        BigDecimal result = REConverterUtils.formatRequiredBigDecimalNoDecimal("1.2");

        // assert
        assertThat(result).isEqualTo(new BigDecimal("1"));
    }

    @Test
    public void formatRequiredShort_avecNull_renvoi0() {
        // arrange
        // act
        short result = REConverterUtils.formatRequiredShort(null);

        // assert
        assertThat(result).isEqualTo((short) 0);
    }

    @Test
    public void formatRequiredShort_avecStringNonConvertible_renvoi0() {
        // arrange
        // act
        short result = REConverterUtils.formatRequiredShort("test");

        // assert
        assertThat(result).isEqualTo((short) 0);
    }

    @Test
    public void formatRequiredShort_avecNb_renvoiNb() {
        // arrange
        // act
        short result = REConverterUtils.formatRequiredShort("20");

        // assert
        assertThat(result).isEqualTo((short) 20);
    }

    @Test
    public void formatOptionalShort_avecNull_renvoiNull() {
        // arrange
        // act
        Short result = REConverterUtils.formatOptionalShort(null);

        // assert
        assertThat(result).isNull();
    }

    @Test
    public void formatOptionalShort_avecStringNonConvertible_renvoiNull() {
        // arrange
        // act
        Short result = REConverterUtils.formatOptionalShort("test");

        // assert
        assertThat(result).isNull();
    }

    @Test
    public void formatOptionalShort_avecNb_renvoiNb() {
        // arrange
        // act
        Short result = REConverterUtils.formatOptionalShort("20");

        // assert
        assertThat(result).isEqualTo(new Short("20"));
    }

    @Test
    public void formatDate_avecNull_renvoiNull() {
        // arrange
        String format = "dd.MM.yyyy";
        // act
        XMLGregorianCalendar result = REConverterUtils.formatDate(null, format);

        // assert
        assertThat(result).isNull();
    }

    @Test
    public void formatDate_avecStringNonConvertible_renvoiNull() {
        // arrange
        String format = "dd.MM.yyyy";
        // act
        XMLGregorianCalendar result = REConverterUtils.formatDate("12.02", format);

        // assert
        assertThat(result).isNull();
    }

    @Test
    public void formatDate_dateDD_MM_YYYY_renvoiDate() throws DatatypeConfigurationException {
        // arrange
        String format = "dd.MM.yyyy";
        XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(2021,2,12,0,0,0,0, 60);

        // act
        XMLGregorianCalendar result = REConverterUtils.formatDate("12.02.2021", format);

        // assert
        assertThat(result).isEqualTo(date);
    }

    @Test
    public void formatDateToAAAAMM_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = REConverterUtils.formatDateToAAAAMM(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatDateToAAAAMM_avecDateFevrier_renvoiDateAAAAMM() throws DatatypeConfigurationException {
        // arrange
        XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(2021,2,26,0,0,0,0, 60);

        // act
        String result = REConverterUtils.formatDateToAAAAMM(date);

        // assert
        assertThat(result).isEqualTo("202102");
    }

    @Test
    public void formatDateToAAAAMM_avecDateDecembre_renvoiDateAAAAMM() throws DatatypeConfigurationException {
        // arrange
        XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(2021,12,26,0,0,0,0, 60);

        // act
        String result = REConverterUtils.formatDateToAAAAMM(date);

        // assert
        assertThat(result).isEqualTo("202112");
    }

    @Test
    public void formatDateToMMAA_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = REConverterUtils.formatDateToMMAA(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatDateToMMAA_avecDateFevrier_renvoiDateMMAA() throws DatatypeConfigurationException {
        // arrange
        XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(2021,2,26,0,0,0,0, 60);

        // act
        String result = REConverterUtils.formatDateToMMAA(date);

        // assert
        assertThat(result).isEqualTo("0221");
    }

    @Test
    public void formatDateToMMAA_avecDateDecembre_renvoiDateMMAA() throws DatatypeConfigurationException {
        // arrange
        XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(2021,12,26,0,0,0,0, 60);

        // act
        String result = REConverterUtils.formatDateToMMAA(date);

        // assert
        assertThat(result).isEqualTo("1221");
    }

    @Test
    public void formatNssToLong_avecNull_renvoi0() {
        // arrange
        // act
        long result = REConverterUtils.formatNssToLong(null);

        // assert
        assertThat(result).isEqualTo(0l);
    }

    @Test
    public void formatNssToLong_avecDateDecembre_renvoiDateMMAA() {
        // arrange
        // act
        long result = REConverterUtils.formatNssToLong("756.000.000.00");

        // assert
        assertThat(result).isEqualTo(75600000000l);
    }

    @Test
    public void formatAAAAtoAA_avecStringEmpty_renvoiStringEmpty() {
        // arrange
        // act
        String result = REConverterUtils.formatAAAAtoAA(StringUtils.EMPTY);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatAAAAtoAA_avecAnneeAAAA_renvoiAnneeAA() {
        // arrange
        // act
        String result = REConverterUtils.formatAAAAtoAA("2021");

        // assert
        assertThat(result).isEqualTo("21");
    }

    @Test
    public void formatAAAAtoAA_avecAnneeAA_renvoiStringEmpty() {
        // arrange
        // act
        String result = REConverterUtils.formatAAAAtoAA("21");

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatMMtoAAxMM_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = REConverterUtils.formatMMtoAAxMM(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatMMtoAAxMM_avecNbMoisSupA12_renvoiDateAAxMM() {
        // arrange
        // act
        String result = REConverterUtils.formatMMtoAAxMM(23);

        // assert
        assertThat(result).isEqualTo("01.11");
    }

    @Test
    public void formatMMtoAAxMM_avecNbMoisInfA12_renvoiDateAAxMM() {
        // arrange
        // act
        String result = REConverterUtils.formatMMtoAAxMM(9);

        // assert
        assertThat(result).isEqualTo("00.09");
    }

    @Test
    public void formatMMtoAAxMM_avec12mois_renvoiDateAAxMM() {
        // arrange
        // act
        String result = REConverterUtils.formatMMtoAAxMM(12);

        // assert
        assertThat(result).isEqualTo("01.00");
    }

    @Test
    public void formatAAMMtoAAxMM_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = REConverterUtils.formatAAMMtoAAxMM(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatAAMMtoAAxMM_avecIntXXXX_renvoiDateAAxMM() {
        // arrange
        // act
        String result = REConverterUtils.formatAAMMtoAAxMM(1109);

        // assert
        assertThat(result).isEqualTo("11.09");
    }

    @Test
    public void formatAAMMtoAAxMM_avecIntAMM_renvoiDateAAxMM() {
        // arrange
        // act
        String result = REConverterUtils.formatAAMMtoAAxMM(406);

        // assert
        assertThat(result).isEqualTo("04.06");
    }

    @Test
    public void formatAAMMtoAAxMM_avecIntXX_renvoiStringEmpty() {
        // arrange
        // act
        String result = REConverterUtils.formatAAMMtoAAxMM(11);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatMMtoAxMM_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = REConverterUtils.formatMMtoAxMM(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatMMtoAxMM_avecMoins12Mois_renvoiDateAxMM() {
        // arrange
        // act
        String result = REConverterUtils.formatMMtoAxMM(9);

        // assert
        assertThat(result).isEqualTo("0.09");
    }

    @Test
    public void formatMMtoAxMM_avecPlus12Mois_renvoiDateAxMM() {
        // arrange
        // act
        String result = REConverterUtils.formatMMtoAxMM(21);

        // assert
        assertThat(result).isEqualTo("1.09");
    }

    @Test
    public void formatMMtoAxMM_avec12Mois_renvoiDateAxMM() {
        // arrange
        // act
        String result = REConverterUtils.formatMMtoAxMM(12);

        // assert
        assertThat(result).isEqualTo("1.00");
    }

    @Test
    public void convertMMtoA_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = REConverterUtils.convertMMtoA(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void convertMMtoA_avecMoins12Mois_renvoiNbAnnee() {
        // arrange
        // act
        String result = REConverterUtils.convertMMtoA(9);

        // assert
        assertThat(result).isEqualTo("0");
    }

    @Test
    public void convertMMtoA_avecPlus12Mois_renvoiNbAnnee() {
        // arrange
        // act
        String result = REConverterUtils.convertMMtoA(24);

        // assert
        assertThat(result).isEqualTo("2");
    }

    @Test
    public void formatBigDecimalToString_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = REConverterUtils.formatBigDecimalToString(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatBigDecimalToString_avecBigDecimal_renvoiNb() {
        // arrange
        // act
        String result = REConverterUtils.formatBigDecimalToString(new BigDecimal("9.1"));

        // assert
        assertThat(result).isEqualTo("9.1");
    }

    @Test
    public void formatBooleanToString_avecNull_renvoiNull() {
        // arrange
        // act
        String result = REConverterUtils.formatBooleanToString(null);

        // assert
        assertThat(result).isNull();
    }

    @Test
    public void formatBooleanToString_avecTrue_renvoi1() {
        // arrange
        // act
        String result = REConverterUtils.formatBooleanToString(Boolean.TRUE);

        // assert
        assertThat(result).isEqualTo("1");
    }

    @Test
    public void formatBooleanToString_avecFalse_renvoi0() {
        // arrange
        // act
        String result = REConverterUtils.formatBooleanToString(Boolean.FALSE);

        // assert
        assertThat(result).isEqualTo("0");
    }

    @Test
    public void formatIntegerToString_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = REConverterUtils.formatIntegerToString(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatIntegerToString_avecInteger_renvoiNb() {
        // arrange
        // act
        String result = REConverterUtils.formatIntegerToString(20);

        // assert
        assertThat(result).isEqualTo("20");
    }

    @Test
    public void formatShortToString_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = REConverterUtils.formatShortToString(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatShortToString_avecShort_renvoiNb() {
        // arrange
        // act
        String result = REConverterUtils.formatShortToString((short) 20);

        // assert
        assertThat(result).isEqualTo("20");
    }

    @Test
    public void formatCodeCasSpecial_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = REConverterUtils.formatCodeCasSpecial(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatCodeCasSpecial_avecCode1Chiffre_renvoiNb2Champs() {
        // arrange
        // act
        String result = REConverterUtils.formatCodeCasSpecial((short) 9);

        // assert
        assertThat(result).isEqualTo("09");
    }

    @Test
    public void formatCodeCasSpecial_avecCode2Chiffres_renvoiNb2Champs() {
        // arrange
        // act
        String result = REConverterUtils.formatCodeCasSpecial((short) 11);

        // assert
        assertThat(result).isEqualTo("11");
    }

    @Test
    public void formatStringWithoutDots_avecNumeroNull_renvoiStringEmtpy() {
        // arrange
        // act
        String result = REConverterUtils.formatStringWithoutDots(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatStringWithoutDots_avecNumeroAvecPoints_renvoiNumeroSansPoint() {
        // arrange
        // act
        String result = REConverterUtils.formatStringWithoutDots("756.000.000.00");

        // assert
        assertThat(result).isEqualTo("75600000000");

    }

}