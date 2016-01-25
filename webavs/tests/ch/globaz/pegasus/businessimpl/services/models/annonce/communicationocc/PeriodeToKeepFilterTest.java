package ch.globaz.pegasus.businessimpl.services.models.annonce.communicationocc;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import org.junit.Test;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.pca.PcaStatus;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamille;

public class PeriodeToKeepFilterTest {

    @Test
    public void testGroupePeriodeAvecDeuxPeriode() throws Exception {
        // V2[ ....... ][
        // V1[ ... ][
        List<PeriodeOcc> periodesNew = new ArrayList<PeriodeOcc>();
        periodesNew.add(buildPeriode("01.2014", "06.2014"));
        periodesNew.add(buildPeriode("07.2014", null));

        List<PeriodeOcc> periodesOld = new ArrayList<PeriodeOcc>();
        periodesOld.add(buildPeriode("01.2014", "03.2014"));
        periodesOld.add(buildPeriode("04.2014", null));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> map = PeriodeToKeepFilter.groupPeriode(periodesNew, periodesOld);
        assertEquals(2, map.size());
        assertEquals(periodesNew.get(0), map.firstKey());
        assertEquals(2, map.firstEntry().getValue().size());
        assertEquals(periodesOld.get(0), map.firstEntry().getValue().get(0));
        assertEquals(periodesOld.get(1), map.firstEntry().getValue().get(1));

        assertEquals(periodesNew.get(1), map.lastKey());
        assertEquals(1, map.lastEntry().getValue().size());
        assertEquals(periodesOld.get(1), map.lastEntry().getValue().get(0));
    }

    @Test
    public void testGroupePeriodeAvecDeuxPeriodeIDentiques() throws Exception {
        // V2[ ....... ][
        // V1[ ....... ][
        List<PeriodeOcc> periodesNew = new ArrayList<PeriodeOcc>();
        periodesNew.add(buildPeriode("01.2014", "03.2014"));
        periodesNew.add(buildPeriode("04.2014", null));

        List<PeriodeOcc> periodesOld = new ArrayList<PeriodeOcc>();
        periodesOld.add(buildPeriode("01.2014", "03.2014"));
        periodesOld.add(buildPeriode("04.2014", null));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> map = PeriodeToKeepFilter.groupPeriode(periodesNew, periodesOld);
        assertEquals(2, map.size());
        assertEquals(periodesNew.get(0), map.firstKey());
        assertEquals(1, map.firstEntry().getValue().size());
        assertEquals(periodesOld.get(0), map.firstEntry().getValue().get(0));
        assertEquals(periodesNew.get(1), map.lastKey());
        assertEquals(1, map.lastEntry().getValue().size());
        assertEquals(periodesOld.get(1), map.lastEntry().getValue().get(0));
    }

    @Test
    public void testGroupePeriodeAvecUnePeriodePuiDeux() throws Exception {
        // V2[ ....... ][
        // V1[
        List<PeriodeOcc> periodesNew = new ArrayList<PeriodeOcc>();
        periodesNew.add(buildPeriode("01.2014", "03.2014"));
        periodesNew.add(buildPeriode("04.2014", null));

        List<PeriodeOcc> periodesOld = new ArrayList<PeriodeOcc>();
        periodesOld.add(buildPeriode("01.2014", null));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> map = PeriodeToKeepFilter.groupPeriode(periodesNew, periodesOld);
        assertEquals(2, map.size());
        assertEquals(periodesNew.get(0), map.firstKey());
        assertEquals(1, map.firstEntry().getValue().size());
        assertEquals(periodesOld.get(0), map.firstEntry().getValue().get(0));
        assertEquals(periodesNew.get(1), map.lastKey());
        assertEquals(1, map.lastEntry().getValue().size());
        assertEquals(periodesOld.get(0), map.lastEntry().getValue().get(0));
    }

