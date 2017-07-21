package globaz.naos.process;

import static org.junit.Assert.*;
import globaz.pavo.db.compte.CICompteIndividuel;
import org.junit.Test;

public class AFControleLppAnnuelProcessTest {

    @Test
    public void testTestCalculAge() throws Exception {
        // en retraite
        assertFalse(AFControleLppAnnuelProcess.testCalculAgeIsSoumis(2016, "19461110", CICompteIndividuel.CS_HOMME,
                "01"));
        assertFalse(AFControleLppAnnuelProcess.testCalculAgeIsSoumis(2016, "19471110", CICompteIndividuel.CS_HOMME,
                "01"));
        assertFalse(AFControleLppAnnuelProcess.testCalculAgeIsSoumis(2016, "19481110", CICompteIndividuel.CS_HOMME,
                "01"));
        assertFalse(AFControleLppAnnuelProcess.testCalculAgeIsSoumis(2016, "19491110", CICompteIndividuel.CS_HOMME,
                "01"));
        assertFalse(AFControleLppAnnuelProcess.testCalculAgeIsSoumis(2016, "19501110", CICompteIndividuel.CS_HOMME,
                "01"));

        // Annee de la retraite
        assertTrue(AFControleLppAnnuelProcess
                .testCalculAgeIsSoumis(2016, "19511110", CICompteIndividuel.CS_HOMME, "01"));
        assertTrue(AFControleLppAnnuelProcess
                .testCalculAgeIsSoumis(2016, "19511110", CICompteIndividuel.CS_HOMME, "10"));
        assertTrue(AFControleLppAnnuelProcess
                .testCalculAgeIsSoumis(2016, "19511110", CICompteIndividuel.CS_HOMME, "11"));
        assertFalse(AFControleLppAnnuelProcess.testCalculAgeIsSoumis(2016, "19511110", CICompteIndividuel.CS_HOMME,
                "12"));

        // Pas en en retraite et + de 18 ans
        assertTrue(AFControleLppAnnuelProcess
                .testCalculAgeIsSoumis(2016, "19521110", CICompteIndividuel.CS_HOMME, "01"));
        assertTrue(AFControleLppAnnuelProcess
                .testCalculAgeIsSoumis(2016, "19531110", CICompteIndividuel.CS_HOMME, "01"));
        assertTrue(AFControleLppAnnuelProcess
                .testCalculAgeIsSoumis(2016, "19541110", CICompteIndividuel.CS_HOMME, "01"));
        assertTrue(AFControleLppAnnuelProcess
                .testCalculAgeIsSoumis(2016, "19971110", CICompteIndividuel.CS_HOMME, "01"));

        // Age = 18 ans
        assertTrue(AFControleLppAnnuelProcess
                .testCalculAgeIsSoumis(2016, "19981110", CICompteIndividuel.CS_HOMME, "01"));

        // Age < 18 ans
        assertFalse(AFControleLppAnnuelProcess.testCalculAgeIsSoumis(2016, "19991110", CICompteIndividuel.CS_HOMME,
                "01"));

        // TEst avec une femme
        // Année de la retraite et mois égal au mois de naissance
        assertTrue(AFControleLppAnnuelProcess
                .testCalculAgeIsSoumis(2010, "19461018", CICompteIndividuel.CS_FEMME, "10"));
        // Année de la retraite et mois avant le mois de naissance
        assertTrue(AFControleLppAnnuelProcess
                .testCalculAgeIsSoumis(2010, "19461018", CICompteIndividuel.CS_FEMME, "09"));
        // Année de la retraite et mois après le mois de naissance
        assertFalse(AFControleLppAnnuelProcess.testCalculAgeIsSoumis(2010, "19461018", CICompteIndividuel.CS_FEMME,
                "11"));
        // Année plus grand que l'année de la retraite
        assertFalse(AFControleLppAnnuelProcess.testCalculAgeIsSoumis(2011, "19461018", CICompteIndividuel.CS_FEMME,
                "10"));
        // Année plus petit que l'année de la retraite
        assertTrue(AFControleLppAnnuelProcess
                .testCalculAgeIsSoumis(2009, "19461018", CICompteIndividuel.CS_FEMME, "10"));
    }

    @Test
    public void testTestIsCouvert() throws Exception {

        assertTrue(AFControleLppAnnuelProcess.testIsCouvert(2010, "1", "12", "20100101", "20101231"));
        assertTrue(AFControleLppAnnuelProcess.testIsCouvert(2010, "3", "6", "20100101", "20101231"));
        assertTrue(AFControleLppAnnuelProcess.testIsCouvert(2010, "03", "06", "20100101", "20101231"));
        assertFalse(AFControleLppAnnuelProcess.testIsCouvert(2010, "1", "6", "20100701", "20101231"));
        assertFalse(AFControleLppAnnuelProcess.testIsCouvert(2010, "6", "12", "20100101", "20100531"));
        assertFalse(AFControleLppAnnuelProcess.testIsCouvert(2010, "4", "06", "20100101", "20100331"));
        assertTrue(AFControleLppAnnuelProcess.testIsCouvert(2010, "3", "6", "20100301", "20100630"));

        assertTrue(AFControleLppAnnuelProcess.testIsCouvert(2010, "1", "12", "0", "20101231"));
        assertTrue(AFControleLppAnnuelProcess.testIsCouvert(2010, "1", "12", null, "20101231"));
        assertTrue(AFControleLppAnnuelProcess.testIsCouvert(2010, "01", "12", null, "20101231"));

        assertTrue(AFControleLppAnnuelProcess.testIsCouvert(2010, "1", "12", "0", "0"));

        assertTrue(AFControleLppAnnuelProcess.testIsCouvert(2010, "1", "12", "0", "0"));
        assertTrue(AFControleLppAnnuelProcess.testIsCouvert(2010, "", "", "", ""));

        assertTrue(AFControleLppAnnuelProcess.testIsCouvert(2010, "1", "12", "20100501", "20101231"));

    }
}
