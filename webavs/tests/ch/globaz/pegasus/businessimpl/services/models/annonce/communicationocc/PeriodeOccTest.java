package ch.globaz.pegasus.businessimpl.services.models.annonce.communicationocc;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.pca.PcaStatus;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamille;

public class PeriodeOccTest {

    @Test
    public void testHasMutationValideForCreateAnnonceRefusRefus() throws Exception {
        Assert.assertFalse(PeriodeOcc.isStatusChange(PcaStatus.REFUS, PcaStatus.REFUS));
    }

    @Test
    public void testHasMutationValideForCreateAnnonceRefusOctroi() throws Exception {
        Assert.assertTrue(PeriodeOcc.isStatusChange(PcaStatus.REFUS, PcaStatus.OCTROI));
    }

    @Test
    public void testHasMutationValideForCreateAnnonceRefusOctroiPartiel() throws Exception {
        Assert.assertTrue(PeriodeOcc.isStatusChange(PcaStatus.REFUS, PcaStatus.OCTROI_PARTIEL));
    }

    @Test
    public void testHasMutationValideForCreateAnnonceOctroiOctroi() throws Exception {
        Assert.assertFalse(PeriodeOcc.isStatusChange(PcaStatus.OCTROI, PcaStatus.OCTROI));
    }

    @Test
    public void testHasMutationValideForCreateAnnonceOctroiRefus() throws Exception {
        Assert.assertTrue(PeriodeOcc.isStatusChange(PcaStatus.OCTROI, PcaStatus.REFUS));
    }

    @Test
    public void testHasMutationValideForCreateAnnonceOctroiOctroiPartiel() throws Exception {
        Assert.assertFalse(PeriodeOcc.isStatusChange(PcaStatus.OCTROI, PcaStatus.OCTROI_PARTIEL));
    }

    @Test
    public void testHasMutationValideForCreateAnnonceOctroipartielOctroiPartiel() throws Exception {
        Assert.assertFalse(PeriodeOcc.isStatusChange(PcaStatus.OCTROI_PARTIEL, PcaStatus.OCTROI_PARTIEL));
    }

    @Test
    public void testHasMutationValideForCreateAnnonceOctroiParteilRefus() throws Exception {
        Assert.assertTrue(PeriodeOcc.isStatusChange(PcaStatus.OCTROI_PARTIEL, PcaStatus.REFUS));
    }

    @Test
    public void testHasMutationValideForCreateAnnoncectroiPartielOctroi() throws Exception {
        Assert.assertFalse(PeriodeOcc.isStatusChange(PcaStatus.OCTROI_PARTIEL, PcaStatus.OCTROI));
    }

    @Test
    public void testIsMembreFamilleSameEmpty() throws Exception {
        Assert.assertTrue(PeriodeOcc.isMembreFamilleSame(new ArrayList<PlanDeCalculWitMembreFamille>(),
                (new ArrayList<PlanDeCalculWitMembreFamille>())));
    }

    @Test
    public void testIsMembreFamilleSameWitOneMembre() throws Exception {
        Assert.assertTrue(PeriodeOcc.isMembreFamilleSame(buildListMembreFamille("1"), buildListMembreFamille("1")));
    }

    @Test
    public void testIsMembreFamilleSameWitOneMembreNotSame() throws Exception {
        List<PlanDeCalculWitMembreFamille> membresFamille1 = buildListMembreFamille("1");
        List<PlanDeCalculWitMembreFamille> membresFamille2 = buildListMembreFamille("2");
        Assert.assertFalse(PeriodeOcc.isMembreFamilleSame(membresFamille1, membresFamille2));
    }

    @Test
    public void testIsMembreFamilleSameWithManyMembres() throws Exception {
        List<PlanDeCalculWitMembreFamille> membresFamille1 = buildListMembreFamille("1", "2", "20", "50");
        List<PlanDeCalculWitMembreFamille> membresFamille2 = buildListMembreFamille("1", "2", "20", "50");
        Assert.assertTrue(PeriodeOcc.isMembreFamilleSame(membresFamille1, membresFamille2));
    }