    @Test
    public void testGroupePeriodeAvecUnePeriodeFerme() throws Exception {
        List<PeriodeOcc> periodesNew = new ArrayList<PeriodeOcc>();
        periodesNew.add(buildPeriode("01.2014", "06.2014"));

        List<PeriodeOcc> periodesOld = new ArrayList<PeriodeOcc>();
        periodesOld.add(buildPeriode("01.2014", "03.2014"));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> map = PeriodeToKeepFilter.groupPeriode(periodesNew, periodesOld);
        assertEquals(1, map.size());
        assertEquals(periodesNew.get(0), map.firstKey());
        assertEquals(1, map.firstEntry().getValue().size());
        assertEquals(periodesOld.get(0), map.firstEntry().getValue().get(0));
    }

    @Test
    public void testGroupePeriodeAvecUnePeriodeOuverte() throws Exception {
        List<PeriodeOcc> periodesNew = new ArrayList<PeriodeOcc>();
        periodesNew.add(buildPeriode("01.2014", null));

        List<PeriodeOcc> periodesOld = new ArrayList<PeriodeOcc>();
        periodesOld.add(buildPeriode("01.2014", null));

        TreeMap<PeriodeOcc, List<PeriodeOcc>> map = PeriodeToKeepFilter.groupPeriode(periodesNew, periodesOld);
        assertEquals(1, map.size());
        assertEquals(periodesNew.get(0), map.firstKey());
        assertEquals(1, map.firstEntry().getValue().size());
        assertEquals(periodesOld.get(0), map.firstEntry().getValue().get(0));
    }

    @Test
    public void testFiltrePeriodeToKeepWithSamePeriode() throws Exception {
        List<PeriodeOcc> periodesNew = new ArrayList<PeriodeOcc>();
        List<PlanDeCalculWitMembreFamille> membresFamille1 = buildListMembreFamille("1");
        periodesNew.add(buildPeriode("01.2014", null, PcaStatus.OCTROI, membresFamille1));

        List<PeriodeOcc> periodesOld = new ArrayList<PeriodeOcc>();
        periodesOld.add(buildPeriode("01.2014", null, PcaStatus.OCTROI, membresFamille1));

        List<PeriodeOcc> periodes = PeriodeToKeepFilter.filtrePeriodeToKeep(periodesNew, periodesOld);
        assertEquals(0, periodes.size());
    }

    @Test
    public void testFiltrePeriodeToKeepWithOneSamePeriode() throws Exception {
        List<PeriodeOcc> periodesNew = new ArrayList<PeriodeOcc>();
        List<PlanDeCalculWitMembreFamille> membresFamille1 = buildListMembreFamille("1");
        periodesNew.add(buildPeriode("04.2014", null, PcaStatus.OCTROI_PARTIEL, membresFamille1));
        periodesNew.add(buildPeriode("01.2014", "03.2014", PcaStatus.OCTROI, membresFamille1));

        List<PeriodeOcc> periodesOld = new ArrayList<PeriodeOcc>();
        periodesOld.add(buildPeriode("04.2014", null, PcaStatus.REFUS, membresFamille1));
        periodesOld.add(buildPeriode("01.2014", "03.2014", PcaStatus.OCTROI, membresFamille1));

        List<PeriodeOcc> periodes = PeriodeToKeepFilter.filtrePeriodeToKeep(periodesNew, periodesOld);
        assertEquals(1, periodes.size());
        assertEquals(periodesNew.get(0), periodes.get(0));
    }

    @Test
    public void testFiltrePeriodeAncienneOcroisNouvelleRefusOctroi() throws Exception {
        // V2[ R ][ O
        // V1[ O
        List<PeriodeOcc> periodesNew = new ArrayList<PeriodeOcc>();
        List<PlanDeCalculWitMembreFamille> membresFamille1 = buildListMembreFamille("1");
        periodesNew.add(buildPeriode("01.2014", "03.2014", PcaStatus.REFUS, membresFamille1));
        periodesNew.add(buildPeriode("04.2014", null, PcaStatus.OCTROI_PARTIEL, membresFamille1));

        List<PeriodeOcc> periodesOld = new ArrayList<PeriodeOcc>();
        periodesOld.add(buildPeriode("01.2014", null, PcaStatus.OCTROI, membresFamille1));

        List<PeriodeOcc> periodes = PeriodeToKeepFilter.filtrePeriodeToKeep(periodesNew, periodesOld);
        assertEquals(2, periodes.size());
        assertEquals(periodesNew.get(0), periodes.get(0));
        assertEquals(periodesNew.get(1), periodes.get(1));
    }

