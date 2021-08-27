package globaz.prestation.acor.acor2020.mapper;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class PRConverterUtilsTest {

    @Test
    public void formatIntToStringWithTwoChar_avecParametreNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = PRConverterUtils.formatIntToStringWithTwoChar(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatIntToStringWithTwoChar_avecChiffre_renvoiStringSur2Caracteres() {
        // arrange
        // act
        String result = PRConverterUtils.formatIntToStringWithTwoChar(1);

        // assert
        assertThat(result).isEqualTo("01");
    }

    @Test
    public void formatIntToStringWithTwoChar_avecNbInf100_renvoiStringSur2Caracteres() {
        // arrange
        // act
        String result = PRConverterUtils.formatIntToStringWithTwoChar(20);

        // assert
        assertThat(result).isEqualTo("20");
    }

    @Test
    public void formatIntToStringWithTwoChar_avecNbSup100_renvoiStringSur3Caracteres() {
        // arrange
        // act
        String result = PRConverterUtils.formatIntToStringWithTwoChar(100);

        // assert
        assertThat(result).isEqualTo("100");
    }

    @Test
    public void formatIntToStringWithSixChar_avecParametreNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = PRConverterUtils.formatIntToStringWithSixChar(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatIntToStringWithSixChar_avecNbInf100000_renvoiStringSur6Caracteres() {
        // arrange
        // act
        String result = PRConverterUtils.formatIntToStringWithSixChar(100);

        // assert
        assertThat(result).isEqualTo("000100");
    }

    @Test
    public void formatIntToStringWithSixChar_avecNbSup1000000_renvoiStringSur7Caracteres() {
        // arrange
        // act
        String result = PRConverterUtils.formatIntToStringWithSixChar(2000000);

        // assert
        assertThat(result).isEqualTo("2000000");
    }

    @Test
    public void formatFloatToStringWithTwoDecimal_avecParametreNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = PRConverterUtils.formatFloatToStringWithTwoDecimal(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatFloatToStringWithTwoDecimal_avecChiffre_renvoiStringAuFormatXX_XX() {
        // arrange
        // act
        String result = PRConverterUtils.formatFloatToStringWithTwoDecimal(1f);

        // assert
        assertThat(result).isEqualTo("01.00");
    }

    @Test
    public void formatFloatToStringWithTwoDecimal_avecFloatXXX_X_renvoiStringAuFormatXXX_XX() {
        // arrange
        // act
        String result = PRConverterUtils.formatFloatToStringWithTwoDecimal(111.1f);

        // assert
        assertThat(result).isEqualTo("111.10");
    }

    @Test
    public void formatFloatToStringWithTwoDecimal_avecFloatXX_XXX_renvoiStringAuFormatXX_XX() {
        // arrange
        // act
        String result = PRConverterUtils.formatFloatToStringWithTwoDecimal(11.111f);

        // assert
        assertThat(result).isEqualTo("11.11");
    }


    @Test
    public void formatRequiredLong_avecNull_renvoi0() {
        // arrange
        // act
        long result = PRConverterUtils.formatRequiredLong(null);

        // assert
        assertThat(result).isEqualTo(0l);
    }

    @Test
    public void formatRequiredLong_avecStringNonConvertible_renvoi0() {
        // arrange
        // act
        long result = PRConverterUtils.formatRequiredLong("test");

        // assert
        assertThat(result).isEqualTo(0l);
    }

    @Test
    public void formatRequiredLong_avecNombre_renvoiNombre() {
        // arrange
        // act
        long result = PRConverterUtils.formatRequiredLong("10");

        // assert
        assertThat(result).isEqualTo(10l);
    }

    @Test
    public void formatRequiredInteger_avecNull_renvoi0() {
        // arrange
        // act
        int result = PRConverterUtils.formatRequiredInteger(null);

        // assert
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void formatRequiredInteger_avecStringNonConvertible_renvoi0() {
        // arrange
        // act
        int result = PRConverterUtils.formatRequiredInteger("test");

        // assert
        assertThat(result).isEqualTo(0);
    }

    @Test
    public void formatRequiredInteger_avecNombre_renvoiNombre() {
        // arrange
        // act
        int result = PRConverterUtils.formatRequiredInteger("10");

        // assert
        assertThat(result).isEqualTo(10);
    }

    @Test
    public void formatRequiredBigDecimal_avecNull_renvoi0() {
        // arrange
        // act
        BigDecimal result = PRConverterUtils.formatRequiredBigDecimal(null);

        // assert
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void formatRequiredBigDecimal_avecStringNonConvertible_renvoi0() {
        // arrange
        // act
        BigDecimal result = PRConverterUtils.formatRequiredBigDecimal("test");

        // assert
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void formatRequiredBigDecimal_avecNombre_renvoiNombre() {
        // arrange
        // act
        BigDecimal result = PRConverterUtils.formatRequiredBigDecimal("10");

        // assert
        assertThat(result).isEqualTo(new BigDecimal(10));
    }

    @Test
    public void formatRequiredBigDecimalDuree_avecNull_renvoi0() {
        // arrange
        // act
        BigDecimal result = PRConverterUtils.formatRequiredBigDecimalDuree(null);

        // assert
        assertThat(result).isEqualTo(new BigDecimal("00.00"));
    }

    @Test
    public void formatRequiredBigDecimalDuree_avecStringNonConvertible_renvoi0() {
        // arrange
        // act
        BigDecimal result = PRConverterUtils.formatRequiredBigDecimalDuree("test");

        // assert
        assertThat(result).isEqualTo(new BigDecimal("00.00"));
    }

    @Test
    public void formatRequiredBigDecimalDuree_avecChiffre_renvoiChiffre() {
        // arrange
        // act
        BigDecimal result = PRConverterUtils.formatRequiredBigDecimalDuree("1");

        // assert
        assertThat(result).isEqualTo(new BigDecimal("1"));
    }

    @Test
    public void formatRequiredBigDecimalNoDecimal_avecNull_renvoi0() {
        // arrange
        // act
        BigDecimal result = PRConverterUtils.formatRequiredBigDecimalNoDecimal(null);

        // assert
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void formatRequiredBigDecimalNoDecimal_avecStringNonConvertible_renvoi0() {
        // arrange
        // act
        BigDecimal result = PRConverterUtils.formatRequiredBigDecimalNoDecimal("test");

        // assert
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void formatRequiredBigDecimalNoDecimal_avecDecimalX_X_renvoiDecimalX() {
        // arrange
        // act
        BigDecimal result = PRConverterUtils.formatRequiredBigDecimalNoDecimal("1.2");

        // assert
        assertThat(result).isEqualTo(new BigDecimal("1"));
    }

    @Test
    public void formatRequiredShort_avecNull_renvoi0() {
        // arrange
        // act
        short result = PRConverterUtils.formatRequiredShort(null);

        // assert
        assertThat(result).isEqualTo((short) 0);
    }

    @Test
    public void formatRequiredShort_avecStringNonConvertible_renvoi0() {
        // arrange
        // act
        short result = PRConverterUtils.formatRequiredShort("test");

        // assert
        assertThat(result).isEqualTo((short) 0);
    }

    @Test
    public void formatRequiredShort_avecNb_renvoiNb() {
        // arrange
        // act
        short result = PRConverterUtils.formatRequiredShort("20");

        // assert
        assertThat(result).isEqualTo((short) 20);
    }

    @Test
    public void formatOptionalShort_avecNull_renvoiNull() {
        // arrange
        // act
        Short result = PRConverterUtils.formatOptionalShort(null);

        // assert
        assertThat(result).isNull();
    }

    @Test
    public void formatOptionalShort_avecStringNonConvertible_renvoiNull() {
        // arrange
        // act
        Short result = PRConverterUtils.formatOptionalShort("test");

        // assert
        assertThat(result).isNull();
    }

    @Test
    public void formatOptionalShort_avecNb_renvoiNb() {
        // arrange
        // act
        Short result = PRConverterUtils.formatOptionalShort("20");

        // assert
        assertThat(result).isEqualTo(new Short("20"));
    }



    @Test
    public void formatDateToAAAAMM_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = PRConverterUtils.formatDateToAAAAMM(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatDateToAAAAMM_avecDateFevrier_renvoiDateAAAAMM() throws DatatypeConfigurationException {
        // arrange
        XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(2021, 2, 26, 0, 0, 0, 0, 60);

        // act
        String result = PRConverterUtils.formatDateToAAAAMM(date);

        // assert
        assertThat(result).isEqualTo("202102");
    }

    @Test
    public void formatDateToAAAAMM_avecDateDecembre_renvoiDateAAAAMM() throws DatatypeConfigurationException {
        // arrange
        XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(2021, 12, 26, 0, 0, 0, 0, 60);

        // act
        String result = PRConverterUtils.formatDateToAAAAMM(date);

        // assert
        assertThat(result).isEqualTo("202112");
    }

    @Test
    public void formatDateToMMAA_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = PRConverterUtils.formatDateToMMAA(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatDateToMMAA_avecDateFevrier_renvoiDateMMAA() throws DatatypeConfigurationException {
        // arrange
        XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(2021, 2, 26, 0, 0, 0, 0, 60);

        // act
        String result = PRConverterUtils.formatDateToMMAA(date);

        // assert
        assertThat(result).isEqualTo("0221");
    }

    @Test
    public void formatDateToMMAA_avecDateDecembre_renvoiDateMMAA() throws DatatypeConfigurationException {
        // arrange
        XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(2021, 12, 26, 0, 0, 0, 0, 60);

        // act
        String result = PRConverterUtils.formatDateToMMAA(date);

        // assert
        assertThat(result).isEqualTo("1221");
    }

    @Test
    public void formatNssToLong_avecNull_renvoi0() {
        // arrange
        // act
        long result = PRConverterUtils.formatNssToLong(null);

        // assert
        assertThat(result).isEqualTo(0l);
    }

    @Test
    public void formatNssToLong_avecDateDecembre_renvoiDateMMAA() {
        // arrange
        // act
        long result = PRConverterUtils.formatNssToLong("756.000.000.00");

        // assert
        assertThat(result).isEqualTo(75600000000l);
    }

    @Test
    public void formatAAAAtoAA_avecStringEmpty_renvoiStringEmpty() {
        // arrange
        // act
        String result = PRConverterUtils.formatAAAAtoAA(StringUtils.EMPTY);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatAAAAtoAA_avecAnneeAAAA_renvoiAnneeAA() {
        // arrange
        // act
        String result = PRConverterUtils.formatAAAAtoAA("2021");

        // assert
        assertThat(result).isEqualTo("21");
    }

    @Test
    public void formatAAAAtoAA_avecAnneeAA_renvoiStringEmpty() {
        // arrange
        // act
        String result = PRConverterUtils.formatAAAAtoAA("21");

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatMMtoAAxMM_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = PRConverterUtils.formatMMtoAAxMM(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatMMtoAAxMM_avecNbMoisSupA12_renvoiDateAAxMM() {
        // arrange
        // act
        String result = PRConverterUtils.formatMMtoAAxMM(23);

        // assert
        assertThat(result).isEqualTo("01.11");
    }

    @Test
    public void formatMMtoAAxMM_avecNbMoisInfA12_renvoiDateAAxMM() {
        // arrange
        // act
        String result = PRConverterUtils.formatMMtoAAxMM(9);

        // assert
        assertThat(result).isEqualTo("00.09");
    }

    @Test
    public void formatMMtoAAxMM_avec12mois_renvoiDateAAxMM() {
        // arrange
        // act
        String result = PRConverterUtils.formatMMtoAAxMM(12);

        // assert
        assertThat(result).isEqualTo("01.00");
    }

    @Test
    public void formatAAMMtoAAxMM_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = PRConverterUtils.formatAAMMtoAAxMM(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatAAMMtoAAxMM_avecIntXXXX_renvoiDateAAxMM() {
        // arrange
        // act
        String result = PRConverterUtils.formatAAMMtoAAxMM(1109);

        // assert
        assertThat(result).isEqualTo("11.09");
    }

    @Test
    public void formatAAMMtoAAxMM_avecIntAMM_renvoiDateAAxMM() {
        // arrange
        // act
        String result = PRConverterUtils.formatAAMMtoAAxMM(406);

        // assert
        assertThat(result).isEqualTo("04.06");
    }

    @Test
    public void formatAAMMtoAAxMM_avecIntXX_renvoiStringEmpty() {
        // arrange
        // act
        String result = PRConverterUtils.formatAAMMtoAAxMM(11);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatMMtoAxMM_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = PRConverterUtils.formatMMtoAxMM(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatMMtoAxMM_avecMoins12Mois_renvoiDateAxMM() {
        // arrange
        // act
        String result = PRConverterUtils.formatMMtoAxMM(9);

        // assert
        assertThat(result).isEqualTo("0.09");
    }

    @Test
    public void formatMMtoAxMM_avecPlus12Mois_renvoiDateAxMM() {
        // arrange
        // act
        String result = PRConverterUtils.formatMMtoAxMM(21);

        // assert
        assertThat(result).isEqualTo("1.09");
    }

    @Test
    public void formatMMtoAxMM_avec12Mois_renvoiDateAxMM() {
        // arrange
        // act
        String result = PRConverterUtils.formatMMtoAxMM(12);

        // assert
        assertThat(result).isEqualTo("1.00");
    }

    @Test
    public void convertMMtoA_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = PRConverterUtils.convertMMtoA(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void convertMMtoA_avecMoins12Mois_renvoiNbAnnee() {
        // arrange
        // act
        String result = PRConverterUtils.convertMMtoA(9);

        // assert
        assertThat(result).isEqualTo("0");
    }

    @Test
    public void convertMMtoA_avecPlus12Mois_renvoiNbAnnee() {
        // arrange
        // act
        String result = PRConverterUtils.convertMMtoA(24);

        // assert
        assertThat(result).isEqualTo("2");
    }

    @Test
    public void formatBigDecimalToString_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = PRConverterUtils.formatBigDecimalToString(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatBigDecimalToString_avecBigDecimal_renvoiNb() {
        // arrange
        // act
        String result = PRConverterUtils.formatBigDecimalToString(new BigDecimal("9.1"));

        // assert
        assertThat(result).isEqualTo("9.1");
    }

    @Test
    public void formatBooleanToString_avecNull_renvoiNull() {
        // arrange
        // act
        String result = PRConverterUtils.formatBooleanToString(null);

        // assert
        assertThat(result).isNull();
    }

    @Test
    public void formatBooleanToString_avecTrue_renvoi1() {
        // arrange
        // act
        String result = PRConverterUtils.formatBooleanToString(Boolean.TRUE);

        // assert
        assertThat(result).isEqualTo("1");
    }

    @Test
    public void formatBooleanToString_avecFalse_renvoi0() {
        // arrange
        // act
        String result = PRConverterUtils.formatBooleanToString(Boolean.FALSE);

        // assert
        assertThat(result).isEqualTo("0");
    }

    @Test
    public void formatIntegerToString_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = PRConverterUtils.formatIntegerToString(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatIntegerToString_avecInteger_renvoiNb() {
        // arrange
        // act
        String result = PRConverterUtils.formatIntegerToString(20);

        // assert
        assertThat(result).isEqualTo("20");
    }

    @Test
    public void formatShortToString_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = PRConverterUtils.formatShortToString(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatShortToString_avecShort_renvoiNb() {
        // arrange
        // act
        String result = PRConverterUtils.formatShortToString((short) 20);

        // assert
        assertThat(result).isEqualTo("20");
    }

    @Test
    public void formatCodeCasSpecial_avecNull_renvoiStringEmpty() {
        // arrange
        // act
        String result = PRConverterUtils.formatCodeCasSpecial(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatCodeCasSpecial_avecCode1Chiffre_renvoiNb2Champs() {
        // arrange
        // act
        String result = PRConverterUtils.formatCodeCasSpecial((short) 9);

        // assert
        assertThat(result).isEqualTo("09");
    }

    @Test
    public void formatCodeCasSpecial_avecCode2Chiffres_renvoiNb2Champs() {
        // arrange
        // act
        String result = PRConverterUtils.formatCodeCasSpecial((short) 11);

        // assert
        assertThat(result).isEqualTo("11");
    }

    @Test
    public void formatStringWithoutDots_avecNumeroNull_renvoiStringEmtpy() {
        // arrange
        // act
        String result = PRConverterUtils.formatStringWithoutDots(null);

        // assert
        assertThat(result).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void formatStringWithoutDots_avecNumeroAvecPoints_renvoiNumeroSansPoint() {
        // arrange
        // act
        String result = PRConverterUtils.formatStringWithoutDots("756.000.000.00");

        // assert
        assertThat(result).isEqualTo("75600000000");

    }
}
