package ch.globaz.vulpecula.domain.models.decompte;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;

public class DecompteSalaireTest {
    private DecompteSalaire decompteSalaire;

    @Before
    public void setUp() {
        decompteSalaire = new DecompteSalaire();
    }

    @Test
    public void givenEmptyListeAbsencesWhenGetTypesAbsencesThenReturnEmpty() {
        List<Absence> absences = new ArrayList<Absence>();
        decompteSalaire.setAbsences(absences);

        assertTrue(decompteSalaire.getTypesAbsences().isEmpty());
    }

    @Test
    public void givenNoListeAbsencesWhenGetTypesAbsencesThenReturnEmpty() {
        assertTrue(decompteSalaire.getTypesAbsences().isEmpty());
    }

    @Test
    public void givenAbsenceTypeJustifieeWhenGetTypesAbsencesThenReturnJustifiee() {
        addAbsence(TypeAbsence.ABSENCES_JUSTIFIEES);

        assertThat(decompteSalaire.getTypesAbsences(), hasItems(TypeAbsence.ABSENCES_JUSTIFIEES));
    }

    @Test
    public void givenAbsenceTypeJustifieeAndMilitaireWhenGetAbsenceValueThenReturnBoth() {
        addAbsence(TypeAbsence.ABSENCES_JUSTIFIEES);
        addAbsence(TypeAbsence.ASSURANCE_MILITAIRE);

        assertThat(decompteSalaire.getTypesAbsences(),
                hasItems(TypeAbsence.ABSENCES_JUSTIFIEES, TypeAbsence.ASSURANCE_MILITAIRE));
    }

    @Test
    public void givenLigneDecompteWithoutAbsenceWhenAddAbsenceThenReturnOneElement() {
        addAbsence(TypeAbsence.ABSENCES_JUSTIFIEES);

        assertThat(decompteSalaire.getTypesAbsences(), hasItems(TypeAbsence.ABSENCES_JUSTIFIEES));
    }

    private void addAbsence(final TypeAbsence typeAbsence) {
        Absence absence = new Absence();
        absence.setType(typeAbsence);

        decompteSalaire.addAbsence(absence);
    }

    @Test
    public void getTauxContribuableAsValue_givenADecompteSalaireWithoutCotisation_ShouldBe0() {
        String actual = decompteSalaire.getTauxContribuableForCaissesSocialesAsValue(false);
        assertEquals("0.00", actual);
    }

    @Test
    public void aDecompteSalaireWithOneCotisationOf4MustReturn4() {
        addTaux(4);

        String actual = decompteSalaire.getTauxContribuableForCaissesSocialesAsValue(false);
        assertEquals("4.00", actual);
    }

    @Test
    public void aDecompteSalaireWithTwoCotisationsOf0_25And1_33MustReturn1_58() {
        addTaux(0.25);
        addTaux(1.33);

        String actual = decompteSalaire.getTauxContribuableForCaissesSocialesAsValue(false);
        assertEquals("1.58", actual);
    }

    @Test
    public void aDecompteSalaireWithoutPosteTravailWhenIsSoumisAVSShouldReturnFalse() {
        assertFalse(decompteSalaire.isEmployeurSoumisAVS());
    }

    @Test
    public void aDecompteSalaireWithPosteTravailAndEmployeurSoumisAVSShouldReturnTrue() {
        Employeur employeur = mock(Employeur.class);
        when(employeur.isSoumisAVS()).thenReturn(true);
        addPosteTravail(employeur);

        assertTrue(decompteSalaire.isEmployeurSoumisAVS());
    }

    @Test
    public void aDecompteSalaireWithoutPosteTravailWhenIsSoumisACShouldReturnFalse() {
        assertFalse(decompteSalaire.isEmployeurSoumisAC());
    }

    @Test
    public void aDecompteSalaireWithPosteTravailAndEmployeurSoumisACShouldReturnTrue() {
        Employeur employeur = mock(Employeur.class);
        when(employeur.isSoumisAC()).thenReturn(true);
        addPosteTravail(employeur);

        assertTrue(decompteSalaire.isEmployeurSoumisAC());
    }

    @Test
    public void aDecompteSalaireWithoutPosteTravailWhenIsSoumisACS2houldReturnFalse() {
        assertFalse(decompteSalaire.isEmployeurSoumisAC2());
    }

    @Test
    public void aDecompteSalaireWithPosteTravailAndEmployeurSoumisAC2ShouldReturnTrue() {
        Employeur employeur = mock(Employeur.class);
        when(employeur.isSoumisAC2()).thenReturn(true);
        addPosteTravail(employeur);

        assertTrue(decompteSalaire.isEmployeurSoumisAC2());
    }

