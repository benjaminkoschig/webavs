package ch.globaz.pegasus.businessimpl.tests.mutation.testCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.pegasus.business.constantes.EPCPMutationPassage;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.models.mutation.MutationAbstract;
import ch.globaz.pegasus.business.models.mutation.MutationPca;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.services.models.mutation.MutationPcaService;
import ch.globaz.pegasus.business.vo.lot.MutationPcaVo;
import ch.globaz.pegasus.businessimpl.services.models.mutation.AlloctionNoelGrouped;
import ch.globaz.pegasus.businessimpl.services.models.mutation.MutationCategorieResolver;
import ch.globaz.pegasus.businessimpl.services.models.mutation.MutationPcaServiceImpl;

public class MutationConsolideData {

    private MutationPca createMutationVersion2PcaCourante() {
        MutationPca mutationDroit3 = new MutationPca();
        mutationDroit3.setCsTypeDecision(IPCDecision.CS_TYPE_OCTROI_AC);
        mutationDroit3.setCsTypePca(IPCPCAccordee.CS_TYPE_PC_INVALIDITE);
        mutationDroit3.setCsTypePreparationDecision(IPCDecision.CS_PREP_STANDARD);
        mutationDroit3.setDateDebutPca("02.2013");
        mutationDroit3.setDateFinPca(null);
        mutationDroit3.setNoVersion("2");
        mutationDroit3.setIdDroit("1");
        mutationDroit3.setIdVersionDroit("2");
        mutationDroit3.setIdPcaActuel("100");
        mutationDroit3.setCsRoleMembreFamille("64004001");
        mutationDroit3.setNom("Magnin");
        mutationDroit3.setPrenom("Dorian");
        mutationDroit3.setNss("756.0000.0000.00");
        mutationDroit3.setMontant("400");
        mutationDroit3.setIdDemande("1510");
        return mutationDroit3;
    }

    private List<MutationAbstract> factoryListeOldCurrent() {

        List<MutationAbstract> listAncienPcaCourante = new ArrayList<MutationAbstract>();

        MutationPca mutationDroit1 = new MutationPca();
        mutationDroit1.setCsTypeDecision(IPCDecision.CS_TYPE_OCTROI_AC);
        mutationDroit1.setCsTypePca(IPCPCAccordee.CS_TYPE_PC_INVALIDITE);
        mutationDroit1.setCsTypePreparationDecision(IPCDecision.CS_PREP_STANDARD);
        mutationDroit1.setDateDebutPca("01.2013");
        mutationDroit1.setDateFinPca(null);
        mutationDroit1.setNoVersion("1");
        mutationDroit1.setIdDroit("1");
        mutationDroit1.setIdVersionDroit("1");
        mutationDroit1.setIdPcaActuel("1");
        mutationDroit1.setCsRoleMembreFamille("64004001");

        mutationDroit1.setNom("Magnin");
        mutationDroit1.setPrenom("Dorian");
        mutationDroit1.setNss("756.0000.0000.00");
        mutationDroit1.setMontant("550");

        MutationPca mutationDroit3 = createMutationVersion2PcaCourante();

        MutationPca mutationDroit2 = new MutationPca();
        mutationDroit1.setCsTypeDecision(IPCDecision.CS_TYPE_OCTROI_AC);
        mutationDroit2.setCsTypePca(IPCPCAccordee.CS_TYPE_PC_INVALIDITE);
        mutationDroit2.setCsTypePreparationDecision(IPCDecision.CS_PREP_STANDARD);
        mutationDroit2.setDateDebutPca("01.2013");
        mutationDroit2.setDateFinPca(null);
        mutationDroit2.setNoVersion("20");
        mutationDroit2.setIdDroit("2");
        mutationDroit2.setIdVersionDroit("20");
        mutationDroit2.setIdPcaActuel("100");
        mutationDroit2.setCsRoleMembreFamille("64004001");

        mutationDroit2.setNom("Totot");
        mutationDroit2.setPrenom("Tatat");
        mutationDroit2.setNss("756.1111.0000.00");
        mutationDroit2.setMontant("150");

        listAncienPcaCourante.add(mutationDroit3);
        listAncienPcaCourante.add(mutationDroit1);
        listAncienPcaCourante.add(mutationDroit2);

        return listAncienPcaCourante;
    }

