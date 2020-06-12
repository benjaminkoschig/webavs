package globaz.corvus.process.liste.rentedouble;

import globaz.corvus.db.rentesaccordees.RERenteJoinPersonneAvs;
import globaz.pyxis.api.ITIPersonne;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.corvus.TestUnitaireAvecGenerateurIDUnique;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.prestation.domaine.PrestationAccordee;

public class REAnalyseurRenteDoubleTest extends TestUnitaireAvecGenerateurIDUnique {

    private REAnalyseurRenteDouble testInstance;

    private String getCodesPrestationsFormattees(Set<PrestationAccordee> prestationsAccordees) {
        StringBuilder codesPrestations = new StringBuilder();
        codesPrestations.append("code prest : [");
        for (Iterator<PrestationAccordee> iterator = prestationsAccordees.iterator(); iterator.hasNext();) {
            codesPrestations.append(iterator.next().getCodePrestation());

            if (iterator.hasNext()) {
                codesPrestations.append(",");
            }
        }
        codesPrestations.append("]");
        return codesPrestations.toString();
    }

    private boolean laRenteEstPresente(RERenteJoinPersonneAvs laRente, List<RERenteJoinPersonneAvs> rentes) {
        for (RERenteJoinPersonneAvs uneRenteDeLaListe : rentes) {
            if (uneRenteDeLaListe.getIdPrestationAccordee().equals(laRente.getIdPrestationAccordee())) {
                return true;
            }
        }
        return false;
    }

    @Before
    public void setUp() throws Exception {
        testInstance = new REAnalyseurRenteDouble();
    }

    @Test
    public void testGetNombreRenteAVS() {
        Set<PrestationAccordee> prestationsAccordees = new HashSet<PrestationAccordee>();

        PrestationAccordee uneR10 = new PrestationAccordee();
        uneR10.setId(genererUnIdUnique());
        uneR10.setCodePrestation(CodePrestation.CODE_10);
        prestationsAccordees.add(uneR10);

        Assert.assertEquals(getCodesPrestationsFormattees(prestationsAccordees), 1,
                testInstance.getNombreRenteAVS(prestationsAccordees));

        PrestationAccordee uneR85 = new PrestationAccordee();
        uneR85.setId(genererUnIdUnique());
        uneR85.setCodePrestation(CodePrestation.CODE_85);
        prestationsAccordees.add(uneR85);

        Assert.assertEquals(getCodesPrestationsFormattees(prestationsAccordees), 1,
                testInstance.getNombreRenteAVS(prestationsAccordees));

        PrestationAccordee uneR33 = new PrestationAccordee();
        uneR33.setId(genererUnIdUnique());
        uneR33.setCodePrestation(CodePrestation.CODE_33);
        prestationsAccordees.add(uneR33);

        Assert.assertEquals(getCodesPrestationsFormattees(prestationsAccordees), 2,
                testInstance.getNombreRenteAVS(prestationsAccordees));

        PrestationAccordee uneR13 = new PrestationAccordee();
        uneR13.setId(genererUnIdUnique());
        uneR13.setCodePrestation(CodePrestation.CODE_13);
        prestationsAccordees.add(uneR13);

        Assert.assertEquals(getCodesPrestationsFormattees(prestationsAccordees), 3,
                testInstance.getNombreRenteAVS(prestationsAccordees));
    }

