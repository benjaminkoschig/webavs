package ch.globaz.al.liste.process;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class GenerateurAnneeRevenuMinimalTest {

    private GenerateurAnneeRevenuMinimal generateur = new GenerateurAnneeRevenuMinimal(2017);

    @Test
    public void testGenererStringAnneeRevenuMinimal2013() throws Exception {
        assertThat(generateur.genererStringAnneeRevenuMinimal("2013:1170")).isEqualTo(
                "(2013,7020),(2014,7020),(2015,7020),(2016,7020),(2017,7020)");
    }

    @Test
    public void testGenererStringAnneeRevenuMinimal2017() throws Exception {
        assertThat(generateur.genererStringAnneeRevenuMinimal("2017:1170")).isEqualTo("(2017,7020)");
    }

    @Test
    public void testGenererStringAnneeRevenuMinimal2018() throws Exception {
        assertThat(generateur.genererStringAnneeRevenuMinimal("2016:1000,2018:1170")).isEqualTo(
                "(2016,6000),(2017,6000),(2018,7020)");
    }

    @Test(expected = RuntimeException.class)
    public void testWithEmptyValue() throws Exception {
        assertThat(generateur.genererStringAnneeRevenuMinimal("")).isEqualTo("");
    }

    @Test(expected = RuntimeException.class)
    public void testWithnullValue() throws Exception {
        assertThat(generateur.genererStringAnneeRevenuMinimal(null)).isEqualTo("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongFormat() throws Exception {
        assertThat(generateur.genererStringAnneeRevenuMinimal("2017,1120")).isEqualTo("");
    }
}
