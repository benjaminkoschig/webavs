package ch.globaz.vulpecula.domain.specifications.congepaye;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public class CPPeriodeInActivitePosteTest {
    private CPPeriodeInActivitePoste cpPeriodeInActivitePoste;

    @Before
    public void setUp() {
        cpPeriodeInActivitePoste = new CPPeriodeInActivitePoste();
    }

    @Test
    public void isValid_GivenAout1972_WithYear1972And1972_ShouldBeTrue() {
        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setPeriodeActivite(new Periode("28.08.1972", null));
        CongePaye congePaye = new CongePaye();
        congePaye.setAnneeDebut(new Annee(1972));
        congePaye.setAnneeFin(new Annee(1972));
        congePaye.setPosteTravail(posteTravail);
        assertTrue(cpPeriodeInActivitePoste.isValid(congePaye));
    }

    @Test
    public void isValid_GivenAout1972AndDecembre1972_WithYear1972And1972_ShouldBeTrue() {
        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setPeriodeActivite(new Periode("28.08.1972", "31.12.1972"));
        CongePaye congePaye = new CongePaye();
        congePaye.setAnneeDebut(new Annee(1972));
        congePaye.setAnneeFin(new Annee(1972));
        congePaye.setPosteTravail(posteTravail);
        assertTrue(cpPeriodeInActivitePoste.isValid(congePaye));
    }

    @Test
    public void isValid_GivenAout1971AndDecembre1974_WithYear1972And1972_ShouldBeTrue() {
        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setPeriodeActivite(new Periode("28.08.1971", "31.12.1974"));
        CongePaye congePaye = new CongePaye();
        congePaye.setAnneeDebut(new Annee(1972));
        congePaye.setAnneeFin(new Annee(1972));
        congePaye.setPosteTravail(posteTravail);
        assertTrue(cpPeriodeInActivitePoste.isValid(congePaye));
    }

    @Test
    public void isValid_GivenAout1973AndDecembre1973_WithYear1972And1972_ShouldBeFalse() {
        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setPeriodeActivite(new Periode("28.08.1973", "31.12.1973"));
        CongePaye congePaye = new CongePaye();
        congePaye.setAnneeDebut(new Annee(1972));
        congePaye.setAnneeFin(new Annee(1972));
        congePaye.setPosteTravail(posteTravail);
        assertFalse(cpPeriodeInActivitePoste.isValid(congePaye));
    }
}
