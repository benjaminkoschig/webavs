package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import static org.mockito.Mockito.*;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.CompteAnnexeResolver;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.PrestationOvDecompte;

public class GeneratePerstationPeriodeDecompteTestCase {

    @Spy
    private PrestationOvDecompte decompteMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        doReturn("Mock RefPaiement").when(decompteMock).concatRefPaiement();
    }

    @BeforeClass
    public static void before() {
        CompteAnnexeResolver.addComptesAnnexes(CompteAnnexeFactory.generateComptesAnnexes());

    }

    // @Test(expected)
    // public void testCompteAnnexeRequerant() throws OrdreVersementException {
    // List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
    // OrdreVersementForList ovList = OrdreVersementFactory.generateOvList("1");
    // OrdreVersementForList ovListConjoint = OrdreVersementFactory.generateOvListConjoint("1");
    // ovList.setIdCompteAnnexeConjoint("40");
    // ovs.add(ovList);
    // ovListConjoint.setIdCompteAnnexeConjoint("40");
    // ovs.add(ovListConjoint);
    // ovs.add(OrdreVersementFactory.generateOvCreancier("250", "25041856", "1"));
    //
    // PrestationOvDecompte decomtpe = GeneratePerstationPeriode.generatePersationPeriode(ovs,
    // this.generateComptesAnnexes());
    // Assert.assertEquals(ovList.getIdTiersRequerant(), decomtpe.getIdTiersRequerant());
    // Assert.assertEquals(ovList.getIdCompteAnnexeRequerant(), decomtpe.getIdCompteAnnexeRequerant());
    // Assert.assertEquals(ovListConjoint.getSimpleOrdreVersement().getIdTiers(), decomtpe.getIdTiersConjoint());
    // Assert.assertEquals(ovListConjoint.getIdCompteAnnexeConjoint(), decomtpe.getIdCompteAnnexeConjoint());
    // }

    @Test
    public void given8OvSouldBe4PeriodeAllWithRestitution() throws OrdreVersementException, ComptabiliserLotException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaireSpecifiedIdPca("1", "10"));
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaireSpecifiedIdPca("2", "20"));
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaireSpecifiedIdPca("3", "30"));
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaireSpecifiedIdPca("4", "40"));
        ovs.add(OrdreVersementFactory.generateOvListRestitutionSpecifiedIdPca("1", "10"));
        ovs.add(OrdreVersementFactory.generateOvListRestitutionSpecifiedIdPca("2", "10"));
        ovs.add(OrdreVersementFactory.generateOvListRestitutionSpecifiedIdPca("3", "10"));
        ovs.add(OrdreVersementFactory.generateOvListRestitutionSpecifiedIdPca("4", "20"));

        PrestationOvDecompte decomtpe = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteMock);

        Assert.assertEquals(4, decomtpe.getPrestationsPeriodes().size());

        for (PrestationPeriode periode : decomtpe.getPrestationsPeriodes()) {
            if (periode.getRequerant().getRestitution() == null) {
                Assert.fail("Pas de restituion trouvé");
            }
        }

    }

    @Test
    public void testCompteAnnexeRequerant() throws OrdreVersementException, ComptabiliserLotException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        OrdreVersementForList ovList = OrdreVersementFactory.generateOvListBeneficiaire("1");
        OrdreVersementForList ovListConjoint = OrdreVersementFactory.generateOvListConjoint("1");
        ovList.setIdCompteAnnexeConjoint("20");
        ovs.add(ovList);
        ovListConjoint.setIdCompteAnnexeConjoint("20");
        ovs.add(ovListConjoint);
        ovs.add(OrdreVersementFactory.generateOvListCreancier("250", "25041856", "1"));

        PrestationOvDecompte decomtpe = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteMock);
        Assert.assertEquals(ovList.getIdTiersRequerant(), decomtpe.getIdTiersRequerant());
        Assert.assertEquals(ovList.getIdCompteAnnexeRequerant(), decomtpe.getIdCompteAnnexeRequerant());
        Assert.assertEquals(ovListConjoint.getSimpleOrdreVersement().getIdTiers(), decomtpe.getIdTiersConjoint());
        Assert.assertEquals(ovListConjoint.getIdCompteAnnexeConjoint(), decomtpe.getIdCompteAnnexeConjoint());
    }

    @Test
    public void testGeneratePresationPeriode() throws OrdreVersementException, ComptabiliserLotException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteMock);
    }

    @Test
    public void testGeneratePresationPeriodeAllocationNoel() throws OrdreVersementException, ComptabiliserLotException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvAllocationNoel(null));
        ovs.add(OrdreVersementFactory.generateOvAllocationNoel(null));

        PrestationOvDecompte decomtpe = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteMock);
        Assert.assertEquals(2, decomtpe.getAllocationsNoel().size());
        Assert.assertEquals(0, decomtpe.getDettes().size());
        Assert.assertEquals(0, decomtpe.getJoursAppoint().size());
        Assert.assertEquals(0, decomtpe.getPrestationsPeriodes().size());
        Assert.assertEquals(0, decomtpe.getCreanciers().size());

    }

    @Test
    public void testGeneratePresationPeriodeCreancier() throws OrdreVersementException, ComptabiliserLotException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListCreancier("100", "1454", "1"));
        PrestationOvDecompte decomtpe = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteMock);
        Assert.assertEquals(0, decomtpe.getAllocationsNoel().size());
        Assert.assertEquals(0, decomtpe.getDettes().size());
        Assert.assertEquals(1, decomtpe.getCreanciers().size());
        Assert.assertEquals(0, decomtpe.getJoursAppoint().size());
        Assert.assertEquals(0, decomtpe.getPrestationsPeriodes().size());
    }

    @Test
    public void testGeneratePresationPeriodeJourAppoint() throws OrdreVersementException, ComptabiliserLotException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        OrdreVersementForList beneficiaire = OrdreVersementFactory.generateOvListBeneficiaire("1");

        ovs.add(beneficiaire);
        ovs.add(OrdreVersementFactory.generateOvListJourAppoint("152", beneficiaire.getSimpleOrdreVersement()
                .getIdPca(), beneficiaire.getSimpleOrdreVersement().getNoGroupePeriode()));

        BigDecimal sum = new BigDecimal(beneficiaire.getSimpleOrdreVersement().getMontant()).add(new BigDecimal(152));

        PrestationOvDecompte decomtpe = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteMock);
        Assert.assertEquals(0, decomtpe.getAllocationsNoel().size());
        Assert.assertEquals(0, decomtpe.getDettes().size());
        // Assert.assertEquals(1, decomtpe.getJoursAppoint().size());
        Assert.assertEquals(1, decomtpe.getPrestationsPeriodes().size());
        Assert.assertEquals(sum, decomtpe.getPrestationsPeriodes().get(0).getRequerant().getMontantBeneficiaire());
    }

    @Test
    public void testGeneratePresationPeriodeStandard() throws OrdreVersementException, ComptabiliserLotException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        OrdreVersementForList ovList = OrdreVersementFactory.generateOvListBeneficiaire("1");
        ovs.add(ovList);
        PrestationOvDecompte decomtpe = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteMock);
        Assert.assertEquals(0, decomtpe.getAllocationsNoel().size());
        Assert.assertEquals(0, decomtpe.getDettes().size());
        Assert.assertEquals(0, decomtpe.getCreanciers().size());
        Assert.assertEquals(0, decomtpe.getJoursAppoint().size());
        Assert.assertEquals(1, decomtpe.getPrestationsPeriodes().size());
        Assert.assertNotNull(decomtpe.getPrestationsPeriodes().get(0).getRequerant().getBeneficiaire());
        Assert.assertNull(decomtpe.getPrestationsPeriodes().get(0).getRequerant().getRestitution());
        Assert.assertNull(decomtpe.getPrestationsPeriodes().get(0).getConjoint());
    }

    @Test
    public void testGeneratePresationPeriodeTestPeriode() throws OrdreVersementException, ComptabiliserLotException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("1"));
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("2"));
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("3"));
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("4"));
        PrestationOvDecompte decomtpe = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteMock);
        Assert.assertEquals(0, decomtpe.getAllocationsNoel().size());
        Assert.assertEquals(0, decomtpe.getDettes().size());
        Assert.assertEquals(0, decomtpe.getJoursAppoint().size());
        Assert.assertEquals(0, decomtpe.getCreanciers().size());

        Assert.assertEquals(4, decomtpe.getPrestationsPeriodes().size());
        Assert.assertNotNull(decomtpe.getPrestationsPeriodes().get(0).getRequerant().getBeneficiaire());
        Assert.assertNull(decomtpe.getPrestationsPeriodes().get(0).getRequerant().getRestitution());
        Assert.assertNull(decomtpe.getPrestationsPeriodes().get(0).getConjoint());
    }

    @Test
    public void testGeneratePresationPeriodeTestPeriodeWithConjoint() throws OrdreVersementException,
            ComptabiliserLotException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListConjoint("2"));
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("2"));
        ovs.add(OrdreVersementFactory.generateOvListConjoint("1"));
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("1"));
        PrestationOvDecompte decomtpe = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteMock);
        Assert.assertEquals(0, decomtpe.getAllocationsNoel().size());
        Assert.assertEquals(0, decomtpe.getDettes().size());
        Assert.assertEquals(0, decomtpe.getCreanciers().size());
        Assert.assertEquals(0, decomtpe.getJoursAppoint().size());
        Assert.assertEquals(2, decomtpe.getPrestationsPeriodes().size());

        Assert.assertEquals("10", decomtpe.getPrestationsPeriodes().get(0).getRequerant().getIdCompteAnnexe());
        Assert.assertEquals("20", decomtpe.getPrestationsPeriodes().get(0).getConjoint().getIdCompteAnnexe());

        Assert.assertNotNull(decomtpe.getPrestationsPeriodes().get(0).getRequerant().getBeneficiaire());
        Assert.assertNotNull(decomtpe.getPrestationsPeriodes().get(0).getConjoint().getBeneficiaire());
        Assert.assertNull(decomtpe.getPrestationsPeriodes().get(0).getRequerant().getRestitution());
        Assert.assertNull(decomtpe.getPrestationsPeriodes().get(0).getConjoint().getRestitution());
    }

    @Test
    public void testGeneratePresationPeriodeTestPeriodeWithConjointAndRestitution() throws OrdreVersementException,
            ComptabiliserLotException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("2"));
        ovs.add(OrdreVersementFactory.generateOvListRestitution("2"));
        ovs.add(OrdreVersementFactory.generateOvListConjoint("2"));
        ovs.add(OrdreVersementFactory.generateOvListRestitutionConjoint("2"));
        ovs.add(OrdreVersementFactory.generateOvListConjoint("1"));
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("1"));
        PrestationOvDecompte decomtpe = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteMock);
        Assert.assertEquals(0, decomtpe.getAllocationsNoel().size());
        Assert.assertEquals(0, decomtpe.getDettes().size());
        Assert.assertEquals(0, decomtpe.getCreanciers().size());
        Assert.assertEquals(0, decomtpe.getJoursAppoint().size());
        Assert.assertEquals(2, decomtpe.getPrestationsPeriodes().size());

        Assert.assertNotNull(decomtpe.getPrestationsPeriodes().get(1).getRequerant().getBeneficiaire());
        Assert.assertNotNull(decomtpe.getPrestationsPeriodes().get(1).getRequerant().getRestitution());
        Assert.assertNotNull(decomtpe.getPrestationsPeriodes().get(1).getConjoint().getBeneficiaire());
        Assert.assertNotNull(decomtpe.getPrestationsPeriodes().get(1).getConjoint().getRestitution());
    }

    @Test
    public void testIdAdressePaiement() throws OrdreVersementException, ComptabiliserLotException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("2"));
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("1"));

        OrdreVersementForList ovList = OrdreVersementFactory.generateOvListBeneficiaire("3");
        ovList.getSimpleOrdreVersement().setIdTiersAdressePaiement("1");
        ovList.setIdCompteAnnexeConjoint(null);
        ovList.getSimpleOrdreVersement().setIdDomaineApplication("111");
        ovs.add(ovList);

        OrdreVersementForList ovListConjoint = OrdreVersementFactory.generateOvListConjoint("3");
        ovListConjoint.setIdCompteAnnexeConjoint("20");
        ovListConjoint.getSimpleOrdreVersement().setIdTiersAdressePaiementConjoint("2");
        ovListConjoint.getSimpleOrdreVersement().setIdDomaineApplication("222");
        ovs.add(ovListConjoint);
        ovs.add(OrdreVersementFactory.generateOvListConjoint("1"));
        ovs.add(OrdreVersementFactory.generateOvListConjoint("2"));

        PrestationOvDecompte decomtpe = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteMock);

        Assert.assertEquals(ovList.getSimpleOrdreVersement().getIdTiersAdressePaiement(),
                decomtpe.getIdTiersAddressePaiementRequerant());
        Assert.assertEquals(ovList.getSimpleOrdreVersement().getIdDomaineApplication(),
                decomtpe.getIdDomaineApplicationRequerant());

        Assert.assertEquals(ovListConjoint.getSimpleOrdreVersement().getIdTiersAdressePaiement(),
                decomtpe.getIdTiersAddressePaiementConjoint());
        Assert.assertEquals(ovListConjoint.getSimpleOrdreVersement().getIdDomaineApplication(),
                decomtpe.getIdDomaineApplicationConjoint());
    }

    @Test
    public void testIdAdressePaiementDom2R() throws OrdreVersementException, ComptabiliserLotException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("3"));
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("2"));

        OrdreVersementForList ovList = OrdreVersementFactory.generateOvListBeneficiaire("1");
        ovList.getSimpleOrdreVersement().setIdTiersAdressePaiement("1");
        ovList.getSimpleOrdreVersement().setIdDomaineApplicationConjoint("222");
        ovList.getSimpleOrdreVersement().setIdTiersAdressePaiementConjoint("2");
        ovList.setIdCompteAnnexeConjoint("20");
        ovList.getSimpleOrdreVersement().setIdDomaineApplication("111");
        ovs.add(ovList);

        PrestationOvDecompte decomtpe = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteMock);

        Assert.assertEquals("11", decomtpe.getIdTiersAddressePaiementRequerant());
        Assert.assertEquals(ovList.getSimpleOrdreVersement().getIdDomaineApplication(),
                decomtpe.getIdDomaineApplicationRequerant());

        Assert.assertEquals(ovList.getSimpleOrdreVersement().getIdTiersAdressePaiementConjoint(),
                decomtpe.getIdTiersAddressePaiementConjoint());
        Assert.assertEquals(ovList.getSimpleOrdreVersement().getIdDomaineApplicationConjoint(),
                decomtpe.getIdDomaineApplicationConjoint());
    }

    @Test
    public void testIdAdressePaiementInverse() throws OrdreVersementException, ComptabiliserLotException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();

        OrdreVersementForList ovListConjoint = OrdreVersementFactory.generateOvListConjoint("3");
        ovListConjoint.setIdCompteAnnexeConjoint("20");
        ovListConjoint.getSimpleOrdreVersement().setIdTiersAdressePaiement("2");
        ovListConjoint.getSimpleOrdreVersement().setIdDomaineApplication("222");
        ovs.add(ovListConjoint);
        ovs.add(OrdreVersementFactory.generateOvListConjoint("1"));
        ovs.add(OrdreVersementFactory.generateOvListConjoint("2"));

        OrdreVersementForList ovList = OrdreVersementFactory.generateOvListBeneficiaire("3");
        ovList.getSimpleOrdreVersement().setIdTiersAdressePaiement("1");
        ovList.setIdCompteAnnexeConjoint("20");
        ovList.getSimpleOrdreVersement().setIdDomaineApplication("111");
        ovs.add(ovList);
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("2"));
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("1"));

        PrestationOvDecompte decomtpe = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteMock);

        Assert.assertEquals(ovList.getSimpleOrdreVersement().getIdTiersAdressePaiement(),
                decomtpe.getIdTiersAddressePaiementRequerant());
        Assert.assertEquals(ovList.getSimpleOrdreVersement().getIdDomaineApplication(),
                decomtpe.getIdDomaineApplicationRequerant());

        Assert.assertEquals(ovListConjoint.getSimpleOrdreVersement().getIdTiersAdressePaiement(),
                decomtpe.getIdTiersAddressePaiementConjoint());

        Assert.assertEquals(ovListConjoint.getSimpleOrdreVersement().getIdDomaineApplication(),
                decomtpe.getIdDomaineApplicationConjoint());
    }

    @Test
    public void testIdRequerantConjoint() throws OrdreVersementException, ComptabiliserLotException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        OrdreVersementForList ovList = OrdreVersementFactory.generateOvListBeneficiaire("1");
        OrdreVersementForList ovListConjoint = OrdreVersementFactory.generateOvListConjoint("1");
        ovList.setIdCompteAnnexeConjoint("20");
        ovList.getSimpleOrdreVersement().setIdDomaineApplication("111");
        ovs.add(ovList);
        ovListConjoint.setIdCompteAnnexeConjoint("20");
        ovListConjoint.getSimpleOrdreVersement().setIdDomaineApplication("222");
        ovs.add(ovListConjoint);
        ovs.add(OrdreVersementFactory.generateOvListCreancier("250", "25041856", "1"));

        PrestationOvDecompte decomtpe = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteMock);
        Assert.assertEquals(ovList.getIdTiersRequerant(), decomtpe.getIdTiersRequerant());
        Assert.assertEquals(ovList.getIdCompteAnnexeRequerant(), decomtpe.getIdCompteAnnexeRequerant());
        Assert.assertEquals(ovList.getSimpleOrdreVersement().getIdTiersAdressePaiement(),
                decomtpe.getIdTiersAddressePaiementRequerant());
        Assert.assertEquals(ovList.getSimpleOrdreVersement().getIdDomaineApplication(),
                decomtpe.getIdDomaineApplicationRequerant());

        Assert.assertEquals(ovListConjoint.getSimpleOrdreVersement().getIdTiers(), decomtpe.getIdTiersConjoint());
        Assert.assertEquals(ovListConjoint.getIdCompteAnnexeConjoint(), decomtpe.getIdCompteAnnexeConjoint());
        Assert.assertEquals(ovListConjoint.getSimpleOrdreVersement().getIdTiersAdressePaiement(),
                decomtpe.getIdTiersAddressePaiementConjoint());
        Assert.assertEquals(ovListConjoint.getSimpleOrdreVersement().getIdDomaineApplication(),
                decomtpe.getIdDomaineApplicationConjoint());
    }

    @Test
    public void testIdRequerantConjointOrdre() throws OrdreVersementException, ComptabiliserLotException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("2"));
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("1"));
        OrdreVersementForList ovList = OrdreVersementFactory.generateOvListBeneficiaire("3");
        ovList.setIdCompteAnnexeConjoint("20");
        ovList.getSimpleOrdreVersement().setIdDomaineApplication("111");
        ovs.add(ovList);

        OrdreVersementForList ovListConjoint = OrdreVersementFactory.generateOvListConjoint("3");
        ovListConjoint.setIdCompteAnnexeConjoint("20");
        ovListConjoint.getSimpleOrdreVersement().setIdDomaineApplication("222");
        ovs.add(ovListConjoint);
        ovs.add(OrdreVersementFactory.generateOvListConjoint("1"));
        ovs.add(OrdreVersementFactory.generateOvListConjoint("2"));

        ovs.add(OrdreVersementFactory.generateOvListCreancier("250", "25041856", "1"));

        PrestationOvDecompte decomtpe = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteMock);

        Assert.assertEquals(ovList.getIdCompteAnnexeRequerant(), decomtpe.getIdCompteAnnexeRequerant());
        Assert.assertEquals(ovListConjoint.getIdCompteAnnexeConjoint(), decomtpe.getIdCompteAnnexeConjoint());
    }

    @Test
    public void testOrdreVersement() throws OrdreVersementException, ComptabiliserLotException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("1"));
        PrestationOvDecompte decomtpe = GeneratePerstationPeriodeDecompte.generatePersationPeriode(ovs, decompteMock);
        Assert.assertEquals(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, decomtpe.getPrestationsPeriodes()
                .get(0).getRequerant().getBeneficiaire().getCsType());

    }

    // @Test
    // public void testSumMontantDisponibleRequerant() throws OrdreVersementException {
    // List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
    // ovs.add(OrdreVersementFactory.generateOvList("1", "1000"));
    // ovs.add(OrdreVersementFactory.generateOvList("2", "1500"));
    // ovs.add(OrdreVersementFactory.generateOvList("3", "1350"));
    //
    // PrestationOvDecompte decomtpe = GeneratePerstationPeriode.generatePersationPeriode(ovs, decompteMock);
    // Assert.assertEquals(new BigDecimal(3850), decomtpe.getMontantRequerant());
    // }

    // @Test
    // public void testSumMontantDisponibleRequerantConjoint() throws OrdreVersementException {
    // List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
    // ovs.add(OrdreVersementFactory.generateOvList("1", "1000"));
    // ovs.add(OrdreVersementFactory.generateOvListConjoint("1", "250"));
    // ovs.add(OrdreVersementFactory.generateOvList("2", "1500"));
    // ovs.add(OrdreVersementFactory.generateOvListConjoint("2", "650"));
    // ovs.add(OrdreVersementFactory.generateOvList("3", "1350"));
    // ovs.add(OrdreVersementFactory.generateOvListConjoint("3", "350"));
    //
    // PrestationOvDecompte decomtpe = GeneratePerstationPeriode.generatePersationPeriode(ovs, decompteMock);
    // Assert.assertEquals(new BigDecimal(3850), decomtpe.getMontantRequerant());
    // Assert.assertEquals(new BigDecimal(1250), decomtpe.getMontantConjoint());
    //
    // }

}
