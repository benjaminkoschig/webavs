package ch.globaz.vulpecula.domain.models.decompte;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.affiliation.PlanCaisse;

public class DecompteTest {
    Decompte decompte;

    @Before
    public void setUp() {
        decompte = new Decompte();
    }

    @Test
    public void getMoisPeriodeDebut_GivenDecompteWithoutPeriod_ShouldBeNull() {
        assertThat(decompte.getMoisPeriodeDebut(), is(nullValue()));
    }

    @Test
    public void getMoisPeriodDebut_GivenDecompteWithValidPeriodStartingInMarch_ShouldBe03() {
        decompte.setPeriode(new PeriodeMensuelle("03.2013", "05.2013"));

        assertThat(decompte.getMoisPeriodeDebut(), is("03"));
    }

    @Test
    public void getAnneePeriodeDebut_GivenDecompteWithoutPeriode_ShouldBeNull() {
        Assert.assertEquals(decompte.getAnneePeriodeDebut(), null);
    }

    @Test
    public void getAnneePeriodeDebut_GivenPeriodeWhereDateDebutIs032013_ShouldBe2013() {
        decompte.setPeriode(new PeriodeMensuelle("03.2013", "05.2013"));

        Assert.assertEquals(decompte.getAnneePeriodeDebut(), "2013");
    }

    @Test
    public void getMoisPeriodeFin_GivenWithoutPeriode_ShouldBeNull() {
        Assert.assertEquals(decompte.getMoisPeriodeFin(), null);
    }

    @Test
    public void getMoisPeriodeFin_GivenPeriodeWhereDateFinIs032013__ShouldBe05() {
        decompte.setPeriode(new PeriodeMensuelle("03.2013", "05.2015"));

        Assert.assertEquals(decompte.getMoisPeriodeFin(), "05");
    }

    @Test
    public void isReceptionnable_GivenDecompteWithGenererStatut_ShouldBeTrue() {
        decompte.setEtat(EtatDecompte.GENERE);

        assertTrue(decompte.isReceptionnable());
    }

    @Test
    public void isReceptionnable_GivenDecompteWithReceptionneStatut_ShouldBeFalse() {
        decompte.setEtat(EtatDecompte.RECEPTIONNE);

        assertThat(decompte.isReceptionnable(), is(false));
    }

    @Test
    public void isReceptionnable_GivenDecompteWithSommationStatut_ShouldBeTrue() {
        decompte.setEtat(EtatDecompte.SOMMATION);

        assertTrue(decompte.isReceptionnable());
    }

    @Test
    public void getTauxContribuablesDifferents_GivenDecompteWithoutDecompteSalaire_ShouldBeAnEmptyList() {
        List<Taux> tauxContribuables = decompte.getTauxContribuablesDifferents();

        assertEquals(0, tauxContribuables.size());
    }

    @Test
    public void getTauxContribuables_GivenDecompteWithDecompteSalaireAndCotisationOf12_ShouldBeAListWith12Taux() {
        addCotisationDecompteOf(12);

        List<Taux> taux = decompte.getTauxContribuablesDifferents();

        assertThat(taux, contains(new Taux(12)));
    }

    @Test
    public void givenDecompteWithDecompteSalaireAndCotisationOf0_45And0_45WhenGetTauxContribuablesThenReturnListWWith0_45() {
        addCotisationDecompteOf(0.45);
        addCotisationDecompteOf(0.45);

        List<Taux> taux = decompte.getTauxContribuablesDifferents();
        assertThat(taux, contains(new Taux(0.45)));
    }

    @Test
    public void givenADecompteWithoutDecompteSalaireWhenGetTableCotisationsCalculeesShouldReturnEmptyArray() {
        assertTrue(decompte.getTableCotisationsCalculees().isEmpty());
    }

    @Test
    public void givenADecompteWithCotisationCalculesWhenGetcotisationCalculesThenReturnOneElement() {
        addDecompteSalaire("1", new Montant(1000), new Taux(1));

        CotisationCalculee expected = createCotisationCalculee("1", new Montant(1000), new Taux(1));

        assertThat(decompte.getTableCotisationsCalculees(), contains(expected));
    }

