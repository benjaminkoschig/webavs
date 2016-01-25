package ch.globaz.vulpecula.external.services;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.external.models.CotisationSearchComplexModel;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;

public class CotisationServiceImplTest {
    private CotisationServiceImpl cotisationService;
    private List<Cotisation> cotisations;

    @Before
    public void setUp() {
        cotisationService = spy(new CotisationServiceImpl());
        cotisations = new ArrayList<Cotisation>();
        doReturn(cotisations).when(cotisationService).searchAndFetch(Matchers.any(CotisationSearchComplexModel.class));
    }

    @Test
    public void findByIdAffilieForDate_GivenEmptyCotisation_ShouldBeEmpty() {
        assertThat(cotisationService.findByIdAffilieForDate("1", Date.now(), Date.now()).size(), is(0));
    }

    @Test
    public void findByIdAffilieForDate20042005_GivenCotisation20062007_ShouldBeEmpty() {
        addCotisation(dateForYear(2006), dateForYear(2007));
        assertThat(cotisationService.findByIdAffilieForDate("1", dateForYear(2004), dateForYear(2005)).size(), is(0));
    }

    @Test
    public void findByIdAffilieForDate20062007_GivenCotisation20062007_ShouldReturn1Cotisation() {
        addCotisation(dateForYear(2006), dateForYear(2007));
        assertThat(cotisationService.findByIdAffilieForDate("1", dateForYear(2006), dateForYear(2007)).size(), is(1));
    }

    @Test
    public void findByIdAffilieForDate20062007_GivenCotisation2006_ShouldReturn1Cotisation() {
        addCotisation(dateForYear(2006), null);
        assertThat(cotisationService.findByIdAffilieForDate("1", dateForYear(2006), dateForYear(2007)).size(), is(1));
    }

    @Test
    public void findByIdAffilieForDate20052007_GivenCotisation2006_ShouldReturn1Cotisation() {
        addCotisation(dateForYear(2006), null);
        assertThat(cotisationService.findByIdAffilieForDate("1", dateForYear(2005), dateForYear(2007)).size(), is(1));
    }

    @Test
    public void findByIdAffilieForDate20072010_GivenCotisation2006_ShouldReturn1Cotisation() {
        addCotisation(dateForYear(2006), null);
        assertThat(cotisationService.findByIdAffilieForDate("1", dateForYear(2007), dateForYear(2010)).size(), is(1));
    }

    @Test
    public void findByIdAffilieForDate2005_GivenCotisation2006_ShouldReturn1Cotisation() {
        addCotisation(dateForYear(2006), null);
        assertThat(cotisationService.findByIdAffilieForDate("1", dateForYear(2005), null).size(), is(1));
    }

    @Test
    public void findByIdAffilieForDate2006_GivenCotisation2004_ShouldReturn1Cotisation() {
        addCotisation(dateForYear(2005), null);
        assertThat(cotisationService.findByIdAffilieForDate("1", dateForYear(2006), null).size(), is(1));
    }

    @Test
    public void findByIdAffilieForDate2006_GivenCotisation20042005_ShouldBeEmpty() {
        addCotisation(dateForYear(2004), dateForYear(2005));
        assertThat(cotisationService.findByIdAffilieForDate("1", dateForYear(2006), null).size(), is(0));
    }

    @Test
    public void findByIdAffilieForDate2007_GivenCotisation20042007_ShouldBeReturn1Cotisation() {
        addCotisation(dateForYear(2004), dateForYear(2007));
        assertThat(cotisationService.findByIdAffilieForDate("1", dateForYear(2007), null).size(), is(1));
    }

    @Test
    public void findByIdAffilieForDate20002010_GivenCotisation2004_ShouldReturn1Cotisation() {
        addCotisation(dateForYear(2000), dateForYear(2010));
        assertThat(cotisationService.findByIdAffilieForDate("1", dateForYear(2004), null).size(), is(1));
    }

    private Date dateForYear(int annee) {
        return new Annee(annee).getFirstDayOfYear();
    }

    private void addCotisation(Date dateDebut, Date dateFin) {
        Cotisation cotisation = new Cotisation();
        cotisation.setDateDebut(dateDebut);
        cotisation.setDateFin(dateFin);
        cotisations.add(cotisation);
    }
}
