package ch.globaz.naos.ree.domain.converter;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeConstants;
import org.junit.Test;

public class ConverterUtilsTest {

    @Test
    public void testTranslateTypeRevenuSalarie() throws Exception {

        // Salarié
        assertEquals(BigInteger.ONE, ConverterUtils.translateTypeRevenuSalarie("310001", ""));
        assertEquals(BigInteger.ONE, ConverterUtils.translateTypeRevenuSalarie("310001", null));
        assertEquals(BigInteger.ONE, ConverterUtils.translateTypeRevenuSalarie("310001", "312002"));
        assertEquals(BigInteger.ONE, ConverterUtils.translateTypeRevenuSalarie("310001", "312003"));

        // Test inconnu
        assertEquals(BigInteger.valueOf(6), ConverterUtils.translateTypeRevenuSalarie("310006", ""));
        assertEquals(BigInteger.valueOf(6), ConverterUtils.translateTypeRevenuSalarie("310006", null));
        assertEquals(BigInteger.valueOf(6), ConverterUtils.translateTypeRevenuSalarie("310006", "312002"));
        assertEquals(BigInteger.valueOf(6), ConverterUtils.translateTypeRevenuSalarie("310006", "312003"));

        // Test code spéciale
        assertEquals(BigInteger.valueOf(7), ConverterUtils.translateTypeRevenuSalarie("310007", "312003"));

        // Test autre
        try {
            assertEquals(BigInteger.valueOf(7), ConverterUtils.translateTypeRevenuSalarie(null, null));
            assertTrue(false);
        } catch (REEBusinessException e) {
            assertTrue(true);
        }

        try {
            assertEquals(BigInteger.valueOf(7), ConverterUtils.translateTypeRevenuSalarie("", null));
            assertTrue(false);
        } catch (REEBusinessException e) {
            assertTrue(true);
        }

        try {
            assertEquals(BigInteger.valueOf(7), ConverterUtils.translateTypeRevenuSalarie("310007", null));
            assertTrue(false);
        } catch (REEBusinessException e) {
            assertTrue(true);
        }

        try {
            assertEquals(BigInteger.valueOf(7), ConverterUtils.translateTypeRevenuSalarie("310007", ""));
            assertTrue(false);
        } catch (REEBusinessException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testTranslateNationality() throws Exception {

        // Nationalité suisse
        assertEquals(BigInteger.ONE, ConverterUtils.translateNationality("315100"));

        // Nationalité autre
        assertEquals(BigInteger.valueOf(2), ConverterUtils.translateNationality("315510"));
        assertEquals(BigInteger.valueOf(2), ConverterUtils.translateNationality("315506"));
        assertEquals(BigInteger.valueOf(2), ConverterUtils.translateNationality("315402"));
        assertEquals(BigInteger.valueOf(2), ConverterUtils.translateNationality("315348"));
        assertEquals(BigInteger.valueOf(2), ConverterUtils.translateNationality("315226"));
        assertEquals(BigInteger.valueOf(2), ConverterUtils.translateNationality("315372"));
        assertEquals(BigInteger.valueOf(2), ConverterUtils.translateNationality("315212"));

        // Test du null
        assertEquals(BigInteger.valueOf(9), ConverterUtils.translateNationality(null));

        // Test de l'inconnu
        assertEquals(BigInteger.valueOf(9), ConverterUtils.translateNationality("315999"));

        // Test du non renseigné
        assertEquals(BigInteger.valueOf(9), ConverterUtils.translateNationality(""));
        assertEquals(BigInteger.valueOf(9), ConverterUtils.translateNationality("0"));
    }

    @Test
    public void testFormatCodeCaisse() throws Exception {
        assertEquals("51010", ConverterUtils.formatCodeCaisse("51.10"));
        assertEquals("51000", ConverterUtils.formatCodeCaisse("51"));
        assertEquals("106000", ConverterUtils.formatCodeCaisse("106"));
        assertEquals("106001", ConverterUtils.formatCodeCaisse("106.1"));
        assertEquals("51001", ConverterUtils.formatCodeCaisse("051.1"));
        assertEquals("51001", ConverterUtils.formatCodeCaisse("051.001"));
        assertEquals("51010", ConverterUtils.formatCodeCaisse("051.010"));
        assertEquals("51100", ConverterUtils.formatCodeCaisse("051.100"));
    }

    @Test
    public void testTranslateSex() throws Exception {

        // Test Homme
        assertEquals("1", ConverterUtils.translateSex("516001"));// Code PYSEXE
        assertEquals("1", ConverterUtils.translateSex("316000"));// Code CISEXE

        // Test Femme
        assertEquals("2", ConverterUtils.translateSex("516002"));// Code PYSEXE
        assertEquals("2", ConverterUtils.translateSex("316001"));// Code CISEXE

        // Test autre => vide
        assertEquals(null, ConverterUtils.translateSex("316002"));
        assertEquals(null, ConverterUtils.translateSex("316002"));
        assertEquals(null, ConverterUtils.translateSex("555654"));
        assertEquals(null, ConverterUtils.translateSex(null));
        assertEquals(null, ConverterUtils.translateSex(""));
        assertEquals(null, ConverterUtils.translateSex("femme"));
        assertEquals(null, ConverterUtils.translateSex("0"));
    }

    @Test
    public void testFormatRevenu() throws Exception {
        String codeExtourne = "311001";

        assertEquals(BigDecimal.valueOf(250.25), ConverterUtils.formatRevenu("250.25", ""));
        assertEquals(BigDecimal.valueOf(250.99), ConverterUtils.formatRevenu("250.99", ""));
        assertEquals(new BigDecimal("250.00"), ConverterUtils.formatRevenu("250.00", ""));

        assertEquals(new BigDecimal("-250.00"), ConverterUtils.formatRevenu("250.00", codeExtourne));
        assertEquals(BigDecimal.valueOf(250), ConverterUtils.formatRevenu("250", ""));
        assertEquals(BigDecimal.valueOf(250), ConverterUtils.formatRevenu("250", "0"));
        assertEquals(BigDecimal.valueOf(250), ConverterUtils.formatRevenu("250", null));

        try {
            assertEquals(BigDecimal.ZERO, ConverterUtils.formatRevenu("", ""));
            assertTrue(false);
        } catch (REEBusinessException e) {
            assertTrue(true);
        }

        try {
            assertEquals(BigDecimal.ZERO, ConverterUtils.formatRevenu(null, ""));
            assertTrue(false);
        } catch (REEBusinessException e) {
            assertTrue(true);
        }

    }

    @Test
    public void testTranslateTypeRevenuIndependant() throws Exception {
        // TSE (écart de spec., la valeur attendue est 2 pour les TSE)
        assertEquals(BigInteger.valueOf(2), ConverterUtils.translateTypeRevenuIndependant("310002", ""));
        assertEquals(BigInteger.valueOf(2), ConverterUtils.translateTypeRevenuIndependant("310002", null));
        assertEquals(BigInteger.valueOf(2), ConverterUtils.translateTypeRevenuIndependant("310002", "312002"));
        assertEquals(BigInteger.valueOf(2), ConverterUtils.translateTypeRevenuIndependant("310002", "312003"));

        // Test Independant
        assertEquals(BigInteger.valueOf(3), ConverterUtils.translateTypeRevenuIndependant("310003", ""));
        assertEquals(BigInteger.valueOf(3), ConverterUtils.translateTypeRevenuIndependant("310003", null));
        assertEquals(BigInteger.valueOf(3), ConverterUtils.translateTypeRevenuIndependant("310003", "312002"));
        assertEquals(BigInteger.valueOf(3), ConverterUtils.translateTypeRevenuIndependant("310003", "312003"));

        // Test code 9
        assertEquals(BigInteger.valueOf(9), ConverterUtils.translateTypeRevenuIndependant("310009", ""));
        assertEquals(BigInteger.valueOf(9), ConverterUtils.translateTypeRevenuIndependant("310009", null));
        assertEquals(BigInteger.valueOf(9), ConverterUtils.translateTypeRevenuIndependant("310009", "312002"));
        assertEquals(BigInteger.valueOf(9), ConverterUtils.translateTypeRevenuIndependant("310009", "312003"));

        // Test code spéciale
        assertEquals(BigInteger.valueOf(7), ConverterUtils.translateTypeRevenuIndependant("310007", "312002"));

        // Test autre

        try {
            assertEquals(BigInteger.valueOf(7), ConverterUtils.translateTypeRevenuIndependant(null, null));
            assertTrue(false);
        } catch (REEBusinessException e) {
            assertTrue(true);
        }

        try {
            assertEquals(BigInteger.valueOf(7), ConverterUtils.translateTypeRevenuIndependant("", null));
            assertTrue(false);
        } catch (REEBusinessException e) {
            assertTrue(true);
        }

        try {
            assertEquals(BigInteger.valueOf(7), ConverterUtils.translateTypeRevenuIndependant("310007", null));
            assertTrue(false);
        } catch (REEBusinessException e) {
            assertTrue(true);
        }

        try {
            assertEquals(BigInteger.valueOf(7), ConverterUtils.translateTypeRevenuIndependant("310007", ""));
            assertTrue(false);
        } catch (REEBusinessException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDefineValidityValue() throws Exception {
        assertEquals(BigDecimal.ZERO, ConverterUtils.defineValidityValue(""));
        assertEquals(BigDecimal.ZERO, ConverterUtils.defineValidityValue((String) null));
        assertEquals(BigDecimal.ZERO, ConverterUtils.defineValidityValue("0"));
        assertEquals(BigDecimal.ZERO, ConverterUtils.defineValidityValue("0"));

        assertEquals(BigDecimal.valueOf(1), ConverterUtils.defineValidityValue("311001"));
        assertEquals(BigDecimal.ONE, ConverterUtils.defineValidityValue("311001"));

    }

    @Test
    public void testConvertToXMLGregorianCalendar() throws Exception {
        String yyyyMMdd = "20101011";
        assertEquals(2010, ConverterUtils.convertToXMLGregorianCalendar(yyyyMMdd).getYear());
        assertEquals(10, ConverterUtils.convertToXMLGregorianCalendar(yyyyMMdd).getMonth());
        assertEquals(11, ConverterUtils.convertToXMLGregorianCalendar(yyyyMMdd).getDay());
        assertEquals(DatatypeConstants.FIELD_UNDEFINED, ConverterUtils.convertToXMLGregorianCalendar(yyyyMMdd)
                .getTimezone());
    }

    @Test
    public void testFormatNssInLong() throws Exception {
        assertEquals((double) 7565555555555L, (double) ConverterUtils.formatNssInLong("756.5555.5555.55"), 0);
        assertEquals((double) 7565555555555L, (double) ConverterUtils.formatNssInLong("7565555555555"), 0);
        assertEquals((double) 7565555555555L, (double) ConverterUtils.formatNssInLong("7565555555555       "), 0);
        assertTrue(ConverterUtils.formatNssInLong("") == null);
        assertTrue(ConverterUtils.formatNssInLong(null) == null);
        // cas trouvé en DB, NSS remplis d'espaces vide
        assertTrue(ConverterUtils.formatNssInLong("                    ") == null);
        try {
            assertEquals((double) 7225555555555L, (double) ConverterUtils.formatNssInLong("722.5555.5555.55"), 0);
            assertTrue(false);
        } catch (REEBusinessException e) {
            System.out.println(e.getMessage());
            assertTrue(true);
        }
        try {
            assertEquals((double) 75655555L, (double) ConverterUtils.formatNssInLong("756.55.55.5"), 0);
            assertTrue(false);
        } catch (REEBusinessException e) {
            System.out.println(e.getMessage());
            assertTrue(true);
        }
    }

    @Test
    public void testDefineDatePartiel() throws Exception {
        assertEquals(2001, ConverterUtils.defineDatePartiel("20010509").getYearMonthDay().getYear());
        assertEquals(5, ConverterUtils.defineDatePartiel("20010509").getYearMonthDay().getMonth());
        assertEquals(9, ConverterUtils.defineDatePartiel("20010509").getYearMonthDay().getDay());
    }
}