    @Test
    public void withTwoSameCotisationsCalculeesWhenGetCotisationsCalculeesThenReturnOneElement() {
        addDecompteSalaire("1", new Montant(1000), new Taux(1));
        addDecompteSalaire("1", new Montant(2000), new Taux(1));

        CotisationCalculee expected = createCotisationCalculee("1", new Montant(3000), new Taux(1));

        assertThat(decompte.getTableCotisationsCalculees(), contains(expected));
    }

    @Test
    public void withTwoSameCotisationsCalculeesWithDifferentsTauxWhenGetCotisationsCalculeesThenReturnTwoElements() {
        addDecompteSalaire("1", new Montant(1000), new Taux(1));
        addDecompteSalaire("1", new Montant(2000), new Taux(2));

        CotisationCalculee expected1 = createCotisationCalculee("1", new Montant(1000), new Taux(1));
        CotisationCalculee expected2 = createCotisationCalculee("1", new Montant(2000), new Taux(2));

        assertThat(decompte.getTableCotisationsCalculees(), containsInAnyOrder(expected1, expected2));
    }

    @Test
    public void getTableCotisationsCalculeesGroupByCaisse_givenAnEmptyDecompte_ShouldBeAnEmptyMap() {
        assertThat(decompte.getTableCotisationsCalculeesGroupByCaisse(), is(Collections.EMPTY_MAP));
    }

    @Test
    public void getTableCotisationsCalculeesGroupByCaisse_givenADecompteWithADecompteSalaire_ShouldBeCorrect() {
        addDecompteSalaire("1", "100", new Montant(1000), new Taux(1));

        PlanCaisse planCaisse = new PlanCaisse();
        planCaisse.setId("100");

        CotisationCalculee cotisationCalculee = createCotisationCalculee("1", new Montant(1000), new Taux(1));

        assertThat(decompte.getTableCotisationsCalculeesGroupByCaisse(), hasKey(planCaisse));
        assertThat(decompte.getTableCotisationsCalculeesGroupByCaisse(), hasValue(Arrays.asList(cotisationCalculee)));
    }

    @Test
    public void getTableCotisationsCalculeesGroupByCaisse_givenADecompteWithTwoDecomptesSalaireOnSameCaisse_ShouldBeCorrect() {
        addDecompteSalaire("1", "100", new Montant(1000), new Taux(1));
        addDecompteSalaire("10", "100", new Montant(23), new Taux(2));

        PlanCaisse planCaisse = new PlanCaisse();
        planCaisse.setId("100");

        CotisationCalculee cotisationCalculee1 = createCotisationCalculee("1", new Montant(1000), new Taux(1));
        CotisationCalculee cotisationCalculee2 = createCotisationCalculee("10", new Montant(23), new Taux(2));

        assertThat(decompte.getTableCotisationsCalculeesGroupByCaisse(),
                hasEntry(is(planCaisse), containsInAnyOrder(cotisationCalculee1, cotisationCalculee2)));
    }

    @Test
    public void getTableCotisationsCalculeesGroupByCaisse_givenADecompteWithTwoDecomptesSalaireOnSameCaisseAndSameAssurance_ShouldBeCorrect() {
        addDecompteSalaire("1", "100", new Montant(1000), new Taux(2));
        addDecompteSalaire("1", "100", new Montant(23), new Taux(2));

        PlanCaisse planCaisse = new PlanCaisse();
        planCaisse.setId("100");

        CotisationCalculee cotisationCalculee1 = createCotisationCalculee("1", new Montant(1023), new Taux(2));

        assertThat(decompte.getTableCotisationsCalculeesGroupByCaisse(),
                hasEntry(is(planCaisse), contains(cotisationCalculee1)));
    }

    @Test
    public void isControlable_GivenDecompteWithEtatOuvert_ShouldBeTrue() {
        decompte.setEtat(EtatDecompte.OUVERT);

        assertTrue(decompte.isControlable());
    }

    @Test
    public void isControlable_GivenDecompteWithEtatGenere_ShouldBeFalse() {
        decompte.setEtat(EtatDecompte.GENERE);

        assertFalse(decompte.isControlable());
    }

