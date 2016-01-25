package ch.globaz.vulpecula.domain.models.decompte;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.vulpecula.domain.constants.Constants;
import ch.globaz.vulpecula.domain.models.common.Date;

public class NumeroDecompteTest {
    @Test
    public void given20130300ShouldBeInvalid() {
        try {
            new NumeroDecompte("20130300");
            fail("Le numéro de décompte doit faire 9 caractères et aucune exception n'a été levée");
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void given201303001WhenGetvalueThen201303001() {
        String actual = new NumeroDecompte("201303001").getValue();

        assertEquals("201303001", actual);
    }

    @Test
    public void given201303001ShouldBeInvalid() {
        new NumeroDecompte("201303001");
    }

    @Test
    public void genererNumeroDecompte_GivenPeriodiqueAndMensuelleForMoisMai_ShouldBe201505010() {
        TypeDecompte typeDecompte = TypeDecompte.PERIODIQUE;
        String periodicite = Constants.PERIODICITE_MENSUELLE;
        Date date = new Date("01.05.2015");
        NumeroDecompte numeroDecompte = NumeroDecompte.next(typeDecompte, date, periodicite, null);

        assertEquals("201505000", numeroDecompte.getValue());
    }

    @Test
    public void genererNumeroDecompte_GivenPeriodiqueAndTrimestrielleForMoisDecembre_ShouldBe201541010() {
        TypeDecompte typeDecompte = TypeDecompte.PERIODIQUE;
        String periodicite = Constants.PERIODICITE_TRIMESTRIELLE;
        Date date = new Date("01.01.2015");
        NumeroDecompte numeroDecompte = NumeroDecompte.next(typeDecompte, date, periodicite, null);

        assertEquals("201541000", numeroDecompte.getValue());
    }

    @Test
    public void genererNumeroDecompte_GivenPeriodiqueAndAnnuelleForMoisDecembre_ShouldBe201518010() {
        TypeDecompte typeDecompte = TypeDecompte.PERIODIQUE;
        String periodicite = Constants.PERIODICITE_ANNUELLE;
        Date date = new Date("01.12.2015");
        NumeroDecompte numeroDecompte = NumeroDecompte.next(typeDecompte, date, periodicite, null);

        assertEquals("201540000", numeroDecompte.getValue());
    }

    @Test
    public void genererNumeroDecompte_GivenComplementaireForMoisDecembre_ShouldBe201518010() {
        TypeDecompte typeDecompte = TypeDecompte.COMPLEMENTAIRE;
        String periodicite = Constants.PERIODICITE_MENSUELLE;
        Date date = new Date("01.12.2015");
        NumeroDecompte numeroDecompte = NumeroDecompte.next(typeDecompte, date, periodicite, null);

        assertEquals("201536000", numeroDecompte.getValue());
    }

    @Test
    public void genererNumeroDecompte_GivenControleEmployeurForMoisDecembre_ShouldBe201518010() {
        TypeDecompte typeDecompte = TypeDecompte.CONTROLE_EMPLOYEUR;
        String periodicite = Constants.PERIODICITE_MENSUELLE;
        Date date = new Date("01.12.2015");
        NumeroDecompte numeroDecompte = NumeroDecompte.next(typeDecompte, date, periodicite, null);

        assertEquals("201517000", numeroDecompte.getValue());
    }

    @Test
    public void getNumeroDecompte_GivenLastNoDecompte010AndDecomptePeriodiqueAnd01052015_ShouldBe201505020() {
        TypeDecompte typeDecompte = TypeDecompte.PERIODIQUE;
        Date date = new Date("01.05.2015");
        String periodicite = Constants.PERIODICITE_MENSUELLE;

        NumeroDecompte numeroDecompte = NumeroDecompte.next(typeDecompte, date, periodicite, "000");

        assertEquals(new NumeroDecompte("201505010"), numeroDecompte);
    }

    @Test
    public void getNumeroDecompte_GivenLastNoDecompteNullAndDecompteComplementaireAnd01052015_ShouldBe201540010() {
        TypeDecompte typeDecompte = TypeDecompte.COMPLEMENTAIRE;
        Date date = new Date("01.05.2015");
        String periodicite = Constants.PERIODICITE_MENSUELLE;

        NumeroDecompte numeroDecompte = NumeroDecompte.next(typeDecompte, date, periodicite, null);

        assertEquals(new NumeroDecompte("201536000"), numeroDecompte);
    }

}
