package ch.globaz.corvus.process.attestationsfiscales;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.prestation.domaine.constantes.DomaineCodePrestation;

/**
 * Test pour les analyseurs de lots dans la génération des attestations fiscales
 * 
 * @author PBA
 */
public class REAnalyseurLotTest {

    private REAbstractAnalyseurLot analyseurLot1;
    private REAbstractAnalyseurLot analyseurLot2;
    private REAbstractAnalyseurLot analyseurLot3;
    private REAbstractAnalyseurLot analyseurLot4;
    private REFamillePourAttestationsFiscales famille;
    private RERentePourAttestationsFiscales rente1;
    private RETiersPourAttestationsFiscales tiers1;

    @Before
    public void setUp() {
        new REAbstractAnalyseurLot("2011", false, DomaineCodePrestation.AI, DomaineCodePrestation.SURVIVANT,
                DomaineCodePrestation.VIEILLESSE) {
        };

        analyseurLot1 = new REAnalyseurLot1("2011");
        analyseurLot2 = new REAnalyseurLot2("2011");
        analyseurLot3 = new REAnalyseurLot3("2011");
        analyseurLot4 = new REAnalyseurLot4("2011");

        famille = new REFamillePourAttestationsFiscales();

        tiers1 = new RETiersPourAttestationsFiscales();
        tiers1.setIdTiers("1");
        famille.getMapTiersBeneficiaire().put("1", tiers1);

        rente1 = new RERentePourAttestationsFiscales();
        rente1.setIdRenteAccordee("1");
        tiers1.getMapRentes().put("1", rente1);
    }

    @Test
    public void testAvecRetro() {

        rente1.setCodePrestation("10");

        // décision avant l'année fiscale et rente commençant dans l'année -> OK
        rente1.setDateDebutDroit("01.2011");
        rente1.setDateDecision("31.12.2010");
        Assert.assertTrue(analyseurLot1.isFamilleDansLot(famille));
        Assert.assertFalse(analyseurLot3.isFamilleDansLot(famille));

        // décision au début de l'année, rente commençant après la décision -> OK
        rente1.setDateDebutDroit("02.2011");
        rente1.setDateDecision("31.01.2011");
        Assert.assertFalse(analyseurLot1.isFamilleDansLot(famille));
        Assert.assertTrue(analyseurLot3.isFamilleDansLot(famille));

        // décision et rente dans le même mois -> faux, possibilité de rétro
        rente1.setDateDebutDroit("02.2011");
        rente1.setDateDecision("01.02.2011");
        Assert.assertFalse(analyseurLot1.isFamilleDansLot(famille));
        Assert.assertFalse(analyseurLot3.isFamilleDansLot(famille));

        // décision après le début de la rente (le tout dans l'année) -> faux, rétro
        rente1.setDateDebutDroit("02.2011");
        rente1.setDateDecision("01.03.2011");
        Assert.assertFalse(analyseurLot1.isFamilleDansLot(famille));
        Assert.assertFalse(analyseurLot3.isFamilleDansLot(famille));

        // décision après l'année, rente dans l'année -> faux, rétro pas pris en compte car décision pas dans l'année
        rente1.setDateDebutDroit("11.2011");
        rente1.setDateDecision("01.01.2012");
        Assert.assertFalse(analyseurLot1.isFamilleDansLot(famille));
        Assert.assertFalse(analyseurLot3.isFamilleDansLot(famille));
    }

    @Test
    public void testLot1() {
        // avec une rente vieillesse
        rente1.setCodePrestation("10");
        Assert.assertTrue(analyseurLot1.isFamilleDansLot(famille));

        rente1.setCodePrestation("35");
        Assert.assertTrue(analyseurLot1.isFamilleDansLot(famille));

        // avec une rente AI
        rente1.setCodePrestation("50");
        Assert.assertTrue(analyseurLot1.isFamilleDansLot(famille));

        rente1.setCodePrestation("54");
        Assert.assertTrue(analyseurLot1.isFamilleDansLot(famille));

        // avec une rente survivant
        rente1.setCodePrestation("13");
        Assert.assertFalse(analyseurLot1.isFamilleDansLot(famille));

        rente1.setCodePrestation("14");
        Assert.assertFalse(analyseurLot1.isFamilleDansLot(famille));

        // rente vieillesse + rente API
        RERentePourAttestationsFiscales rente2 = new RERentePourAttestationsFiscales();
        rente2.setIdRenteAccordee("2");
        tiers1.getMapRentes().put("2", rente2);

        rente1.setCodePrestation("85");
        rente2.setCodePrestation("10");
        Assert.assertTrue(analyseurLot1.isFamilleDansLot(famille));

        rente1.setCodePrestation("10");
        rente2.setCodePrestation("85");
        Assert.assertTrue(analyseurLot1.isFamilleDansLot(famille));
    }