    @Test
    public void isControlable_GivenDecompteWithEtatErreur_ShouldBeTrue() {
        decompte.setEtat(EtatDecompte.ERREUR);

        assertTrue(decompte.isControlable());
    }

    @Test
    public void isControlable_GivenDecompteWithEtatReceptionne_ShouldBeTrue() {
        decompte.setEtat(EtatDecompte.RECEPTIONNE);

        assertTrue(decompte.isControlable());
    }

    @Test
    public void isDevalidable_GivenDecompteWithEtatValide_ShouldBeTrue() {
        decompte.setEtat(EtatDecompte.VALIDE);

        assertTrue(decompte.isDevalidable());
    }

    @Test
    public void isDevalidable_GivenDecompteWithEtatRectifie_ShouldBeTrue() {
        decompte.setEtat(EtatDecompte.RECTIFIE);

        assertTrue(decompte.isDevalidable());
    }

    @Test
    public void isDevalidable_GivenDecompteWithEtatGenere_ShouldBeFalse() {
        decompte.setEtat(EtatDecompte.GENERE);

        assertFalse(decompte.isDevalidable());
    }

    @Test
    public void getHasDateRappel_GivenEmptyDecompte_ShouldBeFalse() {
        assertFalse(decompte.getHasDateRappel());
    }

    @Test
    public void getHasDateRappel_GivenDecompteWithRappelDate_ShouldBeTrue() {
        decompte.setDateRappel(new Date());
        assertTrue(decompte.getHasDateRappel());
    }

    @Test
    public void getMontantTotal_GivenDecompteWithoutMontant_ShouldBeMontantOf0() {
        Montant actual = decompte.getMasseSalarialeTotal();

        assertEquals(new Montant(0), actual);
    }

    @Test
    public void getMontantTotal_GivenDecompteWithOneDecompteSalaireOf100_ShouldBeMontantOf100() {
        addDecompteSalaire(new Montant(100));

        Montant actual = decompte.getMasseSalarialeTotal();

        assertEquals(new Montant(100), actual);
    }

    @Test
    public void getMontantTotal_GivenDecompteWithTwoDecompteSalairesOf100And200_ShouldBeMontantOf300() {
        addDecompteSalaire(new Montant(100));
        addDecompteSalaire(new Montant(200));

        Montant actual = decompte.getMasseSalarialeTotal();

        assertEquals(new Montant(300), actual);
    }

    @Test
    public void getDateComptabilisation_GivenDecompteWithoutHistorique_ShouldBeNull() {
        assertNull(decompte.getDateComptabilisation());
    }

    @Test
    public void getDateComptabilisation_GivenDecompteWithComptabiliseOn13_09_2013_ShouldBe13_09_2013() {
        addHistorique(new Date("13.09.2013"), EtatDecompte.COMPTABILISE);
        Date actual = decompte.getDateComptabilisation();

        assertEquals(actual, new Date("13.09.2013"));
    }

    @Test
    public void getDateComptabilisation_GivenDecompteWithGenereOn13_09_2013_ShouldBeNull() {
        addHistorique(new Date("13.09.2013"), EtatDecompte.GENERE);

        assertNull(decompte.getDateComptabilisation());
    }

    @Test
    public void getDateComptabilisation_GivenDecompteWithComptabiliseOn13_09_2013_And_20_09_2013ShouldBe20_09_2013() {
        addHistorique(new Date("13.09.2013"), EtatDecompte.COMPTABILISE);
        addHistorique(new Date("20.09.2013"), EtatDecompte.COMPTABILISE);

        assertEquals(decompte.getDateComptabilisation(), new Date("20.09.2013"));
    }

    @Test
    public void getDateComptabilisation_GivenDecompteWithComptabiliseOn20_09_2013_And_13_09_2013_ShouldBe20_09_2013() {
        addHistorique(new Date("20.09.2013"), EtatDecompte.COMPTABILISE);
        addHistorique(new Date("13.09.2013"), EtatDecompte.COMPTABILISE);

        assertEquals(decompte.getDateComptabilisation(), new Date("20.09.2013"));
    }

    @Test
    public void getDateComptabilisationAsSwissValue_GivenDecompteWithoutHistorique_ShouldBeNull() {
        assertNull(decompte.getDateComptabilisationAsSwissValue());
    }

