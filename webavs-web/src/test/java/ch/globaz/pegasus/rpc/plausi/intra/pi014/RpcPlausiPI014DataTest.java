package ch.globaz.pegasus.rpc.plausi.intra.pi014;

import static org.assertj.core.api.Assertions.*;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import ch.globaz.common.domaine.Montant;

@RunWith(Parameterized.class)
public class RpcPlausiPI014DataTest {
    @Parameters(name = " FC19: {0}, FC27 : {1}, expected {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { 1010.00, 1005.50, false }, { 1010.00, 3000, true },
                { 1000.00, 1000, true }, { 0, 0, true }, { 10, 5, false } });
    }

    private final Montant FC19;
    private final Montant FC27;
    private final boolean expected;

    public RpcPlausiPI014DataTest(double FC19, double FC27, boolean expected) {
        this.FC19 = new Montant(FC19);
        this.FC27 = new Montant(FC27);
        this.expected = expected;
    }

    @Test
    public void test() {
        assertThat(perpare(FC19, FC27).isValide()).isEqualTo(expected);
    }

    private RpcPlausiPI014Data perpare(Montant FC19, Montant FC27) {
        RpcPlausiPI014Data plausi = new RpcPlausiPI014Data(null);
        plausi.FC19 = FC19;
        plausi.FC27 = FC27;
        return plausi;
    }

}