    @Test
    public void testLot2() {

        // avec une rente vieillesse
        rente1.setCodePrestation("10");
        Assert.assertFalse(analyseurLot2.isFamilleDansLot(famille));

        rente1.setCodePrestation("35");
        Assert.assertFalse(analyseurLot2.isFamilleDansLot(famille));

        // avec une rente AI
        rente1.setCodePrestation("50");
        Assert.assertFalse(analyseurLot2.isFamilleDansLot(famille));

        rente1.setCodePrestation("54");
        Assert.assertFalse(analyseurLot2.isFamilleDansLot(famille));

        // avec une rente survivant
        rente1.setCodePrestation("13");
        Assert.assertTrue(analyseurLot2.isFamilleDansLot(famille));

        rente1.setCodePrestation("14");
        Assert.assertTrue(analyseurLot2.isFamilleDansLot(famille));

        // rente survivant + rente API
        RERentePourAttestationsFiscales rente2 = new RERentePourAttestationsFiscales();
        rente2.setIdRenteAccordee("2");
        tiers1.getMapRentes().put("2", rente2);

        rente1.setCodePrestation("85");
        rente2.setCodePrestation("13");
        Assert.assertTrue(analyseurLot2.isFamilleDansLot(famille));

        rente1.setCodePrestation("13");
        rente2.setCodePrestation("85");
        Assert.assertTrue(analyseurLot2.isFamilleDansLot(famille));
    }

    @Test
    public void testLot3() {
        rente1.setDateDecision("01.01.2011");

        // avec une rente vieillesse
        rente1.setCodePrestation("10");
        Assert.assertTrue(analyseurLot3.isFamilleDansLot(famille));

        rente1.setCodePrestation("35");
        Assert.assertTrue(analyseurLot3.isFamilleDansLot(famille));

        // avec une rente AI
        rente1.setCodePrestation("50");
        Assert.assertTrue(analyseurLot3.isFamilleDansLot(famille));

        rente1.setCodePrestation("54");
        Assert.assertTrue(analyseurLot3.isFamilleDansLot(famille));

        // avec une rente survivant
        rente1.setCodePrestation("13");
        Assert.assertFalse(analyseurLot3.isFamilleDansLot(famille));

        rente1.setCodePrestation("14");
        Assert.assertFalse(analyseurLot3.isFamilleDansLot(famille));

        // rente vieillesse + rente API
        RERentePourAttestationsFiscales rente2 = new RERentePourAttestationsFiscales();
        rente2.setIdRenteAccordee("2");
        tiers1.getMapRentes().put("2", rente2);

        rente1.setCodePrestation("85");
        rente2.setCodePrestation("10");
        Assert.assertTrue(analyseurLot3.isFamilleDansLot(famille));

        rente1.setCodePrestation("10");
        rente2.setCodePrestation("85");
        Assert.assertTrue(analyseurLot3.isFamilleDansLot(famille));
    }

    @Test
    public void testLot4() {
        rente1.setDateDecision("01.01.2011");

        // avec une rente vieillesse
        rente1.setCodePrestation("10");
        Assert.assertFalse(analyseurLot4.isFamilleDansLot(famille));

        rente1.setCodePrestation("35");
        Assert.assertFalse(analyseurLot4.isFamilleDansLot(famille));

        // avec une rente AI
        rente1.setCodePrestation("50");
        Assert.assertFalse(analyseurLot4.isFamilleDansLot(famille));

        rente1.setCodePrestation("54");
        Assert.assertFalse(analyseurLot4.isFamilleDansLot(famille));

        // avec une rente survivant
        rente1.setCodePrestation("13");
        Assert.assertTrue(analyseurLot4.isFamilleDansLot(famille));

        rente1.setCodePrestation("14");
        Assert.assertTrue(analyseurLot4.isFamilleDansLot(famille));

        // rente vieillesse + rente API
        RERentePourAttestationsFiscales rente2 = new RERentePourAttestationsFiscales();
        rente2.setIdRenteAccordee("2");
        tiers1.getMapRentes().put("2", rente2);

        rente1.setCodePrestation("85");
        rente2.setCodePrestation("13");
        Assert.assertTrue(analyseurLot4.isFamilleDansLot(famille));

        rente1.setCodePrestation("13");
        rente2.setCodePrestation("85");
        Assert.assertTrue(analyseurLot4.isFamilleDansLot(famille));
    }

