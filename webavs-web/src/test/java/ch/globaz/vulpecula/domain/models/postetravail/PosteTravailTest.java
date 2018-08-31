/**
 * 
 */
package ch.globaz.vulpecula.domain.models.postetravail;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.models.registre.ConventionQualification;
import ch.globaz.vulpecula.domain.models.registre.Personnel;
import ch.globaz.vulpecula.domain.models.registre.TypeQualification;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;

/**
 * @author Arnaud Geiser (AGE) | Créé le 19 déc. 2013
 * 
 */
public class PosteTravailTest {
    private PosteTravail posteTravail;

    @Before
    public void setup() {
        posteTravail = new PosteTravail();
    }

    @Test
    public void givenPosteTravailWithoutPeriodWhenIsActifShouldBeTrue() {
        boolean actual = posteTravail.isActif();
        Assert.assertEquals(true, actual);
    }

    @Test
    public void givenPosteTravailActifWhenIsActifShouldBeTrue() {
        posteTravail.setPeriodeActivite(new Periode(new Date("01.01.2013"), null));
        boolean actual = posteTravail.isActif();
        Assert.assertEquals(true, actual);
    }

    @Test
    public void givenPosteTravailInactifWhenIsActifShouldBeFalse() {
        posteTravail.setPeriodeActivite(new Periode(new Date("01.01.2013"), new Date("01.06.2013")));
        boolean actual = posteTravail.isActif(new Date("10.10.2013"));
        Assert.assertEquals(false, actual);
    }

    @Test
    public void givenPosteTravailInactifWithDateFinWhenIsActifShouldBeTrue() {
        posteTravail.setPeriodeActivite(new Periode(new Date("01.01.2013"), new Date("11.10.2013")));
        boolean actual = posteTravail.isActif(new Date("10.10.2013"));
        Assert.assertEquals(true, actual);
    }

    @Test
    public void givenPosteTravailActifWithoutPeriodeActiviteWhenIsActifShouldBeTrue() {
        boolean actual = posteTravail.isActif(new Date("10.10.2013"));
        Assert.assertEquals(true, actual);
    }

    @Test
    public void givenPosteTravailActifUntilTodayWhenIsActifThenTrue() {
        posteTravail.setPeriodeActivite(new Periode(new Date("28.01.2014"), new Date("28.01.2014")));
        boolean actual = posteTravail.isActif(new Date("28.01.2014"));
        Assert.assertEquals(true, actual);
    }

    @Test
    public void isActif_GivenPeriodeNotInActivityWithEndDate_ShouldReturnFalse() {
        posteTravail.setPeriodeActivite(new Periode(new Date("01.01.2014"), new Date("01.01.2015")));
        Periode periodeDemande = new Periode(new Date("01.01.2013"), new Date("01.02.2013"));
        assertEquals(false, posteTravail.isActif(periodeDemande));
    }

    @Test
    public void isActif_GivenPeriodeInActivityWithEndDate_ShouldReturnTrue() {
        posteTravail.setPeriodeActivite(new Periode(new Date("01.01.2014"), new Date("01.01.2015")));
        Periode periodeDemande = new Periode(new Date("01.01.2013"), new Date("01.02.2014"));
        assertEquals(true, posteTravail.isActif(periodeDemande));
    }

    @Test
    public void isActif_GivenPeriodeNotInActivityWithoutEndDate_ShouldReturnFalse() {
        posteTravail.setPeriodeActivite(new Periode(new Date("01.01.2014"), null));
        Periode periodeDemande = new Periode(new Date("01.01.2013"), new Date("01.02.2013"));
        assertEquals(false, posteTravail.isActif(periodeDemande));
    }

    @Test
    public void isActif_GivenPeriodeInActivityWithoutEndDate_ShouldReturnTrue() {
        posteTravail.setPeriodeActivite(new Periode(new Date("01.01.2014"), null));
        Periode periodeDemande = new Periode(new Date("01.01.2013"), new Date("01.02.2014"));
        assertEquals(true, posteTravail.isActif(periodeDemande));
    }