    @Test
    public void testGetRentesDoubles() {
        List<RERenteJoinPersonneAvs> rentes = new ArrayList<RERenteJoinPersonneAvs>();

        // 1er tiers, pas de rentes doubles
        RERenteJoinPersonneAvs tiers1_R81 = new RERenteJoinPersonneAvs();
        tiers1_R81.setIdPrestationAccordee("1");
        tiers1_R81.setCodePrestation("81");
        tiers1_R81.setDateDebutDroit("01.2010");
        tiers1_R81.setMontantPrestation("100.00");
        tiers1_R81.setIdTiers("1");
        tiers1_R81.setNssBeneficiaire("756.1234.5678.97");
        tiers1_R81.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(tiers1_R81);

        RERenteJoinPersonneAvs tiers1_R10 = new RERenteJoinPersonneAvs();
        tiers1_R10.setIdPrestationAccordee("2");
        tiers1_R10.setCodePrestation("10");
        tiers1_R10.setDateDebutDroit("01.2010");
        tiers1_R10.setMontantPrestation("100.00");
        tiers1_R10.setIdTiers("1");
        tiers1_R10.setNssBeneficiaire("756.1234.5678.97");
        tiers1_R10.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(tiers1_R10);

        // 2ème tiers, rentes doubles
        RERenteJoinPersonneAvs tiers2_R13 = new RERenteJoinPersonneAvs();
        tiers2_R13.setIdPrestationAccordee("3");
        tiers2_R13.setCodePrestation("13");
        tiers2_R13.setDateDebutDroit("01.2010");
        tiers2_R13.setMontantPrestation("100.00");
        tiers2_R13.setIdTiers("2");
        tiers2_R13.setNssBeneficiaire("756.1234.5678.97");
        tiers2_R13.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(tiers2_R13);

        RERenteJoinPersonneAvs tiers2_R10 = new RERenteJoinPersonneAvs();
        tiers2_R10.setIdPrestationAccordee("4");
        tiers2_R10.setCodePrestation("10");
        tiers2_R10.setDateDebutDroit("01.2010");
        tiers2_R10.setMontantPrestation("100.00");
        tiers2_R10.setIdTiers("2");
        tiers2_R10.setNssBeneficiaire("756.1234.5678.97");
        tiers2_R10.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(tiers2_R10);

        // 3ème tiers, rentes doubles
        RERenteJoinPersonneAvs tiers3_R82 = new RERenteJoinPersonneAvs();
        tiers3_R82.setIdPrestationAccordee("5");
        tiers3_R82.setCodePrestation("82");
        tiers3_R82.setDateDebutDroit("01.2010");
        tiers3_R82.setMontantPrestation("100.00");
        tiers3_R82.setIdTiers("3");
        tiers3_R82.setNssBeneficiaire("756.1234.5678.97");
        tiers3_R82.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(tiers3_R82);

        RERenteJoinPersonneAvs tiers3_R91 = new RERenteJoinPersonneAvs();
        tiers3_R91.setIdPrestationAccordee("6");
        tiers3_R91.setCodePrestation("91");
        tiers3_R91.setDateDebutDroit("01.2010");
        tiers3_R91.setMontantPrestation("100.00");
        tiers3_R91.setIdTiers("3");
        tiers3_R91.setNssBeneficiaire("756.1234.5678.97");
        tiers3_R91.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(tiers3_R91);

        // 4ème tiers, rentes doubles
        RERenteJoinPersonneAvs tiers4_R110 = new RERenteJoinPersonneAvs();
        tiers4_R110.setIdPrestationAccordee("7");
        tiers4_R110.setCodePrestation("110");
        tiers4_R110.setDateDebutDroit("01.2010");
        tiers4_R110.setMontantPrestation("100.00");
        tiers4_R110.setIdTiers("4");
        tiers4_R110.setNssBeneficiaire("756.1234.5678.97");
        tiers4_R110.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(tiers4_R110);

        RERenteJoinPersonneAvs tiers4_R113 = new RERenteJoinPersonneAvs();
        tiers4_R113.setIdPrestationAccordee("8");
        tiers4_R113.setCodePrestation("113");
        tiers4_R113.setDateDebutDroit("01.2010");
        tiers4_R113.setMontantPrestation("100.00");
        tiers4_R113.setIdTiers("4");
        tiers4_R113.setNssBeneficiaire("756.1234.5678.97");
        tiers4_R113.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(tiers4_R113);

        // 5ème tiers, rentes doubles
        RERenteJoinPersonneAvs tiers5_R10 = new RERenteJoinPersonneAvs();
        tiers5_R10.setIdPrestationAccordee("9");
        tiers5_R10.setCodePrestation("10");
        tiers5_R10.setDateDebutDroit("01.2010");
        tiers5_R10.setMontantPrestation("100.00");
        tiers5_R10.setIdTiers("5");
        tiers5_R10.setNssBeneficiaire("756.1234.5678.97");
        tiers5_R10.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(tiers5_R10);

        RERenteJoinPersonneAvs tiers5_R34 = new RERenteJoinPersonneAvs();
        tiers5_R34.setIdPrestationAccordee("10");
        tiers5_R34.setCodePrestation("34");
        tiers5_R34.setDateDebutDroit("01.2010");
        tiers5_R34.setMontantPrestation("100.00");
        tiers5_R34.setIdTiers("5");
        tiers5_R34.setNssBeneficiaire("756.1234.5678.97");
        tiers5_R34.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(tiers5_R34);

        RERenteJoinPersonneAvs tiers5_R45 = new RERenteJoinPersonneAvs();
        tiers5_R45.setIdPrestationAccordee("11");
        tiers5_R45.setCodePrestation("45");
        tiers5_R45.setDateDebutDroit("01.2010");
        tiers5_R45.setMontantPrestation("100.00");
        tiers5_R45.setIdTiers("5");
        tiers5_R45.setNssBeneficiaire("756.1234.5678.97");
        tiers5_R45.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(tiers5_R45);

        List<RERenteJoinPersonneAvs> rentesDoubles = testInstance.getRentesDoubles(rentes);
        Assert.assertFalse(laRenteEstPresente(tiers1_R10, rentesDoubles));
        Assert.assertFalse(laRenteEstPresente(tiers1_R81, rentesDoubles));
        Assert.assertTrue(laRenteEstPresente(tiers2_R10, rentesDoubles));
        Assert.assertTrue(laRenteEstPresente(tiers2_R13, rentesDoubles));
        Assert.assertTrue(laRenteEstPresente(tiers3_R82, rentesDoubles));
        Assert.assertTrue(laRenteEstPresente(tiers3_R91, rentesDoubles));
        Assert.assertTrue(laRenteEstPresente(tiers4_R110, rentesDoubles));
        Assert.assertTrue(laRenteEstPresente(tiers4_R113, rentesDoubles));
        Assert.assertTrue(laRenteEstPresente(tiers5_R10, rentesDoubles));
        Assert.assertTrue(laRenteEstPresente(tiers5_R34, rentesDoubles));
        Assert.assertTrue(laRenteEstPresente(tiers5_R45, rentesDoubles));

    }
    @Ignore
    @Test
    public void testGetRentesDoublesLPART() {
        List<RERenteJoinPersonneAvs> rentes = new ArrayList<RERenteJoinPersonneAvs>();

        // 1er tiers, pas de rentes doubles
        RERenteJoinPersonneAvs tiers1_R55 = new RERenteJoinPersonneAvs();
        tiers1_R55.setIdPrestationAccordee("1");
        tiers1_R55.setCodePrestation("55");
        tiers1_R55.setDateDebutDroit("05.2010");
        tiers1_R55.setMontantPrestation("100.00");
        tiers1_R55.setIdTiers("1");
        tiers1_R55.setNssBeneficiaire("756.1234.5678.97");
        tiers1_R55.setIdTiersNssCompl1("165264");
        tiers1_R55.setCodeSpecial1("60");
        tiers1_R55.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(tiers1_R55);

        RERenteJoinPersonneAvs tiers2_R55 = new RERenteJoinPersonneAvs();
        tiers2_R55.setIdPrestationAccordee("2");
        tiers2_R55.setCodePrestation("55");
        tiers2_R55.setDateDebutDroit("05.2010");
        tiers2_R55.setMontantPrestation("200.00");
        tiers2_R55.setIdTiers("1");
        tiers2_R55.setNssBeneficiaire("756.1234.5678.97");
        tiers2_R55.setIdTiersNssCompl1("179869");
        tiers1_R55.setCodeSpecial1("60");
        tiers2_R55.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(tiers2_R55);

        List<RERenteJoinPersonneAvs> rentesDoubles = testInstance.getRentesDoubles(rentes);
        Assert.assertFalse(laRenteEstPresente(tiers1_R55, rentesDoubles));

    }