    @Test
    public void givenDecompteSalaireWithoutCotisationDecompteWhenGetCotisationsCalculeesShouldReturnAnEmptyList() {
        List<CotisationCalculee> cotisationsCalculees = decompteSalaire.getCotisationCalculees();

        assertTrue(cotisationsCalculees.isEmpty());
    }

    @Test
    public void aDecompteSalaireWithOneCotisationDecompteWhenGetCotisatiosnCalculeesShouldReturnOneElement() {
        decompteSalaire.setSalaireTotal(new Montant(1000));
        decompteSalaire.setPeriode(new Periode(Annee.getCurrentYear().getFirstDayOfYear(), Annee.getCurrentYear()
                .getLastDayOfYear()));

        addCotisationDecompte("1", new Taux(2));

        CotisationCalculee expected = createCotisationCalculee("1", new Montant(1000), new Taux(2));

        assertThat(decompteSalaire.getCotisationCalculees(), contains(expected));
    }

    @Test
    public void aDecompteSalaireWithTwoCotisationsDecomptesWhenGetCotisationsCalculeesShouldReturnTwoElements() {
        decompteSalaire.setSalaireTotal(new Montant(1000));
        decompteSalaire.setPeriode(new Periode(Annee.getCurrentYear().getFirstDayOfYear(), Annee.getCurrentYear()
                .getLastDayOfYear()));
        addCotisationDecompte("1", new Taux(2));
        addCotisationDecompte("2", new Taux(3));

        CotisationCalculee expected1 = createCotisationCalculee("1", new Montant(1000), new Taux(2));
        CotisationCalculee expected2 = createCotisationCalculee("2", new Montant(1000), new Taux(3));

        assertThat(decompteSalaire.getCotisationCalculees(), contains(expected1, expected2));
    }

    @Test
    public void getTauxContribuablesForAVS_GivenEmptyDecompte_ShouldBe0() {
        assertEquals(new Taux(0), decompteSalaire.getTauxContribuableForAVS());
    }

    @Test
    public void getTauxContribuablesForAVS_GivenDecompteWithTauxOf5_5_ShouldBe5_5() {
        CotisationDecompte cotisationDecompte = mock(CotisationDecompte.class);
        when(cotisationDecompte.getTypeAssurance()).thenReturn(TypeAssurance.COTISATION_AVS_AI);
        when(cotisationDecompte.getTaux()).thenReturn(new Taux(5.5));

        decompteSalaire.addCotisationDecompte(cotisationDecompte);

        assertEquals(new Taux(5.5), decompteSalaire.getTauxContribuableForAVS());
    }

    @Test
    public void getTauxContribuablesForAVS_GivenDecompteWith2TauxOf5_5And7_5_ShouldBe13() {
        CotisationDecompte cotisationDecompte = mock(CotisationDecompte.class);
        when(cotisationDecompte.getTypeAssurance()).thenReturn(TypeAssurance.ASSURANCE_CHOMAGE);
        when(cotisationDecompte.getTaux()).thenReturn(new Taux(5.5));

        CotisationDecompte cotisationDecompte2 = mock(CotisationDecompte.class);
        when(cotisationDecompte2.getTypeAssurance()).thenReturn(TypeAssurance.ASSURANCE_CHOMAGE);
        when(cotisationDecompte2.getTaux()).thenReturn(new Taux(7.5));

        decompteSalaire.addCotisationDecompte(cotisationDecompte);
        decompteSalaire.addCotisationDecompte(cotisationDecompte2);

        assertEquals(new Taux(13), decompteSalaire.getTauxContribuableForAC());
    }

    @Test
    public void getTauxContribuablesForAC_GivenEmptyDecompte_ShouldBe0() {
        assertEquals(new Taux(0), decompteSalaire.getTauxContribuableForAC());
    }

    @Test
    public void getTauxContribuablesForAC_GivenDecompteWithTauxOf5_5_ShouldBe5_5() {
        CotisationDecompte cotisationDecompte = mock(CotisationDecompte.class);
        when(cotisationDecompte.getTypeAssurance()).thenReturn(TypeAssurance.ASSURANCE_CHOMAGE);
        when(cotisationDecompte.getTaux()).thenReturn(new Taux(5.5));

        decompteSalaire.addCotisationDecompte(cotisationDecompte);

        assertEquals(new Taux(5.5), decompteSalaire.getTauxContribuableForAC());
    }