    @Test
    public void testFiltrePeriodeAncienneOcroisNouvelleRefusOctroiRefus() throws Exception {
        // V2[ R ][ O ][ R
        // V1[ O
        List<PeriodeOcc> periodesNew = new ArrayList<PeriodeOcc>();
        List<PlanDeCalculWitMembreFamille> membresFamille1 = buildListMembreFamille("1");
        periodesNew.add(buildPeriode("01.2014", "03.2014", PcaStatus.REFUS, membresFamille1));
        periodesNew.add(buildPeriode("04.2014", "06.2014", PcaStatus.OCTROI_PARTIEL, membresFamille1));
        periodesNew.add(buildPeriode("07.2014", null, PcaStatus.REFUS, membresFamille1));

        List<PeriodeOcc> periodesOld = new ArrayList<PeriodeOcc>();
        periodesOld.add(buildPeriode("01.2014", null, PcaStatus.OCTROI, membresFamille1));

        List<PeriodeOcc> periodes = PeriodeToKeepFilter.filtrePeriodeToKeep(periodesNew, periodesOld);
        assertEquals(3, periodes.size());
        assertEquals(periodesNew.get(0), periodes.get(0));
        assertEquals(periodesNew.get(1), periodes.get(1));
        assertEquals(periodesNew.get(2), periodes.get(2));
    }

    @Test
    public void testFiltrePeriodeAncienneOcroisNouvelleOctroisRefusOctroiRefus() throws Exception {
        // V2[ O ][ R ][ O ][ R ][ O
        // V1[ O
        List<PeriodeOcc> periodesNew = new ArrayList<PeriodeOcc>();
        List<PlanDeCalculWitMembreFamille> membresFamille1 = buildListMembreFamille("1");
        periodesNew.add(buildPeriode("01.2014", "03.2014", PcaStatus.OCTROI, membresFamille1));
        periodesNew.add(buildPeriode("04.2014", "06.2014", PcaStatus.REFUS, membresFamille1));
        periodesNew.add(buildPeriode("07.2014", "08.2014", PcaStatus.OCTROI, membresFamille1));
        periodesNew.add(buildPeriode("09.2014", "10.2014", PcaStatus.REFUS, membresFamille1));
        periodesNew.add(buildPeriode("11.2014", null, PcaStatus.OCTROI, membresFamille1));

        List<PeriodeOcc> periodesOld = new ArrayList<PeriodeOcc>();
        periodesOld.add(buildPeriode("01.2014", null, PcaStatus.OCTROI, membresFamille1));

        List<PeriodeOcc> periodes = PeriodeToKeepFilter.filtrePeriodeToKeep(periodesNew, periodesOld);
        assertEquals(3, periodes.size());
        assertEquals(periodesNew.get(1), periodes.get(0));
        assertEquals(periodesNew.get(2), periodes.get(1));
        assertEquals(periodesNew.get(4), periodes.get(2));
    }

    private List<PlanDeCalculWitMembreFamille> buildListMembreFamille(String... ids) {
        List<PlanDeCalculWitMembreFamille> membresFamille = new ArrayList<PlanDeCalculWitMembreFamille>();
        for (String id : ids) {
            PlanDeCalculWitMembreFamille membreFamille = new PlanDeCalculWitMembreFamille();
            membreFamille.getDroitMembreFamille().setId(id);
            membresFamille.add(membreFamille);
        }
        return membresFamille;
    }

    private PeriodeOcc buildPeriode(String dateDebut, String dateFin) {
        List<PlanDeCalculWitMembreFamille> membresFamille1 = buildListMembreFamille("1");
        PeriodeOcc periodeOcc = new PeriodeOcc(dateDebut, dateFin, "1", PcaStatus.OCTROI, "", "1", membresFamille1,
                RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT));
        return periodeOcc;
    }

    private PeriodeOcc buildPeriode(String dateDebut, String dateFin, PcaStatus statusPca,
            List<PlanDeCalculWitMembreFamille> membresFamille) {

        PeriodeOcc periodeOcc = new PeriodeOcc(dateDebut, dateFin, "1", statusPca, "", "1", membresFamille,
                RoleMembreFamille.fromValue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT));
        return periodeOcc;
    }

}