    private void factoryListValidee(List<MutationPca> listPcaValide) {
        MutationPca mutationRetro = new MutationPca();
        // mutation.setCsMotif(csMotif)
        // mutation de type retro pure.
        mutationRetro.setCsTypeDecision(IPCDecision.CS_TYPE_OCTROI_AC);
        mutationRetro.setCsTypePca(IPCPCAccordee.CS_TYPE_PC_INVALIDITE);
        mutationRetro.setCsTypePreparationDecision(IPCDecision.CS_PREP_STANDARD);
        mutationRetro.setDateDebutPca("01.2013");
        mutationRetro.setDateFinPca("03.2013");
        mutationRetro.setNoVersion("2");
        mutationRetro.setIdDroit("1");
        mutationRetro.setIdVersionDroit("2");
        mutationRetro.setIdPcaActuel("1");
        mutationRetro.setCsRoleMembreFamille("64004001");

        mutationRetro.setNom("Magnin");
        mutationRetro.setPrenom("Dorian");
        mutationRetro.setNss("756.0000.0000.00");
        mutationRetro.setMontant("250");

        listPcaValide.add(createMutationVersion2PcaCourante());

        MutationPca mutationCourante = new MutationPca();
        mutationCourante.setCsTypeDecision(IPCDecision.CS_TYPE_OCTROI_AC);
        mutationCourante.setCsTypePca(IPCPCAccordee.CS_TYPE_PC_INVALIDITE);
        mutationCourante.setCsTypePreparationDecision(IPCDecision.CS_PREP_STANDARD);
        mutationCourante.setDateDebutPca("04.2013");
        mutationCourante.setDateFinPca(null);
        mutationCourante.setNoVersion("3");
        mutationCourante.setIdDroit("1");
        mutationCourante.setIdVersionDroit("3");
        mutationCourante.setIdPcaActuel("3");
        mutationCourante.setCsRoleMembreFamille("64004001");

        mutationCourante.setNom("Magnin");
        mutationCourante.setPrenom("Dorian");
        mutationCourante.setNss("756.0000.0000.00");
        mutationCourante.setMontant("200");

        listPcaValide.add(mutationRetro);
        listPcaValide.add(mutationCourante);
    }

    @Test
    @Ignore
    public final void testConsolideAllocationNoel() {

        String dateMonth = "07.2013";
        Assert.assertTrue(true);
        MutationPcaService service = new MutationPcaServiceImpl();

        List<MutationPca> listPcaValide = new ArrayList<MutationPca>();
        listPcaValide.add(createMutationVersion2PcaCourante());

        List<MutationAbstract> listAncienPcaCourante = new ArrayList<MutationAbstract>();

        List<MutationPcaVo> mutations = new ArrayList<MutationPcaVo>();

        Map<String, SimpleAllocationNoel> mapAllocationsNoel = new HashMap<String, SimpleAllocationNoel>();
        SimpleAllocationNoel simpleAllocationNoel = new SimpleAllocationNoel();
        simpleAllocationNoel.setMontantAllocation("100");
        mapAllocationsNoel.put(listPcaValide.get(0).getIdDemande(), simpleAllocationNoel);

        AlloctionNoelGrouped allocations = new AlloctionNoelGrouped(mapAllocationsNoel);
        mutations = service.mergeListAndConsolideData(listPcaValide, listAncienPcaCourante, allocations, dateMonth);

        if (mutations.size() > 3) {
            Assert.fail("Trop de mutation retournée");
        }

        service.sortByDate(mutations);

        MutationPcaVo courante = mutations.get(0);

        Assert.assertEquals("100", courante.getMontantAllocationNoel());

    }

    @Test
    @Ignore
    public final void testConsolideAllocationNoelCoupleSep() {

        String dateMonth = "07.2013";
        Assert.assertTrue(true);
        MutationPcaService service = new MutationPcaServiceImpl();

        List<MutationPca> listPcaValide = new ArrayList<MutationPca>();
        listPcaValide.add(createMutationVersion2PcaCourante());
        listPcaValide.add(createMutationVersion2PcaCourante());

        List<MutationAbstract> listAncienPcaCourante = new ArrayList<MutationAbstract>();

        List<MutationPcaVo> mutations = new ArrayList<MutationPcaVo>();

        Map<String, SimpleAllocationNoel> mapAllocationsNoel = new HashMap<String, SimpleAllocationNoel>();
        SimpleAllocationNoel simpleAllocationNoel = new SimpleAllocationNoel();
        simpleAllocationNoel.setMontantAllocation("100");
        mapAllocationsNoel.put(listPcaValide.get(0).getIdDemande(), simpleAllocationNoel);

        AlloctionNoelGrouped allocations = new AlloctionNoelGrouped(mapAllocationsNoel);
        mutations = service.mergeListAndConsolideData(listPcaValide, listAncienPcaCourante, allocations, dateMonth);

        if (mutations.size() > 3) {
            Assert.fail("Trop de mutation retournée");
        }

        service.sortByDate(mutations);

        MutationPcaVo courante = mutations.get(0);
        MutationPcaVo courante1 = mutations.get(1);
        Assert.assertEquals("100", courante.getMontantAllocationNoel());
        Assert.assertEquals("100", courante1.getMontantAllocationNoel());

    }

