package globaz.apg.helpers.droits;

import globaz.apg.db.droits.APDroitProcheAidant;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CareLeaveEventIdHelperTest {

    @Test
    public void resolveCareLeaveEventId_pourNouveauDroit_renvoieUn() {
        assertThat(new CareLeaveEventIdHelper().resolveCareLeaveEventId(LocalDate.now(), 5, Collections.emptyList()).getId())
                .isEqualTo(1);
    }

    @Test
    public void resolveCareLeaveEventId_avecDroitExistant_renvoieUn() {
        APDroitProcheAidant.NbJourDateMin nbJourDateMin = new APDroitProcheAidant.NbJourDateMin();
        nbJourDateMin.setDateDebutMin(LocalDate.now().minusYears(5));
        nbJourDateMin.setCareLeaveEventId(1);
        assertThat(new CareLeaveEventIdHelper().resolveCareLeaveEventId(nbJourDateMin.getDateDebutMin(), 5, Arrays.asList(nbJourDateMin))
                                               .getId())
                .isEqualTo(1);
    }

    @Test
    public void resolveCareLeaveEventId_avecPlusieurCareEvent_ok() {
        APDroitProcheAidant.NbJourDateMin nbJourDateMin1 = new APDroitProcheAidant.NbJourDateMin();
        nbJourDateMin1.setDateDebutMin(LocalDate.of(2021, 1, 1));
        nbJourDateMin1.setCareLeaveEventId(1);
        APDroitProcheAidant.NbJourDateMin nbJourDateMin2 = new APDroitProcheAidant.NbJourDateMin();
        nbJourDateMin2.setDateDebutMin(LocalDate.of(2021, 6, 1));
        nbJourDateMin2.setCareLeaveEventId(2);
        APDroitProcheAidant.NbJourDateMin nbJourDateMin3 = new APDroitProcheAidant.NbJourDateMin();
        nbJourDateMin3.setDateDebutMin(LocalDate.of(2021, 11, 1));
        nbJourDateMin3.setCareLeaveEventId(3);

        List<APDroitProcheAidant.NbJourDateMin> nbJourDateMins = Arrays.asList(
                nbJourDateMin1,
                nbJourDateMin3,
                nbJourDateMin2);
        CareLeaveEventIdHelper resolver = new CareLeaveEventIdHelper();
        assertThat(resolver.resolveCareLeaveEventId(nbJourDateMin1.getDateDebutMin(), 5, nbJourDateMins).getId()).isEqualTo(1);
        assertThat(resolver.resolveCareLeaveEventId(nbJourDateMin1.getDateDebutMin().plusMonths(5).minusDays(1), 5, nbJourDateMins)
                           .getId()).isEqualTo(1);
        assertThat(resolver.resolveCareLeaveEventId(nbJourDateMin2.getDateDebutMin(), 5, nbJourDateMins).getId()).isEqualTo(2);
        assertThat(resolver.resolveCareLeaveEventId(nbJourDateMin2.getDateDebutMin().plusMonths(5), 5, nbJourDateMins).getId())
                .isEqualTo(3);
        assertThat(resolver.resolveCareLeaveEventId(nbJourDateMin3.getDateDebutMin(), 5, nbJourDateMins).getId()).isEqualTo(3);
        assertThat(resolver.resolveCareLeaveEventId(nbJourDateMin3.getDateDebutMin().plusMonths(5), 5, nbJourDateMins).getId())
                .isEqualTo(4);
    }

    @Test
    public void resolveCareLeaveEventId_avecDroitExistantMaisPasDansLaPeriode_renvoie2() {
        APDroitProcheAidant.NbJourDateMin nbJourDateMin = new APDroitProcheAidant.NbJourDateMin();
        nbJourDateMin.setDateDebutMin(LocalDate.now().minusMonths(20));
        nbJourDateMin.setCareLeaveEventId(1);
        assertThat(new CareLeaveEventIdHelper().resolveCareLeaveEventId(LocalDate.now(), 5, Collections.singletonList(nbJourDateMin))
                                               .getId()).isEqualTo(2);
    }

}
