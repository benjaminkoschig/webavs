package globaz.corvus.annonce;

import static org.junit.Assert.*;
import globaz.corvus.annonce.reader.REBaseDeCalcul10EmeRevisionReader;
import globaz.corvus.annonce.reader.REBaseDeCalcul9EmeRevisionReader;
import org.junit.Test;

public class REBaseDeCalcul9EmeRevisionReaderTest {

    @Test
    public void convertAnneeNiveau() {
        REBaseDeCalcul9EmeRevisionReader reader = new REBaseDeCalcul9EmeRevisionReader();
        assertTrue(2004 == reader.readAnneeNiveau("04"));
        assertTrue(2000 == reader.readAnneeNiveau("00"));
        assertTrue(1999 == reader.readAnneeNiveau("99"));
        assertTrue(2010 == reader.readAnneeNiveau("10"));
        assertTrue(1986 == reader.readAnneeNiveau("86"));
        assertTrue(1992 == reader.readAnneeNiveau("92"));
        assertTrue(null == reader.readAnneeNiveau("100"));
        assertTrue(null == reader.readAnneeNiveau("0"));
    }

    @Test
    public void readDegreInvalidite() {
        REBaseDeCalcul9EmeRevisionReader reader = new REBaseDeCalcul9EmeRevisionReader();
        assertTrue(4 == reader.readDegreInvalidite("04"));
        assertTrue(2 == reader.readDegreInvalidite("2"));
        assertTrue(00 == reader.readDegreInvalidite("00"));
        assertTrue(99 == reader.readDegreInvalidite("99"));
        assertTrue(10 == reader.readDegreInvalidite("10"));
        assertTrue(100 == reader.readDegreInvalidite("100"));
        assertTrue(0 == reader.readDegreInvalidite("0"));
        assertTrue(null == reader.readDegreInvalidite("1001"));
    }

    @Test
    public void readDureeCoEchelleRenteAv73_nombreAnnee() {
        REBaseDeCalcul9EmeRevisionReader reader = new REBaseDeCalcul9EmeRevisionReader();
        assertTrue(4 == reader.readDureeCoEchelleRenteAv73_nombreAnnee("04.06"));
        assertTrue(0 == reader.readDureeCoEchelleRenteAv73_nombreAnnee("00.05"));
        assertTrue(99 == reader.readDureeCoEchelleRenteAv73_nombreAnnee("99.00"));
        assertTrue(10 == reader.readDureeCoEchelleRenteAv73_nombreAnnee("10.10"));
        assertTrue(null == reader.readDureeCoEchelleRenteAv73_nombreAnnee("1010"));
        assertTrue(null == reader.readDureeCoEchelleRenteAv73_nombreAnnee("10.1"));
        assertTrue(null == reader.readDureeCoEchelleRenteAv73_nombreAnnee("10104"));
    }

    @Test
    public void readDureeCoEchelleRenteAv73_nombreMois() {
        REBaseDeCalcul9EmeRevisionReader reader = new REBaseDeCalcul9EmeRevisionReader();
        assertTrue(6 == reader.readDureeCoEchelleRenteAv73_nombreMois("04.06"));
        assertTrue(5 == reader.readDureeCoEchelleRenteAv73_nombreMois("00.05"));
        assertTrue(0 == reader.readDureeCoEchelleRenteAv73_nombreMois("99.00"));
        assertTrue(10 == reader.readDureeCoEchelleRenteAv73_nombreMois("10.10"));
        assertTrue(null == reader.readDureeCoEchelleRenteAv73_nombreAnnee("101"));
        assertTrue(null == reader.readDureeCoEchelleRenteAv73_nombreAnnee("10104"));
        assertTrue(null == reader.readDureeCoEchelleRenteAv73_nombreAnnee("1004"));
    }

    @Test
    public void readDureeCoEchelleRenteDes73_nombreAnnee() {
        REBaseDeCalcul9EmeRevisionReader reader = new REBaseDeCalcul9EmeRevisionReader();
        assertTrue(4 == reader.readDureeCoEchelleRenteDes73_nombreAnnee("04.06"));
        assertTrue(0 == reader.readDureeCoEchelleRenteDes73_nombreAnnee("00.05"));
        assertTrue(99 == reader.readDureeCoEchelleRenteDes73_nombreAnnee("99.00"));
        assertTrue(10 == reader.readDureeCoEchelleRenteDes73_nombreAnnee("10.10"));
        assertTrue(null == reader.readDureeCoEchelleRenteAv73_nombreAnnee("1.01"));
        assertTrue(null == reader.readDureeCoEchelleRenteAv73_nombreAnnee("401"));
        assertTrue(null == reader.readDureeCoEchelleRenteAv73_nombreAnnee("1001"));
        assertTrue(null == reader.readDureeCoEchelleRenteAv73_nombreAnnee("10104"));
    }

