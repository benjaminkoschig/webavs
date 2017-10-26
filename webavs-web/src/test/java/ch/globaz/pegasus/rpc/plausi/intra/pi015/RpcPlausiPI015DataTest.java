package ch.globaz.pegasus.rpc.plausi.intra.pi015;

import static org.fest.assertions.api.Assertions.*;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import ch.globaz.common.domaine.Montant;

@RunWith(Parameterized.class)
public class RpcPlausiPI015DataTest {

    @Parameters(name = " isDomicile: {0}, FC19 : {1}, expected {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { true, 0, false }, { true, 3000, true }, { false, 1000, true },
                { false, 0, true } });
    }

    private final boolean isDomicile;
    private final int FC19;
    private final boolean expected;

    public RpcPlausiPI015DataTest(boolean isDomicile, int FC19, boolean expected) {
        this.isDomicile = isDomicile;
        this.FC19 = FC19;
        this.expected = expected;
    }

    @Test
    public void test() {
        assertThat(perpare(isDomicile, FC19).isValide()).isEqualTo(expected);
    }

    private RpcPlausiPI015Data perpare(boolean isDomicile2, int fC192) {
        RpcPlausiPI015Data plausi = new RpcPlausiPI015Data(null);
        plausi.isDomicile = isDomicile2;
        plausi.FC19 = new Montant(FC19);
        return plausi;
    }

}