    @Test
    public void testHasDeuxAPI() {
        Set<PrestationAccordee> prestationsAccordees = new HashSet<PrestationAccordee>();

        PrestationAccordee uneRenteAPI = new PrestationAccordee();
        uneRenteAPI.setId(genererUnIdUnique());
        uneRenteAPI.setCodePrestation(CodePrestation.CODE_81);
        prestationsAccordees.add(uneRenteAPI);

        Assert.assertFalse(getCodesPrestationsFormattees(prestationsAccordees),
                testInstance.hasDeuxAPI(prestationsAccordees));

        PrestationAccordee uneRenteAVS = new PrestationAccordee();
        uneRenteAVS.setId(genererUnIdUnique());
        uneRenteAVS.setCodePrestation(CodePrestation.CODE_10);
        prestationsAccordees.add(uneRenteAVS);

        Assert.assertFalse(getCodesPrestationsFormattees(prestationsAccordees),
                testInstance.hasDeuxAPI(prestationsAccordees));

        PrestationAccordee uneAutreAPI = new PrestationAccordee();
        uneAutreAPI.setId(genererUnIdUnique());
        uneAutreAPI.setCodePrestation(CodePrestation.CODE_97);

        prestationsAccordees.clear();
        prestationsAccordees.add(uneRenteAPI);
        prestationsAccordees.add(uneAutreAPI);

        Assert.assertTrue(getCodesPrestationsFormattees(prestationsAccordees),
                testInstance.hasDeuxAPI(prestationsAccordees));
    }

