package ch.globaz.vulpecula.businessimpl.services.association;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.repositories.association.AssociationCotisationRepository;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class AssociationCotisationServiceImplTest {
    private AssociationCotisationServiceImpl associationCotisationServiceImpl;
    private AssociationCotisationRepository associationCotisationRepository;

    @Before
    public void setUp() {
        associationCotisationRepository = mock(AssociationCotisationRepository.class);
        associationCotisationServiceImpl = spy(new AssociationCotisationServiceImpl(associationCotisationRepository));
        doReturn("MOCK VALUE").when(associationCotisationServiceImpl).getCodeLibelle(anyString());
    }

    @Test
    public void create_GivenNoCotisation_ShouldBeValid() throws UnsatisfiedSpecificationException {
        associationCotisationServiceImpl.create("1", new ArrayList<AssociationCotisation>());
    }

    @Test
    public void create_GivenCotisationMembre_ShouldBeValid() throws UnsatisfiedSpecificationException {
        AssociationCotisation associationCotisation = createCotisation("1", "1",
                GenreCotisationAssociationProfessionnelle.MEMBRE, "01.01.2015", "31.01.2015", 100);

        associationCotisationServiceImpl.create("1", Arrays.asList(associationCotisation));
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void create_Given2CotisationsMembresSeChevauchant_ShouldThrowException()
            throws UnsatisfiedSpecificationException {
        AssociationCotisation ac = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.MEMBRE,
                "01.01.2015", "31.01.2015", 100);
        AssociationCotisation ac2 = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.MEMBRE,
                "05.01.2015", "31.01.2015", 100);

        associationCotisationServiceImpl.create("1", Arrays.asList(ac, ac2));
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void create_Given2CotisationsMembresDontUneSansFinSeChevauchant_ShouldThrowException()
            throws UnsatisfiedSpecificationException {
        AssociationCotisation ac = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.MEMBRE,
                "01.01.2015", null, 100);
        AssociationCotisation ac2 = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.MEMBRE,
                "05.01.2015", "31.01.2015", 100);

        associationCotisationServiceImpl.create("1", Arrays.asList(ac, ac2));
    }

    @Test
    public void create_Given2CotisationsMembresDontUneSansFin1_ShouldBeValid() throws UnsatisfiedSpecificationException {
        AssociationCotisation ac2 = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.MEMBRE,
                "01.01.2016", null, 100);
        AssociationCotisation ac = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.MEMBRE,
                "16.01.2015", "16.01.2015", 100);

        associationCotisationServiceImpl.create("1", Arrays.asList(ac, ac2));
    }

    @Test
    public void create_Given2CotisationsMembresDontUneSansFin2_ShouldBeValid() throws UnsatisfiedSpecificationException {
        AssociationCotisation ac = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.MEMBRE,
                "16.01.2015", "16.01.2015", 100);
        AssociationCotisation ac2 = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.MEMBRE,
                "01.01.2016", null, 100);

        associationCotisationServiceImpl.create("1", Arrays.asList(ac, ac2));
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void create_Given2CotisationsDifferentesNonMembresSeChevauchant_ShouldThrowException()
            throws UnsatisfiedSpecificationException {
        AssociationCotisation ac = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.NON_MEMBRE,
                "01.01.2015", "31.01.2015", 100);
        AssociationCotisation ac2 = createCotisation("2", "1", GenreCotisationAssociationProfessionnelle.NON_MEMBRE,
                "01.01.2015", "28.01.2015", 100);

        associationCotisationServiceImpl.create("1", Arrays.asList(ac, ac2));
    }

    @Test
    public void create_Given2CotisationsDifferentesNonMembres_ShouldBeOk() throws UnsatisfiedSpecificationException {
        AssociationCotisation ac = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.NON_MEMBRE,
                "01.01.2015", "31.01.2015", 100);
        AssociationCotisation ac2 = createCotisation("2", "2", GenreCotisationAssociationProfessionnelle.NON_MEMBRE,
                "01.01.2016", "28.01.2016", 100);

        associationCotisationServiceImpl.create("1", Arrays.asList(ac, ac2));
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void create_Given2CotisationsNonMembresDifferentes_ShouldThrowException()
            throws UnsatisfiedSpecificationException {
        AssociationCotisation ac = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.NON_MEMBRE,
                "01.01.2015", "31.01.2015", 100);
        AssociationCotisation ac2 = createCotisation("2", "1", GenreCotisationAssociationProfessionnelle.NON_MEMBRE,
                "05.01.2015", "28.01.2015", 100);

        associationCotisationServiceImpl.create("1", Arrays.asList(ac, ac2));
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void create_GivenCotisationWithMasseSalarialeOf50_ShouldThrowException()
            throws UnsatisfiedSpecificationException {
        AssociationCotisation ac = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.NON_MEMBRE,
                "01.01.2015", "31.01.2015", 50);

        associationCotisationServiceImpl.create("1", Arrays.asList(ac));
    }

    @Test
    public void create_GivenCotisationWithMasseSalarialeOf50And80_ShouldBeOk() throws UnsatisfiedSpecificationException {
        AssociationCotisation ac = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.MEMBRE,
                "01.01.2015", "31.01.2015", 50);
        AssociationCotisation ac2 = createCotisation("2", "1", GenreCotisationAssociationProfessionnelle.MEMBRE,
                "01.01.2015", "31.01.2015", 80);

        associationCotisationServiceImpl.create("1", Arrays.asList(ac, ac2));
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void create_GivenCotisationWithMasseSalarialeOf50And80NonChevauchantes_ShouldBeThrowException()
            throws UnsatisfiedSpecificationException {
        AssociationCotisation ac = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.MEMBRE,
                "01.01.2013", "01.01.2015", 50);
        AssociationCotisation ac2 = createCotisation("2", "1", GenreCotisationAssociationProfessionnelle.MEMBRE,
                "01.01.2014", "01.01.2014", 80);

        associationCotisationServiceImpl.create("1", Arrays.asList(ac, ac2));
    }

    @Test
    public void create_Given2CotisationsNonMembreSansDateFinDansDifferenteAssociation_ShouldThrowException()
            throws UnsatisfiedSpecificationException {
        AssociationCotisation ac = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.NON_MEMBRE,
                "01.01.2013", 100);
        AssociationCotisation ac2 = createCotisation("2", "2", GenreCotisationAssociationProfessionnelle.NON_MEMBRE,
                "01.01.2014", 100);

        associationCotisationServiceImpl.create("1", Arrays.asList(ac, ac2));
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void create_Given2CotisationsNonMembreSansDateFinDansMemeAssociation_ShouldThrowException()
            throws UnsatisfiedSpecificationException {
        AssociationCotisation ac = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.NON_MEMBRE,
                "01.01.2013", 100);
        AssociationCotisation ac2 = createCotisation("2", "1", GenreCotisationAssociationProfessionnelle.NON_MEMBRE,
                "01.01.2014", 100);

        associationCotisationServiceImpl.create("1", Arrays.asList(ac, ac2));
    }

    @Test
    public void create_Given2CotisationsNonMembreSansDateFinDansMemeAssociationInverses_ShouldBeOk()
            throws UnsatisfiedSpecificationException {
        AssociationCotisation ac = createCotisation("2", "1", GenreCotisationAssociationProfessionnelle.NON_MEMBRE,
                "26.01.2015", 100);
        AssociationCotisation ac2 = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.NON_MEMBRE,
                "01.01.2015", "25.01.2015", 100);

        associationCotisationServiceImpl.create("1", Arrays.asList(ac, ac2));
    }

    @Test
    public void create_Given2CotisationsNonTaxes_WithMasseSalaire0_ShouldBeOk()
            throws UnsatisfiedSpecificationException {
        AssociationCotisation ac = createCotisation("1", "1", GenreCotisationAssociationProfessionnelle.NON_TAXE,
                "01.01.2015", 0);
        associationCotisationServiceImpl.create("1", Arrays.asList(ac));
    }

    private AssociationCotisation createCotisation(String idCAP, String idAssociationProfessionnelle,
            GenreCotisationAssociationProfessionnelle genre, String dateDebut, String dateFin, double masseSalariale) {
        AssociationCotisation associationCotisation = new AssociationCotisation();
        Administration associationProfessionnelle = new Administration();
        associationProfessionnelle.setId(idAssociationProfessionnelle);
        associationProfessionnelle.setDesignation1("stub");
        CotisationAssociationProfessionnelle cotisationAssociationProfessionnelle = new CotisationAssociationProfessionnelle();
        cotisationAssociationProfessionnelle.setAssociationProfessionnelle(associationProfessionnelle);
        cotisationAssociationProfessionnelle.setId(idCAP);
        associationCotisation.setGenre(genre);
        associationCotisation.setPeriode(new Periode(dateDebut, dateFin));
        associationCotisation.setCotisationAssociationProfessionnelle(cotisationAssociationProfessionnelle);
        associationCotisation.setMasseSalariale(new Taux(masseSalariale));
        return associationCotisation;
    }

    private AssociationCotisation createCotisation(String id, String associationId,
            GenreCotisationAssociationProfessionnelle genre, String dateDebut, double masseSalariale) {
        return createCotisation(id, associationId, genre, dateDebut, null, masseSalariale);
    }
}
