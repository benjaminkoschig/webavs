package ch.globaz.vulpecula.domain.specifications.decompte;

import org.junit.Before;
import org.junit.Test;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;

public class DecompteSalairePeriodeMemeAnneeTest {
    private DecompteSalairePeriodeMemeAnnee decompteSalairePeriodeMemeAnnee;
    private DecompteSalaire decompteSalaire;

    @Before
    public void setUp() {
        decompteSalaire = new DecompteSalaire();
        decompteSalairePeriodeMemeAnnee = new DecompteSalairePeriodeMemeAnnee();
    }

    @Test(expected = UnsatisfiedSpecificationException.class)
    public void isSatisfiedBy_DecompteSalaireAnneeDifferente_ShouldThrowException()
            throws UnsatisfiedSpecificationException {
        decompteSalaire.setPeriode(new Periode("01.01.2013", "31.12.2014"));
        decompteSalairePeriodeMemeAnnee.isSatisfiedBy(decompteSalaire);
    }

    @Test
    public void isSatisfiedBy_DecompteSalaireMemeAnnee_ShouldBeOk() throws UnsatisfiedSpecificationException {
        decompteSalaire.setPeriode(new Periode("01.01.2013", "31.12.2013"));
        decompteSalairePeriodeMemeAnnee.isSatisfiedBy(decompteSalaire);
    }
}