    @Test
    public void testPersonneDecedeeDurantAnneeFiscale() {
        rente1.setCodePrestation("10");
        rente1.setDateDebutDroit("04.2011");
        rente1.setDateFinDroit("10.2011");
        tiers1.setDateDeces("30.09.2011");

        // cas décrit dans le BZ 7923
        Assert.assertFalse(analyseurLot1.isFamilleDansLot(famille));
        Assert.assertFalse(analyseurLot2.isFamilleDansLot(famille));
        Assert.assertFalse(analyseurLot3.isFamilleDansLot(famille));
        Assert.assertFalse(analyseurLot4.isFamilleDansLot(famille));

        rente1.setDateDecision("02.09.2011");
        Assert.assertFalse(analyseurLot1.isFamilleDansLot(famille));
        Assert.assertFalse(analyseurLot2.isFamilleDansLot(famille));
        Assert.assertFalse(analyseurLot3.isFamilleDansLot(famille));
        Assert.assertFalse(analyseurLot4.isFamilleDansLot(famille));
    }

    @Test
    @Ignore
    public void testRenteBloquee() {
        rente1.setCodePrestation("54");
        rente1.setDateDebutDroit("12.2010");
        rente1.setIsRenteBloquee(true);

        // Avec date de fin ultérieur à l'année fiscale
        rente1.setDateFinDroit("01.2012");
        Assert.assertFalse(analyseurLot1.isFamilleDansLot(famille));

        // Avec date de fin dans l'année fiscale
        rente1.setDateFinDroit("12.2011");
        Assert.assertTrue(analyseurLot1.isFamilleDansLot(famille));

        // Sans date de fin
        rente1.setDateFinDroit("");
        Assert.assertFalse(analyseurLot1.isFamilleDansLot(famille));

    }

    @Ignore
    @Test
    public void testRenteCommencantDansAnneeFiscale() {
        rente1.setCodePrestation("10");

        // début de la rente en début d'année, décision avant l'année
        rente1.setDateDebutDroit("01.2011");
        rente1.setDateDecision("31.12.2000");
        Assert.assertTrue(analyseurLot1.isFamilleDansLot(famille));
        Assert.assertFalse(analyseurLot3.isFamilleDansLot(famille));

        // décision dans le mois de la rente
        rente1.setDateDecision("01.01.2011");
        Assert.assertFalse(analyseurLot1.isFamilleDansLot(famille));
        Assert.assertFalse(analyseurLot3.isFamilleDansLot(famille));

        // décision après le mois de la rente
        rente1.setDateDecision("01.02.2011");
        Assert.assertFalse(analyseurLot1.isFamilleDansLot(famille));
        Assert.assertFalse(analyseurLot3.isFamilleDansLot(famille));

        // décision avant la rente (le tout dans l'année)
        rente1.setDateDebutDroit("06.2011");
        rente1.setDateDecision("01.01.2011");
        Assert.assertFalse(analyseurLot1.isFamilleDansLot(famille));
        Assert.assertTrue(analyseurLot3.isFamilleDansLot(famille));
    }

    @Test
    public void testRenteFinissantDansAnneeFiscale() {
        rente1.setCodePrestation("10");
        rente1.setDateDebutDroit("01.2010");

        // inutile de tester une date de fin avant l'année, ce cas n'est pas remonté par la requête SQL

        // fin au début de l'année
        rente1.setDateFinDroit("01.2011");
        Assert.assertFalse(analyseurLot1.isFamilleDansLot(famille));

        // fin au milieu de l'année
        rente1.setDateFinDroit("06.2011");
        Assert.assertFalse(analyseurLot1.isFamilleDansLot(famille));

        // fin à la fin de l'année
        rente1.setDateFinDroit("12.2011");
        Assert.assertFalse(analyseurLot1.isFamilleDansLot(famille));

        // fin après l'année
        rente1.setDateFinDroit("01.2012");
        Assert.assertTrue(analyseurLot1.isFamilleDansLot(famille));
    }
}