    @Test
    public void testHasDeuxPC() {
        Set<PrestationAccordee> prestationsAccordees = new HashSet<PrestationAccordee>();

        PrestationAccordee uneRentePC = new PrestationAccordee();
        uneRentePC.setId(genererUnIdUnique());
        uneRentePC.setCodePrestation(CodePrestation.CODE_110);
        prestationsAccordees.add(uneRentePC);

        Assert.assertFalse(getCodesPrestationsFormattees(prestationsAccordees),
                testInstance.hasDeuxPC(prestationsAccordees));

        PrestationAccordee uneRenteRFM = new PrestationAccordee();
        uneRenteRFM.setId(genererUnIdUnique());
        uneRenteRFM.setCodePrestation(CodePrestation.CODE_250);
        prestationsAccordees.add(uneRenteRFM);

        Assert.assertFalse(getCodesPrestationsFormattees(prestationsAccordees),
                testInstance.hasDeuxPC(prestationsAccordees));

        PrestationAccordee uneAutrePC = new PrestationAccordee();
        uneAutrePC.setId(genererUnIdUnique());
        uneAutrePC.setCodePrestation(CodePrestation.CODE_150);
        prestationsAccordees.clear();
        prestationsAccordees.add(uneRentePC);
        prestationsAccordees.add(uneAutrePC);

        Assert.assertTrue(getCodesPrestationsFormattees(prestationsAccordees),
                testInstance.hasDeuxPC(prestationsAccordees));
    }

    @Test
    public void testHasDeuxRentesAVS() {
        Set<PrestationAccordee> prestationsAccordees = new HashSet<PrestationAccordee>();

        PrestationAccordee uneRenteAVS = new PrestationAccordee();
        uneRenteAVS.setId(genererUnIdUnique());
        uneRenteAVS.setCodePrestation(CodePrestation.CODE_10);
        prestationsAccordees.add(uneRenteAVS);

        Assert.assertFalse(getCodesPrestationsFormattees(prestationsAccordees),
                testInstance.hasDeuxRentesAVS(prestationsAccordees));

        PrestationAccordee uneRenteSurvivant = new PrestationAccordee();
        uneRenteSurvivant.setId(genererUnIdUnique());
        uneRenteSurvivant.setCodePrestation(CodePrestation.CODE_85);
        prestationsAccordees.add(uneRenteSurvivant);

        Assert.assertFalse(getCodesPrestationsFormattees(prestationsAccordees),
                testInstance.hasDeuxRentesAVS(prestationsAccordees));

        PrestationAccordee uneAutreAVS = new PrestationAccordee();
        uneAutreAVS.setCodePrestation(CodePrestation.CODE_33);
        prestationsAccordees.clear();
        prestationsAccordees.add(uneRenteAVS);
        prestationsAccordees.add(uneAutreAVS);

        Assert.assertTrue(getCodesPrestationsFormattees(prestationsAccordees),
                testInstance.hasDeuxRentesAVS(prestationsAccordees));
    }

