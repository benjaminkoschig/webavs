package ch.globaz.pegasus.rpc.plausi.intra.pi006;

import static org.assertj.core.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RpcPlausiPI006DataTest {
    @Parameters(name = " P11: {0}, P6 : {1}, P12 : {2}, expected {3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { 2300, 2350, false, true }, { 2300, 2350, true, false },
                { 2300, 2300, true, true } });
    }

    private final int P11;
    private final int P6;
    private final boolean P12;
    private final boolean expected;

    public RpcPlausiPI006DataTest(int P11, int P6, boolean P12, boolean expected) {
        this.P11 = P11;
        this.P6 = P6;
        this.P12 = P12;
        this.expected = expected;
    }

    @Test
    public void test() {
        assertThat(prepare(P11, P6, P12).isValide()).isEqualTo(expected);
    }

    private RpcPlausiPI006Data prepare(int P11, int P6, boolean P12) {
        RpcPlausiPI006Data plausi = new RpcPlausiPI006Data(null);
        plausi.personsData = new ArrayList<RpcPlausiPI006AddressContainer>();
        RpcPlausiPI006AddressContainer address = new RpcPlausiPI006AddressContainer();
        address.P11 = P11;
        address.P6 = P6;
        address.P12 = P12;
        plausi.personsData.add(address);
        return plausi;
    }
}