    @Test
    public void testIsMembreFamilleSameWithManyMembresNotSame1() throws Exception {
        List<PlanDeCalculWitMembreFamille> membresFamille1 = buildListMembreFamille("1", "2", "20", "50");
        List<PlanDeCalculWitMembreFamille> membresFamille2 = buildListMembreFamille("1", "2", "22", "50");
        Assert.assertFalse(PeriodeOcc.isMembreFamilleSame(membresFamille1, membresFamille2));
    }

    @Test
    public void testIsMembreFamilleSameWithManyMembresNotSame2() throws Exception {
        List<PlanDeCalculWitMembreFamille> membresFamille1 = buildListMembreFamille("1", "2", "20", "50");
        List<PlanDeCalculWitMembreFamille> membresFamille2 = buildListMembreFamille("1", "2", "22");
        Assert.assertFalse(PeriodeOcc.isMembreFamilleSame(membresFamille1, membresFamille2));
    }

    @Test
    public void testIsMembreFamilleSameWithAllSameNotOrdred() throws Exception {
        List<PlanDeCalculWitMembreFamille> membresFamille1 = buildListMembreFamille("1", "2", "20", "50");
        List<PlanDeCalculWitMembreFamille> membresFamille2 = buildListMembreFamille("1", "2", "50", "20");
        Assert.assertTrue(PeriodeOcc.isMembreFamilleSame(membresFamille1, membresFamille2));
    }

    @Test
    public void testMustSplitPeriode() throws Exception {
        List<PlanDeCalculWitMembreFamille> membresFamille1 = new ArrayList<PlanDeCalculWitMembreFamille>();
        membresFamille1.add(buildMembreFamille("1"));

        PeriodeOcc periodeOcc1 = new PeriodeOcc("06.2014", null, "1", PcaStatus.OCTROI, "", "1", membresFamille1,
                RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT));