    @Test
    public void testHasDeuxRFM() {
        Set<PrestationAccordee> prestationsAccordees = new HashSet<PrestationAccordee>();

        PrestationAccordee uneRenteRFM = new PrestationAccordee();
        uneRenteRFM.setId(genererUnIdUnique());
        uneRenteRFM.setCodePrestation(CodePrestation.CODE_210);
        prestationsAccordees.add(uneRenteRFM);

        Assert.assertFalse(getCodesPrestationsFormattees(prestationsAccordees),
                testInstance.hasDeuxRFM(prestationsAccordees));

        PrestationAccordee uneRentePC = new PrestationAccordee();
        uneRentePC.setId(genererUnIdUnique());
        uneRentePC.setCodePrestation(CodePrestation.CODE_110);
        prestationsAccordees.add(uneRentePC);

        Assert.assertFalse(getCodesPrestationsFormattees(prestationsAccordees),
                testInstance.hasDeuxRFM(prestationsAccordees));

        PrestationAccordee uneAutreRFM = new PrestationAccordee();
        uneAutreRFM.setId(genererUnIdUnique());
        uneAutreRFM.setCodePrestation(CodePrestation.CODE_213);
        prestationsAccordees.clear();
        prestationsAccordees.add(uneRenteRFM);
        prestationsAccordees.add(uneAutreRFM);

        Assert.assertTrue(getCodesPrestationsFormattees(prestationsAccordees),
                testInstance.hasDeuxRFM(prestationsAccordees));
    }

    @Test
    public void testHasRX4etRX5() {
        Set<PrestationAccordee> prestationsAccordees = new HashSet<PrestationAccordee>();

        PrestationAccordee uneR34 = new PrestationAccordee();
        uneR34.setId(genererUnIdUnique());
        uneR34.setCodePrestation(CodePrestation.CODE_34);
        prestationsAccordees.add(uneR34);

        Assert.assertFalse(getCodesPrestationsFormattees(prestationsAccordees),
                testInstance.hasRX4etRX5(prestationsAccordees));

        PrestationAccordee uneR10 = new PrestationAccordee();
        uneR10.setId(genererUnIdUnique());
        uneR10.setCodePrestation(CodePrestation.CODE_10);
        prestationsAccordees.add(uneR10);

        Assert.assertFalse(getCodesPrestationsFormattees(prestationsAccordees),
                testInstance.hasRX4etRX5(prestationsAccordees));

        PrestationAccordee uneR44 = new PrestationAccordee();
        uneR44.setId(genererUnIdUnique());
        uneR44.setCodePrestation(CodePrestation.CODE_44);
        prestationsAccordees.clear();
        prestationsAccordees.add(uneR34);
        prestationsAccordees.add(uneR44);

        Assert.assertFalse(getCodesPrestationsFormattees(prestationsAccordees),
                testInstance.hasRX4etRX5(prestationsAccordees));

        PrestationAccordee uneR35 = new PrestationAccordee();
        uneR35.setId(genererUnIdUnique());
        uneR35.setCodePrestation(CodePrestation.CODE_35);
        prestationsAccordees.clear();
        prestationsAccordees.add(uneR34);
        prestationsAccordees.add(uneR35);

        Assert.assertTrue(getCodesPrestationsFormattees(prestationsAccordees),
                testInstance.hasRX4etRX5(prestationsAccordees));
    }