    @Test
    public void getTauxContribuablesForAC_GivenDecompteWith2TauxOf5_5And7_5_ShouldBe13() {
        CotisationDecompte cotisationDecompte = mock(CotisationDecompte.class);
        when(cotisationDecompte.getTypeAssurance()).thenReturn(TypeAssurance.ASSURANCE_CHOMAGE);
        when(cotisationDecompte.getTaux()).thenReturn(new Taux(5.5));

        CotisationDecompte cotisationDecompte2 = mock(CotisationDecompte.class);
        when(cotisationDecompte2.getTypeAssurance()).thenReturn(TypeAssurance.ASSURANCE_CHOMAGE);
        when(cotisationDecompte2.getTaux()).thenReturn(new Taux(7.5));

        decompteSalaire.addCotisationDecompte(cotisationDecompte);
        decompteSalaire.addCotisationDecompte(cotisationDecompte2);

        assertEquals(new Taux(13), decompteSalaire.getTauxContribuableForAC());
    }

    @Test
    public void calculSalaireTotalSiNessaire_GivenEmptyDecompteSalaire_ShouldNotChanged() {
        decompteSalaire.calculChampSalaire();

        assertEquals(0, decompteSalaire.getHeures(), 0);
        assertEquals(Montant.ZERO, decompteSalaire.getSalaireHoraire());
        assertEquals(Montant.ZERO, decompteSalaire.getSalaireTotal());
    }

    @Test
    public void calculSalaireTotalSiNecessaire_GivenDecompteSalaireWithHeuresOf3AndSalaireHoraireOf100_ShouldBeCalculated() {
        decompteSalaire.setHeures(3f);
        decompteSalaire.setSalaireHoraire(new Montant(100));
        decompteSalaire.calculChampSalaire();

        assertEquals(3f, decompteSalaire.getHeures(), 0);
        assertEquals(Montant.valueOf(100), decompteSalaire.getSalaireHoraire());
        assertEquals(Montant.valueOf(300), decompteSalaire.getSalaireTotal());
    }

    private void addCotisationDecompte(final String idCotisation, final Taux taux) {
        Cotisation cotisation = mock(Cotisation.class);
        when(cotisation.getAssuranceId()).thenReturn(idCotisation);

        CotisationDecompte cotisationDecompte = new CotisationDecompte();
        cotisationDecompte.setCotisation(cotisation);
        cotisationDecompte.setTaux(taux);
        decompteSalaire.addCotisationDecompte(cotisationDecompte);
    }

    private CotisationCalculee createCotisationCalculee(final String id, final Montant montant, final Taux taux) {
        Cotisation cotisation = mock(Cotisation.class);
        when(cotisation.getAssuranceId()).thenReturn(id);
        CotisationCalculee cotisationCalculee = new CotisationCalculee(cotisation, montant, taux,
                Annee.getCurrentYear());

        return cotisationCalculee;
    }

    private void addPosteTravail(final Employeur employeur) {
        PosteTravail poste = new PosteTravail();
        poste.setEmployeur(employeur);
        decompteSalaire.setPosteTravail(poste);
    }

    private void addTaux(final double taux) {
        CotisationDecompte cotisationDecompte = new CotisationDecompte();
        cotisationDecompte.setTaux(new Taux(taux));
        decompteSalaire.addCotisationDecompte(cotisationDecompte);
    }

    @Test
    public void isMemeAnneeDecompte_WithDecompteAndDecompteSalaireMemePeriode_ShouldBeTrue() {
        Periode periodeDecompteSalaire = new Periode("01.01.2014", "28.02.2014");
        PeriodeMensuelle periodeDecompte = new PeriodeMensuelle("01.2014", "01.2014");
        Decompte decompte = new Decompte();
        decompte.setPeriode(periodeDecompte);
        decompteSalaire.setPeriode(periodeDecompteSalaire);
        decompteSalaire.setDecompte(decompte);

        assertTrue(decompteSalaire.isMemeAnneeDecompte());
    }

    @Test
    public void isMemeAnneeDecompte_WithDecompteAndDecompteSalaireDifferentePeriode_ShouldBeFalse() {
        Periode periodeDecompteSalaire = new Periode("01.01.2014", "28.02.2014");
        PeriodeMensuelle periodeDecompte = new PeriodeMensuelle("01.2015", "01.2015");
        Decompte decompte = new Decompte();
        decompte.setPeriode(periodeDecompte);
        decompteSalaire.setPeriode(periodeDecompteSalaire);
        decompteSalaire.setDecompte(decompte);

        assertFalse(decompteSalaire.isMemeAnneeDecompte());
    }