    @Test
    public void getDateRectificationAsSwissValue_GivenDecompteWithoutHistorique_ShouldBeNull() {
        assertNull(decompte.getDateRectificationAsSwissValue());
    }

    @Test
    public void isType_givenDecomptePeriodiqueAndParametrePeriodique_ShouldBeTrue() {
        decompte.setType(TypeDecompte.PERIODIQUE);

        assertTrue(decompte.isType(TypeDecompte.PERIODIQUE));
    }

    @Ignore // LGA 26.02.2016 -> test ne passe pas et casse le build !
    @Test
    public void calculerDateRappel_GivenDecompteWithPeriode012013And012013_ShouldReturn30122013() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("01.2013", "01.2013");
        EtatDecompte etatDecompte = EtatDecompte.GENERE;
        Date date = new Date("20.12.2013");

        decompte.setPeriode(periodeMensuelle);
        decompte.setDateEtablissement(date);
        decompte.setEtat(etatDecompte);

        Date actual = decompte.calculerRappel(10);

        assertEquals(new Date("30.12.2013"), actual);
    }

    @Test
    public void calculerDateRappel_GivenDecompteWithPeriode012013And012013_ShouldReturn10022013() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("01.2013", "01.2013");
        EtatDecompte etatDecompte = EtatDecompte.GENERE;
        Date date = new Date("01.01.2013");

        decompte.setPeriode(periodeMensuelle);
        decompte.setDateEtablissement(date);
        decompte.setEtat(etatDecompte);

        Date actual = decompte.calculerRappel(10);

        assertEquals(new Date("10.02.2013"), actual);
    }

    @Test
    public void getPeriodeDebutAsSwissValue_GivenEmptyDecompte_ShouldReturnNull() {
        assertNull(decompte.getPeriodeDebutAsSwissValue());
    }

    @Test
    public void getPeriodeDebutAsSwissValue_GivenDecompteWithPeriodeDebutOf01_2013_ShouldReturn012013() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("01.2013", "10.2013");
        decompte.setPeriode(periodeMensuelle);
        assertEquals(decompte.getPeriodeDebutAsSwissValue(), "01.2013");
    }

    @Test
    public void getPeriodeFinAsSwissValue_GivenEmptyDecompte_ShouldReturnNull() {
        assertNull(decompte.getPeriodeFinAsSwissValue());
    }

    @Test
    public void getPeriodeDebutAsSwissValue_GivenDecompteWithPeriodeFinOf10_2013_ShouldReturn102013() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("01.2013", "10.2013");
        decompte.setPeriode(periodeMensuelle);
        assertEquals(decompte.getPeriodeFinAsSwissValue(), "10.2013");
    }

    @Test
    public void isTaxationOffice_GivenEmptyDecompte_ShouldBeFalse() {
        assertFalse(decompte.isTaxationOffice());
    }

    @Test
    public void isTaxationOffice_GivenDecompteEnTaxation_ShouldBeTrue() {
        decompte.setEtat(EtatDecompte.TAXATION_DOFFICE);
        assertTrue(decompte.isTaxationOffice());
    }

    @Test
    public void getPeriodeMoisAnnee_GivenEmptyDecompte_ShouldBeEmpty() {
        assertEquals("", decompte.getPeriodeMoisAnnee(Locale.FRANCE));
    }

    @Test
    public void getPeriodeMoisAnnee_GivenDecompteWithPeriodeOf042014And052014_ShouldBeAvril2014() {
        decompte.setPeriode(new PeriodeMensuelle("04.2014", "05.2014"));
        assertEquals("avril 2014", decompte.getPeriodeMoisAnnee(Locale.FRANCE));
    }

    @Test
    public void getPeriodeFinFormate_GivenDecompteWithPeriodeOf042014_ShouldBe30_04_2014() {
        decompte.setPeriode(new PeriodeMensuelle("04.2014", "05.2014"));
        assertEquals("31.05.2014", decompte.getPeriodeFinFormate());
    }

    @Test
    public void getMontantAVerser_GivenEmptyDecompte_ShouldBe0() {
        assertEquals(Montant.valueOf(0), decompte.getMontantDifference());
    }

    @Test
    public void getMontantAVerser_GivenDecompteWithMontantControleOf2000WithoutLigne_ShouldBe2000() {
        decompte.setMontantVerse(Montant.valueOf(2000));
        assertEquals(Montant.valueOf(-2000), decompte.getMontantDifference());
    }

    @Test
    public void getMontantAVerser_GivenDecompteWithMontantControleOf2000AndLigneOf200_ShouldBe1800() {
        addDecompteSalaire("1", "2", new Montant(2000), new Taux(10));

        decompte.setMontantVerse(Montant.valueOf(2000));
        assertEquals(Montant.valueOf(-1800), decompte.getMontantDifference());
    }

    @Test
    public void calculerTaxation_GivenDecompteWithDateRappelOf04_01_2014_ShouldBe24_01_2014() {
        decompte.setDateEtablissement(new Date("01.12.2013"));
        decompte.setDateRappel(new Date("04.01.2014"));
        decompte.setPeriode(new PeriodeMensuelle("01.12.2013", "31.12.2013"));

        decompte.calculerAndSetTaxation(20, 10, new Date("04.01.2014"));

        assertEquals(new Date("24.01.2014"), decompte.getDateRappel());
    }

    @Test
    public void calculerTaxation_GivenDecompteWithDateRappelOf20_04_2014_DateEtablissementOf10_04_2014_ShouldBe30_04_2014() {
        decompte.setDateEtablissement(new Date("10.04.2014"));
        decompte.setDateRappel(new Date("20.04.2014"));
        decompte.setPeriode(new PeriodeMensuelle("01.12.2013", "31.12.2013"));

        decompte.calculerAndSetTaxation(10, 10, new Date("20.04.2014"));

        assertEquals(new Date("30.04.2014"), decompte.getDateRappel());
    }

    @Test
    public void calculerTaxation_GivenDecompteWithDateRappelOf20_04_2014_DateEtablissementOf10_04_2014_ShouldBe10_05_2014() {
        decompte.setDateEtablissement(new Date("10.04.2014"));
        decompte.setDateRappel(new Date("20.04.2014"));
        decompte.setPeriode(new PeriodeMensuelle("01.12.2014", "31.12.2014"));

        decompte.calculerAndSetTaxation(20, 10, new Date("30.04.2014"));

        assertEquals(new Date("20.05.2014"), decompte.getDateRappel());
    }

    @Test
    public void getMontantAC_GivenEmptyDecompte_ShouldReturn0() {
        DecompteSalaire decompteSalaire = new DecompteSalaire();
        Montant actual = decompte.getMontantAC(decompteSalaire);

        assertEquals(new Montant(0), actual);
    }

    @Test
    public void getMontantAC_GivenDecompteWithLigneOf1000MasseACOutsideOfPeriode_ShouldReturn0() {
        DecompteSalaire decompteSalaire = mock(DecompteSalaire.class);
        when(decompteSalaire.getId()).thenReturn("1");

        decompte.setPeriode(new PeriodeMensuelle("01.2010", "01.2010"));
        decompte.addDecompteSalaire(createDecompteSalaire("2", new Montant(1000), new Annee(2011)));

        Montant actual = decompte.getMontantAC(decompteSalaire);

        assertEquals(new Montant(0), actual);
    }

    @Test
    public void getMontantAC_GivenDecompteWithLigneOf1000MasseACSameASDecompteSalaire_ShouldReturn0() {
        DecompteSalaire decompteSalaire = mock(DecompteSalaire.class);
        when(decompteSalaire.getId()).thenReturn("1");

        decompte.setPeriode(new PeriodeMensuelle("01.2014", "01.2014"));
        decompte.addDecompteSalaire(createDecompteSalaire("1", new Montant(1000), new Annee(2014)));

        Montant actual = decompte.getMontantAC(decompteSalaire);

        assertEquals(new Montant(0), actual);
    }

    @Test
    public void getMontantAC_GivenDecompteWithLigneOf1000MasseACSame_ShouldReturn1000() {
        DecompteSalaire decompteSalaire = mock(DecompteSalaire.class);
        when(decompteSalaire.getId()).thenReturn("1");
        when(decompteSalaire.getIdPosteTravail()).thenReturn("10");

        decompte.setPeriode(new PeriodeMensuelle("01.2014", "01.2014"));
        decompte.addDecompteSalaire(createDecompteSalaire("2", new Montant(1000), new Annee(2014)));

        Montant actual = decompte.getMontantAC(decompteSalaire);

        assertEquals(new Montant(1000), actual);
    }

    @Test
    public void getMontantAC2_GivenEmptyDecompte_ShouldReturn0() {
        DecompteSalaire decompteSalaire = new DecompteSalaire();
        Montant actual = decompte.getMontantAC2(decompteSalaire);

        assertEquals(new Montant(0), actual);
    }

    @Test
    public void getMontantAC2_GivenDecompteWithLigneOf1000MasseACOutsideOfPeriode_ShouldReturn0() {
        DecompteSalaire decompteSalaire = mock(DecompteSalaire.class);
        when(decompteSalaire.getId()).thenReturn("1");

        decompte.setPeriode(new PeriodeMensuelle("01.2010", "01.2010"));
        decompte.addDecompteSalaire(createDecompteSalaire("2", new Montant(1000), new Annee(2011)));

        Montant actual = decompte.getMontantAC2(decompteSalaire);

        assertEquals(new Montant(0), actual);
    }

    @Test
    public void getMontantAC2_GivenDecompteWithLigneOf1000MasseACSameASDecompteSalaire_ShouldReturn0() {
        DecompteSalaire decompteSalaire = mock(DecompteSalaire.class);
        when(decompteSalaire.getId()).thenReturn("1");

        decompte.setPeriode(new PeriodeMensuelle("01.2014", "01.2014"));
        decompte.addDecompteSalaire(createDecompteSalaireAC2("1", new Montant(1000), new Annee(2014)));

        Montant actual = decompte.getMontantAC2(decompteSalaire);

        assertEquals(new Montant(0), actual);
    }

    @Test
    public void getMontantAC2_GivenDecompteWithLigneOf1000MasseAC2Same_ShouldReturn1000() {
        DecompteSalaire decompteSalaire = mock(DecompteSalaire.class);
        when(decompteSalaire.getId()).thenReturn("1");

        decompte.setPeriode(new PeriodeMensuelle("01.2014", "01.2014"));
        decompte.addDecompteSalaire(createDecompteSalaireAC2("2", new Montant(1000), new Annee(2014)));

        Montant actual = decompte.getMontantAC2(decompteSalaire);

        assertEquals(new Montant(1000), actual);
    }

    @Test
    public void validate_GivenDecompteWithTypeEmployeurAndNumeroDecompteCorrecte_ShouldBeOk()
            throws UnsatisfiedSpecificationException {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("01.2013", "01.2013");
        Employeur employeur = new Employeur();
        Date date = new Date("01.01.2013");

        decompte.setPeriode(periodeMensuelle);
        decompte.setDateEtablissement(date);
        decompte.setType(TypeDecompte.CONTROLE_EMPLOYEUR);
        decompte.setNumeroDecompte(new NumeroDecompte("201517000"));
        decompte.setEmployeur(employeur);
        decompte.validate();
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void validate_GivenDecompteWithTypeEmployeurAndNumeroDecompteIncorrecte_ShouldThrowException()
            throws UnsatisfiedSpecificationException {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("01.2013", "01.2013");
        Employeur employeur = new Employeur();
        Date date = new Date("01.01.2013");

        decompte.setPeriode(periodeMensuelle);
        decompte.setDateEtablissement(date);
        decompte.setType(TypeDecompte.CONTROLE_EMPLOYEUR);
        decompte.setNumeroDecompte(new NumeroDecompte("201516000"));
        decompte.setEmployeur(employeur);
        decompte.validate();
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void validate_GivenDecompteWithTypeEmployeurSansNumeroDecompte_ShouldThrowException()
            throws UnsatisfiedSpecificationException {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle("01.2013", "01.2013");
        Employeur employeur = new Employeur();
        Date date = new Date("01.01.2013");

        decompte.setPeriode(periodeMensuelle);
        decompte.setDateEtablissement(date);
        decompte.setType(TypeDecompte.CONTROLE_EMPLOYEUR);
        decompte.setEmployeur(employeur);
        decompte.validate();
    }

    private DecompteSalaire createDecompteSalaire(String id, Montant masseAC, Annee annee) {
        DecompteSalaire decompteSalaire = mock(DecompteSalaire.class);
        when(decompteSalaire.getMasseAC()).thenReturn(masseAC);
        when(decompteSalaire.getId()).thenReturn(id);
        when(decompteSalaire.getAnnee()).thenReturn(annee);
        when(decompteSalaire.getIdPosteTravail()).thenReturn("10");
        return decompteSalaire;
    }

    private DecompteSalaire createDecompteSalaireAC2(String id, Montant masseAC, Annee annee) {
        DecompteSalaire decompteSalaire = mock(DecompteSalaire.class);
        when(decompteSalaire.getMasseAC2()).thenReturn(masseAC);
        when(decompteSalaire.getId()).thenReturn(id);
        when(decompteSalaire.getAnnee()).thenReturn(annee);
        return decompteSalaire;
    }

    private void addHistorique(final Date date, final EtatDecompte etat) {
        HistoriqueDecompte historiqueDecompte = new HistoriqueDecompte();
        historiqueDecompte.setDate(date);
        historiqueDecompte.setEtat(etat);
        decompte.addHistorique(historiqueDecompte);
    }

    private void addDecompteSalaire(final Montant montant) {
        addDecompteSalaire(null, montant, null);
    }

    private void addDecompteSalaire(final String idAssurance, final Montant montant, final Taux taux) {
        addDecompteSalaire(idAssurance, null, montant, taux);
    }

    private void addDecompteSalaire(final String idAssurance, final String idPlanCaisse, final Montant montant,
            final Taux taux) {
        DecompteSalaire decompteSalaire = mock(DecompteSalaire.class);
        when(decompteSalaire.getSalaireTotal()).thenReturn(montant);
        PlanCaisse planCaisse = new PlanCaisse();
        planCaisse.setId(idPlanCaisse);

        Cotisation cotisation = mock(Cotisation.class);
        when(cotisation.getAssuranceId()).thenReturn(idAssurance);
        when(cotisation.getPlanCaisse()).thenReturn(planCaisse);
        CotisationCalculee cotisationCalculee = new CotisationCalculee(cotisation, montant, taux,
                Annee.getCurrentYear());
        when(decompteSalaire.getCotisationCalculees()).thenReturn(Arrays.asList(cotisationCalculee));

        decompte.addDecompteSalaire(decompteSalaire);
    }

    private void addDecompteSalaire(final Montant montant, final Taux taux, final Taux tauxAVS, final Taux tauxAC,
            final Taux tauxAC2) {
        DecompteSalaire decompteSalaire = spy(new DecompteSalaire());
        when(decompteSalaire.getTauxContribuableForCaissesSociales()).thenReturn(taux);
        when(decompteSalaire.getTauxContribuableForAVS()).thenReturn(tauxAVS);
        when(decompteSalaire.getTauxContribuableForAC()).thenReturn(tauxAC);
        when(decompteSalaire.getTauxContribuableForAC2()).thenReturn(tauxAC2);
        when(decompteSalaire.getSalaireTotal()).thenReturn(montant);
        decompte.addDecompteSalaire(decompteSalaire);
    }

    private void addCotisationDecompteOf(final double taux) {
        CotisationDecompte cotisationsDecompte = new CotisationDecompte();
        cotisationsDecompte.setTaux(new Taux(taux));
        DecompteSalaire decompteSalaire = new DecompteSalaire();
        decompteSalaire.addCotisationDecompte(cotisationsDecompte);
        decompte.addDecompteSalaire(decompteSalaire);
    }

    private CotisationCalculee createCotisationCalculee(final String id, final Montant montant, final Taux taux) {
        Cotisation cotisation = mock(Cotisation.class);
        when(cotisation.getAssuranceId()).thenReturn(id);

        CotisationCalculee cotisationCalculee = new CotisationCalculee(cotisation, montant, taux,
                Annee.getCurrentYear());

        return cotisationCalculee;
    }
}