    @Test
    public void readDureeCoEchelleRenteDes73_nombreMois() {
        REBaseDeCalcul9EmeRevisionReader reader = new REBaseDeCalcul9EmeRevisionReader();
        assertTrue(6 == reader.readDureeCoEchelleRenteDes73_nombreMois("04.06"));
        assertTrue(5 == reader.readDureeCoEchelleRenteDes73_nombreMois("00.05"));
        assertTrue(0 == reader.readDureeCoEchelleRenteDes73_nombreMois("99.00"));
        assertTrue(10 == reader.readDureeCoEchelleRenteDes73_nombreMois("10.10"));
        assertTrue(null == reader.readDureeCoEchelleRenteAv73_nombreAnnee("1.01"));
        assertTrue(null == reader.readDureeCoEchelleRenteAv73_nombreAnnee("101"));
        assertTrue(null == reader.readDureeCoEchelleRenteAv73_nombreAnnee("10104"));
    }

    @Test
    public void readNbreAnneeBonifTrans_nombreAnnee() {
        REBaseDeCalcul9EmeRevisionReader reader = new REBaseDeCalcul9EmeRevisionReader();
        assertTrue(3 == reader.readNbreAnneeBonifTrans_nombreAnnee("3.6"));
        assertTrue(0 == reader.readNbreAnneeBonifTrans_nombreAnnee("0.5"));
        assertTrue(8 == reader.readNbreAnneeBonifTrans_nombreAnnee("8.3"));
        assertTrue(7 == reader.readNbreAnneeBonifTrans_nombreAnnee("7.5"));
        assertTrue(null == reader.readNbreAnneeBonifTrans_nombreAnnee("101"));
        assertTrue(null == reader.readNbreAnneeBonifTrans_nombreAnnee("4"));
    }

    @Test
    public void readDureeCotPourDetRAM_nombreMois() {
        REBaseDeCalcul9EmeRevisionReader reader = new REBaseDeCalcul9EmeRevisionReader();
        assertTrue(6 == reader.readDureeCotPourDetRAM_nombreMois("04.06"));
        assertTrue(5 == reader.readDureeCotPourDetRAM_nombreMois("00.05"));
        assertTrue(0 == reader.readDureeCotPourDetRAM_nombreMois("99.00"));
        assertTrue(10 == reader.readDureeCotPourDetRAM_nombreMois("10.10"));
        assertTrue(null == reader.readDureeCotPourDetRAM_nombreMois("1.01"));
        assertTrue(null == reader.readDureeCotPourDetRAM_nombreMois("101"));
        assertTrue(null == reader.readDureeCotPourDetRAM_nombreMois("10104"));
    }

    @Test
    public void readDureeCotPourDetRAM_nombreAnnee() {
        REBaseDeCalcul9EmeRevisionReader reader = new REBaseDeCalcul9EmeRevisionReader();
        assertTrue(4 == reader.readDureeCotPourDetRAM_nombreAnnee("04.06"));
        assertTrue(0 == reader.readDureeCotPourDetRAM_nombreAnnee("00.05"));
        assertTrue(99 == reader.readDureeCotPourDetRAM_nombreAnnee("99.00"));
        assertTrue(10 == reader.readDureeCotPourDetRAM_nombreAnnee("10.10"));
        assertTrue(null == reader.readDureeCotPourDetRAM_nombreAnnee("1.01"));
        assertTrue(null == reader.readDureeCotPourDetRAM_nombreAnnee("101"));
        assertTrue(null == reader.readDureeCotPourDetRAM_nombreAnnee("10104"));
    }

    @Test
    public void readNbreAnneeBonifTrans_isDemiAnnee() {
        REBaseDeCalcul9EmeRevisionReader reader = new REBaseDeCalcul9EmeRevisionReader();
        assertTrue(null == reader.readNbreAnneeBonifTrans_isDemiAnnee("46"));
        assertTrue(null == reader.readNbreAnneeBonifTrans_isDemiAnnee("45"));
        assertTrue(Boolean.TRUE == reader.readNbreAnneeBonifTrans_isDemiAnnee("0.5"));
        assertTrue(Boolean.FALSE == reader.readNbreAnneeBonifTrans_isDemiAnnee("8.0"));
        assertTrue(Boolean.TRUE == reader.readNbreAnneeBonifTrans_isDemiAnnee("7.5"));
        assertTrue(null == reader.readNbreAnneeBonifTrans_isDemiAnnee("101"));
        assertTrue(null == reader.readNbreAnneeBonifTrans_isDemiAnnee("4"));
    }