    @Test
    public void equals_WithTwoEmptyDecompteSalaires_ShouldBeFalse() {
        boolean actual = decompteSalaire.equals(new DecompteSalaire());
        assertFalse(actual);
    }

    @Test
    public void equals_WithTwoEmptyDecompteSalaireWithSameId_ShouldBeTrue() {
        decompteSalaire.setId("1");
        DecompteSalaire other = new DecompteSalaire();
        other.setId("1");
        boolean actual = decompteSalaire.equals(other);
        assertTrue(actual);
    }

    @Test
    public void isFranchisee_GivenEmptyDecompte_ShouldBeFalse() {
        assertFalse(decompteSalaire.isFranchise());
    }

    @Test
    public void isFranchisee_GivenDecompteWithMontantFranchiseMaisNonForcee_ShouldBeFalse() {
        CotisationDecompte cotisation = mock(CotisationDecompte.class);
        when(cotisation.getMasseForcee()).thenReturn(true);
        when(cotisation.getTypeAssurance()).thenReturn(TypeAssurance.COTISATION_AVS_AI);
        when(cotisation.getMasse()).thenReturn(Montant.valueOf("12000"));
        decompteSalaire.setCotisationsDecompte(Arrays.asList(cotisation));
        decompteSalaire.setSalaireTotal(Montant.valueOf("12000"));
        decompteSalaire.setMontantFranchise(Montant.valueOf("1400"));
        assertFalse(decompteSalaire.isFranchise());
    }

    @Test
    public void isFranchisee_GivenDecompteWithMontantFranchiseForcee_ShouldBeTrue() {
        CotisationDecompte cotisation = mock(CotisationDecompte.class);
        when(cotisation.getMasseForcee()).thenReturn(true);
        when(cotisation.getTypeAssurance()).thenReturn(TypeAssurance.COTISATION_AVS_AI);
        when(cotisation.getMasse()).thenReturn(Montant.valueOf("12000"));
        decompteSalaire.setCotisationsDecompte(Arrays.asList(cotisation));
        decompteSalaire.setSalaireTotal(Montant.valueOf("13000"));
        decompteSalaire.setMontantFranchise(Montant.valueOf("1400"));
        assertTrue(decompteSalaire.isFranchise());
    }

    @Test
    public void isFranchisee_GivenDecompteWithMontantFranchiseForceeMaisPasAVS_ShouldBeFalse() {
        CotisationDecompte cotisation = mock(CotisationDecompte.class);
        when(cotisation.getMasseForcee()).thenReturn(true);
        when(cotisation.getTypeAssurance()).thenReturn(TypeAssurance.COTISATION_RETAVAL);
        when(cotisation.getMasse()).thenReturn(Montant.valueOf("12000"));
        decompteSalaire.setCotisationsDecompte(Arrays.asList(cotisation));
        decompteSalaire.setSalaireTotal(Montant.valueOf("13000"));
        decompteSalaire.setMontantFranchise(Montant.valueOf("1400"));
        assertFalse(decompteSalaire.isFranchise());
    }

    @Test
    public void isSameAnneeCotisations_GivenDecompteWithoutAnneeAndNull_ShouldBeTrue() {
        decompteSalaire.setAnneeCotisations(null);
        assertTrue(decompteSalaire.isSameAnneeCotisations(null));
}

    @Test
    public void isSameAnneeCotisations_GivenDecompte2015AndNull_ShouldBeFalse() {
        decompteSalaire.setAnneeCotisations(new Annee(2015));
        assertFalse(decompteSalaire.isSameAnneeCotisations(null));
    }

    @Test
    public void isSameAnneeCotisations_GivenDecompteWithoutAnneeAn2015_ShouldBeTrue() {
        decompteSalaire.setAnneeCotisations(null);
        assertFalse(decompteSalaire.isSameAnneeCotisations(new Annee(2015)));
    }

    @Test
    public void isSameAnneeCotisations_GivenDecompte2013An2015_ShouldBeTrue() {
        decompteSalaire.setAnneeCotisations(new Annee(2013));
        assertFalse(decompteSalaire.isSameAnneeCotisations(new Annee(2015)));
    }

    @Test
    public void isSameAnneeCotisations_GivenDecompte2015An2015_ShouldBeFalse() {
        decompteSalaire.setAnneeCotisations(new Annee(2015));
        assertTrue(decompteSalaire.isSameAnneeCotisations(new Annee(2015)));
    }
}
