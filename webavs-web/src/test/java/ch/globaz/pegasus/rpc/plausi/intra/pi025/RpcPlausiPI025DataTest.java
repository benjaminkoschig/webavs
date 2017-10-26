package ch.globaz.pegasus.rpc.plausi.intra.pi025;

import static org.fest.assertions.api.Assertions.*;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.intra.pi025.RpcPlausiPI025Data.ParSituationCouple;

@RunWith(Parameterized.class)
public class RpcPlausiPI025DataTest {
    @Parameters(name = " FC33: {0}, par : {1}, nbTotalEnfants : {2}, isDomicile {3}, , expected {4}")
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] { { 19290, ParSituationCouple.PAR1, 0, true, true },
                        { 28935, ParSituationCouple.PAR2, 0, true, true },
                        { 49095, ParSituationCouple.PAR2, 2, true, true },
                        { 55815, ParSituationCouple.PAR2, 3, true, true },
                        { 62535, ParSituationCouple.PAR2, 4, true, true },
                        { 65895, ParSituationCouple.PAR2, 5, true, true },
                        { 69255, ParSituationCouple.PAR2, 6, true, true },
                        { 62970, ParSituationCouple.PAR1, 7, true, true },
                        { 28935, ParSituationCouple.PAR3, 0, true, false } });
    }

    private final boolean isDomicile;
    private final ParSituationCouple par;
    private final int nbTotalEnfants;
    private final double FC33;

    private final boolean expected;

    public RpcPlausiPI025DataTest(double FC33, ParSituationCouple par, int nbTotalEnfants, boolean isDomicile,
            boolean expected) {
        this.FC33 = FC33;
        this.par = par;
        this.nbTotalEnfants = nbTotalEnfants;
        this.isDomicile = isDomicile;
        this.expected = expected;
    }

    @Test
    public void test() {
        assertThat(prepare(FC33, par, nbTotalEnfants, isDomicile).isValide()).isEqualTo(expected);
    }

    private RpcPlausiPI025Data prepare(double FC33, ParSituationCouple par, int nbTotalEnfants, boolean isDomicile) {
        RpcPlausiPI025Data plausi = new RpcPlausiPI025Data(null);
        plausi.FC33 = new Montant(FC33);
        plausi.par = par;
        plausi.nbTotalEnfants = nbTotalEnfants;
        plausi.isDomicile = isDomicile;

        plausi.par1 = new Montant(19290D);
        plausi.par2 = new Montant(28935D);
        plausi.par3 = new Montant(19290D);
        plausi.par4 = new Montant(10080D);
        plausi.par5 = new Montant(6720D);
        plausi.par6 = new Montant(3360D);
        return plausi;
    }
}