    @Test
    public void givenPosteTravailWithTwoOccupationsWhenGetTauxActuel() {
        Occupation o1 = new Occupation();
        o1.setTaux(new Taux(10));
        o1.setDateValidite(new Date("01.01.1990"));
        Occupation o2 = new Occupation();
        o2.setTaux(new Taux(30));
        o2.setDateValidite(new Date("01.01.2013"));

        posteTravail.getOccupations().add(o1);
        posteTravail.getOccupations().add(o2);

        Assert.assertEquals("30.00", posteTravail.getOccupation(new Date("04.04.2013")).getTaux().getValue());
    }

    @Test
    public void givenPosteTravailWithThreeOccupationsWhenGetTauxActuel() {
        Occupation o1 = new Occupation();
        o1.setTaux(new Taux(10));
        o1.setDateValidite(new Date("01.01.1990"));
        Occupation o2 = new Occupation();
        o2.setTaux(new Taux(30));
        o2.setDateValidite(new Date("01.01.2013"));
        Occupation o3 = new Occupation();
        o3.setTaux(new Taux(70));
        o3.setDateValidite(new Date("01.01.2014"));

        posteTravail.getOccupations().add(o1);
        posteTravail.getOccupations().add(o2);
        posteTravail.getOccupations().add(o3);

        Assert.assertEquals("30.00", posteTravail.getOccupation(new Date("04.04.2013")).getTaux().getValue());
    }