        PeriodeOcc periodeOcc2 = new PeriodeOcc("01.2014", "05.2014", "1", PcaStatus.OCTROI, "", "1", membresFamille1,
                RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT));

        Assert.assertFalse(periodeOcc1.hasSameSituation(periodeOcc2));
    }

    @Test
    public void testMustSplitPeriodeWhitMembreFamilleNotSame() throws Exception {
        List<PlanDeCalculWitMembreFamille> membresFamille1 = new ArrayList<PlanDeCalculWitMembreFamille>();
        membresFamille1.add(buildMembreFamille("1"));
        List<PlanDeCalculWitMembreFamille> membresFamille2 = new ArrayList<PlanDeCalculWitMembreFamille>();
        membresFamille2.add(buildMembreFamille("2"));

        PeriodeOcc periodeOcc1 = new PeriodeOcc("06.2014", null, "1", PcaStatus.OCTROI, "", "1", membresFamille1,
                RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT));

        PeriodeOcc periodeOcc2 = new PeriodeOcc("01.2014", "05.2014", "1", PcaStatus.OCTROI, "", "1", membresFamille2,
                RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT));

        Assert.assertTrue(periodeOcc1.hasSameSituation(periodeOcc2));
    }

    @Test
    public void testMustSplitPeriodeWhitMembreWhitStatusNotSame() throws Exception {
        List<PlanDeCalculWitMembreFamille> membresFamille1 = new ArrayList<PlanDeCalculWitMembreFamille>();
        membresFamille1.add(buildMembreFamille("1"));

        PeriodeOcc periodeOcc1 = new PeriodeOcc("06.2014", null, "1", PcaStatus.OCTROI, "", "1", membresFamille1,
                RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT));

        PeriodeOcc periodeOcc2 = new PeriodeOcc("01.2014", "05.2014", "1", PcaStatus.REFUS, "", "1", membresFamille1,
                RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT));

        Assert.assertTrue(periodeOcc1.hasSameSituation(periodeOcc2));
    }

    @Test
    public void testMustSplitPeriodeWhitMembreWhitStatusNotSameAndMembreFamileNotSame() throws Exception {
        List<PlanDeCalculWitMembreFamille> membresFamille1 = new ArrayList<PlanDeCalculWitMembreFamille>();
        membresFamille1.add(buildMembreFamille("1"));

        List<PlanDeCalculWitMembreFamille> membresFamille2 = new ArrayList<PlanDeCalculWitMembreFamille>();
        membresFamille2.add(buildMembreFamille("2"));

        PeriodeOcc periodeOcc1 = new PeriodeOcc("06.2014", null, "1", PcaStatus.OCTROI, "", "1", membresFamille1,
                RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT));

        PeriodeOcc periodeOcc2 = new PeriodeOcc("01.2014", "05.2014", "1", PcaStatus.REFUS, "", "1", membresFamille2,
                RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT));

        Assert.assertTrue(periodeOcc1.hasSameSituation(periodeOcc2));
    }

    @Test
    public void testMustGeneratePeriode() throws Exception {
        PeriodeOcc periodeOcc1 = buildPeriodeRequ("06.2014", null, PcaStatus.REFUS, buildListMembreFamille("1", "2"));
        PeriodeOcc periodeOcc2 = buildPeriodeRequ("06.2014", null, PcaStatus.REFUS, buildListMembreFamille("4", "3"));
        assertFalse(periodeOcc1.mustGeneratePeriode(periodeOcc2));
    }

    @Test
    public void testMustGeneratePeriode1() throws Exception {
        PeriodeOcc periodeOcc1 = buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI, buildListMembreFamille("1", "2"));
        PeriodeOcc periodeOcc2 = buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI, buildListMembreFamille("4", "2"));
        assertTrue(periodeOcc1.mustGeneratePeriode(periodeOcc2));
    }

    @Test
    public void testMustGeneratePeriode2() throws Exception {
        PeriodeOcc periodeOcc1 = buildPeriodeRequ("06.2014", null, PcaStatus.REFUS, buildListMembreFamille("1", "2"));
        PeriodeOcc periodeOcc2 = buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI, buildListMembreFamille("4", "2"));
        assertTrue(periodeOcc1.mustGeneratePeriode(periodeOcc2));
    }

    @Test
    public void testMustGeneratePeriode3() throws Exception {
        PeriodeOcc periodeOcc1 = buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI, buildListMembreFamille("1", "2"));
        PeriodeOcc periodeOcc2 = buildPeriodeRequ("06.2014", null, PcaStatus.REFUS, buildListMembreFamille("4", "2"));
        assertTrue(periodeOcc1.mustGeneratePeriode(periodeOcc2));
    }

    @Test
    public void testMustGeneratePeriode4() throws Exception {
        PeriodeOcc periodeOcc1 = buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI, buildListMembreFamille("1", "2"));
        PeriodeOcc periodeOcc2 = buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI, buildListMembreFamille("1", "2"));
        assertFalse(periodeOcc1.mustGeneratePeriode(periodeOcc2));
    }

    @Test
    public void testMustGeneratePeriode8() throws Exception {
        PeriodeOcc periodeOcc1 = buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI, buildListMembreFamille("1", "2"));
        PeriodeOcc periodeOcc2 = buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI, buildListMembreFamille("3", "2"));
        assertTrue(periodeOcc1.mustGeneratePeriode(periodeOcc2));
    }

    @Test
    public void testMustGeneratePeriode7() throws Exception {
        PeriodeOcc periodeOcc1 = buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI, buildListMembreFamille("2", "1"));
        PeriodeOcc periodeOcc2 = buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI, buildListMembreFamille("2", "1"));
        assertFalse(periodeOcc1.mustGeneratePeriode(periodeOcc2));
    }

    @Test
    public void testMustGeneratePeriode5() throws Exception {
        PeriodeOcc periodeOcc1 = buildPeriodeRequ("06.2014", null, PcaStatus.REFUS, buildListMembreFamille("1", "2"));
        PeriodeOcc periodeOcc2 = buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI, buildListMembreFamille("1", "2"));
        assertTrue(periodeOcc1.mustGeneratePeriode(periodeOcc2));
    }

    @Test
    public void testMustGeneratePeriode6() throws Exception {
        PeriodeOcc periodeOcc1 = buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI, buildListMembreFamille("1", "2"));
        PeriodeOcc periodeOcc2 = buildPeriodeRequ("06.2014", null, PcaStatus.REFUS, buildListMembreFamille("1", "2"));
        assertTrue(periodeOcc1.mustGeneratePeriode(periodeOcc2));
    }

    private List<PlanDeCalculWitMembreFamille> buildListMembreFamille(String... ids) {
        List<PlanDeCalculWitMembreFamille> membresFamille = new ArrayList<PlanDeCalculWitMembreFamille>();
        for (String id : ids) {
            membresFamille.add(buildMembreFamille(id));
        }
        return membresFamille;
    }

    private PlanDeCalculWitMembreFamille buildMembreFamille(String id) {
        PlanDeCalculWitMembreFamille membreFamille = new PlanDeCalculWitMembreFamille();
        membreFamille.getDroitMembreFamille().setId(id);
        return membreFamille;
    }

    private PeriodeOcc buildPeriodeRequ(String dateDebut, String dateFin, PcaStatus status,
            List<PlanDeCalculWitMembreFamille> membresFamille1) {
        PeriodeOcc p = new PeriodeOcc(dateDebut, dateFin, "1", status, "introduction", "10", membresFamille1,
                RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT));

        return p;
    }

    @Test
    public void testGetIdVersionDroit() throws Exception {
        PeriodeOcc p = buildPeriodeRequ("01.2014", "02.2014", PcaStatus.OCTROI, buildListMembreFamille("1"));
        assertEquals("1", p.getIdVersionDroit());
    }

    @Test
    public void testGetIntroduction() throws Exception {
        PeriodeOcc p = buildPeriodeRequ("01.2014", "02.2014", PcaStatus.OCTROI, buildListMembreFamille("1"));
        assertEquals("introduction", p.getIntroduction());
    }

    @Test
    public void testGetIdTiersRequerant() throws Exception {
        PeriodeOcc p = buildPeriodeRequ("01.2014", "02.2014", PcaStatus.OCTROI, buildListMembreFamille("1"));
        assertEquals("10", p.getIdTiersRequerant());
    }

    @Test
    public void testGetNbPersonneDansCalcul() throws Exception {
        PeriodeOcc p = buildPeriodeRequ("01.2014", "02.2014", PcaStatus.OCTROI, buildListMembreFamille("1", "2"));
        assertEquals(2, p.getNbPersonneDansCalcul());
    }

    @Test
    public void testIsRefusPeriodeCloseAvecOctroiTerminee() throws Exception {
        PeriodeOcc p = buildPeriodeRequ("01.2014", "02.2014", PcaStatus.OCTROI, buildListMembreFamille("1"));
        assertFalse(p.isRefusPeriodeClose());
    }

    @Test
    public void testIsRefusPeriodeCloseAvecOctroiNonClos() throws Exception {
        PeriodeOcc p = buildPeriodeRequ("01.2014", null, PcaStatus.OCTROI, buildListMembreFamille("1"));
        assertFalse(p.isRefusPeriodeClose());
    }

    @Test
    public void testIsRefusPeriodeCloseAvecRefuserminee() throws Exception {
        PeriodeOcc p = buildPeriodeRequ("01.2014", "02.2014", PcaStatus.REFUS, buildListMembreFamille("1"));
        assertTrue(p.isRefusPeriodeClose());
    }

    @Test
    public void testIsRefusPeriodeCloseAvecRerfusNonClos() throws Exception {
        PeriodeOcc p = buildPeriodeRequ("01.2014", null, PcaStatus.REFUS, buildListMembreFamille("1"));
        assertFalse(p.isRefusPeriodeClose());
    }

}
