package ch.globaz.vulpecula.domain.specifications.registre;

import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.syndicat.ParametreSyndicat;

public class PSPeriodeValideTest {
    private PSPeriodeValide periodeValide;
    private ParametreSyndicat parametreSyndicat;

    @Before
    public void setUp() {
        periodeValide = new PSPeriodeValide();
        parametreSyndicat = new ParametreSyndicat();
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void isSatisfiedBy_GivenEmptyParametreSyndicat_ShouldThrowUnsatifiedSpecificationException()
            throws UnsatisfiedSpecificationException {
        periodeValide.isSatisfiedBy(parametreSyndicat);
    }

    @Test
    public void isSatisfiedBy_GivenParametreSyndicatSansDateFin_ShouldBeValid()
            throws UnsatisfiedSpecificationException {
        parametreSyndicat.setDateDebut(new Date("01.01.2014"));
        periodeValide.isSatisfiedBy(parametreSyndicat);
    }

    @Test
    public void isSatisfiedBy_GivenParametreSyndicatWithDateDebutAndDateFin_ShouldBeValid()
            throws UnsatisfiedSpecificationException {
        parametreSyndicat.setDateDebut(new Date("01.01.2014"));
        parametreSyndicat.setDateFin(new Date("31.01.2014"));
        periodeValide.isSatisfiedBy(parametreSyndicat);
    }
}