    @Test
    public void givenPosteTravailWithOneOccupationAfterDateActuelWhenGetTauxActuelShouldReturnTaux() {
        Occupation o1 = new Occupation();
        o1.setTaux(new Taux(70));
        o1.setDateValidite(new Date("01.01.2014"));

        posteTravail.getOccupations().add(o1);

        Assert.assertEquals(new Taux(70), posteTravail.getOccupation(new Date("04.04.2013")).getTaux());
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void givenEmptyPosteWhenValidateThenReturnFalse() throws UnsatisfiedSpecificationException {
        posteTravail.validate();
    }

    @Test
    public void givenPosteWithEmployeurAndWithTravailleurWhenValidateThenNothing()
            throws UnsatisfiedSpecificationException {
        setMinRequiredDataForPoste();
        posteTravail.validate();
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void givenPosteWithEmployeurWithTravailleurAndTwoSameDateOccupationsWhenValidateThenThrowUnsatisfiedSpecitificationException()
            throws UnsatisfiedSpecificationException {
        setMinRequiredDataForPoste();

        Occupation o1, o2 = null;
        o1 = new Occupation();
        o1.setDateValidite(new Date("01.01.2014"));
        o1.setTaux(new Taux(100));

        o2 = new Occupation();
        o2.setDateValidite(new Date("01.01.2014"));
        o2.setTaux(new Taux(20));

        addTaux(o1, o2);

        posteTravail.validate();
    }

    @Test
    public void givenPosteWithEmployeurWithTravailleurAndOneOccupationWhenValidateThenNothing()
            throws UnsatisfiedSpecificationException {
        setMinRequiredDataForPoste();

        Occupation o1 = null;
        o1 = new Occupation();
        o1.setDateValidite(new Date("01.01.2014"));
        o1.setTaux(new Taux(100));

        addTaux(o1);

        posteTravail.validate();
    }

    @Test
    public void givenPosteWithoutTaux_ShouldBe() {
        Taux taux = posteTravail.getTauxOccupation(Date.now());
        assertEquals(new Taux(100), taux);
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void givenPosteWithEmployeurWithTravailleurWithoutDateDebutWhenValidateThenReturnFalse()
            throws UnsatisfiedSpecificationException {
        posteTravail.setEmployeur(new Employeur());
        posteTravail.setTravailleur(new Travailleur());

        posteTravail.validate();
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void givenPosteWithEmployeurWithTravailleurWithoutTypeSalaireWhenValidateThenReturnFalse()
            throws UnsatisfiedSpecificationException {
        posteTravail.setEmployeur(new Employeur());
        posteTravail.setTravailleur(new Travailleur());
        posteTravail.setPeriodeActivite(new Periode(new Date("01.01.2013"), null));
        posteTravail.validate();
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void givenPosteWithEmployeurWithTravailleurWithoutQualificationWhenValidateThenReturnFalse()
            throws UnsatisfiedSpecificationException {
        posteTravail.setEmployeur(new Employeur());
        posteTravail.setTravailleur(new Travailleur());
        posteTravail.setPeriodeActivite(new Periode(new Date("01.01.2013"), null));
        posteTravail.setTypeSalaire(TypeSalaire.HEURES);
        posteTravail.validate();
    }

    @Test
    public void givenValidPosteWithoutCotisationsWhenGetTauxContributionShouldReturn0() {
        setMinRequiredDataForPoste();

        Assert.assertEquals(new Taux(0), posteTravail.getTauxContribuable());
    }

    @Test
    public void givenValidPosteWithOneCotisationOfTaux15WhenGetTauxContributionShouldReturn15() {
        setMinRequiredDataForPoste();
        addTauxCotisation(15.0);

        Assert.assertEquals(new Taux(15.0), posteTravail.getTauxContribuable());
    }

    @Test
    public void givenValidPosteWithTowCotisationOfTaux15And1_12WhenGetTauxContributionShouldReturn16_2() {
        setMinRequiredDataForPoste();
        addTauxCotisation(15.0);
        addTauxCotisation(1.2);

        Assert.assertEquals(new Taux(16.2), posteTravail.getTauxContribuable());
    }

    @Test
    public void givenValidPosteWithTwoCotisationsOfTaux15And1_12Where1_12ISAVSWhenGetTauxContributionShouldReturn15() {
        setMinRequiredDataForPoste();
        addTauxCotisation(15.0);
        addTauxCotisation(1.2, TypeAssurance.COTISATION_AVS_AI);

        Assert.assertEquals(new Taux(15), posteTravail.getTauxContribuable());
    }

    @Test
    public void givenValidPosteWithFiveCotisationsOfTaux15And1_12AVSAnd12ACAnd3AC2And3_4WhenGetTauxContributionShouldReturn18_4() {
        setMinRequiredDataForPoste();
        addTauxCotisation(15.0);
        addTauxCotisation(1.2, TypeAssurance.COTISATION_AVS_AI);
        addTauxCotisation(12, TypeAssurance.ASSURANCE_CHOMAGE);
        addTauxCotisation(3, TypeAssurance.COTISATION_AC2);
        addTauxCotisation(3.4);

        Assert.assertEquals(new Taux(18.4), posteTravail.getTauxContribuable());
    }

    @Test
    public void givenNullWhenGetIdConventionThenReturnNull() {
        assertEquals(null, posteTravail.getIdConvention());
    }

    @Test
    public void givenPosteWithConventionWhenGetIdConventionThenReturnIdConvention() {
        setMinRequiredDataForPoste();
        Convention convention = new Convention();
        convention.setId("1");
        posteTravail.getEmployeur().setConvention(convention);

        assertEquals("1", posteTravail.getIdConvention());
    }

    private void addTauxCotisation(final double taux) {
        addTauxCotisation(taux, null);
    }

    private void addTauxCotisation(final double taux, final TypeAssurance typeAssurance) {
        Assurance assurance = new Assurance();
        assurance.setTypeAssurance(typeAssurance);

        Cotisation cotisation = new Cotisation();
        cotisation.setTaux(new Taux(taux));
        cotisation.setAssurance(assurance);

        AdhesionCotisationPosteTravail adhesionCotisationPosteTravail = new AdhesionCotisationPosteTravail();
        adhesionCotisationPosteTravail.setCotisation(cotisation);
        posteTravail.addAdhesionCotisation(adhesionCotisationPosteTravail);
    }

    private void setMinRequiredDataForPoste() {
        Employeur employeur = new Employeur();
        employeur.setId("1");
        employeur.setDateDebut("01.01.2013");

        Travailleur travailleur = new Travailleur();
        travailleur.setId("1");

        posteTravail.setEmployeur(employeur);
        posteTravail.setTravailleur(travailleur);
        posteTravail.setPeriodeActivite(new Periode(new Date("01.01.2013"), null));
        posteTravail.setTypeSalaire(TypeSalaire.HEURES);
        posteTravail.setQualification(Qualification.CHARPENTIER_QUALIFIE);
    }

    private void addTaux(final Occupation... occupations) {
        posteTravail.setOccupations(Arrays.asList(occupations));
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void givenPosteWithPeriodePosteBeforeDebutAffiliationEmployeurThenThrowUnsatisfiedSpecificationException()
            throws UnsatisfiedSpecificationException {
        setMinRequiredDataForPoste();

        posteTravail.getEmployeur().setDateDebut("01.01.2013");
        posteTravail.getEmployeur().setDateFin("31.12.2013");

        posteTravail.setPeriodeActivite(new Periode(new Date("01.01.2012"), new Date("01.06.2013")));

        posteTravail.validate();
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void givenPosteWithPeriodePosteAfterFinAffiliationEmployeurThenReturnFalse()
            throws UnsatisfiedSpecificationException {
        setMinRequiredDataForPoste();

        posteTravail.getEmployeur().setDateDebut("01.01.2013");
        posteTravail.getEmployeur().setDateFin("31.12.2013");

        posteTravail.setPeriodeActivite(new Periode(new Date("01.06.2013"), new Date("01.06.2014")));

        posteTravail.validate();
    }

    public void givenPosteWithPeriodePosteBetweenPeriodeAffiliationEmployeurThenReturnTrue()
            throws UnsatisfiedSpecificationException {
        setMinRequiredDataForPoste();

        posteTravail.getEmployeur().setDateDebut("01.01.2013");
        posteTravail.getEmployeur().setDateFin("31.12.2013");

        posteTravail.setPeriodeActivite(new Periode(new Date("01.01.2013"), new Date("31.12.2013")));

        posteTravail.validate();
    }

    @Test
    public void givenPosteWithPeriodePosteAfterDebutPeriodeAffiliationEmployeurThenReturnTrue()
            throws UnsatisfiedSpecificationException {
        setMinRequiredDataForPoste();

        posteTravail.getEmployeur().setDateDebut("01.01.2013");

        posteTravail.setPeriodeActivite(new Periode(new Date("01.02.2013"), new Date("31.12.2013")));

        posteTravail.validate();
    }

    @Test
    public void givenPosteWithDebutPeriodePosteAfterDebutPeriodeAffiliationEmployeurThenReturnTrue()
            throws UnsatisfiedSpecificationException {
        setMinRequiredDataForPoste();

        posteTravail.getEmployeur().setDateDebut("01.01.2013");

        posteTravail.setPeriodeActivite(new Periode(new Date("01.02.2013"), null));

        posteTravail.validate();
    }

    @Test(expected = IllegalStateException.class)
    public void isQualificationEmploye_WithEmptyPoste_ShouldThrowIllegalStateExcetion() {
        PosteTravail posteTravail = new PosteTravail();
        posteTravail.isQualificationEmploye();
    }

    @Test(expected = IllegalStateException.class)
    public void isQualificationEmploye_WithPosteWithQualificationNoConfiguration_ShouldThrowIllegalStateExcetion() {
        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setQualification(Qualification.APPRENTI);
        posteTravail.isQualificationEmploye();
    }

    @Test
    public void isQualificationEmploye_WithPosteWithQualificationAndConfigurationOuvrier_ShouldBeFalse() {
        PosteTravail posteTravail = new PosteTravail();
        Qualification qualification = Qualification.APPRENTI;
        posteTravail.setQualification(qualification);
        posteTravail.setParametresQualifications(Arrays.asList(create(TypeQualification.OUVRIER,
                Personnel.ADMINISTRATIF)));
        assertThat(posteTravail.isQualificationEmploye(), is(false));
    }

    @Test
    public void isQualificationEmploye_WithPosteWithQualificationAndConfigurationEmploye_ShouldBeTrue() {
        PosteTravail posteTravail = new PosteTravail();
        Qualification qualification = Qualification.APPRENTI;
        posteTravail.setQualification(qualification);
        posteTravail.setParametresQualifications(Arrays.asList(create(TypeQualification.EMPLOYE,
                Personnel.ADMINISTRATIF)));
        assertThat(posteTravail.isQualificationEmploye(), is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void isQualificationOuvrier_WithPosteWithQualificationNoConfiguration_ShouldThrowIllegalStateExcetion() {
        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setQualification(Qualification.APPRENTI);
        posteTravail.isQualificationOuvrier();
    }

    @Test
    public void isQualificationOuvrier_WithPosteWithQualificationAndConfigurationOuvrier_ShouldBeTrue() {
        PosteTravail posteTravail = new PosteTravail();
        Qualification qualification = Qualification.APPRENTI;
        posteTravail.setQualification(qualification);
        posteTravail.setParametresQualifications(Arrays.asList(create(TypeQualification.OUVRIER,
                Personnel.ADMINISTRATIF)));
        assertThat(posteTravail.isQualificationOuvrier(), is(true));
    }

    @Test
    public void isQualificationOuvrier_WithPosteWithQualificationAndConfigurationEmploye_ShouldBeFalse() {
        PosteTravail posteTravail = new PosteTravail();
        Qualification qualification = Qualification.APPRENTI;
        posteTravail.setQualification(qualification);
        posteTravail.setParametresQualifications(Arrays.asList(create(TypeQualification.EMPLOYE,
                Personnel.ADMINISTRATIF)));
        assertThat(posteTravail.isQualificationOuvrier(), is(false));
    }

    @Test
    public void isMensuel_GivenEmptyPosteTravail_ShouldReturnFalse() {
        assertFalse(posteTravail.isMensuel());
    }

    @Test
    public void isMensuel_GivenPosteTravailTypeSalaireMois_ShouldBeTrue() {
        posteTravail.setTypeSalaire(TypeSalaire.MOIS);
        assertTrue(posteTravail.isMensuel());
    }

    private ConventionQualification create(TypeQualification typeQualification, Personnel personnel) {
        ConventionQualification conventionQualification = new ConventionQualification();
        conventionQualification.setTypeQualification(typeQualification);
        conventionQualification.setPersonnel(personnel);
        return conventionQualification;
    }

    @Ignore
    public void hasDroitAJ_GivenCaisse10AndCotisationSMAJ_ShouldReturnTrue() throws JAXBException, URISyntaxException {
        setMinRequiredDataForPoste();
        addTauxCotisation(1.2, TypeAssurance.COTISATION_SM_AJ);

        assertTrue(posteTravail.hasDroitAJ(10));
    }

    @Ignore
    public void hasDroitAJ_GivenCaisse50AndCotisationSMAJ_ShouldReturnFalse() throws JAXBException, URISyntaxException {
        setMinRequiredDataForPoste();
        addTauxCotisation(1.2, TypeAssurance.COTISATION_SM_AJ);

        assertFalse(posteTravail.hasDroitAJ(50));
    }

    @Ignore
    public void hasDroitAJ_GivenCaisse50AndCotisationContributionGeneraleAndContributionGeneraleReduite_ShouldReturnTrue()
            throws JAXBException, URISyntaxException {
        setMinRequiredDataForPoste();
        addTauxCotisation(1.2, TypeAssurance.CONTRIBUTION_GENERALE);
        addTauxCotisation(1.2, TypeAssurance.CONTRIBUTION_GENERALE_REDUITE);

        assertTrue(posteTravail.hasDroitAJ(50));
    }

    @Ignore
    public void hasDroitSM_GivenCaisse10AndCotisationSMAJ_ShouldReturnTrue() throws JAXBException, URISyntaxException {
        setMinRequiredDataForPoste();
        addTauxCotisation(1.2, TypeAssurance.COTISATION_SM_AJ);

        assertTrue(posteTravail.hasDroitSM(10));
    }

    @Ignore
    public void hasDroitSM_GivenCaisse10AndCotisationCongePayes_ShouldReturnFalse() throws JAXBException,
            URISyntaxException {
        setMinRequiredDataForPoste();
        addTauxCotisation(1.2, TypeAssurance.CONGES_PAYES);

        assertFalse(posteTravail.hasDroitSM(10));
    }

    @Ignore
    public void hasDroitSM_GivenCaisse50AndCotisationCongePayes_ShouldReturnFalse() throws JAXBException,
            URISyntaxException {
        setMinRequiredDataForPoste();
        addTauxCotisation(1.2, TypeAssurance.CONGES_PAYES);

        assertFalse(posteTravail.hasDroitSM(50));
    }

    @Ignore
    public void hasDroitSM_GivenCaisse50AndCotisationContributionGenerale_ShouldReturnTrue() throws JAXBException,
            URISyntaxException {
        setMinRequiredDataForPoste();
        addTauxCotisation(1.2, TypeAssurance.CONTRIBUTION_GENERALE);

        assertTrue(posteTravail.hasDroitSM(50));
    }

    @Ignore
    public void hasDroitCP_GivenCaisse10AndCotisationSMAJ_ShouldReturnFalse() throws JAXBException, URISyntaxException {
        setMinRequiredDataForPoste();
        addTauxCotisation(1.2, TypeAssurance.COTISATION_SM_AJ);

        assertFalse(posteTravail.hasDroitCP(10));
    }

    @Ignore
    public void hasDroitCP_GivenCaisse10AndCotisationCongePayes_ShouldReturnTrue() throws JAXBException,
            URISyntaxException {
        setMinRequiredDataForPoste();
        addTauxCotisation(1.2, TypeAssurance.CONGES_PAYES);

        assertTrue(posteTravail.hasDroitCP(10));
    }

    @Ignore
    public void hasDroitAJ_GivenEmptyPosteTravail_ShouldBeFalse() {
        assertFalse(posteTravail.hasDroitAJ(10));
    }

    @Ignore
    public void hasDroitAJAt01012014_GivenPosteTravailWithAJBetween01012014And31012014_ShouldBeTrue() {
        addAdhesionCotisation(TypeAssurance.COTISATION_SM_AJ, new Date("01.01.2014"), new Date("31.12.2014"));
        assertTrue(posteTravail.hasDroitAJ(10, new Date("01.01.2014")));
    }

    @Ignore
    public void hasDroitAJAt01012014_GivenPosteTravailWithAJBetween02012014And31012014_ShouldBeFalse() {
        addAdhesionCotisation(TypeAssurance.COTISATION_SM_AJ, new Date("01.02.2014"), new Date("31.12.2014"));
        assertFalse(posteTravail.hasDroitAJ(10, new Date("01.01.2014")));
    }

    @Ignore
    public void hasDroitSM_GivenEmptyPosteTravail_ShouldBeFalse() {
        assertFalse(posteTravail.hasDroitSM(10));
    }

    @Ignore
    public void hasDroitSMAt01012014_GivenPosteTravailWithSMBetween01012014And31012014_ShouldBeTrue() {
        addAdhesionCotisation(TypeAssurance.COTISATION_SM_AJ, new Date("01.01.2014"), new Date("31.12.2014"));
        assertTrue(posteTravail.hasDroitSM(10, new Date("01.01.2014")));
    }

    @Ignore
    public void hasDroitSMAt01012014_GivenPosteTravailWithSMBetween02012014And31012014_ShouldBeFalse() {
        addAdhesionCotisation(TypeAssurance.COTISATION_SM_AJ, new Date("01.02.2014"), new Date("31.12.2014"));
        assertFalse(posteTravail.hasDroitSM(10, new Date("01.01.2014")));
    }

    @Ignore
    public void hasDroitCP_GivenEmptyPosteTravail_ShouldBeFalse() {
        assertFalse(posteTravail.hasDroitCP(10));
    }

    @Ignore
    public void hasDroitCPAt01012014_GivenPosteTravailWithAJBetween01012014And31012014_ShouldBeFalse() {
        addAdhesionCotisation(TypeAssurance.COTISATION_SM_AJ, new Date("01.01.2014"), new Date("31.12.2014"));
        assertFalse(posteTravail.hasDroitCP(10, new Date("01.01.2014")));
    }

    @Ignore
    public void hasDroitCPAt01012014_GivenPosteTravailWithAJBetween02012014And31012014_ShouldBeFalse() {
        addAdhesionCotisation(TypeAssurance.COTISATION_SM_AJ, new Date("01.02.2014"), new Date("31.12.2014"));
        assertFalse(posteTravail.hasDroitCP(10, new Date("01.01.2014")));
    }

    @Ignore
    public void hasDroitCPAt01012014_GivenPosteTravailWithCPBetween01012014And31012014_ShouldBeTrue() {
        addAdhesionCotisation(TypeAssurance.CONGES_PAYES, new Date("01.01.2014"), new Date("31.12.2014"));
        assertTrue(posteTravail.hasDroitCP(10, new Date("01.01.2014")));
    }

    private void addAdhesionCotisation(TypeAssurance typeAssurance, Date dateDebut, Date dateFin) {
        AdhesionCotisationPosteTravail adhesionCotisationPosteTravail = mock(AdhesionCotisationPosteTravail.class);
        when(adhesionCotisationPosteTravail.getTypeAssurance()).thenReturn(typeAssurance);
        when(adhesionCotisationPosteTravail.getPeriode()).thenReturn(new Periode(dateDebut, dateFin));
        posteTravail.addAdhesionCotisation(adhesionCotisationPosteTravail);
    }
}