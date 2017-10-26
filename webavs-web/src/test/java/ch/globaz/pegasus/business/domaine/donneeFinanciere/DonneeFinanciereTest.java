package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;

public class DonneeFinanciereTest {

    private static DonneeFinanciere dhWihtOutDateFin = newDonneeFinanciere(new Date("10.2015"), null);
    private static DonneeFinanciere dh = newDonneeFinanciere(new Date("10.2015"), new Date("10.2016"));

    @Test
    public void testIsInPeriodeWithBorne() throws Exception {
        assertThat(dh.isInPeriode(dh.getDebut(), dh.getFin())).isTrue();
        assertThat(dh.isInPeriode(dh.getDebut(), dh.getDebut())).isTrue();
        assertThat(dh.isInPeriode(dh.getFin(), dh.getFin())).isTrue();

        assertThat(dhWihtOutDateFin.isInPeriode(dh.getDebut(), dh.getFin())).isTrue();
        assertThat(dhWihtOutDateFin.isInPeriode(dh.getDebut(), dh.getDebut())).isTrue();
        assertThat(dhWihtOutDateFin.isInPeriode(dh.getFin(), dh.getFin())).isTrue();
        assertThat(dh.isInPeriode(new Date("08.10.2015"), new Date("08.10.2016"))).isTrue();
        assertThat(dh.isInPeriode(new Date("08.10.2016"), new Date("08.10.2016"))).isTrue();
        assertThat(dh.isInPeriode(new Date("08.10.2015"), new Date("08.10.2015"))).isTrue();
    }

    @Test(expected = RuntimeException.class)
    public void testIsInPeriodeDateDebutPeriodeBeforeDateFinBorne() throws Exception {
        dhWihtOutDateFin.isInPeriode(dh.getFin(), dh.getDebut());
    }

    @Test(expected = RuntimeException.class)
    public void testIsInPeriodeDateDebutPeriodeBeforeDateFin() throws Exception {
        dhWihtOutDateFin.isInPeriode(new Date("06.2016"), new Date("02.2016"));
    }

    @Test
    public void testIsInPeriodeWithDateDebutDhBeforInThePeriode() throws Exception {
        Date dateDebut = new Date("01.2016");
        assertThat(dh.isInPeriode(dateDebut, null)).isTrue();
        assertThat(dh.isInPeriode(dateDebut, dateDebut)).isTrue();
        assertThat(dh.isInPeriode(dateDebut, new Date("05.2016"))).isTrue();
        assertThat(dh.isInPeriode(dateDebut, dh.getFin())).isTrue();
        assertThat(dhWihtOutDateFin.isInPeriode(dateDebut, null)).isTrue();
        assertThat(dhWihtOutDateFin.isInPeriode(dateDebut, dateDebut)).isTrue();
        assertThat(dhWihtOutDateFin.isInPeriode(dateDebut, new Date("05.2016"))).isTrue();
        assertThat(dhWihtOutDateFin.isInPeriode(dateDebut, dh.getFin())).isTrue();
    }

    @Test
    public void testIsInPeriodeWithDateDebutDhBeforNotInThePeriode() throws Exception {
        Date dateDebut = new Date("01.2017");
        assertThat(dh.isInPeriode(dateDebut, null)).isFalse();
        assertThat(dh.isInPeriode(dateDebut, dateDebut)).isFalse();
        assertThat(dh.isInPeriode(dateDebut, new Date("05.2018"))).isFalse();
        assertThat(dhWihtOutDateFin.isInPeriode(dateDebut, null)).isTrue();
        assertThat(dhWihtOutDateFin.isInPeriode(dateDebut, dateDebut)).isTrue();
        assertThat(dhWihtOutDateFin.isInPeriode(dateDebut, new Date("05.2018"))).isTrue();
    }

    @Test
    public void testIsInPeriodeWithDateDhAfter() throws Exception {
        Date dateDebut = new Date("01.2013");
        assertThat(dh.isInPeriode(dateDebut, null)).isFalse();
        assertThat(dh.isInPeriode(dateDebut, dateDebut)).isFalse();
        assertThat(dh.isInPeriode(dateDebut, new Date("05.2013"))).isFalse();
        assertThat(dhWihtOutDateFin.isInPeriode(dateDebut, null)).isFalse();
        assertThat(dhWihtOutDateFin.isInPeriode(dateDebut, dateDebut)).isFalse();
        assertThat(dhWihtOutDateFin.isInPeriode(dateDebut, new Date("05.2013"))).isFalse();
    }

    @Test
    public void testCasImpossible() throws Exception {
        assertThat(dh.isInPeriode(new Date("01.2016"), new Date("01.2017"))).isFalse();
        assertThat(dh.isInPeriode(new Date("01.2016"), new Date("01.2017"))).isFalse();
        assertThat(dh.isInPeriode(new Date("01.2010"), new Date("01.2017"))).isFalse();
        assertThat(dh.isInPeriode(new Date("01.2010"), new Date("01.2016"))).isFalse();
    }

    private static DonneeFinanciere newDonneeFinanciere(Date debut, Date fin) {
        return new DonneeFinanciere(RoleMembreFamille.REQUERANT, debut, fin, "1", "2") {

            @Override
            protected void definedTypeDonneeFinanciere() {

            }
        };
    }

}
