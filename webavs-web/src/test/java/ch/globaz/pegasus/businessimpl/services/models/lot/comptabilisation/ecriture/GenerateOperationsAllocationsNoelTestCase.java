package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import globaz.jade.exception.JadeApplicationException;
import globaz.osiris.api.APIEcriture;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.CompteAnnexeFactory;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.OrdreVersementFactory;

public class GenerateOperationsAllocationsNoelTestCase {

    @Spy
    private GenerateOperationsAllocationsNoel allocationsNoelMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        doReturn("Mock RefPaiement").when(allocationsNoelMock).formatMotifVersement(any(InfosTiers.class),
                any(String.class), any(String.class), any(String.class));
    }

    @BeforeClass
    public static void before() {
        CompteAnnexeResolver.addComptesAnnexes(CompteAnnexeFactory.generateComptesAnnexes());
    }

    private void generate(GenerateOperationsAllocationsNoel allocationsNoel, List<OrdreVersement> ovs)
            throws JadeApplicationException {

        allocationsNoel.generateAllOperation(ovs, CompteAnnexeFactory.getInfosRequerant(),
                CompteAnnexeFactory.getInfosConjoint(), "strPeriode", "strDecision");
    }

    private List<OrdreVersement> generateList() {
        List<OrdreVersement> ovs = new ArrayList<OrdreVersement>();
        ovs.add(OrdreVersementFactory.generateAllocationNoel("200", CompteAnnexeFactory.ID_TIERS_REQUERANT));
        return ovs;
    }

    @Test
    public void testGenerateAllOperation() throws JadeApplicationException {
        // GenerateOperationsAllocationsNoel allocationsNoel = new GenerateOperationsAllocationsNoel();
        List<OrdreVersement> ovs = generateList();
        generate(allocationsNoelMock, ovs);
    }

    @Test
    public void testGetEcritures() throws JadeApplicationException {
        GenerateOperationsAllocationsNoel allocationsNoel = allocationsNoelMock;
        List<OrdreVersement> ovs = generateList();
        generate(allocationsNoel, ovs);
        Assert.assertEquals(APIEcriture.CREDIT, allocationsNoel.getEcritures().get(0).getCodeDebitCredit());
        Assert.assertEquals(new BigDecimal(200), allocationsNoel.getEcritures().get(0).getMontant());
        Assert.assertEquals(ovs.get(0), allocationsNoel.getEcritures().get(0).getOrdreVersement());
        Assert.assertEquals(TypeEcriture.ALLOCATION_NOEL, allocationsNoel.getEcritures().get(0).getTypeEcriture());
        Assert.assertEquals(SectionPegasus.DECISION_PC, allocationsNoel.getEcritures().get(0).getSection());
        Assert.assertNotNull(allocationsNoel.getEcritures().get(0).getIdRefRubrique());
    }

    @Test
    public void testGetOrdreVersementCompt() throws JadeApplicationException {
        GenerateOperationsAllocationsNoel allocationsNoel = allocationsNoelMock;
        List<OrdreVersement> ovs = generateList();
        generate(allocationsNoel, ovs);
        Assert.assertEquals(new BigDecimal(200), allocationsNoel.getOrdreVersementCompta().get(0).getMontant());
        Assert.assertNotNull(allocationsNoel.getOrdreVersementCompta().get(0).getCompteAnnexe());
        Assert.assertNotNull(allocationsNoel.getOrdreVersementCompta().get(0).getIdTiersAdressePaiement());
        Assert.assertNotNull(allocationsNoel.getOrdreVersementCompta().get(0).getIdDomaineApplication());
        Assert.assertEquals(SectionPegasus.DECISION_PC, allocationsNoel.getOrdreVersementCompta().get(0).getSection());

    }
}
