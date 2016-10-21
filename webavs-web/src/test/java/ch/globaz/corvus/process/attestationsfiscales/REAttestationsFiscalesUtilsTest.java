package ch.globaz.corvus.process.attestationsfiscales;

import globaz.corvus.api.retenues.IRERetenues;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class REAttestationsFiscalesUtilsTest {

    private String annee;
    private int anneeAsInteger;

    @Before
    public void setUp() throws Exception {
        annee = "2011";
        anneeAsInteger = Integer.parseInt(annee);
    }

    @Test
    public void testAvecDecisionPendantAnneeFiscale() {

        // on veut tester le fait avoir une rente commen�ant dans l'ann�e fiscale avec une d�cision sans r�tro

        REFamillePourAttestationsFiscales famille = new REFamillePourAttestationsFiscales();

        RETiersPourAttestationsFiscales tiers1 = new RETiersPourAttestationsFiscales();
        tiers1.setIdTiers("1");
        famille.getMapTiersBeneficiaire().put("1", tiers1);

        RERentePourAttestationsFiscales rente1 = new RERentePourAttestationsFiscales();
        rente1.setIdRenteAccordee("1");
        tiers1.getMapRentes().put("1", rente1);

        // d�cision avant l'ann�e, rente commen�ant dans l'ann�e -> faux
        rente1.setDateDebutDroit("01.2011");
        rente1.setDateDecision("31.12.2010");
        Assert.assertFalse(REAttestationsFiscalesUtils.isAvecDecisionPendantAnneeFiscale(famille, annee));

        // d�cision dans l'ann�e, rente commen�ant apr�s la d�cision (dans l'ann�e) -> OK
        rente1.setDateDebutDroit("02.2011");
        rente1.setDateDecision("31.01.2011");
        Assert.assertTrue(REAttestationsFiscalesUtils.isAvecDecisionPendantAnneeFiscale(famille, annee));

        // d�cision dans l'ann�e, rente commen�ant le m�me mois que la d�cision -> OK
        rente1.setDateDebutDroit("01.2011");
        rente1.setDateDecision("01.01.2011");
        Assert.assertTrue(REAttestationsFiscalesUtils.isAvecDecisionPendantAnneeFiscale(famille, annee));

        // d�cision dans l'ann�e, rente commen�ant avant la d�cision (dans l'ann�e) -> OK
        rente1.setDateDebutDroit("01.2011");
        rente1.setDateDecision("01.02.2011");
        Assert.assertTrue(REAttestationsFiscalesUtils.isAvecDecisionPendantAnneeFiscale(famille, annee));

        // d�cision apr�s l'ann�e, rente commen�ant dans l'ann�e -> faux, on ne prend pas en compte les
        // d�cisions des ann�es futures
        rente1.setDateDebutDroit("10.2011");
        rente1.setDateDecision("01.01.2012");
        Assert.assertFalse(REAttestationsFiscalesUtils.isAvecDecisionPendantAnneeFiscale(famille, annee));
    }

    @Test
    public void testHasPersonneDecedeeDurantAnneeFiscale() {
        REFamillePourAttestationsFiscales famille = new REFamillePourAttestationsFiscales();

        RETiersPourAttestationsFiscales tiers1 = new RETiersPourAttestationsFiscales();
        tiers1.setIdTiers("1");
        famille.getMapTiersBeneficiaire().put("1", tiers1);

        RERentePourAttestationsFiscales rente1 = new RERentePourAttestationsFiscales();
        rente1.setIdRenteAccordee("1");
        tiers1.getMapRentes().put("1", rente1);

        rente1.setCodePrestation("10");
        rente1.setDateDebutDroit("12.2010");

        Assert.assertFalse(REAttestationsFiscalesUtils.hasPersonneDecedeeDurantAnneeFiscale(famille, annee));

        tiers1.setDateDeces("01.01.2011");

        Assert.assertTrue(REAttestationsFiscalesUtils.hasPersonneDecedeeDurantAnneeFiscale(famille, annee));
    }

    @Test
    @Ignore
    public void testHasRenteFinissantDansAnnee() throws Exception {
        REFamillePourAttestationsFiscales famille = new REFamillePourAttestationsFiscales();

        RETiersPourAttestationsFiscales tiers1 = new RETiersPourAttestationsFiscales();
        tiers1.setIdTiers("1");
        famille.getMapTiersBeneficiaire().put("1", tiers1);

        RERentePourAttestationsFiscales rente1 = new RERentePourAttestationsFiscales();
        rente1.setIdRenteAccordee("1");
        tiers1.getMapRentes().put("1", rente1);

        // rente principale
        rente1.setCodePrestation("13");

        // une rente finissant avant l'ann�e fiscale
        rente1.setDateFinDroit("12.2010");
        Assert.assertFalse(REAttestationsFiscalesUtils.hasRenteFinissantDansAnnee(famille, annee));

        // une rente finissant dans l'ann�e fiscale
        rente1.setDateFinDroit("01.2011");
        Assert.assertTrue(REAttestationsFiscalesUtils.hasRenteFinissantDansAnnee(famille, annee));
        rente1.setDateFinDroit("12.2011");
        Assert.assertTrue(REAttestationsFiscalesUtils.hasRenteFinissantDansAnnee(famille, annee));

        // une rente finissant apr�s l'ann�e fiscale
        rente1.setDateFinDroit("01.2012");
        Assert.assertFalse(REAttestationsFiscalesUtils.hasRenteFinissantDansAnnee(famille, annee));

        // une rente sans date de fin
        rente1.setDateFinDroit("");
        Assert.assertFalse(REAttestationsFiscalesUtils.hasRenteFinissantDansAnnee(famille, annee));

        // une rente compl�mentaire finissant dans l'ann�e fiscale
        rente1.setCodePrestation("15");
        rente1.setDateFinDroit("01.2011");
        Assert.assertFalse(REAttestationsFiscalesUtils.hasRenteFinissantDansAnnee(famille, annee));

        rente1.setCodePrestation("13");

        // une rente finissant dans l'ann�e fiscale
        // une deuxi�me rente reprenant le droit mais finissant dans l'ann�e
        RERentePourAttestationsFiscales rente2 = new RERentePourAttestationsFiscales();
        rente2.setIdRenteAccordee("2");
        tiers1.getMapRentes().put("2", rente2);
        rente2.setCodePrestation("10");

        rente1.setDateFinDroit("01.2011");
        rente2.setDateDebutDroit("02.2011");
        rente2.setDateFinDroit("12.2011");
        Assert.assertTrue(REAttestationsFiscalesUtils.hasRenteFinissantDansAnnee(famille, annee));

        // une deuxi�me rente finissant apr�s l'ann�e fiscale
        rente2.setDateFinDroit("01.2012");
        Assert.assertFalse(REAttestationsFiscalesUtils.hasRenteFinissantDansAnnee(famille, annee));

        // une deuxi�me rente sans date de fin
        rente2.setDateFinDroit("");
        Assert.assertFalse(REAttestationsFiscalesUtils.hasRenteFinissantDansAnnee(famille, annee));
    }

    @Test
    public void testImpotSource() {
        // famille vide
        REFamillePourAttestationsFiscales famille = new REFamillePourAttestationsFiscales();
        Assert.assertFalse(REAttestationsFiscalesUtils.hasImpotSourceVerseeDansAnnee(famille, annee));

        // un tiers b�n�ficiaire avec une rente sans retenue
        RETiersPourAttestationsFiscales tiers1 = new RETiersPourAttestationsFiscales();
        tiers1.setIdTiers("1");
        famille.getMapTiersBeneficiaire().put("1", tiers1);

        RERentePourAttestationsFiscales rente1 = new RERentePourAttestationsFiscales();
        rente1.setIdRenteAccordee("1");
        tiers1.getMapRentes().put("1", rente1);

        Assert.assertFalse(REAttestationsFiscalesUtils.hasImpotSourceVerseeDansAnnee(famille, annee));

        // deuxi�me tiers avec une rente ayant une retenue
        RETiersPourAttestationsFiscales tiers2 = new RETiersPourAttestationsFiscales();
        tiers2.setIdTiers("2");
        famille.getMapTiersBeneficiaire().put("2", tiers2);

        RERentePourAttestationsFiscales rente2 = new RERentePourAttestationsFiscales();
        rente2.setIdRenteAccordee("2");
        tiers2.getMapRentes().put("2", rente2);

        RERetenuePourAttestationsFiscales retenue1 = new RERetenuePourAttestationsFiscales();
        retenue1.setIdRetenue("1");
        rente2.getMapRetenues().put("1", retenue1);

        // retenue, de type imp�t � la source, couvrant l'ann�e pr�c�dant l'ann�e fiscale
        retenue1.setCsType(IRERetenues.CS_TYPE_IMPOT_SOURCE);
        retenue1.setDateDebut("01.2010");
        retenue1.setDateFin("12.2010");
        Assert.assertFalse(REAttestationsFiscalesUtils.hasImpotSourceVerseeDansAnnee(famille, annee));

        // retenue couvrant le premier mois de l'ann�e fiscale
        retenue1.setDateFin("01.2011");
        Assert.assertTrue(REAttestationsFiscalesUtils.hasImpotSourceVerseeDansAnnee(famille, annee));

        // retenue couvrant le d�but de l'ann�e fiscale
        retenue1.setDateDebut("01.2011");
        retenue1.setDateFin("06.2011");
        Assert.assertTrue(REAttestationsFiscalesUtils.hasImpotSourceVerseeDansAnnee(famille, annee));

        // retenue couvrant une partie de l'ann�e fiscale
        retenue1.setDateDebut("03.2011");
        retenue1.setDateFin("09.2011");
        Assert.assertTrue(REAttestationsFiscalesUtils.hasImpotSourceVerseeDansAnnee(famille, annee));

        // retenue couvrant la fin de l'ann�e fiscale
        retenue1.setDateDebut("06.2011");
        retenue1.setDateFin("12.2011");
        Assert.assertTrue(REAttestationsFiscalesUtils.hasImpotSourceVerseeDansAnnee(famille, annee));

        // retenue chevauchant la fin de l'ann�e fiscale
        retenue1.setDateDebut("09.2011");
        retenue1.setDateFin("03.2012");
        Assert.assertTrue(REAttestationsFiscalesUtils.hasImpotSourceVerseeDansAnnee(famille, annee));

        // retenue commen�ant en d�cembre de l'ann�e fiscale
        retenue1.setDateDebut("12.2011");
        Assert.assertTrue(REAttestationsFiscalesUtils.hasImpotSourceVerseeDansAnnee(famille, annee));

        // retenue commen�ant en janvier de l'ann�e suivant l'ann�e fiscale
        retenue1.setDateDebut("01.2012");
        Assert.assertFalse(REAttestationsFiscalesUtils.hasImpotSourceVerseeDansAnnee(famille, annee));

        // retenue couvrant toute l'ann�e fiscale
        retenue1.setDateDebut("01.2011");
        retenue1.setDateFin("12.2011");
        Assert.assertTrue(REAttestationsFiscalesUtils.hasImpotSourceVerseeDansAnnee(famille, annee));

        // retenue d'un autre type couvrant toute l'ann��e
        retenue1.setCsType(IRERetenues.CS_TYPE_FACTURE_EXISTANTE);
        Assert.assertFalse(REAttestationsFiscalesUtils.hasImpotSourceVerseeDansAnnee(famille, annee));
    }

    @Test
    public void testRenteBloquee() {
        // famille vide
        REFamillePourAttestationsFiscales famille = new REFamillePourAttestationsFiscales();
        Assert.assertFalse(REAttestationsFiscalesUtils.hasRenteBloquee(famille, annee));

        // un tiers b�n�ficiaire avec une rente quelconque
        RETiersPourAttestationsFiscales tiers1 = new RETiersPourAttestationsFiscales();
        tiers1.setIdTiers("1");
        famille.getMapTiersBeneficiaire().put("1", tiers1);

        RERentePourAttestationsFiscales rente1 = new RERentePourAttestationsFiscales();
        rente1.setIdRenteAccordee("1");
        rente1.setDateDebutDroit("01.2000");
        tiers1.getMapRentes().put("1", rente1);

        Assert.assertFalse(REAttestationsFiscalesUtils.hasRenteBloquee(famille, annee));

        // rente bloqu�e et en cours � la fin de l'ann�e fiscale
        rente1.setIsRenteBloquee(true);
        Assert.assertTrue(REAttestationsFiscalesUtils.hasRenteBloquee(famille, annee));

        // rente bloqu�e avec date de fin de droit avant l'ann�e fiscale
        rente1.setDateFinDroit("12.2010");
        Assert.assertFalse(REAttestationsFiscalesUtils.hasRenteBloquee(famille, annee));

        // rente bloqu�e avec date de fin de droit dans l'ann�e fiscale
        rente1.setDateFinDroit("01.2011");
        Assert.assertFalse(REAttestationsFiscalesUtils.hasRenteBloquee(famille, annee));
        rente1.setDateFinDroit("12.2011");
        Assert.assertFalse(REAttestationsFiscalesUtils.hasRenteBloquee(famille, annee));

        // rente bloqu�e avec date de fin de droit apr�s l'ann�e fiscale
        rente1.setDateFinDroit("01.2012");
        Assert.assertTrue(REAttestationsFiscalesUtils.hasRenteBloquee(famille, annee));
    }

    @Test
    public void testRetro() {

        REFamillePourAttestationsFiscales famille = new REFamillePourAttestationsFiscales();

        RETiersPourAttestationsFiscales tiers1 = new RETiersPourAttestationsFiscales();
        tiers1.setIdTiers("1");
        famille.getMapTiersBeneficiaire().put("1", tiers1);

        RERentePourAttestationsFiscales rente1 = new RERentePourAttestationsFiscales();
        rente1.setIdRenteAccordee("1");
        rente1.setCodePrestation("10");
        tiers1.getMapRentes().put("1", rente1);

        // D�cision avant l'ann�e, rente d�butant avant l'ann�e mais avec r�tro
        rente1.setDateDebutDroit("11.2010");
        rente1.setDateDecision("31.12.2010");
        Assert.assertFalse(REAttestationsFiscalesUtils.hasRetro(famille, anneeAsInteger));

        // D�cision dans l'ann�e, rente d�butant avant l'ann�e (donc r�tro)
        rente1.setDateDebutDroit("12.2010");
        rente1.setDateDecision("01.01.2011");
        Assert.assertTrue(REAttestationsFiscalesUtils.hasRetro(famille, anneeAsInteger));

        // D�cision dans l'ann�e, rente d�butant dans l'ann�e avec r�tro
        rente1.setDateDebutDroit("01.2011");
        rente1.setDateDecision("01.02.2011");
        Assert.assertTrue(REAttestationsFiscalesUtils.hasRetro(famille, anneeAsInteger));

        // D�cision dans l'ann�e, rente d�butant dans le m�me mois que la d�cision -> r�tro
        rente1.setDateDebutDroit("02.2011");
        rente1.setDateDecision("01.02.2011");
        Assert.assertTrue(REAttestationsFiscalesUtils.hasRetro(famille, anneeAsInteger));

        // D�cision dans l'ann�e, rente d�butant dans l'ann�e, sans r�tro
        rente1.setDateDebutDroit("03.2011");
        rente1.setDateDecision("01.02.2011");
        Assert.assertFalse(REAttestationsFiscalesUtils.hasRetro(famille, anneeAsInteger));

        // D�cision dans l'ann�e suivante, rente dans l'ann�e, avec r�tro
        rente1.setDateDebutDroit("10.2011");
        rente1.setDateDecision("01.01.2012");
        Assert.assertFalse(REAttestationsFiscalesUtils.hasRetro(famille, anneeAsInteger));
    }

    @Test
    public void testSansDecisionPendantEtApresAnneeFiscale() {
        REFamillePourAttestationsFiscales famille = new REFamillePourAttestationsFiscales();

        RETiersPourAttestationsFiscales tiers1 = new RETiersPourAttestationsFiscales();
        tiers1.setIdTiers("1");
        famille.getMapTiersBeneficiaire().put("1", tiers1);

        RERentePourAttestationsFiscales rente1 = new RERentePourAttestationsFiscales();
        rente1.setIdRenteAccordee("1");
        tiers1.getMapRentes().put("1", rente1);

        // sans date de d�cision
        Assert.assertTrue(REAttestationsFiscalesUtils.isSansDecisionPendantEtApresAnneeFiscale(famille, annee));

        // juste avant l'ann�e fiscale
        rente1.setDateDecision("31.12.2010");
        Assert.assertTrue(REAttestationsFiscalesUtils.isSansDecisionPendantEtApresAnneeFiscale(famille, annee));

        // 1er janvier de l'ann�e fiscale
        rente1.setDateDecision("01.01.2011");
        Assert.assertFalse(REAttestationsFiscalesUtils.isSansDecisionPendantEtApresAnneeFiscale(famille, annee));

        // 31 d�cembre de l'ann�e fiscale
        rente1.setDateDecision("31.12.2011");
        Assert.assertFalse(REAttestationsFiscalesUtils.isSansDecisionPendantEtApresAnneeFiscale(famille, annee));

        // juste apr�s l'ann�e fiscale
        rente1.setDateDecision("01.01.2012");
        Assert.assertFalse(REAttestationsFiscalesUtils.isSansDecisionPendantEtApresAnneeFiscale(famille, annee));

        rente1.setDateDecision("31.12.2010");

        // deuxi�me rente
        RERentePourAttestationsFiscales rente2 = new RERentePourAttestationsFiscales();
        rente2.setIdRenteAccordee("2");
        tiers1.getMapRentes().put("2", rente2);

        // 2�me rente : d�cision juste avant l'ann�e fiscale
        rente2.setDateDecision("31.12.2010");
        Assert.assertTrue(REAttestationsFiscalesUtils.isSansDecisionPendantEtApresAnneeFiscale(famille, annee));

        // 2�me rente : d�cision au 1er janvier de l'ann�e fiscale
        rente2.setDateDecision("01.01.2011");
        Assert.assertFalse(REAttestationsFiscalesUtils.isSansDecisionPendantEtApresAnneeFiscale(famille, annee));

        // 2�me rente : d�cision au 31 d�cembre de l'ann�e fiscale
        rente2.setDateDecision("31.12.2011");
        Assert.assertFalse(REAttestationsFiscalesUtils.isSansDecisionPendantEtApresAnneeFiscale(famille, annee));

        // 2�me rente : d�cision juste apr�s l'ann�e fiscale
        rente2.setDateDecision("01.01.2012");
        Assert.assertFalse(REAttestationsFiscalesUtils.isSansDecisionPendantEtApresAnneeFiscale(famille, annee));
    }
}