    @Test
    public void testAvecDeuxPCDontUneAllocationDeNoel() {

        List<RERenteJoinPersonneAvs> rentes = new ArrayList<RERenteJoinPersonneAvs>();

        RERenteJoinPersonneAvs rente110 = new RERenteJoinPersonneAvs();
        rente110.setIdPrestationAccordee("1");
        rente110.setCodePrestation("110");
        rente110.setDateDebutDroit("01.2010");
        rente110.setMontantPrestation("100.00");
        rente110.setIdTiers("1");
        rente110.setNssBeneficiaire("756.1234.5678.97");
        rente110.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(rente110);

        RERenteJoinPersonneAvs rente118 = new RERenteJoinPersonneAvs();
        rente118.setIdPrestationAccordee("2");
        rente118.setCodePrestation("118");
        rente118.setDateDebutDroit("01.2010");
        rente118.setMontantPrestation("100.00");
        rente118.setIdTiers("1");
        rente118.setNssBeneficiaire("756.1234.5678.97");
        rente118.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(rente118);

        List<RERenteJoinPersonneAvs> rentesDoubles = testInstance.getRentesDoubles(rentes);
        Assert.assertFalse(laRenteEstPresente(rente110, rentesDoubles));
        Assert.assertFalse(laRenteEstPresente(rente118, rentesDoubles));
    }

    @Test
    public void testAvecToutesLesRentesPossiblesEnEtantValide() {

        List<RERenteJoinPersonneAvs> rentes = new ArrayList<RERenteJoinPersonneAvs>();

        RERenteJoinPersonneAvs rente10 = new RERenteJoinPersonneAvs();
        rente10.setIdPrestationAccordee("1");
        rente10.setCodePrestation("10");
        rente10.setDateDebutDroit("01.2010");
        rente10.setMontantPrestation("100.00");
        rente10.setIdTiers("1");
        rente10.setNssBeneficiaire("756.1234.5678.97");
        rente10.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(rente10);

        RERenteJoinPersonneAvs rente110 = new RERenteJoinPersonneAvs();
        rente110.setIdPrestationAccordee("2");
        rente110.setCodePrestation("110");
        rente110.setDateDebutDroit("01.2010");
        rente110.setMontantPrestation("100.00");
        rente110.setIdTiers("1");
        rente110.setNssBeneficiaire("756.1234.5678.97");
        rente110.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(rente110);

        RERenteJoinPersonneAvs rente118 = new RERenteJoinPersonneAvs();
        rente118.setIdPrestationAccordee("3");
        rente118.setCodePrestation("118");
        rente118.setDateDebutDroit("01.2010");
        rente118.setMontantPrestation("100.00");
        rente118.setIdTiers("1");
        rente118.setNssBeneficiaire("756.1234.5678.97");
        rente118.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(rente118);

        RERenteJoinPersonneAvs rente210 = new RERenteJoinPersonneAvs();
        rente210.setIdPrestationAccordee("4");
        rente210.setCodePrestation("210");
        rente210.setDateDebutDroit("01.2010");
        rente210.setMontantPrestation("100.00");
        rente210.setIdTiers("1");
        rente210.setNssBeneficiaire("756.1234.5678.97");
        rente210.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(rente210);

        RERenteJoinPersonneAvs rente94 = new RERenteJoinPersonneAvs();
        rente94.setIdPrestationAccordee("5");
        rente94.setCodePrestation("94");
        rente94.setDateDebutDroit("01.2010");
        rente94.setMontantPrestation("100.00");
        rente94.setIdTiers("1");
        rente94.setNssBeneficiaire("756.1234.5678.97");
        rente94.setCsSexeBeneficiaire(ITIPersonne.CS_HOMME);
        rentes.add(rente94);

        List<RERenteJoinPersonneAvs> rentesDoubles = testInstance.getRentesDoubles(rentes);
        Assert.assertFalse(laRenteEstPresente(rente10, rentesDoubles));
        Assert.assertFalse(laRenteEstPresente(rente110, rentesDoubles));
        Assert.assertFalse(laRenteEstPresente(rente118, rentesDoubles));
        Assert.assertFalse(laRenteEstPresente(rente210, rentesDoubles));
        Assert.assertFalse(laRenteEstPresente(rente94, rentesDoubles));

    }
}