    @Test
    public void readNombreAnneeBTE_valeurEntiere() {
        REBaseDeCalcul9EmeRevisionReader reader = new REBaseDeCalcul9EmeRevisionReader();
        assertTrue(46 == reader.readNombreAnneeBTE_valeurEntiere("46"));
        assertTrue(5 == reader.readNombreAnneeBTE_valeurEntiere("05"));
        assertTrue(80 == reader.readNombreAnneeBTE_valeurEntiere("80"));
        assertTrue(75 == reader.readNombreAnneeBTE_valeurEntiere("75"));
    }

    @Test
    public void readNombreAnneeBTE_valeurDecimal() {
        REBaseDeCalcul9EmeRevisionReader reader = new REBaseDeCalcul9EmeRevisionReader();
        assertTrue(null == reader.readNombreAnneeBTE_valeurDecimal("46"));
        assertTrue(null == reader.readNombreAnneeBTE_valeurDecimal("05"));
        assertTrue(null == reader.readNombreAnneeBTE_valeurDecimal("80"));
        assertTrue(null == reader.readNombreAnneeBTE_valeurDecimal("75"));
        assertTrue(null == reader.readNombreAnneeBTE_valeurDecimal("101"));
        assertTrue(null == reader.readNombreAnneeBTE_valeurDecimal("4"));
    }

    @Test
    public void readNbreAnneeBTA_valeurEntiere() {
        REBaseDeCalcul10EmeRevisionReader reader = new REBaseDeCalcul10EmeRevisionReader();
        assertTrue(4 == reader.readNbreAnneeBTA_valeurEntiere("04.66"));
        assertTrue(20 == reader.readNbreAnneeBTA_valeurEntiere("20.60"));
        assertTrue(12 == reader.readNbreAnneeBTA_valeurEntiere("12.00"));
        assertTrue(null == reader.readNbreAnneeBTA_valeurEntiere("00.75"));
        assertTrue(null == reader.readNbreAnneeBTA_valeurEntiere("101"));
        assertTrue(null == reader.readNbreAnneeBTA_valeurEntiere("4"));
    }

    @Test
    public void readNbreAnneeBTA_valeurDecimal() {
        REBaseDeCalcul10EmeRevisionReader reader = new REBaseDeCalcul10EmeRevisionReader();
        assertTrue(66 == reader.readNbreAnneeBTA_valeurDecimal("04.66"));
        assertTrue(60 == reader.readNbreAnneeBTA_valeurDecimal("20.60"));
        assertTrue(null == reader.readNbreAnneeBTA_valeurDecimal("12.00"));
        assertTrue(75 == reader.readNbreAnneeBTA_valeurDecimal("00.75"));
        assertTrue(null == reader.readNbreAnneeBTA_valeurDecimal("101"));
        assertTrue(null == reader.readNbreAnneeBTA_valeurDecimal("4"));
    }

    @Test
    public void convertDureeCotManquante48_72() {
        REBaseDeCalcul9EmeRevisionReader reader = new REBaseDeCalcul9EmeRevisionReader();
        assertTrue(46 == reader.readDureeCotManquante48_72("46"));
        assertTrue(5 == reader.readDureeCotManquante48_72("05"));
        assertTrue(80 == reader.readDureeCotManquante48_72("80"));
        assertTrue(75 == reader.readDureeCotManquante48_72("75"));
        assertTrue(null == reader.readDureeCotManquante48_72("101"));
        assertTrue(null == reader.readDureeCotManquante48_72("4"));
    }

    @Test
    public void convertDureeCotManquante73_78() {
        REBaseDeCalcul9EmeRevisionReader reader = new REBaseDeCalcul9EmeRevisionReader();
        assertTrue(46 == reader.readDureeCotManquante73_78("46"));
        assertTrue(5 == reader.readDureeCotManquante73_78("05"));
        assertTrue(80 == reader.readDureeCotManquante73_78("80"));
        assertTrue(75 == reader.readDureeCotManquante73_78("75"));
        assertTrue(null == reader.readDureeCotManquante73_78("101"));
        assertTrue(null == reader.readDureeCotManquante73_78("4"));
    }

    // @Test
    // public void name() {
    //
    // }
    //
    // @Test
    // public void name() {
    //
    // }
}