    @Test
    @Ignore
    public final void testConsolideData() {

        String dateMonth = "07.2013";
        Assert.assertTrue(true);
        MutationPcaService service = new MutationPcaServiceImpl();

        List<MutationPca> listPcaValide = new ArrayList<MutationPca>();

        factoryListValidee(listPcaValide);

        List<MutationAbstract> listAncienPcaCourante = factoryListeOldCurrent();

        List<MutationPcaVo> mutations = new ArrayList<MutationPcaVo>();

        Map<String, SimpleAllocationNoel> mapAllocationsNoel = new HashMap<String, SimpleAllocationNoel>();

        AlloctionNoelGrouped allocations = new AlloctionNoelGrouped(mapAllocationsNoel);
        mutations = service.mergeListAndConsolideData(listPcaValide, listAncienPcaCourante, allocations, dateMonth);
        if (mutations.size() > 3) {
            Assert.fail("Trop de mutation retournée");
        }

        service.sortByDate(mutations);

        MutationPcaVo couranteV3 = mutations.get(0);
        MutationPcaVo couranteV2 = mutations.get(1);
        MutationPcaVo retro = mutations.get(2);

        // Ici on test aussi le mapping.
        Assert.assertEquals("400", couranteV3.getMontantPrecedant());
        Assert.assertEquals(IPCDecision.CS_TYPE_OCTROI_AC, couranteV3.getCsTypeDecision());
        Assert.assertEquals(MutationCategorieResolver.RecapDomainePca.AI, couranteV3.getCodeCategroriePca());
        // Assert.assertEquals(IPCDecision.CS_PREP_STANDARD, courante.getCsTypePcaPrecedantCourante());
        Assert.assertEquals(IPCDecision.CS_PREP_STANDARD, couranteV3.getCsTypePreparationDecision());
        Assert.assertEquals("04.2013", couranteV3.getDateDebutPcaActuel());
        Assert.assertNotNull("Test si la date de debut de la pca précedente et non null",
                couranteV3.getDateDebutPcaPrecedant());
        Assert.assertEquals("02.2013", couranteV3.getDateDebutPcaPrecedant());
        Assert.assertEquals(null, couranteV3.getDateFinPcaActuel());

        // // Assert.assertEquals(courante.getIdPca(), null);
        Assert.assertEquals("200", couranteV3.getMontantActuel());
        Assert.assertEquals("600", couranteV3.getMontantRetro());
        Assert.assertEquals("Magnin", couranteV3.getNom());
        Assert.assertEquals("3", couranteV3.getNoVersion());
        Assert.assertEquals("756.0000.0000.00", couranteV3.getNss());
        Assert.assertEquals("Dorian", couranteV3.getPrenom());
        Assert.assertEquals(EPCPMutationPassage.AUCUN, couranteV3.getPassage());
        Assert.assertEquals("Test s'il y une augementation future", false, couranteV3.isAugementationFutur());
        Assert.assertEquals("Test si on est sur une pca courante", true, couranteV3.isCurrent());
        Assert.assertEquals("Test si la pcas à une diminution", true, couranteV3.isHasDiminutation());
        Assert.assertEquals("Test si la pca est seulement retroactive", false, couranteV3.isPurRetro());

        Assert.assertEquals("550", couranteV2.getMontantPrecedant());
        Assert.assertEquals("400", couranteV2.getMontantActuel());
        Assert.assertEquals("Test le montant retro: ", "2000", couranteV2.getMontantRetro());
        Assert.assertEquals("02.2013", couranteV2.getDateDebutPcaActuel());
        Assert.assertEquals("01.2013", couranteV2.getDateDebutPcaPrecedant());
        Assert.assertEquals(null, couranteV2.getDateFinPcaActuel());
        Assert.assertEquals("2", couranteV2.getNoVersion());
        Assert.assertEquals(EPCPMutationPassage.AUCUN, couranteV2.getPassage());
        Assert.assertEquals("Test s'il y une augementation future", false, couranteV2.isAugementationFutur());
        Assert.assertEquals("Test si on est sur une pca courante", true, couranteV2.isCurrent());
        Assert.assertEquals("Test si la pcas à une diminution", true, couranteV2.isHasDiminutation());
        Assert.assertEquals("Test si la pca est seulement retroactive", false, couranteV2.isPurRetro());

        Assert.assertEquals(null, retro.getMontantPrecedant());
        Assert.assertEquals("250", retro.getMontantActuel());
        Assert.assertEquals("Test le montant retro: ", "750", retro.getMontantRetro());
        Assert.assertEquals("01.2013", retro.getDateDebutPcaActuel());
        Assert.assertEquals(null, retro.getDateDebutPcaPrecedant());
        Assert.assertEquals("03.2013", retro.getDateFinPcaActuel());
        Assert.assertEquals("2", retro.getNoVersion());
        Assert.assertEquals(EPCPMutationPassage.AUCUN, retro.getPassage());
        Assert.assertEquals("Test s'il y une augementation future", false, retro.isAugementationFutur());
        Assert.assertEquals("Test si on est sur une pca courante", false, retro.isCurrent());
        Assert.assertEquals("Test si la pcas à une diminution", false, retro.isHasDiminutation());
        Assert.assertEquals("Test si la pca est seulement retroactive", true, retro.isPurRetro());

    }

}
