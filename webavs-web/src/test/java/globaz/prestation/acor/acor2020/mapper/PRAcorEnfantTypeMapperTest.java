package globaz.prestation.acor.acor2020.mapper;

import ch.admin.zas.xmlns.acor_rentes_in_host._0.PeriodeEnfantType;
import ch.globaz.common.util.Dates;
import globaz.hera.api.ISFPeriode;
import globaz.hera.wrapper.SFPeriodeWrapper;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PRAcorEnfantTypeMapperTest {

    @Test
    public void createPeriode_avecUnePeriodeDeRefusSansDateDeFinEtDateDebutIndentiqueALaDateDeNaissance_doitRetourner0Periodes() {
        SFPeriodeWrapper sfPeriodeWrapper = createPeriodeAfRefus("01.01.2020", null);
        List<ISFPeriode> periodes = new ArrayList<>();
        periodes.add(sfPeriodeWrapper);
        List<PeriodeEnfantType> periodeAfOuRefus = PRAcorEnfantTypeMapper.createPeriodeAllocationFamilliale(periodes, LocalDate.of(2020, 1, 1));
        assertThat(periodeAfOuRefus).isEmpty();
    }

    @Test
    public void createPeriode_avecUnePeriodeDeRefusSansDateDeFin_doitRetourner1Periodes() {
        SFPeriodeWrapper sfPeriodeWrapper = createPeriodeAfRefus("01.01.2021", null);
        List<ISFPeriode> periodes = new ArrayList<>();
        periodes.add(sfPeriodeWrapper);
        List<PeriodeEnfantType> periodeAfOuRefus = PRAcorEnfantTypeMapper.createPeriodeAllocationFamilliale(periodes, LocalDate.of(2020, 1, 1));
        assertThat(periodeAfOuRefus).hasSize(1);
        assertThat(periodeAfOuRefus.get(0).getDebut()).hasToString("2020-01-01");
        assertThat(periodeAfOuRefus.get(0).getFin()).hasToString("2020-12-31");
    }

    @Test
    public void createPeriode_avecUnePeriodeDeRefusAvecDateDeFinEtDateDebutIndentiqueALaDateDeNaissance_doitRetourner1Periodes() {
        SFPeriodeWrapper sfPeriodeWrapper = createPeriodeAfRefus("01.01.2000", "01.01.2005");
        List<ISFPeriode> periodes = new ArrayList<>();
        periodes.add(sfPeriodeWrapper);
        List<PeriodeEnfantType> periodeEnfantTypes = PRAcorEnfantTypeMapper.createPeriodeAllocationFamilliale(periodes, LocalDate.of(2000, 1, 1));
        assertThat(periodeEnfantTypes).hasSize(1);
        assertThat(periodeEnfantTypes.get(0).getDebut()).isEqualTo(Dates.toXMLGregorianCalendar(LocalDate.of(2005, 1, 2)));
    }

    @Test
    public void createPeriode_avecDeuxPeriodesDeRefusAvecDateDefin_doitRetourner3Periodes() {
        List<ISFPeriode> periodes = new ArrayList<>();
        periodes.add(createPeriodeAfRefus("01.01.2001", "01.01.2005"));
        periodes.add(createPeriodeAfRefus("01.01.2007", "01.01.2009"));
        LocalDate dateNaissance = LocalDate.of(2000, 1, 1);
        List<PeriodeEnfantType> periodeAfOuRefus = PRAcorEnfantTypeMapper.createPeriodeAllocationFamilliale(periodes, dateNaissance);
        assertThat(periodeAfOuRefus).hasSize(3);
        assertThat(periodeAfOuRefus.get(0).getDebut()).hasToString("2000-01-01");
        assertThat(periodeAfOuRefus.get(0).getFin()).hasToString("2000-12-31");
        assertThat(periodeAfOuRefus.get(1).getDebut()).hasToString("2005-01-02");
        assertThat(periodeAfOuRefus.get(1).getFin()).hasToString("2006-12-31");
        assertThat(periodeAfOuRefus.get(2).getDebut()).hasToString("2009-01-02");
        assertThat(periodeAfOuRefus.get(2).getFin()).hasToString("2025-01-01");
    }

    @Test
    public void createPeriode_avecDeuxPeriodesDeRefusSamsDateDefin_doitRetourner2Periodes() {
        List<ISFPeriode> periodes = new ArrayList<>();
        periodes.add(createPeriodeAfRefus("01.01.2001", "01.01.2005"));
        periodes.add(createPeriodeAfRefus("01.01.2007", null));
        LocalDate dateNaissance = LocalDate.of(2000, 1, 1);
        List<PeriodeEnfantType> periodeAfOuRefus = PRAcorEnfantTypeMapper.createPeriodeAllocationFamilliale(periodes, dateNaissance);
        assertThat(periodeAfOuRefus).hasSize(2);
        assertThat(periodeAfOuRefus.get(0).getDebut()).hasToString("2000-01-01");
        assertThat(periodeAfOuRefus.get(0).getFin()).hasToString("2000-12-31");
        assertThat(periodeAfOuRefus.get(1).getDebut()).hasToString("2005-01-02");
        assertThat(periodeAfOuRefus.get(1).getFin()).hasToString("2006-12-31");
    }

    @Test
    public void createPeriode_avecDeuxPeriodesDeRefusSansDateDefinEtDateDebutIdentiqueDateNaissance_doitRetourner1Periodes() {
        List<ISFPeriode> periodes = new ArrayList<>();
        periodes.add(createPeriodeAfRefus("01.01.2000", "01.01.2005"));
        periodes.add(createPeriodeAfRefus("01.01.2007", null));
        LocalDate dateNaissance = LocalDate.of(2000, 1, 1);
        List<PeriodeEnfantType> periodeAfOuRefus = PRAcorEnfantTypeMapper.createPeriodeAllocationFamilliale(periodes, dateNaissance);
        assertThat(periodeAfOuRefus).hasSize(1);
        assertThat(periodeAfOuRefus.get(0).getDebut()).hasToString("2005-01-02");
        assertThat(periodeAfOuRefus.get(0).getFin()).hasToString("2006-12-31");
    }

    private SFPeriodeWrapper createPeriodeAfRefus(String dateDebut, String dateFin) {
        SFPeriodeWrapper sfPeriodeWrapper = new SFPeriodeWrapper();
        sfPeriodeWrapper.setDateDebut(dateDebut);
        sfPeriodeWrapper.setDateFin(dateFin);
        sfPeriodeWrapper.setType(ch.globaz.hera.business.constantes.ISFPeriode.CS_TYPE_PERIODE_REFUS_AF);
        return sfPeriodeWrapper;
    }

}
