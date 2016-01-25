package ch.globaz.pegasus.businessimpl.services.models.annonce.communicationocc;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.common.domaine.Periode;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.pca.PcaStatus;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamille;
import ch.globaz.pegasus.businessimpl.utils.RequerantConjoint;

public class PeriodeMergerTest {

    private PeriodeMerger decoupagePeriodeOcc = new PeriodeMerger();

    @Test
    public void testGroupeDecsionsCoupleSeparer() throws Exception {
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI));
        periodes.add(buildPeriodeConj("06.2014", null, PcaStatus.OCTROI));

        List<RequerantConjoint<PeriodeOcc>> list = decoupagePeriodeOcc.groupePeriodeByDateDebut(periodes);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(periodes.get(0), list.get(0).getRequerant());
        Assert.assertEquals(periodes.get(1), list.get(0).getConjoint());
    }

    @Test
    public void testGroupeDecsionsSeulementAvecLeRequerant() throws Exception {
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("01.2014", "05.2015", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI));

        List<RequerantConjoint<PeriodeOcc>> list = decoupagePeriodeOcc.groupePeriodeByDateDebut(periodes);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals(periodes.get(0), list.get(0).getRequerant());
        Assert.assertEquals(periodes.get(1), list.get(1).getRequerant());
    }

    @Test
    public void testDecoupPeriodeByStatusPcaSansDecoupage() throws Exception {
        // [O][O][O][O => [01.2013 -
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("01.2013", "05.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI));
        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = decoupagePeriodeOcc.splitPeriode(periodes);
        Assert.assertEquals(1, mapPeriode.size());
        Assert.assertEquals(new Periode("01.2013", null), mapPeriode.firstKey());
    }

    @Test
    public void testDecoupPeriodeByStatusPcaAvecUnDecoupage() throws Exception {
        // [O][R][O][O => [01.2013 - 05.2013], [06.2013 - 12.2013], [01.2014 -
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("01.2013", "05.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.REFUS));
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = decoupagePeriodeOcc.splitPeriode(periodes);
        Assert.assertEquals(3, mapPeriode.size());
        Assert.assertEquals(new Periode("01.2013", "05.2013"), mapPeriode.firstKey());
        Assert.assertEquals(new Periode("06.2013", "12.2013"), mapPeriode.keySet().toArray()[1]);
        Assert.assertEquals(new Periode("01.2014", null), mapPeriode.lastKey());
    }

    @Test
    public void testDecoupPeriodeByStatusCoupleSepare0() throws Exception {
        // [O O] [O O][O O => [06.2013 -
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI));
        periodes.add(buildPeriodeConj("06.2014", null, PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.OCTROI));
        periodes.add(buildPeriodeConj("01.2014", "05.2014", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeConj("06.2013", "12.2013", PcaStatus.OCTROI));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = decoupagePeriodeOcc.splitPeriode(periodes);
        Assert.assertEquals(1, mapPeriode.size());
        Assert.assertEquals(new Periode("06.2013", null), mapPeriode.firstKey());
    }

    @Test
    public void testDecoupPeriodeByStatusCoupleSepare() throws Exception {
        // [O][R O][O O][O O => [01.2013 -
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("01.2013", "05.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.REFUS));
        periodes.add(buildPeriodeConj("06.2013", "12.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.OCTROI));
        periodes.add(buildPeriodeConj("01.2014", "05.2014", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI));
        periodes.add(buildPeriodeConj("06.2014", null, PcaStatus.OCTROI));
        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = decoupagePeriodeOcc.splitPeriode(periodes);
        Assert.assertEquals(1, mapPeriode.size());
        Assert.assertEquals(new Periode("01.2013", null), mapPeriode.lastKey());
    }

    @Test
    public void testDecoupPeriodeByStatusCoupleSepare1() throws Exception {
        // [O][O R][O O][O O => [01.2013 -
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("01.2013", "05.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeConj("06.2013", "12.2013", PcaStatus.REFUS));
        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.OCTROI));
        periodes.add(buildPeriodeConj("01.2014", "05.2014", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI));
        periodes.add(buildPeriodeConj("06.2014", null, PcaStatus.OCTROI));
        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = decoupagePeriodeOcc.splitPeriode(periodes);
        Assert.assertEquals(1, mapPeriode.size());
        Assert.assertEquals(new Periode("01.2013", null), mapPeriode.lastKey());
    }

    @Test
    public void testDecoupPeriodeByStatusCoupleSepare2() throws Exception {
        // [R R] [O O][O O => [06.2013 - 12.2013][01.2014 -
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI));
        periodes.add(buildPeriodeConj("06.2014", null, PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.OCTROI));
        periodes.add(buildPeriodeConj("01.2014", "05.2014", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.REFUS));
        periodes.add(buildPeriodeConj("06.2013", "12.2013", PcaStatus.REFUS));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = decoupagePeriodeOcc.splitPeriode(periodes);
        Assert.assertEquals(2, mapPeriode.size());
        Assert.assertEquals(new Periode("06.2013", "12.2013"), mapPeriode.firstKey());
        Assert.assertEquals(new Periode("01.2014", null), mapPeriode.lastKey());
    }

    @Test
    public void testDecoupPeriodeByStatusCoupleSepareRetrourDom() throws Exception {
        // [O R][R][R => [06.2013 - 12.2013] [01.2014 -
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeConj("06.2013", "12.2013", PcaStatus.REFUS));
        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.REFUS));
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.REFUS));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = decoupagePeriodeOcc.splitPeriode(periodes);
        Assert.assertEquals(2, mapPeriode.size());
        Assert.assertEquals(new Periode("06.2013", "12.2013"), mapPeriode.firstKey());
        Assert.assertEquals(new Periode("01.2014", null), mapPeriode.lastKey());
    }

    @Test
    public void testDecoupPeriodeByStatusCoupleSepareRetrourDom1() throws Exception {
        // [R O][R][R => [06.2013 - 12.2013] [01.2014 -
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.REFUS));
        periodes.add(buildPeriodeConj("06.2013", "12.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.REFUS));
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.REFUS));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = decoupagePeriodeOcc.splitPeriode(periodes);
        Assert.assertEquals(2, mapPeriode.size());
        Assert.assertEquals(new Periode("06.2013", "12.2013"), mapPeriode.firstKey());
        Assert.assertEquals(new Periode("01.2014", null), mapPeriode.lastKey());
    }

    @Test
    public void testDecoupPeriodeByStatusCoupleSepareRetrourDom2() throws Exception {
        // [O R][O][O => [06.2013 -
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeConj("06.2013", "12.2013", PcaStatus.REFUS));
        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = decoupagePeriodeOcc.splitPeriode(periodes);
        Assert.assertEquals(1, mapPeriode.size());
        Assert.assertEquals(new Periode("06.2013", null), mapPeriode.firstKey());
    }

    @Test
    public void testDecoupPeriodeByStatusCoupleSepareRetrourDom3() throws Exception {
        // [R O][O][O => [06.2013 -
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.REFUS));
        periodes.add(buildPeriodeConj("06.2013", "12.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = decoupagePeriodeOcc.splitPeriode(periodes);
        Assert.assertEquals(1, mapPeriode.size());
        Assert.assertEquals(new Periode("06.2013", null), mapPeriode.firstKey());
    }

    @Test
    public void testDecoupPeriodeByStatusCoupleSepareRetrourDomRetourHome() throws Exception {
        // [R O][O][R O => [06.2013 -
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.REFUS));
        periodes.add(buildPeriodeConj("06.2013", "12.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.REFUS));
        periodes.add(buildPeriodeConj("06.2014", null, PcaStatus.OCTROI));
        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = decoupagePeriodeOcc.splitPeriode(periodes);
        Assert.assertEquals(1, mapPeriode.size());
        Assert.assertEquals(new Periode("06.2013", null), mapPeriode.firstKey());
    }

    @Test
    public void testDecoupPeriodeByStatusCoupleSepareRetrourDomRetourHome2() throws Exception {
        // [R O][R][R O => [06.2013 - 12.2013][01.2014 - 06.2014][09.2014 -
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.REFUS));
        periodes.add(buildPeriodeConj("06.2013", "12.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.REFUS));
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.REFUS));
        periodes.add(buildPeriodeConj("06.2014", null, PcaStatus.OCTROI));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = decoupagePeriodeOcc.splitPeriode(periodes);
        Assert.assertEquals(3, mapPeriode.size());
        Assert.assertEquals(new Periode("06.2013", "12.2013"), mapPeriode.firstKey());
        Assert.assertEquals(new Periode("01.2014", "05.2014"), mapPeriode.keySet().toArray()[1]);
        Assert.assertEquals(new Periode("06.2014", null), mapPeriode.lastKey());
    }

    @Test
    public void testDecoupPeriodeByStatusCoupleSepareRetrourDomRetourHome3() throws Exception {
        // [O R][O][O R => [06.2013 -
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeConj("06.2013", "12.2013", PcaStatus.REFUS));
        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.REFUS));
        periodes.add(buildPeriodeConj("06.2014", null, PcaStatus.OCTROI));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = decoupagePeriodeOcc.splitPeriode(periodes);
        Assert.assertEquals(1, mapPeriode.size());
        Assert.assertEquals(new Periode("06.2013", null), mapPeriode.firstKey());
    }

    @Test
    public void testDecoupPeriodeByStatusCoupleSepareRetrourDomRetourHome4() throws Exception {
        // [O R][R][O R => [06.2013 -
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeConj("06.2013", "12.2013", PcaStatus.REFUS));
        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.REFUS));
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.OCTROI));
        periodes.add(buildPeriodeConj("06.2014", null, PcaStatus.REFUS));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = decoupagePeriodeOcc.splitPeriode(periodes);
        Assert.assertEquals(3, mapPeriode.size());
        Assert.assertEquals(new Periode("06.2013", "12.2013"), mapPeriode.firstKey());
        Assert.assertEquals(new Periode("01.2014", "05.2014"), mapPeriode.keySet().toArray()[1]);
        Assert.assertEquals(new Periode("06.2014", null), mapPeriode.lastKey());
    }

    @Test
    public void testDecoupPeriodeInversConjRequ() throws Exception {
        // [O R][O R => [06.2013 -
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeConj("06.2013", "12.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.REFUS));
        periodes.add(buildPeriodeConj("01.2014", null, PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("01.2014", null, PcaStatus.REFUS));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = decoupagePeriodeOcc.splitPeriode(periodes);
        Assert.assertEquals(1, mapPeriode.size());
        Assert.assertEquals(new Periode("06.2013", null), mapPeriode.firstKey());
    }

    @Test
    public void testDecoupPeriodeAvecDom2R() throws Exception {
        // [O R][O R => [06.2013 -
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("06.2013", "12.2013", PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("01.2014", null, PcaStatus.OCTROI));
        periodes.add(buildPeriodeRequ("01.2014", null, PcaStatus.OCTROI));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> mapPeriode = decoupagePeriodeOcc.splitPeriode(periodes);
        Assert.assertEquals(1, mapPeriode.size());
        Assert.assertEquals(new Periode("06.2013", null), mapPeriode.firstKey());
    }

    @Test
    public void testSlitByPersonneDansCalculeCasSimple() throws Exception {
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();

        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.REFUS, buildListMembreFamille("1")));
        periodes.add(buildPeriodeRequ("06.2014", null, PcaStatus.REFUS, buildListMembreFamille("1")));
        TreeMap<PeriodeOcc, List<PeriodeOcc>> map = decoupagePeriodeOcc.splitPeriode(periodes);

        assertEquals(1, map.size());
        assertEquals(new Periode("01.2014", null), map.firstKey());
    }

    @Test
    public void testDecoupeAvecUnChangementMembreFamille() throws Exception {
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.OCTROI, buildListMembreFamille("1", "2")));
        periodes.add(buildPeriodeRequ("06.2014", "08.2014", PcaStatus.OCTROI, buildListMembreFamille("1", "3")));
        periodes.add(buildPeriodeRequ("09.2014", "12.2014", PcaStatus.OCTROI, buildListMembreFamille("2")));
        periodes.add(buildPeriodeRequ("01.2015", null, PcaStatus.OCTROI, buildListMembreFamille("2")));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> map = decoupagePeriodeOcc.splitPeriode(periodes);

        assertEquals(3, map.size());
        assertEquals(new Periode("01.2014", "05.2014"), map.firstKey());
        assertEquals(new Periode("06.2014", "08.2014"), map.keySet().toArray()[1]);
        assertEquals(new Periode("09.2014", null), map.lastKey());
    }

    @Test
    public void testDecoupeAvecUnChangementMembreFamilleEtStatus() throws Exception {
        List<PeriodeOcc> periodes = new ArrayList<PeriodeOcc>();
        periodes.add(buildPeriodeRequ("01.2014", "05.2014", PcaStatus.OCTROI, buildListMembreFamille("1", "2")));
        periodes.add(buildPeriodeRequ("06.2014", "08.2014", PcaStatus.REFUS, buildListMembreFamille("1", "3")));
        periodes.add(buildPeriodeRequ("09.2014", "12.2014", PcaStatus.OCTROI, buildListMembreFamille("2")));
        periodes.add(buildPeriodeRequ("01.2015", null, PcaStatus.REFUS, buildListMembreFamille("2")));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> map = decoupagePeriodeOcc.splitPeriode(periodes);

        assertEquals(4, map.size());
        assertEquals(new Periode("01.2014", "05.2014"), map.firstKey());
        assertEquals(new Periode("06.2014", "08.2014"), map.keySet().toArray()[1]);
        assertEquals(new Periode("09.2014", "12.2014"), map.keySet().toArray()[2]);
        assertEquals(new Periode("01.2015", null), map.lastKey());
    }

    @Test
    public void testResolveDecisionsMostFavorableWithOnlyRequerant() throws Exception {
        RequerantConjoint<PeriodeOcc> periodes = new RequerantConjoint<PeriodeOcc>();
        periodes.setRequerant(buildPeriodeRequ("01.2014", null, PcaStatus.OCTROI));
        assertEquals(periodes.getRequerant(), decoupagePeriodeOcc.resolveMostFavorable(periodes));
    }

    @Test
    public void testResolveDecisionsMostFavorableWithRequerantOctroitConjointOctroi() throws Exception {
        RequerantConjoint<PeriodeOcc> periodes = new RequerantConjoint<PeriodeOcc>();
        periodes.setRequerant(buildPeriodeRequ("01.2014", null, PcaStatus.OCTROI));
        periodes.setConjoint(buildPeriodeConj("01.2014", null, PcaStatus.OCTROI));
        assertEquals(periodes.getRequerant(), decoupagePeriodeOcc.resolveMostFavorable(periodes));

        periodes.setRequerant(buildPeriodeRequ("01.2014", null, PcaStatus.OCTROI_PARTIEL));
        periodes.setConjoint(buildPeriodeConj("01.2014", null, PcaStatus.OCTROI_PARTIEL));
        assertEquals(periodes.getRequerant(), decoupagePeriodeOcc.resolveMostFavorable(periodes));
    }

    @Test
    public void testResolveDecisionsMostFavorableWithRequerantOctroitConjointRefus() throws Exception {
        RequerantConjoint<PeriodeOcc> periodes = new RequerantConjoint<PeriodeOcc>();
        periodes.setRequerant(buildPeriodeRequ("01.2014", null, PcaStatus.OCTROI));
        periodes.setConjoint(buildPeriodeConj("01.2014", null, PcaStatus.REFUS));
        assertEquals(periodes.getRequerant(), decoupagePeriodeOcc.resolveMostFavorable(periodes));

        periodes.setRequerant(buildPeriodeRequ("01.2014", null, PcaStatus.OCTROI_PARTIEL));
        periodes.setConjoint(buildPeriodeConj("01.2014", null, PcaStatus.REFUS));
        assertEquals(periodes.getRequerant(), decoupagePeriodeOcc.resolveMostFavorable(periodes));
    }

    @Test
    public void testResolveDecisionsMostFavorableWithRequerantRefusConjointOctroi() throws Exception {
        RequerantConjoint<PeriodeOcc> periodes = new RequerantConjoint<PeriodeOcc>();
        periodes.setRequerant(buildPeriodeRequ("01.2014", null, PcaStatus.REFUS));
        periodes.setConjoint(buildPeriodeConj("01.2014", null, PcaStatus.OCTROI));
        assertEquals(periodes.getConjoint(), decoupagePeriodeOcc.resolveMostFavorable(periodes));

        periodes.setRequerant(buildPeriodeRequ("01.2014", null, PcaStatus.REFUS));
        periodes.setConjoint(buildPeriodeConj("01.2014", null, PcaStatus.OCTROI_PARTIEL));
        assertEquals(periodes.getConjoint(), decoupagePeriodeOcc.resolveMostFavorable(periodes));
    }

    private List<PlanDeCalculWitMembreFamille> buildListMembreFamille(String... ids) {
        List<PlanDeCalculWitMembreFamille> membresFamille = new ArrayList<PlanDeCalculWitMembreFamille>();
        for (String id : ids) {
            membresFamille.add(buildMembreFamille(id));
        }
        return membresFamille;
    }

    private PeriodeOcc buildPeriodeConj(String dateDebut, String dateFin, PcaStatus statusPca) {
        List<PlanDeCalculWitMembreFamille> membres = new ArrayList<PlanDeCalculWitMembreFamille>();
        membres.add(new PlanDeCalculWitMembreFamille());
        PeriodeOcc p = new PeriodeOcc(dateDebut, dateFin, "1", statusPca, "1", "1", membres,
                RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_CONJOINT));
        return p;
    }

    private PeriodeOcc buildPeriodeRequ(String dateDebut, String dateFin, PcaStatus status,
            List<PlanDeCalculWitMembreFamille> membresFamille1) {
        PeriodeOcc p = new PeriodeOcc(dateDebut, dateFin, "1", status, "1", "1", membresFamille1,
                RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT));

        return p;
    }

    private PeriodeOcc buildPeriodeRequ(String dateDebut, String dateFin, PcaStatus statusPca) {
        List<PlanDeCalculWitMembreFamille> membres = new ArrayList<PlanDeCalculWitMembreFamille>();
        membres.add(new PlanDeCalculWitMembreFamille());
        PeriodeOcc p = new PeriodeOcc(dateDebut, dateFin, "1", statusPca, "1", "1", membres,
                RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT));
        return p;
    }

    private PlanDeCalculWitMembreFamille buildMembreFamille(String id) {
        PlanDeCalculWitMembreFamille membreFamille = new PlanDeCalculWitMembreFamille();
        membreFamille.getDroitMembreFamille().setId(id);
        return membreFamille;
    }

}
